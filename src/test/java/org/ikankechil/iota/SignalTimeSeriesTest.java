/**
 * SignalTimeSeriesTest.java	v0.1	11 January 2015 10:45:52 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * A JUnit test for <code>SignalTimeSeries</code>.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class SignalTimeSeriesTest {

  private SignalTimeSeries signals;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public final void setSignal() {
    signals = new SignalTimeSeries(null, 1);
    final Signal expected = Signal.BUY;
    signals.signal(expected, 0);

    assertEquals(expected, signals.signal(0));
  }

}
