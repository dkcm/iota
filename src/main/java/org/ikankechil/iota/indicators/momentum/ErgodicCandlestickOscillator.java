/**
 * ErgodicCandlestickOscillator.java  v0.1  14 December 2016 11:33:49 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Range;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Ergodic Candlestick Oscillator (ECO) by William Blau
 *
 * <p>www.forexfactory.com/attachment.php?attachmentid=1740215&d=1440523416<br>
 * http://www.stockfetcher.com/forums2/Indicators/Ergodic-Candlestick-Oscillator/30753<br>
 * https://www.tradingview.com/script/W7zYMP7U-ECO-Blau-s-Ergodic-Candlestick-Oscillator/<br>
 * https://www.tradingview.com/script/vWzBujcZ-Adaptive-Ergodic-Candlestick-Oscillator-LazyBear/<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ErgodicCandlestickOscillator extends AbstractIndicator {

  private final int          shortEMA;
  private final int          longEMA;

  private static final Range RANGE = new Range();

  public ErgodicCandlestickOscillator() {
    this(TWELVE, THIRTY_TWO);
  }

  public ErgodicCandlestickOscillator(final int shortEMA, final int longEMA) {
    super(shortEMA + longEMA - TWO);
    throwExceptionIfNegative(shortEMA, longEMA);

    this.shortEMA = shortEMA;
    this.longEMA = longEMA;
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
    final double[] spans = spans(ohlcv.closes(), ohlcv.opens());
    final double[] emaEMASpan = emaEMA(spans);

    // compute range and EMAs
    final double[] ranges = RANGE.generate(ohlcv, start).get(ZERO).values();
    final double[] emaEMARange = emaEMA(ranges);

    // compute indicator
    for (int i = ZERO; i < output.length; ++i) {
      output[i] = emaEMASpan[i] / emaEMARange[i] * HUNDRED;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  private static final double[] spans(final double[] closes, final double[] opens) {
    final double[] spans = new double[closes.length];
    for (int i = ZERO; i < spans.length; ++i) {
      spans[i] = closes[i] - opens[i];
    }
    return spans;
  }

  private final double[] emaEMA(final double[] values) {
    final double[] emaValues = ema(values, longEMA);
    final double[] emaEMAValues = ema(emaValues, shortEMA);
    return emaEMAValues;
  }

}
