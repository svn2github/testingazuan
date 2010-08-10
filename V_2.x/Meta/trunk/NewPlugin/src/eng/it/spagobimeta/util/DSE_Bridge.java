/*
 * This class provide methods to manage the connection defined 
 * in the Data Source Explorer View of the Eclipse Data Tools Project
 */

package eng.it.spagobimeta.util;


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

public class DSE_Bridge {
	
	public DSE_Bridge()
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
	
	//return the schemas from a Database object
	public EList<Schema> get_schemas(Database db)
	{
		if (db.getVendor().equals("Oracle"))
		{
			EList<Catalog> catalogs = get_catalogs(db);
			Catalog cat = catalogs.get(0);
			return cat.getSchemas();
		}
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
