/**
 * CompositeIndicatorTest.java  v0.1  8 May 2017 10:49:17 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * JUnit test for <code>CompositeIndicator</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class CompositeIndicatorTest extends AbstractIndicatorTest {

  private static final int                PERIOD              = 14;

  private static final CompositeIndicator COMPOSITE_INDICATOR = new CompositeIndicator(new MaximumPrice(PERIOD),
                                                                                       new ConstantValue(PERIOD));

  public CompositeIndicatorTest() {
    super(PERIOD - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = CompositeIndicatorTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance() throws ReflectiveOperationException {
    return COMPOSITE_INDICATOR;
  }

  @Override
  @Test
  public void name() {
    assertEquals("MaximumPrice", COMPOSITE_INDICATOR.toString());
  }

}
