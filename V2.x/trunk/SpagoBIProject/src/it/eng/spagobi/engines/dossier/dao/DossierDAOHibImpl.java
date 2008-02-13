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
package it.eng.spagobi.engines.dossier.dao;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.bo.ObjTemplate;
import it.eng.spagobi.analiticalmodel.document.dao.IBIObjectDAO;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.engines.dossier.bo.ConfiguredBIDocument;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;

/**
 * 
 * @author Zerbetto (davide.zerbetto@eng.it)
 * IDossierDAO implementation using database and hibernate mapping
 */
public class DossierDAOHibImpl implements IDossierDAO {

	public static final String TEMPLATE_FILE_NAME = "dossierTemplate.zip";
	
	static private Logger logger = Logger.getLogger(DossierDAOHibImpl.class);
	
	public static File tempBaseFolder = null;
	
	static {
	    ConfigSingleton config = ConfigSingleton.getInstance();
	    SourceBean pathTmpFoldSB = (SourceBean) config.getAttribute("BOOKLETS.PATH_TMP_FOLDER");
	    String pathTmpFold = (String) pathTmpFoldSB.getAttribute("path");
	    logger.debug("Base temporary dossier folder =" + pathTmpFold);
	    if (!pathTmpFold.startsWith("/") && pathTmpFold.charAt(1) != ':') {
	    	String root = ConfigSingleton.getRootPath();
	    	pathTmpFold = root + "/" + pathTmpFold;
	    }
	    tempBaseFolder = new File(pathTmpFold);
	    tempBaseFolder.mkdirs();
	}
	
	public void unzipTemplate(BIObject dossier) {
		logger.debug("IN");
		try {
			ObjTemplate objTemplate = dossier.getActiveTemplate();
			byte[] bytes = objTemplate.getContent();
			// TODO vedere se si può fare meglio di così
			cleanDossierTempFolder(dossier);
			File tempFolder = getDossierTempFolder(dossier);
			File template = new File(tempFolder.getAbsolutePath() + "/" + TEMPLATE_FILE_NAME);
			FileOutputStream fos = new FileOutputStream(template);
		    fos.write(bytes);
		    fos.flush();
		    fos.close();
		    bytes = null;
			ZipFile zip = new ZipFile(template);
			Enumeration entries = zip.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				copyInputStream(zip.getInputStream(entry),
				           new BufferedOutputStream(new FileOutputStream(tempFolder.getAbsolutePath() + "/" + entry.getName())));
			}
			zip.close();
			template.delete();
		} catch (Exception e) {
			logger.error(e);
		} finally {
			logger.debug("OUT");
		}
	}
	
	public void storeTemplate(BIObject dossier) throws EMFUserError {
		logger.debug("IN");
		File template = null;
		try {
			File presentationTemplate = getTemporaryPresentationTemplate(dossier);
			File processDefinitionFile = getTemporaryProcessDefinitionFile(dossier);
			File dossierConfigFile = getTemporaryDossierConfigurationFile(dossier);
			List docs = getConfiguredDocumentList(dossier);
			if (presentationTemplate == null) {
				EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, 104);
				throw error;
			}
			if (processDefinitionFile == null) {
				EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, 105);
				throw error;
			}
			if (docs == null || docs.size() == 0) {
				EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, 106);
				throw error;
			}
			String[] files = new String[] {
					presentationTemplate.getAbsolutePath(), 
					processDefinitionFile.getAbsolutePath(), 
					dossierConfigFile.getAbsolutePath()
			};
			template = generateTemplateZipFile(files, dossier);
			ObjTemplate objTemplate = generateObjTemplate(template);
			IBIObjectDAO objDAO = DAOFactory.getBIObjectDAO();
			objDAO.modifyBIObject(dossier, objTemplate);
		} catch (EMFUserError e) {
			throw e;
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if (template != null) template.delete();
			logger.debug("OUT");
		}
	}
	
	private byte[] read(File file) throws IOException {
	   InputStream in = new FileInputStream(file);
	   byte[] data = new byte[in.available()];
	   in.read(data);
	   return data;
	}

	private ObjTemplate generateObjTemplate(File templateFile) throws IOException {
		ObjTemplate objTemplate = new ObjTemplate();
		objTemplate.setActive(new Boolean(true));
		objTemplate.setName(TEMPLATE_FILE_NAME);
        byte[] uplCont = read(templateFile);
        objTemplate.setContent(uplCont);
		return objTemplate;
	}
	
	private File generateTemplateZipFile(String[] files, BIObject dossier) throws Exception {
		logger.debug("IN");
		File template = null;
		ZipOutputStream out = null;
		try {
			File tempFolder = getDossierTempFolder(dossier);
			template = new File(tempFolder.getAbsolutePath() + "/" + TEMPLATE_FILE_NAME);
			if (template.exists()) template.delete();
			out = new ZipOutputStream(new FileOutputStream(template));
	        for (int i = 0; i < files.length; i++) {
	        	addEntry(out, files[i]);
	        }
		} finally {
			if (out != null) out.close();
			logger.debug("OUT");
		}
		return template;
	}
	
	private void addEntry(ZipOutputStream out, String filePath) throws IOException {
		logger.debug("IN");
		FileInputStream in = null;
		try {
			byte[] buf = new byte[1024];
	    	in = new FileInputStream(filePath);
	        // Add ZIP entry to output stream.
	        String fileName = filePath;
			if (fileName.contains("/")) {
				fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
			}
			if (fileName.contains("\\")) {
				fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
			}
	        out.putNextEntry(new ZipEntry(fileName));
	        // Transfer bytes from the file to the ZIP file
	        int len;
	        while ((len = in.read(buf)) > 0) {
	            out.write(buf, 0, len);
	        }
	        // Complete the entry
	        out.closeEntry();
		} finally {
			if (in != null) in.close();
			logger.debug("OUT");
		}
	}
	
	public static final void copyInputStream(InputStream in, OutputStream out) throws IOException {
	    byte[] buffer = new byte[1024];
	    int len;
	    while((len = in.read(buffer)) >= 0)
	      out.write(buffer, 0, len);
	    in.close();
	    out.close();
	  }
	
	public void addConfiguredDocument(BIObject dossier,
			ConfiguredBIDocument doc) {
		logger.debug("IN");
		try {
			File docsConfFile = getTemporaryDossierConfigurationFile(dossier);
			SourceBean docsConfSb = null;
			if (docsConfFile != null && docsConfFile.exists()) {
				InputSource stream = new InputSource(new FileReader(docsConfFile));
				docsConfSb = SourceBean.fromXMLStream(stream, true, false);
				docsConfFile.delete();
			} else {
				docsConfFile = createTemporaryDocumentsConfigurationFile(dossier);
				docsConfSb = new SourceBean("DOSSIER_CONFIGURATION");
			}
			String docConfXml = doc.toXml();
			SourceBean docConfSb = SourceBean.fromXMLString(docConfXml, true, false);
			if (docsConfSb.containsAttribute(doc.getLogicalName())) {
				logger.warn("Configuration xml file already contains " + doc.getLogicalName() + " attribute. It will be update");
				docsConfSb.delAttribute(doc.getLogicalName());
			}
			docsConfSb.setAttribute(docConfSb);
			FileOutputStream fos = new FileOutputStream(docsConfFile);
		    fos.write(docsConfSb.toXML(false).getBytes());
		    fos.flush();
		    fos.close();
		} catch (Exception e) {
			logger.error(e);
		} finally {
			logger.debug("OUT");
		}
	}

	private File getTemporaryPresentationTemplate(BIObject dossier) {
		logger.debug("IN");
		File toReturn = null;
		File dossierTempFolder = getDossierTempFolder(dossier);
		File[] files = dossierTempFolder.listFiles();
		if (files != null && files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.getName().toUpperCase().endsWith(".PPT")) {
					toReturn = file;
					break;
				}
			}
		}
		logger.debug("OUT");
		return toReturn;
	}
	
	private File getTemporaryProcessDefinitionFile(BIObject dossier) {
		logger.debug("IN");
		File toReturn = null;
		File dossierTempFolder = getDossierTempFolder(dossier);
		File[] files = dossierTempFolder.listFiles();
		if (files != null && files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.getName().toUpperCase().endsWith(".XML")) {
					toReturn = file;
					break;
				}
			}
		}
		logger.debug("OUT");
		return toReturn;
	}
	
	private File getTemporaryDossierConfigurationFile(BIObject dossier) {
		logger.debug("IN");
		File toReturn = null;
		File dossierTempFolder = getDossierTempFolder(dossier);
		File[] files = dossierTempFolder.listFiles();
		if (files != null && files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.getName().toUpperCase().endsWith(".SBIDOSSIER")) {
					toReturn = file;
					break;
				}
			}
		}
		logger.debug("OUT");
		return toReturn;
	}
	
	private File createTemporaryDocumentsConfigurationFile(BIObject dossier) {
		logger.debug("IN");
		File toReturn = null;
		File dossierTempFolder = getDossierTempFolder(dossier);
		toReturn = new File(dossierTempFolder.getAbsolutePath() + "/dossier-config.sbidossier");
		logger.debug("OUT");
		return toReturn;
	}
	
	private File getDossierTempFolder(BIObject dossier) {
		logger.debug("IN");
		String uuid = dossier.getUuid();
		String tempBasePath = tempBaseFolder.getAbsolutePath();
		File toReturn = new File(tempBasePath + "/" + uuid);
		toReturn.mkdir();
		logger.debug("OUT");
		return toReturn;
	}

	private boolean cleanDossierTempFolder(BIObject dossier) {
		logger.debug("IN");
		String uuid = dossier.getUuid();
		String tempBasePath = tempBaseFolder.getAbsolutePath();
		File folder = new File(tempBasePath + "/" + uuid);
		boolean toReturn = deleteFolder(folder);
		logger.debug("OUT");
		return toReturn;
	}
	
	public static boolean deleteFolder(File directory) {
		try {
			if (directory.isDirectory()) {
				File[] files = directory.listFiles();
				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					if (file.isFile()) {
						boolean deletion = file.delete();
						if (!deletion)
							return false;
					} else
						deleteFolder(file);
				}
			}
			boolean deletion = directory.delete();
			if (!deletion)
				return false;
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public void deleteConfiguredDocument(BIObject dossier,
			String docLogicalName) {
		logger.debug("IN");
		try {
			File docsConfFile = getTemporaryDossierConfigurationFile(dossier);
			SourceBean docsConfSb = null;
			if (docsConfFile != null && docsConfFile.exists()) {
				InputSource stream = new InputSource(new FileReader(docsConfFile));
				docsConfSb = SourceBean.fromXMLStream(stream, true, false);
				docsConfFile.delete();
			} else {
				logger.warn("Dossier configuration file not found.");
				return;
			}
			docsConfSb.delAttribute(docLogicalName);
			FileOutputStream fos = new FileOutputStream(docsConfFile);
		    fos.write(docsConfSb.toXML(false).getBytes());
		    fos.flush();
		    fos.close();
		} catch (Exception e) {
			logger.error(e);
		} finally {
			logger.debug("OUT");
		}

	}

	
	// TODO implementare
	public void deletePresentationVersion(String pathBooklet, String verName) {
		logger.debug("IN");
		try {
			
		} finally {
			logger.debug("OUT");
		}

	}

	// TODO controllare a che serve
	public String getDossierName(String pathBooklet) {
		logger.debug("IN");
		String biobjname = "";
		try {
			if(!pathBooklet.endsWith("/template")) {
				return "";
			}
			try{
				String pathBiObj = pathBooklet.substring(0, pathBooklet.lastIndexOf("/"));
				IBIObjectDAO objectDAO = DAOFactory.getBIObjectDAO();
				BIObject biobj = objectDAO.loadBIObjectForDetail(pathBiObj);
				biobjname = biobj.getName();
			} catch (Exception e){
				logger.error("Cannot recover booklet name ", e);
			}
		} finally {
			logger.debug("OUT");
		}
		return biobjname;
	}

	public InputStream getProcessDefinitionContent(BIObject dossier) {
		logger.debug("IN");
		InputStream is = null;
		try {
			File processDefFile = getTemporaryProcessDefinitionFile(dossier);
			is = new FileInputStream(processDefFile);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			logger.debug("OUT");
		}
		return is;
	}

	public String getProcessDefinitionFileName(BIObject dossier) {
		logger.debug("IN");
		String toReturn = null;
		try {
			File processDefFile = getTemporaryProcessDefinitionFile(dossier);
			toReturn = processDefFile.getName();
			if (toReturn.contains("/")) {
				toReturn = toReturn.substring(toReturn.lastIndexOf("/") + 1);
			}
			if (toReturn.contains("\\")) {
				toReturn = toReturn.substring(toReturn.lastIndexOf("\\") + 1);
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			logger.debug("OUT");
		}
		return toReturn;
	}

	public InputStream getPresentationTemplateContent(BIObject dossier) {
		logger.debug("IN");
		InputStream is = null;
		try {
			File presentationTemplate = getTemporaryPresentationTemplate(dossier);
			is = new FileInputStream(presentationTemplate);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			logger.debug("OUT");
		}
		return is;
	}

	public String getPresentationTemplateFileName(BIObject dossier) {
		logger.debug("IN");
		String toReturn = null;
		try {
			File presentationTemplate = getTemporaryPresentationTemplate(dossier);
			toReturn = presentationTemplate.getName();
			if (toReturn.contains("/")) {
				toReturn = toReturn.substring(toReturn.lastIndexOf("/"));
			}
			if (toReturn.contains("\\")) {
				toReturn = toReturn.substring(toReturn.lastIndexOf("\\"));
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			logger.debug("OUT");
		}
		return toReturn;
	}

	public ConfiguredBIDocument getConfiguredDocument(BIObject dossier, String docLogicalName) {
		logger.debug("IN");
		ConfiguredBIDocument toReturn = null;
		try {
			File docsConfigured = getTemporaryDossierConfigurationFile(dossier);
			InputSource stream = new InputSource(new FileReader(docsConfigured));
			SourceBean docsConfiguredSb = SourceBean.fromXMLStream(stream, true, false);
			SourceBean docSb = (SourceBean) docsConfiguredSb.getAttribute(docLogicalName);
			toReturn = ConfiguredBIDocument.fromXml(docSb);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			logger.debug("OUT");
		}
		return toReturn;
	}

	public List getConfiguredDocumentList(BIObject dossier) {
		logger.debug("IN");
		List toReturn = new ArrayList();
		try {
			File docsConfigured = getTemporaryDossierConfigurationFile(dossier);
			if (docsConfigured != null && docsConfigured.exists()) {
				InputSource stream = new InputSource(new FileReader(docsConfigured));
				SourceBean docsConfiguredSb = SourceBean.fromXMLStream(stream, true, false);
				List docsConfiguredList = docsConfiguredSb.getContainedAttributes();
				if (docsConfiguredList != null && docsConfiguredList.size() > 0) {
					Iterator it = docsConfiguredList.iterator();
					while (it.hasNext()) {
						SourceBeanAttribute docSbSttr = (SourceBeanAttribute) it.next();
						SourceBean docSb = (SourceBean) docSbSttr.getValue();
						ConfiguredBIDocument doc = ConfiguredBIDocument.fromXml(docSb);
						toReturn.add(doc);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			logger.debug("OUT");
		}
		return toReturn;
	}

	public byte[] getCurrentPresentationContent(String pathBooklet) {
		logger.debug("IN");
		try {
			return null;
		} finally {
			logger.debug("OUT");
		}
	}

	public Map getImagesOfTemplatePart(String pathBooklet, String indPart) {
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] getNotesTemplatePart(String pathBooklet, String indPart) {
		logger.debug("IN");
		try {
			return null;
		} finally {
			logger.debug("OUT");
		}
	}

	public byte[] getPresentationVersionContent(String pathBooklet,
			String verName) {
		logger.debug("IN");
		try {
			return null;
		} finally {
			logger.debug("OUT");
		}
	}

	public List getPresentationVersions(String pathBooklet) {
		logger.debug("IN");
		try {
			return null;
		} finally {
			logger.debug("OUT");
		}
	}

	public void storeProcessDefinitionFile(BIObject dossier,
			String pdFileName, byte[] pdFileContent) {
		logger.debug("IN");
		try {
			File dossierTempFolder = getDossierTempFolder(dossier);
			// deletes previous xml files, if any
			deleteContainedFiles(dossierTempFolder, ".xml");
			File processDefFile = new File(dossierTempFolder.getAbsolutePath() + "/" + pdFileName);
			FileOutputStream fos = new FileOutputStream(processDefFile);
		    fos.write(pdFileContent);
		    fos.flush();
		    fos.close();
		} catch (Exception e) {
			logger.error(e);
		} finally {
			logger.debug("OUT");
		}
	}

	public void storePresentationTemplateFile(BIObject dossier,
			String templateFileName, byte[] templateContent) {
		logger.debug("IN");
		try {
			File dossierTempFolder = getDossierTempFolder(dossier);
			deleteContainedFiles(dossierTempFolder, ".ppt");
			File presentationTemplateFile = new File(dossierTempFolder.getAbsolutePath() + "/" + templateFileName);
			FileOutputStream fos = new FileOutputStream(presentationTemplateFile);
		    fos.write(templateContent);
		    fos.flush();
		    fos.close();
		} catch (Exception e) {
			logger.error(e);
		} finally {
			logger.debug("OUT");
		}

	}

	private void deleteContainedFiles(File folder, String extension) {
		logger.debug("IN");
		try {
			extension = extension.toUpperCase();
			File[] files = folder.listFiles();
			for (int i = 0; i < files.length; i++) {
				File aFile = files[i];
				if (aFile.getName().toUpperCase().endsWith(extension)) {
					logger.debug("Deleting file " + aFile.getAbsolutePath());
					aFile.delete();
				}
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			logger.debug("OUT");
		}
	}
	
	public void storeCurrentPresentationContent(String pathBooklet,
			byte[] docContent) {
		// TODO Auto-generated method stub

	}

	public void storeCurrentPresentationContent(String pathBooklet,
			InputStream docContentIS) {
		logger.debug("IN");
		try {
		} finally {
			logger.debug("OUT");
		}

	}

	public void storeNote(String pathBooklet, String indPart, byte[] noteContent) {
		// TODO Auto-generated method stub

	}

	public void storeTemplateImage(String pathBooklet, byte[] image,
			String docLogicalName, int indexTempPart) {
		logger.debug("IN");
		try {
		} finally {
			logger.debug("OUT");
		}

	}

	public void versionPresentation(String pathBooklet, byte[] presContent,
			boolean approved) {
		logger.debug("IN");
		try {
		} finally {
			logger.debug("OUT");
		}

	}

}
