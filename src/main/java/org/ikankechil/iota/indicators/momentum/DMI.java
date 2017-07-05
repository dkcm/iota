/**
 * DMI.java  v0.2  7 January 2015 11:01:15 pm
 *
 * Copyright © 2015-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.indicators.volatility.StandardDeviation;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Dynamic Momentum Index (DMI), a variable-term RSI, by Tushar Chande and Stanley Kroll
 *
 * <p>References:
 * <li>http://www.fmlabs.com/reference/default.htm?url=DMI.htm<br>
 * <li>http://www.sierrachart.com/Download.php?Folder=SupportBoard&download=1446<br>
 * <li>http://www.tradesignalonline.com/en/lexicon/view.aspx?id=Dynamic+Momentum+Index+%28DYMOI%29<br>
 * <li>http://exceltechnical.web.fc2.com/vldmi.html
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class DMI extends AbstractIndicator {

  private final int       sma;
  private final Indicator stdDev;
  private final int       tdMin;
  private final int       tdMax;

  private final int       smaLookback;

  public DMI() {
    this(FOURTEEN, TEN);
  }

  public DMI(final int rsi, final int sma) {
    this(rsi, sma, FIVE, FIVE, THIRTY);
  }

  public DMI(final int rsi, final int sma, final int stdDev, final int tdMin, final int tdMax) {
    super(rsi, tdMax);

    throwExceptionIfNegative(sma, stdDev, tdMin, tdMax);
    if (tdMin > tdMax) {
      throw new IllegalArgumentException(String.format("tdMin > tdMax: %d > %d",
                                                       tdMin,
                                                       tdMax));
    }

    this.sma = sma;
    this.stdDev = new StandardDeviation(stdDev, ONE);
    this.tdMin = tdMin;
    this.tdMax = tdMax;

    smaLookback = sma - ONE;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // SMASTD = SMA10(STD5(Close))
    // Vi = STD5(Close) / SMASTD
    // Td = int(14 / Vi) where 5 <= Td <= 30
    // DMI = RSI(Td)

    // compute volatility index
    final double[] vi = computeVolatilityIndex(ohlcv);

    // compute indicator
    final double[] values = ohlcv.values();
    for (int i = ZERO, v = vi.length - output.length; i < output.length; ++i, ++v) {
      // compute dynamic term
      final int td = dynamicTerm(vi[v]);
      output[i] = dmi(td, values, i + lookback - td);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  private double[] computeVolatilityIndex(final OHLCVTimeSeries ohlcv) {
    // compute standard deviations
    final double[] stdDevs = stdDev.generate(ohlcv).get(ZERO).values();

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

  private static final double dmi(final int td, final double[] values, final int from) {
    return RSI.rsi(td, values, from);
  }

}
