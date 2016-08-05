/**
 * OHLCVReaderTest.java v0.1  5 August 2016 5:04:02 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.io;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * JUnit test for <code>OHLCVReader</code>.
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class OHLCVReaderTest {

  private final OHLCVReader      reader          = new OHLCVReader();

  private static OHLCVTimeSeries EXPECTEDS;

  private static final double    DELTA           = 0d;
  private static final int       INCLUDES_VOLUME = 7;

  private static final String    EMPTY           = "";
  private static final String    COMMA           = ",";
  private static final char      SLASH           = '/';

  private static final String    CSV             = ".csv";
  private static final String    NAME            = OHLCVReaderTest.class.getSimpleName();
  private static final File      INPUT_DIRECTORY = new File(".//./src/test/resources/" + NAME);
  private static final File      SOURCE          = new File(INPUT_DIRECTORY, NAME + CSV);

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    final List<String> lines = Files.readAllLines(SOURCE.toPath(), StandardCharsets.UTF_8);
    Collections.reverse(lines);
    EXPECTEDS = new OHLCVTimeSeries(NAME + SLASH + NAME, lines.size());
    for (int i = 0; i < lines.size(); ++i) {
      final String[] values = lines.get(i).split(COMMA);
      int v = 0;  // skip symbol name
      EXPECTEDS.set(values[++v],
                    Double.valueOf(values[++v]),
                    Double.valueOf(values[++v]),
                    Double.valueOf(values[++v]),
                    Double.valueOf(values[++v]),
                    (values.length >= INCLUDES_VOLUME) ? Long.valueOf(values[++v]) : 0,
                    i);
    }
  }

  @AfterClass
  public static void tearDownAfterClass() {
    EXPECTEDS = null;
  }

  @Test
  public void readOHLCV() throws IOException {
    final OHLCVTimeSeries ohlcv = reader.read(SOURCE);

    assertEquals(EXPECTEDS.toString(), ohlcv.toString());
    assertArrayEquals(EXPECTEDS.opens(), ohlcv.opens(), DELTA);
    assertArrayEquals(EXPECTEDS.highs(), ohlcv.highs(), DELTA);
    assertArrayEquals(EXPECTEDS.lows(), ohlcv.lows(), DELTA);
    assertArrayEquals(EXPECTEDS.closes(), ohlcv.closes(), DELTA);
    assertArrayEquals(EXPECTEDS.volumes(), ohlcv.volumes());
  }

  @Test(expected=NullPointerException.class)
  public void cannotReadNullFile() throws IOException {
    reader.read(null);
  }

  @Test(expected=FileNotFoundException.class)
  public void cannotReadNonexistentFile() throws IOException {
    reader.read(new File(EMPTY));
  }

  @Test(expected=FileNotFoundException.class)
  public void cannotReadDirectory() throws IOException {
    reader.read(INPUT_DIRECTORY);
  }

}
