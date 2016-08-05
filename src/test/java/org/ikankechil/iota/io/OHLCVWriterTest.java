/**
 * OHLCVWriterTest.java v0.1  20 June 2015 12:08:16 am
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
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class OHLCVWriterTest extends TimeSeriesWriterTest {

  private final OHLCVWriter            writer          = new OHLCVWriter();
  private final File                   destination     = new File(INPUT_DIRECTORY, getClass().getSimpleName() + CSV);

  private static final String          SYMBOL          = "A";
  private static final int             LINES           = 5;
  private static final OHLCVTimeSeries OHLCV           = new OHLCVTimeSeries(SYMBOL, LINES);
  private static final List<String>    EXPECTEDS       = new ArrayList<>(OHLCV.size());
  private static final int             START_DATE      = 21230100;

  private static final File            INPUT_DIRECTORY = new File(".//./src/test/resources/" + OHLCVWriterTest.class.getSimpleName());
  private static final File            READ_ONLY       = new File(INPUT_DIRECTORY, "Read-only.csv");

  @BeforeClass
  public static void setUpBeforeClass() {
    if (READ_ONLY.canWrite()) {
      READ_ONLY.setWritable(false);
    }

    // prepare expected lines
    final StringBuilder line = new StringBuilder();
    int date = START_DATE;
    double open = 10d;
    double high = 1000d;
    double low = 0d;
    double close = 100d;
    long volume = 10000;
    for (int i = 0; i < LINES; ++i) {
      line.append(SYMBOL).append(COMMA)
          .append(++date).append(COMMA)
          .append(++open).append(COMMA)
          .append(++high).append(COMMA)
          .append(++low).append(COMMA)
          .append(++close).append(COMMA)
          .append(++volume);
      EXPECTEDS.add(line.toString());
      line.setLength(0);

      OHLCV.set(String.valueOf(date), open, high, low, close, volume, i);
    }
    Collections.reverse(EXPECTEDS);
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    EXPECTEDS.clear();
  }

  @Test
  public void writeOHLCV() throws IOException {
    writer.write(OHLCV, destination);

    final List<String> actuals = Files.readAllLines(destination.toPath(), StandardCharsets.UTF_8);

    // compare
    for (int i = 0; i < EXPECTEDS.size(); ++i) {
      assertEquals("Line: " + i, EXPECTEDS.get(i), actuals.get(i));
    }
    assertEquals(EXPECTEDS.size(), actuals.size());
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
