package it.eng.spagobi.engines.chart.charttypes;

import it.eng.spagobi.engines.chart.charttypes.utils.KpiInterval;
import it.eng.spagobi.engines.chart.charttypes.utils.ThermometerSubrange;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.ThermometerPlot;
import org.jfree.data.general.ValueDataset;
import org.jfree.ui.RectangleInsets;


public class Thermometer extends KpiChart{




	private	Vector subranges=null;
	private String units="";




	public Thermometer() {
		super();
		subranges=new Vector();
	}




	public JFreeChart createDialChart(String chartTitle, ValueDataset dataset) {
		ThermometerPlot plot = new ThermometerPlot(dataset);
		JFreeChart chart = new JFreeChart("Thermometer Demo 1", JFreeChart.DEFAULT_TITLE_FONT,	plot, true);               

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


}
