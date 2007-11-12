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

import it.eng.qbe.conf.QbeConfiguration;
import it.eng.qbe.locale.LocaleUtils;
import it.eng.qbe.log.Logger;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.model.HQLStatement;
import it.eng.qbe.model.IQuery;
import it.eng.qbe.model.IStatement;
import it.eng.qbe.model.io.IDataMartModelRetriever;
import it.eng.qbe.model.io.IQueryPersister;
import it.eng.qbe.utility.JarUtils;
import it.eng.qbe.utility.Utils;
import it.eng.spago.base.ApplicationContainer;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.utilities.DynamicClassLoader;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * @author Andrea Gioia
 *
 */
public class HibernateDataSource extends BasicDataSource implements IHibernateDataSource  {
	
	private String name;
	private List datamartNames = null;
	
	
	private String jndiDataSourceName = null;	
	private String dialect = null;	
	private boolean classLoaderExtended = false;	
	private List alreadyAddedView = null;
	private Configuration configuration = null;
	private SessionFactory sessionFactory = null;
	
	private File formulaFile = null;
	
	private Properties qbeProperties = null;
	
	private Properties labelProperties = null;
	private Map localizedLabelMap = new HashMap();
	
	
	public HibernateDataSource(String datamartName, String jndiDataSourceName, String dialect) {
		this.name = buildDatasourceName(datamartName);
		
		this.datamartNames = new ArrayList();
		this.datamartNames.add(datamartName);
		
		this.jndiDataSourceName = jndiDataSourceName;
		this.dialect = dialect;
		this.alreadyAddedView = new ArrayList();
		this.type = IDataSource.HIBERNATE_DS_TYPE;
	}
		
	public SessionFactory getSessionFactory() {
		if(sessionFactory == null) sessionFactory = createSessionFactory();
		return sessionFactory;
	}
	
	public Configuration getConfiguration() {
		return getConfiguration(getJarFile());
	}
	
		
	
	
	public Properties getLabelProperties() {
		if(labelProperties == null) labelProperties = loadLabelProperties();
		return labelProperties;
	}
	
	public Properties getLabelProperties(Locale locale) {
		Properties props = (Properties)localizedLabelMap.get(locale.getLanguage());
		if(props == null) {
			props = loadLabelProperties(locale);
			localizedLabelMap.put(locale.getLanguage(), props);
		}
		return props;
	}
	
	private Properties loadLabelProperties() {
		
		Properties labelProperties = new Properties();
			
		File dmJarFile = getJarFile();
		JarFile jf;
		try {
			jf = new JarFile(dmJarFile);		
			
			labelProperties = LocaleUtils.getLabelProperties(jf);
					
			List views = getViewJarFiles();
			Iterator it = views.iterator();
			while(it.hasNext()) {
				File viewJarFile = (File)it.next();
				jf = new JarFile(viewJarFile);
				Properties tmpProps = LocaleUtils.getLabelProperties(jf);
				labelProperties.putAll(tmpProps);
			}		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return labelProperties;	
	}
	
	private Properties loadLabelProperties(Locale locale) {
		
		Properties props = new Properties();
		
		try{
			File dmJarFile = getJarFile();
			JarFile jf = new JarFile(dmJarFile);
			
			props = LocaleUtils.getLabelProperties(jf, locale);
				
			if (props.isEmpty()) {
				return loadLabelProperties();
			} else {
				List views = getViewJarFiles();
				Iterator it = views.iterator();
				while(it.hasNext()) {
					File viewJarFile = (File)it.next();
					jf = new JarFile(viewJarFile);
					Properties tmpProps = LocaleUtils.getLabelProperties(jf, locale);
					if(tmpProps.isEmpty()) tmpProps = LocaleUtils.getLabelProperties(jf);
					props.putAll(tmpProps);
				}
			}			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return props;
	
	}
	
		
	public File getFormulaFile() {
		if(formulaFile == null) formulaFile = loadFormulaFile();
		return formulaFile;
	}
	
	private File loadFormulaFile() {
		String formulaFile = getJarFile().getParent() + "/formula.xml";
		return new File(formulaFile);
	}
	
	
	
	
	public Properties getQbeProperties() {
		if(qbeProperties == null) qbeProperties = loadQbeProperties();
		return qbeProperties;
	}
	
	private Properties loadQbeProperties() {
		
		Properties qbeProperties = null;
		
		File dmJarFile = getJarFile();
		JarFile jf = null;
		try {
			jf = new JarFile(dmJarFile);
			qbeProperties = getQbeProperties(jf);
			
			List views = getViewJarFiles();
			Iterator it = views.iterator();
			while(it.hasNext()) {
				File viewJarFile = (File)it.next();
				jf = new JarFile(viewJarFile);
				Properties tmpProps = getQbeProperties(jf);
				qbeProperties.putAll(tmpProps);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		
		return qbeProperties;	
	}
	
	private Properties getQbeProperties(JarFile jf){
		Properties prop = new Properties();
		
		try{
			ZipEntry ze = jf.getEntry("qbe.properties");
			if (ze != null){
				prop = new Properties();
				prop.load(jf.getInputStream(ze));
			}
		} catch(IOException ioe){
			ioe.printStackTrace();
		}
		return prop;
	}
	
	
	private SessionFactory createSessionFactory(){
		Logger.debug(this.getClass(), "createSessionFactory: start method createSessionFactory");
		
		SessionFactory sf = null;
		Configuration cfg = null;
		
		cfg = getConfiguration(getJarFile());
		
		
		sf = cfg.buildSessionFactory();
				
		return sf;
	}
	
	
	public Configuration getConfiguration(File jarFile) {
		if (this.configuration == null)
			this.configuration = initHibernateConfiguration(jarFile);
		return configuration;
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
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.datasource.IHibernateDataSource#getJarFile()
	 */
	public File getJarFile(){
		try{
			IDataMartModelRetriever dataMartModelRetriever = QbeConfiguration.getInstance().getDataMartModelRetriever();
			File jarFile = dataMartModelRetriever.getDatamartJarFile(getDatamartName());
			return jarFile;
		}catch (Exception e) {
			Logger.error(DataMartModel.class, e);
			return null;
		}
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
    		    DynamicClassLoader current = new DynamicClassLoader(jarFile, previous);
			    Thread.currentThread().setContextClassLoader(current);

				//ClassLoader current = URLClassLoader.newInstance(new URL[]{jarFile.toURL()}, previous);				
				//Thread.currentThread().setContextClassLoader(current);
				
				if (container != null) container.setAttribute("DATAMART_CLASS_LOADER", current);

			}
			
		} catch (Exception e) {
			Logger.error(DataMartModel.class, e);
		}
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.datasource.IHibernateDataSource#updateCurrentClassLoader()
	 */
	public void updateCurrentClassLoader(){
		try{
			
			if (!classLoaderExtended){
				
				IDataMartModelRetriever dataMartModelRetriever = QbeConfiguration.getInstance().getDataMartModelRetriever();;
				File jarFile = dataMartModelRetriever.getDatamartJarFile(getDatamartName());
				ClassLoader previous = Thread.currentThread().getContextClassLoader();
//				ClassLoader current = URLClassLoader.newInstance(new URL[]{jarFile.toURL()}, previous);				
    		    DynamicClassLoader current = new DynamicClassLoader(jarFile, previous);    		    
			    Thread.currentThread().setContextClassLoader(current);

				classLoaderExtended = true;
			
			}
			
		}catch (Exception e) {
			
			Logger.error(DataMartModel.class, e);
		
		}
	}	
	
	
	
	private List getViewNames(String datamartName) {
		List viewNames = null;
		IDataMartModelRetriever dataMartModelRetriever;
		try {
			dataMartModelRetriever = QbeConfiguration.getInstance().getDataMartModelRetriever();
			viewNames = dataMartModelRetriever.getViewNames(datamartName);
		} catch (Exception e) {
			Logger.error(DataMartModel.class, e);
		}
		
		
		return viewNames;
	}
	/**
	 * @deprecated
	 * @return
	 */
	private List getViewJarFiles() {
		List viewsJarFiles = new ArrayList();
		
		for(int i = 0; i < datamartNames.size(); i++) {
			String datamartName = (String)datamartNames.get(i);
			List viewNames = getViewNames(datamartName);
			for(int j = 0; j < viewNames.size(); j++) {
				String viewName = (String)viewNames.get(j);
				viewsJarFiles.add( getViewJarFile(datamartName, viewName) );
			}
		}	
		
		return viewsJarFiles;
	}
	
	private File getViewJarFile(String datamartName, String viewName){
		File viewJarFile = null;
		
		try{
			IDataMartModelRetriever dataMartModelRetriever = QbeConfiguration.getInstance().getDataMartModelRetriever();
			viewJarFile =  dataMartModelRetriever.getViewJarFile(datamartName, viewName);
		}catch (Exception e) {
			Logger.error(DataMartModel.class, e);
		}
		
		return viewJarFile;
	}



	/* (non-Javadoc)
	 * @see it.eng.qbe.datasource.IHibernateDataSource#getDialect()
	 */
	public String getDialect() {
		return dialect;
	}



	/* (non-Javadoc)
	 * @see it.eng.qbe.datasource.IHibernateDataSource#setDialect(java.lang.String)
	 */
	public void setDialect(String dialect) {
		this.dialect = dialect;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.datasource.IHibernateDataSource#getJndiDataSourceName()
	 */
	public String getJndiDataSourceName() {
		return jndiDataSourceName;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.datasource.IHibernateDataSource#setJndiDataSourceName(java.lang.String)
	 */
	public void setJndiDataSourceName(String jndiDataSourceName) {
		this.jndiDataSourceName = jndiDataSourceName;
	}

	
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.datasource.IHibernateDataSource#getHibCfg()
	 */
	public Configuration getHibCfg() {
		return getConfiguration();
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.datasource.IHibernateDataSource#setHibCfg(org.hibernate.cfg.Configuration)
	 */
	public void setHibCfg(Configuration hibCfg) {
		this.configuration = hibCfg;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.datasource.IHibernateDataSource#isClassLoaderExtended()
	 */
	public boolean isClassLoaderExtended() {
		return classLoaderExtended;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.datasource.IHibernateDataSource#setClassLoaderExtended(boolean)
	 */
	public void setClassLoaderExtended(boolean classLoaderExtended) {
		this.classLoaderExtended = classLoaderExtended;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.datasource.IHibernateDataSource#getAlreadyAddedView()
	 */
	public List getAlreadyAddedView() {
		return alreadyAddedView;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.datasource.IHibernateDataSource#setAlreadyAddedView(java.util.List)
	 */
	public void setAlreadyAddedView(List alreadyAddedView) {
		this.alreadyAddedView = alreadyAddedView;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.datasource.IHibernateDataSource#setSessionFactory(org.hibernate.SessionFactory)
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.datasource.IHibernateDataSource#getName()
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.datasource.IHibernateDataSource#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	public void refresh() {
		configuration = null;
		sessionFactory = null;
		setClassLoaderExtended(false);
		setAlreadyAddedView(new ArrayList());		
	}



	public List getDatamartNames() {
		return datamartNames;
	}	
	
	private String getDatamartName() {
		return (String)datamartNames.get(0);
	}

	public String getCompositeDatamartName() {
		return getDatamartName();
	}
	
	public String getCompositeDatamartDescription() {
		return getDatamartName();
	}

	public void refreshDatamartViews() {
		refresh();
	}

	public void refreshSharedView(String sharedViewName) {
		refreshDatamartViews();
	}

	public void refreshSharedViews() {
		refreshDatamartViews();
	}
}
