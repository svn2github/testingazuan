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
package it.eng.spagobi.analiticalmodel.execution.service;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.dbaccess.Utils;
import it.eng.spago.dbaccess.sql.DataConnection;
import it.eng.spago.dbaccess.sql.DataRow;
import it.eng.spago.dbaccess.sql.SQLCommand;
import it.eng.spago.dbaccess.sql.result.DataResult;
import it.eng.spago.dbaccess.sql.result.ScrollableDataResult;
import it.eng.spago.dispatching.module.list.basic.AbstractBasicListModule;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.paginator.basic.ListIFace;
import it.eng.spago.paginator.basic.PaginatorIFace;
import it.eng.spago.paginator.basic.impl.GenericList;
import it.eng.spago.paginator.basic.impl.GenericPaginator;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.Parameter;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.ParameterUse;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.dao.IParameterDAO;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.dao.IParameterUseDAO;
import it.eng.spagobi.behaviouralmodel.lov.bo.FixedListDetail;
import it.eng.spagobi.behaviouralmodel.lov.bo.IJavaClassLov;
import it.eng.spagobi.behaviouralmodel.lov.bo.JavaClassDetail;
import it.eng.spagobi.behaviouralmodel.lov.bo.LovDetailFactory;
import it.eng.spagobi.behaviouralmodel.lov.bo.ModalitiesValue;
import it.eng.spagobi.behaviouralmodel.lov.bo.QueryDetail;
import it.eng.spagobi.behaviouralmodel.lov.bo.ScriptDetail;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.services.DelegatedBasicListService;
import it.eng.spagobi.commons.utilities.DataSourceUtilities;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.commons.utilities.PortletUtilities;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * 
 * @author Angelo Bernabei - angelo.bernabei@eng.it
 * 
 * This module read the parameters values 
 * 
 */

public class SelectParametersLookupModule extends AbstractBasicListModule {

    static private Logger logger = Logger.getLogger(SelectParametersLookupModule.class);

	// define variable for value column name
    private String valColName = "";
    private String descriptionColName = "";
    
    private static final String RETURN_PARAM = "returnParam";
    
    private static final String RETURN_FIELD_NAME = "parameterFieldName";
    
    /**
     * Class Constructor
     */
    public SelectParametersLookupModule() {
	super();
    }

    public ListIFace getList(SourceBean request, SourceBean response) throws Exception {
	logger.debug("IN");
	ListIFace list = null;
	// get role / par id / par field name name
	String roleName = (String) request.getAttribute("roleName");
	String parIdStr = (String) request.getAttribute("parameterId");
	String returnParam = (String) request.getAttribute(RETURN_PARAM);
	logger.debug("roleName=" + roleName);
	logger.debug("parameterId=" + parIdStr);
	logger.debug("returnParam=" + returnParam);
	if (roleName == null)
	    logger.warn("roleName is null");
	if (parIdStr == null)
	    logger.warn("parameterId is null");

	Integer parId = new Integer(parIdStr);
	// check if the parameter use is manual input
	IParameterUseDAO parusedao = DAOFactory.getParameterUseDAO();
	ParameterUse paruse = parusedao.loadByParameterIdandRole(parId, roleName);
	Integer manInp = paruse.getManualInput();
	if (manInp.intValue() == 1) {
	    String message = PortletUtilities.getMessage("scheduler.fillparmanually", "component_scheduler_messages");
	    response.setAttribute(SpagoBIConstants.MESSAGE_INFO, message);
	} else {
	    list = loadList(request, response, parId, roleName);
	}
	// fill response
	response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "SelectParameterPublisher");
	
	HashMap parametersMap = new HashMap();
	parametersMap.put("roleName", roleName);
	parametersMap.put("parameterId", parIdStr);
	parametersMap.put(RETURN_PARAM, returnParam);
	parametersMap.put("parameterFieldName", request.getAttribute("parameterFieldName"));
	response.setAttribute("PARAMETERS_MAP", parametersMap);
	
	logger.debug("OUT");
	return list;
    }

    private ListIFace loadList(SourceBean request, SourceBean response, Integer parId, String roleName)
	    throws Exception {
	logger.debug("IN");
	RequestContainer requestContainer = getRequestContainer();
	String parameterFieldName = (String) request.getAttribute("parameterFieldName");
	logger.debug("parameterFieldName=" + parameterFieldName);

	// define the spago paginator and list object
	PaginatorIFace paginator = new GenericPaginator();
	ListIFace list = new GenericList();



	// recover lov object
	IParameterDAO pardao = DAOFactory.getParameterDAO();
	Parameter par = pardao.loadForExecutionByParameterIDandRoleName(parId, roleName);
	ModalitiesValue modVal = par.getModalityValue();

	// get the lov provider
	String lovProvider = modVal.getLovProvider();

	// get from the request the type of lov
	String typeLov = LovDetailFactory.getLovTypeCode(lovProvider);

	// get the user profile
	SessionContainer permSession = this.getRequestContainer().getSessionContainer().getPermanentContainer();
	if (permSession == null)
	    logger.warn("Permanent session container is null!!!!");
	IEngUserProfile profile = (IEngUserProfile) permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
	if (profile == null)
	    logger.warn("IEngUserProfile is null!!!!");

	// read data
	SourceBean rowsSourceBean = null;
	if (typeLov.equalsIgnoreCase("QUERY")) {
	    rowsSourceBean = executeQuery(lovProvider, response, profile);
	    if (rowsSourceBean == null)
		return list;

	} else if (typeLov.equalsIgnoreCase("FIXED_LIST")) {
	    rowsSourceBean = executeFixedList(lovProvider, response, profile);
	    if (rowsSourceBean == null)
		return list;

	} else if (typeLov.equalsIgnoreCase("SCRIPT")) {
	    rowsSourceBean = executeScript(lovProvider, response, profile);
	    if (rowsSourceBean == null)
		return list;

	} else if (typeLov.equalsIgnoreCase("JAVA_CLASS")) {
	    rowsSourceBean = executeJavaClass(lovProvider, response, profile);
	    if (rowsSourceBean == null)
		return list;
	}
	logger.debug("valColName="+valColName);
	// fill paginator
//	int count = 0;
	if (rowsSourceBean != null) {
	    List rows = rowsSourceBean.getAttributeAsList(DataRow.ROW_TAG);
	    for (int i = 0; i < rows.size(); i++) {
		paginator.addRow(rows.get(i));
//		count++;
	    }
	}
//	paginator.setPageSize(count);
	list.setPaginator(paginator);

	// get all the columns name
	rowsSourceBean = list.getPaginator().getAll();
	List colNames = new ArrayList();
	List rows = null;
	if (rowsSourceBean != null) {
	    rows = rowsSourceBean.getAttributeAsList(DataRow.ROW_TAG);
	    if ((rows != null) && (rows.size() != 0)) {
		SourceBean row = (SourceBean) rows.get(0);
		List rowAttrs = row.getContainedAttributes();
		Iterator rowAttrsIter = rowAttrs.iterator();
		while (rowAttrsIter.hasNext()) {
		    SourceBeanAttribute rowAttr = (SourceBeanAttribute) rowAttrsIter.next();
		    colNames.add(rowAttr.getKey());
		}
	    }
	}

	// build module configuration for the list
	StringBuffer moduleConfigStr = new StringBuffer("");
	moduleConfigStr.append("<CONFIG>");
	moduleConfigStr.append("	<QUERIES/>");
	moduleConfigStr.append("	<COLUMNS>");
	// if there's no colum name add a fake column to show that there's no
	// data
	if (colNames.size() == 0) {
	    moduleConfigStr.append("	<COLUMN name=\"No Result Found\" />");
	} else {
	    Iterator iterColNames = colNames.iterator();
	    while (iterColNames.hasNext()) {
		String colName = (String) iterColNames.next();
		moduleConfigStr.append("	<COLUMN name=\"" + colName + "\" />");
	    }
	}
	moduleConfigStr.append("	</COLUMNS>");
	moduleConfigStr.append("	<CAPTIONS>");
	moduleConfigStr.append("		<SELECT_CAPTION  confirm=\"FALSE\" image=\"/img/button_ok.gif\" label=\"SBIListLookPage.selectButton\">");
	moduleConfigStr.append("			<ONCLICK>");
	moduleConfigStr.append("				<![CDATA[");
	moduleConfigStr.append("				parent.document.getElementById('<PARAMETER name='" + RETURN_PARAM + "' scope='SERVICE_REQUEST'/>').value='<PARAMETER name='" + valColName + "' scope='LOCAL'/>';");
	moduleConfigStr.append("				parent.document.getElementById('<PARAMETER name='" + RETURN_PARAM + "' scope='SERVICE_REQUEST'/>Desc').value='<PARAMETER name='" + descriptionColName + "' scope='LOCAL'/>';");
	moduleConfigStr.append("				parent.win_<PARAMETER name='" + RETURN_FIELD_NAME + "' scope='SERVICE_REQUEST'/>.hide();");
	moduleConfigStr.append("				]]>");
	moduleConfigStr.append("			</ONCLICK>");
	moduleConfigStr.append("		</SELECT_CAPTION>");
	moduleConfigStr.append("	</CAPTIONS>");
	moduleConfigStr.append("	<BUTTONS/>");
	moduleConfigStr.append("</CONFIG>");
	SourceBean moduleConfig = SourceBean.fromXMLString(moduleConfigStr.toString());
	response.setAttribute(moduleConfig);

	// filter the list
	String valuefilter = (String) request.getAttribute(SpagoBIConstants.VALUE_FILTER);
	if (valuefilter != null) {
	    String columnfilter = (String) request.getAttribute(SpagoBIConstants.COLUMN_FILTER);
	    String typeFilter = (String) request.getAttribute(SpagoBIConstants.TYPE_FILTER);
	    String typeValueFilter = (String) request.getAttribute(SpagoBIConstants.TYPE_VALUE_FILTER);
	    list = DelegatedBasicListService.filterList(list, valuefilter, typeValueFilter, columnfilter, typeFilter,
		    getResponseContainer().getErrorHandler());
	}

	// fill response
	response.setAttribute(SpagoBIConstants.PARAMETER_FIELD_NAME, parameterFieldName);
	response.setAttribute(SpagoBIConstants.VALUE_COLUMN_NAME, valColName);
	logger.debug("OUT");
	return list;
    }

    
    private SourceBean executeQuery(String lovProvider, SourceBean response, IEngUserProfile profile) throws Exception {
	logger.debug("IN");
	SourceBean result = null;
	logger.debug("lovProvider="+lovProvider);
	QueryDetail qd = QueryDetail.fromXML(lovProvider);
	if (qd.requireProfileAttributes()) {
	    String message = PortletUtilities.getMessage("scheduler.noProfileAttributesSupported",
		    "component_scheduler_messages");
	    response.setAttribute(SpagoBIConstants.MESSAGE_INFO, message);
	    return result;
	}
	valColName = qd.getValueColumnName();
	logger.debug("valColName="+valColName);
	descriptionColName = qd.getDescriptionColumnName();
	logger.debug("descriptionColName="+descriptionColName);
	String datasource = qd.getDataSource();
	String statement = qd.getQueryDefinition();

	try {
	    statement = GeneralUtilities.substituteProfileAttributesInString(statement, profile);
	    result = (SourceBean) executeSelect(getRequestContainer(), getResponseContainer(), datasource, statement);
	} catch (Exception e) {
	    logger.error("Exception",e);
	    String stacktrace = e.toString();
	    response.setAttribute("stacktrace", stacktrace);
            response.setAttribute("errorMessage", cleanStackTrace(stacktrace));
	    response.setAttribute("testExecuted", "false");
	}
	logger.debug("OUT");
	return result;
    }

    private SourceBean executeFixedList(String lovProvider, SourceBean response, IEngUserProfile profile)
	    throws Exception {
	logger.debug("IN");
	logger.debug("lovProvider."+lovProvider);
	SourceBean resultSB = null;
	FixedListDetail fixlistDet = FixedListDetail.fromXML(lovProvider);
	if (fixlistDet.requireProfileAttributes()) {
	    String message = PortletUtilities.getMessage("scheduler.noProfileAttributesSupported",
		    "component_scheduler_messages");
	    response.setAttribute(SpagoBIConstants.MESSAGE_INFO, message);
	    return null;
	}
	valColName = fixlistDet.getValueColumnName();
	logger.debug("valColName:"+valColName);
	descriptionColName = fixlistDet.getDescriptionColumnName();
	logger.debug("descriptionColName="+descriptionColName);
	
	try {
	    String result = fixlistDet.getLovResult(profile);
	    resultSB = SourceBean.fromXMLString(result);
	    if (!resultSB.getName().equalsIgnoreCase("ROWS")) {
		throw new Exception("The fix list is empty");
	    } else if (resultSB.getAttributeAsList(DataRow.ROW_TAG).size() == 0) {
		throw new Exception("The fix list is empty");
	    }
	} catch (Exception e) {
	    logger.error("Error while converting fix lov into spago list", e);
	    String stacktrace = e.toString();
	    response.setAttribute("stacktrace", stacktrace);
	    response.setAttribute("errorMessage", "Error while executing fix list lov");
	    response.setAttribute("testExecuted", "false");
	    return null;
	}
	logger.debug("OUT");
	return resultSB;
    }

    private SourceBean executeScript(String lovProvider, SourceBean response, IEngUserProfile profile) throws Exception {
	logger.debug("IN");
	SourceBean resultSB = null;
	ScriptDetail scriptDetail = ScriptDetail.fromXML(lovProvider);
	if (scriptDetail.requireProfileAttributes()) {
	    String message = PortletUtilities.getMessage("scheduler.noProfileAttributesSupported",
		    "component_scheduler_messages");
	    response.setAttribute(SpagoBIConstants.MESSAGE_INFO, message);
	    return null;
	}
	valColName = scriptDetail.getValueColumnName();
	logger.debug("valColName="+valColName);
	descriptionColName = scriptDetail.getDescriptionColumnName();
	logger.debug("descriptionColName="+descriptionColName);
	try {
	    String result = scriptDetail.getLovResult(profile);
	    resultSB = SourceBean.fromXMLString(result);
	} catch (Exception e) {
	    logger.error("Error while executing the script lov", e);
	    String stacktrace = e.toString();
	    response.setAttribute("stacktrace", stacktrace);
	    response.setAttribute("errorMessage", "Error while executing script");
	    response.setAttribute("testExecuted", "false");
	    return null;
	}
	logger.debug("OUT");
	return resultSB;
    }

    private SourceBean executeJavaClass(String lovProvider, SourceBean response, IEngUserProfile profile)
	    throws Exception {
	logger.debug("IN");
	SourceBean resultSB = null;
	JavaClassDetail javaClassDetail = JavaClassDetail.fromXML(lovProvider);
	if (javaClassDetail.requireProfileAttributes()) {
	    String message = PortletUtilities.getMessage("scheduler.noProfileAttributesSupported",
		    "component_scheduler_messages");
	    response.setAttribute(SpagoBIConstants.MESSAGE_INFO, message);
	    return null;
	}
	valColName = javaClassDetail.getValueColumnName();
	logger.debug("valColName="+valColName);
	descriptionColName = javaClassDetail.getDescriptionColumnName();
	logger.debug("descriptionColName="+descriptionColName);
	try {
	    String javaClassName = javaClassDetail.getJavaClassName();
	    IJavaClassLov javaClassLov = (IJavaClassLov) Class.forName(javaClassName).newInstance();
	    String result = javaClassLov.getValues(profile);
	    resultSB = SourceBean.fromXMLString(result);
	} catch (Exception e) {
	    logger.error("Error while executing the java class lov", e);
	    String stacktrace = e.toString();
	    response.setAttribute("stacktrace", stacktrace);
	    response.setAttribute("errorMessage", "Error while executing java class");
	    response.setAttribute("testExecuted", "false");
	    return null;
	}
	logger.debug("OUT");
	return resultSB;
    }

    /**
     * Executes a select statement.
     * 
     * @param requestContainer
     *                The request container object
     * @param responseContainer
     *                The response container object
     * @param pool
     *                The pool definition string
     * @param statement
     *                The statement definition string
     * @return A generic object containing the Execution results
     * @throws EMFInternalError

     */
    public static Object executeSelect(RequestContainer requestContainer, ResponseContainer responseContainer,
	    String datasource, String statement) throws EMFInternalError {
	logger.debug("IN");
	Object result = null;
	DataConnection dataConnection = null;
	SQLCommand sqlCommand = null;
	DataResult dataResult = null;
	try {
	    DataSourceUtilities dsUtil = new DataSourceUtilities();
	    Connection conn = dsUtil.getConnection(datasource);
	    dataConnection = dsUtil.getDataConnection(conn);
	    sqlCommand = dataConnection.createSelectCommand(statement);
	    dataResult = sqlCommand.execute();
	    ScrollableDataResult scrollableDataResult = (ScrollableDataResult) dataResult.getDataObject();
	    result = scrollableDataResult.getSourceBean();
	} finally {
	    Utils.releaseResources(dataConnection, sqlCommand, dataResult);
	    logger.debug("OUT");
	}
	return result;
    }
    
    private String cleanStackTrace(String stacktrace) {

	int startIndex = stacktrace.indexOf("java.sql.");
	int endIndex = stacktrace.indexOf("\n\tat ", startIndex);
	if (endIndex == -1)
	    endIndex = stacktrace.indexOf(" at ", startIndex);
	if (startIndex != -1 && endIndex != -1)
	    return stacktrace.substring(startIndex, endIndex);
	return "";
    }

}
