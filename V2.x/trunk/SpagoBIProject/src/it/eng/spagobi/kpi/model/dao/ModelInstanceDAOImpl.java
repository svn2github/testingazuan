package it.eng.spagobi.kpi.model.dao;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.kpi.model.bo.ModelInstance;
import it.eng.spagobi.kpi.model.metadata.SbiKpiModelInst;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;

public class ModelInstanceDAOImpl extends AbstractHibernateDAO implements IModelInstanceDAO{
	
	static private Logger logger = Logger.getLogger(ModelInstanceDAOImpl.class);

	public List loadModelsInstanceRoot() throws EMFUserError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		List toReturn = new ArrayList();
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			Criteria crit = aSession.createCriteria(SbiKpiModelInst.class);
			crit.add(Expression.isNull("sbiKpiModelInst"));
			List sbiKpiModelInstanceList = crit.list();
			for (Iterator iterator = sbiKpiModelInstanceList.iterator(); iterator.hasNext();) {
				SbiKpiModelInst sbiKpiModelInst = (SbiKpiModelInst) iterator.next();
				ModelInstance aModelInst = toModelInstanceWithoutChildren(sbiKpiModelInst);
				toReturn.add(aModelInst);
			}
		} catch (HibernateException he) {
			logException(he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 101);

		} finally {
			if (aSession != null) {
				if (aSession.isOpen())
					aSession.close();
			}
		}
		logger.debug("OUT");
		return toReturn;

	}
	
	private ModelInstance toModelInstanceWithoutChildren(SbiKpiModelInst value) {
		logger.debug("IN");
		ModelInstance toReturn = new ModelInstance();

		String name = value.getName();
		String description = value.getDescription();
		Integer id = value.getKpiModelInst();
	

		toReturn.setId(id);
		toReturn.setName(name);
		toReturn.setDescription(description);


		logger.debug("OUT");
		return toReturn;
	}

	public ModelInstance loadModelInstanceWithoutChildrenById(Integer id) throws EMFUserError {
		logger.debug("IN");
		ModelInstance toReturn = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiKpiModelInst hibSbiKpiModelInstance = (SbiKpiModelInst) aSession.load(
					SbiKpiModelInst.class, id);
			toReturn = toModelInstanceWithoutChildren(hibSbiKpiModelInstance);

		} catch (HibernateException he) {
			logger.error("Error while loading the Model Instance with id "
					+ ((id == null) ? "" : id.toString()), he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 101);

		} finally {
			if (aSession != null) {
				if (aSession.isOpen())
					aSession.close();
				logger.debug("OUT");
			}
		}
		logger.debug("OUT");
		return toReturn;
	}

	public void modifyModel(ModelInstance value) throws EMFUserError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			Integer kpiModelInstanceId = value.getId();
			String kpiModelInstanceDesc = value.getDescription();
			String kpiModelInstanceNm = value.getName();

			SbiKpiModelInst sbiKpiModelInst = (SbiKpiModelInst) aSession.load(
					SbiKpiModelInst.class, kpiModelInstanceId);
			sbiKpiModelInst.setDescription(kpiModelInstanceDesc);
			sbiKpiModelInst.setName(kpiModelInstanceNm);
			aSession.update(sbiKpiModelInst);
			tx.commit();

		} catch (HibernateException he) {
			logException(he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 101);

		} finally {
			if (aSession != null) {
				if (aSession.isOpen())
					aSession.close();
			}
		}
		logger.debug("OUT");
	}

	public Integer insertModel(ModelInstance toCreate) throws EMFUserError {
		logger.debug("IN");
		Integer idToReturn;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			// set the sbiKpiModel
			SbiKpiModelInst sbiKpiModelInst = new SbiKpiModelInst();
			sbiKpiModelInst.setName(toCreate.getName());
			sbiKpiModelInst.setDescription(toCreate.getDescription());

			idToReturn = (Integer) aSession.save(sbiKpiModelInst);

			tx.commit();

		} catch (HibernateException he) {
			logException(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 101);

		} finally {
			if (aSession != null) {
				if (aSession.isOpen())
					aSession.close();
			}
		}
		logger.debug("OUT");
		return idToReturn;
		
	}

}
