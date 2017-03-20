/**
 * IotaTest.java  v0.1  22 January 2017 9:36:28 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.strategies.Strategy;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * JUnit test for <code>Iota</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class IotaTest {

  private Iota                iota;

  private static final String EMPTY      = "";

  private static final File   DIRECTORY  = new File(".//./src/test/resources/" + IotaTest.class.getSimpleName());
  private static final File   OHLCV_FILE = new File(DIRECTORY, "SPY_20001220-20161206_w.csv");
  private static final String INDICATORS = "Indicators";

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
    iota = new Iota(DIRECTORY);
  }

  @After
  public void tearDown() throws Exception {
    try {
      iota.stop();
    }
    finally {
      iota = null;
    }
  }

  @Test(expected=NullPointerException.class)
  public void nullFile() {
    iota = new Iota(null);
  }

  @Test(expected=IllegalArgumentException.class)
  public void indicatorDirectoryIsAFile() {
    iota = new Iota(new File(EMPTY));
  }

  @Test(expected=NullPointerException.class)
  public void cannotGenerateIndicatorsWithNullPrices() throws IOException {
    iota.generateIndicators(null, Arrays.asList(new TestIndicator()));
  }

  @Test(expected=IllegalArgumentException.class)
  public void cannotGenerateIndicatorsWithEmptyIndicators() throws IOException {
    iota.generateIndicators(OHLCV_FILE, Collections.emptyList());
  }

  @Test(expected=NullPointerException.class)
  public void cannotGenerateIndicatorsWithNullIndicators() throws IOException {
    iota.generateIndicators(OHLCV_FILE, null);
  }

  @Test(expected=NullPointerException.class)
  public void cannotGenerateSignalsWithNullPrices() throws IOException {
    iota.generateSignals(null, Arrays.asList(new TestStrategy()));
  }

  @Test(expected=IllegalArgumentException.class)
  public void cannotGenerateSignalsWithEmptyStrategies() throws IOException {
    iota.generateSignals(OHLCV_FILE, Collections.emptyList());
  }

  @Test(expected=NullPointerException.class)
  public void cannotGenerateSignalsWithNullStrategies() throws IOException {
    iota.generateSignals(OHLCV_FILE, null);
  }

  @Test(expected=IllegalArgumentException.class)
  public void cannotGenerateSignalsWithNegativeLookback() throws IOException {
    iota.generateSignals(OHLCV_FILE, Arrays.asList(new TestStrategy()), -1);
  }

  @Ignore@Test
  public void generateSignals() {
  }

  private static class TestIndicator implements Indicator {

    @Override
    public int compareTo(final Indicator o) {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public List<TimeSeries> generate(final TimeSeries series) {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public List<TimeSeries> generate(final TimeSeries series, final int start) {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public int lookback() {
      // TODO Auto-generated method stub
      return 0;
    }

  }

  private static class TestStrategy implements Strategy {

    @Override
    public SignalTimeSeries execute(final OHLCVTimeSeries ohlcv) {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public SignalTimeSeries execute(final OHLCVTimeSeries ohlcv, final int lookback) {
      // TODO Auto-generated method stub
      return null;
    }

  }

}
