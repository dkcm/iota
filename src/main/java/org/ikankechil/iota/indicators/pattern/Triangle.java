/**
 * Triangle.java v0.2 8 January 2016 10:56:34 AM
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import static org.ikankechil.iota.indicators.pattern.Extrema.*;
import static org.ikankechil.util.NumberUtility.*;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Indicator;

/**
 * Triangle pattern recognition algorithm.  Covers triangles and wedges.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class Triangle extends AbstractIndicator {

  private final Indicator  topsAndBottoms;

  public Triangle(final int awayPoints) {
    super(ZERO);

    topsAndBottoms = new TopsAndBottoms(awayPoints, null, false);
  }

  private List<TimeSeries> detectLastTriangle(final OHLCVTimeSeries ohlcv) {
    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();
    final String[] dates = ohlcv.dates();

    // generate tops and bottoms
    final List<TimeSeries> tab = topsAndBottoms.generate(ohlcv);
    final double[] tops = tab.get(ZERO).values();
    final double[] bottoms = tab.get(ONE).values();

    // locate last 2 pairs of tops and bottoms
    final int t2 = previousExtremum(tops, size);    // last top
    final int b2 = previousExtremum(bottoms, size); // last bottom
    final int t1 = previousExtremum(tops, t2);      // penultimate top
    final int b1 = previousExtremum(bottoms, b2);   // penultimate bottom

    // compute gradients
    final double mt = gradient(t1, tops[t1], t2, tops[t2]);
    final double mb = gradient(b1, bottoms[b1], b2, bottoms[b2]);

    final double[] resistance = new double[size];
    final double[] support = new double[size];

    // recognise last triangle only
    // 1. support and resistance need to intersect to qualify as a triangle,
    //    i.e. mt < mb
    // 2. top / bottom immediately followed by bottom / top

    if ((mt < mb) &&
        ((t1 < b2) || (b1 < t2))){
      // compute intercepts
      final double ct = intercept(t2, tops[t2], mt);
      final double cb = intercept(b2, bottoms[b2], mb);

      // locate intersection
      // y = m1x + c1
      // y = m2x + c2
      // x = (c2 - c1) / (m1 - m2)
      final double x = (cb - ct) / (mt - mb);
      final double y = f(x, mb, cb);
      logger.info("Intersection: ({}, {})", x, y);

      // determine start and end points first then interpolate just once
      final int x1 = Math.min(b1, t1);
      final int x2 = Math.max(b2, t2);

      Arrays.fill(resistance, ZERO, x1, Double.NaN);
      Arrays.fill(support, ZERO, x1, Double.NaN);
      if (x2 < size) {
        Arrays.fill(resistance, x2, size, Double.NaN);
        Arrays.fill(support, x2, size, Double.NaN);
      }

      interpolate(x1,
                  resistance[x1] = f(x1, mt, ct),
                  x2,
                  resistance[x2] = f(x2, mt, ct),
                  resistance);
      interpolate(x1,
                  support[x1] = f(x1, mb, cb),
                  x2,
                  support[x2] = f(x2, mb, cb),
                  support);
      logger.info(GENERATED_FOR, name, ohlcv);
    }
    else {
      Arrays.fill(resistance, Double.NaN);
      Arrays.fill(support, Double.NaN);
      logger.info("No triangles generated for: {}", ohlcv);
    }

    return Arrays.asList(new TimeSeries(name, dates, resistance),
                         new TimeSeries(name, dates, support));
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();
    final String[] dates = ohlcv.dates();

    final double[] resistance = new double[size];
    final double[] support = new double[size];
    Arrays.fill(resistance, Double.NaN);
    Arrays.fill(support, Double.NaN);

    // generate tops and bottoms
    final List<TimeSeries> tab = topsAndBottoms.generate(ohlcv);
    final double[] tops = tab.get(ZERO).values();
    final double[] bottoms = tab.get(ONE).values();

    // locate 2 pairs of tops and bottoms at a time
    int t1 = nextExtremum(tops, start - ONE);
    int b1 = nextExtremum(bottoms, start - ONE);
    int t2 = nextExtremum(tops, t1);
    int b2 = nextExtremum(bottoms, b1);

    while ((t2 > NOT_FOUND) && (b2 > NOT_FOUND)) {
      // compute gradients
      final double mt = gradient(t1, tops[t1], t2, tops[t2]);
      final double mb = gradient(b1, bottoms[b1], b2, bottoms[b2]);

      // 1. support and resistance need to intersect to qualify as a triangle,
      //    i.e. mt < mb
      // 2. sequence: either TBTB or BTBT
      if ((mt < mb) &&
          ((mt < ZERO) && (mb > ZERO)) &&
//          ((t1 < b2) || (b1 < t2)) &&
          isTriangle(t1, b1, t2, b2)) { // TODO find a better way to express conditions
        // compute intercepts
        final double ct = intercept(t2, tops[t2], mt);
        final double cb = intercept(b2, bottoms[b2], mb);

//        // locate intersection
//        // y = m1x + c1
//        // y = m2x + c2
//        // x = (c2 - c1) / (m1 - m2)
//        final double x = (cb - ct) / (mt - mb);
//        final double y = f(x, mb, cb);
//        logger.info("Intersection: ({}, {})", x, y);

        // determine start and end points first then interpolate just once
        final int x1 = Math.min(b1, t1);
        final int x2 = Math.max(b2, t2);

        interpolate(x1,
                    resistance[x1] = f(x1, mt, ct),
                    x2,
                    resistance[x2] = f(x2, mt, ct),
                    resistance);
        interpolate(x1,
                    support[x1] = f(x1, mb, cb),
                    x2,
                    support[x2] = f(x2, mb, cb),
                    support);
      }

      t2 = nextExtremum(tops, t1 = t2);
      b2 = nextExtremum(bottoms, b1 = b2);
    }

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(name, dates, resistance),
                         new TimeSeries(name, dates, support));
  }

  private static final boolean isTriangle(final int t1,
                                          final int b1,
                                          final int t2,
                                          final int b2) {
    // sequence: either TBTB or BTBT
    final boolean tbtb = (t1 < b1 && b1 < t2 && t2 < b2);
    final boolean btbt = (b1 < t1 && t1 < b2 && b2 < t2);
    return tbtb || btbt;
  }

}
