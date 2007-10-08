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
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.Domain;
import it.eng.spagobi.bo.Engine;
import it.eng.spagobi.bo.LowFunctionality;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.bo.TemplateVersion;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.security.IPortalSecurityProvider;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;
import it.eng.spagobi.utilities.UploadedFile;

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
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;

import sun.misc.BASE64Decoder;

/**
 * A servlet used to manage the requests of the spagobi ireport plugin
 * 
 * @author Luca Fiscato
 */
public class PublishServlet extends HttpServlet{
	
	
	private final String OP_PAR_NOT_FOUND_MSG = "Operation parameter not found";
	private final int OP_PAR_NOT_FOUND = 10;
	
	/**
	 * Init method definition
	 */
	public void init(ServletConfig config) throws ServletException {
        super.init(config);
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
   	    if(operation.equalsIgnoreCase("PUBLISH")) {
   	    	publishManager(mapPar, out, response);
   	    	return;
   	    }    	
	 }
	
	
	private void publishManager(Map mapPar, OutputStream out, HttpServletResponse response) {
		BASE64Decoder decoder = new BASE64Decoder();
		String encodedTemplate = (String)mapPar.get("TEMPLATE");
		byte[] buffer = null;
		try {
			buffer = decoder.decodeBuffer(encodedTemplate);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String template = new String(buffer);
		mapPar.put("TEMPLATE", template);
		
		
		
		
		try {
			String label = (String)mapPar.get("LABEL");
			BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectByLabel(label);
			if(obj == null) {
				obj = createBIObject();
				initBIObject(mapPar, obj);
				DAOFactory.getBIObjectDAO().insertBIObject(obj);
			} else {
				updateBIObject(mapPar, obj);
				DAOFactory.getBIObjectDAO().modifyBIObject(obj);
			}
			
			
		} catch (EMFUserError e) {
			e.printStackTrace();
		}
	}
	
	private void updateBIObject(Map mapPar, BIObject obj) {	
		String name = (String)mapPar.get("NAME");
		String description = (String)mapPar.get("DESCRIPTION");
		String encryptStr = (String)mapPar.get("ENCRYPTED");
		Integer encrypt = (encryptStr.equalsIgnoreCase("" + false)?new Integer(0):new Integer(1));
		String visibleStr = (String)mapPar.get("VISIBLE");
		Integer visible = (visibleStr.equalsIgnoreCase("" + false)?new Integer(0):new Integer(1));
		String type = (String)mapPar.get("TYPE");
		
		int t = -1;
		try {
			List biobjTypes = DAOFactory.getDomainDAO().loadListDomainsByType("BIOBJ_TYPE");
			for(int i = 0; i < biobjTypes.size(); i++) {
				Domain domain = (Domain)biobjTypes.get(i);
				if(domain.getValueCd().equals(type)) t = domain.getValueId().intValue();
			}
		} catch (EMFUserError e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		Integer typeIdInt = new Integer(t);
		
		Engine engine = null;
		List engines = null;
		try {
			engines = DAOFactory.getEngineDAO().loadAllEngines();
		} catch (EMFUserError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i = 0; i < engines.size(); i++) {
			engine = (Engine)engines.get(i);
			if(engine.getBiobjTypeId().intValue() == typeIdInt.intValue()) break;			
		}
		
		obj.setName(name);
		obj.setDescription(description);
		
		obj.setEncrypt(encrypt);
		obj.setVisible(visible);
		
		obj.setEngine(engine);		
		
		UploadedFile file = new UploadedFile();
		String template = (String)mapPar.get("TEMPLATE");
		file.setFileContent(template.getBytes());
		file.setFileName("etlTemplate.xml");
		file.setSizeInBytes(template.getBytes().length);
		obj.setTemplate(file);
		
		Domain domain = null;
		try {
			domain = DAOFactory.getDomainDAO().loadDomainById(engine.getBiobjTypeId());
		} catch (EMFUserError e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		obj.setBiObjectTypeCode(domain.getValueCd());
		
		/*
		TemplateVersion curVer = new TemplateVersion();
		curVer.setVersionName("x");
        curVer.setDataLoad("x");
        obj.setCurrentTemplateVersion(curVer);
        */
		
		obj.setBiObjectTypeID(typeIdInt);		
	}
	
	private void initBIObject(Map mapPar, BIObject obj) {	
		
		// input ...
		String label = (String)mapPar.get("LABEL");
		String name = (String)mapPar.get("NAME");
		String description = (String)mapPar.get("DESCRIPTION");
		String encryptStr = (String)mapPar.get("ENCRYPTED");
		Integer encrypt = (encryptStr.equalsIgnoreCase("" + false)?new Integer(0):new Integer(1));
		String visibleStr = (String)mapPar.get("VISIBLE");
		Integer visible = (visibleStr.equalsIgnoreCase("" + false)?new Integer(0):new Integer(1));
		String functionalitiyCode = (String)mapPar.get("FUNCTIONALITYCODE");	
		String state = (String)mapPar.get("STATE");
		String type = (String)mapPar.get("TYPE");
		
		
		int t = -1;
		try {
			List biobjTypes = DAOFactory.getDomainDAO().loadListDomainsByType("BIOBJ_TYPE");
			for(int i = 0; i < biobjTypes.size(); i++) {
				Domain domain = (Domain)biobjTypes.get(i);
				if(domain.getValueCd().equals(type)) t = domain.getValueId().intValue();
			}
		} catch (EMFUserError e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		Integer typeIdInt = new Integer(t);
		
		Engine engine = null;
		List engines = null;
		try {
			engines = DAOFactory.getEngineDAO().loadAllEngines();
		} catch (EMFUserError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i = 0; i < engines.size(); i++) {
			engine = (Engine)engines.get(i);
			if(engine.getBiobjTypeId().intValue() == typeIdInt.intValue()) break;			
		}
		
		System.out.println(engine.getName());		
		
		obj.setLabel(label);
		obj.setName(name);
		obj.setDescription(description);
		
		obj.setEncrypt(encrypt);
		obj.setVisible(visible);
		
		obj.setEngine(engine);		
		
		UploadedFile file = new UploadedFile();
		String template = (String)mapPar.get("TEMPLATE");
		file.setFileContent(template.getBytes());
		file.setFileName("etlTemplate.xml");
		file.setSizeInBytes(template.getBytes().length);
		obj.setTemplate(file);
		
		Domain domain = null;
		try {
			domain = DAOFactory.getDomainDAO().loadDomainById(engine.getBiobjTypeId());
		} catch (EMFUserError e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		obj.setBiObjectTypeCode(domain.getValueCd());
		
		TemplateVersion curVer = new TemplateVersion();
		curVer.setVersionName("x");
        curVer.setDataLoad("x");
        obj.setCurrentTemplateVersion(curVer);
        
		obj.setBiObjectTypeID(typeIdInt);		
		
		obj.setStateCode(state);
		obj.setStateID(new Integer(55));
		
		List functionalities = new ArrayList();
		try {
			//functionalities = DAOFactory.getLowFunctionalityDAO().loadSubLowFunctionalities(path, false);
			functionalities = DAOFactory.getLowFunctionalityDAO().loadAllLowFunctionalities(false);
		} catch (EMFUserError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List funcs = new ArrayList();
		for(int i = 0; i < functionalities.size(); i++) {
			LowFunctionality functionality = (LowFunctionality)functionalities.get(i);
			if(functionality.getCode().equals(functionalitiyCode)) {
				
				Integer id = functionality.getId();
				funcs.add(id);
				break;
			}
		}
		
		
		obj.setFunctionalities(funcs);
	}
	
	private BIObject createBIObject() {
		BIObject obj = new BIObject();
       
		List functionalitites = new ArrayList();
		TemplateVersion curVer = new TemplateVersion();
		curVer.setVersionName("");
        curVer.setDataLoad("");
		
		obj.setId(new Integer(0));
        obj.setEngine(null);
        obj.setDescription("");
        obj.setLabel("");
        obj.setName("");
        obj.setEncrypt(new Integer(0));
        obj.setVisible(new Integer(1));
        obj.setRelName("");
        obj.setStateID(null);
        obj.setStateCode("");
        obj.setBiObjectTypeID(null);
        obj.setBiObjectTypeCode("");        
        obj.setFunctionalities(functionalitites);        
        obj.setCurrentTemplateVersion(curVer);
        obj.setTemplateVersions(new TreeMap());
        
        return obj;
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
