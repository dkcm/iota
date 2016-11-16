/**
 * T3.java  v0.2  7 January 2015 9:56:35 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * T3 by Tim Tillson
 *
 * <p>Volume factor = 0 -> EMA, 1 -> DEMA, 0.7 -> T3<br>
 *
 * <p>http://www.fmlabs.com/reference/default.htm?url=T3.htm<br>
 * http://www.forexfactory.com/attachment.php?attachmentid=709307&d=1306688278<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class T3 extends AbstractIndicator {

  private final GeneralisedDEMA gd;

  private static final double   T3_VF = 0.7;

  public T3() {
    this(FIVE);
  }

  public T3(final int period) {
    this(period, T3_VF);
  }

  public T3(final int period, final double volumeFactor) {
    super(period, SIX * (period - ONE));
    throwExceptionIfNegative(volumeFactor);

    gd = new GeneralisedDEMA(period, volumeFactor);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // GD(n,v) = EMA(n)*(1+v) - EMA(EMA(n))*v
    // T3(n) = GD(GD(GD(n)))

    final TimeSeries gd1 = gd.generate(ohlcv).get(ZERO);
    final TimeSeries gd2 = gd.generate(gd1).get(ZERO);
    final double[] gd3 = gd.generate(gd2).get(ZERO).values();

    System.arraycopy(gd3, ZERO, output, ZERO, output.length);

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
