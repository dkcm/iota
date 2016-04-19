/**
 * ForceIndexTest.java v0.2 7 December 2015 7:30:28 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>ForceIndex</code>.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class ForceIndexTest extends AbstractIndicatorTest {

  public ForceIndexTest() {
    super(13);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    TEST_CLASS = ForceIndexTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
