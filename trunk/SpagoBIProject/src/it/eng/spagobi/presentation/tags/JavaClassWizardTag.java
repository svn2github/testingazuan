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
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.ChannelUtilities;
import it.eng.spagobi.utilities.messages.IMessageBuilder;
import it.eng.spagobi.utilities.messages.MessageBuilderFactory;
import it.eng.spagobi.utilities.urls.IUrlBuilder;
import it.eng.spagobi.utilities.urls.UrlBuilderFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

/**
 * Presentation tag for Script details. 
 */

public class JavaClassWizardTag extends CommonWizardLovTag {
	
	private HttpServletRequest httpRequest = null;
	protected RequestContainer requestContainer = null;
	protected ResponseContainer responseContainer = null;
	protected IUrlBuilder urlBuilder = null;
    protected IMessageBuilder msgBuilder = null;
	private String javaClassName;
	
	public int doStartTag() throws JspException {
		
		httpRequest = (HttpServletRequest) pageContext.getRequest();
		requestContainer = ChannelUtilities.getRequestContainer(httpRequest);
		responseContainer = ChannelUtilities.getResponseContainer(httpRequest);
		urlBuilder = UrlBuilderFactory.getUrlBuilder();
		msgBuilder = MessageBuilderFactory.getMessageBuilder();
		TracerSingleton.log(SpagoBIConstants.NAME_MODULE, TracerSingleton.DEBUG, 
				            "ScriptWizardTag::doStartTag:: invoked");
		StringBuffer output = new StringBuffer();
		
		output.append("<table width='100%' cellspacing='0' border='0'>\n");
		output.append("	<tr>\n");
		output.append("		<td class='titlebar_level_2_text_section' style='vertical-align:middle;'>\n");
		output.append("			&nbsp;&nbsp;&nbsp;"+ msgBuilder.getMessage("SBIDev.javaClassWiz.title", "messages", httpRequest) +"\n");
		output.append("		</td>\n");
		output.append("		<td class='titlebar_level_2_empty_section'>&nbsp;</td>\n");
		output.append("		<td class='titlebar_level_2_button_section'>\n");
		output.append("			<a style='text-decoration:none;' href='javascript:opencloseJavaWizardInfo()'> \n");
		output.append("				<img width='22px' height='22px'\n");
		output.append("				 	 src='" + urlBuilder.getResourceLink(httpRequest, "/img/info22.jpg")+"'\n");
		output.append("					 name='info'\n");
		output.append("					 alt='"+msgBuilder.getMessage("SBIDev.javaClassWiz.SintaxLbl", "messages", httpRequest)+"'\n");
		output.append("					 title='"+msgBuilder.getMessage("SBIDev.javaClassWiz.SintaxLbl", "messages", httpRequest)+"'/>\n");
		output.append("			</a>\n");
		output.append("		</td>\n");
		String urlImgProfAttr = urlBuilder.getResourceLink(httpRequest, "/img/profileAttributes22.jpg");
		output.append(generateProfAttrTitleSection(urlImgProfAttr));
		output.append("	</tr>\n");
		output.append("</table>\n");
		
		output.append("<br/>\n");
		
		output.append("<div class='div_detail_area_forms_lov'>\n");
		output.append("	<div class='div_detail_label_lov'>\n");
		String scriptLbl = msgBuilder.getMessage("SBIDev.javaClassWiz.javaClassNameLbl", "messages", httpRequest);
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
		output.append("				winJWT = new Window('winJWTInfo', {className: \"alphacube\", title:\""+msgBuilder.getMessage("SBIDev.javaClassWiz.SintaxLbl", "messages", httpRequest)+"\", minWidth:150, destroyOnClose: false});\n");
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
		output.append(msgBuilder.getMessageTextFromResource("it/eng/spagobi/presentation/tags/info/jclasswizardinfo"));
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
