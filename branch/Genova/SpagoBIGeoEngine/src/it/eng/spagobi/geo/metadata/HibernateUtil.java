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
package it.eng.spagobi.geo.metadata;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.geo.configuration.Constants;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {	
	private static SessionFactory sessionFactory = null;
	/*
	static {
		try {
			// Create the SessionFactory
			SourceBean fileCfgSb = ((SourceBean)ConfigSingleton.getInstance().getAttribute("GEOENGINE.HIBERNATE-CFGFILE"));
			
			if (fileCfgSb != null){
				String fileCfg = fileCfgSb.getCharacters();
				fileCfg = fileCfg.trim();
				TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.DEBUG,  "Initializing hibernate Session Factory Described by [" + fileCfg +"]");				
				Configuration conf = new Configuration();
				conf = conf.configure(fileCfg);
				sessionFactory = conf.buildSessionFactory();
				
				//sessionFactory = new Configuration().configure(fileCfg).buildSessionFactory();
			}else{
				TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.DEBUG,  "Initializing hibernate Session Factory with default configuration [hibernate.cfg.xml]");				
				sessionFactory = new Configuration().configure()
					.buildSessionFactory();
			}
		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.DEBUG,  "Initial SessionFactory creation failed. Error: "+ex.getLocalizedMessage());						
			throw new ExceptionInInitializerError(ex);
		}
	}

	
*/
	public static Session currentSession() {
		return sessionFactory.openSession();
	}

	static public SessionFactory getSessionFactory() {
		if(sessionFactory == null) init();
		return sessionFactory;
	}
	
	
	private static void init() {
		try {
			// Create the SessionFactory
			SourceBean fileCfgSb = ((SourceBean)ConfigSingleton.getInstance().getAttribute("GEOENGINE.HIBERNATE-CFGFILE"));
			
			if (fileCfgSb != null){
				String fileCfg = fileCfgSb.getCharacters();
				fileCfg = fileCfg.trim();				
				Configuration conf = new Configuration();
				conf = conf.configure(fileCfg);
				sessionFactory = conf.buildSessionFactory();
				
				//sessionFactory = new Configuration().configure(fileCfg).buildSessionFactory();
			}else{
				sessionFactory = new Configuration().configure()
					.buildSessionFactory();
			}
		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.DEBUG,"Initial SessionFactory creation failed."+ex.getLocalizedMessage());
			throw new ExceptionInInitializerError(ex);
		}
	}
	
}
