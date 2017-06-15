/**
 * UptrendChannels.java  v0.1  15 June 2017 11:29:02 pm
 *
 * Copyright � 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import static org.ikankechil.iota.indicators.pattern.Trendlines.TrendSlopes.*;

/**
 * Uptrend Channels
 *
 * <p>References:
 * <li>http://thepatternsite.com/channels.html<br>
 * <li>http://stockcharts.com/school/doku.php?id=chart_school:chart_analysis:chart_patterns:price_channel_continuation
 * <li>http://www.investopedia.com/terms/p/price-channel.asp
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class UptrendChannels extends Channels {

  public UptrendChannels(final int awayPoints, final double thresholdPercentage) {
    this(awayPoints, thresholdPercentage, ENDPOINT_VICINITY);
  }

  public UptrendChannels(final int awayPoints, final double thresholdPercentage, final int endpointVicinity) {
    super(awayPoints, thresholdPercentage, UP, endpointVicinity);
  }

}