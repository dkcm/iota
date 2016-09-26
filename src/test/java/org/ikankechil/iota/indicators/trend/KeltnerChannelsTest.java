/**
 * KeltnerChannelsTest.java  v0.1  26 September 2016 9:46:25 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>KeltnerChannels</code>.
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class KeltnerChannelsTest extends AbstractIndicatorTest {

  public KeltnerChannelsTest() {
    super(19);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = KeltnerChannelsTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
