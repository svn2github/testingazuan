package it.eng.spagobi.importexport.version18;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
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
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectCMSDAO;
import it.eng.spagobi.bo.dao.IBIObjectDAO;
import it.eng.spagobi.bo.dao.IDomainDAO;
import it.eng.spagobi.bo.dao.ILowFunctionalityDAO;
import it.eng.spagobi.bo.dao.IModalitiesValueDAO;
import it.eng.spagobi.bo.dao.IParameterDAO;
import it.eng.spagobi.bo.dao.IParameterUseDAO;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.importexport.IExportManager;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

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
	
	
	
	public void prepareExport(String pathExpFold, String nameExpFile, boolean expSubObj) throws EMFUserError {
		nameExportFile = nameExpFile;
		pathExportFolder = pathExpFold;
		exportSubObjects = expSubObj;
		if(pathExportFolder.endsWith("/") || pathExportFolder.endsWith("\\") ) {
			pathExportFolder = pathExportFolder.substring(0, pathExportFolder.length() - 1);
		}
		pathBaseFolder = pathExportFolder + "/" + nameExportFile;
		File baseFold = new File(pathBaseFolder); 
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
    		SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "prepareExport",
                    			   "Error while creating structure for exported connections " + e);
    		throw new EMFUserError(EMFErrorSeverity.ERROR, 8005);
    	}
	}
	
	
	
	
	public String exportObjects(List objPaths) throws EMFUserError {
		
		exportPropertiesFile();
		Iterator iterObjs = objPaths.iterator();
		while(iterObjs.hasNext()){
			String path = (String)iterObjs.next();
			exportSingleObj(path);
		}
	    closeSession();
		exportConnectionFile(connections);
		String archivePath = createExportArchive();
		deleteTmpFolder();
		return archivePath;
	}	
		
	
	private void deleteTmpFolder() {
		String folderTmpPath = pathExportFolder + "/" + nameExportFile;
		File folderTmp = new File(folderTmpPath);
		ImpExpGeneralUtilities.deleteDir(folderTmp);
	}
	
	
	
	private String createExportArchive() throws EMFUserError {
		String archivePath = pathExportFolder + "/" + nameExportFile + ".zip";
		try{
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(archivePath));
			compressFolder(pathBaseFolder, out);
			out.flush();
			out.close();
		} catch (Exception e){
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "createExportArchive",
					   			   "Error while creating archive file " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8005);
		}
		return archivePath;
	}
	
	
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
	    	SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "compressSingleFolder",
	    						   "Error while creating archive file " + e);
	    	throw new EMFUserError(EMFErrorSeverity.ERROR, 8005);
	    }
	}
	
	
	
	private void exportPropertiesFile() throws EMFUserError {
		try{
			ConfigSingleton conf = ConfigSingleton.getInstance();
			SourceBean pathSysFunctSB = (SourceBean)conf.getAttribute("SPAGOBI.CMS_PATHS.SYSTEM_FUNCTIONALITIES_PATH");
		    String pathSysFunct = pathSysFunctSB.getCharacters();
			String propFilePath = pathBaseFolder + "/export.properties";
			FileOutputStream fos = new FileOutputStream(propFilePath);
			String properties = "spagobi-version=1.8\n"+
								"cms-basefolder="+pathSysFunct+"\n";
			fos.write(properties.getBytes());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "exportPropertiesFile",
                    			  "Error while exporting properties file " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8005);
		}
	}
	
	
	
	
	

	private void exportSingleObj(String path) throws EMFUserError {
		if((path==null) || path.trim().equals(""))
			return;
		IBIObjectDAO biobjDAO = DAOFactory.getBIObjectDAO();
		BIObject biobj = biobjDAO.loadBIObjectForDetail(path);
		exportTemplate(biobj, path);
		if(exportSubObjects){
			exportSubObjects(biobj, path);
		}
		Engine engine = biobj.getEngine();		
		exporter.insertEngine(engine, session);
		exporter.insertBIObject(biobj, session);
		exportFunctionalities(path);
		List biparams = biobjDAO.getBIObjectParameters(biobj);
		exportBIParamsBIObj(biparams, biobj);
	}
	
	
	
	private void exportTemplate(BIObject biobj, String path) throws EMFUserError {
		IBIObjectCMSDAO cmsdao = DAOFactory.getBIObjectCMSDAO();
		cmsdao.fillBIObjectTemplate(biobj);
		UploadedFile tempFile = biobj.getTemplate();
		byte[] tempFileCont = tempFile.getFileContent();
		String tempFileName = tempFile.getFileName();
		
		String folderTempFilePath = pathContentFolder + path;
		File folderTempFile = new File(folderTempFilePath);
		folderTempFile.mkdirs();
		try{
			FileOutputStream fos = new FileOutputStream(folderTempFilePath + "/" + tempFileName);
			fos.write(tempFileCont);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "exportTemplate",
		                           "Error while exporting template file " + e);
            throw new EMFUserError(EMFErrorSeverity.ERROR, 8005);
		}
	}
	
	
	
	private void exportSubObjects(BIObject biobj, String path) throws EMFUserError {
		try{
			String folderSubObjPath = pathContentFolder + path + "/subobjects";
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
			    fos = new FileOutputStream(folderSubObjPath + "/" + nameSub + ".content");
			    fos.write(subcontent);
				fos.flush();
				fos.close();
			}	
		} catch (Exception e) {
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "exportSubObjects",
		                           "Error while exporting subobjects " + e);
            throw new EMFUserError(EMFErrorSeverity.ERROR, 8005);
		}
	}
	
	
	
	
	private void exportBIParamsBIObj(List biparams, BIObject biobj) throws EMFUserError {
		Iterator iterBIParams = biparams.iterator();
		while(iterBIParams.hasNext()) {
			BIObjectParameter biparam = (BIObjectParameter)iterBIParams.next();
			exporter.insertBIObjectParameter(biparam, biobj, session);
			IParameterDAO parDAO = DAOFactory.getParameterDAO();
			Parameter param = parDAO.loadForDetailByParameterID(biparam.getParameter().getId());
			exporter.insertParameter(param, session);
			IParameterUseDAO paruseDAO = DAOFactory.getParameterUseDAO();
			List paruses = paruseDAO.loadParametersUseByParId(param.getId());
		    exportParUses(paruses);
		}
	}
	
	
	
	
	
	private void exportParUses(List paruses) throws EMFUserError {
		Iterator iterUses = paruses.iterator();
		while(iterUses.hasNext()){
			ParameterUse paruse = (ParameterUse)iterUses.next();
            Integer idLov = paruse.getIdLov();
            IModalitiesValueDAO lovDAO = DAOFactory.getModalitiesValueDAO();
            ModalitiesValue lov = lovDAO.loadModalitiesValueByID(idLov);
            checkConnection(lov, connections);
		    exporter.insertLov(lov, session);
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
					 SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "checkConnection",
							  				"Connection pool name " +nameConnection+ " not found");
					 throw new EMFInternalError(EMFErrorSeverity.ERROR, "Connection pool name " +nameConnection+ " not found");
				} else {
					SourceBean existSB = (SourceBean)conns.getFilteredSourceBeanAttribute("CONNECTION-POOL", 
										  "connectionPoolName", nameConnection);
					if(existSB==null)
						conns.setAttribute(conSB);
				}
			}
		} catch (Exception e) {
			 SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "checkConnection",
								  "Error while checking connection" + e);
			 throw new EMFUserError(EMFErrorSeverity.ERROR, 8005);
		}
	}
	
	
	
    
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
			LowFunctionality funct = functDAO.loadLowFunctionalityByPath(pathFunct);
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
	
		
	
	
	private void exportRoles(List roles) throws EMFUserError {
		Iterator iterRoles = roles.iterator();
		while(iterRoles.hasNext()) {
			Role role = (Role)iterRoles.next();
			exporter.insertRole(role, session);
		}
	}
	
	
	
	
	private void exportConnectionFile(SourceBean conns) throws EMFUserError {
		try{
			String connFilePath = pathBaseFolder + "/connections.xml";
			FileOutputStream fos = new FileOutputStream(connFilePath);
            String xmlString = conns.toXML(false);
			fos.write(xmlString.getBytes());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "exportConnectionFile",
                    			  "Error while exporting connection file " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8005);
		}
	}

	
	
	
	private void closeSession() {
		if(session!=null){
			if(session.isOpen())
				session.close();
		}
		if(sessionFactory!=null){
			sessionFactory.close();
		}
	}




	public void cleanExportEnvironment() {
		closeSession();
		deleteTmpFolder();
	}
	
}
