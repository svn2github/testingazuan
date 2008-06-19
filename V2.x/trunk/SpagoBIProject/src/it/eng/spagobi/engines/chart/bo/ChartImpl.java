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

package it.eng.spagobi.engines.chart.bo;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.engines.chart.bo.charttypes.barcharts.LinkableBar;
import it.eng.spagobi.engines.chart.bo.charttypes.barcharts.OverlaidBarLine;
import it.eng.spagobi.engines.chart.bo.charttypes.barcharts.SimpleBar;
import it.eng.spagobi.engines.chart.bo.charttypes.barcharts.StackedBar;
import it.eng.spagobi.engines.chart.bo.charttypes.boxcharts.SimpleBox;
import it.eng.spagobi.engines.chart.bo.charttypes.clusterchart.SimpleCluster;
import it.eng.spagobi.engines.chart.bo.charttypes.dialcharts.Meter;
import it.eng.spagobi.engines.chart.bo.charttypes.dialcharts.SBISpeedometer;
import it.eng.spagobi.engines.chart.bo.charttypes.dialcharts.SimpleDial;
import it.eng.spagobi.engines.chart.bo.charttypes.dialcharts.Thermometer;
import it.eng.spagobi.engines.chart.bo.charttypes.others.CumulativeCurveChart;
import it.eng.spagobi.engines.chart.bo.charttypes.piecharts.LinkablePie;
import it.eng.spagobi.engines.chart.bo.charttypes.piecharts.SimplePie;
import it.eng.spagobi.engines.chart.utils.DatasetMap;

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
	protected int titleDimension;
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


	/**
	 * configureChart reads the content of the template and sets the chart parameters.
	 * 
	 * @param content the content
	 */
	public void configureChart(SourceBean content) {
		logger.debug("IN");
		// common part for all charts
		//setting the title with parameter values if is necessary
		if(content.getAttribute("name")!=null) {
			String titleChart = (String)content.getAttribute("name");
			String tmpTitle = titleChart;
			while (!tmpTitle.equals("")){
				if (tmpTitle.indexOf("$P{") >= 0){
					String parName = tmpTitle.substring(tmpTitle.indexOf("$P{")+3, tmpTitle.indexOf("}"));
					String parValue = (parametersObject.get(parName)==null)?"":(String)parametersObject.get(parName);
					int pos = tmpTitle.indexOf("$P{"+parName+"}") + (parName.length()+4);
					titleChart = titleChart.replace("$P{" + parName + "}", parValue);
					tmpTitle = tmpTitle.substring(pos);
				}
				else
					tmpTitle = "";
			}
			setName(titleChart);
		}
		else setName("");

		if(content.getAttribute("title_dimension")!=null) 
		{
			String titleD=((String)content.getAttribute("title_dimension"));
			titleDimension=Integer.valueOf(titleD).intValue();
		}
		else setTitleDimension(18);


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
	 * This function creates the chart object.
	 * 
	 * @param chartTitle the chart title
	 * @param dataset the dataset
	 * 
	 * @return the j free chart
	 */

	public JFreeChart createChart(DatasetMap dataset) {
		return null;
	}


	/**
	 * This function creates the object of the right subtype as specified by type and subtype parameters found in template.
	 * 
	 * @param type the type
	 * @param subtype the subtype
	 * 
	 * @return the chart impl
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
			else if(subtype.equalsIgnoreCase("overlaid_barline")){
				sbi=new OverlaidBarLine();
			}		
			else if(subtype.equalsIgnoreCase("stacked_bar")){
				sbi=new StackedBar();
			}		
			
		}

		if(type.equals("BOXCHART")){
			if(subtype.equalsIgnoreCase("simplebox")){
				sbi=new SimpleBox();
			}
		}

		if(type.equals("CLUSTERCHART")){
			if(subtype.equalsIgnoreCase("simplecluster")){
				sbi=new SimpleCluster();
			}
		}



		return sbi;
	}




	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#getData()
	 */
	public String getData() {
		return data;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#getHeight()
	 */
	public int getHeight() {
		return height;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#getName()
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#getWidth()
	 */
	public int getWidth() {
		return width;

	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#setData(java.lang.String)
	 */
	public void setData(String _data) {
		data=_data;		
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#setHeight(int)
	 */
	public void setHeight(int _height) {
		height=_height;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#setName(java.lang.String)
	 */
	public void setName(String _name) {
		name=_name;		
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#setWidth(int)
	 */
	public void setWidth(int _width) {
		width=_width;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#calculateValue()
	 */
	public DatasetMap calculateValue() throws Exception {
		return null;
	}

	/**
	 * Gets the conf dataset.
	 * 
	 * @return the conf dataset
	 */
	public String getConfDataset() {
		return confDataset;
	}

	/**
	 * Sets the conf dataset.
	 * 
	 * @param confDataset the new conf dataset
	 */
	public void setConfDataset(String confDataset) {
		this.confDataset = confDataset; 
	}

	/**
	 * Gets the profile.
	 * 
	 * @return the profile
	 */
	public IEngUserProfile getProfile() {
		return profile;
	}

	/**
	 * Sets the profile.
	 * 
	 * @param profile the new profile
	 */
	public void setProfile(IEngUserProfile profile) {
		this.profile = profile;
	}

	/**
	 * Checks if is lov conf defined.
	 * 
	 * @return true, if is lov conf defined
	 */
	public boolean isLovConfDefined() {
		return isLovConfDefined;
	}

	/**
	 * Sets the lov conf defined.
	 * 
	 * @param isLovConfDefined the new lov conf defined
	 */
	public void setLovConfDefined(boolean isLovConfDefined) {
		this.isLovConfDefined = isLovConfDefined;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#isLinkable()
	 */
	public boolean isLinkable() {
		return false;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the subtype.
	 * 
	 * @return the subtype
	 */
	public String getSubtype() {
		return subtype;
	}

	/**
	 * Sets the subtype.
	 * 
	 * @param subtype the new subtype
	 */
	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#isChangeableView()
	 */
	public boolean isChangeableView() {
		return false;
	}

	/**
	 * Gets the change view label.
	 * 
	 * @param theme the theme
	 * @param i the i
	 * 
	 * @return the change view label
	 */
	public String getChangeViewLabel(String theme, int i) {
		return "";
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#setChangeViewChecked(boolean)
	 */
	public void setChangeViewChecked(boolean b) {
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#getPossibleChangePars()
	 */
	public List getPossibleChangePars() {
		return new Vector();
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#setChangeViewsParameter(java.lang.String, boolean)
	 */
	public void setChangeViewsParameter(String changePar, boolean how) {


	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#getChangeViewParameter(java.lang.String)
	 */
	public boolean getChangeViewParameter(String changePar) {
		return false;		
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#getChangeViewParameterLabel(java.lang.String, int)
	 */
	public String getChangeViewParameterLabel(String changePar, int i) {
		return null;
	}

	/**
	 * Gets the color.
	 * 
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets the color.
	 * 
	 * @param color the new color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#filterDataset(org.jfree.data.general.Dataset, java.util.HashMap, int, int)
	 */
	public Dataset filterDataset(Dataset dataset, HashMap categories, int catSelected, int numberCatsVisualization) {

		return null;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#isLegend()
	 */
	public boolean isLegend() {
		return legend;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#setLegend(boolean)
	 */
	public void setLegend(boolean legend) {
		this.legend = legend;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#getParametersObject()
	 */
	public Map getParametersObject() {
		return parametersObject;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#setParametersObject(java.util.Map)
	 */
	public void setParametersObject(Map parametersObject) {
		this.parametersObject = parametersObject;
	}

	public int getTitleDimension() {
		return titleDimension;
	}

	public void setTitleDimension(int titleDimension) {
		this.titleDimension = titleDimension;
	}






}
