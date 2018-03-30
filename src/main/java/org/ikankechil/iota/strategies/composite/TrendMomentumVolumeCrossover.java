/**
 * TrendMomentumVolumeCrossover.java  v0.1  22 January 2017 9:13:01 pm
 *
 * Copyright © 2017-2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.composite;

import static java.lang.String.*;

import org.ikankechil.iota.strategies.CompositeStrategy;
import org.ikankechil.iota.strategies.Crossover;
import org.ikankechil.iota.strategies.Strategy;
import org.ikankechil.iota.strategies.ThresholdCrossover;
import org.ikankechil.iota.strategies.momentum.LaguerreRSICrossover;
import org.ikankechil.iota.strategies.trend.PPOCrossover;
import org.ikankechil.iota.strategies.volume.ChaikinOscillatorZeroLineCrossover;

/**
 * Composite strategy of trend, momentum and volume strategies.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class TrendMomentumVolumeCrossover extends CompositeStrategy {

  // strategy types
  private static final String TREND    = "trend";
  private static final String MOMENTUM = "momentum";
  private static final String VOLUME   = "volume";

  public TrendMomentumVolumeCrossover() {
    this(6, new PPOCrossover(10), new LaguerreRSICrossover(), new ChaikinOscillatorZeroLineCrossover());
  }

  public TrendMomentumVolumeCrossover(final int window, final Strategy trend, final Strategy momentum, final Strategy volume) {
    super(window, validate(trend, momentum, volume));
  }

  private static Strategy[] validate(final Strategy trend, final Strategy momentum, final Strategy volume) {
    validate(trend, TREND);
    validate(momentum, MOMENTUM);
    validate(volume, VOLUME);
    return new Strategy[] { trend, momentum, volume };
  }

  private static void validate(final Strategy strategy, final String strategyType) {
    if (!isCrossover(strategy) ||
        !strategy.getClass().getPackage().getName().endsWith(strategyType)) {
      throw new IllegalArgumentException(format("Not a %s crossover strategy: %s",
                                                strategyType,
                                                (strategy != null ? strategy.getClass().getName()
                                                                  : strategy)));
    }
  }

  private static boolean isCrossover(final Strategy strategy) {
    return (strategy instanceof Crossover || strategy instanceof ThresholdCrossover);
  }

}
