/**
 * RMO.java  v0.1  23 April 2018 11:14:07 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import static java.lang.Math.*;
import static org.ikankechil.iota.indicators.momentum.RMF.*;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Recursive Median Oscillator (RMO) by John Ehlers
 *
 * <p>References:
 * <li>http://traders.com/Documentation/FEEDbk_docs/2018/03/TradersTips.html<br>
 * <li>http://fxcodebase.com/code/viewtopic.php?f=17&t=65722<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class RMO extends AbstractIndicator {

  private final RMF    rmf;
  private final double twoMinusTwoAlpha;
  private final double oneMinusAlphaSquared;
  private final double oneMinusHalfAlphaSquared;

  public RMO() {
    this(THIRTY);
  }

  public RMO(final int highPass) {
    this(highPass, TWELVE);
  }

  public RMO(final int highPass, final int lowPass) {
    this(highPass, lowPass, FIVE);
  }

  public RMO(final int highPass, final int lowPass, final int median) {
    this(highPass, new RMF(lowPass, median));
  }

  public RMO(final int highPass, final RMF rmf) {
    super(highPass, (rmf.lookback() + highPass - ONE));
    this.rmf = rmf;

    final double alpha = computeAlpha(sqrt(HALF), highPass);
    final double oneMinusAlpha = ONE - alpha;
    twoMinusTwoAlpha = TWO * oneMinusAlpha;
    oneMinusAlphaSquared = oneMinusAlpha * oneMinusAlpha;
    final double oneMinusHalfAlpha = ONE - (HALF * alpha);
    oneMinusHalfAlphaSquared = oneMinusHalfAlpha * oneMinusHalfAlpha;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // compute RMF
    final double[] rmfs = new double[values.length - rmf.lookback()];
    rmf.compute(start, end, values, outBegIdx, outNBElement, rmfs);

    // compute indicator
    double rmf1 = ZERO;
    double rmf2 = ZERO;
    double rmo1 = ZERO;
    double rmo2 = ZERO;
    final double[] rmos = new double[rmfs.length];
    for (int i = ZERO; i < rmos.length; ++i) {
      final double rmf0 = rmfs[i];
      final double rmo0 = rmo(rmf0, rmf1, rmf2, rmo1, rmo2);
      rmos[i] = rmo0;

      // shift forward in time
      rmf2 = rmf1;
      rmf1 = rmf0;
      rmo2 = rmo1;
      rmo1 = rmo0;
    }
    System.arraycopy(rmos, period - ONE, output, start, output.length);

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  private double rmo(final double rmf0,
                     final double rmf1,
                     final double rmf2,
                     final double rmo1,
                     final double rmo2) {
    return oneMinusHalfAlphaSquared * (rmf0 - (TWO * rmf1) + rmf2)
         + (twoMinusTwoAlpha * rmo1)
         - (oneMinusAlphaSquared * rmo2);
  }

}
