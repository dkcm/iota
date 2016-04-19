/**
 * TMF.java	v0.2	21 January 2015 1:14:06 am
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Twiggs Money Flow (TMF) by Colin Twiggs
 * <p>
 * http://www.incrediblecharts.com/indicators/twiggs_money_flow.php
 * http://user42.tuxfamily.org/chart/manual/Twiggs-Money-Flow.html
 *
 * @author Daniel Kuan
 * @version 0.2
 */
class TMF extends AbstractIndicator {

  private final int    trueRangeLookback;
  private final double multiplier;

  public TMF() {
    this(21);
  }

  public TMF(final int period) {
    super(period, period);

    trueRangeLookback = TA_LIB.trueRangeLookback();
    multiplier = (period - ONE) / period;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // 1. Calculate True Range High and True Range Low
    //    True Range High (TRH) is the greater of:
    //      High [today] and Closing price [yesterday]
    //    True Range Low (TRL) is the lesser of:
    //      Low [today] and Closing price [yesterday]
    // 2. Calculate AD using True Range High and True Range Low:
    //    AD = {(Close - TRL) - (TRH - Close)} / {TRH - TRL} * Volume
    // 3. Apply exponential smoothing to AD
    //    Calculate AD[21] as the sum of AD for the first 21 days
    //    On the next day, multiply AD[21] by 20/21 and add AD for day 22
    //    Repeat this process for each subsequent day.
    // 4. Do the same with the divisor
    //    Calculate V[21] as the sum of volume for the same 21 day period as in 3. above
    //    On the next day, multiply V[21] by 20/21 and add Volume for day 22
    //    Repeat this process for each subsequent day
    // 5. Divide AD[21] by V[21]:
    //    Twiggs Money Flow = AD[21] / V[21] expressed as a percentage.

    final double[] highs = ohlcv.highs();
    final double[] lows = ohlcv.lows();
    final double[] closes = ohlcv.closes();

    // compute true range high, true range low and AD
    final double[] ad = new double[ohlcv.size() - trueRangeLookback];
    final long[] volumes = ohlcv.volumes();
    for (int i = ZERO, j = trueRangeLookback; i < ad.length; ++i, ++j) {
      final double highToday = highs[j];
      final double lowToday = lows[j];
      final double closeYesterday = closes[i];

      final double trhigh = Math.max(highToday, closeYesterday);
      final double trlow = Math.min(lowToday, closeYesterday);
      final double close = closes[j]; // offset because trh and trl are shorter

      ad[i] = (((close - trlow) - (trhigh - close)) / (trhigh - trlow)) * volumes[j];
//      ad[i] = ((TWO * (close - trlow) / (trhigh - trlow)) - ONE) * volumes[j];
//      final double numerator = close - trlow;
//      final double denominator = trhigh - trlow;
//      final double m = (TWO * numerator / denominator) - ONE;
//      ad[i] = m * volumes[j];
    }

    // compute indicator
    double sad = ZERO;
    double sv = ZERO;
    for (int j = ZERO, k = trueRangeLookback; j < period; ++j, ++k) {
      sad += ad[j];
      sv += volumes[k];
    }
    int i = ZERO;
    output[i] = sad / sv;
    for (int j = period, k = j + trueRangeLookback; ++i < output.length; ++j, ++k) {
      // exponentially smooth AD
      sad *= multiplier;
      sad += ad[j];
      // exponentially smooth volume
      sv *= multiplier;
      sv += volumes[k];

      output[i] = sad / sv;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
