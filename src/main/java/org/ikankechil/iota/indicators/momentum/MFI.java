/**
 * MFI.java  v0.1  5 December 2014 4:09:09 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Money Flow Index (MFI)
 * <p>
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:money_flow_index_mfi
 *
 * @author Daniel Kuan
 * @version
 */
public class MFI extends AbstractIndicator {

  public MFI() {
    this(FOURTEEN);
  }

  public MFI(final int period) {
    super(period, TA_LIB.mfiLookback(period));
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return TA_LIB.mfi(start,
                      end,
                      ohlcv.highs(),
                      ohlcv.lows(),
                      ohlcv.closes(),
                      toDoubles(ohlcv.volumes()), // copy volumes
                      period,
                      outBegIdx,
                      outNBElement,
                      output);
  }

}
