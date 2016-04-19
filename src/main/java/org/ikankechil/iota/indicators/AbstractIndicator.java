/**
 * AbstractIndicator.java	v0.7	27 November 2014 1:04:00 am
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Abstract superclass for all <code>Indicator</code>s.
 *
 * @author Daniel Kuan
 * @version 0.7
 */
public abstract class AbstractIndicator implements Indicator {
  // TODO Enhancements
  // 1. support updates
  // 2. max(): ignore NaN?
  // 3. merge max() / min()
  // 4. apply Template pattern to generate()
  // 5. remove dependency on TA-Lib
  // 6. add indicator parameters to name

  protected final int           period;
  protected final int           lookback;
  protected final String        name;

  protected static final Core   TA_LIB          = new Core();

  // Numeric constants
  protected static final int    ZERO            = 0;
  protected static final int    ONE             = 1;
  protected static final int    TWO             = 2;
  protected static final int    THREE           = 3;
  protected static final int    FOUR            = 4;
  protected static final int    FIVE            = 5;
  protected static final int    SIX             = 6;
  protected static final int    SEVEN           = 7;
  protected static final int    EIGHT           = 8;
  protected static final int    NINE            = 9;
  protected static final int    TEN             = 10;
  protected static final int    ELEVEN          = 11;
  protected static final int    TWELVE          = 12;
  protected static final int    THIRTEEN        = 13;
  protected static final int    FOURTEEN        = 14;

  protected static final double HALF            = 0.5;
  protected static final double THIRD           = ONE / (double) THREE;
  protected static final double QUARTER         = 0.25;
  protected static final double SIXTH           = ONE / (double) SIX;
  protected static final double FIFTY_PERCENT   = 50.0;
  protected static final double HUNDRED_PERCENT = 100.0;

  protected static final char   SPACE           = ' ';
//  protected static final char   LEFT_BRACKET    = '(';
//  protected static final char   RIGHT_BRACKET   = ')';
  protected static final String GENERATED_FOR   = "{} generated for: {}";

  protected static final Logger logger          = LoggerFactory.getLogger(AbstractIndicator.class);

  public AbstractIndicator(final int lookback) {
    this(ZERO, lookback);
  }

  public AbstractIndicator(final int period, final int lookback) {
    throwExceptionIfNegative(period, lookback);

//    this.name = (name == null || name.trim().isEmpty()) ?
//                getClass().getSimpleName() + LEFT_BRACKET + period + RIGHT_BRACKET :
//                name;
    this.period = period;
    this.lookback = lookback;
    name = getClass().getSimpleName();

    logger.debug("{} lookback: {}", name, lookback);
  }

  @Override
  public int lookback() {
    return lookback;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    return generate(ohlcv, ZERO);
  }

  /**
   * Generate indicator values from prices and volumes.
   *
   * @param ohlcv
   * @param start
   * @return
   */
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();

    final MInteger outBegIdx = new MInteger();
    final MInteger outNBElement = new MInteger();

    final double[] indicator = new double[size - lookback - start];

    final RetCode outcome = compute(start,
                                    size - ONE,
                                    ohlcv,
                                    outBegIdx,
                                    outNBElement,
                                    indicator);
    throwExceptionIfBad(outcome, ohlcv);

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(name, dates, indicator));
  }

  /**
   * @param start
   * @param end
   * @param ohlcv
   * @param outBegIdx
   * @param outNBElement
   * @param output
   * @return
   */
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // default behaviour
    return compute(start,
                   end,
                   ohlcv.closes(),
                   outBegIdx,
                   outNBElement,
                   output);
  }

  @Override
  public List<TimeSeries> generate(final TimeSeries series) {
    return generate(series, ZERO);
  }

  /**
   * Generate indicator values from prices only.
   *
   * @param series
   * @param start
   * @return
   */
  public List<TimeSeries> generate(final TimeSeries series, final int start) {
    throwExceptionIfShort(series);
    final int size = series.size();

    final MInteger outBegIdx = new MInteger();
    final MInteger outNBElement = new MInteger();

    final double[] indicator = new double[size - lookback - start];

    final RetCode outcome = compute(start, // TODO change this to support updates
                                    size - ONE,
                                    series.values(),
                                    outBegIdx,
                                    outNBElement,
                                    indicator);
    throwExceptionIfBad(outcome, series);

    final String[] dates = Arrays.copyOfRange(series.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, series);
    return Arrays.asList(new TimeSeries(name, dates, indicator));
  }

  /**
   * @param start
   * @param end
   * @param values
   * @param outBegIdx
   * @param outNBElement
   * @param output
   * @return
   */
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // default behaviour
    return RetCode.InternalError;
  }

  protected final void throwExceptionIfShort(final TimeSeries series) {
    if (series.size() < lookback) {
      throw new IllegalArgumentException(String.format("Series too short: %s size (%d) < %s lookback (%d)",
                                                       series,
                                                       series.size(),
                                                       name,
                                                       lookback));
    }
  }

  protected static final void throwExceptionIfBad(final RetCode outcome, final TimeSeries series) {
    if (outcome != RetCode.Success) {
      throw new RuntimeException(String.format("%s: %s", series, outcome));
    }
  }

  protected static final void throwExceptionIfNegative(final Number... parameters) {
    for (int i = ZERO; i < parameters.length; ++i) {
      if (parameters[i].doubleValue() < 0d) {
        throw new IllegalArgumentException(String.format("Negative parameter: index = %d, value = %d",
                                                         i,
                                                         parameters[i]));
      }
    }
  }

  protected static final double[] toDoubles(final long... longs) {
    final double[] doubles = new double[longs.length];
    for (int i = ZERO; i < longs.length; ++i) {
      doubles[i] = longs[i];
    }
    return doubles;
  }

  protected static final double[] toDoubles(final int... ints) {
    final double[] doubles = new double[ints.length];
    for (int i = ZERO; i < ints.length; ++i) {
      doubles[i] = ints[i];
    }
    return doubles;
  }

  protected static final double[] smaRoc(final OHLCVTimeSeries ohlcv, final int sma, final int roc) {
    final double[] closes = ohlcv.closes();

    final MInteger outBegIdx = new MInteger();
    final MInteger outNBElement = new MInteger();

    // compute ROC
    final double[] outROC = new double[closes.length - TA_LIB.rocLookback(roc)];

    RetCode outcome = TA_LIB.roc(ZERO,
                                 closes.length - ONE,
                                 closes,
                                 roc,
                                 outBegIdx,
                                 outNBElement,
                                 outROC);
    throwExceptionIfBad(outcome, ohlcv);

    // compute SMA of ROC
    final double[] smaRoc = new double[outROC.length - TA_LIB.smaLookback(sma)];

    outcome = TA_LIB.sma(ZERO,
                         outROC.length - ONE,
                         outROC,
                         sma,
                         outBegIdx,
                         outNBElement,
                         smaRoc);
    throwExceptionIfBad(outcome, ohlcv);

    return smaRoc;
  }

  protected static final double[] sma(final double[] input, final int sma) {
    final double[] ma = new double[input.length - TA_LIB.smaLookback(sma)];

    final MInteger outBegIdx = new MInteger();
    final MInteger outNBElement = new MInteger();

    final RetCode outcome = TA_LIB.sma(ZERO,
                                       input.length - ONE,
                                       input,
                                       sma,
                                       outBegIdx,
                                       outNBElement,
                                       ma);
    throwExceptionIfBad(outcome, null);

    return ma;
  }

  protected static final double[] ema(final double[] input, final int ema) {
    final double[] ma = new double[input.length - TA_LIB.emaLookback(ema)];

    final MInteger outBegIdx = new MInteger();
    final MInteger outNBElement = new MInteger();

    final RetCode outcome = TA_LIB.ema(ZERO,
                                       input.length - ONE,
                                       input,
                                       ema,
                                       outBegIdx,
                                       outNBElement,
                                       ma);
    throwExceptionIfBad(outcome, null);

    return ma;
  }

  protected static final double max(final double... doubles) {
    double max = doubles[ZERO];
    for (int i = ONE; i < doubles.length; ++i) {
      final double d = doubles[i];
      if (d > max) {
        max = d;
      }
    }
    return max;
  }

  protected static final double max(final double[] doubles, final int from, final int to) {
    int i = from;
    double max = doubles[i];
    for (; ++i < to; ) {
      final double d = doubles[i];
      if (d > max) {
        max = d;
      }
    }
    return max;
  }

  protected static final double min(final double... doubles) {
    double min = Double.POSITIVE_INFINITY;
    for (final double d : doubles) {
      if (d < min) {
        min = d;
      }
    }
    return min;
  }

  protected static final double min(final double[] doubles, final int from, final int to) {
    int i = from;
    double min = doubles[i];
    for (; ++i < to; ) {
      final double d = doubles[i];
      if (d < min) {
        min = d;
      }
    }
    return min;
  }

  protected static final double sum(final double... doubles) {
    double sum = ZERO;
    for (final double d : doubles) {
      sum += d;
    }
    return sum;
  }

  protected static final double[] sum(final int period, final double... doubles) { // TODO support multiple arrays?
    final double[] sums = new double[doubles.length - period + ONE];
    int d = ZERO;
    double sum = doubles[d];
    for (; ++d < period; ) {
      sum += doubles[d];
    }
    int i = ZERO;
    sums[i] = sum;
    double first = doubles[i];
    for (; ++i < sums.length; ++d) {
      sums[i] = sum += (doubles[d] - first);
      first = doubles[i];
    }
    return sums;
  }

  protected static final double[] square(final double... doubles) {
    final double[] doublesSquared = Arrays.copyOf(doubles, doubles.length);
    for (int i = ZERO; i < doublesSquared.length; ++i) {
      doublesSquared[i] *= doubles[i];
    }
    return doublesSquared;
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public int compareTo(final Indicator o) {
    return name.compareTo(o.toString());
  }

}
