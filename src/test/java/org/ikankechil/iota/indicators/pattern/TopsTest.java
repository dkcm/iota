/**
 * TopsTest.java	v0.1	8 January 2016 12:09:20 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>Tops</code>.
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class TopsTest extends AbstractIndicatorTest {

  public TopsTest() {
    super(0);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    TEST_CLASS = TopsTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
