/**
 * Iota.java v0.1  21 November 2014 1:49:09 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
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
import java.util.List;
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
import org.ikankechil.iota.strategies.Strategy;
import org.ikankechil.synchronous.TaskHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Indicator and signal generator.
 *
 * @author Daniel Kuan
 * @version 0.1
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

  private final OHLCVReader     ohlcvReader;
  private final IndicatorWriter indicatorWriter;
  private final SignalWriter    signalWriter;

  final File                    indicatorDirectory;
  private final ExecutorService threadPool;

  // File-related constants
  private static final char     UNDERSCORE      = '_';
  private static final String   CSV             = ".csv";
  private static final String   INDICATORS      = "Indicators";
  private static final String   SIGNALS         = "Signals";
  private static final String   OHLCV_REGEX     = "regex:[A-Z0-9]+(_[dwm])?" + CSV;

  // Multi-threading constants
  private static final int      PROCESSORS      = Runtime.getRuntime().availableProcessors();
  private static final int      LOAD_MULTIPLIER = 5; // TODO need different load multipliers for apps

  private static final int      ZERO            = 0;

  static final Logger           logger          = LoggerFactory.getLogger(Iota.class);

  public Iota(final File indicatorDirectory) {
    if (!indicatorDirectory.isDirectory()) {
      throw new IllegalArgumentException("Not a directory: " + indicatorDirectory);
    }
    this.indicatorDirectory = indicatorDirectory;

    ohlcvReader = new OHLCVReader();
    indicatorWriter = new IndicatorWriter();
    signalWriter = new SignalWriter();

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

  class GenerateIndicators extends IotaTaskHelper<File> {

    final Collection<? extends Indicator> indicators;

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
  List<TimeSeries> generateIndicators(final Path prices,
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
  File writeIndicators(final Path prices, final List<? extends TimeSeries> values)
      throws IOException {
    // write to file
    final File exchange = new File(indicatorDirectory,
                                   prices.getParent().getFileName().toString());
    final File destination = new File(exchange,
                                      filename(prices, INDICATORS));
    indicatorWriter.write(values, destination);
    return destination;
  }

  public void generateSignals(final File prices,
                              final Collection<? extends Strategy> strategies)
      throws IOException {
    generateSignals(prices, strategies, Integer.MAX_VALUE);
  }

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

    final IotaFileVisitor<List<SignalTimeSeries>> visitor =
        new IotaFileVisitor<>(OHLCV_REGEX,
                              new GenerateSignals(strategies, lookback),
                              threadPool);
    Files.walkFileTree(prices.toPath(), visitor);

    logger.info("Generated signals for: {}", prices);
  }

  class GenerateSignals extends IotaTaskHelper<List<SignalTimeSeries>> {

    final Collection<? extends Strategy> strategies;
    final int                            lookback;

    public GenerateSignals(final Collection<? extends Strategy> strategies, final int lookback) {
      this.strategies = strategies;
      this.lookback = lookback;
    }

    @Override
    public Callable<List<SignalTimeSeries>> newTask(final Path prices) {
      return new Callable<List<SignalTimeSeries>>() {
        @Override
        public List<SignalTimeSeries> call() throws Exception {
          final List<SignalTimeSeries> tradingSignals = generateSignals(prices, strategies, lookback);
          return tradingSignals;
        }
      };
    }

  }

  List<SignalTimeSeries> generateSignals(final Path prices,
                                         final Collection<? extends Strategy> strategies,
                                         final int lookback)
      throws IOException {
    final List<SignalTimeSeries> tradingSignals = new ArrayList<>(strategies.size());
    // read prices from file
    final OHLCVTimeSeries ohlcv = ohlcvReader.read(prices.toFile());

    // execute strategies
    for (final Strategy strategy : strategies) {
      tradingSignals.add(strategy.execute(ohlcv, lookback));
      logger.info("Strategy {} executed for: {}", strategy, prices);
    }

    return tradingSignals;
  }

  File writeSignals(final Path prices, final List<? extends SignalTimeSeries> tradingSignals)
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

  class IotaFileVisitor<V> extends CompletionServiceFileVisitor<V> {

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

  abstract class IotaTaskHelper<V> implements TaskHelper<Path, V> {

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
    threadPool.awaitTermination(Short.MAX_VALUE, TimeUnit.MILLISECONDS);
    logger.info("Shutdown requested");
  }

}
