/**
 * FXRateCalculator.java  v0.4  11 March 2015 1:08:25 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.utils;

import static org.ikankechil.iota.utils.FXRateCalculator.Operator.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.io.OHLCVReader;
import org.ikankechil.synchronous.TaskExecutor;
import org.ikankechil.synchronous.TaskHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple foreign exchange rate calculator that can compute currency cross
 * rates.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.4
 */
public class FXRateCalculator {
  // TODO v0.5 cater to frequencies in new filename format
  // auto-create intermediate cross rates if required

  private final File                         priceDirectory;
  private final List<String>                 commonCurrencies;
  private final Map<String, OHLCVTimeSeries> cachedOHLCVs;
  private final OHLCVReader                  ohlcvReader;

  private final TaskExecutor                 executor;

  // Currency constants
  private static final List<String>          MAJORS               = Arrays.asList("USD", "EUR", "CHF", "GBP", "JPY");
  private static final int                   ISO4217_LENGTH       = 3;
  private static final Pattern               ISO4217_REGEX        = Pattern.compile("\\p{Alpha}{3}"); // ISO 4217: exactly 3 alphabetic characters

  // File-related constants
  private static final String                CSV                  = ".csv";

  // Log messages
  private static final String                UNAVAILABLE          = "Unavailable: {}";
  private static final String                NO_COMMON_FOUND      = "No source with common {} found for quote: {}";
  private static final String                COMPUTED_FX_RATES    = "Computed FX rates for: {} (size: {})";
  private static final String                EMPTY_FX_RATES       = "Empty FX rates";
  private static final String                CURRENCY_PRIORITISED = "Preferred common currency prioritised: {}";

  private static final Logger                logger               = LoggerFactory.getLogger(FXRateCalculator.class);

  public FXRateCalculator(final File priceDirectory) {
    this(priceDirectory, null);
  }

  public FXRateCalculator(final File priceDirectory, final String preferredCommonCurrency) {
    if (!priceDirectory.isDirectory()) {
      throw new IllegalArgumentException("Not a directory: " + priceDirectory);
    }
    this.priceDirectory = priceDirectory;

    if (isInappropriateCurrency(preferredCommonCurrency)) {
      logger.debug("No appropriate preferred common currency: {}", preferredCommonCurrency);
      commonCurrencies = MAJORS;
    }
    else {
      final String preferred = preferredCommonCurrency.toUpperCase(Locale.ROOT);
      if (MAJORS.contains(preferred)) {
        if (MAJORS.get(0).equals(preferred)) {
          commonCurrencies = MAJORS;
          logger.debug("Preferred common currency already prioritised: {}", preferred);
        }
        else {
          // copy and re-order
          commonCurrencies = newList(MAJORS.size());
          commonCurrencies.addAll(MAJORS);
          commonCurrencies.remove(preferred);
          commonCurrencies.add(0, preferred);
          logger.debug(CURRENCY_PRIORITISED, preferred);
        }
      }
      else {
        commonCurrencies = newList(MAJORS.size() + 1);
        commonCurrencies.add(preferred);
        commonCurrencies.addAll(MAJORS);
        logger.debug(CURRENCY_PRIORITISED, preferred);
      }
    }

    cachedOHLCVs = new HashMap<>();
    ohlcvReader = new OHLCVReader();
    executor = new TaskExecutor(Executors.newCachedThreadPool());
  }

  public List<OHLCVTimeSeries> invertFXRates(final Collection<OHLCVTimeSeries> fxRates) throws InterruptedException {
    if (fxRates.size() < 1) {
      throw new IllegalArgumentException(EMPTY_FX_RATES);
    }

    final Map<OHLCVTimeSeries, OHLCVTimeSeries> inverseFXRates =
        executor.executeAll(fxRates, new FXRCTaskHelper<OHLCVTimeSeries>() {
      @Override
      public Callable<OHLCVTimeSeries> newTask(final OHLCVTimeSeries fxRate) {
        return new Callable<OHLCVTimeSeries>() {
          @Override
          public OHLCVTimeSeries call() throws Exception {
            return invertFXRates(fxRate);
          }
        };
      }
    });

    return new ArrayList<>(inverseFXRates.values());
  }

  public OHLCVTimeSeries invertFXRates(final OHLCVTimeSeries fxRates) throws IOException {
    if (fxRates.size() < 1) {
      throw new IllegalArgumentException(EMPTY_FX_RATES);
    }
    // swap base with quote
    final String pair = fxRates.toString().toUpperCase(Locale.ROOT);
    final String invertedPair = pair.substring(ISO4217_LENGTH, ISO4217_LENGTH << 1) + pair.substring(0, ISO4217_LENGTH);
    logger.info("Inverting FX rates: {} -> {}", pair, invertedPair);

    // retrieve / read instead of compute if existing
    final OHLCVTimeSeries inverseFXRates;
    final File invertedPairSource = new File(priceDirectory, invertedPair + CSV);
    if (invertedPairSource.exists()) {
      inverseFXRates = read(invertedPairSource);
      logger.info("Retrieved existing FX rates for: {} (size: {})", invertedPair, inverseFXRates.size());
    }
    else {
      inverseFXRates = new OHLCVTimeSeries(invertedPair, fxRates.size());
      for (int i = 0; i < inverseFXRates.size(); ++i) {
        final String date = fxRates.date(i);
        final double open = 1 / fxRates.open(i);
        final double high = 1 / fxRates.high(i);
        final double low = 1 / fxRates.low(i);
        final double close = 1 / fxRates.close(i);
        final long volume = fxRates.volume(i);

        inverseFXRates.set(date, open, high, low, close, volume, i);
      }
      logger.info(COMPUTED_FX_RATES, invertedPair, inverseFXRates.size());
    }

    return inverseFXRates;
  }

  public List<OHLCVTimeSeries> computeFXRates(final String baseCurrency, final Collection<String> quoteCurrencies)
      throws InterruptedException {
    throwExceptionIfInappropriateCurrency(baseCurrency);
    throwExceptionIfInappropriateCurrency(quoteCurrencies.toArray(new String[quoteCurrencies.size()]));

    // convert base and quote currencies to upper case
    final String base = baseCurrency.toUpperCase(Locale.ROOT);
    final List<String> quotes = toUpperCase(quoteCurrencies);

    if (quotes.remove(base)) {
      logger.info("Removed base ({}) from quotes", base);
      if (quotes.isEmpty()) {
        throwExceptionSameBaseAndQuote(base);
      }
    }

    cache(base);

    final Map<String, OHLCVTimeSeries> fxRates = executor.executeAll(quotes, new FXRCTaskHelper<String>() {
      @Override
      public Callable<OHLCVTimeSeries> newTask(final String quote) {
        return new Callable<OHLCVTimeSeries>() {
          @Override
          public OHLCVTimeSeries call() throws Exception {
            return computeFXRates(base, quote);
          }
        };
      }
    });

    return new ArrayList<>(fxRates.values());
  }

  public List<OHLCVTimeSeries> computeFXRates(final Collection<String> baseCurrencies, final String quoteCurrency)
      throws InterruptedException {
    throwExceptionIfInappropriateCurrency(baseCurrencies.toArray(new String[baseCurrencies.size()]));
    throwExceptionIfInappropriateCurrency(quoteCurrency);

    // convert base and quote currencies to upper case
    final List<String> bases = toUpperCase(baseCurrencies);
    final String quote = quoteCurrency.toUpperCase(Locale.ROOT);

    if (bases.remove(quote)) {
      logger.info("Removed quote ({}) from bases", quote);
      if (bases.isEmpty()) {
        throwExceptionSameBaseAndQuote(quote);
      }
    }

    cache(quote);

    final Map<String, OHLCVTimeSeries> fxRates = executor.executeAll(bases, new FXRCTaskHelper<String>() {
      @Override
      public Callable<OHLCVTimeSeries> newTask(final String base) {
        return new Callable<OHLCVTimeSeries>() {
          @Override
          public OHLCVTimeSeries call() throws Exception {
            return computeFXRates(base, quote);
          }
        };
      }
    });

    return new ArrayList<>(fxRates.values());
  }

  public OHLCVTimeSeries computeFXRates(final String baseCurrency, final String quoteCurrency)
      throws IOException {
    // Algorithm
    // 1. Specify base and quote currencies in desired currency pair as ISO
    //    currency codes (ISO 4217), e.g. THBSGD - base: THB, quote: SGD
    // 2. Specify source directory
    // 3. Find currency pairs with common Majors
    // 4. Read OHLCVs
    // 5. Compute currency cross rates

    throwExceptionIfInappropriateCurrency(baseCurrency, quoteCurrency);

    // convert base and quote currencies to upper case
    final String base = baseCurrency.toUpperCase(Locale.ROOT);
    final String quote = quoteCurrency.toUpperCase(Locale.ROOT);
    final String pair = base + quote;

    if (base.equals(quote)) {
      throwExceptionSameBaseAndQuote(base);
    }
    else if (new File(priceDirectory, pair + CSV).exists()) {
      logger.info("FX rates already exist for: {}", pair);
    }

    logger.info("Computing FX rates for: {}", pair);

    // find sources
    // 1. common is "base" of both base and quote -> computation: quote / base
    //    e.g. SGDTHB = USDTHB / USDSGD
    // 2. common is "base" of base and "quote" of quote -> computation: 1 / (base * quote)
    //    e.g. CHFGBP = 1 / (USDCHF * GBPUSD)
    // 3. common is "quote" of base and "base" of quote -> computation: base * quote
    //    e.g. GBPSGD = GBPUSD * USDSGD
    // 4. common is "quote" of both base and quote -> computation: base / quote
    //    e.g. NZDAUD = NZDUSD / AUDUSD
    OHLCVTimeSeries fxRates = null;
    for (final String common : commonCurrencies) {
      logger.debug("Finding appropriate sources with common currency: {}", common);

      File baseSource = new File(priceDirectory, common + base + CSV);
      File quoteSource = new File(priceDirectory, common + quote + CSV);
      if (baseSource.exists()) {
        if (quoteSource.exists()) {
          // computation: quote / base
          logger.debug("Common is \"base\" of both base and quote -> computation: quote / base ({} / {})",
                       quote,
                       base);
          fxRates = computeFXRates(pair, quoteSource, baseSource, DIVIDE);
          break;
        }

        quoteSource = new File(priceDirectory, quote + common + CSV);
        if (quoteSource.exists()) {
          // computation: 1 / (base * quote)
          logger.debug("Common is \"base\" of base and \"quote\" of quote -> computation: 1 / (base * quote) (1 / ({} * {}))",
                       base,
                       quote);
          fxRates = computeFXRates(pair, baseSource, quoteSource, INVERSE_MULTIPLE);
          break;
        }

        logger.debug(NO_COMMON_FOUND, common, quote);
      }
      else {
        baseSource = new File(priceDirectory, base + common + CSV);
        if (baseSource.exists()) {
          if (quoteSource.exists()) {
            // computation: base * quote
            logger.debug("Common is \"quote\" of base and \"base\" of quote -> computation: base * quote ({} * {})",
                         base,
                         quote);
            fxRates = computeFXRates(pair, baseSource, quoteSource, MULTIPLY);
            break;
          }

          quoteSource = new File(priceDirectory, quote + common + CSV);
          if (quoteSource.exists()) {
            // computation: base / quote
            logger.debug("Common is \"quote\" of both base and quote -> computation: base / quote ({} / {})",
                         base,
                         quote);
            fxRates = computeFXRates(pair, baseSource, quoteSource, DIVIDE);
            break;
          }

          logger.debug(NO_COMMON_FOUND, common, quote);
        }
      }
    }

    if (fxRates == null) {
      fxRates = new OHLCVTimeSeries(pair, 0);
      logger.warn("No appropriate sources found, so FX rates not computed for: {}", pair);
    }

    return fxRates;
  }

  enum Operator {
    DIVIDE {
      @Override
      double operate(final double left, final double right) {
        return left / right;
      }
    },
    MULTIPLY {
      @Override
      double operate(final double left, final double right) {
        return left * right;
      }
    },
    INVERSE_MULTIPLE {
      @Override
      double operate(final double left, final double right) {
        return DIVIDE.operate(1, MULTIPLY.operate(left, right));
      }
    };

    abstract double operate(final double left, final double right);

  }

  private final OHLCVTimeSeries computeFXRates(final String pair,
                                               final File baseSource,
                                               final File quoteSource,
                                               final Operator operator)
      throws IOException {
    logger.debug("Found appropriate sources: {} and {}",
                 baseSource,
                 quoteSource);

    // read sources
    final OHLCVTimeSeries base = read(baseSource);
    final OHLCVTimeSeries quote = read(quoteSource);

    return computeFXRates(pair, base, quote, operator);
  }

  private final void cache(final String currency) {
    for (final String common : commonCurrencies) {
      try {
        read(new File(priceDirectory, common + currency + CSV));
        continue;
      }
      catch (final IOException ioE) {
        logger.info(UNAVAILABLE, common + currency);
      }
      try {
        read(new File(priceDirectory, currency + common + CSV));
      }
      catch (final IOException ioE) {
        logger.info(UNAVAILABLE, currency + common);
      }
    }
  }

  private final OHLCVTimeSeries read(final File source) throws IOException {
    final String name = source.getName();
    // check cache first
    OHLCVTimeSeries ohlcv = cachedOHLCVs.get(name);
    if (ohlcv == null) {
      synchronized (cachedOHLCVs) {
        cachedOHLCVs.put(name, ohlcv = ohlcvReader.read(source));
        logger.info("New time series read: {}", ohlcv);
      }
    }
    return ohlcv;
  }

  private static final OHLCVTimeSeries computeFXRates(final String pair,
                                                      final OHLCVTimeSeries base,
                                                      final OHLCVTimeSeries quote,
                                                      final Operator operator) {
    // assume different sizes
    final int baseSize = base.size();
    final int quoteSize = quote.size();
    final int size;
    final String[] dates;
    if (baseSize <= quoteSize) {
      size = baseSize;
      dates = base.dates();
    }
    else {
      size = quoteSize;
      dates = quote.dates();
    }

    // compute
    final OHLCVTimeSeries fxRates = new OHLCVTimeSeries(pair, size);
    for (int i = 0, b = baseSize - size, q = quoteSize - size;
         i < fxRates.size();
         ++i, ++b, ++q) {
      final double open = operator.operate(base.open(b), quote.open(q));
      final double high = operator.operate(base.high(b), quote.high(q));
      final double low = operator.operate(base.low(b), quote.low(q));
      final double close = operator.operate(base.close(b), quote.close(q));

      fxRates.set(dates[i], open, high, low, close, 0, i);
    }

    logger.info(COMPUTED_FX_RATES, pair, size);
    return fxRates;

    // e.g.
    // base: EURTHB,20150119,37.679,37.932,37.563,37.823
    // quote: EURSGD,20150119,1.5333,1.5519,1.5319,1.5462
    // THBSGD,20150119,0.04069375514212160619973990817166
    // common major EUR: 20150119,0.040693755142121604,0.04091268585890541,0.04078215265021431,0.040879887898897495
    // common major USD: 20150119,0.040692321856011786,0.040885823754789274,0.04076113709081959,0.04087980857721333
  }

  private static final void throwExceptionSameBaseAndQuote(final String currency) {
    throw new IllegalArgumentException("Base and quote currencies are the same: " + currency);
  }

  private static final void throwExceptionIfInappropriateCurrency(final String... currencies) {
    if (currencies.length < 1) {
      throw new IllegalArgumentException("No currencies specified");
    }
    for (final String currency : currencies) {
      if (isInappropriateCurrency(currency)) {
        throw new IllegalArgumentException("Not ISO 4217 currency code: " + currency);
      }
    }
  }

  private static final boolean isInappropriateCurrency(final String currency) {
    return (currency == null) ||
           currency.isEmpty() ||
           (currency.length() != ISO4217_LENGTH) ||
           !ISO4217_REGEX.matcher(currency).matches();
  }

  private static final List<String> toUpperCase(final Collection<String> strings) {
    final List<String> upperCases = newList(strings.size());
    for (final String string : strings) {
      upperCases.add(string.toUpperCase(Locale.ROOT));
    }
    return upperCases;
  }

  private static final <E> List<E> newList(final int initialCapacity) {
    return new ArrayList<>(initialCapacity);
  }

  private static abstract class FXRCTaskHelper<K> implements TaskHelper<K, OHLCVTimeSeries> {

    private static final OHLCVTimeSeries EMPTY_OHLCV = new OHLCVTimeSeries("", 0);

    @Override
    public OHLCVTimeSeries handleExecutionFailure(final ExecutionException eE, final K operand) {
      return EMPTY_OHLCV;
    }

    @Override
    public OHLCVTimeSeries handleTaskCancellation(final CancellationException cE, final K operand) {
      return EMPTY_OHLCV;
    }

    @Override
    public OHLCVTimeSeries handleTimeout(final TimeoutException tE, final K operand) {
      return EMPTY_OHLCV;
    }

  }

}
