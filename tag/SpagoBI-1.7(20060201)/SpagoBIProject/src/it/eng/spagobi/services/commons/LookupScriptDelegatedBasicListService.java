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
public class LookupScriptDelegatedBasicListService {
	
	public static final String LIST_PAGE = "LIST_PAGE";
	public static final String LIST_FIRST = "LIST_FIRST";
	public static final String LIST_PREV = "LIST_PREV";
	public static final String LIST_NEXT = "LIST_NEXT";
	public static final String LIST_LAST = "LIST_LAST";
	public static final String LIST_CURRENT = "LIST_CURRENT";
	public static final String LIST_NOCACHE = "LIST_NOCACHE";
	public static final String LIST_DELETE = "LIST_DELETE";

	private LookupScriptDelegatedBasicListService() {
		super();
	} // private KFDelegatedBasicListService()

	

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
	public static ListIFace getList(ServiceIFace service, SourceBean request, SourceBean response, SourceBean result) throws Exception {
		
		PaginatorIFace paginator = new GenericPaginator();
		int pagedRows = 10;
		paginator.setPageSize(pagedRows);
		RequestContextIFace serviceRequestContext = (RequestContextIFace) service;
		SourceBean rowsSourceBean = (SourceBean)result.getAttribute("ROWS");
		List rowsList = null;
		if(rowsSourceBean != null)
			rowsList = rowsSourceBean.getAttributeAsList(DataRow.ROW_TAG);
		if ((rowsSourceBean == null)) {
			EMFErrorHandler engErrorHandler = serviceRequestContext.getErrorHandler();
			engErrorHandler.addError(new EMFUserError(EMFErrorSeverity.INFORMATION, 10001));
		} else{
			for (int i = 0; i < rowsList.size(); i++)
				paginator.addRow(rowsList.get(i));
		}
		ListIFace list = new GenericList();
		list.setPaginator(paginator);
		return list;
		
		/*
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
		*/
	} 

} // public class KFDelegatedBasicListService
