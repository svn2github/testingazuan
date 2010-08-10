
package it.eng.qbe.action;

import it.eng.qbe.export.Field;
import it.eng.qbe.export.SQLFieldsReader;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.utility.IDBSpaceChecker;
import it.eng.qbe.utility.Logger;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.ISelectField;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spago.base.ApplicationContainer;
import it.eng.spago.base.Constants;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.action.AbstractAction;
import it.eng.spago.dispatching.action.AbstractHttpAction;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Jar;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.selectors.FilenameSelector;
import org.apache.xerces.parsers.IntegratedParserConfiguration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;



/**
 * @author Andrea Zoppello
 * 
 * This action do the execution of the query represented by ISingleDataMartWizardObject in session
 * 
 * If ISingleDataMartWizardObject is configured to run the query composed automatically this action 
 * do some control on join conditions.
 *  
 */
public class CreateViewAction extends AbstractHttpAction {
	
	private SessionContainer getSessionContainer() {
		return getRequestContainer().getSessionContainer();
	}
	
	private ISingleDataMartWizardObject getDataMartWizard() {
		//return (ISingleDataMartWizardObject)getSessionContainer().getAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD);
		return Utils.getWizardObject(getSessionContainer());
	}
	
	private DataMartModel getDataMartModel() {
		return (DataMartModel)getSessionContainer().getAttribute("dataMartModel");
	}
	
	private static boolean isAbsolutePath(String path) {
		if(path == null) return false;
		return (path.startsWith("/") || path.startsWith("\\") || path.charAt(1) == ':');
	}
	
	private static String getQbeDataMartDir(File baseDir) {
		String qbeDataMartDir = null;
		qbeDataMartDir = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-MART_DIR.dir");
		if( !isAbsolutePath(qbeDataMartDir) )  {
			String baseDirStr = (baseDir != null)? baseDir.toString(): System.getProperty("user.home");
			qbeDataMartDir = baseDir + System.getProperty("file.separator") + qbeDataMartDir;
		}
		return qbeDataMartDir;
	}
	
	public void service(SourceBean request, SourceBean response) throws Exception {				
		String viewName = (String)request.getAttribute("VIEW_NAME");
		String qbeDataMartDir = getQbeDataMartDir(new File(it.eng.spago.configuration.ConfigSingleton.getInstance().getRootPath()));
		
		getResponseContainer().setAttribute(Constants.HTTP_RESPONSE_FREEZED, Boolean.TRUE);
		
		if ((getDataMartWizard().getSelectClause() != null) && (getDataMartWizard().getSelectClause().getSelectFields().size() > 0)){
			String query = getDataMartWizard().getExpertQueryDisplayed();
			if (query  == null){
				getDataMartWizard().setExpertQueryDisplayed(getDataMartWizard().getFinalSqlQuery(getDataMartModel()));
				query = getDataMartWizard().getExpertQueryDisplayed();
			}
					
			if (!(query.startsWith("select") || query.startsWith("SELECT"))){  
						throw new Exception("It's not possible change database status with qbe exepert query");
			}
			
			Session aSession = null;
			Transaction tx = null;
			Statement s  = null;
			String thisGenarationTmpDir = null;
			try{
				SessionFactory aSessionFactory = Utils.getSessionFactory(getDataMartModel(), ApplicationContainer.getInstance());
				aSession = aSessionFactory.openSession();
				tx = aSession.beginTransaction();
				Connection sqlConnection = aSession.connection();
				
				if (!checkSpace(sqlConnection)){
					
					getHttpResponse().getWriter().write("KO - Free Space in Database is not enough");
					return;
				}else{
				
				s = sqlConnection.createStatement();
				
				String viewTemplateFileName =  qbeDataMartDir + System.getProperty("file.separator") + getDataMartModel().getPath() + System.getProperty("file.separator")+"view.template";
				File tplViewFile = new File(viewTemplateFileName);
				
				String createViewDDLString = "CREATE VIEW {0} AS {1}";
				if (tplViewFile.exists()){
					Logger.debug(this.getClass(), " Using file "+viewTemplateFileName + " as template for creating view");
					StringBuffer aStringBuffer = new StringBuffer();
					try {
				        BufferedReader in = new BufferedReader(new FileReader(tplViewFile));
				        String str;
				        while ((str = in.readLine()) != null) {
				            aStringBuffer.append(str);
				        }
				        in.close();
				        createViewDDLString = aStringBuffer.toString();
				    } catch (IOException e) {
				    	e.printStackTrace();
				    }
				}else{
					Logger.debug(this.getClass(), " Using standardTemplate String ");
				}
				
				
				
				
				Object[] pars = new Object[2];
				pars[0] = viewName;
				pars[1] = query;
				
				String sqlCreateView = MessageFormat.format(createViewDDLString,pars);
				
//				sqlBuffer.append("CREATE VIEW "+viewName+"  AS ");
//				sqlBuffer.append(query);
				
				s.execute(sqlCreateView);
				
				Iterator it =  aSessionFactory.getAllClassMetadata().keySet().iterator();
				String className = "";
				if (it.hasNext()){
					className = (String)it.next();
				}
				
				String packageName = className.substring(0, className.lastIndexOf("."));
				UUIDGenerator uuidGenerator = UUIDGenerator.getInstance();
				UUID uuidObj = uuidGenerator.generateTimeBasedUUID();
				String uuidGeneration = uuidObj.toString();
				
				String javaTmpDir = System.getProperty("java.io.tmpdir");
				
				thisGenarationTmpDir = javaTmpDir + File.separator + uuidGeneration+ File.separator;
				
				String directoryOfPackage = Utils.packageAsDir(packageName);
				String destinationFoder = thisGenarationTmpDir + directoryOfPackage + File.separator;
				File f = new File(destinationFoder);
				if (!f.exists()){
					f.mkdirs();
				}
				
				BufferedWriter bwHbm = new BufferedWriter(new FileWriter(destinationFoder+ Utils.asJavaClassIdentifier(viewName)+".hbm.xml"));
				BufferedWriter bwJava = new BufferedWriter(new FileWriter(destinationFoder+ Utils.asJavaClassIdentifier(viewName)+"Id.java"));
				
				bwJava.write("package "+packageName+";\n");
				bwJava.write("import java.util.Date;\n");
				bwJava.write("import java.math.*;\n");
				bwJava.write("import java.lang.*;\n");
				
				bwJava.write("public class "+ Utils.asJavaClassIdentifier(viewName) + "Id implements java.io.Serializable {\n");
				bwHbm.write("<?xml version=\"1.0\"?>");
				bwHbm.write("<!DOCTYPE hibernate-mapping PUBLIC \"-//Hibernate/Hibernate Mapping DTD 3.0//EN\"\n");
				bwHbm.write("\"http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd\">\n");
				bwHbm.write("<hibernate-mapping>\n");
				bwHbm.write("<class name=\""+packageName+"."+Utils.asJavaClassIdentifier(viewName)+"\" table=\""+viewName+"\">\n");
				bwHbm.write("  <composite-id name=\"id\" class=\""+packageName+"."+Utils.asJavaClassIdentifier(viewName)+"Id\">\n");
				it = getDataMartWizard().getSelectClause().getSelectFields().iterator();
				ISelectField selField = null;
				SQLFieldsReader sqlFieldsReader = new SQLFieldsReader(query, sqlConnection);
				Vector flds = sqlFieldsReader.readFields();
				int i = -1;
				Field fld = null;
				while (it.hasNext()){
					selField = (ISelectField)it.next();
					i++;
					fld = (Field)flds.get(i);
					String javaFldName = Utils.asJavaPropertyIdentifier(fld.getName());
					bwHbm.write("<key-property name=\""+javaFldName+"\" type=\""+selField.getHibType()+"\">\n");
					bwHbm.write("<column name=\""+fld.getName()+"\"/>\n");
		            bwHbm.write("</key-property>\n");
		            
		            bwJava.write("private "+getJavaTypeForHibType(selField.getHibType()) + " " + javaFldName+";\n");
		            
		            bwJava.write("public "+getJavaTypeForHibType(selField.getHibType()) + " get"+Utils.capitalize(javaFldName)+"(){\n");
		            bwJava.write("    return this."+javaFldName+";\n");
		            bwJava.write("}\n");
		            
		            bwJava.write("public void  set"+Utils.capitalize(javaFldName)+"("+getJavaTypeForHibType(selField.getHibType())+" par ){\n");
		            bwJava.write("    this."+javaFldName+"=par;\n");
		            bwJava.write("}\n");
				}
				bwJava.write("}\n");
				bwHbm.write("  </composite-id>\n");
				bwHbm.write("</class>\n");
				bwHbm.write("</hibernate-mapping>\n");
				bwJava.flush();
				bwHbm.flush();
				bwJava.close();
				bwHbm.close();
				
				BufferedWriter bwJavaMain = new BufferedWriter(new FileWriter(destinationFoder+ Utils.asJavaClassIdentifier(viewName)+".java"));
				bwJavaMain.write("package "+packageName+";\n");
				bwJavaMain.write("import java.util.Date;\n");
				bwJavaMain.write("import java.math.*;\n");
				bwJavaMain.write("import java.lang.*;\n");
				
				bwJavaMain.write("public class "+ Utils.asJavaClassIdentifier(viewName) + " implements java.io.Serializable {\n");
				bwJavaMain.write("    private "+Utils.asJavaClassIdentifier(viewName) + "Id id;\n");
				
				bwJavaMain.write("	  public " + Utils.asJavaClassIdentifier(viewName)+ "("+Utils.asJavaClassIdentifier(viewName) + "Id id){\n");
				bwJavaMain.write("    	this.id=id;");
				bwJavaMain.write("	  }\n");
				
				
				bwJavaMain.write("	  public "+Utils.asJavaClassIdentifier(viewName) + "Id getId(){\n");
				bwJavaMain.write("    		return this.id;\n");
				bwJavaMain.write("	   }\n");
				
				bwJavaMain.write("	  public void setId("+Utils.asJavaClassIdentifier(viewName) + "Id id){\n");
				bwJavaMain.write("    	this.id=id;");
				bwJavaMain.write("	  }\n");
				
				bwJavaMain.write("}\n");
				bwJavaMain.flush();
				bwJavaMain.close();
				
				
				
				Project proj = new Project();
				Javac javacTask = new Javac();
				javacTask.setProject(proj);
				Path path = new Path(proj, thisGenarationTmpDir);  
				
				javacTask.setSrcdir(path);
				javacTask.setSource("1.4");
				javacTask.setDestdir(new File(thisGenarationTmpDir));
				javacTask.execute();
				
				
				
				String completeFileName = qbeDataMartDir + System.getProperty("file.separator") + getDataMartModel().getPath() + System.getProperty("file.separator") + Utils.asJavaClassIdentifier(viewName)+"View.jar";
				File destJar = new File(completeFileName);
				Jar jarTask = new Jar();
				jarTask.setProject(proj);
				jarTask.setBasedir(new File(thisGenarationTmpDir));
				jarTask.setDestFile(destJar);
				
				jarTask.execute();
				
				
				tx.commit();
				
				getDataMartModel().setHibCfg(null);
				getDataMartModel().setClassLoaderExtended(false);
				getDataMartModel().setAlreadyAddedView(new ArrayList());
				
				ApplicationContainer application = ApplicationContainer.getInstance();
				application.delAttribute(getDataMartModel().getPath());
				Utils.getSessionFactory(getDataMartModel(), application);
				
				getHttpResponse().getWriter().write("OK");
				}
			}catch (Throwable t) {
				getHttpResponse().getWriter().write("KO"+t.getMessage());
				t.printStackTrace();
				if (tx != null)
					tx.rollback();
				
			}finally{
				if (s != null){
					s.close();
				}
				if (aSession != null && aSession.isOpen())
					aSession.close();
				if (thisGenarationTmpDir != null){
					Utils.deleteDir(new File(thisGenarationTmpDir));
				}
				
			}
		}	
		
	}//service
	
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
		

//		Integer, int, long short          integer, long, short         Appropriate SQL type
//
//		char                              character                    char
//
//		java.math.BigDecimal              big_decimal                  NUMERIC, NUMBER
//
//		float, double                     float, double                float, double
//
//		java.lang.Boolean, boolean        boolean, yes_no,             boolean, int
//		                                  true_false
//		  
//		java.lang.string                  string                       varchar, varchar2
//
//		Very long strings                 text                         CLOB, TEXT
//
//		java.util.Date                    date, time, timestamp        DATE, TIME, TIMESTAMP
//
//		java.util.Calendar                calendar, calendar_date      TIMESTAMP, DATE
//
//		java.util.Locale                  locale                       varchar,varchar2
//
//		java.util.TimeZone                timezone                     varchar, varchar2
//
//		java.util Currency                Currency                     varchar, varchar2
//
//		java.sql.Clob                     clob                         CLOB
//
//		java.sql.Blob                     blob                         BLOB
//
//		Java object                       serializable                 binary field
//
//		byte array                        binary                       binary field
//
//		java.lang.Class                   class
		
	}
	

	private boolean checkSpace(Connection aSqlConnection){
		
		String makeCheck =(String) ConfigSingleton.getInstance().getAttribute("QBE.QBE-CHECK-SPACE-BEFORE-CREATEVIEW.check");
		
		if (makeCheck.equalsIgnoreCase("true")){
			Logger.debug(this.getClass(), " check of the space enabled...");
			String className = (String) ConfigSingleton.getInstance().getAttribute("QBE.QBE-CHECK-SPACE-BEFORE-CREATEVIEW.checkerClass");
			Logger.debug(this.getClass(), " check of the space with class ["+className+"]");
			String failsIfString = (String) ConfigSingleton.getInstance().getAttribute("QBE.QBE-CHECK-SPACE-BEFORE-CREATEVIEW.failIfSpaceLess");
			int failIfLess = Integer.parseInt(failsIfString);
			Logger.debug(this.getClass(), " fail if free space is less than ["+failIfLess+"]");
			try{
				IDBSpaceChecker spaceChecker = (IDBSpaceChecker)Class.forName(className).newInstance();
				int freeSpace = spaceChecker.getPercentageOfFreeSpace(aSqlConnection);
				if (freeSpace < failIfLess)
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
	
}
