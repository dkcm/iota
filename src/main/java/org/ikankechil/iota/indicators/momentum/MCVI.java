/**
 * MCVI.java  v0.1  5 August 2015 12:14:51 am
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.OHLCVTimeSeries;

/**
 * Modified Chartmill Value Indicator (MCVI) by Brian Johnson
 * <p>
 * http://traderedge.net/2013/01/19/modified-chartmill-value-indicator-mcvi/
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MCVI extends CVI {

  private final double volatilityModifier;

  public MCVI() {
    this(TEN);
  }

  public MCVI(final int period) {
    super(period);

    volatilityModifier = Math.sqrt(period);
  }

  @Override
  protected double[] computeVolatility(final OHLCVTimeSeries ohlcv) {
    // Formula:
    // n = user specified number of periods
    // Value Consensus (VC) = MA((H+L)/2, n)
    // True High (TH) = Max(H, Ref(C,-1))
    // True Low (TL) = Min(L, Ref(C,-1))
    // True Range (TR) = TH - TL
    // Average True Range (ATR) = MA(TR, n)
    // CVI = (C - VC) / ATR
    // Modified Chartmill Value Indicator (MCVI) = (C - VC) / (ATR * (n ^ 0.5))

    final double[] volatility = super.computeVolatility(ohlcv);
    for (int i = ZERO; i < volatility.length; ++i) {
      volatility[i] *= volatilityModifier;
    }
    return volatility;
  }

}
