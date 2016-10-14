/**
 * OHLCVTimeSeriesTest.java v0.1 6 January 2015 7:04:39 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * A JUnit test for <code>OHLCVTimeSeries</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class OHLCVTimeSeriesTest {

  private OHLCVTimeSeries  ohlcv;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public final void instantiateWithSize() {
    final int size = 2;
    ohlcv = new OHLCVTimeSeries(null, size);

    assertEquals(size, ohlcv.size());

    assertEquals(size, ohlcv.opens().length);
    assertEquals(size, ohlcv.highs().length);
    assertEquals(size, ohlcv.lows().length);
    assertEquals(size, ohlcv.closes().length);
    assertEquals(size, ohlcv.volumes().length);

    assertEquals(size, ohlcv.dates().length);
    assertEquals(size, ohlcv.values().length);
  }

  @Test
  public final void setOpen() {
    ohlcv = new OHLCVTimeSeries(null, 1);
    final double expected = Double.MAX_VALUE;
    ohlcv.open(expected, 0);

    assertEquals(null, expected, ohlcv.open(0), 0);
  }

  @Test
  public final void setHigh() {
    ohlcv = new OHLCVTimeSeries(null, 1);
    final double expected = Double.MAX_VALUE;
    ohlcv.high(expected, 0);

    assertEquals(null, expected, ohlcv.high(0), 0);
  }

  @Test
  public final void setLow() {
    ohlcv = new OHLCVTimeSeries(null, 1);
    final double expected = Double.MAX_VALUE;
    ohlcv.low(expected, 0);

    assertEquals(null, expected, ohlcv.low(0), 0);
  }

  @Test
  public final void setClose() {
    ohlcv = new OHLCVTimeSeries(null, 1);
    final double expected = Double.MAX_VALUE;
    ohlcv.close(expected, 0);

    assertEquals(null, expected, ohlcv.close(0), 0);
  }

  @Test
  public final void setVolume() {
    ohlcv = new OHLCVTimeSeries(null, 1);
    final long expected = Long.MAX_VALUE;
    ohlcv.volume(expected, 0);

    assertEquals(expected, ohlcv.volume(0));
  }

  @Test
  public final void setOHLCV() {
    ohlcv = new OHLCVTimeSeries(null, 1);
    final String date = "expected";
    final double open = 1;
    final double high = 2;
    final double low = -2;
    final double close = -1;
    final long volume = Long.MAX_VALUE;
    ohlcv.set(date, open, high, low, close, volume, 0);

    assertEquals(date, ohlcv.date(0));

    assertEquals(null, open, ohlcv.open(0), 0);
    assertEquals(null, high, ohlcv.high(0), 0);
    assertEquals(null, low, ohlcv.low(0), 0);
    assertEquals(null, close, ohlcv.close(0), 0);
    assertEquals(volume, ohlcv.volume(0));
  }

  @Test
  public final void cannotSetOHLCVBeyondAllocatedSize() {
    final int size = 2;
    ohlcv = new OHLCVTimeSeries(null, size);

    thrown.expect(ArrayIndexOutOfBoundsException.class);
    thrown.expectMessage(String.valueOf(size));
    ohlcv.set(null, 0, 0, 0, 0, 0, size);
  }

  @Test
  public void valuesAndClosesSame() {
    ohlcv = new OHLCVTimeSeries(null, 1);
    assertSame(ohlcv.values(), ohlcv.closes());
  }

  @Test
  public final void noCopiesMade() {
    ohlcv = new OHLCVTimeSeries(null, 1);

    assertSame(ohlcv.opens(), ohlcv.opens());
    assertSame(ohlcv.highs(), ohlcv.highs());
    assertSame(ohlcv.lows(), ohlcv.lows());
    assertSame(ohlcv.closes(), ohlcv.closes());
    assertSame(ohlcv.volumes(), ohlcv.volumes());
  }

}
