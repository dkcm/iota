/**
 * SVEInverseFisherRSITest.java  0.1  11 July 2017 11:26:53 PM
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>SVEInverseFisherRSI</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class SVEInverseFisherRSITest extends AbstractIndicatorTest {

  public SVEInverseFisherRSITest() {
    super(10 + 4 + 6);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = SVEInverseFisherRSITest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
