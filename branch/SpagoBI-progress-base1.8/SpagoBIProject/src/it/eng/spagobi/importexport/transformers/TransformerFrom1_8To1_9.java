package it.eng.spagobi.importexport.transformers;

import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.importexport.ITransformer;
import it.eng.spagobi.importexport.ImportExportConstants;
import it.eng.spagobi.importexport.ImportUtilities;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class TransformerFrom1_8To1_9 implements ITransformer {

	public byte[] transform(byte[] content, String pathImpTmpFolder, String archiveName) {
		try{
			decompressArchive(pathImpTmpFolder, archiveName, content);
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
			content = createExportArchive(pathImpTmpFolder, archiveName);
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
			conn = getConnectionToDatabase(pathImpTmpFold, archiveName);
			String sql = "SELECT * FROM SBI_OBJECTS";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
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
			conn = getConnectionToDatabase(pathImpTmpFold, archiveName);
			String sql = "SELECT * FROM SBI_OBJECTS";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			ResultSet rs1 = null;
			while(rs.next()) {
				String path = rs.getString("PATH");
				int objectID = rs.getInt("BIOBJ_ID");
				String functPath = path.substring(0, path.lastIndexOf('/'));
				sql =  "SELECT * FROM SBI_FUNCTIONS WHERE PATH = '"+functPath+"'";
				rs1 = stmt.executeQuery(sql);
				if(rs1.next()) {
					int functID = rs1.getInt("FUNCT_ID");
					sql = "INSERT INTO SBI_OBJ_FUNC (BIOBJ_ID, FUNCT_ID, PROG) VALUES ("+objectID+", "+functID+", 0)";
					stmt.execute(sql);
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
			conn = getConnectionToDatabase(pathImpTmpFold, archiveName);
			String sql = "SELECT * FROM SBI_FUNCTIONS";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			ResultSet rs1 = null;
			while(rs.next()) {
				String path = rs.getString("PATH");
				int functId = rs.getInt("FUNCT_ID");
				String relativePath = path.substring(baseCmsPath.length());
				String newPath = "/Functionalities" + relativePath;
				sql = "UPDATE SBI_FUNCTIONS SET PATH = '"+newPath+"' WHERE FUNCT_ID = " + functId;
				stmt.executeUpdate(sql);
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
			conn = getConnectionToDatabase(pathImpTmpFolder, archiveName);
			String sql = "";
			Statement stmt = conn.createStatement();
			sql =  "ALTER TABLE sbi_objects ADD COLUMN visible SMALLINT";
			stmt.execute(sql);
			sql =  "UPDATE sbi_objects SET visible=1";
			stmt.executeUpdate(sql);
			sql =  "ALTER TABLE sbi_objects ADD COLUMN uuid VARCHAR";
			stmt.execute(sql);
			sql =  "UPDATE sbi_objects SET uuid=''";
			stmt.executeUpdate(sql);
			sql =  "CREATE TABLE SBI_SUBREPORTS (MASTER_RPT_ID INTEGER NOT NULL, SUB_RPT_ID INTEGER NOT NULL, PRIMARY KEY (MASTER_RPT_ID, SUB_RPT_ID))";
			stmt.execute(sql);
			sql =  "CREATE TABLE SBI_OBJ_PAR_TEMP (OBJ_PAR_ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL,PAR_ID INTEGER NOT NULL,BIOBJ_ID INTEGER NOT NULL,LABEL VARCHAR,REQ_FL SMALLINT,MOD_FL SMALLINT,VIEW_FL SMALLINT,MULT_FL SMALLINT,PROG INTEGER NOT NULL,PARURL_NM VARCHAR,PRIORITY INTEGER)";
			stmt.execute(sql);
			sql =  "INSERT INTO SBI_OBJ_PAR_TEMP (PAR_ID,BIOBJ_ID,LABEL,REQ_FL,MOD_FL,VIEW_FL,MULT_FL,PROG,PARURL_NM) SELECT PAR_ID,BIOBJ_ID,LABEL,REQ_FL,MOD_FL,VIEW_FL,MULT_FL,PROG,PARURL_NM FROM SBI_OBJ_PAR";
			stmt.execute(sql);
			sql =  "DROP TABLE SBI_OBJ_PAR";
			stmt.execute(sql);
			sql =  "CREATE MEMORY TABLE SBI_OBJ_PAR(OBJ_PAR_ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL,PAR_ID INTEGER NOT NULL,BIOBJ_ID INTEGER NOT NULL,LABEL VARCHAR,REQ_FL SMALLINT,MOD_FL SMALLINT,VIEW_FL SMALLINT,MULT_FL SMALLINT,PROG INTEGER NOT NULL,PARURL_NM VARCHAR,PRIORITY INTEGER,CONSTRAINT XPKSBI_OBJ_PAR_1 PRIMARY KEY(OBJ_PAR_ID))";
			stmt.execute(sql);
			sql =  "INSERT INTO SBI_OBJ_PAR (PAR_ID,BIOBJ_ID,LABEL,REQ_FL,MOD_FL,VIEW_FL,MULT_FL,PROG,PARURL_NM) SELECT PAR_ID,BIOBJ_ID,LABEL,REQ_FL,MOD_FL,VIEW_FL,MULT_FL,PROG,PARURL_NM FROM SBI_OBJ_PAR_TEMP";
			stmt.execute(sql);
			sql =  "DROP TABLE SBI_OBJ_PAR_TEMP";
			stmt.execute(sql);
			sql =  "CREATE MEMORY TABLE SBI_OBJ_PARUSE (OBJ_PAR_ID INTEGER NOT NULL, USE_ID INTEGER NOT NULL, OBJ_PAR_FATHER_ID INTEGER NOT NULL, FILTER_COLUMN VARCHAR NOT NULL, FILTER_OPERATION VARCHAR NOT NULL, CONSTRAINT XPKSBI_OBJ_PARUSE PRIMARY KEY(OBJ_PAR_ID,USE_ID))";
			stmt.execute(sql);
			// calculate max id for sbidomains
			sql = "SELECT MAX(VALUE_ID ) AS MAXID FROM SBI_DOMAINS";
			ResultSet rs = stmt.executeQuery(sql);
			int maxid = 1000;
			if(rs.next())
				maxid = rs.getInt("MAXID");
			int idExtEngDom = maxid+1;
			int idIntEngDom = maxid+2;
			// insert sbidomains for engine using maxid
			sql =  "INSERT INTO SBI_DOMAINS (VALUE_ID, VALUE_CD,VALUE_NM,DOMAIN_CD,DOMAIN_NM,VALUE_DS) VALUES("+idExtEngDom+", 'EXT','External Engine','ENGINE_TYPE','Engine types','Business intelligence external engine of SpagoBI platform')";
			stmt.execute(sql);
			sql =  "INSERT INTO SBI_DOMAINS (VALUE_ID, VALUE_CD,VALUE_NM,DOMAIN_CD,DOMAIN_NM,VALUE_DS) VALUES("+idIntEngDom+", 'INT','Internal Engine','ENGINE_TYPE','Engine types','Business intelligence internal engine of SpagoBI platform')";
			stmt.execute(sql);
			sql =  "ALTER TABLE sbi_engines ADD COLUMN ENGINE_TYPE INTEGER";
			stmt.execute(sql);
			sql =  "ALTER TABLE sbi_engines ADD COLUMN CLASS_NM VARCHAR";
			stmt.execute(sql);
			sql =  "ALTER TABLE sbi_engines ADD COLUMN BIOBJ_TYPE INTEGER";
			stmt.execute(sql);
			sql =  "UPDATE sbi_engines SET ENGINE_TYPE = (SELECT  VALUE_ID  FROM SBI_DOMAINS WHERE VALUE_CD = 'EXT')";
			stmt.executeUpdate(sql);
			sql =  "UPDATE sbi_engines SET BIOBJ_TYPE = (SELECT  VALUE_ID  FROM SBI_DOMAINS WHERE VALUE_CD = 'REPORT')";
			stmt.executeUpdate(sql);
			sql =  "ALTER TABLE sbi_paruse ADD COLUMN MAN_IN INTEGER";
			stmt.execute(sql);
			sql =  "UPDATE sbi_paruse SET MAN_IN=0";
			stmt.executeUpdate(sql);
			sql =  "CREATE MEMORY TABLE SBI_EVENTS (ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 100) NOT NULL, USER VARCHAR NOT NULL, CONSTRAINT XPKSBI_EVENTS PRIMARY KEY(ID))";
			stmt.execute(sql);
			sql =  "CREATE MEMORY TABLE SBI_EVENTS_LOG (ID VARCHAR NOT NULL, USER VARCHAR NOT NULL, DATE TIMESTAMP DEFAULT 'now' NOT NULL, DESC VARCHAR NOT NULL, PARAMS VARCHAR NOT NULL, CONSTRAINT XPKSBI_EVENTS_LOG PRIMARY KEY(ID,USER, DATE))";
			stmt.execute(sql);
			
			
			// delete lov manual input and set the manin flag into paruse
			sql = "SELECT LOV_ID FROM SBI_LOV WHERE INPUT_TYPE_CD = 'MAN_IN'";
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				int lovid = rs.getInt("LOV_ID");
				sql = "UPDATE SBI_PARUSE SET LOV_ID = NULL, MAN_IN = '1' WHERE LOV_ID = '"+lovid+"'";
				stmt.executeUpdate(sql);
				sql = "DELETE FROM SBI_LOV WHERE LOV_ID = '"+lovid+"'";
				stmt.execute(sql);	
			}
			
			
			// calculate max id for engined
			sql = "SELECT MAX(ENGINE_ID) AS MAXID FROM SBI_ENGINES";
			rs = stmt.executeQuery(sql);
			int maxidEngine = 1000;
			if(rs.next())
				maxidEngine = rs.getInt("MAXID");
			int idQbeEngine = maxidEngine + 1;
			int idDashEngine = maxidEngine + 2;
			// get id of the domain 'DATAMART'
			sql = "SELECT VALUE_ID FROM SBI_DOMAINS WHERE VALUE_CD = 'DATAMART'";
			rs = stmt.executeQuery(sql);
			rs.next();
			int idDatamartDom = rs.getInt("VALUE_ID");
			// get id of the domain 'DASH'
			sql = "SELECT VALUE_ID FROM SBI_DOMAINS WHERE VALUE_CD = 'DASH'";
			rs = stmt.executeQuery(sql);
			rs.next();
			int idDashDom = rs.getInt("VALUE_ID");
			// insert the default internal engines
			sql = "INSERT INTO SBI_ENGINES VALUES("+idQbeEngine+",0,'Qbe Internal Engine','Qbe Internal Engine','','','','','','QbeInternalEngine',"+idIntEngDom+",'it.eng.spagobi.engines.datamart.SpagoBIQbeInternalEngine',"+idDatamartDom+")"; 
			stmt.execute(sql);
			sql = "INSERT INTO SBI_ENGINES VALUES("+idDashEngine+",0,'Dashboard Internal Engine','Dashboard Internal Engine','','','','','','DashboardInternalEng',"+idIntEngDom+",'it.eng.spagobi.engines.dashboard.SpagoBIDashboardInternalEngine',"+idDashDom+")";
			stmt.execute(sql);
			// for each document, check if it is a dash or datamart and change its egine
			sql = "SELECT * FROM SBI_OBJECTS";
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				int biobjid = rs.getInt("BIOBJ_ID");
				String biobjcode = rs.getString("BIOBJ_TYPE_CD");
				if(biobjcode.equals("DASH")) {
					sql = "UPDATE SBI_OBJECTS SET ENGINE_ID = "+idDashEngine+" WHERE BIOBJ_ID = "+biobjid;
					stmt.executeUpdate(sql);
				}
				if(biobjcode.equals("DATAMART")) {
					sql = "UPDATE SBI_OBJECTS SET ENGINE_ID = "+idQbeEngine+" WHERE BIOBJ_ID = "+biobjid;
					stmt.executeUpdate(sql);
				}
			}
			
			// erase the engines no more useful (into the 1.8 version the dash and qbe objects has a default engine for db key reason)
			sql = "SELECT ENGINE_ID FROM SBI_ENGINES";
			rs = stmt.executeQuery(sql);
			ResultSet rs1 = null;
			while(rs.next()) {
				int engineid = rs.getInt("ENGINE_ID");
				sql = "SELECT BIOBJ_ID FROM SBI_OBJECTS WHERE ENGINE_ID = " + engineid;
				rs1 = stmt.executeQuery(sql);
				if(!rs1.next()) {
					sql = "DELETE FROM SBI_ENGINES WHERE ENGINE_ID = " + engineid;
					stmt.execute(sql);
				}
			}
			
			conn.commit();
			conn.close();
		} catch (Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "changeDatabase",
		                           "Error while changing database " + e);	
		}
	}
	
	
	
	
	private void decompressArchive(String pathImpTmpFold, String archiveName, byte[] archiveCont) throws Exception {
		// create directories of the tmp import folder
		File impTmpFold = new File(pathImpTmpFold);
		impTmpFold.mkdirs();
		// write content uploaded into a tmp archive
		String pathArchiveFile = pathImpTmpFold + "/" +archiveName;
		File archive = new File(pathArchiveFile);
		FileOutputStream fos = new FileOutputStream(archive); 
		fos.write(archiveCont);
		fos.flush();
		fos.close();
		// decompress archive
		ImportUtilities.decompressArchive(pathImpTmpFold, pathArchiveFile);
		// erase archive file 
		archive.delete();
	}
	
	
	private Connection getConnectionToDatabase(String pathImpTmpFolder, String archiveName) {
		Connection connection = null;
		try{
			String driverName = "org.hsqldb.jdbcDriver";
			Class.forName(driverName);
			String url = "jdbc:hsqldb:file:" + pathImpTmpFolder + "/" + archiveName + "/metadata/metadata;shutdown=true"; 
			String username = "sa";
			String password = "";
			connection = DriverManager.getConnection(url, username, password);
			connection.setAutoCommit(true);
		} catch (Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "getConnectionToDatabase",
					               "Error while getting connection to database " + e);	
		}
		return connection;
	}
	
	
	
	/**
	 * Creates the compress export file
	 * @return The path of the exported compress file
	 * @throws EMFUserError
	 */
	private byte[] createExportArchive(String pathExportFolder, String nameExportFile) throws EMFUserError {
		byte[] content = null;
		String archivePath = pathExportFolder + "/" + nameExportFile + ".zip";
		File archiveFile = new File(archivePath);
		if(archiveFile.exists()){
			archiveFile.delete();
		}
		String pathBase = pathExportFolder + "/" + nameExportFile;
		try{
			FileOutputStream fos = new FileOutputStream(archivePath);
			ZipOutputStream out = new ZipOutputStream(fos);
			compressFolder(pathExportFolder, pathBase, out);
			out.flush();
			out.close();
			fos.close();
			FileInputStream fis = new FileInputStream(archivePath);
			content = GeneralUtilities.getByteArrayFromInputStream(fis);
			fis.close();
		} catch (Exception e){
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "createExportArchive",
					   			   "Error while creating archive file " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8005, "component_impexp_messages");
		}
		return content;
	}

	
	
	/**
	 * Compress contents of a folder into an output stream
	 * @param pathFolder The path of the folder to compress
	 * @param out The Compress output stream
	 * @throws EMFUserError
	 */
	private void compressFolder(String pathExportFolder, String pathFolder, ZipOutputStream out) throws EMFUserError {
		File folder = new File(pathFolder);
		String[] entries = folder.list();
	    byte[] buffer = new byte[4096];   
	    int bytes_read;
	    try{
		    for(int i = 0; i < entries.length; i++) {
		      File f = new File(folder, entries[i]);
		      if(f.isDirectory()) {  
		    	  compressFolder(pathExportFolder, pathFolder + "/" + f.getName(), out); 
		      } else {
		    	  FileInputStream in = new FileInputStream(f); 
		    	  String completeFileName = pathFolder + "/" + f.getName();
		    	  String relativeFileName = f.getName();
		    	  if(completeFileName.lastIndexOf(pathExportFolder)!=-1) {
		    		  int index = completeFileName.lastIndexOf(pathExportFolder);
		    		  int len = pathExportFolder.length();
		    		  relativeFileName = completeFileName.substring(index + len + 1);
		    	  }
		    	  ZipEntry entry = new ZipEntry(relativeFileName);  
		    	  out.putNextEntry(entry);                     
		    	  while((bytes_read = in.read(buffer)) != -1)  
		    		  out.write(buffer, 0, bytes_read);
		    	  in.close();
		      }
		    }
	    } catch (Exception e) {
	    	SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "compressSingleFolder",
	    						   "Error while creating archive file " + e);
	    	throw new EMFUserError(EMFErrorSeverity.ERROR, 8005, "component_impexp_messages");
	    }
	}
	
	
	
}
