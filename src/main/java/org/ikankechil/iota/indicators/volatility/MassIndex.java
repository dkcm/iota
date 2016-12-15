/**
 * MassIndex.java  v0.1  14 December 2016 9:53:25 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Range;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Mass Index by Donald Dorsey
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:mass_index<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MassIndex extends AbstractIndicator {

  private final int          ema;
  private final int          sum;

  private static final Range RANGE = new Range();

  public MassIndex() {
    this(NINE, TWENTY_FIVE);
  }

  /**
   *
   *
   * @param ema EMA period
   * @param sum period of the sum of the EMA ratio
   */
  public MassIndex(final int ema, final int sum) {
    super((ema << ONE) + sum - THREE); // = (ema - 1) * 2 + (sum - 1)
    throwExceptionIfNegative(ema, sum);

    this.ema = ema;
    this.sum = sum;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // Single EMA = 9-period exponential moving average (EMA) of the high-low differential
    // Double EMA = 9-period EMA of the 9-period EMA of the high-low differential
    // EMA Ratio = Single EMA divided by Double EMA
    // Mass Index = 25-period sum of the EMA Ratio

    // compute range and EMAs
    final double[] ranges = RANGE.generate(ohlcv, start).get(ZERO).values();
    final double[] singleEMA = ema(ranges, ema);
    final double[] doubleEMA = ema(singleEMA, ema);

    // compute EMA ratio
    final double[] emaRatio = new double[doubleEMA.length];
    for (int i = ZERO, j = i + singleEMA.length - doubleEMA.length; i < emaRatio.length; ++i, ++j) {
      emaRatio[i] = (singleEMA[j] / doubleEMA[i]);
    }

    // compute indicator
    final double[] massIndex = sum(sum, emaRatio);
    System.arraycopy(massIndex, ZERO, output, ZERO, output.length);

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}

