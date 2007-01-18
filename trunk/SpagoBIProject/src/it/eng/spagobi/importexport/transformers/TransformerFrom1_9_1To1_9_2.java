package it.eng.spagobi.importexport.transformers;

import it.eng.spagobi.importexport.ITransformer;
import it.eng.spagobi.importexport.ImportExportConstants;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.PortletUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TransformerFrom1_9_1To1_9_2 implements ITransformer {

	public byte[] transform(byte[] content, String pathImpTmpFolder, String archiveName) {
		try {
			TransformersUtilities.decompressArchive(pathImpTmpFolder, archiveName, content);
		} catch(Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), 
                                   "transform", "Error while decompressing 1.9 exported archive" + e);	
		}
		archiveName = archiveName.substring(0, archiveName.lastIndexOf('.'));
		changeDatabase(pathImpTmpFolder, archiveName);
	    pullOutDatamarts(pathImpTmpFolder, archiveName);

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
	
	private void changeDatabase(String pathImpTmpFolder, String archiveName) {
		Connection conn = null;
		try{
			conn = TransformersUtilities.getConnectionToDatabase(pathImpTmpFolder, archiveName);
			Statement stmt = conn.createStatement();
			String sql =  "UPDATE SBI_ENGINES " +
			              "SET MAIN_URL = 'http://server_name:port/SpagoBIQbeEngine/servlet/AdapterHTTP?ACTION_NAME=SPAGO_BI_START_ACTION&NEW_SESSION=TRUE', " +
						  "DRIVER_NM = 'it.eng.spagobi.drivers.qbe.QbeDriver', " +
						  "ENGINE_TYPE = (SELECT  VALUE_ID  FROM SBI_DOMAINS WHERE VALUE_CD = 'EXT'), " + 
						  "BIOBJ_TYPE = (SELECT  VALUE_ID  FROM SBI_DOMAINS WHERE VALUE_CD = 'DATAMART'), " + 
						  "NAME = 'Qbe External Engine', " +
						  "DESCR = 'External Engine for datamart objects', " +
						  "CLASS_NM = '', " +
						  "LABEL = 'QbeExtEng' " +
						  "WHERE CLASS_NM = 'it.eng.spagobi.engines.datamart.SpagoBIQbeInternalEngine'";
			//String sql =  "DELETE FROM SBI_ENGINES WHERE CLASS_NM = 'it.eng.spagobi.engines.datamart.SpagoBIQbeInternalEngine'";
			//stmt.execute(sql);
			stmt.executeUpdate(sql);
			conn.commit();
			conn.close();
		} catch (Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "changeDatabase",
		                           "Error while changing database " + e);	
		}
	}
	

	private void pullOutDatamarts(String pathImpTmpFolder, String archiveName) {
		Connection conn = null;
		try{			
			conn = TransformersUtilities.getConnectionToDatabase(pathImpTmpFolder, archiveName);
			String sql = "SELECT PATH FROM SBI_OBJECTS WHERE BIOBJ_TYPE_CD = 'DATAMART'";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			conn.commit();
			conn.close();
			while(rs.next()){
				// get the input stream of the template file
				String pathBiObj = rs.getString("PATH");
				String pathFolderBiObj = pathImpTmpFolder + "/" + archiveName + "/contents" + pathBiObj;
			    File fileFolderBiObj = new File(pathFolderBiObj);
			    File[] files = fileFolderBiObj.listFiles();
			    File templateFile = files[0];
			    FileInputStream fisTemplate = new FileInputStream(templateFile);
			    // create the directory for import datamart manual task
			    String pathFolderManualTask = pathImpTmpFolder + "/" + archiveName + 
											  "/"+ImportExportConstants.MANUALTASK_FOLDER_NAME;
			    String pathFolderImpQbeManualTask = pathFolderManualTask + "/ImportDatamartObjects";
			    File fileFolderImpQbeManualTask = new File(pathFolderImpQbeManualTask);
			    fileFolderImpQbeManualTask.mkdirs();
			    // create an output stream to write the template
			    String pathFileDatamartJar = pathFolderImpQbeManualTask + "/" + templateFile.getName();
			    FileOutputStream fileDatamartJarOs = new FileOutputStream(pathFileDatamartJar);
			    // flush datamart to the new file
			    GeneralUtilities.flushFromInputStreamToOutputStream(fisTemplate, fileDatamartJarOs, true);
			    // generate the readme file
			    String plc = PortletUtilities.getPortalLanguageCode();
			    ClassLoader cLoad = Thread.currentThread().getContextClassLoader();
			    InputStream instrIS = cLoad.getResourceAsStream("it/eng/spagobi/importexport/mt/datamartImportManualTask_"+plc);
			    FileOutputStream readmeFos = new FileOutputStream(pathFolderImpQbeManualTask + "/Readme.txt");
			    GeneralUtilities.flushFromInputStreamToOutputStream(instrIS, readmeFos, true);
			    // create properties file 
			    String pathFileProperties = pathFolderManualTask + "/" + "ImportDatamartObjects.properties";
			    FileOutputStream filePropertiesOs = new FileOutputStream(pathFileProperties);
			    String propertiesStr = "name=" + PortletUtilities.getMessage("impexp.manualtask.datamarts", "component_impexp_messages");
			    filePropertiesOs.write(propertiesStr.getBytes());
			    filePropertiesOs.flush();
			    filePropertiesOs.close();
			    // delete old template file 
			    templateFile.delete();
			    // create a new template file
			    String pathNewTemp = pathFolderBiObj + "/template.xml";
			    FileOutputStream fileNewTempOs = new FileOutputStream(pathNewTemp);
			    String newTempStr = "<QBE>\n<DATASOURCE name=\"\" dialect=\"\" />\n"+
			    					"<DATAMART name=\""+getFilenameWithoutExtension(templateFile.getName())+"\"/>\n</QBE>";
			    fileNewTempOs.write(newTempStr.getBytes());
			    fileNewTempOs.flush();
			    fileNewTempOs.close();
			}
		} catch (Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), 
					               "pullOutDatamarts", "Error while pull out datamarts " + e);	
		}
	}

	private String getFilenameWithoutExtension(String fileName) {
		String newFileName = fileName;
		int indLastPoint = fileName.lastIndexOf(".");
		if(indLastPoint!=-1) {
			newFileName = fileName.substring(0, indLastPoint);
		}
		return newFileName;
	}

	
}
