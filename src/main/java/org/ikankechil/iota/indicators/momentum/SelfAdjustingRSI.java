/**
 * SelfAdjustingRSI.java  v0.5  14 January 2015 2:05:43 PM
 *
 * Copyright © 2015-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.indicators.volatility.StandardDeviation;

/**
 * Self-Adjusting RSI by David Sepiashvili
 *
 * <p>References:
 * <li>ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V24/C02/023SEPI.pdf
 * <li>ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V24/C02/037TIPS.pdf
 * <li>http://www.traders.com/documentation/feedbk_docs/2006/02/TradersTips/TradersTips.html<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.5
 */
public class SelfAdjustingRSI extends AbstractIndicator {

  private final Indicator     rsi;
  private final Indicator     stdDev;

  private static final double K           = 1.8;
  private static final int    CENTRE_LINE = FIFTY;

  private static final String MIDDLE_BAND = "Self-Adjusting RSI";
  private static final String UPPER_BAND  = MIDDLE_BAND + " Overbought";
  private static final String LOWER_BAND  = MIDDLE_BAND + " Oversold";

  public SelfAdjustingRSI() {
    this(FOURTEEN);
  }

  public SelfAdjustingRSI(final int period) {
    this(period, K);
  }

  public SelfAdjustingRSI(final int period, final double k) {
    super(period, (period << ONE) - ONE);
    throwExceptionIfNegative(k);

    rsi = new RSI(period);
    stdDev = new StandardDeviation(period, k);
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    // Formulae:
    // Version 1
    // Upper = 50 + k * SDn(RSIn)
    // Lower = 50 – k * SDn(RSIn)
    //
    // Version 2
    // Upper = 50 + c * SMAn(|RSIn – SMAn(RSIn)|)
    // Lower = 50 – c * SMAn(|RSIn – SMAn(RSIn)|)

    throwExceptionIfShort(ohlcv);

    // compute RSI and standard deviation of RSI
    final TimeSeries rsis = rsi.generate(ohlcv, start).get(ZERO);
    final double[] stdDevRsi = stdDev.generate(rsis).get(ZERO).values();

    // compute indicator
    final double[] overbought = new double[stdDevRsi.length];
    final double[] oversold = new double[overbought.length];
    for (int i = ZERO; i < overbought.length; ++i) {
      // using the standard deviation method
      final double kStdDevRsi = stdDevRsi[i];

      overbought[i] = CENTRE_LINE + kStdDevRsi;
      oversold[i]   = CENTRE_LINE - kStdDevRsi;
    }

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, ohlcv.size());

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(UPPER_BAND,
                                        dates,
                                        overbought),
                         new TimeSeries(MIDDLE_BAND,
                                        dates,
                                        Arrays.copyOfRange(rsis.values(),
                                                           stdDev.lookback(),
                                                           rsis.size())),
                         new TimeSeries(LOWER_BAND,
                                        dates,
                                        oversold));
  }

}
