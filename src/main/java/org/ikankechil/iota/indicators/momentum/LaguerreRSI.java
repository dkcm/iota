/**
 * LaguerreRSI.java  v0.3  2 March 2015 1:12:51 PM
 *
 * Copyright © 2015-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.indicators.LaguerreFilter;

/**
 * Laguerre Relative Strength Index by John Ehlers
 *
 * <p>http://xa.yimg.com/kq/groups/17324418/1380195797/name/cybernetic+analysis+for+stocks+and+futures+cutting-edge+dsp+technology+to+improve+your+trading+(0471463078).pdf<br>
 * http://www.mesasoftware.com/papers/TimeWarp.pdf<br>
 * https://forex-strategies-revealed.com/files/user/TimeWarp.doc<br>
 * http://www.jamesgoulding.com/Research_II/Ehlers/Ehlers%20(Time%20Warp).doc<br>
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class LaguerreRSI extends LaguerreFilter {

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
    super(gamma); // damping factor
  }

  @Override
  protected double filter(final double l0, final double l1, final double l2, final double l3) {
    return rsi(l0, l1, l2, l3);
  }

  private static final double rsi(final double l0, final double l1, final double l2, final double l3) {
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
