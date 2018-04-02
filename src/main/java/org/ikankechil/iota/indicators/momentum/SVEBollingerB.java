/**
 * SVEBollingerB.java  v0.1  27 September 2017 11:47:14 PM
 *
 * Copyright © 2017-2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import static org.ikankechil.iota.indicators.AveragePrice.*;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.indicators.trend.WMA;
import org.ikankechil.iota.indicators.trend.ZeroLagTEMA;
import org.ikankechil.iota.indicators.volatility.StandardDeviation;

/**
 * A Smoothed Bollinger %b by Sylvain Vervoort
 *
 * <p>References:
 * <li>http://edmond.mires.co/GES816/28-Smoothing%20The%20Bollinger.pdf<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class SVEBollingerB extends AbstractIndicator {

  private final Indicator     zltema;
  private final Indicator     stdDevB;
  private final Indicator     wma;
  private final Indicator     stdDevUpper;
  private final Indicator     stdDevLower;

  private static final double STD_DEV     = 1.6;

  private static final String UPPER_BAND  = "SVEBollingerB Upper Band";
  private static final String MIDDLE_BAND = "SVEBollingerB Middle Band";
  private static final String LOWER_BAND  = "SVEBollingerB Lower Band";

  public SVEBollingerB() {
    this(EIGHTEEN, TWO, EIGHT, SIXTY_THREE, STD_DEV, STD_DEV);
  }

  public SVEBollingerB(final int period, final double stdDevB, final int tema, final int stdDevPeriod, final double stdDevUpper, final double stdDevLower) {
    super(period, (stdDevPeriod - ONE) + ((tema - ONE) * TWELVE) + (period - ONE));

    zltema = new ZeroLagTEMA(tema);
    this.stdDevB = new StandardDeviation(period, stdDevB);
    wma = new WMA(period);

    // standard deviation multipliers for the upper and lower bands
    this.stdDevUpper = new StandardDeviation(stdDevPeriod, stdDevUpper);
    this.stdDevLower = (stdDevUpper == stdDevLower) ?
                       this.stdDevUpper :
                       new StandardDeviation(stdDevPeriod, stdDevLower);
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    // Formula:
    // 1. Variation on Heikin-Ashi close
    // 2. Zero-lag TEMA
    // 3. WMA middle band
    // 4. Adjust
    throwExceptionIfShort(ohlcv);

    // compute variation on Heikin-Ashi close
    final TimeSeries hacs = heikinAshiClose(ohlcv);

    // compute Zero-lag TEMA
    final TimeSeries zlhacs = zltema.generate(hacs).get(ZERO);
    final TimeSeries zltemas = zltema.generate(zlhacs).get(ZERO);

    // compute standard deviation
    final double[] stdDevs = stdDevB.generate(zltemas).get(ZERO).values();

    // compute WMA
    final double[] wmas = wma.generate(zltemas).get(ZERO).values();

    // compute SVE %b
    final TimeSeries bollingerBs = bollingerB(zltemas.values(), stdDevs, wmas);

    // compute bands
    final double[] upperBand = stdDevUpper.generate(bollingerBs).get(ZERO).values();
    final double[] lowerBand = (stdDevUpper == stdDevLower) ?
                               Arrays.copyOf(upperBand, upperBand.length) :
                               stdDevLower.generate(bollingerBs).get(ZERO).values();

    for (int i = ZERO; i < upperBand.length; ++i) {
      upperBand[i] += FIFTY;
      lowerBand[i] = FIFTY - lowerBand[i];
    }

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, ohlcv.size());

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(UPPER_BAND, dates, upperBand),
                         new TimeSeries(MIDDLE_BAND,
                                        dates,
                                        Arrays.copyOfRange(bollingerBs.values(),
                                                           stdDevUpper.lookback(),
                                                           bollingerBs.size())),
                         new TimeSeries(LOWER_BAND, dates, lowerBand));
  }

  private static TimeSeries heikinAshiClose(final OHLCVTimeSeries ohlcv) {
    final int size = ohlcv.size();
    int i = ZERO;
    double hao = ohlcv.open(i);
    double average = averagePrice(ohlcv.open(i), ohlcv.high(i), ohlcv.low(i), ohlcv.close(i));

    final double[] hacs = new double[size];
    for (int c = ZERO; ++i < size; ++c) {
      hao = (average + hao) * HALF;
      average = averagePrice(ohlcv.open(i), ohlcv.high(i), ohlcv.low(i), ohlcv.close(i));
      hacs[c] = (average + hao + Math.max(ohlcv.high(i), hao) + Math.min(ohlcv.low(i), hao)) * QUARTER;
    }
    return new TimeSeries(MIDDLE_BAND, new String[hacs.length], hacs);
  }

  private static TimeSeries bollingerB(final double[] zltemas,
                                       final double[] stdDevs,
                                       final double[] wmas) {
    final int size = Math.min(zltemas.length, Math.min(stdDevs.length, wmas.length));
    final double[] bollingerBs = new double[size];
    for (int b = ZERO, z = zltemas.length - size, s = stdDevs.length - size, w = wmas.length - size;
         b < bollingerBs.length;
         ++b, ++z, ++s, ++w) {
      bollingerBs[b] = (zltemas[z] + stdDevs[s] - wmas[w]) / stdDevs[s] * FIFTY;
    }
    return new TimeSeries(MIDDLE_BAND, new String[bollingerBs.length], bollingerBs);
  }

}
