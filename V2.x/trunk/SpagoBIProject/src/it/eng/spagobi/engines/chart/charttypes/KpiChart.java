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
import it.eng.spago.base.SourceBeanException;
import it.eng.spagobi.engines.chart.ChartImpl;
import it.eng.spagobi.engines.chart.charttypes.utils.LovAccessFunctions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DefaultValueDataset;


public class KpiChart extends ChartImpl {

	private static transient Logger logger=Logger.getLogger(KpiChart.class);
	protected double lower=0.0;
	protected double upper=0.0;


	Map confParameters;
	SourceBean sbRow;


	public JFreeChart createChart(String chartTitle, org.jfree.data.general.Dataset dataset){
		return null;
	}


	public void configureChart(SourceBean content){
		logger.debug("KpiChart");

		super.configureChart(content);

		try{

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

			}
			else{ // configuration parameters are set in a LOV
				logger.debug("configuration parameters set in LOV");
				String parameters=LovAccessFunctions.getLovResult(profile, confLov);

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


	public boolean isLovConfDefined() {
		return isLovConfDefined;
	}




	public void setLovConfDefined(boolean isLovConfDefined) {
		this.isLovConfDefined = isLovConfDefined;
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


	public Dataset calculateValue() throws SourceBeanException {
		String res=LovAccessFunctions.getLovResult(profile, getDataLov());

		SourceBean sbRows=SourceBean.fromXMLString(res);
		SourceBean sbRow=(SourceBean)sbRows.getAttribute("ROW");
		String result=(String)sbRow.getAttribute("value");

		DefaultValueDataset dataset = new DefaultValueDataset(Double.valueOf(result));
		return dataset;
	}








}
