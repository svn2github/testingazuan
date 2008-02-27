/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.engines.chart.charttypes;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spagobi.engines.chart.charttypes.utils.KpiInterval;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Point;
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
import org.jfree.data.general.ValueDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;


/**
 * A class to generate Speedometer charts
 * @author Giulio Gavardi
 * 
 */
public class SBISpeedometer extends KpiChart{


	private static transient Logger logger=Logger.getLogger(SBISpeedometer.class);

	Vector intervals;
	double increment=0.0;
	int minorTickCount=0;


	public SBISpeedometer() {
		super();
		intervals=new Vector();	
	}

	/**
	 * set parameters for the creation of the chart getting them from template or from LOV
	 * 
	 * @param content the content of the template.

	 * @return A chart that displays a value as a dial.
	 */


	public void configureKpiChart(SourceBean content) {

		super.configureKpiChart(content);

		logger.debug("IN");

		if(!isLovConfDefined){
			logger.debug("Configuration set in template");
			if(confParameters.get("increment")!=null){	
				String increment=(String)confParameters.get("increment");
				setIncrement(Double.valueOf(increment).doubleValue());
			}
			else {
				logger.error("increment not defined");
				return;
			}
			if(confParameters.get("minortickcount")!=null){	
				String minorTickCount=(String)confParameters.get("minortickcount");
				setMinorTickCount(Integer.valueOf(minorTickCount).intValue());
			}
			else {
				setMinorTickCount(10);
			}


			//reading intervals information
			SourceBean intervalsSB = (SourceBean)content.getAttribute("CONF.INTERVALS");
			List intervalsAttrsList=null;
			if(intervalsSB!=null){
				intervalsAttrsList = intervalsSB.getContainedSourceBeanAttributes();
			}

			if(intervalsAttrsList==null || intervalsAttrsList.isEmpty()){ // if intervals are not defined realize a single interval
				logger.warn("intervals not defined; default settings");
				KpiInterval interval=new KpiInterval();
				interval.setMin(getLower());
				interval.setMax(getUpper());
				interval.setColor(Color.WHITE);
				addInterval(interval);
			}
			else{	

				Iterator intervalsAttrsIter = intervalsAttrsList.iterator();
				while(intervalsAttrsIter.hasNext()) {
					SourceBeanAttribute paramSBA = (SourceBeanAttribute)intervalsAttrsIter.next();
					SourceBean param = (SourceBean)paramSBA.getValue();
					String min= (String)param.getAttribute("min");
					String max= (String)param.getAttribute("max");
					String col= (String)param.getAttribute("color");

					KpiInterval interval=new KpiInterval();
					interval.setMin(Double.valueOf(min).doubleValue());
					interval.setMax(Double.valueOf(max).doubleValue());

					Color color=new Color(Integer.decode(col).intValue());
					if(color!=null){
						interval.setColor(color);}
					else{
						interval.setColor(Color.RED);
					}
					addInterval(interval);
				}
			}
		}
		else{
			logger.debug("configuration defined in LOV"+confName);
			String increment=(String)sbRow.getAttribute("increment");
			String minorTickCount=(String)sbRow.getAttribute("minorTickCount");
			setIncrement(Double.valueOf(increment).doubleValue());
			setMinorTickCount(Integer.valueOf(minorTickCount).intValue());

			String intervalsNumber=(String)sbRow.getAttribute("intervalsnumber");
			if(intervalsNumber==null || intervalsNumber.equals("") || intervalsNumber.equals("0")){ // if intervals are not specified
				KpiInterval interval=new KpiInterval();
				interval.setMin(getLower());
				interval.setMax(getUpper());
				interval.setColor(Color.WHITE);
				addInterval(interval);
			}
			else{
				for(int i=1;i<=Integer.valueOf(intervalsNumber).intValue();i++){
					KpiInterval interval=new KpiInterval();
					String min=(String)sbRow.getAttribute("lower"+(new Integer(i)).toString());
					String max=(String)sbRow.getAttribute("upper"+(new Integer(i)).toString());
					String col=(String)sbRow.getAttribute("color"+(new Integer(i)).toString());
					interval.setMin(Double.valueOf(min).doubleValue());
					interval.setMax(Double.valueOf(max).doubleValue());
					Color color=new Color(Integer.decode(col).intValue());
					interval.setColor(color);
					addInterval(interval);

				}
			}
		}
		logger.debug("out");
	}





	/**
	 * Creates a chart of type speedometer.
	 * 
	 * @param chartTitle  the chart title.
	 * @param dataset  the dataset.

	 * @return A chart speedometer.
	 */

	public JFreeChart createDialChart(String chartTitle, ValueDataset dataset) {
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



	public Vector getIntervals() {
		return intervals;
	}





	public void addInterval(KpiInterval interval) {
		this.intervals.add(interval);
	}


	public double getIncrement() {
		return increment;
	}



	public void setIncrement(double increment) {
		this.increment = increment;
	}



	public int getMinorTickCount() {
		return minorTickCount;
	}



	public void setMinorTickCount(int minorTickCount) {
		this.minorTickCount = minorTickCount;
	}








}
