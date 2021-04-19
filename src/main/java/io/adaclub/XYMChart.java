package io.adaclub;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

import java.util.Collections;
import java.util.List;

public class XYMChart extends ApplicationFrame {

    JFreeChart jFreeChart;

    public XYMChart(String title, XYDataset xyDataset, List<XYTextAnnotation> xyTextAnnotations){
        super(title);
        jFreeChart = createChart(title,xyDataset);
        final ChartPanel chartPanel = new ChartPanel(jFreeChart);
        chartPanel.setMouseZoomable( true , false );
        for(XYTextAnnotation xyTextAnnotation : xyTextAnnotations){
            jFreeChart.getXYPlot().addAnnotation(xyTextAnnotation);
        }
        setContentPane(chartPanel);
        pack();
        setVisible(true);
    }

    private JFreeChart createChart(final String name, final XYDataset xydataset )
    {
        return ChartFactory.createScatterPlot(
                name,
                "maxRetracement",
                "maxEarn",
                xydataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false);
    }

    public JFreeChart getjFreeChart() {
        return jFreeChart;
    }

    public static void main(String[] args){

        DefaultXYDataset xydataset = new DefaultXYDataset ();

        String[] a ={"7","2","4"};
        String[] b ={"5","2","4"};

        double[][] data=new double[2][a.length];
        for(int i=0;i<a.length;i++)
        {
            data[0][i]=Double.parseDouble(a[i]);
            data[1][i]=Double.parseDouble(b[i]);
        }

        xydataset.addSeries("nothing", data);

        new XYMChart("COOL",xydataset, Collections.emptyList());
    }
}
