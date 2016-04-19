/**
 * FXC.java v0.2 2 April 2015 3:40:09 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.ui;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.io.OHLCVWriter;
import org.ikankechil.iota.utils.FXRateCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A command-line application that computes currency cross rates.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class FXC {

  private final OptionParser       parser;

  // parameters
  private final OptionSpec<File>   inputDir;
  private final OptionSpec<File>   outputDir;
  private final OptionSpec<String> baseCurrency;
  private final OptionSpec<String> quoteCurrency;
  private final OptionSpec<String> preferredCommonCurrency;
  private final OptionSpec<Void>   reciprocal;

  private static final String      CSV    = ".csv";

  private static final Logger      logger = LoggerFactory.getLogger(FXC.class);

  public FXC() {
    // command-line options:
    // -i input directory
    // -o output directory
    // -b base currency
    // -q quote currency
    // -c preferred common currency
    // -r compute reciprocal rates as well
    parser = new OptionParser();

    // Configuring command-line options
    parser.acceptsAll(Arrays.asList("h", "?"), "Show help").forHelp();

    // parameters
    reciprocal = parser.accepts("r", "Compute reciprocal rates as well");
    outputDir = parser.accepts("o", "Output FX rates directory")
                      .withRequiredArg()
                      .ofType(File.class);
    baseCurrency = parser.accepts("b", "Base currency")
                         .withRequiredArg();
    quoteCurrency = parser.accepts("q", "Quote currency")
                          .requiredUnless(baseCurrency)
                          .withRequiredArg();
    preferredCommonCurrency = parser.accepts("c", "Preferred common currency")
                                    .withRequiredArg();
    inputDir = parser.accepts("i", "Input FX rates directory")
                     .requiredIf(outputDir,
                                 baseCurrency,
                                 quoteCurrency,
                                 preferredCommonCurrency,
                                 reciprocal)
                     .withRequiredArg()
                     .ofType(File.class);

    // operands
    parser.nonOptions("ISO 4217 currency codes");
  }

  public static void main(final String... arguments) throws IOException {
    new FXC().execute(arguments);
  }

  public void execute(final String... arguments) throws IOException {
    logger.info("Command-line option(s): {}", (Object) arguments);

    try {
      final OptionSet options = parser.parse(arguments);

      final FXRateCalculator fxc = new FXRateCalculator(options.valueOf(inputDir),
                                                        options.valueOf(preferredCommonCurrency));
      @SuppressWarnings("unchecked")
      final List<String> currencies = (List<String>) options.nonOptionArguments();

      // compute rates
      final List<OHLCVTimeSeries> fxRates;
      if (options.has(baseCurrency)) {
        // -i <inputFXRatesDirectory> -b <baseCurrency> <quoteCurrencies...>
        fxRates = isSingle(currencies) ?
                  Collections.singletonList(fxc.computeFXRates(options.valueOf(baseCurrency),
                                                               currencies.get(0))) :
                  fxc.computeFXRates(options.valueOf(baseCurrency),
                                     currencies);
      }
      else if (options.has(quoteCurrency)) {
        // -i <inputFXRatesDirectory> -q <quoteCurrency> <baseCurrencies...>
        fxRates = isSingle(currencies) ?
                  Collections.singletonList(fxc.computeFXRates(currencies.get(0),
                                                               options.valueOf(quoteCurrency))) :
                  fxc.computeFXRates(currencies,
                                     options.valueOf(quoteCurrency));
      }
      else {
        throw new IllegalArgumentException("Neither base nor quote currency specified");
      }

      // write rates to file(s)
      final File parent = options.valueOf(outputDir); // null parent acceptable
      write(fxRates, parent);

      // invert rates
      if (options.has(reciprocal)) {
        final List<OHLCVTimeSeries> inverseFXRates = fxc.invertFXRates(fxRates);
        write(inverseFXRates, parent);
      }
    }
    catch (final OptionException |
                 IllegalArgumentException |
                 NullPointerException |
                 InterruptedException e) {
      System.out.println("Command-line option(s): " + Arrays.asList(arguments));
      System.out.println("Error: " + e.getMessage());
      parser.printHelpOn(System.out);
      logger.error(e.getMessage(), e);
    }
  }

  private static final boolean isSingle(final List<String> currencies) {
    return (currencies.size() == 1);
  }

  private static final void write(final List<OHLCVTimeSeries> fxRates,
                                  final File parent)
      throws IOException {
    final OHLCVWriter writer = new OHLCVWriter();
    for (final OHLCVTimeSeries fxRate : fxRates) {
      if (fxRate.size() > 0) {
        writer.write(fxRate, new File(parent, fxRate + CSV));
      }
      else {
        logger.info("Nothing to write for: {}", fxRate);
      }
    }
  }

}
