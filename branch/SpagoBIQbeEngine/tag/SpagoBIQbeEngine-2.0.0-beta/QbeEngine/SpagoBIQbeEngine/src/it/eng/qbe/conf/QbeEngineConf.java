/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.qbe.conf;

import it.eng.qbe.datasource.DataSourceCache;
import it.eng.qbe.locale.IQbeMessageHelper;
import it.eng.qbe.model.io.IDataMartModelRetriever;
import it.eng.qbe.model.io.IQueryPersister;
import it.eng.qbe.naming.NamingStrategy;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.qbe.commons.datasource.QbeDataSourceCache;
import it.eng.spagobi.qbe.commons.naming.QbeNamingStrategy;
import it.eng.spagobi.qbe.commons.urlgenerator.IQbeUrlGenerator;
import it.eng.spagobi.qbe.commons.urlgenerator.PortletQbeUrlGenerator;
import it.eng.spagobi.qbe.commons.urlgenerator.WebQbeUrlGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class QbeEngineConf.
 * 
 * @author Andrea Gioia
 */
public class QbeEngineConf {
	
	/** The locale. */
	private Locale locale = null;
	
	/** The config. */
	private SourceBean config = null;
	
	/** The logger. */
	private static transient Logger logger = Logger.getLogger(QbeEngineConf.class);

	/** The instance. */
	private static QbeEngineConf instance = null;
	
	/**
	 * Gets the single instance of QbeEngineConf.
	 * 
	 * @return single instance of QbeEngineConf
	 */
	public static QbeEngineConf getInstance(){
		if(instance==null) instance = new QbeEngineConf();
		return instance;
	}
	
	/**
	 * Instantiates a new qbe engine conf.
	 */
	private QbeEngineConf() {
		config = ConfigSingleton.getInstance();	
	}
	
	/**
	 * Gets the config.
	 * 
	 * @return the config
	 */
	public SourceBean getConfig() {
		return config;
	}
	
	
	/**
	 * Checks if is web modality active.
	 * 
	 * @return true, if is web modality active
	 */
	public boolean isWebModalityActive() {
		String qbeMode = (String)ConfigSingleton.getInstance().getAttribute("QBE.QBE-MODE.mode"); 
		return qbeMode.equalsIgnoreCase("WEB");		
	}
	
	/**
	 * Gets the url generator.
	 * 
	 * @return the url generator
	 */
	public IQbeUrlGenerator getUrlGenerator() {
		IQbeUrlGenerator urlGenerator = null;
		if( isWebModalityActive() ) {
			urlGenerator = new WebQbeUrlGenerator();
		} else {
			urlGenerator = new PortletQbeUrlGenerator();
		}
		return urlGenerator;
	}
	
	
	/**
	 * Gets the qbe message helper.
	 * 
	 * @return the qbe message helper
	 */
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
	
	/**
	 * Gets the bundle.
	 * 
	 * @return the bundle
	 */
	public String getBundle() {
		return "component_spagobiqbeIE_messages";
	}
	
	/**
	 * Gets the connection names.
	 * 
	 * @return the connection names
	 */
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
	
	/**
	 * Gets the jndi connection name.
	 * 
	 * @param connectionName the connection name
	 * 
	 * @return the jndi connection name
	 */
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
	
	/**
	 * Gets the data mart model retriever.
	 * 
	 * @return the data mart model retriever
	 * 
	 * @throws Exception the exception
	 */
	public IDataMartModelRetriever getDataMartModelRetriever() throws Exception {		
		String dataMartModelRetrieverClassName = (String)ConfigSingleton.getInstance().getAttribute("QBE.DATA-MART-MODEL-RETRIEVER.className");
		IDataMartModelRetriever dataMartModelRetriever = (IDataMartModelRetriever)Class.forName(dataMartModelRetrieverClassName).newInstance();
		return dataMartModelRetriever;
	}
	
	/**
	 * Gets the query persister.
	 * 
	 * @return the query persister
	 * 
	 * @throws Exception the exception
	 */
	public IQueryPersister getQueryPersister() throws Exception{
		String queryPersisterClass = (String)ConfigSingleton.getInstance().getAttribute("QBE.QUERY-PERSISTER.className");
		IQueryPersister queryPersister = (IQueryPersister)Class.forName(queryPersisterClass).newInstance();
		return queryPersister;
	}
	
	public Integer getResultLimit() {
		Integer resultLimit = null;
		String resultLimitStr = (String)ConfigSingleton.getInstance().getAttribute("QBE.QBE-SQL-RESULT-LIMIT.value");
		if(resultLimitStr == null || resultLimitStr.equalsIgnoreCase("none")) {
			resultLimit = null;
		} else {
			try {
				resultLimit = new Integer(resultLimitStr);
			} catch(Throwable t) {
				t.printStackTrace();
			}
		}
		return resultLimit;
	}
	
	/**
	 * Gets the locale.
	 * 
	 * @return the locale
	 */
	public Locale getLocale() {
		if(locale == null) {
			locale = new Locale("it", "ITALY");
		}
		return locale;
	}

	/**
	 * Sets the locale.
	 * 
	 * @param locale the new locale
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	/**
	 * Gets the naming strategy.
	 * 
	 * @return the naming strategy
	 */
	public NamingStrategy getNamingStrategy() {
		return new QbeNamingStrategy();
	}
	
	/**
	 * Gets the data source cache.
	 * 
	 * @return the data source cache
	 */
	public DataSourceCache getDataSourceCache() {
		return QbeDataSourceCache.getInstance();
	}
	
	
}
