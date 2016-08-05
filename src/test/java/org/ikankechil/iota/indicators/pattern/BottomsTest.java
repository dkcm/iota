/**
 * BottomsTest.java  v0.1  8 January 2016 12:10:24 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 *
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class BottomsTest extends AbstractIndicatorTest {

  public BottomsTest() {
    super(0);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = BottomsTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
