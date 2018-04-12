/**
 * MVWAPTest.java  v0.1  12 April 2018 10:48:55 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import static org.ikankechil.iota.indicators.trend.SMATest.*;
import static org.ikankechil.iota.indicators.volume.VWAPTest.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>MVWAP</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MVWAPTest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 10;

  public MVWAPTest() {
    super(DEFAULT_PERIOD - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = MVWAPTest.class;
  }

  @Override
  public Indicator newInstance() throws ReflectiveOperationException {
    return newInstance(DEFAULT_PERIOD);
  }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    return Arrays.asList(new TimeSeries("MVWAP",
                                        Arrays.copyOfRange(series.dates(),
                                                           lookback,
                                                           series.size()),
                                        mvwap(DEFAULT_PERIOD, series)));
  }

  private static double[] mvwap(final int period, final OHLCVTimeSeries series) {
    final double[] vwap = vwap(series);
    final double[] mvwap = sma(period, vwap);
    return mvwap;
  }

}
