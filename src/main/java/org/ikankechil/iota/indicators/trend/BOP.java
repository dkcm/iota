/**
 * BOP.java  v0.2  10 December 2014 9:14:07 AM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Balance Of Power (BOP) by Igor Livshin
 * <p>
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V19/C08/086BMP.pdf<br>
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V19/C08/094TIPS.pdf<br>
 * https://www.linnsoft.com/techind/balance-power-bop<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class BOP extends AbstractIndicator {

  public BOP() {
    super(ZERO);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // BOP = (close - open) / (high - low) = change / range

    for (int i = ZERO; i < output.length; ++i) {
      output[i] = (ohlcv.close(i) - ohlcv.open(i)) / (ohlcv.high(i) - ohlcv.low(i));
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
