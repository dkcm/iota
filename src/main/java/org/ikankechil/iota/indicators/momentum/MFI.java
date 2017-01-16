/**
 * MFI.java  v0.3  5 December 2014 4:09:09 PM
 *
 * Copyright © 2014-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import static org.ikankechil.iota.indicators.TypicalPrice.*;
import static org.ikankechil.iota.indicators.momentum.RSI.*;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Money Flow Index (MFI) by Gene Quong and Avrum Soudack
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:money_flow_index_mfi<br>
 * http://finviz.com/help/technical-analysis/volume.ashx<br>
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class MFI extends AbstractIndicator {

  public MFI() {
    this(FOURTEEN);
  }

  public MFI(final int period) {
    super(period, period);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // 1. Typical Price = (High + Low + Close) / 3
    // 2. Raw Money Flow = Typical Price * Volume
    // 3. Money Flow Ratio = (14-period Positive Money Flow) / (14-period Negative Money Flow)
    // 4. Money Flow Index = 100 - (100 / (1 + Money Flow Ratio))

    // compute Positive and Negative Money Flows
    final double[] pmf = new double[ohlcv.size() - ONE];
    final double[] nmf = new double[pmf.length];

    int p = ZERO;
    double high = ohlcv.high(p);
    double low = ohlcv.low(p);
    double close = ohlcv.close(p);
    double yesterday = typicalPrice(high, low, close);

    for (int i = ZERO; i < pmf.length; ++i) {
      high = ohlcv.high(++p);
      low = ohlcv.low(p);
      close = ohlcv.close(p);
      final double today = typicalPrice(high, low, close);

      final double mf = today * ohlcv.volume(p); // raw money flow
      if (today > yesterday) {
        pmf[i] = mf;
      }
      else if (today < yesterday) {
        nmf[i] = mf;
      }

      // shift forward in time
      yesterday = today;
    }

    // sum Positive and Negative Money Flows
    final double[] spmf = sum(period, pmf);
    final double[] snmf = sum(period, nmf);

    // compute Money Flow Index
    for (int i = ZERO; i < output.length; ++i) {
      output[i] = rsi(spmf[i], snmf[i]);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
