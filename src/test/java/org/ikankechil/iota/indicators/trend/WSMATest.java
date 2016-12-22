/**
 * WSMATest.java  v0.1  30 November 2016 10:03:40 pm
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * JUnit test for <code>WSMA</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class WSMATest extends AbstractIndicatorTest {

  private static final int    DEFAULT_PERIOD        = 5;
  private static final double DEFAULT_VOLUME_FACTOR = 0.4;

  public WSMATest() {
    super(3 * (DEFAULT_PERIOD - 1));
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = WSMATest.class;
  }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    return Arrays.asList(new TimeSeries("WSMA",
                                        Arrays.copyOfRange(series.dates(),
                                                           lookback,
                                                           series.size()),
                                        wsma(DEFAULT_PERIOD, DEFAULT_VOLUME_FACTOR, series.closes())));
  }

  static final double[] wsma(final int period, final double volumeFactor, final double... values) {
    final double[] gd = gd(period, volumeFactor, values);
    final double[] gd2 = gd(period, volumeFactor, gd);
    final double[] wsma = gd(period, volumeFactor, gd2);
    return wsma;
  }

  private static double[] gd(final int period, final double volumeFactor, final double... values) {
    final double[] wma = WMATest.wma(period, values);
    final double[] sma = SMATest.sma(period, values);

    final double[] gd = new double[sma.length];
    for (int i = 0; i < gd.length; ++i) {
      gd[i] = (1 + volumeFactor) * wma[i] - (volumeFactor * sma[i]);
    }
    return gd;
  }

  @Test
  public void smaEquivalence() {
//    final SMA sma = new SMA(DEFAULT_PERIOD);
//    sma.generate(OHL)
  }

}
