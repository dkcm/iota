/**
 * TrendlinesTest.java  v0.5  27 January 2016 12:10:35 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import static org.ikankechil.iota.indicators.pattern.Trendlines.TrendSlopes.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.TrendlineTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.indicators.pattern.Trendlines.TrendSlopes;
import org.ikankechil.iota.indicators.pattern.Trendlines.Trendline;
import org.ikankechil.iota.indicators.pattern.Trendlines.Trends;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * JUnit test for <code>Trendlines</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.5
 */
public class TrendlinesTest extends AbstractIndicatorTest {

  private static final List<Trendline> UPPER_TRENDLINES;
  private static final List<Trendline> LOWER_TRENDLINES;

  private static final int             AWAY_POINTS        = 5;
  private static final int             BREAKOUT_THRESHOLD = 3;
  private static final double          RUNAWAY_THRESHOLD  = 12.5;
  private static final int             DEFAULT_LOOKBACK   = 0;

  private static final String[]        TRENDS             = { "RESISTANCE", "SUPPORT" };
  private static final String[]        SLOPES             = { "UP", "DOWN", "FLAT" };

  static {
    UPPER_TRENDLINES = Arrays.asList(new Trendline(82, 173.54, 125, 169.401250),
                                     new Trendline(139, 185.63, 165, 166.731250),
                                     new Trendline(198, 190.53, 270, 188.6972727),
                                     new Trendline(272, 194.81, 301, 193.11833333),
                                     new Trendline(315, 210.69, 428, 200.2226316),
                                     new Trendline(444, 211.79, 550, 207.0641667),
                                     new Trendline(552, 215.90, 894, 190.6049057),
                                     new Trendline(896, 196.40, 962, 193.9684211),        // unbroken
                                     new Trendline(964, 165.59, 1005, 163.595909090909)); // unbroken
    for (int i = 0; i < UPPER_TRENDLINES.size() - 2; ++i) { // last 2 unbroken
      UPPER_TRENDLINES.get(i).broken(true);
    }

    LOWER_TRENDLINES = Arrays.asList(new Trendline(1, 146.64, 48, 160.303571),
                                     new Trendline(50, 151.71, 111, 177.281200),
                                     new Trendline(113, 161.52, 150, 181.802727),
                                     new Trendline(159, 157.13, 317, 174.2993333),        // unbroken
                                     new Trendline(357, 187.0, 382, 189.6388889),
                                     new Trendline(384, 181.85, 455, 204.8337143),
                                     new Trendline(471, 184.78, 575, 198.8546667),
                                     new Trendline(577, 187.68, 623, 219.765000),
                                     new Trendline(702, 172.57, 741, 184.221250),
                                     new Trendline(743, 172.73, 775, 192.5166667),
                                     new Trendline(777, 172.19, 801, 187.1970588),
                                     new Trendline(803, 182.21, 853, 188.5883784),
                                     new Trendline(875, 179.27, 955, 190.7633333));
    for (final Trendline trendline : LOWER_TRENDLINES) {
      trendline.broken(true);
    }
    LOWER_TRENDLINES.get(3).broken(false);
  }

  public TrendlinesTest() {
    super(DEFAULT_LOOKBACK);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = TrendlinesTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance() {
    return new Trendlines(AWAY_POINTS, BREAKOUT_THRESHOLD, RUNAWAY_THRESHOLD);
  }

  @SuppressWarnings("unused")
  @Test(expected=NullPointerException.class)
  public void cannotInstantiateWithNullUpperTrendSlope() {
    new Trendlines(AWAY_POINTS, BREAKOUT_THRESHOLD, RUNAWAY_THRESHOLD, null, TrendSlopes.UP);
  }

  @SuppressWarnings("unused")
  @Test(expected=NullPointerException.class)
  public void cannotInstantiateWithNullLowerTrendSlope() {
    new Trendlines(AWAY_POINTS, BREAKOUT_THRESHOLD, RUNAWAY_THRESHOLD, TrendSlopes.DOWN, null);
  }

  @Override
  @Test
  public void indicatorValues() {
    // validate values
    super.indicatorValues();

    // validate actual trendlines
    assertEquals(2, actuals.size());
    trendlines(UPPER_TRENDLINES, 0);
    trendlines(LOWER_TRENDLINES, 1);
  }

  private void trendlines(final List<Trendline> expectedTrendlines, final int actualIndex) {
    final List<Trendline> actualTrendlines = ((TrendlineTimeSeries) actuals.get(actualIndex)).trendlines();
    for (int i = 0; i < expectedTrendlines.size(); ++i) {
      final Trendline expectedTrendline = expectedTrendlines.get(i);
      final Trendline actualTrendline = actualTrendlines.get(i);

      final String expected = expectedTrendline.toString();
      assertEquals(expected, actualTrendline.toString());
      assertEquals(expected, expectedTrendline.isBroken(), actualTrendline.isBroken());
    }
    assertEquals(expectedTrendlines.size(), actualTrendlines.size());
  }

  @Test
  public void validTrends() {
    for (final String trend : TRENDS) {
      assertNotNull(Trends.valueOf(trend));
    }
    assertEquals(TRENDS.length, Trends.values().length);
  }

  @Test
  public void validTrendSlopes() {
    for (final String slope : SLOPES) {
      assertNotNull(TrendSlopes.valueOf(slope));
    }
    assertEquals(SLOPES.length, TrendSlopes.values().length);
  }

  @Test
  public void upTrendSlope() throws Exception {
    assertFalse(UP.isRightDirection(Double.NEGATIVE_INFINITY));
    assertFalse(UP.isRightDirection(-Double.MAX_VALUE));
    assertFalse(UP.isRightDirection(0));
    assertTrue(UP.isRightDirection(Double.MIN_VALUE));
    assertTrue(UP.isRightDirection(Double.MAX_VALUE));
    assertFalse(UP.isRightDirection(Double.POSITIVE_INFINITY));
  }

  @Test
  public void downTrendSlope() throws Exception {
    assertFalse(DOWN.isRightDirection(Double.NEGATIVE_INFINITY));
    assertTrue(DOWN.isRightDirection(-Double.MAX_VALUE));
    assertTrue(DOWN.isRightDirection(-Double.MIN_VALUE));
    assertFalse(DOWN.isRightDirection(0));
    assertFalse(DOWN.isRightDirection(Double.MAX_VALUE));
    assertFalse(DOWN.isRightDirection(Double.POSITIVE_INFINITY));
  }

  @Test
  public void flatTrendSlope() throws Exception {
    assertFalse(FLAT.isRightDirection(Double.NEGATIVE_INFINITY));
    assertFalse(FLAT.isRightDirection(-Double.MAX_VALUE));
    assertFalse(FLAT.isRightDirection(-TrendlineTest.PRACTICALLY_HORIZONTAL * 2));
    assertTrue(FLAT.isRightDirection(-TrendlineTest.PRACTICALLY_HORIZONTAL));
    assertTrue(FLAT.isRightDirection(0));
    assertTrue(FLAT.isRightDirection(TrendlineTest.PRACTICALLY_HORIZONTAL));
    assertFalse(FLAT.isRightDirection(TrendlineTest.PRACTICALLY_HORIZONTAL * 2));
    assertFalse(FLAT.isRightDirection(Double.MAX_VALUE));
    assertFalse(FLAT.isRightDirection(Double.POSITIVE_INFINITY));
  }

}
