/**
 * DominantCyclePeriod.java v0.1 10 March 2015 4:41:02 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.util.List;

import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Dominant Cycle Period by John Ehlers
 * <p>
 * http://xa.yimg.com/kq/groups/17324418/1380195797/name/cybernetic+analysis+for+stocks+and+futures+cutting-edge+dsp+technology+to+improve+your+trading+(0471463078).pdf
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class DominantCyclePeriod extends AbstractIndicator {

  public static void main(final String... arguments) {
    final double omega = TWO * Math.PI;

    final DominantCyclePeriod dcp = new DominantCyclePeriod();
    final int size = dcp.lookback() * TEN;
    final TimeSeries series = new TimeSeries("DominantCyclePeriodTest", size);
    for (int i = 0, t = 0; i < series.size(); ++i) {
      series.set(String.valueOf(i), Math.sin(omega * t), i);
    }

    final List<TimeSeries> indicator = dcp.generate(series);

  }

  public DominantCyclePeriod() {
    super(TA_LIB.htDcPeriodLookback());
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return TA_LIB.htDcPeriod(start,
                             end,
                             values,
                             outBegIdx,
                             outNBElement,
                             output);
  }

}
