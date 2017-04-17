/**
 * IndicatorWriter.java  v0.2  26 August 2014 1:06:42 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.ikankechil.iota.TimeSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Writes indicator values.
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class IndicatorWriter extends TimeSeriesWriter {

  private static final String WRITING_INDICATORS = "Writing indicator(s) to: {}";
  private static final String INDICATORS_WRITTEN = "Indicator(s) written to: {}";

  private static final Logger logger             = LoggerFactory.getLogger(IndicatorWriter.class);

  public void write(final Collection<? extends TimeSeries> indicators, final File destination)
      throws IOException {
    logger.info(WRITING_INDICATORS, destination);
    final String[] dates = longest(indicators).dates();
    final List<String> lines = new ArrayList<>(dates.length + 1);

    // write header
    final String header = createHeader(indicators);
    lines.add(header);

    // write in reverse chronological order
    for (int d = dates.length - 1; d >= ZERO; --d) {
      final StringBuilder line = new StringBuilder(dates[d]);
      for (final TimeSeries indicator : indicators) {
        line.append(COMMA);
        // line dates up properly
        final int offset = dates.length - indicator.size();
        final int i = d - offset;
        if (i >= ZERO) {
          line.append(indicator.value(i));
        }
      }
      lines.add(line.toString());
    }

    writer.write(lines, destination);
    logger.info(INDICATORS_WRITTEN, destination);
  }

  public void write(final String[] dates, final double[] indicator, final File destination)
      throws IOException {
    write(dates,
          Collections.singleton(indicator),
          destination);
  }

  /**
   * Dates assumed to be from the longest indicator.
   *
   * @param dates
   * @param indicators
   * @param destination
   * @throws IOException
   */
  public void write(final String[] dates, final Collection<double[]> indicators, final File destination)
      throws IOException {
    logger.info(WRITING_INDICATORS, destination);
    final List<String> lines = new ArrayList<>(dates.length);

    // write in reverse chronological order
    for (int d = dates.length - 1; d >= ZERO; --d) {
      final StringBuilder line = new StringBuilder(dates[d]);
      for (final double[] indicator : indicators) {
        line.append(COMMA).append(indicator[d]);
        // line dates up properly
        final int offset = dates.length - indicator.length;
        final int i = d - offset;
        if (i >= ZERO) {
          line.append(indicator[i]);
        }
      }
      lines.add(line.toString());
    }
    // TODO combine source code across methods
    writer.write(lines, destination);
    logger.info(INDICATORS_WRITTEN, destination);
  }

}
