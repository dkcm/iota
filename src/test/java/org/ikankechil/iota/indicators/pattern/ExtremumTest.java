/**
 * ExtremumTest.java  0.1  21 December 2016 7:03:49 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import static org.junit.Assert.*;

import org.ikankechil.iota.indicators.pattern.Extrema.Extremum;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test for <code>Extrema.Extremum</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ExtremumTest {

  private Extremum            extremum;

  private int                 x1;
  private double              y1;

  private static final double DELTA = 1e-12;
  private static final double BASE  = 100.0;

  @Before
  public void setUp() throws Exception {
    x1 = (int) random();
    y1 = random();

    extremum = new Extremum(x1, y1);
  }

  @After
  public void tearDown() throws Exception {
    x1 = Integer.MIN_VALUE;
    y1 = Double.NaN;

    extremum = null;
  }

  @Test
  public void x1y1() {
    assertEquals(x1, extremum.x());
    assertEquals(y1, extremum.y(), DELTA);
  }

  @Test
  public void stringRepresentation() {
    assertEquals(String.format("Extremum (%d, %f)",
                               x1,
                               y1),
                 extremum.toString());
  }

  private static final double random() {
    return Math.random() * BASE;
  }

}
