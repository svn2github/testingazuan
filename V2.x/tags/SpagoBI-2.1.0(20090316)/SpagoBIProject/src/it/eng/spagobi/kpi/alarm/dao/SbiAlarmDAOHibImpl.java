/**
 * Title: SpagoBI
 * Description: SpagoBI
 * Copyright: Copyright (c) 2008
 * Company: Xaltia S.p.A.
 * 
 * @author Enrico Cesaretti
 * @version 1.0
 */

package it.eng.spagobi.kpi.alarm.dao;

import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.kpi.alarm.metadata.SbiAlarm;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * 
 * @see it.eng.spagobi.kpi.alarm.metadata.SbiAlarm
 * @author Enrico Cesaretti
 */
public class SbiAlarmDAOHibImpl extends AbstractHibernateDAO implements ISbiAlarmDAO{

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(SbiAlarmDAOHibImpl.class);

	
    public void insert(SbiAlarm item) {
        Session session = getSession();
        Transaction tx = null;
        try {
        	tx = session.beginTransaction();
			session.save(item);
			tx.commit();
			
		} catch (HibernateException e) {
			if( tx != null && tx.isActive() ){
				tx.rollback();
			}
			throw e;
			
		}finally{
			session.close();
		}
    }
    
    
    public void insert(Session session, SbiAlarm item) {
        session.save(item);
    }

    public void update(SbiAlarm item) {
        Session session = getSession();
        Transaction tx = null;
        try {
        	tx = session.beginTransaction();
			session.update(item);
			tx.commit();
			
		} catch (HibernateException e) {
			if( tx != null && tx.isActive() ){
				tx.rollback();
			}
			throw e;
			
		}finally{
			session.close();
		}
    }	
    
    public void update(Session session, SbiAlarm item) {
        session.update(item);
    }	
	
    public void delete(SbiAlarm item) {
        Session session = getSession();
        Transaction tx = null;
        try {
        	tx = session.beginTransaction();
			session.delete(item);
			tx.commit();
			
		} catch (HibernateException e) {
			if( tx != null && tx.isActive() ){
				tx.rollback();
			}
			throw e;
			
		}finally{
			session.close();
		}
    }
    
    public void delete(Session session, SbiAlarm item) {
       session.delete(item);
    }

    public void delete(Integer id) {
        Session session = getSession();
        Transaction tx = null;
        try {
        	tx = session.beginTransaction();
        	session.delete(session.load(SbiAlarm.class, id));
			tx.commit();
			
		} catch (HibernateException e) {
			if( tx != null && tx.isActive() ){
				tx.rollback();
			}
			throw e;
			
		}finally{
			session.close();
		}
    }
    
    
    public void delete(Session session, Integer id) {
       	session.delete(session.load(SbiAlarm.class, id));
    }
	
	@SuppressWarnings("unchecked")
    public SbiAlarm findById(Integer id) {
        Session session = getSession();
        Transaction tx = null;
        try {
        	tx = session.beginTransaction();
			SbiAlarm item = (SbiAlarm)session.get(SbiAlarm.class, id);
			tx.commit();
			return item;
			
		} catch (HibernateException e) {
			if( tx != null && tx.isActive() ){
				tx.rollback();
			}
			throw e;
			
		}finally{
			session.close();
		}
    }

    @SuppressWarnings("unchecked")
	public List<SbiAlarm> findAll() {
        Session session = getSession();
        Transaction tx = null;
        try {
        	tx = session.beginTransaction();
			
			List<SbiAlarm> list = (List<SbiAlarm>)session.createQuery("from SbiAlarm").list();
			tx.commit();
			return list;
			
		} catch (HibernateException e) {
			if( tx != null && tx.isActive() ){
				tx.rollback();
			}
			throw e;
			
		}finally{
			session.close();
		}
    }	
	


}

