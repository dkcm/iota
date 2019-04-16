/**
 * KeltnerChannels.java  v0.3  2 August 2015 10:04:02 pm
 *
 * Copyright © 2015-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.TypicalPrice;
import org.ikankechil.iota.indicators.volatility.ATR;

/**
 * Keltner Channels by Chester Keltner and Linda Bradford Raschke
 *
 * <p>References:
 * <li>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:keltner_channels
 * <li>http://www.futurepathtrading.com/pdf/TechnicaltradingManual-1.pdf
 * <li>ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V17/C12/095KELT.pdf
 * <li>http://edmond.mires.co/GES816/39-Trade%20Breakouts%20And%20Retracements%20With%20TMV.pdf<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class KeltnerChannels extends AbstractIndicator {

  private final ATR                 averageTrueRange;
  private final double              volatilityMultiplier;

  private static final TypicalPrice TYPICAL_PRICE = new TypicalPrice();

  private static final String       UPPER_BAND    = "Upper Keltner Channel";
  private static final String       MIDDLE_BAND   = "Middle Keltner Channel";
  private static final String       LOWER_BAND    = "Lower Keltner Channel";

  public KeltnerChannels() {
    this(TWENTY, TWO, TEN);
  }

  public KeltnerChannels(final int ema, final double volatilityMultiplier, final int atr) {
    super(ema, Math.max(ema - ONE, atr));
    throwExceptionIfNegative(volatilityMultiplier, atr);

    averageTrueRange = new ATR(atr);
    this.volatilityMultiplier = volatilityMultiplier;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    // Formula:
    // Middle Line: 20-day exponential moving average
    // Upper Channel Line: 20-day EMA + (2 x ATR(10))
    // Lower Channel Line: 20-day EMA - (2 x ATR(10))

    throwExceptionIfShort(ohlcv);

    // compute EMA of typical prices
    final double[] typical = TYPICAL_PRICE.generate(ohlcv, start).get(ZERO).values();
    final double[] ema = ema(typical, period);

    // compute ATR
    final TimeSeries atr = averageTrueRange.generate(ohlcv, start).get(ZERO);

    // compute indicator
    final int size = ohlcv.size() - start;
    final double[] keltnerUpperChannel = new double[size - lookback];
    final double[] keltnerLowerChannel = new double[keltnerUpperChannel.length];
    for (int i = start, a = atr.size() - keltnerUpperChannel.length, m = ema.length - keltnerUpperChannel.length;
         i < keltnerUpperChannel.length;
         ++i, ++a, ++m) {
      final double buffer = atr.value(a) * volatilityMultiplier;
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
