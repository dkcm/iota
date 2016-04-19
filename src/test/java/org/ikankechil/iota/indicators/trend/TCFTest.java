/**
 * TCFTest.java v0.2 9 December 2015 4:14:25 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>TCF</code>.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class TCFTest extends AbstractIndicatorTest {

  public TCFTest() {
    super(35);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    TEST_CLASS = TCFTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
