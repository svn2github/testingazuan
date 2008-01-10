package it.eng.spagobi.importexport.to.dao;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.importexport.to.AssociationFile;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AssociationFileDAO implements IAssociationFileDAO {

	//private final String ASS_DIRECTORY = "Repository_Association_Files"; 
	
	
	public AssociationFile loadFromID(String id) {
		AssociationFile assFile = null;
		try {
			File fileAssRepDir = getFileOfAssRepDir();
			String pathBaseDirAss = fileAssRepDir.getPath() + "/" + id;
			String pathprop = pathBaseDirAss + "/association.properties";
			FileInputStream fis = new FileInputStream(pathprop);
			Properties props = new Properties();
			props.load(fis);
			fis.close();
			assFile = new AssociationFile();
			assFile.setName(props.getProperty("name"));
			assFile.setDescription(props.getProperty("description"));
			assFile.setId(props.getProperty("id"));
			assFile.setDateCreation(new Long(props.getProperty("creationDate")).longValue());
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "loadFromID",
		  				        "Error while loading association file with id "+id+", " + e);
			assFile = null;
		}
		return assFile;
	}
	
	
	public void saveAssociationFile(AssociationFile assfile, byte[] content) {
		try {
			String id = assfile.getId();
			File fileAssRepDir = getFileOfAssRepDir();
			String pathBaseAssFile = fileAssRepDir.getAbsolutePath() + "/" + id;
			File baseAssFile = new File(pathBaseAssFile);
			baseAssFile.mkdirs();
			String pathXmlAssFile = pathBaseAssFile + "/association.xml";
			FileOutputStream fos = new FileOutputStream(pathXmlAssFile);
			ByteArrayInputStream bais = new ByteArrayInputStream(content);
			GeneralUtilities.flushFromInputStreamToOutputStream(bais, fos, true);
			String pathPropAssFile = pathBaseAssFile + "/association.properties";
			String properties = "id=" + assfile.getId() + "\n";
			properties += "name=" + assfile.getName() + "\n";
			properties += "description=" + assfile.getDescription() + "\n";
			properties += "creationDate=" + assfile.getDateCreation() + "\n";
			fos = new FileOutputStream(pathPropAssFile);
			fos.write(properties.getBytes());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "saveAssociationFile",
                    			  "Error while saving association file, " + e);
		}
	}

	public boolean exists(String id) {
		File fileAssRepDir = getFileOfAssRepDir();
		String pathBaseAssFile = fileAssRepDir.getAbsolutePath() + "/" + id;
		File baseAssFile = new File(pathBaseAssFile);
		// if the folder exists the association file exists
		// if a file with the same name exists then tries to delete it
		if (baseAssFile.exists()) {
			if (baseAssFile.isDirectory()) return true;
			else {
				if (baseAssFile.delete()) return false;
				else return true;
			}
		}
		else return false;
	}
	
	public void deleteAssociationFile(AssociationFile assfile) { 
		try {
			File fileAssRepDir = getFileOfAssRepDir();
			String pathBaseDirAss = fileAssRepDir.getPath() + "/" + assfile.getId();
			File fileBaseDirAss = new File(pathBaseDirAss);
			GeneralUtilities.deleteDir(fileBaseDirAss);
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "deleteAssociationFile",
		  				        "Error while deleting association file, " + e);
		}
	}

		
		
	public List getAssociationFiles() {
		List assFiles = new ArrayList();
		try {
			File fileAssRepDir = getFileOfAssRepDir();
			File[] assBaseDirs = fileAssRepDir.listFiles();
			for(int i=0; i<assBaseDirs.length; i++) {
				File assBaseDir = assBaseDirs[i];
				try {
					if(assBaseDir.isDirectory()) {
						String pathprop = assBaseDir.getPath() + "/association.properties";
						FileInputStream fis = new FileInputStream(pathprop);
						Properties props = new Properties();
						props.load(fis);
						fis.close();
						AssociationFile assFile = new AssociationFile();
						assFile.setName(props.getProperty("name"));
						assFile.setDescription(props.getProperty("description"));
						assFile.setId(props.getProperty("id"));
						assFile.setDateCreation(new Long(props.getProperty("creationDate")).longValue());
						assFiles.add(assFile);
					}
				} catch (Exception e) {
					SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "getAssociationFiles",
  			  				            "Error while recovering info of the ass file with" +
  			  				            "id " + assBaseDir.getName() + "\n , " + e);
				}		
			}
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "getAssociationFiles",
      			  				"Error while getting association file list, " + e);
		}
		return assFiles;	
	}

	public byte[] getContent(AssociationFile assfile) {
		byte[] byts = new byte[0];
		try {
			File fileAssRepDir = getFileOfAssRepDir();
			String pathBaseDirAss = fileAssRepDir.getPath() + "/" + assfile.getId();
			String pathAssFile = pathBaseDirAss + "/association.xml"; 
			FileInputStream fis = new FileInputStream(pathAssFile);
			byts = GeneralUtilities.getByteArrayFromInputStream(fis);
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "deleteAssociationFile",
		  				        "Error while getting content of association file with id " + assfile.getId() + 
		  				        ",\n " + e);
		}
		return byts;
	}
	
	
	
	
	private File getFileOfAssRepDir() {
		File assrepdirFile = null;
		try{
			ConfigSingleton conf = ConfigSingleton.getInstance();
			SourceBean assRepo = (SourceBean)conf.getAttribute("IMPORTEXPORT.ASSOCIATIONS_REPOSITORY");
			String assRepoPath = (String)assRepo.getAttribute("path");
			if(!assRepoPath.startsWith("/")){
				String pathcont = ConfigSingleton.getRootPath();
				assRepoPath = pathcont + "/" + assRepoPath;
			}
			
//			File imptmpdirFile = getFileOfImportTmpDir();
//			if(imptmpdirFile==null) throw new Exception("Cannot recover the file of import tmp directory");
//			String pathImpTmpFolder = imptmpdirFile.getAbsolutePath();
//			// add the associations directory to the path
//			if(! pathImpTmpFolder.endsWith("/") ){
//				pathImpTmpFolder += "/";
//			}
//			pathImpTmpFolder += ASS_DIRECTORY;
			
			// check if the file already exists  and, if not, create the directory
			assrepdirFile = new File(assRepoPath);
			assrepdirFile.mkdirs();
		} catch (Exception e) {
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "getFileOfAssRepDir",
		                           "Error wile getting the associations repository dir file, " + e);
		} 
		return assrepdirFile;
	}
	
	
	private File getFileOfImportTmpDir() {
		File imptmpdirFile = null;
		try{
			// get the tmp import folder path
			ConfigSingleton conf = ConfigSingleton.getInstance();
			SourceBean importerSB = (SourceBean)conf.getAttribute("IMPORTEXPORT.IMPORTER");
			String pathImpTmpFolder = (String)importerSB.getAttribute("tmpFolder");
			if(!pathImpTmpFolder.startsWith("/")){
				String pathcont = ConfigSingleton.getRootPath();
				pathImpTmpFolder = pathcont + "/" + pathImpTmpFolder;
			}
			imptmpdirFile = new File(pathImpTmpFolder);
		} catch (Exception e) {
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "getImportTmpDirFile",
		                           "Error wile getting the import tmp dir file, " + e);
		} 
		return imptmpdirFile;
	}



	
	
	
}
