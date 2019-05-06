/**
 * AlligatorTest.java  v0.1  5 May 2019 6:35:32 PM
 *
 * Copyright © 2019-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import static org.ikankechil.iota.indicators.trend.SmoothedMATest.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.MedianPriceTest;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * JUnit test for {@code Alligator}.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class AlligatorTest extends AbstractIndicatorTest {

  private static final int JAWS  = 13;
  private static final int TEETH = 8;
  private static final int LIPS  = 5;

  public AlligatorTest() {
    super(JAWS - 1 + TEETH);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = AlligatorTest.class;
  }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    final String[] dates = series.dates();
    final int size = series.size();
    final double[] medians = MedianPriceTest.median(series);

    return Arrays.asList(new TimeSeries("Alligator Jaws",
                                        Arrays.copyOfRange(dates,
                                                           lookback,
                                                           size),
                                        alligator(JAWS, TEETH, medians)),
                         new TimeSeries("Alligator Teeth",
                                        Arrays.copyOfRange(dates,
                                                           TEETH - 1 + LIPS,
                                                           size),
                                        alligator(TEETH, LIPS, medians)),
                         new TimeSeries("Alligator Lips",
                                        Arrays.copyOfRange(dates,
                                                           LIPS - 1 + TEETH - LIPS,
                                                           size),
                                        alligator(LIPS, TEETH - LIPS, medians)));
  }

  public static final double[] alligator(final int period,
                                         final int shift,
                                         final double... values) {
    final double[] sma = smoothedMovingAverage(period, values);
    return Arrays.copyOf(sma, sma.length - shift);
  }

  @Override
  @Ignore
  @Test
  public void shorterThanOHLCVByLookback() { /* do nothing */ }

  @Override
  @Ignore
  @Test
  public void datesStartFromLookback() { /* do nothing */ }

}
