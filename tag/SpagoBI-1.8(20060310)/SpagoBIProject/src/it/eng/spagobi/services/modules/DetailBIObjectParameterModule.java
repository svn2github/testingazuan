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
package it.eng.spagobi.services.modules;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.Parameter;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectParameterDAO;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

/**
 * Implements a module which  handles all BI Object parameters management: has methods for parameters load 
 * detail, modify/insertion and deleting operations. The <code>service</code> method has  a 
 * switch for all these operations, differentiated the ones from the others by a <code>message</code> String.
 * 
 * @author sulis
 */

public class DetailBIObjectParameterModule extends AbstractModule {

	private String modality = "";
	public final static String MODULE_PAGE = "DetailBIObjectParameterPage";
	public final static String NAME_ATTR_OBJECT_PAR = "OBJECT_PAR";
	
	public void init(SourceBean config) {
	}

	/**
	 * Reads the operation asked by the user and calls the insertion, modify, detail and 
	 * deletion methods
	 * 
	 * @param request The Source Bean containing all request parameters
	 * @param response The Source Bean containing all response parameters
	 * @throws exception If an exception occurs
	 * 
	 */
	public void service(SourceBean request, SourceBean response) throws Exception {
		
		String message = (String) request.getAttribute(AdmintoolsConstants.MESSAGE_DETAIL);
		SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectParameterModule","service","begin of detail functionality modify/visualization service with message =" +message);
        
		try {
			if (message == null) {
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 101);
				SpagoBITracer.debug(AdmintoolsConstants.NAME_MODULE, "DetailBIObjectParameterModule", "service", "The message parameter is null");
				throw userError;
			}
			if (message.trim().equalsIgnoreCase(AdmintoolsConstants.DETAIL_SELECT)) {
				String id = (String) request.getAttribute("BIOBJ_ID");
				String parId = (String) request.getAttribute("PAR_ID");
				getDetailObjPar(id,parId, response);
			} else if (message.trim().equalsIgnoreCase(AdmintoolsConstants.DETAIL_MOD)) {
				//objParControl(request);
				modDetailObjPar(request, AdmintoolsConstants.DETAIL_MOD, response);
			} else if (message.trim().equalsIgnoreCase(AdmintoolsConstants.DETAIL_NEW)) {
				newDetailBIObjPar(request, response);
			} else if (message.trim().equalsIgnoreCase(AdmintoolsConstants.DETAIL_INS)) {
				objParControl(request);
				modDetailObjPar(request, AdmintoolsConstants.DETAIL_INS, response);
			} else if (message.trim().equalsIgnoreCase(AdmintoolsConstants.DETAIL_DEL)) {
				delDetailObjPar(request, AdmintoolsConstants.DETAIL_DEL, response);
			}

		} catch (EMFUserError eex) {
			EMFErrorHandler errorHandler = getErrorHandler();
			errorHandler.addError(eex);
			return;
		} catch (Exception ex) {
			EMFInternalError internalError = new EMFInternalError(EMFErrorSeverity.ERROR, ex);
			EMFErrorHandler errorHandler = getErrorHandler();
			errorHandler.addError(internalError);
			return;
		}
	}


	/**
	 * Instantiates a new <code>BIObjectParameter<code> object when a new  BI Object 
	 * parameter insertion is required, in order to prepare the page for the insertion.
	 * 
	 * @param response The response SourceBean
	 * @throws EMFUserError If an Exception occurred
	 */
	
	private void newDetailBIObjPar(SourceBean request, SourceBean response) throws EMFUserError {
		try {
			String biobjIdStr = (String)request.getAttribute(ObjectsTreeConstants.OBJECT_ID);
			
			Integer biobjId = new Integer(biobjIdStr);
			
			
        	BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectForDetail(biobjId);
		
        	response.setAttribute(ObjectsTreeConstants.PATH, obj.getPath());
			response.setAttribute(ObjectsTreeConstants.MODALITY, ObjectsTreeConstants.DETAIL_INS);
            			
			BIObjectParameter param = new BIObjectParameter();
            param.setBiObjectID(biobjId);
            param.setLabel("");
            param.setModifiable(new Integer(0));
            param.setMultivalue(new Integer(0));
            param.setParameter(null);
            param.setParameterUrlName("");
            param.setProg(new Integer(0));
            param.setRequired(new Integer(0));
            param.setVisible(new Integer(0));

			response.setAttribute(NAME_ATTR_OBJECT_PAR, param);	
			
		}  catch (Exception ex) {
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectParameterModule","newDetailBIObjectParameter","Cannot prepare page for the insertion", ex  );
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	/**
	 * Gets the detail of a BI Object parameter choosed by the user from the BI Objects
	 * parameters list. It reaches the key from the request and asks to the DB all detail
	 * BI objects parameter information, by calling the method <code>loadBIObjectParameterForDetail</code>.
	 *   
	 * @param key The choosed BIObject parameter id key
	 * @param response The response Source Bean
	 * @throws EMFUserError If an exception occurs
	 */
	private void getDetailObjPar(String key,String key1, SourceBean response) throws EMFUserError {
		try {
			this.modality = AdmintoolsConstants.DETAIL_MOD;
			response.setAttribute("modality", modality);	
			
		    
			
			
			Integer biObjectID = new Integer(key);
			Integer biObjectParameterID = new Integer(key1);
			
			
			BIObjectParameter objPar = DAOFactory.getBIObjectParameterDAO().loadBIObjectParameterForDetail(biObjectID, biObjectParameterID);
			
			
        	BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectForDetail(objPar.getBiObjectID());
		
        	response.setAttribute(ObjectsTreeConstants.PATH, obj.getPath());
			
			response.setAttribute(DetailBIObjectParameterModule.NAME_ATTR_OBJECT_PAR, objPar);		
			
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "DetailBIObjectParameterModule","getDetailDomains","Cannot fill response container", ex  );
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	/**
	 * Inserts/Modifies the detail of a BI object parameter according to the user request. 
	 * When a BI object parameter is modified, the <code>modifyBIObjectParameter</code> method is called; when a new
	 * parameter is added, the <code>insertBIObjectParameter</code>method is called. These two cases are 
	 * differentiated by the <code>mod</code> String input value 
	 * 
	 * @param request The request information contained in a SourceBean Object
	 * @param mod A request string used to differentiate insert/modify operations
	 * @param response The response SourceBean 
	 * @throws EMFUserError If an exception occurs
	 * @throws SourceBeanException If a SourceBean exception occurs
	 */
	private void modDetailObjPar(SourceBean request, String mod, SourceBean response)
	throws EMFUserError, SourceBeanException {
		
	try {
		String mode = (String)request.getAttribute("MESSAGEDET");
		BIObjectParameter objPar  = new BIObjectParameter();
		if(!mode.equals("DETAIL_INS")){
		
		String parIdOld = (String)request.getAttribute("parIdOld");
		
		Integer parIdOldBD = new Integer(parIdOld);
		objPar.setParIdOld(parIdOldBD);  }
		
		String id = (String)request.getAttribute("id");
		
		Integer idBD = new Integer(id);
		
		String parId = (String)request.getAttribute("par_id");
		
		Integer parIdBD = new Integer(parId);
		
		String label = (String)request.getAttribute("label");
		
		String reqFl = (String)request.getAttribute("req_fl");
		Integer reqFlBD = new Integer(reqFl);
		
		String modFl = (String) request.getAttribute("mod_fl");
		Integer modFlBD = new Integer(modFl);
		
		String viewFl = (String) request.getAttribute("view_fl");
		Integer viewFlBD = new Integer(viewFl);
		
		String multFl = (String) request.getAttribute("mult_fl");
		Integer multFlBD = new Integer(multFl);
		
		
		// BigDecimal prog = (BigDecimal) request.getAttribute("prog");
	    String parUrlNm = (String)request.getAttribute("parurl_nm");
	   
		 
       
        objPar.setBiObjectID(idBD);
        Parameter par = new Parameter();
        par.setId(parIdBD);
        objPar.setParameter(par);
        objPar.setLabel(label);
        objPar.setRequired(reqFlBD);
        objPar.setModifiable(modFlBD);
        objPar.setVisible(viewFlBD);
        objPar.setMultivalue(multFlBD);
       // objPar.setProg(prog);
        objPar.setParameterUrlName(parUrlNm);
        
        IBIObjectParameterDAO objParDAO = DAOFactory.getBIObjectParameterDAO();
        
		if (mod.equalsIgnoreCase(AdmintoolsConstants.DETAIL_INS)) {
			objParDAO.insertBIObjectParameter(objPar);
		} else {
			objParDAO.modifyBIObjectParameter(objPar);
		}
		
		
		
    	BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectForDetail(objPar.getBiObjectID());
		response.setAttribute(ObjectsTreeConstants.OBJECT_ID, obj.getId());
		response.setAttribute(ObjectsTreeConstants.PATH, obj.getPath());
        
	} catch (Exception ex) {			
		SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "DetailDomainsModule","modDetailDomains","Cannot fill response container", ex  );
		throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
	}
	response.setAttribute("loopback", "true");
}

	/**
	 * Deletes a BI Object Parameter choosed by user from the BI Object Parameters list.
	 * 
	 * @param request	The request SourceBean
	 * @param mod	A request string used to differentiate delete operation
	 * @param response	The response SourceBean
	 * @throws EMFUserError	If an Exception occurs
	 * @throws SourceBeanException If a SourceBean Exception occurs
	 */
	
	private void delDetailObjPar(SourceBean request, String mod, SourceBean response)
	throws EMFUserError, SourceBeanException {
		try {

			Integer biObjectID = Integer.valueOf((String)request.getAttribute("BIOBJ_ID"));
			Integer biObjectParameterID = Integer.valueOf((String)request.getAttribute("PAR_ID"));	
			IBIObjectParameterDAO biObjectParameterDAO = DAOFactory.getBIObjectParameterDAO();
			BIObjectParameter objPar = biObjectParameterDAO.loadBIObjectParameterForDetail(biObjectID, biObjectParameterID);
			biObjectParameterDAO.eraseBIObjectParameter(objPar);
			
	    	BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectForDetail(objPar.getBiObjectID());
	    	response.setAttribute(ObjectsTreeConstants.OBJECT_ID, obj.getId());
			response.setAttribute(ObjectsTreeConstants.PATH, obj.getPath());
			
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "DetailBIObjectParameterModule","delDetailObjPar","Cannot fill response container", ex  );
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
		response.setAttribute("loopback", "true");
	}
	/**
	 * Controls if a BI object parameter is associated or not to parameter;
	 * if it is, the BI Object Parameter cannot be inserted/modified, 
	 * because it is in use.
	 * 
	 * @param request The request Source Bean
	 * @throws EMFUserError If any exception occurred
	 */
	public void objParControl (SourceBean request) throws EMFUserError{
		String id = (String)request.getAttribute("id");
		Integer idInt = Integer.valueOf(id);
		String par_id = (String)request.getAttribute("par_id");
		List list = DAOFactory.getBIObjectParameterDAO().loadBIObjectParametersById(idInt);
		Iterator i = list.iterator();
		while(i.hasNext()){
			BIObjectParameter BIobjPar = (BIObjectParameter)i.next();
			Parameter param = BIobjPar.getParameter();
			String paramId = param.getId().toString();
			if (paramId.equals(par_id)){
				HashMap params = new HashMap();
				params.put(AdmintoolsConstants.PAGE, ListBIObjectParametersModule.MODULE_PAGE);
				params.put(AdmintoolsConstants.OBJECT_ID, id);
				String path = (String)request.getAttribute("path");
				params.put(ObjectsTreeConstants.PATH,path);
				throw new EMFUserError(EMFErrorSeverity.ERROR, 1026, new Vector(), params);		
				
			}
			
		}
	}
}

