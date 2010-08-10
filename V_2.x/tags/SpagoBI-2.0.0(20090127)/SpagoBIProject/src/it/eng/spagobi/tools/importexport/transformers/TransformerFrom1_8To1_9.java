/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.tools.importexport.transformers;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.commons.utilities.SpagoBITracer;
import it.eng.spagobi.tools.importexport.ITransformer;
import it.eng.spagobi.tools.importexport.ImportExportConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class TransformerFrom1_8To1_9 implements ITransformer {

	/* (non-Javadoc)
	 * @see it.eng.spagobi.tools.importexport.ITransformer#transform(byte[], java.lang.String, java.lang.String)
	 */
	public byte[] transform(byte[] content, String pathImpTmpFolder, String archiveName) {
		try {
			TransformersUtilities.decompressArchive(pathImpTmpFolder, archiveName, content);
		} catch(Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), 
                                   "transform", "Error while decompressing 1.8 exported archive" + e);	
		}
		archiveName = archiveName.substring(0, archiveName.lastIndexOf('.'));
		changeDatabase(pathImpTmpFolder, archiveName);
		String baseCmsFolder = "";
		try {
			baseCmsFolder = getCmsBaseFolder(pathImpTmpFolder, archiveName);
		} catch(Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), 
		                           "transform", "Error while retriving the cms base folder of 1.8 version " + e);	
		}	
		// change dashboard template
		changeDashTempl(pathImpTmpFolder, archiveName);
		// associate objects with functionalities
		createObjectsFunctsAssociations(pathImpTmpFolder, archiveName);
		// update path of functionalitie
		updateFunctionalityPaths(baseCmsFolder, pathImpTmpFolder, archiveName);
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

	
	private void changeDashTempl(String pathImpTmpFold, String archiveName) {
		Connection conn = null;
		try{
			conn = TransformersUtilities.getConnectionToDatabase(pathImpTmpFold, archiveName);
			String sql = "SELECT * FROM SBI_OBJECTS";
			PreparedStatement stmt = null;
			stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				String path = rs.getString("PATH");
				int objectID = rs.getInt("BIOBJ_ID");
				String biobjcode = rs.getString("BIOBJ_TYPE_CD");
				if(biobjcode.equalsIgnoreCase("DASH")) {
					String pathBIObjFolder = pathImpTmpFold + "/" + archiveName + "/contents" + path;
				    File fileBIObjFolder = new File(pathBIObjFolder);
				    File[] files = fileBIObjFolder.listFiles();
				    File tempFile = files[0];
				    FileInputStream fis = new FileInputStream(tempFile);
				    byte[] content = GeneralUtilities.getByteArrayFromInputStream(fis);
				    fis.close();
				    String contentStr = new String(content);
				    SourceBean contentSB = SourceBean.fromXMLString(contentStr);
				    String movie = (String)contentSB.getAttribute("movie");
				    int indDash = movie.indexOf("dashboards");
				    movie = movie.substring(indDash);
				    contentSB.updAttribute("movie", movie);
				    contentSB.updAttribute("DATA.url", "/DashboardService");
				    String nameFileTemp = tempFile.getName();
				    tempFile.delete();
				    FileOutputStream fos = new FileOutputStream(pathBIObjFolder + "/" + nameFileTemp); 
				    fos.write(contentSB.toXML(false).getBytes());
				    fos.flush();
				    fos.close();
				}
			}
			conn.commit();
			conn.close();
			
			
			
		} catch(Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "createObjectsFunctsAssociations",
                                   "Error while creating associations between objects and functions " + e);	
		}
	}
	
	
	private void createObjectsFunctsAssociations(String pathImpTmpFold, String archiveName) {
		Connection conn = null;
		try{
			conn = TransformersUtilities.getConnectionToDatabase(pathImpTmpFold, archiveName);
			String sql = "SELECT * FROM SBI_OBJECTS";
			//Statement stmt = conn.createStatement();
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			ResultSet rs1 = null;
			while(rs.next()) {
				String path = rs.getString("PATH");
				int objectID = rs.getInt("BIOBJ_ID");
				String functPath = path.substring(0, path.lastIndexOf('/'));
				//sql =  "SELECT * FROM SBI_FUNCTIONS WHERE PATH = '"+functPath+"'";
				//rs1 = stmt.executeQuery(sql);
				sql =  "SELECT * FROM SBI_FUNCTIONS WHERE PATH = ?";
			    stmt = conn.prepareStatement(sql);
			    stmt.setString(0, functPath);
			    rs1 = stmt.executeQuery();
				
				if(rs1.next()) {
					int functID = rs1.getInt("FUNCT_ID");
					//sql = "INSERT INTO SBI_OBJ_FUNC (BIOBJ_ID, FUNCT_ID, PROG) VALUES ("+objectID+", "+functID+", 0)";
					//stmt.execute(sql);
					sql = "INSERT INTO SBI_OBJ_FUNC (BIOBJ_ID, FUNCT_ID, PROG) VALUES (?, ?, 0)";
					stmt = conn.prepareStatement(sql);
					stmt.setInt(0, objectID);
					stmt.setInt(1, functID);
					stmt.execute();
				}
			}
			conn.commit();
			conn.close();
		} catch(Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "createObjectsFunctsAssociations",
                                   "Error while creating associations between objects and functions " + e);	
		}
	}
	
	
	private void updateFunctionalityPaths(String baseCmsPath, String pathImpTmpFold, String archiveName){
		Connection conn = null;
		try{
			conn = TransformersUtilities.getConnectionToDatabase(pathImpTmpFold, archiveName);
			String sql = "SELECT * FROM SBI_FUNCTIONS";
			PreparedStatement stmt = null;
			stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			ResultSet rs1 = null;
			while(rs.next()) {
				String path = rs.getString("PATH");
				int functId = rs.getInt("FUNCT_ID");
				String relativePath = path.substring(baseCmsPath.length());
				String newPath = "/Functionalities" + relativePath;
				//sql = "UPDATE SBI_FUNCTIONS SET PATH = '"+newPath+"' WHERE FUNCT_ID = " + functId;
				sql = "UPDATE SBI_FUNCTIONS SET PATH = ? WHERE FUNCT_ID = ?" ;
				stmt = conn.prepareStatement(sql);
				stmt.setString(0, newPath);
				stmt.setInt(1, functId);
				stmt.executeUpdate();
			}
			conn.commit();
			conn.close();
		} catch(Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "updateFunctionalityPaths",
                                   "Error while updating functionalities path " + e);	
		}
	}
	
	
	

	private String getCmsBaseFolder(String pathImpTmpFold, String archiveName) throws Exception {
		//get exported properties
		String pathBaseFolder = pathImpTmpFold + "/" + archiveName;
	    String propFilePath = pathBaseFolder + "/export.properties";
	    FileInputStream fis = new FileInputStream(propFilePath);
		Properties props = new Properties();
		props.load(fis);
		fis.close();
		return props.getProperty("cms-basefolder");
	}
	
	
	
	private void changeDatabase(String pathImpTmpFolder, String archiveName) {
		Connection conn = null;
		try{
			conn = TransformersUtilities.getConnectionToDatabase(pathImpTmpFolder, archiveName);
			String sql = "";
			//Statement stmt = conn.createStatement();
			PreparedStatement stmt = null;
			sql =  "ALTER TABLE sbi_objects ADD COLUMN visible SMALLINT";
			stmt = conn.prepareStatement(sql);
			stmt.execute();
			sql =  "UPDATE sbi_objects SET visible=1";
			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate();
			sql =  "ALTER TABLE sbi_objects ADD COLUMN uuid VARCHAR";
			stmt = conn.prepareStatement(sql);
			stmt.execute();
			sql =  "UPDATE sbi_objects SET uuid=''";
			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate();
			sql =  "CREATE TABLE SBI_SUBREPORTS (MASTER_RPT_ID INTEGER NOT NULL, SUB_RPT_ID INTEGER NOT NULL, PRIMARY KEY (MASTER_RPT_ID, SUB_RPT_ID))";
			stmt = conn.prepareStatement(sql);
			stmt.execute();
			sql =  "CREATE TABLE SBI_OBJ_PAR_TEMP (OBJ_PAR_ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL,PAR_ID INTEGER NOT NULL,BIOBJ_ID INTEGER NOT NULL,LABEL VARCHAR,REQ_FL SMALLINT,MOD_FL SMALLINT,VIEW_FL SMALLINT,MULT_FL SMALLINT,PROG INTEGER NOT NULL,PARURL_NM VARCHAR,PRIORITY INTEGER)";
			stmt = conn.prepareStatement(sql);
			stmt.execute();
			sql =  "INSERT INTO SBI_OBJ_PAR_TEMP (PAR_ID,BIOBJ_ID,LABEL,REQ_FL,MOD_FL,VIEW_FL,MULT_FL,PROG,PARURL_NM) SELECT PAR_ID,BIOBJ_ID,LABEL,REQ_FL,MOD_FL,VIEW_FL,MULT_FL,PROG,PARURL_NM FROM SBI_OBJ_PAR";
			stmt = conn.prepareStatement(sql);
			stmt.execute();
			sql =  "DROP TABLE SBI_OBJ_PAR";
			stmt = conn.prepareStatement(sql);
			stmt.execute();
			sql =  "CREATE MEMORY TABLE SBI_OBJ_PAR(OBJ_PAR_ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL,PAR_ID INTEGER NOT NULL,BIOBJ_ID INTEGER NOT NULL,LABEL VARCHAR,REQ_FL SMALLINT,MOD_FL SMALLINT,VIEW_FL SMALLINT,MULT_FL SMALLINT,PROG INTEGER NOT NULL,PARURL_NM VARCHAR,PRIORITY INTEGER,CONSTRAINT XPKSBI_OBJ_PAR_1 PRIMARY KEY(OBJ_PAR_ID))";
			stmt = conn.prepareStatement(sql);
			stmt.execute();
			sql =  "INSERT INTO SBI_OBJ_PAR (PAR_ID,BIOBJ_ID,LABEL,REQ_FL,MOD_FL,VIEW_FL,MULT_FL,PROG,PARURL_NM) SELECT PAR_ID,BIOBJ_ID,LABEL,REQ_FL,MOD_FL,VIEW_FL,MULT_FL,PROG,PARURL_NM FROM SBI_OBJ_PAR_TEMP";
			stmt = conn.prepareStatement(sql);
			stmt.execute();
			sql =  "DROP TABLE SBI_OBJ_PAR_TEMP";
			stmt = conn.prepareStatement(sql);
			stmt.execute();
			sql =  "CREATE MEMORY TABLE SBI_OBJ_PARUSE (OBJ_PAR_ID INTEGER NOT NULL, USE_ID INTEGER NOT NULL, OBJ_PAR_FATHER_ID INTEGER NOT NULL, FILTER_COLUMN VARCHAR NOT NULL, FILTER_OPERATION VARCHAR NOT NULL, CONSTRAINT XPKSBI_OBJ_PARUSE PRIMARY KEY(OBJ_PAR_ID,USE_ID))";
			stmt = conn.prepareStatement(sql);
			stmt.execute();
			// calculate max id for sbidomains
			sql = "SELECT MAX(VALUE_ID ) AS MAXID FROM SBI_DOMAINS";
			stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			int maxid = 1000;
			if(rs.next())
				maxid = rs.getInt("MAXID");
			int idExtEngDom = maxid+1;
			int idIntEngDom = maxid+2;
			// insert sbidomains for engine using maxid
			sql =  "INSERT INTO SBI_DOMAINS (VALUE_ID, VALUE_CD,VALUE_NM,DOMAIN_CD,DOMAIN_NM,VALUE_DS) VALUES("+idExtEngDom+", 'EXT','External Engine','ENGINE_TYPE','Engine types','Business intelligence external engine of SpagoBI platform')";
			stmt = conn.prepareStatement(sql);
			stmt.execute();
			sql =  "INSERT INTO SBI_DOMAINS (VALUE_ID, VALUE_CD,VALUE_NM,DOMAIN_CD,DOMAIN_NM,VALUE_DS) VALUES("+idIntEngDom+", 'INT','Internal Engine','ENGINE_TYPE','Engine types','Business intelligence internal engine of SpagoBI platform')";
			stmt = conn.prepareStatement(sql);
			stmt.execute();
			sql =  "ALTER TABLE sbi_engines ADD COLUMN ENGINE_TYPE INTEGER";
			stmt = conn.prepareStatement(sql);
			stmt.execute();
			sql =  "ALTER TABLE sbi_engines ADD COLUMN CLASS_NM VARCHAR";
			stmt = conn.prepareStatement(sql);
			stmt.execute();
			sql =  "ALTER TABLE sbi_engines ADD COLUMN BIOBJ_TYPE INTEGER";
			stmt = conn.prepareStatement(sql);
			stmt.execute();
			sql =  "UPDATE sbi_engines SET ENGINE_TYPE = (SELECT  VALUE_ID  FROM SBI_DOMAINS WHERE VALUE_CD = 'EXT')";
			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate();
			sql =  "UPDATE sbi_engines SET BIOBJ_TYPE = (SELECT  VALUE_ID  FROM SBI_DOMAINS WHERE VALUE_CD = 'REPORT')";
			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate();
			sql =  "ALTER TABLE sbi_paruse ADD COLUMN MAN_IN INTEGER";
			stmt = conn.prepareStatement(sql);
			stmt.execute();
			sql =  "UPDATE sbi_paruse SET MAN_IN=0";
			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate();
			sql =  "CREATE MEMORY TABLE SBI_EVENTS (ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 100) NOT NULL, USER VARCHAR NOT NULL, CONSTRAINT XPKSBI_EVENTS PRIMARY KEY(ID))";
			stmt = conn.prepareStatement(sql);
			stmt.execute(sql);
			sql =  "CREATE MEMORY TABLE SBI_EVENTS_LOG (ID VARCHAR NOT NULL, USER VARCHAR NOT NULL, DATE TIMESTAMP DEFAULT 'now' NOT NULL, DESC VARCHAR NOT NULL, PARAMS VARCHAR NOT NULL, CONSTRAINT XPKSBI_EVENTS_LOG PRIMARY KEY(ID,USER, DATE))";
			stmt = conn.prepareStatement(sql);
			stmt.execute();
			
			
			// delete lov manual input and set the manin flag into paruse
			sql = "SELECT LOV_ID FROM SBI_LOV WHERE INPUT_TYPE_CD = 'MAN_IN'";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()) {
				int lovid = rs.getInt("LOV_ID");
				//sql = "UPDATE SBI_PARUSE SET LOV_ID = NULL, MAN_IN = '1' WHERE LOV_ID = '"+lovid+"'";
				//stmt.executeUpdate(sql);
				sql = "UPDATE SBI_PARUSE SET LOV_ID = NULL, MAN_IN = '1' WHERE LOV_ID = ?";
				stmt = conn.prepareStatement(sql);
				stmt.setInt(0, lovid);
				stmt.executeUpdate();
				//sql = "DELETE FROM SBI_LOV WHERE LOV_ID = '"+lovid+"'";
				//stmt.execute(sql);	
				sql = "DELETE FROM SBI_LOV WHERE LOV_ID = ?";
				stmt = conn.prepareStatement(sql);
				stmt.setInt(0, lovid);
				stmt.executeUpdate();
			}
			
			
			// calculate max id for engined
			sql = "SELECT MAX(ENGINE_ID) AS MAXID FROM SBI_ENGINES";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			int maxidEngine = 1000;
			if(rs.next())
				maxidEngine = rs.getInt("MAXID");
			int idQbeEngine = maxidEngine + 1;
			int idDashEngine = maxidEngine + 2;
			// get id of the domain 'DATAMART'
			sql = "SELECT VALUE_ID FROM SBI_DOMAINS WHERE VALUE_CD = 'DATAMART'";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.next();
			int idDatamartDom = rs.getInt("VALUE_ID");
			// get id of the domain 'DASH'
			sql = "SELECT VALUE_ID FROM SBI_DOMAINS WHERE VALUE_CD = 'DASH'";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.next();
			int idDashDom = rs.getInt("VALUE_ID");
			// insert the default internal engines
			//sql = "INSERT INTO SBI_ENGINES VALUES("+idQbeEngine+",0,'Qbe Internal Engine','Qbe Internal Engine','','','','','','QbeInternalEngine',"+idIntEngDom+",'it.eng.spagobi.engines.datamart.SpagoBIQbeInternalEngine',"+idDatamartDom+")";
			sql = "INSERT INTO SBI_ENGINES VALUES(?,0,'Qbe Internal Engine','Qbe Internal Engine','','','','','','QbeInternalEngine',?,'it.eng.spagobi.engines.datamart.SpagoBIQbeInternalEngine',?)";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(0, idQbeEngine);
			stmt.setInt(1, idIntEngDom);
			stmt.setInt(2, idDatamartDom);
			stmt.execute();
			//sql = "INSERT INTO SBI_ENGINES VALUES("+idDashEngine+",0,'Dashboard Internal Engine','Dashboard Internal Engine','','','','','','DashboardInternalEng',"+idIntEngDom+",'it.eng.spagobi.engines.dashboard.SpagoBIDashboardInternalEngine',"+idDashDom+")";
			sql = "INSERT INTO SBI_ENGINES VALUES(?,0,'Dashboard Internal Engine','Dashboard Internal Engine','','','','','','DashboardInternalEng',?,'it.eng.spagobi.engines.dashboard.SpagoBIDashboardInternalEngine',?)";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(0, idDashEngine);
			stmt.setInt(1, idIntEngDom);
			stmt.setInt(2, idDashDom);
			stmt.execute();
			// for each document, check if it is a dash or datamart and change its egine
			sql = "SELECT * FROM SBI_OBJECTS";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()) {
				int biobjid = rs.getInt("BIOBJ_ID");
				String biobjcode = rs.getString("BIOBJ_TYPE_CD");
				if(biobjcode.equals("DASH")) {
					//sql = "UPDATE SBI_OBJECTS SET ENGINE_ID = "+idDashEngine+" WHERE BIOBJ_ID = "+biobjid;
					sql = "UPDATE SBI_OBJECTS SET ENGINE_ID = ? WHERE BIOBJ_ID = ?";
					stmt = conn.prepareStatement(sql);
					stmt.setInt(0, idDashEngine);
					stmt.setInt(1, biobjid);
					stmt.executeUpdate();
				}
				if(biobjcode.equals("DATAMART")) {
					//sql = "UPDATE SBI_OBJECTS SET ENGINE_ID = "+idQbeEngine+" WHERE BIOBJ_ID = "+biobjid;
					sql = "UPDATE SBI_OBJECTS SET ENGINE_ID = ? WHERE BIOBJ_ID = ?";
					stmt = conn.prepareStatement(sql);
					stmt.setInt(0, idQbeEngine);
					stmt.setInt(1, biobjid);
					stmt.executeUpdate();
				}
			}
			
			// erase the engines no more useful (into the 1.8 version the dash and qbe objects has a default engine for db key reason)
			sql = "SELECT ENGINE_ID FROM SBI_ENGINES";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			ResultSet rs1 = null;
			while(rs.next()) {
				int engineid = rs.getInt("ENGINE_ID");
				//sql = "SELECT BIOBJ_ID FROM SBI_OBJECTS WHERE ENGINE_ID = " + engineid;
				sql = "SELECT BIOBJ_ID FROM SBI_OBJECTS WHERE ENGINE_ID = ?";
				stmt = conn.prepareStatement(sql);
				stmt.setInt(0, engineid);
				rs1 = stmt.executeQuery();
				if(!rs1.next()) {
					//sql = "DELETE FROM SBI_ENGINES WHERE ENGINE_ID = " + engineid;
					sql = "DELETE FROM SBI_ENGINES WHERE ENGINE_ID = ?";
					stmt = conn.prepareStatement(sql);
					stmt.setInt(0, engineid);
					stmt.executeUpdate();
				}
			}
			
			conn.commit();
			conn.close();
		} catch (Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "changeDatabase",
		                           "Error while changing database " + e);	
		}
	}
	
}
