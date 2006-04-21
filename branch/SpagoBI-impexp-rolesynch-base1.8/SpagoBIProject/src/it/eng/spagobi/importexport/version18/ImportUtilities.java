package it.eng.spagobi.importexport.version18;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class ImportUtilities {

	public static void decompressArchive(String pathImpTmpFolder, String pathArchiveFile) throws EMFUserError {
		File tmpFolder = new File(pathImpTmpFolder);
		tmpFolder.mkdirs();
		int BUFFER = 2048;
		try {
	         BufferedOutputStream dest = null;
	         FileInputStream fis = new FileInputStream(pathArchiveFile);
	         ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
	         ZipEntry entry;
	         while((entry = zis.getNextEntry()) != null) {
	            int count;
	            byte data[] = new byte[BUFFER];
	            File entryFile = new File(pathImpTmpFolder+ "/" + entry.getName());
	            File entryFileFolder = entryFile.getParentFile();
	            entryFileFolder.mkdirs();
	            FileOutputStream fos = new FileOutputStream(pathImpTmpFolder+ "/" + entry.getName());
	            dest = new BufferedOutputStream(fos, BUFFER);
	            while ((count = zis.read(data, 0, BUFFER)) != -1) {
	               dest.write(data, 0, count);
	            }
	            dest.flush();
	            dest.close();
	         }
	         zis.close();
	      } catch(Exception e) {
	    	  SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, "ImportUtilities" , "decompressArchive",
	        			          "Error during the decompression of the exported file " + e);
	        	throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
	      }
	}
	
	
	
	
	public static SessionFactory getHibSessionExportDB(String pathDBFolder) throws EMFUserError {
		Configuration conf = new Configuration();
		String resource = "it/eng/spagobi/importexport/version18/hibernate.cfg.hsql.export.xml";
		conf = conf.configure(resource);
		String hsqlJdbcString = "jdbc:hsqldb:file:" + pathDBFolder + "/metadata;shutdown=true";
		conf.setProperty("hibernate.connection.url",hsqlJdbcString);
		SessionFactory sessionFactory = conf.buildSessionFactory();
		return sessionFactory;
	}
	
	
}
