/**
 * ForceIndex.java  v0.3  26 February 2015 4:14:23 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.indicators.trend.EMA;
import org.ikankechil.iota.indicators.trend.SMA;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Force Index by Alexander Elder
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:force_index<br>
 * https://www.tradingview.com/stock-charts-support/index.php/Elder's_Force_Index_(EFI)<br>
 * http://www.investopedia.com/articles/trading/03/031203.asp<br>
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class ForceIndex extends AbstractIndicator {

  private final Indicator ma;

  public ForceIndex() {
    this(THIRTEEN);
  }

  public ForceIndex(final int period) {
    this(period, false);
  }

  ForceIndex(final int period, final boolean isSma) {
    super(period, period);

    ma = isSma ? new SMA(period) : new EMA(period);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // Force index = (today's close – previous close) * today's volume

    // compute indicator
    final int size = ohlcv.size();
    final TimeSeries fi = new TimeSeries(EMPTY, size - ONE);
    int c = ZERO;
    double previousClose = ohlcv.close(c);
    for (int i = ZERO; ++c < size; ++i) {
      final double todaysClose = ohlcv.close(c);

      fi.value((todaysClose - previousClose) * ohlcv.volume(c), i);

      // shift forward in time
      previousClose = todaysClose;
    }

    // smooth indicator
    final double[] mafi = ma.generate(fi).get(ZERO).values();
    System.arraycopy(mafi, ZERO, output, ZERO, output.length);

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
