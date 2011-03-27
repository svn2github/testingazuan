/*
 * This class provide methods to manage the connection defined 
 * in the Data Source Explorer View of the Eclipse Data Tools Project
 */

package it.eng.spagobi.meta.model.presentation;



import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import it.eng.spagobi.meta.initializer.PhysicalModelInitializer;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.ModelFactory;
import it.eng.spagobi.meta.model.physical.PhysicalModel;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
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
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ListDialog;

public class DSEBridge {
	
	String defaultCatalog, defaultSchema,modelName; 
	PhysicalModel model;
	java.sql.Connection connection;
	
	public DSEBridge() {}
	
	public IConnectionProfile[] getConnectionProfiles() {
		IConnectionProfile[] connectionProfiles;
		connectionProfiles = ProfileManager.getInstance().getProfiles();
		
		return connectionProfiles;
	}
	
	public IConnectionProfile getConnectionProfile(String connectionName) {
		return ProfileManager.getInstance().getProfileByName(connectionName);
	}
	
	
	public Connection connect(String connectionName) {
		return connect( getConnectionProfile(connectionName) );
	}
	//connect to a specified connection profile and return a JDBC connection
	public Connection connect(IConnectionProfile connectionProfile) {
		Connection jdbcCon = null;
		IStatus status = connectionProfile.connect();
		if (status.getCode()== IStatus.OK) 
		{
		     IManagedConnection managedConnection = ((IConnectionProfile)connectionProfile).getManagedConnection ("java.sql.Connection");
		     if (managedConnection != null) {			
		    	jdbcCon = (java.sql.Connection) managedConnection.getConnection().getRawConnection();
		     }
		     
		     if (jdbcCon != null) {
		    	 return jdbcCon;
		     }
		    	 
		}
		return null;	
	}
	
	
	
	
	
	
	/**
	 * Connect to the specified ConnectionProfile then extract the metadata to initialize
	 * the EMF PhysicalModel
	 */
	/*
	public PhysicalModel connectionExtraction(IConnectionProfile cp) {
		connection = connect(cp);
		//check if connection is OK
		if (connection != null){
						
			//Check if default catalog is found
			defaultCatalog = checkCatalog(connection);
			//Check if default schema is found
			defaultSchema = checkSchema(connection);
			
			//setting physical model name
			if (defaultSchema != null){
				modelName = defaultSchema;
			}
			else {
				modelName = cp.getName();
			}

			
			//ProgressMonitorDialog to show a progress bar for long operation
			ProgressMonitorDialog dialog = new ProgressMonitorDialog(new Shell());
			dialog.setCancelable(false);
			try {
				dialog.run(true, false, new IRunnableWithProgress(){
				    public void run(IProgressMonitor monitor) {
				        monitor.beginTask("Extracting metadata information. This may take a few minutes, please wait...", IProgressMonitor.UNKNOWN);
				        //retrieve root Model reference
				        CoreSingleton cs = CoreSingleton.getInstance();
				        Model rootModel = cs.getRootModel();
						 
				        //initialize the EMF Physical Model
				        PhysicalModelInitializer modelInitializer = new PhysicalModelInitializer();
				        modelInitializer.setRootModel(rootModel);
				        model = modelInitializer.initialize( modelName, connection,  defaultCatalog, defaultSchema);
						monitor.setTaskName("Extraction Completed.");
						monitor.done();				        
				    }
				});
			} catch (InvocationTargetException e1) {
				log("InvocationTargetException in connectionExtraction");
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				log("InvocationTargetException in connectionExtraction");
				e1.printStackTrace();
			}
		}
		return model;
			
	}
	*/
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
	
	/**
	 * check if the connection has a default catalog
	 * if no, let the user choose from those available
	 * @param conn
	 * @return catalog name
	 */
	/*
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
				if (catalogs.size() >  1){
					//Create ListDialog UI
					ListDialog ld = new ListDialog(new Shell());
					ld.setAddCancelButton(true);
					ld.setContentProvider(new ArrayContentProvider());
					ld.setLabelProvider(new CatalogLabelProvider());
					ld.setInput(catalogs);
					ld.setTitle("Select a catalog");
					ld.setHelpAvailable(false);
					ld.setMessage("No default catalog found.\nPlease select a catalog from the list");
					
					//wait for selection
					if (ld.open() == IStatus.OK){
						//obtaining selection
						Object[] res = ld.getResult();
						catalog = (String)res[0];
						return catalog;
					}					
				}
					
			}
				return null;
		}
		catch(Throwable t) {
			throw new RuntimeException("Impossible to check catalog", t);
		}
	}
*/
	/**
	 * check if the connection has a default schema
	 * if no, let the user choose from those available
	 * @param conn
	 * @return schema name
	 */
/*	
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
*/	
	
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
}
