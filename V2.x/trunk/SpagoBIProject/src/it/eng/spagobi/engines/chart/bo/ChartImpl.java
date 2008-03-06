package it.eng.spagobi.engines.chart.bo;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.engines.chart.bo.charttypes.barcharts.LinkableBar;
import it.eng.spagobi.engines.chart.bo.charttypes.barcharts.SimpleBar;
import it.eng.spagobi.engines.chart.bo.charttypes.dialcharts.Meter;
import it.eng.spagobi.engines.chart.bo.charttypes.dialcharts.SBISpeedometer;
import it.eng.spagobi.engines.chart.bo.charttypes.dialcharts.SimpleDial;
import it.eng.spagobi.engines.chart.bo.charttypes.dialcharts.Thermometer;
import it.eng.spagobi.engines.chart.bo.charttypes.piecharts.SimplePie;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Dataset;

public class ChartImpl implements IChart {

	protected String name=null;
	protected int width;
	protected int height;
	protected String dataLov;
	protected String confLov;
	protected boolean isLovConfDefined;
	protected IEngUserProfile profile;
	private static transient Logger logger=Logger.getLogger(ChartImpl.class);
protected String type="";
protected String subtype="";


	


	public void configureChart(SourceBean content) {
		// common part for all charts
		if(content.getAttribute("name")!=null) 
			setName((String)content.getAttribute("name"));
		else setName("");

		String widthS = (String)content.getAttribute("DIMENSION.width");
		String heightS = (String)content.getAttribute("DIMENSION.height");
		if(widthS==null || heightS==null){
			logger.warn("Width or height non defined, use default ones");
			widthS="400";
			heightS="300";
		}

		width=Integer.valueOf(widthS).intValue();
		height=Integer.valueOf(heightS).intValue();

		// get all the data parameters 

		try{					
			Map dataParameters = new HashMap();
			SourceBean dataSB = (SourceBean)content.getAttribute("DATA");
			List dataAttrsList = dataSB.getContainedSourceBeanAttributes();
			Iterator dataAttrsIter = dataAttrsList.iterator();
			while(dataAttrsIter.hasNext()) {
				SourceBeanAttribute paramSBA = (SourceBeanAttribute)dataAttrsIter.next();
				SourceBean param = (SourceBean)paramSBA.getValue();
				String nameParam = (String)param.getAttribute("name");
				String valueParam = (String)param.getAttribute("value");
				dataParameters.put(nameParam, valueParam);
			}

			if(dataParameters.get("datalov")!=null){	
				dataLov=(String)dataParameters.get("datalov");
			}
			else {
				logger.error("no data source specified");
				throw new Exception("No data source specified");}

		
			if(dataParameters.get("conflov")!=null && !(((String)dataParameters.get("conflov")).equalsIgnoreCase("") )){	
				confLov=(String)dataParameters.get("conflov");
				isLovConfDefined=true;
			}
			else {
				isLovConfDefined=false;
			}
		}
		catch (Exception e) {
			logger.error("error in reading dataq source parameters");
		}


	}

	public JFreeChart createChart(String chartTitle, Dataset dataset) {
		return null;
	}

	public static ChartImpl createChart(String type,String subtype){
		ChartImpl sbi=null;
		if(type.equals("DIALCHART")){
			if(subtype.equalsIgnoreCase("speedometer")){
				sbi=new SBISpeedometer();
			}
			else if(subtype.equalsIgnoreCase("simpledial")){
				sbi= new SimpleDial();
			}
			else if(subtype.equalsIgnoreCase("thermomether")){
				sbi= new Thermometer();
			}
			else if(subtype.equalsIgnoreCase("meter")){
				sbi= new Meter();
			}
		}
		if(type.equals("PIECHART")){
			if(subtype.equalsIgnoreCase("simplepie")){
				sbi=new SimplePie();
			}
		}
		if(type.equals("BARCHART")){
			if(subtype.equalsIgnoreCase("simplebar")){
				sbi=new SimpleBar();
			}
			else if(subtype.equalsIgnoreCase("linkablebar")){
				sbi=new LinkableBar();
			}
		}
				
		return sbi;
	}




	public String getDataLov() {
		return dataLov;
	}

	public int getHeight() {
		return height;
	}

	public String getName() {
		return name;
	}

	public int getWidth() {
		return width;
	}

	public void setDataLov(String _dataLov) {
		dataLov=_dataLov;		
	}

	public void setHeight(int _height) {
		height=_height;
	}

	public void setName(String _name) {
		name=_name;		
	}

	public void setWidth(int _width) {
		width=_width;
	}

	public Dataset calculateValue() throws SourceBeanException {
		return null;
	}

	public String getConfLov() {
		return confLov;
	}

	public void setConfLov(String confLov) {
		this.confLov = confLov; 
	}

	public IEngUserProfile getProfile() {
		return profile;
	}

	public void setProfile(IEngUserProfile profile) {
		this.profile = profile;
	}

	public boolean isLovConfDefined() {
		return isLovConfDefined;
	}

	public void setLovConfDefined(boolean isLovConfDefined) {
		this.isLovConfDefined = isLovConfDefined;
	}
	public boolean isLinkable() {
		return false;
	}



	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSubtype() {
		return subtype;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public boolean isChangeableView() {
		return false;
	}



}
