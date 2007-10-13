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

import it.eng.spago.base.SourceBean;
import it.eng.spago.dbaccess.sql.DataConnection;
import it.eng.spago.dbaccess.sql.DataRow;
import it.eng.spago.dbaccess.sql.mappers.SQLMapper;
import it.eng.spago.dispatching.service.RequestContextIFace;
import it.eng.spago.dispatching.service.ServiceIFace;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.init.InitializerIFace;
import it.eng.spago.paginator.basic.ListIFace;
import it.eng.spago.paginator.basic.PaginatorIFace;
import it.eng.spago.paginator.basic.impl.GenericList;
import it.eng.spago.paginator.basic.impl.GenericPaginator;
import it.eng.spago.util.QueryExecutor;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.metadata.HibernateUtil;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.sql.Connection;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DelegatedHibernateConnectionListService extends DelegatedBasicListService {
	
	public static ListIFace getList(ServiceIFace service, SourceBean request, SourceBean response) throws Exception {

		Session aSession = null;
		Transaction tx = null;
		PaginatorIFace paginator = new GenericPaginator();
		InitializerIFace serviceInitializer = (InitializerIFace) service;
		RequestContextIFace serviceRequestContext = (RequestContextIFace) service;
		int pagedRows = 10;
		SourceBean rowsSourceBean = null;
		pagedRows = Integer.parseInt((String) serviceInitializer.getConfig().getAttribute("ROWS"));
		paginator.setPageSize(pagedRows);
		SourceBean statement = (SourceBean) serviceInitializer.getConfig().getAttribute("QUERIES.SELECT_QUERY");
		try {
			aSession = HibernateUtil.currentSession();
			tx = aSession.beginTransaction();
			Connection jdbcConnection = aSession.connection();
			DataConnection dataConnection = getDataConnection(jdbcConnection);
			rowsSourceBean =
				(SourceBean) QueryExecutor.executeQuery(
					serviceRequestContext.getRequestContainer(),
					serviceRequestContext.getResponseContainer(),
					dataConnection,
					statement,
					"SELECT");
		} catch (HibernateException he) {
			logException(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (aSession != null) {
				if (aSession.isOpen()) aSession.close();
			}
		}
		List rowsVector = null;
		if (rowsSourceBean != null)
			rowsVector = rowsSourceBean.getAttributeAsList(DataRow.ROW_TAG);
		if (rowsSourceBean == null) {
			EMFErrorHandler engErrorHandler = serviceRequestContext.getErrorHandler();
			engErrorHandler.addError(new EMFUserError(EMFErrorSeverity.INFORMATION, 10001));
		}
		else
			for (int i = 0; i < rowsVector.size(); i++)
				paginator.addRow(rowsVector.get(i));		
		
		ListIFace list = new GenericList();
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
			list = filterList(list, valuefilter, typeValueFilter, columnfilter, typeFilter, serviceRequestContext.getErrorHandler());
		}
		
		return list;
	}
	
   public static DataConnection getDataConnection(Connection con) throws EMFInternalError {
       DataConnection dataCon = null;
       try {
           Class mapperClass = Class.forName("it.eng.spago.dbaccess.sql.mappers.OracleSQLMapper");
           SQLMapper sqlMapper = (SQLMapper)mapperClass.newInstance();
           dataCon = new DataConnection(con, "2.1", sqlMapper);
       } catch(Exception e) {
           SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, DelegatedHibernateConnectionListService.class.getName() , "getDataConnection",
                   "Error while getting Spago DataConnection " + e);
           throw new EMFInternalError(EMFErrorSeverity.ERROR, "cannot build DataConnection object");
       }
       return dataCon;
   }
   
	/**
	 * Traces the exception information of a throwable input object
	 * @param t The input throwable object
	 */
	public static void logException(Throwable t){
		SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
	            t.getClass().getName(), 
	            "", 
	            t.getMessage());
	}
	
}
