/**
 * NVI.java  v0.2  17 July 2015 3:03:49 PM
 *
 * Copyright © 2015-2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

/**
 * Negative Volume Index (NVI) by Paul Dysart and Norman Fosback
 *
 * <p>References:
 * <li>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:negative_volume_inde<br>
 * <li>https://www.metastock.com/customer/resources/taaz/?p=75<br>
 * <li>https://www.investopedia.com/terms/n/nvi.asp<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class NVI extends PriceAccumulationVolumeIndicator {

  public NVI() {
    super();
  }

  public NVI(final int signal) {
    super(signal);
  }

  @Override
  boolean compareVolume(final long previous, final long current) {
    // Formula:
    // 1. Cumulative NVI starts at 1000
    // 2. Add the Percentage Price Change to Cumulative NVI when Volume Decreases
    // 3. Cumulative NVI is Unchanged when Volume Increases
    // 4. Apply a 255-day EMA for Signals

    return (current < previous);
  }

}
