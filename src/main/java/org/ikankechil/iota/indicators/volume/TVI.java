/**
 * TVI.java  v0.1  8 July 2019 10:30:26 PM
 *
 * Copyright © 2019-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Trade Volume Index
 *
 * <p>References:
 * <li>https://www.investopedia.com/terms/t/tradevolumeindex.asp
 * <li>https://www.investopedia.com/ask/answers/051915/what-trade-volume-index-tvi-formula-and-how-it-calculated.asp
 * <li>https://www.marketinout.com/technical_analysis.php?t=Trade_Volume_Index_(TVI)&id=105
 * <li>https://www.stockmaniacs.net/trade-volume-index-indicator/
 * <li>https://tradingsim.com/blog/trade-volume-index/<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class TVI extends AbstractIndicator {

  private final double mtv;

  public TVI() {
    this(HALF);
  }

  /**
   *
   *
   * @param mtv minimum tick value
   */
  public TVI(final double mtv) {
    super(ZERO);
    throwExceptionIfNegative(mtv);

    this.mtv = mtv;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // price change > MTV: TVI += Volume (Accumulation)
    // price change < -MTV: TVI -= Volume (Distribution)
    // -MTV <= price change <= MTV: repeat last action (Accumulation / Distribution)

    int yesterday = start;
    double pc = ohlcv.close(yesterday);
    double tvi = ohlcv.volume(yesterday);
    output[yesterday] = tvi;
    boolean accumulate = true;

    for (int today = yesterday + ONE; today < output.length; ++today) {
      final double close = ohlcv.close(today);
      final long volume = ohlcv.volume(today);
      final double change = close - pc;
      if (change > mtv) {
        tvi += volume;
        accumulate = true;
      }
      else if (change < -mtv) {
        tvi -= volume;
        accumulate = false;
      }
      else if (accumulate) {
        tvi += volume;
      }
      else {
        tvi -= volume;
      }
      output[today] = tvi;

      // shift forward
      yesterday = today;
      pc = close;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
