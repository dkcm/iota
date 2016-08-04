/**
 * KVOTest.java	v0.2	29 November 2015 7:05:31 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * JUnit test for <code>KVO</code>.
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class KVOTest extends AbstractIndicatorTest {

  public KVOTest() {
    super(110);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = KVOTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Ignore@Override@Test
  public void cannotInstantiateWithNegativePeriod() {
    // not supported
  }

}
