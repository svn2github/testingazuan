package it.eng.spagobi.meta.datamarttree.builder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import it.eng.qbe.datasource.DBConnection;
import it.eng.qbe.datasource.DriverManager;
import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.datasource.configuration.DAOException;
import it.eng.qbe.datasource.configuration.FileDataSourceConfiguration;
import it.eng.qbe.datasource.jpa.JPADataSource;
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


public class DatamartSrtuctureBuilder {

	private static Logger logger = LoggerFactory.getLogger(DatamartSrtuctureBuilder.class);
	private static final String jarPath = "resources//";
	
	/**
	 * Build the datamart structure: get the db connection, load the jar files and build the structure
	 * @return ViewModelStructure: the Model structure with the filters
	 */
	public static ViewModelStructure build(){
		logger.debug("IN: Building the Model Structure..");
		Map<String, Object> dataSourceProperties = new HashMap<String, Object>();
		dataSourceProperties.put("connection", buildDBConnection());
		dataSourceProperties.put("dblinkMap", new HashMap());
		
		ArrayList<String> dataSourceNames = new ArrayList<String>();
		dataSourceNames.add("foodmart");
		
		JPADataSource dataSource = (JPADataSource)getDataSource(dataSourceNames, dataSourceProperties);
		IModelStructure iDatamartModelStructure = dataSource.getModelStructure();
		logger.debug("OUT: ViewModelStructure built for the model names "+dataSourceNames);
		return new ViewModelStructure(iDatamartModelStructure, dataSource, getTreeFilters());
	}
	
	/**
	 * 
	 * @param modelNames
	 * @param dataSourceProperties
	 * @return
	 */
	public static IDataSource getDataSource(List<String> modelNames, Map<String, Object> dataSourceProperties) {
		logger.debug("IN:Getting the data source for the model names "+modelNames+"..");
		Bundle generatorBundle = Platform.getBundle("it.eng.spagobi.meta.datamartTree");
		String path = null; 
		File modelJarFile = null;
		List<File> modelJarFiles = new ArrayList<File>();
		FileDataSourceConfiguration configuration = null; 
		
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
			configuration = new FileDataSourceConfiguration(modelNames.get(i), modelJarFile);
			Iterator<String> it = dataSourceProperties.keySet().iterator();
			while(it.hasNext()) {
				String propertyName = it.next();
				configuration.loadDataSourceProperties().put(propertyName, dataSourceProperties.get(propertyName));
			}
		}
		logger.debug("OUT: Finish to load the data source for the model names "+modelNames+"..");
		return DriverManager.getDataSource(getDriverName(modelJarFile), configuration);
	}
	
	private static QbeTreeFilter getTreeFilters(){
		IQbeTreeEntityFilter entityFilter = null;
		IQbeTreeFieldFilter fieldFilter = null;
		entityFilter = new QbeTreeAccessModalityEntityFilter();
		entityFilter = new QbeTreeOrderEntityFilter(entityFilter);
		fieldFilter = new QbeTreeAccessModalityFieldFilter();
		fieldFilter = new QbeTreeOrderFieldFilter(fieldFilter);
		return new QbeTreeFilter(entityFilter, fieldFilter);
	}	
	
	private static DBConnection buildDBConnection(){
		DBConnection connection = new DBConnection();			
		connection.setName( "FoodMart" );
		connection.setDialect( "org.hibernate.dialect.MySQLInnoDBDialect" );			
		connection.setJndiName("java:comp/env/jdbc/foodmart");			
		connection.setDriverClass( "com.mysql.jdbc.Driver");			
		connection.setPassword( "paola" );
		connection.setUrl( "jdbc:mysql://localhost:3306/foodmart");
		connection.setUsername( "root" );	
		return connection;
	}	
	
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
