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

import it.eng.qbe.model.IStatement;
import it.eng.qbe.model.XIStatement;
import it.eng.qbe.newexport.Field;
import it.eng.qbe.newexport.HqlToSqlQueryRewriter;
import it.eng.qbe.newexport.ReportRunner;
import it.eng.qbe.newexport.SQLFieldsReader;
import it.eng.qbe.newexport.TemplateBuilder;
import it.eng.qbe.newquery.SelectField;
import it.eng.qbe.query.ISelectField;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.qbe.commons.exception.QbeEngineException;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;
import it.eng.spagobi.utilities.engines.EngineException;
import it.eng.spagobi.utilities.mime.MimeUtils;
import it.eng.spagobi.utilities.strings.StringUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;



// TODO: Auto-generated Javadoc
/**
 * The Class ExecuteQueryAction.
 */
public class ExportResultAction extends AbstractQbeEngineAction {
	
	// request
	public static final String MIME_TYPE = "MIME_TYPE";
	public static final String RESPONSE_TYPE = "RESPONSE_TYPE";
	
	// misc
	public static final String RESPONSE_TYPE_INLINE = "RESPONSE_TYPE_INLINE";
	public static final String RESPONSE_TYPE_ATTACHMENT = "RESPONSE_TYPE_ATTACHMENT";
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(ExportResultAction.class);
    
	
	public void service(SourceBean request, SourceBean response) throws EngineException  {				
		
		XIStatement xstatement = null;
		Vector fields = null;
		
		logger.debug("IN");
		
		super.service(request, response);	
		
		String mimeType = getAttributeAsString( MIME_TYPE );
		String responseType = getAttributeAsString( RESPONSE_TYPE );
		
		if( StringUtils.isEmpty( mimeType ) ||  !MimeUtils.isValidMimeType( mimeType )  ) {
			throw new EngineException("[" + mimeType + "] is not a valid value for " + MIME_TYPE + " parameter");
		}
		
		if( StringUtils.isEmpty( responseType ) 
				|| (!RESPONSE_TYPE_INLINE.equalsIgnoreCase(responseType) 
						&&  !RESPONSE_TYPE_ATTACHMENT.equalsIgnoreCase(responseType)) ) {
			throw new EngineException("[" + responseType + "] is not a valid value for " + RESPONSE_TYPE + " parameter");
		}
		
		String fileExtension = MimeUtils.getFileExtension( mimeType );
		boolean writeBackResponseInline = RESPONSE_TYPE_INLINE.equalsIgnoreCase(responseType);
		
		xstatement = getDatamartModel().createXStatement( getEngineInstance().getQuery() );		
		
		xstatement.setParameters( getEnv() );
		String hqlQuery = xstatement.getQueryString();
		
		Session session = getEngineInstance().getDatamartModel().getDataSource().getSessionFactory().openSession();	
		HqlToSqlQueryRewriter queryRewriter = new HqlToSqlQueryRewriter( session );
		String sqlQuery = queryRewriter.rewrite(hqlQuery);
		SQLFieldsReader fieldsReader= new SQLFieldsReader(sqlQuery, session.connection());
		try {
			fields = fieldsReader.readFields();
		} catch (Exception e) {
			throw new EngineException("Impossible to extract fields from query: " + hqlQuery, e);
		}
		
		List selectedFields = getEngineInstance().getQuery().getSelectFields();
		Iterator selectedFieldsIterator = selectedFields.iterator();
		Iterator extractedFieldsIterator =  fields.iterator();
		while( extractedFieldsIterator.hasNext() ) {
			Field exctractedField = (Field)extractedFieldsIterator.next();
			SelectField selectedField = (SelectField)selectedFieldsIterator.next();
			exctractedField.setAlias( selectedField.getAlias() );
			exctractedField.setVisible( selectedField.isVisible() );
		}
		
		TemplateBuilder templateBuilder = new TemplateBuilder(sqlQuery, fields, new HashMap());
		String templateContent = templateBuilder.buildTemplate();
		if( !"text/jrxml".equalsIgnoreCase( mimeType ) ) {
			try {
				File reportFile = File.createTempFile("report", ".rpt");
				setJasperClasspath();
				ReportRunner runner = new ReportRunner( );
				runner.run( templateContent, reportFile, mimeType, session.connection() );
				writeBackToClient(reportFile, writeBackResponseInline, "report." + fileExtension, mimeType);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				
				writeBackToClient(templateContent, writeBackResponseInline, "report." + fileExtension, mimeType);
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
		
		session.close();
			
		
		logger.debug("OUT");
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
