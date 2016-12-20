/**
 * DonchianChannels.java  0.1  20 December 2016 2:53:00 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.MaximumPrice;
import org.ikankechil.iota.indicators.MinimumPrice;

/**
 * DonchianChannels by Richard Donchian
 *
 * <p>https://www.incrediblecharts.com/indicators/donchian_channels.php<br>
 * http://www.investopedia.com/terms/d/donchianchannels.asp<br>
 * https://www.tradingview.com/wiki/Donchian_Channels_(DC)<br>
 * https://en.wikipedia.org/wiki/Donchian_channel<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class DonchianChannels extends AbstractIndicator {

  private final MinimumPrice  min;
  private final MaximumPrice  max;

  private static final String UPPER_BAND  = "Donchian Channels Upper Band";
  private static final String MIDDLE_BAND = "Donchian Channels Middle Band";
  private static final String LOWER_BAND  = "Donchian Channels Lower Band";

  public DonchianChannels() {
    this(TWENTY);
  }

  public DonchianChannels(final int period) {
    super(period, (period - ONE));

    min = new MinimumPrice(period);
    max = new MaximumPrice(period);
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    throwExceptionIfShort(ohlcv);

    final String[] ohlcvDates = ohlcv.dates();
    final TimeSeries lows = new TimeSeries(LOWER_BAND, ohlcvDates, ohlcv.lows());
    final TimeSeries highs = new TimeSeries(UPPER_BAND, ohlcvDates, ohlcv.highs());
    final double[] minPrices = min.generate(lows).get(ZERO).values();
    final double[] maxPrices = max.generate(highs).get(ZERO).values();

    final double[] midPrices = new double[maxPrices.length];
    for (int i = ZERO; i < midPrices.length; ++i) {
      midPrices[i] = (minPrices[i] + maxPrices[i]) * HALF;
    }

    final String[] dates = Arrays.copyOfRange(ohlcvDates, lookback, ohlcv.size());

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(UPPER_BAND, dates, maxPrices),
                         new TimeSeries(MIDDLE_BAND, dates, midPrices),
                         new TimeSeries(LOWER_BAND, dates, minPrices));
  }

}
