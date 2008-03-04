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

/** Configure and draw a dialChart
 *  * @author Giulio Gavardi
 * 
 */

package it.eng.spagobi.engines.chart.bo.charttypes.dialcharts;


import it.eng.spago.base.SourceBean;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Point;

import org.apache.log4j.Logger;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.dial.ArcDialFrame;
import org.jfree.chart.plot.dial.DialBackground;
import org.jfree.chart.plot.dial.DialPlot;
import org.jfree.chart.plot.dial.DialPointer;
import org.jfree.chart.plot.dial.StandardDialScale;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.ValueDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;

public class SimpleDial extends DialCharts{

	private static transient Logger logger=Logger.getLogger(SimpleDial.class);

	private String orientation="horizontal";
	double increment=0.0;
	int minorTickCount=0;


	/**
	 * set parameters for the creation of the chart getting them from template or from LOV
	 * 
	 * @param content the content of the template.

	 * @return A chart that displays a value as a dial.
	 */


	public void configureChart(SourceBean content) {
		logger.debug("IN");
		super.configureChart(content);

		if(!isLovConfDefined){
			if(confParameters.get("increment")!=null){	
				String increment=(String)confParameters.get("increment");
				setIncrement(Double.valueOf(increment).doubleValue());
			}
			else {
				logger.error("increment not defined");
			}
			if(confParameters.get("minortickcount")!=null){	
				String minorTickCount=(String)confParameters.get("minortickcount");
				setMinorTickCount(Integer.valueOf(minorTickCount).intValue());
			}
			else {
				setMinorTickCount(10);
			}
			if(confParameters.get("orientation")!=null){	
				String orientation=(String)confParameters.get("orientation");
				setOrientation(orientation);
			}
			else {
				setOrientation("horizontal");
			}

		} // LOV is defined
		else{
			String increment=(String)sbRow.getAttribute("increment");
			String minorTickCount=(String)sbRow.getAttribute("minorTickCount");
			String orientation=(String)sbRow.getAttribute("orientation");
			setIncrement(Double.valueOf(increment).doubleValue());
			setMinorTickCount(Integer.valueOf(minorTickCount).intValue());			
			if(orientation!=null)
				setOrientation(orientation);
			else
				setOrientation("horizontal");

		}

		logger.debug("out");
	}

	/**
	 * Creates the chart .
	 * 
	 * @param chartTitle  the chart title.
	 * @param dataset  the dataset.

	 * @return A chart .
	 */

	public JFreeChart createChart(String chartTitle, Dataset dataset) {
		// get data for diagrams
		logger.debug("IN");
		DialPlot plot = new DialPlot();
		plot.setDataset((ValueDataset)dataset);

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
		logger.debug("OUT");
		return chart1;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
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
