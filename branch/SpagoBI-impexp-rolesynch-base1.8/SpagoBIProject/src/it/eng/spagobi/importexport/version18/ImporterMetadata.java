package it.eng.spagobi.importexport.version18;

import it.eng.spago.base.SourceBeanException;
import it.eng.spago.cms.CmsManager;
import it.eng.spago.cms.CmsNode;
import it.eng.spago.cms.CmsProperty;
import it.eng.spago.cms.operations.GetOperation;
import it.eng.spago.cms.operations.SetOperation;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.Engine;
import it.eng.spagobi.bo.QueryDetail;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.metadata.SbiChecks;
import it.eng.spagobi.metadata.SbiEngines;
import it.eng.spagobi.metadata.SbiExtRoles;
import it.eng.spagobi.metadata.SbiFuncRole;
import it.eng.spagobi.metadata.SbiFunctions;
import it.eng.spagobi.metadata.SbiLov;
import it.eng.spagobi.metadata.SbiObjPar;
import it.eng.spagobi.metadata.SbiObjParId;
import it.eng.spagobi.metadata.SbiObjects;
import it.eng.spagobi.metadata.SbiParameters;
import it.eng.spagobi.metadata.SbiParuse;
import it.eng.spagobi.metadata.SbiParuseCk;
import it.eng.spagobi.metadata.SbiParuseDet;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class ImporterMetadata {

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
				SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "getAllExportedRoles",
						               "Error while getting exported roles " + he);
				throw new EMFUserError(EMFErrorSeverity.ERROR, 8004);
			} 
			return roles;
		}
		
		
		
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
					engines.add(eng);
				}
			} catch (HibernateException he) {
				SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "getAllExportedEngines",
						               "Error while getting exported engine " + he);
				throw new EMFUserError(EMFErrorSeverity.ERROR, 8004);
			} 
			return engines;
		}
		
		
				
		public List getAllExportedSbiObjects(Transaction tx, Session session, String table) throws EMFUserError {
			List hibList = null;
			try {
				Query hibQuery = session.createQuery(" from " + table);
				hibList = hibQuery.list();
			} catch (HibernateException he) {
				SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "getAllExportedSbiObjects",
						               "Error while getting exported sbi objects " + he);
				throw new EMFUserError(EMFErrorSeverity.ERROR, 8004);
			} 
			return hibList;
		}
		
		
		
		
		
		public void updateReferences(Map associations, Transaction tx, Session session, Class objClass) throws EMFUserError {
			try {
				Set assKeys = associations.keySet();
				Iterator iterAssKeys = assKeys.iterator();
				while(iterAssKeys.hasNext()) {
					String expId = (String)iterAssKeys.next();
					String assId = (String)associations.get(expId);
					Object obj = session.load(objClass, new Integer(expId));
					if(obj instanceof SbiEngines) {
						 SbiEngines hibEngine = (SbiEngines)obj;
						 session.delete(hibEngine);
						 String hqlUpdate = "update SbiObjects o set o.sbiEngines.engineId = " +
						 					":newEngine where o.sbiEngines.engineId = :oldEngine";
						 int updatedEntities = session.createQuery( hqlUpdate )
				         					.setInteger("newEngine", new Integer(assId).intValue() )
				         					.setInteger( "oldEngine", new Integer(expId).intValue() )
				         					.executeUpdate();
					} else if(obj instanceof SbiExtRoles) {
						SbiExtRoles hibRole = (SbiExtRoles)obj;
						session.delete(hibRole);
						String hqlUpdate = "update SbiFuncRole fc set fc.id.role.extRoleId = " +
										   ":newRole where fc.id.role.extRoleId = :oldRole";
					    int updatedEntities = session.createQuery( hqlUpdate )
					         .setInteger("newRole", new Integer(assId).intValue() )
					         .setInteger("oldRole", new Integer(expId).intValue() )
					         .executeUpdate();
					    hqlUpdate = "update SbiParuseDet pd set pd.id.sbiExtRoles.extRoleId = " +
					    			":newRole where pd.id.sbiExtRoles.extRoleId = :oldRole";
					    updatedEntities = session.createQuery( hqlUpdate )
				         .setInteger("newRole", new Integer(assId).intValue() )
				         .setInteger("oldRole", new Integer(expId).intValue() )
				         .executeUpdate();
					} else if(obj instanceof SbiLov) {
						 SbiLov hibLov = (SbiLov)obj;
						 session.delete(hibLov);
						 String hqlUpdate = "update SbiParuse pu set pu.sbiLov.lovId = " +
						 					":newLov where pu.sbiLov.lovId = :oldLov";
						 int updatedEntities = session.createQuery( hqlUpdate )
				         					.setInteger("newLov", new Integer(assId).intValue() )
				         					.setInteger( "oldLov", new Integer(expId).intValue() )
				         					.executeUpdate();
						 
						 
						List list = getAllExportedSbiObjects(tx, session, "SbiLov");
						List listp = getAllExportedSbiObjects(tx, session, "SbiParuse");
						 int i = 0;
						 
					}  else if(obj instanceof SbiChecks) {
						 SbiChecks hibCheck = (SbiChecks)obj;
						 session.delete(hibCheck);
						 String hqlUpdate = "update SbiParuseCk puc set puc.id.sbiChecks.checkId = " +
						 					":new where puc.id.sbiChecks.checkId = :old";
						 int updatedEntities = session.createQuery( hqlUpdate )
				         					.setInteger("new", new Integer(assId).intValue() )
				         					.setInteger( "old", new Integer(expId).intValue() )
				         					.executeUpdate();
					} else if(obj instanceof SbiParuse) {
						 SbiParuse hibParuse = (SbiParuse)obj;
						 session.delete(hibParuse);
						 String hqlUpdate = "update SbiParuseCk puc set puc.id.sbiParuse.useId = " +
						 					":new where puc.id.sbiParuse.useId = :old";
						 int updatedEntities = session.createQuery( hqlUpdate )
				         					.setInteger("new", new Integer(assId).intValue() )
				         					.setInteger( "old", new Integer(expId).intValue() )
				         					.executeUpdate();
						 hqlUpdate = "update SbiParuseDet pud set pud.id.sbiParuse.useId = " +
		 							 ":new where pud.id.sbiParuse.useId = :old";
						 updatedEntities = session.createQuery( hqlUpdate )
      					     			   .setInteger("new", new Integer(assId).intValue() )
      									   .setInteger("old", new Integer(expId).intValue() )
      									   .executeUpdate();
					} else if(obj instanceof SbiParameters) {
						 SbiParameters hibParam = (SbiParameters)obj;
						 session.delete(hibParam);
						 String hqlUpdate = "update SbiParuse pu set pu.sbiParameters.parId = " +
						 					":new where pu.sbiParameters.parId = :old";
						 int updatedEntities = session.createQuery( hqlUpdate )
				         					.setInteger("new", new Integer(assId).intValue() )
				         					.setInteger( "old", new Integer(expId).intValue() )
				         					.executeUpdate();
						 hqlUpdate = "update SbiObjPar op set op.id.sbiParameters.parId = " +
		 							 ":new where op.id.sbiParameters.parId = :old";
						 updatedEntities = session.createQuery( hqlUpdate )
      					     			   .setInteger("new", new Integer(assId).intValue() )
      									   .setInteger("old", new Integer(expId).intValue() )
      									   .executeUpdate();
					} else if(obj instanceof SbiFunctions) {
						 SbiFunctions hibFunct = (SbiFunctions)obj;
						 session.delete(hibFunct);
						 String hqlUpdate = "update SbiFuncRole fr set fr.id.function.functId = " +
						 					":new where fr.id.function.functId = :old";
						 int updatedEntities = session.createQuery( hqlUpdate )
				         					.setInteger("new", new Integer(assId).intValue() )
				         					.setInteger( "old", new Integer(expId).intValue() )
				         					.executeUpdate();
					} else if(obj instanceof SbiObjects) {
						 SbiObjects hibObj = (SbiObjects)obj;
						 session.delete(hibObj);
						 String hqlUpdate = "update SbiObjPar op set op.id.sbiObjects.biobjId = " +
						 					":new where op.id.sbiObjects.biobjId = :old";
						 int updatedEntities = session.createQuery( hqlUpdate )
				         					.setInteger("new", new Integer(assId).intValue() )
				         					.setInteger( "old", new Integer(expId).intValue() )
				         					.executeUpdate();
					}
				}
			} catch (HibernateException he) {
				SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "updateReferences",
			                           "Error while updating references " + he);
				throw new EMFUserError(EMFErrorSeverity.ERROR, 8004);
			} 
		}
		
		
		public void updateConnRefs(Map associations, Transaction tx, Session session) throws EMFUserError {
			try {
				List lovs = getAllExportedSbiObjects(tx, session, "SbiLov");
				Set assKeys = associations.keySet();
				Iterator iterAssKeys = assKeys.iterator();
				while(iterAssKeys.hasNext()) {
					String expConnName = (String)iterAssKeys.next();
					String assConnName = (String)associations.get(expConnName);
					Iterator iterLovs = lovs.iterator();
					while(iterLovs.hasNext()) {
						SbiLov lov = (SbiLov)iterLovs.next();
						if(lov.getInputTypeCd().equalsIgnoreCase("QUERY")) {
							String lovProv = lov.getLovProvider();
							QueryDetail qDet = QueryDetail.fromXML(lovProv);
							qDet.setConnectionName(assConnName);
							lovProv = qDet.toXML();
							lov.setLovProvider(lovProv);
							session.save(lov); 
						}
					}
				}
			} catch (SourceBeanException sbe) {
				SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "updateConnRefs",
									   "Error while updating connection references " + sbe);
				throw new EMFUserError(EMFErrorSeverity.ERROR, 8004);
			}	 
		}
		
		
		
		public void insertObject(Object hibObj, Session session) throws EMFUserError {
			try{
				Serializable serId = session.save(hibObj);
			} catch (HibernateException he) {
				SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "insertObject",
                        			   "Error while inserting object " + he);
				throw new EMFUserError(EMFErrorSeverity.ERROR, 8004);
			}
		}
		
		
		
		
		
		public void insertCmsFunctionality(String curBaseFold, String relExpPath) throws EMFUserError {
			if(relExpPath.startsWith("/"))
				relExpPath = relExpPath.substring(1);
			if(relExpPath.endsWith("/"))
				relExpPath = relExpPath.substring(0, relExpPath.length()-1);
			String[] parts = relExpPath.split("/");
			for(int i=0; i<parts.length; i++){
				String functPath = curBaseFold;
				for(int j=0; j<=i; j++){
					functPath = functPath + "/" + parts[j];	
				}
				CmsNode cmsnode = null;
				CmsManager manager = new CmsManager();
				try {
					GetOperation getOp = new GetOperation(functPath, false, false, false, false);
					cmsnode = manager.execGetOperation(getOp);
					if(cmsnode==null){
						SetOperation setOp = new SetOperation(functPath, SetOperation.TYPE_CONTAINER, false); 
						List properties = new ArrayList();
						String[] typePropValues = new String[] { AdmintoolsConstants.LOW_FUNCTIONALITY_TYPE };
						CmsProperty proptype = new CmsProperty(AdmintoolsConstants.NODE_CMS_TYPE, typePropValues);
				        properties.add(proptype);
				        setOp.setProperties(properties);
						manager.execSetOperation(setOp);
					}
				} catch (Exception e) {
					SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "insertCmsFunctionality",
             			   				   "Error while inserting functionality into cms " + e);
					throw new EMFUserError(EMFErrorSeverity.ERROR, 8004);
				}
			}
			
		}
		
		
		
		
		
		
		
		
		public SbiObjects insertBIObject(SbiObjects obj, String pathContent, Session session) throws EMFUserError {
			CmsNode cmsnode = null;
			CmsManager manager = new CmsManager();
			SbiObjects objToReturn = null;
			try {
				SetOperation setOp = new SetOperation(obj.getPath(), SetOperation.TYPE_CONTAINER, false);
				List properties = new ArrayList();
				String[] typePropValues = new String[] { obj.getObjectTypeCode() };
				CmsProperty proptype = new CmsProperty(AdmintoolsConstants.NODE_CMS_TYPE, typePropValues);
				properties.add(proptype);
				setOp.setProperties(properties);
				manager.execSetOperation(setOp);
				// get the template imputstream
				String pathTempFolder = pathContent + obj.getPath();
				File tempFolder = new File(pathTempFolder);
				File[] files = tempFolder.listFiles();
				File template = files[0];
				String fileTempName = template.getName();
				FileInputStream fis = new FileInputStream(template);
				setOp = new SetOperation(obj.getPath() + "/template", SetOperation.TYPE_CONTENT, false);
				setOp.setContent(fis);
				properties =  new ArrayList();
				String[] nameFilePropValues = new String[] {fileTempName};
				String today = new Date().toString();
				String[] datePropValues = new String[] { today };
				CmsProperty propFileName = new CmsProperty("fileName", nameFilePropValues);
				CmsProperty propDateLoad = new CmsProperty("dateLoad", datePropValues);
				properties.add(propFileName);
				properties.add(propDateLoad);
				setOp.setProperties(properties);
				manager.execSetOperation(setOp);
				fis.close();
				insertObject(obj, session);
				objToReturn = obj;
			} catch (Exception e) {
					SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "insertBIObject",
             			   				   "Error while inserting business objects " + e);
					throw new EMFUserError(EMFErrorSeverity.ERROR, 8004);
			}
			return objToReturn;
		}
		
		
		
		
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
				Integer prog = (Integer)uniqueMap.get("prog");
				hql = "from SbiObjPar op where op.id.sbiParameters.parId = " + paramid +
					  " and op.id.sbiObjects.biobjId = " + biobjid +
					  " and op.id.prog = " + prog;
				hqlQuery = sessionCurrDB.createQuery(hql);
				SbiObjPar hibObjPar = (SbiObjPar)hqlQuery.uniqueResult();
				return hibObjPar;
			} 
			return null;
		}
		
}
		


















