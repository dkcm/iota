/**
 * SignalTimeSeries.java v0.1 6 January 2015 7:46:29 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota;

/**
 *
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.1
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
    signals[index] = signal;
  }

  public void set(final String date, final Signal signal, final int index) {
    // TODO null Signal allowed?
    date(date, index);
    signals[index] = signal;
  }

}
