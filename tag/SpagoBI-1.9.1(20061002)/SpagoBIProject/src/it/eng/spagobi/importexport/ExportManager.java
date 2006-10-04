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

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
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
import it.eng.spagobi.bo.QueryDetail;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.bo.Subreport;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectCMSDAO;
import it.eng.spagobi.bo.dao.IBIObjectDAO;
import it.eng.spagobi.bo.dao.IDomainDAO;
import it.eng.spagobi.bo.dao.ILowFunctionalityDAO;
import it.eng.spagobi.bo.dao.IModalitiesValueDAO;
import it.eng.spagobi.bo.dao.IParameterDAO;
import it.eng.spagobi.bo.dao.IParameterUseDAO;
import it.eng.spagobi.bo.dao.ISubreportDAO;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;
import it.eng.spagobi.utilities.UploadedFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Implements the interface which defines methods for managing the export requests
 */
public class ExportManager implements IExportManager {

	private String nameExportFile = "";
	private String pathExportFolder = "";
    private String pathBaseFolder = "";
    private String pathDBFolder = "";  
    private String pathContentFolder = "";
	private SessionFactory sessionFactory = null;
	private Session session = null;
	private ExporterMetadata exporter = null;
	private boolean exportSubObjects = false;
	SourceBean connections = null;
	
	
	/**
	 * Prepare the environment for export
	 * @param pathExpFold Path of the export folder
	 * @param nameExpFile the name to give to the exported file
	 * @param expSubObj Flag which tells if it's necessary to export subobjects
	 */
	public void prepareExport(String pathExpFold, String nameExpFile, boolean expSubObj) throws EMFUserError {
		nameExportFile = nameExpFile;
		pathExportFolder = pathExpFold;
		exportSubObjects = expSubObj;
		if(pathExportFolder.endsWith("/") || pathExportFolder.endsWith("\\") ) {
			pathExportFolder = pathExportFolder.substring(0, pathExportFolder.length() - 1);
		}
		pathBaseFolder = pathExportFolder + "/" + nameExportFile;
		File baseFold = new File(pathBaseFolder); 
		// if folder exist delete it
		if(baseFold.exists()){
			ImpExpGeneralUtilities.deleteDir(baseFold);
		}
	    baseFold.mkdirs();
	    pathDBFolder =  pathBaseFolder + "/metadata";
	    File dbFold = new File(pathDBFolder); 
	    dbFold.mkdirs();
	    pathContentFolder = pathBaseFolder + "/contents";
        File contFold =new File(pathContentFolder); 
        contFold.mkdirs();
        ExportUtilities.copyMetadataScript(pathDBFolder);
        ExportUtilities.copyMetadataScriptProperties(pathDBFolder);
        sessionFactory = ExportUtilities.getHibSessionExportDB(pathDBFolder);
        session = sessionFactory.openSession();
        exporter = new ExporterMetadata();
        try{
    		connections = new SourceBean("CONNECTIONS");
    	} catch(Exception e) {
    		SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "prepareExport",
                    			   "Error while creating structure for exported connections " + e);
    		throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
    	}
	}
	
	
	
	/**
	 * Exports objects and creates the archive export file
	 * @param objPaths List of path of the objects to export
	 */
	public String exportObjects(List objIds) throws EMFUserError {	
		exportPropertiesFile();
		exportDomains();
		Iterator iterObjs = objIds.iterator();
		while(iterObjs.hasNext()){
			String idobj = (String)iterObjs.next();
			exportSingleObj(idobj);
		}
	    closeSession();
		exportConnectionFile(connections);
		String archivePath = createExportArchive();
		deleteTmpFolder();
		return archivePath;
	}	
		
	
	/**
	 * Delete the temporary folder created for export purpose
	 */
	private void deleteTmpFolder() {
		String folderTmpPath = pathExportFolder + "/" + nameExportFile;
		File folderTmp = new File(folderTmpPath);
		ImpExpGeneralUtilities.deleteDir(folderTmp);
	}
	
	
	/**
	 * Creates the compress export file
	 * @return The path of the exported compress file
	 * @throws EMFUserError
	 */
	private String createExportArchive() throws EMFUserError {
		String archivePath = pathExportFolder + "/" + nameExportFile + ".zip";
		File archiveFile = new File(archivePath);
		if(archiveFile.exists()){
			archiveFile.delete();
		}
		try{
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(archivePath));
			compressFolder(pathBaseFolder, out);
			out.flush();
			out.close();
		} catch (Exception e){
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "createExportArchive",
					   			   "Error while creating archive file " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		}
		return archivePath;
	}
	
	
	/**
	 * Compress contents of a folder into an output stream
	 * @param pathFolder The path of the folder to compress
	 * @param out The Compress output stream
	 * @throws EMFUserError
	 */
	private void compressFolder(String pathFolder, ZipOutputStream out) throws EMFUserError {
		File folder = new File(pathFolder);
		String[] entries = folder.list();
	    byte[] buffer = new byte[4096];   
	    int bytes_read;
	    try{
		    for(int i = 0; i < entries.length; i++) {
		      File f = new File(folder, entries[i]);
		      if(f.isDirectory()) {  
		    	  compressFolder(pathFolder + "/" + f.getName(), out); 
		      } else {
		    	  FileInputStream in = new FileInputStream(f); 
		    	  String completeFileName = pathFolder + "/" + f.getName();
		    	  String relativeFileName = f.getName();
		    	  if(completeFileName.lastIndexOf(pathExportFolder)!=-1) {
		    		  int index = completeFileName.lastIndexOf(pathExportFolder);
		    		  int len = pathExportFolder.length();
		    		  relativeFileName = completeFileName.substring(index + len + 1);
		    	  }
		    	  ZipEntry entry = new ZipEntry(relativeFileName);  
		    	  out.putNextEntry(entry);                     
		    	  while((bytes_read = in.read(buffer)) != -1)  
		    		  out.write(buffer, 0, bytes_read);
		    	  in.close();
		      }
		    }
	    } catch (Exception e) {
	    	SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "compressSingleFolder",
	    						   "Error while creating archive file " + e);
	    	throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
	    }
	}
	
	
	/**
	 * Creates the export properties file
	 * @throws EMFUserError
	 */
	private void exportPropertiesFile() throws EMFUserError {
		try{
			String propFilePath = pathBaseFolder + "/export.properties";
			FileOutputStream fos = new FileOutputStream(propFilePath);
			String properties = "spagobi-version=1.9.1\n";
			fos.write(properties.getBytes());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "exportPropertiesFile",
                    			  "Error while exporting properties file " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		}
	}
	
	/**
	 * Exports SpagoBI Domain Objects
	 * @throws EMFUserError
	 */
	private void exportDomains() throws EMFUserError {
		List domains = DAOFactory.getDomainDAO().loadListDomains();
		Iterator itDom = domains.iterator();
		while(itDom.hasNext()) {
			Domain dom = (Domain)itDom.next();
			exporter.insertDomain(dom, session);
		}
	}
	
	
    /**
     * Export A single SpagoBI BiObject
     * @param path The path of the biobject to export
     * @throws EMFUserError
     */
	private void exportSingleObj(String idObj) throws EMFUserError {
		if((idObj==null) || idObj.trim().equals(""))
			return;
		IBIObjectDAO biobjDAO = DAOFactory.getBIObjectDAO();
		BIObject biobj = biobjDAO.loadBIObjectForDetail(new Integer(idObj));
		exportTemplate(biobj, session);
		if(exportSubObjects){
			exportSubObjects(biobj);
		}
		Engine engine = biobj.getEngine();		
		exporter.insertEngine(engine, session);
		exporter.insertBIObject(biobj, session);
		
		// insert functionalities and association with object
		List functs = biobj.getFunctionalities();
		Iterator iterFunct = functs.iterator();
		while(iterFunct.hasNext()) {
			Integer functId = (Integer)iterFunct.next();
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
		while(itersub.hasNext()) {
			Subreport subRep = (Subreport)itersub.next();
			exporter.insertSubReportAssociation(subRep, session);
			exportSingleObj(subRep.getSub_rpt_id().toString());
		}
	}
	
	
	/**
	 * Export the template of a single SpagoBI Object
	 * @param biobj The BIObject to which the template belongs 
	 * @throws EMFUserError
	 */
	private void exportTemplate(BIObject biobj, Session session) throws EMFUserError {
		IBIObjectCMSDAO cmsdao = DAOFactory.getBIObjectCMSDAO();
		cmsdao.fillBIObjectTemplate(biobj);
		UploadedFile tempFile = biobj.getTemplate();
		byte[] tempFileCont = tempFile.getFileContent();
		
		try{
			if(biobj.getBiObjectTypeCode().equalsIgnoreCase("DASH")) {
				String tempFileStr = new String(tempFileCont);
				SourceBean tempFileSB = SourceBean.fromXMLString(tempFileStr);
				SourceBean datanameSB = (SourceBean)tempFileSB.getFilteredSourceBeanAttribute("DATA.PARAMETER", "name", "dataname");
				String lovLabel = (String)datanameSB.getAttribute("value");
			    IModalitiesValueDAO lovdao = DAOFactory.getModalitiesValueDAO();
			    ModalitiesValue lov = lovdao.loadModalitiesValueByLabel(lovLabel);
			    exporter.insertLov(lov, session);
			}
		} catch(Exception e) {
			SpagoBITracer.major(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "exportTemplate",
                                "Error while exporting lov referenced by template of biobj "+ biobj.getName() +" :" + e);
		}
		
		
		String tempFileName = tempFile.getFileName();
		String folderTempFilePath = pathContentFolder + biobj.getPath();
		File folderTempFile = new File(folderTempFilePath);
		folderTempFile.mkdirs();
		try{
			FileOutputStream fos = new FileOutputStream(folderTempFilePath + "/" + tempFileName);
			fos.write(tempFileCont);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "exportTemplate",
		                           "Error while exporting template file " + e);
            throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		}
	}
	
	
	/**
	 * Export the subobjects of a single SpagoBI Object
	 * @param biobj The BIObject to which the template belongs 
	 * @param path The path of the SpagoBI BIObject
	 * @throws EMFUserError
	 */
	
	private void exportSubObjects(BIObject biobj) throws EMFUserError {
		try{
			String folderSubObjPath = pathContentFolder + biobj.getPath() + "/subobjects";
			File folderSubObjFile = new File(folderSubObjPath);
			folderSubObjFile.mkdirs();
			IBIObjectCMSDAO cmsdao = DAOFactory.getBIObjectCMSDAO();
			List subObjs = cmsdao.getSubObjects(biobj.getPath());
			Iterator iterSubObjs = subObjs.iterator();
			while(iterSubObjs.hasNext()) {
				BIObject.SubObjectDetail subObj = (BIObject.SubObjectDetail)iterSubObjs.next();
				String nameSub = subObj.getName();
				String descr = subObj.getDescription();
				String owner = subObj.getOwner();
				String publicVisibility = "false";
				if(subObj.isPublicVisible())
					publicVisibility = "true";
				String propertiesStr = "name="+nameSub+"\ndescription="+descr+"\nowner="+owner+"\npubvis="+publicVisibility;
				FileOutputStream fos = new FileOutputStream(folderSubObjPath + "/" + nameSub + ".properties");
				fos.write(propertiesStr.getBytes());
				fos.flush();
				fos.close();
				InputStream subis = cmsdao.getSubObject(biobj.getPath(), nameSub);
				byte[] subcontent = GeneralUtilities.getByteArrayFromInputStream(subis);
				subis.close();
			    fos = new FileOutputStream(folderSubObjPath + "/" + nameSub + ".content");
			    fos.write(subcontent);
				fos.flush();
				fos.close();
			}	
		} catch (Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "exportSubObjects",
		                           "Error while exporting subobjects " + e);
            throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		}
	}
	
	
	/**
	 * Exports the BIParameters of a BIObject 
	 * @param biparams List ot the BIParameters belonging to the BIObject
	 * @param biobj The BIObject to which the parametes belong
	 * @throws EMFUserError
	 */
	private void exportBIParamsBIObj(List biparams, BIObject biobj) throws EMFUserError {
		Iterator iterBIParams = biparams.iterator();
		while(iterBIParams.hasNext()) {
			BIObjectParameter biparam = (BIObjectParameter)iterBIParams.next();
			IParameterDAO parDAO = DAOFactory.getParameterDAO();
			Parameter param = parDAO.loadForDetailByParameterID(biparam.getParameter().getId());
			exporter.insertParameter(param, session);
			exporter.insertBIObjectParameter(biparam, session);
			IParameterUseDAO paruseDAO = DAOFactory.getParameterUseDAO();
			List paruses = paruseDAO.loadParametersUseByParId(param.getId());
		    exportParUses(paruses);
		}
	}
	
	
	
	
	/**
	 * Export a list ot Parameter use Objects
	 * @param paruses The list of parameter use objects
	 * @throws EMFUserError
	 */
	private void exportParUses(List paruses) throws EMFUserError {
		Iterator iterUses = paruses.iterator();
		while(iterUses.hasNext()){
			ParameterUse paruse = (ParameterUse)iterUses.next();
            Integer idLov = paruse.getIdLov();
            if(idLov!=null) {
            	IModalitiesValueDAO lovDAO = DAOFactory.getModalitiesValueDAO();
            	ModalitiesValue lov = lovDAO.loadModalitiesValueByID(idLov);
            	checkConnection(lov, connections);
            	exporter.insertLov(lov, session);
            }
		    exporter.insertParUse(paruse, session);
		    List checks = paruse.getAssociatedChecks();
		    Iterator iterChecks = checks.iterator();
		    while(iterChecks.hasNext()){
		    	Check check = (Check)iterChecks.next();
		    	exporter.insertCheck(check, session);
		    	exporter.insertParuseCheck(paruse, check, session);
		    }
		    List roles = paruse.getAssociatedRoles();
		    exportRoles(roles);
		    Iterator iterRoles = roles.iterator();
		    while(iterRoles.hasNext()) {
		    	Role role = (Role)iterRoles.next();
		    	exporter.insertParuseRole(paruse, role, session);
		    } 
		}
	}
	
	
	/**
	 * Checks if a list of value object is a query type and in this case 
	 * exports the name of the SpagoBI connection pool associated to the query
	 * @param lov List of values Object
	 * @param conns SourceBean that defines the connection pools of the current SpagoBI platform
	 * @throws EMFUserError
	 */
	private void checkConnection(ModalitiesValue lov, SourceBean conns) throws EMFUserError {
		try {
			String type = lov.getITypeCd();
			if(type.equalsIgnoreCase("QUERY")) {
				String provider = lov.getLovProvider();
				QueryDetail queryDet = QueryDetail.fromXML(provider);
				String nameConnection = queryDet.getConnectionName();
				ConfigSingleton config = ConfigSingleton.getInstance();
				SourceBean conSB = null;
				conSB = (SourceBean)config.getFilteredSourceBeanAttribute("DATA-ACCESS.CONNECTION-POOL", 
						"connectionPoolName", nameConnection);
				if(conSB == null){
					 SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "checkConnection",
							  				"Connection pool name " +nameConnection+ " not found");
					 Vector paramsErr = new Vector();
					 paramsErr.add(lov.getLabel());
					 paramsErr.add(nameConnection);
					 throw new EMFUserError(EMFErrorSeverity.ERROR, 8008, paramsErr, "component_impexp_messages");
				} else {
					SourceBean existSB = (SourceBean)conns.getFilteredSourceBeanAttribute("CONNECTION-POOL", 
										  "connectionPoolName", nameConnection);
					if(existSB==null)
						conns.setAttribute(conSB);
				}
			}
		} catch (EMFUserError emfue){
			throw emfue;
		} catch (Exception e) {
			 SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "checkConnection",
								  "Error while checking connection" + e);
			 throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		}
	}
	
	
	
    /**
     * Export a single functionality
     * @param path The path of the fuctionality to export
     * @throws EMFUserError
     */
	private void exportFunctionalities(String path) throws EMFUserError {
		List pathFuncts = new ArrayList();
		String pathTmp = path;
		while((pathTmp.lastIndexOf('/')!=-1) && (pathTmp.lastIndexOf('/')!=0)){
			int posLast = pathTmp.lastIndexOf('/');
			pathTmp = pathTmp.substring(0, posLast);
			pathFuncts.add(pathTmp);
		}
		Iterator iterPathFuncts = pathFuncts.iterator();
		while(iterPathFuncts.hasNext()){
			String pathFunct = (String)iterPathFuncts.next();
			ILowFunctionalityDAO functDAO = DAOFactory.getLowFunctionalityDAO();
			LowFunctionality funct = functDAO.loadLowFunctionalityByPath(pathFunct, false);
			if(funct == null) {
				continue;
			}
			exporter.insertFunctionality(funct, session);
			Role[] devRoles = funct.getDevRoles();
			Role[] testRoles = funct.getTestRoles();
			Role[] execRoles = funct.getExecRoles();
			List devRolesList = Arrays.asList(devRoles);
			List testRolesList = Arrays.asList(testRoles);
			List execRolesList = Arrays.asList(execRoles);
			List allRoles = new ArrayList();
			Collections.addAll(allRoles, devRoles);
			Collections.addAll(allRoles, testRoles);
			Collections.addAll(allRoles, execRoles);
			exportRoles(allRoles);
			exportFunctRoles(devRolesList, funct, "DEV");
			exportFunctRoles(testRolesList, funct, "TEST");
			exportFunctRoles(execRolesList, funct, "REL");
		}
	}
	
	
	
	
	/**
	 * Export an association between a functionality and a list of roles
	 * @param roles The list of roles to associate to the functionality
	 * @param funct The functionality which is part of the association
	 * @param state The state of the association
	 * @throws EMFUserError
	 */
	private void exportFunctRoles(List roles, LowFunctionality funct, String state) throws EMFUserError {
		IDomainDAO domDAO = DAOFactory.getDomainDAO();
		Domain stateDom = domDAO.loadDomainByCodeAndValue("STATE", state);
		Integer idState = stateDom.getValueId();
        Iterator iterRoles = roles.iterator();
        while(iterRoles.hasNext()) {
        	Role role = (Role)iterRoles.next();
        	exporter.insertFunctRole(role, funct, idState, state, session);
        }
	}
	
		
	
	/**
	 * Export a list of SpagoBI roles
	 * @param roles The list of roles to export
	 * @throws EMFUserError
	 */
	private void exportRoles(List roles) throws EMFUserError {
		Iterator iterRoles = roles.iterator();
		while(iterRoles.hasNext()) {
			Role role = (Role)iterRoles.next();
			exporter.insertRole(role, session);
		}
	}
	
	
	
	/**
	 * Creates the file describing the connections expoted
	 * @param conns SourceBean describing the connections to export
	 * @throws EMFUserError
	 */
	private void exportConnectionFile(SourceBean conns) throws EMFUserError {
		try{
			String connFilePath = pathBaseFolder + "/connections.xml";
			FileOutputStream fos = new FileOutputStream(connFilePath);
            String xmlString = conns.toXML(false);
			fos.write(xmlString.getBytes());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "exportConnectionFile",
                    			  "Error while exporting connection file " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		}
	}

	
	
	/**
	 * Close hibernate session and session factory relative to the export database
	 */
	private void closeSession() {
		if(session!=null){
			if(session.isOpen())
				session.close();
		}
		if(sessionFactory!=null){
			sessionFactory.close();
		}
	}



	/**
	 * Clean the export environment (close sessions and delete temporary files)
	 */
	public void cleanExportEnvironment() {
		closeSession();
		deleteTmpFolder();
	}
	
}
