/**
 * RainbowCharts.java  v0.4  1 March 2015 5:58:00 pm
 *
 * Copyright © 2015-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Indicator;

/**
 * Rainbow Charts by Mel Widner
 *
 * <p>References:
 * <li>https://www.forex-tsd.com/attachments/elite-section/108853d1286620402-elite-indicators-mel-widner-rainbow-charts.pdf<br>
 * <li>https://c.mql5.com/forextsd/forum/77/mel_widner_-_rainbow_charts.pdf<br>
 * <li>http://xa.yimg.com/kq/groups/16789226/301268564/name/192verv-1.pdf<br>
 * <li>ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V15/C07/RAINBOW.pdf<br>
 *
 * @author Daniel Kuan
 * @version 0.4
 */
public class RainbowCharts extends AbstractIndicator {

  private final Indicator ma;

  public RainbowCharts() {
    this(TWO);
  }

  public RainbowCharts(final int period) {
    this(new SMA(period));
  }

  public RainbowCharts(final MA ma) {
    super(ma.lookback() * TEN);

    this.ma = ma;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    return generate((TimeSeries) ohlcv);
  }

  @Override
  public List<TimeSeries> generate(final TimeSeries series) {
    throwExceptionIfShort(series);

    final TimeSeries ma0 = ma.generate(series).get(ZERO);
    final TimeSeries ma1 = ma.generate(ma0).get(ZERO);
    final TimeSeries ma2 = ma.generate(ma1).get(ZERO);
    final TimeSeries ma3 = ma.generate(ma2).get(ZERO);
    final TimeSeries ma4 = ma.generate(ma3).get(ZERO);
    final TimeSeries ma5 = ma.generate(ma4).get(ZERO);
    final TimeSeries ma6 = ma.generate(ma5).get(ZERO);
    final TimeSeries ma7 = ma.generate(ma6).get(ZERO);
    final TimeSeries ma8 = ma.generate(ma7).get(ZERO);
    final TimeSeries ma9 = ma.generate(ma8).get(ZERO);

    final int size = series.size();
    final String[] dates = Arrays.copyOfRange(series.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, series);

    // MAs have different lengths
    final int length = ma9.size();
    return Arrays.asList(new TimeSeries(name, dates, Arrays.copyOfRange(ma0.values(), ma0.size() - length, ma0.size())),
                         new TimeSeries(name, dates, Arrays.copyOfRange(ma1.values(), ma1.size() - length, ma1.size())),
                         new TimeSeries(name, dates, Arrays.copyOfRange(ma2.values(), ma2.size() - length, ma2.size())),
                         new TimeSeries(name, dates, Arrays.copyOfRange(ma3.values(), ma3.size() - length, ma3.size())),
                         new TimeSeries(name, dates, Arrays.copyOfRange(ma4.values(), ma4.size() - length, ma4.size())),
                         new TimeSeries(name, dates, Arrays.copyOfRange(ma5.values(), ma5.size() - length, ma5.size())),
                         new TimeSeries(name, dates, Arrays.copyOfRange(ma6.values(), ma6.size() - length, ma6.size())),
                         new TimeSeries(name, dates, Arrays.copyOfRange(ma7.values(), ma7.size() - length, ma7.size())),
                         new TimeSeries(name, dates, Arrays.copyOfRange(ma8.values(), ma8.size() - length, ma8.size())),
                         new TimeSeries(name, dates, ma9.values()));
  }

}
