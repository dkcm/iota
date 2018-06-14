/**
 * CamarillaPivotPoints.java  v0.2  27 May 2018 4:04:20 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.stops;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

/**
 * Camarilla Pivot Points by Nick Scott and Slawomir Bobrowski
 *
 * <p>References:
 * <li>http://www.futuresmag.com/2012/10/24/trading-stocks-camarilla-pivots
 * <li>https://www.mypivots.com/dictionary/definition/42/camarilla-pivot-points
 * <li>http://traders.com/documentation/feedbk_docs/2013/03/traderstips.html<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class CamarillaPivotPoints extends AbstractIndicator {

  private static final String R1 = "R1";
  private static final String R2 = "R2";
  private static final String R3 = "R3";
  private static final String R4 = "R4";
  private static final String R5 = "R5";
  private static final String R6 = "R6";
  private static final String S1 = "S1";
  private static final String S2 = "S2";
  private static final String S3 = "S3";
  private static final String S4 = "S4";
  private static final String S5 = "S5";
  private static final String S6 = "S6";

  private static final double M1 = 1.1 / TWELVE;
  private static final double M5 = 1.168;

  public CamarillaPivotPoints() {
    super(ZERO);
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    // Formula:
    // R4 = Close + (High – Low) * 1.1/2
    // R3 = Close + (High – Low) * 1.1/4
    // R2 = Close + (High – Low) * 1.1/6
    // R1 = Close + (High – Low) * 1.1/12
    // S1 = Close – (High – Low) * 1.1/12
    // S2 = Close – (High – Low) * 1.1/6
    // S3 = Close – (High – Low) * 1.1/4
    // S4 = Close – (High – Low) * 1.1/2
    // R5 = R4 + 1.168 * (R4 – R3)
    // R6 = (High/Low) * Close
    // S5 = S4 – 1.168 * (S3 – S4)
    // S6 = Close – (R6 – Close)
    throwExceptionIfShort(ohlcv);

    final String[] dates = ohlcv.dates();
    final int size = ohlcv.size();

    // initialise
    final TimeSeries r1s = new TimeSeries(R1, dates, new double[size]);
    final TimeSeries r2s = new TimeSeries(R2, dates, new double[size]);
    final TimeSeries r3s = new TimeSeries(R3, dates, new double[size]);
    final TimeSeries r4s = new TimeSeries(R4, dates, new double[size]);
    final TimeSeries r5s = new TimeSeries(R5, dates, new double[size]);
    final TimeSeries r6s = new TimeSeries(R6, dates, new double[size]);
    final TimeSeries s1s = new TimeSeries(S1, dates, new double[size]);
    final TimeSeries s2s = new TimeSeries(S2, dates, new double[size]);
    final TimeSeries s3s = new TimeSeries(S3, dates, new double[size]);
    final TimeSeries s4s = new TimeSeries(S4, dates, new double[size]);
    final TimeSeries s5s = new TimeSeries(S5, dates, new double[size]);
    final TimeSeries s6s = new TimeSeries(S6, dates, new double[size]);

    // compute indicator
    for (int i = ZERO; i < size; ++i) {
      final double high = ohlcv.high(i);
      final double low = ohlcv.low(i);
      final double close = ohlcv.close(i);

      final double range = high - low;
      final double m1r = M1 * range;
      r1s.value(close + m1r, i);
      s1s.value(close - m1r, i);
      final double m2r = m1r + m1r;
      r2s.value(close + m2r, i);
      s2s.value(close - m2r, i);
      final double m3r = m2r + m1r;
      final double r3 = close + m3r;
      final double s3 = close - m3r;
      r3s.value(r3, i);
      s3s.value(s3, i);
      final double m4r = m3r + m3r;
      final double r4 = close + m4r;
      final double s4 = close - m4r;
      r4s.value(r4, i);
      s4s.value(s4, i);
      r5s.value(r5(r3, r4), i);
      s5s.value(s5(s3, s4), i);
      final double r6 = r6(high, low, close);
      r6s.value(r6, i);
      s6s.value(s6(close, r6), i);
    }

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(r6s,
                         r5s,
                         r4s,
                         r3s,
                         r2s,
                         r1s,
                         s1s,
                         s2s,
                         s3s,
                         s4s,
                         s5s,
                         s6s);
  }

  private static double r5(final double r3, final double r4) {
    return r4 + M5 * (r4 - r3);
  }

  private static double s5(final double s3, final double s4) {
    return s4 - M5 * (s3 - s4);
  }

  private static double r6(final double high,
                           final double low,
                           final double close) {
    return high / low * close;
  }

  private static double s6(final double close, final double r6) {
    return close - r6 + close;
  }

}
