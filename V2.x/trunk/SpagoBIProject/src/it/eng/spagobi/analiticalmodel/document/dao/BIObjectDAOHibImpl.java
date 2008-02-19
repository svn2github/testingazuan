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
 * Created on 21-giu-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.analiticalmodel.document.dao;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.bo.ObjTemplate;
import it.eng.spagobi.analiticalmodel.document.bo.Viewpoint;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjFunc;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjFuncId;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjPar;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjTemplates;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjects;
import it.eng.spagobi.analiticalmodel.functionalitytree.metadata.SbiFunctions;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.ObjParuse;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.Parameter;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.dao.BIObjectParameterDAOHibImpl;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.dao.IBIObjectParameterDAO;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.dao.IObjParuseDAO;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.dao.IParameterDAO;
import it.eng.spagobi.commons.bo.Role;
import it.eng.spagobi.commons.constants.AdmintoolsConstants;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.metadata.SbiBinContents;
import it.eng.spagobi.commons.metadata.SbiDomains;
import it.eng.spagobi.engines.config.dao.EngineDAOHibImpl;
import it.eng.spagobi.engines.config.metadata.SbiEngines;
import it.eng.spagobi.tools.datasource.metadata.SbiDataSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;

/**
 *	Defines the Hibernate implementations for all DAO methods,
 *  for a BI Object.  
 */
public class BIObjectDAOHibImpl extends AbstractHibernateDAO implements
		IBIObjectDAO {
    
    static private Logger logger = Logger.getLogger(BIObjectDAOHibImpl.class);

	/** 
	 * @see it.eng.spagobi.analiticalmodel.document.dao.IBIObjectDAO#loadBIObjectForExecutionByIdAndRole(java.lang.Integer, java.lang.String)
	 */
	public BIObject loadBIObjectForExecutionByIdAndRole(Integer id, String role) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		BIObject biObject = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			biObject = loadBIObjectForDetail(id);
			String hql = "from SbiObjPar s where s.sbiObject.biobjId = " + biObject.getId() + " order by s.priority asc";
			Query hqlQuery = aSession.createQuery(hql);
			List hibObjectPars = hqlQuery.list();
			SbiObjPar hibObjPar = null;
			Iterator it = hibObjectPars.iterator();
			BIObjectParameter tmpBIObjectParameter = null;
			BIObjectParameterDAOHibImpl aBIObjectParameterDAOHibImpl = new BIObjectParameterDAOHibImpl();
			IParameterDAO aParameterDAO = DAOFactory.getParameterDAO();
			List biObjectParameters = new ArrayList();
			Parameter aParameter = null;
			int count = 1;
			while (it.hasNext()) {
				hibObjPar = (SbiObjPar) it.next();
				tmpBIObjectParameter = aBIObjectParameterDAOHibImpl.toBIObjectParameter(hibObjPar);
				
				//*****************************************************************
				//**************** START PRIORITY RECALCULATION *******************
				//*****************************************************************
				Integer priority = tmpBIObjectParameter.getPriority();
				if (priority == null || priority.intValue() != count) {
				        logger.warn("The priorities of the biparameters for the document with id = " + biObject.getId() + " are not sorted. Priority recalculation starts.");
					aBIObjectParameterDAOHibImpl.recalculateBiParametersPriority(biObject.getId(), aSession);
					tmpBIObjectParameter.setPriority(new Integer(count));
				}
				count++;
				//*****************************************************************
				//**************** END PRIORITY RECALCULATION *******************
				//*****************************************************************
				
				aParameter = aParameterDAO.loadForExecutionByParameterIDandRoleName(
								tmpBIObjectParameter.getParID(), role);
				tmpBIObjectParameter.setParID(aParameter.getId());
				tmpBIObjectParameter.setParameter(aParameter);
				biObjectParameters.add(tmpBIObjectParameter);
			}
			biObject.setBiObjectParameters(biObjectParameters);
			tx.commit();
		} catch (HibernateException he) {
			logger.error(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		return biObject;
	}

	
	
	
	
	/**
	 * @see it.eng.spagobi.analiticalmodel.document.dao.IBIObjectDAO#loadBIObjectById(java.lang.Integer)
	 */
	public BIObject loadBIObjectById(Integer biObjectID) throws EMFUserError {
		BIObject toReturn = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiObjects hibBIObject = (SbiObjects)aSession.load(SbiObjects.class,  biObjectID);
			toReturn = toBIObject(hibBIObject);
			tx.commit();
		} catch (HibernateException he) {
			logger.error(he);
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
	 * @see it.eng.spagobi.analiticalmodel.document.dao.IBIObjectDAO#loadBIObjectForDetail(java.lang.Integer)
	 */
	public BIObject loadBIObjectForDetail(Integer id) throws EMFUserError {

		BIObject biObject = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			String hql = " from SbiObjects where biobjId = " + id;
			Query hqlQuery = aSession.createQuery(hql);
			SbiObjects hibObject = (SbiObjects)hqlQuery.uniqueResult();
			if (hibObject == null) return null;
			biObject = toBIObject(hibObject);
			tx.commit();
		} catch (HibernateException he) {
			logger.error(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		return biObject;
	}
	
	/** 
	 * @see it.eng.spagobi.analiticalmodel.document.dao.IBIObjectDAO#loadBIObjectByLabel(java.lang.String)
	 */
	public BIObject loadBIObjectByLabel(String label) throws EMFUserError {

		BIObject biObject = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			Criterion labelCriterrion = Expression.eq("label",
					label);
			Criteria criteria = aSession.createCriteria(SbiObjects.class);
			criteria.add(labelCriterrion);
			SbiObjects hibObject = (SbiObjects) criteria.uniqueResult();
			if (hibObject == null) return null;
			biObject = toBIObject(hibObject);
			tx.commit();
		} catch (HibernateException he) {
			logger.error(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		return biObject;
	}
	
	
	
	
	
	
	
	
	
	
	/**
	 * @see it.eng.spagobi.analiticalmodel.document.dao.IBIObjectDAO#loadBIObjectForTree(java.lang.Integer)
	 */
	public BIObject loadBIObjectForTree(Integer id) throws EMFUserError {
	        logger.debug("IN. start method with input id:" + id);
		BIObject biObject = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			logger.debug("hibernate session obtained:" + aSession);
			tx = aSession.beginTransaction();
			logger.debug("hibernate transaction started");
			Criterion domainCdCriterrion = Expression.eq("biobjId", id);
			Criteria criteria = aSession.createCriteria(SbiObjects.class);
			criteria.add(domainCdCriterrion);
			logger.debug( "hibernate criteria filled:" + criteria);
			SbiObjects hibObject = (SbiObjects) criteria.uniqueResult();
			logger.debug( "hibernate object retrived:" + hibObject);
			if (hibObject == null) {
				return null;
			}
			biObject = toBIObject(hibObject);
			tx.commit();
		} catch (HibernateException he) {
			logger.error(he);
			if (tx != null)
				tx.rollback();
			logger.error("hibernate exception",he);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
			logger.debug("OUT.end method with input id:" + id);
		}
		return biObject;	
	}

	
	
	
	
	
	/**
	 * @see it.eng.spagobi.analiticalmodel.document.dao.IBIObjectDAO#modifyBIObject(it.eng.spagobi.analiticalmodel.document.bo.BIObject)
	 */
	public void modifyBIObject(BIObject obj) throws EMFUserError {
		internalModify(obj, null);
	}

	/**
	 * @see it.eng.spagobi.analiticalmodel.document.dao.IBIObjectDAO#modifyBIObjectWithoutVersioning(it.eng.spagobi.analiticalmodel.document.bo.BIObject)
	 */
	public void modifyBIObject(BIObject obj, ObjTemplate objTemp) throws EMFUserError {
		internalModify(obj, objTemp);

	}
	
	/**
	 * Updates the biobject data into database.
	 * @param biObject The BI Object as input
	 * @param objTemp The BIObject template 
	 * @throws EMFUserError If any exception occurred
	 */
	private void internalModify(BIObject biObject, ObjTemplate objTemp) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiObjects hibBIObject = (SbiObjects) aSession.load(SbiObjects.class, biObject.getId());
			SbiEngines hibEngine = (SbiEngines) aSession.load(SbiEngines.class,	biObject.getEngine().getId());
			hibBIObject.setSbiEngines(hibEngine); 
			SbiDataSource hibDataSource = (SbiDataSource) aSession.load(SbiDataSource.class, biObject.getDataSourceId());			
			hibBIObject.setDataSource(hibDataSource); // TO REVIEW			
			hibBIObject.setDescr(biObject.getDescription());
			hibBIObject.setLabel(biObject.getLabel());
			hibBIObject.setName(biObject.getName());
			hibBIObject.setEncrypt(new Short(biObject.getEncrypt().shortValue()));
			hibBIObject.setVisible(new Short(biObject.getVisible().shortValue()));
			hibBIObject.setRelName(biObject.getRelName());
			SbiDomains hibState = (SbiDomains) aSession.load(SbiDomains.class, biObject.getStateID());
			hibBIObject.setState(hibState);
			hibBIObject.setStateCode(biObject.getStateCode());
			SbiDomains hibObjectType = (SbiDomains) aSession.load(SbiDomains.class, biObject.getBiObjectTypeID());
			hibBIObject.setObjectType(hibObjectType);
			hibBIObject.setObjectTypeCode(biObject.getBiObjectTypeCode());
			// functionalities erasing
			Set hibFunctionalities = hibBIObject.getSbiObjFuncs();
			for (Iterator it = hibFunctionalities.iterator(); it.hasNext(); ) {
				aSession.delete((SbiObjFunc) it.next());
			}
			// functionalities storing
			Set hibObjFunc = new HashSet();
			List functionalities = biObject.getFunctionalities();
			for (Iterator it = functionalities.iterator(); it.hasNext(); ) {
				Integer functId = (Integer) it.next();
				SbiFunctions aSbiFunctions = (SbiFunctions) aSession.load(SbiFunctions.class, functId);
				SbiObjFuncId aSbiObjFuncId = new SbiObjFuncId();
				aSbiObjFuncId.setSbiFunctions(aSbiFunctions);
				aSbiObjFuncId.setSbiObjects(hibBIObject);
				SbiObjFunc aSbiObjFunc = new SbiObjFunc(aSbiObjFuncId);
				aSession.save(aSbiObjFunc);
				hibObjFunc.add(aSbiObjFunc);
			}
			hibBIObject.setSbiObjFuncs(hibObjFunc);
			// update biobject template info 
			if(objTemp != null) {
				insertObjTemplate(aSession, objTemp, hibBIObject);
			}
			tx.commit();		
		}  catch (HibernateException he) {
			logger.error(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
	}

	
	private void insertObjTemplate(Session aSession, ObjTemplate objTemp, SbiObjects hibBIObject) throws EMFUserError {
		// store the binary content
		SbiBinContents hibBinContent = new SbiBinContents();
		byte[] bytes = null;
		try {
			bytes = objTemp.getContent();
		} catch (EMFInternalError e) {
			logger.error("Could not retrieve content of ObjTemplate object in input.");
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
		hibBinContent.setContent(bytes);
		
		Integer idBin = (Integer)aSession.save(hibBinContent);
		// recover the saved binary hibernate object
		hibBinContent = (SbiBinContents) aSession.load(SbiBinContents.class, idBin);
		// set to not active the current active template
		String hql = "update SbiObjTemplates sot set sot.active = false where sot.active = true and sot.sbiObject.biobjId="+hibBIObject.getBiobjId();
                Query query = aSession.createQuery(hql);
                try{
                	int rowCount = query.executeUpdate();
                } catch (Exception e) {
                	// TODO trace exception
                	System.out.println(e);
                }
        	// get the next prog for the new template
                Integer nextProg = null;
                try {
                	nextProg = DAOFactory.getObjTemplateDAO().getNextProgForTemplate(hibBIObject.getBiobjId());
                } catch (Exception e) {
                	throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
                }
		// store the object template
		SbiObjTemplates hibObjTemplate = new SbiObjTemplates();
		hibObjTemplate.setActive(new Boolean(true));
		hibObjTemplate.setCreationDate(new Date());
		hibObjTemplate.setName(objTemp.getName());
		hibObjTemplate.setProg(nextProg);
		hibObjTemplate.setSbiBinContents(hibBinContent);
		hibObjTemplate.setSbiObject(hibBIObject);
		aSession.save(hibObjTemplate);
	}
	
	
	/**
	 * Implements the query to insert a BIObject and its template. All information needed is stored 
	 * into the input <code>BIObject</code> and <code>ObjTemplate</code> objects.
	 * @param obj The object containing all insert information
	 * @param objTemp The template of the biobject
	 * @throws EMFUserError If an Exception occurred
	 */
	public void insertBIObject(BIObject obj, ObjTemplate objTemp) throws EMFUserError {
		internalInsertBIObject(obj, objTemp);
	}
	
	/**
	 * Implements the query to insert a BIObject. All information needed is stored 
	 * into the input <code>BIObject</code> object.
	 * @param obj The object containing all insert information
	 * @throws EMFUserError If an Exception occurred
	 */
	public void insertBIObject(BIObject obj) throws EMFUserError {
		internalInsertBIObject(obj, null);
	}
	
	private void internalInsertBIObject(BIObject obj, ObjTemplate objTemp) throws EMFUserError {
	    logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiObjects hibBIObject = new SbiObjects();
			SbiEngines hibEngine = (SbiEngines) aSession.load(SbiEngines.class,	obj.getEngine().getId());
			hibBIObject.setSbiEngines(hibEngine); 
			hibBIObject.setDescr(obj.getDescription());
			hibBIObject.setLabel(obj.getLabel());
			hibBIObject.setName(obj.getName());
			hibBIObject.setEncrypt(new Short(obj.getEncrypt().shortValue()));
			hibBIObject.setVisible(new Short(obj.getVisible().shortValue()));
			hibBIObject.setRelName(obj.getRelName());
			SbiDomains hibState = (SbiDomains) aSession.load(SbiDomains.class, obj.getStateID());
			hibBIObject.setState(hibState);
			hibBIObject.setStateCode(obj.getStateCode());
			SbiDomains hibObjectType = (SbiDomains) aSession.load(SbiDomains.class, obj.getBiObjectTypeID());
			hibBIObject.setObjectType(hibObjectType);
			hibBIObject.setObjectTypeCode(obj.getBiObjectTypeCode());
			if (obj.getDataSourceId() != null) {
				SbiDataSource dSource=(SbiDataSource) aSession.load(SbiDataSource.class, obj.getDataSourceId());
				hibBIObject.setDataSource(dSource);
			}
			// uuid generation
			UUIDGenerator uuidGenerator = UUIDGenerator.getInstance();
			UUID uuidObj = uuidGenerator.generateTimeBasedUUID();
			String uuid = uuidObj.toString();
			hibBIObject.setUuid(uuid);
            // save biobject
			Integer id = (Integer) aSession.save(hibBIObject);
			// recover the saved hibernate object
			hibBIObject = (SbiObjects) aSession.load(SbiObjects.class, id);
			// functionalities storing
			Set hibObjFunc = new HashSet();
			List functionalities = obj.getFunctionalities();
			for (Iterator it = functionalities.iterator(); it.hasNext(); ) {
				Integer functId = (Integer) it.next();
				SbiFunctions aSbiFunctions = (SbiFunctions) aSession.load(SbiFunctions.class, functId);
				SbiObjFuncId aSbiObjFuncId = new SbiObjFuncId();
				aSbiObjFuncId.setSbiFunctions(aSbiFunctions);
				aSbiObjFuncId.setSbiObjects(hibBIObject);
				SbiObjFunc aSbiObjFunc = new SbiObjFunc(aSbiObjFuncId);
				aSession.save(aSbiObjFunc);
				hibObjFunc.add(aSbiObjFunc);
			}
			hibBIObject.setSbiObjFuncs(hibObjFunc);	
			
			if(objTemp != null) {
				insertObjTemplate(aSession, objTemp, hibBIObject);
			}
			
			tx.commit();
			obj.setId(id);
		} catch (HibernateException he) {
			logger.error("HibernateException",he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
			logger.debug("OUT");
		}
	}

	
	
	
	
	
	/**
	 * @see it.eng.spagobi.analiticalmodel.document.dao.IBIObjectDAO#eraseBIObject(it.eng.spagobi.analiticalmodel.document.bo.BIObject, java.lang.Integer)
	 */
	public void eraseBIObject(BIObject obj, Integer idFunct) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			// load object
			SbiObjects hibBIObject = (SbiObjects) aSession.load(SbiObjects.class, obj.getId());			
			// erase object from functionalities 
			Set hibObjFuncs = hibBIObject.getSbiObjFuncs();
			Iterator itObjFunc = hibObjFuncs.iterator();
			while (itObjFunc.hasNext()) {
				SbiObjFunc aSbiObjFunc = (SbiObjFunc) itObjFunc.next();
				if (idFunct == null || aSbiObjFunc.getId().getSbiFunctions().getFunctId().intValue() == idFunct.intValue()) {
					logger.debug("Deleting object [" + obj.getName() + "] from folder [" + aSbiObjFunc.getId().getSbiFunctions().getPath() + "]");
					aSession.delete(aSbiObjFunc);
				}
			}

			aSession.flush();
			// reload object
			aSession.refresh(hibBIObject);

			// if the object is no more referenced in any folder, erases it from sbi_obejcts table 
			hibObjFuncs = hibBIObject.getSbiObjFuncs();
			if (hibObjFuncs == null || hibObjFuncs.size() == 0) {
				
				logger.debug("The object [" + obj.getName() + "] is no more referenced by any functionality. It will be completely deleted from db.");
				
				// delete templates
				String hql = "from SbiObjTemplates sot where sot.sbiObject.biobjId="+obj.getId();
				Query query = aSession.createQuery(hql);
				List templs = query.list();
				Iterator iterTempls = templs.iterator();
				while(iterTempls.hasNext()) {
					SbiObjTemplates hibObjTemp = (SbiObjTemplates)iterTempls.next();
					SbiBinContents hibBinCont = hibObjTemp.getSbiBinContents();
					aSession.delete(hibObjTemp);
					aSession.delete(hibBinCont);
					
				}
				
				//delete viewpoints eventually associated
				List viewpoints = new ArrayList();
				IViewpointDAO biVPDAO = DAOFactory.getViewpointDAO();
				viewpoints =  biVPDAO.loadAllViewpointsByObjID(obj.getId());
				for (int i=0; i<viewpoints.size(); i++){
					Viewpoint vp =(Viewpoint)viewpoints.get(i);
					biVPDAO.eraseViewpoint(vp.getVpId());
				}
				
				// delete parameters associated
				Set objPars = hibBIObject.getSbiObjPars();
				Iterator itObjPar = objPars.iterator();
				while (itObjPar.hasNext()) {
					SbiObjPar aSbiObjPar = (SbiObjPar) itObjPar.next();
					// deletes all ObjParuse object (dependencies) of the biparameter
					IObjParuseDAO objParuseDAO = DAOFactory.getObjParuseDAO();
					List objParuses = objParuseDAO.loadObjParuses(aSbiObjPar.getObjParId());
					Iterator itObjParuses = objParuses.iterator();
					while (itObjParuses.hasNext()) {
						ObjParuse aObjParuse = (ObjParuse) itObjParuses.next();
						objParuseDAO.eraseObjParuse(aObjParuse);
					}
					// deletes the biparameter
					aSession.delete(aSbiObjPar);
				}
				
				// delete object
				aSession.delete(hibBIObject);
				
				// update subreports table 
				ISubreportDAO subrptdao = DAOFactory.getSubreportDAO();
				subrptdao.eraseSubreportByMasterRptId(obj.getId());
				subrptdao.eraseSubreportBySubRptId(obj.getId());
			}
			// commit all changes
			tx.commit();				
		} catch (HibernateException he) {
			logger.error(he);
			if (tx != null && tx.isActive())
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} catch (Exception ex) {
			logger.error(ex);
			if (tx != null && tx.isActive())
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100); 
		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
	}

	
	/** 
	 * @see it.eng.spagobi.analiticalmodel.document.dao.IBIObjectDAO#getCorrectRolesForExecution(java.lang.Integer, it.eng.spago.security.IEngUserProfile)
	 */
	public List getCorrectRolesForExecution(Integer id, IEngUserProfile profile) throws EMFUserError {
		List correctRoles = null;
		try  {
			correctRoles = getCorrectRoles(id, profile.getRoles());
		} catch (EMFInternalError emfie) {
		    logger.error("error getting role from the user profile",emfie);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
		return correctRoles;
	}

	
	/** 
	 * @see it.eng.spagobi.analiticalmodel.document.dao.IBIObjectDAO#getCorrectRolesForExecution(java.lang.Integer)
	 */
	public List getCorrectRolesForExecution(Integer id) throws EMFUserError {
		List roles = DAOFactory.getRoleDAO().loadAllRoles();
		List nameRoles = new ArrayList();
		Iterator iterRoles = roles.iterator();
		Role role = null;
		while(iterRoles.hasNext()) {
			role = (Role)iterRoles.next();
			nameRoles.add(role.getName());
		}
		return getCorrectRoles(id, nameRoles);
	}
	
	/**
	 * Gets a list of correct role according to the report at input, identified
	 * by its id
	 * 
	 * @param id	The Integer representing report's id
	 * @param roles	The collection of all roles
	 * @return The correct roles list
	 * @throws EMFUserError if any exception occurred
	 */
	private List getCorrectRoles(Integer id, Collection roles) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		Query hqlQuery = null;
		String hql = null;
		List correctRoles = new ArrayList();
		
		
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			// find all id parameters relative to the objects
			hql = "select par.parId from " +
					     "SbiParameters as par, SbiObjects as obj, SbiObjPar as objpar  " + 
				         "where obj.biobjId = '"+id+"' and " +
				         "      obj.biobjId = objpar.sbiObject.biobjId and " +
				         "      par.parId = objpar.id.sbiParameter.parId ";
			hqlQuery = aSession.createQuery(hql);
			List idParameters = hqlQuery.list();
			
			if(idParameters.size() == 0) {
				List allCorrectRoles = new ArrayList();
				// if the object has not parameter associate all the roles can execute the 
				// object correctly and in the same manner.
				SbiObjects hibBIObject = (SbiObjects)aSession.load(SbiObjects.class, id);
				String objectState = hibBIObject.getState().getValueCd();
				Set hibObjFuncs = hibBIObject.getSbiObjFuncs();
				Iterator itObjFunc = hibObjFuncs.iterator();
				while (itObjFunc.hasNext()) {
					SbiObjFunc aSbiObjFunc = (SbiObjFunc) itObjFunc.next();
					SbiFunctions aSbiFunctions = aSbiObjFunc.getId().getSbiFunctions();
					String rolesHql = "select distinct roles.name from " +
						"SbiExtRoles as roles, SbiFuncRole as funcRole " + 
						"where roles.extRoleId = funcRole.id.role.extRoleId and " +
						"	   funcRole.id.function.functId = " + aSbiFunctions.getFunctId() + " and " +
						"	   funcRole.id.state.valueCd = '" + objectState + "' ";
					Query rolesHqlQuery = aSession.createQuery(rolesHql);
					// get the list of roles that can see the document (in REL or TEST state) in that functionality
					List rolesNames = new ArrayList();
					rolesNames = rolesHqlQuery.list();
					allCorrectRoles.addAll(rolesNames);
				}
				Iterator rolesIt = roles.iterator();
				while (rolesIt.hasNext()) {
					// if the role is a user role and can see the document (in REL or TEST state), 
					// it is a correct role
					String role = rolesIt.next().toString();
					if (allCorrectRoles.contains(role)) correctRoles.add(role);
				}
				return correctRoles;
				// if the object has not parameter associates all the roles that have the execution or
				// test permissions on the containing folders are correct roles in the same manner.
			}
			
			Iterator iterRoles = roles.iterator();
			Iterator iterParam = null;
			String role = null;
			String idPar = null;
			List parUses = null;
			// for each role of the user
			while(iterRoles.hasNext()) {
				boolean correct = true;
				role = iterRoles.next().toString();
				iterParam = idParameters.iterator();
				// for each parameter get the number of the modality for the current role
				while(iterParam.hasNext()) {
					idPar = iterParam.next().toString();
					hql = "select puseDet.id.sbiParuse.useId " +
								 "from SbiParuse as puse, " +
								 "     SbiParuseDet as puseDet, " +
								 "     SbiExtRoles as rol  " + 
			        			 "where rol.name = '"+role+"' and " +
			        			 "      puseDet.id.sbiExtRoles.extRoleId = rol.extRoleId and " +
			        			 "      puse.sbiParameters.parId = "+idPar+" and " +
			        			 "		puseDet.id.sbiParuse.useId = puse.useId";
					hqlQuery = aSession.createQuery(hql);
					parUses = hqlQuery.list();
				    // if the modality for the current role and the current parameter are more
					// or less than one the  role can't execute the report and so it isn't
					// correct
					if(parUses.size()!=1) {
				    	correct = false;
				    }
				}
				if(correct) {
					correctRoles.add(role);
				}
			}
			tx.commit();
		} catch (HibernateException he) {
			logger.error(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		return correctRoles;
	}
	
	
	
	/**
	 * From the Hibernate BI object at input, gives the corrispondent BI
	 * object 
	 * 
	 * @param hibBIObject	The Hibernate BI object
	 * @return the corrispondent output <code>BIObject</code>
	 */
	public BIObject toBIObject(SbiObjects hibBIObject){
		    // create empty biobject
			BIObject aBIObject = new BIObject();
			// set type (type code and id)
			aBIObject.setBiObjectTypeCode(hibBIObject.getObjectTypeCode());
			aBIObject.setBiObjectTypeID(hibBIObject.getObjectType().getValueId());
			// set description
			String descr = hibBIObject.getDescr();
			if(descr==null) descr = "";
			aBIObject.setDescription(descr);
			// set encrypt flag
			aBIObject.setEncrypt(new Integer(hibBIObject.getEncrypt().intValue()));
			// set visible flag
			aBIObject.setVisible(new Integer(hibBIObject.getVisible().intValue()));
			// set engine						
			aBIObject.setEngine(new EngineDAOHibImpl().toEngine(hibBIObject.getSbiEngines()));
			// set data source
			if (hibBIObject.getDataSource()!=null){
				aBIObject.setDataSourceId(new Integer(hibBIObject.getDataSource().getDsId()));
			}
			// set id
			aBIObject.setId(hibBIObject.getBiobjId());
			aBIObject.setLabel(hibBIObject.getLabel());
			aBIObject.setName(hibBIObject.getName());
			// set path
			aBIObject.setPath(hibBIObject.getPath());
			aBIObject.setUuid(hibBIObject.getUuid());
			aBIObject.setRelName(hibBIObject.getRelName());
			aBIObject.setStateCode(hibBIObject.getStateCode());
			aBIObject.setStateID(hibBIObject.getState().getValueId());
			
			List functionlities = new ArrayList();
			Set hibObjFuncs = hibBIObject.getSbiObjFuncs();
			for (Iterator it = hibObjFuncs.iterator(); it.hasNext(); ) {
				SbiObjFunc aSbiObjFunc = (SbiObjFunc) it.next();
				Integer functionalityId = aSbiObjFunc.getId().getSbiFunctions().getFunctId();
				functionlities.add(functionalityId);
			}
			aBIObject.setFunctionalities(functionlities);
			
			return aBIObject;
	}
	

	public List loadAllBIObjects() throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		List realResult = new ArrayList();
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			Query hibQuery = aSession.createQuery(" from SbiObjects s order by s.label");
			List hibList = hibQuery.list();
			Iterator it = hibList.iterator();
			while (it.hasNext()) {
				realResult.add(toBIObject((SbiObjects) it.next()));
			}
			tx.commit();
		} catch (HibernateException he) {
			logger.error(he);
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
	
	public List loadAllBIObjects(String filterOrder) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		List realResult = new ArrayList();
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			Query hibQuery = aSession.createQuery(" from SbiObjects s order by s." + filterOrder);
			List hibList = hibQuery.list();
			Iterator it = hibList.iterator();
			while (it.hasNext()) {
				realResult.add(toBIObject((SbiObjects) it.next()));
			}
			tx.commit();
		} catch (HibernateException he) {
			logger.error(he);
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
	 * Gets the biparameters associated with to a biobject 
	 * @param aBIObject BIObject the biobject to analize
	 * @return List, list of the biparameters associated with the biobject
	 * @throws EMFUserError
	 */
	public List getBIObjectParameters(BIObject aBIObject) throws EMFUserError {
		IBIObjectParameterDAO biobjDAO = DAOFactory.getBIObjectParameterDAO();
		List biparams = biobjDAO.loadBIObjectParametersById(aBIObject.getId());
		return biparams;
	}



	public List loadAllBIObjectsFromInitialPath(String initialPath) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		List realResult = new ArrayList();
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			Query hibQuery = aSession.createQuery(
			"select " +
			"	distinct(objects) " +
			"from " +
			"	SbiObjects as objects, SbiObjFunc as objFuncs, SbiFunctions as functions " +
			"where " +
			"	objects.biobjId = objFuncs.id.sbiObjects.biobjId " +
			"	and objFuncs.id.sbiFunctions.functId = functions.functId " +
			"	and " +
			"		(functions.path = '" + initialPath + "' " +
			"		 or functions.path like '" + initialPath + "/%' ) " + 
			"order by " +
			"	objects.label");
			List hibList = hibQuery.list();
			Iterator it = hibList.iterator();
			while (it.hasNext()) {
				realResult.add(toBIObject((SbiObjects) it.next()));
			}
			tx.commit();
		} catch (HibernateException he) {
			logger.error(he);

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

	public List loadAllBIObjectsFromInitialPath(String initialPath, String filterOrder) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		List realResult = new ArrayList();
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			Query hibQuery = aSession.createQuery(
			"select " +
			"	distinct(objects) " +
			"from " +
			"	SbiObjects as objects, SbiObjFunc as objFuncs, SbiFunctions as functions " +
			"where " +
			"	objects.biobjId = objFuncs.id.sbiObjects.biobjId " +
			"	and objFuncs.id.sbiFunctions.functId = functions.functId " +
			"	and " +
			"		(functions.path = '" + initialPath + "' " +
			"		 or functions.path like '" + initialPath + "/%' ) " + 
			"order by " +
			"	objects." + filterOrder);
			List hibList = hibQuery.list();
			Iterator it = hibList.iterator();
			while (it.hasNext()) {
				realResult.add(toBIObject((SbiObjects) it.next()));
			}
			tx.commit();
		} catch (HibernateException he) {
			logger.error(he);

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




	public BIObject loadBIObjectForDetail(String path) throws EMFUserError {
		BIObject biObject = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			String hql = " from SbiObjects where path = '" + path + "'";
			Query hqlQuery = aSession.createQuery(hql);
			SbiObjects hibObject = (SbiObjects)hqlQuery.uniqueResult();
			if (hibObject == null) return null;
			biObject = toBIObject(hibObject);
			tx.commit();
		} catch (HibernateException he) {
			logger.error(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		return biObject;
	}

}
