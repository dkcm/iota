/**
 * AroonEmergingTrend.java	v0.1	7 September 2016 1:04:19 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.trend;

import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.indicators.trend.Aroon;
import org.ikankechil.iota.strategies.CompositeStrategy;
import org.ikankechil.iota.strategies.Crossover;
import org.ikankechil.iota.strategies.Strategy;
import org.ikankechil.iota.strategies.ThresholdCrossover;

/**
 *
 * <p>
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:aroon
 * <p>
 * Buy:<br>
 * 1. Aroon-Up crosses over Aroon-Down<br>
 * 2. Aroon-Up crosses above 50 and Aroon-Down crosses below 50<br>
 * 3. Aroon-Up reaches 100 and Aroon-Down remains below 30(?)<br>
 *
 * Sell:<br>
 * 1. Aroon-Down crosses over Aroon-Up<br>
 * 2. Aroon-Down crosses above 50 and Aroon-Up crosses below 50<br>
 * 3. Aroon-Down reaches 100 and Aroon-Up remains below 30(?)<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
class AroonEmergingTrend extends CompositeStrategy {

  private static final int CENTERLINE = 50;

  public AroonEmergingTrend(final int window, final Strategy... strategies) {
    super(window, strategies);

    final Indicator aroon = new Aroon();
    final Strategy aroonCrossover = new Crossover(aroon);
    final Strategy aroonCross = new ThresholdCrossover(aroon, CENTERLINE);
    final CompositeStrategy composite = new CompositeStrategy(5, aroonCrossover, aroonCross);

  }

}
