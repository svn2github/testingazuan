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

package it.eng.spagobi.engines.chart.bo.charttypes.piecharts;

/**   @author Giulio Gavardi
 *     giulio.gavardi@eng.it
 */

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spagobi.engines.chart.bo.ChartImpl;
import it.eng.spagobi.engines.chart.bo.charttypes.barcharts.SimpleBar;
import it.eng.spagobi.engines.chart.utils.DataSetAccessFunctions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DefaultPieDataset;

public class PieCharts extends ChartImpl {

	Map confParameters;
	private static transient Logger logger=Logger.getLogger(PieCharts.class);

	public void configureChart(SourceBean content) {
logger.debug("IN");
		super.configureChart(content);

		confParameters = new HashMap();
		SourceBean confSB = (SourceBean)content.getAttribute("CONF");

		if(confSB==null) return;
		List confAttrsList = confSB.getAttributeAsList("PARAMETER");

		Iterator confAttrsIter = confAttrsList.iterator();
		while(confAttrsIter.hasNext()) {
			SourceBean param = (SourceBean)confAttrsIter.next();
			String nameParam = (String)param.getAttribute("name");
			String valueParam = (String)param.getAttribute("value");
			confParameters.put(nameParam, valueParam);
		}	
		logger.debug("OUT");
	}

		public JFreeChart createChart(String chartTitle, Dataset dataset) {
			// TODO Auto-generated method stub
			return super.createChart(chartTitle, dataset);



		}

		public Dataset calculateValue(Map parameters) throws Exception {
			logger.debug("IN");
			String res=DataSetAccessFunctions.getDataSetResult(profile, getData(),parameters);

			SourceBean sbRows=SourceBean.fromXMLString(res);
			SourceBean sbRow=(SourceBean)sbRows.getAttribute("ROW");
			List listAtts=sbRow.getContainedAttributes();
			DefaultPieDataset dataset = new DefaultPieDataset();
			for (Iterator iterator = listAtts.iterator(); iterator.hasNext();) {
				SourceBeanAttribute att = (SourceBeanAttribute) iterator.next();
				String name=att.getKey();
				String valueS=(String)att.getValue();

				//try Double and Integer Conversion

				Double valueD=null;
				try{
					valueD=Double.valueOf(valueS);
				}
				catch (Exception e) {}

				Integer valueI=null;
				if(valueD==null){
					valueI=Integer.valueOf(valueS);
				}

				if(name!=null && valueD!=null){
					dataset.setValue(name, valueD);
				}
				else if(name!=null && valueI!=null){
					dataset.setValue(name, valueI);
				}

			}
			logger.debug("OUT");
			return dataset;
		}



		public Map getConfParameters() {
			return confParameters;
		}

		public void setConfParameters(Map confParameters) {
			this.confParameters = confParameters;
		}


	}
