/**
 * CCI.java  v0.1  4 December 2014 1:38:57 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Commodity Channel Index (CCI) by Donald Lambert
 * <p>
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:commodity_channel_index_cci
 *
 * @author Daniel Kuan
 * @version
 */
public class CCI extends AbstractIndicator {

  public CCI() {
    this(TWENTY);
  }

  public CCI(final int period) {
    super(period, TA_LIB.cciLookback(period));
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return TA_LIB.cci(start,
                      end,
                      ohlcv.highs(),
                      ohlcv.lows(),
                      ohlcv.closes(),
                      period,
                      outBegIdx,
                      outNBElement,
                      output);
  }

}
