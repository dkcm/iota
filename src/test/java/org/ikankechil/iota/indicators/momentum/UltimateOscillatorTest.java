/**
 * UltimateOscillatorTest.java  v0.2  11 July 2015 3:43:16 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>UltimateOscillator</code>.
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class UltimateOscillatorTest extends AbstractIndicatorTest {

  public UltimateOscillatorTest() {
    super(28);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = UltimateOscillatorTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance(final int period) {
    return new UltimateOscillator(period, period * 2, period * 4);
  }

}
