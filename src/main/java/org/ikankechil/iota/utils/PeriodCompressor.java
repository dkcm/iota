/**
 * PeriodCompressor.java  v0.1  22 September 2014 3:49:04 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Compresses OHLCV periods.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class PeriodCompressor { // TODO move to new project

  private static final int    ZERO            = 0;
  private static final int    ONE             = 1;
  private static final int    FOUR            = 4;
  private static final int    SIX             = 6;
  private static final int    EIGHT           = 8;

  private static final String START_YYYY_MM   = "000000";

  private static final String _M              = "_m";
  private static final String _W              = "_w";

  private static final String ERRONEOUS_DATUM = "Erroneous datum at: {}";

  private static final Logger logger          = LoggerFactory.getLogger(PeriodCompressor.class);

  private PeriodCompressor() { /* do not instantiate */ }

  public static final OHLCVTimeSeries dailyToMonthly(final OHLCVTimeSeries daily) {
    final OHLCVTimeSeries monthly = compressToMonthly(daily);
    logger.info("Daily-to-monthly compression completed for: {}", daily);
    return monthly;
  }

  public static final OHLCVTimeSeries weeklyToMonthly(final OHLCVTimeSeries weekly) {
    final OHLCVTimeSeries monthly = compressToMonthly(weekly);
    logger.info("Weekly-to-monthly compression completed for: {}", weekly);
    return monthly;
  }

  private static final OHLCVTimeSeries compressToMonthly(final OHLCVTimeSeries uncompressed) {
    // monthly O = first O of the month
    // monthly H = highest H of the month
    // monthly L = lowest L of the month
    // monthly C = last C of the month
    // monthly V = cumulative V for the month

    String currentYYYYMM = START_YYYY_MM;
    final OHLCVAccumulator u2m = new OHLCVAccumulator();

    final String[] dates = uncompressed.dates();
    for (int d = ZERO; d < dates.length; ++d) {
      final String date = dates[d];
      final String yyyyMM = date.substring(ZERO, SIX);

      final int change = yyyyMM.compareTo(currentYYYYMM);
      if (change > ZERO) {        // new month
        currentYYYYMM = yyyyMM;
        u2m.newPeriod(uncompressed.date(d),
                      uncompressed.open(d),
                      uncompressed.high(d),
                      uncompressed.low(d),
                      uncompressed.close(d),
                      uncompressed.volume(d));
      }
      else if (change == ZERO) {  // same month
        u2m.accumulate(uncompressed.high(d),
                       uncompressed.low(d),
                       uncompressed.close(d),
                       uncompressed.volume(d));
      }
      else {
        logger.error(ERRONEOUS_DATUM, date);
      }
    }

    final OHLCVTimeSeries monthly = u2m.generate(uncompressed + _M);
    return monthly;
  }

  public static final OHLCVTimeSeries dailyToWeekly(final OHLCVTimeSeries daily) {
    final OHLCVAccumulator d2w = new OHLCVAccumulator();
    final Calendar calendar = Calendar.getInstance();
    calendar.clear();

    int currentWeek = ZERO;
    final String[] dates = daily.dates();
    for (int d = ZERO; d < dates.length; ++d) {
      final String date = dates[d];
      final int yyyy = Integer.parseInt(date.substring(ZERO, FOUR));
      final int mm = Integer.parseInt(date.substring(FOUR, SIX));
      final int dd = Integer.parseInt(date.substring(SIX, EIGHT));

      calendar.set(yyyy, mm - ONE, dd);

      final int week = calendar.get(Calendar.WEEK_OF_YEAR);
      if (week > currentWeek) {       // new week
        currentWeek = week;
        d2w.newPeriod(daily.date(d),
                      daily.open(d),
                      daily.high(d),
                      daily.low(d),
                      daily.close(d),
                      daily.volume(d));
        logger.trace("Date: {} Week: {}", date, week);
      }
      else if (week == currentWeek) { // same week
        d2w.accumulate(daily.high(d),
                       daily.low(d),
                       daily.close(d),
                       daily.volume(d));
      }
      else {
        logger.error(ERRONEOUS_DATUM, date);
      }
    }

    final OHLCVTimeSeries weekly = d2w.generate(daily + _W);
    logger.info("Daily-to-weekly compression completed for: {}", daily);
    return weekly;
  }

  static class OHLCVAccumulator {

    private String             d;
    private double             o;
    private double             h;
    private double             l;
    private double             c;
    private long               v;

    private final List<String> dates   = new ArrayList<>();
    private final List<Double> opens   = new ArrayList<>();
    private final List<Double> highs   = new ArrayList<>();
    private final List<Double> lows    = new ArrayList<>();
    private final List<Double> closes  = new ArrayList<>();
    private final List<Long>   volumes = new ArrayList<>();

    public void newPeriod(final String date,
                          final double open,
                          final double high,
                          final double low,
                          final double close,
                          final long volume) {
      closePreviousPeriod();

      // open current period
      d = date;
      o = open;
      h = high;
      l = low;
      c = close;
      v = volume;
    }

    public void accumulate(final double high,
                           final double low,
                           final double close,
                           final long volume) {
      h = Math.max(h, high);
      l = Math.min(l, low);
      c = close;
      v += volume;
    }

    public OHLCVTimeSeries generate(final String seriesName) {
      closePreviousPeriod();

      final OHLCVTimeSeries series = new OHLCVTimeSeries(seriesName, dates.size() - ONE);
      // skip first element
      for (int i = ONE, s = ZERO; i < dates.size(); ++i, ++s) {
        series.set(dates.get(i),
                   opens.get(i),
                   highs.get(i),
                   lows.get(i),
                   closes.get(i),
                   volumes.get(i),
                   s);
      }
      return series;
    }

    private final void closePreviousPeriod() {
      dates.add(d);
      opens.add(o);
      highs.add(h);
      lows.add(l);
      closes.add(c);
      volumes.add(v);
    }

  }

}
