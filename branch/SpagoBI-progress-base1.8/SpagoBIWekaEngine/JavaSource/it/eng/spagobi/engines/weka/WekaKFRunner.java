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
package it.eng.spagobi.engines.weka;

import java.beans.beancontext.BeanContextChild;
import java.beans.beancontext.BeanContextSupport;
import java.io.File;
import java.util.Vector;

import org.apache.log4j.Logger;

import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.DatabaseLoader;
import weka.core.converters.DatabaseSaver;
import weka.gui.beans.BeanConnection;
import weka.gui.beans.BeanInstance;
import weka.gui.beans.Loader;
import weka.gui.beans.Saver;
import weka.gui.beans.xml.XMLBeans;

/**
 * @author Gioia
 *
 */
public class WekaKFRunner {
	
	protected BeanContextSupport beanContextSupport;
	protected Vector beans;
	protected Vector connections;
	protected Vector loaders;
	protected Vector savers;
	
	//	Connection parameters for db Loaders and Savers
	protected String dbUrl = DEFAULT_DB_URL;
	protected String dbUser = DEFAULT_DB_USER;
	protected String dbPassword = DEFAULT_DB_PASSWORD;
	
	private static final String DEFAULT_DB_URL = "jdbc:mysql://localhost/foodmart";
	private static final String DEFAULT_DB_USER = "root";
	private static final String DEFAULT_DB_PASSWORD = "admin";
	
	private static transient Logger logger = Logger.getLogger(WekaKFRunner.class);
	
	static private void log(String msg) {
		logger.debug("WekaKFRunner:" + msg);
	}
	
	
	public WekaKFRunner() { }
	
	public Vector getLoaders() {
		return loaders;
	}

	public Vector getSavers() {
		return savers;
	}
	
	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}
	
	public void reset() {
		this.beanContextSupport = new BeanContextSupport();
		this.beans = new Vector();
		this.connections = new Vector();
		this.loaders = new Vector();
		this.savers = new Vector();
	}
	
	public void loadKFTemplate(File template) throws Exception {
		reset();
		XMLBeans xml = new XMLBeans(null, beanContextSupport); 
		Vector v     = (Vector) xml.read(template);
		beans        = (Vector) v.get(XMLBeans.INDEX_BEANINSTANCES);
		connections  = (Vector) v.get(XMLBeans.INDEX_BEANCONNECTIONS);
		
		beanContextSupport = new BeanContextSupport(); // why?
		beanContextSupport.setDesignTime(true);
				
		for(int i = 0; i < beans.size(); i++) {
			BeanInstance bean = (BeanInstance)beans.get(i);
			log("   " + (i+1) + ". " + bean.getBean().getClass().getName());
		
			// register loaders
			if (bean.getBean() instanceof Loader) {
				log("    - Loader [" + 
					((Loader)bean.getBean()).getLoader().getClass() + "]");
								
				loaders.add(bean.getBean());
			}	
			
			// register savers
			if (bean.getBean() instanceof Saver) {
				log("    - Saver [" + 
					((Saver)bean.getBean()).getSaver().getClass() + "]");
								
				savers.add(bean.getBean());
			}	
			
			if (bean.getBean() instanceof BeanContextChild) {
				log("    - BeanContextChild" );
				beanContextSupport.add(bean.getBean());
			}			
		}
		
		for(int i = 0; i < connections.size(); i++) {
			BeanConnection connection = (BeanConnection)connections.get(i);
			log("   " + (i+1) + ". " + connection.getClass().getName());
		}	
		
		BeanInstance.setBeanInstances(beans, null);
		BeanConnection.setConnections(connections);
	}
	
	/**
	 *  Setup Loader filling missing parameter values	 *
	 */
	public void setupLoaders() {		
		for(int i = 0; i < loaders.size(); i++) {
			Loader loader = (Loader)loaders.get(i);						
			String className = loader.getLoader().getClass().getName();
			
			if(className.equalsIgnoreCase(DatabaseLoader.class.getName())) {
				DatabaseLoader databaseLoader = (DatabaseLoader)loader.getLoader();
				
				// l'url del db, il nome utente e la password non sono 
				// memorizzati nel tempalte file quindi è necessario riinserirli a
				// mano al termine del processo di parsing
				databaseLoader.setUrl(dbUrl);
				databaseLoader.setUser(dbUser);
				databaseLoader.setPassword(dbPassword);		
			}
			else if(className.equalsIgnoreCase(ArffLoader.class.getName())) {
				ArffLoader arffLoader = (ArffLoader)loader.getLoader();
				// setup operation goes here
			}
			else if(className.equalsIgnoreCase(CSVLoader.class.getName())) {
				CSVLoader csvLoader = (CSVLoader)loader.getLoader();
				// setup operation goes here				
			}		
		}
	}
	
	/**
	 *  Setup Saver filling missing parameter values	 *
	 */
	public void setupSavers() {
		for(int i = 0; i < savers.size(); i++) {
			Saver saver = (Saver)savers.get(i);
			
			
			
			String className = saver.getSaver().getClass().getName();
			if(className.equalsIgnoreCase(DatabaseSaver.class.getName())) {
				DatabaseSaver databaseSaver = (DatabaseSaver)saver.getSaver();
				// forzando la modalità di scrittura BATCH il metodo run non torna 
				// fino a che tutte le instanze non sono state scritte sul db. 
				// In futuro sarebbe meglio sostituire gli oggetti databaseSaver con
				// un oggetto custom per la scrittura su db più sofisticato
				//databaseSaver.setRetrieval(databaseSaver.BATCH);
				
				// l'url del db, il nome utente e la password non sono 
				// memorizzati nel tempalte file quindi è necessario riinserirli a
				// mano al termine del processo di parsing
				databaseSaver.setUrl(dbUrl);
				databaseSaver.setUser(dbUser);
				databaseSaver.setPassword(dbPassword);						
			}			
			else if(className.equalsIgnoreCase(ArffSaver.class.getName())) {
				ArffSaver arffSaver = (ArffSaver)saver.getSaver();				
				// setup operation goes here			
			}
		}
	}
		
	public void run() {
		run(true);
	}
	
	public void run(boolean forceSetup) {
		if(forceSetup) {
			log("Configuring loaders & savers ...");
			setupLoaders();
			setupSavers();
		}		
				
		for(int i = 0; i < loaders.size(); i++) {
			Loader loader = (Loader)loaders.get(i);
			log("Start loading: " + loader );			
			loader.startLoading();			
		}		
	}	
}
