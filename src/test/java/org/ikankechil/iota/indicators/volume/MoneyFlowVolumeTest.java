/**
 * MoneyFlowVolumeTest.java  v0.3  7 December 2015 6:10:20 PM
 *
 * Copyright � 2015-2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>MoneyFlowVolume</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class MoneyFlowVolumeTest extends AbstractIndicatorTest {

  public MoneyFlowVolumeTest() {
    super(0);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = MoneyFlowVolumeTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
