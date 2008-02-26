package it.eng.spagobi.engines.chart.charttypes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Point;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.dial.ArcDialFrame;
import org.jfree.chart.plot.dial.DialBackground;
import org.jfree.chart.plot.dial.DialPlot;
import org.jfree.chart.plot.dial.DialPointer;
import org.jfree.chart.plot.dial.StandardDialScale;
import org.jfree.data.general.ValueDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;

public class SimpleDial extends KpiChart{

	private String orientation="horizontal";

	public JFreeChart createDialChart(String chartTitle, ValueDataset dataset) {
		// get data for diagrams
		DialPlot plot = new DialPlot();
		plot.setDataset(dataset);

		ArcDialFrame dialFrame=null;
		if(orientation.equals("vertical")){
			plot.setView(0.78, 0.37, 0.22, 0.26);     
			dialFrame = new ArcDialFrame(-10.0, 20.0); 
		}
		else{
			plot.setView(0.21, 0.0, 0.58, 0.30);
			dialFrame = new ArcDialFrame(60.0, 60.0);
		}

		dialFrame.setInnerRadius(0.60);
		dialFrame.setOuterRadius(0.90);
		dialFrame.setForegroundPaint(Color.darkGray);
		dialFrame.setStroke(new BasicStroke(3.0f));
		plot.setDialFrame(dialFrame);

		GradientPaint gp = new GradientPaint(new Point(), 
				new Color(255, 255, 255), new Point(), 
				new Color(240, 240, 240));
		DialBackground sdb = new DialBackground(gp);
		sdb.setGradientPaintTransformer(new StandardGradientPaintTransformer(
				GradientPaintTransformType.VERTICAL));
		plot.addLayer(sdb);

		StandardDialScale scale=null;
		if(orientation.equals("vertical")){
			scale = new StandardDialScale(0, 100, -8, 16.0, 
					10.0, 4);
		}
		else{
			scale = new StandardDialScale(lower, upper, 115.0, 
					-50.0, increment, minorTickCount);
		}


		scale.setTickRadius(0.88);
		scale.setTickLabelOffset(0.07);
		//scale.setMajorTickIncrement(25.0);
		plot.addScale(0, scale);

		DialPointer needle = new DialPointer.Pin();
		needle.setRadius(0.82);
		plot.addLayer(needle);
		JFreeChart chart1 = new JFreeChart(plot);
		chart1.setTitle(chartTitle);
		return chart1;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}









}
