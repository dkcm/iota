/**
 * AccumulativeSwingIndexTest.java  v0.1  24 June 2019 12:12:23 AM
 *
 * Copyright © 2019-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for {@code AccumulativeSwingIndex}.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class AccumulativeSwingIndexTest extends AbstractIndicatorTest {

  public AccumulativeSwingIndexTest() {
    super(1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = AccumulativeSwingIndexTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
