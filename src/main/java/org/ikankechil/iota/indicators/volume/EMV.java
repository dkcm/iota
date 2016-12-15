/**
 * EMV.java  v0.1  15 December 2016 12:33:06 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.MedianPrice;
import org.ikankechil.iota.indicators.Range;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Ease of Movement (EMV) by Richard Arms
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:ease_of_movement_emv
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class EMV extends AbstractIndicator {

  private final double             volumeFactor;

  private static final MedianPrice MEDIAN        = new MedianPrice();
  private static final Range       RANGE         = new Range();

  private static final double      VOLUME_FACTOR = 1e8;

  public EMV() {
    this(FOURTEEN, VOLUME_FACTOR);
  }

  public EMV(final int period, final double volumeFactor) {
    super(period, period);
    throwExceptionIfNegative(volumeFactor);

    this.volumeFactor = volumeFactor;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // Distance Moved = ((H + L)/2 - (Prior H + Prior L)/2)
    // Box Ratio = ((V/100,000,000)/(H - L))
    // 1-Period EMV = ((H + L)/2 - (Prior H + Prior L)/2) / ((V/100,000,000)/(H - L))
    // 14-Period Ease of Movement = 14-Period simple moving average of 1-period EMV

    final double[] medians = MEDIAN.generate(ohlcv, start).get(ZERO).values();
    final double[] ranges = RANGE.generate(ohlcv, start).get(ZERO).values();

    // compute EMV
    final double[] emv = new double[ohlcv.size() - ONE];
    for (int i = ZERO, j = i + ONE; i < emv.length; ++i, ++j) {
      emv[i] = (medians[j] - medians[i]) * ranges[j] * volumeFactor / ohlcv.volume(j);
    }

    // compute indicator
    final double[] smaEMV = sma(emv, period);
    System.arraycopy(smaEMV, ZERO, output, ZERO, output.length);

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
