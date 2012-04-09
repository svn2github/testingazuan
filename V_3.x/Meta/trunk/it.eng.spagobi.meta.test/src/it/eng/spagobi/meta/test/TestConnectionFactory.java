
package it.eng.spagobi.meta.test;

import java.sql.Connection;
import java.sql.DriverManager;


/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class TestConnectionFactory {
	


	
	public static String getDefaultCatalogue(TestCostants.DatabaseType type) {
		String catalogue;
		
		catalogue = null;
		switch(type) {
        case MYSQL:   catalogue = TestCostants.MYSQL_DEFAULT_CATALOGUE;
        case POSTGRES:  catalogue = TestCostants.POSTGRES_DEFAULT_CATALOGUE;
        case ORACLE:  catalogue = TestCostants.ORACLE_DEFAULT_CATALOGUE;
		}
		
		return catalogue;
	}
	
	public static String getDefaultSchema(TestCostants.DatabaseType type) {
		String schema;
		
		schema = null;
		switch(type) {
        case MYSQL:   schema = TestCostants.MYSQL_DEFAULT_SCHEMA;
        case POSTGRES:  schema = TestCostants.POSTGRES_DEFAULT_SCHEMA;
        case ORACLE:  schema = TestCostants.ORACLE_DEFAULT_SCHEMA;
		}
		
		return schema;
	}
	
	public static Connection createConnection(TestCostants.DatabaseType type) {
		Connection connection;
		
		connection = null;
		switch(type) {
        case MYSQL:   connection = createConnection(TestCostants.MYSQL_URL, TestCostants.MYSQL_USER, TestCostants.MYSQL_PWD, TestCostants.MYSQL_DRIVER);
        case POSTGRES:  connection = createConnection(TestCostants.POSTGRES_URL, TestCostants.POSTGRES_USER, TestCostants.POSTGRES_PWD, TestCostants.POSTGRES_DRIVER);
        case ORACLE:  connection = createConnection(TestCostants.ORACLE_URL, TestCostants.ORACLE_USER, TestCostants.ORACLE_PWD, TestCostants.ORACLE_DRIVER);
		}
		
		return connection;
	}
	
	public static Connection createConnection(String url, String usr, String pwd, String driver) {
		Connection connection;
		
		connection = null;
		try {
			com.mysql.jdbc.Driver o = (com.mysql.jdbc.Driver)Class.forName(TestCostants.MYSQL_DRIVER).newInstance();			  
			boolean b = o.acceptsURL(TestCostants.MYSQL_URL);
			connection = DriverManager.getConnection(TestCostants.MYSQL_URL, TestCostants.MYSQL_USER, TestCostants.MYSQL_PWD);
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
