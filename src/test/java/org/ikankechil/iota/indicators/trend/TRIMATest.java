/**
 * TRIMATest.java  v0.2  17 October 2016 7:06:27 pm
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

/**
 * JUnit test for <code>TRIMA</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class TRIMATest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 30;

  public TRIMATest() {
    super(DEFAULT_PERIOD);
    // lookback dependent on period
    // even: period
    // odd: period - 1
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = TRIMATest.class;
  }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    return Arrays.asList(new TimeSeries("TRIMA",
                                        Arrays.copyOfRange(series.dates(),
                                                           lookback,
                                                           series.size()),
                                        trima(DEFAULT_PERIOD, series.closes())));
  }

  static final double[] trima(final int period, final double... values) {
    final int smaPeriod = (int) Math.ceil((double) (period + 1) / 2);
    final double[] trima = SMATest.sma(smaPeriod, SMATest.sma(smaPeriod, values));
    return trima;
  }

}
