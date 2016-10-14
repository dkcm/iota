/**
 * AroonOscillatorTest.java v0.1	7 September 2016 12:22:01 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>AroonOscillator</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class AroonOscillatorTest extends AbstractIndicatorTest {

  public AroonOscillatorTest() {
    super(25);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = AroonOscillatorTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
