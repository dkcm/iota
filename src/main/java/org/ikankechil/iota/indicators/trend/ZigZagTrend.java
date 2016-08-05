/**
 * ZigZagTrend.java  v0.2  15 December 2015 11:08:56 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Zig Zag Trend Indicator (ZZT) by Spyros Raftopoulos
 * <p>
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V21/C11/233RAFT.pdf
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class ZigZagTrend extends AbstractIndicator {

  private final ZigZag zigzag;
  private final double threshold;

  public ZigZagTrend() {
    this(TEN);
  }

  public ZigZagTrend(final double thresholdPercentage) {
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
  // vr:=Input(“Field (0=Ind/tor, 1=Open, 2=High, 3=Low, 4=Close)”,0,4,0);
  // amnt:=Input(“Reversal amount”,0.01,1000,10);
  // md:=Input(“Method (1=Percent, 2=Points)”,1,2,1);
  //
  // vr:=If(vr=1,OPEN,If(vr=2,HIGH,If(vr=3,LOW,If(vr=4,CLOSE,P))));
  // zz0:=If(md=1, Zig(vr,amnt,%), Zig(vr,amnt,$));
  // zz1:=Ref(zz0,-1);
  // zz2:=Ref(zz0,-2);
  //
  // tr:=ValueWhen(1,zz0>zz1 AND zz1<zz2, zz1);
  // pk:=ValueWhen(1,zz0<zz1 AND zz1>zz2, zz1);
  // PU:=If(md=1,tr+Abs(tr)*amnt/100,tr+amnt);
  // PD:=If(md=1,pk-Abs(pk)*amnt/100,pk-amnt);
  //
  // res:=If(vr>=PU AND zz0>zz1,1, If(vr<=PD AND zz0<zz1,-1,0));
  // res:=If(res<>0,res,ValueWhen(1,res<>0,res));

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

    double trough = ZERO;
    double peak = ZERO;
    double trend = ZERO;
    for (int i = ZERO; ++z < zz.length; ++i) {
      final double zz0 = zz[z];
      if (zz0 > zz1 && zz1 < zz2) {
//        trough = zz1;
        trough = zz1 + (Math.abs(zz1) * threshold);
        logger.debug("Trough detected: {} (index: {})", trough, z);
      }
      else if (zz0 < zz1 && zz1 > zz2) {
//        peak = zz1;
        peak = zz1 - (Math.abs(zz1) * threshold);
        logger.debug("Peak detected: {} (index: {})", peak, z);
      }
//      final double pu = trough + (Math.abs(trough) * threshold);
//      final double pd = peak - (Math.abs(peak) * threshold);

      final double value = values[z];
//      output[i] = (value >= pu && zz0 > zz1) ?  ONE : // up trend
//                  (value <= pd && zz0 < zz1) ? -ONE : // down trend
//                                               trend;
//      trend = output[i];

      output[i] = (value >= trough && zz0 > zz1) ? trend =  ONE : // up trend
                  (value <= peak   && zz0 < zz1) ? trend = -ONE : // down trend
                                                   trend;         // keep existing trend

      zz2 = zz1;
      zz1 = zz0;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
