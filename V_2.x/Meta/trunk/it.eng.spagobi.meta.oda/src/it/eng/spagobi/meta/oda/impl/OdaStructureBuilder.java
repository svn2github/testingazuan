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
package it.eng.spagobi.meta.oda.impl;

import it.eng.qbe.datasource.DriverManager;
import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.datasource.configuration.CompositeDataSourceConfiguration;
import it.eng.qbe.datasource.configuration.DAOException;
import it.eng.qbe.datasource.configuration.FileDataSourceConfiguration;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
public class OdaStructureBuilder {

	private static Logger logger = LoggerFactory.getLogger(OdaStructureBuilder.class);

	
	/**
	 * Get the data source: for each model name, create a file configuration.. Get the driver name (jpa or hibernate)
	 * @param modelNames the name of the models
	 * @param dataSourceProperties the data source properties
	 * @param path path to the of the jar files
	 * @return The IDataSource
	 */
	public static IDataSource getDataSource(List<String> modelNames, Map<String, Object> dataSourceProperties, String path) {
		logger.debug("IN: Getting the data source for the model names "+modelNames+"..");
		File modelJarFile = null;
		List<File> modelJarFiles = new ArrayList<File>();
		CompositeDataSourceConfiguration compositeConfiguration = new CompositeDataSourceConfiguration();
		compositeConfiguration.loadDataSourceProperties().putAll( dataSourceProperties);
		
		for(int i = 0; i < modelNames.size(); i++) {
			modelJarFile = new File(path+modelNames.get(i)+File.separator+"datamart.jar");
			modelJarFiles.add(modelJarFile);
			compositeConfiguration.addSubConfiguration(new FileDataSourceConfiguration(modelNames.get(i), modelJarFile));
		}
		logger.debug("OUT: Finish to load the data source for the model names "+modelNames+"..");
		return DriverManager.getDataSource(getDriverName(modelJarFile), compositeConfiguration);
	}
	
	/**
	 * Get the data source: for each model name, create a file configuration.. Get the driver name (jpa or hibernate)
	 * @param modelNames the name of the models
	 * @param dataSourceProperties the data source properties
	 * @return The IDataSource
	 */
	public static IDataSource getDataSource(List<String> modelNames, Map<String, Object> dataSourceProperties) {
		String jarPath = "resources//JPA//";
		String boundleName = "it.eng.spagobi.meta.querybuilder";
		Bundle generatorBundle = Platform.getBundle(boundleName);
		String path = null; 
		logger.debug("IN:Getting the path to the models..");
		try {
			IPath ipath = new Path(Platform.asLocalURL(generatorBundle.getEntry(jarPath)).getPath());
			path = ipath.toString();
		} catch (IOException e) {
			logger.error("Error loading the datamart file",e);
			throw new SpagoBIServiceException("Error loading the datamart file",e);
		}
		logger.debug("OUT: Path loaded..");
		return getDataSource(modelNames, dataSourceProperties, path);
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
