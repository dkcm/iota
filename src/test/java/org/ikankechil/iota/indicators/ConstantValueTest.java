/**
 * ConstantValueTest.java  v0.1  8 May 2017 10:49:46 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>ConstantValue</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ConstantValueTest extends AbstractIndicatorTest {

  private static final double CONSTANT = 123.456;

  public ConstantValueTest() {
    super(0);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = ConstantValueTest.class;
  }

  @Override
  public Indicator newInstance() throws ReflectiveOperationException {
    return new ConstantValue(CONSTANT);
  }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    final double[] values = new double[series.size()];
    Arrays.fill(values, CONSTANT);
    return Arrays.asList(new TimeSeries("ConstantValue",
                                        series.dates(),
                                        values));
  }

}
