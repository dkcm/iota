/**
 * TimeSeriesWriterTest.java	v0.1	20 June 2015 12:05:41 am
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.io;

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
 * @version 0.1
 */
public abstract class TimeSeriesWriterTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  static final char        COMMA  = ',';
  static final String      CSV    = ".csv";

  @Test
  public abstract void cannotWriteNullPayload() throws IOException;

  @Test
  public abstract void cannotWriteToNullFile() throws IOException;

  @Test
  public abstract void cannotWriteToUnwritableFile() throws IOException;

  @Test
  public abstract void cannotWriteToDirectory() throws IOException;

}
