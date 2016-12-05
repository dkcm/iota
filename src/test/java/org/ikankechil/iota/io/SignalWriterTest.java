/**
 * SignalWriterTest.java  v0.2  13 August 2016 12:39:19 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.io;

import static org.ikankechil.iota.Signal.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.ikankechil.iota.SignalTimeSeries;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * JUnit test for <code>SignalWriter</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class SignalWriterTest extends TimeSeriesWriterTest {

  private final SignalWriter                  writer           = new SignalWriter();
  private final File                          destination      = new File(INPUT_DIRECTORY, getClass().getSimpleName() + CSV);

  private static final List<SignalTimeSeries> SIGNALS          = new ArrayList<>();
  private static final List<SignalTimeSeries> MULTIPLE_SIGNALS = new ArrayList<>();
  private static final List<String>           EXPECTED         = new ArrayList<>();
  private static final List<String>           EXPECTEDS        = new ArrayList<>();

  private static final int                    SIZE_A           = 6;
  private static final int                    SIZE_B           = 2;
  private static final int                    SIZE_C           = 3;
  private static final int                    SIZE_E           = 1;
  private static final int                    START_DATE       = 21230100;

  private static final File                   INPUT_DIRECTORY  = new File(".//./src/test/resources/" + SignalWriterTest.class.getSimpleName());
  private static final File                   READ_ONLY        = new File(INPUT_DIRECTORY, "Read-only.csv");

  @BeforeClass
  public static void setUpBeforeClass() {
    if (READ_ONLY.canWrite()) {
      READ_ONLY.setWritable(false);
    }

    // prepare expected values (see below)
    // Date,A,B,C,E
    // 21230105,SELL,SELL,BUY ,
    // 21230104,NONE,BUY ,NONE,
    // 21230103,SELL,    ,NONE,
    // 21230102,NONE,    ,    ,
    // 21230101,BUY ,    ,    ,
    // 21230100,    ,    ,    ,

    final SignalTimeSeries a = new SignalTimeSeries("A", SIZE_A);
    final SignalTimeSeries b = new SignalTimeSeries("B", SIZE_B);
    final SignalTimeSeries c = new SignalTimeSeries("C", SIZE_C);
    final SignalTimeSeries e = new SignalTimeSeries("E", SIZE_E);
    SIGNALS.add(a);
    MULTIPLE_SIGNALS.add(a);
    MULTIPLE_SIGNALS.add(b);
    MULTIPLE_SIGNALS.add(c);
    MULTIPLE_SIGNALS.add(e);

    // D-Day
    final StringBuilder line = new StringBuilder();
    int ia = 0;
    int d = START_DATE;
    String date = String.valueOf(d);
    a.date(date, ia); // leave signal as null
    line.append(date).append(COMMA);
    EXPECTED.add(line.toString());
    line.append(COMMA)
        .append(COMMA)
        .append(COMMA);
    EXPECTEDS.add(line.toString());
    line.setLength(0);

    // D-Day + 1
    date = String.valueOf(++d);
    a.set(date, BUY, ++ia);
    line.append(date).append(COMMA).append(a.signal(ia));
    EXPECTED.add(line.toString());
    line.append(COMMA)
        .append(COMMA)
        .append(COMMA);
    EXPECTEDS.add(line.toString());
    line.setLength(0);

    // D-Day + 2
    date = String.valueOf(++d);
    a.set(date, NONE, ++ia);
    line.append(date).append(COMMA).append(a.signal(ia));
    EXPECTED.add(line.toString());
    line.append(COMMA)
        .append(COMMA)
        .append(COMMA);
    EXPECTEDS.add(line.toString());
    line.setLength(0);

    // D-Day + 3
    int ic = 0;
    date = String.valueOf(++d);
    a.set(date, SELL, ++ia);
    c.set(date, NONE, ic);
    line.append(date).append(COMMA).append(a.signal(ia));
    EXPECTED.add(line.toString());
    line.append(COMMA)
        .append(COMMA).append(c.signal(ic))
        .append(COMMA);
    EXPECTEDS.add(line.toString());
    line.setLength(0);

    // D-Day + 4
    int ib = 0;
    date = String.valueOf(++d);
    a.set(date, NONE, ++ia);
    b.set(date, BUY, ib);
    c.set(date, NONE, ++ic);
    line.append(date).append(COMMA).append(a.signal(ia));
    EXPECTED.add(line.toString());
    line.append(COMMA).append(b.signal(ib))
        .append(COMMA).append(c.signal(ic))
        .append(COMMA);
    EXPECTEDS.add(line.toString());
    line.setLength(0);

    // D-Day + 5
    date = String.valueOf(++d);
    a.set(date, SELL, ++ia);
    b.set(date, SELL, ++ib);
    c.set(date, BUY, ++ic);
    e.date(date, 0); // leave signal as null
    line.append(date).append(COMMA).append(a.signal(ia));
    EXPECTED.add(line.toString());
    line.append(COMMA).append(b.signal(ib))
        .append(COMMA).append(c.signal(ic))
        .append(COMMA);
    EXPECTEDS.add(line.toString());
    line.setLength(0);

    // reverse order
    Collections.reverse(EXPECTED);
    Collections.reverse(EXPECTEDS);

    // add header
    line.append("Date").append(COMMA).append(a.toString());
    EXPECTED.add(0, line.toString());
    line.append(COMMA).append(b.toString())
        .append(COMMA).append(c.toString())
        .append(COMMA).append(e.toString());
    EXPECTEDS.add(0, line.toString());
  }

  @AfterClass
  public static void tearDownAfterClass() {
    MULTIPLE_SIGNALS.clear();
    EXPECTEDS.clear();
  }

  @Test
  public void writeSingleStrategy() throws IOException {
    writeThenCompare(SIGNALS, EXPECTED);
  }

  @Test
  public void writeMultipleStrategies() throws IOException {
    writeThenCompare(MULTIPLE_SIGNALS, EXPECTEDS);
  }

  private final void writeThenCompare(final List<? extends SignalTimeSeries> signalSeries,
                                      final List<String> expecteds)
      throws IOException {
    writer.write(signalSeries, destination);

    final List<String> actuals = Files.readAllLines(destination.toPath(), StandardCharsets.UTF_8);

    // compare
    for (int i = 0; i < expecteds.size(); ++i) {
      assertEquals("Line: " + i, expecteds.get(i), actuals.get(i));
    }
    assertEquals(expecteds.size(), actuals.size());
  }

  @Test
  public void emptySignalsNotWritten() throws IOException {
    final SignalTimeSeries emptySignals = new SignalTimeSeries("Empty Signals", 0);
    final long expected = destination.lastModified();
    writer.write(Arrays.asList(emptySignals), destination);
    assertEquals(expected, destination.lastModified()); // file not modified
  }

  @Test
  public void emptySignalsListNotWritten() throws IOException {
    final long expected = destination.lastModified();
    writer.write(Collections.emptyList(), destination);
    assertEquals(expected, destination.lastModified()); // file not modified
  }

  @Override
  public void cannotWriteNullPayload() throws IOException {
    writer.write(null, destination);
  }

  @Override
  public void cannotWriteToNullFile() throws IOException {
    writer.write(MULTIPLE_SIGNALS, null);
  }

  @Override
  public void cannotWriteToUnwritableFile() throws IOException {
    writer.write(MULTIPLE_SIGNALS, READ_ONLY);
  }

  @Override
  public void cannotWriteToDirectory() throws IOException {
    writer.write(MULTIPLE_SIGNALS, INPUT_DIRECTORY);
  }

}
