package it.eng.spagobi.engines.chart;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.engines.chart.charttypes.Dashboard;
import it.eng.spagobi.engines.chart.charttypes.SBISpeedometer;
import it.eng.spagobi.engines.chart.charttypes.SimpleDial;
import it.eng.spagobi.engines.chart.charttypes.Thermometer;
import it.eng.spagobi.engines.chart.charttypes.piecharts.SimplePie;

import org.apache.log4j.Logger;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Dataset;

public class ChartImpl implements IChart {

	protected String name=null;
	protected int width;
	protected int height;
	protected String dataName;
	protected String confName;
	protected IEngUserProfile profile;
	private static transient Logger logger=Logger.getLogger(ChartImpl.class);
	protected Map dataParameters;

	public String getConfName() {
		return confName;
	}

	public void setConfName(String confName) {
		this.confName = confName; 
	}

	public IEngUserProfile getProfile() {
		return profile;
	}

	public void setProfile(IEngUserProfile profile) {
		this.profile = profile;
	}

	public void configureChart(SourceBean content) {
		// common part for all charts
		if(content.getAttribute("name")!=null) 
			setName((String)content.getAttribute("name"));
		else setName("");
		String widthS = (String)content.getAttribute("width");
		String heightS = (String)content.getAttribute("height");
		if(widthS==null || heightS==null){
			logger.warn("Width or height non defined, use default ones");
			widthS="400";
			heightS="300";
		}

		width=Integer.valueOf(widthS).intValue();
		height=Integer.valueOf(heightS).intValue();

		// get all the data parameters 
		try{
			dataParameters = new HashMap();
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

			if(dataParameters.get("dataname")!=null){	
				dataName=(String)dataParameters.get("dataname");
			}
			else {
				logger.error("no data source specified");}
			throw new Exception("No data source specified");
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
		if(type.equals("KPICHART")){
			if(subtype.equalsIgnoreCase("speedometer")){
				sbi=new SBISpeedometer();
			}
			else if(subtype.equalsIgnoreCase("simpledial")){
				sbi= new SimpleDial();
			}
			else if(subtype.equalsIgnoreCase("thermomether")){
				sbi= new Thermometer();
			}
			else if(subtype.equalsIgnoreCase("dashboard")){
				sbi= new Dashboard();
			}
		}
		if(type.equals("PIECHART")){
			if(subtype.equalsIgnoreCase("simplepie")){
				sbi=new SimplePie();
			}
		}
		return sbi;
	}




	public String getDataName() {
		return dataName;
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

	public void setDataName(String _dataName) {
		dataName=_dataName;		
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

	public Map getDataParameters() {
		return dataParameters;
	}

	public void setDataParameters(Map dataParameters) {
		this.dataParameters = dataParameters;
	}



}
