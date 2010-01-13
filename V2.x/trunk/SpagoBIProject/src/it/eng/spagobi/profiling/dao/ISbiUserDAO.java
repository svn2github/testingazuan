package it.eng.spagobi.profiling.dao;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.metadata.SbiExtRoles;
import it.eng.spagobi.profiling.bean.SbiAttribute;
import it.eng.spagobi.profiling.bean.SbiExtUserRoles;
import it.eng.spagobi.profiling.bean.SbiUser;
import it.eng.spagobi.profiling.bean.SbiUserAttributes;
import it.eng.spagobi.profiling.bo.UserBO;

import java.util.ArrayList;
import java.util.HashMap;

public interface ISbiUserDAO {
	
	public Integer loadByUserId(String userId) throws EMFUserError;
	
	public SbiUser loadSbiUserByUserId(String userId) throws EMFUserError;
	
	public SbiUser loadSbiUserById(Integer id) throws EMFUserError;
	
	public UserBO loadUserById(Integer id) throws EMFUserError;
	
	public void deleteSbiUserById(Integer id) throws EMFUserError;
	
	public Integer saveSbiUser(SbiUser user) throws EMFUserError;
	
	public void updateSbiUserRoles(SbiExtUserRoles role) throws EMFUserError;
	
	public void updateSbiUserAttributes(SbiUserAttributes attribute) throws EMFUserError;
	
	public ArrayList<SbiExtRoles> loadSbiUserRolesById(Integer id) throws EMFUserError;
	
	public ArrayList<SbiUserAttributes> loadSbiUserAttributesById(Integer id) throws EMFUserError;
	
	public ArrayList<SbiUser> loadSbiUsers() throws EMFUserError;
	
	public ArrayList<UserBO> loadUsers() throws EMFUserError;
	
	public void updateSbiUser(SbiUser user, Integer userID) throws EMFUserError;
	
	public void fullUpdateSbiUser(Integer id, String password, String fullName, ArrayList<String> roles, HashMap<Integer, String> attributes) throws EMFUserError;

}
