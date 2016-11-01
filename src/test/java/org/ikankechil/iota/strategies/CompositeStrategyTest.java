/**
 * CompositeStrategyTest.java  v0.1  28 September 2016 2:41:08 pm
 *
 * Copyright � 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies;

import static org.ikankechil.iota.Signal.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.Signal;
import org.ikankechil.iota.SignalTimeSeries;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test for <code>CompositeStrategy</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class CompositeStrategyTest {

  private Signal[]                     expected;
  private CompositeStrategy            composite;

  private static final int             ZERO           = 0;
  private static final int             ONE            = 1;
  private static final int             THREE          = 3;

  private static final int             WINDOW         = THREE;
  private static final OHLCVTimeSeries OHLCV          = new OHLCVTimeSeries("OHLCV", (WINDOW << ONE) + ONE);

  private static final String          ACTUAL_SIGNALS = "Actual signals: ";

  private static final TestStrategy    BUY_FIRST        = new TestStrategy(OHLCV.size()) {
    @Override
    void populateSignals(final SignalTimeSeries signals) {
      signals.signal(BUY, ZERO);
    }
  };
  private static final TestStrategy    BUY_MIDDLE       = new TestStrategy(OHLCV.size()) {
    @Override
    void populateSignals(final SignalTimeSeries signals) {
      signals.signal(BUY, signals.size() >> ONE);
    }
  };
  private static final TestStrategy    BUY_LAST         = new TestStrategy(OHLCV.size()) {
    @Override
    void populateSignals(final SignalTimeSeries signals) {
      signals.signal(BUY, signals.size() - ONE);
    }
  };
  private static final TestStrategy    BUY_LAST_SHORT   = new TestStrategy(OHLCV.size() - WINDOW) {
    @Override
    void populateSignals(final SignalTimeSeries signals) {
      signals.signal(BUY, signals.size() - ONE);
    }
  };
  private static final TestStrategy    BUY_FIRST_LAST   = new TestStrategy(OHLCV.size()) {
    @Override
    void populateSignals(final SignalTimeSeries signals) {
      signals.signal(BUY, ZERO);
      signals.signal(BUY, signals.size() - ONE);
    }
  };
  private static final TestStrategy    SELL_FIRST       = new TestStrategy(OHLCV.size()) {
    @Override
    void populateSignals(final SignalTimeSeries signals) {
      signals.signal(SELL, ZERO);
    }
  };
  private static final TestStrategy    SELL_MIDDLE      = new TestStrategy(OHLCV.size()) {
    @Override
    void populateSignals(final SignalTimeSeries signals) {
      signals.signal(SELL, signals.size() >> ONE);
    }
  };
  private static final TestStrategy    SELL_LAST        = new TestStrategy(OHLCV.size()) {
    @Override
    void populateSignals(final SignalTimeSeries signals) {
      signals.signal(SELL, signals.size() - ONE);
    }
  };
  private static final TestStrategy    SELL_FIRST_SHORT = new TestStrategy(OHLCV.size() - WINDOW) {
    @Override
    void populateSignals(final SignalTimeSeries signals) {
      signals.signal(SELL, ZERO);
    }
  };

  @Before
  public void setUp() throws Exception {
    expected = newSignals();
  }

  @After
  public void tearDown() throws Exception {
    expected = null;
    composite = null;
  }

  @Test
  public void buyAndSellWillNotMatch() {
    final List<Strategy[]> composites = Arrays.asList(new Strategy[] { BUY_FIRST, SELL_FIRST },
                                                      new Strategy[] { BUY_FIRST, SELL_MIDDLE },
                                                      new Strategy[] { BUY_FIRST, SELL_LAST },
                                                      new Strategy[] { BUY_MIDDLE, SELL_FIRST },
                                                      new Strategy[] { BUY_MIDDLE, SELL_MIDDLE },
                                                      new Strategy[] { BUY_MIDDLE, SELL_LAST },
                                                      new Strategy[] { BUY_MIDDLE, SELL_FIRST, SELL_LAST },
                                                      new Strategy[] { BUY_LAST, SELL_FIRST },
                                                      new Strategy[] { BUY_LAST, SELL_MIDDLE },
                                                      new Strategy[] { BUY_LAST, SELL_LAST });
    for (final Strategy[] strategies : composites) {
      composite = new CompositeStrategy(WINDOW, true, strategies);
      compare(expected, composite);
    }
  }

  @Test
  public void sellAndBuyWillNotMatch() {
    final List<Strategy[]> composites = Arrays.asList(new Strategy[] { SELL_FIRST, BUY_FIRST },
                                                      new Strategy[] { SELL_FIRST, BUY_MIDDLE },
                                                      new Strategy[] { SELL_FIRST, BUY_LAST },
                                                      new Strategy[] { SELL_MIDDLE, BUY_FIRST },
                                                      new Strategy[] { SELL_MIDDLE, BUY_MIDDLE },
                                                      new Strategy[] { SELL_MIDDLE, BUY_LAST },
                                                      new Strategy[] { SELL_MIDDLE, BUY_FIRST, BUY_LAST },
                                                      new Strategy[] { SELL_LAST, BUY_FIRST },
                                                      new Strategy[] { SELL_LAST, BUY_MIDDLE },
                                                      new Strategy[] { SELL_LAST, BUY_LAST });
    for (final Strategy[] strategies : composites) {
      composite = new CompositeStrategy(WINDOW, true, strategies);
      compare(expected, composite);
    }
  }

  @Test
  public void buyDifferentSignalSizes() {
    expected = newSignals(OHLCV.size() - WINDOW);
    expected[expected.length - ONE] = BUY;

    composite = new CompositeStrategy(WINDOW, true, BUY_MIDDLE, BUY_LAST_SHORT);
    compare(expected, composite);
  }

  @Test
  public void sellDifferentSignalSizes() {
    expected = newSignals(OHLCV.size() - WINDOW);
    expected[ZERO] = SELL;

    composite = new CompositeStrategy(WINDOW, true, SELL_FIRST_SHORT, SELL_MIDDLE);
    compare(expected, composite);
  }

  @Test
  public void buyOutsideWindowWillNotMatch() {
    composite = new CompositeStrategy(WINDOW, true, BUY_FIRST, BUY_LAST);
    compare(expected, composite);
  }

  @Test
  public void sellOutsideWindowWillNotMatch() {
    composite = new CompositeStrategy(WINDOW, true, SELL_FIRST, SELL_LAST);
    compare(expected, composite);
  }

  @Test
  public void unanimousBuysSearchesBackwardsAndForwardsByDefault() {
    composite = new CompositeStrategy(WINDOW, true, BUY_MIDDLE, BUY_FIRST, BUY_LAST);
    compare(expected, composite);

    expected[expected.length - ONE] = BUY;
    expected[WINDOW] = BUY;

    composite = new CompositeStrategy(WINDOW, true, BUY_MIDDLE, BUY_FIRST_LAST, BUY_FIRST_LAST);
    compare(expected, composite);
  }

  @Test
  public void unanimousBuysSearchForwardsOnly() {
    composite = new CompositeStrategy(WINDOW, true, true, false, BUY_LAST, BUY_MIDDLE);
    compare(expected, composite);

    expected[expected.length - ONE] = BUY;
    composite = new CompositeStrategy(WINDOW, true, true, false, BUY_MIDDLE, BUY_LAST);
    compare(expected, composite);
  }

  @Test
  public void unanimousBuysSearchBackwardsOnly() {
    composite = new CompositeStrategy(WINDOW, true, false, true, BUY_FIRST, BUY_MIDDLE);
    compare(expected, composite);

    expected[WINDOW] = BUY;
    composite = new CompositeStrategy(WINDOW, true, false, true, BUY_MIDDLE, BUY_FIRST);
    compare(expected, composite);
  }

  @Test
  public void unanimousBuysNoSearch() {
    expected[WINDOW] = BUY;
    composite = new CompositeStrategy(WINDOW, true, false, false, BUY_MIDDLE, BUY_MIDDLE);
    compare(expected, composite);
  }

  @Test
  public void unanimousBuys() {
    expected[WINDOW] = BUY;

    composite = new CompositeStrategy(WINDOW, true, BUY_MIDDLE, BUY_MIDDLE);
    compare(expected, composite);

    composite = new CompositeStrategy(WINDOW, true, BUY_MIDDLE, BUY_MIDDLE, BUY_MIDDLE);
    compare(expected, composite);
  }

  @Test
  public void unanimousSellsNoSearch() {
    expected[WINDOW] = SELL;
    composite = new CompositeStrategy(WINDOW, true, false, false, SELL_MIDDLE, SELL_MIDDLE);
    compare(expected, composite);
  }

  @Test
  public void unanimousSells() {
    expected[WINDOW] = SELL;

    composite = new CompositeStrategy(WINDOW, true, SELL_MIDDLE, SELL_MIDDLE);
    compare(expected, composite);

    composite = new CompositeStrategy(WINDOW, true, SELL_MIDDLE, SELL_MIDDLE, SELL_MIDDLE);
    compare(expected, composite);
  }

  @Test
  public void nonUnanimousBuys() {
    expected[WINDOW] = BUY;
    composite = new CompositeStrategy(WINDOW, false, BUY_MIDDLE, BUY_MIDDLE);
    compare(expected, composite);

    expected[expected.length - ONE] = BUY;
    composite = new CompositeStrategy(WINDOW, false, BUY_MIDDLE, BUY_LAST);
    compare(expected, composite);

    expected[ZERO] = BUY;
    composite = new CompositeStrategy(WINDOW, false, BUY_FIRST, BUY_MIDDLE, BUY_LAST);
    compare(expected, composite);
   }

  @Test
  public void nonUnanimousSells() {
    expected[WINDOW] = SELL;
    composite = new CompositeStrategy(WINDOW, false, SELL_MIDDLE, SELL_MIDDLE);
    compare(expected, composite);

    expected[expected.length - ONE] = SELL;
    composite = new CompositeStrategy(WINDOW, false, SELL_MIDDLE, SELL_LAST);
    compare(expected, composite);

    expected[ZERO] = SELL;
    composite = new CompositeStrategy(WINDOW, false, SELL_FIRST, SELL_MIDDLE, SELL_LAST);
    compare(expected, composite);
  }

  @Test
  public void buyDirectionEquivalence() {
    expected[WINDOW] = BUY;

    composite = new CompositeStrategy(WINDOW, true, true, false, BUY_FIRST, BUY_MIDDLE);
    compare(expected, composite);

    composite = new CompositeStrategy(WINDOW, true, false, true, BUY_MIDDLE, BUY_FIRST);
    compare(expected, composite);
  }

  @Test
  public void sellDirectionEquivalence() {
    expected[WINDOW] = SELL;

    composite = new CompositeStrategy(WINDOW, true, true, false, SELL_FIRST, SELL_MIDDLE);
    compare(expected, composite);

    composite = new CompositeStrategy(WINDOW, true, false, true, SELL_MIDDLE, SELL_FIRST);
    compare(expected, composite);
  }

  private static final void compare(final Signal[] expected, final CompositeStrategy composite) {
    final Signal[] actual = composite.execute(OHLCV).signals();

    final String message = ACTUAL_SIGNALS + Arrays.asList(actual);
    assertArrayEquals(message, expected, actual);
  }

  private static final Signal[] newSignals() {
    return newSignals(OHLCV.size());
  }

  private static final Signal[] newSignals(final int size) {
    final Signal[] signals = new Signal[size];
    for (int i = ZERO; i < signals.length; ++i) {
      signals[i] = NONE;
    }
    return signals;
  }

  abstract static class TestStrategy implements Strategy {

    private final int size;

    public TestStrategy(final int size) {
      this.size = size;
    }

    @Override
    public SignalTimeSeries execute(final OHLCVTimeSeries ohlcv) {
      return execute(ohlcv, MAX_LOOKBACK);
    }

    @Override
    public SignalTimeSeries execute(final OHLCVTimeSeries ohlcv, final int lookback) {
      final SignalTimeSeries signals = new SignalTimeSeries(toString(), size);
      for (int i = ZERO, j = i + ohlcv.size() - size; i < size; ++i, ++j) {
        signals.set(ohlcv.date(j), NONE, i);
      }
      populateSignals(signals);
      return signals;
    }

    abstract void populateSignals(final SignalTimeSeries signals);

  }

}