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
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.PortletUtilities;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

/**
 * Presentation tag for Script details. 
 */

public class JavaClassWizardTag extends CommonWizardLovTag {
	
	private HttpServletRequest httpRequest = null;
    protected RenderRequest renderRequest = null;
    protected RenderResponse renderResponse = null;
	private String javaClassName;
	
	public int doStartTag() throws JspException {
		
		httpRequest = (HttpServletRequest) pageContext.getRequest();
		renderRequest = (RenderRequest) httpRequest.getAttribute("javax.portlet.request");
		renderResponse = (RenderResponse) httpRequest.getAttribute("javax.portlet.response");
		TracerSingleton.log(SpagoBIConstants.NAME_MODULE, TracerSingleton.DEBUG, 
				            "ScriptWizardTag::doStartTag:: invoked");
		StringBuffer output = new StringBuffer();
		
		output.append("<table width='100%' cellspacing='0' border='0'>\n");
		output.append("	<tr>\n");
		output.append("		<td class='titlebar_level_2_text_section' style='vertical-align:middle;'>\n");
		output.append("			&nbsp;&nbsp;&nbsp;"+ PortletUtilities.getMessage("SBIDev.javaClassWiz.title", "messages") +"\n");
		output.append("		</td>\n");
		output.append("		<td class='titlebar_level_2_empty_section'>&nbsp;</td>\n");
		output.append("		<td class='titlebar_level_2_button_section'>\n");
		output.append("			<a style='text-decoration:none;' href='javascript:opencloseJavaWizardInfo()'> \n");
		output.append("				<img width='22px' height='22px'\n");
		output.append("				 	 src='" + renderResponse.encodeURL(renderRequest.getContextPath() + "/img/info22.jpg")+"'\n");
		output.append("					 name='info'\n");
		output.append("					 alt='"+PortletUtilities.getMessage("SBIDev.javaClassWiz.SintaxLbl", "messages")+"'\n");
		output.append("					 title='"+PortletUtilities.getMessage("SBIDev.javaClassWiz.SintaxLbl", "messages")+"'/>\n");
		output.append("			</a>\n");
		output.append("		</td>\n");
		String urlImgProfAttr = renderResponse.encodeURL(renderRequest.getContextPath() + "/img/profileAttributes22.jpg");
		output.append(generateProfAttrTitleSection(urlImgProfAttr));
		output.append("	</tr>\n");
		output.append("</table>\n");
		
		output.append("<br/>\n");
		
		output.append("<div class='div_detail_area_forms_lov'>\n");
		output.append("	<div class='div_detail_label_lov'>\n");
		String scriptLbl = PortletUtilities.getMessage("SBIDev.javaClassWiz.javaClassNameLbl", "messages");
		output.append("			<span class='portlet-form-field-label'>\n");
		output.append(scriptLbl);
		output.append("			</span>\n");
		output.append("	</div>\n");
		output.append("	<div class='div_detail_form'>\n");
	    output.append("		<input type='text' id='javaClassName' name='javaClassName' size='50' onchange='setLovProviderModified(true)' class='portlet-form-input-field' value='" + javaClassName + "' maxlength='100'/>&nbsp;*\n");
	    output.append("	</div>\n");
	    output.append("</div>\n");
		
	    output.append("<script>\n");
		output.append("		var infowizardjavaopen = false;\n");
		output.append("		var winJWT = null;\n");
		output.append("		function opencloseJavaWizardInfo() {\n");
		output.append("			if(!infowizardjavaopen){\n");
		output.append("				infowizardjavaopen = true;");
		output.append("				openJavaWizardInfo();\n");
		output.append("			}\n");
		output.append("		}\n");
		output.append("		function openJavaWizardInfo(){\n");
		output.append("			if(winJWT==null) {\n");
		output.append("				winJWT = new Window('winJWTInfo', {className: \"alphacube\", title:\""+PortletUtilities.getMessage("SBIDev.javaClassWiz.SintaxLbl", "messages")+"\", minWidth:150, destroyOnClose: false});\n");
		output.append("         	winJWT.setContent('javawizardinfodiv', true, false);\n");
		output.append("         	winJWT.showCenter(false);\n");
		output.append("		    } else {\n");
		output.append("         	winJWT.showCenter(false);\n");
		output.append("		    }\n");
		output.append("		}\n");
		output.append("		observerJWT = { onClose: function(eventName, win) {\n");
		output.append("			if (win == winJWT) {\n");
		output.append("				infowizardjavaopen = false;");
		output.append("			}\n");
		output.append("		  }\n");
		output.append("		}\n");
		output.append("		Windows.addObserver(observerJWT);\n");
		output.append("</script>\n");
		
		output.append("<div id='javawizardinfodiv' style='display:none;'>\n");	
		output.append(PortletUtilities.getMessageTextFromResource("it/eng/spagobi/presentation/tags/info/jclasswizardinfo"));
		output.append("</div>\n");
		
        try {
            pageContext.getOut().print(output.toString());
        }
        catch (Exception ex) {
            TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.CRITICAL, "ScriptWizardTag::doStartTag::", ex);
            throw new JspException(ex.getMessage());
        }
		
		return SKIP_BODY;
	}
		
    public int doEndTag() throws JspException {
        TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, "ScriptWizardTag::doEndTag:: invocato");
        return super.doEndTag();
    }
	
	
	public String getJavaClassName() {
		return javaClassName;
	}
	public void setJavaClassName(String javaClassName) {
		this.javaClassName = javaClassName;
	}
}
