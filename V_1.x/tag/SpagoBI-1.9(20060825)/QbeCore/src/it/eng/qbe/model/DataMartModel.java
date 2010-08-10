package it.eng.qbe.model;

import it.eng.qbe.utility.IDataMartModelRetriever;
import it.eng.qbe.utility.IQueryPersister;
import it.eng.qbe.utility.JarUtils;
import it.eng.qbe.utility.Logger;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spago.configuration.ConfigSingleton;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


/**
 * @author Andrea Zoppello
 * 
 * This class represent contains all the information related to the
 * datamart, it's path, the jndi name of the connection, the Hibernate Dialect
 * used etc..
 *
 */
public class DataMartModel implements Serializable {

	/**
	 * 
	 */
	private String name = null;
	/**
	 * 
	 */
	private String label = null;
	/**
	 * 
	 */
	private String description = null;
	
	/**
	 * 
	 */
	private String path = null;
	/**
	 * 
	 */
	private String jndiDataSourceName = null;
	/**
	 * 
	 */
	private String dialect = null;
	
	private Configuration hibCfg = null;
	/**
	 * @param path: The path of the datamart
	 * @param jndiDataSourceName: the name of the jndi datasource
	 * @param dialect: the dialect to use
	 */
	public DataMartModel(String path, String jndiDataSourceName, String dialect){
		this.path = path;
		this.jndiDataSourceName = jndiDataSourceName;
		this.dialect = dialect;
	}
	
	/**
	 * This methos is responsible to create the Hibarnate Configuration Object related
	 * to the datamart
	 * @return the hibernate Configuration
	 */
	private Configuration createHibernateConfiguration(){
		Configuration cfg = new Configuration();
		if ( jndiDataSourceName != null){
			cfg.setProperty("hibernate.dialect", dialect);
			cfg.setProperty("hibernate.connection.datasource", jndiDataSourceName);
			cfg.setProperty("hibernate.cglib.use_reflection_optimizer", "true");			
		}
		return cfg;
	}
	
	/**
	 * This methos is responsible to retrieve the phisical jar file wich contains the datamart
	 * @return the hibernate Configuration
	 */
	public File getJarFile(){
		try{
			IDataMartModelRetriever dataMartModelRetriever = getDataMartModelRetriever();
			File jarFile = dataMartModelRetriever.getJarFile(path,dialect);
			if (jarFile == null){
				System.out.println(" ----------------- JAR FILE NULLO -------------------------- ");
			}
			return jarFile;
		}catch (Exception e) {
			Logger.error(DataMartModel.class, e);
			return null;
		}
	}
	
	/**
	 * This methos is responsible to create the Hibernate Session Factory Object related to the
	 * datamart model
	 * @return the hibernate Configuration
	 */
	public SessionFactory createSessionFactory(){
		Logger.debug(this.getClass(), "createSessionFactory: start method createSessionFactory");
		
		SessionFactory sf = null;
		Configuration cfg = null;
		
		cfg = getHibernateConfiguration(getJarFile());
		sf = cfg.buildSessionFactory();
		
		return sf;
	}
	
	public Configuration getHibernateConfiguration(File jarFile) {
		if (this.hibCfg == null)
			this.hibCfg = initHibernateConfiguration(jarFile);
		return hibCfg;
	}
	public Configuration initHibernateConfiguration(File jarFile) {
		Configuration cfg = null;
		
		
		if (jndiDataSourceName != null && !jndiDataSourceName.equalsIgnoreCase("")) {
			// SE I PARAMETRI VENGONO PASSATI IN INPUT USO QUELLI
			
			Logger.debug(DataMartModel.class, "getHibernateConfiguration: connection properties defined by hand");
			
			
			cfg = new Configuration();
			cfg.setProperty("hibernate.dialect", dialect);
			cfg.setProperty("hibernate.connection.datasource", jndiDataSourceName);
			cfg.setProperty("hibernate.cglib.use_reflection_optimizer", "true");			
			
			Logger.debug(DataMartModel.class, "getHibernateConfiguration: jar file obtained: " + jarFile);
			updateCurrentClassLoader(jarFile);
			Logger.debug(DataMartModel.class, "getHibernateConfiguration: current class loader updated");
			if (jarFile == null)
				System.out.println( " --------------------- JAR FILE NULLO --------------- ");
			cfg.addJar(jarFile);
			Logger.debug(DataMartModel.class, "getHibernateConfiguration: add jar file to configuration");			
		
		}else {
			
			// ALTRIMENTI CERCO I PARAMETRI DI CONFIGURAZIONE SUL FILE hibconn.properies
			URL hibConnPropertiesUrl = JarUtils.getResourceFromJarFile(jarFile, "hibconn.properties") ;
			if (hibConnPropertiesUrl != null){
				Properties prop = new Properties();
				try{
					prop.load(hibConnPropertiesUrl.openStream());
				}catch (IOException ioe) {
					ioe.printStackTrace();
				}
			
				Logger.debug(DataMartModel.class, "getHibernateConfiguration: connection properties loaded by hibconn.properties in jar");
				
				cfg = new Configuration();
				cfg.setProperty("hibernate.dialect", prop.getProperty("hibernate.dialect"));
				cfg.setProperty("hibernate.connection.datasource", prop.getProperty("hibernate.connection.datasource"));
				cfg.setProperty("hibernate.cglib.use_reflection_optimizer", "true");			
				
				this.jndiDataSourceName = prop.getProperty("hibernate.connection.datasource");
				this.dialect = prop.getProperty("hibernate.dialect");
				
				Logger.debug(DataMartModel.class, "getHibernateConfiguration: jar file obtained: " + jarFile);
				updateCurrentClassLoader(jarFile);
				Logger.debug(DataMartModel.class, "getHibernateConfiguration: current class loader updated");
				if (jarFile == null)
					System.out.println( " --------------------- JAR FILE NULLO --------------- ");
				cfg.addJar(jarFile);
				Logger.debug(DataMartModel.class, "getHibernateConfiguration: add jar file to configuration");
			
			} else {
			
				// ALTRIMENTI EFFETTUO LA CONFIGURAZIONE DAL FILE hibernate.cfg.cml
			
				// ---------------------- NOTA BENE
				// IN QUESTO IL FILE DEVE CONTENERE I RIFERIMENTI A TUTTI GLI HBM
				Logger.debug(DataMartModel.class, "getHibernateConfiguration: connection properties defined in hibernate.cfg.xml");
							
				Logger.debug(DataMartModel.class, "getHibernateConfiguration: jar file obtained: " + jarFile);
				updateCurrentClassLoader(jarFile);
				Logger.debug(DataMartModel.class, "getHibernateConfiguration: current class loader updated");
			
				Logger.debug(DataMartModel.class, "getHibernateConfiguration: trying to read configuration from hibernate.cfg.xml file");
				URL url = JarUtils.getResourceFromJarFile(jarFile, "hibernate.cfg.xml") ;
				Logger.debug(DataMartModel.class, "getHibernateConfiguration: configuration file found at " + url);
				
				
				cfg = new Configuration().configure(url);
				this.jndiDataSourceName = cfg.getProperty("hibernate.connection.datasource");
				this.dialect = cfg.getProperty("hibernate.dialect");
			}
		}
		
		if(cfg != null){
			Logger.debug(DataMartModel.class, "getHibernateConfiguration: session factory built: " + cfg);
		}
		else {
			Logger.error(DataMartModel.class, "getHibernateConfiguration: impossible to build configuration");
		}
		
		return cfg;
	}
	
	/**
	 * @return the IDataMartModelRetriever object reading concrete implementation class from the property QBE.DATA-MART-MODEL-RETRIEVER.className in
	 * qbe.xml file
	 */
	public IDataMartModelRetriever getDataMartModelRetriever() throws Exception{
			String dataMartModelRetrieverClassName = (String)ConfigSingleton.getInstance().getAttribute("QBE.DATA-MART-MODEL-RETRIEVER.className");
			IDataMartModelRetriever dataMartModelRetriever = (IDataMartModelRetriever)Class.forName(dataMartModelRetrieverClassName).newInstance();
			return dataMartModelRetriever;
	}
	
	/**
	 * @return the IQueryPersister object reading concrete implementation class from the property QBE.QUERY-PERSISTER.className in
	 * qbe.xml file
	 */
	public IQueryPersister getQueryPersister() throws Exception{
		String queryPersisterClass = (String)ConfigSingleton.getInstance().getAttribute("QBE.QUERY-PERSISTER.className");
		IQueryPersister queryPersister = (IQueryPersister)Class.forName(queryPersisterClass).newInstance();
		return queryPersister;
	}
	
	/**
	 * This method update the Thread Context ClassLoader adding to the class loader the jarFile
	 * @param jarFile
	 */
	public static void updateCurrentClassLoader(File jarFile){
		try{
			ClassLoader previous = Thread.currentThread().getContextClassLoader();
			ClassLoader current = URLClassLoader.newInstance(new URL[]{jarFile.toURL()}, previous);
			Thread.currentThread().setContextClassLoader(current);
		}catch (Exception e) {
			Logger.error(DataMartModel.class, e);
		}
	}
	
	/**
	 * This method retrieve the jarFile of the datamart and update the Thread Context ClassLoader adding this jar 
	 * @param jarFile
	 */
	public void updateCurrentClassLoader(){
		try{
			
			IDataMartModelRetriever dataMartModelRetriever = getDataMartModelRetriever();
			File jarFile = dataMartModelRetriever.getJarFile(path,dialect);
			ClassLoader previous = Thread.currentThread().getContextClassLoader();
			ClassLoader current = URLClassLoader.newInstance(new URL[]{jarFile.toURL()}, previous);
			Thread.currentThread().setContextClassLoader(current);
		}catch (Exception e) {
			Logger.error(DataMartModel.class, e);
		}
	}	

	
	/**
	 * @return dialect
	 */
	public String getDialect() {
		return dialect;
	}

	/**
	 * @param dialect
	 */
	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	/**
	 * @return jndiDataSourceName
	 */
	public String getJndiDataSourceName() {
		return jndiDataSourceName;
	}

	/**
	 * @param jndiDataSourceName
	 */
	public void setJndiDataSourceName(String jndiDataSourceName) {
		this.jndiDataSourceName = jndiDataSourceName;
	}

	/**
	 * @return path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * This method is responsible to persist the Object wizObj using the IQueryPersister 
	 * @param wizObj
	 */
	public void persistQueryAction(ISingleDataMartWizardObject wizObj){
		try{
			getQueryPersister().persist(this, wizObj);
		}catch (Exception e) {
			Logger.error(DataMartModel.class, e);
		}
	}
	
	/**
	 * This method retrieve all queries for a datamart model
	 * @return a List of ISingleDataMartWizardObject that are all queries for a given datamart
	 */
	public List getQueries(){
		List l = new ArrayList();
		try{
			l = getQueryPersister().loadAllQueries(this);
		}catch (Exception e) {
			Logger.error(DataMartModel.class, e);
		}
		return l;
	}
	
	/**
	 * This method retrieve the query related with the datamart model with given queryId
	 * @param queryId: The identifier of the query to get
	 * @return ISingleDataMartWizardObject the object representing the query
	 */
	public ISingleDataMartWizardObject getQuery(String queryId){
		ISingleDataMartWizardObject  query= null;
		try{
			query = getQueryPersister().load(this, queryId);
		}catch (Exception e) {
			Logger.error(DataMartModel.class, e);
		}
		return query;
	}

	/**
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
}
