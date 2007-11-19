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
/*
 * Created on 20-giu-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.tools.datasource.dao;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.tools.datasource.bo.DataSource;
import it.eng.spagobi.tools.datasource.metadata.SbiDataSource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;

/**
 * Defines the Hibernate implementations for all DAO methods,
 * for a data source.
 * 
 * @author giachino
 */
public class DataSourceDAOHibImpl extends AbstractHibernateDAO implements IDataSourceDAO{

	/**
	 * @see it.eng.spagobi.tools.datasource.dao.IDataSourceDAO#loadDataSourceByID(java.lang.Integer)
	 */
	public DataSource loadDataSourceByID(Integer dsID) throws EMFUserError {
		DataSource toReturn = null;
		Session aSession = null;
		Transaction tx = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiDataSource hibDataSource = (SbiDataSource)aSession.load(SbiDataSource.class,  dsID);
			toReturn = toDataSource(hibDataSource);
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
		
		return toReturn;
	}

	/**
	 * @see it.eng.spagobi.tools.datasource.dao.IDataSourceDAO#loadDataSourceByLabel(string)
	 */	
	public DataSource loadDataSourceByLabel(String label) throws EMFUserError {
		DataSource biDS = null;
		Session tmpSession = null;
		Transaction tx = null;
		try {
			tmpSession = getSession();
			tx = tmpSession.beginTransaction();
			Criterion labelCriterrion = Expression.eq("label", label);
			Criteria criteria = tmpSession.createCriteria(SbiDataSource.class);
			criteria.add(labelCriterrion);	
			SbiDataSource hibDS = (SbiDataSource) criteria.uniqueResult();
			if (hibDS == null) return null;
			biDS = toDataSource(hibDS);				
			
			tx.commit();
		} catch (HibernateException he) {
			logException(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (tmpSession!=null){
				if (tmpSession.isOpen()) tmpSession.close();
			}
		}
		return biDS;		
	}

	/**
	 * @see it.eng.spagobi.tools.datasource.dao.IDataSourceDAO#loadAllDataSources()
	 */
	public List loadAllDataSources() throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		List realResult = new ArrayList();
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();

			Query hibQuery = aSession.createQuery(" from SbiDataSource");
			
			List hibList = hibQuery.list();
			Iterator it = hibList.iterator();

			while (it.hasNext()) {
				realResult.add(toDataSource((SbiDataSource) it.next()));
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
	 * @see it.eng.spagobi.tools.datasource.dao.IDataSourceDAO#modifyDataSource(it.eng.spagobi.tools.datasource.bo.DataSource)
	 */
	public void modifyDataSource(DataSource aDataSource) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();

			SbiDataSource hibDataSource = (SbiDataSource) aSession.load(SbiDataSource.class,
					new Integer(aDataSource.getDsId()));			
			hibDataSource.setLabel(aDataSource.getLabel());
			hibDataSource.setDescr(aDataSource.getDescr());
			hibDataSource.setJndi(aDataSource.getJndi());
			hibDataSource.setUrl_connection(aDataSource.getUrlConnection());
			hibDataSource.setUser(aDataSource.getUser());
			hibDataSource.setPwd(aDataSource.getPwd());
			hibDataSource.setDriver(aDataSource.getDriver());
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

	}

	/**
	 * @see it.eng.spagobi.tools.datasource.dao.IDataSourceDAO#insertDataSource(it.eng.spagobi.tools.datasource.bo.DataSource)
	 */
	public void insertDataSource(DataSource aDataSource) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			SbiDataSource hibDataSource = new SbiDataSource();
			hibDataSource.setLabel(aDataSource.getLabel());
			hibDataSource.setDescr(aDataSource.getDescr());
			hibDataSource.setJndi(aDataSource.getJndi());
			hibDataSource.setUrl_connection(aDataSource.getUrlConnection());
			hibDataSource.setUser(aDataSource.getUser());
			hibDataSource.setPwd(aDataSource.getPwd());
			hibDataSource.setDriver(aDataSource.getDriver());
			aSession.save(hibDataSource);
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
	}

	/**
	 * @see it.eng.spagobi.tools.datasource.dao.IDataSourceDAO#eraseDataSource(it.eng.spagobi.tools.datasource.bo.DataSource)
	 */
	public void eraseDataSource(DataSource aDataSource) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiDataSource hibDataSource = (SbiDataSource) aSession.load(SbiDataSource.class,
					new Integer(aDataSource.getDsId()));

			aSession.delete(hibDataSource);
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

	}
	
	/**
	 * From the hibernate DataSource at input, gives
	 * the corrispondent <code>DataSource</code> object.
	 * 
	 * @param hibEngine The hybernate data source
	 * @return The corrispondent <code>DataSource</code> object
	 */
	public DataSource toDataSource(SbiDataSource hibDataSource){
		DataSource ds = new DataSource();
		 
		ds.setDsId(hibDataSource.getDsId());
		ds.setLabel(hibDataSource.getLabel());
		ds.setDescr(hibDataSource.getDescr());
		ds.setJndi(hibDataSource.getJndi());
		ds.setUrlConnection(hibDataSource.getUrl_connection());
		ds.setUser(hibDataSource.getUser());
		ds.setPwd(hibDataSource.getPwd());
		ds.setDriver(hibDataSource.getDriver());
		ds.setEngines(hibDataSource.getSbiEngineses());
		ds.setObjects(hibDataSource.getSbiObjectses());
		
		return ds;
	}
	/**
	 * @see it.eng.spagobi.tools.datasource.dao.IDataSourceDAO#hasBIObjAssociated(java.lang.String)
	 */
	public boolean hasBIObjAssociated (String dsId) throws EMFUserError{
		/**
		 * TODO Hibernate Implementation
		 */
		boolean bool = false; 
		
		
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			Integer dsIdInt = Integer.valueOf(dsId);
			
			String hql = " from SbiObjects s where s.dataSource.dsId = "+ dsIdInt;
			Query aQuery = aSession.createQuery(hql);
			
			List biObjectsAssocitedWithDs = aQuery.list();
			if (biObjectsAssocitedWithDs.size() > 0)
				bool = true;
			else
				bool = false;
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
		return bool;
		
	}
	
	/**
	 * @see it.eng.spagobi.tools.datasource.dao.IDataSourceDAO#hasEngineAssociated(java.lang.String)
	 */
	public boolean hasBIEngineAssociated (String dsId) throws EMFUserError{
		/**
		 * TODO Hibernate Implementation
		 */
		boolean bool = false; 
		
		
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			Integer dsIdInt = Integer.valueOf(dsId);
			
			String hql = " from SbiEngines s where s.dataSource.dsId = "+ dsIdInt;
			Query aQuery = aSession.createQuery(hql);
			
			List biObjectsAssocitedWithEngine = aQuery.list();
			if (biObjectsAssocitedWithEngine.size() > 0)
				bool = true;
			else
				bool = false;
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
		return bool;
		
	}
	
	/** 
	 * @see it.eng.spagobi.tools.datasource.dao.IDataSourceDAO#loadDataSourceByDocumentId(java.lang.Integer)
	 */
	public DataSource loadDataSourceByDocumentId(Integer docId) throws EMFUserError {
		DataSource toReturn = null;
		Session aSession = null;
		Transaction tx = null;
		String hql = null;
		Query hqlQuery = null;
		
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();

			hql = " from SbiObjects as s where s.dataSource = " + docId.toString();
			
			hqlQuery = aSession.createQuery(hql);
			SbiDataSource hibDataSource = (SbiDataSource)hqlQuery.uniqueResult();
			if (hibDataSource == null) return null;
			toReturn = toDataSource(hibDataSource);
			
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
		return toReturn;
	}
	
	/** 
	 * @see it.eng.spagobi.tools.datasource.dao.IDataSourceDAO#loadDataSourceByEngineId(java.lang.Integer)
	 */
	public DataSource loadDataSourceByEngineId(Integer engineId) throws EMFUserError {
		DataSource toReturn = null;
		Session aSession = null;
		Transaction tx = null;
		String hql = null;
		Query hqlQuery = null;
		
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();

			hql = " from SbiEngines as s where s.dataSource = " + engineId.toString();
			
			hqlQuery = aSession.createQuery(hql);
			SbiDataSource hibDataSource = (SbiDataSource)hqlQuery.uniqueResult();
			if (hibDataSource == null) return null;
			toReturn = toDataSource(hibDataSource);
			
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
		return toReturn;
	}
	
	/** 
	 * @see it.eng.spagobi.tools.datasource.dao.IDataSourceDAO#loadDataSourceByEngineLabel(java.lang.String)
	 */
	public DataSource loadDataSourceByEngineLabel(String engineLabel) throws EMFUserError {
		DataSource toReturn = null;
		Session aSession = null;
		Transaction tx = null;
		String hql = null;
		Query hqlQuery = null;
		
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();

			hql = " from SbiEngines as s where s.label = " + engineLabel;
			
			hqlQuery = aSession.createQuery(hql);
			SbiDataSource hibDataSource = (SbiDataSource)hqlQuery.uniqueResult();
			if (hibDataSource == null) return null;
			toReturn = toDataSource(hibDataSource);
			
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
		return toReturn;
	}
}


