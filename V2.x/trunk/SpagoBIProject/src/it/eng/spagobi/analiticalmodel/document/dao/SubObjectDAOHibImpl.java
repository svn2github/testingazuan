package it.eng.spagobi.analiticalmodel.document.dao;

import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SubObjectDAOHibImpl implements ISubObjectDAO {

	public void deleteSubObject(Integer idBIObj, String name)
			throws EMFUserError {
		// TODO Auto-generated method stub
		
	}

	public List getAccessibleSubObjects(Integer idBIObj, IEngUserProfile profile) {
		List subs = new ArrayList();
		return subs;
	}

	public InputStream getSubObject(Integer idBIObj, String name) {
		return null;
	}

	public List getSubObjects(Integer idBIObj) {
		List subs = new ArrayList();
		return subs;
	}

	public void saveSubObject(byte[] content, Integer idBIObj, String name,
			String description, boolean publicVisibility,
			IEngUserProfile profile) throws EMFUserError {
		// TODO Auto-generated method stub
		
	}

	

}
