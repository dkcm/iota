/**
 * UlcerIndexTest.java v0.2 24 November 2015 5:44:47 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>UlcerIndex</code>.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class UlcerIndexTest extends AbstractIndicatorTest {

  public UlcerIndexTest() {
    super(26);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = UlcerIndexTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
