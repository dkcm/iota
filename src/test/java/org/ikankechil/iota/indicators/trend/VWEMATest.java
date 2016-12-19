/**
 * VWEMATest.java  0.1  19 December 2016 2:55:45 PM
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
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>VWEMA</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class VWEMATest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 6;

  public VWEMATest() {
    super(DEFAULT_PERIOD - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = VWEMATest.class;
  }

  @Override
  public Indicator newInstance() throws ReflectiveOperationException {
    return newInstance(DEFAULT_PERIOD);
  }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    return Arrays.asList(new TimeSeries("VWEMA",
                                        Arrays.copyOfRange(series.dates(),
                                                           lookback,
                                                           series.size()),
                                        vwema(DEFAULT_PERIOD, series)));
  }

  static final double[] vwema(final int period, final OHLCVTimeSeries series) {
    final double[] volumes = new double[series.size()];
    final double[] priceVolumes = new double[series.size()];
    for (int i = 0; i < volumes.length; ++i) {
      volumes[i] = series.volume(i);
      priceVolumes[i] = series.volume(i) * series.close(i);
    }

    // VWEMA = EMA(Price * Volume) / EMA(Volume)
    final int emaLookback = period - 1;
    final double[] emaVolumes = EMATest.ema(period, emaLookback, volumes);
    final double[] vwema = EMATest.ema(period, emaLookback, priceVolumes);
    for (int i = 0; i < vwema.length; ++i) {
      vwema[i] /= emaVolumes[i];
    }

    return vwema;
  }

}
