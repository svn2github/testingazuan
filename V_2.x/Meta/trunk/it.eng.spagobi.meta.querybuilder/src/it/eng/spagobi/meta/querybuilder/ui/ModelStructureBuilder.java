/**

 SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.meta.querybuilder.ui;

import it.eng.qbe.datasource.DBConnection;
import it.eng.qbe.datasource.DriverManager;
import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.datasource.configuration.CompositeDataSourceConfiguration;
import it.eng.qbe.datasource.configuration.DAOException;
import it.eng.qbe.datasource.configuration.FileDataSourceConfiguration;
import it.eng.qbe.model.structure.IModelStructure;
import it.eng.qbe.model.structure.ViewModelStructure;
import it.eng.qbe.model.structure.filter.IQbeTreeEntityFilter;
import it.eng.qbe.model.structure.filter.IQbeTreeFieldFilter;
import it.eng.qbe.model.structure.filter.QbeTreeAccessModalityEntityFilter;
import it.eng.qbe.model.structure.filter.QbeTreeAccessModalityFieldFilter;
import it.eng.qbe.model.structure.filter.QbeTreeFilter;
import it.eng.qbe.model.structure.filter.QbeTreeOrderEntityFilter;
import it.eng.qbe.model.structure.filter.QbeTreeOrderFieldFilter;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alberto Ghedin (alberto.ghedin@eng.it)
 *
 */

public class ModelStructureBuilder {

	private static Logger logger = LoggerFactory.getLogger(ModelStructureBuilder.class);
	private static final String jarPath = "resources//JPA//";
	private static final String boundleName = "it.eng.spagobi.meta.querybuilder";
	
	public static IDataSource buildDataSource(){
		logger.debug("IN: Building the Model Structure..");
		Map<String, Object> dataSourceProperties = new HashMap<String, Object>();
		dataSourceProperties.put("connection", buildDBConnection());
		dataSourceProperties.put("dblinkMap", new HashMap());
		
		ArrayList<String> dataSourceNames = new ArrayList<String>();
		dataSourceNames.add("foodmart");
		
		IDataSource dataSource = getDataSource(dataSourceNames, dataSourceProperties);
		return dataSource;
	}
	
	public static ViewModelStructure buildModelView(IDataSource dataSource) {
		IModelStructure iDatamartModelStructure = dataSource.getModelStructure();
		return new ViewModelStructure(iDatamartModelStructure, dataSource, getTreeFilters());
	}
	
	/**
	 * Get the data source: for each model name, create a file configuration.. Get the driver name (jpa or hibernate)
	 * @param modelNames the name of the models
	 * @param dataSourceProperties the data source properties
	 * @return The IDataSource
	 */
	private static IDataSource getDataSource(List<String> modelNames, Map<String, Object> dataSourceProperties) {
		logger.debug("IN: Getting the data source for the model names "+modelNames+"..");
		Bundle generatorBundle = Platform.getBundle(boundleName);
		String path = null; 
		File modelJarFile = null;
		List<File> modelJarFiles = new ArrayList<File>();
		CompositeDataSourceConfiguration compositeConfiguration = new CompositeDataSourceConfiguration();
		compositeConfiguration.loadDataSourceProperties().putAll( dataSourceProperties);
		
		for(int i = 0; i < modelNames.size(); i++) {
			try {
				IPath ipath = new Path(Platform.asLocalURL(generatorBundle.getEntry(jarPath+modelNames.get(i))).getPath());
				path = ipath.toString();
			} catch (IOException e) {
				logger.error("Error loading the datamart file",e);
				throw new SpagoBIServiceException("Error loading the datamart file",e);
			}
			modelJarFile = new File(path+"datamart.jar");
			modelJarFiles.add(modelJarFile);
			compositeConfiguration.addSubConfiguration(new FileDataSourceConfiguration(modelNames.get(i), modelJarFile));
		}
		logger.debug("OUT: Finish to load the data source for the model names "+modelNames+"..");
		return DriverManager.getDataSource(getDriverName(modelJarFile), compositeConfiguration);
	}
	
	/**
	 * Build the tree filters (Order and visible filters for entity end fields)
	 * @return the filters
	 */
	private static QbeTreeFilter getTreeFilters(){
		IQbeTreeEntityFilter entityFilter = null;
		IQbeTreeFieldFilter fieldFilter = null;
		entityFilter = new QbeTreeAccessModalityEntityFilter();
		entityFilter = new QbeTreeOrderEntityFilter(entityFilter);
		fieldFilter = new QbeTreeAccessModalityFieldFilter();
		fieldFilter = new QbeTreeOrderFieldFilter(fieldFilter);
		return new QbeTreeFilter(entityFilter, fieldFilter);
	}	
	
	/**
	 * Initialize the db connection
	 * @return DBConnection
	 */
	private static DBConnection buildDBConnection(){
		DBConnection connection = new DBConnection();			
		connection.setName( "FoodMart" );
		connection.setDialect( "org.hibernate.dialect.MySQLDialect" );			
		connection.setDriverClass( "com.mysql.jdbc.Driver");			
		connection.setPassword( "mysql" );
		connection.setUrl( "jdbc:mysql://localhost:3306/foodmart");
		connection.setUsername( "root" );	
		return connection;
	}	
	
	/**
	 * Get the driver name (hibernate or jpa). It checks if the passed jar file contains the persistence.xml
	 * in the META-INF folder
	 * @param jarFile a jar file with the model definition
	 * @return jpa if the persistence provder is JPA o hibernate otherwise
	 */
	private static String getDriverName(File jarFile){
		logger.debug("IN: Check the driver name. Looking if "+jarFile+" is a jpa jar file..");
		ZipFile zipFile;
		ZipEntry zipEntry;
		String dialectName;
			
		try {
			zipFile = new ZipFile(jarFile);
		} catch (Throwable t) {
			logger.error("Impossible to read jar file [" + jarFile + "]",t);
			throw new DAOException("Impossible to read jar file [" + jarFile + "]");
		} 
		
		zipEntry = zipFile.getEntry("META-INF/persistence.xml");
		dialectName = (zipEntry!=null)? "jpa": "hibernate";
		logger.debug("OUT: "+jarFile+" has the dialect: "+dialectName);
		return dialectName;
	}
	
}
