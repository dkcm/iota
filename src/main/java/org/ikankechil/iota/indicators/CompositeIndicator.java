/**
 * CompositeIndicator.java  v0.1  7 April 2017 12:59:58 am
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;

/**
 * An amalgam of <code>Indicator</code>s.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class CompositeIndicator extends AbstractIndicator {

  private final List<Indicator> indicators;

  public CompositeIndicator(final Indicator... indicators) {
    super(maxLookback(indicators));

    this.indicators = Arrays.asList(indicators);
  }

  private static final int maxLookback(final Indicator... indicators) {
    int maxLookback = ZERO;
    for (final Indicator indicator : indicators) {
      if (indicator.lookback() > maxLookback) {
        maxLookback = indicator.lookback();
      }
    }
    return maxLookback;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    final List<TimeSeries> allIndicatorValues = new ArrayList<>(indicators.size() << ONE);
    for (final Indicator indicator : indicators) {
      final List<TimeSeries> indicatorValues = indicator.generate(ohlcv, start);
      // truncate, if required
      allIndicatorValues.addAll(truncate(indicatorValues, lookback - indicator.lookback()));
    }
    return allIndicatorValues;
  }

  private static final List<TimeSeries> truncate(final List<TimeSeries> indicators, final int offset) {
    List<TimeSeries> truncatedIndicators;

    if (offset > ZERO) {
      final List<TimeSeries> newIndicators = new ArrayList<>(indicators.size());
      for (final TimeSeries indicator : indicators) {
        final TimeSeries newIndicator = new TimeSeries(indicator.toString(), indicator.size() - offset);
        for (int i = offset, j = ZERO; i < indicator.size(); ++i, ++j) {
          newIndicator.set(indicator.date(i), indicator.value(i), j);
        }
        newIndicators.add(newIndicator);
      }
      truncatedIndicators = newIndicators;
    }
    else {
      truncatedIndicators = indicators;
    }

    return truncatedIndicators;
  }

  @Override
  public List<TimeSeries> generate(final TimeSeries series, final int start) {
    final List<TimeSeries> indicatorValues = new ArrayList<>(indicators.size() << ONE);
    for (final Indicator indicator : indicators) {
      indicatorValues.addAll(indicator.generate(series, start));
    }
    return indicatorValues;
  }

  @Override
  public String toString() {
    return indicators.get(ZERO).toString();
  }

}
