/**
 * AcceleratorOscillator.java  v0.2  16 October 2016 11:24:02 pm
 *
 * Copyright © 2016-2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.trend.SMA;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Accelerator Oscillator (AC) by Bill Williams
 *
 * <p>References:
 * <li>http://www.forexfactory.com/attachment.php?attachmentid=1029464&d=1346336346
 * <li>http://www.forexfactory.com/attachment.php?attachmentid=1928837&d=1463398508
 * <li>http://tlc.tdameritrade.com.sg/center/reference/Tech-Indicators/studies-library/A-B/AccelerationDecelerationOsc.html<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class AcceleratorOscillator extends AbstractIndicator {

  private final SMA               sma;
  private final AwesomeOscillator ao;

  public AcceleratorOscillator() {
    this(FIVE, THIRTY_FOUR);
  }

  public AcceleratorOscillator(final int fast, final int slow) {
    super(fast + slow - TWO);

    ao = new AwesomeOscillator(fast, slow);
    sma = new SMA(fast);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // AC = Present Value of AO - 5-Bar Average of AO

    final TimeSeries aos = ao.generate(ohlcv).get(ZERO);
    final TimeSeries aoSma = sma.generate(aos).get(ZERO);

    // compute indicator
    for (int i = ZERO, s = aos.size() - aoSma.size();
         i < output.length;
         ++i, ++s) {
      output[i] = aos.value(s) - aoSma.value(i);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
