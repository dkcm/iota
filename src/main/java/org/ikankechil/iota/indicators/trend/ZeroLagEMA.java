/**
 * ZeroLagEMA.java  v0.2  20 July 2015 11:18:26 pm
 *
 * Copyright © 2015-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

/**
 * Zero-Lag Exponential Moving Average (ZLEMA) by Peter Martin
 *
 * <p>References:
 * <li>http://traders.com/Documentation/FEEDbk_docs/2008/05/TradersTips/TradersTips.html<br>
 * <li>https://user42.tuxfamily.org/chart/manual/Zero_002dLag-Exponential-Moving-Average.html<br>
 * <li>http://www.mesasoftware.com/papers/ZeroLag.pdf<br>
 * <li>http://traders.com/Documentation/FEEDbk_docs/2010/11/TradersTips.html<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class ZeroLagEMA extends ZeroLagMA {

  public ZeroLagEMA() {
    this(TEN);
  }

  public ZeroLagEMA(final int period) {
    this(new EMA(period)); // lookback = (period - 1) * 2
  }

  public ZeroLagEMA(final EMA ema) {
    super(ema);

    // Formula:
    // Period:= Input("What Period",1,250,10);
    // EMA1:= Mov(CLOSE,Period,E);
    // EMA2:= Mov(EMA1,Period,E);
    // Difference:= EMA1 - EMA2;
    // ZeroLagEMA:= EMA1 + Difference;
  }

}
