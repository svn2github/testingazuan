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

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.validation.EMFValidationError;
import it.eng.spago.validation.coordinator.ValidationCoordinator;
import it.eng.spagobi.bo.Domain;
import it.eng.spagobi.bo.Engine;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IEngineDAO;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * This class implements a module which  handles all engine management: has methos for engine load, 
 * details,moodify/insertion and deleting operations. The <code>service</code> method has  a 
 * switch for all these operations, differentiated the ones from the others by a <code>message</code> String.
 * 
 * @author sulis
 */
public class DetailEngineModule extends AbstractModule {
	
	private String modalita = "";
	
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

		String message = (String) request.getAttribute("MESSAGEDET");
		SpagoBITracer.debug(AdmintoolsConstants.NAME_MODULE, "DetailEngineModule","service","begin of detail Engine modify/visualization service with message =" +message);

		EMFErrorHandler errorHandler = getErrorHandler();
		try {
			if (message == null) {
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 101);
				SpagoBITracer.debug(AdmintoolsConstants.NAME_MODULE, "DetailEngineModule", "service", "The message parameter is null");
				throw userError;
			}
			if (message.trim().equalsIgnoreCase(AdmintoolsConstants.DETAIL_SELECT)) {
				String id = (String) request.getAttribute("id");
				getDettaglioEngine(id, response);
			} else if (message.trim().equalsIgnoreCase(AdmintoolsConstants.DETAIL_MOD)) {
				modDettaglioEngine(request, AdmintoolsConstants.DETAIL_MOD, response);
			} else if (message.trim().equalsIgnoreCase(AdmintoolsConstants.DETAIL_NEW)) {
				newDettaglioEngine(response);
			} else if (message.trim().equalsIgnoreCase(AdmintoolsConstants.DETAIL_INS)) {
				modDettaglioEngine(request, AdmintoolsConstants.DETAIL_INS, response);
			} else if (message.trim().equalsIgnoreCase(AdmintoolsConstants.DETAIL_DEL)) {
				delDettaglioEngine(request, AdmintoolsConstants.DETAIL_DEL, response);
			}

		} catch (EMFUserError eex) {
			errorHandler.addError(eex);
			return;
		} catch (Exception ex) {
			EMFInternalError internalError = new EMFInternalError(EMFErrorSeverity.ERROR, ex);
			errorHandler.addError(internalError);
			return;
		}
	}
	/**
	 * Gets the detail of an engine choosed by the user from the 
	 * engines list. It reaches the key from the request and asks to the DB all detail
	 * engine information, by calling the method <code>loadEngineByID</code>.
	 *   
	 * @param key The choosed engine id key
	 * @param response The response Source Bean
	 * @throws EMFUserError If an exception occurs
	 */
	private void getDettaglioEngine(String key, SourceBean response) throws EMFUserError {
		try {
			this.modalita = AdmintoolsConstants.DETAIL_MOD;
			response.setAttribute("modality", modalita);
			Engine engine = DAOFactory.getEngineDAO().loadEngineByID(new Integer(key));
			response.setAttribute("engineObj", engine);
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "DettaglioEngineModule","getDettaglioEngine","Cannot fill response container", ex  );
			HashMap params = new HashMap();
			params.put(AdmintoolsConstants.PAGE, ListEnginesModule.MODULE_PAGE);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 1014, new Vector(), params);
		}
	}
	 /**
	 * Inserts/Modifies the detail of an engine according to the user request. 
	 * When an engine is modified, the <code>modifyEngine</code> method is called; when a new
	 * engine is added, the <code>insertEngine</code>method is called. These two cases are 
	 * differentiated by the <code>mod</code> String input value .
	 * 
	 * @param request The request information contained in a SourceBean Object
	 * @param mod A request string used to differentiate insert/modify operations
	 * @param response The response SourceBean 
	 * @throws EMFUserError If an exception occurs
	 * @throws SourceBeanException If a SourceBean exception occurs
	 */
	private void modDettaglioEngine(SourceBean request, String mod, SourceBean response)
		throws EMFUserError, SourceBeanException {
		
		try {
			//**********************************************************************
			
			String engineTypeIdStr = (String) request.getAttribute("engineTypeId");
			Integer engineTypeId = new Integer(engineTypeIdStr);
			Domain engineType = DAOFactory.getDomainDAO().loadDomainById(engineTypeId);
			
			if ("EXT".equalsIgnoreCase(engineType.getValueCd())) ValidationCoordinator.validate("PAGE", "ExternalEngineDetailPage", this);
			else ValidationCoordinator.validate("PAGE", "InternalEngineDetailPage", this);
			
			Engine engine = recoverEngineDetails(request);
			EMFErrorHandler errorHandler = getErrorHandler();
			
			// if there are some validation errors into the errorHandler does not write into DB
			Collection errors = errorHandler.getErrors();
			if (errors != null && errors.size() > 0) {
				Iterator iterator = errors.iterator();
				while (iterator.hasNext()) {
					Object error = iterator.next();
					if (error instanceof EMFValidationError) {
						response.setAttribute("engineObj", engine);
						response.setAttribute("modality", mod);
						return;
					}
				}
			}
			
			if (mod.equalsIgnoreCase(AdmintoolsConstants.DETAIL_INS)) {
				DAOFactory.getEngineDAO().insertEngine(engine);
			} else {
				DAOFactory.getEngineDAO().modifyEngine(engine);
			}
            
		} catch (EMFUserError e){
			HashMap params = new HashMap();
			params.put(AdmintoolsConstants.PAGE, ListEnginesModule.MODULE_PAGE);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 1012, new Vector(), params);
			
		}
		
		catch (Exception ex) {			
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "DetailEngineModule","modDetailEngine","Cannot fill response container", ex  );
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
		response.setAttribute("loopback", "true");
	}
	
	/**
	 * Deletes an engine choosed by user from the engines list.
	 * 
	 * @param request	The request SourceBean
	 * @param mod	A request string used to differentiate delete operation
	 * @param response	The response SourceBean
	 * @throws EMFUserError	If an Exception occurs
	 * @throws SourceBeanException If a SourceBean Exception occurs
	 */
	private void delDettaglioEngine(SourceBean request, String mod, SourceBean response)
		throws EMFUserError, SourceBeanException {
		
		try {
			String id = (String) request.getAttribute("id");
            IEngineDAO enginedao = DAOFactory.getEngineDAO();
			Engine engine = enginedao.loadEngineByID(new Integer(id));
//			controls if the engine is in use by any BIObject
			boolean isAss = enginedao.hasBIObjAssociated(id);
			if (isAss){
				HashMap params = new HashMap();
				params.put(AdmintoolsConstants.PAGE, ListEnginesModule.MODULE_PAGE);
				EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, 1030, new Vector(), params );
				getErrorHandler().addError(error);
				return;
				
			}
			enginedao.eraseEngine(engine);
		}   catch (EMFUserError e){
			  HashMap params = new HashMap();
			  params.put(AdmintoolsConstants.PAGE, ListEnginesModule.MODULE_PAGE);
			  throw new EMFUserError(EMFErrorSeverity.ERROR, 1013, new Vector(), params);
				
			}
		    catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "DetailEngineModule","delDetailRuolo","Cannot fill response container", ex  );
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
		response.setAttribute("loopback", "true");
	}
	
	
	/**
	 * Instantiates a new <code>engine<code> object when a new engine insertion is required, in order
	 * to prepare the page for the insertion.
	 * 
	 * @param response The response SourceBean
	 * @throws EMFUserError If an Exception occurred
	 */
	
	private void newDettaglioEngine(SourceBean response) throws EMFUserError {
		try {
			Engine engine = null;
			this.modalita = AdmintoolsConstants.DETAIL_INS;
			response.setAttribute("modality", modalita);
            engine = new Engine();
            engine.setCriptable(new Integer(0));
			engine.setId(new Integer(-1));
			engine.setDescription("");
			engine.setDirUpload("");
			engine.setDirUsable("");
			engine.setSecondaryUrl("");
			engine.setName("");
			engine.setUrl("");
			engine.setDriverName("");
			engine.setEngineTypeId(new Integer(-1));
			engine.setClassName("");
			engine.setBiobjTypeId(new Integer(-1));
			response.setAttribute("engineObj", engine);
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "DetailEngineModule","newDetailEngine","Cannot prepare page for the insertion", ex  );
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	
	private Engine recoverEngineDetails (SourceBean request) throws EMFUserError {
		
		String idStr = (String)request.getAttribute("id");
		Integer id = new Integer(idStr);
		String description = (String)request.getAttribute("description");
		String label = (String)request.getAttribute("label");
		String name = (String)request.getAttribute("name");
		
		String engineTypeIdStr = (String) request.getAttribute("engineTypeId");
		Integer engineTypeId = new Integer(engineTypeIdStr);
		Domain engineType = DAOFactory.getDomainDAO().loadDomainById(engineTypeId);
		
		String url = "";
		String driverName = "";
		String className = "";
		if ("EXT".equalsIgnoreCase(engineType.getValueCd())) {
			// in case of external engine url and driverName are considered
			url = (String) request.getAttribute("url");
			driverName = (String)request.getAttribute("driverName");
		} else {
			// in case of internal engine only className is considered
			className = (String)request.getAttribute("className");
		}
		String secondaryUrl = (String)request.getAttribute("secondaryUrl");
		String dirUpload = (String)request.getAttribute("dirUpload");
		String dirUsable = (String)request.getAttribute("dirUsable");
		String criptableStr = (String)request.getAttribute("criptable");
		Integer criptable = new Integer(criptableStr);
		String biobjTypeIdStr = (String)request.getAttribute("biobjTypeId");
		Integer biobjTypeId = new Integer(biobjTypeIdStr);
		
		Engine engine  = new Engine();
        engine.setCriptable(criptable);
        engine.setLabel(label);
		engine.setId(id);
		engine.setDescription(description);
		engine.setDirUpload(dirUpload);
		engine.setDirUsable(dirUsable);
		engine.setSecondaryUrl(secondaryUrl);
		engine.setName(name);
		engine.setUrl(url);
		engine.setDriverName(driverName);
		engine.setEngineTypeId(engineTypeId);
		engine.setClassName(className);
		engine.setBiobjTypeId(biobjTypeId);
		
        List enginesList = DAOFactory.getEngineDAO().loadAllEngines();
		Iterator i = enginesList.listIterator();
		while (i.hasNext()) {
			Engine en = (Engine) i.next();
			if (en.getLabel().equals(label) && !en.getId().equals(id)) {
				HashMap params = new HashMap();
				params.put(AdmintoolsConstants.PAGE,
						ListEnginesModule.MODULE_PAGE);
				EMFValidationError error = new EMFValidationError(EMFErrorSeverity.ERROR,
						"1011", new Vector(), params);
				getErrorHandler().addError(error);
			}
		}
        
		return engine;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	private Hashtable getAddInfo(){
//		Vector addInfo = new Vector();
//		addInfo.add("Codice");
//		Hashtable information = new Hashtable();
//		information.put("121",addInfo);
//		return information;
//	}
//	
//	private Hashtable engineNameList(){
//		ConfigSingleton configSingleton = ConfigSingleton.getInstance();
//	    List engine = configSingleton.getAttributeAsList("ENGINES.ENGINE");
//	    Iterator engineIT = engine.iterator();
//	    String nameEngine = "";
//	    Hashtable nameHT = new Hashtable();
//	    while(engineIT.hasNext()){
//	    	SourceBean engineSB = (SourceBean)engineIT.next();
//	    	nameEngine = (String)engineSB.getAttribute("name");
//	    	nameHT.put(nameEngine, nameEngine);
//	    }
//	    return nameHT;
//	}
}
