package it.eng.spagobi.test.dbunit.utility;

import it.eng.spago.dbaccess.DataConnectionManager;

import java.sql.Connection;

import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

public class DBConnectionTestCase extends DatabaseTestCase {
	
	protected FlatXmlDataSet dataSet = null;

	protected IDatabaseConnection getConnection() throws Exception {
		Connection jdbcConnection = DataConnectionManager.getInstance().getConnection("spagobi").getInternalConnection();
		return new DatabaseConnection(jdbcConnection);
	}

	protected IDataSet getDataSet() throws Exception {
		dataSet = new FlatXmlDataSet(getClass().getResourceAsStream(
				"/FullDataSet.xml"),true);
		return dataSet;
	}

}
