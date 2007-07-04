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
package it.eng.qbe.model;

import it.eng.qbe.datasource.HibernateDataSource;
import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.model.accessmodality.DataMartModelAccessModality;
import it.eng.qbe.model.structure.DataMartModelStructure;
import it.eng.qbe.model.structure.builder.BasicDataMartStructureBuilder;
import it.eng.qbe.utility.IDataMartModelRetriever;
import it.eng.qbe.utility.IQueryPersister;
import it.eng.qbe.utility.Logger;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spago.configuration.ConfigSingleton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class DataMartModel implements IDataMartModel {
	
	private String name = null;	
	private String label = null;	
	private String description = null;	
	private HibernateDataSource dataSource = null; 
	private DataMartModelStructure dataMartModelStructure = null;
	private DataMartModelAccessModality dataMartModelAccessModality = null;
	private Properties dataMartProperties = null;
	
	
	public DataMartModel(IDataSource dataSource){
		this.dataSource = (HibernateDataSource)dataSource;
		HibernateDataSource hibernateDataSource = (HibernateDataSource)dataSource;
		if(hibernateDataSource.getPath() != null && hibernateDataSource.getJndiDataSourceName() != null && hibernateDataSource.getDialect() != null)
			this.dataMartModelStructure = BasicDataMartStructureBuilder.buildDataMartStructure(dataSource);
		
		/*
		String fileName = "C:\\Prototipi\\SpagoBI-1.9.2-QbE\\webapps\\SpagoBIQbeEngine\\tmp\\datamarts\\foodmart2\\modalities.xml";
		File file = new File(fileName);		
		this.dataMartModelAccessModality = new DataMartModelAccessModality(file, "ProductManagerAnalysis");
		*/
		this.dataMartModelAccessModality = new DataMartModelAccessModality();
		
		this.dataMartProperties = new Properties();
		
	}
	
	/**
	 * @param path: The path of the datamart
	 * @param jndiDataSourceName: the name of the jndi datasource
	 * @param dialect: the dialect to use
	 * 
	 * @deprecated use <code>new DataMartModel(new HibernateDataSource(path, jndiDataSourceName, dialect))</code>
	 */
	public DataMartModel(String path, String jndiDataSourceName, String dialect){		
		dataSource = new HibernateDataSource(path, jndiDataSourceName, dialect);
		if(path != null && jndiDataSourceName != null && dialect != null)
			this.dataMartModelStructure = BasicDataMartStructureBuilder.buildDataMartStructure(dataSource);
		/*
		String fileName = "C:\\Prototipi\\SpagoBI-1.9.2-QbE\\webapps\\SpagoBIQbeEngine\\tmp\\datamarts\\foodmart2\\modalities.xml";
		File file = new File(fileName);
		this.dataMartModelAccessModality = new DataMartModelAccessModality(file, "ProductManagerAnalysis");
		*/
		this.dataMartProperties = new Properties();
		this.dataMartModelAccessModality = new DataMartModelAccessModality();
	}
	
	
	//////////////////////////////////////////////////////////////////////////
	/// Query Persistence Handling methods
	/////////////////////////////////////////////////////////////////////////
		
	/**
	 * This method is responsible to persist the Object wizObj using the IQueryPersister 
	 * @param wizObj
	 */
	public void persistQueryAction(ISingleDataMartWizardObject wizObj){
		try{
			dataSource.getQueryPersister().persist(this, wizObj);
		}catch (Exception e) {
			Logger.error(DataMartModel.class, e);
		}
	}
	
	/**
	 * This method retrieve all queries for a datamart model
	 * @return a List of ISingleDataMartWizardObject that are all queries for a given datamart
	 */
	public List getQueries(){
		List l = new ArrayList();
		try{
			l = dataSource.getQueryPersister().loadAllQueries(this);
		}catch (Exception e) {
			Logger.error(DataMartModel.class, e);
		}
		return l;
	}
	
	/**
	 * This method retrieve the query related with the datamart model with given queryId
	 * @param queryId: The identifier of the query to get
	 * @return ISingleDataMartWizardObject the object representing the query
	 */
	public ISingleDataMartWizardObject getQuery(String queryId){
		ISingleDataMartWizardObject  query= null;
		try{
			query = dataSource.getQueryPersister().load(this, queryId);
		}catch (Exception e) {
			Logger.error(DataMartModel.class, e);
		}
		return query;
	}
	
	
	
	//////////////////////////////////////////////////////////////////////////
	/// Access methds
	/////////////////////////////////////////////////////////////////////////
	
	
	/**
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}		
	
	
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////
	/// METODI DEPRECATI
	//////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * This methos is responsible to retrieve the phisical jar file wich contains the datamart
	 * @return the hibernate Configuration
	 * 
	 * @deprecated use <code>getDataSource().getJarFile()</code>
	 */
	public File getJarFile(){
		return dataSource.getJarFile();
	}
	
	/**
	 * @deprecated use <code>getDataSource().getJarFile()</code>
	 */
	public List getViewJarFiles(){
		return dataSource.getViewJarFiles();
	}
	
	/**
	 * This methos is responsible to create the Hibernate Session Factory Object related to the
	 * datamart model
	 * @return the hibernate Configuration
	 * 
	 * @deprecated use <code>getDataSource().createSessionFactory()</code>
	 */
	public SessionFactory createSessionFactory(){		
		return dataSource.getSessionFactory();	
	}
	
	/**
	 * @deprecated use <code>getDataSource().getHibernateConfiguration(jarFile)</code>
	 */
	public Configuration getHibernateConfiguration(File jarFile) {
		return dataSource.getHibernateConfiguration(jarFile);
	}
	
	/**
	 * @deprecated use <code>getDataSource().initHibernateConfiguration(jarFile)</code>
	 */
	public Configuration initHibernateConfiguration(File jarFile) {
		return dataSource.initHibernateConfiguration(jarFile);
	}
	
	
	
	public IDataMartModelRetriever getDataMartModelRetriever() throws Exception {		
		String dataMartModelRetrieverClassName = (String)ConfigSingleton.getInstance().getAttribute("QBE.DATA-MART-MODEL-RETRIEVER.className");
		IDataMartModelRetriever dataMartModelRetriever = (IDataMartModelRetriever)Class.forName(dataMartModelRetrieverClassName).newInstance();
		return dataMartModelRetriever;
	}
	
	/**
	 * @return the IQueryPersister object reading concrete implementation class from the property 
	 * QBE.QUERY-PERSISTER.className in qbe.xml file
	 * 
	 */
	public IQueryPersister getQueryPersister() throws Exception{
		String queryPersisterClass = (String)ConfigSingleton.getInstance().getAttribute("QBE.QUERY-PERSISTER.className");
		IQueryPersister queryPersister = (IQueryPersister)Class.forName(queryPersisterClass).newInstance();
		return queryPersister;
	}
	
	/**
	 * This method update the Thread Context ClassLoader adding to the class loader the jarFile
	 * @param jarFile 
	 * 
	 * @deprecated use <code>HibernateDataSource.updateCurrentClassLoader(jarFile)</code>
	 */
	public static void updateCurrentClassLoader(File jarFile){
		HibernateDataSource.updateCurrentClassLoader(jarFile);
	}
	
	/**
	 * This method retrieve the jarFile of the datamart and update the Thread Context ClassLoader adding this jar 
	 * @param jarFile
	 * 
	 * @deprecated use <code>getDataSource().updateCurrentClassLoader()</code>
	 */
	public void updateCurrentClassLoader(){
		dataSource.updateCurrentClassLoader();
	}	

	
	/**
	 * @return dialect
	 * 
	 * @deprecated use <code>getDataSource().getDialect()</code>
	 */
	public String getDialect() {
		return dataSource.getDialect();
	}

	/**
	 * @param dialect
	 * 
	 * @deprecated use <code>getDataSource().setDialect(dialect)</code>
	 */
	public void setDialect(String dialect) {
		dataSource.setDialect(dialect);
	}

	/**
	 * @return jndiDataSourceName
	 * 
	 * @deprecated use <code>getDataSource().getJndiDataSourceName()</code>
	 */
	public String getJndiDataSourceName() {
		return dataSource.getJndiDataSourceName();
	}

	/**
	 * @param jndiDataSourceName
	 * 
	 * @deprecated use <code>getDataSource().setJndiDataSourceName(jndiDataSourceName)</code>
	 */
	public void setJndiDataSourceName(String jndiDataSourceName) {
		dataSource.setJndiDataSourceName(jndiDataSourceName);
	}

	/**
	 * @return path
	 * 
     * @deprecated use <code>getDataSource().getPath()</code>
	 */
	public String getPath() {
		return dataSource.getPath();
	}

	/**
	 * @param path
	 * 
	 * @deprecated use <code>getDataSource().setPath(path)</code>
	 */
	public void setPath(String path) {
		dataSource.setPath(path);
	}
	
	/**
	 * @deprecated use <code>getDataSource().getHibCfg()</code>
	 */
	public Configuration getHibCfg() {
		return dataSource.getHibCfg();
	}

	/**
	 * @deprecated use <code>getDataSource().setHibCfg(hibCfg)</code>
	 */
	public void setHibCfg(Configuration hibCfg) {
		dataSource.setHibCfg(hibCfg);
	}

	/**
	 * @deprecated use <code>getDataSource().isClassLoaderExtended()</code>
	 */
	public boolean isClassLoaderExtended() {
		return dataSource.isClassLoaderExtended();
	}

	/**
	 * @deprecated use <code>getDataSource().setClassLoaderExtended(classLoaderExtended)</code>
	 */
	public void setClassLoaderExtended(boolean classLoaderExtended) {
		dataSource.setClassLoaderExtended(classLoaderExtended);
	}

	/**
	 * @deprecated use <code>getDataSource().getAlreadyAddedView()</code>
	 */
	public List getAlreadyAddedView() {
		return dataSource.getAlreadyAddedView();
	}

	/**
	 * @deprecated use <code>getDataSource().setAlreadyAddedView(alreadyAddedView)</code>
	 */
	public void setAlreadyAddedView(List alreadyAddedView) {
		dataSource.setAlreadyAddedView(alreadyAddedView);
	}


	public DataMartModelStructure getDataMartModelStructure() {
		return dataMartModelStructure;
	}


	public void setDataMartModelStructure(
			DataMartModelStructure dastaMartModelStructure) {
		this.dataMartModelStructure = dastaMartModelStructure;
	}


	public HibernateDataSource getDataSource() {
		return dataSource;
	}


	public void setDataSource(HibernateDataSource dataSource) {
		this.dataSource = dataSource;
	}


	public IStatement createStatement() {
		return new HQLStatement(this);
	}


	public IStatement createStatement(IQuery query) {
		return new HQLStatement(this, query);
	}





	public DataMartModelAccessModality getDataMartModelAccessModality() {
		return dataMartModelAccessModality;
	}





	public void setDataMartModelAccessModality(
			DataMartModelAccessModality dataMartModelAccessModality) {
		this.dataMartModelAccessModality = dataMartModelAccessModality;
	}

	public Properties getDataMartProperties() {
		return dataMartProperties;
	}

	public void setDataMartProperties(Properties dataMartProperties) {
		this.dataMartProperties = dataMartProperties;
	}
}
