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
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.ChannelUtilities;
import it.eng.spagobi.utilities.messages.IMessageBuilder;
import it.eng.spagobi.utilities.messages.MessageBuilderFactory;
import it.eng.spagobi.utilities.urls.IUrlBuilder;
import it.eng.spagobi.utilities.urls.UrlBuilderFactory;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

/**
 * Presentation tag for Query Wizard details. 
 */
public class QueryWizardTag extends CommonWizardLovTag {

	private HttpServletRequest httpRequest = null;
    protected RequestContainer requestContainer = null;
	protected ResponseContainer responseContainer = null;
	protected IUrlBuilder urlBuilder = null;
    protected IMessageBuilder msgBuilder = null;
    private String connectionName;
    private String queryDef;
	
	
	public String getConnectionName() {
		return connectionName;
	}
	
	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}
	
	public String getQueryDef() {
		return queryDef;
	}
	
	public void setQueryDef(String queryDef) {
		this.queryDef = queryDef;
	}
	
	public int doEndTag() throws JspException {
        TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, "QueryWizardTag::doEndTag:: invocato");
        return super.doEndTag();
    }
	
	public int doStartTag() throws JspException {
		TracerSingleton.log(SpagoBIConstants.NAME_MODULE, TracerSingleton.DEBUG, 
				            "QueryWizardTag::doStartTag:: invoked");
		httpRequest = (HttpServletRequest) pageContext.getRequest();
		requestContainer = ChannelUtilities.getRequestContainer(httpRequest);
		responseContainer = ChannelUtilities.getResponseContainer(httpRequest);
		urlBuilder = UrlBuilderFactory.getUrlBuilder();
		msgBuilder = MessageBuilderFactory.getMessageBuilder();
		String connNameField = msgBuilder.getMessage("SBIDev.queryWiz.connNameField", "messages", httpRequest);
		String queryDefField = msgBuilder.getMessage("SBIDev.queryWiz.queryDefField", "messages", httpRequest);
		ConfigSingleton config = ConfigSingleton.getInstance();
		List dbConnection = config.getAttributeAsList("DATA-ACCESS.CONNECTION-POOL");
		Iterator itDbCon = dbConnection.iterator();
		
		StringBuffer output = new StringBuffer();
		
		output.append("<table width='100%' cellspacing='0' border='0'>\n");
		output.append("	<tr>\n");
		output.append("		<td class='titlebar_level_2_text_section' style='vertical-align:middle;'>\n");
		output.append("			&nbsp;&nbsp;&nbsp;"+ msgBuilder.getMessage("SBIDev.queryWiz.wizardTitle", "messages", httpRequest) +"\n");
		output.append("		</td>\n");
		output.append("		<td class='titlebar_level_2_empty_section'>&nbsp;</td>\n");
		output.append("		<td class='titlebar_level_2_button_section'>\n");
		output.append("			<a style='text-decoration:none;' href='javascript:opencloseQueryWizardInfo()'> \n");
		output.append("				<img width='22px' height='22px'\n");
		output.append("				 	 src='" + urlBuilder.getResourceLink(httpRequest, "/img/info22.jpg")+"'\n");
		output.append("					 name='info'\n");
		output.append("					 alt='"+msgBuilder.getMessage("SBIDev.queryWiz.showSintax", "messages", httpRequest)+"'\n");
		output.append("					 title='"+msgBuilder.getMessage("SBIDev.queryWiz.showSintax", "messages", httpRequest)+"'/>\n");
		output.append("			</a>\n");
		output.append("		</td>\n");
		String urlImgProfAttr = urlBuilder.getResourceLink(httpRequest, "/img/profileAttributes22.jpg");
		output.append(generateProfAttrTitleSection(urlImgProfAttr));
		output.append("	</tr>\n");
		output.append("</table>\n");
		
		output.append("<br/>\n");
		
	    output.append("<div class='div_detail_area_forms_lov'>\n");	
	    output.append("		<div class='div_detail_label_lov'>\n");
		output.append("			<span class='portlet-form-field-label'>\n");
		output.append(connNameField);
		output.append("			</span>\n");
		output.append("		</div>\n");
		output.append("		<div class='div_detail_form'>\n");
		output.append("			<select onchange='setLovProviderModified(true);' style='width:180px;' class='portlet-form-input-field' name='connName' id='connName' >\n");
		while (itDbCon.hasNext()) {
			SourceBean connectionPool = (SourceBean) itDbCon.next();
			String connectionPoolName = (String) connectionPool.getAttribute("connectionPoolName");
			String connectionDescription = (String) connectionPool.getAttribute("connectionDescription");
			if (connectionDescription == null || connectionDescription.trim().equals("")) connectionDescription = connectionPoolName;
			String connNameSelected = "";
			if (connectionPoolName.equals(connectionName)) connNameSelected = "selected=\"selected\"";
			output.append("			<option value='" + connectionPoolName + "' " + connNameSelected + ">" + connectionDescription + "</option>\n");
		}
		output.append("			</select>\n");
		output.append("		</div>\n");
		output.append("		<div class='div_detail_label_lov'>\n");
		output.append("			<span class='portlet-form-field-label'>\n");
		output.append(queryDefField);
		output.append("			</span>\n");
		output.append("		</div>\n");
		output.append("		<div style='height:110px;' class='div_detail_form'>\n");
		output.append("			<textarea style='height:100px;' class='portlet-text-area-field' name='queryDef' onchange='setLovProviderModified(true);'  cols='50'>" + queryDef + "</textarea>\n");
		output.append("		</div>\n");
		output.append("		<div class='div_detail_label_lov'>\n");
		output.append("			&nbsp;\n");
		output.append("		</div>\n");
		output.append("</div>\n");
	    
		
		output.append("<script>\n");
		output.append("		var infowizardqueryopen = false;\n");
		output.append("		var winQWT = null;\n");
		output.append("		function opencloseQueryWizardInfo() {\n");
		output.append("			if(!infowizardqueryopen){\n");
		output.append("				infowizardqueryopen = true;");
		output.append("				openQueryWizardInfo();\n");
		output.append("			}\n");
		output.append("		}\n");
		output.append("		function openQueryWizardInfo(){\n");
		output.append("			if(winQWT==null) {\n");
		output.append("				winQWT = new Window('winQWTInfo', {className: \"alphacube\", title:\""+msgBuilder.getMessage("SBIDev.queryWiz.showSintax", "messages", httpRequest)+"\", width:680, height:150, destroyOnClose: false});\n");
		output.append("         	winQWT.setContent('querywizardinfodiv', false, false);\n");
		output.append("         	winQWT.showCenter(false);\n");
		output.append("		    } else {\n");
		output.append("         	winQWT.showCenter(false);\n");
		output.append("		    }\n");
		output.append("		}\n");
		output.append("		observerQWT = { onClose: function(eventName, win) {\n");
		output.append("			if (win == winQWT) {\n");
		output.append("				infowizardqueryopen = false;");
		output.append("			}\n");
		output.append("		  }\n");
		output.append("		}\n");
		output.append("		Windows.addObserver(observerQWT);\n");
		output.append("</script>\n");
		
		output.append("<div id='querywizardinfodiv' style='display:none;'>\n");	
		output.append(msgBuilder.getMessageTextFromResource("it/eng/spagobi/presentation/tags/info/querywizardinfo"));
		output.append("</div>\n");	
		
        try {
            pageContext.getOut().print(output.toString());
        }
        catch (Exception ex) {
            TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.CRITICAL, "QueryWizardTag::doStartTag::", ex);
            throw new JspException(ex.getMessage());
        }
		return SKIP_BODY;
	}
	
	
}
