/**
 * SignalTimeSeries.java	v0.2  6 January 2015 7:46:29 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota;

/**
 * A time-series of trading signals.
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class SignalTimeSeries extends TimeSeries {

  private final Signal[] signals;

  public SignalTimeSeries(final String name, final int size) {
    super(name, size);

    signals = new Signal[size];
  }

  public Signal[] signals() {
    return signals;
  }

  public Signal signal(final int index) {
    return signals[index];
  }

  public void signal(final Signal signal, final int index) {
    if (signal == null) {
      throw new NullPointerException("Null signal at index: " + index);
    }
    signals[index] = signal;
  }

  public void set(final String date, final Signal signal, final int index) {
    if (signal == null) {
      throw new NullPointerException("Null signal at date: " + date + ", index: " + index);
    }
    date(date, index);
    signals[index] = signal;
  }

}
