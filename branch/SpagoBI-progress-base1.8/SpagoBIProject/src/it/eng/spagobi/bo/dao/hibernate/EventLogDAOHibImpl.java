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

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.EventLog;
import it.eng.spagobi.bo.dao.IEventLogDAO;
import it.eng.spagobi.metadata.SbiEventsLog;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @author Gioia
 *
 */
public class EventLogDAOHibImpl extends AbstractHibernateDAO implements IEventLogDAO {
	
	public EventLog loadEventLog(String id, String user, String date) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		EventLog realResult = null;
		String hql = null;
		Query hqlQuery = null;
		
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();

			hql = "from SbiEventsLog as eventlog " + 
	         "where eventlog.user = '" + user + "' and " + 
	         "eventlog.id = '" + id + "' and " +
	         "eventlog.date = :eventDate";
			
			long time = Long.valueOf(date).longValue();
			
			hqlQuery = aSession.createQuery(hql);
			hqlQuery.setTimestamp("eventDate", new Date(time));
			SbiEventsLog aSbiEventsLog = (SbiEventsLog) hqlQuery.uniqueResult();
			realResult = toEventsLog(aSbiEventsLog);
			
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

	
	
	public List loadEventsLogByUser(String user) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		List realResult = new ArrayList();
		String hql = null;
		Query hqlQuery = null;
		
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();

			hql = "from SbiEventsLog as eventlog " + 
	         "where eventlog.user = '" + user + "' order by eventlog.date";
			
			hqlQuery = aSession.createQuery(hql);
			List hibList = hqlQuery.list();
			
			Iterator it = hibList.iterator();

			while (it.hasNext()) {
				realResult.add(toEventsLog((SbiEventsLog) it.next()));
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

	public void insertEventLog(EventLog eventLog) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		aSession = getSession();
		
		tx = aSession.beginTransaction();
		SbiEventsLog hibEventLog = toSbiEventsLog(eventLog);		
		aSession.save(hibEventLog);	
		tx.commit();	
	}

	public void eraseEventLog(EventLog eventLog) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();			
			SbiEventsLog hibEventLog = toSbiEventsLog(eventLog);			
			aSession.delete(hibEventLog);
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

	public void eraseEventsLogByUser(String user) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		String hql = null;
		Query hqlQuery = null;
		List events = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			hql = "from SbiEventsLog as eventlog " + 
	         "where eventlog.user = '" + user + "'";
			
			hqlQuery = aSession.createQuery(hql);
			events = hqlQuery.list();
			
			Iterator it = events.iterator();
			while (it.hasNext()) {
				aSession.delete((SbiEventsLog) it.next());
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
	
	private EventLog toEventsLog(SbiEventsLog hibEventLog) {
		EventLog eventLog = new EventLog();
		eventLog.setId(hibEventLog.getId());
		eventLog.setUser(hibEventLog.getUser());
		eventLog.setDate(hibEventLog.getDate());
		eventLog.setDesc(hibEventLog.getDesc());
		eventLog.setParams(hibEventLog.getParams());
		return eventLog;
	}
	
	private SbiEventsLog toSbiEventsLog(EventLog eventLog) {
		SbiEventsLog hibEventLog = new SbiEventsLog();
		hibEventLog.setId(eventLog.getId());
		hibEventLog.setUser(eventLog.getUser());
		hibEventLog.setDate(eventLog.getDate());
		hibEventLog.setDesc(eventLog.getDesc());
		hibEventLog.setParams(eventLog.getParams());
		return hibEventLog;
	}
}
