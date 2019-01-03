/**
 * Gaps.java  v0.1  3 January 2019 8:54:59 PM
 *
 * Copyright © 2019 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

/**
 * Gaps
 *
 * <p>References:
 * <li>https://stockcharts.com/school/doku.php?id=chart_school:chart_analysis:gaps_and_gap_analysis
 * <li>https://www.investopedia.com/university/charts/charts8.asp
 * <li>http://thepatternsite.com/GaugingGaps.html
 * <li>http://thepatternsite.com/gaps.html<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class Gaps extends AbstractIndicator {

  private static final String UP_GAPS   = "Up Gaps";
  private static final String DOWN_GAPS = "Down Gaps";

  public Gaps() {
    super(ONE);
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    throwExceptionIfShort(ohlcv);

    final int size = ohlcv.size();
    final double[] upGaps = new double[size - lookback];
    final double[] downGaps = new double[upGaps.length];
    Arrays.fill(upGaps, Double.NaN);
    Arrays.fill(downGaps, Double.NaN);

    int yesterday = start;
    double highYesterday = ohlcv.high(yesterday);
    double lowYesterday = ohlcv.low(yesterday);

    // compute indicator
    for (int today = yesterday + lookback; today < size; ++today) {
      final double lowToday = ohlcv.low(today);
      final double highToday = ohlcv.high(today);

      // up gaps and down gaps are mutually exclusive
      if (isGap(lowToday, highYesterday)) {      // up gap
        upGaps[yesterday] = lowToday;
      }
      else if (isGap(lowYesterday, highToday)) { // down gap
        downGaps[yesterday] = highToday;
      }

      // shift forward in time
      yesterday = today;
      highYesterday = highToday;
      lowYesterday = lowToday;
    }

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(UP_GAPS, dates, upGaps),
                         new TimeSeries(DOWN_GAPS, dates, downGaps));
  }

  /**
   *
   * @param low low price
   * @param high high price
   * @return true if {@code low} more than {@code high}
   */
  public static final boolean isGap(final double low, final double high) {
    return (low > high);
  }

}
