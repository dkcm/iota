/**
 * IndicatorWriterTest.java  v0.2  20 June 2015 12:04:22 am
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.ikankechil.iota.TimeSeries;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * JUnit test for <code>IndicatorWriter</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class IndicatorWriterTest extends TimeSeriesWriterTest {

  private final IndicatorWriter         writer          = new IndicatorWriter();
  private final File                    destination     = new File(INPUT_DIRECTORY, getClass().getSimpleName() + CSV);

  private static final int              SMALL           = 5;
  private static final int              MEDIUM          = SMALL << 1;
  private static final int              LARGE           = MEDIUM << 1;
  private static final int              START_DATE      = 21050100;

  private static final List<TimeSeries> PAYLOADS        = new ArrayList<>();
  private static final List<String>     EXPECTEDS       = new ArrayList<>(LARGE + 1);

  private static final File             INPUT_DIRECTORY = new File(".//./src/test/resources/" + IndicatorWriterTest.class.getSimpleName());
  private static final File             READ_ONLY       = new File(INPUT_DIRECTORY, "Read-only.csv");

  @BeforeClass
  public static void setUpBeforeClass() {
    if (READ_ONLY.canWrite()) {
      READ_ONLY.setWritable(false);
    }

    final TimeSeries a = new TimeSeries("A", SMALL);
    final TimeSeries b = new TimeSeries("B", LARGE);
    final TimeSeries c = new TimeSeries("C", MEDIUM);

    // create time-lines
    final List<String> chrono = new ArrayList<>();
    for (int i = 0, date = START_DATE; i < LARGE; ++i) {
      chrono.add(String.valueOf(++date));
    }
    final List<String> reverseChrono = new ArrayList<>(chrono);
    Collections.reverse(reverseChrono);

    // build time series
    PAYLOADS.addAll(Arrays.asList(a, b, c));

    for (final TimeSeries payload : PAYLOADS) {
      final int size = payload.size();
      for (int i = 0, t = chrono.size() - size, value = size;
          i < size;
          ++i, ++t) {
        payload.set(chrono.get(t), --value, i);
      }
    }

    // create header
    final StringBuilder header = new StringBuilder("Date");
    for (final TimeSeries payload : PAYLOADS) {
      header.append(COMMA).append(payload.toString());
    }
    EXPECTEDS.add(header.toString());

    // prepare expected lines
    for (int i = 0; i < reverseChrono.size(); ++i) {
      final String date = reverseChrono.get(i);
      final StringBuilder line = new StringBuilder(date);
      for (final TimeSeries payload : PAYLOADS) {
        line.append(COMMA);
        if (i < payload.size()) {
          line.append((double) i);
        }
      }
      EXPECTEDS.add(line.toString());
    }
  }

  @AfterClass
  public static void tearDownAfterClass() {
    PAYLOADS.clear();
    EXPECTEDS.clear();
  }

  @Test
  public void writeIndicatorsOfDifferentLengths() throws IOException {
    writer.write(PAYLOADS, destination);

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
    writer.write(PAYLOADS, null);
  }

  @Override
  public void cannotWriteToUnwritableFile() throws IOException {
    writer.write(PAYLOADS, READ_ONLY);
  }

  @Override
  public void cannotWriteToDirectory() throws IOException {
    writer.write(PAYLOADS, INPUT_DIRECTORY);
  }

}
