package it.eng.spagobi.engines.chart.charttypes.piecharts;


import it.eng.spago.base.SourceBean;

import java.awt.Font;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.PieDataset;

public class SimplePie extends PieCharts{

	public void configureChart(SourceBean content) {
		// TODO Auto-generated method stub
		super.configureChart(content);
	}

	public JFreeChart createChart(String chartTitle, Dataset dataset) {
		super.createChart(chartTitle, dataset);
        JFreeChart chart = ChartFactory.createPieChart(
                chartTitle,  
                (PieDataset)dataset,             // data
                true,                // include legend
                true,
                false
            );
        
        
        
            TextTitle title = chart.getTitle();
            title.setToolTipText("A title tooltip!");
            
            
            PiePlot plot = (PiePlot) chart.getPlot();
            plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
           // plot.setNoDataMessages("No data available");
            plot.setCircular(false);
            plot.setLabelGap(0.02);
            plot.setNoDataMessage("No data available");
           
            plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
            "{0} = {1}"));
            
            return chart;
		
	
	
	
	
	
	
	}

	
	
}
