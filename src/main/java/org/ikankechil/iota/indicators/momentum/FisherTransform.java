/**
 * FisherTransform.java v0.1  16 January 2015 10:14:56 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Fisher Transform by John Ehlers
 * <p>
 * http://user42.tuxfamily.org/chart/manual/Fisher-Transform.html
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V20/C11/130fish.pdf
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V20/C11/142tips.pdf
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class FisherTransform extends AbstractIndicator {

  private final int           maxLookback;
  private final int           ema5Lookback;

  private static final double LIMIT           = 0.999;
  private static final int    _1072632447     = 1072632447;
  private static final double INVERSE_1512775 = ONE / (double) 1512775;

  public FisherTransform() {
    this(TEN);
  }

  public FisherTransform(final int period) {
    super(period, TA_LIB.maxLookback(period) + TA_LIB.emaLookback(FIVE) + TA_LIB.emaLookback(THREE));

    maxLookback = TA_LIB.maxLookback(period);
    ema5Lookback = TA_LIB.emaLookback(FIVE);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // 1. price = (high + low) / 2
    // 2. raw = 2 * (price - Ndaylow) / (Ndayhigh - Ndaylow) - 1
    // 3. smoothed = EMA[5] of raw
    // 4. fisher = EMA[3] of ln((1 + smoothed) / (1 - smoothed))

    // compute median prices
//    final double[] medPrices = new double[ohlcv.size()];
//    RetCode outcome = TA_LIB.medPrice(start,
//                                      end,
//                                      ohlcv.highs(),
//                                      ohlcv.lows(),
//                                      outBegIdx,
//                                      outNBElement,
//                                      medPrices);
//    throwExceptionIfBad(outcome, ohlcv);
    final double[] medPrices = new double[ohlcv.size()];
    final double[] highs = ohlcv.highs();
    final double[] lows = ohlcv.lows();
    for (int i = ZERO; i < medPrices.length; ++i) {
      medPrices[i] = (highs[i] + lows[i]) * HALF;
    }
    RetCode outcome;

    // compute median price highs and lows
//    final double[] medHighs = new double[medPrices.length - maxLookback];
//    outcome = TA_LIB.max(ZERO,
//                         medPrices.length - ONE,
//                         medPrices,
//                         period,
//                         outBegIdx,
//                         outNBElement,
//                         medHighs);
//    throwExceptionIfBad(outcome, ohlcv);
//
//    final double[] medLows = new double[medHighs.length];
//    outcome = TA_LIB.min(ZERO,
//                         medPrices.length - ONE,
//                         medPrices,
//                         period,
//                         outBegIdx,
//                         outNBElement,
//                         medLows);
//    throwExceptionIfBad(outcome, ohlcv);

    // alternate
    final double[] medHighs = new double[medPrices.length - maxLookback];
    final double[] medLows = new double[medHighs.length];
    outcome = TA_LIB.minMax(ZERO,
                            medPrices.length - ONE,
                            medPrices,
                            period,
                            outBegIdx,
                            outNBElement,
                            medHighs,
                            medLows);
    throwExceptionIfBad(outcome, ohlcv);

    // compute raw
    final double[] raws = new double[medHighs.length];
    for (int i = ZERO, j = i + maxLookback; i < raws.length; ++i, ++j) {
      final double medPrice = medPrices[j];
      final double high = medHighs[i];
      final double low = medLows[i];

      raws[i] = ((TWO * (medPrice - low)) / (high - low)) - ONE;
//      raws[i] = ((medPrice - low) - (high - medPrice)) / (high - low);
    }

    // compute EMA5 of raw
    final double[] ema5 = new double[raws.length - ema5Lookback];
    outcome = TA_LIB.ema(ZERO,
                         raws.length - ONE,
                         raws,
                         FIVE,
                         outBegIdx,
                         outNBElement,
                         ema5);
    throwExceptionIfBad(outcome, ohlcv);
//    final double[] ema5 = ema(raws, FIVE);

    // compute log
    for (int i = ZERO; i < ema5.length; ++i) {
      // limit / "clamp" to avoid infinity and NaN
      final double smoothed = limit(ema5[i]);
      ema5[i] = fisher(smoothed);
    }

    // compute EMA3 of log
    outcome = TA_LIB.ema(ZERO,
                         ema5.length - ONE,
                         ema5,
                         THREE,
                         outBegIdx,
                         outNBElement,
                         output);
    throwExceptionIfBad(outcome, ohlcv);

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  private static final double fisher(final double x) {
    return ln((ONE + x) / (ONE - x));
//    return Math.log((ONE + x) / (ONE - x));
  }

  private static final double ln(final double x) {
    final double y = (Double.doubleToLongBits(x) >> THIRTY_TWO);
    return (y - _1072632447) * INVERSE_1512775;
  }

  private static final double limit(final double d) {
    return (d >  LIMIT) ?  LIMIT :
           (d < -LIMIT) ? -LIMIT : d;
  }

}
