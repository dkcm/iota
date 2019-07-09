/**
 * TVITest.java  v0.1  8 July 2019 11:12:33 PM
 *
 * Copyright © 2019-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for {@code TVI}.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class TVITest extends AbstractIndicatorTest {

  public TVITest() {
    super(0);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = TVITest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
