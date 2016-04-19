/**
 * TimeSeriesWriter.java v0.2 12 January 2015 4:06:52 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.io;

import java.util.List;

import org.ikankechil.io.TextWriter;
import org.ikankechil.iota.TimeSeries;

/**
 *
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
abstract class TimeSeriesWriter {

  final TextWriter    writer;

  static final int    ZERO       = 0;

  static final String EMPTY      = "";
  static final char   COMMA      = ',';
  static final char   QUOTES     = '"';
  static final char   UNDERSCORE = '_';

  static final String DATE       = "Date";

  public TimeSeriesWriter() {
    writer = new TextWriter();
  }

  static final String createHeader(final List<? extends TimeSeries> timeSeries) {
    final StringBuilder header = new StringBuilder(DATE);
    for (final TimeSeries series : timeSeries) {
      header.append(COMMA);
      // surround with " if series name has commas (e.g. PPO(12, 26, 9))
      final String seriesName = series.toString();
      if (seriesName.indexOf(COMMA) > 0) {
        header.append(QUOTES).append(seriesName).append(QUOTES);
      }
      else {
        header.append(seriesName);
      }
    }
    return header.toString();
  }

  static final TimeSeries longest(final List<? extends TimeSeries> timeSeries) {
    int max = 0;
    TimeSeries longest = null;
    for (final TimeSeries series : timeSeries) {
      if (series.size() > max) {
        longest = series;
        max = longest.size();
      }
    }
    return longest;
  }

}
