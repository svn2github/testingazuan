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

import it.eng.spago.base.Constants;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dbaccess.DataConnectionManager;
import it.eng.spago.dbaccess.SQLStatements;
import it.eng.spago.dbaccess.Utils;
import it.eng.spago.dbaccess.sql.DataConnection;
import it.eng.spago.dbaccess.sql.DataRow;
import it.eng.spago.dbaccess.sql.SQLCommand;
import it.eng.spago.dbaccess.sql.result.DataResult;
import it.eng.spago.dbaccess.sql.result.InformationDataResult;
import it.eng.spago.dbaccess.sql.result.ScrollableDataResult;
import it.eng.spago.dispatching.service.RequestContextIFace;
import it.eng.spago.dispatching.service.ServiceIFace;
import it.eng.spago.dispatching.service.list.basic.IFaceBasicListService;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.init.InitializerIFace;
import it.eng.spago.paginator.basic.ListIFace;
import it.eng.spago.paginator.basic.PaginatorIFace;
import it.eng.spago.paginator.basic.impl.GenericList;
import it.eng.spago.paginator.basic.impl.GenericPaginator;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spago.util.ContextScooping;
import it.eng.spago.util.QueryExecutor;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
/**
 * Provides all methods to handle a lookup list of objects. Its methods are called from a list
 * module class to get the objects list.
 */
public class LookupQueryDelegatedBasicListService {
	
	public static final String LIST_PAGE = "LIST_PAGE";
	public static final String LIST_FIRST = "LIST_FIRST";
	public static final String LIST_PREV = "LIST_PREV";
	public static final String LIST_NEXT = "LIST_NEXT";
	public static final String LIST_LAST = "LIST_LAST";
	public static final String LIST_CURRENT = "LIST_CURRENT";
	public static final String LIST_NOCACHE = "LIST_NOCACHE";
	public static final String LIST_DELETE = "LIST_DELETE";

	private LookupQueryDelegatedBasicListService() {
		super();
	} // private KFDelegatedBasicListService()

	/**
	 * The service method for this class.
	 * 
	 * @param service	The service interface object
	 * @param request	The request Source Bean
	 * @param response	The response Source Bean
	 * @throws Exception	If any Exception occurred
	 */
	public static void service(ServiceIFace service, SourceBean request, SourceBean response) throws Exception {
		if ((service == null) || (request == null) || (response == null)) {
			TracerSingleton.log(
				Constants.NOME_MODULO,
				TracerSingleton.WARNING,
				"KFDelegatedBasicListService::service: parametri non validi");
			return;
		} // if ((service == null) || (request == null) || (response == null))
		TracerSingleton.log(
			Constants.NOME_MODULO,
			TracerSingleton.DEBUG,
			"KFDelegatedBasicListService::service: request",
			request);
		String message = getMessage(request);
		if ((message == null) || message.equalsIgnoreCase("BEGIN"))
			message = LIST_FIRST;
		String list_nocache = (String) request.getAttribute(LIST_NOCACHE);
		if (list_nocache == null)
			list_nocache = "FALSE";
		IFaceBasicListService listService = (IFaceBasicListService) service;
		/*
		if (message.equalsIgnoreCase(LIST_DELETE)) {
			listService.delete(request, response);
			message = LIST_PAGE;
			list_nocache = "TRUE";
		} // if (message.equalsIgnoreCase(LIST_DELETE))
		*/
		if ((listService.getList() == null) || list_nocache.equalsIgnoreCase("TRUE"))
			listService.setList(listService.getList(request, response));
		if (listService.getList() == null) {
			TracerSingleton.log(
				Constants.NOME_MODULO,
				TracerSingleton.WARNING,
				"KFDelegatedBasicListService::service: _list nullo");
			return;
		} // if (listService.getList() == null)
		int pagedListNumber = 1;
		if (message.equalsIgnoreCase(LIST_PAGE)) {
			String list_page = (String) request.getAttribute(LIST_PAGE);
			if (list_page == null)
				list_page = "1";
			try {
				pagedListNumber = Integer.parseInt(list_page);
			} // try
			catch (Exception ex) {
				TracerSingleton.log(
					Constants.NOME_MODULO,
					TracerSingleton.CRITICAL,
					"KFDelegatedBasicListService::service: Integer.parseInt(list_page)",
					ex);
			} // catch (Exception ex) try
		} // if (message.equalsIgnoreCase(LIST_PAGE))
		else if (message.equalsIgnoreCase(LIST_FIRST))
			pagedListNumber = 1;
		else if (message.equalsIgnoreCase(LIST_PREV))
			pagedListNumber = listService.getList().getPrevPage();
		else if (message.equalsIgnoreCase(LIST_NEXT))
			pagedListNumber = listService.getList().getNextPage();
		else if (message.equalsIgnoreCase(LIST_LAST))
			pagedListNumber = listService.getList().pages();
		else if (message.equalsIgnoreCase(LIST_CURRENT))
			pagedListNumber = listService.getList().getCurrentPage();
		TracerSingleton.log(
			Constants.NOME_MODULO,
			TracerSingleton.DEBUG,
			"KFDelegatedBasicListService::service: pagedListNumber [" + pagedListNumber + "]");
		listService.getList().clearDynamicData();
		listService.callback(request, response, listService.getList(), pagedListNumber);
		SourceBean pagedList = listService.getList().getPagedList(pagedListNumber);
		if (pagedList == null) {
			TracerSingleton.log(
				Constants.NOME_MODULO,
				TracerSingleton.WARNING,
				"KFDelegatedBasicListService::service: pagedList nullo");
			return;
		} // if (pagedList == null)
		try {
			response.setAttribute(pagedList);
		} // try
		catch (SourceBeanException ex) {
			TracerSingleton.log(
				Constants.NOME_MODULO,
				TracerSingleton.CRITICAL,
				"KFDelegatedBasicListService::service: response.setAttribute(pagedList)",
				ex);
		} // catch (SourceBeanException ex) try
		TracerSingleton.log(
			Constants.NOME_MODULO,
			TracerSingleton.DEBUG,
			"KFDelegatedBasicListService::service: response",
			response);
	} // public static void service(ServiceIFace service, SourceBean request,

	// SourceBean response) throws Exception
	/**
	 * Gets the list for a particular SpagoBI object.
	 * 
	 * @param service	The service interface object
	 * @param request	The request Source Bean
	 * @param response	The response Source Bean
	 * @throws Exception	If any exception occurred
	 * 
	 */
	public static ListIFace getList(ServiceIFace service, SourceBean request, SourceBean response, SourceBean moduleConfig) throws Exception {
		PaginatorIFace paginator = new GenericPaginator();
		InitializerIFace serviceInitializer = (InitializerIFace) service;
		
		int pagedRows = 0;
		if (moduleConfig.getAttribute("ROWS") != null){
			pagedRows = Integer.parseInt((String)moduleConfig.getAttribute("ROWS"));
		}
		
		paginator.setPageSize(pagedRows);
		String pool = ((SourceBean)moduleConfig.getAttribute("CONNECTION")).getCharacters();
		String statement = ((SourceBean)moduleConfig.getAttribute("STMT")).getCharacters();
		String visibleColumns = ((SourceBean)moduleConfig.getAttribute("VISIBLE-COLUMNS")).getCharacters();
		String valueColumn = ((SourceBean)moduleConfig.getAttribute("VALUE-COLUMN")).getCharacters();
		
		
		RequestContextIFace serviceRequestContext = (RequestContextIFace) service;
		SourceBean rowsSourceBean =
			(SourceBean) executeSelect(
				serviceRequestContext.getRequestContainer(),
				serviceRequestContext.getResponseContainer(),
				pool,
				statement,
				"SELECT");
		List rowsVector = null;
		if (rowsSourceBean != null)
			rowsVector = rowsSourceBean.getAttributeAsList(DataRow.ROW_TAG);
		if ((rowsSourceBean == null)) {//|| (rowsVector.size() == 0)) {
			EMFErrorHandler engErrorHandler = serviceRequestContext.getErrorHandler();
			engErrorHandler.addError(new EMFUserError(EMFErrorSeverity.INFORMATION, 10001));
		} // if ((rowsSourceBean == null) || (rowsVector.size() == 0))
		else{
			for (int i = 0; i < rowsVector.size(); i++)
				paginator.addRow(rowsVector.get(i));
		}
		ListIFace list = new GenericList();
		list.setPaginator(paginator);
		// list.addStaticData(firstData);
		return list;
	} // public static ListIFace getList(ServiceIFace service, SourceBean
	/**
	 * Executes a select statement.
	 * 
	 * @param requestContainer The request container object
	 * @param responseContainer The response container object
	 * @param pool The pool definition string
	 * @param statement	The statement definition string
	 * @param type
	 * @return A generic object containing the Execution results
	 */
	 public static Object executeSelect(RequestContainer requestContainer,
			ResponseContainer responseContainer, String pool, String statement,
			String type) {
		Object result = null;
		DataConnectionManager dataConnectionManager = null;
		DataConnection dataConnection = null;
		SQLCommand sqlCommand = null;
		DataResult dataResult = null;
		try {
			dataConnectionManager = DataConnectionManager.getInstance();
			dataConnection = dataConnectionManager.getConnection(pool);
			sqlCommand = dataConnection.createSelectCommand(statement);

			dataResult = sqlCommand.execute();

			ScrollableDataResult scrollableDataResult = (ScrollableDataResult) dataResult
					.getDataObject();
			result = scrollableDataResult.getSourceBean();

			
		} // try
		catch (Exception ex) {
			TracerSingleton.log(Constants.NOME_MODULO,
					TracerSingleton.CRITICAL, "executeSelect:", ex);
		} // catch (Exception ex) try
		finally {
			Utils.releaseResources(dataConnection, sqlCommand, dataResult);
		} // finally try
		return result;
	} //     public static Object executeQuery(RequestContainer
	
	// SourceBean response)
	 /**
		 * Gets the information contained in a Source Bean attribute 
		 * identified by the key "MESSAGE". 
		 * 
		 * @param request The input Source Bean
		 */
	 public static String getMessage(SourceBean request) {
		return (String) request.getAttribute("MESSAGE");
	} // public static String getMessage(SourceBean request)
} // public class KFDelegatedBasicListService
