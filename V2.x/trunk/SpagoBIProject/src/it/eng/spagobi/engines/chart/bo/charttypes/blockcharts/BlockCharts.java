package it.eng.spagobi.engines.chart.bo.charttypes.blockcharts;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.engines.chart.bo.ChartImpl;
import it.eng.spagobi.engines.chart.bo.charttypes.barcharts.BarCharts;
import it.eng.spagobi.engines.chart.bo.charttypes.blockcharts.util.RangeBlocks;

public class BlockCharts extends ChartImpl{
	private static transient Logger logger=Logger.getLogger(BlockCharts.class);

	// FIeld Database to recover from dataset
	public static String ANNOTATION="ANNOTATION";
	//static String SALE_CODE="SALA_CODICE";
	public static String BEGIN_ACTIVITY_DATE="BEGIN_DATE";
	public static String PATTERN="PATTERN";
	public static String DURATION="DURATA";

	Map confParameters;
	String xLabel;
	String yLabel;
	Double xMin;
	Double xMax;
	Double yMin;
	Double yMax;
	SimpleDateFormat beginDateFormat;
	SimpleDateFormat viewDateFormat;

	boolean yAutoRange;

	ArrayList<RangeBlocks> ranges;
	static final String X_LABEL = "x_label";
	static final String Y_LABEL = "y_label";
	static final String X_MAX = "x_max";
	static final String X_MIN = "x_min";
	static final String Y_MAX = "y_max";
	static final String Y_MIN = "y_min";
	static final String VIEW_DATE_FORMAT = "view_date_format";
	static final String Y_AUTO_RANGE = "y_auto_range";
	static final String HOUR_CODE = "ORA";



	@Override
	public void configureChart(SourceBean content) {
		super.configureChart(content);
		confParameters = new HashMap();
		SourceBean confSB = (SourceBean)content.getAttribute("CONF");

		List confAttrsList = confSB.getAttributeAsList("PARAMETER");

		Iterator confAttrsIter = confAttrsList.iterator();
		while(confAttrsIter.hasNext()) {
			SourceBean param = (SourceBean)confAttrsIter.next();
			String nameParam = (String)param.getAttribute("name");
			String valueParam = (String)param.getAttribute("value");
			confParameters.put(nameParam, valueParam);
		}

		if(confParameters.get(X_LABEL)!=null){	
			xLabel=(String)confParameters.get(X_LABEL);
		}
		else{
			xLabel="Hours";	
		}

		if(confParameters.get(Y_LABEL)!=null){	
			yLabel=(String)confParameters.get(Y_LABEL);
		}
		else{
			yLabel="Time";	
		}

		if(confParameters.get(X_MAX)!=null && confParameters.get(X_MIN)!=null){	
			String xMaxS=(String)confParameters.get(X_MAX);
			String xMinS=(String)confParameters.get(X_MIN);
			xMax=Double.valueOf(xMaxS);
			xMin=Double.valueOf(xMinS);
		}
		else{
			xMax=24.0;
			xMin=0.0;
		}

		if(confParameters.get(Y_AUTO_RANGE)!=null){	
			if(confParameters.get(Y_AUTO_RANGE).toString().equalsIgnoreCase("true"))
				yAutoRange=true;
			else yAutoRange=false;
		}
		else{
			yAutoRange=false;
		}



		if(confParameters.get(VIEW_DATE_FORMAT)!=null){	
			String viewFormat=(String)confParameters.get(VIEW_DATE_FORMAT);
			try{
				viewDateFormat=new SimpleDateFormat(viewFormat);
			}
			catch (Exception e) {
				logger.error("Wrong date format "+viewFormat+ ": use default");
				viewDateFormat=new SimpleDateFormat("dd/MM/yyyy");
			}
		}
		else{
			viewDateFormat=new SimpleDateFormat("dd/MM/yyyy");
		}


		ranges=new ArrayList<RangeBlocks>();
		SourceBean rangesSB = (SourceBean)content.getAttribute("RANGES");
		List rangesList = rangesSB.getAttributeAsList("RANGE");
		Iterator rangesIter = rangesList.iterator();
		while(rangesIter.hasNext()) {
			SourceBean range = (SourceBean)rangesIter.next();
			String rangeLabel = (String)range.getAttribute("label");
			String rangeColor = (String)range.getAttribute("color");
			String rangePattern = (String)range.getAttribute("pattern");
			Color color=null;
			if(rangeColor!=null){
				color=new Color(Integer.decode(rangeColor).intValue());
			}
			RangeBlocks block=new RangeBlocks(rangeLabel, rangePattern, color);
			ranges.add(block);
		}


		logger.debug("OUT");
	}


	public Map getConfParameters() {
		return confParameters;
	}


	public void setConfParameters(Map confParameters) {
		this.confParameters = confParameters;
	}








}
