/**
 * MaddoxMomentumTest.java  v0.2  24 November 2015 4:12:34 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>MaddoxMomentum</code>.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class MaddoxMomentumTest extends AbstractIndicatorTest {

  public MaddoxMomentumTest() {
    super(10);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = MaddoxMomentumTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
