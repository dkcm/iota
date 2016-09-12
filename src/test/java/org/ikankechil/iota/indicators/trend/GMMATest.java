/**
 * GMMATest.java  v0.2  28 November 2015 10:45:25 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import static org.ikankechil.iota.indicators.trend.EMATest.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * JUnit test for <code>GMMA</code>.
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class GMMATest extends AbstractIndicatorTest {

  private static final String GMMA    = "GMMA";

  private static final int[]  PERIODS = {  3,  5,  8, 10, 12, 15,
                                          30, 35, 40, 45, 50, 60 };

  public GMMATest() {
    super(PERIODS[PERIODS.length - 1] - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = GMMATest.class;
  }

  @Ignore
  @Override
  @Test
  public void cannotInstantiateWithNegativePeriod() {
    // not supported
  }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    final int size = series.size();
    final String[] dates = Arrays.copyOfRange(series.dates(), lookback, size);
    final double[] closes = series.closes();

    final List<TimeSeries> gmma = new ArrayList<>(PERIODS.length);
    for (final int period : PERIODS) {
      final double[] ema = ema(period, period - 1, closes);
      gmma.add(new TimeSeries(GMMA + period,
                              dates,
                              Arrays.copyOfRange(ema, ema.length - dates.length, ema.length)));
      // ema.length - dates.length = lookback - period + 1
    }

    return gmma;
  }

}
