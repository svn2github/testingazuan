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
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spago.util.ContextScooping;
import it.eng.spago.util.JavaScript;
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
 * Builds and presents all objects list for all admin 
 * SpagoBI's list modules. Once a list module has been executed, 
 * the list tag builds all the correspondent jsp page and gives the results
 * 
 * @author sulis
 */

public class ListBIParametersTag extends ListTag
{
    
	/**
	 * Starting from the module <code>buttonsSB</code> object, 
	 * creates all buttons for the jsp list. 
	 * 
	 * @throws JspException If any exception occurs.
	 */
	
	protected StringBuffer makeButton() throws JspException {

		StringBuffer htmlStream = new StringBuffer();
		
		//htmlStream.append("<table width='100%'\n");
		//htmlStream.append("	<tr>\n");
		//htmlStream.append("		<td>&nbsp;</td>\n");
				
		SourceBean buttonsSB = (SourceBean)_layout.getAttribute("BUTTONS");
		List buttons = buttonsSB.getContainedSourceBeanAttributes();
		Iterator iter = buttons.listIterator();
		while(iter.hasNext()) {
			SourceBeanAttribute buttonSBA = (SourceBeanAttribute)iter.next();
			SourceBean buttonSB = (SourceBean)buttonSBA.getValue();
			List parameters = buttonSB.getAttributeAsList("PARAMETER");
			HashMap paramsMap = getParametersMap(parameters, null);
			String img = (String)buttonSB.getAttribute("image");
			String labelCode = (String)buttonSB.getAttribute("label");
			String label = PortletUtilities.getMessage(labelCode, "messages");
			htmlStream.append("<form action='"+renderResponse.createActionURL()+"' id='form"+label+"'  method='POST' >\n");
			htmlStream.append("	<td class=\"header-button-column-portlet-section\">\n");
			Set paramsKeys = paramsMap.keySet();
			Iterator iterpar = paramsKeys.iterator();
			while(iterpar.hasNext()) {
				String paramKey = (String)iterpar.next();
				String paramValue = (String)paramsMap.get(paramKey);
				//paramKey = JavaScript.escape(paramKey.toUpperCase());
				//paramValue = JavaScript.escape(paramValue.toUpperCase());
				while(paramValue.indexOf("%20") != -1) {
					paramValue = paramValue.replaceAll("%20", " ");
				}
				htmlStream.append("	  <input type='hidden' name='"+paramKey+"' value='"+paramValue+"' /> \n");
			}
			//htmlStream.append("				<input type='image' src='"+renderResponse.encodeURL(renderRequest.getContextPath() + img)+"' />\n");
			//htmlStream.append("			</form>\n");
			//htmlStream.append("		    <a href='javascript:document.getElementById(\"form"+label+"\").submit()'>"+label+"</a>\n");
			htmlStream.append("		<a href='javascript:document.getElementById(\"form"+label+"\").submit()'><img class=\"header-button-image-portlet-section\" title='" + label + "' alt='" + label + "' src='"+renderResponse.encodeURL(renderRequest.getContextPath() + img)+"' /></a>\n");
			htmlStream.append("	</td>\n");
			htmlStream.append("</form>\n");
			
			//_htmlStream.append("		<td width='150px' align='center'>\n");
			//_htmlStream.append("			<a href='"+buttonUrl.toString()+"'><img src='"+renderResponse.encodeURL(renderRequest.getContextPath() + img)+"' /></a><br/>\n");
			//_htmlStream.append("		    <a href='"+buttonUrl.toString()+"' class='portlet-menu-item' >"+label+"</a>\n");
			//_htmlStream.append("		</td>\n");
		}
		//htmlStream.append("		<td>&nbsp;</td>\n");
		//htmlStream.append("	</tr>\n");
		//htmlStream.append("</table>\n");
		//htmlStream.append("<br/><br/>\n");
		
		return htmlStream;
	} 



}
	
	



