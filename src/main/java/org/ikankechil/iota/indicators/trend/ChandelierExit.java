/**
 * ChandelierExit.java  v0.1  30 July 2015 12:30:13 am
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Chandelier Exit by Charles LeBeau
 * <p>
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:chandelier_exit
 * https://www.incrediblecharts.com/indicators/chandelier_exits.php
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ChandelierExit extends AbstractIndicator {

  private final double        multiplier;

  private static final String CHANDELIER_EXIT_LONG  = "Chandelier Exit Long";
  private static final String CHANDELIER_EXIT_SHORT = "Chandelier Exit Short";

  public ChandelierExit() {
    this(TWENTY_TWO);
  }

  public ChandelierExit(final int period) {
    this(period, THREE);
  }

  public ChandelierExit(final double multiplier) {
    this(TWENTY_TWO, multiplier);
  }

  public ChandelierExit(final int period, final double multiplier) {
    super(period, TA_LIB.atrLookback(period));
    throwExceptionIfNegative(multiplier);

    this.multiplier = multiplier;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();

    final MInteger outBegIdx = new MInteger();
    final MInteger outNBElement = new MInteger();

    // Formula:
    // Chandelier Exit (long) = 22-day High - ATR(22) x 3
    // Chandelier Exit (short) = 22-day Low + ATR(22) x 3

    // compute highs
    final double[] highs = ohlcv.highs();
    final double[] max = new double[size - lookback + ONE];
    RetCode outcome = TA_LIB.max(ZERO,
                                 size - ONE,
                                 highs,
                                 period,
                                 outBegIdx,
                                 outNBElement,
                                 max);
    throwExceptionIfBad(outcome, ohlcv);

    // compute lows
    final double[] lows = ohlcv.lows();
    final double[] min = new double[max.length];
    outcome = TA_LIB.min(ZERO,
                         size - ONE,
                         lows,
                         period,
                         outBegIdx,
                         outNBElement,
                         min);
    throwExceptionIfBad(outcome, ohlcv);

    // compute ATR
    final double[] atr = new double[size - lookback];
    outcome = TA_LIB.atr(ZERO,
                         size - ONE,
                         highs,
                         lows,
                         ohlcv.closes(),
                         period,
                         outBegIdx,
                         outNBElement,
                         atr);
    throwExceptionIfBad(outcome, ohlcv);

    // compute indicator
    final double[] chandelierExitLong = new double[atr.length];
    final double[] chandelierExitShort = new double[chandelierExitLong.length];
    for (int i = ZERO, m = ONE; i < chandelierExitLong.length; ++i, ++m) {
      final double buffer = atr[i] * multiplier;
      chandelierExitLong[i] = max[m] - buffer;
      chandelierExitShort[i] = min[m] + buffer;
    }

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(CHANDELIER_EXIT_LONG, dates, chandelierExitLong),
                         new TimeSeries(CHANDELIER_EXIT_SHORT, dates, chandelierExitShort));
  }

}
