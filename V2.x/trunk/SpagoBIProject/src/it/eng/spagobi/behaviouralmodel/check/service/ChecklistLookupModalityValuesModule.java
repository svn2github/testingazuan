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
package it.eng.spagobi.behaviouralmodel.check.service;

import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.navigation.LightNavigationManager;
import it.eng.spago.paginator.basic.ListIFace;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.Parameter;
import it.eng.spagobi.behaviouralmodel.lov.bo.ILovDetail;
import it.eng.spagobi.behaviouralmodel.lov.bo.LovDetailFactory;
import it.eng.spagobi.behaviouralmodel.lov.bo.LovResultHandler;
import it.eng.spagobi.behaviouralmodel.lov.bo.LovToListService;
import it.eng.spagobi.behaviouralmodel.lov.bo.ModalitiesValue;
import it.eng.spagobi.behaviouralmodel.lov.dao.IModalitiesValueDAO;
import it.eng.spagobi.commons.constants.ObjectsTreeConstants;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.services.AbstractBasicCheckListModule;
import it.eng.spagobi.commons.services.DelegatedBasicListService;
import it.eng.spagobi.commons.utilities.GeneralUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * Loads the predefined lookup list of values
 */
public class ChecklistLookupModalityValuesModule extends AbstractBasicCheckListModule {

    private static transient Logger logger = Logger.getLogger(ChecklistLookupModalityValuesModule.class);
    private String lovResult = null;
    private ILovDetail lovProvDetail = null;

    /**
     * Class Constructor.
     */
    public ChecklistLookupModalityValuesModule() {
	super();
    }

    /* (non-Javadoc)
     * @see it.eng.spagobi.commons.services.AbstractBasicCheckListModule#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
     */
    public void service(SourceBean request, SourceBean response) throws Exception {
	logger.debug("IN");
	initSession(request);
	BIObjectParameter biParam = getBIParameter(request);
	Parameter par = biParam.getParameter();
	ModalitiesValue lov = par.getModalityValue();
	String lovProv = lov.getLovProvider();
	lovProvDetail = LovDetailFactory.getLovFromXML(lovProv);
	lovResult = biParam.getLovResult();
	if (lovResult == null) {
	    IEngUserProfile profile = getUserProfile(request);
	    lovResult = lovProvDetail.getLovResult(profile);
	}
	response.setAttribute(getLovModuleConfig(lovResult, request));
	config = getConfig();
	if (config == null)
	    config = (SourceBean) response.getAttribute("CONFIG");
	_request = request;
	_response = response;
	String message = (String) request.getAttribute("MESSAGE");
	if (message == null || message.equalsIgnoreCase("INIT_CHECKLIST")) {
	    preprocess(request);
	    DelegatedBasicListService.service(this, request, response);
	    postprocess(response);
	    response.setAttribute("PUBLISHER_NAME", "CheckLinksDefaultPublischer");
	} else if (message.equalsIgnoreCase("HANDLE_CHECKLIST")) {
	    // events rised by navigation buttons defined in CheckListTag class
	    // (method makeNavigationButton)
	    if (request.getAttribute("prevPage") != null) {
		navigationHandler(request, response, false);
		logger.debug("OUT");
		return;
	    }
	    if (request.getAttribute("firstPage") != null) {
			navigationHandler(request, response, 1);
			logger.debug("OUT");
			return;
		}
	    if (request.getAttribute("nextPage") != null) {
		navigationHandler(request, response, true);
		logger.debug("OUT");
		return;
	    }
	    if (request.getAttribute("lastPage") != null) {
	    	int lastPage = Integer.valueOf(request.getAttribute("LIST_PAGE").toString()).intValue();
			navigationHandler(request, response, lastPage);
			logger.debug("OUT");
			return;
		}
	    // events rised by action buttons defined in module.xml file (module
	    // name="ListLookupReportsModule")
	    if (request.getAttribute("saveback") != null) {
		preprocess(request);
		save();
		exitFromModule(response, false);
		logger.debug("OUT");
		return;
	    }
	    if (request.getAttribute("save") != null) {
		preprocess(request);
		save();
		request.updAttribute("MESSAGE", "LIST_PAGE");
		request.updAttribute("LIST_PAGE", new Integer(pageNumber).toString());
		DelegatedBasicListService.service(this, request, response);
		postprocess(response);
		response.setAttribute("PUBLISHER_NAME", "CheckLinksDefaultPublischer");
		logger.debug("OUT");
		return;
	    }
	    if (request.getAttribute("back") != null) {
		exitFromModule(response, true);
		logger.debug("OUT");
		return;
	    }
	    
	    // default
    	int destPage = Integer.valueOf(request.getAttribute("LIST_PAGE").toString()).intValue();
		navigationHandler(request, response, destPage);
		logger.debug("OUT");
		return;
	    
	} else {
	    logger.error("The message parameter value " + message + "is not allowed");
	}
	logger.debug("OUT");
    }

    /* (non-Javadoc)
     * @see it.eng.spagobi.commons.services.AbstractBasicCheckListModule#createCheckedObjectMap(it.eng.spago.base.SourceBean)
     */
    public void createCheckedObjectMap(SourceBean request) throws Exception {
	logger.debug("IN");
	checkedObjectsMap = new HashMap();
	String biobjParId = (String) request.getAttribute("LOOKUP_OBJ_PAR_ID");
	String biobjParName = (String) request.getAttribute("LOOKUP_PARAMETER_NAME");

	BIObject obj = (BIObject) getSession(request).getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
	List parameters = obj.getBiObjectParameters();
	BIObjectParameter biparam = null;
	for (int i = 0; i < parameters.size(); i++) {
	    BIObjectParameter tmpBIParam = (BIObjectParameter) parameters.get(i);
	    if (tmpBIParam.getId().toString().equalsIgnoreCase(biobjParId)) {
		biparam = tmpBIParam;
		break;
	    }
	}
	if (biparam == null)
	    return;

	List values = biparam.getParameterValues();
	if (values == null)
	    return;
	for (int i = 0; i < values.size(); i++) {
	    String value = (String) values.get(i);
	    // System.out.println("--> value: " + value);
	    if (value == null || value.trim().equalsIgnoreCase(""))
		continue;
	    String key = GeneralUtilities.encode(value);
	    checkedObjectsMap.put(key, key);
	}
	logger.debug("OUT");
    }

    /* (non-Javadoc)
     * @see it.eng.spagobi.commons.services.AbstractBasicCheckListModule#updateCheckedObjectMap(it.eng.spago.base.SourceBean)
     */
    public void updateCheckedObjectMap(SourceBean request) throws Exception {
	logger.debug("IN");
	checkedObjectsMap = new HashMap();

	SourceBean checked = (SourceBean) getRequestContainer().getSessionContainer().getAttribute(CHECKED_OBJECTS);
	List objectsList = checked.getAttributeAsList(OBJECT);
	for (int i = 0; i < objectsList.size(); i++) {
	    SourceBean object = (SourceBean) objectsList.get(i);
	    String key = getObjectKey(object);
	    checkedObjectsMap.put(key, key);
	}

	List checkedEntityKeys = getCheckedObjectKeys(request);
	for (int i = 0; i < checkedEntityKeys.size(); i++) {
	    String key = (String) checkedEntityKeys.get(i);
	    checkedObjectsMap.put(key, key);
	}
	logger.debug("OUT");
    }

    /**
     * Stores into session all the parameters needed to checklist handling
     */
    private void initSession(SourceBean request) {
	logger.debug("IN");
	SessionContainer session = this.getRequestContainer().getSessionContainer();
	if (session.getAttribute("CHK_LIST_INITIALIZED") == null) {
	    session.setAttribute("LOOKUP_PARAMETER_NAME", request.getAttribute("LOOKUP_PARAMETER_NAME"));
	    session.setAttribute("LOOKUP_PARAMETER_ID", request.getAttribute("LOOKUP_PARAMETER_ID"));
	    session.setAttribute("mod_val_id", request.getAttribute("mod_val_id"));
	    session.setAttribute("correlated_paruse_id", request.getAttribute("correlated_paruse_id"));
	    session.setAttribute("CHK_LIST_INITIALIZED", "true");
	}
	logger.debug("OUT");
    }

    /**
     * Removes from session all the parameters used to checklist handling.
     * Leaves in session the output parameters neded by the module caller.
     * Clearing these parameters is a caller task!
     */
    private void clearSession() {
	logger.debug("IN");
	SessionContainer session = this.getRequestContainer().getSessionContainer();
	session.delAttribute("CHK_LIST_INITIALIZED");
	session.delAttribute("LOOKUP_PARAMETER_ID");
	logger.debug("OUT");
    }

    /* (non-Javadoc)
     * @see it.eng.spagobi.commons.services.AbstractBasicCheckListModule#exitFromModule(it.eng.spago.base.SourceBean, boolean)
     */
    public void exitFromModule(SourceBean response, boolean abort) throws Exception {
	logger.debug("IN");
	SessionContainer session = this.getRequestContainer().getSessionContainer();
	if (!abort && returnValues) {
	    List valuesSBList = getCheckedObjects().getAttributeAsList("OBJECT");
	    String valueColumn = (String) ((SourceBean) config.getAttribute("KEYS.OBJECT")).getAttribute("key");
	    List values = new ArrayList();
	    List descriptions = new ArrayList();
	    for (int i = 0; i < valuesSBList.size(); i++) {
		SourceBean valueSB = (SourceBean) valuesSBList.get(i);
		LovResultHandler lovResultHandler = new LovResultHandler(lovResult);
		String valueToSearch = (String) valueSB.getAttribute(valueColumn);
		SourceBean rowSB = lovResultHandler.getRow(valueToSearch, lovProvDetail.getValueColumnName());
		if (rowSB == null)
		    rowSB = lovResultHandler.getRow(GeneralUtilities.decode(valueToSearch), lovProvDetail
			    .getValueColumnName());
		if (rowSB != null) {
		    values.add(GeneralUtilities.decode((String) valueSB.getAttribute(valueColumn)));
		    descriptions.add(rowSB.getAttribute(lovProvDetail.getDescriptionColumnName()));
		}
	    }
	    session.setAttribute("LOOKUP_VALUE", values);
	    session.setAttribute("LOOKUP_DESC", descriptions);
	}
	getRequestContainer().getSessionContainer().delAttribute(CHECKED_OBJECTS);
	String moduleName = (String) _request.getAttribute("AF_MODULE_NAME");
	session.setAttribute("RETURN_FROM_MODULE", moduleName);
	session.setAttribute("RETURN_STATUS", ((abort) ? "ABORT" : "OK"));
	session.setAttribute("LOOKUP_PARAMETER_NAME", (String) getSession(_request).getAttribute(
		"LOOKUP_PARAMETER_NAME"));
	response.setAttribute("PUBLISHER_NAME", "returnToExecBIObjLoop");
	clearSession();
	logger.debug("OUT");
    }

    /* (non-Javadoc)
     * @see it.eng.spagobi.commons.services.AbstractBasicCheckListModule#navigationHandler(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean, boolean)
     */
    public void navigationHandler(SourceBean request, SourceBean response, boolean moveNext) throws Exception {
	super.navigationHandler(request, response, moveNext);
	response.delAttribute("PUBLISHER_NAME");
	response.setAttribute("PUBLISHER_NAME", "ChecklistLookupPublisher");
    }

    
    /* (non-Javadoc)
     * @see it.eng.spagobi.commons.services.AbstractBasicCheckListModule#navigationHandler(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean, int)
     */
    public void navigationHandler(SourceBean request, SourceBean response, int destPage) throws Exception {
	super.navigationHandler(request, response, destPage);
	response.delAttribute("PUBLISHER_NAME");
	response.setAttribute("PUBLISHER_NAME", "ChecklistLookupPublisher");
    }
    
    private BIObjectParameter getBIParameter(SourceBean request) {
	logger.debug("IN");
	BIObject obj = (BIObject) getSession(request).getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
	String parameterUrlName = (String) getSession(request).getAttribute("LOOKUP_PARAMETER_NAME");
	List biparams = obj.getBiObjectParameters();
	Iterator iterParams = biparams.iterator();
	while (iterParams.hasNext()) {
	    BIObjectParameter biParam = (BIObjectParameter) iterParams.next();
	    if (biParam.getParameterUrlName().equalsIgnoreCase(parameterUrlName))
		return biParam;
	}
	logger.debug("OUT");
	return null;
    }

    private SourceBean getLovModuleConfig(String lovResult, SourceBean request) throws Exception {
	logger.debug("IN");
	IEngUserProfile profile = getUserProfile(request);
	LovToListService lovToListService = new LovToListService(lovProvDetail, profile);
	SourceBean config = lovToListService.getListServiceBaseConfig(getModalityValue(request).getDescription());
	String valsueColumn = lovProvDetail.getValueColumnName();
	SourceBean captionsSB = new SourceBean("CAPTIONS");
	config.setAttribute(captionsSB);
	SourceBean buttonsSB = new SourceBean("BUTTONS");
	SourceBean backButtonSB = createBackButton(getBackButtonParams(request));
	buttonsSB.setAttribute(backButtonSB);
	SourceBean selectButtonSB = createSelectButton(getBackButtonParams(request));
	buttonsSB.setAttribute(selectButtonSB);
	config.setAttribute(buttonsSB);
	logger.debug("OUT");
	return config;
    }

    private ListIFace getListFromLov(SourceBean request, SourceBean response) throws Exception {
	logger.debug("IN");
	ListIFace list = null;
	IEngUserProfile profile = getUserProfile(request);
	LovToListService lovToListService = new LovToListService(lovProvDetail, profile);
	list = lovToListService.getLovAsListService();
	// filter the list
	String valuefilter = (String) request.getAttribute(SpagoBIConstants.VALUE_FILTER);
	if (valuefilter != null) {
	    String columnfilter = (String) request.getAttribute(SpagoBIConstants.COLUMN_FILTER);
	    String typeFilter = (String) request.getAttribute(SpagoBIConstants.TYPE_FILTER);
	    String typeValueFilter = (String) request.getAttribute(SpagoBIConstants.TYPE_VALUE_FILTER);
	    list = DelegatedBasicListService.filterList(list, valuefilter, typeValueFilter, columnfilter, typeFilter,
		    getResponseContainer().getErrorHandler());
	}
	logger.debug("OUT");
	return list;
    }

    private SessionContainer getPermanentSession(SourceBean request) {
	return getSession(request).getPermanentContainer();
    }

    private IEngUserProfile getUserProfile(SourceBean request) {
	return (IEngUserProfile) getPermanentSession(request).getAttribute(IEngUserProfile.ENG_USER_PROFILE);
    }

    private ModalitiesValue getModalityValue(SourceBean request) throws EMFUserError {
	logger.debug("IN");
	String modValId = (String) request.getAttribute("mod_val_id");
	if (modValId == null)
	    modValId = (String) getSession(request).getAttribute("mod_val_id");
	Integer idModVal = Integer.valueOf(modValId);
	IModalitiesValueDAO aModalitiesValueDAO = DAOFactory.getModalitiesValueDAO();
	logger.debug("OUT");
	return aModalitiesValueDAO.loadModalitiesValueByID(idModVal);
    }

    private boolean isCorrelated(SourceBean request) {
	String correlatedParuseIdStr = (String) request.getAttribute("correlated_paruse_id");
	if (correlatedParuseIdStr == null)
	    correlatedParuseIdStr = (String) getSession(request).getAttribute("correlated_paruse_id");
	return (correlatedParuseIdStr != null && !correlatedParuseIdStr.equals(""));
    }

    private HashMap getParams(SourceBean request) {
	logger.debug("IN");
	HashMap paramsMap = new HashMap();
	String lookupParameterName = (String) request.getAttribute("LOOKUP_PARAMETER_NAME");
	// String actor = (String) request.getAttribute(SpagoBIConstants.ACTOR);
	paramsMap.put("LOOKUP_PARAMETER_NAME", lookupParameterName);
	// paramsMap.put(SpagoBIConstants.ACTOR, actor);
	paramsMap.put("mod_val_id", request.getAttribute("mod_val_id"));
	logger.debug("OUT");
	return paramsMap;
    }

    private HashMap getSelectCaptionParams(SourceBean request) {
	logger.debug("IN");
	HashMap selectCaptionParams = new HashMap();
	selectCaptionParams.putAll(getParams(request));
	selectCaptionParams.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
	selectCaptionParams.put(SpagoBIConstants.MESSAGEDET, ObjectsTreeConstants.EXEC_PHASE_RETURN_FROM_LOOKUP);
	selectCaptionParams.put("PAGE", "ValidateExecuteBIObjectPage");
	logger.debug("OUT");
	return selectCaptionParams;
    }

    private HashMap getBackButtonParams(SourceBean request) {
	logger.debug("IN");
	HashMap backButtonParams = new HashMap();
	backButtonParams.putAll(getParams(request));
	backButtonParams.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
	backButtonParams.put(SpagoBIConstants.MESSAGEDET, ObjectsTreeConstants.EXEC_PHASE_RETURN_FROM_LOOKUP);
	backButtonParams.put("PAGE", "ValidateExecuteBIObjectPage");
	backButtonParams.put("LOOKUP_VALUE", "");
	logger.debug("OUT");
	return backButtonParams;
    }

    /**
     * @param list
     * @param filteredList
     */
    private void deselectFilteredValues(BIObjectParameter biParameter, ListIFace filteredList) {
	logger.debug("IN");
	String valueColumn = (String) ((SourceBean) config.getAttribute("KEYS.OBJECT")).getAttribute("key");

	HashMap map = new HashMap();

	SourceBean allrowsSB = filteredList.getPaginator().getAll();
	List rows = allrowsSB.getAttributeAsList("ROW");
	Iterator iterRow = rows.iterator();
	while (iterRow.hasNext()) {
	    SourceBean row = (SourceBean) iterRow.next();
	    Object attribute = row.getAttribute(valueColumn);
	    map.put(attribute, attribute);
	    // System.out.println(" - " + attribute);
	}

	List values = biParameter.getParameterValues();
	if (values != null) {
	    for (int i = 0; i < values.size(); i++) {
		String value = (String) values.get(i);
		// System.out.println(" - [" + value + "]");
		// System.out.println(" -> [" + GeneralUtilities.encode(value) +
		// "]");
		if (map.get(value) == null) {
		    // System.out.println(" *** ");
		    checkedObjectsMap.remove(GeneralUtilities.encode(value));
		}
	    }
	}
	logger.debug("OUT");
    }

    /**
     * Finds the names of the visible columns with the StringTokenizer from the
     * String at input.
     * 
     * @param visibleColumns
     *                The String at input to be tokenized
     * @return A Vector containing the names of the visible columns
     */
    private Vector findVisibleColumns(String visibleColumns) {
	logger.debug("IN");
	if (visibleColumns == null || visibleColumns.trim().equals(""))
	    return new Vector();
	StringTokenizer strToken = new StringTokenizer(visibleColumns, ",");
	Vector columns = new Vector();
	while (strToken.hasMoreTokens()) {
	    String val = strToken.nextToken().trim();
	    columns.add(val);
	}
	logger.debug("OUT");
	return columns;
    }

    /**
     * Creates a BACK_BUTTON SourceBean with the parameters passed at input
     * 
     * @param backButtonParams
     *                The HashMap containing the parameters names and values
     * @return The BACK_BUTTON SourceBean
     * @throws SourceBeanException
     */
    private SourceBean createBackButton(HashMap backButtonParams) throws SourceBeanException {

	logger.debug("IN");
	if (backButtonParams == null || backButtonParams.size() == 0)
	    return new SourceBean("BACK_BUTTON");
	String backButtonStr = "<BACK_BUTTON confirm=\"FALSE\" name=\"back\" image=\"/img/back.png\" "
		+ "label=\"SBIListLookPage.backButton\"/>";

	SourceBean toReturn = SourceBean.fromXMLString(backButtonStr);
	logger.debug("OUT");
	return toReturn;
    }

    private SourceBean createSelectButton(HashMap backButtonParams) throws SourceBeanException {
	logger.debug("IN");
	if (backButtonParams == null || backButtonParams.size() == 0)
	    return new SourceBean("BACK_BUTTON");
	String backButtonStr = "<BACK_BUTTON confirm=\"FALSE\" name=\"saveback\" image=\"/img/button_ok.gif\" "
		+ "label=\"SBIListLookPage.backButton\"/>";

	SourceBean toReturn = SourceBean.fromXMLString(backButtonStr);
	logger.debug("OUT");
	return toReturn;
    }

    /**
     * Creates a SELECT_CAPTION SourceBean with the parameters passed at input
     * 
     * @param selectCaptionParams
     *                The HashMap containing the parameters names and values
     * @param valueColumn
     *                The relative variable to be set in the caption URL
     *                representing the lookup value
     * @return The SELECT_CAPTION SourceBean
     * @throws SourceBeanException
     */
    private SourceBean createSelectCaption(HashMap selectCaptionParams, String valueColumn) throws SourceBeanException {
	logger.debug("IN");
	if (selectCaptionParams == null || selectCaptionParams.size() == 0)
	    return new SourceBean("SELECT_CAPTION");
	String selectCaptionStr = "<SELECT_CAPTION confirm=\"FALSE\" image=\"/img/button_ok.gif\" "
		+ " label=\"SBIListLookPage.selectButton\">";
	Set keys = selectCaptionParams.keySet();
	Iterator it = keys.iterator();
	while (it.hasNext()) {
	    String key = it.next().toString();
	    String value = selectCaptionParams.get(key).toString();
	    selectCaptionStr += "	<PARAMETER name=\"" + key + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + value
		    + "\" />";
	}
	selectCaptionStr += "	<PARAMETER name=\"LOOKUP_VALUE\" scope=\"LOCAL\" type=\"RELATIVE\" value=\""
		+ valueColumn + "\" />";
	selectCaptionStr += "</SELECT_CAPTION>";
	SourceBean toReturn = SourceBean.fromXMLString(selectCaptionStr);
	logger.debug("OUT");
	return toReturn;
    }

    /**
     * Creates a COLUMNS SourceBean with all the columns in the Vector passed at
     * input
     * 
     * @param columns
     *                The Vector containing the columns to be visualized
     * @return The COLUMNS SourceBean
     * @throws SourceBeanException
     */
    private SourceBean createColumnsSB(Vector columns) throws SourceBeanException {
	logger.debug("IN");
	if (columns == null || columns.size() == 0)
	    return new SourceBean("COLUMNS");
	String columnsStr = "<COLUMNS>";
	for (int i = 0; i < columns.size(); i++) {
	    columnsStr += "	<COLUMN name=\"" + columns.get(i).toString() + "\" />";
	}
	columnsStr += "</COLUMNS>";
	SourceBean columnsSB = SourceBean.fromXMLString(columnsStr);
	logger.debug("OUT");
	return columnsSB;
    }

    /**
     * Gets the list.
     * 
     * @param request The request SourceBean
     * @param response The response SourceBean
     * 
     * @return ListIFace
     * 
     * @throws Exception the exception
     */
    public ListIFace getList(SourceBean request, SourceBean response) throws Exception {

	logger.debug("IN");
	ListIFace list = null;
	HashMap paramsMap = null;

	list = getListFromLov(request, response);
	paramsMap = getParams(request);

	String inputType = getModalityValue(request).getITypeCd();
	if (isCorrelated(request)) {
	    list = filterListForCorrelatedParam(request, list);
	    String correlatedParuseIdStr = (String) request.getAttribute("correlated_paruse_id");
	    paramsMap.put("correlated_paruse_id", request.getAttribute("correlated_paruse_id"));
	    paramsMap.put("LOOKUP_PARAMETER_ID", request.getAttribute("LOOKUP_PARAMETER_ID"));
	}
	String executionId = (String) request.getAttribute("spagobi_execution_id");
	String flowId = (String) request.getAttribute("spagobi_flow_id");
	if (executionId != null && flowId != null) {
	    paramsMap.put("spagobi_execution_id", executionId);
	    paramsMap.put("spagobi_flow_id", flowId);
	}

	response.setAttribute("PARAMETERS_MAP", paramsMap);
	response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "LookupPublisher");
	logger.debug("OUT");
	return list;
    }

}
