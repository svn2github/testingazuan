package it.eng.spagobi.engines.chart.bo.charttypes.barcharts;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;

import java.awt.Color;
import java.awt.GradientPaint;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.Dataset;

public class SimpleBar extends BarCharts{

	boolean changeViewChecked=false; //false is vertical, true is horizontal

	public static final String CHANGE_VIEW_LABEL="Set Horizontal View";

	public void configureChart(SourceBean content) {
		// TODO Auto-generated method stub
		super.configureChart(content);
	}

	public JFreeChart createChart(String chartTitle, Dataset dataset) {
		// TODO Auto-generated method stub
		super.createChart(chartTitle, dataset);

		PlotOrientation plotOrientation=PlotOrientation.VERTICAL;
		if(changeViewChecked)
		{
			plotOrientation=PlotOrientation.HORIZONTAL;
		}


		JFreeChart chart = ChartFactory.createBarChart(
				chartTitle,       // chart title
				categoryLabel,               // domain axis label
				valueLabel,                  // range axis label
				(CategoryDataset)dataset,                  // data
				plotOrientation, // orientation
				true,                     // include legend
				true,                     // tooltips?
				false                     // URLs?
		);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

		// set the background color for the chart...
		chart.setBackgroundPaint(Color.white);

		// get a reference to the plot for further customisation...
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setDomainGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.white);

		// set the range axis to display integers only...
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		// disable bar outlines...
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);

		// set up gradient paints for series...
		GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.blue, 
				0.0f, 0.0f, new Color(0, 0, 64));
		GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.green, 
				0.0f, 0.0f, new Color(0, 64, 0));
		GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, Color.red, 
				0.0f, 0.0f, new Color(64, 0, 0));
		renderer.setSeriesPaint(0, gp0);
		renderer.setSeriesPaint(1, gp1);
		renderer.setSeriesPaint(2, gp2);

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(
				CategoryLabelPositions.createUpRotationLabelPositions(
						Math.PI / 6.0));

		return chart;

	}

	public Dataset calculateValue() throws SourceBeanException {
		return super.calculateValue();
	}

	public boolean isChangeViewChecked() {
		return changeViewChecked;
	}

	public void setChangeViewChecked(boolean changeViewChecked) {
		this.changeViewChecked = changeViewChecked;
	}

	public boolean isChangeableView() {
		return true;	
	}




}
