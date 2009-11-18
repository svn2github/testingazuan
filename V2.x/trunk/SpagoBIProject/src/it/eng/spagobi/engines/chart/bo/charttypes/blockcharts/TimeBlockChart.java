package it.eng.spagobi.engines.chart.bo.charttypes.blockcharts;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.engines.chart.bo.charttypes.blockcharts.util.Activity;
import it.eng.spagobi.engines.chart.bo.charttypes.blockcharts.util.AnnotationBlock;
import it.eng.spagobi.engines.chart.bo.charttypes.blockcharts.util.RangeBlocks;
import it.eng.spagobi.engines.chart.bo.charttypes.utils.MyXYItemLabelGenerator;
import it.eng.spagobi.engines.chart.utils.DataSetAccessFunctions;
import it.eng.spagobi.engines.chart.utils.DatasetMap;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.LookupPaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.data.time.Day;
import org.jfree.data.time.Minute;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;

public class TimeBlockChart extends BlockCharts {

	private static transient Logger logger=Logger.getLogger(TimeBlockChart.class);
	Date beginDate;
	Date endDate;

	HashMap<String , AnnotationBlock> annotations;

//	Utility Map
	HashMap<String, Integer> patternRangeIndex;

	static final long ONE_HOUR = 60 * 60 * 1000L;
	public static long daysBetween(Date d1, Date d2){
		return ( (d2.getTime() - d1.getTime() + ONE_HOUR) / 
				(ONE_HOUR * 24));
	}   
	
	static final long ONE_DAY = 24 * 60 * 60 * 1000;
	static final long ANNOTATION_HEIGHT = 24 * 60 * 40 * 1000;
	static final long BLOCK_HEIGHT = 24 * 60 * 35 * 1000;


	@Override
	public DatasetMap calculateValue() throws Exception {
		logger.debug("IN");
		super.calculateValue();
		DatasetMap datasetMap=new DatasetMap();
		String res=DataSetAccessFunctions.getDataSetResultFromId(profile, getData(),parametersObject);

		Calendar c=new GregorianCalendar();
		c.set(9 + 2000, Calendar.JANUARY, 1);
		beginDate=c.getTime();
		Calendar c1=new GregorianCalendar();
		c1.set(9 + 2000, Calendar.JANUARY, 10);
		endDate=c1.getTime();

		long daysBetween=daysBetween(beginDate,endDate); 
		long minutesBetweenLong=daysBetween*24*60;
		int mbl=Long.valueOf(minutesBetweenLong).intValue();
		//		count days

		ArrayList<Activity> activities=new ArrayList<Activity>();

		RegularTimePeriod t = new Day(1,1,2009);
		DefaultXYZDataset dataset = new DefaultXYZDataset();
		double[] xvalues = new double[mbl];    
		double[] yvalues = new double[mbl];    
		double[] zvalues = new double[mbl];

		RegularTimePeriod timePeriod = null;

		SourceBean sbRows=SourceBean.fromXMLString(res);
		List listAtts=sbRows.getAttributeAsList("ROW");
		// Run all rows

		int j=0;
		// for each activity
		for (Iterator iterator = listAtts.iterator(); iterator.hasNext();) {
			SourceBean row = (SourceBean) iterator.next();
			Activity activity=new Activity(row);
			activities.add(activity);
			// if a new Code create a new Annotation

			//			if(timePeriod==null){
//			timePeriod = new Day(activity.getBeginDate().getDay(),activity.getBeginDate().getMonth(),activity.getBeginDate().getYear());
//			}
		}

		annotations=new HashMap<String, AnnotationBlock>();
		// run all the activities
		for (Iterator iterator = activities.iterator(); iterator.hasNext();) {
			Activity activity = (Activity) iterator.next();

			RegularTimePeriod rtp = new Day(activity.getBeginDate());
			long secondmills= rtp.getFirstMillisecond();

			Minute minute=activity.getMinutes();
			for(int i=0;i<activity.getDuration();i++){
				// convert from hour to number axis (da sessantesimi a centesimi!)
				Integer hour=Integer.valueOf(minute.getHourValue());
				Integer minuteValue=Integer.valueOf(minute.getMinute());
				Double doubleMinuteValue=Double.valueOf(((double)minuteValue.intValue()));
				// minuteValue : 60 = x :100
				double convertedMinuteValue=(doubleMinuteValue*100)/60.0;
				double convertedMinuteValueCent=convertedMinuteValue/100;
				
				double hourD=(double)hour.intValue();
				double converted=hourD+convertedMinuteValueCent;
				
				String yVal=Double.valueOf(converted).toString();
				xvalues[j]=secondmills;
				yvalues[j]=Double.valueOf(yVal);
				zvalues[j]=patternRangeIndex.get(activity.getPattern())+0.5;
				//System.out.println("Date: "+activity.getBeginDate()+":"+Double.valueOf(xvalues[j]).toString()+", Hour: "+Double.valueOf(yvalues[j]).toString()+", Value: "+Double.valueOf(zvalues[j]).toString());
				if(annotations.get(activity.getCode())==null){
					AnnotationBlock annotation=new AnnotationBlock(activity.getCode());
					annotation.setXPosition(xvalues[j]);
					annotation.setYPosition(yvalues[j]);
					annotations.put(annotation.getAnnotation(), annotation);
				}
				minute=(Minute)minute.next();
				j++;
			}


		}

		dataset.addSeries("Series 1", 
				new double[][] { xvalues, yvalues, zvalues });

		datasetMap.getDatasets().put("1", dataset);
		return datasetMap;
	}

	@Override
	public void configureChart(SourceBean content) {
		logger.debug("IN");
		super.configureChart(content);

		patternRangeIndex=new HashMap<String, Integer>();
		for(int i=0; i<ranges.size(); i++){
			RangeBlocks rangeBlocks = ranges.get(i);
			patternRangeIndex.put(rangeBlocks.getPattern(), Integer.valueOf(i));
		}

	}





	@Override
	public JFreeChart createChart(DatasetMap datasets) {
		super.createChart(datasets);
		DefaultXYZDataset dataset=(DefaultXYZDataset)datasets.getDatasets().get("1"); 

		DateAxis xAxis = new DateAxis(yLabel);
		xAxis.setLowerMargin(0.0);
		xAxis.setUpperMargin(0.0);
		xAxis.setInverted(false);
		xAxis.setDateFormatOverride(new SimpleDateFormat("dd/MM/yyyy"));
		if(yAutoRange){
			xAxis.setAutoRange(true);
		}
		else{
			DateFormat formatter=new SimpleDateFormat("dd/MM/yyyy");
			DateTickUnit unit = new DateTickUnit(DateTickUnit.DAY, 1, formatter);
			xAxis.setTickUnit(unit);
		}

		if(beginDate!=null && endDate!=null){
			xAxis.setRange(beginDate, endDate);
		}

//		Calendar c=new GregorianCalendar();
//		c.set(9 + 2000, Calendar.JANUARY, 1);
//		java.util.Date minima=c.getTime();
//		Calendar c1=new GregorianCalendar();
//		c1.set(9 + 2000, Calendar.FEBRUARY, 1);
//		java.util.Date massima=c1.getTime();

		NumberAxis yAxis = new NumberAxis(xLabel);
		yAxis.setUpperMargin(0.0);
		yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		yAxis.setRange(xMin, xMax);
		XYBlockRenderer renderer = new XYBlockRenderer();
		renderer.setBlockWidth(BLOCK_HEIGHT);
		// one block for each minute!
		renderer.setBlockHeight(0.017);
		//renderer.setBlockWidth(1);
		renderer.setBlockAnchor(RectangleAnchor.BOTTOM_LEFT);

//		MyXYItemLabelGenerator my=new MyXYItemLabelGenerator();
//		renderer.setItemLabelsVisible(null);
//		renderer.setSeriesItemLabelGenerator(0, my);
//		renderer.setSeriesItemLabelsVisible(0, true);

//		XYTextAnnotation annotation1 = new XYTextAnnotation(
//		"P_",1.2309372E12, 14.3);
//		XYTextAnnotation annotation2 = new XYTextAnnotation(
//		"P_",1.2308508E12, 16.3);

		for (Iterator iterator = annotations.keySet().iterator(); iterator.hasNext();) {
			String annotationCode = (String) iterator.next();
			AnnotationBlock annotationBlock=annotations.get(annotationCode);
			XYTextAnnotation xyAnnotation = new XYTextAnnotation(
					annotationBlock.getAnnotation(),annotationBlock.getXPosition()+ANNOTATION_HEIGHT, annotationBlock.getYPosition());
			xyAnnotation.setFont(new Font("nome",Font.BOLD,8));
			xyAnnotation.setPaint(Color.BLACK);
			xyAnnotation.setTextAnchor(TextAnchor.BOTTOM_LEFT);
			renderer.addAnnotation(xyAnnotation);
		}

		LookupPaintScale paintScale = new LookupPaintScale(0.5, ranges.size()+0.5, color);
		String[] labels=new String[ranges.size()+1];
labels[0]="";

		// ******************** SCALE ****************************
		for (Iterator iterator = ranges.iterator(); iterator.hasNext();) {
			RangeBlocks range = (RangeBlocks) iterator.next();
			Integer index=patternRangeIndex.get(range.getPattern());
			Color color=range.getColor();
			if(color!=null){
				Paint colorTransparent=new Color(color.getRed(), color.getGreen(), color.getBlue(), 50);			
				paintScale.add(index+0.5, colorTransparent);
			}
			//String insertLabel="            "+range.getLabel();
			String insertLabel=range.getLabel();			
			labels[index+1]=insertLabel;
		}
		renderer.setPaintScale(paintScale);

		
		
		SymbolAxis scaleAxis = new SymbolAxis(null, labels);
		scaleAxis.setRange(0.5, ranges.size()+0.5);
		scaleAxis.setPlot(new PiePlot());
		scaleAxis.setGridBandsVisible(false);

		org.jfree.chart.title.PaintScaleLegend psl = new PaintScaleLegend(paintScale, scaleAxis);
		psl.setMargin(new RectangleInsets(3, 10, 3, 10));
		psl.setPosition(RectangleEdge.BOTTOM);
		psl.setAxisOffset(5.0);
		// ******************** END SCALE ****************************

		
		
		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
		plot.setOrientation(PlotOrientation.HORIZONTAL);
		plot.setBackgroundPaint(Color.lightGray);
		plot.setRangeGridlinePaint(Color.white);
		plot.setAxisOffset(new RectangleInsets(5, 5, 5, 5));

		JFreeChart chart = new JFreeChart(name, plot);
		chart.removeLegend();
		chart.setBackgroundPaint(Color.white);

		chart.addSubtitle(psl);

		
		return chart;

	}





}



