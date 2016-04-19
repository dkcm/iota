/**
 * ZigZag.java v0.4 6 August 2015 5:47:20 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import static org.ikankechil.util.NumberUtility.*;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Zig Zag by Arthur A. Merrill
 * <p>
 * http://tadoc.org/indicator/ZIGZAG.htm
 * http://stockcharts.com/school/doku.php?id=chart_school:trading_strategies:swing_charting
 *
 * @author Daniel Kuan
 * @version 0.4
 */
public class ZigZag extends AbstractIndicator {

  private final double  threshold;
  private final boolean isInterpolate;
  private final double  upTrendThreshold;
  private final double  downTrendThreshold;

  public ZigZag() {
    this(TEN);
  }

  /**
   * @param thresholdPercentage reversal threshold (%)
   */
  public ZigZag(final double thresholdPercentage) {
    this(thresholdPercentage, true);
  }

  /**
   * @param thresholdPercentage reversal threshold (%)
   * @param interpolate
   */
  public ZigZag(final double thresholdPercentage, final boolean interpolate) {
    super(ZERO);
    throwExceptionIfNegative(thresholdPercentage);

    threshold = thresholdPercentage / HUNDRED_PERCENT;
    isInterpolate = interpolate;
    upTrendThreshold = ONE + threshold;
    downTrendThreshold = ONE - threshold;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // zigzag on range
    zigzag(ohlcv.lows(), ohlcv.highs(), output);

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // zigzag on closes only
    zigzag(values, values, output);

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  private final void zigzag(final double[] lows,
                            final double[] highs,
                            final double[] output) {
    int i = ZERO;
    int lastMinIndex = i;
    int lastMaxIndex = i;
    double currentLow = lows[lastMinIndex];
    double currentHigh = highs[lastMaxIndex];
    double lastMin = currentLow;
    double lastMax = currentHigh;

    // compute local minima and maxima
    boolean trendingUpwards = true;
    output[i] = currentLow;
    for (; ++i < output.length; ) {
      currentLow = lows[i];
      currentHigh = highs[i];
      if (trendingUpwards) {
        if (currentLow <= lastMax * downTrendThreshold) { // reversal
          constructUptrend(lastMinIndex, lastMin, lastMaxIndex, lastMax, output);

          // prepare for new down trend
          trendingUpwards = false;
          lastMin = currentLow;
          lastMinIndex = i;
        }
        else if (currentHigh > lastMax) { // new max
          lastMax = currentHigh;
          lastMaxIndex = i;
        }
      }
      else {
        if (currentHigh >= lastMin * upTrendThreshold) {  // reversal
          constructDowntrend(lastMaxIndex, lastMax, lastMinIndex, lastMin, output);

          // prepare for new up trend
          trendingUpwards = true;
          lastMax = currentHigh;
          lastMaxIndex = i;
        }
        else if (currentLow < lastMin) {  // new min
          lastMin = currentLow;
          lastMinIndex = i;
        }
      }
    }

    // assign last minimum / maximum provisionally
    i = output.length - ONE;
    if (lastMaxIndex > lastMinIndex) {  // up trend
      constructUptrend(lastMinIndex, lastMin, lastMaxIndex, lastMax, output);
      constructDowntrend(lastMaxIndex, lastMax, i, currentLow, output);
    }
    else {                              // down trend
      constructDowntrend(lastMaxIndex, lastMax, lastMinIndex, lastMin, output);
      constructUptrend(lastMinIndex, lastMin, i, currentHigh, output);
    }
  }

  private final void constructUptrend(final int lastMinIndex,
                                      final double lastMin,
                                      final int lastMaxIndex,
                                      final double lastMax,
                                      final double... output) {
    output[lastMaxIndex] = lastMax;
    if (isInterpolate) {
      interpolate(lastMinIndex, lastMin, lastMaxIndex, lastMax, output);
    }
  }

  private final void constructDowntrend(final int lastMaxIndex,
                                        final double lastMax,
                                        final int lastMinIndex,
                                        final double lastMin,
                                        final double... output) {
    output[lastMinIndex] = lastMin;
    if (isInterpolate) {
      interpolate(lastMaxIndex, lastMax, lastMinIndex, lastMin, output);
    }
  }

}
