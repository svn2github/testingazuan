package it.eng.spagobi.engines.chart.bo.charttypes.scattercharts;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.engines.chart.utils.DatasetMap;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.ui.Layer;
import org.jfree.ui.LengthAdjustmentType;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;


public class MarkerScatter extends ScatterCharts {

	String xRangeLow=null;
	String xRangeHigh=null;
	String yRangeLow=null;
	String yRangeHigh=null;
	String xMarkerStartInt=null;
	String xMarkerEndInt=null;
	String yMarkerStartInt=null;
	String yMarkerEndInt=null;
	String xMarkerValue=null;
	String yMarkerValue=null;
	String xMarkerColor=null;
	String yMarkerColor=null;
	String xMarkerIntColor=null;
	String yMarkerIntColor=null;
	String xMarkerLabel=null;
	String yMarkerLabel=null;
	
	
	
	private static transient Logger logger=Logger.getLogger(MarkerScatter.class);
	
	public void configureChart(SourceBean content) {
		logger.debug("IN");
		super.configureChart(content);
		SourceBean param = (SourceBean)content.getAttribute("CONF");
		
		List parameters = param.getAttributeAsList("PARAMETER");
		if(parameters!=null){
 
			for (Iterator iterator = parameters.iterator(); iterator.hasNext();) {
				SourceBean att = (SourceBean) iterator.next();
				String name=(att.getAttribute("name")==null)?"":(String)att.getAttribute("name");
				if (name.equalsIgnoreCase("x_range")){
					xRangeLow = (att.getAttribute("value_low")==null)?"0":(String)att.getAttribute("value_low");
					xRangeHigh =(att.getAttribute("value_high")==null)?"0":(String)att.getAttribute("value_high");
				}
				if (name.equalsIgnoreCase("y_range")){
					yRangeLow = (att.getAttribute("value_low")==null)?"0":(String)att.getAttribute("value_low");
					yRangeHigh =(att.getAttribute("value_high")==null)?"0":(String)att.getAttribute("value_high");
				}
				if (name.equalsIgnoreCase("x_marker")){
					xMarkerStartInt = (att.getAttribute("value_start_int")==null)?"0":(String)att.getAttribute("value_start_int");
					xMarkerEndInt = (att.getAttribute("value_end_int")==null)?"0":(String)att.getAttribute("value_end_int");
					xMarkerValue = (att.getAttribute("value_marker")==null)?"0":(String)att.getAttribute("value_marker");
					xMarkerColor = (att.getAttribute("color")==null)?"0":(String)att.getAttribute("color");
					xMarkerIntColor = (att.getAttribute("color_int")==null)?"0":(String)att.getAttribute("color_int");
					xMarkerLabel = (att.getAttribute("label")==null)?"":(String)att.getAttribute("label");
				}
				if (name.equalsIgnoreCase("y_marker")){
					yMarkerStartInt = (att.getAttribute("value_start_int")==null)?"0":(String)att.getAttribute("value_start_int");
					yMarkerEndInt = (att.getAttribute("value_end_int")==null)?"0":(String)att.getAttribute("value_end_int");
					yMarkerValue = (att.getAttribute("value_marker")==null)?"0":(String)att.getAttribute("value_marker");
					yMarkerColor = (att.getAttribute("color")==null)?"0":(String)att.getAttribute("color");
					yMarkerIntColor = (att.getAttribute("color_int")==null)?"0":(String)att.getAttribute("color_int");
					yMarkerLabel = (att.getAttribute("label")==null)?"":(String)att.getAttribute("label");
				}
			}
		}
		
		logger.debug("OUT");	
	}

	public JFreeChart createChart(DatasetMap datasets) {

		DefaultXYDataset dataset=(DefaultXYDataset)datasets.getDatasets().get("1");

		JFreeChart chart = ChartFactory.createScatterPlot(
				name, yLabel, xLabel, dataset, 
				PlotOrientation.HORIZONTAL, true, true, false);

		TextTitle title =setStyleTitle(name, styleTitle);
		chart.setTitle(title);
		chart.setBackgroundPaint(Color.white);
		if(subName!= null && !subName.equals("")){
			TextTitle subTitle =setStyleTitle(subName, styleSubTitle);
			chart.addSubtitle(subTitle);
		}
		
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setForegroundAlpha(0.65f);
		

		XYItemRenderer renderer = plot.getRenderer();


		int seriesN=dataset.getSeriesCount();
		if(colorMap!=null){
			for (int i = 0; i < seriesN; i++) {
				String serieName=(String)dataset.getSeriesKey(i);
				Color color=(Color)colorMap.get(serieName);
				if(color!=null){
					renderer.setSeriesPaint(i, color);
				}	
			}
		}
		
	   
	   // add un interval  marker for the Y axis...
		if (yMarkerStartInt != null && yMarkerEndInt != null && !yMarkerStartInt.equals("") && !yMarkerEndInt.equals("")){
		    Marker intMarkerY = new IntervalMarker(Double.parseDouble(yMarkerStartInt), Double.parseDouble(yMarkerEndInt));
		    intMarkerY.setLabelOffsetType(LengthAdjustmentType.EXPAND);
		    intMarkerY.setPaint(new Color(Integer.decode(yMarkerIntColor).intValue()));
		    //intMarkerY.setLabel(yMarkerLabel);
		    intMarkerY.setLabelAnchor(RectangleAnchor.BOTTOM_RIGHT);
		    intMarkerY.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
	        plot.addDomainMarker (intMarkerY, Layer.BACKGROUND);
		}
		 // add un interval  marker for the X axis...
        if (xMarkerStartInt != null && xMarkerEndInt != null && !xMarkerStartInt.equals("") && !xMarkerEndInt.equals("")){
	        Marker intMarkerX = new IntervalMarker(Double.parseDouble(xMarkerStartInt), Double.parseDouble(xMarkerEndInt));
	        intMarkerX.setLabelOffsetType(LengthAdjustmentType.EXPAND);
		    intMarkerX.setPaint(new Color(Integer.decode(xMarkerIntColor).intValue()));
		    //intMarkerX.setLabel(xMarkerLabel);
		    intMarkerX.setLabelAnchor(RectangleAnchor.BOTTOM_RIGHT);
		    intMarkerX.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
	        plot.addRangeMarker(intMarkerX, Layer.BACKGROUND);
        }
     // add a labelled marker for the Y axis...
        if (yMarkerValue != null && !yMarkerValue.equals("")){        	
	        Marker markerY = new ValueMarker(Double.parseDouble(yMarkerValue));
	        markerY.setLabelOffsetType(LengthAdjustmentType.EXPAND);
	        if (!yMarkerColor.equals(""))  markerY.setPaint(new Color(Integer.decode(yMarkerColor).intValue()));
	        markerY.setLabel(yMarkerLabel);
	        markerY.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
	        markerY.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
	        plot.addDomainMarker(markerY, Layer.BACKGROUND);
        }
     // add a labelled marker for the X axis...
        if (xMarkerValue != null && !xMarkerValue.equals("")){
	        Marker markerX = new ValueMarker(Double.parseDouble(xMarkerValue));
	        markerX.setLabelOffsetType(LengthAdjustmentType.EXPAND);
	        if (!xMarkerColor.equals("")) markerX.setPaint(new Color(Integer.decode(xMarkerColor).intValue()));
	        markerX.setLabel(xMarkerLabel);
	        markerX.setLabelAnchor(RectangleAnchor.BOTTOM_RIGHT);
	        markerX.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
	        plot.addRangeMarker(markerX, Layer.BACKGROUND);
        }

        if (xRangeLow != null && !xRangeLow.equals("") && xRangeHigh != null && !xRangeHigh.equals("")){
	        ValueAxis rangeAxis = plot.getRangeAxis();
		    //rangeAxis.setRange(Double.parseDouble(xRangeLow), Double.parseDouble(xRangeHigh));
		    rangeAxis.setRangeWithMargins(Double.parseDouble(xRangeLow), Double.parseDouble(xRangeHigh));
        }
        else{
        	NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
    		rangeAxis.setAutoRange(true);
    		rangeAxis.setRange(xMin,xMax);
        }
        if (yRangeLow != null && !yRangeLow.equals("") && yRangeHigh != null && !yRangeHigh.equals("")){
		    ValueAxis domainAxis = plot.getDomainAxis();
		    //domainAxis.setRange(Double.parseDouble(yRangeLow), Double.parseDouble(yRangeHigh));
		    domainAxis.setRangeWithMargins(Double.parseDouble(yRangeLow), Double.parseDouble(yRangeHigh));
        }
        else{
        	NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
    		domainAxis.setAutoRange(true);
    		domainAxis.setRange(yMin, yMax);
        }
		return chart;
	}


}
