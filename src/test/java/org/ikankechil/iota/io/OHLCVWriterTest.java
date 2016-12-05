/**
 * OHLCVWriterTest.java  v0.2  20 June 2015 12:08:16 am
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.io;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * JUnit test for <code>OHLCVWriter</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class OHLCVWriterTest extends TimeSeriesWriterTest {

  private final OHLCVWriter            writer          = new OHLCVWriter();
  private final File                   destination     = new File(INPUT_DIRECTORY, getClass().getSimpleName() + CSV);

  private static final String          SYMBOL          = "A";
  private static final char            FREQUENCY       = 'd';
  private static final int             LINES           = 5;
  private static final OHLCVTimeSeries OHLCV           = new OHLCVTimeSeries(SYMBOL, LINES);
  private static final List<String>    EXPECTEDS       = new ArrayList<>(OHLCV.size());
  private static final int             START_DATE      = 21230100;
  private static final double          OPEN            = 10d;
  private static final double          HIGH            = 1000d;
  private static final double          LOW             = 0d;
  private static final double          CLOSE           = 100d;
  private static final int             VOLUME          = 10000;

  private static final File            INPUT_DIRECTORY = new File(".//./src/test/resources/" + OHLCVWriterTest.class.getSimpleName());
  private static final File            READ_ONLY       = new File(INPUT_DIRECTORY, "Read-only.csv");

  private static final String          LINE_           = "Line: ";

  @BeforeClass
  public static void setUpBeforeClass() {
    if (READ_ONLY.canWrite()) {
      READ_ONLY.setWritable(false);
    }

    // prepare expected lines
    assertTrue(prepare(OHLCV, EXPECTEDS, false));
  }

  private static final boolean prepare(final OHLCVTimeSeries ohlcv,
                                       final List<String> expectedResults,
                                       final boolean dropVolume) {
    final StringBuilder line = new StringBuilder();
    int date = START_DATE;
    double open = OPEN;
    double high = HIGH;
    double low = LOW;
    double close = CLOSE;
    long volume = VOLUME;

    for (int i = 0; i < ohlcv.size(); ++i) {
      line.append(SYMBOL).append(COMMA)
          .append(++date).append(COMMA)
          .append(++open).append(COMMA)
          .append(++high).append(COMMA)
          .append(++low).append(COMMA)
          .append(++close);
      if (dropVolume) {
        ++volume;
      }
      else {
        line.append(COMMA).append(++volume);
      }
      expectedResults.add(line.toString());
      line.setLength(0);

      ohlcv.set(String.valueOf(date), open, high, low, close, volume, i);
    }
    Collections.reverse(expectedResults);
    return true;
  }

  @AfterClass
  public static void tearDownAfterClass() {
    EXPECTEDS.clear();
  }

  @Test
  public void writeOHLCV() throws IOException {
    writer.write(OHLCV, destination);

    final List<String> actuals = Files.readAllLines(destination.toPath(), StandardCharsets.UTF_8);
    compare(EXPECTEDS, actuals);
  }

  @Test
  public void dropFrequencyFromSymbolName() throws IOException {
    final OHLCVTimeSeries symbolWithFrequency = new OHLCVTimeSeries(SYMBOL + UNDERSCORE + FREQUENCY, 1);
    final List<String> expecteds = new ArrayList<>(symbolWithFrequency.size());
    assertTrue(prepare(symbolWithFrequency,
                       expecteds,
                       false));

    writer.write(symbolWithFrequency, destination);

    final List<String> actuals = Files.readAllLines(destination.toPath(), StandardCharsets.UTF_8);
    compare(expecteds, actuals);
  }

  @Test
  public void dropVolume() throws IOException {
    final boolean keepVolume = false;
    final OHLCVWriter dropVolume = new OHLCVWriter(keepVolume);
    final OHLCVTimeSeries ohlcv = new OHLCVTimeSeries(SYMBOL, 1);
    final List<String> expecteds = new ArrayList<>(ohlcv.size());
    assertTrue(prepare(ohlcv,
                       expecteds,
                       !keepVolume));

    dropVolume.write(ohlcv, destination);

    final List<String> actuals = Files.readAllLines(destination.toPath(), StandardCharsets.UTF_8);
    compare(expecteds, actuals);
  }

  private static final void compare(final List<String> expecteds, final List<String> actuals) {
    for (int i = 0; i < expecteds.size(); ++i) {
      assertEquals(LINE_ + i, expecteds.get(i), actuals.get(i));
    }
    assertEquals(expecteds.size(), actuals.size());
  }

  @Override
  public void cannotWriteNullPayload() throws IOException {
    writer.write(null, destination);
  }

  @Override
  public void cannotWriteToNullFile() throws IOException {
    writer.write(OHLCV, null);
  }

  @Override
  public void cannotWriteToUnwritableFile() throws IOException {
    writer.write(OHLCV, READ_ONLY);
  }

  @Override
  public void cannotWriteToDirectory() throws IOException {
    writer.write(OHLCV, INPUT_DIRECTORY);
  }

}
