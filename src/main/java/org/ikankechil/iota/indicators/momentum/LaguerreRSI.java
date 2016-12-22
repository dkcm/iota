/**
 * LaguerreRSI.java  v0.2  2 March 2015 1:12:51 PM
 *
 * Copyright © 2015-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Laguerre Relative Strength Index by John Ehlers
 *
 * <p>http://xa.yimg.com/kq/groups/17324418/1380195797/name/cybernetic+analysis+for+stocks+and+futures+cutting-edge+dsp+technology+to+improve+your+trading+(0471463078).pdf<br>
 * https://forex-strategies-revealed.com/files/user/TimeWarp.doc<br>
 * http://www.jamesgoulding.com/Research_II/Ehlers/Ehlers%20(Time%20Warp).doc<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class LaguerreRSI extends AbstractIndicator {

  private final double gamma; // damping factor

  /**
   * Default damping factor = 0.5
   */
  public LaguerreRSI() {
    this(HALF);
  }

  /**
   *
   * @param gamma damping factor
   */
  public LaguerreRSI(final double gamma) {
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
    // Option 1
    // l0 = (1 - gamma) * price  + gamma * prevL0;
    // l1 = -gamma * l0 + prevL0 + gamma * prevL1;
    // l2 = -gamma * l1 + prevL1 + gamma * prevL2;
    // l3 = -gamma * l2 + prevL2 + gamma * prevL3;
    //
    // cu:= If(L0>=L1, L0-L1,0) + If(L1>=L2, L1-L2,0) + If(L2>=L3, L2-L3,0);
    // cd:= If(L0<L1, L1-L0,0) + If(L1<L2, L2-L1,0) + If(L2<L3, L3-L2,0);
    //
    // LaguerreRSI:= If(cu+cd=0,0,cu/(cu+cd));

    // Option 2
    // L0 = (1 – gamma)*Close   + gamma *L0[1];
    // L1 = - gamma *L0 + L0[1] + gamma *L1[1];
    // L2 = - gamma *L1 + L1[1] + gamma *L2[1];
    // L3 = - gamma *L2 + L2[1] + gamma *L3[1];
    //
    // CU = 0;
    // CD = 0;
    // If L0 >= L1 then CU =      L0 - L1 Else CD =      L1 - L0;
    // If L1 >= L2 then CU = CU + L1 - L2 Else CD = CD + L2 - L1;
    // If L2 >= L3 then CU = CU + L2 - L3 Else CD = CD + L3 - L2;
    //
    // If CU + CD <> 0 then LaguerreRSI = CU / (CU + CD);

    double previousL0 = values[ZERO];
    double previousL1 = values[ONE];
    double previousL2 = values[TWO];
    double previousL3 = values[THREE];

    // compute indicator
    for (int i = ZERO, today = FOUR; i < output.length; ++i, ++today) {
      // finite impulse response (FIR) filter
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

      // rsi
      output[i] = rsi(l0, l1, l2, l3);

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

  private static final double rsi(final double l0, final double l1, final double l2, final double l3) {
    double cu = ZERO;
    double cd = ZERO;
    if (l0 >= l1) {
      cu = l0 - l1;
    }
    else {
      cd = l1 - l0;
    }
    if (l1 >= l2) {
      cu += l1 - l2;
    }
    else {
      cd += l2 - l1;
    }
    if (l2 >= l3) {
      cu += l2 - l3;
    }
    else {
      cd += l3 - l2;
    }

    final double cuPlusCd = cu + cd;
    return (cuPlusCd == ZERO) ? ZERO : (cu / cuPlusCd);
  }

}
