/**
 * SVEBollingerBTest.java  v0.1  3 April 2018 1:11:44 AM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>SVEBollingerB</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class SVEBollingerBTest extends AbstractIndicatorTest {

  public SVEBollingerBTest() {
    super(163);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = SVEBollingerBTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
