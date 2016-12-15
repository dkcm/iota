/**
 * TRIX.java  v0.1  15 December 2016 9:44:12 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.IndicatorWithSignalLine;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Triple Exponential (TRIX) by Jack Hutson
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:trix<br>
 * https://c.forex-tsd.com/forum/64/trix_-_jack_huton.pdf<br>
 * https://c.forex-tsd.com/forum/63/trix_-_the_original.pdf<br>
 * http://www.investopedia.com/articles/technical/02/092402.asp<br>
 * https://en.wikipedia.org/wiki/Trix_(technical_analysis)<br>
 * http://www.incrediblecharts.com/indicators/trix_indicator.php<br>
 * http://chartalytics.com/cgi-bin/details?TRIX-indicates-an-downtrend&c=33<br>
 * http://www.blastchart.com/Community/IndicatorGuide/Indicators/TRIX.aspx<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class TRIX extends IndicatorWithSignalLine {

  public TRIX() {
    this(FIVE, NINE);
  }

  public TRIX(final int period, final int signal) {
    super(period, signal, (period * THREE) + signal - THREE);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // TRIX = (EMA3[today] - EMA3[yesterday]) / EMA3[yesterday] * 100

    // compute EMAs
    final double[] ema3 = ema(ema(ema(values, period), period), period);

    // compute indicator
    for (int i = ZERO, j = i + ONE; i < output.length; ++i, ++j) {
      output[i] = HUNDRED_PERCENT * (ema3[j] / ema3[i] - ONE);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
