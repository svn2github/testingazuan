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

package it.eng.spagobi.services;

import it.eng.spago.base.SourceBean;
import it.eng.spago.cms.CmsManager;
import it.eng.spago.cms.CmsNode;
import it.eng.spago.cms.CmsProperty;
import it.eng.spago.cms.operations.GetOperation;
import it.eng.spago.cms.operations.SetOperation;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.Engine;
import it.eng.spagobi.bo.LowFunctionality;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.security.IPortalSecurityProvider;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;

/**
 * A servlet used to manage the requests of the spagobi ireport plugin
 * 
 * @author Luca Fiscato
 */
public class IReportPluginServlet extends HttpServlet{
	
	private final String ATTR_PATH_SYS_FUNCT = "";
	private final int OP_PAR_NOT_FOUND = 10;
	private final int USER_PAR_NOT_FOUND = 11;
	private final int PWD_PAR_NOT_FOUND = 12;
	private final int USER_NOT_AUTH = 13;
	private final int PATH_PAR_NOT_FOUND = 14;
	private final int FILENAME_PAR_NOT_FOUND = 18;
	private final int TREE_GEN_ERROR = 15;
	private final int ERROR_CHECK_IN = 16;
	private final int ERROR_CHECK_OUT = 16;
	private final int ERROR_CMS_FILE = 17;
	private final int ERROR_TREE_RECOVER = 20;
	private final String OP_PAR_NOT_FOUND_MSG = "Operation parameter not found";
	private final String USER_PAR_NOT_FOUND_MSG = "Username not found";
	private final String PWD_PAR_NOT_FOUND_MSG = "Password not found";
	private final String USER_NOT_AUTH_MSG = "User not Authenticated";
	private final String PATH_PAR_NOT_FOUND_MSG = "Parameter path not found";
	private final String TREE_GEN_ERROR_MSG = "Error during tree generation";
	private final String ERROR_CHECK_IN_MSG = "Cannot checkin file";
	private final String ERROR_CMS_FILE_MSG = "Cannot retrive file";
	private final String FILENAME_PAR_NOT_FOUND_MSG = "Filename not found";
	private final String ERROR_CHECK_OUT_MSG = "Cannot checkout file";
	private final String ERROR_TREE_RECOVER_MSG = "Cannot recover the business object tree";
	
	private List drivers = new ArrayList();
	
	/**
	 * Init method definition
	 */
	public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String driversStr = config.getInitParameter("DRIVERS");
        String[] driversArr = driversStr.split(";");
        drivers = Arrays.asList(driversArr);
     } 
	
    /**
     * Service method definition
     * 
     * @param request The http servlet request
     * @param response The http servlet response
     * @throws IOException If any exception occurred
     */
     //	name = (String)mapPar.get("name");
     //path = (String)mapPar.get("path");
	public void service(HttpServletRequest request, HttpServletResponse response) 
						throws IOException, ServletException {
	 	
		OutputStream out = response.getOutputStream();
        // get parameters map		
        Map mapPar = getParameter(request);
	    // get operation parameter
   	    String operation = (String)mapPar.get("OPERATION");
   	    // if no operation parameter is found send back error
   	    if((operation==null) || (operation.trim().equals(""))) {
			String msgErr = createErrorMessage(this.OP_PAR_NOT_FOUND, this.OP_PAR_NOT_FOUND_MSG);
			flushOut(msgErr, out);
		 	return;
		}
   	    // switch operation type
   	    if(operation.equalsIgnoreCase("LOGIN"))  {
	   	    loginManager(mapPar, out);
   	        return;
   	    } else if(operation.equalsIgnoreCase("CHECKOUT")) {
   	    	checkoutManager(mapPar, out, response);
   	    	return;
   	    } else if(operation.equalsIgnoreCase("CHECKIN")) {
   	    	checkinManager(mapPar, out);
   	    	return;
   	    }    	
	 }
	
	
	
	/**
	 * Manage the checkout request
	 * 
	 * @param mapPar request parameters
	 * @param out buffer output
	 * @param response http response
	 */
	private void checkoutManager(Map mapPar, OutputStream out, HttpServletResponse response) {
		// get path 
	   	String path = (String)mapPar.get("PATH");
	   	// if no password parameter is found send back error
	   	if((path==null) || (path.trim().equals(""))) {
			String msgErr = createErrorMessage(this.PATH_PAR_NOT_FOUND, this.PATH_PAR_NOT_FOUND_MSG);
			flushOut(msgErr, out);
			return;
		}
	   	// get the content from the cms
	   	path += "/template";
	    InputStream jcrContentStream = null;
		try{
            CmsManager manager = new CmsManager();
			GetOperation getOp = new GetOperation();
			getOp.setPath(path);
			getOp.setRetriveContentInformation("true");
			getOp.setRetrivePropertiesInformation("true");
			getOp.setRetriveVersionsInformation("false");
			getOp.setRetriveChildsInformation("false");
			CmsNode cmsnode = manager.execGetOperation(getOp);
			jcrContentStream = cmsnode.getContent();
			String fileName = "NoName";
			List properties = cmsnode.getProperties();
			Iterator iterProps = properties.iterator();
			while(iterProps.hasNext()){
				CmsProperty prop = (CmsProperty)iterProps.next();
				if(prop.getName().equalsIgnoreCase("fileName"))
					 fileName = prop.getStringValues()[0];
			}
			byte[] jcrContent = GeneralUtilities.getByteArrayFromInputStream(jcrContentStream);
			response.setHeader("Content-Disposition","attachment; filename=\"" + fileName + "\";");
			response.setContentLength(jcrContent.length);
			response.setStatus(200);
			response.getOutputStream().write(jcrContent);
	 	    response.getOutputStream().flush();
		} catch (Exception e) {
			SpagoBITracer.critical("SPAGOBI", this.getClass().getName(),
								   "checkoutManager", "Checkout Error:", e);
			String msgerr = createErrorMessage(this.ERROR_CHECK_OUT, this.ERROR_CHECK_OUT_MSG);
   	        flushOut(msgerr, out);
   	        return;
		}
	}
	
	
	
	
	/**
	 * Manage the checkin request
	 * 
	 * @param mapPar map of the request parameters
	 * @param out buffer output
	 */
	private void checkinManager(Map mapPar, OutputStream out) {
		// get path 
	    String path = (String)mapPar.get("PATH");
        // if no path parameter is found send back error
	   	if((path==null) || (path.trim().equals(""))) {
			String msgErr = createErrorMessage(this.PATH_PAR_NOT_FOUND, this.PATH_PAR_NOT_FOUND_MSG);
			flushOut(msgErr, out);
			return;
		}
	   	// get name file
	   	String nameFile = (String)mapPar.get("FILENAME");
        // if no nameFile parameter is found send back error
	   	if((nameFile==null) || (nameFile.trim().equals(""))) {
			String msgErr = createErrorMessage(this.FILENAME_PAR_NOT_FOUND, this.FILENAME_PAR_NOT_FOUND_MSG);
			flushOut(msgErr, out);
			return;
		}
	   	try{
   	    	InputStream fileis = (InputStream)mapPar.get("FILE");
            CmsManager manager = new CmsManager();
   	    	SetOperation setOp = new SetOperation();
	        setOp.setContent(fileis);
	        setOp.setType(SetOperation.TYPE_CONTENT);
	        setOp.setPath(path+"/template");
	        setOp.setEraseOldProperties(false);
	        List properties = new ArrayList();
	        String[] nameFilePropValues = new String[] { nameFile };
	        String today = new Long(new Date().getTime()).toString();
	        //String today = new Date().toString();
	        String[] datePropValues = new String[] { today };
	        CmsProperty propname = new CmsProperty("fileName", nameFilePropValues);
	        CmsProperty propdateLoad = new CmsProperty("dateLoad", datePropValues);
	        properties.add(propname);
	        properties.add(propdateLoad);
	        setOp.setProperties(properties);
            manager.execSetOperation(setOp);
	        String msg = createResponseMessage("");
	        flushOut(msg, out);
	        return;
	   } catch (Exception e) {
				SpagoBITracer.critical("SPAGOBI", this.getClass().getName(),
									   "checkinManager", "Checkin Error:", e);
	   	     	String msgerr = createErrorMessage(this.ERROR_CHECK_IN, this.ERROR_CHECK_IN_MSG);
	   	        flushOut(msgerr, out);
	   	        return;
	   }
	}
		
	
	
	
    /**
     * Manage the login request, authenticate the user and send back the object tree or an error message
     * 
     * @param mapPar map of the request parameter
     * @param out output buffer
     */
	private void loginManager(Map mapPar, OutputStream out) {
		// get user name 
	    String username = (String)mapPar.get("USERNAME");
   	    // if no username parameter is found send back error
	   	if((username==null) || (username.trim().equals(""))) {
			String msgErr = createErrorMessage(this.USER_PAR_NOT_FOUND, this.USER_PAR_NOT_FOUND_MSG);
			flushOut(msgErr, out);
			return;
		}
	    String password = (String)mapPar.get("PASSWORD");
   	    // if no password parameter is found send back error
	   	if((password==null) || (password.trim().equals(""))) {
	   		String msgErr = createErrorMessage(this.PWD_PAR_NOT_FOUND, this.PWD_PAR_NOT_FOUND_MSG);
	   		flushOut(msgErr, out);
	   		return;
		}
	   	// get user roles
	    List roles = getRoles(username, password);
	    // if list of roles is empty user is not authenticated
	    if(roles.isEmpty()) {
           	String msgErr = createErrorMessage(this.USER_NOT_AUTH, this.USER_NOT_AUTH_MSG);
     		flushOut(msgErr, out);
           	return;
        } 
	    
	    List tree = null;
	    try{
	    	tree = DAOFactory.getLowFunctionalityDAO().loadAllLowFunctionalities(true);
	    } catch(Exception e) {
	    	SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
	    						"loginManager", "Cannot recover business object tree", e);
	    	String msgErr = createErrorMessage(this.ERROR_TREE_RECOVER, this.ERROR_TREE_RECOVER_MSG);
     		flushOut(msgErr, out);
           	return;
	    }
	    
	    SourceBean treeSB = filterToSourceBean(tree, roles);
	    String treeStr = "";
	    if(treeSB!=null) { 
		    treeStr = treeSB.toString();
		    int firstTagClose = treeStr.indexOf(">");
		    treeStr = treeStr.substring(firstTagClose+1);
		    treeStr = treeStr.trim();
	    }
	    String respMsg = createResponseMessage(treeStr);
		flushOut(respMsg, out);
	}
	
	
	/**
	 * Filter the object tree, base on the user roles, calling a recursive function. 
	 * 
	 * @param tree List of the functionalities (each one contains objects)
	 * @param userRoles list of the user roles
	 * @return SourceBean of the filtered tree
	 */
	private SourceBean filterToSourceBean(List tree, List userRoles) {
		SourceBean treeSB = null;
		try{
			LowFunctionality rootFunct = null;
			Iterator iterTree = tree.iterator();
			while(iterTree.hasNext())  {
				 LowFunctionality funct = (LowFunctionality)iterTree.next();
				 if(funct.getParentId()==null){
					 rootFunct = funct;
				 }
			}
			List userRolesNames = new ArrayList();
			Iterator iteruro = userRoles.iterator();
			while(iteruro.hasNext()) {
				Role role = (Role)iteruro.next();
				if(role!=null) {
					userRolesNames.add(role.getName());
				}
			}
			treeSB = addFunctToSB(rootFunct, tree, userRolesNames);
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					            "filterToSourceBean", "error while converting tree into sourcebean", e);
		}
		return treeSB;
	}
	
	
	/**
	 * Recursive function that checks if a functionalities and the contained objects dhould be added
	 * to the tree of document to send to the client. 
	 * 
	 * @param funct Functionality to check
	 * @param tree List of all the fucntionalities
	 * @return userRoles List of the user roles names
	 */
	private SourceBean addFunctToSB(LowFunctionality funct, List tree, List userRoles) {
		SourceBean folderSB = null; 
		try{
			folderSB = new SourceBean("folder");
			Integer parentId = funct.getParentId();
			Integer id = funct.getId();

			boolean canDevelop = false;
			if(parentId==null) {
				canDevelop = true; // root case
			} else {
				Role[] devRoles = funct.getDevRoles();
			    for(int i=0; i<devRoles.length; i++) {
			    	Role devRole = devRoles[i];
			    	if(userRoles.contains(devRole.getName())){
			    		canDevelop = true;
			    		break;
			    	}
			    }
			}
		    
		    if(!canDevelop)
		    	return null;

			folderSB.setAttribute("name", funct.getName());
	        List biobjects = funct.getBiObjects();
	        Iterator iterBiobjects = biobjects.iterator();
	        while(iterBiobjects.hasNext()){
	           	BIObject biobject = (BIObject)iterBiobjects.next();
	           	String state = biobject.getStateCode();
	           	String type = biobject.getBiObjectTypeCode();
	           	Engine engine = biobject.getEngine();
	           	String drivername = engine.getDriverName();
	           	if( (drivername!=null) && 
	           		drivers.contains(drivername) && 
	           		state.equalsIgnoreCase("dev") &&
	           		type.equalsIgnoreCase("report")) {
                	SourceBean objSB = new SourceBean("object");
					objSB.setAttribute("name", biobject.getName());
					objSB.setAttribute("path", biobject.getPath());
					folderSB.setAttribute(objSB);
				}
	        }
	            
	        //for each child of the functionality call recursively the function
			Iterator iterTree = tree.iterator();
			while(iterTree.hasNext())  {
				 LowFunctionality otherFunct = (LowFunctionality)iterTree.next();
				 Integer parentIdOther = otherFunct.getParentId();
				 if( (parentIdOther!=null) && (parentIdOther.equals(id))){
					 SourceBean childFolderSB = addFunctToSB(otherFunct, tree, userRoles);
					 if(childFolderSB!=null)
						 folderSB.setAttribute(childFolderSB);
				 }
			}      
				
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
		                         "addFunctToSB", "cannot add functionality" + funct.getPath(), e);
		}
		
		if(folderSB.getContainedSourceBeanAttributes().isEmpty())
			return null;
		else return folderSB;
	}
	
	
	
   	/**
   	 * Get the portal roles assigned to a user, if the user doesn't exist the role list is empty 
   	 * 
   	 * @param username username of the user
   	 * @param password password of the user
   	 * @return
   	 */    
    private	List getRoles(String username, String password) {
    	List roles = new ArrayList();
    	ConfigSingleton config = ConfigSingleton.getInstance();
    	SourceBean securityconfSB = (SourceBean)config.getAttribute("SPAGOBI.SECURITY.PORTAL-SECURITY-CLASS");
 	    String portalSecurityProviderClass = (String) securityconfSB.getAttribute("className");
 	    IPortalSecurityProvider portalSecurityProvider = null;
 	    try {
 	    	Class portSecClass = Class.forName(portalSecurityProviderClass);
 	    	portalSecurityProvider = (IPortalSecurityProvider)portSecClass.newInstance();
 	    } catch (Exception e) {
 	    	return roles;
 	    }
 	   SourceBean portSecClassConfig = (SourceBean) securityconfSB.getAttribute("CONFIG");
 	    roles = portalSecurityProvider.getUserRoles(username, portSecClassConfig);
 	    return roles;
    }
	
	
	
	/**
	 * Retrive all parameter from the request and put them into a map. If the parameter is a
	 * file uploaded it will be put into the map whit "FILE" keyword
	 * 
	 * @param request http request 
	 * @return Map of the request parameters
	 */
    private Map getParameter(HttpServletRequest request) {
    	Map mapPar = new HashMap();
    	boolean isMultipart = FileUpload.isMultipartContent(request);
        if(isMultipart) {
         	DiskFileUpload upload = new DiskFileUpload();
         	try {
         		List items = upload.parseRequest(request);
         		Iterator iter = items.iterator();
         		while (iter.hasNext()) {
         			FileItem item = (FileItem) iter.next();
         			if (item.isFormField()) {
         				String name = item.getFieldName().toUpperCase();
         				String value = item.getString();
         				mapPar.put(name,value);	    	    	        	    	
         			} else {
         				InputStream file = item.getInputStream();
         				mapPar.put("FILE", file);
         			}
         		}
         	} catch (IOException ioe) {
         		SpagoBITracer.critical("SPAGOBI", this.getClass().getName(),
						               "getParameter", "error while reading request parameters", ioe);
            } catch (FileUploadException e1) {
            	SpagoBITracer.critical("SPAGOBI", this.getClass().getName(),
						               "getParameter", "error while reading request parameters", e1);
         	}
        } else {
        	Enumeration enumpar = request.getParameterNames();
        	while(enumpar.hasMoreElements()) {
        		String namepar = (String)enumpar.nextElement();
        		String value = request.getParameter(namepar);
        		mapPar.put(namepar.toUpperCase(), value);
        	}
        }
        return mapPar;
    }
        
    /**
     * Create the xml evelope for the response message when no errors occur
     * 
     * @param body body of the response message
     * @return The String format of the xml envelope response
     */
    private String createResponseMessage(String body) {
		String err = "<response><header></header><body>"+body+"</body>" +
	                 "<fault><faultcode></faultcode>" +
	                 "<faultstring></faultstring>" +
	                 "</fault></response>";
		return err; 
	}
    
    
    /**
     * Create the xml evelope for the response message when an error occur
     * 
     * @param code numeric code of the error
     * @param error message of the error
     * @return The String format of the xml envelope response
     */
	private String createErrorMessage(int code, String error) {
		String err = "<response><header></header><body></body>" +
	                 "<fault><faultcode>"+code+"</faultcode>" +
	                 "<faultstring>"+error+"</faultstring>" +
	                 "</fault></response>";
		return err; 
	}

	
	/**
	 * Flush out to the client the response message
	 * 
	 * @param message message that will be sent to the client
	 * @param out OutputStream to flush out
	 */
	
	private void flushOut(String message, OutputStream out) {
		try {
			byte[] msgBy = message.getBytes();
			out.write(msgBy);
			out.flush();
			out.close();
		} catch (IOException ioe) {
			
		}
	}
}
