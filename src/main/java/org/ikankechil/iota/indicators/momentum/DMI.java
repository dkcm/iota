/**
 * DMI.java  v0.1  7 January 2015 11:01:15 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Dynamic Momentum Index (DMI), a variable term RSI
 *
 * <p>http://www.fmlabs.com/reference/default.htm?url=DMI.htm<br>
 * http://www.sierrachart.com/Download.php?Folder=SupportBoard&download=1446<br>
 * http://www.tradesignalonline.com/en/lexicon/view.aspx?id=Dynamic+Momentum+Index+%28DYMOI%29<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
class DMI extends AbstractIndicator {

  private final int        sma;
  private final int        stdDev;
  private final int        tdMin;
  private final int        tdMax;

  private final int        smaLookback;
  private final int        stdDevLookback;

  private static final int STD_DEVS = ONE;

  public DMI() {
    this(FOURTEEN, TEN);
  }

  public DMI(final int rsi, final int sma) {
    this(rsi, sma, FIVE, FIVE, THIRTY);
  }

  public DMI(final int rsi, final int sma, final int stdDev, final int tdMin, final int tdMax) {
    super(rsi, TA_LIB.stdDevLookback(stdDev, STD_DEVS) + TA_LIB.smaLookback(sma));

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

    smaLookback = TA_LIB.smaLookback(sma);
    stdDevLookback = lookback - smaLookback;
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

    // compute standard deviations
    final double[] stdDevs = new double[values.length - stdDevLookback];
    RetCode outcome = TA_LIB.stdDev(start,
                                    end,
                                    values,
                                    stdDev,
                                    ONE,
                                    outBegIdx,
                                    outNBElement,
                                    stdDevs);
    throwExceptionIfBad(outcome, null);

    final double[] smaStds = new double[stdDevs.length - smaLookback];
    outcome = TA_LIB.sma(ZERO,
                         stdDevs.length - ONE,
                         stdDevs,
                         sma,
                         outBegIdx,
                         outNBElement,
                         smaStds);
    throwExceptionIfBad(outcome, null);

    // compute volatility index
    final double[] vi = new double[smaStds.length];
    for (int i = ZERO; i < vi.length; ++i) {
      vi[i] = stdDevs[i + smaLookback] / smaStds[i];
    }

    // compute indicator TODO TO COMPLETE
    for (int i = ZERO; i < output.length; ++i) {
      // compute dynamic term
      int td = (int) (period / vi[ZERO]);
      td = td < tdMin ? tdMin :
           td > tdMax ? tdMax : td;

      final double[] rsi = new double[values.length - td];
      // TODO buggy? use org.ikankechil.iota.indicators.momentum.RSI instead
      TA_LIB.rsi(ZERO,
                 i + lookback,
                 values,
                 td,
                 outBegIdx,
                 outNBElement,
                 rsi);
      throwExceptionIfBad(outcome, null);
      output[i] = rsi[ZERO];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
