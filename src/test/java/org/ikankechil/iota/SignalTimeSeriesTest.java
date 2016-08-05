/**
 * SignalTimeSeriesTest.java  v0.2  11 January 2015 10:45:52 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test for <code>SignalTimeSeries</code>.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class SignalTimeSeriesTest {

  private SignalTimeSeries    signals;

  private static final String DATE   = "20150101";

  @Before
  public void setUp() throws Exception {
    signals = new SignalTimeSeries(null, 1);
  }

  @After
  public void tearDown() throws Exception {
    signals = null;
  }

  @Test
  public final void size() {
    assertEquals(1, signals.signals().length);
  }

  @Test
  public final void setSignal() {
    final Signal expected = Signal.BUY;
    signals.signal(expected, 0);

    assertEquals(expected, signals.signal(0));
  }

  @Test
  public final void setSignal2() {
    final Signal expected = Signal.SELL;
    signals.set(DATE, expected, 0);

    assertEquals(expected, signals.signal(0));
  }

  @Test(expected=NullPointerException.class)
  public final void cannotSetNullSignal() {
    signals.set(DATE, null, 0);
  }

  @Test(expected=NullPointerException.class)
  public final void cannotSetNullSignal2() {
    signals.signal(null, 0);
  }

}
