/**
 * RMFTest.java  v0.1  23 April 2018 11:16:09 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import static org.junit.Assert.*;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.momentum.RMF.Medians;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * JUnit test for <code>RMF</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class RMFTest extends AbstractIndicatorTest {

  private static final double DELTA = 0;

  public RMFTest() {
    super(15);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = RMFTest.class;
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

}
