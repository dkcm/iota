/**
 * PPOCrossover.java  v0.1  21 September 2016 12:56:30 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.trend;

import org.ikankechil.iota.indicators.trend.PPO;
import org.ikankechil.iota.strategies.CrossoverWithThreshold;

/**
 * PPO Crossover strategy.
 * <p>
 * Buy when PPO crosses over PPO signal line while under a threshold<br>
 * Sell when PPO crosses PPO signal line while above a threshold<br>
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class PPOCrossover extends CrossoverWithThreshold {

  public PPOCrossover(final double threshold) {
    this(-threshold, threshold);
  }

  public PPOCrossover(final int fast, final int slow, final int signal, final double threshold) {
    this(fast, slow, signal, -threshold, threshold);
  }

  public PPOCrossover(final PPO ppo, final double threshold) {
    this(ppo, -threshold, threshold);
  }

  public PPOCrossover(final double buy, final double sell) {
    this(TWELVE, TWENTY_SIX, NINE, buy, sell);
  }

  public PPOCrossover(final int fast, final int slow, final int signal, final double buy, final double sell) {
    this(new PPO(fast, slow, signal), buy, sell);
  }

  public PPOCrossover(final PPO ppo, final double buy, final double sell) {
    super(ppo, buy, sell);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

}
