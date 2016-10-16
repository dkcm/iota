/**
 * VolumeTest.java  v0.1  11 September 2016 12:24:39 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import java.io.IOException;

import org.junit.BeforeClass;

/**
 * JUnit test for <code>Volume</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class VolumeTest extends AbstractIndicatorTest {

  public VolumeTest() {
    super(0);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = VolumeTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
