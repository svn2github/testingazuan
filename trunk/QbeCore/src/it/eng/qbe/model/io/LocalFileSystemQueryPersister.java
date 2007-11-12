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
package it.eng.qbe.model.io;

import it.eng.qbe.conf.QbeConfiguration;
import it.eng.qbe.log.Logger;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.utility.FileUtils;
import it.eng.qbe.utility.SpagoBIInfo;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.security.IEngUserProfile;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

/**
 * @author Andrea Zoppello
 * 
 * An implementation of IQueryPersister that retrieve and persist 
 * queries using the File System
 *
 */
public class LocalFileSystemQueryPersister implements IQueryPersister {

	

	
	protected void persistToFile(DataMartModel dm, ISingleDataMartWizardObject wizObject, String fileName) {

		try {
			File f = new File(fileName);
			if (!f.exists()){
				f.createNewFile();
			}
		    XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(
		        new FileOutputStream(fileName)));
		    encoder.writeObject(wizObject);
		    encoder.close();
		} catch (IOException e) {
			Logger.error(LocalFileSystemQueryPersister.class, e);
		}
		
	}
	
	public void persist(DataMartModel dm, ISingleDataMartWizardObject wizObject) {		
		persist((File)null, dm, wizObject);
	}
	
	public void persist(File baseDir, DataMartModel dm, ISingleDataMartWizardObject wizObject) {
		
		String qbeDataMartDir = FileUtils.getQbeDataMartDir(baseDir);
		//qbeDataMartDir = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-MART_DIR.dir");
		
		String key = wizObject.getQueryId();
		String fileName = null;
		
		String publicDmDir = qbeDataMartDir +System.getProperty("file.separator")+  dm.getName();
		String privateDmDir = publicDmDir + System.getProperty("file.separator") + wizObject.getOwner();
		if (wizObject.getVisibility() == false){
			File f = new File(privateDmDir);
			if (!f.exists()){
				f.mkdirs();
			}
			fileName =  privateDmDir + System.getProperty("file.separator") + key+ ".qbe";
		}else{
			File f = new File(publicDmDir);
			if (!f.exists()){
				f.mkdirs();
			}
			fileName = qbeDataMartDir +System.getProperty("file.separator")+  dm.getName() + System.getProperty("file.separator") + key+ ".qbe";
		}
		
		
		persistToFile(dm, wizObject, fileName);
	}
	
	public ISingleDataMartWizardObject load(DataMartModel dm, String key) {
		return load((File)null, dm, key);
	}
	
	public ISingleDataMartWizardObject load(File baseDir, DataMartModel dm, String key) {

    	RequestContainer requestCont = RequestContainer.getRequestContainer();
        IEngUserProfile userProfile =(IEngUserProfile)requestCont.getSessionContainer().getPermanentContainer().getAttribute(IEngUserProfile.ENG_USER_PROFILE);
        SpagoBIInfo spagoBIInfo =(SpagoBIInfo)requestCont.getSessionContainer().getAttribute("spagobi");
    	
        
        String qbeDataMartDir = FileUtils.getQbeDataMartDir(baseDir);
		//qbeDataMartDir = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-MART_DIR.dir");
		
        String fileName = qbeDataMartDir +System.getProperty("file.separator")+  dm.getName() + System.getProperty("file.separator") + key+ ".qbe";
        
        ISingleDataMartWizardObject wiz = loadFromFile(new File(fileName));
        if (wiz == null && userProfile != null){
        	fileName = qbeDataMartDir + System.getProperty("file.separator")+  dm.getName() + System.getProperty("file.separator") + userProfile.getUserUniqueIdentifier() + System.getProperty("file.separator") + key+ ".qbe";
        	wiz = loadFromFile(new File(fileName));
        }
        
        if (wiz == null && spagoBIInfo != null){
        	fileName = qbeDataMartDir + System.getProperty("file.separator")+  dm.getName() + System.getProperty("file.separator") + spagoBIInfo.getUser() + System.getProperty("file.separator") + key+ ".qbe";
        	wiz = loadFromFile(new File(fileName));
        }
        return wiz;
	}
    
    protected ISingleDataMartWizardObject loadFromFile(File f) {
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
    
    private List loadFirstLevelQuery(String directory){
    	
    	File dir = new File(directory);
//   	 It is also possible to filter the list of returned files.
       // This example does not return any files that start with `.'.
       FilenameFilter filter = new FilenameFilter() {
           public boolean accept(File dir, String name) {
               return name.endsWith("qbe");
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
                   if (!f.isDirectory()){
                   	   query = loadFromFile(f);
                       queries.add(query);
                   }
                   
               }
           }
       }
       
       return queries;
    }
    public List loadAllQueries(DataMartModel dm) {
    	String dmName = dm.getName();
    	File qbeDataMartDir = QbeConfiguration.getInstance().getQbeDataMartDir();
    	File publicTargetDir = new File(qbeDataMartDir, dmName);
    	return loadFirstLevelQuery(publicTargetDir.getAbsolutePath());
    }
    
    public List getPrivateQueriesFor(DataMartModel dm, String userID) {
    	String dmName = dm.getName();
    	File qbeDataMartDir = QbeConfiguration.getInstance().getQbeDataMartDir();
    	File publicTargetDir = new File(qbeDataMartDir, dmName);
    	File privateTargetDir = new File(publicTargetDir, userID);
    
    	return loadFirstLevelQuery(privateTargetDir.getAbsolutePath());
    }
	

	

}
