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
import it.eng.spagobi.bo.lov.FixedListDetail;
import it.eng.spagobi.bo.lov.ILovDetail;
import it.eng.spagobi.bo.lov.JavaClassDetail;
import it.eng.spagobi.bo.lov.LovDetailFactory;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.services.commons.DelegatedBasicListService;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Tests the fixed-list and produces the list as output. 
 * 
 * @author Gioia
 */

public class ListTestFixedListModule extends AbstractBasicListModule {

	public ListTestFixedListModule() {
		super();
	}
	
	public ListIFace getList(SourceBean request, SourceBean response)
			throws Exception {
		
		if (isAttributeValorized(request, "lovProviderModified")) {
			handleLovProviderModification(request, response);
		}
				
		String lovProvider = getModalitiesValue().getLovProvider();
		ILovDetail lovDetail = LovDetailFactory.getLovFromXML(lovProvider);
				
		SourceBean resultSB = null;
    	
		try {
			
			String result = lovDetail.getLovResult(getUserProfile());
    		    		
    		try {
        		resultSB = SourceBean.fromXMLString(result);
        		        		
        		// if the script result has not the visible-columns or value-column definition
        		// returns an empty list and puts in response the lov result as a string
        		if (getVisibleColumns(resultSB) == null || getValueColumn(resultSB) == null) {
        			response.setAttribute("testExecuted", "yes");
        			response.setAttribute("result", result);
        			return new GenericList();
        		}
        		        		
        		if(keyColumnContainsNullValues(resultSB)) {
        			throw new Exception();
        		}
        		        		
        		response.setAttribute(getListModuleConfiguration(resultSB));
        		
    		} catch (Exception ex) {
    			throw new SourceBeanException("Invalid_XML_Output");
    		}

    	} catch (Exception e) {
    		String errorMessage = e.getMessage();
    		if (errorMessage != null) response.setAttribute("errorMessage", errorMessage);
    		else response.setAttribute("errorMessage", "");
    	}
    	
    	    	
		ListIFace list = new GenericList();
		list.setPaginator(getPaginator(resultSB));
		list = getFilteredList(request, list);
		
		response.setAttribute("testExecuted", "yes");
		
		return list;
	}
	
	
	
	private String getAttributeAsString(SourceBean request, String attributeName) {
		return (String) request.getAttribute(attributeName);
	}
	
	private boolean isAttributeValorized(SourceBean request, String attributeName) {
		String lovProviderModified = getAttributeAsString(request, attributeName);
		return (lovProviderModified != null && !lovProviderModified.trim().equals(""));
	}
	
	private void handleLovProviderModification(SourceBean request, SourceBean response) throws SourceBeanException {
		response.setAttribute("lovProviderModified", getAttributeAsString(request, "lovProviderModified"));
		HashMap map = new HashMap();
		map.put("lovProviderModified", getAttributeAsString(request, "lovProviderModified"));
		response.setAttribute("PARAMETERS_MAP", map);
	}
	
	private SessionContainer getSession() {
		SessionContainer session = null;
		RequestContainer requestContainer = getRequestContainer();
		session = requestContainer.getSessionContainer();
		return session;
	}
	
	private ModalitiesValue getModalitiesValue() {
		ModalitiesValue modVal = null;		
		modVal = (ModalitiesValue) getSession().getAttribute(SpagoBIConstants.MODALITY_VALUE_OBJECT);
		return modVal;
	}
	
	private IEngUserProfile getUserProfile() {
		return (IEngUserProfile) getSession().getPermanentContainer().getAttribute(IEngUserProfile.ENG_USER_PROFILE);
	}
	
	private SourceBean getVisibleColumns(SourceBean resultSB) {
		return  (SourceBean) resultSB.getAttribute("VISIBLE-COLUMNS");
	}
	
	private SourceBean getValueColumn(SourceBean resultSB) {
		return  (SourceBean) resultSB.getAttribute("VALUE-COLUMN");
	}
	
	private Vector getVisibleColumnsAsVector(SourceBean resultSB) {
		Vector columns = new Vector();
		String visibleColumns = getVisibleColumns(resultSB).getCharacters();
		StringTokenizer strToken = new StringTokenizer(visibleColumns, ",");
		while (strToken.hasMoreTokens()) {
			String val = strToken.nextToken().trim();
			columns.add(val);
		}
		return columns;
	}
	
	private boolean keyColumnContainsNullValues(SourceBean resultSB) {
		String valueColumn = getValueColumn(resultSB).getCharacters().trim();
		List rowsVector = resultSB.getAttributeAsList(DataRow.ROW_TAG);
		Iterator it = rowsVector.iterator();
		
		while (it.hasNext()) {
			SourceBean row = (SourceBean) it.next();
			Object value = row.getAttribute(valueColumn);
			if (value == null) return true;;
		}
		
		return false;
	}
	
	private SourceBean getListModuleConfiguration(SourceBean resultSB) throws SourceBeanException {
		SourceBean listModuleConfiguration = null;
		
		Vector columns = getVisibleColumnsAsVector(resultSB);
		
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
		listModuleConfiguration = SourceBean.fromXMLString(moduleConfigStr);
		
		return listModuleConfiguration;
	}
	
	private PaginatorIFace getPaginator(SourceBean resultSB) {
		
		PaginatorIFace paginator = new GenericPaginator();
				
		if (resultSB != null) {
			List rowsVector = resultSB.getAttributeAsList(DataRow.ROW_TAG);
			for (int i = 0; i < rowsVector.size(); i++)
				paginator.addRow(rowsVector.get(i));
		}		
		
		return paginator;
	}
	
	private ListIFace getFilteredList(SourceBean request, ListIFace list) {
		ListIFace filteredList = list;
		
		String valuefilter = (String) request.getAttribute(SpagoBIConstants.VALUE_FILTER);
		if (valuefilter != null) {
			String columnfilter = (String) request
					.getAttribute(SpagoBIConstants.COLUMN_FILTER);
			String typeFilter = (String) request
					.getAttribute(SpagoBIConstants.TYPE_FILTER);
			String typeValueFilter = (String) request
					.getAttribute(SpagoBIConstants.TYPE_VALUE_FILTER);
			filteredList = DelegatedBasicListService.filterList(list, valuefilter, typeValueFilter, columnfilter, 
							typeFilter, getResponseContainer().getErrorHandler());
		}
		
		return filteredList;
	}

}
