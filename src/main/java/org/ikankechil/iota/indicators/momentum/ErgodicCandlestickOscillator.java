/**
 * ErgodicCandlestickOscillator.java  v0.2  14 December 2016 11:33:49 pm
 *
 * Copyright © 2016-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Ergodic Candlestick Oscillator (ECO) by William Blau
 *
 * <p>References:
 * <li>https://www.tradingview.com/scripts/ergodic/
 * <li>https://www.tradingview.com/script/W7zYMP7U-ECO-Blau-s-Ergodic-Candlestick-Oscillator/
 * <li>https://www.tradingview.com/script/vWzBujcZ-Adaptive-Ergodic-Candlestick-Oscillator-LazyBear/
 * <li>http://www.stockfetcher.com/forums2/Indicators/Ergodic-Candlestick-Oscillator/30753
 * <li>http://awesomepennystockstowatch.blogspot.com/2014/08/eco-ergodic-candlestick-oscillator.html
 * <li>https://www.mql5.com/en/articles/190<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class ErgodicCandlestickOscillator extends AbstractIndicator {

  private final int fast;
  private final int slow;

  public ErgodicCandlestickOscillator() {
    this(TWELVE, THIRTY_TWO);
  }

  public ErgodicCandlestickOscillator(final int fast, final int slow) {
    super(fast + slow - TWO);
    throwExceptionIfNegative(fast, slow);

    this.fast = fast;
    this.slow = slow;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // ECO = EMA(EMA(close - open)) / EMA(EMA(high - low)) * 100

    // compute EMA(EMA(close - open))
    final double[] spans = TSI.ema(difference(ohlcv.closes(), ohlcv.opens()), slow, fast);

    // compute range and EMAs
    final double[] ranges = TSI.ema(difference(ohlcv.highs(), ohlcv.lows()), slow, fast);

    // compute indicator
    for (int i = start; i < output.length; ++i) {
      output[i] = spans[i] / ranges[i] * HUNDRED;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
