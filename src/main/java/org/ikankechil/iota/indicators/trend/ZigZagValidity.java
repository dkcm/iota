/**
 * ZigZagValidity.java	v0.1	15 December 2015 11:22:16 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * ZigZag Validity (ZZV) by Spyros Raftopoulos
 * <p>
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V20/C08/159ZIG.pdf
 *
 * @author Daniel Kuan
 * @version 0.1
 */
class ZigZagValidity extends AbstractIndicator {

  private final ZigZag zigzag;
  private final double threshold;

  public ZigZagValidity() {
    this(TEN);
  }

  public ZigZagValidity(final double thresholdPercentage) {
    super(TWO);

    zigzag = new ZigZag(thresholdPercentage);
    threshold = thresholdPercentage / HUNDRED_PERCENT;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // perc:=Input(“Percent”,0.2,100,10);
    // Z:=Zig(Close,perc,%);
    // last:=ValueWhen(1,( Z > Ref(Z,-1) AND Ref(Z,-1) < Ref(Z,-2) )
    // OR
    // ( Z < Ref(Z,-1) AND Ref(Z,-1) > Ref(Z,-2) ), Ref(Z,-1));
    //
    // pc:=(Close-last) * 100 / last;
    // pc:= Abs(pc);
    //
    // SD:=(z>Ref(z,-1) AND Ref(z,-1)>Ref(z,-2)) OR (z<Ref(z,-1) AND Ref(z,-1)<Ref(z,-2));
    //
    // res:=If(pc>=perc ,1,0);
    // If(Alert(res,2) AND SD,1,res)

    final double[] zz = new double[values.length];
    final RetCode outcome = zigzag.compute(start,
                                           end,
                                           values,
                                           outBegIdx,
                                           outNBElement,
                                           zz);
    throwExceptionIfBad(outcome, null);

    int z = ZERO;
    double zz2 = zz[z];
    double zz1 = zz[++z];
    for (int i = ZERO; ++z < zz.length; ++i) {
      final double zz0 = zz[z];
      if ((zz0 > zz1 && zz1 < zz2) || // trough
          (zz0 < zz1 && zz1 > zz2)) { // peak
        final double change = Math.abs(values[z] / zz1 - ONE);
        if (change >= threshold) {
          output[i] = ONE;
        }
      }
      zz2 = zz1;
      zz1 = zz0;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
