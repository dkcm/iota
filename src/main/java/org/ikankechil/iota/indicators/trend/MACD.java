/**
 * MACD.java  v0.4  27 November 2014 1:06:03 am
 *
 * Copyright © 2014-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.IndicatorWithSignalLineAndHistogram;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Moving Average Convergence/Divergence (MACD) by Gerald Appel
 *
 * <p>References:
 * <li>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:moving_average_convergence_divergence_macd
 * <li>http://www.investopedia.com/terms/m/macd.asp
 * <li>https://www.fidelity.com/learning-center/trading-investing/technical-analysis/technical-indicator-guide/macd
 * <li>http://www.forexabode.com/forex-school/technical-indicators/macd/<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.4
 */
public class MACD extends IndicatorWithSignalLineAndHistogram {

  private final int fast;
  private final int slow;

  public MACD() {
    this(TWELVE, TWENTY_SIX, NINE);
  }

  public MACD(final int fast, final int slow, final int signal) {
    super(signal, Math.max(fast, slow) + signal - TWO);
    throwExceptionIfNegative(fast, slow, signal);

    this.fast = fast;
    this.slow = slow;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // MACD Line: (12-day EMA - 26-day EMA)
    // Signal Line: 9-day EMA of MACD Line
    // MACD Histogram: MACD Line - Signal Line

    // compute fast and slow EMAs
    final double[] fastEMAs = smoothenPrices(fast, ohlcv);
    final double[] slowEMAs = smoothenPrices(slow, ohlcv);

    // compute MACD
    final double[] macd = difference(fastEMAs, slowEMAs);
    System.arraycopy(macd, ZERO, output, ZERO, output.length);

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  double[] smoothenPrices(final int smoothingPeriod, final OHLCVTimeSeries ohlcv) {
    return ema(ohlcv.closes(), smoothingPeriod);
  }

  public static final double[] macd(final int fast, final int slow, final double... closes) {
    final double[] fastEMAs = ema(closes, fast);
    final double[] slowEMAs = ema(closes, slow);
    return difference(fastEMAs, slowEMAs);
  }

}
