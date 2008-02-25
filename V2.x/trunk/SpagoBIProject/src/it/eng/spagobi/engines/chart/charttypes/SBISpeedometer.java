/* --------------
 * DialDemo1.java
 * --------------
 * (C) Copyright 2006, 2007, by Object Refinery Limited.
 */

package it.eng.spagobi.engines.chart.charttypes;

import it.eng.spagobi.engines.chart.charttypes.utils.KpiInterval;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Point;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.dial.DialBackground;
import org.jfree.chart.plot.dial.DialCap;
import org.jfree.chart.plot.dial.DialPlot;
import org.jfree.chart.plot.dial.DialPointer;
import org.jfree.chart.plot.dial.DialTextAnnotation;
import org.jfree.chart.plot.dial.DialValueIndicator;
import org.jfree.chart.plot.dial.StandardDialFrame;
import org.jfree.chart.plot.dial.StandardDialRange;
import org.jfree.chart.plot.dial.StandardDialScale;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.general.ValueDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;


/**
 * A class to generate Speedometer charts
 */
public class SBISpeedometer {


	private static transient Logger logger=Logger.getLogger(SBISpeedometer.class);

	/** The dataset. */
	DefaultValueDataset dataset;

	double lower;
	double upper;
	double increment;
	int minorTickCount;
	String name;
	Vector intervals;





	public SBISpeedometer() {
		super();
		intervals=new Vector();	
	}





	/**
	 * Creates a chart displaying a circular dial.
	 * 
	 * @param chartTitle  the chart title.
	 * @param dataset  the dataset.

	 * @return A chart that displays a value as a dial.
	 */

	public JFreeChart createStandardDialChart(String chartTitle, ValueDataset dataset) {
		logger.debug("IN");
		DialPlot plot = new DialPlot();
		plot.setDataset(dataset);
		plot.setDialFrame(new StandardDialFrame());

		plot.setBackground(new DialBackground());
		DialTextAnnotation annotation1 = new DialTextAnnotation(name);
		annotation1.setFont(new Font("Dialog", Font.BOLD, 14));
		annotation1.setRadius(0.7);

		plot.addLayer(annotation1);

		DialValueIndicator dvi = new DialValueIndicator(0);
		plot.addLayer(dvi);

		StandardDialScale scale = new StandardDialScale(lower, 
				upper, -120, -300, 10.0, 4);
		scale.setMajorTickIncrement(increment);
		scale.setMinorTickCount(minorTickCount);
		scale.setTickRadius(0.88);
		scale.setTickLabelOffset(0.15);
		scale.setTickLabelFont(new Font("Dialog", Font.PLAIN, 14));
		plot.addScale(0, scale);

		plot.addPointer(new DialPointer.Pin());

		DialCap cap = new DialCap();
		plot.setCap(cap);

		// sets intervals
		for (Iterator iterator = intervals.iterator(); iterator.hasNext();) {
			KpiInterval interval = (KpiInterval) iterator.next();
			StandardDialRange range = new StandardDialRange(interval.getMin(), interval.getMax(), 
					interval.getColor()); 
			range.setInnerRadius(0.52);
			range.setOuterRadius(0.55);
			plot.addLayer(range);

		}

		GradientPaint gp = new GradientPaint(new Point(), 
				new Color(255, 255, 255), new Point(), 
				new Color(170, 170, 220));
		DialBackground db = new DialBackground(gp);
		db.setGradientPaintTransformer(new StandardGradientPaintTransformer(
				GradientPaintTransformType.VERTICAL));
		plot.setBackground(db);

		plot.removePointer(0);
		DialPointer.Pointer p = new DialPointer.Pointer();
		p.setFillPaint(Color.yellow);
		plot.addPointer(p);

		logger.debug("OUT");
		return new JFreeChart(chartTitle, plot);
	}








	public DefaultValueDataset getDataset() {
		return dataset;
	}





	public void setDataset(DefaultValueDataset dataset) {
		this.dataset = dataset;
	}





	public double getLower() {
		return lower;
	}





	public void setLower(double lower) {
		this.lower = lower;
	}





	public double getUpper() {
		return upper;
	}





	public void setUpper(double upper) {
		this.upper = upper;
	}





	public double getIncrement() {
		return increment;
	}





	public void setIncrement(double increment) {
		this.increment = increment;
	}





	public Vector getIntervals() {
		return intervals;
	}





	public void addInterval(KpiInterval interval) {
		this.intervals.add(interval);
	}





	public String getName() {
		return name;
	}





	public void setName(String name) {
		this.name = name;
	}





	public int getMinorTickCount() {
		return minorTickCount;
	}





	public void setMinorTickCount(int minorTickCount) {
		this.minorTickCount = minorTickCount;
	}



}
