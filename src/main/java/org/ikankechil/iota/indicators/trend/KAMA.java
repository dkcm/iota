/**
 * KAMA.java  v0.3  9 December 2014 12:18:38 PM
 *
 * Copyright © 2014-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Kaufman Adaptive Moving Average (KAMA) by Perry Kaufman
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:kaufman_s_adaptive_moving_average<br>
 * http://www.investopedia.com/articles/trading/08/adaptive-moving-averages.asp<br>
 * http://exceltechnical.web.fc2.com/kama.html<br>
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class KAMA extends AbstractIndicator implements MA {

//  private final double    scDiff;
//  private final double    slowestSC;
//  private final Indicator er;

  public KAMA() {
    this(TEN);
  }

  public KAMA(final int period) {
    this(period, TWO, THIRTY);
  }

  public KAMA(final int period, final int fastest, final int slowest) {
    super(period, period);
    throwExceptionIfNegative(fastest, slowest);

//    final double fsc = TWO / (double) (fastest + ONE);
//    slowestSC = TWO / (double) (slowest + ONE);
//
//    scDiff = fsc - slowestSC;
//
//    er = new EfficiencyRatio(period);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // Current KAMA = Prior KAMA + SC x (Price - Prior KAMA)
    //
    // where
    // SC = [ER x (fastest SC - slowest SC) + slowest SC]^2
    // ER = Change/Volatility
    // Change = ABS(Close - Close(10 periods ago))
    // Volatility = Sum10(ABS(Close - Prior Close))

//    final double[] efficiencyRatio = er.generate(new TimeSeries(name, new String[values.length], values)).get(ZERO).values();
//    final double[] efficiencyRatio = computeEfficiencyRatio(period, values);
//
//    // first value is SMA
//    int v = ZERO;
//    double kama = values[v];
//    while (++v < period) {
//      kama += values[v];
//    }
//    int i = ZERO;
//    output[i] = kama /= period;
//
//    // compute indicator
//    while (++i < output.length) {
//      final double sc = (efficiencyRatio[i] * scDiff) + slowestSC;
//      output[i] = kama = EMA.ema(sc * sc, values[v++], kama);
//    }
//
//    outBegIdx.value = lookback;
//    outNBElement.value = output.length;
//    return RetCode.Success;

    return TA_LIB.kama(start,
                       end,
                       values,
                       period,
                       outBegIdx,
                       outNBElement,
                       output);
  }

  private static final double[] computeEfficiencyRatio(final int period, final double[] values) {
    final double[] change = computeChange(period, values);
    final double[] volatility = computeVolatility(period, values);

    final double[] efficiencyRatio = new double[change.length];
    for (int i = ZERO, v = volatility.length - change.length;
         i < efficiencyRatio.length;
         ++i, ++v) {
      efficiencyRatio[i] = change[i] / volatility[v];
    }

    return efficiencyRatio;
  }

  private static final double[] computeChange(final int period, final double[] values) {
    final double[] change = new double[values.length - period];
    for (int i = ZERO, v = period; i < change.length; ++i, ++v) {
      change[i] = Math.abs(values[v] - values[i]);
    }
    return change;
  }

  private static final double[] computeVolatility(final int period, final double[] values) {
    final double[] roc1 = new double[values.length - ONE];
    for (int i = ZERO; i < roc1.length; ) {
      roc1[i] = Math.abs(-values[i] + values[++i]);
    }
    final double[] volatility = sum(period, roc1);
    return volatility;
  }

}
