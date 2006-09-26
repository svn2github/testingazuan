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
package it.eng.spagobi.bo.dao.hibernate;

import it.eng.spago.base.PortletAccess;
import it.eng.spago.base.SourceBean;
import it.eng.spago.cms.CmsManager;
import it.eng.spago.cms.CmsNode;
import it.eng.spago.cms.CmsProperty;
import it.eng.spago.cms.CmsVersion;
import it.eng.spago.cms.exceptions.BuildOperationException;
import it.eng.spago.cms.exceptions.OperationExecutionException;
import it.eng.spago.cms.operations.DeleteOperation;
import it.eng.spago.cms.operations.GetOperation;
import it.eng.spago.cms.operations.RestoreOperation;
import it.eng.spago.cms.operations.SetOperation;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.ObjParuse;
import it.eng.spagobi.bo.Parameter;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.bo.TemplateVersion;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectDAO;
import it.eng.spagobi.bo.dao.IBIObjectParameterDAO;
import it.eng.spagobi.bo.dao.IObjParuseDAO;
import it.eng.spagobi.bo.dao.IParameterDAO;
import it.eng.spagobi.bo.dao.ISubreportDAO;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.metadata.SbiDomains;
import it.eng.spagobi.metadata.SbiEngines;
import it.eng.spagobi.metadata.SbiFunctions;
import it.eng.spagobi.metadata.SbiObjFunc;
import it.eng.spagobi.metadata.SbiObjFuncId;
import it.eng.spagobi.metadata.SbiObjPar;
import it.eng.spagobi.metadata.SbiObjects;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeMap;

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

	/** 
	 * @see it.eng.spagobi.bo.dao.IBIObjectDAO#loadBIObjectForExecutionByIdAndRole(java.lang.Integer, java.lang.String)
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
					SpagoBITracer.minor(SpagoBIConstants.NAME_MODULE, 
						    "BIObjectDAOHibImpl", 
						    "loadBIObjectForExecutionByPathAndRole", 
						    "The priorities of the biparameters for the document with id = " + biObject.getId() + " are not sorted. Priority recalculation starts.");
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
			logException(he);
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
	 * @see it.eng.spagobi.bo.dao.IBIObjectDAO#loadBIObjectById(java.lang.Integer)
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
	 * @see it.eng.spagobi.bo.dao.IBIObjectDAO#loadBIObjectForDetail(java.lang.Integer)
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
			gatherCMSInformation(biObject);
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
		return biObject;
	}
	
	/** 
	 * @see it.eng.spagobi.bo.dao.IBIObjectDAO#loadBIObjectByLabel(java.lang.String)
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
			logException(he);
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
	 * Load the CMS Information of the biObject
	 * 
	 * @param biObject
	 */
	public void gatherCMSInformation(BIObject biObject) throws EMFUserError {
		
		try {
			GetOperation getOp = new GetOperation();
			String path = biObject.getPath() + "/template";
			getOp.setPath(path);
			getOp.setRetriveContentInformation("true");
			getOp.setRetrivePropertiesInformation("true");
			getOp.setRetriveVersionsInformation("true");
			getOp.setRetriveChildsInformation("false");
            CmsManager manager = new CmsManager();
			CmsNode cmsnode = manager.execGetOperation(getOp);
			if(cmsnode != null){
				String currentVerStr = cmsnode.getVersion();
				List versions = cmsnode.getVersions();
				Iterator iterVer = versions.iterator();
				TreeMap templates = new TreeMap();
				TemplateVersion currentVer = null;
				while(iterVer.hasNext()) {
					CmsVersion ver = (CmsVersion)iterVer.next();
					String nameVer = ver.getName();
					// retrive version
					getOp.setVersion(nameVer);
					CmsNode cmsnodever = manager.execGetOperation(getOp);
					List properties = cmsnodever.getProperties();
					Iterator iterProps = properties.iterator();
					String nameFile = "";
					String dateLoadStr = "";
					while(iterProps.hasNext()) {
						CmsProperty prop = (CmsProperty)iterProps.next();
						String nameProp = prop.getName();
						if(nameProp.equalsIgnoreCase("fileName")) 
							nameFile = prop.getStringValues()[0];
						if(nameProp.equalsIgnoreCase("dateLoad")) 
							dateLoadStr = prop.getStringValues()[0];
					}
					
					// instance templateVersion object and set version name, date and file name
					TemplateVersion tempVer = new TemplateVersion();
					tempVer.setVersionName(nameVer);
					tempVer.setNameFileTemplate(nameFile);
					// the date is stored as a long number, but the template contains the formatted date string
					Long dateLong = null;
					Date date = null;
					try{
						dateLong = new Long(dateLoadStr);
						date = new Date(dateLong.longValue());
					} catch (Exception e) {
						// compatibility towards version < 1.9
						try{
							SimpleDateFormat dateForm = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
							date = dateForm.parse(dateLoadStr);
							dateLong = new Long(date.getTime());
						} catch(Exception ee) {
							dateLong = new Long(0);
						}
					}
					
					// format the date
					Locale portalLoc = PortletAccess.getPortalLocale();
					String dateFormatted = "";
					if(dateLong.longValue() != 0) {
						SimpleDateFormat dateForm = null;
						if(portalLoc!=null) {
							dateForm = new SimpleDateFormat("d MMM yyyy HH:mm:ss", portalLoc);
						} else { 
							dateForm = new SimpleDateFormat("d MMM yyyy HH:mm:ss");
						}
						dateFormatted = dateForm.format(date);
					} else {
						dateFormatted = " ";
					}

                   
					tempVer.setDataLoad(dateFormatted);
					templates.put(dateLong, tempVer);
					if(nameVer.equalsIgnoreCase(currentVerStr)) {
						currentVer = tempVer;
					}
				}
				biObject.setTemplateVersions(templates);
				biObject.setCurrentTemplateVersion(currentVer);
			}else{
				biObject.setTemplateVersions(new TreeMap());
				TemplateVersion tv = new TemplateVersion();
				tv.setVersionName("");
				biObject.setCurrentTemplateVersion(tv);
			}
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "BIObjectDAOImpl",
				"loadByPath", "Cannot recover detail information", ex);
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
		
	}
	
	
	
	
	
	
	/**
	 * @see it.eng.spagobi.bo.dao.IBIObjectDAO#loadBIObjectForTree(java.lang.Integer)
	 */
	public BIObject loadBIObjectForTree(Integer id) throws EMFUserError {
		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, "BIObjectDAOImpl",
							"loadBIObjectForTree", "start method with input id:" + id);
		BIObject biObject = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, "BIObjectDAOImpl",
					"loadBIObjectForTree", "hibernate session obtained:" + aSession);
			tx = aSession.beginTransaction();
			SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, "BIObjectDAOImpl",
					"loadBIObjectForTree", "hibernate transaction started");
			Criterion domainCdCriterrion = Expression.eq("biobjId", id);
			Criteria criteria = aSession.createCriteria(SbiObjects.class);
			criteria.add(domainCdCriterrion);
			SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, "BIObjectDAOImpl",
					"loadBIObjectForTree", "hibernate criteria filled:" + criteria);
			SbiObjects hibObject = (SbiObjects) criteria.uniqueResult();
			SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, "BIObjectDAOImpl",
					"loadBIObjectForTree", "hibernate object retrived:" + hibObject);
			if (hibObject == null) {
				return null;
			}
			biObject = toBIObject(hibObject);
			tx.commit();
		} catch (HibernateException he) {
			logException(he);
			if (tx != null)
				tx.rollback();
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, "BIObjectDAOImpl",
					"loadBIObjectForTree", "hibernate exception:" + he);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, "BIObjectDAOImpl",
				"loadBIObjectForTree", "end method with input id:" + id);
		return biObject;	
	}

	
	
	
	
	
	/**
	 * @see it.eng.spagobi.bo.dao.IBIObjectDAO#modifyBIObject(it.eng.spagobi.bo.BIObject)
	 */
	public void modifyBIObject(BIObject obj) throws EMFUserError {
		internalModify(obj, true);
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IBIObjectDAO#modifyBIObjectWithoutVersioning(it.eng.spagobi.bo.BIObject)
	 */
	public void modifyBIObjectWithoutVersioning(BIObject obj)
			throws EMFUserError {
		internalModify(obj, false);

	}
	
	/**
	 * Updates the CMS repository if the user has loaded a file template. If he has,
	 * the boolean input parameter <code>version</code> is true and the CNS is uploaded,
	 * else not.
	 * 
	 * @param biObject The BI Object as input
	 * @param version The boolean input parameter
	 * @throws EMFUserError If any exception occurred
	 */
	private void internalModify(BIObject biObject, boolean version)
			throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiObjects hibBIObject = (SbiObjects) aSession.load(
					SbiObjects.class, biObject.getId());
			SbiEngines hibEngine = (SbiEngines) aSession.load(SbiEngines.class,
					biObject.getEngine().getId());
			hibBIObject.setSbiEngines(hibEngine); // TO REVIEW
			hibBIObject.setDescr(biObject.getDescription());
			hibBIObject.setLabel(biObject.getLabel());
			hibBIObject.setName(biObject.getName());
			hibBIObject.setEncrypt(new Short(biObject.getEncrypt().shortValue()));
			hibBIObject.setVisible(new Short(biObject.getVisible().shortValue()));
			
			hibBIObject.setRelName(biObject.getRelName());
			SbiDomains hibState = (SbiDomains) aSession.load(SbiDomains.class,
					biObject.getStateID());
			hibBIObject.setState(hibState);
			hibBIObject.setStateCode(biObject.getStateCode());
			SbiDomains hibObjectType = (SbiDomains) aSession.load(
					SbiDomains.class, biObject.getBiObjectTypeID());
			hibBIObject.setObjectType(hibObjectType);
			hibBIObject.setObjectTypeCode(biObject.getBiObjectTypeCode());
//			hibBIObject.setPath(biObject.getPath());
			// Copiato dall'altro codice
//			RequestContainer requestContainer = RequestContainer
//					.getRequestContainer();
//			SessionContainer session = requestContainer.getSessionContainer();
//			SessionContainer permSession = session.getPermanentContainer();
//			IEngUserProfile profile = (IEngUserProfile) permSession
//					.getAttribute(IEngUserProfile.ENG_USER_PROFILE);

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
			
			
			CmsManager manager = new CmsManager();
			
			if (version) {
				// if user has load a file template update the cms reporitory
				if(biObject.getTemplate().getFileContent().length > 0) {
					// controls that the relevant node in CMS exists; if the node does not exist, it is created
					GetOperation getOp = new GetOperation();
					getOp.setPath(hibBIObject.getPath());
					getOp.setRetriveContentInformation("false");
					getOp.setRetrivePropertiesInformation("false");
					getOp.setRetriveVersionsInformation("false");
					getOp.setRetriveChildsInformation("false");
					CmsNode cmsnode = manager.execGetOperation(getOp);
					if (cmsnode == null) {
						// if the node does not exist in CMS, it is created
						SetOperation setOp = new SetOperation();
						setOp.setPath(hibBIObject.getPath());
						setOp.setType(SetOperation.TYPE_CONTAINER);
						setOp.setEraseOldProperties(true);
						// define properties
						List properties = new ArrayList();
						String[] typePropValues = new String[] { biObject.getBiObjectTypeCode() };
						CmsProperty proptype = new CmsProperty(AdmintoolsConstants.NODE_CMS_TYPE, typePropValues);
						properties.add(proptype);
						setOp.setProperties(properties);
						manager.execSetOperation(setOp);
					}
					SetOperation setOp = new SetOperation();
					setOp.setContent(new ByteArrayInputStream(biObject.getTemplate().getFileContent()));
					setOp.setType(SetOperation.TYPE_CONTENT);
					String path = hibBIObject.getPath() + "/template";
					setOp.setPath(path);
					// define properties list
					List properties = new ArrayList();
					String[] nameFilePropValues = new String[] { biObject.getTemplate().getFileName() };
					String today = new Long(new Date().getTime()).toString();
					String[] datePropValues = new String[] { today };
					CmsProperty propFileName = new CmsProperty("fileName", nameFilePropValues);
					CmsProperty propDateLoad = new CmsProperty("dateLoad", datePropValues);
					properties.add(propFileName);
					properties.add(propDateLoad);
                    setOp.setProperties(properties);
                    // exec operation
					manager.execSetOperation(setOp);
				} else if (biObject.getNameCurrentTemplateVersion() != null) {
					GetOperation getOp = new GetOperation();
					getOp.setPath(hibBIObject.getPath() + "/template" );
					getOp.setRetriveChildsInformation("false");
					getOp.setRetriveContentInformation("false");
					getOp.setRetrivePropertiesInformation("false");
					getOp.setRetriveVersionsInformation("false");
					CmsNode cmsnode = manager.execGetOperation(getOp);
					String verName = cmsnode.getVersion();
					if(!biObject.getNameCurrentTemplateVersion().equals(verName)) {
						RestoreOperation resOp = new RestoreOperation();
						resOp.setPath(hibBIObject.getPath() + "/template");
						resOp.setVersion(biObject.getNameCurrentTemplateVersion());
						manager.execRestoreOperation(resOp);
					}
				}
			}
			// Clear bytes in memory
			biObject.setTemplate(null);
			tx.commit();
		} catch (OperationExecutionException oe) {

			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE,
					"BIObjectDAOImpl", "internalModify",
					"Cannot recover detail information", oe);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} catch (BuildOperationException boe) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE,
					"BIObjectDAOImpl", "internalModify",
					"Cannot recover detail information", boe);

			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}  catch (HibernateException he) {
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
	 * @see it.eng.spagobi.bo.dao.IBIObjectDAO#insertBIObject(it.eng.spagobi.bo.BIObject)
	 */
	public void insertBIObject(BIObject obj) throws EMFUserError {
		BIObject biObject = obj;

		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiObjects hibBIObject = new SbiObjects();
			SbiEngines hibEngine = (SbiEngines) aSession.load(SbiEngines.class,
					biObject.getEngine().getId());
			hibBIObject.setSbiEngines(hibEngine); 
			hibBIObject.setDescr(biObject.getDescription());
			hibBIObject.setLabel(biObject.getLabel());
			hibBIObject.setName(biObject.getName());
			hibBIObject.setEncrypt(new Short(biObject.getEncrypt().shortValue()));
			hibBIObject.setVisible(new Short(biObject.getVisible().shortValue()));
			
			hibBIObject.setRelName(biObject.getRelName());
			SbiDomains hibState = (SbiDomains) aSession.load(SbiDomains.class,
					biObject.getStateID());
			hibBIObject.setState(hibState);
			hibBIObject.setStateCode(biObject.getStateCode());
			SbiDomains hibObjectType = (SbiDomains) aSession.load(
					SbiDomains.class, biObject.getBiObjectTypeID());
			hibBIObject.setObjectType(hibObjectType);
			hibBIObject.setObjectTypeCode(biObject.getBiObjectTypeCode());
			
			// uuid generation
			UUIDGenerator uuidGenerator = UUIDGenerator.getInstance();
			UUID uuidObj = uuidGenerator.generateTimeBasedUUID();
			String uuid = uuidObj.toString();
			hibBIObject.setUuid(uuid);
			
			ConfigSingleton config = ConfigSingleton.getInstance();
			SourceBean biobjectsPathSB = (SourceBean) config.getAttribute(SpagoBIConstants.CMS_BIOBJECTS_PATH);
			String biobjectsPath = (String) biobjectsPathSB.getAttribute("path");
			String path = biobjectsPath + "/" + uuid;
			hibBIObject.setPath(path);

			Integer id = (Integer) aSession.save(hibBIObject);
			
			hibBIObject = (SbiObjects) aSession.load(SbiObjects.class, id);
			
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
			
//			RequestContainer requestContainer = RequestContainer
//					.getRequestContainer();
//			SessionContainer session = requestContainer.getSessionContainer();
//			SessionContainer permSession = session.getPermanentContainer();
//			IEngUserProfile profile = (IEngUserProfile) permSession
//					.getAttribute(IEngUserProfile.ENG_USER_PROFILE);

			
			CmsManager manager = new CmsManager();
			SetOperation setOp = new SetOperation();
			setOp.setPath(path);
			setOp.setType(SetOperation.TYPE_CONTAINER);
			setOp.setEraseOldProperties(true);
			// define properties
			List properties = new ArrayList();
			String[] typePropValues = new String[] { biObject.getBiObjectTypeCode() };
			CmsProperty proptype = new CmsProperty(AdmintoolsConstants.NODE_CMS_TYPE, typePropValues);
			properties.add(proptype);
			setOp.setProperties(properties);
			manager.execSetOperation(setOp);
			// Set the report template
			if (biObject.getTemplate().getFileContent().length > 0) {
				setOp.setContent(new ByteArrayInputStream(biObject.getTemplate().getFileContent()));
				setOp.setType(SetOperation.TYPE_CONTENT);
				setOp.setPath(path + "/template");
				// define properties
				properties =  new ArrayList();
				String[] nameFilePropValues = new String[] { biObject.getTemplate().getFileName() };
				String today = new Long(new Date().getTime()).toString();
				String[] datePropValues = new String[] { today };
				CmsProperty propFileName = new CmsProperty("fileName", nameFilePropValues);
				CmsProperty propDateLoad = new CmsProperty("dateLoad", datePropValues);
				properties.add(propFileName);
				properties.add(propDateLoad);
				setOp.setProperties(properties);
				manager.execSetOperation(setOp);
			}
			// Clear bytes in memory
			biObject.setTemplate(null);
			tx.commit();
			
			obj.setId(id);
			
		} catch (OperationExecutionException oe) {

			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE,
					"BIObjectDAOImpl", "internalModify",
					"Cannot recover detail information", oe);

			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} catch (BuildOperationException boe) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE,
					"BIObjectDAOImpl", "internalModify",
					"Cannot recover detail information", boe);

			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
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
	 * @see it.eng.spagobi.bo.dao.IBIObjectDAO#eraseBIObject(it.eng.spagobi.bo.BIObject, java.lang.Integer)
	 */
	public void eraseBIObject(BIObject obj, Integer idFunct) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiObjects hibBIObject = (SbiObjects) aSession.load(
					SbiObjects.class, obj.getId());
			
			// erasing object from functionality
			Set hibObjFuncs = hibBIObject.getSbiObjFuncs();
			Iterator itObjFunc = hibObjFuncs.iterator();
			while (itObjFunc.hasNext()) {
				SbiObjFunc aSbiObjFunc = (SbiObjFunc) itObjFunc.next();
				if (idFunct == null || aSbiObjFunc.getId().getSbiFunctions().getFunctId().intValue() == idFunct.intValue()) {
					SpagoBITracer.debug(AdmintoolsConstants.NAME_MODULE,
							"BIObjectDAOImpl", "eraseBIObject",
							"Deleting object [" + obj.getName() + "] from folder [" + aSbiObjFunc.getId().getSbiFunctions().getPath() + "]");
					aSession.delete(aSbiObjFunc);
				}
			}
			
			tx.commit();
			aSession.refresh(hibBIObject);
			
			// if the object is no more referenced in any folder, erases it from sbi_obejcts table and from CMS
			//hibBIObject = (SbiObjects) aSession.load(SbiObjects.class, obj.getId());
			hibObjFuncs = hibBIObject.getSbiObjFuncs();
			if (hibObjFuncs == null || hibObjFuncs.size() == 0) {
			
				tx = aSession.beginTransaction();
				SpagoBITracer.debug(AdmintoolsConstants.NAME_MODULE,
						"BIObjectDAOImpl", "eraseBIObject",
						"The object [" + obj.getName() + "] is no more referenced by any functionality. It will be completely deleted from db and from CMS.");
				
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
				
				aSession.delete(hibBIObject);
				
				// update subreports table 
				ISubreportDAO subrptdao = DAOFactory.getSubreportDAO();
				subrptdao.eraseSubreportByMasterRptId(obj.getId());
				subrptdao.eraseSubreportBySubRptId(obj.getId());
				
				tx.commit();
				
				/*
				DataConnection dataConnection = null;
				SQLCommand sqlCommand = null;
							
				DataConnectionManager dataConnectionManager = DataConnectionManager.getInstance();
				dataConnection = dataConnectionManager.getConnection("spagobi");
				String statement = null;
								
				statement = SQLStatements.getStatement("DELETE_SUBREPORTS");
				statement = statement.replaceFirst("\\?", obj.getId().toString());
				SpagoBITracer.debug(AdmintoolsConstants.NAME_MODULE,
						"BIObjectDAOImpl", "eraseBIObject",
						"Executing statement: " + statement);
				sqlCommand = dataConnection.createDeleteCommand(statement);
				sqlCommand.execute();
				SpagoBITracer.debug(AdmintoolsConstants.NAME_MODULE,
						"BIObjectDAOImpl", "eraseBIObject",
						"Statement executed succesfully");
				
				statement = SQLStatements.getStatement("DELETE_SUBREPORTS_BY_SUBRPTID");
				statement = statement.replaceFirst("\\?", obj.getId().toString());
				SpagoBITracer.debug(AdmintoolsConstants.NAME_MODULE,
						"BIObjectDAOImpl", "eraseBIObject",
						"Executing statement: " + statement);
				sqlCommand = dataConnection.createDeleteCommand(statement);
				sqlCommand.execute();	
				SpagoBITracer.debug(AdmintoolsConstants.NAME_MODULE,
						"BIObjectDAOImpl", "eraseBIObject",
						"Statement executed succesfully");
				*/
				
	            // get profile user
//				RequestContainer requestContainer =  RequestContainer.getRequestContainer();
//				SessionContainer session = requestContainer.getSessionContainer();
//				SessionContainer permSession = session.getPermanentContainer();
//				IEngUserProfile profile = (IEngUserProfile)permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
				
				// erase from cms if exists
				GetOperation getOp = new GetOperation();
				String path = obj.getPath();
				getOp.setPath(path);
				getOp.setRetriveContentInformation("false");
				getOp.setRetrivePropertiesInformation("false");
				getOp.setRetriveVersionsInformation("false");
				getOp.setRetriveChildsInformation("false");
	            CmsManager manager = new CmsManager();
				CmsNode cmsnode = manager.execGetOperation(getOp);
				if (cmsnode != null) {
					DeleteOperation delOp = new DeleteOperation();
					delOp.setPath(obj.getPath());
					manager.execDeleteOperation(delOp);
				} else {
					SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE,
							"BIObjectDAOImpl", "eraseBIObject",
							"No CMS node corresponds to the BIObject [" + obj.getName() + "] with path [" + obj.getPath() + "]!!! " + 
							"The erase operation will have no effects on CMS.");
				}
			}
		} catch (HibernateException he) {
			logException(he);
			if (tx != null && tx.isActive())
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} catch (Exception ex) {
			logException(ex);
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
	 * @see it.eng.spagobi.bo.dao.IBIObjectDAO#getCorrectRolesForExecution(java.lang.Integer, it.eng.spago.security.IEngUserProfile)
	 */
	public List getCorrectRolesForExecution(Integer id, IEngUserProfile profile) throws EMFUserError {
		List correctRoles = null;
		try  {
			correctRoles = getCorrectRoles(id, profile.getRoles());
		} catch (EMFInternalError emfie) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
								"BIObjectDAOHibImpl", 
								"getCorrectRolesForExecution", 
								"error getting role from the user profile", emfie);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
		return correctRoles;
	}

	
	/** 
	 * @see it.eng.spagobi.bo.dao.IBIObjectDAO#getCorrectRolesForExecution(java.lang.Integer)
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
			logException(he);
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
		
			BIObject aBIObject = new BIObject();
			
			aBIObject.setBiObjectTypeCode(hibBIObject.getObjectTypeCode());
			aBIObject.setBiObjectTypeID(hibBIObject.getObjectType().getValueId());
			
			aBIObject.setDescription(hibBIObject.getDescr());
			aBIObject.setEncrypt(new Integer(hibBIObject.getEncrypt().intValue()));
			aBIObject.setVisible(new Integer(hibBIObject.getVisible().intValue()));
									
			aBIObject.setEngine(new EngineDAOHibImpl().toEngine(hibBIObject.getSbiEngines()));
			
			aBIObject.setId(hibBIObject.getBiobjId());
			aBIObject.setLabel(hibBIObject.getLabel());
			aBIObject.setName(hibBIObject.getName());
			
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
	
	/**
	 * Given all parameters for the input hibernate BI object, fills the other
	 * <code>BIObject</code> at input with these parameters.
	 * 
	 * @param aBIObject	The BI object to fill parameters in
	 * @param hibBIObject The Hibernate BI object
	 * @param role
	 */
//	public void fillBIObjectParameters(BIObject aBIObject, SbiObjects hibBIObject, String role){
//		
//		Set  hibBIObjectPars= hibBIObject.getSbiObjPars();
//		Iterator it = hibBIObjectPars.iterator();
//		SbiObjPar sbiObjPar = null;
//		List biObjectParameters = new ArrayList();
//		while (it.hasNext()){
//			sbiObjPar = (SbiObjPar)it.next();
//		
//		}
//		
//		aBIObject.setBiObjectParameters(biObjectParameters);
//	}





	public List loadAllBIObjects() throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		List realResult = new ArrayList();
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			Query hibQuery = aSession.createQuery(" from SbiObjects s order by s.name");
			List hibList = hibQuery.list();
			Iterator it = hibList.iterator();
			while (it.hasNext()) {
				realResult.add(toBIObject((SbiObjects) it.next()));
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
			"	objects.name");
			List hibList = hibQuery.list();
			Iterator it = hibList.iterator();
			while (it.hasNext()) {
				realResult.add(toBIObject((SbiObjects) it.next()));
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
			gatherCMSInformation(biObject);
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
		return biObject;
	}

}
