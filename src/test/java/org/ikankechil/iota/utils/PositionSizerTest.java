/**
 * PositionSizerTest.java  v0.1  26 October 2016 2:42:35 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.utils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOError;
import java.io.IOException;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.indicators.volatility.ATR;
import org.ikankechil.iota.io.OHLCVReader;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JUnit test for <code>PositionSizer</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class PositionSizerTest {

  private static OHLCVTimeSeries OHLCV;

  private static final File      INPUT_DIRECTORY = new File(".//./src/test/resources/" + PositionSizerTest.class.getSimpleName());
  private static final File      OHLCV_FILE      = new File(INPUT_DIRECTORY, "MMM_w.csv");

  private static final Logger    logger          = LoggerFactory.getLogger(PositionSizerTest.class);

  static { // read once across all tests
    try {
      OHLCV = new OHLCVReader().read(OHLCV_FILE);
    }
    catch (final IOException ioE) {
      logger.error("Cannot read OHLCV file: {}", OHLCV_FILE, ioE);
      throw new IOError(ioE);
    }
  }

  @Test
  public void sizePosition() {
    final double capital = 100e3;   // dollars
    final double risk = 1.5;        // percent
    final double leverage = 5;      // multiple
    final double stopLoss = 25;     // percent
    final double volatility = 2.01; // dollars
    final double price = 12.34;

    final int expected = sizePosition(capital, risk, leverage, stopLoss, volatility, price);

    assertEquals(expected, PositionSizer.sizePosition(capital, risk, leverage, stopLoss, volatility, price));

    final double leveragedRiskCapital = leverage * risk * capital;
    assertEquals(expected, PositionSizer.sizePosition(leveragedRiskCapital, stopLoss, volatility, price));

    final double leveragedRiskCapitalPerPosition = leverage * risk * capital / stopLoss;
    assertEquals(expected, PositionSizer.sizePosition(leveragedRiskCapitalPerPosition, volatility, price));
  }

  @Test
  public void sizePositions() {
    final double capital = 250e3; // dollars
    final double risk = 9.7;      // percent
    final double leverage = 2;    // multiple
    final double stopLoss = 5;    // percent
    final Indicator volatilityModel = new ATR();

    final OHLCVTimeSeries ohlcv = OHLCV;
    final PositionSizer positionSizer = new PositionSizer(capital, risk, leverage, stopLoss, volatilityModel);
    final int[] positionSizes = positionSizer.sizePositions(ohlcv);

    final int[] expecteds = sizePositions(capital, risk, leverage, stopLoss, ohlcv, volatilityModel);
    assertArrayEquals(expecteds, positionSizes);
  }

  @Test
  public void sizePositionsWithoutVolatilityModel() {
    final double capital = 1000e3; // dollars
    final double risk = 2.858;     // percent
    final double leverage = 1.2;   // multiple
    final double stopLoss = 3.25;  // percent

    final OHLCVTimeSeries ohlcv = OHLCV;
    final PositionSizer positionSizer = new PositionSizer(capital, risk, leverage, stopLoss);
    final int[] positionSizes = positionSizer.sizePositions(ohlcv);

    final int[] expecteds = sizePositions(capital, risk, leverage, stopLoss, ohlcv);
    assertArrayEquals(expecteds, positionSizes);
  }

  private static final int[] sizePositions(final double capital,
                                           final double risk,
                                           final double leverage,
                                           final double stopLoss,
                                           final OHLCVTimeSeries ohlcv,
                                           final Indicator volatilityModel) {
    final double[] closes = ohlcv.closes();
    final double[] volatilities = volatilityModel.generate(ohlcv).get(0).values();
    final int[] positionSizes = new int[volatilities.length];
    for (int i = 0, j = closes.length - volatilities.length; i < positionSizes.length; ++i, ++j) {
      positionSizes[i] = sizePosition(capital, risk, leverage, stopLoss, volatilities[i], closes[j]);
    }
    return positionSizes;
  }

  private static final int[] sizePositions(final double capital,
                                           final double risk,
                                           final double leverage,
                                           final double stopLoss,
                                           final OHLCVTimeSeries ohlcv) {
    final double[] closes = ohlcv.closes();
    final int[] positionSizes = new int[closes.length];
    for (int i = 0; i < positionSizes.length; ++i) {
      positionSizes[i] = sizePosition(capital, risk, leverage, stopLoss, 1, closes[i]);
    }
    return positionSizes;
  }

  private static final int sizePosition(final double capital,
                                        final double risk,
                                        final double leverage,
                                        final double stopLoss,
                                        final double volatility,
                                        final double pricePerUnit) {
    return (int) ((capital * risk * leverage) / (stopLoss * volatility * pricePerUnit));
  }

}
