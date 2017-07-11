/**
 * MAMA.java  v0.1  5 January 2015 7:12:17 PM
 *
 * Copyright © 2015-2017 Daniel Kuan.  All rights reserved.
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
 * MESA Adaptive Moving Average (MAMA), an adaptive EMA that varies the EMA
 * alpha to the phase rate of change.  Also includes as its signal line a
 * Following Adaptive Moving Average (FAMA) whose alpha is half that of MAMA's.
 *
 * <p>http://www.mesasoftware.com/papers/MAMA.pdf<br>
 * http://www2.wealth-lab.com/wl5wiki/MAMA.ashx<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MAMA extends AbstractIndicator implements MA {

  private final double        fastLimit;
  private final double        slowLimit;

  private static final double SLOW_LIMIT = 0.05;

  private static final WMA    WMA        = new WMA(FOUR);

  // computation constants
  private static final double _0962      = 0.0962;
  private static final double _5769      = 0.5769;
  private static final double _54        = 0.54;
  private static final double _075       = 0.075;

  private static final String FAMA       = "FAMA"; // Following Adaptive Moving Average

  public MAMA() {
    this(HALF, SLOW_LIMIT);
  }

  /**
   *
   *
   * @param fastLimit maximum alpha value
   * @param slowLimit minimum alpha value
   */
  public MAMA(final double fastLimit, final double slowLimit) {
    super(TA_LIB.mamaLookback(fastLimit, slowLimit));
    throwExceptionIfNegative(fastLimit, slowLimit);

    this.fastLimit = fastLimit;
    this.slowLimit = slowLimit;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();

    final MInteger outBegIdx = new MInteger();
    final MInteger outNBElement = new MInteger();

    final double[] outMAMA = new double[size - lookback];
    final double[] outFAMA = new double[outMAMA.length];

    final RetCode outcome = TA_LIB.mama(ZERO,
                                        size - ONE,
                                        ohlcv.closes(),
                                        fastLimit,
                                        slowLimit,
                                        outBegIdx,
                                        outNBElement,
                                        outMAMA,
                                        outFAMA);
    throwExceptionIfBad(outcome, ohlcv);

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), outBegIdx.value, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(name, dates, outMAMA),
                         new TimeSeries(FAMA, dates, outFAMA));
  }

  @Override
  public List<TimeSeries> generate(final TimeSeries series, final int start) {
    throwExceptionIfShort(series);
    final int size = series.size();

    // compute detrender
    final TimeSeries smooth = WMA.generate(series).get(ZERO);



    final double[] mamas = new double[size - lookback];
    final double[] famas = new double[mamas.length];

    final double deltaPhase = 0;
    alpha(deltaPhase);

    final String[] dates = Arrays.copyOfRange(series.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, series);
    return Arrays.asList(new TimeSeries(name, dates, mamas),
                         new TimeSeries(FAMA, dates, famas));
  }

  private final double alpha(final double deltaPhase) {
    final double alpha = fastLimit / deltaPhase;
    return (alpha < slowLimit) ? slowLimit : alpha;
  }

}
