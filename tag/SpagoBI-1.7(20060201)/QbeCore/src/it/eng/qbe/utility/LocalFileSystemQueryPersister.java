package it.eng.qbe.utility;

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrea Zoppello
 * 
 * An implementation of IQueryPersister that retrieve and persist 
 * queries using the File System
 *
 */
public class LocalFileSystemQueryPersister implements
		IQueryPersister {

	protected void persistToFile(DataMartModel dm, ISingleDataMartWizardObject wizObject, String fileName) {

		try {
		   
		    XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(
		        new FileOutputStream(fileName)));
		    encoder.writeObject(wizObject);
		    encoder.close();
		} catch (FileNotFoundException e) {
			Logger.error(LocalFileSystemQueryPersister.class, e);
		}
		
	}
	public void persist(DataMartModel dm, ISingleDataMartWizardObject wizObject) {
		 // Serialize object into XML
		String key = wizObject.getQueryId();
		String fileName = dm.getPath() + System.getProperty("file.separator") + key+ ".qbe";
		persistToFile(dm, wizObject, fileName);
	}
	
	public ISingleDataMartWizardObject load(DataMartModel dm, String key) {

    	 // DeSerialize object into XML
		String fileName = dm.getPath() + System.getProperty("file.separator") + key+ ".qbe";
        return loadFromFile(dm, new File(fileName));
    
		
	}
    
    protected ISingleDataMartWizardObject loadFromFile(DataMartModel dm, File f) {
        try {
            XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(
                new FileInputStream(f)));
        
            
            ISingleDataMartWizardObject o = (ISingleDataMartWizardObject)decoder.readObject();
            decoder.close();
            return o;
        } catch (FileNotFoundException e) {
    		Logger.error(LocalFileSystemQueryPersister.class, e);
    		return null;
        }
    }
    
    public List loadAllQueries(DataMartModel dm) {
    	File dir = new File(dm.getPath());
        
//    	 It is also possible to filter the list of returned files.
        // This example does not return any files that start with `.'.
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return !name.endsWith("jar");
            }
        };
        
        
    	
    	ISingleDataMartWizardObject query = null;
    	List queries = new ArrayList();
    	File f = null;
        boolean isDir = dir.isDirectory();
        if (isDir) {
        	String[] children = dir.list(filter);
            if (children == null) {
                // Either dir does not exist or is not a directory
            } else {
                for (int i=0; i<children.length; i++) {
                    // Get filename of file or directory
                    String filename = children[i];
                    f = new File(dir, filename);
                    query = loadFromFile(dm,f);
                    queries.add(query);
                }
            }
        }
        
        return queries;
    }

	

}
