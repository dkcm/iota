/**
 * SVEInverseFisherRSI.java  0.1  10 July 2017 10:45:58 PM
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.indicators.trend.RainbowCharts;
import org.ikankechil.iota.indicators.trend.WMA;
import org.ikankechil.iota.indicators.trend.ZeroLagEMA;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * A Smoothed RSI Inverse Fisher Transform by Sylvain Vervoort
 *
 * <p>References:
 * <li>http://xa.yimg.com/kq/groups/16789226/301268564/name/192verv-1.pdf<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class SVEInverseFisherRSI extends AbstractIndicator {

  private final Indicator        rsi;
  private final Indicator        zlema;

  private static final Indicator RAINBOW         = new RainbowCharts(new WMA(TWO));
  private static final int[]     RAINBOW_WEIGHTS = { FIVE, FOUR, THREE, TWO, ONE, ONE, ONE, ONE, ONE, ONE };

  public SVEInverseFisherRSI() {
    this(FOUR, FOUR);
  }

  /**
   *
   *
   * @param rsi
   * @param ema
   */
  public SVEInverseFisherRSI(final int rsi, final int ema) {
    this(new RSI(rsi), new ZeroLagEMA(ema));
  }

  public SVEInverseFisherRSI(final RSI rsi, final ZeroLagEMA ema) {
    super(RAINBOW.lookback() + rsi.lookback() + ema.lookback());

    this.rsi = rsi;
    zlema = ema;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // 1. Rainbow chart
    // 2. Weighted average
    // 3. RSI
    // 4. Zero-lag EMA
    // 5. Inverse Fisher transform

    final TimeSeries weightedRainbow = weight(RAINBOW.generate(ohlcv));

    // normalise from RSI to Inverse Fisher range
    final TimeSeries rsis = rsi2Fisher(rsi.generate(weightedRainbow).get(ZERO));

    final double[] zlemas = zlema.generate(rsis).get(ZERO).values();

    // compute indicator
    for (int i = ZERO; i < zlemas.length; ++i) {
      final double fisher = inverseFisher(zlemas[i]);
      // normalise from Inverse Fisher to RSI range
      output[i] = fisher2Rsi(fisher);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  private static final TimeSeries weight(final List<TimeSeries> rainbow) {
    final int size = rainbow.get(ZERO).size();
    final double[] weightedRainbow = new double[size];
    for (int i = ZERO; i < size; ++i) {
      double sum = ZERO;
      for (int w = ZERO; w < RAINBOW_WEIGHTS.length; ++w) {
        sum += RAINBOW_WEIGHTS[w] * rainbow.get(w).value(i);
      }
      weightedRainbow[i] = sum * TWENTIETH;
    }
    return new TimeSeries(EMPTY, new String[size], weightedRainbow);
  }

  private static final TimeSeries rsi2Fisher(final TimeSeries rsis) {
    for (int i = ZERO; i < rsis.size(); ++i) {
      final double fisher = rsi2Fisher(rsis.value(i));
      rsis.value(fisher, i);
    }
    return rsis;
  }

  private static final double rsi2Fisher(final double rsi) {
    return TENTH * (rsi - FIFTY);
  }

  private static final double fisher2Rsi(final double fisher) {
    return FIFTY * (fisher + ONE);
  }

  public static final double inverseFisher(final double x) {
    final double e = Math.exp(TWO * x);
    return (e - ONE) / (e + ONE); // y = (e^2x - 1) / (e^2x + 1)
    // TODO use Padme's approximation for -1 < x < 1
  }

}
