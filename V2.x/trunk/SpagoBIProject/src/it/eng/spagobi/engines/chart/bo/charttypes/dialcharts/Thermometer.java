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

/** Configure and draw a thermomether chart
 *  * @author Giulio Gavardi
 * 
 */

package it.eng.spagobi.engines.chart.bo.charttypes.dialcharts;


import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spagobi.engines.chart.bo.charttypes.utils.KpiInterval;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.ThermometerPlot;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.ValueDataset;
import org.jfree.ui.RectangleInsets;



public class Thermometer extends DialCharts{


	private static transient Logger logger=Logger.getLogger(Thermometer.class);

	private	Vector intervals=null;
	private String units="";



	public Thermometer() {
		super();
		intervals=new Vector();
	}


	/**
	 * Creates a chart of type thermometer.
	 * 
	 * @param chartTitle  the chart title.
	 * @param dataset  the dataset.

	 * @return A chart thermometer.
	 */


	public JFreeChart createChart(String chartTitle, Dataset dataset) {
		logger.debug("IN");
		ThermometerPlot plot = new ThermometerPlot((ValueDataset)dataset);
		JFreeChart chart = new JFreeChart(chartTitle, JFreeChart.DEFAULT_TITLE_FONT,	plot, true);               
		chart.setBackgroundPaint(color);
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
	 * set parameters for the creation of the chart getting them from template or from LOV
	 * 
	 * @param content the content of the template.

	 * @return A chart that displays a value as a dial.
	 */


	public void configureChart(SourceBean content) {
		logger.debug("IN");
		super.configureChart(content);

		if(!isLovConfDefined){

			if(confParameters.get("unit")!=null){	
				String unit=(String)confParameters.get("unit");
				setUnits(unit);
			}
			else
				setUnits("");


			//reading intervals information
			SourceBean subrangesSB = (SourceBean)content.getAttribute("CONF.SUBRANGES");
			List subrangesAttrsList=null;
			if(subrangesSB!=null){
				subrangesAttrsList = subrangesSB.getContainedSourceBeanAttributes();
			}

			if(subrangesAttrsList==null || subrangesAttrsList.isEmpty()){ // if subranges are not defined 
				logger.error("subranges not correctly defined");			}
			else{	

				Iterator subrangesAttrsIter = subrangesAttrsList.iterator();
				while(subrangesAttrsIter.hasNext()) {
					SourceBeanAttribute paramSBA = (SourceBeanAttribute)subrangesAttrsIter.next();
					SourceBean param = (SourceBean)paramSBA.getValue();
					String range= (String)param.getAttribute("label");
					String min= (String)param.getAttribute("min");
					String max= (String)param.getAttribute("max");
					String col= (String)param.getAttribute("color");

					KpiInterval subrange=new KpiInterval();

					subrange.setLabel(range);
					subrange.setMin(Double.valueOf(min).doubleValue());
					subrange.setMax(Double.valueOf(max).doubleValue());

					Color color=new Color(Integer.decode(col).intValue());
					if(color!=null){
						subrange.setColor(color);}
					else{
						subrange.setColor(Color.RED);
					}
					addIntervals(subrange);
				}
			}
		}
		else{

			String unit=(String)sbRow.getAttribute("unit");
			if(unit!=null)
				setUnits(unit);
			else
				setUnits("");

			String subranges=(String)sbRow.getAttribute("subranges");
			if(subranges.equalsIgnoreCase("NO")){ // if intervals are not specified
				logger.warn("no subranges defined");
			}
			else{
				for(int i=1;i<=3;i++){
					KpiInterval subrange=new KpiInterval();
					String label=(String)sbRow.getAttribute("label"+(new Integer(i)).toString());
					String min=(String)sbRow.getAttribute("min"+(new Integer(i)).toString());
					String max=(String)sbRow.getAttribute("max"+(new Integer(i)).toString());
					String col=(String)sbRow.getAttribute("color"+(new Integer(i)).toString());

					subrange.setLabel(label);
					subrange.setMin(Double.valueOf(min).doubleValue());
					subrange.setMax(Double.valueOf(max).doubleValue());
					Color color=new Color(Integer.decode(col).intValue());
					subrange.setColor(color);
					addIntervals(subrange);

				}
			}
		}
		logger.debug("OUT");
	}




	public Vector getIntervals() {
		return intervals;
	}




	public void addIntervals(KpiInterval subrange) {
		this.intervals.add(subrange);
	}




	public String getUnits() {
		return units;
	}




	public void setUnits(String units) {
		this.units = units;
	}


}
