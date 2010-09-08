/**
 * 
 */
package it.eng.spagobi.meta.test;

import it.eng.spagobi.meta.cwm.CWMImplType;
import it.eng.spagobi.meta.cwm.CWMMapperFactory;
import it.eng.spagobi.meta.cwm.ICWM;
import it.eng.spagobi.meta.cwm.ICWMMapper;
import it.eng.spagobi.meta.initializer.PhysicalModelInitializer;
import it.eng.spagobi.meta.model.physical.PhysicalModel;

import java.sql.Connection;
import java.sql.DriverManager;


/**
 * @author agioia
 *
 */
public class CreateModelTest {
	
	public static String FILENAME = "D:\\Documenti\\Progetti\\metadati\\workspace\\SpagoBIMetaData\\model.xml";
	
	public static String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	public static String MYSQL_URL = "jdbc:mysql://localhost:3306/spagobi_local";
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
	

	
	public static PhysicalModel getModelFromMySQLConnection() throws Exception {
		Class.forName(MYSQL_DRIVER).newInstance();			  
        Connection conn = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PWD);
        PhysicalModelInitializer modelInitializer = new PhysicalModelInitializer();
        PhysicalModel model = modelInitializer.initizlize( "modeldemo", conn,  MYSQL_DEFAULT_CATALOGUE, MYSQL_DEFAULT_SCHEMA);
        return model;
	}
	
	public static PhysicalModel  getModelFromPostgresConnection() throws Exception {
		Class.forName(POSTGRES_DRIVER).newInstance();			  
        Connection conn = DriverManager.getConnection(POSTGRES_URL, POSTGRES_USER, POSTGRES_PWD);
        PhysicalModelInitializer modelInitializer = new PhysicalModelInitializer();
        PhysicalModel model = modelInitializer.initizlize( "modeldemo", conn,  POSTGRES_DEFAULT_CATALOGUE, POSTGRES_DEFAULT_SCHEMA);
        return model;
	}
	
	public static PhysicalModel getModelFromOracleConnection() throws Exception {
		Class.forName(ORACLE_DRIVER).newInstance();			  
        Connection conn = DriverManager.getConnection(ORACLE_URL, ORACLE_USER, ORACLE_PWD);
        PhysicalModelInitializer modelInitializer = new PhysicalModelInitializer();
        PhysicalModel model = modelInitializer.initizlize( "modeldemo", conn,  ORACLE_DEFAULT_CATALOGUE, ORACLE_DEFAULT_SCHEMA);
        return model;
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
	
		PhysicalModel model;
		PhysicalModelInitializer modelInitializer;
		ICWMMapper modelMapper;
		ICWM cwm;
		        
        // db schema -> spagobi model -> cwm (jmi) -> xmi
		       
        //model = getModelFromOracleConnection();
        //model = getModelFromPostgresConnection();
        model = getModelFromMySQLConnection();
        
        modelMapper = CWMMapperFactory.getMapper(CWMImplType.JMI);
        cwm = modelMapper.encodeModel(model);        
        cwm.exportToXMI(FILENAME);
     
	}

}
