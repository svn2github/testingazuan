/**
 * Copyright (c) 2005, Engineering Ingegneria Informatica s.p.a.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this list of 
      conditions and the following disclaimer.
      
    * Redistributions in binary form must reproduce the above copyright notice, this list of 
      conditions and the following disclaimer in the documentation and/or other materials 
      provided with the distribution.
      
    * Neither the name of the Engineering Ingegneria Informatica s.p.a. nor the names of its contributors may
      be used to endorse or promote products derived from this software without specific
      prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND 
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, 
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE

 */

package it.eng.spagobi.drivers.jpivot;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.Engine;
import it.eng.spagobi.bo.BIObject.SubObjectDetail;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectCMSDAO;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.drivers.EngineURL;
import it.eng.spagobi.drivers.IEngineDriver;
import it.eng.spagobi.drivers.exceptions.InvalidOperationRequest;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.ParameterValuesEncoder;
import it.eng.spagobi.utilities.PortletUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;
import it.eng.spagobi.utilities.UploadedFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import sun.misc.BASE64Encoder;


/**
 * Driver Implementation (IEngineDriver Interface) for JPivot Engine. 
 */
public class JPivotDriver implements IEngineDriver {

	private void addLocale(Map map) {
		ConfigSingleton config = ConfigSingleton.getInstance();
		Locale portalLocale = null;
		try {
			portalLocale =  PortletUtilities.getPortalLocale();
		} catch (Exception e) {
			SpagoBITracer.major("ENGINES",
					this.getClass().getName(),
					"addLocale(Map)",
					"Error while getting portal locale.");
			portalLocale = new Locale("en", "US");
		}
		SpagoBITracer.debug("ENGINES", this.getClass().getName(), "addLocale(Map)", 
				"Portal locale: " + portalLocale);
		SourceBean languageSB = null;
		if (portalLocale != null && portalLocale.getLanguage() != null) {
			languageSB = (SourceBean)config.getFilteredSourceBeanAttribute("SPAGOBI.LANGUAGE_SUPPORTED.LANGUAGE", 
					"language", portalLocale.getLanguage());
			SpagoBITracer.debug("ENGINES", this.getClass().getName(), "addLocale(Map)", 
					"Found language configuration for portal locale [" + portalLocale + "]: " + languageSB);
		}
		if (languageSB != null) {
			map.put("country", (String)languageSB.getAttribute("country"));
			map.put("language", (String)languageSB.getAttribute("language"));
			SpagoBITracer.debug("ENGINES", this.getClass().getName(), "addLocale(Map)", 
				"Adding [" + languageSB.getAttribute("country") + "," +
						languageSB.getAttribute("language") + "] locale.");
		} else {
			map.put("country", "US");
			map.put("language", "en");
			SpagoBITracer.debug("ENGINES", this.getClass().getName(), "addLocale(Map)", 
					"Adding [en,US] locale as default.");
		}			
	}
	
	/**
	 * Returns a map of parameters which will be send in the request to the 
	 * engine application.
	 * @param biObject Object to execute
	 * @param profile Profile of the user 
	 * @param roleName the name of the execution role
	 * @return Map The map of the execution call parameters
	 */
	public Map getParameterMap(Object biobject, IEngUserProfile profile, String roleName) {
		Map map = new Hashtable();
		try{
			BIObject biobj = (BIObject)biobject;
			map = getMap(biobj, profile, roleName);
		} catch (ClassCastException cce) {
			SpagoBITracer.major("ENGINES",
					this.getClass().getName(),
					"getParameterMap(Object, IEngUserProfile, String)",
					"The parameter is not a BIObject type",
					cce);
		} 
		map = applySecurity(map);
		return map;
	}
	/**
	 * Returns a map of parameters which will be send in the request to the 
	 * engine application.
	 * @param biObject Object container of the subObject
	 * @param subObject SubObject to execute
	 * @param profile Profile of the user 
	 * @param roleName the name of the execution role
	 * @return Map The map of the execution call parameters
  	 */
	public Map getParameterMap(Object object, Object subObject, IEngUserProfile profile, String roleName) {
		Map map = new Hashtable();
		try{
			BIObject biobj = (BIObject)object;
			map = getMap(biobj, profile, roleName);
			SubObjectDetail subObj = (SubObjectDetail)subObject;
			map = getParameterMap(object, profile, roleName);
			String nameSub = subObj.getName();
			map.put("nameSubObject", nameSub);
			String descrSub = subObj.getDescription();
			map.put("descriptionSubObject", descrSub);
			String visStr = "Private";
			boolean visBool = subObj.isPublicVisible();
		    if(visBool) 
		    	visStr = "Public";
			map.put("visibilitySubObject", visStr);
			// get subobject data from cms
			IBIObjectCMSDAO biObjCMSDAO = DAOFactory.getBIObjectCMSDAO();
		 	InputStream subObjDataIs = biObjCMSDAO.getSubObject(biobj.getPath(), subObj.getName());
		 	byte[] subObjDataBytes  = GeneralUtilities.getByteArrayFromInputStream(subObjDataIs);
		 	// encode and set the subobject data as a parameter
		 	BASE64Encoder bASE64Encoder = new BASE64Encoder();
			map.put("subobjectdata", bASE64Encoder.encode(subObjDataBytes));
			
		} catch (ClassCastException cce) {
			SpagoBITracer.major("ENGINES",
					this.getClass().getName(),
					"getParameterMap(Object, Object, IEngUserProfile, String)",
					"The second parameter is not a SubObjectDetail type",
					cce);
		} catch (EMFUserError emfue) {
			SpagoBITracer.major("ENGINES",
					this.getClass().getName(),
					"getParameterMap(Object, Object, IEngUserProfile, String)",
					"Error while creating cmsDao for BiObject",
					emfue);
		} 
		map = applySecurity(map);
		return map;
	}
    
	
	
	
	
	
	
	
	 /**
	 * Return a map of parameters which will be sended in the request to the 
	 * engine application.
	 * @param biObject Object to execute
	 * @return Map The map of the execution call parameters
	 * @deprecated
  	*/
	public Map getParameterMap(Object biobject){
		Map map = new Hashtable();
		try{
			BIObject biobj = (BIObject)biobject;
			map = getMap(biobj, null, "");
		} catch (ClassCastException cce) {
			SpagoBITracer.major("ENGINES",
					this.getClass().getName(),
					"getParameterMap(Object)",
					"The parameter is not a BIObject type",
					cce);
		} 
		map = applySecurity(map);
		return map;
	}			
	/**
	 * Return a map of parameters which will be sended in the request to the 
	 * engine application.
	 * @param biObject Object to execute
	 * @param profile Profile of the user 
	 * @return Map The map of the execution call parameters
	 * @deprecated
	 */
	public Map getParameterMap(Object object, IEngUserProfile profile){
		Map map = new Hashtable();
		try{
			BIObject biobj = (BIObject)object;
			map = getMap(biobj, profile, "");
		} catch (ClassCastException cce) {
			SpagoBITracer.major("ENGINES",
					this.getClass().getName(),
					"getParameterMap(Object, IEngUserProfile)",
					"The parameter is not a BIObject type",
					cce);
		} 
		map = applySecurity(map);
		return map;
	}
	/**
	 * Return a map of parameters which will be sended in the request to the 
	 * engine application.
	 * @param biObject Object container of the subObject
	 * @param subObject SubObject to execute
	 * @return Map The map of the execution call parameters
	 * @deprecated
  	 */
	public Map getParameterMap(Object object, Object subObject){
		Map map = new Hashtable();
		try{
			BIObject biobj = (BIObject)object;
			map = getMap(biobj, null, "");
			SubObjectDetail subObj = (SubObjectDetail)subObject;
			map = getParameterMap(object, null, "");
			String nameSub = subObj.getName();
			map.put("nameSubObject", nameSub);
			String descrSub = subObj.getDescription();
			map.put("descriptionSubObject", descrSub);
			String visStr = "Private";
			boolean visBool = subObj.isPublicVisible();
		    if(visBool) 
		    	visStr = "Public";
			map.put("visibilitySubObject", visStr);
			// get subobject data from cms
			IBIObjectCMSDAO biObjCMSDAO = DAOFactory.getBIObjectCMSDAO();
		 	InputStream subObjDataIs = biObjCMSDAO.getSubObject(biobj.getPath(), subObj.getName());
		 	byte[] subObjDataBytes  = GeneralUtilities.getByteArrayFromInputStream(subObjDataIs);
		 	// encode and set the subobject data as a parameter
		 	BASE64Encoder bASE64Encoder = new BASE64Encoder();
			map.put("subobjectdata", bASE64Encoder.encode(subObjDataBytes));
			
		} catch (ClassCastException cce) {
			SpagoBITracer.major("ENGINES",
					this.getClass().getName(),
					"getParameterMap(Object, Object)",
					"The second parameter is not a SubObjectDetail type",
					cce);
		} catch (EMFUserError emfue) {
			SpagoBITracer.major("ENGINES",
					this.getClass().getName(),
					"getParameterMap(Object, Object)",
					"Error while creating cmsDao for BiObject",
					emfue);
		} 
		map = applySecurity(map);
		return map;
	}
    /**
	 * Return a map of parameters which will be sended in the request to the 
	 * engine application.
	 * @param biObject Object container of the subObject
	 * @param subObject SubObject to execute
	 * @param profile Profile of the user 
	 * @return Map The map of the execution call parameters
	 * @deprecated
  	 */
    public Map getParameterMap(Object object, Object subObject, IEngUserProfile profile){
    	Map map = new Hashtable();
		try{
			BIObject biobj = (BIObject)object;
			map = getMap(biobj, profile, "");
			SubObjectDetail subObj = (SubObjectDetail)subObject;
			map = getParameterMap(object, profile, "");
			String nameSub = subObj.getName();
			map.put("nameSubObject", nameSub);
			String descrSub = subObj.getDescription();
			map.put("descriptionSubObject", descrSub);
			String visStr = "Private";
			boolean visBool = subObj.isPublicVisible();
		    if(visBool) 
		    	visStr = "Public";
			map.put("visibilitySubObject", visStr);
			// get subobject data from cms
			IBIObjectCMSDAO biObjCMSDAO = DAOFactory.getBIObjectCMSDAO();
		 	InputStream subObjDataIs = biObjCMSDAO.getSubObject(biobj.getPath(), subObj.getName());
		 	byte[] subObjDataBytes  = GeneralUtilities.getByteArrayFromInputStream(subObjDataIs);
		 	// encode and set the subobject data as a parameter
		 	BASE64Encoder bASE64Encoder = new BASE64Encoder();
			map.put("subobjectdata", bASE64Encoder.encode(subObjDataBytes));
			
		} catch (ClassCastException cce) {
			SpagoBITracer.major("ENGINES",
					this.getClass().getName(),
					"getParameterMap(Object, Object, IEngUserProfile)",
					"The second parameter is not a SubObjectDetail type",
					cce);
		} catch (EMFUserError emfue) {
			SpagoBITracer.major("ENGINES",
					this.getClass().getName(),
					"getParameterMap(Object, Object, IEngUserProfile)",
					"Error while creating cmsDao for BiObject",
					emfue);
		} 
		map = applySecurity(map);
		return map;
	}
	
	
	
	
	
	
	
       
    /**
     * Starting from a BIObject extracts from it the map of the paramaeters for the
     * execution call
     * @param biobj BIObject to execute
     * @return Map The map of the execution call parameters
     */    
	protected Map getMap(BIObject biobj, IEngUserProfile profile, String roleName) {
   		Map pars = new Hashtable();
		biobj.loadTemplate();
		UploadedFile uploadedFile =  biobj.getTemplate();
		byte[] template = uploadedFile.getFileContent();
		BASE64Encoder bASE64Encoder = new BASE64Encoder();
		pars.put("template", bASE64Encoder.encode(template));
		pars.put("spagobiurl", GeneralUtilities.getSpagoBiContentRepositoryServlet());
		pars.put("templatePath",biobj.getPath() + "/template");
		pars.put("query", "dynamicOlap");
		String username = "";
		if(profile!=null)
			username = (String)profile.getUserUniqueIdentifier();
		pars.put("user", username);
		pars.put("role", roleName);
		pars = addDataAccessParameter(profile, roleName, pars, template);
        pars = addBIParameters(biobj, pars);
        addLocale(pars);
        return pars;
	} 
 
	
	/**
	 * Adds parameter for data access based on the user functionalities
	 * @param profile Profile of the user
	 * @param roleName Role name of the user
	 * @param pars Map of previous parameters
	 * @param template bytes of the biobject template
	 * @return The parameter map containing parameter for data access control
	 */
	protected Map addDataAccessParameter(IEngUserProfile profile, String roleName, Map pars, byte[] templateBy) {
		try{
			// value of the parameter to send to the engine
			String valueAccessPar = "";
			// get the user functionalities associated to the execution role
			Collection profFuncts = profile.getFunctionalitiesByRole(roleName);
			String dapv = "{"; //data access parameter value (dapv)
			// trasnform template bytes into a string
			String templateStr = new String(templateBy);
			// read the template as a sourcebena
			SourceBean templateSB = SourceBean.fromXMLString(templateStr);
			// get the list of granted dimesions
			List grantDims = templateSB.getAttributeAsList("DATA-ACCESS.GRANTED-DIMENSIONS.DIMENSION");
			// for each granted dimension
			Iterator iterGrantDims = grantDims.iterator();
			while(iterGrantDims.hasNext()) {
				SourceBean dimSB = (SourceBean)iterGrantDims.next();
				// get name of the dimension
				String dimName = (String)dimSB.getAttribute("name");
				dapv = dapv + dimName + "{";
				// get the grantSource of the dimension
				String grantSource = (String)dimSB.getAttribute("grantSource");
				// based on grant source fill the data access token
				if( (grantSource!=null) && grantSource.equalsIgnoreCase("ProfileFunctionalities") ) {
					String dat = getDataAccessToken(dimSB, profFuncts);
					dapv = dapv + dat + "};";
				}
				if( (grantSource!=null) && grantSource.equalsIgnoreCase("ProfileAttributes") ) {
					String dat = getDataAccessToken(dimSB, profile); 
					dapv = dapv + dat + "};";
				}
			}
			if(dapv.endsWith(";")) {
				dapv = dapv.substring(0, dapv.length()-1);
			}
			dapv = dapv + "}";
			pars.put("dimension_access_rules", dapv);		    
		} catch(Exception e) {
			return pars;
		}
		return pars;
	}
	
	
	private String getDataAccessToken(SourceBean dimSB, Collection profileFuncts) {
		String datoken = "access=custom,";
		// get the dimension name
		String dimName = (String)dimSB.getAttribute("name");
		// for each funtionality check if it is a data access functionality and 
		// if it is related to the template dimension
		Iterator iterProfFuncts = profileFuncts.iterator();
		while(iterProfFuncts.hasNext()){
			Object objFunct = iterProfFuncts.next();
			if(objFunct instanceof String) {
				String strFunct = (String)objFunct;
				if(strFunct.startsWith("data_access:")) {
					strFunct = strFunct.substring(12);
					if(strFunct.startsWith("/"+dimName)){
						strFunct = strFunct.replaceFirst("/", "[");
				    	if(strFunct.indexOf("/")==-1){
				    		continue; // means that the path has only one element (only the dimension name)
				    	}
				    	strFunct = strFunct.replaceAll("/", "].[");
				    	strFunct = strFunct + "]";
				    	datoken = datoken + "member=" + strFunct + "=all,";
					}
				}
			}
		}
		if(datoken.endsWith(",")) {
			datoken = datoken.substring(0, datoken.length()-1);
		}
		return datoken;
	}
	
	
	private String getDataAccessToken(SourceBean dimSB, IEngUserProfile profile) {
		String datoken = "";
		try {
			String dimensionName = (String) dimSB.getAttribute("name");
			SourceBean rulesSB = (SourceBean)dimSB.getAttribute("RULES");
			String access = (String) rulesSB.getAttribute("access");
			if (access == null) {
				access = "none";
				SpagoBITracer.warning(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
			            "getDataAccessToken", "Access is not defined for dimension " + dimensionName + "." +
			            		" Default value 'none' will be considered.");
			} else if (!access.equalsIgnoreCase("custom") && !access.equalsIgnoreCase("all") 
					&& !access.equalsIgnoreCase("none")) {
				access = "none";
				SpagoBITracer.warning(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
			            "getDataAccessToken", "Access is not defined correctly for dimension " + dimensionName + "." +
			            		" Default value 'none' will be considered.");
			}
			if (access.equalsIgnoreCase("none") || access.equalsIgnoreCase("all")) {
				datoken = "access=" + access.toLowerCase(); 
			} else {
				datoken = "access=custom";
				String topLevel = (String) rulesSB.getAttribute("topLevel");
				String bottomLevel = (String) rulesSB.getAttribute("bottomLevel");
				if (topLevel != null) datoken += ",topLevel=" + topLevel;
				if (bottomLevel != null) datoken += ",bottomLevel=" + bottomLevel;
				List members = rulesSB.getAttributeAsList("MEMBERS.MEMBER");
				Iterator membersIt = members.iterator();
				while (membersIt.hasNext()) {
					SourceBean member = (SourceBean) membersIt.next();
					String memberName = (String) member.getAttribute("name");
					String memberAccess = (String) member.getAttribute("access");
					if (memberAccess == null || (!memberAccess.equalsIgnoreCase("all") 
							&& !memberAccess.equalsIgnoreCase("none"))) {
						SpagoBITracer.warning(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
					            "getDataAccessToken", "Access is not defined correctly for member " + memberName + "." +
					            		" Default value 'none' will be considered.");
						memberAccess = "none";
					}
					if (memberName != null) {
						// memberName = GeneralUtilities.substituteProfileAttributesInString(memberName, profile);
						// datoken += ",member=" + memberName + "=" + memberAccess;
						List memberNames = new ArrayList();
						memberNames.add(memberName);
						memberNames = generateMemeberNames(memberNames, profile);
						Iterator memberNamesIter = memberNames.iterator();
						while(memberNamesIter.hasNext()){
							String membName = (String)memberNamesIter.next();
							datoken += ",member=" + membName + "=" + memberAccess;
						}
					}
				}
			}
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
					            "addToDataAccessMapTokens", "Error while returning data access token", e);
		}
		return datoken;
	}
    
	
	
	
	private List generateMemeberNames(List memNames, IEngUserProfile profile) {
		List generatedMemList = new ArrayList();
		Iterator memNamesIter = memNames.iterator();
		// for all member names
		while(memNamesIter.hasNext()) {
			String memName = (String)memNamesIter.next();
			String tmpMemName = memName;
			// check if the name has a profile attribute
			int startInd = memName.indexOf("${");
			if(startInd!=-1) {
				// if it has, recover the name and value of the profile attribute
				int endInd = memName.indexOf("}", startInd);
				String nameProfAttr = memName.substring((startInd+2), endInd);
				String valueProfAttr = null;
				try {
					valueProfAttr = (String)profile.getUserAttribute(nameProfAttr);
				} catch (Exception e) {
					SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
							            "generateMemeberNames", "Error while recovering profile attribute " +
							            nameProfAttr + " of the user " + profile.getUserUniqueIdentifier(), e);
				}
				// if the value of the profile attribute is not null
				if(valueProfAttr!=null) {
					// all values special character
					if(valueProfAttr.equalsIgnoreCase("*")) {
						String tmp = tmpMemName.substring(0, startInd);
						int indlp = tmp.lastIndexOf(".");
						if(indlp!=-1){
							tmpMemName = tmpMemName.substring(0, indlp);
						} else {
							tmpMemName = "";
						}
						generatedMemList.add(tmpMemName);
					// the attribute is multivalue
					} else if(isAttributeMultivalue(valueProfAttr)) {
						String[] values = splitProfAttrValues(valueProfAttr);
						List tmpMemNames = new ArrayList();
						for(int i=0; i<values.length; i++) {
							String val = values[i];
							String tmpMemNamei = tmpMemName.substring(0, startInd) + val + tmpMemName.substring(endInd+1);
							tmpMemNames.add(tmpMemNamei);
						}
						tmpMemNames = generateMemeberNames(tmpMemNames, profile);
						generatedMemList.addAll(tmpMemNames);
					// the attribute is single value	
					} else {
						tmpMemName = tmpMemName.substring(0, startInd) + valueProfAttr + tmpMemName.substring(endInd+1);
						List tmpMemNames = new ArrayList();
						tmpMemNames.add(tmpMemName);
						tmpMemNames = generateMemeberNames(tmpMemNames, profile);
						generatedMemList.addAll(tmpMemNames);
					}
				} else { // if(valueProfAttr!=null)
					generatedMemList.add(memName);
				}
			} else { // if(startInd!=-1)
				generatedMemList.add(memName);
			}
		}
		return generatedMemList;
	}
          
	
	private boolean isAttributeMultivalue(String value) {
		if(!value.startsWith("{")) {
			return false;
		} 
		if(value.length() < 6) {
			return false;
		}
		if(!value.endsWith("}}")) {
			return false;
		}
		if(value.charAt(2) != '{') {
			return false;
		}
		return true;
	}
	
	private String[] splitProfAttrValues(String value) {
		char splitter = value.charAt(1);
		String valuesList = value.substring(3, value.length() - 2);
		String [] values = valuesList.split(String.valueOf(splitter));
		return values;
	}
	
    /**
     * Add into the parameters map the BIObject's BIParameter names and values
     * @param biobj BIOBject to execute
     * @param pars Map of the parameters for the execution call  
     * @return Map The map of the execution call parameters
     */
	protected Map addBIParameters(BIObject biobj, Map pars) {
		if(biobj==null) {
			SpagoBITracer.warning("ENGINES",
								  this.getClass().getName(),
								  "addBIParameters",
								  "BIObject parameter null");
			return pars;
		}
		if(biobj.getBiObjectParameters() != null){
			BIObjectParameter biobjPar = null;
			String value = null;
			ParameterValuesEncoder parValuesEncoder = new ParameterValuesEncoder();
			for(Iterator it = biobj.getBiObjectParameters().iterator(); it.hasNext();){
				try {
					biobjPar = (BIObjectParameter)it.next();
					/*
					value = (String)biobjPar.getParameterValues().get(0);
					pars.put(biobjPar.getParameterUrlName(), value);
					*/
					value = parValuesEncoder.encode(biobjPar);
					pars.put(biobjPar.getParameterUrlName(), value);
				} catch (Exception e) {
					SpagoBITracer.warning("ENGINES",
										  this.getClass().getName(),
										  "addBIParameters",
										  "Error while processing a BIParameter",
										  e);
				}
			}
		}
  		return pars;
	}
	
	
	/**
	 * Applys changes for security reason if necessary
	 * @param pars The map of parameters
	 * @return the map of parameters to send to the engine 
	 */
	protected Map applySecurity(Map pars) {
		return pars;
	}
	
	/**
	 * Returns the url to be invoked for editing template document
	 * 
	 * @param biobject The biobject
	 * @return the url to be invoked for editing template document
	 */
	public EngineURL getEditDocumentTemplateBuildUrl(Object biobject) throws InvalidOperationRequest {
		BIObject obj = null;
		try {
			obj = (BIObject) biobject;
		} catch (ClassCastException cce) {
			SpagoBITracer.major("ENGINES",
					this.getClass().getName(), "getEditDocumentTemplateBuildUrl",
					"The input object is not a BIObject type", cce);
			return null;
		}
		Engine engine = obj.getEngine();
		String url = engine.getUrl();
		//url = url.substring(0, url.lastIndexOf("/"));
		//url += "/editQuery.jsp";
		obj.loadTemplate();
		UploadedFile uploadedFile =  obj.getTemplate();
		byte[] template = uploadedFile.getFileContent();
		BASE64Encoder bASE64Encoder = new BASE64Encoder();
		String templateName = obj.getCurrentTemplateVersion().getNameFileTemplate();
		// sometimes template name contains complete path
		// TODO to review (this control should not be performed)
		int index = templateName.lastIndexOf("/");
		if (index != -1) templateName = templateName.substring(index + 1);
		index = templateName.lastIndexOf("\\");
		if (index != -1) templateName = templateName.substring(index + 1);
		HashMap parameters = new HashMap();
		parameters.put("biobject_path", obj.getPath());
		parameters.put("spagobiurl", GeneralUtilities.getSpagoBiContentRepositoryServlet());
		parameters.put("templateName", templateName);
		parameters.put("template", bASE64Encoder.encode(template));
		//parameters.put("new_session", "true");
		parameters.put("forward", "editQuery.jsp");
		addLocale(parameters);
		EngineURL engineURL = new EngineURL(url, parameters);
		return engineURL;
	}
	
	/**
	 * Returns the url to be invoked for creating a new template document
	 * 
	 * @param biobject The biobject
	 * @return the url to be invoked for creating a new template document
	 */
	public EngineURL getNewDocumentTemplateBuildUrl(Object biobject) throws InvalidOperationRequest {
		BIObject obj = null;
		try {
			obj = (BIObject) biobject;
		} catch (ClassCastException cce) {
			SpagoBITracer.major("ENGINES",
					this.getClass().getName(), "getEditDocumentTemplateBuildUrl",
					"The input object is not a BIObject type", cce);
			return null;
		}
		Engine engine = obj.getEngine();
		String url = engine.getUrl();
		//url = url.substring(0, url.lastIndexOf("/"));
		//url += "/initialQueryCreator.jsp";
		HashMap parameters = new HashMap();
		parameters.put("biobject_path", obj.getPath());
		parameters.put("spagobiurl", GeneralUtilities.getSpagoBiContentRepositoryServlet());
		//parameters.put("new_session", "true");
		parameters.put("forward", "initialQueryCreator.jsp");
		addLocale(parameters);
		EngineURL engineURL = new EngineURL(url, parameters);
		return engineURL;
	}
	

}
