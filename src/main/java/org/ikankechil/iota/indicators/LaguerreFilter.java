/**
 * LaguerreFilter.java  v0.1  12 May 2017 11:30:33 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Laguerre Filter by John Ehlers
 *
 * <p>http://www.mesasoftware.com/papers/TimeWarp.pdf<br>
 * https://user42.tuxfamily.org/chart/manual/Laguerre-Filter.html<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public abstract class LaguerreFilter extends AbstractIndicator {

  private final double        gamma;      // damping factor

  private static final double GAMMA = 0.8;

  /**
   * Default damping factor = 0.8
   */
  public LaguerreFilter() {
    this(GAMMA);
  }

  /**
   *
   *
   * @param gamma damping factor
   */
  public LaguerreFilter(final double gamma) {
    super(FOUR);
    throwExceptionIfNegative(gamma);

    this.gamma = gamma;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // l0 = (1 - gamma) * price  + gamma * prevL0;
    // l1 = -gamma * l0 + prevL0 + gamma * prevL1;
    // l2 = -gamma * l1 + prevL1 + gamma * prevL2;
    // l3 = -gamma * l2 + prevL2 + gamma * prevL3;

    double previousL0 = values[ZERO];
    double previousL1 = values[ONE];
    double previousL2 = values[TWO];
    double previousL3 = values[THREE];

    // compute indicator
    for (int i = ZERO, today = FOUR; i < output.length; ++i, ++today) {
      // Laguerre coefficients
      final double l0 = ((1 - gamma) * values[today]) + (gamma * previousL0);
      final double l1 = (-gamma * l0) + previousL0    + (gamma * previousL1);
      final double l2 = (-gamma * l1) + previousL1    + (gamma * previousL2);
      final double l3 = (-gamma * l2) + previousL2    + (gamma * previousL3);

//      // alternative
//      final double v = values[today];
//      final double l0 = (gamma * (previousL0 - v)) + v;
//      final double l1 = (gamma * (previousL1 - l0)) + previousL0;
//      final double l2 = (gamma * (previousL2 - l1)) + previousL1;
//      final double l3 = (gamma * (previousL3 - l2)) + previousL2;

      // Laguerre output
      output[i] = filter(l0, l1, l2, l3);

      // shift forward in time
      previousL0 = l0;
      previousL1 = l1;
      previousL2 = l2;
      previousL3 = l3;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  protected abstract double filter(final double l0, final double l1, final double l2, final double l3);

}
