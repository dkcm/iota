/**
 * DowntrendChannelBreakout.java  v0.1  10 August 2017 3:44:57 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.pattern;

import org.ikankechil.iota.indicators.pattern.DowntrendChannels;

/**
 * Downtrend channel breakout strategy.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class DowntrendChannelBreakout extends TrendlineBreakout {

  /**
   *
   *
   * @param downtrendChannels
   */
  public DowntrendChannelBreakout(final DowntrendChannels downtrendChannels) {
    super(downtrendChannels);
  }

}
