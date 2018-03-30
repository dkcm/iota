/**
 * TrendMomentumVolumeCrossoverTest.java  v0.1  31 March 2018 1:32:08 AM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.composite;

import static org.junit.Assert.*;

import org.ikankechil.iota.strategies.Strategy;
import org.ikankechil.iota.strategies.momentum.SimpleStochasticOscillatorCrossover;
import org.ikankechil.iota.strategies.trend.GoldenCross;
import org.ikankechil.iota.strategies.volume.ChaikinOscillatorThresholdCrossover;
import org.junit.Test;

/**
 * JUnit test for <code>TrendMomentumVolumeCrossover</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class TrendMomentumVolumeCrossoverTest {

  private static final int      ONE      = 1;

  private static final Strategy TREND    = new GoldenCross();
  private static final Strategy MOMENTUM = new SimpleStochasticOscillatorCrossover();
  private static final Strategy VOLUME   = new ChaikinOscillatorThresholdCrossover(ONE);

  @Test(expected=IllegalArgumentException.class)
  public void invalidStrategies() {
    assertNull(new TrendMomentumVolumeCrossover(ONE, null, null, null));
  }

  @Test(expected=IllegalArgumentException.class)
  public void invalidTrendStrategy() {
    assertNull(new TrendMomentumVolumeCrossover(ONE, MOMENTUM, null, null));
  }

  @Test(expected=IllegalArgumentException.class)
  public void invalidMomentumStrategy() {
    assertNull(new TrendMomentumVolumeCrossover(ONE, TREND, VOLUME, null));
  }

  @Test(expected=IllegalArgumentException.class)
  public void invalidVolumeStrategy() {
    assertNull(new TrendMomentumVolumeCrossover(ONE, TREND, MOMENTUM, TREND));
  }

  @Test
  public void validStrategies() {
    assertNotNull(new TrendMomentumVolumeCrossover(ONE, TREND, MOMENTUM, VOLUME));
    assertNotNull(new TrendMomentumVolumeCrossover());
  }

}
