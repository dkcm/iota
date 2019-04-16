/**
 * StandardErrorBands.java  v0.2  26 September 2016 4:45:48 pm
 *
 * Copyright © 2016-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.indicators.trend.LinearRegression;

/**
 * Standard Error Bands by Jon Andersen
 *
 * <p>References:
 * <li>ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V14/C09/STANDAR.pdf
 * <li>ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V14/C09/SIDESTA.pdf
 * <li>ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V14/C09/TRADERS.pdf
 * <li>ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V14/C10/TRADERS.pdf
 * <li>https://reference.wolfram.com/language/ref/indicator/StandardErrorBands.html
 * <li>http://www.vantharp.com/Tharps-Thoughts/635_June_26_2013.html<br>
 * <br>
 *
 * <p>Strategy:
 * <li>http://www.vantharp.com/Tharps-Thoughts/637_July_10_2013.html<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class StandardErrorBands extends AbstractIndicator {

  private final Indicator     linearRegression;
  private final Indicator     standardError;
  private final int           smooth;

  private static final String UPPER_BAND  = "Standard Error Bands Upper Band";
  private static final String MIDDLE_BAND = "Standard Error Bands Middle Band";
  private static final String LOWER_BAND  = "Standard Error Bands Lower Band";

  public StandardErrorBands() {
    this(TWENTY_ONE, THREE, TWO); // recommended periods: 21, 34
  }

  public StandardErrorBands(final int period, final int smooth, final double standardErrorMultiplier) {
    super(period, ((period - ONE) << ONE) + (smooth - ONE));
    throwExceptionIfNegative(smooth, standardErrorMultiplier);

    linearRegression = new LinearRegression(period);
    standardError = new StandardError(period, standardErrorMultiplier);
    this.smooth = smooth;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    // Formula:
    // Standard Error bands were invented by Jon Andersen as a trend following
    // indicator. SE Bands are built around a linear regression line using the
    // standard error of regression. First the linear regression value is
    // calculated. Then a short term simple moving average of it is calculated
    // to smooth it out. Next, the standard error value is calculated and
    // smoothed similarly. Then, the first average is plotted and the second
    // average (SMA of SE) is multiplied by ±2 and plotted up and down.

    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();

    final double[] upperBand = new double[size - lookback];
    final double[] lowerBand = new double[upperBand.length];

    // compute linear regression moving average
    final TimeSeries lr = linearRegression.generate(ohlcv, start).get(ZERO);
    final double[] lrma = sma(lr.values(), smooth);

    // compute standard error moving average
    final double[] se = standardError.generate(lr).get(ZERO).values();
    final double[] sema = sma(se, smooth);
    for (int i = ZERO, j = standardError.lookback(); i < upperBand.length; ++i, ++j) {
      final double seb = sema[i]; // multiplier already applied
      upperBand[i] = lrma[j] + seb;
      lowerBand[i] = lrma[j] - seb;
    }

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(UPPER_BAND,
                                        dates,
                                        upperBand),
                         new TimeSeries(MIDDLE_BAND,
                                        dates,
                                        Arrays.copyOfRange(lrma,
                                                           standardError.lookback(),
                                                           lrma.length)),
                         new TimeSeries(LOWER_BAND,
                                        dates,
                                        lowerBand));
  }

}
