/**
 * Vortex.java  v0.1  15 December 2014 2:24:23 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.volatility.TR;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Vortex Indicator
 * <p>
 * http://www.traders.com/Reprints/PDF_reprints/VFX_VORTEX.PDF
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class Vortex extends AbstractIndicator {

  private final TR            trueRange;

  private static final String PLUS_VORTEX  = "+Vortex";
  private static final String MINUS_VORTEX = "-Vortex";

  public Vortex() {
    this(FOURTEEN); // periods: 14, 21, or 55
  }

  public Vortex(final int period) {
    super(period, period);

    trueRange = new TR();
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();

    final double[] highs = ohlcv.highs();
    final double[] lows = ohlcv.lows();

    // compute +VM and -VM
    final double[] plusVM = new double[size - ONE];
    final double[] minusVM = new double[size - ONE];

    for (int i = ONE, j = ZERO; i < size; ++i, ++j) {
      plusVM[j] = Math.abs(highs[i] - lows[j]);
      minusVM[j] = Math.abs(lows[i] - highs[j]);
    }

    // compute +VM and -VM sums
    final MInteger outBegIdx = new MInteger();
    final MInteger outNBElement = new MInteger();

    final int length = size - lookback;
    final double[] sumPlusVM = new double[length];
    RetCode outcome = TA_LIB.sum(ZERO,
                                 plusVM.length - ONE,
                                 plusVM,
                                 period,
                                 outBegIdx,
                                 outNBElement,
                                 sumPlusVM);
    throwExceptionIfBad(outcome, ohlcv);

    final double[] sumMinusVM = new double[length];
    outcome = TA_LIB.sum(ZERO,
                         minusVM.length - ONE,
                         minusVM,
                         period,
                         outBegIdx,
                         outNBElement,
                         sumMinusVM);
    throwExceptionIfBad(outcome, ohlcv);

    // compute true range
    final double[] tr = trueRange.generate(ohlcv).get(ZERO).values();

    // compute true range sum
    final double[] sumTR = new double[length];
    outcome = TA_LIB.sum(ZERO,
                         tr.length - ONE,
                         tr,
                         period,
                         outBegIdx,
                         outNBElement,
                         sumTR);
    throwExceptionIfBad(outcome, ohlcv);

    // compute +VI and -VI indicator
    for (int i = ZERO; i < length; ++i) {
      sumPlusVM[i] /= sumTR[i]; // re-use to save memory
      sumMinusVM[i] /= sumTR[i];
    }

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(PLUS_VORTEX, dates, sumPlusVM),
                         new TimeSeries(MINUS_VORTEX, dates, sumMinusVM));
  }

}
