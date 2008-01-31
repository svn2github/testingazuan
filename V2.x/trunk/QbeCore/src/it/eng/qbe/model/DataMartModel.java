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

import it.eng.qbe.bo.DatamartLabels;
import it.eng.qbe.bo.DatamartProperties;
import it.eng.qbe.bo.Formula;
import it.eng.qbe.conf.QbeConf;
import it.eng.qbe.datasource.BasicHibernateDataSource;
import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.datasource.IHibernateDataSource;
import it.eng.qbe.export.Field;
import it.eng.qbe.export.SQLFieldsReader;
import it.eng.qbe.locale.LocaleUtils;
import it.eng.qbe.log.Logger;
import it.eng.qbe.model.accessmodality.DataMartModelAccessModality;
import it.eng.qbe.model.io.IDataMartModelRetriever;
import it.eng.qbe.model.io.IQueryPersister;
import it.eng.qbe.model.io.LocalFileSystemQueryPersister;
import it.eng.qbe.model.structure.DataMartModelStructure;
import it.eng.qbe.model.structure.builder.BasicDataMartStructureBuilder;
import it.eng.qbe.model.views.ViewBuilder;
import it.eng.qbe.query.IQuery;
import it.eng.qbe.query.ISelectField;
import it.eng.qbe.utility.IDBSpaceChecker;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.SingleDataMartWizardObjectSourceBeanImpl;
import it.eng.spago.base.ApplicationContainer;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.utilities.sql.SqlUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.jar.JarFile;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Jar;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.types.Path;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;


public class DataMartModel implements IDataMartModel {
	
	private String name = null;	
	private String label = null;	
	private String description = null;	
	private IHibernateDataSource dataSource = null; 
	
	private DataMartModelStructure dataMartModelStructure = null;
	private DataMartModelAccessModality dataMartModelAccessModality = null;
	private Properties dataMartProperties = null;
	
	
	
	
	
	public DataMartModel(IDataSource dataSource){
		this.dataSource = (IHibernateDataSource)dataSource;
		this.name = getDataSource().getDatamartName();
		this.description = getDataSource().getDatamartName();
		this.label = getDataSource().getDatamartName();
		
		this.dataMartModelStructure = BasicDataMartStructureBuilder.buildDataMartStructure(dataSource);		
	
		this.dataMartModelAccessModality = new DataMartModelAccessModality();
		
		this.dataMartProperties = new Properties();		
	}
	
	
	public DatamartLabels getLabels() {
		return dataSource.getLabels();
	}
	
	public DatamartLabels getLabels(Locale locale) {
		DatamartLabels labels = (DatamartLabels)dataSource.getLabels(locale);
		if(labels == null) labels = getLabels();
		
		return labels;
	}
	
	public DatamartProperties getProperties() {
		return  dataSource.getProperties();
	}
	
	public Formula getFormula() {
		return dataSource.getFormula();
	}
	
	
	
	
	/**
	 * FIXME: It works only on qbe query
	 * 
	 * @param name
	 * @param query
	 * @throws Exception
	 */
	public void addView(String name, ISingleDataMartWizardObject dmWizard) throws Exception {	
		
		if ( !dmWizard.getQuery().isEmpty() ){
			
			
			String sqlQuery = dmWizard.getFinalSqlQuery(this);
			
					
			if (!SqlUtils.isSelectStatement(sqlQuery)){  
				throw new Exception("It's not possible change database status with qbe query");
			}
			
			Session aSession = null;
			Transaction tx = null;
			Statement s  = null;
			File thisTmpDir = null;
			
			try{
				SessionFactory aSessionFactory = getDataSource().getSessionFactory();
				aSession = aSessionFactory.openSession();
				tx = aSession.beginTransaction();
				Connection sqlConnection = aSession.connection();
				
				if (!checkSpace(sqlConnection)){
					throw new Exception("KO - Free Space in Database is not enough");
				}else{
				
				
					UUIDGenerator uuidGenerator = UUIDGenerator.getInstance();
					UUID uuidObj = uuidGenerator.generateTimeBasedUUID();
					String uuidGeneration = uuidObj.toString();
						
					File tmpDir = QbeConf.getInstance().getQbeTempDir();
						
					thisTmpDir = new File(tmpDir, uuidGeneration);
						
					String viewTemplateFileName =  QbeConf.getInstance().getQbeDataMartDir() 
													+ System.getProperty("file.separator") + getName() 
													+ System.getProperty("file.separator") + "view.template";
					
					File viewTemplateFile = new File(viewTemplateFileName);
					
					
					ViewBuilder viewBuilder = new ViewBuilder();
					viewBuilder.buildView(name, sqlQuery, sqlConnection, viewTemplateFile);
					
					Iterator it =  aSessionFactory.getAllClassMetadata().keySet().iterator();
					String className = "";
					if (it.hasNext()){
						className = (String)it.next();
					}
					String packageName = className.substring(0, className.lastIndexOf("."));
					SQLFieldsReader sqlFieldsReader = new SQLFieldsReader(sqlQuery, sqlConnection);
					
					List columnNames = new ArrayList();
					List columnHibernateTypes = new ArrayList();
					
					Iterator queryFileds = dmWizard.getQuery().getSelectFieldsIterator();
					
					Vector columns = sqlFieldsReader.readFields();
					int i = 0;
					while(queryFileds.hasNext()) {
						ISelectField filed = (ISelectField)queryFileds.next();
						Field column = (Field)columns.get(i++);
						
						columnHibernateTypes.add(filed.getType());
						columnNames.add(column.getName());					
					}
					
					viewReverseEngineering(name, packageName, thisTmpDir, columnNames, columnHibernateTypes);
					
					compileJavaClasses(thisTmpDir);
					
					
										
					String destJarFileDirName = QbeConf.getInstance().getQbeDataMartDir()  
												+ System.getProperty("file.separator") 
												+ getName();
					
					
					File destJarFileDir = new File(destJarFileDirName);
					if(!destJarFileDir.exists()) {
						destJarFileDir.mkdirs();
					}
					
					File destJarFile = new File(destJarFileDir.getAbsolutePath(), Utils.asJavaClassIdentifier(name) + "View.jar");
					
					createJar(thisTmpDir, destJarFile);
					
					
					tx.commit();
					
					getDataSource().refreshSharedView(name);
							
				}
			}catch (Throwable t) {
				t.printStackTrace();
				if (tx != null)
					tx.rollback();
				
			} finally{
				if (s != null){
					s.close();
				}
				if (aSession != null && aSession.isOpen())
					aSession.close();
				if (thisTmpDir != null){
					Utils.deleteDir(thisTmpDir);
				}
				
			}
		}	
		
	}
	
	private void compileJavaClasses(File srcDir) {
		Project project = new Project();
		Javac javacTask = new Javac();
		javacTask.setProject(project);
		
		Path path = new Path(project, srcDir.toString());  		
		javacTask.setSrcdir(path);
		javacTask.setSource("1.4");
		javacTask.setDestdir(srcDir);
		
		javacTask.execute();
	}
	
	private void createJar(File sourceDir, File destJarFile) {
		Project project = new Project();
		Jar jarTask = new Jar();
		jarTask.setProject(project);
		
		jarTask.setBasedir(sourceDir);
		jarTask.setDestFile(destJarFile);
		
		jarTask.execute();
	}
	
	private void viewReverseEngineering(String name, String packageName, File destDir, List columnNames, List columnHibernateTypes) throws IOException {
		
		String directoryOfPackage = Utils.packageAsDir(packageName);
		String destinationFoder = destDir.toString() + File.separator + directoryOfPackage + File.separator;
		File f = new File(destinationFoder);
		if (!f.exists()){
			f.mkdirs();
		}
		
		BufferedWriter bwHbm = new BufferedWriter(new FileWriter(destinationFoder+ Utils.asJavaClassIdentifier(name)+".hbm.xml"));
		BufferedWriter bwJava = new BufferedWriter(new FileWriter(destinationFoder+ Utils.asJavaClassIdentifier(name)+"Id.java"));
		BufferedWriter labelProps = new BufferedWriter(new FileWriter(destDir+ "label.properties"));
		BufferedWriter qbeProps = new BufferedWriter(new FileWriter(destDir+ "qbe.properties"));
		
		
		bwJava.write("package "+packageName+";\n");
		bwJava.write("import java.util.Date;\n");
		bwJava.write("import java.math.*;\n");
		bwJava.write("import java.lang.*;\n");				
		bwJava.write("public class "+ Utils.asJavaClassIdentifier(name) + "Id implements java.io.Serializable {\n");
		
		bwHbm.write("<?xml version=\"1.0\"?>");
		bwHbm.write("<!DOCTYPE hibernate-mapping PUBLIC \"-//Hibernate/Hibernate Mapping DTD 3.0//EN\"\n");
		bwHbm.write("\"http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd\">\n");
		bwHbm.write("<hibernate-mapping>\n");
		bwHbm.write("<class name=\""+packageName+"."+Utils.asJavaClassIdentifier(name)+"\" table=\""+name+"\">\n");
		bwHbm.write("  <composite-id name=\"id\" class=\""+packageName+"."+Utils.asJavaClassIdentifier(name)+"Id\">\n");
		
		labelProps.write("class." + packageName + "." + Utils.asJavaClassIdentifier(name) + "=" + Utils.asJavaClassIdentifier(name) + "\n\n");
		qbeProps.write(packageName + "." + Utils.asJavaClassIdentifier(name) + ".type=view");		
		
		for(int i = 0; i < columnNames.size(); i++) {
			String columnName = (String)columnNames.get(i);
			String columnHibernateType = (String)columnHibernateTypes.get(i);
			
			
			String javaFldName = Utils.asJavaPropertyIdentifier(columnName);
			bwHbm.write("<key-property name=\""+javaFldName+"\" type=\""+ columnHibernateType + "\">\n");
			bwHbm.write("<column name=\""+columnName+"\"/>\n");
            bwHbm.write("</key-property>\n");
            
            bwJava.write("private "+ getJavaTypeForHibType(columnHibernateType) + " " + javaFldName+";\n");		            
            
            bwJava.write("public "+ getJavaTypeForHibType(columnHibernateType) + " get"+Utils.capitalize(javaFldName)+"(){\n");
            bwJava.write("    return this."+javaFldName+";\n");
            bwJava.write("}\n");
            
            bwJava.write("public void  set"+Utils.capitalize(javaFldName)+"("+ getJavaTypeForHibType(columnHibernateType)+" par ){\n");
            bwJava.write("    this."+javaFldName+"=par;\n");
            bwJava.write("}\n");
            
            labelProps.write("field.id." + javaFldName + "=id." + javaFldName + "\n");
		}
		bwJava.write("}\n");
		
		bwHbm.write("  </composite-id>\n");
		bwHbm.write("</class>\n");
		bwHbm.write("</hibernate-mapping>\n");
		
		bwJava.flush();
		bwHbm.flush();
		bwJava.close();
		bwHbm.close();
		labelProps.flush();
		labelProps.close();
		qbeProps.flush();
		qbeProps.close();
		
		
		BufferedWriter bwJavaMain = new BufferedWriter(new FileWriter(destinationFoder+ Utils.asJavaClassIdentifier(name)+".java"));
		bwJavaMain.write("package "+packageName+";\n");
		bwJavaMain.write("import java.util.Date;\n");
		bwJavaMain.write("import java.math.*;\n");
		bwJavaMain.write("import java.lang.*;\n");
		
		bwJavaMain.write("public class "+ Utils.asJavaClassIdentifier(name) + " implements java.io.Serializable {\n");
		bwJavaMain.write("    private "+Utils.asJavaClassIdentifier(name) + "Id id;\n");
		
		bwJavaMain.write("	  public " + Utils.asJavaClassIdentifier(name)+ "("+Utils.asJavaClassIdentifier(name) + "Id id){\n");
		bwJavaMain.write("    	this.id=id;");
		bwJavaMain.write("	  }\n");
		
		
		bwJavaMain.write("	  public "+Utils.asJavaClassIdentifier(name) + "Id getId(){\n");
		bwJavaMain.write("    		return this.id;\n");
		bwJavaMain.write("	   }\n");
		
		bwJavaMain.write("	  public void setId("+Utils.asJavaClassIdentifier(name) + "Id id){\n");
		bwJavaMain.write("    	this.id=id;");
		bwJavaMain.write("	  }\n");
		
		bwJavaMain.write("}\n");
		bwJavaMain.flush();
		bwJavaMain.close();
	}
	
	public String getJavaTypeForHibType(String hibType){
		
		if (hibType.equalsIgnoreCase("integer")){
			return "Integer";
		}else if (hibType.equalsIgnoreCase("integer")){
			return "Long";
		}else if (hibType.equalsIgnoreCase("short")){
			return "Short";
		}else if (hibType.equalsIgnoreCase("character") || hibType.equalsIgnoreCase("string")){
			return "String";
		}else if (hibType.equalsIgnoreCase("boolean")){
			return "Boolean";
		}if (  hibType.equalsIgnoreCase("date") 
			|| hibType.equalsIgnoreCase("time") 
			|| hibType.equalsIgnoreCase("timestamp")){
			return "Date";
		}if (hibType.equalsIgnoreCase("big_decimal")){
			return "BigDecimal";
		}else{
			return "String";
		}		
	}
	
	private boolean checkSpace(Connection aSqlConnection){
		
		if (QbeConf.getInstance().isSpaceCheckerEnabled()){
			try{
				IDBSpaceChecker spaceChecker = QbeConf.getInstance().getDbSpaceChecker();
				int freeSpace = spaceChecker.getPercentageOfFreeSpace(aSqlConnection);
				if (freeSpace < QbeConf.getInstance().getFreeSpaceLbLimit())
					return false;
				else
					return true;
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
		}else{
			Logger.debug(this.getClass(), " check of the space disabled...");
			return true;
		}
	}
	
	
	
	/**
	 * This method is responsible to persist the Object wizObj using the IQueryPersister 
	 * @param wizObj
	 */
	public void persistQueryAction(ISingleDataMartWizardObject wizObj){
		try{
			QbeConf.getInstance().getQueryPersister().persist(this, wizObj);
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
			l = QbeConf.getInstance().getQueryPersister().loadAllQueries(this);
		}catch (Exception e) {
			Logger.error(DataMartModel.class, e);
		}
		return l;
	}
	
	public List getPrivateQueriesFor(String userIdentifier) {
		List l = new ArrayList();
		try{
			IQueryPersister queryPersister = QbeConf.getInstance().getQueryPersister();
			if(queryPersister instanceof LocalFileSystemQueryPersister) {
				LocalFileSystemQueryPersister localFileSystemQueryPersister = (LocalFileSystemQueryPersister)queryPersister;
				l = localFileSystemQueryPersister.getPrivateQueriesFor(this, userIdentifier);
			}
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
			query = QbeConf.getInstance().getQueryPersister().load(this, queryId);
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
	
	
	
	
	
	
	
	
	
	
	

	
	


	public DataMartModelStructure getDataMartModelStructure() {
		return dataMartModelStructure;
	}


	public void setDataMartModelStructure(
			DataMartModelStructure dastaMartModelStructure) {
		this.dataMartModelStructure = dastaMartModelStructure;
	}


	public IHibernateDataSource getDataSource() {
		return dataSource;
	}


	public void setDataSource(BasicHibernateDataSource dataSource) {
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
