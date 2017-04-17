/**
 * SignalsSummaryWriterTest.java  v0.1  17 April 2017 12:27:26 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ikankechil.iota.SignalTimeSeries;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 *
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class SignalsSummaryWriterTest extends TimeSeriesWriterTest {

  private final SignalsSummaryWriter          writer           = new SignalsSummaryWriter();
  private final File                          destination      = new File(INPUT_DIRECTORY, getClass().getSimpleName() + CSV);

  private static final List<SignalTimeSeries> MULTIPLE_SIGNALS = new ArrayList<>();
  private static final List<String>           EXPECTEDS        = new ArrayList<>();

  private static final int                    SIZE_A           = 6;
  private static final int                    SIZE_B           = 2;
  private static final int                    SIZE_C           = 3;
  private static final int                    SIZE_E           = 1;
  private static final int                    START_DATE       = 21230100;

  private static final File                   INPUT_DIRECTORY  = new File(".//./src/test/resources/" + SignalsSummaryWriterTest.class.getSimpleName());
  private static final File                   READ_ONLY        = new File(INPUT_DIRECTORY, "Read-only.csv");

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    if (READ_ONLY.canWrite()) {
      READ_ONLY.setWritable(false);
    }

    final SignalTimeSeries a = new SignalTimeSeries("A", SIZE_A);
    final SignalTimeSeries b = new SignalTimeSeries("B", SIZE_B);
    final SignalTimeSeries c = new SignalTimeSeries("C", SIZE_C);
    final SignalTimeSeries e = new SignalTimeSeries("E", SIZE_E);
    MULTIPLE_SIGNALS.add(a);
    MULTIPLE_SIGNALS.add(b);
    MULTIPLE_SIGNALS.add(c);
    MULTIPLE_SIGNALS.add(e);

  }

  @AfterClass
  public static void tearDownAfterClass() {
    MULTIPLE_SIGNALS.clear();
    EXPECTEDS.clear();
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
