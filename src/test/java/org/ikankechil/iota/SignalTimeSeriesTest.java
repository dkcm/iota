/**
 * SignalTimeSeriesTest.java  v0.3  11 January 2015 10:45:52 PM
 *
 * Copyright © 2015-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota;

import static org.ikankechil.iota.Signal.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test for <code>SignalTimeSeries</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class SignalTimeSeriesTest {

  private SignalTimeSeries    signals;

  private static final String DATE = "20150101";

  @Before
  public void setUp() throws Exception {
    signals = new SignalTimeSeries(null, 1);
  }

  @After
  public void tearDown() throws Exception {
    signals = null;
  }

  @Test(expected=IllegalArgumentException.class)
  public final void cannotInstantiateWithNegativeSize() {
    signals = new SignalTimeSeries(null, -1);
  }

  @Test(expected=IllegalArgumentException.class)
  public final void cannotInstantiateWithZeroSize() {
    signals = new SignalTimeSeries(null, 0);
  }

  @Test
  public final void size() {
    assertEquals(1, signals.signals().length);
  }

  @Test
  public final void setSignalWithoutDate() {
    final Signal expected = BUY;
    final int index = 0;
    signals.signal(expected, index);

    assertEquals(expected, signals.signal(index));
    assertNull(signals.date(index));
  }

  @Test
  public final void setSignalWithDate() {
    final Signal expected = SELL;
    final int index = 0;
    signals.set(DATE, expected, index);

    assertEquals(expected, signals.signal(index));
    assertEquals(DATE, signals.date(index));
  }

  @Test(expected=NullPointerException.class)
  public final void cannotSetNullSignal() {
    signals.set(DATE, null, 0);
  }

  @Test(expected=NullPointerException.class)
  public final void cannotSetNullSignal2() {
    signals.signal(null, 0);
  }

  @Test
  public final void condenseSignals() {
    final SignalTimeSeries sparseSignals = new SignalTimeSeries(null, 10);
    sparseSignals.set(String.valueOf(0), BUY, 0);
    sparseSignals.set(String.valueOf(2), NONE, 2);
    sparseSignals.set(String.valueOf(4), SELL, 4);
    sparseSignals.set(String.valueOf(7), NONE, 7);
    sparseSignals.set(String.valueOf(9), BUY, 9);

    final SignalTimeSeries condensedSignals = SignalTimeSeries.condenseSignals(sparseSignals, null);

    assertEquals(3, condensedSignals.size());

    assertEquals(BUY, condensedSignals.signal(0));
    assertEquals(sparseSignals.date(0), condensedSignals.date(0));
    assertEquals(SELL, condensedSignals.signal(1));
    assertEquals(sparseSignals.date(4), condensedSignals.date(1));
    assertEquals(BUY, condensedSignals.signal(2));
    assertEquals(sparseSignals.date(9), condensedSignals.date(2));

    assertEquals(sparseSignals.toString(), condensedSignals.toString());
  }

}
