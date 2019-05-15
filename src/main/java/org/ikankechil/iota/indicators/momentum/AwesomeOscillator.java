/**
 * AwesomeOscillator.java  v0.3  9 December 2014 1:14:06 PM
 *
 * Copyright © 2014-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.indicators.MedianPrice;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Awesome Oscillator (AO) by Bill Williams
 *
 * <p>References:
 * <li>https://www.tradingview.com/wiki/Awesome_Oscillator_(AO)
 * <li>https://www.metatrader5.com/en/terminal/help/indicators/bw_indicators/awesome
 * <li>http://www.forexfactory.com/attachment.php?attachmentid=1029464&d=1346336346
 * <li>http://www.forexfactory.com/attachment.php?attachmentid=1928837&d=1463398508<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class AwesomeOscillator extends AbstractIndicator {

  private final int              fast;
  private final int              slow;

  private static final Indicator MEDIAN = new MedianPrice();

  public AwesomeOscillator() {
    this(FIVE, THIRTY_FOUR);
  }

  public AwesomeOscillator(final int fast, final int slow) {
    super(slow - ONE);
    throwExceptionIfNegative(fast, slow);

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
    // compute median
    return compute(start,
                   end,
                   MEDIAN.generate(ohlcv, start).get(ZERO).values(),
                   outBegIdx,
                   outNBElement,
                   output);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // AO(n) = SMA(MP(n), 5) - SMA(MP(n), 34)

    // compute fast and slow SMAs
    final double[] fastSMAs = sma(values, fast);
    final double[] slowSMAs = sma(values, slow);

    // compute AO
    System.arraycopy(difference(fastSMAs, slowSMAs),
                     ZERO,
                     output,
                     ZERO,
                     output.length);

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
