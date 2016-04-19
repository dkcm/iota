/**
 * Kendall.java	v0.1	19 July 2015 9:59:43 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Kendall Indicator by Eric Kendall
 * <p>
 * http://www.addownload.eu/metaK.htm#Kendall Indicator
 * http://exceltechnical.web.fc2.com/kendall.html
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class Kendall extends AbstractIndicator {

  private final int roc;

  public Kendall() {
    this(TWELVE, FIVE);
  }

  public Kendall(final int roc, final int sma) {
    super(sma, TA_LIB.rocLookback(roc) + TA_LIB.smaLookback(sma));
    throwExceptionIfNegative(roc);

    this.roc = roc;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    final double[] smaRoc = smaRoc(ohlcv, period, roc);
    System.arraycopy(smaRoc, ZERO, output, ZERO, output.length);

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
