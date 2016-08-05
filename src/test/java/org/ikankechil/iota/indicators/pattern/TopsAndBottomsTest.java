/**
 * TopsAndBottomsTest.java  v0.2  31 December 2015 7:58:28 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>TopsAndBottoms</code>.
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class TopsAndBottomsTest extends AbstractIndicatorTest {

  public TopsAndBottomsTest() {
    super(0);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = TopsAndBottomsTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
