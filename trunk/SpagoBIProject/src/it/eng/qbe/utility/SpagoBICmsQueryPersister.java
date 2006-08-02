package it.eng.qbe.utility;


import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectCMSDAO;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SpagoBICmsQueryPersister implements
		IQueryPersister {

	
	
	
	protected void persistToFile(DataMartModel dm, ISingleDataMartWizardObject wizObject, String fileName) {
		try{
            // get profile from session					
			RequestContainer reqCont = RequestContainer.getRequestContainer();
			SessionContainer session = reqCont.getSessionContainer();
			SessionContainer permSession = session.getPermanentContainer();
			IEngUserProfile profile = (IEngUserProfile)permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
			// get path of the object parent
			String pathObj =  dm.getPath();
			// get description of the query 
			String description = wizObject.getDescription();
			// get name of the query
			String name = wizObject.getQueryId();
			// get visibility
			boolean visibility = false;
			// get the bytes of the content
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(baos));
			encoder.writeObject(wizObject);
		    encoder.close();
		    byte[] content = baos.toByteArray();
			// save subobject
			IBIObjectCMSDAO cmsDao = DAOFactory.getBIObjectCMSDAO();
			cmsDao.saveSubObject(content, pathObj, name, description, visibility, profile);
		} catch (Exception e) {
			Logger.error(SpagoBICmsQueryPersister.class, e);
		}	
	}
	
	
	
	public void persist(DataMartModel dm, ISingleDataMartWizardObject wizObject) {
		try{
            // get profile from session					
			RequestContainer reqCont = RequestContainer.getRequestContainer();
			SessionContainer session = reqCont.getSessionContainer();
			SessionContainer permSession = session.getPermanentContainer();
			IEngUserProfile profile = (IEngUserProfile)permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
			// get path of the object parent
			String pathObj =  dm.getPath();
			// get description of the query 
			String description = wizObject.getDescription();
			// get name of the query
			String name = wizObject.getQueryId();
			// get visibility
			boolean visibility = wizObject.getVisibility();
			wizObject.setOwner((String)profile.getUserUniqueIdentifier());
			// get the bytes of the content
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(baos));
			encoder.writeObject(wizObject);
		    encoder.close();
		    byte[] content = baos.toByteArray();
			// save subobject
			IBIObjectCMSDAO cmsDao = DAOFactory.getBIObjectCMSDAO();
			cmsDao.saveSubObject(content, pathObj, name, description, visibility, profile);
		} catch (Exception e) {
			Logger.error(SpagoBICmsQueryPersister.class, e);
		}	
	}
	
	
	
	
	public ISingleDataMartWizardObject load(DataMartModel dm, String key) {
		 // get profile from session					
		RequestContainer reqCont = RequestContainer.getRequestContainer();
		SessionContainer session = reqCont.getSessionContainer();
		SessionContainer permSession = session.getPermanentContainer();
		IEngUserProfile profile = (IEngUserProfile)permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		// get path of the object parent
		String pathObj =  dm.getPath();
		// get subobject content 
		ISingleDataMartWizardObject o = null;
		try {
			IBIObjectCMSDAO cmsDao = DAOFactory.getBIObjectCMSDAO();
			InputStream is = cmsDao.getSubObject(pathObj, key);
			// decode content and return object
			XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(is));
			o = (ISingleDataMartWizardObject)decoder.readObject();
	        decoder.close();
		} catch (Exception e) {
			Logger.error(SpagoBICmsQueryPersister.class, e);
		}
        return o;
	}
 


    public List loadAllQueries(DataMartModel dm) {
    	ArrayList queries = new ArrayList();
        return queries;
    }

	

}
