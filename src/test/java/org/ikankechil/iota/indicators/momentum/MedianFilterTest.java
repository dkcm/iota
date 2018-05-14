/**
 * MedianFilterTest.java  v0.1  14 May 2018 9:00:24 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import static org.junit.Assert.*;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.momentum.MedianFilter.Medians;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * JUnit test for <code>MedianFilter</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MedianFilterTest extends AbstractIndicatorTest {

  private static final double DELTA = 0;

  public MedianFilterTest() {
    super(4);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = MedianFilterTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Test
  public void even() throws Exception {
    final double[] values = { 0.0, 1.0, 2.0, 3.0, 4.0, 5.0 };
    assertEquals(2.5, Medians.EVEN.compute(values), DELTA);
  }

  @Test
  public void odd() throws Exception {
    final double[] values = { 0.0, 1.0, 2.0, 3.0, 4.0 };
    assertEquals(2.0, Medians.ODD.compute(values), DELTA);
  }

  @Test
  public void toMedianEven() throws Exception {
    for (final int even : new int[] { 0, 2, 4, 6, 8 }) {
      assertEquals(Medians.EVEN, Medians.toMedian(even));
    }
  }

  @Test
  public void toMedianOdd() throws Exception {
    for (final int odd : new int[] { 1, 3, 5, 7, 9 }) {
      assertEquals(Medians.ODD, Medians.toMedian(odd));
    }
  }

}
