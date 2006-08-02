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
package it.eng.spagobi.presentation.tags;

import it.eng.spago.base.Constants;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.RequestContainerPortletAccess;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.ResponseContainerPortletAccess;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.navigation.LightNavigationManager;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spago.util.ContextScooping;
import it.eng.spago.util.JavaScript;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.services.modules.ExecuteBIObjectModule;
import it.eng.spagobi.utilities.PortletUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Builds and presents all objects list for all admin 
 * SpagoBI's list modules. Once a list module has been executed, 
 * the lookup list tag builds all the correspondent jsp page and gives the results
 * 
 * @author sulis
 */

public class LookupListTag extends TagSupport {
	
	protected String _actionName = null;
	protected String _moduleName = null;
	protected String _serviceName = null;
	protected SourceBean _content = null;
	protected SourceBean _layout = null;
	protected String _providerURL = null;
	protected RequestContainer _requestContainer = null;
	protected SourceBean _serviceRequest = null;
	protected ResponseContainer _responseContainer = null;
	protected SourceBean _serviceResponse = null;
	protected StringBuffer _htmlStream = null;
	protected SourceBean _moduleConfig = null;
	protected HashMap _paramsMap = null; 
    protected String labelLinkSaltoPagina;
    
    private HttpServletRequest httpRequest = null;
    private RenderResponse renderResponse = null;
    private RenderRequest renderRequest = null;
    private HashMap _providerUrlMap = new HashMap();
    PortletRequest portReq = null;
    
    public LookupListTag() {
    	labelLinkSaltoPagina = "Vai alla Pagina";	
    }

    
    
    /**
	 * @see it.eng.spagobi.presentation.tags.ListTag#doStartTag()
	 * 
	 */
	public int doStartTag() throws JspException {
		SpagoBITracer.info("Admintools", "LookupListTag", "doStartTag", "method invoked");
		httpRequest = (HttpServletRequest) pageContext.getRequest();
		renderResponse =(RenderResponse)httpRequest.getAttribute("javax.portlet.response");
		renderRequest =(RenderRequest)httpRequest.getAttribute("javax.portlet.request");
		_requestContainer = RequestContainerPortletAccess.getRequestContainer(httpRequest);
		portReq = PortletUtilities.getPortletRequest();
		_serviceRequest = _requestContainer.getServiceRequest();
		_responseContainer = ResponseContainerPortletAccess.getResponseContainer(httpRequest);
		_serviceResponse = _responseContainer.getServiceResponse();
		ConfigSingleton configure = ConfigSingleton.getInstance();
		if (_actionName != null) {
			_serviceName = _actionName;
			_content = _serviceResponse;
			SourceBean actionBean =
				(SourceBean) configure.getFilteredSourceBeanAttribute("ACTIONS.ACTION", "NAME", _actionName);
			_layout = (SourceBean) actionBean.getAttribute("CONFIG");
			_providerURL = "ACTION_NAME=" + _actionName + "&";
			_providerUrlMap.put("ACTION_NAME", _actionName);
		} // if (_actionName != null)
		else if (_moduleName != null) {
			_serviceName = _moduleName;
			_content = (SourceBean) _serviceResponse.getAttribute(_moduleName);
//			SourceBean moduleBean =
//				(SourceBean) configure.getFilteredSourceBeanAttribute("MODULES.MODULE", "NAME", _moduleName);
			//_layout = (SourceBean) moduleBean.getAttribute("CONFIG");
			String pageName = (String) _serviceRequest.getAttribute("PAGE");
			_providerURL = "PAGE=" + pageName + "&MODULE=" + _moduleName + "&";
			_providerUrlMap.put("PAGE", pageName);
			_providerUrlMap.put("MODULE", _moduleName);
		} // if (_moduleName != null)
		else {
			SpagoBITracer.critical("Admintools", "LookupListTag", "doStartTag", "service name not specified");
			throw new JspException("Business name not specified !");
		} // if (_content == null)
		if (_content == null) {
			SpagoBITracer.warning("Admintools", "LookupListTag", "doStartTag", "list content null");
			return SKIP_BODY;
		} // if (_content == null)
		
		//*************************
		_moduleConfig = (SourceBean) _serviceResponse.getAttribute(_moduleName.toUpperCase()+".MODULE_CONFIG");
		String actor = (String) _moduleConfig.getAttribute(SpagoBIConstants.ACTOR);
		String parameterName = (String) _moduleConfig.getAttribute("LOOKUP_PARAMETER_NAME");
		String originalPage = (String) _moduleConfig.getAttribute("ORIGINAL_PAGE");
		Integer modId = (Integer) _serviceResponse.getAttribute(_moduleName + ".mod_val_id");
		_paramsMap = new HashMap();
		_paramsMap.putAll(_providerUrlMap);
		_paramsMap.put("mod_val_id", modId.toString());
		_paramsMap.put("LOOKUP_PARAMETER_NAME", parameterName);
		_paramsMap.put("ORIGINAL_PAGE", originalPage);
		_paramsMap.put(SpagoBIConstants.ACTOR, actor);
		String lightNavigatorDisabled = (String) _serviceRequest.getAttribute(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED);
		if (lightNavigatorDisabled != null) {
			_paramsMap.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, lightNavigatorDisabled);
		}
		//*************************
		
		_htmlStream = new StringBuffer();
		makeForm();
		try {
			pageContext.getOut().print(_htmlStream);
		} // try
		catch (Exception ex) {
			SpagoBITracer.critical("Admintools", "LookupListTag", "doStartTag", "Impossible to send the stream");
			throw new JspException("Impossible to send the stream");
		} // catch (Exception ex)
		return SKIP_BODY;
	} // public int doStartTag() throws JspException

	
	
	
	
	 
    /**
	 * @see it.eng.spagobi.presentation.tags.ListTag#makeForm()
	 * 
	 */
	
	protected void makeForm() throws JspException {

		String title = (String) _moduleConfig.getAttribute("TITLE");
		if (title == null){
			title = "Choose a value from list";
		}
		
		_htmlStream.append(" <table class='header-table-portlet-section'>\n");		
		_htmlStream.append("	<tr class='header-row-portlet-section' style='vertical-align:middle;padding-left:5px;'>\n");
		_htmlStream.append("			<td class='header-title-column-portlet-section'>" + title + "</td>\n");
		_htmlStream.append("			<td class=\"header-empty-column-portlet-section\">&nbsp;</td>\n");
		_htmlStream.append(				makeButton() + "\n");
		_htmlStream.append("	</tr>\n");
		_htmlStream.append(" </table>\n");
		
		makeRows();
		makeNavigationButton();

	} // public void makeForm()

	 
    /**
	 * @see it.eng.spagobi.presentation.tags.ListTag#makeRows()
	 * 
	 */
	protected void makeRows() throws JspException 
	{
		_htmlStream.append("<TABLE width='100%'>\n");
		_htmlStream.append("	<TR>\n");
		
		String visibleColumns = ((SourceBean) _moduleConfig.getAttribute("QUERY.VISIBLE-COLUMNS")).getCharacters();
		String valueColumn = ((SourceBean) _moduleConfig.getAttribute("QUERY.VALUE-COLUMN")).getCharacters();
		
		StringTokenizer strToken = new StringTokenizer(visibleColumns, ",");
		List _columns = new ArrayList();
		while(strToken.hasMoreTokens()) {
			String val = strToken.nextToken().trim();
			_columns.add(val);
		}
		
		// Generate TD for caption
		_htmlStream.append("<TD class='portlet-section-header' align='center'>&nbsp;</TD>\n");
		for (int i = 0; i < _columns.size(); i++) {
			_columns.get(i);
			_htmlStream.append("<TD class='portlet-section-header' valign='center' align=left >" + _columns.get(i) + "</TD>\n");
		} 
		_htmlStream.append("</TR>\n");
		
		List rows = _content.getAttributeAsList("PAGED_LIST.ROWS.ROW");
	    
		for (int i = 0; i < rows.size(); i++) {
			
			SourceBean row = (SourceBean) rows.get(i);
			_htmlStream.append(" <tr class='portlet-section-body'>\n");
			
			/*
			String img = (String)captionSB.getAttribute("image");
			String label = (String)captionSB.getAttribute("label");
			*/
			String img = "/img/button_ok.png";
			String label = PortletUtilities.getMessage("SBIListLookPage.selectButton", "messages");
			
			PortletURL lookupBackURL = createUrl(_paramsMap);
			lookupBackURL.setParameter(SpagoBIConstants.MESSAGEDET, ObjectsTreeConstants.EXEC_PHASE_RETURN_FROM_LOOKUP);
			lookupBackURL.setParameter("LOOKUP_VALUE", (String)row.getAttribute(valueColumn));
			
			_htmlStream.append(" <td width='20'>\n");
			_htmlStream.append("     <a href='"+lookupBackURL.toString()+"'>\n");
			_htmlStream.append("			<img title='" + label + "' alt='"+label+"' src='"+renderResponse.encodeURL(renderRequest.getContextPath() + img)+"' />\n");
			_htmlStream.append("     </a>\n");
			_htmlStream.append(" </td>\n");

			for (int j = 0; j < _columns.size(); j++) {
				String nameColumn =  (String)_columns.get(j);
				Object fieldObject = row.getAttribute(nameColumn);
				String field = null;
				if (fieldObject != null)
					field = fieldObject.toString();
				else
					field = "&nbsp;";
				_htmlStream.append(" <td class='portlet-font' valign='top' >" + field + "</td>\n");
			} 
			_htmlStream.append(" </tr>\n");
		}
		_htmlStream.append(" </table>\n");
	} 
	 
    /**
	 * @see it.eng.spagobi.presentation.tags.ListTag#makeNavigationButton()
	 * 
	 */

	protected void makeNavigationButton() throws JspException {
		
		String pageNumberString = (String) _content.getAttribute("PAGED_LIST.PAGE_NUMBER");
		int pageNumber = 1;
		try {
			pageNumber = Integer.parseInt(pageNumberString);
		} 
		catch (NumberFormatException ex) {
			TracerSingleton.log(
				Constants.NOME_MODULO,
				TracerSingleton.WARNING,
				"LookupListTag::makeNavigationButton:: PAGE_NUMBER nullo");
		} 
		String pagesNumberString = (String) _content.getAttribute("PAGED_LIST.PAGES_NUMBER");
		int pagesNumber = 1;
		try {
			pagesNumber = Integer.parseInt(pagesNumberString);
		} 
		catch (NumberFormatException ex) {
			TracerSingleton.log(
				Constants.NOME_MODULO,
				TracerSingleton.WARNING,
				"LookupListTag::makeNavigationButton:: PAGES_NUMBER nullo");
		} 

		int prevPage = pageNumber - 1;
		if (prevPage < 1)
			prevPage = 1;
		int nextPage = pageNumber + 1;
		if (nextPage > pagesNumber)
			nextPage = pagesNumber;
		
		String visibleColStr = ((SourceBean) _moduleConfig.getAttribute("QUERY.VISIBLE-COLUMNS")).getCharacters();
		String[] visibleCols = visibleColStr.split(",");
		for (int i = 0; i < visibleCols.length; i++) visibleCols[i] = visibleCols[i].trim();
		
		// add the parameter for the provider
		//paramsMap.putAll(_providerUrlMap);
				
		_htmlStream.append(" <TABLE CELLPADDING=0 CELLSPACING=0  WIDTH='100%' BORDER=0>\n");
		_htmlStream.append("	<TR class='portlet-section-header'>\n");
		_htmlStream.append("		<TD valign='center' align='left' width='14'>\n");
						
        // create link for previous page
		HashMap prevParamsMap = new HashMap();
		prevParamsMap.putAll(_paramsMap);
		prevParamsMap.put("MESSAGE", "LIST_PAGE");
		prevParamsMap.put("LIST_PAGE", String.valueOf(prevPage));
		PortletURL prevUrl = createUrl(prevParamsMap);
		
		HashMap nextParamsMap = new HashMap();
		nextParamsMap.putAll(_paramsMap);
		nextParamsMap.put("MESSAGE", "LIST_PAGE");
		nextParamsMap.put("LIST_PAGE", String.valueOf(nextPage));
		PortletURL nextUrl = createUrl(nextParamsMap);	
		
		String valueFilter = (String) _serviceRequest.getAttribute(SpagoBIConstants.VALUE_FILTER);
		String columnFilter = (String) _serviceRequest.getAttribute(SpagoBIConstants.COLUMN_FILTER);
		String typeFilter = (String) _serviceRequest.getAttribute(SpagoBIConstants.TYPE_FILTER);
		if (valueFilter != null && columnFilter != null && typeFilter != null) {
			prevUrl.setParameter(SpagoBIConstants.VALUE_FILTER, valueFilter);
			prevUrl.setParameter(SpagoBIConstants.COLUMN_FILTER, columnFilter);
			prevUrl.setParameter(SpagoBIConstants.TYPE_FILTER, typeFilter);
			nextUrl.setParameter(SpagoBIConstants.VALUE_FILTER, valueFilter);
			nextUrl.setParameter(SpagoBIConstants.COLUMN_FILTER, columnFilter);
			nextUrl.setParameter(SpagoBIConstants.TYPE_FILTER, typeFilter);
		} else {
			valueFilter = "";
			columnFilter = "";
			typeFilter = "";
		}
		
		if (pageNumber != 1) {	
			_htmlStream.append("		<A href=\""+prevUrl.toString()+"\"><IMG src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/prevPage.gif")+"' ALIGN=RIGHT border=0></a>\n"); 
		} else {
			_htmlStream.append("		<IMG src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/prevPage.gif")+"' ALIGN=RIGHT border=0>\n");
		}		
		_htmlStream.append("		</TD>\n");
				
		// create center blank cell
		_htmlStream.append("		<TD>&nbsp;</TD>\n");
		
        // visualize page numbers
		String pageLabel = PortletUtilities.getMessage("ListTag.pageLable", "messages");
		String pageOfLabel = PortletUtilities.getMessage("ListTag.pageOfLable", "messages");
		_htmlStream.append("						<TD align='center'>\n");
		_htmlStream.append("							<font class='aindice'>&nbsp;"+pageLabel+ " " + pageNumber + " " +pageOfLabel+ " " + pagesNumber + "&nbsp;</font>\n");
		_htmlStream.append("						    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n");
		
		String label = PortletUtilities.getMessage("SBIListLookPage.labelFilter", "messages");
		String labelStart = PortletUtilities.getMessage("SBIListLookPage.startWith", "messages");;
		String labelEnd = PortletUtilities.getMessage("SBIListLookPage.endWith", "messages");;
		String labelContain = PortletUtilities.getMessage("SBIListLookPage.contains", "messages");;
		String labelEqual = PortletUtilities.getMessage("SBIListLookPage.isEquals", "messages");;
		String labelFilter = PortletUtilities.getMessage("SBIListLookPage.filter", "messages");
		String labelAll = PortletUtilities.getMessage("SBIListLookPage.all", "messages");
		
		PortletURL allUrl = createUrl(_paramsMap);
		allUrl.setParameter(SpagoBIConstants.VALUE_FILTER, "");
		
		PortletURL filterUrl = createUrl(_paramsMap);
		
		_htmlStream.append("						    <br/><br/>\n");
		_htmlStream.append("						    <form action='"+filterUrl.toString()+"' id='formFilter' method='post'>\n");
		_htmlStream.append("						    "+label+"\n");
		_htmlStream.append("						    <select name='"+SpagoBIConstants.COLUMN_FILTER+"'>\n");
		for (int i = 0; i < visibleCols.length; i++) {
			String selected = "";
			if(visibleCols[i].equalsIgnoreCase(columnFilter))
				selected = " selected='selected' "; 
		_htmlStream.append("						    	<option value='"+visibleCols[i]+"' "+selected+" >"+visibleCols[i]+"</option>\n");	
		}
		String selected = "";
		_htmlStream.append("						    </select>\n");
		_htmlStream.append("						    <select name='"+SpagoBIConstants.TYPE_FILTER+"'>\n");
		if(typeFilter.equalsIgnoreCase(SpagoBIConstants.START_FILTER))
			selected = " selected='selected' ";
		else selected = "";
		_htmlStream.append("						    	<option value='"+SpagoBIConstants.START_FILTER+"' "+selected+" >"+labelStart+"</option>\n");
		if(typeFilter.equalsIgnoreCase(SpagoBIConstants.END_FILTER))
			selected = " selected='selected' ";
		else selected = "";
		_htmlStream.append("						    	<option value='"+SpagoBIConstants.END_FILTER+"' "+selected+" >"+labelEnd+"</option>\n");
		if(typeFilter.equalsIgnoreCase(SpagoBIConstants.EQUAL_FILTER))
			selected = " selected='selected' ";
		else selected = "";
		_htmlStream.append("						    	<option value='"+SpagoBIConstants.EQUAL_FILTER+"' "+selected+" >"+labelEqual+"</option>\n");
		if(typeFilter.equalsIgnoreCase(SpagoBIConstants.CONTAIN_FILTER))
			selected = " selected='selected' ";
		else selected = "";
		_htmlStream.append("						    	<option value='"+SpagoBIConstants.CONTAIN_FILTER+"' "+selected+" >"+labelContain+"</option>\n");
		_htmlStream.append("						    </select>\n");
		_htmlStream.append("						    <input type='text' name='"+SpagoBIConstants.VALUE_FILTER+"' size='10' value='"+valueFilter+"' /> \n");
		_htmlStream.append("						    <a href='javascript:document.getElementById(\"formFilter\").submit()'>"+labelFilter+"</a> \n");
		_htmlStream.append(" <a href='"+allUrl.toString()+"'>"+labelAll+"</a> \n");
		_htmlStream.append("						    </form> \n");
		
		// create link for next page
		_htmlStream.append("		<TD valign='center' align='right' width='14'>\n");
			
		if(pageNumber != pagesNumber) {	
			_htmlStream.append("		<A href=\""+nextUrl.toString()+"\"><IMG src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/nextPage.gif")+"' ALIGN=RIGHT border=0></a>\n"); 
		} else {
			_htmlStream.append("		<IMG src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/nextPage.gif")+"' ALIGN=RIGHT border=0>\n");
		}		
		_htmlStream.append("		</TD>\n");
		_htmlStream.append("	</TR>\n");
		_htmlStream.append("</TABLE>\n");
	} 

	
	
	 
    /**
	 * @see it.eng.spagobi.presentation.tags.ListTag#makeButton()
	 * 
	 */
	
	protected StringBuffer makeButton() throws JspException {
		
		StringBuffer htmlStream = new StringBuffer();

		PortletURL backURL = createUrl(_paramsMap);
		backURL.setParameter(SpagoBIConstants.MESSAGEDET, ObjectsTreeConstants.EXEC_PHASE_RETURN_FROM_LOOKUP);
		backURL.setParameter("LOOKUP_VALUE", "");
		
		String labBack = PortletUtilities.getMessage("SBIListLookPage.backButton", "messages");
		
		htmlStream.append("		<td class=\"header-button-column-portlet-section\">\n");
		htmlStream.append("		    <a href='"+backURL.toString()+"'>\n");
		htmlStream.append("			<img class=\"header-button-image-portlet-section\" title='" + labBack + "' alt='" + labBack + "' src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")+"' />\n");
		htmlStream.append("		    </a>\n");
		htmlStream.append("		</td>\n");
		
		return htmlStream;
	} 
	
		
	 
    /**
	 * Gets the parameters map for the list.
	 * 
	 * @param parameters The parameters input list
	 * @param row The source bean containing the value object
	 * @return The parameters hash map
	 * 
	 */
	protected HashMap getParametersMap(List parameters, SourceBean row) throws JspException {
		
		HashMap params = new HashMap(); 
       
		for (int i = 0; i < parameters.size(); i++) {
			String name = (String) ((SourceBean) parameters.get(i)).getAttribute("NAME");
			String type = (String) ((SourceBean) parameters.get(i)).getAttribute("TYPE");
			String value = (String) ((SourceBean) parameters.get(i)).getAttribute("VALUE");
			String scope = (String) ((SourceBean) parameters.get(i)).getAttribute("SCOPE");
			
			if (name != null) {
				name = JavaScript.escape(name.toUpperCase());
								
				if ((type != null) && type.equalsIgnoreCase("RELATIVE")) {
					if ((scope != null) && scope.equalsIgnoreCase("LOCAL")) {
						if (row == null) {
							SpagoBITracer.critical("adminTools", "LookupListTag", "getParametersMap", "Impossible to associate local scope to the button");
							throw new JspException("Impossible to associate local scope to the button");
						} // if (row == null)
						Object valueObject = row.getAttribute(value);
						if (valueObject != null)
							value = valueObject.toString();
					} // if ((scope != null) && scope.equalsIgnoreCase("LOCAL"))
					else
						value =	(String)(ContextScooping.getScopedParameter(_requestContainer, _responseContainer, value, scope)).toString();
				} // if ((type != null) && type.equalsIgnoreCase("RELATIVE"))
				if (value == null)
					value = "";
				value = JavaScript.escape(value);
			} // if (name != null)
			
			params.put(name, value);
			
		} // for (int i = 0; i < parameters.size(); i++)
		return params;
	} // protected StringBuffer getParametersList(Vector parameters, SourceBean row) throws JspException

	 
    /**
	 * @see it.eng.spagobi.presentation.tags.ListTag#createUrl(java.util.HashMap)
	 * 
	 */
	private PortletURL createUrl(HashMap paramsMap) {
		PortletURL url = renderResponse.createActionURL();
		Set paramsKeys = paramsMap.keySet();
		Iterator iter = paramsKeys.iterator();
		while(iter.hasNext()) {
			String paramKey = (String)iter.next();
			String paramValue = (String)paramsMap.get(paramKey);
			paramKey = JavaScript.escape(paramKey.toUpperCase());
			paramValue = JavaScript.escape(paramValue.toUpperCase());
            url.setParameter(paramKey, paramValue); 		
		}
		return url;
	}
	
	 
    /**
	 * @see it.eng.spagobi.presentation.tags.ListTag#getQueryStringParameter()
	 * 
	 */
//	protected HashMap getQueryStringParameter() {
//		
//		HashMap params = new HashMap();
//		List queryParameters = _serviceRequest.getContainedAttributes();
//		
//		for (int i = 0; i < queryParameters.size(); i++) {			
//			SourceBeanAttribute parameter = (SourceBeanAttribute) queryParameters.get(i);
//			String parameterKey = parameter.getKey();
//			if (parameterKey.equalsIgnoreCase("ACTION_NAME")
//				|| parameterKey.equalsIgnoreCase("PAGE")
//				|| parameterKey.equalsIgnoreCase("MODULE")
//				|| parameterKey.equalsIgnoreCase("MESSAGE")
//				|| parameterKey.equalsIgnoreCase("LIST_PAGE")
//				|| parameterKey.equalsIgnoreCase("LIST_NOCACHE")
//				|| parameterKey.equalsIgnoreCase("NAVIGATOR_DISABLED")
//				|| parameterKey.equalsIgnoreCase("NAVIGATOR_RESET"))
//				continue;
//			String parameterValue = parameter.getValue().toString();
//			params.put(parameterKey, parameterValue);
//		} // for (int i = 0; i < queryParameters.size(); i++)
//		return params;
//	} // protected String getQueryString()

	 
    /**
	 * @see it.eng.spagobi.presentation.tags.ListTag#setActionName(java.lang.String)
	 * 
	 */	
	public void setActionName(String actionName) {
		TracerSingleton.log(
			Constants.NOME_MODULO,
			TracerSingleton.DEBUG,
			"DefaultDetailTag::setActionName:: actionName [" + actionName + "]");
		_actionName = actionName;
	} // public void setActionName(String actionName)
	
	/**
	 * @see it.eng.spagobi.presentation.tags.ListTag#setModuleName(java.lang.String)
	 * 
	 */	
	public void setModuleName(String moduleName) {
		TracerSingleton.log(
			Constants.NOME_MODULO,
			TracerSingleton.INFORMATION,
			"LookupListTag::setModuleName:: moduleName [" + moduleName + "]");
		_moduleName = moduleName;
	} // public void setModuleName(String moduleName)

	/**
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspException {
		TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.INFORMATION, "LookupListTag::doEndTag:: invocato");
		_actionName = null;
		_moduleName = null;
		_serviceName = null;
		_content = null;
		_layout = null;
		_providerURL = null;
		_requestContainer = null;
		_serviceRequest = null;
		_responseContainer = null;
		_serviceResponse = null;
		_htmlStream = null;
		return super.doEndTag();
	} // public int doEndTag() throws JspException
} // public class LookupListTag extends TagSupport




























//protected void makeJavaScript() {
//	_htmlStream.append("<SCRIPT language=\"ButtonHandlerTag\" type=\"text/javascript\">\n");
//	_htmlStream.append("function goConfirm(url, alertFlag) {\n");
//	_htmlStream.append("var _url = \"AdapterHTTP?\" + url;\n");
//	_htmlStream.append("if (alertFlag == 'TRUE' ) {\n");
//	_htmlStream.append("if (confirm('Confermi operazione'))\n");
//	_htmlStream.append("window.location = _url;\n");
//	_htmlStream.append("}\n");
//	_htmlStream.append("else\n");
//	_htmlStream.append("window.location = _url;\n");
//	_htmlStream.append("}\n");
//	_htmlStream.append("// -->\n");
//	_htmlStream.append("</SCRIPT>\n");
//	SourceBean insertButton = (SourceBean) _layout.getAttribute("BUTTONS.INSERT_BUTTON");
//	SourceBean deleteCaption = (SourceBean) _layout.getAttribute("CAPTIONS.DELETE_CAPTION");
//	
//	RequestContainer requestContainer = RequestContainerPortletAccess.getRequestContainer(httpRequest);
//	SourceBean request = requestContainer.getServiceRequest();
//	boolean disableTorna = false;
//	if (request.containsAttribute(Navigator.NAVIGATOR_RESET))
//		if (((String)request.getAttribute(Navigator.NAVIGATOR_RESET)).equalsIgnoreCase("TRUE"))
//			disableTorna = true;
//		
//	_htmlStream.append("<SCRIPT>\n");
//	_htmlStream.append("function inizio() {\n");
//	if 	(deleteCaption == null)	
//		_htmlStream.append("	sospendiSingoloPulsante ('ELIMI');\n");
//	if (insertButton == null)
//		_htmlStream.append("	sospendiSingoloPulsante ('NUOVO') ;\n");
//	if (disableTorna)
//		_htmlStream.append("	sospendiSingoloPulsante ('TORNA') ;\n");
//	_htmlStream.append("}\n");
//	
//	_htmlStream.append("		function reloadPulsanti() {\n");
//	if 	(deleteCaption != null)	
//		_htmlStream.append("		   reloadPulsante('ELIMI','listaForm','RB_LISTA');\n");
//	_htmlStream.append("		   reloadPulsante('MODIF','listaForm','RB_LISTA');\n");
//	if (!disableTorna)
//		_htmlStream.append("		   reloadPulsante('TORNA','listaForm','RB_LISTA');\n");
//	if (insertButton != null)
//		_htmlStream.append("		   reloadPulsante('NUOVO','listaForm','RB_LISTA');\n");
//	_htmlStream.append("		}\n");
//	_htmlStream.append("		function sospendiPulsanti () {\n");
//	_htmlStream.append("		  sospendiSingoloPulsante ('ELIMI');\n");
//	_htmlStream.append("		  sospendiSingoloPulsante ('MODIF');\n");
//	_htmlStream.append("		  sospendiSingoloPulsante ('TORNA');\n");
//	_htmlStream.append("		  sospendiSingoloPulsante ('NUOVO');\n");
//	_htmlStream.append("		}\n");
//	_htmlStream.append("		function inviaRichiesta(pulsante,url,alertFlag) {\n");
//	_htmlStream.append("			var is_attivo = eval(pulsante+'H');\n");
//	_htmlStream.append("			if ( is_attivo == 'false') { \n");
//	_htmlStream.append("				alert ('Operazione disabilitata');\n");
//	_htmlStream.append("				return;\n");
//	_htmlStream.append("			}\n");
//	_htmlStream.append("			var result = getIdSelezionato('listaForm','RB_LISTA');\n");
//	_htmlStream.append("			if (pulsante == \"ELIMI\") {\n");
//	_htmlStream.append("               if ((alertFlag == 'TRUE' ) && ((confirm('Confermi cancellazione?')))){\n");
//	_htmlStream.append("				  sospendiPulsanti ();\n");
//	_htmlStream.append("				  var _url = 'AdapterHTTP?';\n");
//	_htmlStream.append("				  _url = _url + result + url;\n");
//	_htmlStream.append("                  window.location = _url;\n");
//	_htmlStream.append("				  return ;\n");
//	_htmlStream.append("			   }\n");
//	_htmlStream.append("			} else if (pulsante == \"MODIF\") {\n");
//	_htmlStream.append("				sospendiPulsanti ();\n");
//	_htmlStream.append("				var _url = 'AdapterHTTP?';\n");
//	_htmlStream.append("				_url = _url + result + url;\n");
//	_htmlStream.append("				window.location = _url;\n");
//	_htmlStream.append("				return ;\n");
//	_htmlStream.append("			} else if (pulsante == \"TORNA\") {\n");
//	_htmlStream.append("				sospendiPulsanti();\n");
//	_htmlStream.append("				window.location = 'AdapterHTTP?NAVIGATOR_BACK=1&LIST_NOCACHE=TRUE';\n");
//	_htmlStream.append("				return;\n");
//	_htmlStream.append("			} else if (pulsante == \"NUOVO\") {\n");
//	_htmlStream.append("				sospendiPulsanti ();\n");
//	_htmlStream.append("				var _url = 'AdapterHTTP?';\n");
//	_htmlStream.append("				_url = _url + url;\n");
//	_htmlStream.append("				window.location = _url;\n");
//	_htmlStream.append("				return ;\n");
//	_htmlStream.append("			}\n");
//	_htmlStream.append("			return;\n");
//	_htmlStream.append("		}\n");
//	_htmlStream.append("</SCRIPT>\n");
//
//	} // protected void makeJavaScript()






