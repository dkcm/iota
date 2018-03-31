/**
 * FibonacciRetracementsTest.java  v0.1  31 March 2018 10:42:11 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.stops;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>FibonacciRetracements</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class FibonacciRetracementsTest extends AbstractIndicatorTest {

  public FibonacciRetracementsTest() {
    super(0);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = FibonacciRetracementsTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
