/**
 * SignalTimeSeries.java  v0.3  6 January 2015 7:46:29 PM
 *
 * Copyright © 2015-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota;

import java.util.ArrayList;
import java.util.List;

/**
 * A time-series of trading signals.
 *
 * @author Daniel Kuan
 * @version 0.3
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

  public static final SignalTimeSeries condenseSignals(final SignalTimeSeries sparseSignals) {
    return condenseSignals(sparseSignals, sparseSignals.toString());
  }

  public static final SignalTimeSeries condenseSignals(final SignalTimeSeries sparseSignals, final String seriesName) {
    final List<Integer> sparseSignalIndices = new ArrayList<>(sparseSignals.size());
    for (int i = 0; i < sparseSignals.size(); ++i) {
      final Signal sparseSignal = sparseSignals.signal(i);
      if (sparseSignal == Signal.BUY || sparseSignal == Signal.SELL) {
        sparseSignalIndices.add(i);
      }
    }

    final SignalTimeSeries condensedSignals = new SignalTimeSeries(seriesName,
                                                                   sparseSignalIndices.size());
    int index = -1;
    for (final int i : sparseSignalIndices) {
      condensedSignals.set(sparseSignals.date(i), sparseSignals.signal(i), ++index);
    }

    return condensedSignals;
  }

}
