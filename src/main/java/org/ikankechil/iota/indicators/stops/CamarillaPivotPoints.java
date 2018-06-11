/**
 * CamarillaPivotPoints.java  v0.1  27 May 2018 4:04:20 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.stops;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

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
 * @version 0.1
 */
public class CamarillaPivotPoints extends AbstractIndicator {

  private static final String R    = "R";
  private static final String S    = "S";
  private static final String R5   = "R5";
  private static final String R6   = "R6";
  private static final String S5   = "S5";
  private static final String S6   = "S6";

  private static final double BASE = 1.1;
  private static final double M5   = 1.168;

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
    final Map<Levels, TimeSeries> resistances = new EnumMap<>(Levels.class);
    final Map<Levels, TimeSeries> supports = new EnumMap<>(Levels.class);
    for (final Levels level : Levels.values()) {
      final int l = level.ordinal() + ONE;
      resistances.put(level, new TimeSeries(R + l, dates, new double[size]));
      supports.put(level, new TimeSeries(S + l, dates, new double[size]));
    }
    final TimeSeries r5s = new TimeSeries(R5, dates, new double[size]);
    final TimeSeries r6s = new TimeSeries(R6, dates, new double[size]);
    final TimeSeries s5s = new TimeSeries(S5, dates, new double[size]);
    final TimeSeries s6s = new TimeSeries(S6, dates, new double[size]);

    // compute indicator
    final double[] highs = ohlcv.highs();
    final double[] lows = ohlcv.lows();
    final double[] closes = ohlcv.closes();

    for (int i = ZERO; i < size; ++i) {
      final double high = highs[i];
      final double low = lows[i];
      final double range = high - low;
      final double close = closes[i];
      for (final Levels level : Levels.values()) {
        final double multipliedRange = range * level.multiplier;
        resistances.get(level).value(close + multipliedRange, i);
        supports.get(level).value(close - multipliedRange, i);
      }
      r5s.value(r5(resistances, i), i);
      s5s.value(s5(supports, i), i);
      final double r6 = r6(high, low, close);
      r6s.value(r6, i);
      s6s.value(s6(close, r6), i);
    }

    // sort
    final List<TimeSeries> camarillaPivotPoints = new ArrayList<>(resistances.values());
    camarillaPivotPoints.add(r5s);
    camarillaPivotPoints.add(r6s);
    Collections.reverse(camarillaPivotPoints);
    camarillaPivotPoints.addAll(supports.values());
    camarillaPivotPoints.add(s5s);
    camarillaPivotPoints.add(s6s);

    logger.info(GENERATED_FOR, name, ohlcv);
    return camarillaPivotPoints;
  }

  private static double r5(final Map<Levels, TimeSeries> resistances, final int index) {
    final double r3 = resistances.get(Levels.L_THREE).value(index);
    final double r4 = resistances.get(Levels.L_FOUR).value(index);
    return r4 + M5 * (r4 - r3);
  }

  private static double s5(final Map<Levels, TimeSeries> supports, final int index) {
    final double s3 = supports.get(Levels.L_THREE).value(index);
    final double s4 = supports.get(Levels.L_FOUR).value(index);
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

  private enum Levels {
    L_ONE(BASE / TWELVE),
    L_TWO(BASE / SIX),
    L_THREE(BASE / FOUR),
    L_FOUR(BASE / TWO);

    final double multiplier;

    Levels(final double multiplier) {
      this.multiplier = multiplier;
    }

  }

}
