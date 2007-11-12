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
package it.eng.qbe.action;

import it.eng.qbe.conf.QbeConf;
import it.eng.qbe.log.Logger;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.action.AbstractAction;
import it.eng.spagobi.utilities.ParametersDecoder;
import it.eng.spagobi.utilities.callbacks.audit.AuditAccessUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import sun.misc.BASE64Decoder;


/**
 * @author Andrea Gioia
 */
public class SpagoBIStartAction extends AbstractAction {
	
	//private static transient Logger logger = Logger.getLogger(SpagoBIStartAction.class);
	
	public static final String TEMPLATE = "template";
	public static final String DATA_SOURCE_DIALECT = "DIALECT";
	public static final String DATA_SOURCE_NAME = "JNDI_DS";
	public static final String DATAMART_PATH = "PATH";	
	public static final String DBLINK_MAP = "DBLINK_MAP";	
	public static final String QUERI_ID =  "queryId";
	public static final String SPAGOBI_USER =  "SPAGOBI_USER";
	public static final String SPAGOBI_URL =  "SPAGOBI_URL";
	public static final String SPAGOBI_PATH =  "SPAGOBI_PATH";
	public static final String SPAGOBI_COUNTRY =  "SPAGOBI_COUNTRY";
	public static final String SPAGOBI_LANGUAGE =  "SPAGOBI_LANGUAGE";
	public static final String MAP_CATALOGUE_MANAGER_URL = "MAP_CATALOGUE_MANAGER_URL";
	
	
	private void addParToParMap(Map params, String parName, String parValue) {
		String newParValue;
		
		ParametersDecoder decoder = new ParametersDecoder();
		if(decoder.isMultiValues(parValue)) {			
			List values = decoder.decode(parValue);
			newParValue = "";
			for(int i = 0; i < values.size(); i++) {
				newParValue += (i>0?",":"");
				newParValue += values.get(i);
			}
			
		} else {
			newParValue = parValue;
		}
		
		params.put(parName, newParValue);
	}
	
	private Map getParametersMap(SourceBean request) {
		Map params = new HashMap();
		List attributes = request.getContainedAttributes();
		Iterator it = attributes.iterator();
		String parName = null;
		String parValue = null;
		Logger.debug(SpagoBIStartAction.class, this.getClass().getName() +":service:Reading request parameters...");
		while (it.hasNext()) {
			SourceBeanAttribute attribute = (SourceBeanAttribute)it.next();
			parName = attribute.getKey();
			parValue = (String)attribute.getValue();
			addParToParMap(params, parName, parValue);
			System.out.println(this.getClass().getName() +":service:Read " +
					"parameter [" + parName + "] with value [" + parValue + "] from request");
			Logger.debug(SpagoBIStartAction.class, this.getClass().getName() +":service:Read " +
					"parameter [" + parName + "] with value [" + parValue + "] from request");
		}
		return params;
	}
	
	public void service(SourceBean request, SourceBean response) {
		
		RequestContainer requestContainer = getRequestContainer();
		SessionContainer session = requestContainer.getSessionContainer();
		
		// AUDIT UPDATE
		HttpServletRequest httpRequest = (HttpServletRequest) this.getRequestContainer().getInternalRequest();
		String auditId = httpRequest.getParameter("SPAGOBI_AUDIT_ID");
		AuditAccessUtils auditAccessUtils = 
			(AuditAccessUtils) httpRequest.getSession().getAttribute("SPAGOBI_AUDIT_UTILS");
		if (auditId != null) {
			if (auditAccessUtils != null) auditAccessUtils.updateAudit(auditId, new Long(System.currentTimeMillis()), null, 
					"EXECUTION_STARTED", null, null);
		}
		session.setAttribute("SPAGOBI_AUDIT_ID", auditId);
		
		Logger.debug(SpagoBIStartAction.class, this.getClass().getName() + ":service:Start processing a new request...");
		Map parameters = getParametersMap(request);
		Logger.debug(SpagoBIStartAction.class, this.getClass().getName() +":service:Request parameters read sucesfully" + parameters);
				
		try {
			String templatePath = (String)parameters.get("templatePath");
			parameters.remove("templatePath");
			String spagobiurl = (String)parameters.get("spagobiurl");
			parameters.remove("spagobiurl");
			String mapCatalogueManagerUrl = (String)parameters.get("mapCatalogueManagerUrl");
			parameters.remove("mapCatalogueManagerUrl");
			
			String user = (String)parameters.get("user");
			parameters.remove("user");
			
			
			String template = (String)parameters.get(TEMPLATE);
			parameters.remove(TEMPLATE);
			BASE64Decoder bASE64Decoder = new BASE64Decoder();
			template = new String(bASE64Decoder.decodeBuffer(template));
			System.out.println("Template: " + template);
			
			
			
			
			SourceBean templateSB = SourceBean.fromXMLString(template);
			
			Object path = null;
			
			Map dblinkMap = new HashMap();
			List dmModalities = new ArrayList();
			
			if(templateSB.getName().equalsIgnoreCase("COMPOSITE-QBE")) {
				List dmNames = new ArrayList();
				List qbeList = templateSB.getAttributeAsList("QBE");
				for(int i = 0; i < qbeList.size(); i++) {
					SourceBean qbeSB = (SourceBean)qbeList.get(i);
										
					SourceBean datamartSB = (SourceBean)qbeSB.getAttribute("DATAMART");
					String dmName = (String)datamartSB.getAttribute("name");
					dmNames.add(dmName);
					String dblink = (String)datamartSB.getAttribute("dblink");
					if(dblink != null) dblinkMap.put(dmName, dblink);
					
					SourceBean modalitySB = (SourceBean)qbeSB.getAttribute("MODALITY");
					dmModalities.add(modalitySB);					
				}
				
				path = dmNames;
			} else {
				SourceBean datamartSB = (SourceBean)templateSB.getAttribute("DATAMART");
				String dmName = (String)datamartSB.getAttribute("name");
				path = dmName;
				SourceBean modalitySB = (SourceBean)templateSB.getAttribute("MODALITY");
				dmModalities.add(modalitySB);
			}
			
			
			SourceBean datasourceSB = (SourceBean)templateSB.getAttribute("DATASOURCE");
			
			
			session.delAttribute("FUNCTIONALITIES");
			SourceBean functionalitiesSB = (SourceBean)templateSB.getAttribute("FUNCTIONALITIES");
			Map functionalities = null;
			if(functionalitiesSB != null)
				functionalities = QbeConf.getInstance().getFunctianalityProperties(functionalitiesSB);
			if(functionalities != null) session.setAttribute("FUNCTIONALITIES", functionalities);
						
			

			
			String dsName = (String)datasourceSB.getAttribute("name");
			System.out.println("Data-Source: " + dsName);
			String dsDialect = (String)datasourceSB.getAttribute("dialect");
			System.out.println("Dialect: " + dsDialect);
						
			String queryId = (String)parameters.get("query");
			parameters.remove("query");
			System.out.println("Query: " + queryId);
			
			String country = (String)parameters.get("country");
			parameters.remove("country");
			System.out.println("country: " + country);
			
			String language = (String)parameters.get("language");
			parameters.remove("language");
			System.out.println("language: " + language);
			
			String props = "";
			Iterator it = parameters.keySet().iterator();
			while(it.hasNext()) {
				String parameterName = (String)it.next();
				Object parameterValue = parameters.get(parameterName);
				if(parameterValue.getClass().getName().equalsIgnoreCase(String.class.getName())) {
					props += parameterName + "=" + parameterValue + "\n";
				}
			}
			
			response.setAttribute(SPAGOBI_USER, user);
			response.setAttribute(SPAGOBI_URL, spagobiurl);
			response.setAttribute(MAP_CATALOGUE_MANAGER_URL, mapCatalogueManagerUrl);
			response.setAttribute(SPAGOBI_PATH, templatePath);
			response.setAttribute(DATA_SOURCE_DIALECT, dsDialect);
			response.setAttribute(DATA_SOURCE_NAME, dsName);
			response.setAttribute(DATAMART_PATH, path);
			response.setAttribute(DBLINK_MAP, dblinkMap);			
			response.setAttribute(QUERI_ID, queryId);
			response.setAttribute(SPAGOBI_COUNTRY, country);
			response.setAttribute(SPAGOBI_LANGUAGE, language);
			response.setAttribute("DATAMART_PROPERTIES", props.toString());
			response.setAttribute("MODALITY", dmModalities);
		} catch (SourceBeanException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
