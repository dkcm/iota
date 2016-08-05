/**
 * AwesomeOscillatorTest.java  v0.2  10 January 2015 2:43:37 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>AwesomeOscillator</code>.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class AwesomeOscillatorTest extends AbstractIndicatorTest {

//  private static final int FAST = 5;
  private static final int SLOW = 34;

  public AwesomeOscillatorTest() {
    super(SLOW - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = AwesomeOscillatorTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance(final int period) {
    return new AwesomeOscillator(period, SLOW);
  }

}
