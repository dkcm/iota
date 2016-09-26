/**
 * BetterBollingerBands.java  v0.2  19 July 2015 12:45:34 am
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

/**
 * Better Bollinger Bands by Dennis McNicholl
 * <p>
 * http://www.futuresmag.com/2010/04/30/fixing-bollinger-bands
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class BetterBollingerBands extends AbstractIndicator {

  private final double        stdDev;

  // smoothing constants
  private final double        alpha;
  private final double        one_alpha;
  private final double        two_alpha;
  private final double        inverseOne_alpha;

  private static final String UPPER_BAND  = "Better Bollinger Bands Upper Band";
  private static final String MIDDLE_BAND = "Better Bollinger Bands Middle Band";
  private static final String LOWER_BAND  = "Better Bollinger Bands Lower Band";

  public BetterBollingerBands() {
    this(TWENTY, TWO);
  }

  public BetterBollingerBands(final int period, final double stdDev) {
    super(period, TWO); // limits 2 to 200
    throwExceptionIfNegative(stdDev);

    alpha = TWO / (double) (period + ONE);
    one_alpha = ONE - alpha;
    two_alpha = TWO - alpha;
    inverseOne_alpha = ONE / one_alpha;

    this.stdDev = stdDev; // limits .01 to 10
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();

    final double[] upperBand = new double[size - lookback];
    final double[] middleBand = new double[upperBand.length];
    final double[] lowerBand = new double[upperBand.length];

    int i = ZERO;
    double pmt = ohlcv.close(i);
    double put = pmt;
    double pmt2 = ZERO;
    double put2 = pmt2;

    for (int b = ZERO; b < upperBand.length; ++b, ++i) {
      final double close = ohlcv.close(i);

      final double mt = (alpha * close) + (one_alpha * pmt);
      final double ut = (alpha * mt) + (one_alpha * put);
      final double centre = ((two_alpha * mt) - ut) * inverseOne_alpha;

      final double mt2 = (alpha * Math.abs(close - centre)) + (one_alpha * pmt2);
      final double ut2 = (alpha * mt2) + (one_alpha * put2);
      final double dt2 = stdDev * ((two_alpha * mt2) - ut2) * inverseOne_alpha;

      middleBand[b] = centre;
      upperBand[b] = centre + dt2;
      lowerBand[b] = centre - dt2;

      pmt = mt;
      put = ut;
      pmt2 = mt2;
      put2 = ut2;
    }

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(UPPER_BAND, dates, upperBand),
                         new TimeSeries(MIDDLE_BAND, dates, middleBand),
                         new TimeSeries(LOWER_BAND, dates, lowerBand));
  }

}
