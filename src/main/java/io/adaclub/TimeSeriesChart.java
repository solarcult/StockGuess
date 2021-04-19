package io.adaclub;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.xy.XYDataset;


public class TimeSeriesChart extends ApplicationFrame {

    JFreeChart jFreeChart;


    public TimeSeriesChart(String title,XYDataset xyDataset) {
        super(title);
        jFreeChart = createChart(title,xyDataset);
        final ChartPanel chartPanel = new ChartPanel(jFreeChart);
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

    public JFreeChart getjFreeChart() {
        return jFreeChart;
    }

}
