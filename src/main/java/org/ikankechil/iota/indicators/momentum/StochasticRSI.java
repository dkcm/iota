/**
 * StochasticRSI.java  v0.2  9 December 2014 12:25:28 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

/**
 * Stochastic RSI by Tushar Chande and Stanley Kroll
 *
 * <p>http://www.fmlabs.com/reference/default.htm?url=StochRSI.htm<br>
 * http://www.sierrachart.com/Download.php?Folder=SupportBoard&download=1446<br>
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:stochrsi<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class StochasticRSI extends AbstractIndicator {

  private final RSI rsi;
  private final int fastK;
  private final int fastD;

  public StochasticRSI() {
    this(FOURTEEN, FIVE, THREE);
  }

  public StochasticRSI(final int period, final int fastK, final int fastD) {
    super(period, (period + fastK + fastD - TWO));
    throwExceptionIfNegative(fastK, fastD);

    rsi = new RSI(period);
    this.fastK = fastK;
    this.fastD = fastD;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    // Formula:
    // StochRSI = (RSI - Lowest RSI) / (Highest RSI - Lowest RSI)

    throwExceptionIfShort(ohlcv);

    // compute RSI
    final double[] rsis = rsi.generate(ohlcv).get(ZERO).values();

    // compute indicator
    final double[] fastKs = FastStochastic.fastStochasticK(fastK, rsis, rsis, rsis);
    final double[] fastDs = sma(fastKs, fastD);

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, ohlcv.size());

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(name,
                                        dates,
                                        Arrays.copyOfRange(fastKs,
                                                           fastD - ONE,
                                                           fastKs.length)),
                         new TimeSeries(name,
                                        dates,
                                        fastDs));
  }

}
