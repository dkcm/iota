/**
 * VWAPTest.java  v0.1  12 April 2018 10:43:53 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import static org.ikankechil.iota.indicators.TypicalPriceTest.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>VWAP</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class VWAPTest extends AbstractIndicatorTest {

  public VWAPTest() {
    super(0);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = VWAPTest.class;
  }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    return Arrays.asList(new TimeSeries("VWAP",
                                        series.dates(),
                                        vwap(series)));
  }

  static double[] vwap(final OHLCVTimeSeries series) {
    final double[] typical = typical(series);
    final long[] volumes = series.volumes();
    final double[] vwap = new double[typical.length];
    double ctpv = 0;
    double cv = 0;
    for (int i = 0; i < vwap.length; ++i) {
      ctpv += typical[i] * volumes[i];
      cv += volumes[i];
      vwap[i] = ctpv / cv;
    }
    return vwap;
  }

}
