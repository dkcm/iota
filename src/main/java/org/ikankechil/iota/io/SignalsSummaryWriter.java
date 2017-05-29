/**
 * SignalsSummaryWriter.java  v0.1  28 March 2017 12:27:18 am
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.ikankechil.iota.SignalTimeSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Writes trading signal summaries.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class SignalsSummaryWriter extends TimeSeriesWriter {

  private static final String SIGNALS_SUMMARY_HEADER = "Symbol,Date,Signal";

  private static final Logger logger                 = LoggerFactory.getLogger(SignalsSummaryWriter.class);

  public void write(final Collection<? extends SignalTimeSeries> signalsSummary, final File destination)
      throws IOException {
    logger.info("Writing signals summary to: {}", destination);

    final List<String> lines = new ArrayList<>(signalsSummary.size());
    lines.add(SIGNALS_SUMMARY_HEADER);
    logger.debug("Header: {}", SIGNALS_SUMMARY_HEADER);

    for (final SignalTimeSeries signals : signalsSummary) {
      final String symbol = signals.toString();
      for (int i = ZERO; i < signals.size(); ++i) {
        final StringBuilder line = new StringBuilder(symbol);
        line.append(COMMA)
                   .append(signals.date(i))
                   .append(COMMA)
                   .append(signals.signal(i));

        lines.add(line.toString());
      }
    }

    writer.write(lines, destination);
    logger.info("Signals summary written to: {}", destination);
  }

}
