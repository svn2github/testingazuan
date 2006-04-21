package it.eng.spagobi.importexport.version18;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.Engine;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.metadata.SbiDomains;
import it.eng.spagobi.metadata.SbiEngines;
import it.eng.spagobi.metadata.SbiExtRoles;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class ImporterMetadata {

		public List getAllExportedRoles(Transaction tx, Session session) throws EMFInternalError {
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
				throw new EMFInternalError(EMFErrorSeverity.ERROR, "error while getting exported roles");
			} 
			return roles;
		}
		
		
		public List getAllExportedSbiRoles(Transaction tx, Session session) throws EMFInternalError {
			List hibList = null;
			try {
				Query hibQuery = session.createQuery(" from SbiExtRoles");
				hibList = hibQuery.list();
			} catch (HibernateException he) {
				SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "getAllExportedSbiRoles",
						               "Error while getting exported sbi roles " + he);
				throw new EMFInternalError(EMFErrorSeverity.ERROR, "error while getting exported sbi roles");
			} 
			return hibList;
		}
		
		
		public List getAllExportedEngines(Transaction tx, Session session) throws EMFInternalError {
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
				throw new EMFInternalError(EMFErrorSeverity.ERROR, "error while getting exported engines");
			} 
			return engines;
		}
		
		
		public List getAllExportedSbiEngines(Transaction tx, Session session) throws EMFInternalError {
			List hibList = null;
			try {
				Query hibQuery = session.createQuery(" from SbiEngines");
				hibList = hibQuery.list();
			} catch (HibernateException he) {
				SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "getAllExportedSbiEngines",
						               "Error while getting exported sbi engine " + he);
				throw new EMFInternalError(EMFErrorSeverity.ERROR, "error while getting exported sbi engines");
			} 
			return hibList;
		}
		
	
		
		
		public void updateRoleReferences(Map roleAssociations, Transaction tx, Session session) throws EMFInternalError {
			try {
				Set rolesAssKeys = roleAssociations.keySet();
				Iterator iterRoleAssKeys = rolesAssKeys.iterator();
				while(iterRoleAssKeys.hasNext()) {
					String roleExpId = (String)iterRoleAssKeys.next();
					String roleAssId = (String)roleAssociations.get(roleExpId);
				    SbiExtRoles hibRole = (SbiExtRoles)session.load(SbiExtRoles.class, new Integer(roleExpId));
				    session.delete(hibRole);
				    String hqlUpdate = "update SbiFuncRole fc set fc.id.role.extRoleId = :newRole where fc.id.role.extRoleId = :oldRole";
				    int updatedEntities = session.createQuery( hqlUpdate )
				         .setInteger("newRole", new Integer(roleAssId).intValue() )
				         .setInteger("oldRole", new Integer(roleExpId).intValue() )
				         .executeUpdate();
				    hqlUpdate = "update SbiParuseDet pd set pd.id.sbiParuse.useId = :newRole where pd.id.sbiParuse.useId = :oldRole";
				    updatedEntities = session.createQuery( hqlUpdate )
			         .setInteger("newRole", new Integer(roleAssId).intValue() )
			         .setInteger("oldRole", new Integer(roleExpId).intValue() )
			         .executeUpdate();
				}
			} catch (HibernateException he) {
				SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "updateRoleReference",
			                           "Error while updating role references " + he);
				throw new EMFInternalError(EMFErrorSeverity.ERROR, "error while updating role references");
			} 
		}
		
		
		
		
		public void updateEngineReferences(Map engineAssociations, Transaction tx, Session session) throws EMFInternalError {
			try {
				Set enginesAssKeys = engineAssociations.keySet();
				Iterator iterEngineAssKeys = enginesAssKeys.iterator();
				while(iterEngineAssKeys.hasNext()) {
					String engineExpId = (String)iterEngineAssKeys.next();
					String engineAssId = (String)engineAssociations.get(engineExpId);
				    SbiEngines hibEngine = (SbiEngines)session.load(SbiEngines.class, new Integer(engineExpId));
				    session.delete(hibEngine);
				    String hqlUpdate = "update SbiObjects o set o.sbiEngines.engineId = :newEngine where o.sbiEngines.engineId = :oldEngine";
				    int updatedEntities = session.createQuery( hqlUpdate )
				         .setInteger("newEngine", new Integer(engineAssId).intValue() )
				         .setInteger( "oldEngine", new Integer(engineExpId).intValue() )
				         .executeUpdate();
				}
			} catch (HibernateException he) {
				SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "updateEngineReferences",
			                           "Error while updating engine references " + he);
				throw new EMFInternalError(EMFErrorSeverity.ERROR, "error while updating engine references");
			} 
		}
		
		
		
		
		
		public Integer insertRole(SbiExtRoles hibRole, Session session) throws EMFInternalError {
			Integer id = null;
			try{
				Serializable serId = session.save(hibRole);
				id = (Integer)serId;
			} catch (HibernateException he) {
				SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "insertRole",
                        			   "Error while inserting role " + he);
				throw new EMFInternalError(EMFErrorSeverity.ERROR, "error while inserting role");
			}
			return id;
		}
		
		
		public Integer insertEngine(SbiEngines hibEng, Session session) throws EMFInternalError {
			Integer id = null;
			try{
				Serializable serId = session.save(hibEng);
				id = (Integer)serId;
			} catch (HibernateException he) {
				SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "insertEngine",
                        			   "Error while inserting engine " + he);
				throw new EMFInternalError(EMFErrorSeverity.ERROR, "error while inserting engine");
			}
			return id;
		}
		
}
