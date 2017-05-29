/**
 * TrendlineTimeSeriesTest.java  0.1  17 December 2016 12:22:49 AM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.indicators.pattern.Trendlines.Trendline;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * JUnit test for <code>TrendlineTimeSeries</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class TrendlineTimeSeriesTest {

  private TrendlineTimeSeries trendlineTimeSeries;

  @Rule
  public ExpectedException    thrown = ExpectedException.none();

  @After
  public void tearDown() {
    trendlineTimeSeries = null;
  }

  @Test
  public final void cannotInstantiateWithNullTrendlines() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("Trendlines cannot be null");
    trendlineTimeSeries = new TrendlineTimeSeries(null, new String[1], new double[1], null);
  }

  @Test
  public void copyTrendlines() {
    final List<Trendline> expecteds = Arrays.asList(new Trendline(0, 1, 2, 3));
    trendlineTimeSeries = new TrendlineTimeSeries(null, new String[1], new double[1], expecteds);

    final List<Trendline> actuals = trendlineTimeSeries.trendlines();

    assertEquals(expecteds, actuals);
    assertNotSame(expecteds, actuals); // copy made
  }

}
