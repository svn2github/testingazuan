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

import groovy.lang.Binding;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dbaccess.DataConnectionManager;
import it.eng.spago.dbaccess.sql.DataConnection;
import it.eng.spago.dbaccess.sql.DataRow;
import it.eng.spago.dbaccess.sql.SQLCommand;
import it.eng.spago.dbaccess.sql.result.DataResult;
import it.eng.spago.dbaccess.sql.result.ScrollableDataResult;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.navigation.LightNavigationManager;
import it.eng.spago.paginator.basic.ListIFace;
import it.eng.spago.paginator.basic.PaginatorIFace;
import it.eng.spago.paginator.basic.impl.GenericList;
import it.eng.spago.paginator.basic.impl.GenericPaginator;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spago.validation.EMFValidationError;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.ModalitiesValue;
import it.eng.spagobi.bo.ObjParuse;
import it.eng.spagobi.bo.Parameter;
import it.eng.spagobi.bo.ScriptDetail;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectParameterDAO;
import it.eng.spagobi.bo.dao.IModalitiesValueDAO;
import it.eng.spagobi.bo.dao.IObjParuseDAO;
import it.eng.spagobi.bo.dao.IParameterDAO;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.security.IUserProfileFactory;
import it.eng.spagobi.services.commons.AbstractBasicCheckListModule;
import it.eng.spagobi.services.commons.DelegatedBasicListService;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Loads the predefined lookup list of values 
 */
public class ChecklistLookupModalityValuesModule extends AbstractBasicCheckListModule {
		
	/**
	 * Class Constructor
	 *
	 */
	public ChecklistLookupModalityValuesModule() {
		super();
	} 
	
	public void createCheckedObjectMap(SourceBean request) throws Exception {
		checkedObjectsMap = new HashMap();
		String biobjParId = (String)request.getAttribute("LOOKUP_OBJ_PAR_ID");
		String biobjParName = (String)request.getAttribute("LOOKUP_PARAMETER_NAME");
		
		
		BIObject obj = (BIObject)getSession(request).getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
		List parameters = obj.getBiObjectParameters();
		BIObjectParameter biparam = null;
		for(int i = 0; i < parameters.size(); i++) {
			BIObjectParameter tmpBIParam = (BIObjectParameter)parameters.get(i);
			if(tmpBIParam.getId().toString().equalsIgnoreCase(biobjParId)) {
				biparam = tmpBIParam;
				break;
			}
		}
		if(biparam == null) return;
		
		List values = biparam.getParameterValues();
		if( values == null ) return;
		for(int i = 0; i < values.size(); i++) {
			String value = (String)values.get(i);
			//System.out.println("--> value: " + value);
			if(value == null || value.trim().equalsIgnoreCase("")) continue;
			String key = GeneralUtilities.encode(value);
			checkedObjectsMap.put(key, key);
		}
	}
	
	/**
	 * Stores into session all the parameters needed to checklist handling
	 */
	private void initSession(SourceBean request) {
		SessionContainer session = this.getRequestContainer().getSessionContainer();
		
		if(session.getAttribute("CHK_LIST_INITIALIZED")==null) {
			session.setAttribute("LOOKUP_PARAMETER_NAME", request.getAttribute("LOOKUP_PARAMETER_NAME"));
			session.setAttribute("LOOKUP_PARAMETER_ID", request.getAttribute("LOOKUP_PARAMETER_ID"));
			session.setAttribute("mod_val_id", request.getAttribute("mod_val_id"));
			session.setAttribute("correlated_paruse_id", request.getAttribute("correlated_paruse_id"));			
			session.setAttribute("CHK_LIST_INITIALIZED", "true");
		}
	}
	
	/**
	 * Removes from session all the parameters used to checklist handling. Leaves in session the 
	 * output parameters neded by the module caller. Clearing these parameters is a caller task!
	 */
	private void clearSession() {
		SessionContainer session = this.getRequestContainer().getSessionContainer();
		session.delAttribute("CHK_LIST_INITIALIZED");
		session.delAttribute("LOOKUP_PARAMETER_ID");
	}
	
	public void exitFromModule(SourceBean response, boolean abort) throws Exception{
		SessionContainer session = this.getRequestContainer().getSessionContainer();		
		
		if(!abort && returnValues){
			List l = getCheckedObjects().getAttributeAsList("OBJECT");
			
			String valueColumn =(String)((SourceBean)config.getAttribute("KEYS.OBJECT")).getAttribute("key");
			
			List results = new ArrayList();
			for(int i = 0; i < l.size(); i++) {
				SourceBean sb = (SourceBean)l.get(i);
				results.add(GeneralUtilities.decode((String)sb.getAttribute(valueColumn)));
			}
			
			session.setAttribute("LOOKUP_VALUE", results);
		}
		
		getRequestContainer().getSessionContainer().delAttribute(CHECKED_OBJECTS);	
		
		String moduleName = (String)_request.getAttribute("AF_MODULE_NAME");
				
		session.setAttribute("RETURN_FROM_MODULE", moduleName);
		session.setAttribute("RETURN_STATUS", ((abort)?"ABORT":"OK") );
		session.setAttribute("LOOKUP_PARAMETER_NAME", (String)getSession(_request).getAttribute("LOOKUP_PARAMETER_NAME"));
		response.setAttribute("PUBLISHER_NAME", "returnToExecBIObjLoop");
		
		clearSession();
	}
	
	public void navigationHandler(SourceBean request, SourceBean response, boolean moveNext) throws Exception{
		super.navigationHandler(request, response, moveNext);
		response.delAttribute("PUBLISHER_NAME");
		response.setAttribute("PUBLISHER_NAME", "ChecklistLookupPublisher");	
	}
	
	/*
	public void printChecked(String phase) throws Exception {
		/////////////////
		System.out.println(phase + ": ...");
		Iterator it = checkedObjectsMap.keySet().iterator();
		while(it.hasNext()) {
			System.out.println(phase + ": " + it.next());
		}
		
		//List l = getCheckedObjects().getAttributeAsList("OBJECT");
		
		//String valueColumn =(String)((SourceBean)config.getAttribute("KEYS.OBJECT")).getAttribute("key");
		
		//for(int i = 0; i < l.size(); i++) {
		//	SourceBean sb = (SourceBean)l.get(i);
		//	System.out.println(phase + ": " + GeneralUtilities.decode((String)sb.getAttribute(valueColumn)));
		//}
		
		////////////////
	}
	*/

	
	public void service(SourceBean request, SourceBean response) throws Exception {		
		initSession(request);
		
		// get the input type from request (query || script)
		String inputType = getModalityValue(request).getITypeCd();
		
		// different input type call different methods to build the list configuration envelope 
		if (inputType.equalsIgnoreCase(SpagoBIConstants.INPUT_TYPE_QUERY_CODE)) {			
			response.setAttribute(getQueryModuleConfig(request));
		} else if (inputType.equalsIgnoreCase(SpagoBIConstants.INPUT_TYPE_FIX_LOV_CODE)) {			
			response.setAttribute(getFixLovModuleConfig(request));
		} else if(inputType.equalsIgnoreCase(SpagoBIConstants.INPUT_TYPE_SCRIPT_CODE)) {			
			response.setAttribute(getScriptModuleConfig(request));
		}
		
		config = getConfig();
		if(config == null) config = (SourceBean) response.getAttribute("CONFIG");
		
		_request = request;
		_response = response;
		
		String message = (String)request.getAttribute("MESSAGE");

		if(message == null || message.equalsIgnoreCase("INIT_CHECKLIST")) {
			preprocess(request);		
			DelegatedBasicListService.service(this, request, response);
			postprocess(response); 
			response.setAttribute("PUBLISHER_NAME", "CheckLinksDefaultPublischer");	
			
			
		}
		else if(message.equalsIgnoreCase("HANDLE_CHECKLIST")) {
									
			// events rised by navigation buttons defined in CheckListTag class (method makeNavigationButton)
			if(request.getAttribute("prevPage") != null){
				navigationHandler(request, response, false);
				return;
			}
			
			if(request.getAttribute("nextPage") != null){
				navigationHandler(request, response, true);
				return;
			}
			
			//	events rised by action buttons defined in module.xml file (module name="ListLookupReportsModule")
			if(request.getAttribute("saveback") != null){
				preprocess(request);
				save();
				exitFromModule(response, false);
				return;
			}
							
			if(request.getAttribute("save") != null) {				
				preprocess(request);
				save();
				request.updAttribute("MESSAGE", "LIST_PAGE");	
				request.setAttribute("LIST_PAGE", new Integer(pageNumber).toString());	
				DelegatedBasicListService.service(this, request, response); 
				postprocess(response); 
				response.setAttribute("PUBLISHER_NAME", "CheckLinksDefaultPublischer");
				return;			
			}
			
			if(request.getAttribute("back") != null) {			
				exitFromModule(response, true);
				return;
			}
			
		}
		else {
			// error
		}			
		
		
		//super.service(request, response); 	
	}
	
	private SessionContainer getSession(SourceBean request) {
		RequestContainer reqCont = getRequestContainer();
		return reqCont.getSessionContainer();		
	}
	
	private SessionContainer getPermanentSession(SourceBean request) {
		return getSession(request).getPermanentContainer();
	}
	
		
	private IEngUserProfile getUserProfile(SourceBean request) {
		 return (IEngUserProfile) getPermanentSession(request).getAttribute(IEngUserProfile.ENG_USER_PROFILE);		
	}
	
	private ModalitiesValue getModalityValue(SourceBean request) throws EMFUserError {
		String modValId = (String)request.getAttribute("mod_val_id");
		if(modValId == null) modValId = (String)getSession(request).getAttribute("mod_val_id");
		Integer idModVal = Integer.valueOf(modValId);
		IModalitiesValueDAO aModalitiesValueDAO = DAOFactory.getModalitiesValueDAO(); 
		return aModalitiesValueDAO.loadModalitiesValueByID(idModVal);
	}
	
	private boolean isCorrelated(SourceBean request) {
		String correlatedParuseIdStr = (String) request.getAttribute("correlated_paruse_id");
		if(correlatedParuseIdStr == null) correlatedParuseIdStr = (String)getSession(request).getAttribute("correlated_paruse_id");
		return (correlatedParuseIdStr != null && !correlatedParuseIdStr.equals(""));
	}

	private HashMap getParams(SourceBean request) {
		HashMap paramsMap = new HashMap();
		String lookupParameterName = (String) request.getAttribute("LOOKUP_PARAMETER_NAME");
		String actor = (String) request.getAttribute(SpagoBIConstants.ACTOR);
		paramsMap.put("LOOKUP_PARAMETER_NAME", lookupParameterName);
		paramsMap.put(SpagoBIConstants.ACTOR, actor);
		paramsMap.put("mod_val_id", request.getAttribute("mod_val_id"));
		return paramsMap;
	}
	
	private HashMap getSelectCaptionParams(SourceBean request) {
		HashMap selectCaptionParams = new HashMap();
		selectCaptionParams.putAll(getParams(request));
		selectCaptionParams.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
		selectCaptionParams.put(SpagoBIConstants.MESSAGEDET, ObjectsTreeConstants.EXEC_PHASE_RETURN_FROM_LOOKUP);
		selectCaptionParams.put("PAGE", "ValidateExecuteBIObjectPage");
		return selectCaptionParams;
	}
	
	private HashMap getBackButtonParams(SourceBean request) {
		HashMap backButtonParams = new HashMap();
		backButtonParams.putAll(getParams(request));
		backButtonParams.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
		backButtonParams.put(SpagoBIConstants.MESSAGEDET, ObjectsTreeConstants.EXEC_PHASE_RETURN_FROM_LOOKUP);
		backButtonParams.put("PAGE", "ValidateExecuteBIObjectPage");
		backButtonParams.put("LOOKUP_VALUE", "");
		return backButtonParams;
	}
	
	
	
	private SourceBean getScriptModuleConfig(SourceBean request) throws Exception {
		SourceBean moduleConfig = null;
		
		IEngUserProfile profile = getUserProfile(request);
		String lov = getModalityValue(request).getLovProvider();
		ScriptDetail scriptDet = ScriptDetail.fromXML(lov);
		String script = scriptDet.getScript();
		Binding bind = GeneralUtilities.fillBinding(profile);
		String result = GeneralUtilities.testScript(script, bind);
		SourceBean rowsSourceBean = null;
		try{
			rowsSourceBean = SourceBean.fromXMLString(result);
		} catch(Exception e) {
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, 
								"ListLookupModalityValuesModule", 
								"getList", "Error during parsing of the script result",e);
		}
		
		SourceBean visibleColumnsSB = (SourceBean) rowsSourceBean.getAttribute("VISIBLE-COLUMNS");
		String visibleColumns = visibleColumnsSB.getCharacters();
		Vector columns = findVisibleColumns(visibleColumns);
		
		SourceBean valueColumnSB = (SourceBean) rowsSourceBean.getAttribute("VALUE-COLUMN");
		String valueColumn = valueColumnSB.getCharacters().trim();
		
		String moduleConfigStr = "";
		
		moduleConfigStr += "<CONFIG rows=\"10\" title=\"" + getModalityValue(request).getDescription() + "\">";
		
		moduleConfigStr += "	<KEYS>";
		moduleConfigStr += "		<OBJECT key='"+ valueColumn +"'/>";
		moduleConfigStr += "	</KEYS>";
		
		moduleConfigStr += "	<QUERIES/>";
		
		moduleConfigStr += "</CONFIG>";
		
		
		moduleConfig = SourceBean.fromXMLString(moduleConfigStr);
		
		SourceBean columnsSB = createColumnsSB(columns);
		moduleConfig.setAttribute(columnsSB);

		SourceBean captionsSB = new SourceBean("CAPTIONS");
		//SourceBean selectCaptionSB = createSelectCaption(getSelectCaptionParams(request), valueColumn);
		//captionsSB.setAttribute(selectCaptionSB);
		moduleConfig.setAttribute(captionsSB);
		
		SourceBean buttonsSB = new SourceBean("BUTTONS");
		SourceBean backButtonSB = createBackButton(getBackButtonParams(request));
		buttonsSB.setAttribute(backButtonSB);
		SourceBean selectButtonSB = createSelectButton(getBackButtonParams(request));
		buttonsSB.setAttribute(selectButtonSB);
		moduleConfig.setAttribute(buttonsSB);

		return moduleConfig;
	
	}
				
	private SourceBean getQueryModuleConfig(SourceBean request) throws Exception {
		SourceBean moduleConfig = null;
		
		String queryDetXML = getModalityValue(request).getLovProvider();
		SourceBean queryXML = SourceBean.fromXMLString(queryDetXML);
		String visibleColumns = ((SourceBean) queryXML.getAttribute("VISIBLE-COLUMNS")).getCharacters();
		String valueColumn = ((SourceBean) queryXML.getAttribute("VALUE-COLUMN")).getCharacters();
		String pool = ((SourceBean) queryXML.getAttribute("CONNECTION")).getCharacters();
		String statement = ((SourceBean) queryXML.getAttribute("STMT")).getCharacters();
		IEngUserProfile profile = getUserProfile(request);
		statement = GeneralUtilities.substituteProfileAttributesInString(statement, profile);
		
		Vector columns = findVisibleColumns(visibleColumns);
		
		String moduleConfigStr = "";
		moduleConfigStr += "<CONFIG pool=\"" + pool + "\" rows=\"10\" title=\"" + getModalityValue(request).getDescription() + "\">";
		moduleConfigStr += "	<KEYS>";
		moduleConfigStr += "		<OBJECT key='"+ valueColumn +"'/>";
		moduleConfigStr += "	</KEYS>";
		
		moduleConfigStr += "	<QUERIES>";
		moduleConfigStr += "		<SELECT_QUERY statement=\"" + statement + "\" />";
		moduleConfigStr += "	</QUERIES>";
		moduleConfigStr += "</CONFIG>";
		moduleConfig = SourceBean.fromXMLString(moduleConfigStr);
		
		SourceBean columnsSB = createColumnsSB(columns);
		moduleConfig.setAttribute(columnsSB);

		SourceBean captionsSB = new SourceBean("CAPTIONS");
		//SourceBean selectCaptionSB = createSelectCaption(getSelectCaptionParams(request), valueColumn);
		//captionsSB.setAttribute(selectCaptionSB);
		moduleConfig.setAttribute(captionsSB);
		
		SourceBean buttonsSB = new SourceBean("BUTTONS");
		SourceBean backButtonSB = createBackButton(getBackButtonParams(request));
		buttonsSB.setAttribute(backButtonSB);
		SourceBean selectButtonSB = createSelectButton(getBackButtonParams(request));
		buttonsSB.setAttribute(selectButtonSB);
		moduleConfig.setAttribute(buttonsSB);
		
		return moduleConfig;
	}
	
	
	
	private SourceBean getFixLovModuleConfig(SourceBean request) throws Exception {
		SourceBean moduleConfig = null;
		
		String lovDetXML = getModalityValue(request).getLovProvider();
		IEngUserProfile profile = getUserProfile(request);
		lovDetXML = GeneralUtilities.substituteProfileAttributesInString(lovDetXML,profile);
		SourceBean lovXML = SourceBean.fromXMLString(lovDetXML);
		
		
		String visibleColumns = "DESC";
		Vector columns = findVisibleColumns(visibleColumns);
		
		//String valueColumn = (String)lovXML.getAttribute("VALUE");
		//String valueColumn = (String)((SourceBean)lovXML.getAttribute("LOV-ELEMENT")).getAttribute("VALUE");				
		String valueColumn = "VALUE";
		
		String moduleConfigStr = "";
		moduleConfigStr += "<CONFIG rows=\"10\" title=\"" + getModalityValue(request).getDescription() + "\">";
		moduleConfigStr += "	<KEYS>";
		moduleConfigStr += "		<OBJECT key='"+ valueColumn +"'/>";
		moduleConfigStr += "	</KEYS>";
		moduleConfigStr += "	<QUERIES/>";
		moduleConfigStr += "</CONFIG>";
		moduleConfig = SourceBean.fromXMLString(moduleConfigStr);
		
		SourceBean columnsSB = createColumnsSB(columns);
		moduleConfig.setAttribute(columnsSB);

		SourceBean captionsSB = new SourceBean("CAPTIONS");
		//SourceBean selectCaptionSB = createSelectCaption(getSelectCaptionParams(request), valueColumn);
		//captionsSB.setAttribute(selectCaptionSB);
		moduleConfig.setAttribute(captionsSB);
		
		SourceBean buttonsSB = new SourceBean("BUTTONS");
		SourceBean backButtonSB = createBackButton(getBackButtonParams(request));
		buttonsSB.setAttribute(backButtonSB);
		SourceBean selectButtonSB = createSelectButton(getBackButtonParams(request));
		buttonsSB.setAttribute(selectButtonSB);
		moduleConfig.setAttribute(buttonsSB);
		
		return moduleConfig;
	}
	
	
	
	private ListIFace filterListForCorrelatedParam(SourceBean request, ListIFace list) throws Exception {
		String objParIdStr = (String) request.getAttribute("LOOKUP_PARAMETER_ID");
		if(objParIdStr == null) objParIdStr = (String)getSession(request).getAttribute("LOOKUP_PARAMETER_ID");
				
		Integer objParId = Integer.valueOf(objParIdStr);
		Integer correlatedParuseId = Integer.valueOf((String) request.getAttribute("correlated_paruse_id"));
		IObjParuseDAO objParuseDAO = DAOFactory.getObjParuseDAO();
		ObjParuse objParuse = objParuseDAO.loadObjParuse(objParId, correlatedParuseId);
		Integer objParFatherId = objParuse.getObjParFatherId();
		// get object from the session
		BIObject obj = (BIObject) getSession(request).getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
        
		List biparams = obj.getBiObjectParameters();
		Iterator iterParams = null;
		
		// find the parameter
		BIObjectParameter lookupBIParameter = null;
		iterParams = biparams.iterator();
    	while (iterParams.hasNext()) {
    		BIObjectParameter aBIParameter = (BIObjectParameter) iterParams.next();
    		if (aBIParameter.getId().equals(objParId)) {
    			lookupBIParameter = aBIParameter;
    			break;
    		}
    	}
		
		// find the parameter for the correlation
		BIObjectParameter objParFather = null;
        iterParams = biparams.iterator();
        while (iterParams.hasNext()) {
        	BIObjectParameter aBIObjectParameter = (BIObjectParameter) iterParams.next();
        	if (aBIObjectParameter.getId().equals(objParFatherId)) {
        		objParFather = aBIObjectParameter;
        		break;
        	}
        }
        IParameterDAO parameterDAO = DAOFactory.getParameterDAO();
        Parameter parameter = parameterDAO.loadForDetailByParameterID(objParFather.getParID());
        String valueTypeFilter = parameter.getType();
        
		String valueFilter = "";
		List valuesFilter = objParFather.getParameterValues();
		if (valuesFilter == null) return list;

		switch (valuesFilter.size()) {
			case 0: return list;
			case 1: valueFilter = (String) valuesFilter.get(0);
					if (valueFilter != null && !valueFilter.equals("")) {
						ListIFace filteredList = DelegatedBasicListService.filterList(list, valueFilter, valueTypeFilter, 
							objParuse.getFilterColumn(), objParuse.getFilterOperation(), 
							getResponseContainer().getErrorHandler());
						deselectFilteredValues(lookupBIParameter, filteredList);
						return filteredList;
					}
					else return list;
			default: 
				ListIFace filteredList = DelegatedBasicListService.filterList(list, valuesFilter, valueTypeFilter, 
							objParuse.getFilterColumn(), objParuse.getFilterOperation(), 
							getResponseContainer().getErrorHandler());
				deselectFilteredValues(lookupBIParameter, filteredList);
				return filteredList;
		}
	}
	
	
	
	
	/**
	 * @param list
	 * @param filteredList
	 */
	private void deselectFilteredValues(BIObjectParameter biParameter, ListIFace filteredList) {
		
		String valueColumn = (String)((SourceBean) config.getAttribute("KEYS.OBJECT")).getAttribute("key");
		
		HashMap map = new HashMap();
		
		SourceBean allrowsSB = filteredList.getPaginator().getAll();
		List rows = allrowsSB.getAttributeAsList("ROW");
		Iterator iterRow = rows.iterator();
		while (iterRow.hasNext()) {
			SourceBean row = (SourceBean) iterRow.next();
			Object attribute = row.getAttribute(valueColumn);		
			map.put(attribute, attribute);
			//System.out.println(" - " + attribute);
		}
		
		List values = biParameter.getParameterValues();		
		for(int i = 0; i < values.size(); i++) {
			String value = (String)values.get(i);
			//System.out.println(" -  [" + value + "]");
			//System.out.println(" -> [" + GeneralUtilities.encode(value) + "]");
			if(map.get(value) == null) {
				//System.out.println(" *** ");
				checkedObjectsMap.remove(GeneralUtilities.encode(value));
			}
		}
	}

	/**
	 * Finds the names of the visible columns with the StringTokenizer from the String at input.
	 * 
	 * @param visibleColumns The String at input to be tokenized
	 * @return A Vector containing the names of the visible columns
	 */
	private Vector findVisibleColumns(String visibleColumns) {
		if (visibleColumns == null || visibleColumns.trim().equals("")) return new Vector ();
		StringTokenizer strToken = new StringTokenizer(visibleColumns, ",");
		Vector columns = new Vector();
		while (strToken.hasMoreTokens()) {
			String val = strToken.nextToken().trim();
			columns.add(val);
		}
		return columns;
	}


	/**
	 * Creates a BACK_BUTTON SourceBean with the parameters passed at input
	 * 
	 * @param backButtonParams The HashMap containing the parameters names and values
	 * @return The BACK_BUTTON SourceBean
	 * @throws SourceBeanException
	 */
	private SourceBean createBackButton(HashMap backButtonParams) throws SourceBeanException {
		
		
		if (backButtonParams == null || backButtonParams.size() == 0) return new SourceBean("BACK_BUTTON");
		String backButtonStr = "<BACK_BUTTON confirm=\"FALSE\" name=\"back\" image=\"/img/back.png\" " +
				"label=\"SBIListLookPage.backButton\"/>";
		
		SourceBean toReturn = SourceBean.fromXMLString(backButtonStr);
		return toReturn;
	}
	
	private SourceBean createSelectButton(HashMap backButtonParams) throws SourceBeanException {
			
		if (backButtonParams == null || backButtonParams.size() == 0) return new SourceBean("BACK_BUTTON");
		String backButtonStr = "<BACK_BUTTON confirm=\"FALSE\" name=\"saveback\" image=\"/img/button_ok.gif\" " +
				"label=\"SBIListLookPage.backButton\"/>";
		
		SourceBean toReturn = SourceBean.fromXMLString(backButtonStr);
		return toReturn;
	}

	/**
	 * Creates a SELECT_CAPTION SourceBean with the parameters passed at input
	 * 
	 * @param selectCaptionParams The HashMap containing the parameters names and values
	 * @param valueColumn The relative variable to be set in the caption URL representing the lookup value
	 * @return The SELECT_CAPTION SourceBean
	 * @throws SourceBeanException
	 */
	private SourceBean createSelectCaption(HashMap selectCaptionParams, String valueColumn) throws SourceBeanException {
		if (selectCaptionParams == null || selectCaptionParams.size() == 0) return new SourceBean("SELECT_CAPTION");
		String selectCaptionStr = "<SELECT_CAPTION confirm=\"FALSE\" image=\"/img/button_ok.gif\" " +
				" label=\"SBIListLookPage.selectButton\">";
		Set keys = selectCaptionParams.keySet();
		Iterator it = keys.iterator();
		while (it.hasNext()) {
			String key = it.next().toString();
			String value = selectCaptionParams.get(key).toString();
			selectCaptionStr += "	<PARAMETER name=\"" + key + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + value + "\" />";
		}
		selectCaptionStr += "	<PARAMETER name=\"LOOKUP_VALUE\" scope=\"LOCAL\" type=\"RELATIVE\" value=\"" + valueColumn + "\" />";
		selectCaptionStr += "</SELECT_CAPTION>";
		SourceBean toReturn = SourceBean.fromXMLString(selectCaptionStr);
		return toReturn;
	}
	
	/**
	 * Creates a COLUMNS SourceBean with all the columns in the Vector passed at input
	 * 
	 * @param columns The Vector containing the columns to be visualized
	 * @return The COLUMNS SourceBean 
	 * @throws SourceBeanException
	 */
	private SourceBean createColumnsSB(Vector columns) throws SourceBeanException {
		if (columns == null || columns.size() == 0) return new SourceBean("COLUMNS");
		String columnsStr = "<COLUMNS>";
		for (int i = 0; i < columns.size(); i++) {
			columnsStr += "	<COLUMN name=\"" + columns.get(i).toString() + "\" />";
		}
		columnsStr += "</COLUMNS>";
		SourceBean columnsSB = SourceBean.fromXMLString(columnsStr);
		return columnsSB;
	}
	
	
	
	
	
	/**
	 * Gets the list
	 * 
	 * @param request The request SourceBean
	 * @param response The response SourceBean
	 * @return ListIFace 
	 */
	public ListIFace getList(SourceBean request, SourceBean response) throws Exception {		
		ListIFace list = null;		
		
		HashMap paramsMap = null;	
		
		// get the input type from request (query || script)
		String inputType = getModalityValue(request).getITypeCd();
		
		// different input type call different delegated class to build the list 
		if (inputType.equalsIgnoreCase(SpagoBIConstants.INPUT_TYPE_QUERY_CODE)) {			
			list = getListFromQuery(request, response);
			paramsMap = getParams(request);
			String correlatedParuseIdStr = (String) request.getAttribute("correlated_paruse_id");
			if (correlatedParuseIdStr != null && !correlatedParuseIdStr.equals("")) {
				paramsMap.put("correlated_paruse_id", request.getAttribute("correlated_paruse_id"));
				paramsMap.put("LOOKUP_PARAMETER_ID", request.getAttribute("LOOKUP_PARAMETER_ID"));	
			}		
		} 
		else if(inputType.equalsIgnoreCase(SpagoBIConstants.INPUT_TYPE_FIX_LOV_CODE)) {			
			list = getListFromLov(request, response);
			paramsMap = getParams(request);
		}
		else if(inputType.equalsIgnoreCase(SpagoBIConstants.INPUT_TYPE_SCRIPT_CODE)) {			
			list = getListFromScript(request, response);
			paramsMap = getParams(request);
		}
		
		response.setAttribute("PARAMETERS_MAP", paramsMap);		
		response.setAttribute(SpagoBIConstants.PUBLISHER_NAME , "LookupPublisher");
		
		return list;		
	}
	
	private ListIFace getListFromLov(SourceBean request, SourceBean response) throws Exception {
		ListIFace list = null;
		
		String lovDetXML = getModalityValue(request).getLovProvider();
		IEngUserProfile profile = getUserProfile(request);
		lovDetXML = GeneralUtilities.substituteProfileAttributesInString(lovDetXML,profile);
		SourceBean lovXML = SourceBean.fromXMLString(lovDetXML);
		
		PaginatorIFace paginator = new GenericPaginator();
		List rowsVector = new ArrayList();
		if (lovXML != null) {
			List lovElement = lovXML.getAttributeAsList("LOV-ELEMENT");
			for(int j = 0; j < lovElement.size(); j++) {			
				SourceBean row = new SourceBean("ROW");
				SourceBean tsb = (SourceBean)lovElement.get(j);
				String descVal = (String)tsb.getAttribute("DESC");
				String valueVal = (String)tsb.getAttribute("VALUE");
				row.setAttribute("DESC", descVal);
				row.setAttribute("VALUE", valueVal);
				rowsVector.add(row);
			}
			
			
		}
			

		if (lovXML != null) {
			for (int i = 0; i < rowsVector.size(); i++)
				paginator.addRow(rowsVector.get(i));
		}
		list = new GenericList();
		list.setPaginator(paginator);
		
		// filter the list 
		String valuefilter = (String) request.getAttribute(SpagoBIConstants.VALUE_FILTER);
		if (valuefilter != null) {
			String columnfilter = (String) request
					.getAttribute(SpagoBIConstants.COLUMN_FILTER);
			String typeFilter = (String) request
					.getAttribute(SpagoBIConstants.TYPE_FILTER);
			String typeValueFilter = (String) request
					.getAttribute(SpagoBIConstants.TYPE_VALUE_FILTER);
			list = DelegatedBasicListService.filterList(list, valuefilter, typeValueFilter, 
					columnfilter, typeFilter, getResponseContainer().getErrorHandler());
		}		
		
		return list;
	}
	
	private ListIFace getListFromQuery (SourceBean request, SourceBean response) throws Exception {
		ListIFace list = null;
		
		//response.setAttribute(getQueryModuleConfig(request));		
		list = DelegatedBasicListService.getList(this, request, response);
		// if the parameter is correlated filter out the list properly
		if (isCorrelated(request)) {
			list = filterListForCorrelatedParam(request, list);				
		}  
		
		return list;
	}
	
	private ListIFace getListFromScript(SourceBean request, SourceBean response) throws Exception {
		ListIFace list = null;
		
		IEngUserProfile profile = getUserProfile(request);
		String lov = getModalityValue(request).getLovProvider();
		ScriptDetail scriptDet = ScriptDetail.fromXML(lov);
		String script = scriptDet.getScript();
		Binding bind = GeneralUtilities.fillBinding(profile);
		String result = GeneralUtilities.testScript(script, bind);
		SourceBean rowsSourceBean = null;
		try{
			rowsSourceBean = SourceBean.fromXMLString(result);
		} catch(Exception e) {
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, 
								"ListLookupModalityValuesModule", 
								"getList", "Error during parsing of the script result",e);
		}
		
    	PaginatorIFace paginator = new GenericPaginator();
		List rowsVector = null;
		if (rowsSourceBean != null)
			rowsVector = rowsSourceBean.getAttributeAsList(DataRow.ROW_TAG);

		if (rowsSourceBean != null) {
			for (int i = 0; i < rowsVector.size(); i++)
				paginator.addRow(rowsVector.get(i));
		}
		list = new GenericList();
		list.setPaginator(paginator);
		
		// filter the list 
		String valuefilter = (String) request.getAttribute(SpagoBIConstants.VALUE_FILTER);
		if (valuefilter != null) {
			String columnfilter = (String) request
					.getAttribute(SpagoBIConstants.COLUMN_FILTER);
			String typeFilter = (String) request
					.getAttribute(SpagoBIConstants.TYPE_FILTER);
			String typeValueFilter = (String) request
					.getAttribute(SpagoBIConstants.TYPE_VALUE_FILTER);
			list = DelegatedBasicListService.filterList(list, valuefilter, typeValueFilter, 
					columnfilter, typeFilter, getResponseContainer().getErrorHandler());
		}		
		
		return list;
	}
	
} 

