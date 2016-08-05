/**
 * VZOTest.java  v0.2  10 December 2015 9:30:52 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>VZO</code>.
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class VZOTest extends AbstractIndicatorTest {

  public VZOTest() {
    super(13);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = VZOTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
