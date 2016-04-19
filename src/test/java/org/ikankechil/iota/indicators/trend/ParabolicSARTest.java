/**
 * ParabolicSARTest.java v0.2 26 November 2015 11:02:43 AM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * JUnit test for <code>ParabolicSAR</code>.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class ParabolicSARTest extends AbstractIndicatorTest {

  public ParabolicSARTest() {
    super(1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    TEST_CLASS = ParabolicSARTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Ignore@Override@Test
  public void cannotInstantiateWithNegativePeriod() {
    // not supported
  }

}
