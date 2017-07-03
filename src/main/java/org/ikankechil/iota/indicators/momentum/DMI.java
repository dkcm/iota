/**
 * DMI.java  v0.1  7 January 2015 11:01:15 pm
 *
 * Copyright © 2015-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Dynamic Momentum Index (DMI), a variable-term RSI
 *
 * <p>References:
 * <li>http://www.fmlabs.com/reference/default.htm?url=DMI.htm<br>
 * <li>http://www.sierrachart.com/Download.php?Folder=SupportBoard&download=1446<br>
 * <li>http://www.tradesignalonline.com/en/lexicon/view.aspx?id=Dynamic+Momentum+Index+%28DYMOI%29<br>
 * <li>http://exceltechnical.web.fc2.com/vldmi.html
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class DMI extends AbstractIndicator {

  private final int sma;
  private final int stdDev;
  private final int tdMin;
  private final int tdMax;

  private final int smaLookback;
  private final int stdDevLookback;

  public DMI() {
    this(FOURTEEN, TEN);
  }

  public DMI(final int rsi, final int sma) {
    this(rsi, sma, FIVE, FIVE, THIRTY);
  }

  public DMI(final int rsi, final int sma, final int stdDev, final int tdMin, final int tdMax) {
    super(rsi, stdDev + sma - TWO);

    throwExceptionIfNegative(sma, stdDev, tdMin, tdMax);
    if (tdMin > tdMax) {
      throw new IllegalArgumentException(String.format("tdMin > tdMax: %d > %d",
                                                       tdMin,
                                                       tdMax));
    }

    this.sma = sma;
    this.stdDev = stdDev;
    this.tdMin = tdMin;
    this.tdMax = tdMax;

    smaLookback = sma - ONE;
    stdDevLookback = stdDev - ONE;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // SMASTD = SMA10(STD5(Close))
    // Vi = STD5(Close) / SMASTD
    // Td = int(14 / Vi) where 5 <= Td <= 30
    // DMI = RSI(Td)

    // compute volatility index
    final double[] vi = computeVolatilityIndex(values);

    // compute indicator TODO TO COMPLETE
    for (int i = ZERO; i < output.length; ++i) {
      // compute dynamic term
      final int td = dynamicTerm(vi[ZERO]);

      final double[] rsi = new double[values.length - td];
      // TODO buggy? use org.ikankechil.iota.indicators.momentum.RSI instead
//      final RetCode outcome = TA_LIB.rsi(ZERO,
//                                         i + lookback,
//                                         values,
//                                         td,
//                                         outBegIdx,
//                                         outNBElement,
//                                         rsi);
//      throwExceptionIfBad(outcome, null);
      output[i] = rsi[ZERO];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  private double[] computeVolatilityIndex(final double[] values) {
    // compute standard deviations
    final double[] stdDevs = new double[values.length - stdDevLookback];
    final RetCode outcome = TA_LIB.stdDev(ZERO,
                                          values.length - ONE,
                                          values,
                                          stdDev,
                                          ONE,
                                          new MInteger(),
                                          new MInteger(),
                                          stdDevs);
    throwExceptionIfBad(outcome, null);

    final double[] smaStds = sma(stdDevs, sma);

    // compute volatility index
    final double[] vi = new double[smaStds.length];
    for (int i = ZERO; i < vi.length; ++i) {
      vi[i] = stdDevs[i + smaLookback] / smaStds[i];
    }

    return vi;
  }

  private final int dynamicTerm(final double volatilityIndex) {
    int td = (int) (period / volatilityIndex);
    td = td < tdMin ? tdMin :
         td > tdMax ? tdMax : td;
    return td;
  }

}
