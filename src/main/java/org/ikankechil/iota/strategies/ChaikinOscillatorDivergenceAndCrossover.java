/**
 * ChaikinOscillatorDivergenceAndCrossover.java  v0.2  8 September 2016 7:32:30 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.SignalTimeSeries;
import org.ikankechil.iota.indicators.volume.ChaikinOscillator;

/**
 * Chaikin Oscillator divergence and zero-line crossover
 * <p>
 * "It is designed to measure the momentum behind buying and selling pressure
 * (Accumulation Distribution Line). A move into positive territory indicates
 * that the Accumulation Distribution Line is rising and buying pressure
 * prevails. A move into negative territory indicates that the Accumulation
 * Distribution Line is falling and selling pressure prevails. Chartists can
 * anticipate crosses into positive or negative territory by looking for bullish
 * or bearish divergences, respectively."
 * <p>
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:chaikin_oscillator
 * https://www.tradingview.com/stock-charts-support/index.php/Chaikin_Oscillator
 * http://www.investopedia.com/ask/answers/112814/how-do-i-create-basic-trading-strategy-chaikin-oscillator.asp
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class ChaikinOscillatorDivergenceAndCrossover implements Strategy {

  private final Strategy   divergenceAndZeroLineCross;

  private static final int ZERO_LINE = 0;

  public ChaikinOscillatorDivergenceAndCrossover(final int window, final int awayPoints) {
    this(6, 20, window, awayPoints);
  }

  public ChaikinOscillatorDivergenceAndCrossover(final int fast, final int slow, final int window, final int awayPoints) {
    this(new ChaikinOscillator(fast, slow), window, awayPoints);
  }

  public ChaikinOscillatorDivergenceAndCrossover(final ChaikinOscillator chaikinOscillator, final int window, final int awayPoints) {
    final Strategy divergence = new DivergenceStrategy(chaikinOscillator, awayPoints);
    final Strategy zeroLineCross = new ThresholdCrossover(chaikinOscillator, ZERO_LINE);

    divergenceAndZeroLineCross = new CompositeStrategy(window, divergence, zeroLineCross);
  }

  @Override
  public SignalTimeSeries execute(final OHLCVTimeSeries ohlcv) {
    return divergenceAndZeroLineCross.execute(ohlcv);
  }

  @Override
  public SignalTimeSeries execute(final OHLCVTimeSeries ohlcv, final int lookback) {
    return divergenceAndZeroLineCross.execute(ohlcv, lookback);
  }

}
