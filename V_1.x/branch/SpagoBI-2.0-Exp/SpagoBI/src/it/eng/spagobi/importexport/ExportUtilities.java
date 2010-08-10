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
package it.eng.spagobi.importexport;

import it.eng.spago.dbaccess.sql.DataConnection;
import it.eng.spago.dbaccess.sql.mappers.SQLMapper;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.importexport.ImportExportConstants;
import it.eng.spagobi.utilities.SpagoBITracer;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Imlements utilities for export purposes
 */
public class ExportUtilities {

	
	/**
	 * Copy the metadata script of the exported database into the export folder 
	 * @param pathDBFolder Path of the export database folder
	 * @throws EMFUserError
	 */
	public static void copyMetadataScript(String pathDBFolder) throws EMFUserError {
		try {
	        Thread curThread = Thread.currentThread();
	        ClassLoader classLoad = curThread.getContextClassLoader();
	        String resource = "it/eng/spagobi/importexport/metadata/exportdb/metadata.script";
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
        	SpagoBITracer.major(ImportExportConstants.NAME_MODULE, "ExportUtilities" , "copyMetadataScript",
        			"Error during the copy of the metadata exportdatabase script " + e);
        	throw new EMFUserError(EMFErrorSeverity.ERROR, "100", "component_impexp_messages");
        }
	}
	
	
	
	/**
	 * Copy the properties file of the exported database into the export folder 
	 * @param pathDBFolder Path of the export database folder
	 * @throws EMFUserError
	 */
	public static void copyMetadataScriptProperties(String pathDBFolder) throws EMFUserError {
		try {
	        Thread curThread = Thread.currentThread();
	        ClassLoader classLoad = curThread.getContextClassLoader();
	        String resource = "it/eng/spagobi/importexport/metadata/exportdb/metadata.properties";
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
        	SpagoBITracer.major(ImportExportConstants.NAME_MODULE, "ExportUtilities" , "copyMetadataScriptProperties",
        			"Error during the copy of the metadata exportdatabase properties " + e);
        	throw new EMFUserError(EMFErrorSeverity.ERROR, "100", "component_impexp_messages");
        }
	}
	
	
	
	/**
	 * Creates an Hibernate session factory for the export database
	 * @param pathDBFolder Path of the export database folder
	 * @return The Hibernate Session Factory
	 * @throws EMFUserError
	 */
	public static SessionFactory getHibSessionExportDB(String pathDBFolder) throws EMFUserError {
		Configuration conf = new Configuration();
		String resource = "it/eng/spagobi/importexport/metadata/hibernate.cfg.hsql.export.xml";
		conf = conf.configure(resource);
		String hsqlJdbcString = "jdbc:hsqldb:file:" + pathDBFolder + "/metadata;shutdown=true";
		conf.setProperty("hibernate.connection.url",hsqlJdbcString);
		SessionFactory sessionFactory = conf.buildSessionFactory();
		return sessionFactory;
	}
	
	
	/**
	 * Creates a sql connection for the exported database
	 * @param pathDBFolder  Path of the export database folder
	 * @return Connection to the export database
	 * @throws EMFUserError
	 */
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
        	SpagoBITracer.major(ImportExportConstants.NAME_MODULE, "ExportUtilities" , "copyMetadataScript",
        			"Error while getting connection to export database " + e);
        	throw new EMFUserError(EMFErrorSeverity.ERROR, "100", "component_impexp_messages");
        }
        return sqlconn;
	}
	
	
	/**
	 * Creates a Spago DataConnection object starting from a connection to the export database
	 * @param con Connection to the export database
	 * @return The Spago DataConnection Object
	 * @throws EMFInternalError
	 */
	public static DataConnection getDataConnection(Connection con) throws EMFInternalError {
		DataConnection dataCon = null;
		try {
			Class mapperClass = Class.forName("it.eng.spago.dbaccess.sql.mappers.OracleSQLMapper");
			SQLMapper sqlMapper = (SQLMapper)mapperClass.newInstance();
			dataCon = new DataConnection(con, "2.1", sqlMapper);
		} catch(Exception e) {
			SpagoBITracer.major(ImportExportConstants.NAME_MODULE, "ExportUtilities" , "getDataConnection",
        			"Error while getting Spago  DataConnection " + e);
			throw new EMFInternalError(EMFErrorSeverity.ERROR, "cannot build DataConnection object");
		}
		return dataCon;
	}
	
	

}
