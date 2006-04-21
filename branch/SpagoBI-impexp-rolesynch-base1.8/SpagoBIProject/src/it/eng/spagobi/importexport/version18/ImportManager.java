package it.eng.spagobi.importexport.version18;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.importexport.IImportManager;
import it.eng.spagobi.metadata.HibernateUtil;
import it.eng.spagobi.metadata.SbiEngines;
import it.eng.spagobi.metadata.SbiExtRoles;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.type.AssociationType;

public class ImportManager implements IImportManager {

	private String pathImportTmpFolder = "";
    private String pathBaseFolder = "";
    private String pathDBFolder = "";  
    private String pathContentFolder = "";
	private ImporterMetadata importer = null;
	private Properties props = null;
	private SessionFactory sessionFactoryExpDB = null;
	private static Session sessionExpDB = null;
	private static Transaction txExpDB = null;
	private static Session sessionCurrDB = null;
	private static Transaction txCurrDB = null;
	
	
	
	public void prepareImport(String pathImpTmpFold, String pathArchiveFile) throws EMFUserError, EMFInternalError {
		ImportUtilities.decompressArchive(pathImpTmpFold, pathArchiveFile);
		File archive = new File(pathArchiveFile);
		String archiveName = archive.getName();
		int lastDotPos = archiveName.lastIndexOf(".");
		if(lastDotPos!=-1)
			archiveName = archiveName.substring(0, lastDotPos);
		pathImportTmpFolder = pathImpTmpFold;
		pathBaseFolder = pathImportTmpFolder + "/" + archiveName;
	    pathDBFolder =  pathBaseFolder + "/metadata";
	    pathContentFolder = pathBaseFolder + "/contents";
	    String propFilePath = pathBaseFolder + "/export.properties";
		try{
		    FileInputStream fis = new FileInputStream(propFilePath);
			props = new Properties();
			props.load(fis);
		} catch (Exception e){
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "prepareImport",
		   			               "Error while reading properties file " + e);
			throw new EMFInternalError(EMFErrorSeverity.ERROR, "error while reading properties file");
		}
		importer = new ImporterMetadata();
		sessionFactoryExpDB = ImportUtilities.getHibSessionExportDB(pathDBFolder);
		sessionExpDB = sessionFactoryExpDB.openSession();
		txExpDB = sessionExpDB.beginTransaction();
		sessionCurrDB = HibernateUtil.currentSession();
		txCurrDB = sessionCurrDB.beginTransaction();
	}
	
	public String getExportVersion() {
		return props.getProperty("spagobi-version");
	}
	
	public String getCurrentVersion() {
		ConfigSingleton conf = ConfigSingleton.getInstance();
		SourceBean curVerSB = (SourceBean)conf.getAttribute("IMPORTEXPORT.CURRENTVERSION");
		String curVer = (String)curVerSB.getAttribute("version");
		return curVer;
	}
	
	public List getExportedRoles() throws EMFUserError, EMFInternalError {
		List exportedRoles = null;
		try{
			exportedRoles = importer.getAllExportedRoles(txExpDB, sessionExpDB);
		} catch(EMFInternalError emfie) {
			txExpDB.rollback();
			closeSession();
			closeSessionFactory();
			throw emfie;
		}
		return exportedRoles;
	}

	
	public List getExportedEngines() throws EMFUserError, EMFInternalError {
		List exportedEngines = null;
		try{
			exportedEngines = importer.getAllExportedEngines(txExpDB, sessionExpDB);
		} catch(EMFInternalError emfie) {
			txExpDB.rollback();
			closeSession();
			closeSessionFactory();
		    throw emfie;
		}
		return exportedEngines;
	}

	public void updateRoleReferences(Map roleAssociations) throws EMFUserError, EMFInternalError {
		/* 
		 * The key of the map are the id of the exported roles
		 * Each key value is the id of the system role associate
		 */
		// each exported role should be associate only to one system role
		Set rolesAssKeys = roleAssociations.keySet();
		Iterator iterRoleAssKeys1 = rolesAssKeys.iterator();
		while(iterRoleAssKeys1.hasNext()) {
			String roleExpId = (String)iterRoleAssKeys1.next();
			String roleAssId = (String)roleAssociations.get(roleExpId);
			Iterator iterRoleAssKeys2 = rolesAssKeys.iterator();
			while(iterRoleAssKeys2.hasNext()) {
				String otherRoleExpId = (String)iterRoleAssKeys2.next();
				if(!otherRoleExpId.equals(roleExpId)) {
					String otherRoleAssId = (String)roleAssociations.get(otherRoleExpId);
					if(otherRoleAssId.equals(roleAssId)){
						throw new EMFUserError(EMFErrorSeverity.ERROR, 8001);
					}
				}
			}
		}
		try{
			importer.updateRoleReferences(roleAssociations, txExpDB, sessionExpDB);
		} catch(EMFInternalError emfie) {
			txExpDB.rollback();
			closeSession();
			closeSessionFactory();
		    throw emfie;
		}		
	}
	
	public void updateEngineReferences(Map roleAssociations) throws EMFUserError, EMFInternalError {
		/* 
		 * The key of the map are the id of the exported engines
		 * Each key value is the id of the system engine associate
		 */
		try{
			importer.updateEngineReferences(roleAssociations, txExpDB, sessionExpDB);
		} catch(EMFInternalError emfie) {
			txExpDB.rollback();
			closeSession();
			closeSessionFactory();
		    throw emfie;
		}		
		
	}
	
	private void closeSessionFactory() {
		if(sessionFactoryExpDB!=null){
			sessionFactoryExpDB.close();
		}
	}
	
	
	private void closeSession() {
		if(sessionExpDB!=null){
			if(sessionExpDB.isOpen()) {
				sessionExpDB.close();
			}
		}
		if(sessionCurrDB!=null){
			if(sessionCurrDB.isOpen()) {
				sessionCurrDB.close();
			}
		}		
	}

	public void commitAllChanges() throws EMFUserError, EMFInternalError {
		txExpDB.commit();
		txCurrDB.commit();
		closeSession();
		closeSessionFactory();
	}
	
	
	
	
	
	
	
	
	
	public void importObjects() throws EMFUserError, EMFInternalError {
		importRoles();
		importEngines();
	}
	

	private void importRoles() throws EMFUserError, EMFInternalError {
		try{
			List exportedRoles = importer.getAllExportedSbiRoles(txExpDB, sessionExpDB);
			Iterator iterSbiRoles = exportedRoles.iterator();
			Map roleAss = new HashMap();
			while(iterSbiRoles.hasNext()){
				SbiExtRoles role = (SbiExtRoles)iterSbiRoles.next();
				Integer oldId = role.getExtRoleId();
			    Integer newId = importer.insertRole(role, sessionCurrDB);
			    roleAss.put(oldId.toString(), newId.toString());
			}
			importer.updateRoleReferences(roleAss, txExpDB, sessionExpDB);
		} catch(EMFInternalError emfie) {
			txExpDB.rollback();
			txCurrDB.rollback();
			closeSession();
			closeSessionFactory();
			throw emfie;
		}
	}
	
	
	
	private void importEngines() throws EMFUserError, EMFInternalError {
		try{
			List exportedEngines = importer.getAllExportedSbiEngines(txExpDB, sessionExpDB);
			Iterator iterSbiEngines = exportedEngines.iterator();
			Map engineAss = new HashMap();
			while(iterSbiEngines.hasNext()){
				SbiEngines engine = (SbiEngines)iterSbiEngines.next();
				Integer oldId = engine.getEngineId();
			    Integer newId = importer.insertEngine(engine, sessionCurrDB);
			    engineAss.put(oldId.toString(), newId.toString());
			}
			importer.updateEngineReferences(engineAss, txExpDB, sessionExpDB);
		} catch(EMFInternalError emfie) {
			txExpDB.rollback();
			txCurrDB.rollback();
			closeSession();
			closeSessionFactory();
			throw emfie;
		}
	}
	
	
}
