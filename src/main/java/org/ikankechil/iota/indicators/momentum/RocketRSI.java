/**
 * RocketRSI.java  v0.1  14 April 2018 12:54:52 AM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import static org.ikankechil.iota.indicators.momentum.FisherTransform.*;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Rocket RSI by John Ehlers
 *
 * <p>References:
 * <li>http://traders.com/Documentation/FEEDbk_docs/2018/05/TradersTips.html<br>
 * <li>http://technical.traders.com/content/TTlink.asp?mo=05&yr=2018<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class RocketRSI extends AbstractIndicator {

  private final Momentum momentum;
  private final double   c1;
  private final double   c2;
  private final double   c3;

  public RocketRSI() {
    this(EIGHT, TEN);
  }

  public RocketRSI(final int smooth, final int rsi) {
    super(smooth, (rsi + smooth + ONE)); // (rsi - 1) + 1 + (smooth + 1)

    momentum = new Momentum(rsi - ONE);

    // compute coefficients
    final double constant = Math.sqrt(TWO) * Math.PI / smooth;
    final double a1 = Math.exp(-constant);
    c2 = TWO * a1 * Math.cos(constant);
    c3 = -a1 * a1;
    c1 = (ONE - c2 - c3) * HALF;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // 1. half dominant cycle momentum
    // 2. Super Smoother filter
    // 3. accumulate
    // 4. apply fisher transform

    final double[] momenta = computeMomentum(values);
    final double[] ss = computeSuperSmoother(momenta);

    // compute indicator
    for (int i = ZERO; i < output.length; ++i) {
      final double rsi = accumulate(period, ss, i);
      output[i] = HALF * fisher(limit(rsi));
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  private double[] computeMomentum(final double... closes) {
    final double[] momenta = new double[closes.length - momentum.lookback()];
    momentum.compute(ZERO, closes.length, closes, new MInteger(), new MInteger(), momenta);
    return momenta;
  }

  private double[] computeSuperSmoother(final double... momenta) {
    final double[] ss = new double[momenta.length - ONE];

    double ss0;
    double ss1 = ZERO;
    double ss2 = ZERO;
    int m = ZERO;
    double pm = momenta[m];
    for (int s = ZERO; s < ss.length; ++s) {
      final double cm = momenta[++m];
      ss[s] = ss0 = c1 * (cm + pm) + (c2 * ss1) + (c3 * ss2);

      pm = cm;
      ss2 = ss1;
      ss1 = ss0;
    }

    return ss;
  }

  private static double accumulate(final int period, final double[] superSmoother, final int from) {
    double cu = ZERO;
    double cd = ZERO;
    int s = from;
    double pss = superSmoother[s];
    final int to = from + period + ONE;
    while (++s < to) {
      final double css = superSmoother[s];
      final double change = css - pss;
      if (change > ZERO) {
        cu += change;
      }
      else if (change < ZERO) {
        cd -= change;
      }
      pss = css;
    }

    final double cuPlusCd = cu + cd;
    return (cuPlusCd == ZERO) ? ZERO : ((cu - cd) / cuPlusCd);
  }

}
