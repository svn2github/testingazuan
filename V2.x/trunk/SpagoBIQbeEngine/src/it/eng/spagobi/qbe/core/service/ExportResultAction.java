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
package it.eng.spagobi.qbe.core.service;

import it.eng.qbe.model.XIStatement;
import it.eng.qbe.newexport.Field;
import it.eng.qbe.newexport.HqlToSqlQueryRewriter;
import it.eng.qbe.newexport.ReportRunner;
import it.eng.qbe.newexport.SQLFieldsReader;
import it.eng.qbe.newexport.TemplateBuilder;
import it.eng.qbe.newquery.SelectField;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.qbe.commons.exception.QbeEngineException;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.engines.SpagoBIEngineException;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceExceptionHandler;
import it.eng.spagobi.utilities.mime.MimeUtils;


import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;



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
		XIStatement statement = null;
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
			Assert.assertNotNull(getEngineInstance().getQuery(), "Query object cannot be null in oder to execute " + this.getActionName() + " service");
			Assert.assertTrue(getEngineInstance().getQuery().isEmpty() == false, "Query object cannot be empty in oder to execute " + this.getActionName() + " service");
					
			Assert.assertNotNull(mimeType, "Input parameter [" + MIME_TYPE + "] cannot be null in oder to execute " + this.getActionName() + " service");		
			Assert.assertTrue( MimeUtils.isValidMimeType( mimeType ) == true, "[" + mimeType + "] is not a valid value for " + MIME_TYPE + " parameter");
			
			Assert.assertNotNull(responseType, "Input parameter [" + RESPONSE_TYPE + "] cannot be null in oder to execute " + this.getActionName() + " service");		
			Assert.assertTrue( RESPONSE_TYPE_INLINE.equalsIgnoreCase(responseType) || RESPONSE_TYPE_ATTACHMENT.equalsIgnoreCase(responseType), "[" + responseType + "] is not a valid value for " + RESPONSE_TYPE + " parameter");
			
			
			fileExtension = MimeUtils.getFileExtension( mimeType );
			writeBackResponseInline = RESPONSE_TYPE_INLINE.equalsIgnoreCase(responseType);
			
			statement = getEngineInstance().getDatamartModel().createXStatement( getEngineInstance().getQuery() );		
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
			
			Assert.assertTrue(getEngineInstance().getQuery().getSelectFields().size() == extractedFields.size(), 
					"The number of fields extracted from query resultset cannot be different from the number of fields specified into the query select clause");
			
			decorateExtractedFields( extractedFields );
			
			
			
			params = new HashMap();
			params.put("pagination", getPaginationParamVaue(mimeType) );
			
			templateBuilder = new TemplateBuilder(sqlQuery, extractedFields, params);
			templateContent = templateBuilder.buildTemplate();
			if( !"text/jrxml".equalsIgnoreCase( mimeType ) ) {
				try {
					reportFile = File.createTempFile("report", ".rpt");
				} catch (IOException ioe) {
					throw new SpagoBIEngineException("Impossible to create a temporary file to store the template generated on the fly", ioe);
				}
				
				setJasperClasspath();
				connection = session.connection();
				
				runner = new ReportRunner( );
				try {
					runner.run( templateContent, reportFile, mimeType, connection);
				}  catch (Exception e) {
					throw new SpagoBIEngineException("Impossible compile or to export the report", e);
				}
				
				try {				
					writeBackToClient(reportFile, null, writeBackResponseInline, "report." + fileExtension, mimeType);
				} catch (IOException ioe) {
					throw new SpagoBIEngineException("Impossible to write back the responce to the client", ioe);
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
		List selectedFields = getEngineInstance().getQuery().getSelectFields();
		Iterator selectedFieldsIterator = selectedFields.iterator();
		Iterator extractedFieldsIterator =  extractedFields.iterator();
		while( extractedFieldsIterator.hasNext() ) {
			Field exctractedField = (Field)extractedFieldsIterator.next();
			SelectField selectedField = (SelectField)selectedFieldsIterator.next();
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
