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
package it.eng.qbe.model;

import it.eng.qbe.bo.DatamartProperties;
import it.eng.qbe.bo.Formula;
import it.eng.qbe.conf.QbeCoreSettings;
import it.eng.qbe.datasource.BasicHibernateDataSource;
import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.datasource.IHibernateDataSource;
import it.eng.qbe.export.Field;
import it.eng.qbe.export.SQLFieldsReader;
import it.eng.qbe.log.Logger;
import it.eng.qbe.model.accessmodality.DataMartModelAccessModality;
import it.eng.qbe.model.io.IQueryPersister;
import it.eng.qbe.model.io.LocalFileSystemQueryPersister;
import it.eng.qbe.model.structure.DataMartField;
import it.eng.qbe.model.structure.DataMartModelStructure;
import it.eng.qbe.model.structure.builder.BasicDataMartStructureBuilder;
import it.eng.qbe.model.views.ViewBuilder;
import it.eng.qbe.newexport.HqlToSqlQueryRewriter;
import it.eng.qbe.newquery.Query;
import it.eng.qbe.newquery.SelectField;
import it.eng.qbe.query.IQuery;
import it.eng.qbe.utility.IDBSpaceChecker;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spagobi.utilities.sql.SqlUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Jar;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.types.Path;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;


// TODO: Auto-generated Javadoc
/**
 * The Class DataMartModel.
 */
public class DataMartModel implements IDataMartModel {
	
	/** The name. */
	private String name = null;	
	
	/** The label. */
	private String label = null;	
	
	/** The description. */
	private String description = null;	
	
	/** The data source. */
	private IHibernateDataSource dataSource = null; 
	
	/** The data mart model structure. */
	private DataMartModelStructure dataMartModelStructure = null;
	
	/** The data mart model access modality. */
	private DataMartModelAccessModality dataMartModelAccessModality = null;
	
	/** The data mart properties. */
	private Map dataMartProperties = null;
	
	
	
	
	
	/**
	 * Instantiates a new data mart model.
	 * 
	 * @param dataSource the data source
	 */
	public DataMartModel(IDataSource dataSource){
		this.dataSource = (IHibernateDataSource)dataSource;
		this.name = getDataSource().getDatamartName();
		this.description = getDataSource().getDatamartName();
		this.label = getDataSource().getDatamartName();
		
		this.dataMartModelStructure = BasicDataMartStructureBuilder.buildDataMartStructure(dataSource);		
	
		this.dataMartModelAccessModality = new DataMartModelAccessModality();
		
		this.dataMartProperties = new HashMap();		
	}
	
	
	
	
	/**
	 * Gets the properties.
	 * 
	 * @return the properties
	 */
	public DatamartProperties getProperties() {
		return  dataSource.getProperties();
	}
	
	/**
	 * Gets the formula.
	 * 
	 * @return the formula
	 */
	public Formula getFormula() {
		return dataSource.getFormula();
	}
	
	
	
	
	/**
	 * FIXME: It works only on qbe query.
	 * 
	 * @param name the name
	 * @param dmWizard the dm wizard
	 * 
	 * @throws Exception the exception
	 */
	public void addView(String name, Query query) throws Exception {	
		
		if ( !query.isEmpty() ){
			
			XIStatement xstatement = createXStatement( query );
			String hqlQuery = xstatement.getQueryString();
			Session session = getDataSource().getSessionFactory().openSession();	
			HqlToSqlQueryRewriter queryRewriter = new HqlToSqlQueryRewriter( session );
			String sqlQuery = queryRewriter.rewrite(hqlQuery);
					
			if (!SqlUtils.isSelectStatement(sqlQuery)){  
				throw new Exception("It's not possible change database status with qbe query");
			}
			
			
			Transaction tx = null;
			Statement s  = null;
			File thisTmpDir = null;
			
			try{
				tx = session.beginTransaction();
				Connection sqlConnection = session.connection();
				
				if (!checkSpace(sqlConnection)){
					throw new Exception("KO - Free Space in Database is not enough");
				}else{
				
				
					UUIDGenerator uuidGenerator = UUIDGenerator.getInstance();
					UUID uuidObj = uuidGenerator.generateTimeBasedUUID();
					String uuidGeneration = uuidObj.toString();
						
					File tmpDir = QbeCoreSettings.getInstance().getQbeTempDir();
						
					thisTmpDir = new File(tmpDir, uuidGeneration);
						
					String viewTemplateFileName =  QbeCoreSettings.getInstance().getQbeDataMartDir() 
													+ System.getProperty("file.separator") + getName() 
													+ System.getProperty("file.separator") + "view.template";
					
					File viewTemplateFile = new File(viewTemplateFileName);
					
					
					ViewBuilder viewBuilder = new ViewBuilder();
					viewBuilder.buildView(name, sqlQuery, sqlConnection, viewTemplateFile);
					
					Iterator it =  getDataSource().getSessionFactory().getAllClassMetadata().keySet().iterator();
					String className = "";
					if (it.hasNext()){
						className = (String)it.next();
					}
					
					String packageName = className.substring(0, className.lastIndexOf("."));
					SQLFieldsReader sqlFieldsReader = new SQLFieldsReader(sqlQuery, sqlConnection);
					
					System.out.println( "--> " + packageName + "." + name +  "//" + name);
					
					List columnNames = new ArrayList();
					List columnAliases = new ArrayList();
					List columnHibernateTypes = new ArrayList();
					
					Iterator queryFileds = query.getSelectFields().iterator();
					
					Vector columns = sqlFieldsReader.readFields();
					int i = 0;
					while(queryFileds.hasNext()) {
						SelectField field = (SelectField)queryFileds.next();
						Field column = (Field)columns.get(i++);
						
						
						DataMartField datamartField = getDataMartModelStructure().getField( field.getUniqueName() );
						columnHibernateTypes.add( datamartField.getType() );
						columnNames.add( column.getName() );
						columnAliases.add( field.getAlias() );
						System.out.println( "--> " + packageName + "." + name +  "/id." 
								+ Utils.asJavaPropertyIdentifier( column.getName() ) + "=" + field.getAlias());
						
					}
					
					
					viewReverseEngineering(name, packageName, thisTmpDir, columnNames, columnAliases, columnHibernateTypes);
					
					compileJavaClasses(thisTmpDir);
					
					
										
					String destJarFileDirName = QbeCoreSettings.getInstance().getQbeDataMartDir()  
												+ System.getProperty("file.separator") 
												+ getName();
					
					
					File destJarFileDir = new File(destJarFileDirName);
					if(!destJarFileDir.exists()) {
						destJarFileDir.mkdirs();
					}
					
					File destJarFile = new File(destJarFileDir.getAbsolutePath(), Utils.asJavaClassIdentifier(name) + "View.jar");
					
					createJar(thisTmpDir, destJarFile);
					
					
					tx.commit();
							
				}
			}catch (Exception e) {				
				if (tx != null) tx.rollback();
				throw e;				
			} finally{
				if (s != null){
					s.close();
				}
				if (session != null && session.isOpen())
					session.close();
				if (thisTmpDir != null){
					Utils.deleteDir(thisTmpDir);
				}
				
			}
			
			getDataSource().refreshSharedView(name);
			getDataSource().getSessionFactory();
			setDataMartModelStructure( BasicDataMartStructureBuilder.buildDataMartStructure( getDataSource() ) );
		}	
		
	}
	
	/**
	 * Compile java classes.
	 * 
	 * @param srcDir the src dir
	 */
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
	
	/**
	 * Creates the jar.
	 * 
	 * @param sourceDir the source dir
	 * @param destJarFile the dest jar file
	 */
	private void createJar(File sourceDir, File destJarFile) {
		Project project = new Project();
		Jar jarTask = new Jar();
		jarTask.setProject(project);
		
		jarTask.setBasedir(sourceDir);
		jarTask.setDestFile(destJarFile);
		
		jarTask.execute();
	}
	
	/**
	 * View reverse engineering.
	 * 
	 * @param name the name
	 * @param packageName the package name
	 * @param destDir the dest dir
	 * @param columnNames the column names
	 * @param columnHibernateTypes the column hibernate types
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void viewReverseEngineering(String name, String packageName, File destDir, List columnNames, List columnAliases, List columnHibernateTypes) throws IOException {
		
		String directoryOfPackage = Utils.packageAsDir(packageName);
		String destinationFoder = destDir.toString() + File.separator + directoryOfPackage + File.separator;
		File f = new File(destinationFoder);
		if (!f.exists()){
			f.mkdirs();
		}
		
		BufferedWriter bwHbm = new BufferedWriter(new FileWriter(destinationFoder + Utils.asJavaClassIdentifier(name)+".hbm.xml"));
		BufferedWriter bwJava = new BufferedWriter(new FileWriter(destinationFoder + Utils.asJavaClassIdentifier(name)+"Id.java"));
		BufferedWriter labelProps = new BufferedWriter(new FileWriter(destDir + File.separator + "label.properties"));
		BufferedWriter qbeProps = new BufferedWriter(new FileWriter(destDir + File.separator + "qbe.properties"));
		
		
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
		
		labelProps.write(packageName + "." + Utils.asJavaClassIdentifier(name) + "//" + Utils.asJavaClassIdentifier(name) + "=" + Utils.asJavaClassIdentifier(name) + "\n\n");
		qbeProps.write(packageName + "." + Utils.asJavaClassIdentifier(name) + "//" + Utils.asJavaClassIdentifier(name) + ".type=view");		
		
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
            
            labelProps.write(packageName + "." + Utils.asJavaClassIdentifier(name) + "/id." + javaFldName + "=" + (String)columnAliases.get(i) + "\n");
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
	
	/**
	 * Gets the java type for hib type.
	 * 
	 * @param hibType the hib type
	 * 
	 * @return the java type for hib type
	 */
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
	
	/**
	 * Check space.
	 * 
	 * @param aSqlConnection the a sql connection
	 * 
	 * @return true, if successful
	 */
	private boolean checkSpace(Connection aSqlConnection){
		
		if (QbeCoreSettings.getInstance().isSpaceCheckerEnabled()){
			try{
				IDBSpaceChecker spaceChecker = QbeCoreSettings.getInstance().getDbSpaceChecker();
				int freeSpace = spaceChecker.getPercentageOfFreeSpace(aSqlConnection);
				if (freeSpace < QbeCoreSettings.getInstance().getFreeSpaceLbLimit())
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
	 * This method is responsible to persist the Object wizObj using the IQueryPersister.
	 * 
	 * @param wizObj the wiz obj
	 */
	public void persistQueryAction(ISingleDataMartWizardObject wizObj){
		try{
			QbeCoreSettings.getInstance().getQueryPersister().persist(this, wizObj);
		}catch (Exception e) {
			Logger.error(DataMartModel.class, e);
		}
	}
	
	/**
	 * This method retrieve all queries for a datamart model.
	 * 
	 * @return a List of ISingleDataMartWizardObject that are all queries for a given datamart
	 */
	public List getQueries(){
		List l = new ArrayList();
		try{
			l = QbeCoreSettings.getInstance().getQueryPersister().loadAllQueries(this);
		}catch (Exception e) {
			Logger.error(DataMartModel.class, e);
		}
		return l;
	}
	
	/**
	 * Gets the private queries for.
	 * 
	 * @param userIdentifier the user identifier
	 * 
	 * @return the private queries for
	 */
	public List getPrivateQueriesFor(String userIdentifier) {
		List l = new ArrayList();
		try{
			IQueryPersister queryPersister = QbeCoreSettings.getInstance().getQueryPersister();
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
	 * This method retrieve the query related with the datamart model with given queryId.
	 * 
	 * @param queryId the query id
	 * 
	 * @return ISingleDataMartWizardObject the object representing the query
	 */
	public ISingleDataMartWizardObject getQuery(String queryId){
		ISingleDataMartWizardObject  query= null;
		try{
			query = QbeCoreSettings.getInstance().getQueryPersister().load(this, queryId);
		}catch (Exception e) {
			Logger.error(DataMartModel.class, e);
		}
		return query;
	}
	
	
	
	//////////////////////////////////////////////////////////////////////////
	/// Access methds
	/////////////////////////////////////////////////////////////////////////
	
	
	/**
	 * Gets the description.
	 * 
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 * 
	 * @param description the description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the label.
	 * 
	 * @return label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets the label.
	 * 
	 * @param label the label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}		
	
	
	
	
	
	
	
	
	
	
	

	
	


	/* (non-Javadoc)
	 * @see it.eng.qbe.model.IDataMartModel#getDataMartModelStructure()
	 */
	public DataMartModelStructure getDataMartModelStructure() {
		return dataMartModelStructure;
	}


	/**
	 * Sets the data mart model structure.
	 * 
	 * @param dastaMartModelStructure the new data mart model structure
	 */
	public void setDataMartModelStructure(
			DataMartModelStructure dastaMartModelStructure) {
		this.dataMartModelStructure = dastaMartModelStructure;
	}


	/* (non-Javadoc)
	 * @see it.eng.qbe.model.IDataMartModel#getDataSource()
	 */
	public IHibernateDataSource getDataSource() {
		return dataSource;
	}


	/**
	 * Sets the data source.
	 * 
	 * @param dataSource the new data source
	 */
	public void setDataSource(BasicHibernateDataSource dataSource) {
		this.dataSource = dataSource;
	}


	/* (non-Javadoc)
	 * @see it.eng.qbe.model.IDataMartModel#createStatement()
	 */
	public IStatement createStatement() {
		return new HQLStatement(this);
	}
	
	public XIStatement createXStatement() {
		return new XHQLStatement(this);
	}


	/* (non-Javadoc)
	 * @see it.eng.qbe.model.IDataMartModel#createStatement(it.eng.qbe.query.IQuery)
	 */
	public IStatement createStatement(IQuery query) {
		return new HQLStatement(this, query);
	}
	
	public XIStatement createXStatement(Query query) {
		return new XHQLStatement(this, query);
	}





	/* (non-Javadoc)
	 * @see it.eng.qbe.model.IDataMartModel#getDataMartModelAccessModality()
	 */
	public DataMartModelAccessModality getDataMartModelAccessModality() {
		return dataMartModelAccessModality;
	}





	/* (non-Javadoc)
	 * @see it.eng.qbe.model.IDataMartModel#setDataMartModelAccessModality(it.eng.qbe.model.accessmodality.DataMartModelAccessModality)
	 */
	public void setDataMartModelAccessModality(
			DataMartModelAccessModality dataMartModelAccessModality) {
		this.dataMartModelAccessModality = dataMartModelAccessModality;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.model.IDataMartModel#getDataMartProperties()
	 */
	
	public Map getDataMartProperties() {
		return dataMartProperties;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.model.IDataMartModel#setDataMartProperties(java.util.Properties)
	 */
	
	public void setDataMartProperties(Map dataMartProperties) {
		this.dataMartProperties = dataMartProperties;
	}
	
}
