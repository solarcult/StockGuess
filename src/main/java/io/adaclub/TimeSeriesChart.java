package io.adaclub;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;


public class TimeSeriesChart extends ApplicationFrame {

    public TimeSeriesChart(String title,XYDataset xyDataset) {
        super(title);
        final JFreeChart chart = createChart(title,xyDataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setMouseZoomable( true , false );
        setContentPane(chartPanel);
        pack();
        setVisible(true);
    }

    private JFreeChart createChart(final String name, final XYDataset xydataset )
    {
        return ChartFactory.createTimeSeriesChart(
                name,
                "Date",
                "Price",
                xydataset,
                false,
                false,
                false);
    }

    public static void main( final String[ ] args )
    {
//        final String title = "Time Series Management";
//
//        final TimeSeriesChart demo = new TimeSeriesChart(title,null);
//        demo.pack( );
//        demo.setVisible( true );

    }


}
