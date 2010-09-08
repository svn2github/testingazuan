/**
 * 
 */
package it.eng.spagobi.meta.initializer;

import it.eng.spagobi.meta.model.physical.pojo.PhysicalModel;
import it.eng.spagobi.meta.model.physical.pojo.PhysicalPrimaryKey;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class PhysicalModelInitializer {
	
	public PhysicalModel initizlize(String modelName, Connection conn, String defaultCatalog, String defaultSchema) {
		PhysicalModel model;
		DatabaseMetaData dbMeta;
		
		try {
			model = new PhysicalModel(modelName);
			log("model: " + model.getName()); 
			
			dbMeta = conn.getMetaData();
			
			initDatabaseMeta(model, dbMeta);	
			
			log("db name: " + model.getDatabaseName());
			log("db version: " + model.getDatabaseVersion());
			
			initCatalogMeta(model, conn, defaultCatalog);
			log("catalog [" + dbMeta.getCatalogTerm() + "]: " + model.getCatalog());
						
			initSchemaMeta(model, dbMeta, defaultSchema);
			log("schema [" + dbMeta.getSchemaTerm() + "]: " + model.getSchema());
			
			//initTypesMeta(model, dbMeta);
			initTablesMeta(model, dbMeta);
	
			initPrimaryKeysMeta(model, dbMeta);
			
			initForeignKeysMeta(model, dbMeta);
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to initialize physical model", t);
		}
		
		return model;
	}
	
	
	private void initDatabaseMeta(PhysicalModel model, DatabaseMetaData dbMeta) {
		try {
			model.setDatabaseName( dbMeta.getDatabaseProductName());
			model.setDatabaseVersion( dbMeta.getDatabaseProductVersion());
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to initialize database metadata", t);
		}
	}
	
	private void initCatalogMeta(PhysicalModel model, Connection conn, String defaultCatalog) {
		try {
			model.setCatalog( MetadataExtractor.retriveCatalog(conn, defaultCatalog) );
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to initialize catalog metadata", t);
		}
	}
	
	private void initSchemaMeta(PhysicalModel model, DatabaseMetaData dbMeta, String defaultSchema) {
		try {
			model.setSchema( MetadataExtractor.retriveSchema(dbMeta, defaultSchema) );
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to initialize schema metadata", t);
		}
	}
	
	
	
	private void initTablesMeta(PhysicalModel model, DatabaseMetaData dbMeta) {
		try {
			model.addTables( MetadataExtractor.retriveTables(dbMeta, model.getCatalog(), model.getSchema()) );
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to initialize tables metadata", t);
		}
	}
	
	private void initForeignKeysMeta(PhysicalModel model, DatabaseMetaData dbMeta) {
		try {
			for(int i = 0; i < model.getTables().size(); i++) {
				model.addForeignKeys( MetadataExtractor.retriveForeignKey(dbMeta, model.getCatalog(), model.getSchema(), model.getTables().get(i).getName()) );
			}
			
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to initialize foreignKeys metadata", t);
		}
	}
	
	private void initPrimaryKeysMeta(PhysicalModel model, DatabaseMetaData dbMeta) {
		PhysicalPrimaryKey primaryKey;
		try {
			for(int i = 0; i < model.getTables().size(); i++) {
				primaryKey = MetadataExtractor.retrivePrimaryKey(dbMeta, model.getCatalog(), model.getSchema(), model.getTables().get(i).getName());
				if(primaryKey != null) {
					model.addPrimaryKey( primaryKey );
				}
			}
			
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to initialize primaryKeys metadata", t);
		}
	}
	
	
	

	//  --------------------------------------------------------
	//	Static methods
	//  --------------------------------------------------------
	
	private static void log(String msg) {
		System.out.println(msg);
	}
	
}
