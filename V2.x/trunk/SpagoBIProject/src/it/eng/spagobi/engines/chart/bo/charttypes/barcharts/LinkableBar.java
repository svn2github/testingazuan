package it.eng.spagobi.engines.chart.bo.charttypes.barcharts;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.engines.chart.bo.charttypes.utils.MyCategoryUrlGenerator;

import java.awt.Color;
import java.util.Iterator;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.Dataset;

public class LinkableBar extends BarCharts {

	String rootUrl=null;
	String mode="";

	public Dataset calculateValue() throws SourceBeanException {
		return super.calculateValue();		
	}


	public void configureChart(SourceBean content) {
		// TODO Auto-generated method stub
		super.configureChart(content);
	}


	public JFreeChart createChart(String chartTitle, Dataset dataset) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		super.createChart(chartTitle, dataset);

		CategoryAxis categoryAxis = new CategoryAxis(categoryLabel);
		ValueAxis valueAxis = new NumberAxis(valueLabel);
		org.jfree.chart.renderer.category.BarRenderer renderer = new org.jfree.chart.renderer.category.BarRenderer();

		renderer.setToolTipGenerator(new StandardCategoryToolTipGenerator());

		if(mode.equalsIgnoreCase(SpagoBIConstants.DOCUMENT_COMPOSITION)){
			renderer.setItemURLGenerator(new MyCategoryUrlGenerator(rootUrl));
		}
		else{
			renderer.setItemURLGenerator(new StandardCategoryURLGenerator(rootUrl));
		}

		CategoryPlot plot = new CategoryPlot((CategoryDataset)dataset, categoryAxis, valueAxis, renderer);
		plot.setOrientation(PlotOrientation.VERTICAL);
		JFreeChart chart = new JFreeChart(chartTitle, JFreeChart.DEFAULT_TITLE_FONT, plot, true);



		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

		// set the background color for the chart...
		chart.setBackgroundPaint(color);

		// get a reference to the plot for further customisation...
		//CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setDomainGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.white);

		// set the range axis to display integers only...
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		// disable bar outlines...
		//BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);

		// set up gradient paints for series...
		if(colorMap!=null){

			for (Iterator iterator = colorMap.keySet().iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				Color col= (Color)colorMap.get(key);
				renderer.setSeriesPaint((Integer.valueOf(key).intValue())-1, col);
			}
		}

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(
				CategoryLabelPositions.createUpRotationLabelPositions(
						Math.PI / 6.0));

		return chart;

	}

	public String getRootUrl() {
		return rootUrl;
	}

	public void setRootUrl(String rootUrl) {
		this.rootUrl = rootUrl;
	}

	public boolean isLinkable(){
		return true;
	}


	public String getMode() {
		return mode;
	}


	public void setMode(String mode) {
		this.mode = mode;
	}

}
