/**
 * FastStochastic.java v0.1 8 December 2014 8:49:06 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Fast Stochastic Oscillator
 * <p>
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:stochastic_oscillator_fast_slow_and_full
 *
 * @author Daniel Kuan
 * @version
 */
public class FastStochastic extends AbstractIndicator {

  private final int fastK;
  private final int fastD;

  public FastStochastic() {
    this(FOURTEEN, THREE);
  }

  public FastStochastic(final int fastK, final int fastD) {
    super(TA_LIB.stochFLookback(fastK, fastD, MAType.Sma));
    throwExceptionIfNegative(fastK, fastD);

    this.fastK = fastK;
    this.fastD = fastD;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();

    final MInteger outBegIdx = new MInteger();
    final MInteger outNBElement = new MInteger();

    final double[] outFastK = new double[size - lookback];
    final double[] outFastD = new double[outFastK.length];

    final RetCode outcome = TA_LIB.stochF(ZERO,
                                          size - ONE,
                                          ohlcv.highs(),
                                          ohlcv.lows(),
                                          ohlcv.closes(),
                                          fastK,
                                          fastD,
                                          MAType.Sma,
                                          outBegIdx,
                                          outNBElement,
                                          outFastK,
                                          outFastD);
    throwExceptionIfBad(outcome, ohlcv);

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(name, dates, outFastK),
                         new TimeSeries(name, dates, outFastD));
  }

}
