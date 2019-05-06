/**
 * GatorOscillator.java  v0.1  21 April 2019 11:41:46 PM
 *
 * Copyright © 2019-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import static java.lang.Math.*;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Indicator;

/**
 * Gator Oscillator by Bill Williams
 *
 * <p>References:
 * <li>http://forex-indicators.net/bill-williams/gator-oscillator
 * <li>https://www.metatrader5.com/en/terminal/help/indicators/bw_indicators/go
 * <li>https://library.tradingtechnologies.com/trade/chrt-ti-gator-oscillator.html<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class GatorOscillator extends AbstractIndicator {

  private final Indicator     alligator;

  private static final String GATOR_OSCILLATOR_UPPER = "Gator Oscillator Upper";
  private static final String GATOR_OSCILLATOR_LOWER = "Gator Oscillator Lower";

  public GatorOscillator() {
    this(THIRTEEN, EIGHT, FIVE);
  }

  public GatorOscillator(final int jaws, final int teeth, final int lips) {
    this(new Alligator(jaws, teeth, lips));
  }

  public GatorOscillator(final Alligator alligator) {
    super(alligator.lookback());

    this.alligator = alligator;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    // Formula:
    // Gator Oscillator histogram
    // above zero = absolute difference between blue and red lines
    // below zero = absolute difference between red and green lines

    throwExceptionIfShort(ohlcv);

    final List<TimeSeries> alligators = alligator.generate(ohlcv, start);
    final TimeSeries blues = alligators.get(ZERO);
    final TimeSeries reds = alligators.get(ONE);
    final TimeSeries greens = alligators.get(TWO);

    final double[] uppers = new double[blues.size()];
    final double[] lowers = new double[uppers.length];
    for (int i = start, r = i + reds.size() - blues.size(), g = i + greens.size() - blues.size();
         i < uppers.length;
         ++i, ++r, ++g) {
      uppers[i] = abs(blues.value(i) - reds.value(r));
      lowers[i] = -abs(reds.value(r) - greens.value(g));
    }

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(GATOR_OSCILLATOR_UPPER,
                                        blues.dates(),
                                        uppers),
                         new TimeSeries(GATOR_OSCILLATOR_LOWER,
                                        blues.dates(),
                                        lowers));
  }

}
