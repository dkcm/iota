/**
 * WSMA.java  v0.1  22 October 2016 11:48:07 pm
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Weighted / Simple Moving Average (WSMA) by Alexandros Leontitsis and Jenny Pange
 *
 * <p>http://stat-athens.aueb.gr/~esi/proceedings/17/esi17-p519.pdf<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class WSMA extends AbstractIndicator implements MA {

  private final WMA           wma;
  private final SMA           sma;
  private final double        volumeFactor;

  private static final double VOLUME_FACTOR = 0.4;

  public WSMA() {
    this(FIVE);
  }

  public WSMA(final int period) {
    this(period, VOLUME_FACTOR);
  }

  public WSMA(final int period, final double volumeFactor) {
    super(period, THREE * (period - ONE));
    throwExceptionIfNegative(volumeFactor);

    this.volumeFactor = volumeFactor;
    wma = new WMA(period);
    sma = new SMA(period);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // GD(w,v) = (1 + v)WMA(w) - vSMA(w)
    // WSMA = GD(GD(GD))

    final TimeSeries wsma = gd(gd(gd(ohlcv)));
    System.arraycopy(wsma.values(), ZERO, output, ZERO, output.length);

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  private final TimeSeries gd(final TimeSeries values) {
    final double[] wmas = wma.generate(values).get(ZERO).values();
    final double[] smas = sma.generate(values).get(ZERO).values();

    final TimeSeries gd = new TimeSeries(EMPTY, wmas.length);
    for (int i = ZERO; i < gd.size(); ++i) {
      final double w = wmas[i];
      gd.value(w + volumeFactor * (w - smas[i]), i);
    }
    return gd;
  }

}
