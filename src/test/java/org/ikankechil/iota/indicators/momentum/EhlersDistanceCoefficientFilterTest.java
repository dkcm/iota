/**
 * EhlersDistanceCoefficientFilterTest.java v0.2 9 July 2015 12:55:11 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>EhlersDistanceCoefficientFilter</code>.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class EhlersDistanceCoefficientFilterTest extends AbstractIndicatorTest {

  private static final int PERIOD = 5;

  public EhlersDistanceCoefficientFilterTest() {
    super((PERIOD * 2) - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    TEST_CLASS = EhlersDistanceCoefficientFilterTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance() {
    return new EhlersDistanceCoefficientFilter(PERIOD);
  }

  @Override
  public Indicator newInstance(final int period) {
    return new EhlersDistanceCoefficientFilter(period);
  }

}
