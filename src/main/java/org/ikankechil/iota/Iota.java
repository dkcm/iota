/**
 * Iota.java  v0.2  21 November 2014 1:49:09 PM
 *
 * Copyright © 2014-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.ikankechil.io.CompletionServiceFileVisitor;
import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.io.IndicatorWriter;
import org.ikankechil.iota.io.OHLCVReader;
import org.ikankechil.iota.io.SignalWriter;
import org.ikankechil.iota.io.SignalsSummaryWriter;
import org.ikankechil.iota.strategies.Strategy;
import org.ikankechil.synchronous.TaskHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Indicator and signal generator.
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class Iota {
  // TODO
  // 1. [COMPLETED] how to create folders
  // 2. [Not supported] spin-off 1 task for each file-write?
  // 3. [In progress] Update indicator files
  // 4. [COMPLETED] Generate trade signals
  // 5. [In progress] Write faster
  //    Time taken to write for signal and indicator generation:
  //    ~50% and up to 90% of execution time respectively (~1ms / file)
  // 6. [In progress] Consolidate indicators into one file to save space from
  //    redundant dates (but how should the reader segregate?)

  private final OHLCVReader          ohlcvReader;
  private final IndicatorWriter      indicatorWriter;
  private final SignalWriter         signalWriter;
  private final SignalsSummaryWriter signalsSummaryWriter;

  final File                         indicatorDirectory;
  private final ExecutorService      threadPool;

  // File-related constants
  private static final char          UNDERSCORE      = '_';
  private static final String        CSV             = ".csv";
  private static final String        INDICATORS      = "Indicators";
  private static final String        SIGNALS         = "Signals";
  private static final String        OHLCV_REGEX     = "regex:[A-Z0-9]+(_[dwm])?" + CSV;

  // Multi-threading constants
  private static final int           PROCESSORS      = Runtime.getRuntime().availableProcessors();
  private static final int           LOAD_MULTIPLIER = 5; // TODO need different load multipliers for apps

  private static final int           ZERO            = 0;

  static final Logger                logger          = LoggerFactory.getLogger(Iota.class);

  public Iota(final File indicatorDirectory) {
    if (!indicatorDirectory.isDirectory()) {
      throw new IllegalArgumentException("Not a directory: " + indicatorDirectory);
    }
    this.indicatorDirectory = indicatorDirectory;

    ohlcvReader = new OHLCVReader();
    indicatorWriter = new IndicatorWriter();
    signalWriter = new SignalWriter();
    signalsSummaryWriter = new SignalsSummaryWriter();

    threadPool = Executors.newFixedThreadPool(PROCESSORS * LOAD_MULTIPLIER);
  }

  /**
   * Generates indicator values from <code>prices</code> and writes them to
   * files.
   *
   * @param prices
   * @param indicators
   * @return <code>indicatorDirectory</code>
   * @throws IOException
   *           if an I/O error occurs when walking the file tree
   */
  public File generateIndicators(final File prices,
                                 final Collection<? extends Indicator> indicators)
      throws IOException {
    if (indicators.isEmpty()) {
      throw new IllegalArgumentException("No indicators to generate for: " + prices);
    }
    logger.info("Sourcing prices from: {}", prices);
    logger.info("Generating indicator(s) in: {}", indicatorDirectory);

    final IotaFileVisitor<?> visitor = new IotaFileVisitor<>(OHLCV_REGEX,
                                                             new GenerateIndicators(indicators),
                                                             threadPool);
    Files.walkFileTree(prices.toPath(), visitor);

    logger.info("Generated indicator(s) in: {}", indicatorDirectory);
    return indicatorDirectory;
  }

  private class GenerateIndicators extends IotaTaskHelper<File> {

    private final Collection<? extends Indicator> indicators;

    GenerateIndicators(final Collection<? extends Indicator> indicators) {
      this.indicators = indicators;
    }

    @Override
    public Callable<File> newTask(final Path prices) {
      return new Callable<File>() {
        @Override
        public File call() throws Exception {
          final List<TimeSeries> indicatorValues = generateIndicators(prices, indicators);
          return writeIndicators(prices, indicatorValues); // write to file
        }
      };
    }

  }

  /**
   * Generates indicator values.
   *
   * @param prices a file containing price and volume
   * @param indicators a <code>List</code> of indicators
   * @return
   * @throws IOException
   */
  private final List<TimeSeries> generateIndicators(final Path prices,
                                                    final Collection<? extends Indicator> indicators)
      throws IOException {
    final List<TimeSeries> indicatorValues = new ArrayList<>(indicators.size() << 1);
    // read prices from file
    final OHLCVTimeSeries ohlcv = ohlcvReader.read(prices.toFile());

    // sort indicators in alphabetical order
    final List<Indicator> sortedIndicators = new ArrayList<>(indicators);
    Collections.sort(sortedIndicators);

    // generate indicator(s)
    for (final Indicator indicator : sortedIndicators) {
      indicatorValues.addAll(indicator.generate(ohlcv));
      logger.info("Indicator {} generated for: {}", indicator, prices);
    }

    return indicatorValues;
  }

  /**
   * Writes indicator values
   *
   * @param prices
   * @param values
   * @return
   * @throws IOException
   */
  private final File writeIndicators(final Path prices, final List<? extends TimeSeries> values)
      throws IOException {
    // write to file
    final File exchange = new File(indicatorDirectory,
                                   prices.getParent().getFileName().toString());
    final File destination = new File(exchange,
                                      filename(prices, INDICATORS));
    indicatorWriter.write(values, destination);
    return destination;
  }

  /**
   * Generates trading signals from <code>prices</code>.
   *
   * @param prices a file or directory with OHLCV
   * @param strategies trading strategies
   * @throws IOException
   */
  public void generateSignals(final File prices,
                              final Collection<? extends Strategy> strategies)
      throws IOException {
    generateSignals(prices, strategies, Integer.MAX_VALUE);
  }

  /**
   * Generates trading signals from <code>prices</code>.
   *
   * @param prices a file or directory with OHLCV
   * @param strategies trading strategies
   * @param lookback
   * @throws IOException
   */
  public void generateSignals(final File prices,
                              final Collection<? extends Strategy> strategies,
                              final int lookback)
      throws IOException {
    if (strategies.isEmpty()) {
      throw new IllegalArgumentException("No strategies to execute on: " + prices);
    }
    if (lookback < ZERO) {
      throw new IllegalArgumentException("Negative lookback: " + lookback);
    }
    logger.info("Generating signals from: {}", prices);

    final GenerateSignals generateSignalsTaskHelper = new GenerateSignals(strategies, lookback);
    final IotaFileVisitor<Map<Strategy, SignalTimeSeries>> visitor =
        new IotaFileVisitor<>(OHLCV_REGEX,
                              generateSignalsTaskHelper,
                              threadPool);
    Files.walkFileTree(prices.toPath(), visitor);

    // compile results by strategy
//    final Map<Strategy, List<SignalTimeSeries>> results = transform(visitor.results());
    writeSignalsSummary(generateSignalsTaskHelper.signalsSummaries());

    logger.info("Generated signals for: {}", prices);
  }

  private final Map<Strategy, File> writeSignalsSummary(final Map<Strategy, List<SignalTimeSeries>> signalsSummaries) {
    final Map<Strategy, File> files = new LinkedHashMap<>(signalsSummaries.size());

    for (final Entry<Strategy, List<SignalTimeSeries>> signalsSummary : signalsSummaries.entrySet()) {
      final Strategy strategy = signalsSummary.getKey();
      final File destination = new File(indicatorDirectory,
                                        strategy.toString() + UNDERSCORE + SIGNALS + CSV);
      files.put(strategy, destination);

      try {
        signalsSummaryWriter.write(signalsSummary.getValue(), destination);
      }
      catch (final IOException ioE) {
        logger.warn("Could not write signals summary: {}", destination, ioE);
      }
    }

    return files;
  }

  private static final <K, V> Map<K, List<V>> transform(final Collection<Map<K, V>> maps) {
    final Map<K, List<V>> lists = new LinkedHashMap<>();
    for (final Map<K, V> map : maps) {
      for (final Entry<K, V> entry : map.entrySet()) {
        List<V> list = lists.get(entry.getKey());
        if (list == null) {
          lists.put(entry.getKey(), list = new ArrayList<>());
        }
        list.add(entry.getValue());
      }
    }
    return lists;
  }

  private class GenerateSignals extends IotaTaskHelper<Map<Strategy, SignalTimeSeries>> {

    private final Collection<? extends Strategy>        strategies;
    private final int                                   lookback;
    private final Map<Strategy, List<SignalTimeSeries>> signalsSummaries;

    public GenerateSignals(final Collection<? extends Strategy> strategies, final int lookback) {
      this.strategies = strategies;
      this.lookback = lookback;

      signalsSummaries = new LinkedHashMap<>(strategies.size());
      for (final Strategy strategy : strategies) {
        signalsSummaries.put(strategy, new ArrayList<>());
      }
    }

    @Override
    public Callable<Map<Strategy, SignalTimeSeries>> newTask(final Path prices) {
      return new Callable<Map<Strategy, SignalTimeSeries>>() {
        @Override
        public Map<Strategy, SignalTimeSeries> call() throws Exception {
          // generate trading signals as time series, one for each strategy
          final Map<Strategy, SignalTimeSeries> tradingSignals = generateSignals(prices, strategies, lookback);
//          writeSignals(prices, tradingSignals.values()); // write to file
          storeSignals(prices, tradingSignals);
          return tradingSignals;
        }
      };
    }

    private final void storeSignals(final Path prices,
                                    final Map<Strategy, SignalTimeSeries> tradingSignals) {
      for (final Entry<Strategy, SignalTimeSeries> strategy : tradingSignals.entrySet()) {
        final int elements = prices.getNameCount();
        final String fileName = prices.subpath(elements - 2, elements).toString();
        final SignalTimeSeries condensedSignals =
            SignalTimeSeries.condenseSignals(strategy.getValue(),
                                             fileName.substring(ZERO, fileName.lastIndexOf(CSV)));
        signalsSummaries.get(strategy.getKey())
                        .add(condensedSignals);
      }
    }

    public Map<Strategy, List<SignalTimeSeries>> signalsSummaries() {
      return Collections.unmodifiableMap(signalsSummaries);
    }

  }

  /**
   * Generates trading signals for each strategy.
   *
   * @param prices a file with OHLCV
   * @param strategies trading strategies
   * @param lookback
   * @return trading signals as time series, one for each strategy
   * @throws IOException
   */
  private final Map<Strategy, SignalTimeSeries> generateSignals(final Path prices,
                                                                final Collection<? extends Strategy> strategies,
                                                                final int lookback)
      throws IOException {
    final Map<Strategy, SignalTimeSeries> tradingSignals = new LinkedHashMap<>(strategies.size());
    // read prices from file
    final OHLCVTimeSeries ohlcv = ohlcvReader.read(prices.toFile());

    // execute strategies
    for (final Strategy strategy : strategies) {
      tradingSignals.put(strategy, strategy.execute(ohlcv, lookback));
      logger.info("Executed strategy {} for: {}", strategy, prices);
    }

    return tradingSignals;
  }

  private final File writeSignals(final Path prices, final Collection<? extends SignalTimeSeries> tradingSignals)
      throws IOException {
    // write all strategies' results to one file
    final File exchange = new File(indicatorDirectory,
                                   prices.getParent().getFileName().toString());
    final File destination = new File(exchange,
                                      filename(prices, SIGNALS));
    signalWriter.write(tradingSignals, destination);
    return destination;
  }

  private static final String filename(final Path prices, final String suffix) {
    // AA.csv / AA_m.csv -> AA_MACD.csv / AA_MACD_m.csv
    final String name = prices.getFileName().toString();
    final int i = name.indexOf(UNDERSCORE);
    final StringBuilder filename = new StringBuilder(name);
    filename.insert((i >= ZERO) ? i : filename.indexOf(CSV),
                    UNDERSCORE + suffix);
    return filename.toString();
  }

  private class IotaFileVisitor<V> extends CompletionServiceFileVisitor<V> {

    public IotaFileVisitor(final String syntaxAndPattern,
                           final TaskHelper<Path, V> taskHelper,
                           final Executor executor) {
      super(syntaxAndPattern,
            taskHelper,
            executor);
    }

    @Override
    public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs)
        throws IOException {
      super.preVisitDirectory(dir, attrs);

      // create subdirectory under indicatorDirectory if it does not exist
      if (!Files.isSameFile(startDirectory(), dir)) {
        final File exchange = new File(indicatorDirectory, dir.getFileName().toString());
        if (!exchange.exists()) {
          exchange.mkdir();
          logger.debug("Created folder: {}", exchange);
        }
      }

      return FileVisitResult.CONTINUE;
    }

  }

  private abstract class IotaTaskHelper<V> implements TaskHelper<Path, V> {

    @Override
    public V handleExecutionFailure(final ExecutionException eE, final Path operand) {
      return null;
    }

    @Override
    public V handleTaskCancellation(final CancellationException cE, final Path operand) {
      return null;
    }

    @Override
    public V handleTimeout(final TimeoutException tE, final Path operand) {
      return null;
    }

  }

  public void stop() throws InterruptedException {
    threadPool.shutdown();
    logger.info("Shutdown requested");
    threadPool.awaitTermination(Short.MAX_VALUE, TimeUnit.MILLISECONDS);
  }

}
