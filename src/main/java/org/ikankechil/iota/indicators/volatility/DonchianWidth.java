/**
 * DonchianWidth.java  v0.1  7 May 2019 11:43:54 PM
 *
 * Copyright © 2019-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Indicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Donchian Width by Richard Donchian
 *
 * <p>References:
 * <li>https://www.tradingview.com/script/5HFUxNEv-Donchian-Channel-Width/
 * <li>https://www.barchart.com/education/technical-indicators/donchian_width
 * <li>https://library.tradingtechnologies.com/trade/chrt-ti-donchian-width.html<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class DonchianWidth extends AbstractIndicator {

  private final Indicator donchianChannels;

  public DonchianWidth() {
    this(TWENTY);
  }

  public DonchianWidth(final int period) {
    this(new DonchianChannels(period));
  }

  public DonchianWidth(final DonchianChannels donchianChannels) {
    super(donchianChannels.lookback());

    this.donchianChannels = donchianChannels;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {

    final List<TimeSeries> dc = donchianChannels.generate(ohlcv, start);
    final double[] uppers = dc.get(ZERO).values();
    final double[] lowers = dc.get(TWO).values();

    final double[] dw = difference(uppers, lowers);
    System.arraycopy(dw, ZERO, output, ZERO, output.length);

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
