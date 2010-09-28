/**
 * 
 */
package it.eng.spagobi.meta.model.commons;

import java.sql.Types;

/**
 * @see http://download.oracle.com/javase/1.4.2/docs/api/java/sql/Types.html
 * @see http://download.oracle.com/javase/1.3/docs/guide/jdbc/getstart/mapping.html
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class JDBCTypeMapper {
	public static Class getJavaType(short jdbcType) {
		switch(jdbcType) {
			case Types.ARRAY: 			return java.sql.Array.class;
			case Types.BIGINT: 			return java.lang.Long.class;
			case Types.BINARY: 			return java.lang.Byte[].class;
			case Types.BIT: 			return java.lang.Boolean.class;
			case Types.BLOB: 			return java.sql.Blob.class;
			case Types.CHAR: 			return java.lang.String.class;
			case Types.CLOB: 			return java.sql.Clob.class;
			case Types.DATE: 			return java.sql.Date.class;
			case Types.DECIMAL: 		return java.math.BigDecimal.class;
			case Types.DISTINCT: 		return java.lang.Object.class;
			case Types.DOUBLE: 			return java.lang.Double.class;
			case Types.FLOAT: 			return java.lang.Double.class;
			case Types.INTEGER: 		return java.lang.Integer.class;
			case Types.JAVA_OBJECT: 	return java.lang.Object.class;
			case Types.LONGVARBINARY: 	return java.lang.Byte[].class;
			case Types.LONGVARCHAR: 	return java.lang.String.class;
			case Types.NULL: 			return java.lang.Object.class;
			case Types.NUMERIC: 		return java.math.BigDecimal.class;
			case Types.OTHER: 			return java.lang.Object.class;
			case Types.REAL: 			return java.lang.Float.class;
			case Types.REF: 			return java.sql.Ref.class;
			case Types.SMALLINT: 		return java.lang.Short.class;
			case Types.STRUCT: 			return java.sql.Struct.class;
			case Types.TIME: 			return java.sql.Time.class;
			case Types.TIMESTAMP: 		return java.sql.Timestamp.class;
			case Types.TINYINT: 		return java.lang.Byte.class;
			case Types.VARBINARY: 		return java.lang.Byte[].class;
			case Types.VARCHAR: 		return java.lang.String.class;
			default: 					return null;
			
		}
	}
}
