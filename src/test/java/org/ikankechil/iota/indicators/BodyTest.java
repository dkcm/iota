/**
 * BodyTest.java  0.1  19 December 2016 6:50:06 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * JUnit test for <code>Body</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class BodyTest extends AbstractIndicatorTest {

  public BodyTest() {
    super(0);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = BodyTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Ignore
  @Override
  @Test
  public void cannotInstantiateWithNegativePeriod() {
    // not supported
  }

}
