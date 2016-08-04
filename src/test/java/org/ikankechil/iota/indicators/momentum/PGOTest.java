/**
 * PGOTest.java v0.2 Nov 26, 2015 4:27:17 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>PGO</code>.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class PGOTest extends AbstractIndicatorTest {
  // TODO test data expects EMA in ATR, not TA-Lib's SMA

  public PGOTest() {
    super(25);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = PGOTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
