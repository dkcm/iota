/**
 * STARCBands.java  v0.4  8 January 2015 6:06:28 PM
 *
 * Copyright © 2015-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Indicator;

/**
 * Stoller Average Range Channels (STARC) by Manning Stoller
 *
 * <p>References:
 * <li>http://www.investopedia.com/terms/s/starc.asp
 * <li>http://fxtrade.oanda.com/learn/forex-indicators/starc-bands<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.4
 */
public class STARCBands extends AbstractIndicator {

  private final Indicator     atr;
  private final double        multiplier;

  private static final String UPPER_BAND  = "Upper STARC Band";
  private static final String MIDDLE_BAND = "Middle STARC Band";
  private static final String LOWER_BAND  = "Lower STARC Band";

  public STARCBands() {
    this(SIX, TWO);
  }

  public STARCBands(final int period, final double multiplier) {
    this(period, FIFTEEN, multiplier);
  }

  public STARCBands(final int period, final int atr, final double multiplier) {
    super(period, Math.max(period - ONE, atr));
    throwExceptionIfNegative(atr, multiplier);

    this.atr = new ATR(atr);
    this.multiplier = multiplier;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    // Formula
    // Upper STARC band = SMA + ATR * multiplier
    // Lower STARC band = SMA - ATR * multiplier

    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();

    // compute SMA and ATR
    final double[] smas = sma(ohlcv.closes(), period);
    final double[] atrs = atr.generate(ohlcv, start).get(ZERO).values();

    // compute indicator
    final double[] upperSTARCBand = new double[size - lookback];
    final double[] lowerSTARCBand = new double[upperSTARCBand.length];

    final int smaOffset = smas.length - atrs.length;
    for (int i = ZERO, s = smaOffset, a = lookback - atr.lookback();
         i < upperSTARCBand.length;
         ++i, ++s, ++a) {
      final double sma = smas[s];
      final double atrM = atrs[a] * multiplier;
      upperSTARCBand[i] = sma + atrM;
      lowerSTARCBand[i] = sma - atrM;
    }

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(UPPER_BAND,
                                        dates,
                                        upperSTARCBand),
                         new TimeSeries(MIDDLE_BAND,
                                        dates,
                                        Arrays.copyOfRange(smas,
                                                           smaOffset,
                                                           smas.length)),
                         new TimeSeries(LOWER_BAND,
                                        dates,
                                        lowerSTARCBand));
  }

}
