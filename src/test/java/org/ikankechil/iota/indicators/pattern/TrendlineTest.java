/**
 * TrendlineTest.java  v0.2  22 November 2016 10:51:59 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import static org.junit.Assert.*;

import org.ikankechil.iota.indicators.pattern.Trendlines.Trendline;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test for <code>Trendlines.Trendline</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class TrendlineTest {

  private Trendline           trendline;

  private int                 x1;
  private double              y1;
  private int                 x2;
  private double              y2;
  private double              gradient;
  private double              yIntercept;

  private static final double DELTA = 1e-12;
  private static final double BASE  = 100.0;

  public TrendlineTest() { /* do nothing */ }

  @Before
  public void setUp() throws Exception {
    x1 = (int) random();
    y1 = random();
    x2 = (int) random();
    y2 = random();
    gradient = (y2 - y1) / (x2 - x1);
    yIntercept = y2 - (gradient * x2);

    trendline = new Trendline(x1, y1, x2, y2);
  }

  @After
  public void tearDown() throws Exception {
    x1 = Integer.MIN_VALUE;
    y1 = Double.NaN;
    x2 = Integer.MIN_VALUE;
    y2 = Double.NaN;
    gradient = Double.NaN;
    yIntercept = Double.NaN;

    trendline = null;
  }

  @Test
  public void x1y1() {
    assertEquals(x1, trendline.x1());
    assertEquals(y1, trendline.y1(), DELTA);
    assertFalse(trendline.isConfirmed());
    assertFalse(trendline.isBroken());
  }

  @Test
  public void changeX1() {
    x1 = (int) random();
    trendline.x1y1(x1, y1);

    assertEquals(x1, trendline.x1());
    assertEquals(y1, trendline.y1(), DELTA);
    assertFalse(trendline.isConfirmed());
    assertFalse(trendline.isBroken());
  }

  @Test
  public void changeY1() {
    y1 = random();
    trendline.x1y1(x1, y1);

    assertEquals(x1, trendline.x1());
    assertEquals(y1, trendline.y1(), DELTA);
    assertFalse(trendline.isConfirmed());
    assertFalse(trendline.isBroken());
  }

  @Test
  public void changeX1Y1() {
    x1 = (int) random();
    y1 = random();
    trendline.x1y1(x1, y1);

    assertEquals(x1, trendline.x1());
    assertEquals(y1, trendline.y1(), DELTA);
    assertFalse(trendline.isConfirmed());
    assertFalse(trendline.isBroken());
  }

  @Test
  public void x2y2() {
    assertEquals(x2, trendline.x2());
    assertEquals(y2, trendline.y2(), DELTA);
    assertFalse(trendline.isConfirmed());
    assertFalse(trendline.isBroken());
  }

  @Test
  public void trendConfirmation() {
    assertFalse(trendline.isConfirmed());

    x2 = (int) random();
    y2 = random();
    trendline.x2y2(x2, y2); // confirm trend

    assertEquals(x2, trendline.x2());
    assertEquals(y2, trendline.y2(), DELTA);
    assertTrue(trendline.isConfirmed());
    assertFalse(trendline.isBroken());
  }

  @Test
  public void trendBreak() {
    assertFalse(trendline.isBroken());
    trendline.broken(true);
    assertTrue(trendline.isBroken());
    trendline.broken(false);
    assertFalse(trendline.isBroken());
  }

  @Test
  public void changeX2() {
    x2 = (int) random();
    trendline.x2y2(x2, y2);

    assertEquals(x2, trendline.x2());
    assertEquals(y2, trendline.y2(), DELTA);
    assertTrue(trendline.isConfirmed());
    assertFalse(trendline.isBroken());
  }

  @Test
  public void changeY2() {
    y2 = random();
    trendline.x2y2(x2, y2);

    assertEquals(x2, trendline.x2());
    assertEquals(y2, trendline.y2(), DELTA);
    assertTrue(trendline.isConfirmed());
    assertFalse(trendline.isBroken());
  }

  @Test
  public void noChangeInX1Y1() {
    trendline.x1y1(x1, y1);

    assertEquals(x1, trendline.x1());
    assertEquals(y1, trendline.y1(), DELTA);
    assertFalse(trendline.isConfirmed());
    assertFalse(trendline.isBroken());
    assertEquals(gradient, trendline.m(), DELTA);
    assertEquals(yIntercept, trendline.c(), DELTA);
  }

  @Test
  public void noChangeInX2Y2() {
    trendline.x2y2(x2, y2);

    assertEquals(x2, trendline.x2());
    assertEquals(y2, trendline.y2(), DELTA);
    assertFalse(trendline.isConfirmed());
    assertFalse(trendline.isBroken());
    assertEquals(gradient, trendline.m(), DELTA);
    assertEquals(yIntercept, trendline.c(), DELTA);
  }

  @Test
  public void gradient() {
    assertEquals(gradient, trendline.m(), DELTA);
  }

  @Test
  public void yIntercept() {
    assertEquals(yIntercept, trendline.c(), DELTA);
  }

  @Test
  public void recomputeGradientAndYIntercept() {
    x1 = (int) random();
    y1 = random();
    trendline.x1y1(x1, y1);

    gradient = (y2 - y1) / (x2 - x1);
    assertEquals(gradient, trendline.m(), DELTA);

    yIntercept = y2 - (gradient * x2);
    assertEquals(yIntercept, trendline.c(), DELTA);
  }

  @Test
  public void f() {
    final double x = random();
    final double fx = (gradient * x) + yIntercept;
    assertEquals(fx, trendline.f(x), DELTA);
  }

  @Test
  public void stringRepresentation() {
    assertEquals(String.format("Trendline (%d, %f) -> (%d, %f): y = %fx + %f",
                               x1,
                               y1,
                               x2,
                               y2,
                               gradient,
                               yIntercept),
                 trendline.toString());
  }

  private static final double random() {
    return Math.random() * BASE;
  }

}
