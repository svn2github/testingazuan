/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.engines.kpi.bo.charttypes.dialcharts;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.ValueDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spagobi.engines.chart.bo.charttypes.dialcharts.SBISpeedometer;
import it.eng.spagobi.engines.chart.bo.charttypes.utils.KpiInterval;
import it.eng.spagobi.engines.chart.utils.DatasetMap;
import it.eng.spagobi.engines.kpi.bo.ChartImpl;

/**
 * 
 * @author Chiara Chiarelli
 * 
 */

public class Speedometer extends ChartImpl {
	
	private static transient Logger logger=Logger.getLogger(SBISpeedometer.class);


	double increment=0.0;
	int minorTickCount=5;
	boolean dialtextuse = false ;
	String dialtext = "";


	/**
	 * Instantiates a new sBI speedometer.
	 */
	public Speedometer() {
		super();
		intervals=new Vector();	
	}

	public void configureChart(HashMap conf) {
		logger.info("IN");
		super.configureChart(conf);
		logger.debug("OUT");
	}
	
	public void setThresholds(List thresholds) {
		logger.info("IN");
		super.setThresholds(thresholds);
		logger.debug("OUT");
	}


	/**
	 * Creates a chart of type speedometer.
	 * 
	 * @param chartTitle  the chart title.
	 * @param dataset  the dataset.
	 * 
	 * @return A chart speedometer.
	 */

	public JFreeChart createChart(DatasetMap datasets) {
		logger.debug("IN");
		Dataset dataset=(Dataset)datasets.getDatasets().get("1");
		
		DialPlot plot = new DialPlot();
		plot.setDataset((ValueDataset)dataset);
		plot.setDialFrame(new StandardDialFrame());

		plot.setBackground(new DialBackground());
		if(dialtextuse){
			DialTextAnnotation annotation1 = new DialTextAnnotation(dialtext);			
			annotation1.setFont(styleTitle.getFont());
			annotation1.setRadius(0.7);

			plot.addLayer(annotation1);
		}
		
		DialValueIndicator dvi = new DialValueIndicator(0);
		plot.addLayer(dvi);

		increment = (upper-lower)/10;
		StandardDialScale scale = new StandardDialScale(lower, upper, -120, -300, 10.0, 4);
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
		JFreeChart chart=new JFreeChart(name, plot);
		
		TextTitle title = setStyleTitle(name, styleTitle);
		chart.setTitle(title);
		if(subName!= null && !subName.equals("")){
			TextTitle subTitle =setStyleTitle(subName, styleSubTitle);
			chart.addSubtitle(subTitle);
		}
		
		chart.setBackgroundPaint(color);
		return chart;
	}



	/**
	 * Gets the intervals.
	 * 
	 * @return the intervals
	 */
	public Vector getIntervals() {
		return intervals;
	}





	/**
	 * Adds the interval.
	 * 
	 * @param interval the interval
	 */
	public void addInterval(KpiInterval interval) {
		this.intervals.add(interval);
	}


	/**
	 * Gets the increment.
	 * 
	 * @return the increment
	 */
	public double getIncrement() {
		return increment;
	}



	/**
	 * Sets the increment.
	 * 
	 * @param increment the new increment
	 */
	public void setIncrement(double increment) {
		this.increment = increment;
	}



	/**
	 * Gets the minor tick count.
	 * 
	 * @return the minor tick count
	 */
	public int getMinorTickCount() {
		return minorTickCount;
	}



	/**
	 * Sets the minor tick count.
	 * 
	 * @param minorTickCount the new minor tick count
	 */
	public void setMinorTickCount(int minorTickCount) {
		this.minorTickCount = minorTickCount;
	}


}
