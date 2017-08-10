/**
 * UptrendChannelBreakout.java  v0.1  10 August 2017 3:43:36 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.pattern;

import org.ikankechil.iota.indicators.pattern.UptrendChannels;

/**
 * Uptrend channel breakout strategy.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class UptrendChannelBreakout extends TrendlineBreakout {

  /**
   *
   *
   * @param uptrendChannels
   */
  public UptrendChannelBreakout(final UptrendChannels uptrendChannels) {
    super(uptrendChannels);
  }

}
