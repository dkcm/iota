/**
 * IntradayIntensityIndex  v0.1  10 April 2018 2:35:00 pm
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

/**
 * Intraday Intensity Index (III) by David Bostian
 *
 * <p>References:
 * <li>https://www.investopedia.com/terms/i/intradayintensityindex.asp<br>
 * <li>https://www.tradingview.com/script/klr607Yi-INTRADAY-INTENSITY-INDEX-IIIX-by-KIVAN%C3%87-fr3762/<br>
 * <li>https://www.wisestocktrader.com/indicators/5807-bostian-s-intraday-intensity-index-by-david-bostian-for-metastock<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class IntradayIntensityIndex extends MoneyFlowVolume {

  @Override
  double mfv(final double high,
             final double low,
             final double close,
             final double range,
             final long volume) {
    // Formula:
    // III = (2 x close - high - low) / ((high - low) x volume)

    return ((close - low) - (high - close)) / (range * volume);
  }

}
