/**
 * TimeSeriesTest.java  v0.1  18 December 2014 12:13:50 am
 *
 * Copyright © 2014-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * JUnit test for <code>TimeSeries</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class TimeSeriesTest {

  private TimeSeries       timeSeries;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public final void instantiateWithNullName() {
    final int size = 2;
    timeSeries = new TimeSeries(null, size);

    assertEquals(null, timeSeries.toString());
  }

  @Test
  public final void cannotInstantiateWhenDatesAndValuesHaveDifferentLengths() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Different lengths: dates (0), values (1)");
    timeSeries = new TimeSeries(null, new String[0], new double[1]);
  }

  @Test
  public final void instantiateWithSize() {
    final int size = 2;
    timeSeries = new TimeSeries(null, size);

    assertEquals(size, timeSeries.size());
    assertEquals(size, timeSeries.dates().length);
    assertEquals(size, timeSeries.values().length);
  }

  @Test
  public final void setDate() {
    timeSeries = new TimeSeries(null, new String[1], new double[1]);
    final String expected = "expected";
    timeSeries.date(expected, 0);

    assertEquals(expected, timeSeries.date(0));
  }

  @Test
  public final void setValue() {
    timeSeries = new TimeSeries(null, new String[1], new double[1]);
    final double expected = Double.MAX_VALUE;
    timeSeries.value(expected, 0);

    assertEquals(null, expected, timeSeries.value(0), 0);
  }

  @Test
  public final void setDateAndValue() {
    timeSeries = new TimeSeries(null, new String[1], new double[1]);
    final String expectedDate = "expected";
    final double expectedValue = Double.MAX_VALUE;
    timeSeries.set(expectedDate, expectedValue, 0);

    assertEquals(expectedDate, timeSeries.date(0));
    assertEquals(null, expectedValue, timeSeries.value(0), 0);
  }

  @Test
  public final void cannotSetDateAndValueBeyondAllocatedSize() {
    final int size = 2;
    timeSeries = new TimeSeries(null, size);

    thrown.expect(ArrayIndexOutOfBoundsException.class);
    thrown.expectMessage(String.valueOf(size));
    timeSeries.set(null, 0, size);
  }

  @Test
  public final void cannotSetDateBeyondAllocatedSize() {
    final int size = 2;
    timeSeries = new TimeSeries(null, size);

    thrown.expect(ArrayIndexOutOfBoundsException.class);
    thrown.expectMessage(String.valueOf(size));
    timeSeries.date(null, size);
  }

  @Test
  public final void cannotSetValueBeyondAllocatedSize() {
    final int size = 2;
    timeSeries = new TimeSeries(null, size);

    thrown.expect(ArrayIndexOutOfBoundsException.class);
    thrown.expectMessage(String.valueOf(size));
    timeSeries.value(0, size);
  }

  @Test
  public final void cannotGetDateBeyondAllocatedSize() {
    final int size = 2;
    timeSeries = new TimeSeries(null, size);

    thrown.expect(ArrayIndexOutOfBoundsException.class);
    thrown.expectMessage(String.valueOf(size));
    timeSeries.date(size);
  }

  @Test
  public final void cannotGetValueBeyondAllocatedSize() {
    final int size = 2;
    timeSeries = new TimeSeries(null, size);

    thrown.expect(ArrayIndexOutOfBoundsException.class);
    thrown.expectMessage(String.valueOf(size));
    timeSeries.value(size);
  }

  @Test
  public final void noCopiesMade() {
    final String[] dates = new String[1];
    final double[] values = new double[1];
    timeSeries = new TimeSeries(null, dates, values);

    assertSame(dates, timeSeries.dates());
    assertSame(values, timeSeries.values());
  }

}
