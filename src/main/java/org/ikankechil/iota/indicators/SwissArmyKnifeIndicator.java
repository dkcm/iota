/**
 * SwissArmyKnifeIndicator.java  v0.1  15 May 2017 5:13:06 pm
 *
 * Copyright © 2017-2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import static java.lang.Math.*;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Swiss Army Knife Indicator by John Ehlers
 *
 * <p>References:
 * <li>https://www.mesasoftware.com/papers/SwissArmyKnifeIndicator.pdf<br>
 * <li>http://traders.com/Documentation/FEEDbk_docs/2006/01/TradersTips/TradersTips.html<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class SwissArmyKnifeIndicator extends AbstractIndicator {

  private final double c0;
  private final double c1;
  private final int    n;
  private final double b0;
  private final double b1;
  private final double b2;
  private final double a1;
  private final double a2;

  public SwissArmyKnifeIndicator(final int period, final Filters filter) {
    this(filter.coefficients(period));
  }

  public SwissArmyKnifeIndicator(final double c0, final double c1, final int n, final double b0, final double b1, final double b2, final double a1, final double a2) {
    super(Math.max(n, TWO));
    throwExceptionIfNegative(n);

    this.c0 = c0;
    this.c1 = c1;
    this.n = n;
    this.b0 = b0;
    this.b1 = b1;
    this.b2 = b2;
    this.a1 = a1;
    this.a2 = a2;
  }

  private SwissArmyKnifeIndicator(final Coefficients filter) {
    this(filter.c0, filter.c1, filter.n, filter.b0, filter.b1, filter.b2, filter.a1, filter.a2);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // output = c0 * (b0 * input + b1 * input1 + b2 * input2) + a1 * output1 + a2 * output2 - c1 * inputN

    int i2 = lookback - TWO, i1 = i2 + ONE, i = i1 + ONE;
    double v2 = values[i2];
    double v1 = values[i1];
    double v = values[i];

    // compute indicator
    for (int iN = i - n;
         i < output.length;
         i2 = i1, i1 = i, ++i, ++iN) {
      output[i] = c0 * (b0 * v + b1 * v1 + b2 * v2) +
                  a1 * output[i1] + a2 * output[i2] - c1 * values[iN];

      // shift forward in time
      v2 = v1;
      v1 = v;
      v = values[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  private static class Coefficients {

    private final double c0;
    private final double c1;
    private final int    n;
    private final double b0;
    private final double b1;
    private final double b2;
    private final double a1;
    private final double a2;

    Coefficients(final double c0, final double c1, final int n, final double b0, final double b1, final double b2, final double a1, final double a2) {
      this.c0 = c0;
      this.c1 = c1;
      this.n = n;
      this.b0 = b0;
      this.b1 = b1;
      this.b2 = b2;
      this.a1 = a1;
      this.a2 = a2;
    }

  }

  public enum Filters {
    EMA {
      @Override
      public Coefficients coefficients(final int period) {
        final double angle = TWO * PI / period;
        final double cos = cos(angle);
        final double alpha = (cos + sin(angle) - ONE) / cos;
        return new Coefficients(ONE, ZERO, ZERO, alpha, ZERO, ZERO, (ONE - alpha), ZERO);
      }
    },
    SMA {
      @Override
      public Coefficients coefficients(final int period) {
        final double inversePeriod = ONE / (double) period;
        return new Coefficients(ONE, inversePeriod, period, inversePeriod, ZERO, ZERO, ONE, ZERO);
      }
    },
    GAUSSIAN {
      @Override
      public Coefficients coefficients(final int period) {
        final double angle = TWO * PI / period;
        final double beta = 2.415 * (ONE - cos(angle));
        final double alpha = -beta + sqrt(beta * (beta + TWO));
        final double alpha2 = alpha * alpha;
        final double twoAlpha = TWO * alpha;
        return new Coefficients(alpha2, ZERO, ZERO, ONE, ZERO, ZERO, (TWO - twoAlpha), -(ONE + alpha2 - twoAlpha));
      }
    },
    BUTTERWORTH {
      @Override
      public Coefficients coefficients(final int period) {
        final double angle = TWO * PI / period;
        final double beta = 2.415 * (ONE - cos(angle));
        final double alpha = -beta + sqrt(beta * (beta + TWO));
        final double alpha2 = alpha * alpha;
        final double twoAlpha = TWO * alpha; // same as Gaussian
        return new Coefficients(alpha2 * QUARTER, ZERO, ZERO, ONE, TWO, ONE, (TWO - twoAlpha), -(ONE + alpha2 - twoAlpha));
      }
    },
    SMOOTH {
      @Override
      public Coefficients coefficients(final int period) {
        return new Coefficients(QUARTER, ZERO, ZERO, ONE, TWO, ONE, ZERO, ZERO);
      }
    },
    HIGH_PASS {
      @Override
      public Coefficients coefficients(final int period) {
        final double angle = TWO * PI / period;
        final double cos = cos(angle);
        final double alpha = (cos + sin(angle) - ONE) / cos; // same as EMA
        return new Coefficients(ONE - alpha * HALF, ZERO, ZERO, ONE, -ONE, ZERO, (ONE - alpha), ZERO);
      }
    },
    TWO_POLE_HIGH_PASS {
      @Override
      public Coefficients coefficients(final int period) {
        final double angle = TWO * PI / period;
        final double beta = 2.415 * (ONE - cos(angle));
        final double alpha = -beta + sqrt(beta * (beta + TWO));
        final double alpha2 = alpha * alpha;
        final double twoAlpha = TWO * alpha; // same as Gaussian
        return new Coefficients((ONE + alpha2 * QUARTER - alpha), ZERO, ZERO, ONE, -TWO, ONE, (TWO - twoAlpha), -(ONE + alpha2 - twoAlpha));
      }
    },
    BAND_PASS {
      @Override
      public Coefficients coefficients(final int period) {
        final double angle = TWO * PI / period;
        final double delta = 0.1; // 0.05 < delta < 0.5
        final double inverseGamma = (ONE / cos(TWO * angle * delta));
        final double alpha = inverseGamma - sqrt((inverseGamma * inverseGamma) - ONE);
        final double beta = cos(angle);
        return new Coefficients((ONE - alpha) * HALF, ZERO, ZERO, ONE, ZERO, -ONE, beta * (ONE - alpha), -alpha);
      }
    },
    BAND_STOP {
      @Override
      public Coefficients coefficients(final int period) {
        final double angle = TWO * PI / period;
        final double delta = 0.1; // 0.05 < delta < 0.5
        final double inverseGamma = (ONE / cos(TWO * angle * delta));
        final double alpha = inverseGamma - sqrt((inverseGamma * inverseGamma) - ONE);
        final double beta = cos(angle); // same as band pass
        return new Coefficients((ONE + alpha) * HALF, ZERO, ZERO, ONE, -TWO * beta, ONE, beta * (ONE + alpha), -alpha);
      }
    };

    public abstract Coefficients coefficients(final int period);

  }

}
