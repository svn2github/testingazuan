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
import java.awt.Font;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DialShape;
import org.jfree.chart.plot.MeterInterval;
import org.jfree.chart.plot.MeterPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.Range;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.ValueDataset;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spagobi.engines.kpi.bo.charttypes.dialcharts.Meter;
import it.eng.spagobi.engines.chart.bo.charttypes.utils.KpiInterval;
import it.eng.spagobi.engines.chart.utils.DatasetMap;
import it.eng.spagobi.engines.kpi.bo.ChartImpl;
import it.eng.spagobi.kpi.threshold.bo.Threshold;

/**
 * 
 * @author Chiara Chiarelli
 * 
 */

public class Meter extends ChartImpl {
	
	private static transient Logger logger=Logger.getLogger(Meter.class);
	


	/**
	 * Instantiates a new meter.
	 */
	public Meter() {
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
	 * Creates the chart .
	 * 
	 * @param chartTitle  the chart title.
	 * @param dataset  the dataset.
	 * 
	 * @return A chart .
	 */

	public JFreeChart createChart() {

		if (dataset==null){
			return null;
		}
		MeterPlot plot = new MeterPlot((ValueDataset)dataset);
		plot.setRange(new Range(lower, upper));


		for (Iterator iterator = intervals.iterator(); iterator.hasNext();) {
			KpiInterval interval = (KpiInterval) iterator.next();

			plot.addInterval(new MeterInterval(interval.getLabel(), new Range(interval.getMin(), interval.getMax()), 
					Color.lightGray, new BasicStroke(2.0f), 
					interval.getColor()));
		}

		plot.setNeedlePaint(Color.darkGray);
		plot.setDialBackgroundPaint(Color.white);
		plot.setDialOutlinePaint(Color.gray);
		plot.setDialShape(DialShape.CHORD);
		plot.setMeterAngle(260);
		plot.setTickLabelsVisible(true);
		plot.setTickLabelFont(new Font("Dialog", Font.BOLD, 10));
		plot.setTickLabelPaint(Color.darkGray);
		plot.setTickSize(5.0);
		plot.setTickPaint(Color.lightGray);
		

		plot.setValuePaint(Color.black);
		plot.setValueFont(new Font("Dialog", Font.BOLD, 14));

		JFreeChart chart = new JFreeChart(name, 
				JFreeChart.DEFAULT_TITLE_FONT, plot, legend);
		chart.setBackgroundPaint(color);
		
		TextTitle title = setStyleTitle(name, styleTitle);
		chart.setTitle(title);
		if(subName!= null && !subName.equals("")){
			TextTitle subTitle =setStyleTitle(subName, styleSubTitle);
			chart.addSubtitle(subTitle);
		}

		return chart;
	}


}
