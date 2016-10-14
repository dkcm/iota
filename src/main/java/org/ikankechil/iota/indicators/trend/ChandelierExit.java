/**
 * ChandelierExit.java  v0.2  30 July 2015 12:30:13 am
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.MaximumPrice;
import org.ikankechil.iota.indicators.MinimumPrice;
import org.ikankechil.iota.indicators.volatility.ATR;

/**
 * Chandelier Exit by Charles LeBeau
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:chandelier_exit<br>
 * https://www.incrediblecharts.com/indicators/chandelier_exits.php<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class ChandelierExit extends AbstractIndicator {

  private final MaximumPrice  maxPrice;
  private final MinimumPrice  minPrice;
  private final ATR           atr;
  private final double        multiplier;

  private static final String CHANDELIER_EXIT_LONG  = "Chandelier Exit Long";
  private static final String CHANDELIER_EXIT_SHORT = "Chandelier Exit Short";

  public ChandelierExit() {
    this(TWENTY_TWO);
  }

  public ChandelierExit(final int period) {
    this(period, THREE);
  }

  public ChandelierExit(final double multiplier) {
    this(TWENTY_TWO, multiplier);
  }

  public ChandelierExit(final int period, final double multiplier) {
    super(period, period);
    throwExceptionIfNegative(multiplier);

    maxPrice = new MaximumPrice(period);
    minPrice = new MinimumPrice(period);
    atr = new ATR(period);
    this.multiplier = multiplier;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();

    // Formula:
    // Chandelier Exit (long) = 22-day High - ATR(22) x 3
    // Chandelier Exit (short) = 22-day Low + ATR(22) x 3

    // compute highs and lows
    final TimeSeries highs = new TimeSeries(EMPTY, size);
    final TimeSeries lows = new TimeSeries(EMPTY, size);
    for (int i = ZERO; i < size; ++i) {
      highs.value(ohlcv.high(i), i);
      lows.value(ohlcv.low(i), i);
    }
    final TimeSeries max = maxPrice.generate(highs).get(ZERO);
    final TimeSeries min = minPrice.generate(lows).get(ZERO);

    // compute ATR
    final TimeSeries atrs = atr.generate(ohlcv).get(ZERO);

    // compute indicator
    final double[] chandelierExitLong = new double[atrs.size()];
    final double[] chandelierExitShort = new double[chandelierExitLong.length];
    for (int i = ZERO, m = ONE; i < chandelierExitLong.length; ++i, ++m) {
      final double buffer = atrs.value(i) * multiplier;
      chandelierExitLong[i] = max.value(m) - buffer;
      chandelierExitShort[i] = min.value(m) + buffer;
    }

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(CHANDELIER_EXIT_LONG, dates, chandelierExitLong),
                         new TimeSeries(CHANDELIER_EXIT_SHORT, dates, chandelierExitShort));
  }

}
