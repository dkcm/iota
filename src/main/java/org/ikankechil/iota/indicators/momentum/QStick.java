/**
 * QStick.java  0.1  19 December 2016 6:25:22 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.Body;
import org.ikankechil.iota.indicators.IndicatorWithSignalLine;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Quick stick (QStick) by Tushar Chande
 *
 * <p>http://www.precisiontradingsystems.com/QSTICK.htm<br>
 * http://www.technicalindicators.net/indicators-technical-analysis/102-qstick-indcator
 * https://www.tradingview.com/script/ssL68jQu-Indicator-Chande-s-QStick-Indicator/<br>
 * http://www.investopedia.com/terms/q/qstick.asp<br>
 * http://tradingsim.com/blog/qstick/<br>
 * http://stockfetcher.com/forums2/Indicators/QStick/30763<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class QStick extends IndicatorWithSignalLine {

  private static final Body BODY = new Body();

  public QStick() {
    this(TWELVE, NINE);
  }

  public QStick(final int period, final int signal) {
    super(period, signal, period + signal - TWO);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // QStick = SMA(Close - Open)

    final double[] bodies = BODY.generate(ohlcv).get(ZERO).values();
    final double[] qsticks = sma(bodies, period);
    System.arraycopy(qsticks, ZERO, output, ZERO, output.length);

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
