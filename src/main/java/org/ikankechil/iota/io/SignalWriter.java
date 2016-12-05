/**
 * SignalWriter.java  v0.3  12 January 2015 10:03:55 AM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.ikankechil.iota.Signal;
import org.ikankechil.iota.SignalTimeSeries;
import org.ikankechil.util.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Writes trading signals.
 *
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class SignalWriter extends TimeSeriesWriter {

  private static final Logger logger = LoggerFactory.getLogger(SignalWriter.class);

  public void write(final List<? extends SignalTimeSeries> signalSeries, final File destination)
      throws IOException {
    logger.info("Writing signals to: {}", destination);

    if (hasSignals(signalSeries)) {
      final List<String> lines = new ArrayList<>();

      // write header
      final String header = createHeader(signalSeries);
      lines.add(header);
      logger.debug("Header: {}", header);

      if (signalSeries.size() > 1) {
        lines.addAll(tabulate(signalSeries));
        logger.debug("Multiple strategies");
      }
      else {
        // write in reverse chronological order
        add(signalSeries.get(ZERO), lines);
        logger.debug("Single strategy");
      }

      writer.write(lines, destination);
      logger.info("Signals written to: {}", destination);
    }
    else {
      logger.info("No signals available; nothing written to: {}", destination);
    }
  }

  private static final boolean hasSignals(final List<? extends SignalTimeSeries> signalSeries) {
    boolean hasSignals = false;
    for (final SignalTimeSeries signals : signalSeries) {
      hasSignals |= (signals.size() > ZERO);
    }
    return hasSignals;
  }

  private static final List<String> tabulate(final List<? extends SignalTimeSeries> signalSeries) {
    // merge SignalTimeSeries as they may be of different lengths and dates
    // e.g.
    // Date,StrategyA,StrategyB,StrategyC
    // 20110901,SELL,SELL,BUY
    // 20090803,    ,BUY ,
    // 20080102,SELL,    ,
    // 20060801,    ,    ,SELL
    // 20050502,BUY ,    ,BUY

    // tabulate signals in reverse chronological order
    final Map<String, Signal[]> table = tabulate(signalSeries, StringUtility.REVERSE_ORDER);

    // convert table to lines
    return convert(table);
  }

  private static final Map<String, Signal[]> tabulate(final List<? extends SignalTimeSeries> signalSeries,
                                                      final Comparator<String> order) {
    // tabulate signals in reverse chronological order
    final Map<String, Signal[]> table = new TreeMap<>(order);

    int index = ZERO;
    final int size = signalSeries.size();
    for (final SignalTimeSeries signals : signalSeries) {
      final String[] dates = signals.dates();
      for (int i = ZERO; i < dates.length; ++i) {
        final String date = dates[i];
        if (table.containsKey(date)) {
          table.get(date)[index] = signals.signal(i);
        }
        else {
          final Signal[] value = new Signal[size];
          value[index] = signals.signal(i);
          table.put(date, value);
        }
      }
      ++index;
    }

    return table;
  }

  private static final List<String> convert(final Map<String, Signal[]> table) {
    // convert table to lines
    final List<String> lines = new ArrayList<>();

    final StringBuilder line = new StringBuilder();
    for (final Entry<String, Signal[]> entry : table.entrySet()) {
      line.append(entry.getKey());  // date
      for (final Signal signal : entry.getValue()) {
        line.append(COMMA)
            .append((signal == null) ? EMPTY : signal);
      }
      lines.add(line.toString());
      line.setLength(ZERO);
    }

    return lines;
  }

  private static final void add(final SignalTimeSeries signals, final List<String> lines) {
    // write in reverse chronological order
    final String[] dates = signals.dates();
    final StringBuilder line = new StringBuilder();
    for (int d = dates.length - 1; d >= ZERO; --d) {
      final Signal signal = signals.signal(d);
      line.append(dates[d])
          .append(COMMA)
          .append((signal == null) ? EMPTY : signal);
      lines.add(line.toString());
      line.setLength(ZERO);
    }
  }

}
