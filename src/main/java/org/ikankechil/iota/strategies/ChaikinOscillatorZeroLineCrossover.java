/**
 * ChaikinOscillatorZeroLineCrossover.java	v0.1	16 September 2016 12:46:43 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.SignalTimeSeries;
import org.ikankechil.iota.indicators.volume.ChaikinOscillator;

/**
 *
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ChaikinOscillatorZeroLineCrossover implements Strategy {

  private final Strategy   zeroLineCross;

  private static final int ZERO_LINE = 0;

  public ChaikinOscillatorZeroLineCrossover() {
    this(6, 20);
  }

  public ChaikinOscillatorZeroLineCrossover(final int fast, final int slow) {
    this(new ChaikinOscillator(fast, slow));
  }

  public ChaikinOscillatorZeroLineCrossover(final ChaikinOscillator chaikinOscillator) {
    zeroLineCross = new ThresholdCrossover(chaikinOscillator, ZERO_LINE);
  }

  @Override
  public SignalTimeSeries execute(final OHLCVTimeSeries ohlcv) {
    return zeroLineCross.execute(ohlcv);
  }

  @Override
  public SignalTimeSeries execute(final OHLCVTimeSeries ohlcv, final int lookback) {
    return zeroLineCross.execute(ohlcv, lookback);
  }

}
