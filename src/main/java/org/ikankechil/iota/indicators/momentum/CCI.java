/**
 * CCI.java  v0.2  4 December 2014 1:38:57 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.TypicalPrice;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Commodity Channel Index (CCI) by Donald Lambert
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:commodity_channel_index_cci<br>
 * http://www.mboot.com/PDF/ccicycliCert.pdf<br>
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V13/C12/THECOMM.pdf<br>
 * https://www.cornertrader.ch/export/sites/cornertrader/.content/.galleries/downloads/PDF/en/tutorials/2-3-technical-analysis-technical-indicators.pdf<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class CCI extends AbstractIndicator {

  private final double              cciFactor;

  private static final TypicalPrice TYPICAL_PRICE = new TypicalPrice();
  private static final double       CCI_CONSTANT  = 0.015;

  public CCI() {
    this(TWENTY);
  }

  public CCI(final int period) {
    this(period, CCI_CONSTANT);
  }

  public CCI(final int period, final double cciConstant) {
    super(period, period - ONE);
    throwExceptionIfNegative(cciConstant);

    cciFactor = period / cciConstant;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // CCI = (Typical Price - 20-period SMA of TP) / (.015 x Mean Deviation)
    // Typical Price (TP) = (High + Low + Close)/3
    // Constant = .015
    //
    // There are four steps to calculating the Mean Deviation. First, subtract
    // the most recent 20-period average of the typical price from each period's
    // typical price. Second, take the absolute values of these numbers. Third,
    // sum the absolute values. Fourth, divide by the total number of periods
    // (20).

    final double[] typicalPrices = TYPICAL_PRICE.generate(ohlcv).get(ZERO).values();
    final double[] smaTypicalPrices = sma(typicalPrices, period);

    for (int i = ZERO, j = i + lookback; i < output.length; ++i, ++j) {
      final double smaTypicalPrice = smaTypicalPrices[i];
      final double deviation = typicalPrices[j] - smaTypicalPrice;

      double sumAbsDeviation = Math.abs(deviation);
      for (int k = i; k < j; ++k) {
        sumAbsDeviation += Math.abs(typicalPrices[k] - smaTypicalPrice);
      }

      output[i] = deviation * cciFactor / sumAbsDeviation;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
