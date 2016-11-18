/**
 * AcceleratorOscillator.java  v0.1  16 October 2016 11:24:02 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Accelerator Oscillator (AC) by Bill Williams
 *
 * <p>http://www.forexfactory.com/attachment.php?attachmentid=1029464&d=1346336346<br>
 * http://www.forexfactory.com/attachment.php?attachmentid=1928837&d=1463398508<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class AcceleratorOscillator extends AbstractIndicator {

  private final int               fast;
  private final AwesomeOscillator ao;

  public AcceleratorOscillator() {
    this(FIVE, THIRTY_FOUR);
  }

  public AcceleratorOscillator(final int fast, final int slow) {
    super(fast + slow - TWO);

    ao = new AwesomeOscillator(fast, slow);
    this.fast = fast;
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

    final double[] aos = ao.generate(ohlcv).get(ZERO).values();
    final double[] aoSma = sma(aos, fast);

    // compute indicator
    for (int i = ZERO, s = aos.length - aoSma.length;
         i < output.length;
         ++i, ++s) {
      output[i] = aos[s] - aoSma[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
