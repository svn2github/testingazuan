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

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.cms.exceptions.BuildOperationException;
import it.eng.spago.cms.exceptions.OperationExecutionException;
import it.eng.spago.cms.exec.CMSConnection;
import it.eng.spago.cms.exec.OperationExecutor;
import it.eng.spago.cms.exec.OperationExecutorManager;
import it.eng.spago.cms.exec.operations.DeleteOperation;
import it.eng.spago.cms.exec.operations.GetOperation;
import it.eng.spago.cms.exec.operations.OperationBuilder;
import it.eng.spago.cms.exec.operations.SetOperation;
import it.eng.spago.cms.exec.results.ElementDescriptor;
import it.eng.spago.cms.init.CMSManager;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.LowFunctionality;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.bo.dao.ILowFunctionalityDAO;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.metadata.SbiDomains;
import it.eng.spagobi.metadata.SbiExtRoles;
import it.eng.spagobi.metadata.SbiFuncRole;
import it.eng.spagobi.metadata.SbiFuncRoleId;
import it.eng.spagobi.metadata.SbiFunctions;
import it.eng.spagobi.services.modules.TreeObjectsModule;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;

/**
 * Defines the Hibernate implementations for all DAO methods,
 * for a functionality. 
 * 
 * @author sulis
 */
public class LowFunctionalityDAOHibImpl extends AbstractHibernateDAO implements ILowFunctionalityDAO{

	
	/**
	 * @see it.eng.spagobi.bo.dao.ILowFunctionalityDAO#loadLowFunctionalityByID(java.lang.Integer)
	 * 
	 */
	public LowFunctionality loadLowFunctionalityByID(Integer functionalityID) throws EMFUserError {
		LowFunctionality funct = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiFunctions hibFunct = (SbiFunctions)aSession.load(SbiFunctions.class, functionalityID);
			funct = toLowFunctionality(hibFunct);
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
		return funct;
	}
	
	
	
	/**
	 * @see it.eng.spagobi.bo.dao.ILowFunctionalityDAO#loadLowFunctionalityByPath(java.lang.String)
	 * 
	 */
	public LowFunctionality loadLowFunctionalityByPath(String functionalityPath)
			throws EMFUserError {
		
		LowFunctionality lowFunctionaliy = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			Criterion domainCdCriterrion = Expression.eq("path",
					functionalityPath);
			Criteria criteria = aSession.createCriteria(SbiFunctions.class);
			criteria.add(domainCdCriterrion);
			SbiFunctions hibFunct = (SbiFunctions) criteria.uniqueResult();
			if (hibFunct == null) return null;
			
			lowFunctionaliy = toLowFunctionality(hibFunct);
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
		return lowFunctionaliy;
	}

	/**
	 * @see it.eng.spagobi.bo.dao.ILowFunctionalityDAO#modifyLowFunctionality(it.eng.spagobi.bo.LowFunctionality)
	 * 
	 */
	public void modifyLowFunctionality(LowFunctionality aLowFunctionality)
			throws EMFUserError {

		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiFunctions hibFunct = (SbiFunctions) aSession.load(
					SbiFunctions.class, aLowFunctionality.getId());
			// delete all roles functionality
			Set oldRoles = hibFunct.getSbiFuncRoles();
			Iterator iterOldRoles = oldRoles.iterator();
			while (iterOldRoles.hasNext()) {
				SbiFuncRole role = (SbiFuncRole) iterOldRoles.next();
				aSession.delete(role);
			}
			// save roles functionality
			Set functRoleToSave = new HashSet();
			functRoleToSave.addAll(saveRolesFunctionality(aSession, hibFunct,
					aLowFunctionality, "DEV"));
			functRoleToSave.addAll(saveRolesFunctionality(aSession, hibFunct,
					aLowFunctionality, "TEST"));
			functRoleToSave.addAll(saveRolesFunctionality(aSession, hibFunct,
					aLowFunctionality, "REL"));
			// set new roles into sbiFunctions
			hibFunct.setSbiFuncRoles(functRoleToSave);
			// set new data
			hibFunct.setCode(aLowFunctionality.getCode());
			hibFunct.setDescr(aLowFunctionality.getDescription());
			Criterion domainCdCriterrion = Expression.eq("valueCd",
					aLowFunctionality.getCodType());
			Criteria criteria = aSession.createCriteria(SbiDomains.class);
			criteria.add(domainCdCriterrion);
			SbiDomains functTypeDomain = (SbiDomains) criteria.uniqueResult();
			if (functTypeDomain == null){
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
					    "LowFunctionalityDAOHibImpl", 
					    "modifyLowFunctionality", 
					    "The Domain with value_cd="+aLowFunctionality.getCodType()+" does not exist.");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 1037);
			}
			
			hibFunct.setFunctType(functTypeDomain);
			hibFunct.setFunctTypeCd(aLowFunctionality.getCodType());
			hibFunct.setName(aLowFunctionality.getName());
			hibFunct.setPath(aLowFunctionality.getPath());
			// commit all changes
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
	 * Saves all roles for a functionality, using session and state information.
	 * The state for a functionality can be Developing, Testing and execution and 
	 * each state has its own roles.
	 * 
	 * @param aSession The current session object
	 * @param hibFunct The functionality hibernate object
	 * @param aLowFunctionality The Low Functionality object
	 * @param state The string defining the state
	 * @return A collection object containing all roles 
	 * @throws EMFUserError 
	 * 
	 */
	private Set saveRolesFunctionality(Session aSession, SbiFunctions hibFunct, LowFunctionality aLowFunctionality, String state) throws EMFUserError {
		Set functRoleToSave = new HashSet();
		Criterion domainCdCriterrion = null;
		Criteria criteria = null;	
		criteria = aSession.createCriteria(SbiDomains.class);
		domainCdCriterrion = Expression.eq("valueCd", state);
		criteria.add(domainCdCriterrion);
		SbiDomains devStateDomain = (SbiDomains)criteria.uniqueResult();
		if (devStateDomain == null){
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
				    "LowFunctionalityDAOHibImpl", 
				    "saveRolesFunctionality", 
				    "The Domain with value_cd="+state+" does not exist.");
			throw new EMFUserError(EMFErrorSeverity.ERROR, 1039);
		}
		Role[] roles = null;
		if(state.equalsIgnoreCase("DEV")) {
			roles = aLowFunctionality.getDevRoles();
		} else if(state.equalsIgnoreCase("TEST")) {
			roles = aLowFunctionality.getTestRoles();
		} else if(state.equalsIgnoreCase("REL")) {
			roles = aLowFunctionality.getExecRoles();
		}
		for(int i=0; i<roles.length; i++) {
			Role role = roles[i];
			domainCdCriterrion = Expression.eq("name", role.getName());
			criteria = aSession.createCriteria(SbiExtRoles.class);
			criteria.add(domainCdCriterrion);
			SbiExtRoles hibRole = (SbiExtRoles)criteria.uniqueResult();
			SbiFuncRoleId sbifuncroleid = new SbiFuncRoleId();
			sbifuncroleid.setFunction(hibFunct);
			sbifuncroleid.setState(devStateDomain);
			sbifuncroleid.setRole(hibRole);
			SbiFuncRole sbifuncrole = new SbiFuncRole();
			sbifuncrole.setId(sbifuncroleid);
			sbifuncrole.setStateCd(devStateDomain.getValueCd());
			aSession.save(sbifuncrole);
			functRoleToSave.add(sbifuncrole);
		}
		return functRoleToSave;
	}
	
	
	/**
	 * @see it.eng.spagobi.bo.dao.ILowFunctionalityDAO#insertLowFunctionality(it.eng.spagobi.bo.LowFunctionality, it.eng.spago.security.IEngUserProfile)
	 * 
	 */
	public void insertLowFunctionality(LowFunctionality aLowFunctionality,
			IEngUserProfile profile) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		CMSConnection connection = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiFunctions hibFunct = new SbiFunctions();
			hibFunct.setCode(aLowFunctionality.getCode());
			hibFunct.setDescr(aLowFunctionality.getDescription());
			Criterion domainCdCriterrion = Expression.eq("valueCd",
					aLowFunctionality.getCodType());
			Criteria criteria = aSession.createCriteria(SbiDomains.class);
			criteria.add(domainCdCriterrion);
			SbiDomains functTypeDomain = (SbiDomains) criteria.uniqueResult();
			if (functTypeDomain == null){
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
					    "LowFunctionalityDAOHibImpl", 
					    "insertLowFunctionality", 
					    "The Domain with value_cd="+aLowFunctionality.getCodType()+" does not exist.");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 1038);
			}
			hibFunct.setFunctType(functTypeDomain);
			hibFunct.setFunctTypeCd(aLowFunctionality.getCodType());
			hibFunct.setName(aLowFunctionality.getName());
			hibFunct.setPath(aLowFunctionality.getPath());
			aSession.save(hibFunct);
			// save roles functionality
			Set functRoleToSave = new HashSet();
			functRoleToSave.addAll(saveRolesFunctionality(aSession, hibFunct,
					aLowFunctionality, "DEV"));
			functRoleToSave.addAll(saveRolesFunctionality(aSession, hibFunct,
					aLowFunctionality, "TEST"));
			functRoleToSave.addAll(saveRolesFunctionality(aSession, hibFunct,
					aLowFunctionality, "REL"));
			// set new roles into sbiFunctions
			hibFunct.setSbiFuncRoles(functRoleToSave);

			OperationExecutor executor = OperationExecutorManager
					.getOperationExecutor();
			connection = CMSManager.getInstance().getConnection();
			SetOperation set = OperationBuilder.buildSetOperation();
			set.setPath(aLowFunctionality.getPath());
			set.setType(SetOperation.TYPE_CONTAINER);
			set.setEraseOldProperties(true);
			String[] typePropValues = new String[] { AdmintoolsConstants.LOW_FUNCTIONALITY_TYPE };
			set.setStringProperty(AdmintoolsConstants.NODE_CMS_TYPE,
					typePropValues);
			executor.setObject(connection, set, profile, true);
			tx.commit();
		} catch (OperationExecutionException oee) {
			logException(oee);
			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} catch (BuildOperationException boe) {
			logException(boe);
			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} catch (EMFInternalError emfie) {
			logException(emfie);
			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} catch (HibernateException he) {
			logException(he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			if(connection!=null){
				if(!connection.isClose()) {
					connection.close();
				}
			}
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}

	}

	/**
	 * @see it.eng.spagobi.bo.dao.ILowFunctionalityDAO#eraseLowFunctionality(it.eng.spagobi.bo.LowFunctionality, it.eng.spago.security.IEngUserProfile)
	 * 
	 */
	public void eraseLowFunctionality(LowFunctionality aLowFunctionality, IEngUserProfile profile) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		
		CMSConnection connection = null;
		try {

			if(hasChild(aLowFunctionality.getPath())) {
				HashMap params = new HashMap();
				params.put(AdmintoolsConstants.PAGE, TreeObjectsModule.MODULE_PAGE);
				params.put(SpagoBIConstants.ACTOR, SpagoBIConstants.ADMIN_ACTOR);
				params.put(SpagoBIConstants.OPERATION, SpagoBIConstants.FUNCTIONALITIES_OPERATION);
				throw new EMFUserError(EMFErrorSeverity.ERROR, 1000, new Vector(), params);
			}
		
			OperationExecutor executor = OperationExecutorManager.getOperationExecutor();
			connection = CMSManager.getInstance().getConnection();
			
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiFunctions hibFunct = (SbiFunctions) aSession.load(
					SbiFunctions.class, aLowFunctionality.getId());
			Set oldRoles = hibFunct.getSbiFuncRoles();
			Iterator iterOldRoles = oldRoles.iterator();
			while (iterOldRoles.hasNext()) {
				SbiFuncRole role = (SbiFuncRole) iterOldRoles.next();
				aSession.delete(role);
			}
			aSession.delete(hibFunct);
			
			DeleteOperation del = OperationBuilder.buildDeleteOperation();
			del.setPath(aLowFunctionality.getPath());
			executor.deleteObject(connection, del, profile, true);
			
			tx.commit();
		} catch (HibernateException he) {
			logException(he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} catch (EMFUserError emfue) {
			if (tx != null)
				tx.rollback();
			throw emfue;
		} catch (Exception e) {
			logException(e);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if(connection!=null){
				if(!connection.isClose()) {
					connection.close();
				}
			}
			if(aSession != null)
				if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
	}

	/**
	 * From the Hibernate Low Functionality object at input, gives the corrispondent 
	 * <code>LowFunctionality</code> object.
	 * 
	 * @param hibBIObject	The Hibernate BI object
	 * @return the corrispondent output <code>BIObject</code>
	 */
	private LowFunctionality toLowFunctionality(SbiFunctions hibFunct){
		
		LowFunctionality lowFunct = new LowFunctionality();
		lowFunct.setId(hibFunct.getFunctId());
		lowFunct.setCode(hibFunct.getCode());
		lowFunct.setCodType(hibFunct.getFunctTypeCd());
		lowFunct.setDescription(hibFunct.getDescr());
		lowFunct.setName(hibFunct.getName());
		lowFunct.setPath(hibFunct.getPath());
		
		List devRolesList = new ArrayList();
		List testRolesList = new ArrayList();
		List execRolesList = new ArrayList();
		
		Set roles = hibFunct.getSbiFuncRoles();
		Iterator iterRoles = roles.iterator();
		while(iterRoles.hasNext()) {
			SbiFuncRole hibfuncrole = (SbiFuncRole)iterRoles.next();
		    SbiExtRoles hibRole = hibfuncrole.getId().getRole();
		    SbiDomains hibState = hibfuncrole.getId().getState();
		    
		    RoleDAOHibImpl roleDAO =  new RoleDAOHibImpl();
		    Role role = roleDAO.toRole(hibRole);
		    
		    String state = hibState.getValueCd();
		    if(state.equals("DEV")) {
		    	devRolesList.add(role);
		    } else if(state.equals("TEST")) {
		    	testRolesList.add(role);
		    } else if(state.equals("REL")) {
		    	execRolesList.add(role);
		    }
		}
		
		Role[] execRoles = new Role[execRolesList.size()];
		Role[] devRoles = new Role[devRolesList.size()];
		Role[] testRoles = new Role[testRolesList.size()];

		for (int i = 0; i < execRolesList.size(); i++)
			execRoles[i] = (Role) execRolesList.get(i);
		for (int i = 0; i < testRolesList.size(); i++)
			testRoles[i] = (Role) testRolesList.get(i);
		for (int i = 0; i < devRolesList.size(); i++)
			devRoles[i] = (Role) devRolesList.get(i);

		lowFunct.setDevRoles(devRoles);
		lowFunct.setTestRoles(testRoles);
		lowFunct.setExecRoles(execRoles);
		
		return lowFunct;
	}

	
	/**
	 * @see it.eng.spagobi.bo.dao.ILowFunctionalityDAO#existByCode(java.lang.String)
	 * 
	 */
	public Integer existByCode(String code) throws EMFUserError {
		Integer id = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			Criterion domainCdCriterrion = Expression.eq("code", code);
			Criteria criteria = aSession.createCriteria(SbiFunctions.class);
			criteria.add(domainCdCriterrion);
			SbiFunctions func = (SbiFunctions) criteria.uniqueResult();
			if (func != null) {
				id = func.getFunctId();
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
		return id;
	}

	/**
	 * @see it.eng.spagobi.bo.dao.ILowFunctionalityDAO#hasChild(java.lang.String)
	 * 
	 */
	public boolean hasChild(String path) throws EMFUserError {
		CMSConnection connection = null;
		try{
			RequestContainer reqCont = RequestContainer.getRequestContainer();
			SessionContainer sessCont = reqCont.getSessionContainer();
			SessionContainer permSess = sessCont.getPermanentContainer();
			IEngUserProfile profile = (IEngUserProfile)permSess.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
			OperationExecutor executor = OperationExecutorManager.getOperationExecutor();
			connection = CMSManager.getInstance().getConnection();
			GetOperation get = OperationBuilder.buildGetOperation();
			get.setPath(path);
			ElementDescriptor desc = executor.getObject(connection, get, profile, true);
			if(desc.hasChilds()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			if(connection!=null) {
				if(!connection.isClose()) {
					connection.close();
				}
			}
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
					"LowFunctionalityDAOImpl", 
					"existByCode", 
					"Error during recorer of the functionality by code", e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 1003);
		}
	}
	

}
