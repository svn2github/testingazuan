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
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.EventLog;
import it.eng.spagobi.bo.dao.IEventLogDAO;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.metadata.SbiEventRole;
import it.eng.spagobi.metadata.SbiEventRoleId;
import it.eng.spagobi.metadata.SbiEventsLog;
import it.eng.spagobi.metadata.SbiExtRoles;
import it.eng.spagobi.services.modules.ListEnginesModule;
import it.eng.spagobi.services.modules.ListParametersModule;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @author Gioia
 *
 */
public class EventLogDAOHibImpl extends AbstractHibernateDAO implements IEventLogDAO {
	
	/**
	 * @see it.eng.spagobi.bo.dao.IEventLogDAO#loadEventLogById(Integer)
	 * 
	 */
	public EventLog loadEventLogById(Integer id) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		EventLog realResult = null;
//		String hql = null;
//		Query hqlQuery = null;
		
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiEventsLog aSbiEventsLog = (SbiEventsLog) aSession.get(SbiEventsLog.class, id);
//			hql = "from SbiEventsLog as eventlog " + 
//	         "where eventlog.user = '" + user + "' and " + 
//	         "eventlog.id = '" + id + "' and " +
//	         "eventlog.date = :eventDate";
//			
//			long time = Long.valueOf(date).longValue();
//			
//			hqlQuery = aSession.createQuery(hql);
//			hqlQuery.setTimestamp("eventDate", new Date(time));
//			SbiEventsLog aSbiEventsLog = (SbiEventsLog) hqlQuery.uniqueResult();
			
			if (aSbiEventsLog == null) return null; 
			
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

	
	/**
	 * @see it.eng.spagobi.bo.dao.IEventLogDAO#loadEventsLogByUser(it.eng.spago.security.IEngUserProfile)
	 * 
	 */
	public List loadEventsLogByUser(IEngUserProfile profile) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		List realResult = new ArrayList();
		String hql = null;
		Query hqlQuery = null;
		Collection roles = null;
		try {
			roles = profile.getRoles();
		} catch (EMFInternalError e) {
			logException(e);
			return new ArrayList();
		}
		if (roles == null || roles.size() == 0) return new ArrayList();
		boolean isFirtElement = true;
		String collectionRoles = "";
		Iterator rolesIt = roles.iterator();
		while (rolesIt.hasNext()) {
			String roleName = (String) rolesIt.next();
			if (isFirtElement) {
				collectionRoles += roleName;
				isFirtElement = false;
			} else {
				collectionRoles += "', '" + roleName;
			}
		}
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();

			hql = 
				"select " +
					"eventlog " +
				"from " +
					"SbiEventsLog as eventlog, " +
					"SbiEventRole as eventRole, " +
					"SbiExtRoles as roles " + 
	         	"where " +
	         		"eventlog.id = eventRole.id.event.id and " +
	         		"eventRole.id.role.extRoleId = roles.extRoleId " +
	         		"and " +
	         		"roles.name in ('" + collectionRoles + "') " +
	         	"order by " +
	         		"eventlog.date";
			
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

	/**
	 * @see it.eng.spagobi.bo.dao.IEventLogDAO#insertEventLog(it.eng.spagobi.bo.EventLog)
	 * 
	 */
	public Integer insertEventLog(EventLog eventLog) throws EMFUserError {
		Session session = null;
		Transaction tx = null;
		session = getSession();
		tx = session.beginTransaction();
		//SbiEventsLog hibEventLog = toSbiEventsLog(aSession, eventLog);
		
		SbiEventsLog hibEventLog = new SbiEventsLog();
		//hibEventLog.setId(eventLog.getId());
		hibEventLog.setUser(eventLog.getUser());
		hibEventLog.setDate(eventLog.getDate());
		hibEventLog.setDesc(eventLog.getDesc());
		hibEventLog.setParams(eventLog.getParams());
		hibEventLog.setHandler(eventLog.getHandler());
		session.save(hibEventLog);
		Set hibEventRoles = new HashSet();
		List roles = eventLog.getRoles();
		Iterator rolesIt = roles.iterator();
		while (rolesIt.hasNext()) {
			String roleName = (String) rolesIt.next();
			String hql = "from SbiExtRoles as roles " + 
	         "where roles.name = '" + roleName + "'";
			
			Query hqlQuery = session.createQuery(hql);
			SbiExtRoles aHibRole = (SbiExtRoles) hqlQuery.uniqueResult();
			if (aHibRole == null) {
				SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, 
			            this.getClass().getName(), 
			            "toSbiEventsLog", 
			            "Role with name = '" + roleName + "' does not exist!!");
				continue;
			}
			SbiEventRoleId eventRoleId = new SbiEventRoleId();
			eventRoleId.setEvent(hibEventLog);
			eventRoleId.setRole(aHibRole);
			SbiEventRole aSbiEventRole = new SbiEventRole(eventRoleId);
			session.save(aSbiEventRole);
			hibEventRoles.add(aSbiEventRole);
		}
		hibEventLog.setRoles(hibEventRoles);
		tx.commit();
		return hibEventLog.getId();
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IEventLogDAO#eraseEventLog(it.eng.spagobi.bo.EventLog)
	 * 
	 */
	public void eraseEventLog(EventLog eventLog) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();			
			SbiEventsLog hibEventLog = (SbiEventsLog) aSession.load(SbiEventsLog.class, eventLog.getId());
			Set roles = hibEventLog.getRoles();
			Iterator rolesIt = roles.iterator();
			while (rolesIt.hasNext()) {
				SbiEventRole aSbiEventRole = (SbiEventRole) rolesIt.next();
				aSession.delete(aSbiEventRole);
			}
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

	/**
	 * @see it.eng.spagobi.bo.dao.IEventLogDAO#eraseEventsLogByUser(String)
	 * 
	 */
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
				SbiEventsLog aSbiEventsLog = (SbiEventsLog) it.next();
				Set roles = aSbiEventsLog.getRoles();
				Iterator rolesIt = roles.iterator();
				while (rolesIt.hasNext()) {
					SbiEventRole aSbiEventRole = (SbiEventRole) rolesIt.next();
					aSession.delete(aSbiEventRole);
				}
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
		eventLog.setHandler(hibEventLog.getHandler());
		List roles = new ArrayList();
		Set rolesSet = hibEventLog.getRoles();
		Iterator rolesIt = rolesSet.iterator();
		while (rolesIt.hasNext()) {
			SbiEventRole hibEventRole = (SbiEventRole) rolesIt.next();
		    SbiExtRoles hibRole = hibEventRole.getId().getRole();
		    roles.add(hibRole.getName());
		}
		eventLog.setRoles(roles);
		return eventLog;
	}
	
}
