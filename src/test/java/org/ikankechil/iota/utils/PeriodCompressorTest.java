/**
 * PeriodCompressorTest.java	v0.2	12 November 2015 12:25:55 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.utils;

import static org.junit.Assert.*;

import java.io.File;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.io.OHLCVReader;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * JUnit test for <code>PeriodCompressor</code>.
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class PeriodCompressorTest {

  @Rule
  public final ExpectedException thrown          = ExpectedException.none();

  private static OHLCVTimeSeries DAILY;
  private static OHLCVTimeSeries EXPECTED_WEEKLY;
  private static OHLCVTimeSeries EXPECTED_MONTHLY;

  private static final int       ZERO            = 0;

  private static final String    SYMBOL          = "A";
  private static final String    _W              = "_w";
  private static final String    _M              = "_m";

  private static final File      INPUT_DIRECTORY = new File(".//./src/test/resources/" + PeriodCompressorTest.class.getSimpleName().replace('.', '/'));
  private static final String    CSV             = ".csv";

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    final OHLCVReader reader = new OHLCVReader();
    DAILY = reader.read(new File(INPUT_DIRECTORY, SYMBOL + CSV));
    EXPECTED_WEEKLY = reader.read(new File(INPUT_DIRECTORY, SYMBOL + _W + CSV));
    EXPECTED_MONTHLY = reader.read(new File(INPUT_DIRECTORY, SYMBOL + _M + CSV));
  }

  @Test
  public void cannotCompressDailyToMonthlyWithNullTimeSeries() {
    thrown.expect(NullPointerException.class);
    PeriodCompressor.dailyToMonthly(null);
  }

  @Test
  public void cannotCompressWeeklyToMonthlyWithNullTimeSeries() {
    thrown.expect(NullPointerException.class);
    PeriodCompressor.weeklyToMonthly(null);
  }

  @Test
  public void cannotCompressDailyToWeeklyWithNullTimeSeries() {
    thrown.expect(NullPointerException.class);
    PeriodCompressor.dailyToWeekly(null);
  }

  @Test
  public void dailyToMonthly() {
    final OHLCVTimeSeries actual = PeriodCompressor.dailyToMonthly(DAILY);

    assertArrayEquals(EXPECTED_MONTHLY.dates(), actual.dates());
    assertArrayEquals(EXPECTED_MONTHLY.opens(), actual.opens(), ZERO);
    assertArrayEquals(EXPECTED_MONTHLY.highs(), actual.highs(), ZERO);
    assertArrayEquals(EXPECTED_MONTHLY.lows(), actual.lows(), ZERO);
    assertArrayEquals(EXPECTED_MONTHLY.closes(), actual.closes(), ZERO);
    assertArrayEquals(EXPECTED_MONTHLY.volumes(), actual.volumes());
    assertEquals(EXPECTED_MONTHLY.toString(), actual.toString());
  }

  @Ignore@Test
  public void weeklyToMonthly() {
    final OHLCVTimeSeries actual = PeriodCompressor.weeklyToMonthly(EXPECTED_WEEKLY);

    assertArrayEquals(EXPECTED_MONTHLY.dates(), actual.dates());
    assertArrayEquals(EXPECTED_MONTHLY.opens(), actual.opens(), ZERO);
    assertArrayEquals(EXPECTED_MONTHLY.highs(), actual.highs(), ZERO);
    assertArrayEquals(EXPECTED_MONTHLY.lows(), actual.lows(), ZERO);
    assertArrayEquals(EXPECTED_MONTHLY.closes(), actual.closes(), ZERO);
    assertArrayEquals(EXPECTED_MONTHLY.volumes(), actual.volumes());
    assertEquals(EXPECTED_MONTHLY.toString(), actual.toString());
  }

  @Test
  public void dailyToWeekly() {
    final OHLCVTimeSeries actual = PeriodCompressor.dailyToWeekly(DAILY);

    assertArrayEquals(EXPECTED_WEEKLY.dates(), actual.dates());
    assertArrayEquals(EXPECTED_WEEKLY.opens(), actual.opens(), ZERO);
    assertArrayEquals(EXPECTED_WEEKLY.highs(), actual.highs(), ZERO);
    assertArrayEquals(EXPECTED_WEEKLY.lows(), actual.lows(), ZERO);
    assertArrayEquals(EXPECTED_WEEKLY.closes(), actual.closes(), ZERO);
    assertArrayEquals(EXPECTED_WEEKLY.volumes(), actual.volumes());
    assertEquals(EXPECTED_WEEKLY.toString(), actual.toString());
  }

}
