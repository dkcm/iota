/**
 * DCCI.java  v0.1  25 November 2015 12:12:13 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.IndicatorWithSignalLine;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Dual Commodity Channel Index (DCCI) by D.W. Davies
 *
 * <p>ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V13/C01/DEFININ.PDF<br>
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V13/C12/THECOMM.pdf<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class DCCI extends IndicatorWithSignalLine {

  private final CCI cci;

  public DCCI() {
    this(TWENTY);
  }

  public DCCI(final int period) {
    this(period, FIVE);
  }

  public DCCI(final int period, final int signal) {
    super(signal, TA_LIB.cciLookback(period) + TA_LIB.emaLookback(signal));

    cci = new CCI(period);

    // Recommended arguments
    // Monthly price charts: the five-period smoothed and 20-month CCI
    // Weekly price charts: the five-period smoothed and 40-week CCI
    // Daily price charts: the five-period smoothed and 80-period CCI
    // Intraday five-minute price charts: the five-period smoothed and 80-period CCI
    // Intraday hourly price charts: the five-period smoothed and 40-period CCI
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return cci.compute(start,
                       end,
                       ohlcv,
                       outBegIdx,
                       outNBElement,
                       output);
  }

}
