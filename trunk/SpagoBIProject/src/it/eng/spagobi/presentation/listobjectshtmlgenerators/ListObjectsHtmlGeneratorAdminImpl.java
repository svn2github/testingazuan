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
package it.eng.spagobi.presentation.listobjectshtmlgenerators;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.RequestContainerPortletAccess;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.navigation.LightNavigationManager;
import it.eng.spago.paginator.basic.ListIFace;
import it.eng.spago.paginator.basic.PaginatorIFace;
import it.eng.spago.paginator.basic.impl.GenericList;
import it.eng.spago.paginator.basic.impl.GenericPaginator;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.services.modules.DetailBIObjectModule;
import it.eng.spagobi.services.modules.ExecuteBIObjectModule;
import it.eng.spagobi.services.modules.TreeObjectsModule;
import it.eng.spagobi.utilities.ObjectsAccessVerifier;
import it.eng.spagobi.utilities.PortletUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Generates the HTML code for the constraction of Business Objects Administration
 * jsp page <code>treeAdminObjects.jsp</code>. 
 * 
 * @author sulis
 */
public class ListObjectsHtmlGeneratorAdminImpl implements IListObjectsHtmlGenerator {
    
	private RenderResponse renderResponse = null;
	private RenderRequest renderRequest = null;
	private HttpServletRequest httpRequest = null;
	protected StringBuffer htmlStream = null;
	private Vector columns = new Vector();
	private int listPage = 1;
	private IEngUserProfile profile = null;
	PortletRequest portReq = null;
	protected RequestContainer _requestContainer = null;
	protected SourceBean _serviceRequest = null;
	
	/**
	 * Starting from the list interface (taken at imput) this method creates the particular
	 * list administration objects. Creates this list taking at input all the particular 
	 * administration object and presenting at output the same list as a table.
	 * Each row of the table represent a singular objects. there are three operations 
	 * for each object: object execution, detail end erasing.
	 * 
	 *  @param list the list interface object
	 *  @param httpReq The request object
	 *  @param listPageStr String utility object for list navigation
	 * 
	 */
	
	public StringBuffer makeList(ListIFace list, HttpServletRequest httpReq, String listPageStr) {
		
		httpRequest = httpReq;
		renderResponse =(RenderResponse)httpRequest.getAttribute("javax.portlet.response");
		renderRequest = (RenderRequest)httpRequest.getAttribute("javax.portlet.request");
		htmlStream = new StringBuffer();
		_requestContainer = RequestContainerPortletAccess.getRequestContainer(httpRequest);
		_serviceRequest = _requestContainer.getServiceRequest();
		RequestContainer requestContainer = RequestContainerPortletAccess.getRequestContainer(httpRequest);
		SessionContainer sessionContainer = requestContainer.getSessionContainer();
		SessionContainer permanentSession = sessionContainer.getPermanentContainer();
        profile = (IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		
        portReq = PortletUtilities.getPortletRequest();
        
		try{
			listPage = Integer.parseInt(listPageStr);
		} catch(Exception e) {
			// ignore, take 1 as default
		}
		
		// filter the list 
		String valuefilter = (String) httpRequest.getParameter(SpagoBIConstants.VALUE_FILTER);
//		if((valuefilter!=null) && !(valuefilter.trim().equals(""))) {
//			list = filterList(list, valuefilter, request);
//		}
		if (valuefilter != null) {
			list = filterList(list, valuefilter);
		}
		
		generateColumns();
		makeColumns();
		makeRows(list);
		makeNavigationButton(list);
        return htmlStream;
	}

	/**
	 * From the <code>ListIFace</code> given as input, filters all the elements
	 * as per user request.
	 * 
	 * @param listAll the input list
	 * @param valuefilter The value of the filter
	 * @return	The filtered output list
	 */
	private ListIFace filterList(ListIFace list, String valuefilter) {
		valuefilter = valuefilter.toUpperCase();
		String columnfilter = (String) httpRequest
				.getParameter(SpagoBIConstants.COLUMN_FILTER);
		if ((columnfilter == null) || (columnfilter.trim().equals(""))) {
			return list;
		}
		String typeFilter = (String) httpRequest
				.getParameter(SpagoBIConstants.TYPE_FILTER);
		if ((typeFilter == null) || (typeFilter.trim().equals(""))) {
			return list;
		}
		PaginatorIFace newPaginator = new GenericPaginator();
		newPaginator.setPageSize(list.getPaginator().getPageSize());
		SourceBean allrowsSB = list.getPaginator().getAll();
		List rows = allrowsSB.getAttributeAsList("ROW");
		Iterator iterRow = rows.iterator();
		while (iterRow.hasNext()) {
			SourceBean row = (SourceBean) iterRow.next();
			String value = (String) row.getAttribute(columnfilter);
			if (value == null)
				value = "";
			value = value.toUpperCase();
			if (typeFilter.equalsIgnoreCase(SpagoBIConstants.START_FILTER)) {
				if (value.trim().startsWith(valuefilter))
					newPaginator.addRow(row);
			} else if (typeFilter.equalsIgnoreCase(SpagoBIConstants.END_FILTER)) {
				if (value.trim().endsWith(valuefilter))
					newPaginator.addRow(row);
			} else if (typeFilter
					.equalsIgnoreCase(SpagoBIConstants.CONTAIN_FILTER)) {
				if (value.indexOf(valuefilter) != -1)
					newPaginator.addRow(row);
			} else if (typeFilter
					.equalsIgnoreCase(SpagoBIConstants.EQUAL_FILTER)) {
				if (value.equals(valuefilter)
						|| value.trim().equals(valuefilter))
					newPaginator.addRow(row);
			}
		}
		ListIFace newList = new GenericList();
		newList.setPaginator(newPaginator);
		return newList;
	}
	
	/**
	 * Recovers all information to build table output columns
	 *
	 */
	private void generateColumns() {
		try {
			SourceBean columLabel = new SourceBean("COLUMN");
			columLabel.setAttribute("LABEL", PortletUtilities.getMessage("SBISet.objects.columnLabel", "messages") );
			columLabel.setAttribute("NAME", "Label");
			
			SourceBean columName = new SourceBean("COLUMN");
			columName.setAttribute("LABEL", PortletUtilities.getMessage("SBISet.objects.columnName", "messages") );
			columName.setAttribute("NAME", "Name");
			
			SourceBean columDescription = new SourceBean("COLUMN");
			columDescription.setAttribute("LABEL", PortletUtilities.getMessage("SBISet.objects.columnDescr", "messages") );
			columDescription.setAttribute("NAME", "Description");
			
			SourceBean columType = new SourceBean("COLUMN");
			columType.setAttribute("LABEL", PortletUtilities.getMessage("SBISet.objects.columnType", "messages"));
			columType.setAttribute("NAME", "Type");
			
			SourceBean columState = new SourceBean("COLUMN");
			columState.setAttribute("LABEL", PortletUtilities.getMessage("SBISet.objects.columnState", "messages"));
			columState.setAttribute("NAME", "State");
			columns.add(columLabel);
			columns.add(columName);
			columns.add(columDescription);
			columns.add(columType);
			columns.add(columState);
		} catch (Exception e) {
			// TODO trace and throw exception
			SpagoBITracer.major("", "ListObjectsHtmlGeneratorAdminImpl", "generateColumns", "cannot generate columns");
			return;
		}
	}
	
		/**
		 * Generates the HTML code for all columns
		 *
		 */
	protected void makeColumns()  {
		htmlStream.append("<TABLE width='100%'>\n");
		htmlStream.append("	<TR>\n");
		for(int i=0; i<columns.size(); i++) {
			String nameColumn = (String) ((SourceBean) columns.elementAt(i)).getAttribute("LABEL");
			htmlStream.append("<TD class='portlet-section-header' valign='center' align=left >" + nameColumn + "</TD>\n");
		} 
		int numCaption = 3;
		for(int i=0; i<numCaption; i++) {
			htmlStream.append("<TD class='portlet-section-header' align='center'>&nbsp;</TD>\n");
		} 
		htmlStream.append("</TR>\n");
	} 
	
	
	/**
	 * Recovers information and generates html code for table rows 
	 * @param list The list interface object
	 */
	
	protected void makeRows(ListIFace list)	{

        // js function for item action confirm
        String confirmCaption = PortletUtilities.getMessage("SBISet.objects.confirmCaption", "messages");
        htmlStream.append(" <script>\n");
        htmlStream.append("    function actionConfirm(message, url){\n");
        htmlStream.append("        if (confirm('" + confirmCaption + " ' + message + '?')){\n");
        htmlStream.append("            location.href = url;\n");
        htmlStream.append("        }\n");
        htmlStream.append("    }\n");
        htmlStream.append(" </script>\n");       
 
		SourceBean page = list.getPagedList(listPage);
		List rows = page.getAttributeAsList("ROWS.ROW");
        boolean alternate = false;
        String rowClass;
		for(int i=0; i<rows.size(); i++) {
			SourceBean row = (SourceBean) rows.get(i);
            
            rowClass = (alternate) ? "portlet-section-alternate" : "portlet-section-body";
            alternate = !alternate;
            
            htmlStream.append(" <tr class='portlet-font'>\n");            
			for (int j=0; j<columns.size(); j++) {
				String nameColumn = (String) ((SourceBean)columns.elementAt(j)).getAttribute("NAME");
				Object fieldObject = row.getAttribute(nameColumn);
				String field = null;
				if (fieldObject != null)
					field = fieldObject.toString();
				else
					field = "&nbsp;";
				htmlStream.append(" <td class='" + rowClass + "' valign='top' >" + field + "</td>\n");
			}
			String path = (String)row.getAttribute("Path");
			// add caption for execution
			String state = (String)row.getAttribute("State");
			if(ObjectsAccessVerifier.canExec(state, path, profile)) {
				htmlStream.append(" <td width='40px' class='" + rowClass + "' valign='top'>\n");
				createExecuteObjectLink(path, htmlStream);
				htmlStream.append(" </td>\n");
			} else {
				htmlStream.append(" <td width='40px' class='" + rowClass + "' valign='top'>&nbsp;</td>\n");
			}
			// add caption for detail
			htmlStream.append(" <td width='40px' class='" + rowClass + "' valign='top'>\n");
			createDetailObjectLink(path, htmlStream);
			htmlStream.append(" </td>\n");
			// add caption for erase
			htmlStream.append(" <td width='40px' class='" + rowClass + "' valign='top'>\n");
			createEraseObjectLink(path, htmlStream);
			htmlStream.append(" </td>\n");
			htmlStream.append(" </tr>\n");
		}
		htmlStream.append(" </table>\n");
	} 
	
	/**
	 * Creates the button in order to allow Object detail information recovering.
	 * It adds the html code to the <code>htmlString</code> received as input. 
	 * 
	 * @param path The String representing the object's path
	 * @param htmlStream The String Buffer  to whom code is added
	 */
	private void createDetailObjectLink(String path, StringBuffer htmlStream) {
		PortletURL addUrl = renderResponse.createActionURL();
		addUrl.setParameter(ObjectsTreeConstants.PAGE, DetailBIObjectModule.MODULE_PAGE);
		addUrl.setParameter(ObjectsTreeConstants.MESSAGE_DETAIL, ObjectsTreeConstants.DETAIL_SELECT);
		addUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.ADMIN_ACTOR);
		addUrl.setParameter(ObjectsTreeConstants.PATH, path);
		String capTitle = PortletUtilities.getMessage("SBISet.objects.captionDetail", "messages");
		htmlStream.append("<a title=\""+capTitle+"\" class=\"linkOperation\" href=\""+addUrl.toString()+"\">\n");
        htmlStream.append("<img src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/detail.gif" )+"' /></a>\n");
	}
	
	/**
	 * Creates the button in order to allow Object execution .
	 * It adds the html code to the <code>htmlString</code> received as input. 
	 * 
	 * @param path The String representing the object's path
	 * @param htmlStream The String Buffer  to whom code is added
	 */
	private void createExecuteObjectLink(String path, StringBuffer htmlStream) {
		PortletURL execUrl = renderResponse.createActionURL();
		execUrl.setParameter(ObjectsTreeConstants.PAGE, ExecuteBIObjectModule.MODULE_PAGE);
		execUrl.setParameter(ObjectsTreeConstants.PATH, path);
		execUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.ADMIN_ACTOR);
		execUrl.setParameter(SpagoBIConstants.MESSAGEDET, ObjectsTreeConstants.EXEC_PHASE_CREATE_PAGE);
		String capTitle = PortletUtilities.getMessage("SBISet.objects.captionExecute", "messages");
		htmlStream.append("<a title=\""+capTitle+"\" class=\"linkOperation\" href=\""+execUrl.toString()+"\">\n");
        htmlStream.append("<img src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/execObject.gif" )+"' /></a>\n");
	}
	/**
	 * Creates the button in order to allow Object erasing .
	 * It adds the html code to the <code>htmlString</code> received as input. 
	 * 
	 * @param path The String representing the object's path
	 * @param htmlStream The String Buffer  to whom code is added
	 */
	private void createEraseObjectLink(String path, StringBuffer htmlStream) {
		PortletURL addUrl = renderResponse.createActionURL();
		addUrl.setParameter(ObjectsTreeConstants.PAGE, DetailBIObjectModule.MODULE_PAGE);
		addUrl.setParameter(ObjectsTreeConstants.MESSAGE_DETAIL, ObjectsTreeConstants.DETAIL_DEL);
		addUrl.setParameter(ObjectsTreeConstants.PATH, path);
		addUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.ADMIN_ACTOR);
		String capTitle = PortletUtilities.getMessage("SBISet.objects.captionErase", "messages");
        htmlStream.append("<a title=\""+capTitle+"\" class=\"linkOperation\" href='javascript:actionConfirm(\"" + capTitle + "\", \"" + addUrl.toString() + "\");'>\n");
        htmlStream.append("<img src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/erase.gif" )+"' /></a>\n");
	}
	
	
	/**
	 * Makes all navigation buttons for the list. The table presenting the objects
	 * list has a maximun number of rows. Once passed this number, this method 
	 * splits the table into pages, with arrow buttons to navigate between pages and
	 * page number information, presented as "Current Page Number " of "Total Pages Number" 
	 * 
	 * @param list	The list interface object
	 */
	protected void makeNavigationButton(ListIFace list) {
		
		int pagesNumber = list.getPaginator().pages();
		int prevPage = listPage - 1;
		if (prevPage < 1)
			prevPage = 1;
		int nextPage = listPage + 1;
		if (nextPage > pagesNumber)
			nextPage = pagesNumber;
					
		htmlStream.append(" <TABLE CELLPADDING=0 CELLSPACING=0  WIDTH='100%' BORDER=0>\n");
		htmlStream.append("	<TR>\n");
		htmlStream.append("		<TD class='portlet-section-footer' valign='center' align='left' width='14'>\n");
		
		PortletURL prevUrl = renderResponse.createActionURL();
		prevUrl.setParameter(ObjectsTreeConstants.PAGE, TreeObjectsModule.MODULE_PAGE);
		prevUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.ADMIN_ACTOR);
		prevUrl.setParameter(SpagoBIConstants.OBJECTS_VIEW, SpagoBIConstants.VIEW_OBJECTS_AS_LIST);
		prevUrl.setParameter(SpagoBIConstants.LIST_PAGE, String.valueOf(prevPage));
		
		PortletURL nextUrl = renderResponse.createActionURL();
		nextUrl.setParameter(ObjectsTreeConstants.PAGE, TreeObjectsModule.MODULE_PAGE);
		nextUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.ADMIN_ACTOR);
		nextUrl.setParameter(SpagoBIConstants.OBJECTS_VIEW, SpagoBIConstants.VIEW_OBJECTS_AS_LIST);
		nextUrl.setParameter(SpagoBIConstants.LIST_PAGE, String.valueOf(nextPage));
		
		PortletURL allUrl = renderResponse.createActionURL();
		allUrl.setParameter(ObjectsTreeConstants.PAGE, TreeObjectsModule.MODULE_PAGE);
		allUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.ADMIN_ACTOR);
		allUrl.setParameter(SpagoBIConstants.OBJECTS_VIEW, SpagoBIConstants.VIEW_OBJECTS_AS_LIST);
		
		PortletURL filterURL = renderResponse.createActionURL();
		filterURL.setParameter(ObjectsTreeConstants.PAGE, TreeObjectsModule.MODULE_PAGE);
		filterURL.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.ADMIN_ACTOR);
		filterURL.setParameter(SpagoBIConstants.OBJECTS_VIEW, SpagoBIConstants.VIEW_OBJECTS_AS_LIST);
		
		// if the navigator is disabled entering the list, the navigator remains disabled 
		// untill the user leaves the list
		String lightNavigatorDisabled = (String) _serviceRequest.getAttribute(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED);
		if (lightNavigatorDisabled != null) { 
			allUrl.setParameter(
					LightNavigationManager.LIGHT_NAVIGATOR_DISABLED,
					lightNavigatorDisabled);
			filterURL.setParameter(
					LightNavigationManager.LIGHT_NAVIGATOR_DISABLED,
					lightNavigatorDisabled);
			nextUrl.setParameter(
					LightNavigationManager.LIGHT_NAVIGATOR_DISABLED,
					lightNavigatorDisabled);
			prevUrl.setParameter(
					LightNavigationManager.LIGHT_NAVIGATOR_DISABLED,
					lightNavigatorDisabled);
		}
		
		String valueFilter = (String) httpRequest
				.getParameter(SpagoBIConstants.VALUE_FILTER);
		String columnFilter = (String) httpRequest
				.getParameter(SpagoBIConstants.COLUMN_FILTER);
		String typeFilter = (String) httpRequest
				.getParameter(SpagoBIConstants.TYPE_FILTER);

		if (valueFilter != null && columnFilter != null && typeFilter != null) {
			// the filter form was submitted
			nextUrl.setParameter(SpagoBIConstants.VALUE_FILTER, valueFilter);
			nextUrl.setParameter(SpagoBIConstants.COLUMN_FILTER, columnFilter);
			nextUrl.setParameter(SpagoBIConstants.TYPE_FILTER, typeFilter);
			prevUrl.setParameter(SpagoBIConstants.VALUE_FILTER, valueFilter);
			prevUrl.setParameter(SpagoBIConstants.COLUMN_FILTER, columnFilter);
			prevUrl.setParameter(SpagoBIConstants.TYPE_FILTER, typeFilter);
		} else {
			// the filter form was not submitted
			valueFilter = "";
			columnFilter = "";
			typeFilter = "";
		}
		
		if (listPage != 1) {	
			htmlStream.append("		<A href=\""+prevUrl.toString()+"\"><IMG src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/prevPage.gif")+"' ALIGN=RIGHT border=0></a>\n"); 
		} else {
			htmlStream.append("		<IMG src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/prevPage.gif")+"' ALIGN=RIGHT border=0>\n");
		}		
		htmlStream.append("		</TD>\n");
				
		// create center blank cell
		//htmlStream.append("		<TD class='portlet-section-footer'>&nbsp;</TD>\n");
		
        // visualize page numbers
		String pageLabel = PortletUtilities.getMessage("ListTag.pageLable", "messages");
		String pageOfLabel = PortletUtilities.getMessage("ListTag.pageOfLable", "messages");
		htmlStream.append("						<TD class='portlet-section-footer' align='center'>\n");
		htmlStream.append("							<font class='aindice'>&nbsp;"+pageLabel+ " " + listPage + " " +pageOfLabel+ " " + pagesNumber + "&nbsp;</font>\n");
		htmlStream.append("						    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n");
		
		String label = PortletUtilities.getMessage("SBIListLookPage.labelFilter", "messages");
		String labelStart = PortletUtilities.getMessage("SBIListLookPage.startWith", "messages");;
		String labelEnd = PortletUtilities.getMessage("SBIListLookPage.endWith", "messages");;
		String labelContain = PortletUtilities.getMessage("SBIListLookPage.contains", "messages");;
		String labelEqual = PortletUtilities.getMessage("SBIListLookPage.isEquals", "messages");;
		String labelFilter = PortletUtilities.getMessage("SBIListLookPage.filter", "messages");
		String labelAll = PortletUtilities.getMessage("SBIListLookPage.all", "messages");
		
//		String formSuffix = "_";
//		if (_actionName != null) formSuffix += _actionName;
//		else formSuffix += _moduleName;
		
//		String formId = "formFilter" + formSuffix;
		
		String formId = "formFilter";
		
		htmlStream.append("						    <br/><br/>\n");
		htmlStream.append("						    <form action='"+filterURL+"' id='" + formId +"' method='post'>\n");
		htmlStream.append("						    "+label+"\n");
		htmlStream.append("						    <select name='" + SpagoBIConstants.COLUMN_FILTER + "'>\n");
		
		for (int i = 0; i < columns.size(); i++) {
			String nameColumn = (String) ((SourceBean) columns.elementAt(i)).getAttribute("NAME");
			String labelColumnCode = (String) ((SourceBean) columns.elementAt(i)).getAttribute("LABEL");
			String labelColumn = PortletUtilities.getMessage(labelColumnCode, "messages");
			String selected = "";
			if (nameColumn.equalsIgnoreCase(columnFilter))
				selected = " selected='selected' "; 
			htmlStream.append("						    	<option value='"+nameColumn+"' "+selected+" >"+labelColumn+"</option>\n");
		}
		String selected = "";
		htmlStream.append("						    </select>\n");
		htmlStream.append("						    <select name='" + SpagoBIConstants.TYPE_FILTER + "'>\n");
		if (typeFilter.equalsIgnoreCase(SpagoBIConstants.START_FILTER))
			selected = " selected='selected' ";
		else selected = "";
		htmlStream.append("						    	<option value='"+SpagoBIConstants.START_FILTER+"' "+selected+" >"+labelStart+"</option>\n");
		if (typeFilter.equalsIgnoreCase(SpagoBIConstants.END_FILTER))
			selected = " selected='selected' ";
		else selected = "";
		htmlStream.append("						    	<option value='"+SpagoBIConstants.END_FILTER+"' "+selected+" >"+labelEnd+"</option>\n");
		if (typeFilter.equalsIgnoreCase(SpagoBIConstants.EQUAL_FILTER))
			selected = " selected='selected' ";
		else selected = "";
		htmlStream.append("						    	<option value='"+SpagoBIConstants.EQUAL_FILTER+"' "+selected+" >"+labelEqual+"</option>\n");
		if (typeFilter.equalsIgnoreCase(SpagoBIConstants.CONTAIN_FILTER))
			selected = " selected='selected' ";
		else selected = "";
		htmlStream.append("						    	<option value='"+SpagoBIConstants.CONTAIN_FILTER+"' "+selected+" >"+labelContain+"</option>\n");
		htmlStream.append("						    </select>\n");
		htmlStream.append("						    <input type='text' name='" + SpagoBIConstants.VALUE_FILTER + "' size='10' value='"+valueFilter+"' /> \n");
		htmlStream.append("						    <a href='javascript:document.getElementById(\"" + formId +"\").submit()'>"+labelFilter+"</a> \n");
		htmlStream.append(" <a href='"+allUrl.toString()+"'>"+labelAll+"</a> \n");
		htmlStream.append("						    </form> \n");
		
		// create link for next page
		htmlStream.append("		<TD class='portlet-section-footer' valign='center' align='right' width='14'>\n");			
		if(listPage != pagesNumber) {	
			htmlStream.append("		<A href=\""+nextUrl.toString()+"\"><IMG src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/nextPage.gif")+"' ALIGN=RIGHT border=0></a>\n"); 
		} else {
			htmlStream.append("		<IMG src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/nextPage.gif")+"' ALIGN=RIGHT border=0>\n");
		}		
		htmlStream.append("		</TD>\n");
		htmlStream.append("	</TR>\n");
		htmlStream.append("</TABLE>\n");
	} 
		
	
}
