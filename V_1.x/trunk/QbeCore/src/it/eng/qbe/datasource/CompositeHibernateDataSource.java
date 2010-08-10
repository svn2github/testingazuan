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
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Table;

/**
 * @author Andrea Gioia
 *
 */
public class CompositeHibernateDataSource extends BasicDataSource implements IHibernateDataSource  {
	
	private String name;
	private List datamartNames = null;
	private Map dblinkMap = null;
	
	
	private boolean classLoaderExtended = false;	
	private List alreadyAddedView = null;
	
	private String jndiDataSourceName = null;	
	private String dialect = null;	
	
	
	
	private Map configurationMap = new HashMap();	
	private Map sessionFactoryMap = new HashMap();	
	
	private Configuration compositeConfiguration = null;
	private SessionFactory compositeSessionFactory = null;
	
	
	
	
	private File formulaFile = null;
	
	private Properties qbeProperties = null;
	
	private Properties labelProperties = null;
	private Map localizedLabelMap = new HashMap();
	
	public CompositeHibernateDataSource(List datamartNames, String jndiDataSourceName, String dialect) {
		this(datamartNames, new HashMap(), jndiDataSourceName, dialect);
	}
		
	public CompositeHibernateDataSource(List datamartNames, Map dblinkMap, String jndiDataSourceName, String dialect) {
		this.name = buildDatasourceName(datamartNames);		
		this.datamartNames = datamartNames;
		this.dblinkMap = dblinkMap;
		this.jndiDataSourceName = jndiDataSourceName;
		this.dialect = dialect;
		this.alreadyAddedView = new ArrayList();
		type = IDataSource.COMPOSITE_HIBERNATE_DS_TYPE;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.datasource.IHibernateDataSource#getSessionFactory(java.lang.String)
	 */
	public SessionFactory getSessionFactory(String dmName) {
		if(compositeSessionFactory == null) initHibernate();
		return (SessionFactory)sessionFactoryMap.get(dmName);
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.datasource.IHibernateDataSource#getSessionFactory()
	 */
	public SessionFactory getSessionFactory() {
		if(compositeSessionFactory == null) initHibernate();
		return compositeSessionFactory;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.datasource.IHibernateDataSource#getConfiguration(java.lang.String)
	 */
	public Configuration getConfiguration(String dmName) {
		if(compositeConfiguration == null) initHibernate();
		return (Configuration)configurationMap.get(dmName);
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.datasource.IHibernateDataSource#getConfiguration()
	 */
	public Configuration getConfiguration() {
		if(compositeConfiguration == null) initHibernate();
		return compositeConfiguration;
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
			
		List jarFiles = getJarFiles();
		for(int i = 0; i < jarFiles.size(); i++) {
		
			File dmJarFile = (File)jarFiles.get(i);
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
		}
				
		return labelProperties;	
	}
	
	private Properties loadLabelProperties(Locale locale) {
		
		Properties props = new Properties();
		
		List jarFiles = getJarFiles();
		for(int i = 0; i < jarFiles.size(); i++) {
		
			File dmJarFile = (File)jarFiles.get(i);
			try{
				
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
		}
		
		return props;
	
	}
	
		
	public File getFormulaFile() {
		if(formulaFile == null) {
			try {
				formulaFile = loadFormulaFile();
			} catch (IOException e) {
				e.printStackTrace();
				List jarFiles = getJarFiles();
				File jarFile = (File)jarFiles.get(0);
				formulaFile = new File(jarFile.getParent() + "/formula.xml");
			}
		}
		return formulaFile;
	}
	
	private File loadFormulaFile() throws IOException {
		throw new IOException("loadFormulaFile not supported for CompositeHibernateDataSource");
	}
	
	
	
	
	public Properties getQbeProperties() {
		if(qbeProperties == null) qbeProperties = loadQbeProperties();
		return qbeProperties;
	}
	
	private Properties loadQbeProperties() {
		
		Properties qbeProperties = null;
		
		List jarFiles = getJarFiles();
		for(int i = 0; i < jarFiles.size(); i++) {
		
			File dmJarFile = (File)jarFiles.get(i);
		
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
	
	
	
	
	
	
	
	private void initHibernate(){
		Logger.debug(this.getClass(), "initSessionFactories: start method initSessionFactories");
				
		compositeConfiguration = buildEmptyConfiguration();
		
		addDatamarts();
		addSharedViews();
		addDbLinks();	
		
		compositeSessionFactory = compositeConfiguration.buildSessionFactory();
	}	
	
	private Configuration buildEmptyConfiguration() {
		Configuration cfg = null;
		
		cfg = new Configuration();
		cfg.setProperty("hibernate.dialect", dialect);
		cfg.setProperty("hibernate.connection.datasource", jndiDataSourceName);
		cfg.setProperty("hibernate.cglib.use_reflection_optimizer", "true");	
		
		return cfg;
	}
	
	private void addDatamarts() {
		
		for(int i = 0; i < datamartNames.size(); i++) {
			String dmName = (String)datamartNames.get(i);
			addDatamart(dmName, !classLoaderExtended);		
		}	
		classLoaderExtended = true;
	}
	
	private void addDatamart(String dmName, boolean extendClassLoader) {
		Configuration cfg = null;	
		SessionFactory sf = null;
		File jarFile = null;
		
		jarFile = getDatamartJarFile(dmName);
		if(jarFile == null) return;
		
		cfg = buildEmptyConfiguration();
		configurationMap.put(dmName, cfg);
		
		if (extendClassLoader){
			updateCurrentClassLoader(jarFile);
		}	
		
		cfg.addJar(jarFile);
		
		try {
			compositeConfiguration.addJar(jarFile);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		
		addViews(dmName);
		
		sf = cfg.buildSessionFactory();
		sessionFactoryMap.put(dmName, sf);		
	}
	
	private boolean addViews(String datamartName) {
		return addViews(datamartName, datamartName);
	}
	
	private boolean addViews(String datamartName, String configurationName) {		
		boolean result = false;
		
		List viewNames = getViewNames(datamartName);
		if(viewNames.size() > 0) {
			for(int i = 0; i < viewNames.size(); i++) {
				String viewName = (String)viewNames.get(i);
				result = (result || addView(datamartName, viewName, configurationName));
			}
		}
		
		return result;
	}	
	
	private boolean addView(String datamartName, String viewName, String configurationName) {
		
		boolean result = false;
		
		Configuration cfg = null;
		File viewJarFile = null;
		
		viewJarFile = getViewJarFile(datamartName,viewName);
		if(viewJarFile == null) return false;
		
		cfg = (Configuration)configurationMap.get(configurationName);
		if(cfg == null) {
			cfg = buildEmptyConfiguration();
			configurationMap.put(configurationName, cfg);
		}
		
		if (!(alreadyAddedView.contains(viewJarFile.getAbsolutePath()))){ 
			updateCurrentClassLoader(viewJarFile);
			cfg.addJar(viewJarFile);
			compositeConfiguration.addJar(viewJarFile);
			alreadyAddedView.add(viewJarFile.getAbsolutePath());
			result = true;
		}
		
		return result;
	}
	
	
	private boolean addSharedViews() {
		String sharedViewsName = "Views";
		
		Configuration cfg = null;
		SessionFactory sf = null;
		boolean configurationHasChanged = false;
		
		configurationHasChanged = addViews(getCompositeDatamartName(), sharedViewsName);
		
		if(configurationHasChanged) {
			cfg = (Configuration)configurationMap.get(sharedViewsName);		
			sf = cfg.buildSessionFactory();	
			sessionFactoryMap.put(sharedViewsName, sf);	
		}
		
		return configurationHasChanged;
	}
	
	
	private void addDbLinks() {
		Configuration cfg = null;
		
		for(int i = 0; i < datamartNames.size(); i++) {
			String dmName = (String)datamartNames.get(i);
			String dbLink = (String)dblinkMap.get(dmName);
			if(dbLink != null) {
				cfg = (Configuration)configurationMap.get(dmName);
				Iterator it = cfg.getClassMappings();
				while(it.hasNext()) {
					PersistentClass persistentClass = (PersistentClass)it.next();
					String entityName = persistentClass.getEntityName();
					persistentClass = compositeConfiguration.getClassMapping(entityName);
					Table table = persistentClass.getTable();
					table.setName(table.getName() + "@" + dbLink);
				}
			}
		}
	}
	
	
	
	
	
	
	
	public void refreshDatamartViews() {
		if(compositeConfiguration == null) {
			initHibernate();
			return;
		}
		
		boolean compositeConfigurationHasChanged = false;
		boolean datamartConfigurationHasChanged = false;
		Configuration cfg = null;
		SessionFactory sf = null;
		
		for(int i = 0; i < datamartNames.size(); i++) {
			String datamartName = (String)datamartNames.get(i);
			datamartConfigurationHasChanged = addViews(datamartName);
			if(datamartConfigurationHasChanged) {
				cfg = (Configuration)configurationMap.get(datamartName);
				sf = cfg.buildSessionFactory();
				sessionFactoryMap.put(datamartName, sf);
			}
			compositeConfigurationHasChanged = (compositeConfigurationHasChanged || datamartConfigurationHasChanged);
		}
		
		if(compositeConfigurationHasChanged) {
			
			compositeSessionFactory = compositeConfiguration.buildSessionFactory();
		}
	}
	
	
	public void refreshSharedViews() {
		boolean configurationHasChanged = false;
		String sharedViewsConfiguration = "Views";
		
		Configuration cfg = null;
		SessionFactory sf = null;
		
		if(compositeConfiguration == null) {
			initHibernate();
			return;
		}
		
		configurationHasChanged = addViews(getCompositeDatamartName(), sharedViewsConfiguration);
				
		if(configurationHasChanged) {
			cfg = (Configuration)configurationMap.get(sharedViewsConfiguration);
			sf = cfg.buildSessionFactory();	
			sessionFactoryMap.put(sharedViewsConfiguration, sf);	
			compositeSessionFactory = compositeConfiguration.buildSessionFactory();
		}		
	}
	
	public void refreshSharedView(String sharedViewName) {
		boolean configurationHasChanged = false;
		String sharedViewsConfiguration = "Views";
		
		Configuration cfg = null;
		SessionFactory sf = null;
		
		if(compositeConfiguration == null) {
			initHibernate();
			return;
		}
		
		configurationHasChanged = addView(getCompositeDatamartName(), sharedViewName, sharedViewsConfiguration);
				
		if(configurationHasChanged) {
			cfg = (Configuration)configurationMap.get(sharedViewsConfiguration);
			sf = cfg.buildSessionFactory();	
			sessionFactoryMap.put(sharedViewsConfiguration, sf);	
			compositeSessionFactory = compositeConfiguration.buildSessionFactory();
		}	
	}
	
	
	
	
	
	
	
	
	
	
		
	private File getDatamartJarFile(String datamartName){
		File datamartJarFile = null;
		
		try{
			IDataMartModelRetriever dataMartModelRetriever = QbeConfiguration.getInstance().getDataMartModelRetriever();
			datamartJarFile = dataMartModelRetriever.getDatamartJarFile(datamartName);
		}catch (Exception e) {
			Logger.error(DataMartModel.class, e);
		}
		
		return datamartJarFile;
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
	
	
	/**
	 * @deprecated
	 * @return
	 */
	private List getJarFiles() {
		List jarFiles = new ArrayList();
		for(int i = 0; i < datamartNames.size(); i++) {
			String dmName = (String)datamartNames.get(i);
			File jarFile = getDatamartJarFile(dmName);
			jarFiles.add(jarFile);
		}	
		
		return jarFiles;
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
	
	
	
	
	private static void updateCurrentClassLoader(File jarFile){
		
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
	 * @see it.eng.qbe.datasource.IHibernateDataSource#getPaths()
	 */
	public List getPaths() {
		return datamartNames;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.datasource.IHibernateDataSource#setPaths(java.util.List)
	 */
	public void setPaths(List paths) {
		this.datamartNames = paths;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.datasource.IHibernateDataSource#getHibCfg()
	 */
	public Configuration getHibCfg() {
		return this.getConfiguration();
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.datasource.IHibernateDataSource#setHibCfg(org.hibernate.cfg.Configuration)
	 */
	public void setHibCfg(Configuration hibCfg) {
		this.compositeConfiguration = hibCfg;
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
		this.compositeSessionFactory = sessionFactory;
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
		compositeConfiguration = null;
		compositeSessionFactory = null;
		setClassLoaderExtended(false);
		setAlreadyAddedView(new ArrayList());	
		configurationMap = new HashMap();	
		sessionFactoryMap = new HashMap();	
	}

	

	public List getDatamartNames() {
		return datamartNames;
	}

	public String getCompositeDatamartName() {
		return buildDatamartName(getDatamartNames());
	}

	public String getCompositeDatamartDescription() {
		return buildDatamartDescription(getDatamartNames());
	}

	
	
}
