/**
 * PriceAccumulationVolumeIndicator.java  v0.1  10 April 2018 10:10:15 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.IndicatorWithSignalLine;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Price Accumulation Volume Indicator by Paul Dysart
 *
 * <p>References:
 * <li>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:negative_volume_inde<br>
 * <li>https://www.metastock.com/customer/resources/taaz/?p=75<br>
 * <li>https://www.investopedia.com/terms/n/nvi.asp<br>
 * <li>https://www.metastock.com/customer/resources/taaz/?p=92<br>
 * <li>https://www.investopedia.com/terms/p/pvi.asp<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
abstract class PriceAccumulationVolumeIndicator extends IndicatorWithSignalLine {

  // scaled down by 100 from original 1000 to reduce computations
  private static final int VI_START = TEN;
  private static final int YEAR     = 255;

  public PriceAccumulationVolumeIndicator() {
    this(YEAR);
  }

  public PriceAccumulationVolumeIndicator(final int signal) {
    super(signal, signal - ONE);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // 1. Cumulative PVI / NVI starts at 1000
    // 2. Add the Percentage Price Change to Cumulative PVI / NVI when Volume Increases / Decreases
    // 3. Cumulative PVI / NVI is Unchanged when Volume Decreases / Increases
    // 4. Apply a 255-day EMA for Signals

    // compute indicator
    int i = ZERO;
    double vi = output[i] = VI_START;
    long pv = ohlcv.volume(i);  // previous volume
    double pc = ohlcv.close(i); // previous close

    while (++i < output.length) {
      final long volume = ohlcv.volume(i);
      final double close = ohlcv.close(i);
      if (compareVolume(pv, volume)) {
        // price %change
        vi += (close / pc) - ONE;
      }
      output[i] = vi;

      // shift forward in time
      pv = volume;
      pc = close;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  abstract boolean compareVolume(final long previous, final long current);

}
