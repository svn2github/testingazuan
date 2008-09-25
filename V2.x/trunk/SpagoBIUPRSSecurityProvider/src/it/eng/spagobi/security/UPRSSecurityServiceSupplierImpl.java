/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.security;

import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.dao.IUserFunctionalityDAO;
import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;
import it.eng.spagobi.services.security.service.ISecurityServiceSupplier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.fiat.UPRS.UPSServiceSoapProxy;
import com.fiat.UPRS.URSServiceSoapProxy;

public class UPRSSecurityServiceSupplierImpl implements ISecurityServiceSupplier {
	
	static private final String USER_IDENTIFIER = "USER_IDENTIFIER";
	static private final String AUTH_TICKET = "AUTH_TICKET";
	static private final String AUTH_OPCODE = "OPCODE";
	static private final String AUTH_BRAND = "BRAND";
    
	static private Logger logger = Logger.getLogger(UPRSSecurityServiceSupplierImpl.class);

    /**
     * Return an SpagoBIUserProfile implementation starting from the id of the
     * user.
     * 
     * @param identifier    the user identifier. Is a string with ticket, opCode and brand information
     * 
     * @return The User Profile Interface implementation object
     */

    public SpagoBIUserProfile createUserProfile(String userIdentifier) {
	logger.debug("IN - userIdentifier: " + userIdentifier);
	
	BASE64Decoder bASE64Decoder = new BASE64Decoder();
	String ticket = null;
	String opCode = null;
	String brand = null;
	
	
	
	
	 //////////////////////////TEST MODE /////////////////////////////////
	String tmpTicket ="";
	 URSServiceSoapProxy ursProxy = new URSServiceSoapProxy();
	 if (!SecurityProviderUtilities.getProxyInfo()) return null;
	 
	 try{    	
		  ticket = ursProxy.getAuthTicket("0062211.d001"); // oppure 0062230.D001, opCode=IT.LINK, brand= null
		  String tmpUserIdentifier = 
		   				" <USER_IDENTIFIER> "+
					    	   "<AUTH_TICKET><![CDATA["+ticket+"]]></AUTH_TICKET> " +
					    	  
					    	   "<OPCODE>IT.LINK</OPCODE> " +
					    	   "<BRAND></BRAND>" +
					    	"</USER_IDENTIFIER> ";
		
		 userIdentifier = new BASE64Encoder().encodeBuffer(tmpUserIdentifier.getBytes("UTF-8")); 

	 }catch(Exception ex){
	 	logger.error("TESTING...... Error during calling URS Web Service. "+ ex.getMessage());
			return null;
	 }   
	
		/////////////////////END TEST MODE /////////////////////////////////
	
	 
	 
	try{
		String xmlString = new String(bASE64Decoder.decodeBuffer(userIdentifier)); 
		SourceBean identifierSB = SourceBean.fromXMLString(xmlString);	
		ticket = ((SourceBean)identifierSB.getAttribute(AUTH_TICKET)).getCharacters();
		opCode = ((SourceBean)identifierSB.getAttribute(AUTH_OPCODE)).getCharacters();
		brand =((SourceBean)identifierSB.getAttribute(AUTH_BRAND)).getCharacters();
	} catch(Exception e ){
		logger.error("Error during parsing user identifier. Impossible call to UPS Web Service. "+ e.getStackTrace());
		return null;
	}
	 
	//call UPS Service
	UPSServiceSoapProxy upsProxy = new UPSServiceSoapProxy();
	
    String userProfile = null;
    if (!SecurityProviderUtilities.getProxyInfo()) return null;
    try{    	
    	userProfile = upsProxy.getCommonProfiles(ticket, opCode, brand);
    }catch(Exception ex){
    	logger.error("Error during calling UPS Web Service. "+ ex.getMessage());
		return null;
    }
   
    
    String role = null;
    String userId = null;
	try{
		//String xmlString = new String(bASE64Decoder.decodeBuffer(userProfile)); 	
		//SourceBean profileSB = SourceBean.fromXMLString(xmlString);	
		SourceBean profileSB = SourceBean.fromXMLString(userProfile);	
		role = ((SourceBean)profileSB.getAttribute("USER.APPLICATIONS.APPLICATION")).getCharacters();
		logger.debug("Role returned from UPRS: " + role);
		userId = ((SourceBean)profileSB.getAttribute("USER.ATTRIBUTES.LOGINNAME")).getCharacters();
		logger.debug("User returned from UPRS: " + userId);
		if (userId != null) userId = userId.substring(userId.indexOf("/")+1);
	} catch(Exception e ){
		logger.error("Error during parsing user identifier. Impossible call to UPS Web Service. "+ e.getMessage());
		return null;
	}
	
	
	
    
	/////////////////// TEST ////////////////////////////
	userId = "ADMIN";
	//////////// END TEST //////////////////////////////
	
	
	
	
	
	SpagoBIUserProfile profile = new SpagoBIUserProfile();
	ArrayList roles = new ArrayList();
	profile.setUserId(userId);

	// get roles of the user
	
	HashMap lstRoles = SecurityProviderUtilities.getAllUserRoles(userId);
	
    Matcher matcher = null;
    Set infoKeys = lstRoles.keySet();
	Iterator infoKeyIter = infoKeys.iterator();
	while(infoKeyIter.hasNext()) {
		String roleID = infoKeyIter.next().toString();
		//String value = lstRoles.get(labelcode).toString();
		Pattern pattern = SecurityProviderUtilities.getFilterPattern();
		matcher = pattern.matcher(roleID);
		if (!matcher.find()) {
		    continue;
		}
		roles.add(roleID);
		logger.debug("Roles load into SpagoBI profile: " + roleID);
    }
    
	// start load profile attributes
	HashMap userAttributes =  SecurityProviderUtilities.getUserProfileAttributes(userId);
	logger.debug("Attributes load into SpagoBI profile: " + userAttributes);
	// end load profile attributes
	
	String[] roleStr = new String[roles.size()];
	for (int i = 0; i < roles.size(); i++) {
	    roleStr[i] = (String) roles.get(i);
	}


	profile.setRoles(roleStr);
	profile.setAttributes(userAttributes);
	profile.setFunctions(readFunctionality(profile.getRoles()));
	
	logger.debug("OUT");
	return profile;
    }

    /**
     * Return a boolean : true if the user is authorized to continue, false
     * otherwise.
     * 
     * @param String
     *                the current user id
     * @param String
     *                the current pwd
     * @return The User Profile Interface implementation object
     */
    public boolean checkAuthorization(String userId, String pwd) {
	logger.warn("checkAuthorization NOT implemented. Check maked from SSO");
	return true;

    }
    
    
    private String[] readFunctionality(String[] roles) {
	logger.debug("IN");
	try {
	    IUserFunctionalityDAO dao = DAOFactory.getUserFunctionalityDAO();	
	    return dao.readUserFunctionality(roles);
	} catch (EMFUserError e) {
	    logger.error("EMFUserError", e);
	} catch (Exception e) {
	    logger.error("Exception", e);
	} finally {
	    logger.debug("OUT");
	}
	return null;
    }
    
}
