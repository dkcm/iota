/**
 * MarketFacilitationIndexTest.java  v0.1  15 April 2019 12:31:35 AM
 *
 * Copyright © 2019-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for {@code MarketFacilitationIndex}.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MarketFacilitationIndexTest extends AbstractIndicatorTest {

  public MarketFacilitationIndexTest() {
    super(1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = MarketFacilitationIndexTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
