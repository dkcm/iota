/**
 * SimpleStochasticOscillatorCrossover.java  v0.1  29 August 2016 9:29:16 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.momentum;

import org.ikankechil.iota.indicators.momentum.Stochastic;
import org.ikankechil.iota.strategies.CrossoverWithThreshold;

/**
 * Simple Stochastic Oscillator Crossover strategy.
 * <p>
 * Buy:
 * 1. SlowK crosses over SlowD
 * 2. SlowK < 20
 * Sell:
 * 1. SlowK crosses under SlowD
 * 2. SlowK > 80
 *
 * http://www.futuresmag.com/2012/09/30/complementary-approach-trading-technical-indicators?page=2
 * https://www.tradingview.com/script/dtrRRU3z-Strategy-Stochastic-Crossover/
 * http://www.investopedia.com/articles/trading/08/macd-stochastic-double-cross.asp
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class SimpleStochasticOscillatorCrossover extends CrossoverWithThreshold {

  // thresholds
  private static final double OVERSOLD   = 20.0;
  private static final double OVERBOUGHT = 80.0;

  public SimpleStochasticOscillatorCrossover() {
    this(FOURTEEN, THREE, THREE);
  }

  public SimpleStochasticOscillatorCrossover(final int fastK, final int slowK, final int slowD) {
    this(fastK, slowK, slowD, OVERSOLD, OVERBOUGHT);
  }

  public SimpleStochasticOscillatorCrossover(final double oversold, final double overbought) {
    this(FOURTEEN, THREE, THREE, oversold, overbought);
  }

  public SimpleStochasticOscillatorCrossover(final int fastK, final int slowK, final int slowD, final double oversold, final double overbought) {
    this(new Stochastic(fastK, slowK, slowD), oversold, overbought);
  }

  public SimpleStochasticOscillatorCrossover(final Stochastic stochastic, final double oversold, final double overbought) {
    super(stochastic, oversold, overbought);
  }

}
