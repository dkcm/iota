/**
 * Aroon.java v0.1  4 December 2014 10:45:54 AM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
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
 * Aroon by Tushar Chande
 * <p>
 * <a href=
 * "http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:aroon"
 * >http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:
 * aroon</a>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class Aroon extends AbstractIndicator {

//  private final double[]      aroons;

  private static final String AROON_UP   = "Aroon Up";
  private static final String AROON_DOWN = "Aroon Down";

  public Aroon() {
    this(25);
  }

  public Aroon(final int period) {
    super(period, TA_LIB.aroonLookback(period));

//    // set-up aroons
//    aroons = new double[period + ONE];
//    int i = ZERO;
//    aroons[i] = HUNDRED_PERCENT;
//    final double factor = HUNDRED_PERCENT / period;
//    for (; ++i < period; ) {
//      aroons[i] = (period - i) * factor;  // = (period - i) / period x 100
//    }
//    aroons[i] = ZERO;
  }

//  @Override
//  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
//    // Formula:
//    // Aroon-Up = ((25 - Days Since 25-day High)/25) x 100
//    // Aroon-Down = ((25 - Days Since 25-day Low)/25) x 100
//
//    throwExceptionIfShort(ohlcv);
//    final int size = ohlcv.size();
//
//    final double[] highs = ohlcv.highs();
//    final double[] lows = ohlcv.lows();
//
//    // find first high and low
//    int i = ZERO;
//    int maxIndex = i;
//    int minIndex = i;
//    double max = highs[maxIndex];
//    double min = lows[minIndex];
//    for (; ++i < period; ) {
//      final double high = highs[i];
//      if (high > max) {
//        max = high;
//        maxIndex = i;
//      }
//
//      final double low = lows[i];
//      if (low < min) {
//        min = low;
//        minIndex = i;
//      }
//    }
//
//    // compute indicator
//    final double[] aroonUp = new double[size - lookback];
//    final double[] aroonDown = new double[aroonUp.length];
//
////    i = period;
//    for (int a = ZERO; i < size; ++a, ++i) {
//      // compute aroon up
//      double high = highs[i];
//      if (high > max) {
//        max = high;
//        maxIndex = i;
//        aroonUp[a] = aroons[ZERO];
//      }
//      else {
//        if (maxIndex < i - period) { // out of window
//          max = highs[++maxIndex];
//          for (int j = ++maxIndex; j < i; ++j) { // find new high
//            high = highs[j];
//            if (high > max) {
//              max = high;
//              maxIndex = j;
//            }
//          }
//        }
//        aroonUp[a] = aroons[i - maxIndex];
//      }
//
//      // compute aroon down
//      double low = lows[i];
//      if (low < min) {
//        min = low;
//        minIndex = i;
//        aroonDown[a] = aroons[ZERO];
//      }
//      else {
//        if (minIndex < i - period) { // out of window
//          min = lows[++minIndex];
//          for (int j = ++minIndex; j < i; ++j) { // find new low
//            low = lows[j];
//            if (low < min) {
//              min = low;
//              minIndex = j;
//            }
//          }
//        }
//        aroonDown[a] = aroons[i - minIndex];
//      }
//    }
//
//    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);
//
//    logger.info(GENERATED_FOR, name, ohlcv);
//    return Arrays.asList(new TimeSeries(AROON_UP, dates, aroonUp),
//                         new TimeSeries(AROON_DOWN, dates, aroonDown));
//  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    // Formula:
    // Aroon-Up = ((25 - Days Since 25-day High)/25) x 100
    // Aroon-Down = ((25 - Days Since 25-day Low)/25) x 100

    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();

    final MInteger outBegIdx = new MInteger();
    final MInteger outNBElement = new MInteger();

    final double[] aroonUp = new double[size - lookback];
    final double[] aroonDown = new double[aroonUp.length];

    final RetCode outcome = TA_LIB.aroon(ZERO,
                                         size - ONE,
                                         ohlcv.highs(),
                                         ohlcv.lows(),
                                         period,
                                         outBegIdx,
                                         outNBElement,
                                         aroonDown,
                                         aroonUp);
    throwExceptionIfBad(outcome, ohlcv);

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(AROON_UP, dates, aroonUp),
                         new TimeSeries(AROON_DOWN, dates, aroonDown));
  }

}
