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

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dbaccess.sql.DataRow;
import it.eng.spago.dispatching.module.list.basic.AbstractBasicListModule;
import it.eng.spago.paginator.basic.ListIFace;
import it.eng.spago.paginator.basic.PaginatorIFace;
import it.eng.spago.paginator.basic.impl.GenericList;
import it.eng.spago.paginator.basic.impl.GenericPaginator;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.ModalitiesValue;
import it.eng.spagobi.bo.javaClassLovs.IJavaClassLov;
import it.eng.spagobi.bo.lov.JavaClassDetail;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.services.commons.DelegatedBasicListService;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Tests the script and produces the list as output. 
 * 
 * @author Zerbetto
 */

public class ListTestJavaClassModule extends AbstractBasicListModule {

	public ListTestJavaClassModule() {
		super();
	}

	public ListIFace getList(SourceBean request, SourceBean response)
			throws Exception {
		
		String lovProviderModified = (String) request.getAttribute("lovProviderModified");
		if (lovProviderModified != null && !lovProviderModified.trim().equals("")) {
			response.setAttribute("lovProviderModified", lovProviderModified);
			HashMap map = new HashMap();
			map.put("lovProviderModified", lovProviderModified);
			response.setAttribute("PARAMETERS_MAP", map);
		}
		RequestContainer requestContainer = getRequestContainer();
		SessionContainer session = requestContainer.getSessionContainer();
		ModalitiesValue modVal = (ModalitiesValue) session.getAttribute(SpagoBIConstants.MODALITY_VALUE_OBJECT);
		String lovProvider = modVal.getLovProvider();
		JavaClassDetail javaClassDetail = JavaClassDetail.fromXML(lovProvider);
		SourceBean rowsSourceBean = null;
    	
		try {
			
			IEngUserProfile profile = (IEngUserProfile) session.getPermanentContainer().getAttribute(IEngUserProfile.ENG_USER_PROFILE);
			String javaClassName = javaClassDetail.getJavaClassName();
			IJavaClassLov javaClassLov = (IJavaClassLov) Class.forName(javaClassName).newInstance();
    		String result = javaClassLov.getValues(profile);
    		Vector columns = new Vector();
    		
    		try {
        		rowsSourceBean = SourceBean.fromXMLString(result);
        		SourceBean visibleColumnsSB = (SourceBean) rowsSourceBean.getAttribute("VISIBLE-COLUMNS");
        		SourceBean valueColumnSB = (SourceBean) rowsSourceBean.getAttribute("VALUE-COLUMN");
        		
        		// if the script result has not the visible-columns or value-column definition
        		// returns an empty list and puts in response the script result as a string
        		if (visibleColumnsSB == null || valueColumnSB == null) {
        			response.setAttribute("testExecuted", "yes");
        			response.setAttribute("result", result);
        			return new GenericList();
        		}
        		
        		String visibleColumns = visibleColumnsSB.getCharacters();
        		StringTokenizer strToken = new StringTokenizer(visibleColumns, ",");
        		while (strToken.hasMoreTokens()) {
        			String val = strToken.nextToken().trim();
        			columns.add(val);
        		}
        		
        		String valueColumn = valueColumnSB.getCharacters().trim();
        		List rowsVector = rowsSourceBean.getAttributeAsList(DataRow.ROW_TAG);
        		Iterator it = rowsVector.iterator();
        		// all the rows must have an attribute (not null) with key valueColumn
        		while (it.hasNext()) {
        			SourceBean row = (SourceBean) it.next();
        			Object value = row.getAttribute(valueColumn);
        			if (value == null) throw new Exception();
        		}
        		
        		String moduleConfigStr = "";
        		moduleConfigStr += "<CONFIG>";
        		moduleConfigStr += "	<QUERIES/>";
        		moduleConfigStr += "	<COLUMNS>";
        		for (int i = 0; i < columns.size(); i++) {
        			moduleConfigStr += "	<COLUMN name=\"" + columns.get(i).toString() + "\" />";
        		}
        		moduleConfigStr += "	</COLUMNS>";
        		moduleConfigStr += "	<CAPTIONS/>";
        		moduleConfigStr += "	<BUTTONS/>";
        		moduleConfigStr += "</CONFIG>";
        		SourceBean moduleConfig = SourceBean.fromXMLString(moduleConfigStr);
        		response.setAttribute(moduleConfig);
        		
    		} catch (Exception ex) {
    			throw new SourceBeanException("Invalid_XML_Output");
    		}

    	} catch (Exception e) {
    		String errorMessage = e.getMessage();
    		if (errorMessage != null) response.setAttribute("errorMessage", errorMessage);
    		else response.setAttribute("errorMessage", "");
    	}
    	
    	PaginatorIFace paginator = new GenericPaginator();
		List rowsVector = null;
		if (rowsSourceBean != null)
			rowsVector = rowsSourceBean.getAttributeAsList(DataRow.ROW_TAG);

		if (rowsSourceBean != null) {
			for (int i = 0; i < rowsVector.size(); i++)
				paginator.addRow(rowsVector.get(i));
		}
		ListIFace list = new GenericList();
		list.setPaginator(paginator);
		response.setAttribute("testExecuted", "yes");
		// filter the list 
		String valuefilter = (String) request.getAttribute(SpagoBIConstants.VALUE_FILTER);
		if (valuefilter != null) {
			String columnfilter = (String) request
					.getAttribute(SpagoBIConstants.COLUMN_FILTER);
			String typeFilter = (String) request
					.getAttribute(SpagoBIConstants.TYPE_FILTER);
			String typeValueFilter = (String) request
					.getAttribute(SpagoBIConstants.TYPE_VALUE_FILTER);
			list = DelegatedBasicListService.filterList(list, valuefilter, typeValueFilter, columnfilter, 
					typeFilter, getResponseContainer().getErrorHandler());
		}
		return list;
	}

}
