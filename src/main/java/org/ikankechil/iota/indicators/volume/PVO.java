/**
 * PVO.java  v0.1  15 December 2016 1:12:50 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.trend.PPO;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Percentage Volume Oscillator (PVO)
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:percentage_volume_oscillator_pvo<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class PVO extends PPO {

  public PVO() {
    this(TWELVE, TWENTY_SIX, NINE);
  }

  public PVO(final int fast, final int slow, final int signal) {
    super(fast, slow, signal);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return compute(start,
                   end,
                   toDoubles(ohlcv.volumes()),
                   outBegIdx,
                   outNBElement,
                   output);
  }

}
