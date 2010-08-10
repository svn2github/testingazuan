/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.qbe.datasource;

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.model.HQLStatement;
import it.eng.qbe.model.IQuery;
import it.eng.qbe.model.IStatement;
import it.eng.qbe.utility.IDataMartModelRetriever;
import it.eng.qbe.utility.IQueryPersister;
import it.eng.qbe.utility.JarUtils;
import it.eng.qbe.utility.Logger;
import it.eng.spago.base.ApplicationContainer;
import it.eng.spago.configuration.ConfigSingleton;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * @author Andrea Gioia
 *
 */
public class HibernateDataSource extends BasicDataSource  {
	
	private String name;
	private String path = null;	
	private String jndiDataSourceName = null;	
	private String dialect = null;	
	private boolean classLoaderExtended = false;	
	private List alreadyAddedView = null;
	private Configuration configuration = null;
	private SessionFactory sessionFactory = null;
	
	
	public HibernateDataSource(String path, String jndiDataSourceName, String dialect) {
		this.path = path;
		this.jndiDataSourceName = jndiDataSourceName;
		this.dialect = dialect;
		this.alreadyAddedView = new ArrayList();
		type = IDataSource.HIBERNATE_DS_TYPE;
	}
	
	public SessionFactory getSessionFactory() {
		if(sessionFactory == null) sessionFactory = createSessionFactory();
		return sessionFactory;
	}
	
	/**
	 * This methos is responsible to create the Hibernate Session Factory Object related to the
	 * datamart model
	 * @return the hibernate Configuration
	 */
	private SessionFactory createSessionFactory(){
		Logger.debug(this.getClass(), "createSessionFactory: start method createSessionFactory");
		
		SessionFactory sf = null;
		Configuration cfg = null;
		
		cfg = getHibernateConfiguration(getJarFile());
		
		/*
		List viewsJarFile = getViewJarFiles();
		
		File f = null;
		for (Iterator it = viewsJarFile.iterator(); it.hasNext(); ){
			f = ((File)it.next());
			if (!(alreadyAddedView.contains(f.getAbsolutePath()))){
				cfg.addJar(f);
				alreadyAddedView.add(f.getAbsolutePath());
			}
		}
		*/
		sf = cfg.buildSessionFactory();
				
		return sf;
	}
	
	public Configuration getHibernateConfiguration(File jarFile) {
		if (this.configuration == null)
			this.configuration = initHibernateConfiguration(jarFile);
		return configuration;
	}
	
	public Configuration getHibernateConfiguration() {
		return getHibernateConfiguration(getJarFile());
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
			if (!classLoaderExtended){
				updateCurrentClassLoader(jarFile);
				List viewJarFiles = getViewJarFiles();
				for (int i=0; i < viewJarFiles.size(); i++){
					updateCurrentClassLoader((File)viewJarFiles.get(i));
				}
				this.classLoaderExtended = true;
			}
			Logger.debug(DataMartModel.class, "getHibernateConfiguration: current class loader updated");
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
				if (!classLoaderExtended){
					updateCurrentClassLoader(jarFile);
					
					List viewJarFiles = getViewJarFiles();
					for (int i=0; i < viewJarFiles.size(); i++){
						updateCurrentClassLoader((File)viewJarFiles.get(i));
					}
					
					this.classLoaderExtended = true;
				}
				this.classLoaderExtended = true;
				Logger.debug(DataMartModel.class, "getHibernateConfiguration: current class loader updated");
				cfg.addJar(jarFile);
				Logger.debug(DataMartModel.class, "getHibernateConfiguration: add jar file to configuration");
			
			} else {
			
				// ALTRIMENTI EFFETTUO LA CONFIGURAZIONE DAL FILE hibernate.cfg.cml
			
				// ---------------------- NOTA BENE
				// IN QUESTO IL FILE DEVE CONTENERE I RIFERIMENTI A TUTTI GLI HBM
				Logger.debug(DataMartModel.class, "getHibernateConfiguration: connection properties defined in hibernate.cfg.xml");
							
				Logger.debug(DataMartModel.class, "getHibernateConfiguration: jar file obtained: " + jarFile);
				if (!classLoaderExtended){
					updateCurrentClassLoader(jarFile);
					List viewJarFiles = getViewJarFiles();
					for (int i=0; i < viewJarFiles.size(); i++){
						updateCurrentClassLoader((File)viewJarFiles.get(i));
					}
					this.classLoaderExtended = true;
				}
				this.classLoaderExtended = true;
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
		
		if(cfg != null) {
			List viewsJarFile = getViewJarFiles();
			
			File f = null;
			for (Iterator it = viewsJarFile.iterator(); it.hasNext(); ){
				f = ((File)it.next());
				if (!(alreadyAddedView.contains(f.getAbsolutePath()))){
					cfg.addJar(f);
					alreadyAddedView.add(f.getAbsolutePath());
				}
			}
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
			return jarFile;
		}catch (Exception e) {
			Logger.error(DataMartModel.class, e);
			return null;
		}
	}		
	
	public IDataMartModelRetriever getDataMartModelRetriever() throws Exception {		
		String dataMartModelRetrieverClassName = (String)ConfigSingleton.getInstance().getAttribute("QBE.DATA-MART-MODEL-RETRIEVER.className");
		IDataMartModelRetriever dataMartModelRetriever = (IDataMartModelRetriever)Class.forName(dataMartModelRetrieverClassName).newInstance();
		return dataMartModelRetriever;
	}
	
	/**
	 * @return the IQueryPersister object reading concrete implementation class from the property 
	 * QBE.QUERY-PERSISTER.className in qbe.xml file
	 * 
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
		
		boolean wasAlreadyLoaded = false;
		ApplicationContainer container = null;
		
		try {
			
			container = ApplicationContainer.getInstance();
			if (container != null) {
				ClassLoader cl = (ClassLoader) container.getAttribute("DATAMART_CLASS_LOADER");
				if (cl != null) {
					Thread.currentThread().setContextClassLoader(cl);
				}
			}
			
			JarFile jar = new JarFile(jarFile);
			Enumeration entries = jar.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = (JarEntry) entries.nextElement();
				if (entry.getName().endsWith(".class")) {
					String entryName = entry.getName();
					String className = entryName.substring(0, entryName.lastIndexOf(".class"));
					className = className.replaceAll("/", ".");
					className = className.replaceAll("\\\\", ".");
					try {
						Thread.currentThread().getContextClassLoader().loadClass(className);
						wasAlreadyLoaded = true;
						break;
					} catch (Exception e) {
						wasAlreadyLoaded = false;
						break;
					}
				}
			}
			
		} catch (Exception e) {
			Logger.error(DataMartModel.class, e);
		}
		
		
		
		try {
			/*
			 * TEMPORARY: the next instruction forcing the loading of all classes in the path...
			 * (ie. for some qbe that have in common any classes but not all and that at the moment they aren't loaded corretly)
			 */
			wasAlreadyLoaded = false;

			if (!wasAlreadyLoaded) {
				
				ClassLoader previous = Thread.currentThread().getContextClassLoader();
				
				ClassLoader current = URLClassLoader.newInstance(new URL[]{jarFile.toURL()}, previous);
				
				Thread.currentThread().setContextClassLoader(current);
				
				if (container != null) container.setAttribute("DATAMART_CLASS_LOADER", current);

			}
			
		} catch (Exception e) {
			Logger.error(DataMartModel.class, e);
		}
	}
	
	/**
	 * This method retrieve the jarFile of the datamart and update the Thread Context ClassLoader adding this jar 
	 * @param jarFile
	 */
	public void updateCurrentClassLoader(){
		try{
			
			if (!classLoaderExtended){
				
				IDataMartModelRetriever dataMartModelRetriever = getDataMartModelRetriever();
				File jarFile = dataMartModelRetriever.getJarFile(path,dialect);
				ClassLoader previous = Thread.currentThread().getContextClassLoader();
				ClassLoader current = URLClassLoader.newInstance(new URL[]{jarFile.toURL()}, previous);
				Thread.currentThread().setContextClassLoader(current);
				
				
				classLoaderExtended = true;
			
			}
			
		}catch (Exception e) {
			
			Logger.error(DataMartModel.class, e);
		
		}
	}	
	
	public List getViewJarFiles(){
		try{
			IDataMartModelRetriever dataMartModelRetriever = getDataMartModelRetriever();
			return dataMartModelRetriever.getViewJarFiles(path,dialect);
		}catch (Exception e) {
			Logger.error(DataMartModel.class, e);
			return null;
		}
	}



	public String getDialect() {
		return dialect;
	}



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
	 * @deprecated use getHibernateConfiguration()
	 */
	public Configuration getHibCfg() {
		return getHibernateConfiguration();
	}

	/**
	 * @deprecated external bbject cannot set the configuration
	 */
	public void setHibCfg(Configuration hibCfg) {
		this.configuration = hibCfg;
	}

	public boolean isClassLoaderExtended() {
		return classLoaderExtended;
	}

	public void setClassLoaderExtended(boolean classLoaderExtended) {
		this.classLoaderExtended = classLoaderExtended;
	}

	public List getAlreadyAddedView() {
		return alreadyAddedView;
	}

	public void setAlreadyAddedView(List alreadyAddedView) {
		this.alreadyAddedView = alreadyAddedView;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
