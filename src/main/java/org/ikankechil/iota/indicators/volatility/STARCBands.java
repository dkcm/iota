/**
 * STARCBands.java  v0.2  8 January 2015 6:06:28 PM
 *
 * Copyright � 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

/**
 * Stoller Average Range Channels (STARC) by Manning Stoller
 *
 * <p>http://www.investopedia.com/terms/s/starc.asp<br>
 * http://fxtrade.oanda.com/learn/forex-indicators/starc-bands<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class STARCBands extends AbstractIndicator {

  private final ATR           atr;
  private final double        multiplier;
  private final int           smaOffset;

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
    smaOffset = lookback - (period - ONE);
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    // Formula
    // Upper STARC band = SMA + ATR * multiplier
    // Lower STARC band = SMA - ATR * multiplier

    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();
    final double[] closes = ohlcv.closes();

    // compute SMA and ATR
    final double[] smas = sma(closes, period);
    final double[] atrs = atr.generate(ohlcv).get(ZERO).values();

    // compute indicator
    final double[] upperSTARCBand = new double[size - lookback];
    final double[] lowerSTARCBand = new double[upperSTARCBand.length];

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
