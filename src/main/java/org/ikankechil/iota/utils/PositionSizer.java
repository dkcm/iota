/**
 * PositionSizer.java  v0.1  26 October 2016 9:06:26 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.utils;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.Indicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Position-sizing utility.
 *
 * <p>http://www.investopedia.com/articles/trading/09/determine-position-size.asp<br>
 * http://www.vantharp.com/tharp-concepts/position-sizing.asp<br>
 * http://thepatternsite.com/MoneyMgmt.html<br>
 * http://stansberryresearch.com/investor-education/position-sizing/<br>
 * https://vantagepointtrading.com/archives/2031<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class PositionSizer {

  private final double        leveragedRiskCapitalPerPosition;
  private final Indicator     volatilityModel;

  private static final Logger logger = LoggerFactory.getLogger(PositionSizer.class);

  /**
   *
   *
   * @param capital available capital to trade
   * @param risk percentage of capital risked per trade idea
   * @param leverage capital multiplier
   * @param stopLoss maximum acceptable percentage loss
   */
  public PositionSizer(final double capital, final double risk, final double leverage, final double stopLoss) {
    this(capital, risk, leverage, stopLoss, null);
  }

  /**
   *
   *
   * @param capital available capital to trade
   * @param risk percentage of capital risked per trade idea
   * @param leverage capital multiplier
   * @param stopLoss maximum acceptable percentage loss
   * @param volatilityModel model used to compute volatility
   */
  public PositionSizer(final double capital, final double risk, final double leverage, final double stopLoss, final Indicator volatilityModel) {
    this.volatilityModel = volatilityModel;
    leveragedRiskCapitalPerPosition = capital * risk * leverage / stopLoss;

    logger.info("Leveraged Risk Capital per Position = ${}", leveragedRiskCapitalPerPosition);
  }

  public int[] sizePositions(final OHLCVTimeSeries ohlcv) {
    final int[] positionSizes;

    if (volatilityModel != null) {
      final double[] volatilities = volatilityModel.generate(ohlcv).get(0).values();

      positionSizes = new int[volatilities.length];
      for (int i = 0, j = volatilityModel.lookback(); i < positionSizes.length; ++i, ++j) {
        positionSizes[i] = sizePosition(leveragedRiskCapitalPerPosition, volatilities[i], ohlcv.close(j));
      }
    }
    else {
      positionSizes = new int[ohlcv.size()];
      for (int i = 0; i < positionSizes.length; ++i) {
        positionSizes[i] = sizePosition(leveragedRiskCapitalPerPosition, ohlcv.close(i));
      }
    }

    return positionSizes;
  }

  public static final int sizePosition(final double leveragedRiskCapitalPerPosition,
                                       final double pricePerUnit) {
    return (int) (leveragedRiskCapitalPerPosition / pricePerUnit);
  }

  public static final int sizePosition(final double leveragedRiskCapitalPerPosition,
                                       final double volatility,
                                       final double pricePerUnit) {
    return (int) (leveragedRiskCapitalPerPosition / (volatility * pricePerUnit));
  }

  public static final int sizePosition(final double leveragedRiskCapital,
                                       final double stopLoss,
                                       final double volatility,
                                       final double pricePerUnit) {
    return (int) (leveragedRiskCapital / (stopLoss * volatility * pricePerUnit));
  }

  /**
   * Computes the size of each trade.
   *
   * @param capital available capital to trade
   * @param risk percentage of capital risked per trade idea
   * @param leverage capital multiplier
   * @param stopLoss maximum acceptable percentage loss
   * @param volatility price variation over time
   * @param pricePerUnit price per share or contract
   * @return number of shares or contracts per trade (rounded down to the
   *         nearest integer)
   */
  public static final int sizePosition(final double capital,
                                       final double risk,
                                       final double leverage,
                                       final double stopLoss,
                                       final double volatility,
                                       final double pricePerUnit) {
    // Formula:
    // position size = (capital * risk * leverage) / (stopLoss * volatility * pricePerUnit))

    final double leveragedRiskCapital = capital * risk * leverage;
    return sizePosition(leveragedRiskCapital, stopLoss, volatility, pricePerUnit);
  }

}
