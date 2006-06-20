package it.eng.spagobi.importexport.transformers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.importexport.ITransformer;
import it.eng.spagobi.importexport.ImportExportConstants;
import it.eng.spagobi.importexport.ImportUtilities;
import it.eng.spagobi.metadata.HibernateUtil;
import it.eng.spagobi.metadata.SbiEngines;
import it.eng.spagobi.metadata.SbiFunctions;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

public class TransformerFrom1_8To1_9 implements ITransformer {

	public byte[] transform(byte[] content, String pathImpTmpFolder, String archiveName) {
		try{
			decompressArchive(pathImpTmpFolder, archiveName, content);
		} catch(Exception e) {
			System.out.println(e);
		}
		archiveName = archiveName.substring(0, archiveName.lastIndexOf('.'));
		changeDatabase(pathImpTmpFolder, archiveName);
		String baseCmsFolder = "";
		try {
			baseCmsFolder = getCmsBaseFolder(pathImpTmpFolder, archiveName);
		} catch(Exception e) {
			System.out.println(e);
		}	
		// associate objects with functionalities
		createObjectsFunctsAssociations(pathImpTmpFolder, archiveName);
		// update path of functionalitie
		updateFunctionalityPaths(baseCmsFolder, pathImpTmpFolder, archiveName);
		// compress archive
		try {
			content = createExportArchive(pathImpTmpFolder, archiveName);
		} catch (Exception e) {
			System.out.println(e);
		}
		// delete tmp dir content
		File tmpDir = new File(pathImpTmpFolder);
		GeneralUtilities.deleteContentDir(tmpDir);
		return content;
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
			System.out.println(e);
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
			System.out.println(e);
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
			//sql =  "ALTER TABLE SBI_OBJ_PAR RENAME TO SBI_OBJ_PAR_2";
			//stmt.execute(sql);
			//sql =  "ALTER TABLE SBI_OBJ_PAR_2 DROP CONSTRAINT XPKSBI_OBJ_PAR";
			//stmt.execute(sql);
			//sql =  "INSERT INTO SBI_OBJ_PAR (PAR_ID,BIOBJ_ID,LABEL,REQ_FL,MOD_FL,VIEW_FL,MULT_FL,PROG,PARURL_NM) SELECT PAR_ID,BIOBJ_ID,LABEL,REQ_FL,MOD_FL,VIEW_FL,MULT_FL,PROG,PARURL_NM FROM SBI_OBJ_PAR_2";
			//stmt.execute(sql);
			//sql =  "DROP TABLE SBI_OBJ_PAR_2";
			//stmt.execute(sql);
			//sql =  "ALTER TABLE SBI_OBJ_PAR ADD CONSTRAINT FK_SBI_OBJ_PAR_1 FOREIGN KEY(BIOBJ_ID) REFERENCES SBI_OBJECTS(BIOBJ_ID)";
			//stmt.execute(sql);
			//sql =  "ALTER TABLE SBI_OBJ_PAR ADD CONSTRAINT FK_SBI_OBJ_PAR_2 FOREIGN KEY(PAR_ID) REFERENCES SBI_PARAMETERS(PAR_ID)";
			//stmt.execute(sql);
			sql =  "CREATE MEMORY TABLE SBI_OBJ_PARUSE (OBJ_PAR_ID INTEGER NOT NULL, USE_ID INTEGER NOT NULL, OBJ_PAR_FATHER_ID INTEGER NOT NULL, FILTER_COLUMN VARCHAR NOT NULL, FILTER_OPERATION VARCHAR NOT NULL, CONSTRAINT XPKSBI_OBJ_PARUSE PRIMARY KEY(OBJ_PAR_ID,USE_ID))";
			stmt.execute(sql);
			// calculate max id for sbidomains
			sql = "SELECT MAX(VALUE_ID ) AS MAXID FROM SBI_DOMAINS";
			ResultSet rs = stmt.executeQuery(sql);
			int maxid = 1000;
			if(rs.next())
				maxid = rs.getInt("MAXID");
			// insert sbidomains for engine using maxid
			sql =  "INSERT INTO SBI_DOMAINS (VALUE_ID, VALUE_CD,VALUE_NM,DOMAIN_CD,DOMAIN_NM,VALUE_DS) VALUES("+(maxid+1)+", 'EXT','External Engine','ENGINE_TYPE','Engine types','Business intelligence external engine of SpagoBI platform')";
			stmt.execute(sql);
			sql =  "INSERT INTO SBI_DOMAINS (VALUE_ID, VALUE_CD,VALUE_NM,DOMAIN_CD,DOMAIN_NM,VALUE_DS) VALUES("+(maxid+2)+", 'INT','Internal Engine','ENGINE_TYPE','Engine types','Business intelligence internal engine of SpagoBI platform')";
			stmt.execute(sql);
			sql =  "ALTER TABLE sbi_engines ADD COLUMN ENGINE_TYPE INTEGER";
			stmt.execute(sql);
			sql =  "ALTER TABLE sbi_engines ADD COLUMN CLASS_NM VARCHAR";
			stmt.execute(sql);
			sql =  "ALTER TABLE sbi_engines ADD COLUMN BIOBJ_TYPE INTEGER";
			stmt.execute(sql);
			//sql =  "ALTER TABLE sbi_engines ADD CONSTRAINT FK_SBI_ENGINES_1 FOREIGN KEY(BIOBJ_TYPE) REFERENCES SBI_DOMAINS(VALUE_ID)";
			//stmt.execute(sql);
			//sql =  "ALTER TABLE sbi_engines ADD CONSTRAINT FK_SBI_ENGINES_2 FOREIGN KEY(ENGINE_TYPE) REFERENCES SBI_DOMAINS(VALUE_ID)";
			//stmt.execute(sql);
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
			conn.commit();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
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
			System.out.println(e);
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
	
	
	
	
	
	
	
	
	
	
	/*
	private void updateFunctionalitiesReference(String pathImpTmpFolder, String archiveName) {
		Connection conn = null;
		try{
			conn = getConnectionToDatabase(pathImpTmpFolder, archiveName);
			String sql = "SELECT * FROM SBI_FUNCTIONS";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			ResultSet rs1 = null;
			while(rs.next()) {
				String path = rs.getString("PATH");
				int functId = rs.getInt("FUNCT_ID");
				String parentPath = path.substring(0, path.lastIndexOf('/'));
				if((parentPath!=null)&&!parentPath.trim().equals("")) {
					sql = "SELECT FUNCT_ID FROM SBI_FUNCTIONS WHERE PATH = '"+parentPath+"'";
					rs1 = stmt.executeQuery(sql);
					if(rs1.next()) {
						int parFunctId = rs1.getInt("FUNCT_ID");
						sql = "UPDATE SBI_FUNCTIONS SET PARENT_FUNCT_ID = "+parFunctId+" WHERE FUNCT_ID = " + functId;
						stmt.executeUpdate(sql);
					}
				}
			}
			conn.commit();
			conn.close();	
			
			
			Session sess = HibernateUtil.currentSession();
			Transaction tx = sess.beginTransaction();
			Query hibQuery = sess.createQuery("from SbiFunctions where parentFunct is null");
			SbiFunctions functRoot = (SbiFunctions)hibQuery.uniqueResult();
			Integer idFunctRoot = functRoot.getFunctId();
			tx.commit();
			sess.close();
			conn = getConnectionToDatabase(pathImpTmpFolder, archiveName);
			stmt = conn.createStatement();
			sql = "UPDATE SBI_FUNCTIONS SET PARENT_FUNCT_ID = "+idFunctRoot+" WHERE PARENT_FUNCT_ID IS NULL";
			stmt.executeUpdate(sql);
			conn.commit();
			conn.close();	
		   
			
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	 */
	
	
}
