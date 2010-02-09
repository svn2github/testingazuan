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
package it.eng.spagobi.engines.qbe.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Session;

import it.eng.qbe.export.Exporter;
import it.eng.qbe.export.Field;
import it.eng.qbe.export.HqlToSqlQueryRewriter;
import it.eng.qbe.export.ReportRunner;
import it.eng.qbe.export.SQLFieldsReader;
import it.eng.qbe.export.TemplateBuilder;
import it.eng.qbe.model.IStatement;
import it.eng.qbe.model.QbeDataSet;
import it.eng.qbe.query.DataMartSelectField;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.engines.qbe.QbeEngineConfig;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.engines.EngineConstants;
import it.eng.spagobi.utilities.engines.SpagoBIEngineException;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceExceptionHandler;
import it.eng.spagobi.utilities.mime.MimeUtils;



// TODO: Auto-generated Javadoc
/**
 * The Class ExecuteQueryAction.
 */
public class ExportResultAction extends AbstractQbeEngineAction {
	
	// INPUT PARAMETERS
	public static final String MIME_TYPE = "MIME_TYPE";
	public static final String RESPONSE_TYPE = "RESPONSE_TYPE";
	
	// misc
	public static final String RESPONSE_TYPE_INLINE = "RESPONSE_TYPE_INLINE";
	public static final String RESPONSE_TYPE_ATTACHMENT = "RESPONSE_TYPE_ATTACHMENT";
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(ExportResultAction.class);
    
	
	public void service(SourceBean request, SourceBean response) {				
		
		String responseType = null;
		boolean writeBackResponseInline = false;
		String mimeType = null;
		String fileExtension = null;
		IStatement statement = null;
		Session session = null;	
		Connection connection = null;
		HqlToSqlQueryRewriter queryRewriter = null;
		String hqlQuery = null;
		String sqlQuery = null;
		SQLFieldsReader fieldsReader = null;
		Vector extractedFields = null;
		Map params = null;
		TemplateBuilder templateBuilder = null;
		String templateContent = null;
		File reportFile = null;
		ReportRunner runner = null;
		
		logger.debug("IN");
		
		try {
			super.service(request, response);	
			
			mimeType = getAttributeAsString( MIME_TYPE );
			logger.debug(MIME_TYPE + ": " + mimeType);		
			responseType = getAttributeAsString( RESPONSE_TYPE );
			logger.debug(RESPONSE_TYPE + ": " + responseType);
					
			Assert.assertNotNull(getEngineInstance(), "It's not possible to execute " + this.getActionName() + " service before having properly created an instance of EngineInstance class");
			Assert.assertNotNull(getEngineInstance().getActiveQuery(), "Query object cannot be null in oder to execute " + this.getActionName() + " service");
			Assert.assertTrue(getEngineInstance().getActiveQuery().isEmpty() == false, "Query object cannot be empty in oder to execute " + this.getActionName() + " service");
					
			Assert.assertNotNull(mimeType, "Input parameter [" + MIME_TYPE + "] cannot be null in oder to execute " + this.getActionName() + " service");		
			Assert.assertTrue( MimeUtils.isValidMimeType( mimeType ) == true, "[" + mimeType + "] is not a valid value for " + MIME_TYPE + " parameter");
			
			Assert.assertNotNull(responseType, "Input parameter [" + RESPONSE_TYPE + "] cannot be null in oder to execute " + this.getActionName() + " service");		
			Assert.assertTrue( RESPONSE_TYPE_INLINE.equalsIgnoreCase(responseType) || RESPONSE_TYPE_ATTACHMENT.equalsIgnoreCase(responseType), "[" + responseType + "] is not a valid value for " + RESPONSE_TYPE + " parameter");
			
			
			fileExtension = MimeUtils.getFileExtension( mimeType );
			writeBackResponseInline = RESPONSE_TYPE_INLINE.equalsIgnoreCase(responseType);
			
			statement = getEngineInstance().getDatamartModel().createStatement( getEngineInstance().getActiveQuery() );		
			//logger.debug("Parametric query: [" + statement.getQueryString() + "]");
			
			statement.setParameters( getEnv() );
			hqlQuery = statement.getQueryString();
			logger.debug("Executable HQL query: [" + hqlQuery + "]");
					
			session = getEngineInstance().getDatamartModel().getDataSource().getSessionFactory().openSession();	
			queryRewriter = new HqlToSqlQueryRewriter( session );
			sqlQuery = queryRewriter.rewrite(hqlQuery);
			logger.debug("Executable SQL query: [" + sqlQuery + "]");
			
			logger.debug("Exctracting fields ...");
			fieldsReader = new SQLFieldsReader(sqlQuery, session.connection());
			try {
				extractedFields = fieldsReader.readFields();
			} catch (Exception e) {
				logger.debug("Impossible to extract fields from query");
				throw new SpagoBIEngineException("Impossible to extract fields from query: " + hqlQuery, e);
			}
			logger.debug("Fields extracted succesfully");
			
			Assert.assertTrue(getEngineInstance().getActiveQuery().getDataMartSelectFields(true).size() == extractedFields.size(), 
					"The number of fields extracted from query resultset cannot be different from the number of fields specified into the query select clause");
			
			decorateExtractedFields( extractedFields );
			
			
			
			params = new HashMap();
			params.put("pagination", getPaginationParamVaue(mimeType) );
			
			templateBuilder = new TemplateBuilder(sqlQuery, extractedFields, params);
			templateContent = templateBuilder.buildTemplate();
			if( !"text/jrxml".equalsIgnoreCase( mimeType ) ) {
				if( "application/vnd.ms-excel".equalsIgnoreCase( mimeType ) ) {
					RequestContainer requestContainer = RequestContainer.getRequestContainer();
					SessionContainer permSess = requestContainer.getSessionContainer();
					String language=(String)permSess.getAttribute("AF_LANGUAGE");
					String country=(String)permSess.getAttribute("AF_COUNTRY");
					//START PART from EXECUTEQUERYACTION
					UserProfile userProfile = (UserProfile)getEnv().get(EngineConstants.ENV_USER_PROFILE);
					QbeDataSet dataSet = null;
					IDataStore dataStore = null;
					Integer limit = 0;
					Integer start = 0;
					Integer maxSize = QbeEngineConfig.getInstance().getResultLimit();	
					boolean isMaxResultsLimitBlocking = QbeEngineConfig.getInstance().isMaxResultLimitBlocking();
					logger.debug("Executing query ...");
					dataSet = new QbeDataSet(statement);
					dataSet.setAbortOnOverflow(isMaxResultsLimitBlocking);
					
					Map userAttributes = new HashMap();
					UserProfile profile = (UserProfile)this.getEnv().get(EngineConstants.ENV_USER_PROFILE);
					Iterator it = profile.getUserAttributeNames().iterator();
					while(it.hasNext()) {
						String attributeName = (String)it.next();
						Object attributeValue = profile.getUserAttribute(attributeName);
						userAttributes.put(attributeName, attributeValue);
					}
					dataSet.addBinding("attributes", userAttributes);
					dataSet.addBinding("parameters", this.getEnv());
					dataSet.loadData(start, limit, (maxSize == null? -1: maxSize.intValue()));
					
					dataStore = dataSet.getDataStore();
					//END PART from EXECUTEQUERYACTION
					
					Exporter exp = new Exporter(dataStore);
					Workbook wb = exp.exportInExcel(language,country);
					
					File file = new File("workbook.xls");
					FileOutputStream stream = new FileOutputStream(file);
					wb.write(stream);
					stream.flush();
					stream.close();
					try {				
						writeBackToClient(file, null, writeBackResponseInline, "workbook.xls", mimeType);
					} catch (IOException ioe) {
						throw new SpagoBIEngineException("Impossible to write back the responce to the client", ioe);
					}	finally{
						if(file != null && file.exists()) {
							try {
								file.delete();
							} catch (Exception e) {
								logger.warn("Impossible to delete temporary file " + file, e);
							}
						}
					}
			
				}else{
					try {
						reportFile = File.createTempFile("report", ".rpt");
					} catch (IOException ioe) {
						throw new SpagoBIEngineException("Impossible to create a temporary file to store the template generated on the fly", ioe);
					}
					
					setJasperClasspath();
					connection = session.connection();
					
					runner = new ReportRunner( );
					Locale locale = this.getLocale();
					try {
						runner.run( templateContent, reportFile, mimeType, connection, locale);
					}  catch (Exception e) {
						throw new SpagoBIEngineException("Impossible compile or to export the report", e);
					}
					
					try {				
						writeBackToClient(reportFile, null, writeBackResponseInline, "report." + fileExtension, mimeType);
					} catch (IOException ioe) {
						throw new SpagoBIEngineException("Impossible to write back the responce to the client", ioe);
					}	
				}
			} else {
				try {				
					writeBackToClient(200, templateContent, writeBackResponseInline, "report." + fileExtension, mimeType);
				} catch (IOException e) {
					throw new SpagoBIEngineException("Impossible to write back the responce to the client", e);
				}
			}
		} catch (Throwable t) {			
			throw SpagoBIEngineServiceExceptionHandler.getInstance().getWrappedException(getActionName(), getEngineInstance(), t);
		} finally {
			
			try {
				// closing session will close also all connection created into this section
				session.close();
			} catch (Exception e) {
				logger.warn("Impossible to close the connection used to execute the report in " + getActionName() + " service", e);
			}
			
			if(reportFile != null && reportFile.exists()) {
				try {
					reportFile.delete();
				} catch (Exception e) {
					logger.warn("Impossible to delete temporary file " + reportFile, e);
				}
			}
		}
		
		logger.debug("OUT");
	}
	
	


	




	private void decorateExtractedFields(List extractedFields) {
		List selectedFields = getEngineInstance().getActiveQuery().getDataMartSelectFields(true);
		Iterator selectedFieldsIterator = selectedFields.iterator();
		Iterator extractedFieldsIterator =  extractedFields.iterator();
		while( extractedFieldsIterator.hasNext() ) {
			Field exctractedField = (Field)extractedFieldsIterator.next();
			DataMartSelectField selectedField = (DataMartSelectField)selectedFieldsIterator.next();
			exctractedField.setAlias( selectedField.getAlias() );
			exctractedField.setVisible( selectedField.isVisible() );
		}
	}
	
	private String getPaginationParamVaue(String mimeType) {
		if("application/pdf".equalsIgnoreCase(mimeType) || "application/rtf".equalsIgnoreCase(mimeType)) {
			return "false";
		} 

		return "true";
	}
	
	/**
	 * Sets the jasper classpath.
	 */
	private void setJasperClasspath(){
		// get the classpath used by JasperReprorts Engine (by default equals to WEB-INF/lib)
		String webinflibPath = ConfigSingleton.getInstance().getRootPath() + System.getProperty("file.separator") + "WEB-INF" + System.getProperty("file.separator") +"lib";
		//logger.debug("JasperReports lib-dir is [" + this.getClass().getName()+ "]");
		
		// get all jar file names in the jasper classpath
		//logger.debug("Reading jar files from lib-dir...");
		StringBuffer jasperReportClassPathStringBuffer  = new StringBuffer();
		File f = new File(webinflibPath);
		String fileToAppend = null;
		
		if (f.isDirectory()){
			String[] jarFiles = f.list();
			for (int i=0; i < jarFiles.length; i++){
				String namefile = jarFiles[i];
				if(!namefile.endsWith("jar"))
					continue; // the inclusion of txt files causes problems
				fileToAppend = webinflibPath + System.getProperty("file.separator")+ jarFiles[i];
				//logger.debug("Appending jar file [" + fileToAppend + "] to JasperReports classpath");
				jasperReportClassPathStringBuffer.append(fileToAppend);
				jasperReportClassPathStringBuffer.append(System.getProperty("path.separator"));  
			}
		}
		
		String jasperReportClassPath = jasperReportClassPathStringBuffer.toString();
		jasperReportClassPath = jasperReportClassPath.substring(0, jasperReportClassPath.length() - 1);
		
		// set jasper classpath property
		System.setProperty("jasper.reports.compile.class.path", jasperReportClassPath);
		//logger.debug("Set [jasper.reports.compile.class.path properties] to value [" + System.getProperty("jasper.reports.compile.class.path")+"]");	
		
		// append HibernateJarFile to jasper classpath
		if(jasperReportClassPath != null && !jasperReportClassPath.equalsIgnoreCase("")) 
			jasperReportClassPath += System.getProperty("path.separator");
		
		//jasperReportClassPath += jarFile.toString();		
		System.setProperty("jasper.reports.compile.class.path", jasperReportClassPath);
	
	}

}
