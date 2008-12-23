/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.tools.dataset.dao;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.tools.dataset.bo.DataSetConfig;
import it.eng.spagobi.tools.dataset.bo.FileDataSet;
import it.eng.spagobi.tools.dataset.bo.JClassDataSet;
import it.eng.spagobi.tools.dataset.bo.QueryDataSet;
import it.eng.spagobi.tools.dataset.bo.ScriptDataSet;
import it.eng.spagobi.tools.dataset.bo.WSDataSet;
import it.eng.spagobi.tools.dataset.metadata.SbiDataSetConfig;
import it.eng.spagobi.tools.dataset.metadata.SbiFileDataSet;
import it.eng.spagobi.tools.dataset.metadata.SbiJClassDataSet;
import it.eng.spagobi.tools.dataset.metadata.SbiQueryDataSet;
import it.eng.spagobi.tools.dataset.metadata.SbiScriptDataSet;
import it.eng.spagobi.tools.dataset.metadata.SbiWSDataSet;
import it.eng.spagobi.tools.datasource.bo.DataSource;
import it.eng.spagobi.tools.datasource.dao.DataSourceDAOHibImpl;
import it.eng.spagobi.tools.datasource.metadata.SbiDataSource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;

public class DataSetDAOHibImpl extends AbstractHibernateDAO implements IDataSetDAO{
	static private Logger logger = Logger.getLogger(DataSetDAOHibImpl.class);
	
	/**
	 * Load data set by id.
	 * 
	 * @param dsID the ds id
	 * 
	 * @return the data set
	 * 
	 * @throws EMFUserError the EMF user error
	 * 
	 * @see it.eng.spagobi.tools.dataset.dao.IDataSetDAO#loadDataSetByID(java.lang.Integer)
	 */
	public DataSetConfig loadDataSetByID(Integer dsID) throws EMFUserError {
		logger.debug("IN");
		DataSetConfig toReturn = null;
		Session aSession = null;
		Transaction tx = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiDataSetConfig hibDataSet = (SbiDataSetConfig)aSession.load(SbiDataSetConfig.class,  dsID);
			toReturn = toDataSet(hibDataSet);
			tx.commit();

		} catch (HibernateException he) {
			logger.error("Error while loading the data Set with id " + dsID.toString(), he);			

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
				logger.debug("OUT");
			}
		}
		logger.debug("OUT");
		return toReturn;
	}

	/**
	 * Load data set by label.
	 * 
	 * @param label the label
	 * 
	 * @return the data set
	 * 
	 * @throws EMFUserError the EMF user error
	 * 
	 * @see it.eng.spagobi.tools.dataset.dao.IDataSetDAO#loadDataSetByLabel(string)
	 */	
	public DataSetConfig loadDataSetByLabel(String label) throws EMFUserError {
		logger.debug("IN");
		DataSetConfig biDS = null;
		Session tmpSession = null;
		Transaction tx = null;
		try {
			tmpSession = getSession();
			tx = tmpSession.beginTransaction();
			Criterion labelCriterrion = Expression.eq("label", label);
			Criteria criteria = tmpSession.createCriteria(SbiDataSetConfig.class);
			criteria.add(labelCriterrion);	
			SbiDataSetConfig hibDS = (SbiDataSetConfig) criteria.uniqueResult();
			if (hibDS == null) return null;
			biDS = toDataSet(hibDS);				

			tx.commit();
		} catch (HibernateException he) {
			logger.error("Error while loading the data set with label " + label, he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (tmpSession!=null){
				if (tmpSession.isOpen()) tmpSession.close();
			}
		}
		logger.debug("OUT");
		return biDS;		
	}

	/**
	 * Load all data sets.
	 * 
	 * @return the list
	 * 
	 * @throws EMFUserError the EMF user error
	 * 
	 * @see it.eng.spagobi.tools.dataset.dao.IDataSetDAO#loadAllDataSets()
	 */
	public List loadAllDataSets() throws EMFUserError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		List realResult = new ArrayList();
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();

			Query hibQuery = aSession.createQuery(" from SbiDataSetConfig");

			List hibList = hibQuery.list();
			Iterator it = hibList.iterator();

			while (it.hasNext()) {
				realResult.add(toDataSet((SbiDataSetConfig) it.next()));
			}
			tx.commit();
		} catch (HibernateException he) {
			logger.error("Error while loading all data sets ", he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();				
			}
		}
		logger.debug("OUT");
		return realResult;
	}

	/**
	 * Modify data set.
	 * 
	 * @param aDataSet the a data set
	 * 
	 * @throws EMFUserError the EMF user error
	 * 
	 * @see it.eng.spagobi.tools.dataset.dao.IDataSetDAO#modifyDataSet(it.eng.spagobi.tools.dataset.bo.DataSetConfig)
	 */
	public void modifyDataSet(DataSetConfig aDataSet) throws EMFUserError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();


			SbiDataSetConfig hibDataSet = (SbiDataSetConfig) aSession.load(SbiDataSetConfig.class,
					new Integer(aDataSet.getDsId()));			
			
			if(aDataSet instanceof FileDataSet){
				//hibDataSet=new SbiFileDataSet();
				if(((FileDataSet)aDataSet).getFileName()!=null){
					((SbiFileDataSet)hibDataSet).setFileName(((FileDataSet)aDataSet).getFileName());
				}
			}

			else if(aDataSet instanceof QueryDataSet){
				//hibDataSet=new SbiQueryDataSet();
				if(((QueryDataSet)aDataSet).getQuery()!=null){
					((SbiQueryDataSet)hibDataSet).setQuery(((QueryDataSet)aDataSet).getQuery());
				}
				if(((QueryDataSet)aDataSet).getDataSource()!=null){
					SbiDataSource hibDataSource = null;
					hibDataSource = (SbiDataSource) aSession.load(SbiDataSource.class, new Integer(((QueryDataSet)aDataSet).getDataSource().getDsId()));
					((SbiQueryDataSet)hibDataSet).setDataSource(hibDataSource);	
				}				
			}

			else if(aDataSet instanceof WSDataSet){
				//hibDataSet=new SbiWSDataSet();
				if(((WSDataSet)aDataSet).getAdress()!=null){
					((SbiWSDataSet)hibDataSet).setAdress(((WSDataSet)aDataSet).getAdress());
				}
				if(((WSDataSet)aDataSet).getExecutorClass()!=null){
					((SbiWSDataSet)hibDataSet).setExecutorClass(((WSDataSet)aDataSet).getExecutorClass());
				}	
			}
			
			else if(aDataSet instanceof ScriptDataSet){

				if(((ScriptDataSet)aDataSet).getScript()!=null){
					((SbiScriptDataSet)hibDataSet).setScript(((ScriptDataSet)aDataSet).getScript());
				}
			}
			
			else if(aDataSet instanceof JClassDataSet){

				if(((JClassDataSet)aDataSet).getJavaClassName()!=null){
					((SbiJClassDataSet)hibDataSet).setJavaClassName(((JClassDataSet)aDataSet).getJavaClassName());
				}
			}
			
			hibDataSet.setLabel(aDataSet.getLabel());
			hibDataSet.setTransformerId(aDataSet.getTransformerId());
			hibDataSet.setPivotColumnName(aDataSet.getPivotColumnName());
			hibDataSet.setPivotRowName(aDataSet.getPivotRowName());
			hibDataSet.setPivotColumnValue(aDataSet.getPivotColumnValue());
			hibDataSet.setName(aDataSet.getName());			
			hibDataSet.setDescription(aDataSet.getDescription());
			hibDataSet.setParameters(aDataSet.getParameters());
			
			
			
			tx.commit();
		} catch (HibernateException he) {
			logger.error("Error while modifing the data Set with id " + ((aDataSet == null)?"":String.valueOf(aDataSet.getDsId())), he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
				logger.debug("OUT");
			}
		}

	}

	/**
	 * Insert data set.
	 * 
	 * @param aDataSet the a data set
	 * 
	 * @throws EMFUserError the EMF user error
	 * 
	 * @see it.eng.spagobi.tools.dataset.dao.IDataSetDAO#insertDataSet(it.eng.spagobi.tools.dataset.bo.DataSetConfig)
	 */
	public void insertDataSet(DataSetConfig aDataSet) throws EMFUserError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiDataSetConfig hibDataSet =null;


			if(aDataSet instanceof FileDataSet){
				hibDataSet=new SbiFileDataSet();
				if(((FileDataSet)aDataSet).getFileName()!=null){
					((SbiFileDataSet)hibDataSet).setFileName(((FileDataSet)aDataSet).getFileName());
				}
			}

			else if(aDataSet instanceof QueryDataSet){
				hibDataSet=new SbiQueryDataSet();
				if(((QueryDataSet)aDataSet).getQuery()!=null){
					((SbiQueryDataSet)hibDataSet).setQuery(((QueryDataSet)aDataSet).getQuery());
				}
				if(((QueryDataSet)aDataSet).getDataSource()!=null){
					SbiDataSource hibDataSource = null;
					hibDataSource = (SbiDataSource) aSession.load(SbiDataSource.class, new Integer(((QueryDataSet)aDataSet).getDataSource().getDsId()));
					((SbiQueryDataSet)hibDataSet).setDataSource(hibDataSource);	
				}				
			}

			else if(aDataSet instanceof WSDataSet){
				hibDataSet=new SbiWSDataSet();
				if(((WSDataSet)aDataSet).getAdress()!=null){
					((SbiWSDataSet)hibDataSet).setAdress(((WSDataSet)aDataSet).getAdress());
				}
				if(((WSDataSet)aDataSet).getExecutorClass()!=null){
					((SbiWSDataSet)hibDataSet).setExecutorClass(((WSDataSet)aDataSet).getExecutorClass());
				}	
			}
			
			else if(aDataSet instanceof JClassDataSet){
				hibDataSet=new SbiJClassDataSet();
				if(((JClassDataSet)aDataSet).getJavaClassName()!=null){
					((SbiJClassDataSet)hibDataSet).setJavaClassName(((JClassDataSet)aDataSet).getJavaClassName());
				}
			}
			
			else if(aDataSet instanceof ScriptDataSet){
				hibDataSet=new SbiScriptDataSet();
				if(((ScriptDataSet)aDataSet).getScript()!=null){
					((SbiScriptDataSet)hibDataSet).setScript(((ScriptDataSet)aDataSet).getScript());
				}
			}


			hibDataSet.setLabel(aDataSet.getLabel());
			hibDataSet.setTransformerId(aDataSet.getTransformerId());
			hibDataSet.setPivotColumnName(aDataSet.getPivotColumnName());
			hibDataSet.setPivotRowName(aDataSet.getPivotRowName());
			hibDataSet.setPivotColumnValue(aDataSet.getPivotColumnValue());
			hibDataSet.setDescription(aDataSet.getDescription());
			hibDataSet.setName(aDataSet.getName());
			hibDataSet.setParameters(aDataSet.getParameters());

			aSession.save(hibDataSet);
			tx.commit();
		} catch (HibernateException he) {
			logger.error("Error while inserting the data Set with id " + ((aDataSet == null)?"":String.valueOf(aDataSet.getDsId())), he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
				logger.debug("OUT");
			}
		}
	}

	/**
	 * Erase data set.
	 * 
	 * @param aDataSet the a data set
	 * 
	 * @throws EMFUserError the EMF user error
	 * 
	 * @see it.eng.spagobi.tools.dataset.dao.IDataSetDAO#eraseDataSet(it.eng.spagobi.tools.dataset.bo.DataSetConfig)
	 */
	public void eraseDataSet(DataSetConfig aDataSet) throws EMFUserError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiDataSetConfig hibDataSet = (SbiDataSetConfig) aSession.load(SbiDataSetConfig.class,
					new Integer(aDataSet.getDsId()));

			aSession.delete(hibDataSet);
			tx.commit();
		} catch (HibernateException he) {
			logger.error("Error while erasing the data Set with id " + ((aDataSet == null)?"":String.valueOf(aDataSet.getDsId())), he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
				logger.debug("OUT");
			}
		}

	}

	/**
	 * From the hibernate DataSet at input, gives
	 * the corrispondent <code>DataSet</code> object.
	 * 
	 * @param hibDataSet The hybernate data set
	 * 
	 * @return The corrispondent <code>DataSet</code> object
	 */
	public DataSetConfig toDataSet(SbiDataSetConfig hibDataSet){
		DataSetConfig ds = null;
		if(hibDataSet instanceof SbiFileDataSet){
			ds = new FileDataSet();
			((FileDataSet)ds).setFileName(((SbiFileDataSet)hibDataSet).getFileName());
		}
		
		if(hibDataSet instanceof SbiQueryDataSet){
			ds=new QueryDataSet();
			((QueryDataSet)ds).setQuery(((SbiQueryDataSet)hibDataSet).getQuery());
			
			SbiDataSource sbids=((SbiQueryDataSet)hibDataSet).getDataSource();
			if(sbids!=null){
			DataSourceDAOHibImpl dataSourceDao=new DataSourceDAOHibImpl();
			DataSource dataSource=dataSourceDao.toDataSource(sbids);
			((QueryDataSet)ds).setDataSource(dataSource);
			}
		}

		if(hibDataSet instanceof SbiWSDataSet){
			ds=new WSDataSet();
			((WSDataSet)ds).setAdress(((SbiWSDataSet)hibDataSet).getAdress());
			((WSDataSet)ds).setExecutorClass(((SbiWSDataSet)hibDataSet).getExecutorClass());
		}
		
		if(hibDataSet instanceof SbiScriptDataSet){
			ds=new ScriptDataSet();
			((ScriptDataSet)ds).setScript(((SbiScriptDataSet)hibDataSet).getScript());
		}
		
		if(hibDataSet instanceof SbiJClassDataSet){
			ds=new JClassDataSet();
			((JClassDataSet)ds).setJavaClassName(((SbiJClassDataSet)hibDataSet).getJavaClassName());
		}

		ds.setDsId(hibDataSet.getDsId());
		ds.setName(hibDataSet.getName());
		ds.setLabel(hibDataSet.getLabel());
		ds.setTransformerId(hibDataSet.getTransformerId());
		ds.setPivotColumnName(hibDataSet.getPivotColumnName());
		ds.setPivotRowName(hibDataSet.getPivotRowName());
		ds.setPivotColumnValue(hibDataSet.getPivotColumnValue());
		ds.setDescription(hibDataSet.getDescription());		
		ds.setParameters(hibDataSet.getParameters());		
		
		return ds;
	}
	
	/**
	 * Checks for bi obj associated.
	 * 
	 * @param dsId the ds id
	 * 
	 * @return true, if checks for bi obj associated
	 * 
	 * @throws EMFUserError the EMF user error
	 * 
	 * @see it.eng.spagobi.tools.dataSet.dao.IDataSetDAO#hasBIObjAssociated(java.lang.String)
	 */
	public boolean hasBIObjAssociated (String dsId) throws EMFUserError{
		logger.debug("IN");		
		boolean bool = false; 


		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			Integer dsIdInt = Integer.valueOf(dsId);

			//String hql = " from SbiObjects s where s.dataSet.dsId = "+ dsIdInt;
			String hql = " from SbiObjects s where s.dataSet.dsId = ?";
			Query aQuery = aSession.createQuery(hql);
			aQuery.setInteger(0, dsIdInt.intValue());
			List biObjectsAssocitedWithDs = aQuery.list();
			if (biObjectsAssocitedWithDs.size() > 0)
				bool = true;
			else
				bool = false;
			tx.commit();
		} catch (HibernateException he) {
			logger.error("Error while getting the objects associated with the data set with id " + dsId, he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		logger.debug("OUT");
		return bool;

	}

	/**
	 * @see it.eng.spagobi.tools.dataset.dao.IDataSetDAO#hasEngineAssociated(java.lang.String)
	 */
	/*	public boolean hasBIEngineAssociated (String dsId) throws EMFUserError{
		logger.debug("IN");
		boolean bool = false; 


		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			Integer dsIdInt = Integer.valueOf(dsId);

			String hql = " from SbiEngines s where s.dataSet.dsId = "+ dsIdInt;
			Query aQuery = aSession.createQuery(hql);

			List biObjectsAssocitedWithEngine = aQuery.list();
			if (biObjectsAssocitedWithEngine.size() > 0)
				bool = true;
			else
				bool = false;
			tx.commit();
		} catch (HibernateException he) {
			logger.error("Error while getting the engines associated with the data set with id " + dsId, he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		logger.debug("OUT");
		return bool;

	}*/

}



