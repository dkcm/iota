/**
 * DowntrendChannels.java  v0.2  15 June 2017 11:29:22 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import static org.ikankechil.iota.indicators.pattern.Trendlines.TrendSlopes.*;

/**
 * Downtrend Channels
 *
 * <p>References:
 * <li>http://thepatternsite.com/channels.html<br>
 * <li>http://stockcharts.com/school/doku.php?id=chart_school:chart_analysis:chart_patterns:price_channel_continuation
 * <li>http://www.investopedia.com/terms/p/price-channel.asp
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class DowntrendChannels extends Channels {

  public DowntrendChannels(final int awayPoints, final double breakoutThresholdPercentage, final double runawayThresholdPercentage) {
    this(awayPoints, breakoutThresholdPercentage, runawayThresholdPercentage, ENDPOINT_VICINITY);
  }

  public DowntrendChannels(final int awayPoints, final double breakoutThresholdPercentage, final double runawayThresholdPercentage, final int endpointVicinity) {
    super(awayPoints, breakoutThresholdPercentage, runawayThresholdPercentage, DOWN, endpointVicinity);
  }

}
