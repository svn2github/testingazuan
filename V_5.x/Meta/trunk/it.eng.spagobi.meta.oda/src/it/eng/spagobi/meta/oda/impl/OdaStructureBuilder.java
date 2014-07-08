/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.oda.impl;

import it.eng.qbe.datasource.DriverManager;
import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.datasource.configuration.CompositeDataSourceConfiguration;
import it.eng.qbe.datasource.configuration.FileDataSourceConfiguration;
import it.eng.qbe.datasource.configuration.dao.DAOException;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

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
	 * @param persistenceUnitNames the name of the models
	 * @param dataSourceProperties the data source properties
	 * @param path path to the of the jar files
	 * @return The IDataSource
	 */
	public static IDataSource getDataSourceSingleModel(List<String> persistenceUnitNames, Map<String, Object> dataSourceProperties, String path) {
		logger.debug("IN: Getting the data source for the model names "+persistenceUnitNames+"..");
		String modelName = persistenceUnitNames.get(0);
		if(modelName.lastIndexOf("_") != -1) {
			modelName = modelName.substring(0, modelName.lastIndexOf("_"));
		}
		String f = path.substring(0, path.indexOf("dist"))+modelName+File.separator+"dist"+File.separator+"datamart.jar";

		File modelJarFile = null;
		List<File> modelJarFiles = new ArrayList<File>();
		CompositeDataSourceConfiguration compositeConfiguration = new CompositeDataSourceConfiguration();
		compositeConfiguration.loadDataSourceProperties().putAll( dataSourceProperties);


		modelJarFile = new File(f);
		modelJarFiles.add(modelJarFile);
		compositeConfiguration.addSubConfiguration(new FileDataSourceConfiguration(persistenceUnitNames.get(0), modelJarFile));

		logger.debug("OUT: Finish to load the data source for the model names "+persistenceUnitNames+"..");
		IDataSource ds = DriverManager.getDataSource(getDriverName(modelJarFile), compositeConfiguration, false); 
		return ds;
	}

	/**
	 * Get the driver name (hibernate or jpa). It checks if the passed jar file contains the persistence.xml
	 * in the META-INF folder
	 * @param jarFile a jar file with the model definition
	 * @return jpa if the persistence provder is JPA o hibernate otherwise
	 */
	private static String getDriverName(File jarFile){
		logger.debug("IN: Check the driver name. Looking if "+jarFile+" is a jpa jar file..");
		JarInputStream zis;
		JarEntry zipEntry;
		String dialectName;
		boolean isJpa = false;

		try {
			FileInputStream fis = new FileInputStream(jarFile);
			zis = new JarInputStream(fis);
			while((zipEntry=zis.getNextJarEntry())!=null){
				logger.debug("Zip Entry is [{}]",zipEntry.getName());
				if(zipEntry.getName().equals("META-INF/persistence.xml") ){
					isJpa = true;
					break;
				}
				zis.closeEntry();
			}
			zis.close();
			fis.close();
			if(isJpa){
				dialectName = "jpa";
			} else{
				dialectName = "hibernate";
			}
		} catch (Throwable t) {
			logger.error("Impossible to read jar file [" + jarFile + "]",t);
			throw new DAOException("Impossible to read jar file [" + jarFile + "]");
		} 


		logger.debug("OUT: "+jarFile+" has the dialect: "+dialectName);
		return dialectName+"_with_cl";
	}



}
