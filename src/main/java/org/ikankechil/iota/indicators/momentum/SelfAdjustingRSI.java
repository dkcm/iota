/**
 * SelfAdjustingRSI.java v0.3 14 January 2015 2:05:43 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Self-Adjusting RSI by David Sepiashvili
 * <p>
 * {@link ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V24/C02/023SEPI.pdf}
 * {@link ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V24/C02/037TIPS.pdf}
 * {@link http://www.traders.com/documentation/feedbk_docs/2006/02/TradersTips/TradersTips.html}
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class SelfAdjustingRSI extends AbstractIndicator {

  private final double        k;
  private final int           stdDevLookback;
  private final int           rsiLookback;
  private final RSI           rsiIndicator;

  private static final int    CENTRE_LINE = 50;

  private static final String MIDDLE_BAND = "Self-Adjusting RSI";
  private static final String UPPER_BAND  = MIDDLE_BAND + " Overbought";
  private static final String LOWER_BAND  = MIDDLE_BAND + " Oversold";

  public SelfAdjustingRSI() {
    this(FOURTEEN);
  }

  public SelfAdjustingRSI(final int period) {
    this(period, 1.8);
  }

  public SelfAdjustingRSI(final int period, final double k) {
    super(period, TA_LIB.stdDevLookback(period, k) + TA_LIB.rsiLookback(period));
    throwExceptionIfNegative(k);

    this.k = k;
    rsiLookback = TA_LIB.rsiLookback(period);
    stdDevLookback = lookback - rsiLookback;

    rsiIndicator = new RSI(period);
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    // Formulae:
    // Version 1
    // Upper = 50 + k * SDn(RSIn)
    // Lower = 50 – k * SDn(RSIn)
    //
    // Version 2
    // Upper = 50 + c * SMAn(|RSIn – SMAn(RSIn)|)
    // Lower = 50 – c * SMAn(|RSIn – SMAn(RSIn)|)

    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();

    final MInteger outBegIdx = new MInteger();
    final MInteger outNBElement = new MInteger();

    // compute RSI
    final double[] rsi = new double[size - rsiLookback];
    RetCode outcome = rsiIndicator.compute(ZERO,
                                           size - ONE,
                                           ohlcv.closes(),
                                           outBegIdx,
                                           outNBElement,
                                           rsi);
    throwExceptionIfBad(outcome, ohlcv);

    // compute standard deviation of RSI
    final double[] stdDevRsi = new double[size - lookback];
    outcome = TA_LIB.stdDev(ZERO,
                            rsi.length - ONE,
                            rsi,
                            period,
                            k,
                            outBegIdx,
                            outNBElement,
                            stdDevRsi);
    throwExceptionIfBad(outcome, ohlcv);

    // compute indicator
    final double[] overbought = new double[stdDevRsi.length];
    final double[] oversold = new double[overbought.length];
    for (int i = ZERO; i < overbought.length; ++i) {
      // using the standard deviation method
      final double kStdDevRsi = stdDevRsi[i];

      overbought[i] = CENTRE_LINE + kStdDevRsi;
      oversold[i]   = CENTRE_LINE - kStdDevRsi;
    }

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(UPPER_BAND,
                                        dates,
                                        overbought),
                         new TimeSeries(MIDDLE_BAND,
                                        dates,
                                        Arrays.copyOfRange(rsi,
                                                           stdDevLookback,
                                                           rsi.length)),
                         new TimeSeries(LOWER_BAND,
                                        dates,
                                        oversold));
  }

}
