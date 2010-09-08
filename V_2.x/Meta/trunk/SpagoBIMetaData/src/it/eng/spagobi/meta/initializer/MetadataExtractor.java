/**
 * 
 */
package it.eng.spagobi.meta.initializer;

import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalForeignKey;
import it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class MetadataExtractor {
	
	private static void log(String msg) {
		System.out.println(msg);
	}
	
	
	public static String retriveCatalog(Connection conn, String defaultCatalog) {
		String catalog;		
		List<String> catalogs;
		DatabaseMetaData dbMeta;
		ResultSet rs;
		Iterator<String> it;
		
		catalog = null;
		
		try {
			
			catalog = conn.getCatalog();
			if(catalog == null) {
				dbMeta = conn.getMetaData();
				
				rs = dbMeta.getCatalogs();
				catalogs = new ArrayList();
				while (rs.next()) {
					catalogs.add( rs.getString(1) );
				}
				
				if(catalogs.size() == 0) {
					log("No schema [" + dbMeta.getSchemaTerm() + "] defined");
				} else if(catalogs.size() == 1) {
					catalog = catalogs.get(1);
				} else {
					String targetCatalog = null;
					it = catalogs.iterator();
					while (it.hasNext()) {
						String s = it.next();
						log("s [" + s + "]");
						if(s.equalsIgnoreCase(defaultCatalog)) {
							targetCatalog = defaultCatalog;
							break;
						}
					}
					if(targetCatalog == null) {
						throw new RuntimeException("No catalog named [" + defaultCatalog + "] is available on db");
					}
					catalog = targetCatalog;
				}
			}			
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to retrive target catalog", t);
		}
		
		return catalog;
	}
	
	public static String retriveSchema(DatabaseMetaData dbMeta, String defaultSchema) {
		String schema;
		List<String> schemas;
		ResultSet rs;
		Iterator<String> it;
		
		schema = null;
		
		try {
			rs = dbMeta.getSchemas();
			schemas = new ArrayList();
			while (rs.next()) {
				schemas.add( rs.getString(1) );
			}
			
			if(schemas.size() == 0) {
				log("No schema [" + dbMeta.getSchemaTerm() + "] defined");
			} else if(schemas.size() == 1) {
				schema = schemas.get(1);
			} else {
				String targetSchema = null;
				it = schemas.iterator();
				while (it.hasNext()) {
					String s = it.next();
					if(s.equalsIgnoreCase(defaultSchema)) {
						targetSchema = defaultSchema;
						break;
					}
				}
				if(targetSchema == null) {
					throw new RuntimeException("No schema named [" + defaultSchema + "] is available on db");
				}
				schema = targetSchema;
			}
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to retrive target schema", t);
		}
		
		return schema;
	}
	
	public static List<PhysicalTable> retriveTables(DatabaseMetaData dbMeta, String targetCatalog, String targetSchema) {
		List<PhysicalTable> tables;
		Map<String, PhysicalTable> tabesLookupMap;
		ResultSet tableRs, columnRs;
		PhysicalTable table;
		List<PhysicalColumn> columns;
		PhysicalColumn column;
		List<PhysicalPrimaryKey> primaryKeys;
		PhysicalPrimaryKey primaryKey;
		List<PhysicalForeignKey> foreignKeys;
		PhysicalForeignKey foreignKey;
		
		tables = new ArrayList();
				
		try {
			tableRs = dbMeta.getTables(targetCatalog, targetSchema, null, new String[]{"TABLE", "VIEW"});
			
			/* 
			--------------------------------------------------
			resultset's structure
			--------------------------------------------------
			1. TABLE_CAT String => table catalog (may be null)
			2. TABLE_SCHEM String => table schema (may be null)
			3. TABLE_NAME String => table name
			
			Data Warehouse Management Model 181
			4. TABLE_TYPE String => table type. Typical types are “TABLE”, “VIEW”, “SYSTEM TABLE”,”GLOBAL TEMPORARY”, “LOCAL TEMPORARY”, “ALIAS”, “SYNONYM”.
			5. REMARKS String => explanatory comment on the table
			6. TYPE_CAT String => the types catalog (may be null)
			7. TYPE_SCHEM String => the types schema (may be null)
			8. TYPE_NAME String => type name (may be null)
			9. SELF_REFERENCING_COL_NAME String => name of the designated “identifier” column of a typed table (may be null)
			10. REF_GENERATION String => specifies how values in SELF_REFERENCING_COL_NAME are created. Values are “SYSTEM”, “USER”, “DERIVED”. (may be null)
			 */
			while (tableRs.next()) {
				table = new PhysicalTable();
				
				table.setCatalog( tableRs.getString("TABLE_CAT") );
				table.setSchema( tableRs.getString("TABLE_SCHEM") );
				table.setName( tableRs.getString("TABLE_NAME") );
				table.setComment( tableRs.getString("REMARKS") );
				table.setType( tableRs.getString("TABLE_TYPE") );
				
				log("Table: " + table.getName() + "[" + table.getType() + "]");
				
				columns = retriveColumns(dbMeta, targetCatalog, targetSchema, table.getName());
				for(int i = 0; i < columns.size(); i++) {
					column = columns.get(i);
					table.addColumn(column);
				}
						
				tables.add(table);
			}
			
			
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to retrive tables metadata", t);
		}
		
		return tables;
	}
	
	public static List<PhysicalColumn> retriveColumns(DatabaseMetaData dbMeta, String targetCatalog, String targetSchema, String targetTable) {
		List<PhysicalColumn> columns;
		ResultSet rs;
		PhysicalColumn column;
		
		columns = new ArrayList();
		
		try {
			rs = dbMeta.getColumns(targetCatalog, targetSchema, targetTable, null);
			
			/*
			1.  TABLE_CAT String => table catalog (may be null)
			2.  TABLE_SCHEM String => table schema (may be null)
			3.  TABLE_NAME String => table name
			4.  COLUMN_NAME String => column name
			5.  DATA_TYPE short => SQL type from java.sql.Types
			6.  TYPE_NAME String => Data source dependent type name, for a UDT the type name is fully qualified
			7.  COLUMN_SIZE int => column size. For char or date types this is the maximum number of characters, for numeric or decimal types this is precision.
			8.  BUFFER_LENGTH is not used.
			9.  DECIMAL_DIGITS int => the number of fractional digits
			10. NUM_PREC_RADIX int => Radix (typically either 10 or 2)
			11. NULLABLE int => is NULL allowed. columnNoNulls - might not allow NULL values; columnNullable - definitely allows NULL values; columnNullableUnknown - nullability unknown
			12. REMARKS String => comment describing column (may be null)
			13. COLUMN_DEF String => default value (may be null)
			14. SQL_DATA_TYPE int => unused
			15. SQL_DATETIME_SUB int => unused
			16. CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in the column
			17. ORDINAL_POSITION int => index of column in table (starting at 1)
			18. IS_NULLABLE String => “NO” means column definitely does not allow NULL values; “YES” means the column might allow NULL values. An empty string means nobody knows.
			19. SCOPE_CATLOG String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn’t REF)
			20. SCOPE_SCHEMA String => schema of table that is the scope of a
			
			182 Chapter 5 reference attribute (null if the DATA_TYPE isn’t REF)
			21. SCOPE_TABLE String => table name that is the scope of a reference attribute (null if the DATA_TYPE isn’t REF)
			22. SOURCE_DATA_TYPE short => source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE isn’t DISTINCT or user-generated REF)
			 */
			while (rs.next()) 	{
				column = new PhysicalColumn();
				column.setName(rs.getString("COLUMN_NAME"));
				column.setComment( rs.getString("REMARKS") );
				column.setDataType( rs.getShort("DATA_TYPE") );
				column.setTypeName( rs.getString("TYPE_NAME") );
				column.setSize(rs.getInt("COLUMN_SIZE") );
				column.setOctectLength(rs.getInt("CHAR_OCTET_LENGTH") );
				column.setDecimalDigits(rs.getInt("DECIMAL_DIGITS") );
				column.setRadix( rs.getInt("NUM_PREC_RADIX") );
				column.setDefaultValue( rs.getString("COLUMN_DEF") );
				column.setNullable( !"NO".equalsIgnoreCase(rs.getString("IS_NULLABLE")) );
				column.setPosition( rs.getInt("ORDINAL_POSITION") );
				
				columns.add(column);
				log("  - column: " + column.getName() + " [" + column.getTypeName() + "]" + " [" + column.getDefaultValue() + "]");
			}
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to retrive primaryKeys metadata", t);
		}
		
		return columns;
	}
	
	public static PhysicalPrimaryKey retrivePrimaryKey(DatabaseMetaData dbMeta, String targetCatalog, String targetSchema, String targetTable) {
		PhysicalPrimaryKey primaryKey;
		ResultSet rs;
		
		primaryKey = null;
		
		try {
			rs = dbMeta.getPrimaryKeys(targetCatalog, targetSchema, targetTable);
			/*
			1. TABLE_CAT String => table catalog (may be null)
			2. TABLE_SCHEM String => table schema (may be null)
			3. TABLE_NAME String => table name
			4. COLUMN_NAME String => column name
			5. KEY_SEQ short => sequence number within primary key
			6. PK_NAME String => primary key name (may be null)
			*/
			
			
			while (rs.next()) {
				if(primaryKey == null) {
					primaryKey = new PhysicalPrimaryKey();
					primaryKey.setPkName( rs.getString("PK_NAME") );
					primaryKey.setTableName( rs.getString("TABLE_NAME") );
				}
				primaryKey.addColumnName( rs.getString("COLUMN_NAME") );
			}
			
			
		
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to retrive primaryKeys metadata", t);
		}
	
		return primaryKey;
	}
	
	
	public static List<PhysicalForeignKey> retriveForeignKey(DatabaseMetaData dbMeta, String targetCatalog, String targetSchema, String targetTable) {
		List<PhysicalForeignKey> foreignKeys;
		ResultSet rs;
		PhysicalForeignKey foreignKey;
		
		
		foreignKeys = new ArrayList();
		foreignKey = null;
		
		try {
			rs = dbMeta.getImportedKeys(targetCatalog, targetSchema, targetTable);
			/*
			1. PKTABLE_CAT String => primary key table catalog (may be null)
			2. PKTABLE_SCHEM String => primary key table schema (may be null)
			3. PKTABLE_NAME String => primary key table name
			4. PKCOLUMN_NAME String => primary key column name
			5. FKTABLE_CAT String => foreign key table catalog (may be null) being exported (may be null)
			6. FKTABLE_SCHEM String => foreign key table schema (may be null) being exported (may be null)
			7. FKTABLE_NAME String => foreign key table name being exported
			8. FKCOLUMN_NAME String => foreign key column name being exported
			9. KEY_SEQ short => sequence number within foreign key
			10. UPDATE_RULE short => What happens to foreign key when primary is updated:
				importedNoAction - do not allow update of primary key if it has been imported
				importedKeyCascade - change imported key to agree with primary key update
				importedKeySetNull - change imported key to NULL if its primary key has been updated
				importedKeySetDefault - change imported key to default values if its primary key has been updated
				importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility)
			11. DELETE_RULE short => What happens to the foreign key when primary is deleted:
				importedKeyNoAction - do not allow delete of primary key if it has been imported
				importedKeyCascade - delete rows that import a deleted key
				importedKeySetNull - change imported key to NULL if its primary key has been deleted
				importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility)
				importedKeySetDefault - change imported key to default if its primary key has been deleted
			12. FK_NAME String => foreign key name (may be null)
			13. PK_NAME String => primary key name (may be null)
			14. DEFERRABILITY short => can the evaluation of foreign key constraints be deferred until commit:
				importedKeyInitiallyDeferred - see SQL92 for definition
				importedKeyInitiallyImmediate - see SQL92 for definition
				importedKeyNotDeferrable - see SQL92 for definition
			 */
			while (rs.next()) {
				String fkName = rs.getString("FK_NAME");
				if(foreignKey == null) { // OK it's the first iteration
					
					foreignKey = new PhysicalForeignKey();
					foreignKey.setFkName(fkName);
					foreignKey.setFkTableName( rs.getString("FKTABLE_NAME") );					
					foreignKey.setPkName(rs.getString("PK_NAME"));
					foreignKey.setPkTableName( rs.getString("PKTABLE_NAME") );
				
				} else if (!foreignKey.getFkName().equals(fkName)) { // we have finished with the previous fk

					foreignKeys.add(foreignKey);
					
					foreignKey = new PhysicalForeignKey();
					foreignKey.setFkName(fkName);
					foreignKey.setFkTableName( rs.getString("FKTABLE_NAME") );					
					foreignKey.setPkName(rs.getString("PK_NAME"));
					foreignKey.setPkTableName( rs.getString("PKTABLE_NAME") );
					
				}
				
				foreignKey.addFkColumnName( rs.getString("FKCOLUMN_NAME") );
				foreignKey.addPkColumnName( rs.getString("PKCOLUMN_NAME") );
				
			}

		} catch(Throwable t) {
			throw new RuntimeException("Impossible to retrive foreignKeys metadata", t);
		}
	
		return foreignKeys;
	}
}
