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
package it.eng.spagobi.services.commons;

import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dbaccess.DataConnectionManager;
import it.eng.spago.dbaccess.SQLStatements;
import it.eng.spago.dbaccess.sql.DataConnection;
import it.eng.spago.dbaccess.sql.SQLCommand;
import it.eng.spago.dbaccess.sql.result.DataResult;
import it.eng.spago.dbaccess.sql.result.ScrollableDataResult;
import it.eng.spago.dispatching.module.list.basic.AbstractBasicListModule;
import it.eng.spago.paginator.basic.ListIFace;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Gioia
 *
 */
public class AbstractBasicCheckListModule extends AbstractBasicListModule {

	protected SourceBean config;
	Map checkedObjectsMap = null;
	int pageNumber = 0;
	boolean returnValues = true;
	
	public static final String MODULE_PAGE = "CheckListPage";
	
	public static final String OBJECT = "OBJECT";
	public static final String CHECKED_OBJECTS = "CHECKEDOBJECTS";
	
	
	public static void clearSession(SessionContainer session, String moduleName){
		
		// clear all input parameters
		ConfigSingleton spagoBiConfig = ConfigSingleton.getInstance();
		// TODO patch this
		SourceBean moduleConfig = (SourceBean)spagoBiConfig.getFilteredSourceBeanAttribute("MODULES.MODULE", "NAME", moduleName);
		List parametersList = null;
		SourceBean parameter = null;
		String pvalue, str;
		
		parametersList = moduleConfig.getAttributeAsList("QUERIES.SELECT_QUERY.PARAMETER");		
		for(int i = 0; i < parametersList.size(); i++) {			
			parameter = (SourceBean)parametersList.get(i);
			pvalue = (String)parameter.getAttribute("value");
			if(pvalue == null) {
				str = (String)parameter.getAttribute("name");
				session.delAttribute(str);
			}			
		}
		
		parametersList = moduleConfig.getAttributeAsList("QUERIES.CHECKED_QUERY.PARAMETER");		
		for(int i = 0; i < parametersList.size(); i++) {			
			parameter = (SourceBean)parametersList.get(i);
			pvalue = (String)parameter.getAttribute("value");
			if(pvalue == null) {
				str = (String)parameter.getAttribute("name");
				session.delAttribute(str);
			}			
		}
		
		// clear all output parameters
		session.delAttribute("RETURN_FROM_MODULE");
		session.delAttribute("RETURN_STATUS");
	}
	
	public AbstractBasicCheckListModule(){
		super();
	}
			
	public void save() throws Exception {
		SourceBean chekhedObjects = getCheckedObjects();
	}
	
	public void exitFromModule(SourceBean response, boolean abort) throws Exception{
		SessionContainer session = this.getRequestContainer().getSessionContainer();
		
		if(!abort && returnValues){
			SourceBean chekhedObjects = getCheckedObjects();
			session.setAttribute("RETURN_VALUES", chekhedObjects);
		}
		
		String moduleName = (String)_request.getAttribute("AF_MODULE_NAME");
				
		session.setAttribute("RETURN_FROM_MODULE", moduleName);
		session.setAttribute("RETURN_STATUS", ((abort)?"ABORT":"OK") );
		response.setAttribute("PUBLISHER_NAME", "ReturnBackPublisher");
	}
	
	public String getObjectKey(SourceBean object) {
		String objectIdName = (String)((SourceBean) config.getAttribute("KEYS.OBJECT")).getAttribute("key");				
		String objectIdValue = object.getAttribute(objectIdName).toString();
		return objectIdValue.toLowerCase();
	}
	
	public SourceBean getObject(String key) throws Exception {
		String objectIdName = (String)((SourceBean) config.getAttribute("KEYS.OBJECT")).getAttribute("key");
		SourceBean object = new SourceBean(OBJECT);
		object.setAttribute(objectIdName, key);
		return object;
	}
	
	public SourceBean getCheckedObjects() throws Exception{
		SourceBean chekhedObjects = new SourceBean(CHECKED_OBJECTS);
		Iterator it = checkedObjectsMap.keySet().iterator();
		while(it.hasNext()){
			String key = (String)it.next();
			SourceBean object = getObject(key);
			chekhedObjects.setAttribute(object);
		}	
		return chekhedObjects;
	}
		
	public String[] getQueryParameters(String queryName, SourceBean request) {
		String[] parameters = null;
		
		List parametersList = config.getAttributeAsList("QUERIES." + queryName + ".PARAMETER");
		parameters = new String[parametersList.size()];
		SourceBean parameter = null;
		String pvalue, str; int index;
		for(int i = 0; i < parametersList.size(); i++) {			
			parameter = (SourceBean)parametersList.get(i);
			str = (String)parameter.getAttribute("number");
			index = Integer.parseInt(str);
			pvalue = (String)parameter.getAttribute("value");
			if(pvalue == null) {
				str = (String)parameter.getAttribute("name");
				pvalue = (String)getAttribute(str, request);
			}
			parameters[index] = pvalue;		
		}
		
		return parameters;
	}	
	
	public String getQueryStatement(String queryName, String[] parameters) {
		String statementStr = null;
						
		SourceBean statement = (SourceBean) config.getAttribute("QUERIES." + queryName);		
		statementStr = SQLStatements.getStatement((String) statement.getAttribute("STATEMENT"));
		for(int i = 0; i < parameters.length; i++) {			
			statementStr = statementStr.replaceFirst("\\?", parameters[i]);
		}
		return statementStr;
	}
			
	private List getCheckedObjectKeys(SourceBean request){
		List results = new ArrayList();
		List attrs = request.getContainedAttributes();
		for(int i = 0; i < attrs.size(); i++){
			SourceBeanAttribute attr = (SourceBeanAttribute)attrs.get(i);
			String key = (String)attr.getKey();
			if(key.startsWith("checkbox")) {
				String id = key.substring(key.indexOf(':')+1, key.length());
				results.add(id);
			}
		}
		
		return results;
	}
	
	public void createCheckedObjectMap(SourceBean request) throws Exception {
		checkedObjectsMap = new HashMap();

		// get CHECKED_QUERY query parameters

		String[] parameters = getQueryParameters("CHECKED_QUERY", request);

		// get CHECKED_QUERY statment
		String statement = getQueryStatement("CHECKED_QUERY", parameters);

		// exec CHECKED_QUERY
		ScrollableDataResult scrollableDataResult = null;
		SQLCommand sqlCommand = null;
		DataConnection dataConnection = null;
		DataResult dataResult = null;
		String pool = null;
		try {
			pool = (String) config.getAttribute("POOL");
			dataConnection = DataConnectionManager.getInstance().getConnection(
					pool);
			sqlCommand = dataConnection.createSelectCommand(statement);
			dataResult = sqlCommand.execute();
			scrollableDataResult = (ScrollableDataResult) dataResult
					.getDataObject();
			SourceBean chekedObjectsBean = scrollableDataResult.getSourceBean();
			List checkedObjectsList = chekedObjectsBean
					.getAttributeAsList("ROW");
			for (int i = 0; i < checkedObjectsList.size(); i++) {
				SourceBean objects = (SourceBean) checkedObjectsList.get(i);
				String key = getObjectKey(objects);
				checkedObjectsMap.put(key, key);
			}

		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass()
					.getName(), "createCheckedObjectMap", e.getMessage(), e);
		} finally {
			if (dataConnection != null)
				dataConnection.close();
		}
	}
	
	public void updateCheckedObjectMap(SourceBean request) throws Exception {
		checkedObjectsMap = new HashMap();
		
		String sourceBeanStr = (String)request.getAttribute(CHECKED_OBJECTS);
		SourceBean checked = SourceBean.fromXMLString(sourceBeanStr);
		List objectsList = checked.getAttributeAsList(OBJECT);
		for(int i = 0; i < objectsList.size(); i++) {
			SourceBean object = (SourceBean)objectsList.get(i);
			String key = getObjectKey(object);	
			checkedObjectsMap.put(key, key);
		}		
		
		List checkedEntityKeys = getCheckedObjectKeys(request);
		for(int i = 0; i < checkedEntityKeys.size(); i++) {
			String key = (String)checkedEntityKeys.get(i);	
			checkedObjectsMap.put(key, key);
		}	
	}
	
	public void preprocess(SourceBean request) throws Exception {		
		if(request.getAttribute(CHECKED_OBJECTS) != null) {
			updateCheckedObjectMap(request);
			String pageNumberStr = (String)request.getAttribute("PAGE_NUMBER");
			pageNumber = Integer.parseInt(pageNumberStr);
		}
		else {
			createCheckedObjectMap(request);
			pageNumber = 1;
		}		
	}
	
	
	public boolean isChecked(SourceBean object) {
		return (checkedObjectsMap.get(getObjectKey(object)) != null);
	}
		
	public void postprocess(SourceBean response) throws Exception {	
		List objectsList = response.getAttributeAsList("PAGED_LIST.ROWS.ROW");
		SourceBean pagedList = (SourceBean)response.getAttribute("PAGED_LIST");
		response.delAttribute("PAGED_LIST");
		pagedList.delAttribute("ROWS");
		
		SourceBean rows = new SourceBean("ROWS");
		for(int i = 0; i < objectsList.size(); i++) {
			SourceBean object = (SourceBean)objectsList.get(i);
			
			if(isChecked(object)) {					
				object.setAttribute("CHECKED", "true");	
				String key = getObjectKey(object);
				checkedObjectsMap.remove(key);
			}
			else {
				object.setAttribute("CHECKED", "false");				
			}
			rows.setAttribute(object);
		}		
				
		SourceBean chekhedObjects = getCheckedObjects();
		
		pagedList.setAttribute(rows);
		response.setAttribute(pagedList);
		response.setAttribute(chekhedObjects);
	}
	
	public SourceBean _request = null;
	public SourceBean _response = null;
	
	public void service(SourceBean request, SourceBean response) throws Exception {		
		config = getConfig();
		_request = request;
		_response = response;
		
		String message = (String)request.getAttribute("MESSAGE");

		if(message == null || message.equalsIgnoreCase("INIT_CHECKLIST")) {
			preprocess(request);	
			super.service(request, response); 
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
				super.service(request, response); 
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
	}
	
	public void navigationHandler(SourceBean request, SourceBean response, boolean moveNext) throws Exception{
		preprocess(request);
		int destPageNumber = (moveNext)? pageNumber+1: pageNumber-1;		
		request.updAttribute("MESSAGE", "LIST_PAGE");	
		request.setAttribute("LIST_PAGE", "" + destPageNumber);			
		super.service(request, response); 				
		postprocess(response); 
		response.setAttribute("PUBLISHER_NAME", "CheckLinksDefaultPublischer");	
	}
	
	
	
	protected Object getAttribute(String attrName, SourceBean request) {
		Object attrValue = null;
		attrValue = request.getAttribute(attrName);
		if(attrValue == null) {
			SessionContainer session = this.getRequestContainer().getSessionContainer();
			attrValue = session.getAttribute(attrName);
		}
		else {
		}
			
		return attrValue;
	}
	
	public ListIFace getList(SourceBean request, SourceBean response) throws Exception {
		return DelegatedBasicListService.getList(this, request, response);
	} 
	
	public boolean delete(SourceBean request, SourceBean response) {
		return DelegatedBasicListService.delete(this, request, response);
	} 
	
}
