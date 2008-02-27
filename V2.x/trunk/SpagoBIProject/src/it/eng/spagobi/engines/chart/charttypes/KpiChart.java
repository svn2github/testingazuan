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

package it.eng.spagobi.engines.chart.charttypes;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.engines.chart.charttypes.utils.LovAccessFunctions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.ValueDataset;

public class KpiChart {
	
	private static transient Logger logger=Logger.getLogger(KpiChart.class);
	double lower=0.0;
	double upper=0.0;
	String name=null;
	int width;
	int height;
	String dataName;
	String confName;
	boolean isLovConfDefined;
	Map dataParameters;
	Map confParameters;
	SourceBean sbRow;
	IEngUserProfile profile;
	
	public JFreeChart createDialChart(String chartTitle, ValueDataset dataset){
		return null;
	}
	

	public void configureKpiChart(SourceBean content){
		logger.debug("KpiChart");

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
				logger.error("no data source specified");
				throw new Exception("no data source specified");
			}


		if(dataParameters.get("confname")!=null && dataParameters.get("confname")!=""){	
				isLovConfDefined=true;
				confName=(String)dataParameters.get("confname");
			}
			else {
				isLovConfDefined=false;
			}


			if(isLovConfDefined==false){  // the configuration parameters are set in template
				logger.debug("Configuration in template");
				confParameters = new HashMap();
				SourceBean confSB = (SourceBean)content.getAttribute("CONF.GENERAL");
				List confAttrsList = confSB.getContainedSourceBeanAttributes();
				Iterator confAttrsIter = confAttrsList.iterator();
				while(confAttrsIter.hasNext()) {
					SourceBeanAttribute paramSBA = (SourceBeanAttribute)confAttrsIter.next();
					SourceBean param = (SourceBean)paramSBA.getValue();
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

			}
			else{ // configuration parameters are set in a LOV
				logger.debug("configuration parameters set in LOV");
				String parameters=LovAccessFunctions.getLovResult(profile, confName);

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


				if(lower==null || upper==null){
					logger.error("error in reading configuration lov");
					throw new Exception("error in reading configuration lov");
				}

				setLower(Double.valueOf(lower).doubleValue());
				setUpper(Double.valueOf(upper).doubleValue());

			}
		}catch (Exception e) {
			logger.error("error in reading template configurations");
		}

		logger.debug("OUT");
	}	



	public double getLower() {
		return lower;
	}

	public void setLower(double lower) {
		this.lower = lower;
	}

	public double getUpper() {
		return upper;
	}

	public void setUpper(double upper) {
		this.upper = upper;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
	public int getWidth() {
		return width;
	}




	public void setWidth(int width) {
		this.width = width;
	}




	public int getHeight() {
		return height;
	}




	public void setHeight(int height) {
		this.height = height;
	}




	public String getDataName() {
		return dataName;
	}




	public void setDataName(String dataName) {
		this.dataName = dataName;
	}




	public String getConfName() {
		return confName;
	}




	public void setConfName(String confName) {
		this.confName = confName;
	}




	public boolean isLovConfDefined() {
		return isLovConfDefined;
	}




	public void setLovConfDefined(boolean isLovConfDefined) {
		this.isLovConfDefined = isLovConfDefined;
	}




	public Map getDataParameters() {
		return dataParameters;
	}




	public void setDataParameters(Map dataParameters) {
		this.dataParameters = dataParameters;
	}




	public Map getConfParameters() {
		return confParameters;
	}




	public void setConfParameters(Map confParameters) {
		this.confParameters = confParameters;
	}




	public SourceBean getSbRow() {
		return sbRow;
	}




	public void setSbRow(SourceBean sbRow) {
		this.sbRow = sbRow;
	}




	public IEngUserProfile getProfile() {
		return profile;
	}




	public void setProfile(IEngUserProfile profile) {
		this.profile = profile;
	}


	
	
}
