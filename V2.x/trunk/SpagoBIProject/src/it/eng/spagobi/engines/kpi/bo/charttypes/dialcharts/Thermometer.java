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

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.ThermometerPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.ValueDataset;
import org.jfree.ui.RectangleInsets;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spagobi.engines.chart.bo.charttypes.utils.KpiInterval;
import it.eng.spagobi.engines.chart.utils.DatasetMap;
import it.eng.spagobi.engines.kpi.bo.ChartImpl;

/**
 * 
 * @author Chiara Chiarelli
 * 
 */

public class Thermometer extends ChartImpl {
	
	private static transient Logger logger=Logger.getLogger(Thermometer.class);

	private String units="";



	/**
	 * Instantiates a new thermometer.
	 */
	public Thermometer() {
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
	 * Creates a chart of type thermometer.
	 * 
	 * @param chartTitle  the chart title.
	 * @param dataset  the dataset.
	 * 
	 * @return A chart thermometer.
	 */
	public JFreeChart createChart() {
		logger.debug("IN");

		ThermometerPlot plot = new ThermometerPlot((ValueDataset)dataset);
		JFreeChart chart = new JFreeChart(name, JFreeChart.DEFAULT_TITLE_FONT,	plot, true);               
		chart.setBackgroundPaint(color);
	
		TextTitle title = setStyleTitle(name, styleTitle);
		chart.setTitle(title);
		if(subName!= null && !subName.equals("")){
			TextTitle subTitle =setStyleTitle(subName, styleSubTitle);
			chart.addSubtitle(subTitle);
		}

		
		plot.setInsets(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		plot.setPadding(new RectangleInsets(10.0, 10.0, 10.0, 10.0));
		plot.setThermometerStroke(new BasicStroke(2.0f));
		plot.setThermometerPaint(Color.lightGray);
		plot.setGap(3);
		plot.setValueLocation(3);

		plot.setRange(lower, upper);


		if(units.equalsIgnoreCase("FAHRENHEIT"))plot.setUnits(ThermometerPlot.UNITS_FAHRENHEIT);	
		else if(units.equalsIgnoreCase("CELCIUS")) plot.setUnits(ThermometerPlot.UNITS_CELCIUS);	
		else if(units.equalsIgnoreCase("KELVIN")) plot.setUnits(ThermometerPlot.UNITS_KELVIN);	
		else plot.setUnits(ThermometerPlot.UNITS_NONE);


		// set subranges	
		for (Iterator iterator = intervals.iterator(); iterator.hasNext();){
			KpiInterval subrange = (KpiInterval) iterator.next();
			int range=0;
			if(subrange.getLabel().equalsIgnoreCase("NORMAL"))range=(ThermometerPlot.NORMAL);
			else if(subrange.getLabel().equalsIgnoreCase("WARNING"))range=(ThermometerPlot.WARNING);
			else if(subrange.getLabel().equalsIgnoreCase("CRITICAL"))range=(ThermometerPlot.CRITICAL);

			plot.setSubrange(range, subrange.getMin(), subrange.getMax());
			if(subrange.getColor()!=null){
				plot.setSubrangePaint(range, subrange.getColor());
			}
			//plot.setDisplayRange(subrange.getRange(), subrange.getLower(), subrange.getUpper());	
		}
		//plot.setFollowDataInSubranges(true);
		logger.debug("OUT");

		return chart;       
	}


	/**
	 * Gets the units.
	 * 
	 * @return the units
	 */
	public String getUnits() {
		return units;
	}




	/**
	 * Sets the units.
	 * 
	 * @param units the new units
	 */
	public void setUnits(String units) {
		this.units = units;
	}


}
