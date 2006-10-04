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
package it.eng.spagobi.utilities;

import it.eng.spago.error.EMFInternalError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.LowFunctionality;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.bo.dao.DAOFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
/**
 * Contains some methods to control user exec/dev/test rights.
 * 
 * @author sulis
 */
public class ObjectsAccessVerifier {

	
	/**
	 * 
	 * Controls if the  current user can develop the object relative to the input path
	 * 
	 * @param state state of the object
	 * @param path path of the folder containing te object
	 * @param profile user profile
	 * @return A boolean control value
	 */
//	public static boolean canDev(String state, String path, IEngUserProfile profile) {
//		if(!state.equals("DEV")) {
//			return false;		
//		}
//		//String pathFunct = path.substring(0, path.lastIndexOf('/'));
//		//return canDevInternal(pathFunct,profile);
//		return canDevInternal(path, profile);
//	}
	
	
	
	
	/**
	 * 
	 * Controls if current user can exec the object relative to the input path
	 * 
	 * @param state state of the object
	 * @param path path of the folder containing te object
	 * @param profile user profile
	 * @return A boolean control value
	 */
//	public static boolean canExec(String state, String path, IEngUserProfile profile) {
//		if(!state.equals("REL")) {
//			return false;
//		}
//		//String pathFunct = path.substring(0, path.lastIndexOf('/'));
//		//return canExecInternal(pathFunct,profile);
//		return canExecInternal(path, profile);
//	}
	
	
	
	/**
	 * 
	 * Control if current user can test the object relative to the input path
	 * 
	 * @param state state of the object
	 * @param path path of the object
	 * @param profile user profile
	 * @return A boolean control value
	 */
//	public static boolean canTest(String state, String path, IEngUserProfile profile) {
//		if(!state.equals("TEST")) {
//			return false;		
//		}
////		String pathFunct = path.substring(0, path.lastIndexOf('/'));
////		return canTestInternal(pathFunct,profile);
//		return canTestInternal(path, profile);
//		
//		
//	}
	
	
	/**
	 * Control if the current user can develop new object into the functionality path
	 * 
	 * @param path path of the lowFunctionality
	 * @param profile user profile
	 * @return A boolean control value
	 */
//	public static boolean canDev(String path, IEngUserProfile profile) {
//
//		return canDevInternal(path, profile);
//	}
	/**
	 * Control if the current user can test new object into the functionality path
	 * 
	 * @param path path of the lowFunctionality
	 * @param profile user profile
	 * @return A boolean control value
	 */
//	public static boolean canTest(String path, IEngUserProfile profile) {
//		
//		return canTestInternal (path, profile);
//		
//	}
	/**
	 * Control if the current user can execute new object into the functionality path
	 * 
	 * @param path path of the lowFunctionality
	 * @param profile user profile
	 * @return A boolean control value
	 */
//	public static boolean canExec(String path, IEngUserProfile profile) {
//
//		return canExecInternal(path,profile);
//	}
	/**
	 * Private method called by the corrispondent public method canExec. Executes roles
	 * functionalities control .
	 * @param path path of the lowFunctionality
	 * @param profile user profile
	 * @return A boolean control value
	 */
//	private static boolean canExecInternal (String path, IEngUserProfile profile){
//		Collection roles = null;
//		try {
//			roles = profile.getRoles();
//		} catch (EMFInternalError emfie) {
//			return false;
//		}
//		
//		LowFunctionality funct = null;
//		try{
//			funct = DAOFactory.getLowFunctionalityDAO().loadLowFunctionalityByPath(path);
//		} catch (Exception e) {
//			return false;
//		}
//		Role[] execRoles = funct.getExecRoles();
//		List execRoleNames = new ArrayList();
//		for(int i=0; i<execRoles.length; i++) {
//			Role role = execRoles[i];
//			execRoleNames.add(role.getName());
//		}
//		
//		Iterator iterRoles = roles.iterator();
//		String roleName = "";
//		while(iterRoles.hasNext()) {
//			roleName = (String)iterRoles.next();
//			if(execRoleNames.contains(roleName)) {
//				return true;
//			}
//		}
//		return false;
//		
//		
//	}
	/**
	 * Private method called by the corrispondent public method canTest. Executes roles
	 * functionalities control .
	 * @param path path of the lowFunctionality
	 * @param profile user profile
	 * @return A boolean control value
	 */
//	private static boolean canTestInternal(String path, IEngUserProfile profile){
//		Collection roles = null;
//		try {
//			roles = profile.getRoles();
//		} catch (EMFInternalError emfie) {
//			return false;
//		}
//		
//		LowFunctionality funct = null;
//		try{
//			funct = DAOFactory.getLowFunctionalityDAO().loadLowFunctionalityByPath(path);
//		} catch (Exception e) {
//			return false;
//		}
//		Role[] testRoles = funct.getTestRoles();
//		List testRoleNames = new ArrayList();
//		for(int i=0; i<testRoles.length; i++) {
//			Role role = testRoles[i];
//			testRoleNames.add(role.getName());
//		}
//		
//		Iterator iterRoles = roles.iterator();
//		String roleName = "";
//		while(iterRoles.hasNext()) {
//			roleName = (String)iterRoles.next();
//			if(testRoleNames.contains(roleName)) {
//				return true;
//			}
//		}
//		return false;
//		
//	}
	/**
	 * Private method called by the corrispondent public method canDev. Executes roles
	 * functionalities control .
	 * @param path path of the lowFunctionality
	 * @param profile user profile
	 * @return A boolean control value
	 */
//	private static boolean canDevInternal(String path, IEngUserProfile profile){
//		Collection roles = null;
//		try {
//			roles = profile.getRoles();
//		} catch (EMFInternalError emfie) {
//			return false;
//		}
//		
//		LowFunctionality funct = null;
//		try{
//			funct = DAOFactory.getLowFunctionalityDAO().loadLowFunctionalityByPath(path);
//		} catch (Exception e) {
//			return false;
//		}
//		Role[] devRoles = funct.getDevRoles();
//		List devRoleNames = new ArrayList();
//		for(int i=0; i<devRoles.length; i++) {
//			Role role = devRoles[i];
//			devRoleNames.add(role.getName());
//		}
//		
//		Iterator iterRoles = roles.iterator();
//		String roleName = "";
//		while(iterRoles.hasNext()) {
//			roleName = (String)iterRoles.next();
//			if(devRoleNames.contains(roleName)) {
//				return true;
//			}
//		}
//		return false;
//		
//	}
	
	/**
	 * 
	 * Controls if the  current user can develop the object relative to the input folder id
	 * 
	 * @param state state of the object
	 * @param folderId The id of the folder containing te object
	 * @param profile user profile
	 * @return A boolean control value
	 */
	public static boolean canDev(String state, Integer folderId, IEngUserProfile profile) {
		if(!state.equals("DEV")) {
			return false;		
		}
		//String pathFunct = path.substring(0, path.lastIndexOf('/'));
		//return canDevInternal(pathFunct,profile);
		return canDevInternal(folderId, profile);
	}
	
	
	
	
	/**
	 * 
	 * Controls if current user can exec the object relative to the input folder id
	 * 
	 * @param state state of the object
	 * @param folderId The id of the folder containing te object
	 * @param profile user profile
	 * @return A boolean control value
	 */
	public static boolean canExec(String state, Integer folderId, IEngUserProfile profile) {
		if(!state.equals("REL")) {
			return false;
		}
		//String pathFunct = path.substring(0, path.lastIndexOf('/'));
		//return canExecInternal(pathFunct,profile);
		return canExecInternal(folderId, profile);
	}
	
	
	
	/**
	 * 
	 * Control if current user can test the object relative to the folder id
	 * 
	 * @param state state of the object
	 * @param folderId The id of the folder containing the object
	 * @param profile user profile
	 * @return A boolean control value
	 */
	public static boolean canTest(String state, Integer folderId, IEngUserProfile profile) {
		if(!state.equals("TEST")) {
			return false;		
		}
//		String pathFunct = path.substring(0, path.lastIndexOf('/'));
//		return canTestInternal(pathFunct,profile);
		return canTestInternal(folderId, profile);
		
		
	}
	
	
	/**
	 * Control if the current user can develop new object into the functionality identified by its id
	 * 
	 * @param folderId The id of the lowFunctionality
	 * @param profile user profile
	 * @return A boolean control value
	 */
	public static boolean canDev(Integer folderId, IEngUserProfile profile) {

		return canDevInternal(folderId, profile);
	}
	
	/**
	 * Control if the current user can test new object into the functionality identified by its id
	 * 
	 * @param folderId The id of the lowFunctionality
	 * @param profile user profile
	 * @return A boolean control value
	 */
	public static boolean canTest(Integer folderId, IEngUserProfile profile) {
		
		return canTestInternal (folderId, profile);
		
	}
	
	/**
	 * Control if the current user can execute new object into the functionality identified by its id
	 * 
	 * @param folderId The id of the lowFunctionality
	 * @param profile user profile
	 * @return A boolean control value
	 */
	public static boolean canExec(Integer folderId, IEngUserProfile profile) {

		return canExecInternal(folderId,profile);
	}
	/**
	 * Private method called by the corrispondent public method canExec. Executes roles
	 * functionalities control .
	 * @param folderId The id of the lowFunctionality
	 * @param profile user profile
	 * @return A boolean control value
	 */
	private static boolean canExecInternal (Integer folderId, IEngUserProfile profile){
		Collection roles = null;
		try {
			roles = profile.getRoles();
		} catch (EMFInternalError emfie) {
			return false;
		}
		
		LowFunctionality funct = null;
		try{
			funct = DAOFactory.getLowFunctionalityDAO().loadLowFunctionalityByID(folderId, false);
		} catch (Exception e) {
			return false;
		}
		Role[] execRoles = funct.getExecRoles();
		List execRoleNames = new ArrayList();
		for(int i=0; i<execRoles.length; i++) {
			Role role = execRoles[i];
			execRoleNames.add(role.getName());
		}
		
		Iterator iterRoles = roles.iterator();
		String roleName = "";
		while(iterRoles.hasNext()) {
			roleName = (String)iterRoles.next();
			if(execRoleNames.contains(roleName)) {
				return true;
			}
		}
		return false;
		
		
	}
	/**
	 * Private method called by the corrispondent public method canTest. Executes roles
	 * functionalities control .
	 * @param folderId The id of the lowFunctionality
	 * @param profile user profile
	 * @return A boolean control value
	 */
	private static boolean canTestInternal(Integer folderId, IEngUserProfile profile){
		Collection roles = null;
		try {
			roles = profile.getRoles();
		} catch (EMFInternalError emfie) {
			return false;
		}
		
		LowFunctionality funct = null;
		try{
			funct = DAOFactory.getLowFunctionalityDAO().loadLowFunctionalityByID(folderId, false);
		} catch (Exception e) {
			return false;
		}
		Role[] testRoles = funct.getTestRoles();
		List testRoleNames = new ArrayList();
		for(int i=0; i<testRoles.length; i++) {
			Role role = testRoles[i];
			testRoleNames.add(role.getName());
		}
		
		Iterator iterRoles = roles.iterator();
		String roleName = "";
		while(iterRoles.hasNext()) {
			roleName = (String)iterRoles.next();
			if(testRoleNames.contains(roleName)) {
				return true;
			}
		}
		return false;
		
	}
	/**
	 * Private method called by the corrispondent public method canDev. Executes roles
	 * functionalities control .
	 * @param folderId The id of the lowFunctionality
	 * @param profile user profile
	 * @return A boolean control value
	 */
	private static boolean canDevInternal(Integer folderId, IEngUserProfile profile){
		Collection roles = null;
		try {
			roles = profile.getRoles();
		} catch (EMFInternalError emfie) {
			return false;
		}
		
		LowFunctionality funct = null;
		try{
			funct = DAOFactory.getLowFunctionalityDAO().loadLowFunctionalityByID(folderId, false);
		} catch (Exception e) {
			return false;
		}
		Role[] devRoles = funct.getDevRoles();
		List devRoleNames = new ArrayList();
		for(int i=0; i<devRoles.length; i++) {
			Role role = devRoles[i];
			devRoleNames.add(role.getName());
		}
		
		Iterator iterRoles = roles.iterator();
		String roleName = "";
		while(iterRoles.hasNext()) {
			roleName = (String)iterRoles.next();
			if(devRoleNames.contains(roleName)) {
				return true;
			}
		}
		return false;
		
	}
	
}


