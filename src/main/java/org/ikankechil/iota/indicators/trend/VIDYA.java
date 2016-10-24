/**
 * VIDYA.java  v0.2  8 January 2015 6:56:46 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.momentum.CMO;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Variable Index Dynamic Average (VIDYA)
 *
 * <p>ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V13/C10/SIDEVAR.pdf<br>
 * http://www.straticator.com/vidya-variable-index-dynamic-average/<br>
 * http://www.fmlabs.com/reference/default.htm?url=VIDYA.htm<br>
 * http://www.thewizardtrader.com/Education/TechnicalIndicators/Indicators/VIDYA.aspx<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class VIDYA extends AbstractIndicator {

  private final double        sc; // Smoothing Constant, sc = 2 / (n + 1)
  private final CMO           cmo;

  private static final double ONE_PERCENT = ONE / HUNDRED_PERCENT;

  public VIDYA() {
    this(TWELVE, NINE);
  }

  public VIDYA(final int period, final int cmo) {
    super(cmo, (cmo - ONE));
    throwExceptionIfNegative(period);

    sc = TWO / (double) (period + ONE);
    this.cmo = new CMO(cmo);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // Smoothing Constant, sc = 2 / (n + 1)
    // Volatility Index, vi = CMO(price)
    // VIDYA = (sc * vi * price) + (1 - sc * vi) * VIDYA_1

    // compute Volatility Index
    final double[] vi = cmo.generate(ohlcv).get(ZERO).values();

    // compute indicator TODO buggy?
    final double[] closes = ohlcv.closes();
    double previous = output[ZERO] = closes[lookback]; // seed
    for (int i = ONE, j = i + lookback, k = ZERO; i < output.length; ++i, ++j, ++k) {
      final double scVi = sc * Math.abs(vi[k] * ONE_PERCENT);
      output[i] = (scVi * closes[j]) + ((ONE - scVi) * previous);
      previous = output[i];
    }
    logger.debug("Volatility Index: {}", vi);

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
