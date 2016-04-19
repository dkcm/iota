/**
 * OHLCVTimeSeries.java	v0.2	19 November 2014 9:41:36 pm
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota;

/**
 * A time-series representation of price and volume.
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class OHLCVTimeSeries extends TimeSeries {

  private final double[] opens;
  private final double[] highs;
  private final double[] lows;
  private final long[]   volumes;

  public OHLCVTimeSeries(final String name, final int size) {
    super(name, size);

    opens = new double[size];
    highs = new double[size];
    lows = new double[size];
    // closes are emulated by values in the superclass

    volumes = new long[size];
  }

  // Bulk gets

  public double[] opens() {
    return opens;
  }

  public double[] highs() {
    return highs;
  }

  public double[] lows() {
    return lows;
  }

  public double[] closes() {
    return values();
  }

  public long[] volumes() {
    return volumes;
  }

  // Gets

  public double open(final int index) {
    return opens[index];
  }

  public double high(final int index) {
    return highs[index];
  }

  public double low(final int index) {
    return lows[index];
  }

  public double close(final int index) {
    return value(index);
  }

  public long volume(final int index) {
    return volumes[index];
  }

  // Sets

  public void open(final double value, final int index) {
    opens[index] = value;
  }

  public void high(final double value, final int index) {
    highs[index] = value;
  }

  public void low(final double value, final int index) {
    lows[index] = value;
  }

  public void close(final double value, final int index) {
    value(value, index);
  }

  public void volume(final long value, final int index) {
    volumes[index] = value;
  }

  public void set(final String date,
                  final double open,
                  final double high,
                  final double low,
                  final double close,
                  final long volume,
                  final int index) {
    set(date, close, index); // close emulated by value in superclass

    opens[index] = open;
    highs[index] = high;
    lows[index] = low;

    volumes[index] = volume;
  }

}
