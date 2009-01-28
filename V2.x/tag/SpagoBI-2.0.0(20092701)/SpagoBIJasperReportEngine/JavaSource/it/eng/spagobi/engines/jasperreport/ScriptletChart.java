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

package it.eng.spagobi.engines.jasperreport;




import it.eng.spagobi.services.proxy.DocumentExecuteServiceProxy;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;

/**
 * @author Giulio Gavardi
 *         giulio.gavardi@eng.it
 */

public class ScriptletChart extends JRDefaultScriptlet {

	private static transient Logger logger=Logger.getLogger(ScriptletChart.class);


	public void afterReportInit() throws JRScriptletException {

		logger.debug("IN");

		Vector exclude=new Vector();
		exclude.add("spagobicontext");
		exclude.add("documentLabel");
		exclude.add("SPAGOBI_AUDIT_ID");
		exclude.add("REPORT_VIRTUALIZER");
		exclude.add("SBI_USERID");
		exclude.add("REPORT_SCRIPTLET");
		exclude.add("REPORT_FORMAT_FACTORY");
		exclude.add("SBI_HTTP_SESSION");
		//exclude.add("userId");
		exclude.add("REPORT_TIME_ZONE");
		exclude.add("EXECUTION_ID");
		exclude.add("REPORT_LOCALE");
		exclude.add("REPORT_PARAMETERS_MAP");

		exclude.add("BACK_END_SPAGOBI_CONTEXT");
		exclude.add("IS_IGNORE_PAGINATION");
		exclude.add("REPORT_CONNECTION");
		exclude.add("REPORT_PARAMETERS_MAP");



		try {
			// Creazione GET method

			HashMap parametersMap=(HashMap)this.getParameterValue("REPORT_PARAMETERS_MAP");

						
			String userId=(String)parametersMap.get("SBI_USERID");

			logger.debug("user id is "+userId);

			HttpSession session=(HttpSession)parametersMap.get("SBI_HTTP_SESSION");

			HashMap chartParameters=new HashMap();

			// Get the chart Label
			Object labelO=this.getVariableValue("chart_label");
			String label=null;
			if(labelO!=null)label=labelO.toString();
			else {
				logger.error("chart label null");
			}

			//Get all the parameters for chart (they are labelled as chart_nameParameter

			for (Iterator iterator = parametersMap.keySet().iterator(); iterator.hasNext();) {
				String name = (String ) iterator.next();
				if(name.equalsIgnoreCase("chart_label")){
					Object value=parametersMap.get(name);
					label=value.toString();
				}
				else{
					if(!exclude.contains(name)){
						Object value=parametersMap.get(name);
						if(value!=null){
							logger.debug("parameters to service: "+name+" "+value.toString());
							chartParameters.put(name, value);
						}				
					}
				}

				


			}
			
			logger.debug("chart label: "+label);

			DocumentExecuteServiceProxy proxy=new DocumentExecuteServiceProxy(userId,session);
			logger.debug("Calling Service");
			byte[] image=proxy.executeChart(label, chartParameters);
			logger.debug("Back from Service");

			InputStream is=new ByteArrayInputStream(image);

			logger.debug("Input Stream filled, Setting variable");
			this.setVariableValue("chart_image", is);

			logger.debug("OUT");
		} 
		catch (Exception e) {
			logger.error("Error in scriptlet",e);
			throw new JRScriptletException(e);
		}


	}










}
