
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
	        case MYSQL:   
	        	catalogue = TestCostants.MYSQL_DEFAULT_CATALOGUE;
	        	break;
	        case POSTGRES:  
	        	catalogue = TestCostants.POSTGRES_DEFAULT_CATALOGUE;
	        	break;
	        case ORACLE:  
	        	catalogue = TestCostants.ORACLE_DEFAULT_CATALOGUE;
	        	break;
		}
		
		return catalogue;
	}
	
	public static String getDefaultSchema(TestCostants.DatabaseType type) {
		String schema;
		
		schema = null;
		switch(type) {
        	case MYSQL:   
        		schema = TestCostants.MYSQL_DEFAULT_SCHEMA;
        		break;
        	case POSTGRES:  
        		schema = TestCostants.POSTGRES_DEFAULT_SCHEMA;
        		break;
        	case ORACLE:  
        		schema = TestCostants.ORACLE_DEFAULT_SCHEMA;
        		break;
		}
		
		return schema;
	}
	
	public static Connection createConnection(TestCostants.DatabaseType type) {
		Connection connection;
		
		connection = null;
		switch(type) {
	        case MYSQL:   
	        	connection = createConnection(TestCostants.MYSQL_URL, TestCostants.MYSQL_USER, TestCostants.MYSQL_PWD, TestCostants.MYSQL_DRIVER);
	        	break;
	        case POSTGRES:  
	        	connection = createConnection(TestCostants.POSTGRES_URL, TestCostants.POSTGRES_USER, TestCostants.POSTGRES_PWD, TestCostants.POSTGRES_DRIVER);
	        	break;
	        case ORACLE:  
	        	connection = createConnection(TestCostants.ORACLE_URL, TestCostants.ORACLE_USER, TestCostants.ORACLE_PWD, TestCostants.ORACLE_DRIVER);
	        	break;
		}
		
		return connection;
	}
	
	public static Connection createConnection(String url, String usr, String pwd, String driver) {
		Connection connection;
		
		connection = null;
		try {
			java.sql.Driver  o = (java.sql.Driver )Class.forName(driver).newInstance();			  
			boolean b = o.acceptsURL(url);
			connection = DriverManager.getConnection(url, usr, pwd);
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
