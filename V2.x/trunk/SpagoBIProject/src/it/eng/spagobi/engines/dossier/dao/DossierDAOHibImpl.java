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
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;
import org.xml.sax.InputSource;

/**
 * 
 * @author Zerbetto (davide.zerbetto@eng.it)
 * IDossierDAO implementation using database and hibernate mapping
 */
public class DossierDAOHibImpl implements IDossierDAO {

	public static final String TEMPLATE_FILE_NAME = "dossierTemplate.zip";
	public static final String DOSSIER_CONF_FILE_NAME = "dossier-config.sbidossier";
	
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
	
	public String init(BIObject dossier) {
		logger.debug("IN");
		try {
			// TODO vedere se si può fare meglio di così
			UUIDGenerator uuidGenerator = UUIDGenerator.getInstance();
			UUID uuidObj = uuidGenerator.generateTimeBasedUUID();
			String uuid = uuidObj.toString();
			File tempFolder = new File(tempBaseFolder.getAbsolutePath() + "/" + dossier.getId().toString() + "/" + uuid);
			if (tempFolder.exists()) deleteFolder(tempFolder);
			tempFolder.mkdirs();
			File template = new File(tempFolder.getAbsolutePath() + "/" + TEMPLATE_FILE_NAME);
			ObjTemplate objTemplate = dossier.getActiveTemplate();
			if (objTemplate != null) {
				byte[] bytes = objTemplate.getContent();
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
			}
			return tempFolder.getAbsolutePath();
		} catch (Exception e) {
			logger.error(e);
			return null;
		} finally {
			logger.debug("OUT");
		}
	}
	
	public void storeTemplate(Integer dossierId, String pathTempFolder) {
		logger.debug("IN");
		File template = null;
		try {
			File presentationTemplate = getTemporaryPresentationTemplate(pathTempFolder);
			File processDefinitionFile = getTemporaryProcessDefinitionFile(pathTempFolder);
			File dossierConfigFile = getTemporaryDossierConfigurationFile(pathTempFolder);
			String[] files = new String[] {
					presentationTemplate.getAbsolutePath(), 
					processDefinitionFile.getAbsolutePath(), 
					dossierConfigFile.getAbsolutePath()
			};
			template = generateTemplateZipFile(files, pathTempFolder);
			ObjTemplate objTemplate = generateObjTemplate(template);
			IBIObjectDAO objDAO = DAOFactory.getBIObjectDAO();
			BIObject dossier = objDAO.loadBIObjectById(dossierId);
			objDAO.modifyBIObject(dossier, objTemplate);
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
	
	private File generateTemplateZipFile(String[] files, String pathTempFolder) throws Exception {
		logger.debug("IN");
		File template = null;
		ZipOutputStream out = null;
		try {
			template = new File(pathTempFolder + "/" + TEMPLATE_FILE_NAME);
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
	
	public void addConfiguredDocument(ConfiguredBIDocument doc, String pathTempFolder) {
		logger.debug("IN");
		try {
			File docsConfFile = getTemporaryDossierConfigurationFile(pathTempFolder);
			SourceBean docsConfSb = null;
			if (docsConfFile != null && docsConfFile.exists()) {
				InputSource stream = new InputSource(new FileReader(docsConfFile));
				docsConfSb = SourceBean.fromXMLStream(stream, true, false);
				docsConfFile.delete();
			} else {
				docsConfFile = createTemporaryDocumentsConfigurationFile(pathTempFolder);
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

	private File getTemporaryPresentationTemplate(String pathTempFolder) {
		logger.debug("IN");
		File toReturn = null;
		File dossierTempFolder = new File(pathTempFolder);
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
	
	private File getTemporaryProcessDefinitionFile(String pathTempFolder) {
		logger.debug("IN");
		File toReturn = null;
		File dossierTempFolder = new File(pathTempFolder);
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
	
	private File getTemporaryDossierConfigurationFile(String pathTempFolder) {
		logger.debug("IN");
		File toReturn = null;
		File dossierTempFolder = new File(pathTempFolder);
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
	
	private File createTemporaryDocumentsConfigurationFile(String pathTempFolder) {
		logger.debug("IN");
		File toReturn = null;
		toReturn = new File(pathTempFolder + "/" + DOSSIER_CONF_FILE_NAME);
		logger.debug("OUT");
		return toReturn;
	}
	
	public static boolean deleteFolder(File directory) {
		logger.debug("IN");
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
		} finally {
			logger.debug("OUT");
		}
		return true;
	}
	
	public void deleteConfiguredDocument(String docLogicalName, String pathTempFolder) {
		logger.debug("IN");
		try {
			File docsConfFile = getTemporaryDossierConfigurationFile(pathTempFolder);
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

	public InputStream getProcessDefinitionContent(String pathTempFolder) {
		logger.debug("IN");
		InputStream is = null;
		try {
			File processDefFile = getTemporaryProcessDefinitionFile(pathTempFolder);
			is = new FileInputStream(processDefFile);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			logger.debug("OUT");
		}
		return is;
	}

	public String getProcessDefinitionFileName(String pathTempFolder) {
		logger.debug("IN");
		String toReturn = null;
		try {
			File processDefFile = getTemporaryProcessDefinitionFile(pathTempFolder);
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

	public InputStream getPresentationTemplateContent(String pathTempFolder) {
		logger.debug("IN");
		InputStream is = null;
		try {
			File presentationTemplate = getTemporaryPresentationTemplate(pathTempFolder);
			is = new FileInputStream(presentationTemplate);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			logger.debug("OUT");
		}
		return is;
	}

	public String getPresentationTemplateFileName(String pathTempFolder) {
		logger.debug("IN");
		String toReturn = null;
		try {
			File presentationTemplate = getTemporaryPresentationTemplate(pathTempFolder);
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

	public ConfiguredBIDocument getConfiguredDocument(String docLogicalName, String pathTempFolder) {
		logger.debug("IN");
		ConfiguredBIDocument toReturn = null;
		try {
			File docsConfigured = getTemporaryDossierConfigurationFile(pathTempFolder);
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

	public List getConfiguredDocumentList(String pathTempFolder) {
		logger.debug("IN");
		List toReturn = new ArrayList();
		try {
			File docsConfigured = getTemporaryDossierConfigurationFile(pathTempFolder);
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
	
	public void storeProcessDefinitionFile(String pdFileName, byte[] pdFileContent, String pathTempFolder) {
		logger.debug("IN");
		try {
			File dossierTempFolder = new File(pathTempFolder);
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

	public void storePresentationTemplateFile(String templateFileName, byte[] templateContent, String pathTempFolder) {
		logger.debug("IN");
		try {
			File dossierTempFolder = new File(pathTempFolder);
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

	public Integer getDossierId(String pathTempFolder) {
		logger.debug("IN");
		try {
			File file = new File(pathTempFolder);
			File parent = file.getParentFile();
			String dossierIdStr = parent.getName();
			if (dossierIdStr.contains("/")) {
				dossierIdStr = dossierIdStr.substring(dossierIdStr.lastIndexOf("/") + 1);
			}
			if (dossierIdStr.contains("\\")) {
				dossierIdStr = dossierIdStr.substring(dossierIdStr.lastIndexOf("\\") + 1);
			}
			Integer dossierId = new Integer(dossierIdStr);
			return dossierId;
		} finally {
			logger.debug("OUT");
		}
	}

}
