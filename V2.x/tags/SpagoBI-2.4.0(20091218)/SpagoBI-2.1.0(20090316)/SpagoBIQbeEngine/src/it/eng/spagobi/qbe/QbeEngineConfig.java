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
package it.eng.spagobi.qbe;

import it.eng.qbe.datasource.DataSourceCache;
import it.eng.qbe.locale.IQbeMessageHelper;
import it.eng.qbe.model.io.IDataMartModelRetriever;
import it.eng.qbe.model.io.IQueryPersister;
import it.eng.qbe.model.io.LocalFileSystemDataMartModelRetriever;
import it.eng.qbe.naming.NamingStrategy;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.qbe.commons.datasource.QbeDataSourceCache;
import it.eng.spagobi.qbe.commons.naming.QbeNamingStrategy;
import it.eng.spagobi.qbe.commons.urlgenerator.IQbeUrlGenerator;
import it.eng.spagobi.qbe.commons.urlgenerator.PortletQbeUrlGenerator;
import it.eng.spagobi.qbe.commons.urlgenerator.WebQbeUrlGenerator;
import it.eng.spagobi.services.common.EnginConf;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.file.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;



/**
 * The Class QbeEngineConf.
 * 
 * @author Andrea Gioia
 */
public class QbeEngineConfig {
	
	
	private EnginConf engineConfig;
	
	private static QbeEngineConfig instance = null;
	
	public static String QBE_MODE = "QBE_MODE";
	public static String QBE_DATAMART_DIR = "QBE_DATAMART_DIR";
	public static String QBE_DATAMART_RETRIVER = "QBE_DATAMART_RETRIVER";	
	public static String SPAGOBI_SERVER_URL = "SPAGOBI_SERVER_URL";
	public static String DEFAULT_SPAGOBI_SERVER_URL = "http://localhost:8080/SpagoBI";
	
	
	private static transient Logger logger = Logger.getLogger(QbeEngineConfig.class);
	
	
	public static QbeEngineConfig getInstance(){
		if(instance==null) {
			instance = new QbeEngineConfig();
		}
		return instance;
	}
	
	
	/** The locale. */
	private Locale locale = null;
	
	/** The config. */
	private SourceBean config = null;	
	
	/**
	 * Instantiates a new qbe engine conf.
	 */
	private QbeEngineConfig() {
		
		setEngineConfig( EnginConf.getInstance() );
		
		// deprecated
		config = ConfigSingleton.getInstance();	
	}
	
	public SourceBean getConfigSourceBean() {
		return getEngineConfig().getConfig();
	}
	
	// core settings
	
	public File getQbeDataMartDir() {
		File qbeDataMartDir;
		
		qbeDataMartDir = null;
		
		String property = getProperty( QBE_DATAMART_DIR );
		if( property != null ) {
			String baseDirStr = getEngineResourcePath();
			File baseDir = new File(baseDirStr);																			
			if( !FileUtils.isAbsolutePath( property ) )  {
				property = baseDir + System.getProperty("file.separator") + property;
				qbeDataMartDir = new File(property);
			}
		} 
				
		return qbeDataMartDir;		
	}
	
	public IDataMartModelRetriever getDataMartModelRetriever() {		
		IDataMartModelRetriever dataMartModelRetriever = null;
		
		String property = getProperty( QBE_DATAMART_RETRIVER );
		if(property != null) {
			try {
				dataMartModelRetriever = (IDataMartModelRetriever)Class.forName( property ).newInstance();
				if(dataMartModelRetriever instanceof LocalFileSystemDataMartModelRetriever) {
					LocalFileSystemDataMartModelRetriever localFileSystemDataMartModelRetriever = (LocalFileSystemDataMartModelRetriever)dataMartModelRetriever;
					localFileSystemDataMartModelRetriever.setContextDir(getQbeDataMartDir());
				}
			} catch (Exception e) {
				logger.warn("Impossible to instatiate the specified datamartRetriver class [" + property + "]", e);
			}
			
		}
		
		return dataMartModelRetriever;
	}
	
	// engine settings
	
	public String getEngineResourcePath() {
		String path = null;
		if(getEngineConfig().getResourcePath() != null) {
			path = getEngineConfig().getResourcePath() + System.getProperty("file.separator") + "qbe";
		} else {
			path = ConfigSingleton.getRootPath() + System.getProperty("file.separator") + "resources" + System.getProperty("file.separator") + "qbe";
		}
		
		return path;
	}
	
	public boolean isWebModalityActive() {
		boolean isWebModalityActive;
		
		isWebModalityActive = true;
		
		String property = getProperty( QBE_MODE );
		if( property != null ) {
			isWebModalityActive = property.equalsIgnoreCase("WEB");
		} 
		
		
		return isWebModalityActive;		
	}
	
	
	
	// utils 
	
	private String getProperty(String propertName) {
		String propertyValue = null;		
		SourceBean sourceBeanConf;
		
		Assert.assertNotNull( getConfigSourceBean(), "Impossible to parse engine-config.xml file");
		
		sourceBeanConf = (SourceBean) getConfigSourceBean().getAttribute( propertName);
		if(sourceBeanConf != null) {
			propertyValue  = (String) sourceBeanConf.getCharacters();
			logger.debug("Configuration attribute [" + propertName + "] is equals to: [" + propertyValue + "]");
		}
		
		return propertyValue;		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public IQbeUrlGenerator getUrlGenerator() {
		IQbeUrlGenerator urlGenerator = null;
		if( isWebModalityActive() ) {
			urlGenerator = new WebQbeUrlGenerator();
		} else {
			urlGenerator = new PortletQbeUrlGenerator();
		}
		return urlGenerator;
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

	public EnginConf getEngineConfig() {
		return engineConfig;
	}

	public void setEngineConfig(EnginConf engineConfig) {
		this.engineConfig = engineConfig;
	}
	
	
}
