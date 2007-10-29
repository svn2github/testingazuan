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

import it.eng.spago.base.SourceBeanException;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.Engine;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectCMSDAO;
import it.eng.spagobi.bo.lov.QueryDetail;
import it.eng.spagobi.metadata.SbiChecks;
import it.eng.spagobi.metadata.SbiDomains;
import it.eng.spagobi.metadata.SbiEngines;
import it.eng.spagobi.metadata.SbiExtRoles;
import it.eng.spagobi.metadata.SbiFuncRole;
import it.eng.spagobi.metadata.SbiFunctions;
import it.eng.spagobi.metadata.SbiLov;
import it.eng.spagobi.metadata.SbiObjFunc;
import it.eng.spagobi.metadata.SbiObjPar;
import it.eng.spagobi.metadata.SbiObjParuse;
import it.eng.spagobi.metadata.SbiObjects;
import it.eng.spagobi.metadata.SbiParameters;
import it.eng.spagobi.metadata.SbiParuse;
import it.eng.spagobi.metadata.SbiParuseCk;
import it.eng.spagobi.metadata.SbiParuseDet;
import it.eng.spagobi.metadata.SbiSubreports;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Implements methods to gather information from exported database 
 * and to make some checks into the current SpagoBI database
 */
public class ImporterMetadata {

		/**
		 * Get the list of exported hibernate role objects 
		 * @param tx Hiberante transaction for the exported database
		 * @param session Hiberante session for the exported database
		 * @return The list of exported hibernate roles
		 * @throws EMFUserError
		 */
		public List getAllExportedRoles(Transaction tx, Session session) throws EMFUserError {
			List roles = new ArrayList();
			try {
				Query hibQuery = session.createQuery(" from SbiExtRoles");
				List hibList = hibQuery.list();
				Iterator it = hibList.iterator();
				while(it.hasNext()) {
					SbiExtRoles hibRole = (SbiExtRoles)it.next();
					Role role = new Role();
					role.setCode(hibRole.getCode());
					role.setDescription(hibRole.getDescr());
					role.setId(hibRole.getExtRoleId());
					role.setName(hibRole.getName());
					role.setRoleTypeCD(hibRole.getRoleTypeCode());
					role.setRoleTypeID(hibRole.getRoleType().getValueId());
					roles.add(role);
				}
			} catch (HibernateException he) {
				SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "getAllExportedRoles",
						               "Error while getting exported roles " + he);
				throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
			} 
			return roles;
		}
		
		
		/**
		 * Get the list of exported hibernate engine objects 
		 * @param tx Hibernate transaction for the exported database
		 * @param session Hibernate session for the exported database
		 * @return The list of exported hibernate engines
		 * @throws EMFUserError
		 */
		public List getAllExportedEngines(Transaction tx, Session session) throws EMFUserError {
			List engines = new ArrayList();
			try {
				Query hibQuery = session.createQuery(" from SbiEngines");
				List hibList = hibQuery.list();
				Iterator it = hibList.iterator();
				while(it.hasNext()) {
					SbiEngines hibEngine = (SbiEngines)it.next();
					Engine eng = new Engine();
					eng.setCriptable(new Integer(hibEngine.getEncrypt().intValue()));
					eng.setDescription(hibEngine.getDescr());
					eng.setDirUpload(hibEngine.getObjUplDir());
					eng.setDirUsable(hibEngine.getObjUseDir());
					eng.setDriverName(hibEngine.getDriverNm());
					eng.setId(hibEngine.getEngineId());
					eng.setName(hibEngine.getName());
					eng.setLabel(hibEngine.getLabel());
					eng.setSecondaryUrl(hibEngine.getSecnUrl());
					eng.setUrl(hibEngine.getMainUrl());
					eng.setLabel(hibEngine.getLabel());
					eng.setClassName(hibEngine.getClassNm());
					engines.add(eng);
				}
			} catch (HibernateException he) {
				SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "getAllExportedEngines",
						               "Error while getting exported engine " + he);
				throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
			} 
			return engines;
		}
		
		
		/**
		 * Get the list of exported hibernate objects
		 * @param tx Hibernate transaction for the exported database
		 * @param session Hibernate session for the exported database
		 * @param table The name of the table corresponding to the hibernate objects to gather
		 * @return The list of exported hibernate objects
		 * @throws EMFUserError
		 */		
		public List getAllExportedSbiObjects(Transaction tx, Session session, String table) throws EMFUserError {
			List hibList = null;
			try {
				Query hibQuery = session.createQuery(" from " + table);
				hibList = hibQuery.list();
			} catch (HibernateException he) {
				SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "getAllExportedSbiObjects",
						               "Error while getting exported sbi objects " + he);
				throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
			} 
			return hibList;
		}
		
		
		/**
		 * Get an existing object identified by the id and the class
		 * @param id The Object id
		 * @param objClass The class of the object
		 * @param tx Hibernate transaction for a database
		 * @param session Hibernate session for a database
		 * @return The existing hibernate object
		 */		
		public Object getObject(Integer id, Class objClass, Transaction tx, Session session) {
			Object hibObject = null;
			try {
				hibObject = session.load(objClass, id);
			} catch (HibernateException he) {
				SpagoBITracer.major(ImportExportConstants.NAME_MODULE, this.getClass().getName(), 
						           "getExistingObject",
						           "Error while getting the existing object with class "+objClass.getName()+" " +
						           "and id " +id+ " \n " + he);
			} 
			return hibObject;
		}
		
			
		
		/**
		 * Upadates the connection name into the query lov objects based on the 
		 * assocaition defined by the user between exported and current SpagoBI connection
		 * @param associations Map of associations between exported connections 
		 * and connections of the current SpagoBI platform
		 * @param tx Hibernate transaction for the exported database
		 * @param session Hibernate session for the exported database
		 * @throws EMFUserError
		 */
		public void updateConnRefs(Map associations, Transaction tx, Session session, 
				                   MetadataLogger log) throws EMFUserError {
			try {
				List lovs = getAllExportedSbiObjects(tx, session, "SbiLov");
				Iterator iterLovs = lovs.iterator();
				while(iterLovs.hasNext()) {
					SbiLov lov = (SbiLov)iterLovs.next();
					if(lov.getInputTypeCd().equalsIgnoreCase("QUERY")) {
						String lovProv = lov.getLovProvider();
						QueryDetail qDet = QueryDetail.fromXML(lovProv);
						String oldConnName = qDet.getConnectionName();
						String assConnName = (String) associations.get(oldConnName);
						
						// register user association 
						if( (assConnName != null) && 
							!assConnName.trim().equals("") &&
							(oldConnName != null) && 
							!oldConnName.trim().equals("") ) {
							
							qDet.setConnectionName(assConnName);
							lovProv = qDet.toXML();
							lov.setLovProvider(lovProv);
							session.save(lov);
							log.log("Changed the connection name from "+oldConnName+" to " +
									assConnName + " for the lov " + lov.getName());
						} 			
					}
				}
			} catch (SourceBeanException sbe) {
				SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "updateConnRefs",
									   "Error while updating connection references " + sbe);
				throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
			}	 
		}
		
		
		/**
		 * Insert a generic Hibernate object into the exported database
		 * @param hibObj The object to insert
		 * @param session Hibernate session for the exported database
		 * @throws EMFUserError
		 */
		public void insertObject(Object hibObj, Session session) throws EMFUserError {
			try{
				Serializable serId = session.save(hibObj);
			} catch (HibernateException he) {
				SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "insertObject",
                        			   "Error while inserting object " + he);
				throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
			} catch (Exception e){
				SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "insertObject",
										"Error while inserting object " + e);
				throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
			}
		}
		
		
		
		/**
		 * Insert a BIObject into the database and cms repository
		 * @param obj The Hiberante BIObject to insert
		 * @param pathContent The path of the temporary contents directory
		 * @param session Hibernate session for the exported database
		 * @return The Hibernate BIObject inserted
		 * @throws EMFUserError
		 */
		public SbiObjects insertBIObject(SbiObjects obj, String pathContent, Session session) throws EMFUserError {
			IBIObjectCMSDAO cmsdao = DAOFactory.getBIObjectCMSDAO();
			SbiObjects objToReturn = null;
			try {
				String pathTempFolder = pathContent + obj.getPath();
				// normalize path
				File temp = new File(pathTempFolder);
				pathTempFolder = temp.getAbsolutePath();
				String newPath = cmsdao.importDocument(pathTempFolder);
				obj.setPath(newPath);
				insertObject(obj, session);
				objToReturn = obj;
			} catch (Exception e) {
					SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "insertBIObject",
             			   				   "Error while inserting business objects " + e);
					throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
			}
			return objToReturn;
		}
		
		
		
		
		/**
		 * Check the existance of an object, based on his unique constraints, 
		 * into the current SpagoBI database
		 * @param unique The object which contains the unique constraints for the object
		 * @param sessionCurrDB Hibernate session for the current SpagoBI database
		 * @param hibObj An empty object usefull to identify  the kind of object to analize
		 * @return The existing Object or null if it doesn't exist 
		 * @throws EMFUserError
		 */
		public Object checkExistence(Object unique, Session sessionCurrDB, Object hibObj) throws EMFUserError {
			String hql = null;
			Query hqlQuery = null;
			if(hibObj instanceof SbiParameters) {
				String label = (String)unique;
				hql = "from SbiParameters sp where sp.label = '" + label + "'";
				hqlQuery = sessionCurrDB.createQuery(hql);
				SbiParameters hibPar = (SbiParameters)hqlQuery.uniqueResult();
				return hibPar;
			} else if(hibObj instanceof SbiExtRoles) {
				String roleName = (String)unique;
				hql = "from SbiExtRoles er where er.name = '" + roleName + "'";
				hqlQuery = sessionCurrDB.createQuery(hql);
				SbiExtRoles hibRole = (SbiExtRoles)hqlQuery.uniqueResult();
				return hibRole;
			} else if(hibObj instanceof SbiObjects) {
				String label = (String)unique;
				hql = "from SbiObjects obj where obj.label = '" + label + "'";
				hqlQuery = sessionCurrDB.createQuery(hql);
				SbiObjects hibBIObj = (SbiObjects)hqlQuery.uniqueResult();
				return hibBIObj;
			} else if(hibObj instanceof SbiLov) {
				String label = (String)unique;
				hql = "from SbiLov lov where lov.label = '" + label + "'";
				hqlQuery = sessionCurrDB.createQuery(hql);
				SbiLov hibLov = (SbiLov)hqlQuery.uniqueResult();
				return hibLov;
			} else if(hibObj instanceof SbiFunctions) {
				String code = (String)unique;
				hql = "from SbiFunctions f where f.code = '" + code + "'";
				hqlQuery = sessionCurrDB.createQuery(hql);
				SbiFunctions hibFunct = (SbiFunctions)hqlQuery.uniqueResult();
				return hibFunct;
			} else if(hibObj instanceof SbiEngines) {
				String label = (String)unique;
				hql = "from SbiEngines eng where eng.label = '" + label + "'";
				hqlQuery = sessionCurrDB.createQuery(hql);
				SbiEngines hibEng = (SbiEngines)hqlQuery.uniqueResult();
				return hibEng;
			} else if(hibObj instanceof SbiChecks) {
				String label = (String)unique;
				hql = "from SbiChecks ch where ch.label = '" + label + "'";
				hqlQuery = sessionCurrDB.createQuery(hql);
				SbiChecks hibCheck = (SbiChecks)hqlQuery.uniqueResult();
				return hibCheck;
			} else if(hibObj instanceof SbiParuse) {
				Map uniqueMap = (Map)unique;
				String label = (String)uniqueMap.get("label");
				Integer parid = (Integer)uniqueMap.get("idpar");
				hql = "from SbiParuse pu where pu.label='"+label+"' and pu.sbiParameters.parId = " + parid;
				hqlQuery = sessionCurrDB.createQuery(hql);
				SbiParuse hibParuse = (SbiParuse)hqlQuery.uniqueResult();
				return hibParuse;
			} else if(hibObj instanceof SbiFuncRole) {
				Map uniqueMap = (Map)unique;
				Integer stateid = (Integer)uniqueMap.get("stateid");
				Integer roleid = (Integer)uniqueMap.get("roleid");
				Integer functionid = (Integer)uniqueMap.get("functionid");
				hql = "from SbiFuncRole fr where fr.id.state.valueId=" + stateid + 
					  " and fr.id.role.extRoleId = " + roleid +
					  " and fr.id.function.functId = " + functionid;
				hqlQuery = sessionCurrDB.createQuery(hql);
				SbiFuncRole hibFunctRole = (SbiFuncRole)hqlQuery.uniqueResult();
				return hibFunctRole;
			} else if(hibObj instanceof SbiParuseDet) {
				Map uniqueMap = (Map)unique;
				Integer paruseid = (Integer)uniqueMap.get("paruseid");
				Integer roleid = (Integer)uniqueMap.get("roleid");
				hql = "from SbiParuseDet pud where pud.id.sbiExtRoles.extRoleId = " + roleid +
					  " and pud.id.sbiParuse.useId = " + paruseid;
				hqlQuery = sessionCurrDB.createQuery(hql);
				SbiParuseDet hibParuseDet = (SbiParuseDet)hqlQuery.uniqueResult();
				return hibParuseDet;
			} else if(hibObj instanceof SbiParuseCk) {
				Map uniqueMap = (Map)unique;
				Integer paruseid = (Integer)uniqueMap.get("paruseid");
				Integer checkid = (Integer)uniqueMap.get("checkid");
				hql = "from SbiParuseCk puc where puc.id.sbiChecks.checkId = " + checkid +
					  " and puc.id.sbiParuse.useId = " + paruseid;
				hqlQuery = sessionCurrDB.createQuery(hql);
				SbiParuseCk hibParuseCk = (SbiParuseCk)hqlQuery.uniqueResult();
				return hibParuseCk;
			} else if(hibObj instanceof SbiObjPar) {
				Map uniqueMap = (Map)unique;
				Integer paramid = (Integer)uniqueMap.get("paramid");
				Integer biobjid = (Integer)uniqueMap.get("biobjid");
				String urlname = (String)uniqueMap.get("urlname");
				hql = "from SbiObjPar op where op.sbiParameter.parId = " + paramid +
					  " and op.sbiObject.biobjId = " + biobjid + 
				      " and op.parurlNm = '" + urlname + "'";
				hqlQuery = sessionCurrDB.createQuery(hql);
				SbiObjPar hibObjPar = (SbiObjPar)hqlQuery.uniqueResult();
				return hibObjPar;
			} else if(hibObj instanceof SbiDomains) {
				Map uniqueMap = (Map)unique;
				String valuecd = (String)uniqueMap.get("valuecd");
				String domaincd = (String)uniqueMap.get("domaincd");
				hql = "from SbiDomains dom where dom.valueCd='"+valuecd+"' and dom.domainCd='"+domaincd+"'";
				hqlQuery = sessionCurrDB.createQuery(hql);
				SbiDomains hibDom = (SbiDomains)hqlQuery.uniqueResult();
				return hibDom;
			} else if(hibObj instanceof SbiObjFunc) {
				Map uniqueMap = (Map)unique;
				Integer objid = (Integer)uniqueMap.get("objectid");
				Integer functionid = (Integer)uniqueMap.get("functionid");
				hql = "from SbiObjFunc objfun where objfun.id.sbiObjects.biobjId = " + objid +
					  " and objfun.id.sbiFunctions.functId = " + functionid;
				hqlQuery = sessionCurrDB.createQuery(hql);
				SbiObjFunc hibObjFunct = (SbiObjFunc)hqlQuery.uniqueResult();
				return hibObjFunct;
			} else if(hibObj instanceof SbiSubreports) {
				Map uniqueMap = (Map)unique;
				Integer masterid = (Integer)uniqueMap.get("masterid");
				Integer subid = (Integer)uniqueMap.get("subid");
				hql = "from SbiSubreports subreport where subreport.id.masterReport.biobjId = " + masterid +
					  " and subreport.id.subReport.biobjId = " + subid;
				hqlQuery = sessionCurrDB.createQuery(hql);
				SbiSubreports hibSubRep = (SbiSubreports)hqlQuery.uniqueResult();
				return hibSubRep;
			} else if(hibObj instanceof SbiObjParuse) {
				Map uniqueMap = (Map)unique;
				Integer objparid = (Integer)uniqueMap.get("objparid");
				Integer paruseid = (Integer)uniqueMap.get("paruseid");
				Integer objparfathid = (Integer)uniqueMap.get("objparfathid");
				String filterOp = (String)uniqueMap.get("filterop");
				hql = "from SbiObjParuse objparuse where objparuse.id.sbiObjPar.objParId = " + objparid +
					  " and objparuse.id.sbiParuse.useId = " + paruseid + 
				      " and objparuse.id.sbiObjParFather.objParId = " + objparfathid + 
			          " and objparuse.id.filterOperation = '" + filterOp + "'";
				hqlQuery = sessionCurrDB.createQuery(hql);
				SbiObjParuse hibObjParUse = (SbiObjParuse)hqlQuery.uniqueResult();
				return hibObjParUse;
			} 
			
			return null;
		}
		
}

