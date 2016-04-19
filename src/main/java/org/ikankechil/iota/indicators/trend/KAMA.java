/**
 * KAMA.java v0.2 9 December 2014 12:18:38 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Kaufman Adaptive Moving Average (KAMA)
 * <p>
 * http://www.investopedia.com/articles/trading/08/adaptive-moving-averages.asp
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class KAMA extends AbstractIndicator {

  public KAMA(final int period) {
    super(period, TA_LIB.kamaLookback(period));
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return TA_LIB.kama(start,
                       end,
                       values,
                       period,
                       outBegIdx,
                       outNBElement,
                       output);
  }

}
