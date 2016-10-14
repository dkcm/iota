/**
 * MACD.java  v0.1  27 November 2014 1:06:03 am
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Moving Average Convergence/Divergence (MACD) by Gerald Appel
 *
 * <p><a href=
 * "http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:moving_average_convergence_divergence_macd"
 * >http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:
 * moving_average_convergence_divergence_macd</a><br>
 * <a href="http://www.investopedia.com/terms/m/macd.asp">http://www.investopedia.com/terms/m/macd.asp</a>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MACD extends AbstractIndicator {

  private final int           fast;
  private final int           slow;
  private final int           signal;

  private static final String MACD_SIGNAL    = "MACD Signal";
  private static final String MACD_HISTOGRAM = "MACD Histogram";

  public MACD() {
    this(TWELVE, TWENTY_SIX, NINE);
  }

  public MACD(final int fast, final int slow, final int signal) {
    super(TA_LIB.macdLookback(fast, slow, signal));
    throwExceptionIfNegative(fast, slow, signal);

    this.fast = fast;
    this.slow = slow;
    this.signal = signal;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    // Formula:
    // MACD Line: (12-day EMA - 26-day EMA)
    // Signal Line: 9-day EMA of MACD Line
    // MACD Histogram: MACD Line - Signal Line

    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();

    final MInteger outBegIdx = new MInteger();
    final MInteger outNBElement = new MInteger();

    final double[] outMACD = new double[size - lookback];
    final double[] outMACDSignal = new double[outMACD.length];
    final double[] outMACDHist = new double[outMACD.length];

    final RetCode outcome = TA_LIB.macd(ZERO, // TODO change this to support updates
                                        size - ONE,
                                        ohlcv.closes(),
                                        fast,
                                        slow,
                                        signal,
                                        outBegIdx,
                                        outNBElement,
                                        outMACD,
                                        outMACDSignal,
                                        outMACDHist);
    throwExceptionIfBad(outcome, ohlcv);

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(name, dates, outMACD),
                         new TimeSeries(MACD_SIGNAL, dates, outMACDSignal),
                         new TimeSeries(MACD_HISTOGRAM, dates, outMACDHist));
  }

}
