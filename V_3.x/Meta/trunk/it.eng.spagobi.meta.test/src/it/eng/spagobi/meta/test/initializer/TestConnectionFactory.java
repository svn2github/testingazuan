
package it.eng.spagobi.meta.test.initializer;

import java.sql.Connection;
import java.sql.DriverManager;


/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class TestConnectionFactory {
	
	public static String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	public static String MYSQL_URL = "jdbc:mysql://localhost:3306/foodmart_key";
	public static String MYSQL_USER = "root";
	public static String MYSQL_PWD = "mysql";
	public static String MYSQL_DEFAULT_CATALOGUE = null; //"foodmart";		
	public static String MYSQL_DEFAULT_SCHEMA = null;	
	
	public static String POSTGRES_DRIVER = "org.postgresql.Driver";
	public static String POSTGRES_URL = "jdbc:postgresql://localhost:5432/geodwh";
	public static String POSTGRES_USER = "geodwh";
	public static String POSTGRES_PWD = "geodwh";
	public static String POSTGRES_DEFAULT_CATALOGUE = null;	
	public static String POSTGRES_DEFAULT_SCHEMA = "public";	
	
	public static String ORACLE_DRIVER = "oracle.jdbc.OracleDriver";
	public static String ORACLE_URL = "jdbc:oracle:thin:@172.27.1.90:1521:REPO";
	public static String ORACLE_USER = "spagobi";
	public static String ORACLE_PWD = "bispago";	
	public static String ORACLE_DEFAULT_CATALOGUE = null;	
	public static String ORACLE_DEFAULT_SCHEMA = "SPAGOBI";	
	
	public enum DatabaseType { MYSQL, POSTGRES, ORACLE };

	
	public static String getDefaultCatalogue(DatabaseType type) {
		String catalogue;
		
		catalogue = null;
		switch(type) {
        case MYSQL:   catalogue = MYSQL_DEFAULT_CATALOGUE;
        case POSTGRES:  catalogue = POSTGRES_DEFAULT_CATALOGUE;
        case ORACLE:  catalogue = ORACLE_DEFAULT_CATALOGUE;
		}
		
		return catalogue;
	}
	
	public static String getDefaultSchema(DatabaseType type) {
		String schema;
		
		schema = null;
		switch(type) {
        case MYSQL:   schema = MYSQL_DEFAULT_SCHEMA;
        case POSTGRES:  schema = POSTGRES_DEFAULT_SCHEMA;
        case ORACLE:  schema = ORACLE_DEFAULT_SCHEMA;
		}
		
		return schema;
	}
	
	public static Connection createConnection(DatabaseType type) {
		Connection connection;
		
		connection = null;
		switch(type) {
        case MYSQL:   connection = createConnection(MYSQL_URL, MYSQL_USER, MYSQL_PWD, MYSQL_DRIVER);
        case POSTGRES:  connection = createConnection(POSTGRES_URL, POSTGRES_USER, POSTGRES_PWD, POSTGRES_DRIVER);
        case ORACLE:  connection = createConnection(ORACLE_URL, ORACLE_USER, ORACLE_PWD, ORACLE_DRIVER);
		}
		
		return connection;
	}
	
	public static Connection createConnection(String url, String usr, String pwd, String driver) {
		Connection connection;
		
		connection = null;
		try {
			com.mysql.jdbc.Driver o = (com.mysql.jdbc.Driver)Class.forName(MYSQL_DRIVER).newInstance();			  
			boolean b = o.acceptsURL(MYSQL_URL);
			connection = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PWD);
		} catch (Throwable t){
			throw new RuntimeException("Impossible to create connection [url: " + url
					+ "; usr: " + usr
					+ "; pwd: " + pwd
					+ "; driver: " + driver
					+ "]", t);
		}
        return connection;
	}
}
