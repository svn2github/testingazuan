package it.eng.spagobi.engines.chart.bo.charttypes.clusterchart;

import java.awt.Color;
import java.awt.Font;

import it.eng.spagobi.engines.chart.utils.DatasetMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.DefaultXYZDataset;

public class SimpleCluster extends ClusterCharts {

	public JFreeChart createChart(DatasetMap datasets) {

		DefaultXYZDataset dataset=(DefaultXYZDataset)datasets.getDatasets().get("1");

		JFreeChart chart = ChartFactory.createBubbleChart(
				name, yLabel, xLabel, dataset, 
				PlotOrientation.HORIZONTAL, true, true, false);

		/*Font font = new Font("Tahoma", Font.BOLD, titleDimension);
		TextTitle title = new TextTitle(name, font);
		chart.setTitle(title);*/
		
		TextTitle title =setStyleTitle(name, styleTitle);
		chart.setTitle(title);
		if(subName!= null && !subName.equals("")){
			TextTitle subTitle =setStyleTitle(subName, styleSubTitle);
			chart.addSubtitle(subTitle);
		}

		chart.setBackgroundPaint(color);
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setForegroundAlpha(0.65f);
		

		XYItemRenderer renderer = plot.getRenderer();


		int seriesN=dataset.getSeriesCount();
		if(colorMap!=null){
			for (int i = 0; i < seriesN; i++) {
				String serieName=(String)dataset.getSeriesKey(i);
				Color color=(Color)colorMap.get(serieName);
				if(color!=null){
					renderer.setSeriesPaint(i, color);
				}	
			}
		}


		// increase the margins to account for the fact that the auto-range 
		// doesn't take into account the bubble size...
		NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
		//domainAxis.setAutoRange(true);
		domainAxis.setRange(yMin, yMax);
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		//rangeAxis.setAutoRange(true);
		rangeAxis.setRange(xMin,xMax);
		return chart;
	}

}
