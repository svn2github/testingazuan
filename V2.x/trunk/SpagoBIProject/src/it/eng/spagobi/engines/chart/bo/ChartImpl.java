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

package it.eng.spagobi.engines.chart.bo;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.engines.chart.bo.charttypes.barcharts.LinkableBar;
import it.eng.spagobi.engines.chart.bo.charttypes.barcharts.SimpleBar;
import it.eng.spagobi.engines.chart.bo.charttypes.dialcharts.Meter;
import it.eng.spagobi.engines.chart.bo.charttypes.dialcharts.SBISpeedometer;
import it.eng.spagobi.engines.chart.bo.charttypes.dialcharts.SimpleDial;
import it.eng.spagobi.engines.chart.bo.charttypes.dialcharts.Thermometer;
import it.eng.spagobi.engines.chart.bo.charttypes.piecharts.LinkablePie;
import it.eng.spagobi.engines.chart.bo.charttypes.piecharts.SimplePie;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Dataset;


/**   @author Giulio Gavardi
 *     giulio.gavardi@eng.it
 */
 

public class ChartImpl implements IChart {

	protected String name=null;
	protected int width;
	protected int height;
	protected String data;
	protected String confDataset;
	protected boolean isLovConfDefined;
	protected IEngUserProfile profile;
	protected String type="";
	protected String subtype="";
	protected Color color;
	protected boolean legend=true;
	private static transient Logger logger=Logger.getLogger(ChartImpl.class);
	protected Map parametersObject;

	
/**  configureChart reads the content of the template and sets the chart parameters
 * 
 */
	public void configureChart(SourceBean content) {
		logger.debug("IN");
		// common part for all charts
		if(content.getAttribute("name")!=null) 
			setName((String)content.getAttribute("name"));
		else setName("");
		
		String colS = (String)content.getAttribute("COLORS.background");
		if(colS!=null) 
		{
			Color col=new Color(Integer.decode(colS).intValue());
			if(col!=null){
				setColor(col);}
			else{
				setColor(Color.white);
			}
		}
		else { 	
			setColor(Color.white);
		}

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
			SourceBean dataSB = (SourceBean)content.getAttribute("CONF");
			List dataAttrsList = dataSB.getContainedSourceBeanAttributes();
			Iterator dataAttrsIter = dataAttrsList.iterator();
			while(dataAttrsIter.hasNext()) {
				SourceBeanAttribute paramSBA = (SourceBeanAttribute)dataAttrsIter.next();
				SourceBean param = (SourceBean)paramSBA.getValue();
				String nameParam = (String)param.getAttribute("name");
				String valueParam = (String)param.getAttribute("value");
				dataParameters.put(nameParam, valueParam);
			}


			if(dataParameters.get("confdataset")!=null && !(((String)dataParameters.get("confdataset")).equalsIgnoreCase("") )){	
				confDataset=(String)dataParameters.get("confdataset");
				isLovConfDefined=true;
			}
			else {
				isLovConfDefined=false;
			}
			
			legend=true;
			if(dataParameters.get("legend")!=null && !(((String)dataParameters.get("legend")).equalsIgnoreCase("") )){	
				String leg=(String)dataParameters.get("legend");
				if(leg.equalsIgnoreCase("false"))
					legend=false;
			}
			
			
		}
		catch (Exception e) {
			logger.error("error in reading data source parameters");
		}


	}
	
	/**
	 * This function creates the chart object
	 */

	public JFreeChart createChart(String chartTitle, Dataset dataset) {
		return null;
	}

	
	/**
	 * This function creates the object of the right subtype as specified by type and subtype parameters found in template
	 */
	
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
			if(subtype.equalsIgnoreCase("linkablepie")){
				sbi=new LinkablePie();
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




	public String getData() {
		return data;
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

	public void setData(String _data) {
		data=_data;		
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

	public Dataset calculateValue() throws Exception {
		return null;
	}

	public String getConfDataset() {
		return confDataset;
	}

	public void setConfDataset(String confDataset) {
		this.confDataset = confDataset; 
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

	public String getChangeViewLabel(String theme, int i) {
		return "";
	}

	public void setChangeViewChecked(boolean b) {
	}

	public List getPossibleChangePars() {
		return new Vector();
	}

	public void setChangeViewsParameter(String changePar, boolean how) {


	}

	public boolean getChangeViewParameter(String changePar) {
		return false;		
	}

	public String getChangeViewParameterLabel(String changePar, int i) {
		return null;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Dataset filterDataset(Dataset dataset, HashMap categories, int catSelected, int numberCatsVisualization) {

		return null;
	}

	public boolean isLegend() {
		return legend;
	}

	public void setLegend(boolean legend) {
		this.legend = legend;
	}

	public Map getParametersObject() {
		return parametersObject;
	}

	public void setParametersObject(Map parametersObject) {
		this.parametersObject = parametersObject;
	}






}
