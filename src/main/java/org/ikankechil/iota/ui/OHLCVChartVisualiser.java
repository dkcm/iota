/**
 * OHLCVChartVisualiser.java  v0.2  27 December 2014 3:14:46 PM
 *
 * Copyright © 2014-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.ui;

import static org.ikankechil.iota.ui.OHLCVChartVisualiser.DateFormats.*;
import static org.ikankechil.iota.ui.OHLCVChartVisualiser.PlotConfiguration.*;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.io.OHLCVReader;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTitleAnnotation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SegmentedTimeline;
import org.jfree.chart.labels.HighLowItemLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.HighLowRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.ShortTextTitle;
import org.jfree.data.time.Day;
import org.jfree.data.time.MovingAverage;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RefineryUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Convenience visualiser of OHLCV charts.
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class OHLCVChartVisualiser {
  // TODO Enhancements
  // 1. parallel plot construction
  // 2. [DONE] allow overlays (e.g. Bollinger Bands)
  // 3. transition to JavaFX?

  private final OHLCVReader     ohlcvReader;

  private final DateAxis        timeAxis;
  private final NumberAxis      ohlcAxis;
//  private final NumberAxis    vAxis;

  private final HighLowRenderer ohlcRenderer;

//  private static final char     UNDERSCORE  = '_';

  private static final double   LABEL_ANGLE = Math.PI / 2;
  private static final double   LABEL_X     = 0.001;
  private static final double   LABEL_Y     = 0.999;

  private static final Logger   logger      = LoggerFactory.getLogger(OHLCVChartVisualiser.class);

  public OHLCVChartVisualiser() {
    ohlcvReader = new OHLCVReader();

    timeAxis = new DateAxis("Date");
    timeAxis.setTimeline(SegmentedTimeline.newMondayThroughFridayTimeline());
    ohlcAxis = new NumberAxis(OHLC.label);
    ohlcAxis.setAutoRangeIncludesZero(false);
    ohlcAxis.setLabelAngle(LABEL_ANGLE);
//    vAxis = new NumberAxis(VOLUME.label);
//    vAxis.setLabelAngle(LABEL_ANGLE);

    ohlcRenderer = new HighLowRenderer() /*{ // Close >= Open ? Green : Red

      private static final long serialVersionUID = -5712458852461956470L;

      @Override
      public void drawItem(final Graphics2D g2,
                           final XYItemRendererState state,
                           final Rectangle2D dataArea,
                           final PlotRenderingInfo info,
                           final XYPlot plot,
                           final ValueAxis domainAxis,
                           final ValueAxis rangeAxis,
                           final XYDataset dataset,
                           final int series,
                           final int item,
                           final CrosshairState crosshairState,
                           final int pass) {
        if (dataset instanceof OHLCDataset) {
          final Color color = getColour((OHLCDataset) dataset, series, item);
          setSeriesPaint(series, color);
        }
        super.drawItem(g2, state, dataArea, info, plot, domainAxis, rangeAxis, dataset, series, item, crosshairState, pass);
      }

      private final Color getColour(final OHLCDataset dataset, final int series, final int item) {
        final double close = dataset.getCloseValue(series, item);
        final double open = dataset.getOpenValue(series, item);
        return (close >= open) ? Color.GREEN : Color.RED;
      }

    }*/;
    ohlcRenderer.setBaseToolTipGenerator(new HighLowItemLabelGenerator());
  }

  enum DateFormats {
    OHLCV("yyyyMMdd"),
    DISPLAY("d-MMM-yyyy");

    final DateFormat dateFormat;

    DateFormats(final String pattern) {
      dateFormat = new SimpleDateFormat(pattern, Locale.US);
    }
  }

  enum PlotConfiguration {
    OHLC("$", 50),
    VOLUME("Volume", 10),
    INDICATOR("", 15);  // unused label

    final String label;
    final int    plotWeight;

    PlotConfiguration(final String label, final int plotWeight) {
      this.label = label;
      this.plotWeight = plotWeight;
    }
  }

  public void displayChart(final File source, final List<? extends Indicator> overlays, final List<? extends Indicator> indicators)
      throws IOException, ParseException {
    final OHLCVTimeSeries ohlcv = ohlcvReader.read(source);

    // OHLC
    final OHLCDataset ohlcDataset = newOHLCDataset(ohlcv);
    final Map<Indicator, TimeSeriesCollection> ots = (overlays != null && !overlays.isEmpty()) ?
                                                     newIndicatorTimeSeries(ohlcv, overlays) :
                                                     null;
    final XYPlot ohlcPlot = newOHLCPlot(ohlcDataset, ots);
    logger.info("OHLC plot formed");

    // volume
    final TimeSeriesCollection volume = newVolumeTimeSeries(ohlcDataset);
    final XYPlot vPlot = newVolumePlot(volume);
    logger.info("Volume plot formed");

    // indicators
    final Map<Indicator, TimeSeriesCollection> its = newIndicatorTimeSeries(ohlcv, indicators);
    final List<XYPlot> iPlots = newIndicatorPlots(its);
    logger.info("Indicator plot(s) formed");

    // chart
    final JFreeChart chart = newChart(ohlcPlot, vPlot, iPlots);
    displayChart(chart);
    logger.info("Chart constructed and displayed");
  }

  private static final OHLCDataset newOHLCDataset(final OHLCVTimeSeries ohlcv) throws ParseException {
    return new DefaultHighLowDataset(ohlcv.toString(),
                                     toDates(ohlcv),
                                     ohlcv.highs(),
                                     ohlcv.lows(),
                                     ohlcv.opens(),
                                     ohlcv.closes(),
                                     toDoubles(ohlcv.volumes()));
  }

  private static final Date[] toDates(final OHLCVTimeSeries ohlcv) throws ParseException {
    final Date[] dates = new Date[ohlcv.size()];
    for (int i = 0; i < dates.length; ++i) {
      dates[i] = OHLCV.dateFormat.parse(ohlcv.date(i));
    }
    return dates;
  }

  private static final double[] toDoubles(final long[] longs) {
    final double[] doubles = new double[longs.length];
    for (int i = 0; i < longs.length; ++i) {
      doubles[i] = longs[i];
    }
    return doubles;
  }

  private XYPlot newOHLCPlot(final OHLCDataset ohlcDataset, final Map<Indicator, TimeSeriesCollection> overlays) {
    // OHLC
    final XYPlot ohlcPlot = new XYPlot(ohlcDataset, timeAxis, ohlcAxis, ohlcRenderer);
//    ohlcPlot.setDomainCrosshairVisible(true);
//    ohlcPlot.setDomainCrosshairPaint(false);
//    ohlcPlot.setRangeCrosshairVisible(true);
    ohlcPlot.setRangePannable(true);
    ohlcPlot.setBackgroundPaint(Color.GRAY);

    // Overlays
    if (overlays != null) {
      int index = 0;
      for (final Entry<Indicator, TimeSeriesCollection> overlay : overlays.entrySet()) {
        ohlcPlot.setDataset(++index, overlay.getValue());
        ohlcPlot.addAnnotation(new XYTitleAnnotation(LABEL_X,
                                                     LABEL_Y - (index * 50 * LABEL_X),
                                                     new ShortTextTitle(overlay.getKey().toString()),
                                                     RectangleAnchor.TOP_LEFT));
        final StandardXYItemRenderer overlayRenderer = new StandardXYItemRenderer();
        ohlcPlot.setRenderer(index, overlayRenderer);
        overlayRenderer.setSeriesPaint(index, Color.BLUE);
      }
    }

    return ohlcPlot;
  }

  private static final TimeSeriesCollection newVolumeTimeSeries(final OHLCDataset ohlcDataset) {
    final TimeSeries volume = new TimeSeries(VOLUME.label);
    final DefaultHighLowDataset ohlc = (DefaultHighLowDataset) ohlcDataset;
    for (int i = 0; i < ohlcDataset.getItemCount(0); ++i) {
      volume.add(new Day(ohlc.getXDate(0, i)), ohlcDataset.getVolumeValue(0, i));
    }
    return new TimeSeriesCollection(volume);
  }

  private XYPlot newVolumePlot(final TimeSeriesCollection volume) {
    // Volume
    final XYBarRenderer vRenderer = new XYBarRenderer();
    vRenderer.setDrawBarOutline(false);
    vRenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator(StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
                                                                     DISPLAY.dateFormat,
                                                                     new DecimalFormat("0,000.00")));
    vRenderer.setSeriesPaint(0, Color.BLUE);
    final XYPlot vPlot = new XYPlot(volume, timeAxis, new NumberAxis(), vRenderer);
    vPlot.addAnnotation(new XYTitleAnnotation(LABEL_X,
                                              LABEL_Y,
                                              new ShortTextTitle(VOLUME.label),
                                              RectangleAnchor.TOP_LEFT));
    vPlot.setBackgroundPaint(Color.GRAY);

    // VMA
    vPlot.setDataset(1, MovingAverage.createMovingAverage(volume, "MA", 50, 50));
    final StandardXYItemRenderer vmaRenderer = new StandardXYItemRenderer();
    vPlot.setRenderer(1, vmaRenderer);
    vmaRenderer.setSeriesPaint(0, Color.RED);

    return vPlot;
  }

  private static final Map<Indicator, TimeSeriesCollection> newIndicatorTimeSeries(final OHLCVTimeSeries ohlcv,
                                                                                   final List<? extends Indicator> indicators) {
    final Map<Indicator, TimeSeriesCollection> indicatorTimeSeries = new LinkedHashMap<>();
    for (final Indicator indicator : indicators) {
      final TimeSeriesCollection tsc = newIndicatorTimeSeries(ohlcv, indicator);
      indicatorTimeSeries.put(indicator, tsc);
    }
    return indicatorTimeSeries;
  }

  private static final TimeSeriesCollection newIndicatorTimeSeries(final OHLCVTimeSeries ohlcv,
                                                                   final Indicator indicator) {
    final TimeSeriesCollection tsc = new TimeSeriesCollection();
    try {
      for (final org.ikankechil.iota.TimeSeries timeSeries : indicator.generate(ohlcv)) {
        tsc.addSeries(toChartTimeSeries(timeSeries));
      }
    }
    catch (final IllegalArgumentException iaE) {
      logger.warn("Insufficient data points for: {}", indicator.toString(), iaE);
    }
    return tsc;
  }

  private static final TimeSeries toChartTimeSeries(final org.ikankechil.iota.TimeSeries timeSeries) {
    final TimeSeries chartTimeSeries = new TimeSeries(timeSeries.toString());
    for (int i = 0; i < timeSeries.size(); ++i) {
      chartTimeSeries.add(toDay(timeSeries.date(i)), timeSeries.value(i));
    }
    return chartTimeSeries;
  }

  private static final Day toDay(final String date) {
    final int year = Integer.parseInt(date.substring(0, 4));
    final int month = Integer.parseInt(date.substring(4, 6));
    final int day = Integer.parseInt(date.substring(6, 8));
    return new Day(day, month, year);
  }

  private List<XYPlot> newIndicatorPlots(final Map<Indicator, TimeSeriesCollection> indicators) {
    final List<XYPlot> iPlots = new ArrayList<>();
    for (final Entry<Indicator, TimeSeriesCollection> indicator : indicators.entrySet()) {
      final XYLineAndShapeRenderer iRenderer = new XYLineAndShapeRenderer(true, false);
      iRenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator(StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
                                                                       DISPLAY.dateFormat,
                                                                       new DecimalFormat("0.0000")));
      iRenderer.setSeriesPaint(0, Color.BLUE);
      iRenderer.setSeriesPaint(1, Color.RED);
      iRenderer.setSeriesPaint(2, Color.BLACK);
//      final NumberAxis iAxis = new NumberAxis(indicator.getKey().toString());
//      iAxis.setLabelAngle(LABEL_ANGLE);
      final NumberAxis iAxis = new NumberAxis();
      final XYPlot iPlot = new XYPlot(indicator.getValue(), timeAxis, iAxis, iRenderer);
      iPlot.addAnnotation(new XYTitleAnnotation(LABEL_X,
                                                LABEL_Y,
                                                new ShortTextTitle(indicator.getKey().toString()),
                                                RectangleAnchor.TOP_LEFT));
      iPlot.setBackgroundPaint(Color.GRAY);
      iPlots.add(iPlot);
    }
    return iPlots;
  }

  private final JFreeChart newChart(final XYPlot ohlcPlot, final XYPlot vPlot, final List<XYPlot> iPlots) {
    final CombinedDomainXYPlot combinedPlot = new CombinedDomainXYPlot(timeAxis);
    combinedPlot.add(ohlcPlot, OHLC.plotWeight);
    combinedPlot.add(vPlot, VOLUME.plotWeight);
    for (final XYPlot iPlot : iPlots) {
      combinedPlot.add(iPlot, INDICATOR.plotWeight);
    }

    combinedPlot.setGap(2.0);
    combinedPlot.setDomainGridlinePaint(Color.white);
    combinedPlot.setDomainGridlinesVisible(true);
    combinedPlot.setDomainPannable(true);

    final JFreeChart chart = new JFreeChart((String) ohlcPlot.getDataset().getSeriesKey(0),
                                            JFreeChart.DEFAULT_TITLE_FONT,
                                            combinedPlot,
                                            true);
//    ChartUtilities.applyCurrentTheme(chart);
    logger.info("New chart created");

    return chart;
  }

  private static final void displayChart(final JFreeChart chart) {
    // chart panel
    final ChartPanel panel = new ChartPanel(chart);
    panel.setFillZoomRectangle(true);
    panel.setMouseWheelEnabled(true);
    panel.setPreferredSize(new Dimension(1000, 540));
    panel.setDomainZoomable(true);
    panel.setRangeZoomable(true);
//    panel.setToolTipText(null);
    panel.setHorizontalAxisTrace(true);
    panel.setVerticalAxisTrace(true);

    // application frame
    final ApplicationFrame frame = new ApplicationFrame("OHLCV and Indicators: " + chart.getTitle().getText());
    frame.setContentPane(panel);
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);
  }

}
