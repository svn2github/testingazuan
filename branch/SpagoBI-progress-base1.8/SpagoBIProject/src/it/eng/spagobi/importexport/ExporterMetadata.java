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
package it.eng.spagobi.importexport;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.Check;
import it.eng.spagobi.bo.Domain;
import it.eng.spagobi.bo.Engine;
import it.eng.spagobi.bo.LowFunctionality;
import it.eng.spagobi.bo.ModalitiesValue;
import it.eng.spagobi.bo.Parameter;
import it.eng.spagobi.bo.ParameterUse;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IDomainDAO;
import it.eng.spagobi.metadata.SbiChecks;
import it.eng.spagobi.metadata.SbiDomains;
import it.eng.spagobi.metadata.SbiEngines;
import it.eng.spagobi.metadata.SbiExtRoles;
import it.eng.spagobi.metadata.SbiFuncRole;
import it.eng.spagobi.metadata.SbiFuncRoleId;
import it.eng.spagobi.metadata.SbiFunctions;
import it.eng.spagobi.metadata.SbiLov;
import it.eng.spagobi.metadata.SbiObjects;
import it.eng.spagobi.metadata.SbiParameters;
import it.eng.spagobi.metadata.SbiParuse;
import it.eng.spagobi.metadata.SbiParuseCk;
import it.eng.spagobi.metadata.SbiParuseCkId;
import it.eng.spagobi.metadata.SbiParuseDet;
import it.eng.spagobi.metadata.SbiParuseDetId;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Implements methods to insert exported metadata into the exported database 
 */
public class ExporterMetadata {

	/**
	 * Insert a domain into the exported database
	 * @param domain Domain object to export
	 * @param session Hibernate session for the exported database
	 * @throws EMFUserError
	 */
	public void insertDomain(Domain domain, Session session) throws EMFUserError {
		try {
			Transaction tx = session.beginTransaction();
			Query hibQuery = session.createQuery(" from SbiDomains where valueId = " + domain.getValueId());
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				return;
			}
			SbiDomains hibDomain = new SbiDomains(domain.getValueId());
			hibDomain.setDomainCd(domain.getDomainCode());
			hibDomain.setDomainNm(domain.getDomainName());
			hibDomain.setValueCd(domain.getValueCd());
			hibDomain.setValueDs(domain.getValueDescription());
			hibDomain.setValueNm(domain.getValueName());
			session.save(hibDomain);
			tx.commit();
		} catch (Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "insertDomain",
					"Error while inserting domain into export database " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8005, "component_impexp_messages");
		}
	}
	
	/**
	 * Insert an engine into the exported database
	 * @param engine Engine Object to export
	 * @param session Hibernate session for the exported database
	 * @throws EMFUserError
	 */
	public void insertEngine(Engine engine, Session session) throws EMFUserError {
		try {
			Transaction tx = session.beginTransaction();
			Query hibQuery = session.createQuery(" from SbiEngines where engineId = " + engine.getId());
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				return;
			}
			SbiEngines hibEngine = new SbiEngines(engine.getId());
			hibEngine.setName(engine.getName());
			hibEngine.setLabel(engine.getLabel());
			hibEngine.setDescr(engine.getDescription());
			hibEngine.setDriverNm(engine.getDriverName());
			hibEngine.setEncrypt(new Short((short)engine.getCriptable().intValue()));
			hibEngine.setMainUrl(engine.getUrl());
			hibEngine.setObjUplDir(engine.getDirUpload());
			hibEngine.setObjUseDir(engine.getDirUsable());
			hibEngine.setSecnUrl(engine.getSecondaryUrl());
			session.save(hibEngine);
			tx.commit();
		} catch (Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "insertEngine",
					"Error while inserting engine into export database " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8005, "component_impexp_messages");
		}
	}
	
	
	/**
	 * Insert a biobject into the exported database
	 * @param biobj BIObject to export
	 * @param session Hibernate session for the exported database
	 * @throws EMFUserError
	 */
	public void insertBIObject(BIObject biobj, Session session) throws EMFUserError {
		try {
			Transaction tx = session.beginTransaction();
			Query hibQuery = session.createQuery(" from SbiObjects where biobjId = " + biobj.getId());
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				return;
			}
			Engine engine = biobj.getEngine();	
			SbiEngines hibEngine = (SbiEngines)session.load(SbiEngines.class, engine.getId());
			SbiDomains hibState = (SbiDomains)session.load(SbiDomains.class, biobj.getStateID());
			SbiDomains hibObjectType = (SbiDomains)session.load(SbiDomains.class, biobj.getBiObjectTypeID());
			SbiObjects hibBIObj = new SbiObjects(biobj.getId());
			hibBIObj.setSbiEngines(hibEngine); 
			hibBIObj.setDescr(biobj.getDescription());
			hibBIObj.setLabel(biobj.getLabel());
			hibBIObj.setName(biobj.getName());
			hibBIObj.setEncrypt(new Short(biobj.getEncrypt().shortValue()));
			hibBIObj.setRelName(biobj.getRelName());
			hibBIObj.setState(hibState);
			hibBIObj.setStateCode(biobj.getStateCode());
			hibBIObj.setObjectType(hibObjectType);
			hibBIObj.setObjectTypeCode(biobj.getBiObjectTypeCode());
			hibBIObj.setPath(biobj.getPath());
			session.save(hibBIObj);
			tx.commit();
		} catch (Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "insertBIObject",
					"Error while inserting biobject into export database " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8005, "component_impexp_messages");
		}
	}
	
	
	/**
	 * Insert a BIObject Parameter into the exported database
	 * @param biobjpar BIObject parameter to insert
	 * @param biobj BIObject to which the parameter belongs
	 * @param session Hibernate session for the exported database
	 * @throws EMFUserError
	 */
	public void insertBIObjectParameter(BIObjectParameter biobjpar, BIObject biobj, Session session) throws EMFUserError {
		/*
		try {
			Transaction tx = session.beginTransaction();
			Integer parid = biobjpar.getParameter().getId();
			Integer objid = biobj.getId();
			String query = " from SbiObjPar where id.sbiParameters.parId = " + parid +
						   " and id.sbiObjects.biobjId = " + objid;
			Query hibQuery = session.createQuery(query);
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				return;
			}
			// built key
			SbiObjParId hibBIObjParId = new SbiObjParId();
			SbiParameters hibParameter = (SbiParameters)session.load(SbiParameters.class, parid);
			SbiObjects hibBIObject = (SbiObjects)session.load(SbiObjects.class, objid);
			hibBIObjParId.setSbiObjects(hibBIObject);
			hibBIObjParId.setSbiParameters(hibParameter);
			hibBIObjParId.setProg(new Integer(0));
			// build BI Object Parameter
			SbiObjPar hibBIObjPar = new SbiObjPar(hibBIObjParId);
			hibBIObjPar.setLabel(biobjpar.getLabel());
			hibBIObjPar.setReqFl(new Short(biobjpar.getRequired().shortValue()));
			hibBIObjPar.setModFl(new Short(biobjpar.getModifiable().shortValue()));
			hibBIObjPar.setViewFl(new Short(biobjpar.getVisible().shortValue()));
			hibBIObjPar.setMultFl(new Short(biobjpar.getMultivalue().shortValue()));
			hibBIObjPar.setParurlNm(biobjpar.getParameterUrlName());
			// save the BI Object Parameter
			session.save(hibBIObjPar);
			tx.commit();
		} catch (Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "insertBIObjectParameter",
					               "Error while inserting BIObjectParameter into export database " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8005, "component_impexp_messages");
		}
		*/
	}
	
	
	
	
	
	/**
	 * Insert a parameter into the exported database
	 * @param param The param object to insert
	 * @param session Hibernate session for the exported database
	 * @throws EMFUserError
	 */
	public void insertParameter(Parameter param, Session session) throws EMFUserError {
		try {
			Transaction tx = session.beginTransaction();
			Query hibQuery = session.createQuery(" from SbiParameters where parId = " + param.getId());
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				return;
			}
			SbiDomains hibParamType = (SbiDomains)session.load(SbiDomains.class, param.getTypeId());
			SbiParameters hibParam = new SbiParameters(param.getId());
			hibParam.setDescr(param.getDescription());
			hibParam.setLength(new Short(param.getLength().shortValue()));
			hibParam.setLabel(param.getLabel());
			hibParam.setName(param.getName());
			hibParam.setParameterTypeCode(param.getType());
			hibParam.setMask(param.getMask());
			hibParam.setParameterType(hibParamType);
			session.save(hibParam);
			tx.commit();
		} catch (Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "insertParameter",
					"Error while inserting parameter into export database " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8005, "component_impexp_messages");
		}
	}
	
	
	
	/**
	 * Insert a parameter use into the exported database
	 * @param parUse The Parameter use object to export
	 * @param session Hibernate session for the exported database
	 * @throws EMFUserError
	 */
	public void insertParUse(ParameterUse parUse, Session session) throws EMFUserError {
		try {
			Transaction tx = session.beginTransaction();
			Query hibQuery = session.createQuery(" from SbiParuse where useId = " + parUse.getUseID());
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				return;
			}
			SbiParuse hibParuse = new SbiParuse(parUse.getUseID());
			// Set the relation with parameter
			SbiParameters hibParameters = (SbiParameters)session.load(SbiParameters.class, parUse.getId());
			hibParuse.setSbiParameters(hibParameters);
			// Set the relation with idLov 
			SbiLov hibLov = (SbiLov)session.load(SbiLov.class, parUse.getIdLov());
			hibParuse.setSbiLov(hibLov);
			hibParuse.setLabel(parUse.getLabel());
			hibParuse.setName(parUse.getName());
			hibParuse.setDescr(parUse.getDescription());
			session.save(hibParuse);
			tx.commit();
		} catch (Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "insertParUse",
					"Error while inserting parameter use into export database " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8005, "component_impexp_messages");
		}
	}
	
	
	
	
	/**
	 * Insert a list of value into the exported database
	 * @param lov The list of values object to export
	 * @param session Hibernate session for the exported database
	 * @throws EMFUserError
	 */
	public void insertLov(ModalitiesValue lov, Session session) throws EMFUserError {
		try {
			Transaction tx = session.beginTransaction();
			Query hibQuery = session.createQuery(" from SbiLov where lovId = " + lov.getId());
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				return;
			}
			SbiLov hibLov = new SbiLov(lov.getId());
			hibLov.setName(lov.getName());
			hibLov.setLabel(lov.getLabel());
			hibLov.setDescr(lov.getDescription());
			SbiDomains inpType = (SbiDomains)session.load(SbiDomains.class, new Integer(lov.getITypeId()));
			hibLov.setInputType(inpType);
			hibLov.setInputTypeCd(lov.getITypeCd());
			hibLov.setLovProvider(lov.getLovProvider());
			session.save(hibLov);
			tx.commit();
		} catch (Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "insertLov",
					"Error while inserting lov into export database " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8005, "component_impexp_messages");
		}
	}
	
	
	
	/**
	 * Insert a check into the exported database
	 * @param check The check object to export
	 * @param session Hibernate session for the exported database
	 * @throws EMFUserError
	 */
	public void insertCheck(Check check, Session session) throws EMFUserError {
		try {
			Transaction tx = session.beginTransaction();
			Query hibQuery = session.createQuery(" from SbiChecks where checkId = " + check.getCheckId());
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				return;
			}
			SbiDomains checkType = (SbiDomains)session.load(SbiDomains.class, check.getValueTypeId());
			SbiChecks hibCheck = new SbiChecks(check.getCheckId());
			hibCheck.setCheckType(checkType);
			hibCheck.setDescr(check.getDescription());
			hibCheck.setName(check.getName());
			hibCheck.setLabel(check.getLabel());
			hibCheck.setValue1(check.getFirstValue());
			hibCheck.setValue2(check.getSecondValue());
			hibCheck.setValueTypeCd(check.getValueTypeCd());
			session.save(hibCheck);
			tx.commit();
		} catch (Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "insertCheck",
					"Error while inserting check into export database " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8005, "component_impexp_messages");
		}
	}
	
	
	/**
	 * Insert an association between a parameter use and a check into the exported database
	 * @param parUse The paruse object which is an element of the association
	 * @param check The check object which is an element of the association
	 * @param session Hibernate session for the exported database
	 * @throws EMFUserError
	 */
	public void insertParuseCheck(ParameterUse parUse, Check check, Session session) throws EMFUserError {
		try {
			Transaction tx = session.beginTransaction();
			Integer paruseId = parUse.getUseID();
			Integer checkId = check.getCheckId();
			String query = " from SbiParuseCk where id.sbiParuse.useId = " + paruseId +
						   " and id.sbiChecks.checkId = " + checkId;
			Query hibQuery = session.createQuery(query);
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				return;
			}
			// built key
			SbiParuseCkId hibParuseCkId = new SbiParuseCkId();
			SbiChecks hibChecks = (SbiChecks)session.load(SbiChecks.class, check.getCheckId());
			SbiParuse hibParuse = (SbiParuse)session.load(SbiParuse.class, parUse.getUseID());
			hibParuseCkId.setSbiChecks(hibChecks);
			hibParuseCkId.setSbiParuse(hibParuse);
			SbiParuseCk hibParuseCheck = new SbiParuseCk(hibParuseCkId);
			hibParuseCheck.setProg(new Integer(0));
			session.save(hibParuseCheck);
			tx.commit();
		} catch (Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "insertParuseCheck",
					               "Error while inserting paruse and check association into export database " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8005, "component_impexp_messages");
		}
	}
	
	
	
	/**
	 * Insert an association between a parameter use and a role into the exported database
	 * @param parUse The paruse object which is an element of the association
	 * @param role The role object which is an element of the association
	 * @param session Hibernate session for the exported database
	 * @throws EMFUserError
	 */
	public void insertParuseRole(ParameterUse parUse, Role role, Session session) throws EMFUserError {
		try {
			Transaction tx = session.beginTransaction();
			Integer paruseId = parUse.getUseID();
			Integer roleId = role.getId();
			String query = " from SbiParuseDet where id.sbiParuse.useId = " + paruseId +
						   " and id.sbiExtRoles.extRoleId = " + roleId;
			Query hibQuery = session.createQuery(query);
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				return;
			}
			// built key
			SbiParuseDetId hibParuseDetId = new SbiParuseDetId();
			SbiParuse hibParuse = (SbiParuse)session.load(SbiParuse.class, parUse.getUseID());
			SbiExtRoles hibExtRole = (SbiExtRoles)session.load(SbiExtRoles.class, role.getId());
			hibParuseDetId.setSbiExtRoles(hibExtRole);
			hibParuseDetId.setSbiParuse(hibParuse);
			SbiParuseDet hibParuseDet = new SbiParuseDet(hibParuseDetId);
			session.save(hibParuseDet);
			tx.commit();
		} catch (Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "insertParuseDet",
					               "Error while inserting paruse and role association into export database " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8005, "component_impexp_messages");
		}
	}
	
	
	
	/**
	 * Insert a functionality into the exported database
	 * @param funct Functionality Object to export
	 * @param session Hibernate session for the exported database
	 * @throws EMFUserError
	 */
	public void insertFunctionality(LowFunctionality funct, Session session) throws EMFUserError {
		try {
			Transaction tx = session.beginTransaction();
			Query hibQuery = session.createQuery(" from SbiFunctions where funct_id = " + funct.getId());
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				return;
			}
			IDomainDAO domDAO = DAOFactory.getDomainDAO();
			Domain functTypeDom = domDAO.loadDomainByCodeAndValue("FUNCT_TYPE", funct.getCodType());
			SbiDomains hibFunctType = (SbiDomains)session.load(SbiDomains.class, functTypeDom.getValueId());
			
			SbiFunctions hibFunct = new SbiFunctions(funct.getId());
			hibFunct.setCode(funct.getCode());
			hibFunct.setDescr(funct.getDescription());
			hibFunct.setFunctTypeCd(funct.getCodType());
			hibFunct.setFunctType(hibFunctType);
			hibFunct.setName(funct.getName());
			hibFunct.setPath(funct.getPath());
			session.save(hibFunct);
			tx.commit();
		} catch (Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "insertFunctionality",
					"Error while inserting Functionality into export database " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8005, "component_impexp_messages");
		}
	}
	
	
	
	
	
	/**
	 * Insert a role into the exported database
	 * @param role The role object to export
	 * @param session Hibernate session for the exported database
	 * @throws EMFUserError
	 */
	public void insertRole(Role role, Session session) throws EMFUserError {
		try {
			Transaction tx = session.beginTransaction();
			Query hibQuery = session.createQuery(" from SbiExtRoles where extRoleId = " + role.getId());
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				return;
			}
			SbiExtRoles hibRole = new SbiExtRoles(role.getId());
			hibRole.setCode(role.getCode());
			hibRole.setDescr(role.getDescription());
			hibRole.setName(role.getName());
			SbiDomains roleType = (SbiDomains)session.load(SbiDomains.class, role.getRoleTypeID());
			hibRole.setRoleType(roleType);
			hibRole.setRoleTypeCode(role.getRoleTypeCD());
			session.save(hibRole);
			tx.commit();
		} catch (Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "insertRole",
					"Error while inserting role into export database " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8005, "component_impexp_messages");
		}
	}
	
	
	
	/**
	 * Insert an association between a functionality and a role into the exported database
	 * @param role The role object which is an element of the association
	 * @param funct The functionality object which is an element of the association
	 * @param stateId The id of the State associated to the couple role / functionality
	 * @param stateCD The code of the State associated to the couple role / functionality
	 * @param session Hibernate session for the exported database
	 * @throws EMFUserError
	 */
	public void insertFunctRole(Role role, LowFunctionality funct, Integer stateId, String stateCD, Session session) throws EMFUserError {
		try {
			Transaction tx = session.beginTransaction();
			Integer roleid = role.getId();
			Integer functid = funct.getId();
			String query = " from SbiFuncRole where id.function = " + functid +
						   " and id.role = " + roleid + " and id.state = " + stateId ;
			Query hibQuery = session.createQuery(query);
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				return;
			}
			// built key
			SbiFuncRoleId hibFuncRoleId = new SbiFuncRoleId();
			SbiFunctions hibFunct = (SbiFunctions)session.load(SbiFunctions.class, funct.getId());
			SbiExtRoles hibRole = (SbiExtRoles)session.load(SbiExtRoles.class, role.getId());
			SbiDomains hibState = (SbiDomains)session.load(SbiDomains.class, stateId);
			hibFuncRoleId.setFunction(hibFunct);
			hibFuncRoleId.setRole(hibRole);
			hibFuncRoleId.setState(hibState);
			SbiFuncRole hibFunctRole = new SbiFuncRole(hibFuncRoleId);
			hibFunctRole.setStateCd(stateCD);
			session.save(hibFunctRole);
			tx.commit();
		} catch (Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "insertFunctRole",
					               "Error while inserting function and role association into export database " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8005, "component_impexp_messages");
		}
	}
	
	
	
	
	
}
