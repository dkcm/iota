/**
 * HeikinAshiTimeSeriesTest.java  v0.1  13 January 2017 8:43:54 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * JUnit test for <code>HeikinAshiTimeSeries</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class HeikinAshiTimeSeriesTest {

  private static final double DELTA = 1e-13;

  @Test
  public void heikinAshiConversion() {
    final int size = 5;
    final OHLCVTimeSeries ohlcv = new OHLCVTimeSeries(null, size);
    int i = -1;
    ohlcv.set("20110103", 147.21, 148.20, 147.14, 147.48, ++i, i);
    ohlcv.set("20110104", 147.56, 148.22, 146.64, 147.64, ++i, i);
    ohlcv.set("20110105", 147.34, 147.48, 146.73, 147.05, ++i, i);
    ohlcv.set("20110106", 147.13, 148.79, 146.82, 148.66, ++i, i);
    ohlcv.set("20110107", 148.79, 148.86, 146.94, 147.93, ++i, i);

    // convert to Heikin-Ashi candlesticks
    final OHLCVTimeSeries ha = new HeikinAshiTimeSeries(ohlcv);

    // compare date, OHLCV
    final double[][] expecteds = new double[5][];
    i = -1;
    expecteds[++i] = new double[] { 147.21, 148.2, 147.14, 147.5075, i };
    expecteds[++i] = new double[] { 147.35875, 148.22, 146.64, 147.515, i };
    expecteds[++i] = new double[] { 147.436875, 147.48, 146.73, 147.15, i };
    expecteds[++i] = new double[] { 147.2934375, 148.79, 146.82, 147.85, i };
    expecteds[++i] = new double[] { 147.57171875, 148.86, 146.94, 148.13, i };

    i = -1;
    while (++i < expecteds.length) {
      assertArrayEquals(expecteds[i],
                        new double[] { ha.open(i), ha.high(i), ha.low(i), ha.close(i), ha.volume(i) },
                        DELTA);
      assertEquals(ohlcv.date(i), ha.date(i));
    }
  }

}
