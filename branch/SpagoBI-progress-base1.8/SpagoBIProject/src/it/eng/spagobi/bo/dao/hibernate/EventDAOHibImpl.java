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
package it.eng.spagobi.bo.dao.hibernate;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.Event;
import it.eng.spagobi.bo.dao.IEventDAO;
import it.eng.spagobi.metadata.HibernateUtil;
import it.eng.spagobi.metadata.SbiEvents;
import it.eng.spagobi.metadata.SbiSubreports;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * @author Gioia
 *
 */
public class EventDAOHibImpl extends AbstractHibernateDAO implements IEventDAO{
	
	public Event loadEvent(Integer eventId, String user) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		Event realResult = null;
		String hql = null;
		Query hqlQuery = null;
		
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
		
			SbiEvents hibEvent = (SbiEvents)aSession.load(SbiEvents.class, eventId);
			
			realResult = toEvent(hibEvent);
			tx.commit();
		} catch (HibernateException he) {
			logException(he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		return realResult;
	}

	public List loadEvents(String user) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		List realResult = new ArrayList();
		String hql = null;
		Query hqlQuery = null;
		
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();

			hql = "from SbiEvents as event " + 
	         "where event.user = '" + user + "'";
			
			hqlQuery = aSession.createQuery(hql);
			List hibList = hqlQuery.list();
			
			Iterator it = hibList.iterator();

			while (it.hasNext()) {
				realResult.add(toEvent((SbiEvents) it.next()));
			}
			tx.commit();
		} catch (HibernateException he) {
			logException(he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		return realResult;
	}

	public Integer registerEvent(String user) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		aSession = getSession();
		
		tx = aSession.beginTransaction();
		SbiEvents hibEvent = new SbiEvents();
		hibEvent.setUser(user);
		aSession.save(hibEvent);	
		tx.commit();
		return hibEvent.getId();
	}

	public void unregisterEvent(Integer id, String user) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		String hql = null;
		Query hqlQuery = null;
		
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();			
			SbiEvents hibEvent = new SbiEvents(id, user);			
			aSession.delete(hibEvent);
			tx.commit();
		} catch (HibernateException he) {
			logException(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} catch (Exception ex) {
			logException(ex);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100); 
		}	finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
	}

	public void unregisterEvent(Event event) throws EMFUserError {
		unregisterEvent(event.getId(), event.getUser());		
	}

	public void unregisterEvents(String user) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		String hql = null;
		Query hqlQuery = null;
		List events = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			hql = "from SbiEvents as event " + 
	         "where event.user = '" + user + "'";
			
			hqlQuery = aSession.createQuery(hql);
			events = hqlQuery.list();
			
			Iterator it = events.iterator();
			while (it.hasNext()) {
				aSession.delete((SbiEvents) it.next());
			}			
			tx.commit();
		} catch (HibernateException he) {
			logException(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} catch (Exception ex) {
			logException(ex);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100); 
		}	finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}		
	}	

	private Event toEvent(SbiEvents hibEvent) {
		Event event = new Event();
		event.setId(hibEvent.getId());
		event.setUser(hibEvent.getUser());
		return event;
	}
	
	/*
	public static void main(String[] args) throws EMFUserError {
		EventDAOHibImpl eventDAO = new EventDAOHibImpl();
		for(int i = 0; i < 10; i++) {
			Integer id = eventDAO.registerEvent("root");
			System.out.println("Id: " + id);
		}
	}
	*/
}
