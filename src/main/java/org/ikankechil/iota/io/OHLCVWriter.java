/**
 * OHLCVWriter.java  v0.4  11 March 2015 4:49:46 PM
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
 *
 *
 * @author Daniel Kuan
 * @version 0.4
 */
public class OHLCVWriter extends TimeSeriesWriter {

  private final boolean       keepVolume;

  private static final Logger logger = LoggerFactory.getLogger(OHLCVWriter.class);

  public OHLCVWriter() {
    this(true);
  }

  public OHLCVWriter(final boolean keepVolume) {
    this.keepVolume = keepVolume;
  }

  public void write(final OHLCVTimeSeries prices, final File destination)
      throws IOException {
    logger.info("Writing OHLCV to: {}", destination);
    final String symbol = getSymbol(prices);
    final String[] dates = prices.dates();
    final List<String> lines = new ArrayList<>(prices.size());

    // write in reverse chronological order
    final StringBuilder line = new StringBuilder(symbol);
    for (int d = dates.length - 1; d >= ZERO; --d) {
      line.append(COMMA).append(dates[d])
          .append(COMMA).append(prices.open(d))
          .append(COMMA).append(prices.high(d))
          .append(COMMA).append(prices.low(d))
          .append(COMMA).append(prices.close(d));
      if (keepVolume) {
        line.append(COMMA).append(prices.volume(d));
      }
      lines.add(line.toString());
      line.setLength(symbol.length());  // clear date and OHLCV
    }

    writer.write(lines, destination);
    logger.info("OHLCV (lines: {}) written to: {}", lines.size(), destination);
  }

  private static final String getSymbol(final OHLCVTimeSeries prices) {
    final String seriesName = prices.toString();
    final int underscore = seriesName.indexOf(UNDERSCORE);
    // drop frequency from symbol name (if any)
    final String symbol = (underscore < ZERO) ? seriesName
                                              : seriesName.substring(ZERO, underscore);
    return symbol;
  }

}
