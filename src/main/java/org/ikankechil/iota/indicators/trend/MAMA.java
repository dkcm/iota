/**
 * MAMA.java  v0.1  5 January 2015 7:12:17 PM
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
 * MESA Adaptive Moving Average (MAMA), an adaptive EMA that varies the EMA
 * alpha to the phase rate of change.  Also includes as its signal line a
 * Following Adaptive Moving Average (FAMA) whose alpha is half that of MAMA's.
 *
 * <p>http://www.mesasoftware.com/papers/MAMA.pdf<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MAMA extends AbstractIndicator {

  private final double        fast;
  private final double        slow;

  private static final double SLOW_LIMIT = 0.05;

  private static final String FAMA       = "FAMA"; // Following Adaptive Moving Average

  public MAMA() {
    this(HALF, SLOW_LIMIT);
  }

  public MAMA(final double fast, final double slow) {
    super(TA_LIB.mamaLookback(fast, slow));
    throwExceptionIfNegative(fast, slow);

    this.fast = fast;
    this.slow = slow;
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
                                        fast,
                                        slow,
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

}
