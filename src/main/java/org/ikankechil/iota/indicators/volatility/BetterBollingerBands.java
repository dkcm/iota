/**
 * BetterBollingerBands.java  v0.4  19 July 2015 12:45:34 am
 *
 * Copyright © 2015-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

/**
 * Better Bollinger Bands by Dennis McNicholl
 *
 * <p>References:
 * <li>http://www.vantharp.com/Tharps-Thoughts/634_June_19_2013.html
 * <li>https://www.tradingview.com/script/dK1uhbN8-DEnvelope-Better-Bollinger-Bands/
 * <li>https://www.mql5.com/en/forum/173534<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.4
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
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    // Formula:
    // alpha = 2/(period + 1);
    // mt = alpha*close + (1 - alpha)*m1;
    // ut = alpha*m1 + (1 - alpha)*ut;
    // dt = ((2 - alpha)*m1 - ut)/(1 - alpha);
    // mt2 = alpha*absvalue(close - dt) + (1 - alpha)*m2;
    // ut2 = alpha*m2 + (1 - alpha)*ut2;
    // dt2 = ((2 - alpha)*m2 - ut2)/(1 - alpha);
    // but = dt + factor*dt2;
    // blt = dt - factor*dt2;

    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();

    final double[] upperBand = new double[size - lookback];
    final double[] middleBand = new double[upperBand.length];
    final double[] lowerBand = new double[upperBand.length];

    compute(upperBand, middleBand, lowerBand, ohlcv);

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(UPPER_BAND, dates, upperBand),
                         new TimeSeries(MIDDLE_BAND, dates, middleBand),
                         new TimeSeries(LOWER_BAND, dates, lowerBand));
  }

  private void compute(final double[] upperBand,
                       final double[] middleBand,
                       final double[] lowerBand,
                       final OHLCVTimeSeries ohlcv) {
    int i = ZERO;
    double pmt = ohlcv.close(i);
    double put = pmt;
    double pmt2 = ZERO;
    double put2 = pmt2;

    for (int b = ZERO; b < upperBand.length; ++b, ++i) {
      final double close = ohlcv.close(i);

      final double mt = allocate(close, pmt);
      final double ut = allocate(mt, put);
      final double centre = distribute(mt, ut);

      final double mt2 = allocate(Math.abs(close - centre), pmt2);
      final double ut2 = allocate(mt2, put2);
      final double dt2 = stdDev * distribute(mt2, ut2);

      middleBand[b] = centre;
      upperBand[b] = centre + dt2;
      lowerBand[b] = centre - dt2;

      // shift forward in time
      pmt = mt;
      put = ut;
      pmt2 = mt2;
      put2 = ut2;
    }
  }

  private double allocate(final double left, final double right) {
    return (alpha * left) + (one_alpha * right);
  }

  private double distribute(final double left, final double right) {
    return ((two_alpha * left) - right) * inverseOne_alpha;
  }

}
