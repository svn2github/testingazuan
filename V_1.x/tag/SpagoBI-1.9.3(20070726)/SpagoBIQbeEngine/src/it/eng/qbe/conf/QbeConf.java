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
package it.eng.qbe.conf;

import it.eng.qbe.utility.IDataMartModelRetriever;
import it.eng.qbe.utility.IQbeMessageHelper;
import it.eng.qbe.utility.IQueryPersister;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author Andrea Gioia
 */
public class QbeConf {
	private SourceBean config = null;
	
	private static transient Logger logger = Logger.getLogger(QbeConf.class);

	private static QbeConf instance = null;
	
	public static QbeConf getInstance(){
		if(instance==null) instance = new QbeConf();
		return instance;
	}
	
	private QbeConf() {
		config = ConfigSingleton.getInstance();	
	}
	
	public SourceBean getConfig() {
		return config;
	}
	
	public IQbeMessageHelper getQbeMessageHelper(){
		String qbeMsgHelperClass = (String)config.getAttribute("QBE.QBE-MSG-HELPER.className");
		
		IQbeMessageHelper msgHelper = null;
		try{
			msgHelper = (IQbeMessageHelper)Class.forName(qbeMsgHelperClass).newInstance();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return msgHelper;
	}
	
	public List getConnectionNames() {
		List connectionNames = new ArrayList();;
		List connections = config.getAttributeAsList("DATA-ACCESS.CONNECTION");
		if(connections != null && connections.size() > 0) {
			for(int i = 0; i < connections.size(); i++) {
				SourceBean connectionSB = (SourceBean)connections.get(i);
				connectionNames.add((String)connectionSB.getAttribute("name"));
			}				
		}		
		
		return connectionNames;		
	}
	
	public String getJndiConnectionName(String connectionName) {
		String jndiConnectionName = null;
		String jndiContext = null;
		SourceBean connectionSB = (SourceBean)config.getFilteredSourceBeanAttribute("DATA-ACCESS.CONNECTION", "name", connectionName);
		if(connectionSB != null) {
			jndiConnectionName = (String)connectionSB.getAttribute("jndiResourceName");
			//jndiConnectionName = "java:comp/env/" + jndiConnectionName; IT DOES NOT WORK ON JOnAS
			jndiContext = (String) connectionSB.getAttribute("jndiContext");
			if (jndiContext != null && !jndiContext.trim().equals("")) {
				if (jndiContext.endsWith("/")) jndiConnectionName = jndiContext + jndiConnectionName;
				else jndiConnectionName = jndiContext + "/" + jndiConnectionName;
			}
		}
		return jndiConnectionName;
	}
	
	public IDataMartModelRetriever getDataMartModelRetriever() throws Exception {		
		String dataMartModelRetrieverClassName = (String)ConfigSingleton.getInstance().getAttribute("QBE.DATA-MART-MODEL-RETRIEVER.className");
		IDataMartModelRetriever dataMartModelRetriever = (IDataMartModelRetriever)Class.forName(dataMartModelRetrieverClassName).newInstance();
		return dataMartModelRetriever;
	}
	
	public IQueryPersister getQueryPersister() throws Exception{
		String queryPersisterClass = (String)ConfigSingleton.getInstance().getAttribute("QBE.QUERY-PERSISTER.className");
		IQueryPersister queryPersister = (IQueryPersister)Class.forName(queryPersisterClass).newInstance();
		return queryPersister;
	}
	
	
	
	private void addFunctianalityProperties(Map functionalities, String functionalityName, Properties props) {
		Properties p = (Properties)functionalities.get(functionalityName);
		if(p == null) {
			p = new Properties();
		}
		p.putAll(props);
		functionalities.put(functionalityName, p);
	}
	
	public Map getFunctianalityProperties(SourceBean functionalitiesSB) {
		Map functionalities = new HashMap();
		
		List list = functionalitiesSB.getAttributeAsList("FUNCTIONALITY");
		for(int i = 0; i < list.size(); i++) {
			SourceBean functionalitySB = (SourceBean)list.get(i);
			String functionalityName = (String)functionalitySB.getAttribute("name");
			Properties props = new Properties();
			List parameters = functionalitySB.getAttributeAsList("PARAMETER");
			for(int j = 0; j < parameters.size(); j++) {
				SourceBean parameterSB = (SourceBean)parameters.get(j);
				String parameterName = (String)parameterSB.getAttribute("name");
				String parameterValue = (String)parameterSB.getAttribute("value");
				props.put(parameterName, parameterValue);
			}
			addFunctianalityProperties(functionalities, functionalityName, props);
		}
		
		return functionalities;
	}
	
	
	
	
}
