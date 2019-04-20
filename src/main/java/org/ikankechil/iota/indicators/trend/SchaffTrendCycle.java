/**
 * SchaffTrendCycle.java  v0.1  11 April 2019 11:54:34 PM
 *
 * Copyright © 2019-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import static org.ikankechil.iota.indicators.momentum.FastStochastic.*;
import static org.ikankechil.iota.indicators.trend.MACD.*;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Schaff Trend Cycle by Doug Schaff
 *
 * <p>References:
 * <li>https://www.technicalindicators.net/indicators-technical-analysis/169-stc-schaff-trend-cycle
 * <li>https://www.investopedia.com/articles/forex/10/schaff-trend-cycle-indicator.asp
 * <li>https://www.tradingview.com/script/dbxXeuw2-Indicator-Schaff-Trend-Cycle-STC/
 * <li>http://forex-indicators.net/cycle-indicators/schaff-trend-cycle
 * <li>http://exceltechnical.web.fc2.com/stc.html<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class SchaffTrendCycle extends AbstractIndicator {

  private final int fast;
  private final int slow;
  private final int k;
  private final int d;

  public SchaffTrendCycle() {
    this(TWENTY + THREE, FIFTY, TEN, TEN);
  }

  public SchaffTrendCycle(final int fast, final int slow, final int k, final int d) {
    super(Math.max(fast, slow) - ONE + (k + d - TWO) * TWO);
    throwExceptionIfNegative(fast, slow, k, d);

    this.fast = fast;
    this.slow = slow;
    this.k = k;
    this.d = d;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // 1. MACD
    // 2. PF = Stochastic of #1
    // 3. Stochastic of #2
    // STC = %D of #3

    final double[] macd = macd(fast, slow, ohlcv.closes());
    final double[] pf = stochastic(macd);
    final double[] pff = stochastic(pf);
    System.arraycopy(pff, ZERO, output, ZERO, output.length);

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  private double[] stochastic(final double[] values) {
    final double[] fastKs = fastStochasticK(k, values, values, values);
    return sma(fastKs, d);
  }

}
