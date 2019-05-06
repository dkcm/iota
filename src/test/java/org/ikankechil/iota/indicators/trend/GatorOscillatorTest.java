/**
 * GatorOscillatorTest.java  v0.1  5 May 2019 6:36:04 PM
 *
 * Copyright © 2019-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import static org.ikankechil.iota.indicators.trend.AlligatorTest.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.MedianPriceTest;
import org.junit.BeforeClass;

/**
 * JUnit test for {@code GatorOscillator}.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class GatorOscillatorTest extends AbstractIndicatorTest {

  private static final int JAWS  = 13;
  private static final int TEETH = 8;
  private static final int LIPS  = 5;

  public GatorOscillatorTest() {
    super(JAWS - 1 + TEETH);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = GatorOscillatorTest.class;
  }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    final double[] medians = MedianPriceTest.median(series);
    final double[] jaws = alligator(JAWS, TEETH, medians);
    final double[] teeth = alligator(TEETH, LIPS, medians);
    final double[] lips = alligator(LIPS, TEETH - LIPS, medians);

    final double[] uppers = new double[jaws.length];
    final double[] lowers = new double[jaws.length];
    for (int i = 0, j = teeth.length - jaws.length, k = lips.length - jaws.length; i < uppers.length; ++i, ++j, ++k) {
      uppers[i] = Math.abs(jaws[i] - teeth[j]);
      lowers[i] = -Math.abs(teeth[j] - lips[k]);
    }

    final String[] dates = Arrays.copyOfRange(series.dates(),
                                              lookback,
                                              series.size());
    return Arrays.asList(new TimeSeries("Gator Oscillator Upper",
                                        dates,
                                        uppers),
                         new TimeSeries("Gator Oscillator Lower",
                                        dates,
                                        lowers));
  }

}
