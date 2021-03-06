/**
 * EhlersDistanceCoefficientFilter.java  v0.3  8 July 2015 3:07:41 PM
 *
 * Copyright � 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.indicators.EhlersFilter;

/**
 * Ehlers Distance Coefficient Filter by John Ehlers.
 *
 * <p>http://www.mesasoftware.com/papers/EhlersFilters.pdf<br>
 * http://traders.com/Documentation/FEEDbk_docs/2001/04/TradersTips/TradersTips.html<br>
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V19/C04/040NON.pdf<br>
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class EhlersDistanceCoefficientFilter extends EhlersFilter {

  // cache to reduce coefficient recalculation
  private double[] coefficients;

//  private static final String DISTANCE_COEFFICIENTS = "Distance Coefficients[{}] = {}";

  public EhlersDistanceCoefficientFilter(final int period) {
    super(period, period, (period << ONE) - ONE); // lookback = 2 * period - 1
  }

  @Override
  protected double[] coefficients(final int index, final double... values) {
    // Formula:
    // sum of the distances squared at each data point are the coefficients of
    // the Ehlers filter
    if (coefficients == null) {
      // populate
      coefficients = new double[period];
      for (int c = ZERO, v = index + period; c < coefficients.length; ++c, ++v) {
        // coefficient = sum of distances squared
        coefficients[c] = sumOfDistancesSquared(period, v, values);
      }
    }
    else {
      // shift
      final int c = period - ONE;
      System.arraycopy(coefficients, ONE, coefficients, ZERO, c);
      coefficients[c] = sumOfDistancesSquared(period, index + period + c, values);
    }
//    logger.debug(DISTANCE_COEFFICIENTS, index, coefficients);

    return coefficients;
  }

  private static final double sumOfDistancesSquared(final int period,
                                                    final int index,
                                                    final double... values) {
    double sum = ZERO;
    final double value = values[index];
    for (int i = index - period; i < index; ++i) {
      final double distance = (value - values[i]);
      sum += (distance * distance);
    }
    return sum;
  }

  @Override
  protected void cleanUp() {
    // clear distance coefficients
    coefficients = null;
  }

}
