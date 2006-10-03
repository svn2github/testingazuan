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
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.navigation.LightNavigationManager;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spago.util.ContextScooping;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.PortletUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author Gioia
 *
 */
public class CheckBoxTag extends TagSupport {
	
	private HttpServletRequest httpRequest = null;
	
	protected RenderRequest renderRequest = null;
    protected RenderResponse renderResponse = null;
  	
    protected RequestContainer _requestContainer = null;
    protected ResponseContainer _responseContainer = null;
    
    protected SourceBean _serviceRequest = null;	
	protected SourceBean _serviceResponse = null;
    
	PortletRequest portReq = null;
    SessionContainer _session = null;
	
	protected String _actionName = null;
	protected String _moduleName = null;
	protected String pageName = null;
	
	protected String _serviceName = null;
	protected SourceBean _content = null;
	protected SourceBean _layout = null;
	protected String _providerURL = null;	
	protected StringBuffer _htmlStream = null;
	protected Vector _columns = null;
    protected String labelLinkSaltoPagina;
    protected String _filter = null;
    protected int pageNumber;
    protected int pagesNumber;
    
	protected EMFErrorHandler _errorHandler = null;
    
    //  the _paramsMap contains all the ADDITIONAL parameters set by the action or module for the navigation buttons ("next", "previous", "filter" and "all" buttons)
    protected HashMap _paramsMap = new HashMap();
    
    
   
    // the _providerUrlMap contains all the parameters for the navigation buttons ("next", "previous", "filter" and "all" buttons)
    private HashMap _providerUrlMap = new HashMap();
    
    
	public int doStartTag() throws JspException {
		
		SpagoBITracer.info("Admintools", "ListTag", "doStartTag", " method invoked");
		
		httpRequest = (HttpServletRequest) pageContext.getRequest();
		renderResponse =(RenderResponse)httpRequest.getAttribute("javax.portlet.response");
		renderRequest =(RenderRequest)httpRequest.getAttribute("javax.portlet.request");
			
		_requestContainer = RequestContainerPortletAccess.getRequestContainer(httpRequest);
		portReq = PortletUtilities.getPortletRequest();
		_serviceRequest = _requestContainer.getServiceRequest();
		_responseContainer = ResponseContainerPortletAccess.getResponseContainer(httpRequest);
				
		_session = _requestContainer.getSessionContainer();
		
		_serviceResponse = _responseContainer.getServiceResponse();
		
		ConfigSingleton configure = ConfigSingleton.getInstance();
		if (_actionName != null) {
			_serviceName = _actionName;
			_content = _serviceResponse;
			SourceBean actionBean =
				(SourceBean) configure.getFilteredSourceBeanAttribute("ACTIONS.ACTION", "NAME", _actionName);
			_layout = (SourceBean) actionBean.getAttribute("CONFIG");
			if (_layout == null) {
				// if the layout is dinamically created it is an attribute of the response
				_layout = (SourceBean) _serviceResponse.getAttribute("CONFIG");
			}
			_providerURL = "ACTION_NAME=" + _actionName + "&";
			_providerUrlMap.put("ACTION_NAME", _actionName);
			HashMap params = (HashMap) _serviceResponse.getAttribute("PARAMETERS_MAP");
			if (params != null) {
				_paramsMap = params;
				_providerUrlMap.putAll(_paramsMap);
			}
		} 
		else if (_moduleName != null) {
			_serviceName = _moduleName;
			SpagoBITracer.debug("Admintools", "ListTag", "doStartTag", " Module Name: " + _moduleName);
			_content = (SourceBean) _serviceResponse.getAttribute(_moduleName);
			SourceBean moduleBean =
				(SourceBean) configure.getFilteredSourceBeanAttribute("MODULES.MODULE", "NAME", _moduleName);
			
			if(moduleBean!=null) SpagoBITracer.debug("Admintools", "ListTag", "doStartTag", _moduleName + " configuration loaded");
			_layout = (SourceBean) moduleBean.getAttribute("CONFIG");
			if (_layout == null) {
				// if the layout is dinamically created it is an attribute of the response
				_layout = (SourceBean) _serviceResponse.getAttribute(_moduleName + ".CONFIG");
			}
			
			pageName = (String) _serviceRequest.getAttribute("PAGE");
			SpagoBITracer.debug("Admintools", "ListTag", "doStartTag", " PAGE: " + pageName);
			_providerURL = "PAGE=" + pageName + "&MODULE=" + _moduleName + "&";
			_providerUrlMap.put("PAGE", pageName);
			_providerUrlMap.put("MODULE", _moduleName);
			
			HashMap params = (HashMap) _serviceResponse.getAttribute(_moduleName + ".PARAMETERS_MAP");
			if (params != null) {
				_paramsMap = params;
				_providerUrlMap.putAll(_paramsMap);
			}
		} // if (_moduleName != null)
		else {
			SpagoBITracer.critical("Admintools", "ListTag", "doStartTag", "service name not specified");
			throw new JspException("Business name not specified !");
		} // if (_content == null)
		if (_content == null) {
			SpagoBITracer.warning("Admintools", "ListTag", "doStartTag", "list content null");
			return SKIP_BODY;
		} // if (_content == null)
		if (_layout == null) {
			SpagoBITracer.warning("Admintools", "ListTag", "doStartTag", "list module configuration null");
			return SKIP_BODY;
		} // if (_layout == null)
		
		// if the LightNavigator is disabled entering the list, it is kept disabled untill exiting the list
		Object lightNavigatorDisabledObj = _serviceRequest.getAttribute(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED);
		if (lightNavigatorDisabledObj != null) {
			String lightNavigatorDisabled = (String) lightNavigatorDisabledObj;
			_providerUrlMap.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, lightNavigatorDisabled);
		}
		
		
		
		try {
			
		} 
		catch (NumberFormatException ex) {
			TracerSingleton.log(
				Constants.NOME_MODULO,
				TracerSingleton.WARNING,
				"ListTag::makeNavigationButton:: PAGES_NUMBER nullo");
		} 
		
		String pageNumberString = (String) _content.getAttribute("PAGED_LIST.PAGE_NUMBER");
		String pagesNumberString = (String) _content.getAttribute("PAGED_LIST.PAGES_NUMBER");
		pageNumber = 1; 
		pagesNumber = 1;
		try {
			pageNumber = Integer.parseInt(pageNumberString);
			pagesNumber = Integer.parseInt(pagesNumberString);
		} 
		catch (NumberFormatException ex) {
			TracerSingleton.log(
				Constants.NOME_MODULO,
				TracerSingleton.WARNING,
				"ListTag::makeNavigationButton:: PAGE(s)_NUMBER nullo");
		} 
		
		_htmlStream = new StringBuffer();
		HashMap params = new HashMap();
		params.put("PAGE", pageName); 
		params.put("MESSAGE", "HANDLE_CHECKLIST"); 
		params.put("LIGHT_NAVIGATOR_DISABLED", "true"); 
		//params.put("CHECKEDOBJECTS", _content.getAttribute("CHECKEDOBJECTS"));
		_session.setAttribute("CHECKEDOBJECTS", _content.getAttribute("CHECKEDOBJECTS"));
		params.put("PAGE_NUMBER", new Integer(pageNumber).toString());
		PortletURL url = createUrl(params);	
		
		_htmlStream.append(" <form method='POST' id='form' action='" + url.toString() + "'>\n");
		makeForm();
		_htmlStream.append(" </form>\n");
		
		try {
			pageContext.getOut().print(_htmlStream);
		} // try
		catch (Exception ex) {
			SpagoBITracer.critical("Admintools", "ListTag", "doStartTag", "Impossible to send the stream");
			throw new JspException("Impossible to send the stream");
		} // catch (Exception ex)
		return SKIP_BODY;
	}
	
	/**
	 * Creates a form into the jsp page.
	 * 
	 * @throws JspException If any exception occurs.
	 */
	
	protected void makeForm() throws JspException {
		
		String titleCode = (String) _layout.getAttribute("TITLE");
		SourceBean buttonsSB = (SourceBean) _layout.getAttribute("BUTTONS");
		List buttons = buttonsSB.getContainedSourceBeanAttributes();
		
		if (titleCode != null && buttons.size() > 0) {
			String title = PortletUtilities.getMessage(titleCode, "messages");
			_htmlStream.append(" <table class=\"header-table-portlet-section\">\n");
			_htmlStream.append("	<tr class='header-row-portlet-section'>\n");
			_htmlStream.append("			<td class=\"header-title-column-portlet-section\" style=\"vertical-align:middle;padding-left:5px;\" >" + title + "</td>\n");
			_htmlStream.append("			<td class=\"header-empty-column-portlet-section\">&nbsp;</td>\n");
			_htmlStream.append(				makeButton(buttons) + "\n");
			_htmlStream.append("	</tr>\n");
			_htmlStream.append(" </table>\n");
		}

		makeColumns();
		makeRows();
		makeNavigationButton();
		
	} // public void makeForm()

	
	/**
	 * Builds Table list columns, reading all request information.
	 * 
	 * @throws JspException If any Exception occurs.
	 */
	
	protected void makeColumns() throws JspException {
		
		SourceBean captionSB = (SourceBean) _layout.getAttribute("CAPTIONS");
		List captions = captionSB.getContainedSourceBeanAttributes();
		int numCaps = captions.size();
		
		_columns = new Vector();
		List columnsVector = _layout.getAttributeAsList("COLUMNS.COLUMN");
		for (int i = 0; i < columnsVector.size(); i++) {
			String hidden = (String)((SourceBean) columnsVector.get(i)).getAttribute("HIDDEN");
			if (hidden == null || hidden.trim().equalsIgnoreCase("FALSE"))
				_columns.add((SourceBean) columnsVector.get(i));
		}
		if ((_columns == null) || (_columns.size() == 0)) {
			SpagoBITracer.critical("Admintools", "ListTag", "doStartTag", "Columns names not defined");
			throw new JspException("Columns names not defined");
		} 
		
		_htmlStream.append("<TABLE style='width:100%;margin-top:1px'>\n");
		_htmlStream.append("	<TR>\n");

		for (int i = 0; i < _columns.size(); i++) {
			String nameColumn = (String) ((SourceBean) _columns.elementAt(i)).getAttribute("NAME");
			String labelColumnCode = (String) ((SourceBean) _columns.elementAt(i)).getAttribute("LABEL");
			String labelColumn = "";
			if (labelColumnCode != null) labelColumn = PortletUtilities.getMessage(labelColumnCode, "messages");
			else labelColumn = nameColumn;
			// if an horizontal-align is specified it is considered, otherwise the defualt is align='left'
			String align = (String) ((SourceBean) _columns.elementAt(i)).getAttribute("horizontal-align");
			if (align == null || align.trim().equals("")) align = "left";
			_htmlStream.append("<TD class='portlet-section-header' valign='center' align='" + align + "'  >" + labelColumn + "</TD>\n");
		} 
		for(int i=0; i<numCaps; i++) {
			_htmlStream.append("<TD class='portlet-section-header' align='center'>&nbsp;</TD>\n");
		} 
		_htmlStream.append("	<TD class='portlet-section-header' align='center'>&nbsp;</TD>\n");
		_htmlStream.append("</TR>\n");
	} 
	
	
	
	
	
	/**
	 * Builds Table list rows, reading all query information.
	 * 
	 * @throws JspException If any Exception occurs.
	 */
	
	
	protected void makeRows() throws JspException {

		List rows = _content.getAttributeAsList("PAGED_LIST.ROWS.ROW");
	    
		// js function for item action confirm
		String confirmCaption = PortletUtilities.getMessage("ListTag.confirmCaption", "messages");
		_htmlStream.append(" <script>\n");
		_htmlStream.append("	function actionConfirm(message, url){\n");
		_htmlStream.append("		if (confirm('" + confirmCaption + " ' + message + '?')){\n");
		_htmlStream.append("			location.href = url;\n");
		_htmlStream.append("		}\n");
		_htmlStream.append("	}\n");
		_htmlStream.append(" </script>\n");
		
		boolean alternate = false;
        String rowClass;
		for(int i = 0; i < rows.size(); i++) {
			SourceBean row = (SourceBean) rows.get(i);
            
            rowClass = (alternate) ? "portlet-section-alternate" : "portlet-section-body";
            alternate = !alternate;    
            
            _htmlStream.append(" <tr class='portlet-font'>\n");
			for (int j = 0; j < _columns.size(); j++) {
				String nameColumn = (String) ((SourceBean) _columns.elementAt(j)).getAttribute("NAME");
				Object fieldObject = row.getAttribute(nameColumn);
				String field = null;
				if (fieldObject != null)
					field = fieldObject.toString();
				else
					field = "&nbsp;";
				// if an horizontal-align is specified it is considered, otherwise the defualt is align='left'
				String align = (String) ((SourceBean) _columns.elementAt(j)).getAttribute("horizontal-align");
				if (align == null || align.trim().equals("")) align = "left";
				_htmlStream.append(" <td class='" + rowClass + "' align='" + align + "' valign='top' >" + field + "</td>\n");
			} 
			
			
			SourceBean captionsSB = (SourceBean) _layout.getAttribute("CAPTIONS");
			List captions = captionsSB.getContainedSourceBeanAttributes();
			Iterator iter = captions.iterator();
			while (iter.hasNext()) {
				SourceBeanAttribute captionSBA = (SourceBeanAttribute)iter.next();
				SourceBean captionSB = (SourceBean)captionSBA.getValue();
				List parameters = captionSB.getAttributeAsList("PARAMETER");
				HashMap paramsMap = getParametersMap(parameters, row);
				String img = (String)captionSB.getAttribute("image");
				String labelCode = (String)captionSB.getAttribute("label");
				String label = PortletUtilities.getMessage(labelCode, "messages");
				PortletURL buttonUrl = createUrl(paramsMap);
				boolean confirm = false;
				if (captionSB.getAttribute("confirm") != null &&
						((String)captionSB.getAttribute("confirm")).equalsIgnoreCase("TRUE")){
					confirm = true;
				}
				_htmlStream.append(" <td width='20' class='" + rowClass + "'>\n");
				if (confirm){
					_htmlStream.append("     <a href='javascript:actionConfirm(\"" + label + "\", \"" + buttonUrl.toString() + "\");'>\n");	
				}else{
					_htmlStream.append("     <a href='"+buttonUrl.toString()+"'>\n");	
				}
				_htmlStream.append("			<img title='"+label+"' alt='"+label+"' src='"+renderResponse.encodeURL(renderRequest.getContextPath() + img)+"' />\n");
				_htmlStream.append("     </a>\n");
				_htmlStream.append(" </td>\n");
			}
			_htmlStream.append(" <td class='" + rowClass + "' width='30px'>\n");	
			String checked = (String)row.getAttribute("CHECKED");
			
			//String objectIdName = (String)((SourceBean) _layout.getAttribute("KEYS.OBJECT")).getAttribute("key");
			Object key = row.getAttribute("ROW_ID");			
			if(checked.equalsIgnoreCase("true")) {
				_htmlStream.append("\t<input type='checkbox' name='checkbox' value='" + GeneralUtilities.substituteQuotesIntoString(key.toString()) + "' checked>\n");
			}
			else {
				_htmlStream.append("\t<input type='checkbox' name='checkbox' value='" + GeneralUtilities.substituteQuotesIntoString(key.toString()) + "'>\n");
			}
			
			_htmlStream.append(" </td>\n");
			
			_htmlStream.append(" </tr>\n");
		}
		
		_htmlStream.append(" </table>\n");
	} 
	
	
	
	
	/**
	 * Builds list navigation buttons inside the list tag. If the number
	 * of elements is higher than 10, they are divided into pages; this
	 * methods creates forward and backward arrows and page number information 
	 * for navigation.
	 * 
	 * @throws JspException If any Exception occurs
	 */
	

	protected void makeNavigationButton() throws JspException {
		int prevPage = pageNumber - 1;
		if (prevPage < 1)
			prevPage = 1;
		int nextPage = pageNumber + 1;
		if (nextPage > pagesNumber)
			nextPage = pagesNumber;
				
				
		_htmlStream.append(" <TABLE CELLPADDING=0 CELLSPACING=0  WIDTH='100%' BORDER=0>\n");
		_htmlStream.append("	<TR>\n");
		_htmlStream.append("		<TD class='portlet-section-footer' valign='center' align='left' width='14'>\n");
		
		
		
		if(pageNumber != 1) {	
			//_htmlStream.append("		<A href=\""+prevUrl.toString()+"\"><IMG src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/prevPage.gif")+"' ALIGN=RIGHT border=0></a>\n"); 
			_htmlStream.append("<input type='image' " +
					  "name='" + "prevPage" + "' " +					  
					  "src ='"+ renderResponse.encodeURL(renderRequest.getContextPath() + "/img/prevPage.gif") + "' " + 
					  "align='left' border=0" +
					  "alt='" + "GO To Previous Page" + "'>\n");
		} else {
			_htmlStream.append("		<IMG src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/prevPage.gif")+"' ALIGN=RIGHT border=0>\n");
		}		
		_htmlStream.append("		</TD>\n");
				
		// create center blank cell
		_htmlStream.append("		<TD class='portlet-section-footer'>page " + pageNumber + " of " + pagesNumber + "\n");
		_htmlStream.append("						    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n");
		
		
		//form for checked elements filtering
		
		if (_filter != null && _filter.equalsIgnoreCase("enabled")) {
			String checked = "";
			String isChecked = (String)_serviceRequest.getAttribute("checked");
			if(isChecked == null){
				isChecked = "true";
			}
			if(isChecked.equals("true")){
				checked = "checked='checked'";
			}
			
			_htmlStream.append("						    <br/><br/>\n");
			//_htmlStream.append("	<form method='POST' action='" + formUrl.toString() + "' id ='objectForm' name='objectForm'>\n");
			String label = PortletUtilities.getMessage("CheckboxTag.showChecked","messages");
			_htmlStream.append("						    "+label+"\n");
			_htmlStream.append("<input type=\"checkbox\"" + checked + " \n");
			_htmlStream.append("				onclick=\"submitForm()\" name=\"filterCheckbox\" id=\"filterCheckbox\" value=\"true\"/>\n");
			_htmlStream.append("<input type =\"hidden\" id=\"checkFilter\" name=\"checkFilter\" value=\"\" />");
			_htmlStream.append("<input type =\"hidden\" id=\"checked\" name=\"checked\" value=\"" + isChecked + "\" />");
			//_htmlStream.append("						    </form> \n");
			_htmlStream.append("						    </TD>\n");
			_htmlStream.append("<script>\n");
			_htmlStream.append("function submitForm() {\n");
			_htmlStream.append("var checkFilter=document.getElementById('checkFilter'); ");
			_htmlStream.append("var filterCheckbox=document.getElementById('filterCheckbox'); ");
			_htmlStream.append("var checked=document.getElementById('checked'); ");
			_htmlStream.append("if(filterCheckbox.checked == false){");
			_htmlStream.append("checked.value = 'false'");
			_htmlStream.append("}");
			_htmlStream.append("else{");
			_htmlStream.append("checked.value = 'true'");
			_htmlStream.append("}");
			_htmlStream.append("checkFilter.value='checkFilter';");
			_htmlStream.append("document.getElementById('form').submit();\n");
			_htmlStream.append("} \n");
			_htmlStream.append("</script>");
		}
		
		
		//	 create link for next page
		_htmlStream.append("		<TD class='portlet-section-footer' valign='center' align='right' width='14'>\n");				
		if(pageNumber != pagesNumber) {	
			//_htmlStream.append("		<A href=\""+nextUrl.toString()+"\"><IMG src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/nextPage.gif")+"' ALIGN=RIGHT border=0></a>\n"); 
			_htmlStream.append("<input type='image' " +
					  "name='" + "nextPage" + "' " +
					  "src ='"+ renderResponse.encodeURL(renderRequest.getContextPath() + "/img/nextPage.gif") + "' " +
					  "align='right' border='0'" +
					  "alt='" + "GO To Next Page" + "'>\n");
		} else {
			_htmlStream.append("		<IMG src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/nextPage.gif")+"' ALIGN=RIGHT border=0>\n");
		}		
		_htmlStream.append("		</TD>\n");
		
		_htmlStream.append("	</TR>\n");
		_htmlStream.append("</TABLE>\n");
	} 

	
	
	/**
	 * Starting from the module <code>buttonsSB</code> object, 
	 * creates all buttons for the jsp list. 
	 * @param buttons The list of the buttons 
	 * 
	 * @throws JspException If any exception occurs.
	 */
	
	protected StringBuffer makeButton(List buttons) throws JspException {

		StringBuffer htmlStream = new StringBuffer();

		Iterator iter = buttons.listIterator();
		while (iter.hasNext()) {
			SourceBeanAttribute buttonSBA = (SourceBeanAttribute)iter.next();
			SourceBean buttonSB = (SourceBean)buttonSBA.getValue();
			List parameters = buttonSB.getAttributeAsList("PARAMETER");
			HashMap paramsMap = getParametersMap(parameters, null);
			
			String name = (String) buttonSB.getAttribute("name");
			String img = (String) buttonSB.getAttribute("image");
			String labelCode = (String) buttonSB.getAttribute("label");			
			String label = PortletUtilities.getMessage(labelCode, "messages");
			
			PortletURL buttonUrl = createUrl(paramsMap);
			
			htmlStream.append("<td class=\"header-button-column-portlet-section\">\n");
			htmlStream.append("<input type='image' " +
									  "name='" + name + "' " +
									  "title='" + label + "' " +
									  "class='header-button-image-portlet-section'" + 	
									  "src ='"+ renderResponse.encodeURL(renderRequest.getContextPath() + img) + "' " +
									  "alt='" + label + "'>\n");
			htmlStream.append("</td>\n");
		}
		
		return htmlStream;
	} 
	
		
	/**
	 * Gets all parameter information from a module, putting them into a HashMap.
	 * 
	 * @param parameters The parameters list
	 * @param row The value objects Source Bean 
	 * @return The parameters Hash Map
	 * @throws JspException If any Exception occurred
	 */
	
	protected HashMap getParametersMap(List parameters, SourceBean row) throws JspException {
		
		HashMap params = new HashMap(); 
       
		for (int i = 0; i < parameters.size(); i++) {
			String name = (String) ((SourceBean) parameters.get(i)).getAttribute("NAME");
			String type = (String) ((SourceBean) parameters.get(i)).getAttribute("TYPE");
			String value = (String) ((SourceBean) parameters.get(i)).getAttribute("VALUE");
			String scope = (String) ((SourceBean) parameters.get(i)).getAttribute("SCOPE");
			
			if (name != null) {
				//name = JavaScript.escape(name.toUpperCase());
				name = name.toUpperCase();
				
				if ((type != null) && type.equalsIgnoreCase("RELATIVE")) {
					if ((scope != null) && scope.equalsIgnoreCase("LOCAL")) {
						if (row == null) {
							SpagoBITracer.critical("adminTools", "ListTag", "getParametersMap", "Impossible to associate local scope to the button");
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
				//value = JavaScript.escape(value);
			} // if (name != null)
			
			params.put(name, value);
			
		} // for (int i = 0; i < parameters.size(); i++)
		return params;
	} // protected StringBuffer getParametersList(Vector parameters, SourceBean row) throws JspException

	
	/**
	 * From the parameter HashMap at input, creates the reference navigation url.
	 * 
	 * @param paramsMap The parameter HashMap
	 * @return A <code>portletURL</code> object representing the navigation URL
	 */
	protected PortletURL createUrl(HashMap paramsMap) {
		PortletURL url = renderResponse.createActionURL();
		Set paramsKeys = paramsMap.keySet();
		Iterator iter = paramsKeys.iterator();
		while(iter.hasNext()) {
			String paramKey = (String)iter.next();
			String paramValue = paramsMap.get(paramKey).toString();
            url.setParameter(paramKey, paramValue); 		
		}
		return url;
	}	
	
	
	/**
	 * Traces the setting of an action name.
	 * 
	 * @param actionName The action name string at input. 
	 */
	public void setActionName(String actionName) {
		TracerSingleton.log(
			Constants.NOME_MODULO,
			TracerSingleton.DEBUG,
			"DefaultDetailTag::setActionName:: actionName [" + actionName + "]");
		_actionName = actionName;
	} // public void setActionName(String actionName)

	/**
	 * Traces the setting of a module name.
	 * 
	 * @param moduleName The module name string at input. 
	 */
	
	public void setModuleName(String moduleName) {
		TracerSingleton.log(
			Constants.NOME_MODULO,
			TracerSingleton.INFORMATION,
			"ListTag::setModuleName:: moduleName [" + moduleName + "]");
		_moduleName = moduleName;
	} // public void setModuleName(String moduleName)

	/**
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	
	public int doEndTag() throws JspException {
		TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.INFORMATION, "ListTag::doEndTag:: invocato");
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
		_columns = null;
		return super.doEndTag();
	} // public int doEndTag() throws JspException



	public void setFilter(String filter) {
		TracerSingleton.log(
				Constants.NOME_MODULO,
				TracerSingleton.INFORMATION,
				"ListTag::setFilter:: filter " + filter);
		_filter = filter;
	}
}


















