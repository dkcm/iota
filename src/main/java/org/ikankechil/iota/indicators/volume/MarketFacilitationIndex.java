/**
 * MarketFacilitationIndex.java  v0.1  14 April 2019 11:43:09 PM
 *
 * Copyright © 2019-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

/**
 * Market Facilitation Index by Bill Williams
 *
 * <p>References:
 * <li>http://forex-indicators.net/bill-williams/mfi
 * <li>https://www.tradingview.com/script/0BANZQhK-Indicator-Market-Facilitation-Index-MFIndex/
 * <li>https://en.wikipedia.org/wiki/Market_facilitation_index
 * <li>https://www.mql5.com/en/code/7992<be>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MarketFacilitationIndex extends AbstractIndicator {

  private static final String GREEN = "MarketFacilitationIndex Green";
  private static final String FADE  = "MarketFacilitationIndex Fade";
  private static final String FAKE  = "MarketFacilitationIndex Fake";
  private static final String SQUAT = "MarketFacilitationIndex Squat";

  public MarketFacilitationIndex() {
    super(ONE);
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    // Formula:
    // MFI = (High - Low) / Volume

    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();

    final double[] green = new double[size - lookback - start];
    final double[] fade = new double[green.length];
    final double[] fake = new double[green.length];
    final double[] squat = new double[green.length];

    int i = start;
    double pmfi = mfi(ohlcv, i);
    double pv = ohlcv.volume(i);

    while (++i < green.length) {
      final double mfi = mfi(ohlcv, i);
      final double v = ohlcv.volume(i);

      if (mfi > pmfi && v > pv) {
        green[i] = mfi;
      }
      else if (mfi < pmfi && v < pv) {
        fade[i] = mfi;
      }
      else if (mfi > pmfi && v < pv) {
        fake[i] = mfi;
      }
      else if (mfi < pmfi && v > pv) {
        squat[i] = mfi;
      }

      // shift forward
      pmfi = mfi;
      pv = v;
    }

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(GREEN, dates, green),
                         new TimeSeries(FADE, dates, fade),
                         new TimeSeries(FAKE, dates, fake),
                         new TimeSeries(SQUAT, dates, squat));
  }

  private static double mfi(final OHLCVTimeSeries ohlcv, final int i) {
    return (ohlcv.high(i) - ohlcv.low(i)) / ohlcv.volume(i);
  }

}
