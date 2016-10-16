/**
 * ChaikinOscillatorTest.java  v0.1  15 August 2016 7:36:21 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>ChaikinOscillator</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ChaikinOscillatorTest extends AbstractIndicatorTest {

  public ChaikinOscillatorTest() {
    super(9);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = ChaikinOscillatorTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
