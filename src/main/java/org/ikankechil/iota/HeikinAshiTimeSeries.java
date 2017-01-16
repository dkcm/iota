/**
 * HeikinAshiTimeSeries.java  v0.1  13 January 2017 8:04:24 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota;

import static java.lang.Math.*;

/**
 * A time-series representation of Heikin-Ashi Candlesticks.
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:chart_analysis:heikin_ashi<br>
 * http://www.investopedia.com/articles/technical/04/092204.asp<br>
 * https://www.earnforex.com/books/en/trading-strategy/Using_The_Heikin_Ashi_Technique_D_Valcu.pdf<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class HeikinAshiTimeSeries extends OHLCVTimeSeries {

  private static final double QUARTER = 0.25;
  private static final double HALF    = 0.5;

  public HeikinAshiTimeSeries(final OHLCVTimeSeries ohlcv) {
    super(ohlcv.toString(), ohlcv.size());

    convert(ohlcv);
  }

  private final void convert(final OHLCVTimeSeries ohlcv) {
    // Algorithm:
    //
    // Courtesy of ChartSchool.com
    //
    // 1. The Heikin-Ashi Close is simply an average of the open, high, low and
    // close for the current period.
    // HA-Close = (Open(0) + High(0) + Low(0) + Close(0)) / 4
    //
    // 2. The Heikin-Ashi Open is the average of the prior Heikin-Ashi
    // candlestick open plus the close of the prior Heikin-Ashi candlestick.
    // HA-Open = (HA-Open(-1) + HA-Close(-1)) / 2
    //
    // 3. The Heikin-Ashi High is the maximum of three data points: the current
    // period's high, the current Heikin-Ashi candlestick open or the current
    // Heikin-Ashi candlestick close.
    // HA-High = Maximum of the High(0), HA-Open(0) or HA-Close(0)
    //
    // 4. The Heikin-Ashi low is the minimum of three data points: the current
    // period's low, the current Heikin-Ashi candlestick open or the current
    // Heikin-Ashi candlestick close.
    // HA-Low = Minimum of the Low(0), HA-Open(0) or HA-Close(0)

    // first heikin-ashi candlestick
    int i = 0;
    double hao = ohlcv.open(i); // alternative: hao(ohlcv.open(i), ohlcv.close(i));
    double hac = hac(ohlcv, i);
    double hah = ohlcv.high(i);
    double hal = ohlcv.low(i);

    set(ohlcv.date(i), hao, hah, hal, hac, ohlcv.volume(i), i);

    // set remaining heikin-ashi candlestick values
    final int size = ohlcv.size();
    while (++i < size) {
      hao = hao(hao, hac);
      hac = hac(ohlcv, i);
      hah = max(ohlcv.high(i), max(hao, hac));
      hal = min(ohlcv.low(i), min(hao, hac));

      set(ohlcv.date(i), hao, hah, hal, hac, ohlcv.volume(i), i);
    }
  }

  private static final double hao(final double hao, final double hac) {
    return (hao + hac) * HALF;
  }

  private static final double hac(final OHLCVTimeSeries ohlcv, final int index) {
    return (ohlcv.open(index) + ohlcv.high(index) + ohlcv.low(index) + ohlcv.close(index)) * QUARTER;
  }

}
