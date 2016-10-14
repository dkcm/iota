/**
 * MFI.java  v0.2  5 December 2014 4:09:09 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.TypicalPrice;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Money Flow Index (MFI) by Gene Quong and Avrum Soudack
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:money_flow_index_mfi<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class MFI extends AbstractIndicator {

  private static final TypicalPrice TP = new TypicalPrice();

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

    final TimeSeries typicalPrices = TP.generate(ohlcv).get(ZERO);

    // compute Positive and Negative Money Flows
    final double[] pmf = new double[ohlcv.size() - ONE];
    final double[] nmf = new double[pmf.length];
    int p = ZERO;
    double yesterday = typicalPrices.value(p);
    for (int i = ZERO; i < pmf.length; ++i) {
      final double today = typicalPrices.value(++p);

      if (today > yesterday) {
        pmf[i] = today * ohlcv.volume(p);
      }
      else if (today < yesterday) {
        nmf[i] = today * ohlcv.volume(p);
      }

      // shift forward in time
      yesterday = today;
    }

    // sum Positive and Negative Money Flows
    final double[] spmf = sum(period, pmf);
    final double[] snmf = sum(period, nmf);

    // compute Money Flow Index
    for (int i = ZERO; i < output.length; ++i) {
      output[i] = RSI.rsi(spmf[i], snmf[i]);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;

//    return TA_LIB.mfi(start,
//                      end,
//                      ohlcv.highs(),
//                      ohlcv.lows(),
//                      ohlcv.closes(),
//                      toDoubles(ohlcv.volumes()), // copy volumes
//                      period,
//                      outBegIdx,
//                      outNBElement,
//                      output);
  }

}
