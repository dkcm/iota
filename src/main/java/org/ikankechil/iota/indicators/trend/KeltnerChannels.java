/**
 * KeltnerChannels.java  v0.1  2 August 2015 10:04:02 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Keltner Channels by Chester Keltner and Linda Bradford Raschke
 * <p>
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:keltner_channels
 * http://www.futurepathtrading.com/pdf/TechnicaltradingManual-1.pdf
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V17/C12/095KELT.pdf
 * http://edmond.mires.co/GES816/39-Trade%20Breakouts%20And%20Retracements%20With%20TMV.pdf
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class KeltnerChannels extends AbstractIndicator {

  private final double        multiplier;
  private final int           atrPeriod;
  private final int           atrLookback;

  private static final String UPPER_BAND  = "Upper Keltner Channel";
  private static final String MIDDLE_BAND = "Middle Keltner Channel";
  private static final String LOWER_BAND  = "Lower Keltner Channel";

  public KeltnerChannels() {
    this(TWENTY, TWO, TEN);
  }

  public KeltnerChannels(final int ema, final double multiplier, final int atr) {
    super(ema, Math.max(TA_LIB.emaLookback(ema), TA_LIB.atrLookback(atr)));
    throwExceptionIfNegative(multiplier, atr);

    atrPeriod = atr;
    atrLookback = TA_LIB.atrLookback(atr);
    this.multiplier = multiplier;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();

    final MInteger outBegIdx = new MInteger();
    final MInteger outNBElement = new MInteger();

    // Formula:
    // Middle Line: 20-day exponential moving average
    // Upper Channel Line: 20-day EMA + (2 x ATR(10))
    // Lower Channel Line: 20-day EMA - (2 x ATR(10))

    final double[] highs = ohlcv.highs();
    final double[] lows = ohlcv.lows();
    final double[] closes = ohlcv.closes();

    // compute typical prices
    final double[] typical = new double[size];
    RetCode outcome = TA_LIB.typPrice(ZERO,
                                      size - ONE,
                                      highs,
                                      lows,
                                      closes,
                                      outBegIdx,
                                      outNBElement,
                                      typical);
    throwExceptionIfBad(outcome, ohlcv);

    // compute EMA
    final double[] ema = ema(typical, period);

    // compute ATR
    final double[] atr = new double[size - atrLookback];
    outcome = TA_LIB.atr(ZERO,
                         size - ONE,
                         highs,
                         lows,
                         closes,
                         atrPeriod,
                         outBegIdx,
                         outNBElement,
                         atr);
    throwExceptionIfBad(outcome, ohlcv);

    // compute indicator
    final double[] keltnerUpperChannel = new double[size - lookback];
    final double[] keltnerLowerChannel = new double[keltnerUpperChannel.length];
    for (int i = ZERO, a = atr.length - keltnerUpperChannel.length, m = ema.length - keltnerUpperChannel.length;
         i < keltnerUpperChannel.length;
         ++i, ++a, ++m) {
      final double buffer = atr[a] * multiplier;
      final double middle = ema[m];
      keltnerUpperChannel[i] = middle + buffer;
      keltnerLowerChannel[i] = middle - buffer;
    }

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(UPPER_BAND,
                                        dates,
                                        keltnerUpperChannel),
                         new TimeSeries(MIDDLE_BAND,
                                        dates,
                                        Arrays.copyOfRange(ema,
                                                           ema.length - keltnerUpperChannel.length,
                                                           ema.length)),
                         new TimeSeries(LOWER_BAND,
                                        dates,
                                        keltnerLowerChannel));
  }

}
