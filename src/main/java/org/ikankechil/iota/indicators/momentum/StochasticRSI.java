/**
 * StochasticRSI.java  v0.1  9 December 2014 12:25:28 PM
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
 * Stochastic RSI by Tushar Chande and Stanley Kroll
 *
 * <p>http://www.fmlabs.com/reference/default.htm?url=StochRSI.htm<br>
 * http://www.sierrachart.com/Download.php?Folder=SupportBoard&download=1446<br>
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:stochrsi<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class StochasticRSI extends AbstractIndicator {

  private final int fastK;
  private final int fastD;

  public StochasticRSI() {
    this(FOURTEEN, FIVE, THREE);
  }

  public StochasticRSI(final int period, final int fastK, final int fastD) {
    super(period, TA_LIB.stochRsiLookback(period, fastK, fastD, MAType.Sma));
    throwExceptionIfNegative(fastK, fastD);

    this.fastK = fastK;
    this.fastD = fastD;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    // Formula:
    // StochRSI = (RSI - Lowest Low RSI) / (Highest High RSI - Lowest Low RSI)
    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();

    final MInteger outBegIdx = new MInteger();
    final MInteger outNBElement = new MInteger();

    final double[] outFastK = new double[size - lookback];
    final double[] outFastD = new double[outFastK.length];

    final RetCode outcome = TA_LIB.stochRsi(ZERO,
                                            size - ONE,
                                            ohlcv.closes(),
                                            period,
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
