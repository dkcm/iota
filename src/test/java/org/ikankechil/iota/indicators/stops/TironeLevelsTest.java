/**
 * TironeLevelsTest.java  v0.1  30 April 2019 11:14:08 PM
 *
 * Copyright © 2019-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.stops;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for {@code TironeLevels}.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class TironeLevelsTest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 20;

  public TironeLevelsTest() {
    super(DEFAULT_PERIOD - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = TironeLevelsTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
