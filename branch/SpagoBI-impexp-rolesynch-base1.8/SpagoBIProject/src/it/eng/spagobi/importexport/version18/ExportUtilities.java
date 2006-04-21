package it.eng.spagobi.importexport.version18;

import it.eng.spago.dbaccess.sql.DataConnection;
import it.eng.spago.dbaccess.sql.mappers.SQLMapper;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class ExportUtilities {

	public static void copyMetadataScript(String pathDBFolder) throws EMFUserError {
		try {
	        Thread curThread = Thread.currentThread();
	        ClassLoader classLoad = curThread.getContextClassLoader();
	        String resource = "it/eng/spagobi/importexport/version18/metadata.script";
	        InputStream ismetadata = classLoad.getResourceAsStream(resource);
	        String pathDBFile = pathDBFolder + "/metadata.script";
	        FileOutputStream fos = new FileOutputStream(pathDBFile);
	        int read = 0;
	        while( (read = ismetadata.read()) != -1) {
	        	fos.write(read);
	        }
	        fos.flush();
	        fos.close();
	        ismetadata.close();
        } catch (Exception e) {
        	SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, "ExportUtilities" , "copyMetadataScript",
        			"Error during the copy of the metadata exportdatabase script " + e);
        	throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
        }
	}
	
	
	
	
	public static void copyMetadataScriptProperties(String pathDBFolder) throws EMFUserError {
		try {
	        Thread curThread = Thread.currentThread();
	        ClassLoader classLoad = curThread.getContextClassLoader();
	        String resource = "it/eng/spagobi/importexport/version18/metadata.properties";
	        InputStream ismetadata = classLoad.getResourceAsStream(resource);
	        String pathDBFile = pathDBFolder + "/metadata.properties";
	        FileOutputStream fos = new FileOutputStream(pathDBFile);
	        int read = 0;
	        while( (read = ismetadata.read()) != -1) {
	        	fos.write(read);
	        }
	        fos.flush();
	        fos.close();
	        ismetadata.close();
        } catch (Exception e) {
        	SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, "ExportUtilities" , "copyMetadataScriptProperties",
        			"Error during the copy of the metadata exportdatabase properties " + e);
        	throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
        }
	}
	
	
	
	
	public static SessionFactory getHibSessionExportDB(String pathDBFolder) throws EMFUserError {
		Configuration conf = new Configuration();
		String resource = "it/eng/spagobi/importexport/version18/hibernate.cfg.hsql.export.xml";
		conf = conf.configure(resource);
		String hsqlJdbcString = "jdbc:hsqldb:file:" + pathDBFolder + "/metadata;shutdown=true";
		conf.setProperty("hibernate.connection.url",hsqlJdbcString);
		SessionFactory sessionFactory = conf.buildSessionFactory();
		return sessionFactory;
	}
	
	
	
	public static Connection getConnectionExportDB(String pathDBFolder) throws EMFUserError {
		Connection sqlconn = null;
		try {
			String driverName = "org.hsqldb.jdbcDriver";
			Class.forName(driverName);
	        String url = "jdbc:hsqldb:file:" + pathDBFolder + "/metadata;shutdown=true";
	        String username = "sa";
	        String password = "";
	         sqlconn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
        	SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, "ExportUtilities" , "copyMetadataScript",
        			"Error while getting connection to export database " + e);
        	throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
        }
        return sqlconn;
	}
	
	
	
	public static DataConnection getDataConnection(Connection con) throws EMFInternalError {
		DataConnection dataCon = null;
		try {
			Class mapperClass = Class.forName("it.eng.spago.dbaccess.sql.mappers.OracleSQLMapper");
			SQLMapper sqlMapper = (SQLMapper)mapperClass.newInstance();
			dataCon = new DataConnection(con, "2.1", sqlMapper);
		} catch(Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, "ExportUtilities" , "getDataConnection",
        			"Error while getting Spago  DataConnection " + e);
			throw new EMFInternalError(EMFErrorSeverity.ERROR, "cannot build DataConnection object");
		}
		return dataCon;
	}
	
	
	
	
	
	
}
