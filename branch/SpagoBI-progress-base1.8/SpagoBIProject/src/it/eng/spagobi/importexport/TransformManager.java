package it.eng.spagobi.importexport;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class TransformManager {

	
	public byte[] applyTransformations(byte[] impArchive, String archiveName, String pathImpTmpFolder) throws EMFUserError {
		ConfigSingleton conf = ConfigSingleton.getInstance();
		try{
			decompressArchive(pathImpTmpFolder, archiveName, impArchive);
		} catch(Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), 
		               "applyTransformations",
		               "Error while decompressing exported archive " + e);
		}
		String archiveNameWithoutExtension = archiveName.substring(0, archiveName.lastIndexOf('.'));
		String currVersion = getCurrentVersion();
		String expVersion = "";
		try{
			expVersion = getExportVersion(pathImpTmpFolder, archiveNameWithoutExtension);
		} catch(Exception e){
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), 
					               "applyTransformations",
        			               "Error while getting version name of the exporting system " + e);
		}
		// erase tmp folder
		File fileTmpDir = new File(pathImpTmpFolder);
		GeneralUtilities.deleteContentDir(fileTmpDir);
		// if the exported version is equal to the current the archive has not to be transformed
		if(currVersion.trim().equalsIgnoreCase(expVersion.trim())) {
			return impArchive;
		}
		// read transformer class from configuration
		List transClassName  = new ArrayList();
		List transList = conf.getAttributeAsList("IMPORTEXPORT.TRANSFORMERS.TRANSFORM");
		fillListTransClassName(transList, expVersion, transClassName);
		// instance all transformer
		Iterator iterTrans = transClassName.iterator();
		while(iterTrans.hasNext()) {
			String className = (String)iterTrans.next();
			try{
				Class impClass = Class.forName(className);
				ITransformer transformer = (ITransformer)impClass.newInstance();
				impArchive = transformer.transform(impArchive, pathImpTmpFolder, archiveName);
			} catch(Exception e) {
				SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), 
			                           "applyTransformations",
			                           "Error while instancing the transformer and executing transformation " + e);
			}
		}
		// return file archive content	
		return impArchive;
	}
	

	
	private void fillListTransClassName(List transList, String nameVerOrigin, List transformers) {
		Iterator iter = transList.iterator();
		while(iter.hasNext()) {
			SourceBean sb = (SourceBean)iter.next();
			String verOrigin = (String)sb.getAttribute("from");
			String verDest = (String)sb.getAttribute("to");
		    String className = (String)sb.getAttribute("class");
		    if(verOrigin.trim().equalsIgnoreCase(nameVerOrigin.trim())){
		    	transformers.add(className);
		    	fillListTransClassName(transList, verDest, transformers);
		    }
		}
	}
	
	
	
	/**
	 * Gets the current SpagobI version
	 * @return The current SpagoBI version
	 */
	public String getCurrentVersion() {
		ConfigSingleton conf = ConfigSingleton.getInstance();
		SourceBean curVerSB = (SourceBean)conf.getAttribute("IMPORTEXPORT.CURRENTVERSION");
		String curVer = (String)curVerSB.getAttribute("version");
		return curVer;
	}
	
	/**
	 * Gets the SpagoBI version of the exported file
	 * @return The SpagoBI version of the exported file
	 */
	public String getExportVersion(String pathImpTmpFold, String archiveName) throws Exception {
		//get exported properties
		String pathBaseFolder = pathImpTmpFold + "/" + archiveName;
	    String propFilePath = pathBaseFolder + "/export.properties";
	    FileInputStream fis = new FileInputStream(propFilePath);
		Properties props = new Properties();
		props.load(fis);
		fis.close();
		return props.getProperty("spagobi-version");
	}
	
	
	private void decompressArchive(String pathImpTmpFold, String archiveName, byte[] archiveCont) throws Exception {
		// create directories of the tmp import folder
		File impTmpFold = new File(pathImpTmpFold);
		impTmpFold.mkdirs();
		// write content uploaded into a tmp archive
		String pathArchiveFile = pathImpTmpFold + "/" +archiveName;
		File archive = new File(pathArchiveFile);
		FileOutputStream fos = new FileOutputStream(archive); 
		fos.write(archiveCont);
		fos.flush();
		fos.close();
		// decompress archive
		ImportUtilities.decompressArchive(pathImpTmpFold, pathArchiveFile);
		// erase archive file 
		archive.delete();
	}
	
	
	

	
	
	
	
	
}
