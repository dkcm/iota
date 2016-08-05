/**
 * Stochastic.java  v0.1  4 December 2014 2:05:03 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Stochastic Oscillator by J. Welles Wilder
 * <p>
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:stochastic_oscillator_fast_slow_and_full
 *
 * @author Daniel Kuan
 * @version
 */
public class Stochastic extends AbstractIndicator {

  private final int           fastK;
  private final int           slowK;
  private final int           slowD;

  private static final String K = "Stochastic %K";
  private static final String D = "Stochastic %D";

  public Stochastic() {
    this(FOURTEEN, THREE, THREE);
  }

  /**
   * @param fastK period
   * @param slowK period
   * @param slowD period
   */
  public Stochastic(final int fastK, final int slowK, final int slowD) {
    super(TA_LIB.stochLookback(fastK,
                               slowK,
                               MAType.Sma,
                               slowD,
                               MAType.Sma));
    throwExceptionIfNegative(fastK, slowK, slowD);

    this.fastK = fastK;
    this.slowK = slowK;
    this.slowD = slowD;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    // Formula:
    // %K = 100 * ((Current Close +/- Lowest Low(n)) / (Highest High(n) +/- Lowest Low(n)))
    // %D = 100 * (Sum(Current Close +/- Lowest Low(n)) / Sum(Highest High(n) +/- Lowest Low(n)))

    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();

    final MInteger outBegIdx = new MInteger();
    final MInteger outNBElement = new MInteger();

    final double[] outSlowK = new double[size - lookback];
    final double[] outSlowD = new double[outSlowK.length];

    final RetCode outcome = TA_LIB.stoch(ZERO,
                                         size - ONE,
                                         ohlcv.highs(),
                                         ohlcv.lows(),
                                         ohlcv.closes(),
                                         fastK,
                                         slowK,
                                         MAType.Sma,
                                         slowD,
                                         MAType.Sma,
                                         outBegIdx,
                                         outNBElement,
                                         outSlowK,
                                         outSlowD);
    throwExceptionIfBad(outcome, ohlcv);

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(K, dates, outSlowK),
                         new TimeSeries(D, dates, outSlowD));
  }

}
