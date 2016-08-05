/**
 * Phasor.java  v0.1  11 March 2015 11:07:37 AM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 *
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class Phasor extends AbstractIndicator {

  private static final String IN_PHASE   = "Phasor in-phase";
  private static final String QUADRATURE = "Phasor quadrature";

  public Phasor() {
    super(TA_LIB.htPhasorLookback());
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();

    final MInteger outBegIdx = new MInteger();
    final MInteger outNBElement = new MInteger();

    final double[] outInPhase = new double[size - lookback];
    final double[] outQuadrature = new double[outInPhase.length];

    final RetCode outcome = TA_LIB.htPhasor(ZERO,
                                            size - ONE,
                                            ohlcv.closes(),
                                            outBegIdx,
                                            outNBElement,
                                            outInPhase,
                                            outQuadrature);
    throwExceptionIfBad(outcome, ohlcv);

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(IN_PHASE, dates, outInPhase),
                         new TimeSeries(QUADRATURE, dates, outQuadrature));
  }

}
