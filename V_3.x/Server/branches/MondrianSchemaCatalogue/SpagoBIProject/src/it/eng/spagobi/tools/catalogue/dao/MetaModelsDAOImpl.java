/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.tools.catalogue.dao;

import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.commons.dao.SpagoBIDOAException;
import it.eng.spagobi.tools.catalogue.metadata.SbiMetaModel;
import it.eng.spagobi.utilities.assertion.Assert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.LogMF;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class MetaModelsDAOImpl extends AbstractHibernateDAO implements IMetaModelsDAO {

	static private Logger logger = Logger.getLogger(MetaModelsDAOImpl.class);

	public MetaModel loadMetaModelById(Integer id) {
		LogMF.debug(logger, "IN: id = [{0}]", id);
		
		MetaModel toReturn = null;
		Session session = null;
		Transaction transaction = null;
		
		try {
			if(id == null) {
				throw new IllegalArgumentException("Input parameter [id] cannot be null");
			}
			
			try {
				session = getSession();
				Assert.assertNotNull(session, "session cannot be null");
				transaction = session.beginTransaction();
				Assert.assertNotNull(transaction, "transaction cannot be null");
			} catch(Throwable t) {
				throw new SpagoBIDOAException("An error occured while creating the new transaction", t);
			}
			
			SbiMetaModel hibModel = (SbiMetaModel) session.load(SbiMetaModel.class, id);
			logger.debug("Model loaded");
			
			toReturn = toModel(hibModel);
			
			transaction.rollback();
		} catch (Throwable t) {
			logException(t);
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			throw new SpagoBIDOAException("An unexpected error occured while loading model with id [" + id + "]", t);	
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		
		LogMF.debug(logger, "OUT: returning [{0}]", toReturn);
		return toReturn;
	}

	public MetaModel loadMetaModelByName(String name) {
		LogMF.debug(logger, "IN: name = [{0}]", name);
		
		MetaModel toReturn = null;
		Session session = null;
		Transaction transaction = null;
		
		try {
			if(name == null) {
				throw new IllegalArgumentException("Input parameter [name] cannot be null");
			}
			
			try {
				session = getSession();
				Assert.assertNotNull(session, "session cannot be null");
				transaction = session.beginTransaction();
				Assert.assertNotNull(transaction, "transaction cannot be null");
			} catch(Throwable t) {
				throw new SpagoBIDOAException("An error occured while creating the new transaction", t);
			}
			
			Query query = session.createQuery(" from SbiMetaModel m where m.name = ?");
			query.setString(0, name);
			SbiMetaModel hibModel = (SbiMetaModel) query.uniqueResult();
			logger.debug("Model loaded");
			
			toReturn = toModel(hibModel);
			
			transaction.rollback();
		} catch (Throwable t) {
			logException(t);
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			throw new SpagoBIDOAException("An unexpected error occured while loading model with name [" + name + "]", t);	
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		
		LogMF.debug(logger, "OUT: returning [{0}]", toReturn);
		return toReturn;
	}

	public List<MetaModel> loadAllMetaModels() {
		logger.debug("IN");
		
		List<MetaModel> toReturn = new ArrayList<MetaModel>();
		Session session = null;
		Transaction transaction = null;
		
		try {
			
			try {
				session = getSession();
				Assert.assertNotNull(session, "session cannot be null");
				transaction = session.beginTransaction();
				Assert.assertNotNull(transaction, "transaction cannot be null");
			} catch(Throwable t) {
				throw new SpagoBIDOAException("An error occured while creating the new transaction", t);
			}
			
			Query query = session.createQuery(" from SbiMetaModel");
			List list = query.list();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				toReturn.add(toModel((SbiMetaModel) it.next()));
			}
			logger.debug("Models loaded");
			
			transaction.rollback();
		} catch (Throwable t) {
			logException(t);
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			throw new SpagoBIDOAException("An unexpected error occured while loading models' list", t);	
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		
		LogMF.debug(logger, "OUT: returning [{0}]", toReturn);
		return toReturn;
	}

	public void modifyMetaModel(MetaModel model) {
		LogMF.debug(logger, "IN: model = [{0}]", model);
		
		Session session = null;
		Transaction transaction = null;
		
		try {
			if (model == null) {
				throw new IllegalArgumentException("Input parameter [model] cannot be null");
			}
			if (model.getId() == null) {
				throw new IllegalArgumentException("Input model's id cannot be null");
			}
			
			try {
				session = getSession();
				Assert.assertNotNull(session, "session cannot be null");
				transaction = session.beginTransaction();
				Assert.assertNotNull(transaction, "transaction cannot be null");
			} catch(Throwable t) {
				throw new SpagoBIDOAException("An error occured while creating the new transaction", t);
			}
			
			SbiMetaModel hibModel = (SbiMetaModel) session.load(SbiMetaModel.class, model.getId());
			logger.debug("Model loaded");
			hibModel.setName(model.getName());
			hibModel.setDescription(model.getDescription());
			
			updateSbiCommonInfo4Update(hibModel);
			session.save(hibModel);
			
			transaction.commit();
		} catch (Throwable t) {
			logException(t);
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			throw new SpagoBIDOAException("An unexpected error occured while saving model [" + model + "]", t);	
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		
		logger.debug("OUT");
		
	}

	public void insertMetaModel(MetaModel model) {
		LogMF.debug(logger, "IN: model = [{0}]", model);
		
		Session session = null;
		Transaction transaction = null;
		
		try {
			if (model == null) {
				throw new IllegalArgumentException("Input parameter [model] cannot be null");
			}
			
			try {
				session = getSession();
				Assert.assertNotNull(session, "session cannot be null");
				transaction = session.beginTransaction();
				Assert.assertNotNull(transaction, "transaction cannot be null");
			} catch(Throwable t) {
				throw new SpagoBIDOAException("An error occured while creating the new transaction", t);
			}
			
			SbiMetaModel hibModel =  new SbiMetaModel();
			hibModel.setName(model.getName());
			hibModel.setDescription(model.getDescription());
			
			updateSbiCommonInfo4Insert(hibModel);
			session.save(hibModel);
			
			transaction.commit();
			
			model.setId(hibModel.getId());
			
		} catch (Throwable t) {
			logException(t);
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			throw new SpagoBIDOAException("An unexpected error occured while saving model [" + model + "]", t);	
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		
		logger.debug("OUT");
		
	}

	public void eraseMetaModel(MetaModel model) {
		LogMF.debug(logger, "IN: model = [{0}]", model);

		Session session = null;
		Transaction transaction = null;
		
		try {
			if (model == null) {
				throw new IllegalArgumentException("Input parameter [model] cannot be null");
			}
			if (model.getId() == null) {
				throw new IllegalArgumentException("Input model's id cannot be null");
			}
			
			try {
				session = getSession();
				Assert.assertNotNull(session, "session cannot be null");
				transaction = session.beginTransaction();
				Assert.assertNotNull(transaction, "transaction cannot be null");
			} catch(Throwable t) {
				throw new SpagoBIDOAException("An error occured while creating the new transaction", t);
			}
			
			SbiMetaModel hibModel = (SbiMetaModel) session.load(SbiMetaModel.class, model.getId());
			if (hibModel == null) {
				logger.warn("Model [" + model + "] not found");
			} else {
				session.delete(hibModel);
			}
			
			transaction.commit();
		} catch (Throwable t) {
			logException(t);
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			throw new SpagoBIDOAException("An unexpected error occured while deleting model [" + model + "]", t);	
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		
		logger.debug("OUT");
		
	}
	
	public MetaModel toModel(SbiMetaModel hibModel) {
		logger.debug("IN");
		MetaModel toReturn = null;
		if (hibModel != null) {
			toReturn = new MetaModel();
			toReturn.setId(hibModel.getId());
			toReturn.setName(hibModel.getName());
			toReturn.setDescription(hibModel.getDescription());
		}
		logger.debug("OUT");
		return toReturn;
	}

}