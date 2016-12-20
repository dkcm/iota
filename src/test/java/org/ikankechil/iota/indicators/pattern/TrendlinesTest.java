/**
 * TrendlinesTest.java  v0.3  27 January 2016 12:10:35 PM
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.TrendlineTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.indicators.pattern.Trendlines.Trendline;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * JUnit test for <code>Trendlines</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class TrendlinesTest extends AbstractIndicatorTest {

  private Trendlines                   trendlines;

  private static final List<Trendline> DOWN_TRENDLINES;
  private static final List<Trendline> UP_TRENDLINES;

  private static final int             DEFAULT_AWAY_POINTS = 5;
  private static final int             DEFAULT_LOOKBACK    = 0;

  static {
    DOWN_TRENDLINES = Arrays.asList(new Trendline(82, 173.54, 125, 169.401250),
                                    new Trendline(139, 185.63, 165, 166.731250),
                                    new Trendline(198, 190.53, 270, 188.6972727),
                                    new Trendline(272, 194.81, 301, 193.11833333),
                                    new Trendline(315, 210.69, 428, 200.2226316),
                                    new Trendline(444, 211.79, 550, 207.0641667),
                                    new Trendline(552, 215.90, 894, 190.6049057),
                                    new Trendline(896, 196.40, 1005, 192.38421053));

    UP_TRENDLINES = Arrays.asList(new Trendline(1, 146.64, 48, 160.303571),
                                  new Trendline(50, 151.71, 111, 177.281200),
                                  new Trendline(113, 161.52, 150, 181.802727),
                                  new Trendline(159, 157.13, 469, 190.816667),
                                  new Trendline(471, 184.78, 575, 198.8546667),
                                  new Trendline(577, 187.68, 623, 219.765000),
                                  new Trendline(702, 172.57, 741, 184.221250),
                                  new Trendline(743, 172.73, 775, 192.5166667),
                                  new Trendline(777, 172.19, 801, 187.1970588),
                                  new Trendline(803, 182.21, 853, 188.5883784),
                                  new Trendline(875, 179.27, 955, 190.7633333));
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
    trendlines = new Trendlines(DEFAULT_AWAY_POINTS);
    return trendlines;
  }

  @Override
  @Test
  public void indicatorValues() {
    super.indicatorValues();
    assertEquals(2, actuals.size());
    downTrendlines();
    upTrendlines();
  }

  private void downTrendlines() {
    final List<Trendline> downTrendlines = ((TrendlineTimeSeries) actuals.get(0)).trendlines();
    for (int i = 0; i < DOWN_TRENDLINES.size(); ++i) {
      assertEquals(DOWN_TRENDLINES.get(i).toString(), downTrendlines.get(i).toString());
    }
    assertEquals(DOWN_TRENDLINES.size(), downTrendlines.size());
  }

  private void upTrendlines() {
    final List<Trendline> upTrendlines = ((TrendlineTimeSeries) actuals.get(1)).trendlines();
    for (int i = 0; i < UP_TRENDLINES.size(); ++i) {
      assertEquals(UP_TRENDLINES.get(i).toString(), upTrendlines.get(i).toString());
    }
    assertEquals(UP_TRENDLINES.size(), upTrendlines.size());
  }

}
