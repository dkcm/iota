/**
 * InstantaneousTrendline.java  v0.1  11 March 2015 10:22:05 AM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Instantaneous Trendline by John Ehlers
 *
 * <p>http://xa.yimg.com/kq/groups/17324418/1380195797/name/cybernetic+analysis+for+stocks+and+futures+cutting-edge+dsp+technology+to+improve+your+trading+(0471463078).pdf<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class InstantaneousTrendline extends AbstractIndicator {

  public InstantaneousTrendline() {
    super(TA_LIB.htTrendlineLookback());
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return TA_LIB.htTrendline(start,
                              end,
                              values,
                              outBegIdx,
                              outNBElement,
                              output);
  }

}
