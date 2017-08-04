/**
 * Rectangles.java  v0.2  15 June 2017 10:06:49 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import static org.ikankechil.iota.indicators.pattern.Trendlines.TrendSlopes.*;

/**
 * Rectangles
 *
 * <p>References:
 * <li>http://stockcharts.com/school/doku.php?id=chart_school:chart_analysis:chart_patterns:rectangle_continuation<br>
 * <li>http://thepatternsite.com/recttops.html<br>
 * <li>http://thepatternsite.com/rectbots.html<br>
 * <li>http://www.investopedia.com/articles/trading/08/rectangle-formation.asp<br>
 * <li>https://tradingsim.com/blog/rectangle-pattern/<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class Rectangles extends Channels {

  public Rectangles(final int awayPoints, final double breakoutThresholdPercentage, final double runawayThresholdPercentage) {
    this(awayPoints, breakoutThresholdPercentage, runawayThresholdPercentage, ENDPOINT_VICINITY);
  }

  public Rectangles(final int awayPoints, final double breakoutThresholdPercentage, final double runawayThresholdPercentage, final int endpointVicinity) {
    super(awayPoints, breakoutThresholdPercentage, runawayThresholdPercentage, FLAT, endpointVicinity);
  }

}
