/**
 * CGOscillatorTest.java  v0.1  31 March 2018 2:32:44 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>CGOscillator</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class CGOscillatorTest extends AbstractIndicatorTest {

  private static final int PERIOD = 10;

  public CGOscillatorTest() {
    super(PERIOD - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = CGOscillatorTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
