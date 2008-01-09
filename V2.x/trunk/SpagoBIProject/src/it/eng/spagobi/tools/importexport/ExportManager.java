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
package it.eng.spagobi.tools.importexport;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.dao.IBIObjectDAO;
import it.eng.spagobi.analiticalmodel.document.dao.ISnapshotDAO;
import it.eng.spagobi.analiticalmodel.document.dao.ISubObjectDAO;
import it.eng.spagobi.analiticalmodel.document.dao.ISubreportDAO;
import it.eng.spagobi.analiticalmodel.functionalitytree.bo.LowFunctionality;
import it.eng.spagobi.analiticalmodel.functionalitytree.dao.ILowFunctionalityDAO;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.Parameter;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.ParameterUse;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.dao.IParameterDAO;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.dao.IParameterUseDAO;
import it.eng.spagobi.behaviouralmodel.check.bo.Check;
import it.eng.spagobi.behaviouralmodel.lov.bo.ModalitiesValue;
import it.eng.spagobi.behaviouralmodel.lov.dao.IModalitiesValueDAO;
import it.eng.spagobi.commons.bo.Domain;
import it.eng.spagobi.commons.bo.Role;
import it.eng.spagobi.commons.bo.Subreport;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.dao.IDomainDAO;
import it.eng.spagobi.engines.config.bo.Engine;
import it.eng.spagobi.tools.datasource.bo.DataSource;
import it.eng.spagobi.tools.datasource.dao.IDataSourceDAO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Implements the interface which defines methods for managing the export
 * requests
 */
public class ExportManager implements IExportManager {

    static private Logger logger = Logger.getLogger(ExportManager.class);
    private String nameExportFile = "";
    private String pathExportFolder = "";
    private String pathBaseFolder = "";
    private String pathDBFolder = "";
    private String pathContentFolder = "";
    private SessionFactory sessionFactory = null;
    private Session session = null;
    private ExporterMetadata exporter = null;
    private boolean exportSubObjects = false;
    private boolean exportSnapshots = false;

    /**
     * Prepare the environment for export
     * 
     * @param pathExpFold
     *                Path of the export folder
     * @param nameExpFile
     *                the name to give to the exported file
     * @param expSubObj
     *                Flag which tells if it's necessary to export subobjects
     * @param expSnaps
     *                Flag which tells if it's necessary to export snapshots
     */
    public void prepareExport(String pathExpFold, String nameExpFile, boolean expSubObj, boolean expSnaps)
	    throws EMFUserError {
	logger.debug("IN. pathExpFold="+pathExpFold+" nameExpFile="+nameExpFile+" expSubObj="+expSubObj+" expSnaps"+expSnaps);
	try {
	    nameExportFile = nameExpFile;
	    pathExportFolder = pathExpFold;
	    exportSubObjects = expSubObj;
	    exportSnapshots = expSnaps;
	    if (pathExportFolder.endsWith("/") || pathExportFolder.endsWith("\\")) {
		pathExportFolder = pathExportFolder.substring(0, pathExportFolder.length() - 1);
	    }
	    pathBaseFolder = pathExportFolder + "/" + nameExportFile;
	    File baseFold = new File(pathBaseFolder);
	    // if folder exist delete it
	    if (baseFold.exists()) {
		ImpExpGeneralUtilities.deleteDir(baseFold);
	    }
	    baseFold.mkdirs();
	    pathDBFolder = pathBaseFolder + "/metadata";
	    File dbFold = new File(pathDBFolder);
	    dbFold.mkdirs();
	    pathContentFolder = pathBaseFolder + "/contents";
	    File contFold = new File(pathContentFolder);
	    contFold.mkdirs();
	    ExportUtilities.copyMetadataScript(pathDBFolder);
	    ExportUtilities.copyMetadataScriptProperties(pathDBFolder);
	    sessionFactory = ExportUtilities.getHibSessionExportDB(pathDBFolder);
	    session = sessionFactory.openSession();
	    exporter = new ExporterMetadata();

	} catch (Exception e) {
	    logger.error("Error while creating export environment " , e);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
	}finally{
	    logger.debug("OUT");
	}
    }

    /**
     * Exports objects and creates the archive export file
     * 
     * @param objPaths
     *                List of path of the objects to export
     */
    public String exportObjects(List objIds) throws EMFUserError {
	logger.debug("IN");
	try {
	    exportPropertiesFile();
	    exportDomains();
	    Iterator iterObjs = objIds.iterator();
	    while (iterObjs.hasNext()) {
		String idobj = (String) iterObjs.next();
		exportSingleObj(idobj);
	    }
	    closeSession();
	    String archivePath = createExportArchive();
	    deleteTmpFolder();
	    return archivePath;
	} catch (EMFUserError emfue) {
	    throw emfue;
	} catch (Exception e) {
	    logger.error("Error while exporting objects " , e);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
	}finally{
	    logger.debug("OUT");
	}
    }

    /**
     * Delete the temporary folder created for export purpose
     */
    private void deleteTmpFolder() {
	logger.debug("IN");
	String folderTmpPath = pathExportFolder + "/" + nameExportFile;
	File folderTmp = new File(folderTmpPath);
	ImpExpGeneralUtilities.deleteDir(folderTmp);
	logger.debug("OUT");
    }

    /**
     * Creates the compress export file
     * 
     * @return The path of the exported compress file
     * @throws EMFUserError
     */
    private String createExportArchive() throws EMFUserError {
	logger.debug("IN");
	FileOutputStream fos = null;
	ZipOutputStream out = null;
	String archivePath = pathExportFolder + "/" + nameExportFile + ".zip";
	try {
	    File archiveFile = new File(archivePath);
	    if (archiveFile.exists()) {
		archiveFile.delete();
	    }
	    fos = new FileOutputStream(archivePath);
	    out = new ZipOutputStream(fos);
	    compressFolder(pathBaseFolder, out);
	    out.flush();
	} catch (Exception e) {
	    logger.error("Error while creating archive file " , e);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
	} finally {
	    try {
		if (out != null) {
		    out.close();
		}
		if (fos != null) {
		    fos.close();
		}
	    } catch (Exception e) {
		logger.error("Error while closing streams " , e);
	    }
	    logger.debug("OUT");
	}
	
	return archivePath;
    }

    /**
     * Compress contents of a folder into an output stream
     * 
     * @param pathFolder
     *                The path of the folder to compress
     * @param out
     *                The Compress output stream
     * @throws EMFUserError
     */
    private void compressFolder(String pathFolder, ZipOutputStream out) throws EMFUserError {
	logger.debug("IN");
	File folder = new File(pathFolder);
	String[] entries = folder.list();
	byte[] buffer = new byte[4096];
	int bytes_read;
	FileInputStream in = null;
	try {
	    for (int i = 0; i < entries.length; i++) {
		File f = new File(folder, entries[i]);
		if (f.isDirectory()) {
		    compressFolder(pathFolder + "/" + f.getName(), out);
		} else {
		    in = new FileInputStream(f);
		    String completeFileName = pathFolder + "/" + f.getName();
		    String relativeFileName = f.getName();
		    if (completeFileName.lastIndexOf(pathExportFolder) != -1) {
			int index = completeFileName.lastIndexOf(pathExportFolder);
			int len = pathExportFolder.length();
			relativeFileName = completeFileName.substring(index + len + 1);
		    }
		    ZipEntry entry = new ZipEntry(relativeFileName);
		    out.putNextEntry(entry);
		    while ((bytes_read = in.read(buffer)) != -1)
			out.write(buffer, 0, bytes_read);
		    in.close();
		}
	    }
	} catch (Exception e) {
	    logger.error("Error while creating archive file " , e);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
	} finally {
	    try {
		if (in != null) {
		    in.close();
		}
	    } catch (Exception e) {
		logger.error("Error while closing streams " , e);
	    }
	    logger.debug("OUT");
	}
    }

    /**
     * Creates the export properties file
     * 
     * @throws EMFUserError
     */
    private void exportPropertiesFile() throws EMFUserError {
	logger.debug("IN");
	FileOutputStream fos = null;
	try {
	    String propFilePath = pathBaseFolder + "/export.properties";
	    fos = new FileOutputStream(propFilePath);
	    ConfigSingleton config = ConfigSingleton.getInstance();
	    SourceBean currentVersionSB = (SourceBean) config.getAttribute("IMPORTEXPORT.CURRENTVERSION");
	    String version = (String) currentVersionSB.getAttribute("version");
	    String properties = "spagobi-version=" + version + "\n";
	    fos.write(properties.getBytes());
	    fos.flush();
	    fos.close();
	} catch (Exception e) {
	    logger.error( "Error while exporting properties file " , e);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
	} finally {
	    try {
		if (fos != null) {
		    fos.close();
		}
	    } catch (Exception e) {
		logger.error("Error while closing streams " , e);
	    }
	    logger.debug("OUT");
	}
    }

    /**
     * Exports SpagoBI Domain Objects
     * 
     * @throws EMFUserError
     */
    private void exportDomains() throws EMFUserError {
	logger.debug("IN");
	try {
	    List domains = DAOFactory.getDomainDAO().loadListDomains();
	    Iterator itDom = domains.iterator();
	    while (itDom.hasNext()) {
		Domain dom = (Domain) itDom.next();
		exporter.insertDomain(dom, session);
	    }
	} catch (Exception e) {
	    logger.error("Error while exporting domains " , e);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
	}finally{
	    logger.debug("OUT");
	}
    }

    /**
     * Export A single SpagoBI BiObject
     * 
     * @param path
     *                The path of the biobject to export
     * @throws EMFUserError
     */
    private void exportSingleObj(String idObj) throws EMFUserError {
	logger.debug("IN");
	try {
	    if ((idObj == null) || idObj.trim().equals(""))
		return;
	    IBIObjectDAO biobjDAO = DAOFactory.getBIObjectDAO();
	    BIObject biobj = biobjDAO.loadBIObjectForDetail(new Integer(idObj));
	    IDataSourceDAO dsDao=DAOFactory.getDataSourceDAO();
	    DataSource ds=dsDao.loadDataSourceByID(biobj.getDataSourceId());
	    exporter.insertDataSource(ds, session);



	    Engine engine = biobj.getEngine();
	    exporter.insertEngine(engine, session);
	    exporter.insertBIObject(biobj, session);
	    
	    if(exportSubObjects) {
		ISubObjectDAO subDao=DAOFactory.getSubObjectDAO();
		List subObjectLis=subDao.getSubObjects(biobj.getId());
		if (subObjectLis!=null && !subObjectLis.isEmpty())
		    	exporter.insertSubObject(biobj,subObjectLis, session);
	    }
	    if(exportSnapshots) {
		ISnapshotDAO subDao=DAOFactory.getSnapshotDAO();
		List snapshotLis=subDao.getSnapshots(biobj.getId());
		if (snapshotLis!=null && !snapshotLis.isEmpty())
		    	exporter.insertSnapshot(biobj,snapshotLis, session);
	    }	    

	    // insert functionalities and association with object
	    List functs = biobj.getFunctionalities();
	    Iterator iterFunct = functs.iterator();
	    while (iterFunct.hasNext()) {
		Integer functId = (Integer) iterFunct.next();
		ILowFunctionalityDAO lowFunctDAO = DAOFactory.getLowFunctionalityDAO();
		LowFunctionality funct = lowFunctDAO.loadLowFunctionalityByID(functId, false);
		exporter.insertFunctionality(funct, session);
		exporter.insertObjFunct(biobj, funct, session);
	    }
	    // export parameters
	    List biparams = biobjDAO.getBIObjectParameters(biobj);
	    exportBIParamsBIObj(biparams, biobj);
	    // export parameters dependecies
	    exporter.insertBiParamDepend(biparams, session);

	    // export subReport relation
	    ISubreportDAO subRepDao = DAOFactory.getSubreportDAO();
	    List subList = subRepDao.loadSubreportsByMasterRptId(biobj.getId());
	    Iterator itersub = subList.iterator();
	    while (itersub.hasNext()) {
		Subreport subRep = (Subreport) itersub.next();
		exporter.insertSubReportAssociation(subRep, session);
		exportSingleObj(subRep.getSub_rpt_id().toString());
	    }
	} catch (EMFUserError emfue) {
	    throw emfue;
	} catch (Exception e) {
	    logger.error("Error while exporting document with id " + idObj + " :" , e);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
	}finally{
	    logger.debug("OUT");
	}
	
    }


    /**
     * Exports the BIParameters of a BIObject
     * 
     * @param biparams
     *                List ot the BIParameters belonging to the BIObject
     * @param biobj
     *                The BIObject to which the parametes belong
     * @throws EMFUserError
     */
    private void exportBIParamsBIObj(List biparams, BIObject biobj) throws EMFUserError {
	logger.debug("IN");
	Iterator iterBIParams = biparams.iterator();
	while (iterBIParams.hasNext()) {
	    BIObjectParameter biparam = (BIObjectParameter) iterBIParams.next();
	    IParameterDAO parDAO = DAOFactory.getParameterDAO();
	    Parameter param = parDAO.loadForDetailByParameterID(biparam.getParameter().getId());
	    exporter.insertParameter(param, session);
	    exporter.insertBIObjectParameter(biparam, session);
	    IParameterUseDAO paruseDAO = DAOFactory.getParameterUseDAO();
	    List paruses = paruseDAO.loadParametersUseByParId(param.getId());
	    exportParUses(paruses);
	}
	logger.debug("OUT");
    }

    /**
     * Export a list ot Parameter use Objects
     * 
     * @param paruses
     *                The list of parameter use objects
     * @throws EMFUserError
     */
    private void exportParUses(List paruses) throws EMFUserError {
	logger.debug("IN");
	Iterator iterUses = paruses.iterator();
	while (iterUses.hasNext()) {
	    ParameterUse paruse = (ParameterUse) iterUses.next();
	    Integer idLov = paruse.getIdLov();
	    if (idLov != null) {
		IModalitiesValueDAO lovDAO = DAOFactory.getModalitiesValueDAO();
		ModalitiesValue lov = lovDAO.loadModalitiesValueByID(idLov);
		exporter.insertLov(lov, session);
	    }
	    exporter.insertParUse(paruse, session);
	    List checks = paruse.getAssociatedChecks();
	    Iterator iterChecks = checks.iterator();
	    while (iterChecks.hasNext()) {
		Check check = (Check) iterChecks.next();
		exporter.insertCheck(check, session);
		exporter.insertParuseCheck(paruse, check, session);
	    }
	    List roles = paruse.getAssociatedRoles();
	    exportRoles(roles);
	    Iterator iterRoles = roles.iterator();
	    while (iterRoles.hasNext()) {
		Role role = (Role) iterRoles.next();
		exporter.insertParuseRole(paruse, role, session);
	    }
	}
	logger.debug("OUT");
    }

 


    private void addToList(List dest, List src) {
	logger.debug("IN");
	Iterator iterSrc = src.iterator();
	while (iterSrc.hasNext()) {
	    Object obj = iterSrc.next();
	    dest.add(obj);
	}
	logger.debug("OUT");
    }

    /**
     * Export an association between a functionality and a list of roles
     * 
     * @param roles
     *                The list of roles to associate to the functionality
     * @param funct
     *                The functionality which is part of the association
     * @param state
     *                The state of the association
     * @throws EMFUserError
     */
    private void exportFunctRoles(List roles, LowFunctionality funct, String state) throws EMFUserError {
	logger.debug("IN");
	IDomainDAO domDAO = DAOFactory.getDomainDAO();
	Domain stateDom = domDAO.loadDomainByCodeAndValue("STATE", state);
	Integer idState = stateDom.getValueId();
	Iterator iterRoles = roles.iterator();
	while (iterRoles.hasNext()) {
	    Role role = (Role) iterRoles.next();
	    exporter.insertFunctRole(role, funct, idState, state, session);
	}
	logger.debug("OUT");
    }

    /**
     * Export a list of SpagoBI roles
     * 
     * @param roles
     *                The list of roles to export
     * @throws EMFUserError
     */
    private void exportRoles(List roles) throws EMFUserError {
	logger.debug("IN");
	Iterator iterRoles = roles.iterator();
	while (iterRoles.hasNext()) {
	    Role role = (Role) iterRoles.next();
	    exporter.insertRole(role, session);
	}
	logger.debug("OUT");
    }



    /**
     * Close hibernate session and session factory relative to the export
     * database
     */
    private void closeSession() {
	logger.debug("IN");
	if (session != null) {
	    if (session.isOpen())
		session.close();
	}
	if (sessionFactory != null) {
	    sessionFactory.close();
	}
	logger.debug("OUT");
    }

    /**
     * Clean the export environment (close sessions and delete temporary files)
     */
    public void cleanExportEnvironment() {
	logger.debug("IN");
	closeSession();
	deleteTmpFolder();
	logger.debug("OUT");
    }

}
