/**
 * TimeSeriesWriterTest.java  v0.2  20 June 2015 12:05:41 am
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.io;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 *
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public abstract class TimeSeriesWriterTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  static final char        COMMA  = ',';
  static final String      CSV    = ".csv";

  @Test(expected=NullPointerException.class)
  public abstract void cannotWriteNullPayload() throws IOException;

  @Test(expected=NullPointerException.class)
  public abstract void cannotWriteToNullFile() throws IOException;

  @Test(expected=FileNotFoundException.class)
  public abstract void cannotWriteToUnwritableFile() throws IOException;

  @Test(expected=FileNotFoundException.class)
  public abstract void cannotWriteToDirectory() throws IOException;

}
