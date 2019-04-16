/**
 * Vortex.java  v0.3  15 December 2014 2:24:23 PM
 *
 * Copyright © 2014-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.indicators.volatility.TR;

/**
 * Vortex Indicator by Etienne Botes and Douglas Siepman
 *
 * <p>References:
 * <li>http://www.traders.com/Reprints/PDF_reprints/VFX_VORTEX.PDF<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class Vortex extends AbstractIndicator {

  private static final Indicator TR           = new TR();

  private static final String    PLUS_VORTEX  = "+Vortex";
  private static final String    MINUS_VORTEX = "-Vortex";

  public Vortex() {
    this(FOURTEEN); // periods: 13, 14, 21, 34 or 55
  }

  public Vortex(final int period) {
    super(period, period);
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();

    // compute +VM and -VM
    final double[] plusVM = new double[size - ONE];
    final double[] minusVM = new double[plusVM.length];
    computeVortexMovements(plusVM, minusVM, ohlcv);

    // compute +VM and -VM sums
    final double[] sumPlusVM = sum(period, plusVM);
    final double[] sumMinusVM = sum(period, minusVM);

    // compute true range
    final double[] tr = TR.generate(ohlcv, start).get(ZERO).values();

    // compute true range sum
    final double[] sumTR = sum(period, tr);

    // compute +VI and -VI indicator
    computeVortexIndicators(sumPlusVM, sumMinusVM, sumTR); // re-use arrays to save memory

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(PLUS_VORTEX, dates, sumPlusVM),
                         new TimeSeries(MINUS_VORTEX, dates, sumMinusVM));
  }

  private static void computeVortexMovements(final double[] plusVM,
                                             final double[] minusVM,
                                             final OHLCVTimeSeries ohlcv) {
    final int size = ohlcv.size();
    final double[] highs = ohlcv.highs();
    final double[] lows = ohlcv.lows();

    int t = ZERO;
    double ph = highs[t];
    double pl = lows[t];
    for (int j = ZERO; ++t < size; ++j) {
      final double ch = highs[t];
      final double cl = lows[t];
      plusVM[j] = Math.abs(ch - pl);
      minusVM[j] = Math.abs(cl - ph);

      // shift forward in time
      ph = ch;
      pl = cl;
    }
  }

  private static void computeVortexIndicators(final double[] plusVI,
                                              final double[] minusVI,
                                              final double[] sumTR) {
    for (int i = ZERO; i < plusVI.length; ++i) {
      final double inverseSumTR = ONE / sumTR[i];
      plusVI[i] *= inverseSumTR;
      minusVI[i] *= inverseSumTR;
    }
  }

}
