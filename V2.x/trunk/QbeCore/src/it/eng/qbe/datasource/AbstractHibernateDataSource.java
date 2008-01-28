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

import it.eng.qbe.bo.Formula;
import it.eng.qbe.conf.QbeConf;
import it.eng.qbe.locale.LocaleUtils;
import it.eng.qbe.log.Logger;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.model.io.IDataMartModelRetriever;
import it.eng.spago.base.ApplicationContainer;
import it.eng.spagobi.utilities.DynamicClassLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Table;

public abstract class AbstractHibernateDataSource extends AbstractDataSource implements IHibernateDataSource {

	private String datamartName = null;
	private List datamartNames = null;
	
	private Map dblinkMap = null;
	
	private DBConnection connection = null;
	//private String jndiDataSourceName = null;	
	//private String dialect = null;		
	
	private Formula formula = null;
	
	
	
	protected Configuration buildEmptyConfiguration() {
		Configuration cfg = null;
		
		cfg = new Configuration();
		
		if(connection.isJndiConncetion()) {
			cfg.setProperty("hibernate.connection.datasource", connection.getJndiName());
		} else {
			cfg.setProperty("hibernate.connection.url", connection.getUrl());
			cfg.setProperty("hibernate.connection.password", connection.getPassword());
			cfg.setProperty("hibernate.connection.username", connection.getUsername());
			cfg.setProperty("hibernate.connection.driver_class", connection.getDriverClass());
		}
				
		cfg.setProperty("hibernate.dialect", connection.getDialect());
		
		cfg.setProperty("hibernate.cglib.use_reflection_optimizer", "true");
		cfg.setProperty("hibernate.show_sql", "false");
		
		return cfg;
	}	

	
	public String getDatamartName() {
		return datamartName;
	}

	public void setDatamartName(String datamartName) {
		this.datamartName = datamartName;
	}
	
	/*
	public String getDialect() {
		return dialect;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}
	
	public String getJndiDataSourceName() {
		return jndiDataSourceName;
	}

	public void setJndiDataSourceName(String jndiDataSourceName) {
		this.jndiDataSourceName = jndiDataSourceName;
	}
	*/
	
	protected File getDatamartJarFile(String datamartName){
		File datamartJarFile = null;
		
		try{
			IDataMartModelRetriever dataMartModelRetriever = QbeConf.getInstance().getDataMartModelRetriever();
			datamartJarFile = dataMartModelRetriever.getDatamartJarFile(datamartName);
		}catch (Exception e) {
			Logger.error(DataMartModel.class, e);
		}
		
		return datamartJarFile;
	}
	
	protected List getViewNames(String datamartName) {
		List viewNames = null;
		IDataMartModelRetriever dataMartModelRetriever;
		try {
			dataMartModelRetriever = QbeConf.getInstance().getDataMartModelRetriever();
			viewNames = dataMartModelRetriever.getViewNames(datamartName);
		} catch (Exception e) {
			Logger.error(DataMartModel.class, e);
		}		
		
		return viewNames;
	}
	
	protected File getViewJarFile(String datamartName, String viewName){
		File viewJarFile = null;
		
		try{
			IDataMartModelRetriever dataMartModelRetriever = QbeConf.getInstance().getDataMartModelRetriever();
			viewJarFile =  dataMartModelRetriever.getViewJarFile(datamartName, viewName);
		}catch (Exception e) {
			Logger.error(DataMartModel.class, e);
		}
		
		return viewJarFile;
	}
	
	
	
	
	
	protected Properties loadLabelProperties(String datamartName) {
		Properties labelProperties = new Properties();
		
		File dmJarFile = getDatamartJarFile(datamartName);
		JarFile jf;
		try {
			jf = new JarFile(dmJarFile);		
			
			labelProperties = LocaleUtils.getLabelProperties(jf);
					
			List viewNames = getViewNames(datamartName);
			Iterator it = viewNames.iterator();
			while(it.hasNext()) {
				String viewName = (String)it.next();
				File viewJarFile = getViewJarFile(datamartName, viewName);
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
	
	protected Properties loadLabelProperties(String datamartName, Locale locale) {
		
		Properties labelProperties = null;
		
		File dmJarFile = getDatamartJarFile(datamartName);
		if(dmJarFile == null || !dmJarFile.exists()) return new Properties();
		
		try{
			
			JarFile jf = new JarFile(dmJarFile);
			
			labelProperties = LocaleUtils.getLabelProperties(jf, locale);
				
			if (labelProperties.isEmpty()) {
				return loadLabelProperties(datamartName);
			} else {
				List viewNames = getViewNames(datamartName);
				Iterator it = viewNames.iterator();
				while(it.hasNext()) {
					String viewName = (String)it.next();
					File viewJarFile = getViewJarFile(datamartName, viewName);
					jf = new JarFile(viewJarFile);
					Properties tmpProps = LocaleUtils.getLabelProperties(jf, locale);
					if(tmpProps.isEmpty()) tmpProps = LocaleUtils.getLabelProperties(jf);
					labelProperties.putAll(tmpProps);
				}
			}			
		}catch (Exception e) {
			e.printStackTrace();
			return new Properties();
		}	
		
		
		return labelProperties;	
	}
	
	
	
	protected Properties loadQbeProperties(String datamartName) {
		
		Properties qbeProperties = null;
		
		File dmJarFile = getDatamartJarFile(datamartName);
		if(dmJarFile == null || !dmJarFile.exists()) return new Properties();
		
		JarFile jf = null;
		try {
			jf = new JarFile(dmJarFile);
			qbeProperties = loadQbePropertiesFormJarFile(jf);
			
			List viewNames = getViewNames(datamartName);
			Iterator it = viewNames.iterator();
			while(it.hasNext()) {
				String viewName = (String)it.next();
				File viewJarFile = getViewJarFile(datamartName, viewName);
				jf = new JarFile(viewJarFile);
				Properties tmpProps = loadQbePropertiesFormJarFile(jf);
				qbeProperties.putAll(tmpProps);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return new Properties();
		}				
		
		return qbeProperties;	
	}
	
	private Properties loadQbePropertiesFormJarFile(JarFile jf){
		Properties prop = null;
		
		try{
			ZipEntry ze = jf.getEntry("qbe.properties");
			if (ze != null){
				prop = new Properties();
				prop.load(jf.getInputStream(ze));
			} else {
				prop = new Properties();
			}
		} catch(IOException ioe){
			ioe.printStackTrace();
			return new Properties();
		}
		return prop;
	}
	
	
	
	

	
	
	/*
	protected File loadFormulaFile(String datamartName) {
		String formulaFile = getDatamartJarFile( datamartName ).getParent() + "/formula.xml";
		return new File(formulaFile);
	}
	*/
	protected File loadFormulaFile(String datamartName) {
		String formulaFile = getDatamartJarFile( datamartName ).getParent() + "/formula.xml";
		return new File(formulaFile);
	}
	
	
	
	
	
	
	
	
	
	
	

	protected static void updateCurrentClassLoader(File jarFile){
		
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


	public DBConnection getConnection() {
		return connection;
	}


	public void setConnection(DBConnection connection) {
		this.connection = connection;
	}


	public List getDatamartNames() {
		return datamartNames;
	}


	public void setDatamartNames(List datamartNames) {
		this.datamartNames = datamartNames;
	}


	public Map getDblinkMap() {
		return dblinkMap;
	}


	public void setDblinkMap(Map dblinkMap) {
		this.dblinkMap = dblinkMap;
	}

		
	protected void addDbLink(String dmName, Configuration srcCfg, Configuration dstCfg) {
		
		String dbLink = null;
		PersistentClass srcPersistentClass = null;
		PersistentClass dstPersistentClass = null;
		String targetEntityName = null;
		Table targetTable = null;
		
		if(dbLink != null) {
			dbLink = (String)dblinkMap.get(dmName);
			Iterator it = srcCfg.getClassMappings();
			while(it.hasNext()) {
				srcPersistentClass = (PersistentClass)it.next();
				targetEntityName = srcPersistentClass.getEntityName();
				dstPersistentClass = dstCfg.getClassMapping(targetEntityName);
				targetTable = dstPersistentClass.getTable();
				targetTable.setName(targetTable.getName() + "@" + dbLink);
			}
		}
		
	}


	public Formula getFormula() {
		return formula;
	}


	public void setFormula(Formula formula) {
		this.formula = formula;
	}
}
