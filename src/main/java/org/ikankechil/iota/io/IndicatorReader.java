/**
 * IndicatorReader.java  v0.1  7 December 2014 11:59:45 am
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ikankechil.io.TextReader;
import org.ikankechil.io.TextTransform;
import org.ikankechil.io.TextTransformer;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.util.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reads indicator values.
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class IndicatorReader { // TODO support new file format

  private final TextReader  reader;

  private static final int  ZERO   = 0;
  private static final int  ONE    = 1;

  private static final char COMMA  = ',';

  static final Logger       logger = LoggerFactory.getLogger(IndicatorReader.class);

  public IndicatorReader() {
    reader = new TextReader();
  }

  public List<TimeSeries> read(final File source) throws FileNotFoundException, IOException {
    logger.info("Reading indicator(s) from: {}", source);

    final List<String> lines = reader.read(source);

    // read header(s)
    final List<String> headers = StringUtility.split(lines.remove(ZERO), COMMA);
    final List<TimeSeries> indicators = new ArrayList<>(headers.size() - ONE);
    for (int i = ONE; i < headers.size(); ++i) {
      indicators.add(new TimeSeries(headers.get(i), lines.size()));
    }

    // read and transform lines
    Collections.reverse(lines);
    logger.debug("Reversed order");
    new TextTransformer(new IndicatorTransform(indicators)).transform(lines);

    logger.info("Indicator(s) read from: {}", source);
    return indicators;
  }

  class IndicatorTransform implements TextTransform {

    private final List<TimeSeries> indicators;
    private int                    row = -ONE;

    public IndicatorTransform(final List<TimeSeries> indicators) {
      this.indicators = indicators;
    }

    @Override
    public String transform(final String line) {
      // Format (e.g.)
      // Date,MACD,MACD Signal,MACD Histogram
      // 20140902,1.4781013071103128,0.6559090333780755,0.8221922737322374
      // 20140801,1.372025734660486,0.4503609649450161,0.92166476971547

      ++row;
      final List<String> values = StringUtility.split(line, COMMA);

      final String date = values.get(ZERO); // save time by not parsing date
      for (int column = ONE; column < values.size(); ++column) {
        try {
          final double value = Double.parseDouble(values.get(column));

          indicators.get(column - ONE).set(date, value, row);
        }
        catch (final NumberFormatException nfE) {
          logger.warn("Bad number: {}", line, nfE);
        }
      }

      return date;
    }

  }

}
