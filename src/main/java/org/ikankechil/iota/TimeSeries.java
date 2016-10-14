/**
 * TimeSeries.java	v0.2  26 November 2014 2:50:28 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota;

/**
 * A time-series of <code>double</code> values.
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class TimeSeries { // TODO create Builder / factory method?

  private final String   name;
  private final String[] dates;
  private final double[] values;

  public TimeSeries(final String name, final int size) {
    this(name, new String[size], new double[size]);
  }

  /**
   * @param name
   * @param dates
   * @param values
   * @throws IllegalArgumentException
   *           if <code>dates</code> and <code>values</code> are of unequal
   *           lengths
   */
  public TimeSeries(final String name, final String[] dates, final double[] values) {
    if (dates.length != values.length) {
      throw new IllegalArgumentException(String.format("Different lengths: dates (%d), values (%d)",
                                                       dates.length,
                                                       values.length));
    }

    this.name = name;
    this.dates = dates;
    this.values = values;
  }

  public String[] dates() {
    return dates;
  }

  public double[] values() {
    return values;
  }

  /**
   * Gets date at <code>index</code>.
   *
   * @param index
   * @return
   */
  public String date(final int index) {
    return dates[index];
  }

  /**
   * Gets value at <code>index</code>.
   *
   * @param index
   * @return
   */
  public double value(final int index) {
    return values[index];
  }

  /**
   * Sets date at <code>index</code>.
   *
   * @param value
   * @param index
   */
  public void date(final String value, final int index) {
    dates[index] = value;
  }

  /**
   * Sets value at <code>index</code>.
   *
   * @param value
   * @param index
   */
  public void value(final double value, final int index) {
    values[index] = value;
  }

  public void set(final String date,
                  final double value,
                  final int index) {
    dates[index] = date;
    values[index] = value;
  }

  @Override
  public String toString() {
    return name;
  }

  public int size() {
    return dates.length;
  }

}
