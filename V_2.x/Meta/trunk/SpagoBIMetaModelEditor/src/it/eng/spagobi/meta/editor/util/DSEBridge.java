/*
 * This class provide methods to manage the connection defined 
 * in the Data Source Explorer View of the Eclipse Data Tools Project
 */

package it.eng.spagobi.meta.editor.util;



import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import it.eng.spagobi.meta.editor.views.PhysicalModelView;
import it.eng.spagobi.meta.initializer.PhysicalModelInitializer;
import it.eng.spagobi.meta.model.physical.PhysicalModel;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.IManagedConnection;
import org.eclipse.datatools.connectivity.ProfileManager;
import org.eclipse.datatools.connectivity.sqm.core.connection.ConnectionInfo;
import org.eclipse.datatools.modelbase.sql.schema.Catalog;
import org.eclipse.datatools.modelbase.sql.schema.Database;
import org.eclipse.datatools.modelbase.sql.schema.Schema;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.datatools.modelbase.sql.tables.Table;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ListDialog;

public class DSEBridge {
	
	public DSEBridge()
	{	
		
	}
	
	//return current defined connection profiles in the DSE View
	public IConnectionProfile[] get_CP()
	{
		IConnectionProfile[] cps = ProfileManager.getInstance().getProfiles();
		return cps;
	}
	
	//connect to a specified connection profile and return a JDBC connection
	public java.sql.Connection connect_CP(IConnectionProfile cp)
	{
		java.sql.Connection jdbcCon = null;
		IStatus status = cp.connect();
		if (status.getCode()== IStatus.OK) 
		{
		     IManagedConnection managedConnection = ((IConnectionProfile)cp).getManagedConnection ("java.sql.Connection");
		     if (managedConnection != null) {			
		    	jdbcCon = (java.sql.Connection) managedConnection.getConnection().getRawConnection();
		     }
		     
		     if (jdbcCon != null) {
		    	 return jdbcCon;
		     }
		    	 
		}
		return null;	
	}
	
	/*
	 * Connect to the specified ConnectionProfile then extract the metadata to initialize
	 * the EMF PhysicalModel
	 */
	public PhysicalModel connectionExtraction(IConnectionProfile cp){
		java.sql.Connection conn = connect_CP(cp);
		//check if connection is OK
		if (conn != null){
			String modelName = cp.getInstanceID();
			
			//Check if default catalog is found
			String defaultCatalog = checkCatalog(conn);
			//Check if default schema is found
			String defaultSchema = checkSchema(conn);
			//initialize the EMF Physical Model
			PhysicalModelInitializer modelInitializer = new PhysicalModelInitializer();
			PhysicalModel model = modelInitializer.initialize( modelName, conn,  defaultCatalog, defaultSchema);
	        return model;
		}
		return null;
			
	}
	
	//return the database model object which can be used to extract schemas, tables, ect
	public Database get_dbModel(IConnectionProfile cp)
	{
		IManagedConnection managedConnection = ((IConnectionProfile)cp).getManagedConnection(
		"org.eclipse.datatools.connectivity.sqm.core.connection.ConnectionInfo");
		if (managedConnection != null) {
			ConnectionInfo connectionInfo = (ConnectionInfo) managedConnection.getConnection().getRawConnection();
			if (connectionInfo != null) {
				Database database = connectionInfo.getSharedDatabase();
				return database;
			}
		}
		return null;
	}
	
	//check if the connection has a default catalog
	public String checkCatalog(java.sql.Connection conn){
		String catalog;
		List<String> catalogs;
		DatabaseMetaData dbMeta;
		ResultSet rs;
		try{
			catalog = conn.getCatalog();
			if (catalog != null)
				return catalog;
			else {
				dbMeta = conn.getMetaData();
				rs = dbMeta.getCatalogs();
				catalogs = new ArrayList();
				while (rs.next()) {
					catalogs.add( rs.getString(1) );
				}
				if (catalogs.size() == 1)
					return catalogs.get(1);
			}
				return null;
		}
		catch(Throwable t) {
			throw new RuntimeException("Impossible to check catalog", t);
		}
	}

	//check if the connection has a default schema
	public String checkSchema(java.sql.Connection conn){
		String schema = null;
		List<String> schemas;
		ResultSet rs;
		DatabaseMetaData dbMeta;
		
		try{
			dbMeta = conn.getMetaData();
			rs = dbMeta.getSchemas();
			schemas = new ArrayList();
			while (rs.next()) {
				schemas.add( rs.getString(1) );
				log(rs.getString(1));
			}
			if(schemas.size() == 0) {
				log("No schemas defined");
			} else if(schemas.size() == 1) {
				return schema = schemas.get(1);
			} else if(schemas.size() > 1){
				//Create ListDialog UI
				ListDialog ld = new ListDialog(new Shell());
				ld.setAddCancelButton(true);
				ld.setContentProvider(new ArrayContentProvider());
				ld.setLabelProvider(new SchemaLabelProvider());
				ld.setInput(schemas);
				ld.setTitle("Select a schema");
				ld.setHelpAvailable(false);
				ld.setMessage("No default schema found.\nPlease select a schema from the list");
				
				//wait for selection
				if (ld.open() == IStatus.OK){
					//obtaining selection
					Object[] res = ld.getResult();
					schema = (String)res[0];
					return schema;
				}
			}
			return null;
		}
		catch(Throwable t) {
			throw new RuntimeException("Impossible to check schema", t);
		}
	}	
	
	
	//return the schemas from a Database object
	public EList<Schema> get_schemas(Database db)
	{
		EList<Schema> schemas = db.getSchemas();
		return schemas;
	}
	
	//return the catalogs from a Database object
	public EList<Catalog> get_catalogs(Database db)
	{
		EList<Catalog> catalogs = db.getCatalogs();
		return catalogs;
	}
	
	//return the tables from a schema object
	public EList<Table> get_tables(Schema sch)
	{
		EList<Table> tables = sch.getTables();
		return tables;
	}
	
	//return the tables from a schema object
	public EList<Column> get_columns(Table tab)
	{
		EList<Column> columns = tab.getColumns();
		return columns;
	}
	
	//  --------------------------------------------------------
	//	Static methods
	//  --------------------------------------------------------
	
	private static void log(String msg) {
		System.out.println(msg);
	}
}
