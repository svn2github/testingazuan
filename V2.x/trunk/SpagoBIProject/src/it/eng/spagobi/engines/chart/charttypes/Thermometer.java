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

package it.eng.spagobi.engines.chart.charttypes;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spagobi.engines.chart.charttypes.utils.ThermometerSubrange;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.ThermometerPlot;
import org.jfree.data.general.ValueDataset;
import org.jfree.ui.RectangleInsets;



public class Thermometer extends KpiChart{


	private static transient Logger logger=Logger.getLogger(Thermometer.class);

	private	Vector subranges=null;
	private String units="";



	public Thermometer() {
		super();
		subranges=new Vector();
	}




	public JFreeChart createDialChart(String chartTitle, ValueDataset dataset) {
		ThermometerPlot plot = new ThermometerPlot(dataset);
		JFreeChart chart = new JFreeChart(chartTitle, JFreeChart.DEFAULT_TITLE_FONT,	plot, true);               

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
		for (Iterator iterator = subranges.iterator(); iterator.hasNext();){
			ThermometerSubrange subrange = (ThermometerSubrange) iterator.next();
			plot.setSubrange(subrange.getRange(), subrange.getLower(), subrange.getUpper());
			if(subrange.getColor()!=null){
				plot.setSubrangePaint(subrange.getRange(), subrange.getColor());
			}
			//plot.setDisplayRange(subrange.getRange(), subrange.getLower(), subrange.getUpper());	
		}
		//plot.setFollowDataInSubranges(true);


		return chart;       
	}




	public void configureKpiChart(SourceBean content) {
		super.configureKpiChart(content);

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
					String range= (String)param.getAttribute("range");
					String min= (String)param.getAttribute("min");
					String max= (String)param.getAttribute("max");
					String col= (String)param.getAttribute("color");

					ThermometerSubrange subrange=new ThermometerSubrange();

					int r=-1;
					if(range.equalsIgnoreCase("NORMAL"))r=ThermometerPlot.NORMAL;
					else if(range.equalsIgnoreCase("WARNING"))r=ThermometerPlot.WARNING;
					else if(range.equalsIgnoreCase("CRITICAL"))r=ThermometerPlot.CRITICAL;

					subrange.setRange(r);
					subrange.setLower(Double.valueOf(min).doubleValue());
					subrange.setUpper(Double.valueOf(max).doubleValue());

					Color color=new Color(Integer.decode(col).intValue());
					if(color!=null){
						subrange.setColor(color);}
					else{
						subrange.setColor(Color.RED);
					}
					addSubranges(subrange);
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
					ThermometerSubrange subrange=new ThermometerSubrange();
					String range=(String)sbRow.getAttribute("range"+(new Integer(i)).toString());
					String min=(String)sbRow.getAttribute("lower"+(new Integer(i)).toString());
					String max=(String)sbRow.getAttribute("upper"+(new Integer(i)).toString());
					String col=(String)sbRow.getAttribute("color"+(new Integer(i)).toString());

					if(range.equalsIgnoreCase("NORMAL"))subrange.setRange(ThermometerPlot.NORMAL);
					else if(range.equalsIgnoreCase("WARNING"))subrange.setRange(ThermometerPlot.WARNING);
					else if(range.equalsIgnoreCase("CRITICAL"))subrange.setRange(ThermometerPlot.CRITICAL);

					subrange.setLower(Double.valueOf(min).doubleValue());
					subrange.setUpper(Double.valueOf(max).doubleValue());
					Color color=new Color(Integer.decode(col).intValue());
					subrange.setColor(color);
					addSubranges(subrange);

				}
			}
		}

	}




	public Vector getSubranges() {
		return subranges;
	}




	public void addSubranges(ThermometerSubrange subrange) {
		this.subranges.add(subrange);
	}




	public String getUnits() {
		return units;
	}




	public void setUnits(String units) {
		this.units = units;
	}


}
