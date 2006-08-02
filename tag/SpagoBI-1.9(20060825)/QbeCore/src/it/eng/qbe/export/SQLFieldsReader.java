/**
 * 
 */
package it.eng.qbe.export;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Class that can get fields (i.e name and type) from a sql query string
 * 
 * @author Gioia
 */
public class SQLFieldsReader implements IFieldsReader {

	private String query;
	private Connection connection;

	public SQLFieldsReader(String query, Connection connection) {
		this.query = query;
		this.connection = connection;
	}
	
	
	public Vector readFields() throws Exception {
		 Vector queryFields = new Vector();
		
        PreparedStatement ps = null;
        try {
        	ps = connection.prepareStatement( query );
             
            // Some JDBC drivers don't supports this method...
            try { ps.setFetchSize(0); } catch(Exception e ) {}
             
             
             ResultSet rs = ps.executeQuery();             
             ResultSetMetaData rsmd = rs.getMetaData();             
             
             List columns = new ArrayList();
             for (int i=1; i <= rsmd.getColumnCount(); ++i) {
            	 Field field = new Field(
                         rsmd.getColumnLabel(i), 
                         getJdbcTypeClass(rsmd, i) );
                 
            	 queryFields.add( field );
             }
         }
         catch(Exception e) {
        	 e.printStackTrace();
         }
		
		
		return queryFields;
	}
	
	public static String getJdbcTypeClass(ResultSetMetaData rsmd, int t ) {
         String cls = "java.lang.Object";

         try {
             cls = rsmd.getColumnClassName(t);
             cls =  Field.getFieldType(cls);

         } catch (Exception ex) {
             // if getColumnClassName is not supported...
             try {
                 int type = rsmd.getColumnType(t);
                 switch( type ) {
                         case java.sql.Types.TINYINT:
                         case java.sql.Types.BIT:
                                 cls = "java.lang.Byte";
                                 break;
                         case java.sql.Types.SMALLINT:
                                 cls = "java.lang.Short";
                                 break;
                         case java.sql.Types.INTEGER:
                                 cls = "java.lang.Integer";
                                 break;
                         case java.sql.Types.FLOAT:
                         case java.sql.Types.REAL:
                         case java.sql.Types.DOUBLE:
                         case java.sql.Types.NUMERIC:
                         case java.sql.Types.DECIMAL:
                                 cls = "java.lang.Double";
                                 break;
                         case java.sql.Types.CHAR:
                         case java.sql.Types.VARCHAR:
                                 cls = "java.lang.String";
                                 break;

                         case java.sql.Types.BIGINT:
                                 cls = "java.lang.Long";
                                 break;
                         case java.sql.Types.DATE:
                                 cls = "java.util.Date";
                                 break;
                         case java.sql.Types.TIME:
                                 cls = "java.sql.Time";
                                 break;
                         case java.sql.Types.TIMESTAMP:
                                 cls = "java.sql.Timestamp";
                                 break;
                 }
             } catch (Exception ex2){
                 ex2.printStackTrace();
             }
         }
         return cls;
	 }
	
}
