/**
 * GMMA.java  v0.2  4 March 2015 1:05:52 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

/**
 * Guppy Multiple Moving Average (GMMA) by Darryl Guppy
 * <p>
 * http://www.chartnexus.com.sg/learning/gmma.php
 * http://www.investopedia.com/terms/g/guppy-multiple-moving-average.asp
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class GMMA extends AbstractIndicator {

  // short-term EMAs
  private static final int SHORT1 = THREE;
  private static final int SHORT2 = FIVE;
  private static final int SHORT3 = EIGHT;
  private static final int SHORT4 = TEN;
  private static final int SHORT5 = TWELVE;
  private static final int SHORT6 = FIFTEEN;

  // long-term EMAs
  private static final int LONG1  = THIRTY;
  private static final int LONG2  = THIRTY_FIVE;
  private static final int LONG3  = FORTY;
  private static final int LONG4  = FORTY_FIVE;
  private static final int LONG5  = FIFTY;
  private static final int LONG6  = SIXTY;

  public GMMA() {
    super(LONG6 - ONE);
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    throwExceptionIfShort(ohlcv);

    final double[] closes = ohlcv.closes();

    // compute short-term group
    final double[] short1 = ema(closes, SHORT1);
    final double[] short2 = ema(closes, SHORT2);
    final double[] short3 = ema(closes, SHORT3);
    final double[] short4 = ema(closes, SHORT4);
    final double[] short5 = ema(closes, SHORT5);
    final double[] short6 = ema(closes, SHORT6);

    // compute long-term group
    final double[] long1 = ema(closes, LONG1);
    final double[] long2 = ema(closes, LONG2);
    final double[] long3 = ema(closes, LONG3);
    final double[] long4 = ema(closes, LONG4);
    final double[] long5 = ema(closes, LONG5);
    final double[] long6 = ema(closes, LONG6);

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, ohlcv.size());

    logger.info(GENERATED_FOR, name, ohlcv);

    // MAs have different lengths
    final int length = long6.length;
    return Arrays.asList(new TimeSeries(name + SHORT1, dates, Arrays.copyOfRange(short1, short1.length - length, short1.length)),
                         new TimeSeries(name + SHORT2, dates, Arrays.copyOfRange(short2, short2.length - length, short2.length)),
                         new TimeSeries(name + SHORT3, dates, Arrays.copyOfRange(short3, short3.length - length, short3.length)),
                         new TimeSeries(name + SHORT4, dates, Arrays.copyOfRange(short4, short4.length - length, short4.length)),
                         new TimeSeries(name + SHORT5, dates, Arrays.copyOfRange(short5, short5.length - length, short5.length)),
                         new TimeSeries(name + SHORT6, dates, Arrays.copyOfRange(short6, short6.length - length, short6.length)),
                         new TimeSeries(name + LONG1,  dates, Arrays.copyOfRange(long1,  long1.length - length,  long1.length)),
                         new TimeSeries(name + LONG2,  dates, Arrays.copyOfRange(long2,  long2.length - length,  long2.length)),
                         new TimeSeries(name + LONG3,  dates, Arrays.copyOfRange(long3,  long3.length - length,  long3.length)),
                         new TimeSeries(name + LONG4,  dates, Arrays.copyOfRange(long4,  long4.length - length,  long4.length)),
                         new TimeSeries(name + LONG5,  dates, Arrays.copyOfRange(long5,  long5.length - length,  long5.length)),
                         new TimeSeries(name + LONG6,  dates, long6));
  }

}
