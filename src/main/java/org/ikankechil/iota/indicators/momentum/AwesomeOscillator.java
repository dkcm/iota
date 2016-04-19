/**
 * AwesomeOscillator.java v0.2 9 December 2014 1:14:06 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.MedianPrice;
import org.ikankechil.iota.indicators.trend.SMA;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Awesome Oscillator (AO)
 * <p>
 * http://www.forexgurus.co.uk/indicators/awesome-oscillator
 * http://www.forexfactory.com/attachment.php?attachmentid=1029464&d=1346336346
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class AwesomeOscillator extends AbstractIndicator {

  private final SMA                fastSMA;
  private final SMA                slowSMA;

  private static final MedianPrice MEDIAN = new MedianPrice();

  public AwesomeOscillator() {
    this(FIVE, 34);
  }

  public AwesomeOscillator(final int fast, final int slow) {
    super(slow - ONE);
    throwExceptionIfNegative(fast, slow);

    fastSMA = new SMA(fast);
    slowSMA = new SMA(slow);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // AO(n) = SMA(MP(n), 5) - SMA(MP(n), 34)

    // compute median
    final TimeSeries medians = MEDIAN.generate(ohlcv).get(ZERO);

    // compute fast SMA
    final double[] fastSMAs = fastSMA.generate(medians).get(ZERO).values();

    // compute slow SMA
    final double[] slowSMAs = slowSMA.generate(medians).get(ZERO).values();

    // compute AO
    for (int s = ZERO, f = fastSMAs.length - slowSMAs.length;
         s < output.length;
         ++s, ++f) {
      output[s] = fastSMAs[f] - slowSMAs[s];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
