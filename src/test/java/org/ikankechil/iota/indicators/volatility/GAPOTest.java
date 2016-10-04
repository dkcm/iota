/**
 * GAPOTest.java  v0.1  4 October 2016 11:54:32 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>GAPO</code>.
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class GAPOTest extends AbstractIndicatorTest {

  public GAPOTest() {
    super(4);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = GAPOTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
