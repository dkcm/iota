/**
 * AbstractStrategyTest.java  v0.1  8 December 2016 12:17:23 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract superclass for all <code>Strategy</code> JUnit tests.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class AbstractStrategyTest {

  private static final File       INPUT_DIRECTORY = new File(".//./src/test/resources/" + AbstractStrategyTest.class.getSimpleName());
  private static final File       OHLCV_FILE      = new File(INPUT_DIRECTORY, "SPY_20001220-20161206_w.csv");

  private static final double     DELTA           = 1e-6;

  private static final Logger     logger          = LoggerFactory.getLogger(AbstractStrategyTest.class);

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void test() {
    fail("Not yet implemented");
  }

}
