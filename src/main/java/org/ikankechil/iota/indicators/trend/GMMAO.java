/**
 * GMMAO.java  v0.1  16 April 2018 10:51:36 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.util.List;

import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.IndicatorWithSignalLine;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Guppy MMA Oscillator (GMMAO) by Leon Wilson
 *
 * <p>References:
 * <li>http://www.meta-formula.com/Metastock-Formulas-G.html#Guppy MMA Oscillator<br>
 * <li>https://www.quantshare.com/item-371-guppy-multiple-moving-average-oscillator<br>
 * <li>http://www.omnitrader.com/currentclients/otforum/get-attachment.asp?attachmentid=3006<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class GMMAO extends IndicatorWithSignalLine {

  private static final GMMA GMMA = new GMMA();

  public GMMAO() {
    this(THIRTEEN);
  }

  public GMMAO(final int signal) {
    super(signal, GMMA.lookback() + (signal - ONE));
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // GMMAO = sum(fast) - sum(slow)
    // GMMAO signal line

    final List<TimeSeries> gmma = GMMA.generate(new TimeSeries(EMPTY, new String[values.length], values));

    // compute indicator
    for (int i = ZERO; i < output.length; ++i) {
      int g = -ONE;
      final double fast = gmma.get(++g).value(i) + gmma.get(++g).value(i) + gmma.get(++g).value(i) +
                          gmma.get(++g).value(i) + gmma.get(++g).value(i) + gmma.get(++g).value(i);
      final double slow = gmma.get(++g).value(i) + gmma.get(++g).value(i) + gmma.get(++g).value(i) +
                          gmma.get(++g).value(i) + gmma.get(++g).value(i) + gmma.get(++g).value(i);
      output[i] = (fast - slow);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
