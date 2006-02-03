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
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dbaccess.sql.DataRow;
import it.eng.spago.dispatching.module.list.basic.AbstractBasicListModule;
import it.eng.spago.navigation.LightNavigationManager;
import it.eng.spago.paginator.basic.ListIFace;
import it.eng.spago.paginator.basic.PaginatorIFace;
import it.eng.spago.paginator.basic.impl.GenericList;
import it.eng.spago.paginator.basic.impl.GenericPaginator;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.ModalitiesValue;
import it.eng.spagobi.bo.ScriptDetail;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IModalitiesValueDAO;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.services.commons.DelegatedBasicListService;
import it.eng.spagobi.services.commons.LookupScriptDelegatedBasicListService;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Loads the predefined lookup list of values 
 * 
 * @author sulis
 */
public class ListLookupModalityValuesModule extends AbstractBasicListModule {
	
	
	/**
	 * Class Constructor
	 *
	 */
	public ListLookupModalityValuesModule() {
		super();
	} 
	
	
	
	/**
	 * Gets the list
	 * 
	 * @param request The request SourceBean
	 * @param response The response SourceBean
	 * @return ListIFace 
	 */
	public ListIFace getList(SourceBean request, SourceBean response) throws Exception {
		
		// laod the parameter use
		Integer idModVal = Integer.valueOf((String)request.getAttribute("mod_val_id"));
		IModalitiesValueDAO aModalitiesValueDAO = DAOFactory.getModalitiesValueDAO(); 
		ModalitiesValue modVal = aModalitiesValueDAO.loadModalitiesValueByID(idModVal);
		
		String lookupParameterName = (String) request.getAttribute("LOOKUP_PARAMETER_NAME");
		String actor = (String) request.getAttribute(SpagoBIConstants.ACTOR);
		HashMap paramsMap = new HashMap();
		paramsMap.put("LOOKUP_PARAMETER_NAME", lookupParameterName);
		paramsMap.put(SpagoBIConstants.ACTOR, actor);
		paramsMap.put("mod_val_id", idModVal.toString());
		
		response.setAttribute("PARAMETERS_MAP", paramsMap);
		
		HashMap selectCaptionParams = new HashMap();
		selectCaptionParams.putAll(paramsMap);
		selectCaptionParams.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
		selectCaptionParams.put(SpagoBIConstants.MESSAGEDET, ExecuteBIObjectModule.EXEC_PHASE_RETURN_FROM_LOOKUP);
		selectCaptionParams.put("PAGE", "ValidateExecuteBIObjectPage");

		HashMap backButtonParams = new HashMap();
		backButtonParams.putAll(paramsMap);
		backButtonParams.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
		backButtonParams.put(SpagoBIConstants.MESSAGEDET, ExecuteBIObjectModule.EXEC_PHASE_RETURN_FROM_LOOKUP);
		backButtonParams.put("PAGE", "ValidateExecuteBIObjectPage");
		backButtonParams.put("LOOKUP_VALUE", "");
		
		// define the list of values
		ListIFace list = null;
		
		// get the input type from request (query || script)
		String inputType = modVal.getITypeCd();
		
		// different input type call different delegated class to build the list 
		if (inputType.equalsIgnoreCase(SpagoBIConstants.INPUT_TYPE_QUERY_CODE)) {

			String queryDetXML = modVal.getLovProvider();
			SourceBean queryXML = SourceBean.fromXMLString(queryDetXML);
			String visibleColumns = ((SourceBean) queryXML
					.getAttribute("VISIBLE-COLUMNS")).getCharacters();
			String valueColumn = ((SourceBean) queryXML
					.getAttribute("VALUE-COLUMN")).getCharacters();
			String pool = ((SourceBean) queryXML.getAttribute("CONNECTION")).getCharacters();
			String statement = ((SourceBean) queryXML.getAttribute("STMT")).getCharacters();
			
			Vector columns = findVisibleColumns(visibleColumns);
			
			String moduleConfigStr = "";
			moduleConfigStr += "<CONFIG pool=\"" + pool + "\" rows=\"10\" title=\"" + modVal.getDescription() + "\">";
			moduleConfigStr += "	<QUERIES>";
			moduleConfigStr += "		<SELECT_QUERY statement=\"" + statement + "\" />";
			moduleConfigStr += "	</QUERIES>";
			moduleConfigStr += "</CONFIG>";
			SourceBean moduleConfig = SourceBean.fromXMLString(moduleConfigStr);
			
			SourceBean columnsSB = createColumnsSB(columns);
			moduleConfig.setAttribute(columnsSB);

			SourceBean captionsSB = new SourceBean("CAPTIONS");
			SourceBean selectCaptionSB = createSelectCaption(selectCaptionParams, valueColumn);
			captionsSB.setAttribute(selectCaptionSB);
			moduleConfig.setAttribute(captionsSB);
			
			SourceBean buttonsSB = new SourceBean("BUTTONS");
			SourceBean backButtonSB = createBackButton(backButtonParams);
			buttonsSB.setAttribute(backButtonSB);
			moduleConfig.setAttribute(buttonsSB);

			response.setAttribute(moduleConfig);
			
			list = DelegatedBasicListService.getList(this, request, response);
			
//			String configQuery = modVal.getLovProvider();
//			configQuery = PortletUtilities.cleanString(configQuery);
//			SourceBean configModule = SourceBean.fromXMLString(configQuery);
//			SourceBean moduleConfig = new SourceBean("MODULE_CONFIG");
//			moduleConfig.setAttribute("TITLE", modVal.getDescription());
//			moduleConfig.setAttribute("ORIGINAL_PAGE", request.getAttribute("ORIGINAL_PAGE"));
//			moduleConfig.setAttribute("LOOKUP_PARAMETER_NAME", request.getAttribute("LOOKUP_PARAMETER_NAME"));
//			String actor = (String) request.getAttribute(SpagoBIConstants.ACTOR);
//			moduleConfig.setAttribute(SpagoBIConstants.ACTOR, actor);
//			moduleConfig.setAttribute(configModule);
//			response.setAttribute(moduleConfig);
//			response.setAttribute("mod_val_id", idModVal);
//			list = LookupQueryDelegatedBasicListService.getList(this, request, response, configModule);
			
		} else if(inputType.equalsIgnoreCase(SpagoBIConstants.INPUT_TYPE_SCRIPT_CODE)) {
			
			RequestContainer reqCont = getRequestContainer();
			SessionContainer session = reqCont.getSessionContainer();
			SessionContainer permSess = session.getPermanentContainer();
			IEngUserProfile profile = (IEngUserProfile)permSess.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
			String lov = modVal.getLovProvider();
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
    		moduleConfigStr += "<CONFIG rows=\"10\" title=\"" + modVal.getDescription() + "\">";
    		moduleConfigStr += "	<QUERIES/>";
			moduleConfigStr += "</CONFIG>";
			SourceBean moduleConfig = SourceBean.fromXMLString(moduleConfigStr);
			
			SourceBean columnsSB = createColumnsSB(columns);
			moduleConfig.setAttribute(columnsSB);

			SourceBean captionsSB = new SourceBean("CAPTIONS");
			SourceBean selectCaptionSB = createSelectCaption(selectCaptionParams, valueColumn);
			captionsSB.setAttribute(selectCaptionSB);
			moduleConfig.setAttribute(captionsSB);
			
			SourceBean buttonsSB = new SourceBean("BUTTONS");
			SourceBean backButtonSB = createBackButton(backButtonParams);
			buttonsSB.setAttribute(backButtonSB);
			moduleConfig.setAttribute(buttonsSB);

    		response.setAttribute(moduleConfig);
			
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
    			list = DelegatedBasicListService.filterList(list, valuefilter, request);
    		}
    		
//			list = LookupScriptDelegatedBasicListService.getList(this, request, response, rows);
//			List attrs =  rows.getContainedAttributes();
//			Iterator iter = attrs.iterator();
//			SourceBean configModule = new SourceBean("QUERY");
//			while(iter.hasNext()) {
//				SourceBeanAttribute sba = (SourceBeanAttribute)iter.next();
//				SourceBean sb = (SourceBean)sba.getValue();
//				configModule.setAttribute(sb);
//			}
//			SourceBean moduleConfig = new SourceBean("MODULE_CONFIG");
//			moduleConfig.setAttribute("TITLE", modVal.getDescription());
//			//moduleConfig.setAttribute("ORIGINAL_PAGE", request.getAttribute("ORIGINAL_PAGE"));
//			moduleConfig.setAttribute("LOOKUP_PARAMETER_NAME", request.getAttribute("LOOKUP_PARAMETER_NAME"));
//			String actor = (String)request.getAttribute(SpagoBIConstants.ACTOR);
//			moduleConfig.setAttribute(SpagoBIConstants.ACTOR, actor);
//			moduleConfig.setAttribute(configModule);
//			response.setAttribute(moduleConfig);
//			response.setAttribute("mod_val_id", idModVal);
		}
		
		response.setAttribute(SpagoBIConstants.PUBLISHER_NAME , "LookupPublisher");
		
		return list;
		
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
		String backButtonStr = "<BACK_BUTTON confirm=\"FALSE\" image=\"/img/back.png\" " +
				"label=\"SBIListLookPage.backButton\">";
		Set keys = backButtonParams.keySet();
		Iterator it = keys.iterator();
		while (it.hasNext()) {
			String key = it.next().toString();
			String value = backButtonParams.get(key).toString();
			backButtonStr += "	<PARAMETER name=\"" + key + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + value + "\" />";
		}
		backButtonStr += "</BACK_BUTTON>";
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
	
} 

