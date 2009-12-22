package it.eng.spagobi.profiling.dao;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.profiling.bean.SbiUser;

public interface ISbiUserDAO {
	
	public SbiUser loadSbiUserByUserId(Integer userId) throws EMFUserError;
	
	public SbiUser loadSbiUserById(Integer id) throws EMFUserError;
	
	public void saveSbiUser(SbiUser user) throws EMFUserError;

}
