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

package it.eng.spagobi.engines.chart.bo.charttypes.dialcharts;


import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spagobi.engines.chart.bo.ChartImpl;
import it.eng.spagobi.engines.chart.utils.DataSetAccessFunctions;
import it.eng.spagobi.engines.chart.utils.DatasetMap;
import it.eng.spagobi.engines.chart.utils.StyleLabel;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultValueDataset;

/** 
 *  * @author Giulio Gavardi
 *     giulio.gavardi@eng.it
 */

public class DialCharts extends ChartImpl {

	private static transient Logger logger=Logger.getLogger(DialCharts.class);
	protected double lower=0.0;
	protected double upper=0.0;
	StyleLabel labelsTickStyle;
	StyleLabel labelsValueStyle;
	Map confParameters;
	SourceBean sbRow;
	


	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.ChartImpl#createChart(java.lang.String, org.jfree.data.general.Dataset)
	 */
	public JFreeChart createChart(DatasetMap dataset){
		return null;
	}


	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.ChartImpl#configureChart(it.eng.spago.base.SourceBean)
	 */
	public void configureChart(SourceBean content){
		logger.debug("IN");

		super.configureChart(content);

		try{
			// check if there is some info about additional labels style

			SourceBean styleTickLabelsSB = (SourceBean)content.getAttribute("STYLE_TICK_LABELS");
			if(styleTickLabelsSB!=null){

				String fontS = (String)content.getAttribute("STYLE_TICK_LABELS.font");
				if(fontS==null){
					fontS = defaultLabelsStyle.getFontName();
				}
				String sizeS = (String)content.getAttribute("STYLE_TICK_LABELS.size");
				String colorS = (String)content.getAttribute("STYLE_TICK_LABELS.color");

				try{
					Color color= Color.BLACK;
					if(colorS!=null){
						color=Color.decode(colorS);
					}else{
						defaultLabelsStyle.getColor();
					}
					int size= 12;
					if(sizeS!=null){
						size=Integer.valueOf(sizeS).intValue();
					}else{
						size = defaultLabelsStyle.getSize();
					}
					
					labelsTickStyle=new StyleLabel(fontS,size,color);

				}
				catch (Exception e) {
					logger.error("Wrong style labels settings, use default");
				}

			}else{
				labelsTickStyle = defaultLabelsStyle;
			}
			
			SourceBean styleValueLabelsSB = (SourceBean)content.getAttribute("STYLE_VALUE_LABEL");
			if(styleValueLabelsSB!=null){

				String fontS = (String)content.getAttribute("STYLE_VALUE_LABEL.font");
				if(fontS==null){
					fontS = defaultLabelsStyle.getFontName();
				}
				String sizeS = (String)content.getAttribute("STYLE_VALUE_LABEL.size");
				String colorS = (String)content.getAttribute("STYLE_VALUE_LABEL.color");
				
				try{
					Color color= Color.BLACK;
					if(colorS!=null){
						color=Color.decode(colorS);
					}else{
						defaultLabelsStyle.getColor();
					}
					int size= 12;
					if(sizeS!=null){
						size=Integer.valueOf(sizeS).intValue();
					}else{
						size = defaultLabelsStyle.getSize();
					}
					labelsValueStyle=new StyleLabel(fontS,size,color);

				}
				catch (Exception e) {
					logger.error("Wrong style labels settings, use default");
				}

			}else{
				labelsValueStyle = defaultLabelsStyle;
			}

			if(isLovConfDefined==false){  // the configuration parameters are set in template
				logger.debug("Configuration in template");
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
				if(confParameters.get("lower")!=null){	
					String lower=(String)confParameters.get("lower");
					setLower(Double.valueOf(lower).doubleValue());
				}
				else {
					logger.error("lower bound not defined");
					throw new Exception("lower bound not defined");
				}
				if(confParameters.get("upper")!=null){	
					String upper=(String)confParameters.get("upper");
					setUpper(Double.valueOf(upper).doubleValue());
				}
				else {
					logger.error("upper bound not defined");
					throw new Exception("upper bound not defined");
				}
				
				multichart=false;
				if(confParameters.get("multichart")!=null && !(((String)confParameters.get("multichart")).equalsIgnoreCase("") )){	
					String multiple=(String)confParameters.get("multichart");
					if(multiple.equalsIgnoreCase("true"))
						setMultichart(true);
				}
				
				orientationMultichart="horizontal";
				if(confParameters.get("orientation_multichart")!=null && !(((String)confParameters.get("orientation_multichart")).equalsIgnoreCase("") )){	
					String ori=(String)confParameters.get("orientation_multichart");
					if(ori.equalsIgnoreCase("horizontal") || ori.equalsIgnoreCase("vertical") )
						setOrientationMultichart(ori);
				}

			}
			else{ // configuration parameters are set in a LOV
				logger.debug("configuration parameters set in LOV");
				//String parameters=LovAccessFunctions.getLovResult(profile, confLov);

				
				
				String parameters=DataSetAccessFunctions.getDataSetResultFromLabel(profile, confDataset, parametersObject);
				
				
				SourceBean sourceBeanResult=null;
				try {
					sourceBeanResult = SourceBean.fromXMLString(parameters);
				} catch (SourceBeanException e) {
					logger.error("error in reading configuration lov");
					throw new Exception("error in reading configuration lov");
				}

				sbRow=(SourceBean)sourceBeanResult.getAttribute("ROW");
				String lower=(String)sbRow.getAttribute("lower");
				String upper=(String)sbRow.getAttribute("upper");
				String legend=(String)sbRow.getAttribute("legend");
				String multichart=(String)sbRow.getAttribute("multichart");
				String orientation=(String)sbRow.getAttribute("orientation_multichart");

				if(lower==null || upper==null){
					logger.error("error in reading configuration lov");
					throw new Exception("error in reading configuration lov");
				}

				setLower(Double.valueOf(lower).doubleValue());
				setUpper(Double.valueOf(upper).doubleValue());
				setMultichart((multichart.equals("true")?true:false));
				setLegend(legend.equals("true")?true:false);
				setOrientationMultichart(orientation);
			}
			
			
		}catch (Exception e) {
			logger.error("error in reading template configurations");
		}

		logger.debug("OUT");
	}	




	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.ChartImpl#calculateValue()
	 */
	public DatasetMap calculateValue() throws Exception{
		logger.debug("IN");
		String res=DataSetAccessFunctions.getDataSetResultFromId(profile, getData(),parametersObject);
		if (res!=null){
			
			if (name.indexOf("$F{") >= 0){
				List atts=new Vector();
				atts.add(res);
				setTitleParameter(atts);
			}
			
			logger.debug("Dataset result:"+res);
			SourceBean sbRows=SourceBean.fromXMLString(res);
			SourceBean sbRow=(SourceBean)sbRows.getAttribute("ROW");
			String result="";
			if(sbRow==null){
				result=(new Double(lower)).toString();
			}
			else{
				result=(String)sbRow.getAttribute("value");
			}
			DefaultValueDataset dataset = new DefaultValueDataset(Double.valueOf(result));
			logger.debug("OUT");

			DatasetMap datasets=new DatasetMap();
			datasets.addDataset("1",dataset);
			return datasets;		}
		logger.error("dataset is null!!!!!!!!!");
		return null;
	}




	/**
	 * Gets the lower.
	 * 
	 * @return the lower
	 */
	public double getLower() {
		return lower;
	}

	/**
	 * Sets the lower.
	 * 
	 * @param lower the new lower
	 */
	public void setLower(double lower) {
		this.lower = lower;
	}

	/**
	 * Gets the upper.
	 * 
	 * @return the upper
	 */
	public double getUpper() {
		return upper;
	}

	/**
	 * Sets the upper.
	 * 
	 * @param upper the new upper
	 */
	public void setUpper(double upper) {
		this.upper = upper;
	}


	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.ChartImpl#isLovConfDefined()
	 */
	public boolean isLovConfDefined() {
		return isLovConfDefined;
	}




	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.ChartImpl#setLovConfDefined(boolean)
	 */
	public void setLovConfDefined(boolean isLovConfDefined) {
		this.isLovConfDefined = isLovConfDefined;
	}




	/**
	 * Gets the conf parameters.
	 * 
	 * @return the conf parameters
	 */
	public Map getConfParameters() {
		return confParameters;
	}




	/**
	 * Sets the conf parameters.
	 * 
	 * @param confParameters the new conf parameters
	 */
	public void setConfParameters(Map confParameters) {
		this.confParameters = confParameters;
	}




	/**
	 * Gets the sb row.
	 * 
	 * @return the sb row
	 */
	public SourceBean getSbRow() {
		return sbRow;
	}




	/**
	 * Sets the sb row.
	 * 
	 * @param sbRow the new sb row
	 */
	public void setSbRow(SourceBean sbRow) {
		this.sbRow = sbRow;
	}

	
}
