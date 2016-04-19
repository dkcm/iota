/**
 * OHLCVWriter.java v0.2 11 March 2015 4:49:46 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Writes open, high, low and closing prices as well as volumes.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class OHLCVWriter extends TimeSeriesWriter {
  // TODO
  // 1. drop frequency from symbol name (if any)
  // 2. number of significant figures to be written
  // 3. allow volume to be dropped

  private static final Logger logger = LoggerFactory.getLogger(OHLCVWriter.class);

  public void write(final OHLCVTimeSeries prices, final File destination)
      throws IOException {
    logger.info("Writing OHLCV to: {}", destination);
    final String seriesName = prices.toString();
    final int underscore = seriesName.indexOf(UNDERSCORE);
    // drop frequency from symbol name (if any)
    final String symbol = (underscore < ZERO) ? seriesName
                                              : seriesName.substring(ZERO, underscore);
    final String[] dates = prices.dates();
    final List<String> lines = new ArrayList<>(prices.size());

    // write in reverse chronological order
    for (int d = dates.length - 1; d >= ZERO; --d) {
      final StringBuilder line = new StringBuilder(symbol);
      line.append(COMMA).append(dates[d]);
      line.append(COMMA).append(prices.open(d));
      line.append(COMMA).append(prices.high(d));
      line.append(COMMA).append(prices.low(d));
      line.append(COMMA).append(prices.close(d));
      line.append(COMMA).append(prices.volume(d));
      lines.add(line.toString());
    }

    writer.write(lines, destination);
    logger.info("OHLCV (size: {}) written to: {}", lines.size(), destination);
  }

}
