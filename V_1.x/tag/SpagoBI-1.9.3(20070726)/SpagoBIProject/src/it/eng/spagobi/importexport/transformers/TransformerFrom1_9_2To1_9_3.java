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
package it.eng.spagobi.importexport.transformers;

import groovy.lang.Binding;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.dbaccess.sql.DataRow;
import it.eng.spagobi.bo.lov.LovDetailFactory;
import it.eng.spagobi.bo.lov.ScriptDetail;
import it.eng.spagobi.importexport.ITransformer;
import it.eng.spagobi.importexport.ImportExportConstants;
import it.eng.spagobi.managers.ScriptManager;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.PortletUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import sun.misc.BASE64Encoder;

public class TransformerFrom1_9_2To1_9_3 implements ITransformer {

	public byte[] transform(byte[] content, String pathImpTmpFolder, String archiveName) {
		try {
			TransformersUtilities.decompressArchive(pathImpTmpFolder, archiveName, content);
		} catch(Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), 
                                   "transform", "Error while decompressing 1.9.2 exported archive" + e);	
		}
		archiveName = archiveName.substring(0, archiveName.lastIndexOf('.'));

		adviseForBirtDocuments(pathImpTmpFolder, archiveName);
		arrangeSubobjects(pathImpTmpFolder, archiveName);
		buildCmsNodes(pathImpTmpFolder, archiveName);
		changeDatabase(pathImpTmpFolder, archiveName);
		
		// compress archive
		try {
			content = TransformersUtilities.createExportArchive(pathImpTmpFolder, archiveName);
		} catch (Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), 
					               "transform", "Error while creating creating the export archive " + e);	
		}
		// delete tmp dir content
		File tmpDir = new File(pathImpTmpFolder);
		GeneralUtilities.deleteContentDir(tmpDir);
		return content;
	}
	
	private void arrangeSubobjects(String pathImpTmpFolder, String archiveName) {
		Connection conn = null;
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			conn = TransformersUtilities.getConnectionToDatabase(pathImpTmpFolder, archiveName);
			Statement stmt = conn.createStatement();
			String selectAllOlapDocs =  "SELECT PATH FROM SBI_OBJECTS WHERE BIOBJ_TYPE_CD = 'OLAP'";
			ResultSet rs = stmt.executeQuery(selectAllOlapDocs);
			while(rs.next()){
				// for each old olap subobjects, if present, arrange class name
				String pathBiObj = rs.getString("PATH");
				String pathSubobejctsFolder = pathImpTmpFolder + "/" + archiveName + "/contents" + pathBiObj + "/subobjects";
				// finds the template file
				File pathSubobejctsFolderFile = new File(pathSubobejctsFolder);
				if (pathSubobejctsFolderFile.exists() && pathSubobejctsFolderFile.isDirectory()) {
					File[] files = pathSubobejctsFolderFile.listFiles();
					for (int i = 0; i < files.length; i++) {
						File aContainedFile = files[i];
						if (aContainedFile.isFile() && aContainedFile.getName().endsWith(".content")) {
							Properties props = new Properties();
							props.setProperty("it.eng.spagobi.bean.AnalysisBean", 
									"it.eng.spagobi.jpivotaddins.bean.AnalysisBean");
							StringBuffer aStringBuffer = new StringBuffer();
							is = new FileInputStream(aContainedFile);
							replaceParametersInBuffer(is, aStringBuffer, props);
							is.close();
							aContainedFile.delete();
							File destFile = new File(aContainedFile.getAbsolutePath());
							fos = new FileOutputStream(destFile);
							fos.write(aStringBuffer.toString().getBytes());
							fos.flush();
							fos.close();
						}
					}
				}
			}
		} catch (Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "arrangeSubobjects",
		                           "Error while changing database " + e);	
		} finally {
			try {
				if (conn != null && !conn.isClosed()) conn.close();
			} catch (SQLException e) {
				SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "arrangeSubobjects",
                           "Error while closing connection " + e);	
			}
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "arrangeSubobjects",
	                           "Error while closing input stream " + e);
				}
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e) {
					SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "arrangeSubobjects",
	                           "Error while closing output stream " + e);
				}
		}
	}
	
	private void changeDatabase(String pathImpTmpFolder, String archiveName) {
		Connection conn = null;
		try {
			conn = TransformersUtilities.getConnectionToDatabase(pathImpTmpFolder, archiveName);
			Statement stmt = conn.createStatement();
			
			// manages lovs' definition
			String selectAllLovs =  "SELECT LABEL, INPUT_TYPE_CD, LOV_PROVIDER FROM SBI_LOV";
			ResultSet rs = stmt.executeQuery(selectAllLovs);
			while (rs.next()){
				// for each old lov, resolves retrocompatibility problems
				String label = rs.getString("LABEL");
				String inputTypeCd = rs.getString("INPUT_TYPE_CD");
				String oldLovProvider = rs.getString("LOV_PROVIDER");
				String newLovProvider = resolveLovsRetrocompatibilityProblems(label, inputTypeCd, oldLovProvider);
				if (newLovProvider != null) {
					newLovProvider = newLovProvider.replaceAll("'", "''");
					String updateLov = "UPDATE SBI_LOV SET LOV_PROVIDER = '" + newLovProvider + "' " +
							"WHERE LABEL = '" + label + "'";
					stmt.executeUpdate(updateLov);
					conn.commit();
				}

			}
			
			// manages sbi_obj_paruse table (parameters dependencies)
			String sql =  "ALTER TABLE SBI_OBJ_PARUSE ADD COLUMN PROG INTEGER";
			stmt.execute(sql);
			sql =  "UPDATE SBI_OBJ_PARUSE SET PROG=1";
			stmt.executeUpdate(sql);
			sql =  "ALTER TABLE SBI_OBJ_PARUSE ADD COLUMN PRE_CONDITION VARCHAR";
			stmt.execute(sql);
			sql =  "UPDATE SBI_OBJ_PARUSE SET PRE_CONDITION=''";
			stmt.executeUpdate(sql);
			sql =  "ALTER TABLE SBI_OBJ_PARUSE ADD COLUMN POST_CONDITION VARCHAR";
			stmt.execute(sql);
			sql =  "UPDATE SBI_OBJ_PARUSE SET POST_CONDITION=''";
			stmt.executeUpdate(sql);
			sql =  "ALTER TABLE SBI_OBJ_PARUSE ADD COLUMN LOGIC_OPERATOR VARCHAR";
			stmt.execute(sql);
			sql =  "UPDATE SBI_OBJ_PARUSE SET LOGIC_OPERATOR=''";
			stmt.executeUpdate(sql);
			conn.commit();
			
			// insert new document type ETL
			sql = "SELECT MAX(VALUE_ID) AS MAXID FROM SBI_DOMAINS";
			ResultSet domainRs = stmt.executeQuery(sql);
			int maxid = 1000;
			if (domainRs.next())
				maxid = domainRs.getInt("MAXID");
			int idEtlDocumentTypeDom = maxid + 1;
			sql =  "INSERT INTO SBI_DOMAINS " +
					"(VALUE_ID, VALUE_CD, VALUE_NM, DOMAIN_CD, DOMAIN_NM, VALUE_DS) VALUES" +
					"(" + idEtlDocumentTypeDom + ", 'ETL','ETL process','BIOBJ_TYPE','BI Object types','ETL process')";
			stmt.execute(sql);
			conn.commit();
			
			// manages sbi_functions table
			sql =  "ALTER TABLE SBI_FUNCTIONS ADD COLUMN PROG INTEGER";
			stmt.execute(sql);
			sql =  "SELECT FUNCT_ID, CODE, PATH FROM SBI_FUNCTIONS";
			ResultSet foldersRs = stmt.executeQuery(sql);
			while (foldersRs.next()) {
				String updateProgQuery = null;
				int folderId = foldersRs.getInt("FUNCT_ID");
				//String code = foldersRs.getString("CODE");
				String path = foldersRs.getString("PATH");
				String parentPath = path.substring(0, path.lastIndexOf("/"));
				String findCurrentMaxProg = "SELECT MAX(PROG) AS MAX_PROG FROM SBI_FUNCTIONS " +
					"WHERE PROG IS NOT NULL AND PATH LIKE '" + parentPath + "/%' AND PATH NOT LIKE '" + parentPath + "/%/%'";
				
				ResultSet progRs = stmt.executeQuery(findCurrentMaxProg);
				if (progRs.next()) {
					int currentMaxProg = progRs.getInt("MAX_PROG");
					Integer newProg = new Integer(currentMaxProg + 1);
					updateProgQuery = "UPDATE SBI_FUNCTIONS SET PROG = " + newProg.toString() + " WHERE FUNCT_ID = " + folderId;
				} else {
					updateProgQuery = "UPDATE SBI_FUNCTIONS SET PROG = 1 WHERE FUNCT_ID = " + folderId;
				}
				stmt.execute(updateProgQuery);
				conn.commit();
			}
			
			// manages new SpagoBIJPivotEngine Url
			sql = "SELECT ENGINE_ID, MAIN_URL FROM SBI_ENGINES " +
					"WHERE DRIVER_NM LIKE '%it.eng.spagobi.drivers.jpivot.JPivotDriver%'";
			ResultSet jpivotEnginesRS = stmt.executeQuery(sql);
			while (jpivotEnginesRS.next()) {
				int engineId = jpivotEnginesRS.getInt("ENGINE_ID");
				String url = jpivotEnginesRS.getString("MAIN_URL");
				if (url != null && url.trim().endsWith("jpivotOlap.jsp")) {
					url = url.substring(0, url.lastIndexOf("jpivotOlap.jsp")) + "JPivotServlet";
					String updateEngineQuery = "UPDATE SBI_ENGINES SET MAIN_URL = '" + url + "' WHERE ENGINE_ID = " + engineId;
					stmt.execute(updateEngineQuery);
				}
			}
			
			conn.commit();
			conn.close();
			
		} catch (Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "changeDatabase",
		                           "Error while changing database " + e);	
		} finally {
			try {
				if (conn != null && !conn.isClosed()) conn.close();
			} catch (SQLException e) {
				SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "changeDatabase",
                           "Error while closing connection " + e);	
			}
		}
	}
	
	
	private void adviseForBirtDocuments(String pathImpTmpFolder, String archiveName) {
		Connection conn = null;
		try {
			conn = TransformersUtilities.getConnectionToDatabase(pathImpTmpFolder, archiveName);
			Statement stmt = conn.createStatement();
			String sql = "SELECT ENGINE_ID FROM SBI_ENGINES " +
				"WHERE DRIVER_NM LIKE '%it.eng.spagobi.drivers.birt.BirtReportDriver%'";
			ResultSet birtEnginesRS = stmt.executeQuery(sql);
			String errBirtDocsFound = "";
			while (birtEnginesRS.next()) {
				int engineId = birtEnginesRS.getInt("ENGINE_ID");
				sql = "SELECT LABEL, NAME, DESCR, PATH FROM SBI_OBJECTS WHERE ENGINE_ID = " + engineId;
				ResultSet docsRS = stmt.executeQuery(sql);
				while (docsRS.next()) {
					// get the input stream of the template file
					String pathBiObj = docsRS.getString("PATH");
					String label = docsRS.getString("LABEL");
					String name = docsRS.getString("NAME");
					String descr = docsRS.getString("DESCR");
					String pathFolderBiObj = pathImpTmpFolder + "/" + archiveName + "/contents" + pathBiObj;
				    File fileFolderBiObj = new File(pathFolderBiObj);
				    File[] files = fileFolderBiObj.listFiles();
				    File templateFile = null;
				    if (files != null && files.length > 0) {
				    	templateFile = files[0];
				    }
				    if (templateFile != null && templateFile.exists()) {
				    	FileInputStream fisTemplate = null;
				    	try {
					    	fisTemplate = new FileInputStream(templateFile);
					    	byte[] template = GeneralUtilities.getByteArrayFromInputStream(fisTemplate);
					    	String templateStr = new String (template);
					    	if (templateStr.indexOf("org.eclipse.birt.report.data.oda.subjdbc") != -1) {
								errBirtDocsFound += label + " - " + name + " - " + descr + "\n";
					    	}
				    	} finally {
				    		if (fisTemplate != null) fisTemplate.close();
				    	}
				    }
				}
			}
			if (!errBirtDocsFound.trim().equals("")) {
			    // create the directory for birt update manual task
			    String pathFolderManualTask = pathImpTmpFolder + "/" + archiveName + 
											  "/"+ImportExportConstants.MANUALTASK_FOLDER_NAME;
			    String pathFolderImpQbeManualTask = pathFolderManualTask + "/BirtDocumentsUpdate";
			    File fileFolderImpQbeManualTask = new File(pathFolderImpQbeManualTask);
			    fileFolderImpQbeManualTask.mkdirs();
			    // generate the readme file
			    String plc = PortletUtilities.getPortalLanguageCode();
			    ClassLoader cLoad = Thread.currentThread().getContextClassLoader();
			    InputStream instrIS = null;
			    FileOutputStream readmeFos = null;
			    FileOutputStream filePropertiesOs = null;
			    try {
				    instrIS = cLoad.getResourceAsStream("it/eng/spagobi/importexport/mt/birtDocumentsUpdateManualTask_"+plc);
				    readmeFos = new FileOutputStream(pathFolderImpQbeManualTask + "/Readme.txt");
				    GeneralUtilities.flushFromInputStreamToOutputStream(instrIS, readmeFos, false);
				    String append = "\n" + errBirtDocsFound;
				    readmeFos.write(append.getBytes());
				    // create properties file 
				    String pathFileProperties = pathFolderManualTask + "/" + "BirtDocumentsUpdate.properties";
				    filePropertiesOs = new FileOutputStream(pathFileProperties);
				    String propertiesStr = "name=" + PortletUtilities.getMessage("impexp.manualtask.birtDocsUpdate", "component_impexp_messages");
				    filePropertiesOs.write(propertiesStr.getBytes());
				    filePropertiesOs.flush();
			    } finally {
			    	if (instrIS != null) instrIS.close();
			    	if (readmeFos != null) readmeFos.close();
			    	if (filePropertiesOs != null) filePropertiesOs.close();
			    }
			}
			
		} catch (Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "adviseForBirtDocuments",
		                           "Error while changing database " + e);	
		} finally {
			try {
				if (conn != null && !conn.isClosed()) conn.close();
			} catch (SQLException e) {
				SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "adviseForBirtDocuments",
                           "Error while closing connection " + e);	
			}
		}
	}
	
	/**
	 * Tries to resolve retrocompatibility problems relevant to lov_provider definition of lov
	 * @param label The label of the current lov being updated
	 * @param inputTypeCd The script type code (QUERY/SCRIPT/FIX_LOV/JAVA_CLASS)
	 * @param oldLovProvider The previous lov_provider
	 * @return The new lov_provider or null if the updating process generates an error.
	 */
	private String resolveLovsRetrocompatibilityProblems(String label, String inputTypeCd, String oldLovProvider) {
		String toReturn = null;
		try {
			if ("FIX_LOV".equalsIgnoreCase(inputTypeCd)) {
				SourceBean oldSB = SourceBean.fromXMLString(oldLovProvider);
				toReturn = "<" + LovDetailFactory.FIXEDLISTLOV + "><ROWS>";
				List elements = oldSB.getAttributeAsList("LOV-ELEMENT");
				Iterator elementsIt = elements.iterator();
				while (elementsIt.hasNext()) {
					SourceBean element = (SourceBean) elementsIt.next();
					String description = (String) element.getAttribute("DESC");
					String value = (String) element.getAttribute("VALUE");
					toReturn += "<ROW DESCRIPTION=\"" + description +  "\" VALUE=\"" + value +  "\" />";
				}
				toReturn += "</ROWS><VALUE-COLUMN>VALUE</VALUE-COLUMN>" +
						"<DESCRIPTION-COLUMN>DESCRIPTION</DESCRIPTION-COLUMN>" +
						"<VISIBLE-COLUMNS>DESCRIPTION</VISIBLE-COLUMNS>" + 
						"<INVISIBLE-COLUMNS>VALUE</INVISIBLE-COLUMNS>" +
						"</" + LovDetailFactory.FIXEDLISTLOV + ">";
			} else if ("SCRIPT".equalsIgnoreCase(inputTypeCd)) {
				SourceBean oldSB = SourceBean.fromXMLString(oldLovProvider);
				SourceBean singleValueSB = (SourceBean) oldSB.getAttribute("SINGLE_VALUE");
				String singleValueStr = null;
				if (singleValueSB != null) singleValueStr = singleValueSB.getCharacters(); 
				boolean isSingleValue = (singleValueStr != null && singleValueStr.equalsIgnoreCase("true"));
				SourceBean scriptSB = (SourceBean) oldSB.getAttribute("SCRIPT");
				String script = scriptSB.getCharacters();
				// TODO adapt script encoded characters like '<', '>', '/' ecc.?
				if (isSingleValue) {
					toReturn =	"<SCRIPTLOV>" +
								    "<SCRIPT>" + script + "</SCRIPT>" +	
								    "<VALUE-COLUMN>VALUE</VALUE-COLUMN>" +
								    "<DESCRIPTION-COLUMN>VALUE</DESCRIPTION-COLUMN>" +
								    "<VISIBLE-COLUMNS>VALUE</VISIBLE-COLUMNS>" +
								    "<INVISIBLE-COLUMNS></INVISIBLE-COLUMNS>" +
							    "</SCRIPTLOV>";
				} else {
					ScriptDetail scriptDet = new ScriptDetail("<SCRIPTLOV><SCRIPT>" + script + "</SCRIPT></SCRIPTLOV>");
					List requiredProfileAttrs = scriptDet.getProfileAttributeNames();
					HashMap profileAttributes = new HashMap();
					Iterator requiredProfileAttrsIt = requiredProfileAttrs.iterator();
					while (requiredProfileAttrsIt.hasNext()) {
						String attrName = (String) requiredProfileAttrsIt.next();
						profileAttributes.put(attrName, "test");
					}
					Binding bind = ScriptManager.fillBinding(profileAttributes);
					String result = ScriptManager.runScript(script, bind);
					SourceBean source = SourceBean.fromXMLString(result);
					boolean toconvert = false;
					if (!source.getName().equalsIgnoreCase("ROWS")) {
						toconvert = true;
					} else {
						List rowsList = source.getAttributeAsList(DataRow.ROW_TAG);
						if (rowsList == null || rowsList.size() == 0) {
							toconvert = true;
						} else {
							String defaultName = "";
							SourceBean rowSB = (SourceBean) rowsList.get(0);
							List attributes = rowSB.getContainedAttributes();
							if (attributes != null && attributes.size() > 0) {
								SourceBeanAttribute attribute = (SourceBeanAttribute) attributes.get(0);
								defaultName = attribute.getKey();
							}
							// if a value column is specified, it is considered
							SourceBean valueColumnSB = (SourceBean) source.getAttribute("VALUE-COLUMN");
							if (valueColumnSB != null) {
								String valueColumn = valueColumnSB.getCharacters();
								if (valueColumn != null) {
									scriptDet.setValueColumnName(valueColumn);
								}
							} else {
								scriptDet.setValueColumnName(defaultName);
							}
							SourceBean visibleColumnsSB = (SourceBean) source.getAttribute("VISIBLE-COLUMNS");
							if (visibleColumnsSB != null) {
								String allcolumns = visibleColumnsSB.getCharacters();
								if (allcolumns != null) {
									String[] columns = allcolumns.split(",");
									scriptDet.setVisibleColumnNames(Arrays.asList(columns));
								}
							} else {
								String[] columns = new String[] {defaultName};
								scriptDet.setVisibleColumnNames(Arrays.asList(columns));
							}
							SourceBean descriptionColumnSB = (SourceBean) source.getAttribute("DESCRIPTION-COLUMN");
							if (descriptionColumnSB != null) {
								String descriptionColumn = descriptionColumnSB.getCharacters();
								if (descriptionColumn != null) {
									scriptDet.setDescriptionColumnName(descriptionColumn);
								}
							} else {
								scriptDet.setDescriptionColumnName(defaultName);
							}
						}
					}
					if (toconvert) {
						toReturn =	"<SCRIPTLOV>" +
					    				"<SCRIPT>" + script + "</SCRIPT>" +	
									    "<VALUE-COLUMN>VALUE</VALUE-COLUMN>" +
									    "<DESCRIPTION-COLUMN>VALUE</DESCRIPTION-COLUMN>" +
									    "<VISIBLE-COLUMNS>VALUE</VISIBLE-COLUMNS>" +
									    "<INVISIBLE-COLUMNS></INVISIBLE-COLUMNS>" +
								    "</SCRIPTLOV>";
					} else {
						toReturn = scriptDet.toXML();
					}
				}
			} else if ("JAVA_CLASS".equalsIgnoreCase(inputTypeCd)) {
				
			}
			return toReturn;
		} catch (Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "resolveRetrocompatibilityProblems",
                    "Could not update lov with label [" + label + "] and lov_provider:\n" +
                    		oldLovProvider + ".\n" + e);
			return null;
		}
		
	}
	
	/**
	 * Build all cms nodes for all biobjects
	 * @param pathImpTmpFolder
	 * @param archiveName
	 */
	private void buildCmsNodes(String pathImpTmpFolder, String archiveName) {
		Connection conn = null;
		try {
			conn = TransformersUtilities.getConnectionToDatabase(pathImpTmpFolder, archiveName);
			String sql = "SELECT PATH, UUID, BIOBJ_TYPE_CD FROM SBI_OBJECTS";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			conn.commit();
			conn.close();
			while(rs.next()){
				// get the input stream of the template file
				String pathBiObj = rs.getString("PATH");
				String uuid = rs.getString("UUID");
				String biobjTypeCd = rs.getString("BIOBJ_TYPE_CD");
				String pathFolderBiObj = pathImpTmpFolder + "/" + archiveName + "/contents" + pathBiObj;
				buildDocumentNodes(pathFolderBiObj, uuid, biobjTypeCd);
			}
		} catch (Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), 
		               "buildCmsNodes", "Error building cms nodes " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) conn.close();
			} catch (SQLException e) {
				SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "changeDatabase",
	                       "Error while closing connection " + e);	
			}
		}
	}
	
	/**
	 * Build all cms nodes for a single biobject
	 * @param pathFolderBiObj
	 * @param uuid
	 * @param biobjTypeCd
	 * @throws IOException 
	 */
	private void buildDocumentNodes(String pathFolderBiObj, String uuid, String biobjTypeCd) throws Exception {
		ClassLoader cLoad = Thread.currentThread().getContextClassLoader();
		BASE64Encoder bASE64Encoder = new BASE64Encoder();
		
		// finds the template file
		File pathFolderBiObjFile = new File(pathFolderBiObj);
		File[] files = pathFolderBiObjFile.listFiles();
		File template = null;
		for (int i = 0; i < files.length; i++) {
			File aContainedFile = files[i];
			if (aContainedFile.isFile()) {
				template = aContainedFile;
				break;
			}
		}
		if (template != null) {
			// builds template.xml file with template node
			InputStream templateCmsNodeIS = cLoad.getResourceAsStream("it/eng/spagobi/importexport" +
					"/transformers/nodesFor1_9_3/template.xml");
			long today = new Date().getTime();
			Properties props = new Properties();
			props.setProperty("${dateLoad}", new Long(today).toString());
			String templateFileName = template.getName();
			props.setProperty("${fileName}", templateFileName.substring(templateFileName.lastIndexOf(File.separator) + 1));
			FileInputStream templateIS = new FileInputStream(template);
			byte[] templateContent = GeneralUtilities.getByteArrayFromInputStream(templateIS);
			templateIS.close();
			template.delete();
			String base64 = bASE64Encoder.encode(templateContent);
			base64 = base64.replaceAll("\n", "");
			base64 = base64.replaceAll("\t", "");
			base64 = base64.replaceAll("\r", "");
			props.setProperty("${CONTENT.STREAM}", base64);
			replaceParametersInFile(templateCmsNodeIS, pathFolderBiObj + "/template.xml", props);
			templateCmsNodeIS.close();
		}
		
		// builds document.xml with document node
	    InputStream docCmsNodeIS = cLoad.getResourceAsStream("it/eng/spagobi/importexport" +
	    		"/transformers/nodesFor1_9_3/document.xml");
	    Properties props = new Properties();
		props.setProperty("${DOCUMENT_UUID}", uuid);
		props.setProperty("${NODE_CMS_TYPE}", biobjTypeCd);
		replaceParametersInFile(docCmsNodeIS, pathFolderBiObj + "/document.xml", props);
		docCmsNodeIS.close();
		
		// builds subobjects.xml file with subobjects nodes
		File folderSubObjFile = new File(pathFolderBiObj + "/subobjects");
		if (folderSubObjFile.exists() && folderSubObjFile.isDirectory()) {
			File[] subobjFiles = folderSubObjFile.listFiles();
			if (subobjFiles == null || subobjFiles.length == 0){
				return;
			}
			StringBuffer aStringBuffer = new StringBuffer();
			for (int i = 0; i < subobjFiles.length; i++) {
				File subobjPropertiesFile = subobjFiles[i];
				String completeFileName = subobjPropertiesFile.getName();
				String extension = completeFileName.substring(completeFileName.lastIndexOf('.') + 1);
				String subobjectName = completeFileName.substring(0, completeFileName.lastIndexOf('.'));
				if (extension.equalsIgnoreCase("content")) {
					continue;
				}
				FileInputStream fis = new FileInputStream(subobjPropertiesFile);
				Properties subobjectsProps = new Properties();
				subobjectsProps.load(fis);
				String name = subobjectsProps.getProperty("name");
				String description = subobjectsProps.getProperty("description") ;
				String owner = subobjectsProps.getProperty("owner");
				String pubvisStr =  subobjectsProps.getProperty("pubvis");
				String visibilityStr = "false";
				if (pubvisStr.equalsIgnoreCase("true")) visibilityStr = "true";

				fis.close();
				byte[] subobjectContent = null;
				File subobjContentFile = null;
				for (int j = 0; j < subobjFiles.length; j++) {
					subobjContentFile = subobjFiles[j];
					completeFileName = subobjContentFile.getName();
					if (completeFileName.equals(subobjectName + ".content")) {
						break;
					}
				}
				fis = new FileInputStream(subobjContentFile);
				subobjectContent = GeneralUtilities.getByteArrayFromInputStream(fis);
				fis.close();
				
				InputStream subobjectCmsNodeIS = cLoad.getResourceAsStream("it/eng/spagobi/importexport" +
	    			"/transformers/nodesFor1_9_3/subobject.xml");
			    Properties subobjectProps = new Properties();
			    subobjectProps.setProperty("${name}", name);
			    String todayDate = DateFormat.getDateInstance().format(new Date());
			    subobjectProps.setProperty("${lastModifcationDate}", todayDate);
			    subobjectProps.setProperty("${owner}", owner);
				String base64 = bASE64Encoder.encode(subobjectContent);
				base64 = base64.replaceAll("\n", "");
				base64 = base64.replaceAll("\t", "");
				base64 = base64.replaceAll("\r", "");
			    subobjectProps.setProperty("${CONTENT.STREAM}", base64);
			    subobjectProps.setProperty("${creationDate}", todayDate);
			    subobjectProps.setProperty("${description}", description);
			    subobjectProps.setProperty("${public}", visibilityStr);
			    replaceParametersInBuffer(subobjectCmsNodeIS, aStringBuffer, subobjectProps);
			    subobjectCmsNodeIS.close();
			    subobjPropertiesFile.delete();
			    subobjContentFile.delete();
			}
			
			InputStream allSubobjectCmsNodeIS = cLoad.getResourceAsStream("it/eng/spagobi/importexport" +
				"/transformers/nodesFor1_9_3/subobjects.xml");
			Properties allSubobjectsProps = new Properties();
			allSubobjectsProps.setProperty("${SUOBJECTS}", aStringBuffer.toString());
			replaceParametersInFile(allSubobjectCmsNodeIS, pathFolderBiObj + "/subobjects.xml", allSubobjectsProps);
			allSubobjectCmsNodeIS.close();
			
			// deletes old subobjects folder if empty
			File[] remainingFiles = folderSubObjFile.listFiles();
			if (remainingFiles == null || remainingFiles.length == 0) folderSubObjFile.delete();
		}
	}
	
	/**
	 * Obtains a StringBuffer from the InputStream, then replace the properties at input in this StringBuffer,
	 * then writes the StringBuffer into a file specified by the String file path at input
	 * @param is
	 * @param destFilePath
	 * @param props
	 * @throws Exception
	 */
	private static void replaceParametersInFile(InputStream is, String destFilePath, 
			Properties props) throws Exception {
		FileOutputStream fos = null;
		try {
			StringBuffer aStringBuffer = new StringBuffer();
			replaceParametersInBuffer(is, aStringBuffer, props);
			File destFile = new File(destFilePath);
			fos = new FileOutputStream(destFile);
			fos.write(aStringBuffer.toString().getBytes());
			fos.flush();
		} finally {
			if (fos != null) fos.close();
		}
	}
	
	/**
	 * Obtains a StringBuffer from the InputStream, then replace the properties at input in this StringBuffer,
	 * then append the StringBuffer to the one given at input
	 * @param is
	 * @param aStringBuffer
	 * @param props
	 * @throws Exception
	 */
	private static void replaceParametersInBuffer(InputStream is, StringBuffer aStringBuffer, 
			Properties props) throws Exception {
		StringBuffer tempStringBuffer = new StringBuffer();
		byte[] buffer = new byte[2048];
		int len;
		while ((len = is.read(buffer)) >= 0) {
			String temp = new String(buffer, 0, len);
			tempStringBuffer.append(temp);
		}
		Set keys = props.keySet();
		Iterator it = keys.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			String value = props.getProperty(key);
			int startIndex = tempStringBuffer.indexOf(key);
			while (startIndex != -1) {
				tempStringBuffer.replace(startIndex, startIndex + key.length(), value);
				startIndex = tempStringBuffer.indexOf(key, startIndex + value.length());
			}
		}
		aStringBuffer.append(tempStringBuffer);
		
	}
	
}
