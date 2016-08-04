/**
 * AbstractIndicatorTest.java v0.4  10 January 2015 1:45:25 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.io.IndicatorReader;
import org.ikankechil.iota.io.IndicatorWriter;
import org.ikankechil.iota.io.OHLCVReader;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tictactec.ta.lib.Core;

/**
 * Abstract superclass for all <code>Indicator</code> JUnit tests.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.4
 */
public abstract class AbstractIndicatorTest {

  private Indicator               indicator;
  protected final int             lookback;
  private Class<Indicator>        classUnderTest;

  @Rule
  public final ExpectedException  thrown          = ExpectedException.none();

  private static OHLCVTimeSeries  OHLCV;
  private static List<TimeSeries> EXPECTEDS;

  protected static final Core     CORE            = new Core();

  private static final File       INPUT_DIRECTORY = new File(".//./src/test/resources/" + AbstractIndicatorTest.class.getSimpleName());
  private static final File       OHLCV_FILE      = new File(INPUT_DIRECTORY, "IBM_20110103-20141231.csv");

  protected static Class<?>       TEST_CLASS;
  private static final String     TEST            = "Test";
  private static final String     EMPTY           = "";
  private static final String     CSV             = ".csv";

  private static final String     INDICATOR       = "Indicator: ";

  private static final double     DELTA           = 1e-6;

  private static final Logger     logger          = LoggerFactory.getLogger(AbstractIndicatorTest.class);

  static { // read once across all tests
    try {
      OHLCV = new UnmodifiableOHLCVTimeSeries(new OHLCVReader().read(OHLCV_FILE));
    }
    catch (final IOException ioE) {
      logger.error("Cannot read OHLCV file: {}", OHLCV_FILE, ioE);
      throw new IOError(ioE);
    }
  }

  /**
   * Generates <code>Indicator</code> test data.
   *
   * @param arguments fully-qualified class name of the target
   *          <code>Indicator</code>.
   * @throws IOException
   * @throws ReflectiveOperationException
   */
  public static void main(final String... arguments) throws IOException, ReflectiveOperationException {
    final int parameterCount = arguments.length - 1;
    final Class<?> clazz = Class.forName(arguments[0]);
    for (final Constructor<?> constructor : clazz.getConstructors()) {
      if (constructor.getParameterCount() == parameterCount) {
        final Indicator indicator = (Indicator) constructor.newInstance((Object[]) Arrays.copyOfRange(arguments, 1, arguments.length));
        final File destination = new File(INPUT_DIRECTORY, indicator.getClass().getSimpleName() + TEST + CSV);
        new IndicatorWriter().write(indicator.generate(OHLCV), destination);
        break;
      }
    }
  }

  @SuppressWarnings("unchecked")
  public AbstractIndicatorTest(final int lookback) {
    this.lookback = lookback;
    try {
      classUnderTest = (Class<Indicator>) Class.forName(TEST_CLASS.getName().replace(TEST, EMPTY));
    }
    catch (final ClassNotFoundException cnfE) {
      fail("Class under test not found: " + classUnderTest);
    }

    // Algorithm:
    // 1. Read OHLCV file
    // 2. Generate indicator values
    // 3. Read expected values file
    // 4. Compare values
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    final File expecteds = new File(INPUT_DIRECTORY, TEST_CLASS.getSimpleName() + CSV);
    if (expecteds.exists()) {
      EXPECTEDS = Collections.unmodifiableList(new IndicatorReader().read(expecteds));
    }
    else {
      fail("Expected results file does not exist: " + expecteds);
    }
  }

  @Before
  public void setUp() throws Exception {
    indicator = newInstance();
  }

  @After
  public void tearDown() throws Exception {
    indicator = null;
  }

  public Indicator newInstance() throws ReflectiveOperationException {
    final Constructor<Indicator> constructor = classUnderTest.getConstructor();
    constructor.setAccessible(true);
    return constructor.newInstance();
  }

  public Indicator newInstance(final int period) throws ReflectiveOperationException {
    final Constructor<Indicator> constructor = classUnderTest.getConstructor(Integer.TYPE);
    constructor.setAccessible(true);
    return constructor.newInstance(period);
  }

  @Test(expected=ReflectiveOperationException.class)
  public void cannotInstantiateWithNegativePeriod() throws ReflectiveOperationException {
    newInstance(-1);
  }

  @Test
  public void name() {
    assertEquals(indicator.getClass().getSimpleName(), indicator.toString());
  }

  @Test
  public void lookback() {
    assertEquals(lookback, indicator.lookback());
  }

  @Test
  public void shorterThanOHLCVByLookback() {
    for (final TimeSeries series : indicator.generate(OHLCV)) {
      assertEquals(series.toString(), OHLCV.size() - lookback, series.size());
    }
  }

  @Test
  public void datesStartFromLookback() {
    final String[] expected = Arrays.copyOfRange(OHLCV.dates(),
                                                 lookback,
                                                 OHLCV.size());
    final List<TimeSeries> actuals = indicator.generate(OHLCV);

    for (final TimeSeries actual : actuals) {
      assertArrayEquals(actual.toString(), expected, actual.dates());
    }
  }

  @Test
  public void indicatorValues() {
    final List<TimeSeries> expecteds = generate(OHLCV);
    final List<TimeSeries> actuals = indicator.generate(OHLCV);

    int i = -1;
    for (final TimeSeries expected : expecteds) {
      final String message = INDICATOR + expected.toString();
//      final double[] e = expected.values();
//      final double[] a = actuals.get(0).values();
//      for (int j = 0; j < e.length; ++j) {
////        assertEquals(e[j], a[j], 1);
//        System.out.println(j + ": " + OHLCV.close(j + lookback) + " " + e[j] + " " + a[j]);
//      }

      final TimeSeries actual = actuals.get(++i);
      assertArrayEquals(message,
                        expected.values(),
                        actual.values(),
                        DELTA);
      assertEquals(expected.toString(), actual.toString());
    }
    assertEquals(expecteds.size(), actuals.size());
  }

  /**
   * Generates expected indicator values.
   *
   * @param series
   * @return
   */
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    // default behaviour
    return EXPECTEDS;
  }

  private static class UnmodifiableOHLCVTimeSeries extends OHLCVTimeSeries {

    private final OHLCVTimeSeries ohlcv;
    private final int             size;

    UnmodifiableOHLCVTimeSeries(final OHLCVTimeSeries ohlcv) {
      super(ohlcv.toString(), 0);

      this.ohlcv = ohlcv;
      size = ohlcv.size();
    }

    @Override
    public String[] dates() {
      return Arrays.copyOf(ohlcv.dates(), size);
    }

    @Override
    public double[] opens() {
      return Arrays.copyOf(ohlcv.opens(), size);
    }

    @Override
    public double[] highs() {
      return Arrays.copyOf(ohlcv.highs(), size);
    }

    @Override
    public double[] lows() {
      return Arrays.copyOf(ohlcv.lows(), size);
    }

    @Override
    public double[] values() {
      return Arrays.copyOf(ohlcv.values(), size);
    }

    @Override
    public long[] volumes() {
      return Arrays.copyOf(ohlcv.volumes(), size);
    }

    @Override
    public String date(final int index) {
      return ohlcv.date(index);
    }

    @Override
    public double open(final int index) {
      return ohlcv.open(index);
    }

    @Override
    public double high(final int index) {
      return ohlcv.high(index);
    }

    @Override
    public double low(final int index) {
      return ohlcv.low(index);
    }

    @Override
    public double value(final int index) {
      return ohlcv.value(index);
    }

    @Override
    public long volume(final int index) {
      return ohlcv.volume(index);
    }

    @Override
    public void date(final String value, final int index) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void open(final double value, final int index) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void high(final double value, final int index) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void low(final double value, final int index) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void value(final double value, final int index) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void volume(final long value, final int index) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void set(final String date,
                    final double value,
                    final int index) {
      throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
      return size;
    }

  }

}
