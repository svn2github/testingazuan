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
/*
 * Created on 7-lug-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.utilities;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.RequestContainerAccess;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.constants.UtilitiesConstants;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.portlet.PortletRequest;





/**
 * 
 * Contains some SpagoBI's general utilities.
 * 
 * @author zoppello
 *
 */
public class GeneralUtilities {
	/**
	 * The Main method
	 * 
	 * @param args String for command line arguments
	 */
	public static void main(String[] args) {
	}

	/**
	 * Given an <code>InputStream</code> as input, gets the correspondent bytes array.
	 * @param is The input straeam 
	 * @return An array of bytes obtained from the input stream.
	 */
	public static byte[] getByteArrayFromInputStream(InputStream is) {
	
		try {
			java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
			java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream(
					baos);
	
			int c = 0;
			byte[] b = new byte[1024];
			while ((c = is.read(b)) != -1) {
				if (c == 1024)
					bos.write(b);
				else
					bos.write(b, 0, c);
			}
			bos.flush();
			byte[] ret = baos.toByteArray();
			bos.close();
			return ret;
		} catch (IOException ioe) {
			SpagoBITracer.major(UtilitiesConstants.NAME_MODULE,
					PortletUtilities.class.getName(),
					"getByteArrayFromInputStream",
					"Exception", ioe);
			return null;
		}
		
	}

	/**
	 * From a String identifying the complete name for a file, gets the relative file names, 
	 * which are substrings of the starting String, according to the java separator "/".
	 * 
	 * @param completeFileName The string representing the file name 
	 * @return	relative names substring
	 */
	public static String getRelativeFileNames(String completeFileName){
		String fileSeparator = System.getProperty("file.separator");
		String javaSeparator = "/";
		if (completeFileName.indexOf(fileSeparator) < 0){
			if (completeFileName.indexOf(javaSeparator) < 0){ 
				return completeFileName;
			}else{
				int lastIndexOf = completeFileName.lastIndexOf(javaSeparator);
				return completeFileName.substring(lastIndexOf+1);
			}		
		}else{
			int lastIndexOf = completeFileName.lastIndexOf(fileSeparator);
			return completeFileName.substring(lastIndexOf+1);
		}
	}
	
	/**
	 * Returns a string containing the localhost IP address.
	 * 
	 * @return The IP address String
	 */
	public static String getLocalIPAddressAsString(){
		String ipAddrStr = "";
		try {
	        InetAddress addr = InetAddress.getLocalHost();
	        byte[] ipAddr = addr.getAddress();
	    
	        // Convert to dot representation
	        
	        for (int i=0; i<ipAddr.length; i++) {
	            if (i > 0) {
	                ipAddrStr += ".";
	            }
	            ipAddrStr += ipAddr[i]&0xFF;
	        }
	    } catch (UnknownHostException e) {
	    	SpagoBITracer.critical("SpagoBIUtilities", GeneralUtilities.class.getName(), "getLocalIPAddressAsString", "Excecption", e);
	    }
	    return ipAddrStr;
	}
	
	/**
	 * Returns the context address for SpagoBI as an URL and puts it into a string. The
	 * information contained are the Srever name and port. Before saving, both them are written 
	 * into the output console.
	 * 
	 * @return A String with SpagoBI's context adderss
	 */
	public static String getSpagoBiContextAddress(){
		RequestContainer aRequestContainer = RequestContainer.getRequestContainer();
		PortletRequest portletRequest = PortletUtilities.getPortletRequest();
		return "http://"+portletRequest.getServerName()+ ":"+portletRequest.getServerPort() +"/spagobi"; 
	
	}
	
	/**
	 * Gets the spagoBI's content repository servlet information as a string.
	 * 
	 * @return A string containing spagoBI's content repository servlet information
	 */
	public static String getSpagoBiContentRepositoryServlet(){
		
	    return getSpagoBiContextAddress() + "/ContentRepositoryServlet";
	}
	
	
	
	/**
	 * Get all the possible profile attributes of the users.
	 * The attributes are contained into a configuration file which contains the name 
	 * of the attribute and the test value of the attribute. The test value is used during 
	 * the test of a script that use the attribute. 
	 * 
	 * @return HashMap of the attributes. HashMap keys are profile attribute.
	 * HashMap values are test values. 
	 * 
	 */
	public static HashMap getAllProfileAttributes() {
		List attrs = ConfigSingleton.getInstance().getAttributeAsList("PROFILE_ATTRIBUTES.ATTRIBUTE");
		HashMap attrsMap = new HashMap();
		if(attrs==null) {
			return attrsMap;
		}
		Iterator iterAttrs = attrs.iterator();
		SourceBean attrSB = null;
		String nameattr = null;
		String attrvalue = null;
		while(iterAttrs.hasNext()) {
			attrSB = (SourceBean)iterAttrs.next();
			if(attrSB==null)
				continue;
			nameattr = (String)attrSB.getAttribute("name");
		    attrvalue = (String)attrSB.getAttribute("valuefortest");
		    attrsMap.put(nameattr, attrvalue);
		}
		return attrsMap;
	}
	
	
	public static Binding fillBinding(HashMap attrs) {
		Binding bind = new Binding();
		Set setattrs = attrs.keySet();
		Iterator iterattrs = setattrs.iterator();
		String key = null;
		String value = null;
		while(iterattrs.hasNext()) {
			key = iterattrs.next().toString();
			value = attrs.get(key).toString();
			bind.setVariable(key, value);
		}
		return bind;
	}
	
	
	public static Binding fillBinding(IEngUserProfile profile) {
		
		Binding bind = new Binding();
		HashMap allAttrs = getAllProfileAttributes();
		Set setattrs = allAttrs.keySet();
		Iterator iterattrs = setattrs.iterator();
		String key = null;
		Object valueobj = null;
		while(iterattrs.hasNext()) {
			key = iterattrs.next().toString();
			try {
				valueobj = profile.getUserAttribute(key);
			} catch (Exception e) {
				valueobj = null;
			}
			if(valueobj!=null)
				bind.setVariable(key, valueobj.toString());
		}
		return bind;
	}
	
	
	public static String testScript(String script, Binding bind) throws Exception {
		String result = "";
		// get the sourcebena of the default script language
		SourceBean scriptLangSB = (SourceBean)ConfigSingleton.getInstance().
								  getFilteredSourceBeanAttribute("SPAGOBI.SCRIPT_LANGUAGE_SUPPORTED.SCRIPT_LANGUAGE", 
																 "default", "true");
		// get the name of the default script language
		String name = (String)scriptLangSB.getAttribute("name");
		// the only language supported now is groovy so if the default script isn't groovy
		// throw an exception and return an empty string
		if(!name.equalsIgnoreCase("groovy")) {
			SpagoBITracer.critical("GeneralUtilities", 
								   GeneralUtilities.class.getName(), 
								   "testScript", "The only script language supported is groovy, " +
								   "the configuration file has no configuration for groovy");
			return "";
		}
		GroovyShell shell = new GroovyShell(bind);
		Object value = shell.evaluate(script);
        result = value.toString();
        return result;
        
        /*
         * implementation with bsf, seems not possible to laucha groovy expression with Bindings
         * so we use groovy directly
         * 
		String name = (String)scriptLangSB.getAttribute("name");
		String engclass = (String)scriptLangSB.getAttribute("engineclass");
		String id = (String)scriptLangSB.getAttribute("identifier");
		String shortid = (String)scriptLangSB.getAttribute("shortidentifier");
		BSFManager.registerScriptingEngine(name, engclass, new String[] { id, shortid }	);
        BSFManager manager = new BSFManager(); 
        try {
        	Object answer = manager.eval(name, "Test1.groovy", 0, 0, script);
        	result = answer.toString();
        } catch (BSFException e1) {
        	e1.printStackTrace();
        }
        */
	}
	
	
}
