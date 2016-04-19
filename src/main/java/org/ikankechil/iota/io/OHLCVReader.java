/**
 * OHLCVReader.java v0.2  16 June 2014 7:06:15 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.io;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.ikankechil.io.TextReader;
import org.ikankechil.io.TextTransform;
import org.ikankechil.io.TextTransformer;
import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.util.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reads open, high, low and closing prices as well as volumes.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class OHLCVReader {

  private final TextReader    reader;

  // Numeric constants
  static final int            ZERO   = 0;

  private static final String EMPTY  = "";
  private static final char   DOT    = '.';

  static final Logger         logger = LoggerFactory.getLogger(OHLCVReader.class);

  public OHLCVReader() {
    reader = new TextReader();
  }

  public OHLCVTimeSeries read(final File source) throws IOException {
    logger.info("Reading OHLCV from: {}", source);

    final List<String> lines = reader.read(source);

    lines.remove(EMPTY);
    Collections.reverse(lines);
    logger.debug("Reversed order");

    final OHLCVTimeSeries prices = new OHLCVTimeSeries(name(source), lines.size());

    new TextTransformer(new OHLCVTransform(prices)).transform(lines);

    logger.info("Time series {} (size: {}) read from {}",
                lines.get(ZERO),
                lines.size(),
                source);
    return prices;
  }

  private static final String name(final File source) {
    final String name = source.getName();
    return name.substring(ZERO, name.lastIndexOf(DOT));
  }

  class OHLCVTransform implements TextTransform {

    private final OHLCVTimeSeries prices;
    private int                   row   = -1;

    // Constants
    private static final char     COMMA = ',';

    public OHLCVTransform(final OHLCVTimeSeries prices) {
      this.prices = prices;
    }

    @Override
    public String transform(final String line) {
      // Format
      // Symbol, Date, Open, High, Low, Close, Volume
      // e.g. GDX,20140605,22.50,22.79,22.42,22.65,23381300

      final List<String> values = StringUtility.split(line, COMMA);

      int column = ZERO;
      final String symbol = values.get(column);
      final String date = values.get(++column); // save time by not parsing date

      // TODO bottleneck: string to double conversion
      final double open = parseDouble(values.get(++column));
      final double high = parseDouble(values.get(++column));
      final double low = parseDouble(values.get(++column));
      final double close = parseDouble(values.get(++column));

      final long volume = ++column < values.size() ? parseLong(values.get(column)) : ZERO;

      prices.set(date, open, high, low, close, volume, ++row);

      return symbol;
    }

  }

  static final double parseDouble(final String s) {
    double d;
    try {
      d = Double.parseDouble(s); // TODO is it faster to parse in 2 parts (before and after decimal point)?
    }
    catch (final NumberFormatException nfE) {
      d = Double.NaN;
      logger.warn("Bad number: {}", s, nfE);
    }
    return d;
  }

  static final long parseLong(final String s) {
    long l;
    try {
      l = Long.parseLong(s);
    }
    catch (final NumberFormatException nfE) { // TODO parseDouble just in case?
      l = (long) Double.NaN;
      logger.warn("Bad number: {}", s, nfE);
    }
    return l;
  }

}
