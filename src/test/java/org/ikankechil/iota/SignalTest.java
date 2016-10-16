/**
 * SignalTest.java  v0.1  21 July 2016 6:46:31 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * JUnit test for <code>Signal</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class SignalTest {

  private static final String[] SIGNALS = { "BUY", "SELL", "NONE" };

  @Test
  public void validSignals() {
    for (final String signal : SIGNALS) {
      assertNotNull(Signal.valueOf(signal));
    }
    assertEquals(SIGNALS.length, Signal.values().length);
  }

}
