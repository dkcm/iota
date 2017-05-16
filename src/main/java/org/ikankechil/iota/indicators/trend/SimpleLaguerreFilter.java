/**
 * SimpleLaguerreFilter.java  v0.1  13 May 2017 12:02:54 am
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.LaguerreFilter;

/**
 * Simple Laguerre Filter by John Ehlers
 *
 * <p>http://www.mesasoftware.com/papers/TimeWarp.pdf<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class SimpleLaguerreFilter extends LaguerreFilter {

  private final double        weight0;
  private final double        weight1;
  private final double        weight2;
  private final double        weight3;
  private final double        inverseWeights;

  private static final double GAMMA = 0.8;

  public SimpleLaguerreFilter() {
    this(ONE, TWO, TWO, ONE, GAMMA);
  }

  /**
   *
   *
   * @param gamma damping factor
   */
  public SimpleLaguerreFilter(final double weight0, final double weight1, final double weight2, final double weight3, final double gamma) {
    super(gamma);
    throwExceptionIfNegative(weight0, weight1, weight2, weight3);

    this.weight0 = weight0;
    this.weight1 = weight1;
    this.weight2 = weight2;
    this.weight3 = weight3;

    inverseWeights = ONE / (weight0 + weight1 + weight2 + weight3);
  }

  @Override
  protected double filter(final double l0, final double l1, final double l2, final double l3) {
    return (weight0 * l0 + weight1 * l1 + weight2 * l2 + weight3 * l3) * inverseWeights;
  }

}
