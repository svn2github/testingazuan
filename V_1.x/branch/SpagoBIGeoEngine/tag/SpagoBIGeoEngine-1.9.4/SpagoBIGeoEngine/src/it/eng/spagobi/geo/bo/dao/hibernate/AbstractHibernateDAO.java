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
package it.eng.spagobi.geo.bo.dao.hibernate;


import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.geo.configuration.Constants;
import it.eng.spagobi.geo.metadata.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.SessionFactory;



/**
 * Abstract class that al DAO will have to extend.
 * 
 * @author Giachino
 */
public class AbstractHibernateDAO {
	private static SessionFactory sessionFactory = null;
	/**
	 * Gets tre current session.
	 * @return The current session object.
	 */
	/*
	public Session getSession(){
		return HibernateUtil.currentSession();
	}
	*/
	/**
	* Gets tre current session.
	* @return The current session object.
	*/
public Session getSession() {
	Session session = null;

	if(AbstractHibernateDAO.sessionFactory == null)
		AbstractHibernateDAO.sessionFactory = HibernateUtil.getSessionFactory();
//	AbstractHibernateDAO.sessionFactory = getSessionFactory();

	try {
	session = AbstractHibernateDAO.sessionFactory.openSession();
	} catch (Exception e) {
//	 Make sure you log the exception, as it might be swallowed
	TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.DEBUG,"Opening a new sesion failed."+e.getLocalizedMessage());
	
	throw new ExceptionInInitializerError(e);
	}
	return session;
	}
	/**
	 * Traces the exception information of a throwable input object
	 * @param t The input throwable object
	 */
	public void logException(Throwable t){
		TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.DEBUG, t.getMessage());

	}
}
