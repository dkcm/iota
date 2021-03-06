/**
 * LaguerreFilter.java  v0.3  12 May 2017 11:30:33 pm
 *
 * Copyright � 2017-2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Laguerre Filter by John Ehlers
 *
 * <p>References:
 * <li>http://www.mesasoftware.com/papers/TimeWarp.pdf
 * <li>http://www.stockspotter.com/Files/timewarp.pdf
 * <li>https://user42.tuxfamily.org/chart/manual/Laguerre-Filter.html<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public abstract class LaguerreFilter extends AbstractIndicator {

  private final double gamma; // damping factor
  private final double oneMinusGamma;

  /**
   * Increase damping factor to increase smoothing and lag.
   *
   * @param gamma damping factor
   */
  public LaguerreFilter(final double gamma) {
    super(FOUR);
    throwExceptionIfNegative(gamma);

    this.gamma = gamma;
    oneMinusGamma = ONE - gamma;
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

    int today = start;
    double previousL0 = values[today];
    double previousL1 = values[++today];
    double previousL2 = values[++today];
    double previousL3 = values[++today];

    // damped Laguerre coefficients
    double gammaL0 = gamma * previousL0;
    double gammaL1 = gamma * previousL1;
    double gammaL2 = gamma * previousL2;

    // compute indicator
    for (int i = ZERO; i < output.length; ++i) {
      // Laguerre coefficients
      final double l0 = (oneMinusGamma * values[++today]) + gammaL0;
      gammaL0 = gamma * l0;
      final double l1 = -gammaL0 + previousL0 + gammaL1;
      gammaL1 = gamma * l1;
      final double l2 = -gammaL1 + previousL1 + gammaL2;
      gammaL2 = gamma * l2;
      final double l3 = -gammaL2 + previousL2 + (gamma * previousL3);

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

  /**
   * Filter Laguerre coefficients
   *
   * @param l0 first Laguerre coefficient
   * @param l1 second Laguerre coefficient
   * @param l2 third Laguerre coefficient
   * @param l3 fourth Laguerre coefficient
   * @return Laguerre output
   */
  protected abstract double filter(final double l0, final double l1, final double l2, final double l3);

}
