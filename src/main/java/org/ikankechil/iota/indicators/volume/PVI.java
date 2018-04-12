/**
 * PVI.java  v0.1  10 April 2018 11:26:40 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

/**
 * Positive Volume Index (PVI) by Paul Dysart and Norman Fosback
 *
 * <p>References:
 * <li>https://www.metastock.com/customer/resources/taaz/?p=92<br>
 * <li>https://www.investopedia.com/terms/p/pvi.asp<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class PVI extends PriceAccumulationVolumeIndicator {

  public PVI() {
    super();
  }

  public PVI(final int signal) {
    super(signal);
  }

  @Override
  boolean compareVolume(final long previous, final long current) {
    return (current > previous);
  }

}
