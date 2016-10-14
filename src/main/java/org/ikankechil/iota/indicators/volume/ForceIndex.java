/**
 * ForceIndex.java  v0.2  26 February 2015 4:14:23 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Force Index by Alexander Elder
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:force_index<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class ForceIndex extends AbstractIndicator {

  private final MAType ma;

  public ForceIndex() {
    this(THIRTEEN);
  }

  public ForceIndex(final int period) {
    this(period, MAType.Ema);
  }

  ForceIndex(final int period, final MAType ma) {
    super(period, period);
    this.ma = (ma == null) ? MAType.Ema : ma;
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
    final double[] fi = new double[size - ONE];
    int c = ZERO;
    double previousClose = ohlcv.close(c);
    for (int i = ZERO; ++c < size; ++i) {
      final double todaysClose = ohlcv.close(c);

      fi[i] = (todaysClose - previousClose) * ohlcv.volume(c);

      // shift forward in time
      previousClose = todaysClose;
    }

    // smooth indicator
    return TA_LIB.movingAverage(ZERO,
                                fi.length - ONE,
                                fi,
                                period,
                                ma,
                                outBegIdx,
                                outNBElement,
                                output);
  }

}
