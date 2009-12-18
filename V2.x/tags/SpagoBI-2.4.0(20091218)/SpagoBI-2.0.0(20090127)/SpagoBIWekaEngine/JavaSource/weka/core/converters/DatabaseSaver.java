/**
 *
 *	LICENSE: see COPYING file
 *
**/

/*
 *    DatabaseSaver.java
 *    Copyright (C) 2004 Stefan Mutter
 *
 */

package weka.core.converters;

import it.eng.spagobi.engines.weka.configurators.FilterConfigurator;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.OptionHandler;
import weka.core.Utils;

/**
 * Writes to a database (tested with MySQL, InstantDB, HSQLDB).
 * 
 * Available options are: -T <table name> <br>
 * Sets the name of teh table (default: the name of the relation)
 * <p>
 * 
 * -P <br>
 * If set, a primary key column is generated automatically (containing the row
 * number as INTEGER). The name of this columns is defined in the DatabaseUtils
 * file.
 * <p>
 * 
 * -i <input-file> <br>
 * Specifies an ARFF file as input (for command line use)
 * <p>
 * 
 * 
 * @author Stefan Mutter (mutter@cs.waikato.ac.nz)
 * @version $Revision: 1.2 $
 */
public class DatabaseSaver extends AbstractSaver implements BatchConverter,
		IncrementalConverter, DatabaseConverter, OptionHandler {

	private static transient Logger logger = Logger.getLogger(FilterConfigurator.class);
	
	/** The database connection */
	private DatabaseConnection databaseConnection;

	/** The name of the tablein which the instances should be stored */
	private String m_tableName;

	/** An input arff file (for command line use) */
	private String m_inputFile;

	/**
	 * The database specific type for a string (read in from the properties
	 * file)
	 */
	private String m_createText;

	/**
	 * The database specific type for a double (read in from the properties
	 * file)
	 */
	private String m_createDouble;

	/** The database specific type for an int (read in from the properties file) */
	private String m_createInt;

	/**
	 * The name of the primary key column that will be automatically generated
	 * (if enabled). The name is read from DatabaseUtils.
	 */
	private String m_idColumn;

	/** counts the rowsand used as a primary key value */
	private int m_count;

	/** Flag indicating if a primary key column should be added */
	private boolean m_id;

	/**
	 * Flag indicating whether the default name of the table is the relaion name
	 * or not.
	 */
	private boolean m_tabName;

	/** The property file for the database connection */
	protected static String PROPERTY_FILE = "database.properties";

	/** Properties associated with the database connection */
	protected static Properties PROPERTIES;

	protected int dbWriteMode = DELETE_INSERT;
	protected String[] keyColumnNames = null;
	protected boolean versioning = false;
	protected String versionColumnName;
	protected String version;

	public static final int DROP_INSERT = 0;
	public static final int DELETE_INSERT = 1;
	public static final int INSERT = 2;
	public static final int UPDATE_INSERT = 3;
	
	/**
	 * Sets the db write mode.
	 * 
	 * @param dbWriteMode the new db write mode
	 */
	public void setDbWriteMode(String dbWriteMode) {
		if(dbWriteMode == null) {
			//	leave default values
			return;
		}
		
		if(dbWriteMode.equalsIgnoreCase("DROP_INSERT")) {
			this.dbWriteMode = DROP_INSERT;
		} 
		else if (dbWriteMode.equalsIgnoreCase("DELETE_INSERT")) {
			this.dbWriteMode = DELETE_INSERT;
		}
		else if (dbWriteMode.equalsIgnoreCase("INSERT")) {
			this.dbWriteMode = INSERT;
		}
		else if (dbWriteMode.equalsIgnoreCase("UPDATE_INSERT")) {
			this.dbWriteMode = UPDATE_INSERT;
		}
		else {
			// leave default values
		}
	}
	
	

	/** reads the property file */
	static {

		try {
			PROPERTIES = Utils.readProperties(PROPERTY_FILE);
		} catch (Exception ex) {
			logger.error("Problem reading properties. Fix before continuing.", ex);
		}
	}

	/**
	 * Constructor.
	 * 
	 * @throws Exception throws Exception if property file cannot be read
	 */
	public DatabaseSaver() throws Exception {

		resetOptions();
		m_createText =  PROPERTIES.getProperty("CREATE_STRING");
		m_createDouble = PROPERTIES.getProperty("CREATE_DOUBLE");
		m_createInt = PROPERTIES.getProperty("CREATE_INT");
		m_idColumn = PROPERTIES.getProperty("idColumn");
	}

	/**
	 * Resets the Saver ready to save a new data set.
	 */
	public void resetOptions() {

		super.resetOptions();
		setRetrieval(NONE);
		m_tableName = "";
		m_count = 1;
		m_id = false;
		m_tabName = true;
		try {
			if (databaseConnection != null && databaseConnection.isConnected())
				databaseConnection.disconnectFromDatabase();
			databaseConnection = new DatabaseConnection();
		} catch (Exception ex) {
			printException(ex);
		}
	}

	/**
	 * Cancels the incremental saving process and tries to drop the table if the
	 * write mode is CANCEL.
	 */
	public void cancel() {

		if (getWriteMode() == CANCEL) {
			try {
				databaseConnection.execute("DROP TABLE " + m_tableName);
				if(databaseConnection.tableExists(m_tableName)) {
					logger.error("Table cannot be dropped");
				}
			} catch (Exception ex) {
				logger.error("cancel", ex);
			}
			resetOptions();
		}
	}

	/**
	 * Returns a string describing this Saver.
	 * 
	 * @return a description of the Saver suitable for displaying in the
	 * explorer/experimenter gui
	 */
	public String globalInfo() {
		return "Writes to a database";
	}

	/**
	 * Sets the table's name.
	 * 
	 * @param tn the name of the table
	 */
	public void setTableName(String tn) {

		m_tableName = tn;
	}

	/**
	 * Gets the table's name.
	 * 
	 * @return the table's name
	 */
	public String getTableName() {

		return m_tableName;
	}

	/**
	 * Returns the tip text fo this property.
	 * 
	 * @return the string
	 */
	public String tableNameTipText() {

		return "Sets the name of the table.";
	}

	/**
	 * En/Dis-ables the automatic generation of a primary key.
	 * 
	 * @param flag the flag
	 */
	public void setAutoKeyGeneration(boolean flag) {

		m_id = flag;
	}

	/**
	 * Gets whether or not a primary key will be generated automatically.
	 * 
	 * @return true if a primary key column will be generated, false otherwise
	 */
	public boolean getAutoKeyGeneration() {

		return m_id;
	}

	/**
	 * Returns the tip text fo this property.
	 * 
	 * @return the string
	 */
	public String autoKeyGenerationTipText() {

		return "If set to true, a primary key column is generated automatically (containing the row number as INTEGER). The name of the key is read from DatabaseUtils (idColumn)"
				+ " This primary key can be used for incremental loading (requires an unique key). This primary key will not be loaded as an attribute.";
	}

	/**
	 * En/Dis-ables that the relation name is used for the name of the table
	 * (default enabled).
	 * 
	 * @param flag the flag
	 */
	public void setRelationForTableName(boolean flag) {

		m_tabName = flag;
	}

	/**
	 * Gets whether or not the relation name is used as name of the table.
	 * 
	 * @return true if the relation name is used as the name of the table, false
	 * otherwise
	 */
	public boolean getRelationForTableName() {

		return m_tabName;
	}

	/**
	 * Returns the tip text fo this property.
	 * 
	 * @return the string
	 */
	public String relationForTableNameTipText() {

		return "If set to true, the relation name will be used as name for the database table. Otherwise the user has to provide a table name.";
	}

	/**
	 * Sets the database URL.
	 * 
	 * @param url the url
	 */
	public void setUrl(String url) {

		databaseConnection.setDatabaseURL(url);

	}

	/**
	 * Gets the database URL.
	 * 
	 * @return the URL
	 */
	public String getUrl() {

		return databaseConnection.getDatabaseURL();
	}

	/**
	 * Returns the tip text fo this property.
	 * 
	 * @return the string
	 */
	public String urlTipText() {

		return "The URL of the database";
	}

	/**
	 * Sets the database user.
	 * 
	 * @param user the user
	 */
	public void setUser(String user) {

		databaseConnection.setUsername(user);
	}

	/**
	 * Gets the database user.
	 * 
	 * @return the user name
	 */
	public String getUser() {

		return databaseConnection.getUsername();
	}

	/**
	 * Returns the tip text fo this property.
	 * 
	 * @return the string
	 */
	public String userTipText() {

		return "The user name for the database";
	}

	/**
	 * Sets the database password.
	 * 
	 * @param password the password
	 */
	public void setPassword(String password) {

		databaseConnection.setPassword(password);
	}

	/**
	 * Returns the tip text fo this property.
	 * 
	 * @return the string
	 */
	public String passwordTipText() {

		return "The database password";
	}

	/**
	 * Sets the database url.
	 * 
	 * @param url the database url
	 * @param userName the user name
	 * @param password the password
	 */
	public void setDestination(String url, String userName, String password) {

		try {
			databaseConnection = new DatabaseConnection();
			databaseConnection.setDatabaseURL(url);
			databaseConnection.setUsername(userName);
			databaseConnection.setPassword(password);
		} catch (Exception ex) {
			printException(ex);
		}
	}

	/**
	 * Sets the database url.
	 * 
	 * @param url the database url
	 */
	public void setDestination(String url) {

		try {
			databaseConnection = new DatabaseConnection();
			databaseConnection.setDatabaseURL(url);
		} catch (Exception ex) {
			printException(ex);
		}
	}

	/**
	 * Sets the database url using the DatabaseUtils file.
	 */
	public void setDestination() {

		try {
			databaseConnection = new DatabaseConnection();
		} catch (Exception ex) {
			printException(ex);
		}
	}
	
	/**
	 * Sets the database url using the given connection.
	 * 
	 * @param connection the connection
	 */
	public void setDestination(Connection connection) {

		try {
			databaseConnection = new DatabaseConnection();
			databaseConnection.setConnection(connection);
		} catch (Exception ex) {
			printException(ex);
		}
	}

	/**
	 * Opens a connection to the database.
	 */
	public void connectToDatabase() {

		try {
			if (!databaseConnection.isConnected())
				databaseConnection.connectToDatabase();
		} catch (Exception ex) {
			printException(ex);
		}
	}

	/**
	 * Writes the structure (header information) to a database by creating a new
	 * table.
	 */
	private void writeStructure() throws Exception {

		StringBuffer query = new StringBuffer();
		Instances structure = getInstances();
		query.append("CREATE TABLE ");
		query.append(m_tableName);
		if (structure.numAttributes() == 0)
			throw new Exception("Instances have no attribute.");
		query.append(" ( ");
		if (m_id) {
			if (databaseConnection.getUpperCase())
				m_idColumn = m_idColumn.toUpperCase();
			query.append(m_idColumn);
			query.append(" ");
			query.append(m_createInt);
			query.append(" PRIMARY KEY,");
		}
		for (int i = 0; i < structure.numAttributes(); i++) {
			Attribute att = structure.attribute(i);
			String attName = att.name();
			attName = attName.replaceAll("[^\\w]", "_");
			if (databaseConnection.getUpperCase())
				query.append(attName.toUpperCase());
			else
				query.append(attName);
			if (att.isDate())
				query.append(" DATE");
			else {
				if (att.isNumeric())
					query.append(" " + m_createDouble);
				else
					query.append(" " + m_createText);
			}
			if (i != structure.numAttributes() - 1)
				query.append(", ");
		}
		
		if(versioning) {
			query.append(", ");
			if (databaseConnection.getUpperCase())
				query.append(versionColumnName.toUpperCase());
			else
				query.append(versionColumnName);
			query.append(" " + m_createText);
		}
		
		query.append(" )");
		databaseConnection.execute(query.toString());

	}

	private void prepareStructure() throws Exception {
		Instances structure = getInstances();
		if (m_tabName || m_tableName.equals(""))
			m_tableName = structure.relationName();
		if (databaseConnection.getUpperCase()) {
			m_tableName = m_tableName.toUpperCase();
			m_createInt = m_createInt.toUpperCase();
			m_createDouble = m_createDouble.toUpperCase();
			m_createText = m_createText.toUpperCase();
		}
		m_tableName = m_tableName.replaceAll("[^\\w]", "_");

		switch (dbWriteMode) {
		case DROP_INSERT:
			try {
				databaseConnection.execute("DROP TABLE " + m_tableName);
			} catch(Exception e) {
				logger.error("Table cannot be dropped.");
			}
			writeStructure();
			break;
		case DELETE_INSERT:
			if (!databaseConnection.tableExists(m_tableName)) {
				writeStructure();
				break;
			}
			databaseConnection.execute("DELETE FROM " + m_tableName);
			if (!databaseConnection.isTableEmpty(m_tableName)) {
				logger.error("Table cannot be delated.");
				throw new Exception();
			}
			break;
		case INSERT:
			if (!databaseConnection.tableExists(m_tableName))
				writeStructure();
			break;
		case UPDATE_INSERT:
			if (!databaseConnection.tableExists(m_tableName))
				writeStructure();
			break;
			
		default:
			if (!databaseConnection.tableExists(m_tableName))
				writeStructure();
			break;
		}
	}
	
	private String columnNamesStr = "";
	private String[] columnNames = null;
	
	private void setColumnNamesStr() throws Exception {
		Instances structure = getInstances();
		
		if (structure.numAttributes() == 0)
			throw new Exception("Instances have no attribute.");		
			
		int j = 0;
		
		if (m_id) {
			columnNames = new String[structure.numAttributes()+1];
			if (databaseConnection.getUpperCase())
				m_idColumn = m_idColumn.toUpperCase();
			columnNames[j++] = m_idColumn;			
		}
		else {
			columnNames = new String[structure.numAttributes()];
		}
		
		for (int i = 0; i < structure.numAttributes(); i++) {
			Attribute att = structure.attribute(i);
			String attName = att.name();
			attName = attName.replaceAll("[^\\w]", "_");
			if (databaseConnection.getUpperCase())
				columnNames[j++] = attName.toUpperCase();
			else
				columnNames[j++] = attName;
			
		}		
		
		columnNamesStr += "(";
		for(int i = 0; i < columnNames.length; i++) {
			if(i != 0) columnNamesStr += ", ";
			columnNamesStr += columnNames[i];
		}
		if(versioning) columnNamesStr += ", " + versionColumnName;
		columnNamesStr += ")";
	}
	
	private void writeInstance(Instance inst) throws Exception {

		StringBuffer insert = new StringBuffer();
		insert.append("INSERT INTO ");
		insert.append(m_tableName);
		insert.append(" " + columnNamesStr );
		insert.append(" VALUES ( ");
			
		if (m_id) {
			insert.append(m_count);
			insert.append(", ");
			m_count++;
		}
		
		for (int j = 0; j < inst.numAttributes(); j++) {
			if (inst.isMissing(j))
				insert.append("NULL");
			else {
				if ((inst.attribute(j)).isNumeric())
					insert.append(inst.value(j));
				else {
					String stringInsert = "'" + inst.stringValue(j) + "'";
					stringInsert = stringInsert.replaceAll("''", "'");
					insert.append(stringInsert);
				}
			}
			if (j != inst.numAttributes() - 1)
				insert.append(", ");
		}
		
		if(versioning)
			insert.append(", '" + version + "'");
		
		insert.append(" )");
		databaseConnection.fastExecute(insert.toString());
		/*
		if (databaseConnection.execute(insert.toString()) == false
				&& databaseConnection.getUpdateCount() < 1) {
			throw new IOException("Tuple cannot be inserted.");
		}
		*/
	}
	
	private void updateInstance(Instance inst) throws Exception {

		StringBuffer select = new StringBuffer();
		StringBuffer where = new StringBuffer();
		select.append("SELECT * FROM ");
		select.append(m_tableName);
		where.append(" WHERE ");
		for(int i = 0; i < columnNames.length; i++) {
			if(isKeyColumnName(columnNames[i])) {
				if(i!=0) where.append(" AND");
				where.append(" " + columnNames[i]);
				where.append(" = ");
				if ((inst.attribute(i)).isNumeric())
					where.append(inst.value(i));
				else {
					String stringInsert = "'" + inst.stringValue(i) + "'";
					stringInsert = stringInsert.replaceAll("''", "'");
					where.append(stringInsert);
				}
			}
		}
		if(versioning && isKeyColumnName(versionColumnName)){
			where.append(" AND " + versionColumnName + " = '" + version + "'");
		}

		if(!databaseConnection.fastExecute(select.toString() + where.toString())) {
			writeInstance(inst);
			return;
		}

		
		StringBuffer update = new StringBuffer();
		update.append("UPDATE ");
		update.append(m_tableName);
		update.append(" SET ");
		for(int i = 0; i < columnNames.length; i++) {
			if(i!=0) update.append(" ,");
			update.append(" " + columnNames[i]);
			update.append(" = ");
			if ((inst.attribute(i)).isNumeric())
				update.append(inst.value(i));
			else {
				String stringInsert = "'" + inst.stringValue(i) + "'";
				stringInsert = stringInsert.replaceAll("''", "'");
				update.append(stringInsert);
			}
		}

		databaseConnection.fastExecute(update.toString() + where.toString());	
	}

	/**
	 * Saves an instances incrementally. Structure has to be set by using the
	 * setStructure() method or setInstances() method. When a structure is set,
	 * a table is created.
	 * 
	 * @param inst the instance to save
	 * 
	 * @throws IOException throws IOEXception.
	 */
	public void writeIncremental(Instance inst) throws IOException {

		int writeMode = getWriteMode();
		Instances structure = getInstances();

		if (databaseConnection == null)
			throw new IOException("No database has been set up.");
		if (getRetrieval() == BATCH)
			throw new IOException(
					"Batch and incremental saving cannot be mixed.");
		setRetrieval(INCREMENTAL);

		try {
			if (!databaseConnection.isConnected())
				connectToDatabase();
			if (writeMode == WAIT) {
				if (structure == null) {
					setWriteMode(CANCEL);
					if (inst != null)
						throw new Exception(
								"Structure(Header Information) has to be set in advance");
				} else
					setWriteMode(STRUCTURE_READY);
				writeMode = getWriteMode();
			}
			if (writeMode == CANCEL) {
				cancel();
			}
			if (writeMode == STRUCTURE_READY) {
				setWriteMode(WRITE);
				writeStructure();
				writeMode = getWriteMode();
			}
			if (writeMode == WRITE) {
				if (structure == null)
					throw new IOException("No instances information available.");
				if (inst != null) {
					// write instance
					writeInstance(inst);
				} else {
					// close
					databaseConnection.disconnectFromDatabase();
					resetStructure();
					m_count = 1;
				}
			}
		} catch (Exception ex) {
			printException(ex);
		}
	}

	/**
	 * Writes a Batch of instances.
	 * 
	 * @throws IOException throws IOException
	 */
	public void writeBatch() throws IOException {

		Instances instances = getInstances();
		try {
			setColumnNamesStr();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (instances == null)
			throw new IOException("No instances to save");
		if (getRetrieval() == INCREMENTAL)
			throw new IOException(
					"Batch and incremental saving cannot be mixed.");
		if (databaseConnection == null)
			throw new IOException("No database has been set up.");
		setRetrieval(BATCH);
		try {
			if (!databaseConnection.isConnected())
				connectToDatabase();
			setWriteMode(WRITE);
			prepareStructure();
			for (int i = 0; i < instances.numInstances(); i++) {
				if(dbWriteMode != UPDATE_INSERT) {
					writeInstance(instances.instance(i));
				}
				else {
					updateInstance(instances.instance(i));
				}
				
			}
			databaseConnection.disconnectFromDatabase();
			setWriteMode(WAIT);
			resetStructure();
			m_count = 1;
		} catch (Exception ex) {
			printException(ex);
		}
	}

	/**
	 * Prints an exception
	 * 
	 * @param ex
	 *            the exception to print
	 */
	private void printException(Exception ex) {

		while (ex != null) {
			if (ex instanceof SQLException) {
				ex = ((SQLException) ex).getNextException();
			} else
				ex = null;
		}

	}

	/**
	 * Gets the setting.
	 * 
	 * @return the current setting
	 */
	public String[] getOptions() {
		Vector options = new Vector();

		if ((m_tableName != null) && (m_tableName.length() != 0)) {
			options.add("-T");
			options.add(m_tableName);
		}

		if (m_id)
			options.add("-P");

		if ((m_inputFile != null) && (m_inputFile.length() != 0)) {
			options.add("-i");
			options.add(m_inputFile);
		}

		return (String[]) options.toArray(new String[options.size()]);
	}

	/**
	 * Lists the available options.
	 * 
	 * @return an enumeration of the available options
	 */
	public java.util.Enumeration listOptions() {

		FastVector newVector = new FastVector(3);

		newVector.addElement(new Option(
				"\tThe name of the table (default: the relation name).", "T",
				1, "-T <table name>"));
		newVector
				.addElement(new Option(
						"\tAdd an ID column as primary key. The name is specified in the DatabaseUtils file. The DatabaseLoader won't load this column.",
						"P", 0, "-P"));
		newVector
				.addElement(new Option(
						"\tInput file in arff format that should be saved in database.",
						"i", 1, "-i<input file name>"));

		return newVector.elements();
	}

	/**
	 * Sets the options.
	 * 
	 * Available options are: -T <table name> <br>
	 * Sets the name of teh table (default: the name of the relation)
	 * <p>
	 * 
	 * -P <br>
	 * If set, a primary key column is generated automatically (containing the
	 * row number as INTEGER)
	 * <p>
	 * 
	 * -i <input-file> <br>
	 * Specifies an ARFF file as input (for command line use)
	 * <p>
	 * 
	 * @param options the options
	 * 
	 * @throws Exception if options cannot be set
	 */
	public void setOptions(String[] options) throws Exception {

		String tableString, inputString;
		tableString = Utils.getOption('T', options);
		inputString = Utils.getOption('i', options);
		resetOptions();
		if (tableString.length() != 0) {
			m_tableName = tableString;
			m_tabName = false;
		}
		m_id = Utils.getFlag('P', options);
		if (inputString.length() != 0) {
			try {
				m_inputFile = inputString;
				ArffLoader al = new ArffLoader();
				File inputFile = new File(inputString);
				al.setSource(inputFile);
				setInstances(al.getDataSet());
				if (tableString.length() == 0)
					m_tableName = getInstances().relationName();
			} catch (Exception ex) {
				printException(ex);
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Main method.
	 * 
	 * @param options should contain the options of a Saver.
	 */
	public static void main(String[] options) {

		StringBuffer text = new StringBuffer();
		text.append("\n\nDatabaseSaver options:\n");
		try {
			DatabaseSaver asv = new DatabaseSaver();
			try {
				Enumeration enumi = asv.listOptions();
				while (enumi.hasMoreElements()) {
					Option option = (Option) enumi.nextElement();
					text.append(option.synopsis() + '\n');
					text.append(option.description() + '\n');
				}
				asv.setOptions(options);
				asv.setDestination();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			// incremental

			/*
			 * asv.setRetrieval(INCREMENTAL); Instances instances =
			 * asv.getInstances(); asv.setStructure(instances); for(int i = 0; i <
			 * instances.numInstances(); i++){ //last instance is null and
			 * finishes incremental saving
			 * asv.writeIncremental(instances.instance(i)); }
			 * asv.writeIncremental(null);
			 */

			// batch
			asv.writeBatch();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * Gets the db write mode.
	 * 
	 * @return the db write mode
	 */
	public int getDbWriteMode() {
		return dbWriteMode;
	}

	/**
	 * Sets the db write mode.
	 * 
	 * @param dbWriteMode the new db write mode
	 */
	public void setDbWriteMode(int dbWriteMode) {
		this.dbWriteMode = dbWriteMode;
	}

	/**
	 * Gets the version.
	 * 
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Sets the version.
	 * 
	 * @param version the new version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Gets the version column name.
	 * 
	 * @return the version column name
	 */
	public String getVersionColumnName() {
		return versionColumnName;
	}

	/**
	 * Sets the version column name.
	 * 
	 * @param versionColumnName the new version column name
	 */
	public void setVersionColumnName(String versionColumnName) {
		this.versionColumnName = versionColumnName;
	}

	/**
	 * Checks if is versioning.
	 * 
	 * @return true, if is versioning
	 */
	public boolean isVersioning() {
		return versioning;
	}

	/**
	 * Sets the versioning.
	 * 
	 * @param versioning the new versioning
	 */
	public void setVersioning(boolean versioning) {
		this.versioning = versioning;
	}

	/**
	 * Gets the key column names.
	 * 
	 * @return the key column names
	 */
	public String[] getKeyColumnNames() {
		return keyColumnNames;
	}

	/**
	 * Sets the key column names.
	 * 
	 * @param keyColumnNames the new key column names
	 */
	public void setKeyColumnNames(String[] keyColumnNames) {
		this.keyColumnNames = keyColumnNames;
	}
	
	/**
	 * Checks if is key column name.
	 * 
	 * @param columnName the column name
	 * 
	 * @return true, if is key column name
	 */
	public boolean isKeyColumnName(String columnName) {
		for(int i = 0; i < keyColumnNames.length; i++) {
			if(columnName.equalsIgnoreCase(keyColumnNames[i])) return true;
		}
		return false;
	}

}
