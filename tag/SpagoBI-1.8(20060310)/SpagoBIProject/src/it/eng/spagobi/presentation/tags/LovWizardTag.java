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
import it.eng.spagobi.bo.LovDetail;
import it.eng.spagobi.bo.LovDetailList;
import it.eng.spagobi.utilities.PortletUtilities;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Presentation tag for Fix Lov Wizard details. 
 * 
 * @author Zerbetto
 */

public class LovWizardTag extends TagSupport {

	private String lovProvider;
	
	private HttpServletRequest httpRequest = null;
    protected RenderRequest renderRequest = null;
    protected RenderResponse renderResponse = null;
	
	public int doStartTag() throws JspException {
		TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, "LovWizardTag::doStartTag:: invocato");
		httpRequest = (HttpServletRequest) pageContext.getRequest();
		renderRequest = (RenderRequest) httpRequest.getAttribute("javax.portlet.request");
		renderResponse = (RenderResponse) httpRequest.getAttribute("javax.portlet.response");
		StringBuffer output = new StringBuffer();
		
		try {
			
			String newItemNameField = PortletUtilities.getMessage("SBIDev.lovWiz.newItemNameField", "messages");
			String newItemValueField = PortletUtilities.getMessage("SBIDev.lovWiz.newItemValueField", "messages");
			output.append("<input type='hidden' id='insertFixLovItem' name='' value=''/>\n");
			output.append("<div class='div_detail_area_forms_lov'>\n");	
			output.append("		<div class='div_detail_label_lov'>\n");
			output.append("			<span class='portlet-form-field-label'>\n");
			output.append(newItemNameField);
			output.append("			</span>\n");
			output.append("		</div>\n");
			output.append("		<div class='div_detail_form'>\n");
			output.append("			<input class='portlet-form-input-field' type='text' name='nameOfFixedLovItemNew' size='50' value=''/>&nbsp;*\n");
			output.append("		</div>\n");
			output.append("		<div class='div_detail_label_lov'>\n");
			output.append("			<span class='portlet-form-field-label'>\n");
			output.append(newItemValueField);
			output.append("			</span>\n");
			output.append("		</div>\n");
			output.append("		<div class='div_detail_form'>\n");
			output.append("			<input class='portlet-form-input-field' type='text' name='valueOfFixedLovItemNew' size='50' value=''>&nbsp;*\n");
			output.append("		</div>\n");
			output.append("		<div class='div_detail_label_lov'>\n");
			output.append("			&nbsp;\n");
			output.append("		</div>\n");
			output.append("		<div class='div_detail_form'>\n");
			output.append("			<input type='image' name='insertFixLovItem' value='insertFixLovItem'\n");
			output.append("				src='" + renderResponse.encodeURL(renderRequest.getContextPath() + "/img/attach.gif") + "'\n");
			String addButtMsg = PortletUtilities.getMessage("SBIDev.lovWiz.addButt", "messages");
			output.append("				title='" + addButtMsg + "' alt='" + addButtMsg + "'\n");
			output.append("			/>\n");
			output.append("			<a href='javascript:newFixLovItemFormSubmit();' class='portlet-form-field-label' style='text-decoration:none;'>\n");
			output.append("				" + addButtMsg + "\n");
			output.append("			</a>\n");
			output.append("		</div>\n");
			output.append("</div>\n");
			
			/*
			output.append("<table class='object-details-table'>\n");
			output.append("	<tr height='25'>\n");
			String newItemNameField = PortletUtilities.getMessage("SBIDev.lovWiz.newItemNameField", "messages");
			output.append("		<td align='right' class='portlet-form-field-label' > " + newItemNameField + "</td>\n");
			output.append("		<td>&nbsp;</td>\n");
			output.append("		<td>\n");
			output.append("			<input class='portlet-form-input-field' type='text' name='nameOfFixedLovItemNew' size='50' value=''>&nbsp;*\n");
			output.append("			&nbsp;&nbsp;&nbsp;");
			output.append("			<input type='image' name='insertFixLovItem' value='insertFixLovItem'\n");
			output.append("				src='" + renderResponse.encodeURL(renderRequest.getContextPath() + "/img/attach.gif") + "'\n");
			String addButtMsg = PortletUtilities.getMessage("SBIDev.lovWiz.addButt", "messages");
			output.append("				title='" + addButtMsg + "' alt='" + addButtMsg + "'\n");
			output.append("			/>\n");
			output.append("		</td>\n");
			output.append("	</tr>\n");
			output.append("	<tr height='25'>\n");
			String newItemValueField = PortletUtilities.getMessage("SBIDev.lovWiz.newItemValueField", "messages");
			output.append("		<td align='right' class='portlet-form-field-label' > " + newItemValueField + "</td>\n");
			output.append("		<td>&nbsp;</td>\n");
			output.append("		<td>\n");
			output.append("			<input class='portlet-form-input-field' type='text' name='valueOfFixedLovItemNew' size='50' value=''>&nbsp;*\n");
			output.append("		</td>\n");
			output.append("	</tr>\n");
			output.append("</table>\n");
			*/
			//output.append("<br/>\n");
			
			List lovs = new ArrayList();
			if (lovProvider != null  &&  !lovProvider.equals("")){
				lovs = LovDetailList.fromXML(lovProvider).getLovs();
			}
			output.append("<table class=\"table_detail_fix_lov\">\n");
		  	output.append("	<tr>\n");
		  	output.append("		<td colspan='1' class='portlet-section-header'>\n");
		  	String tableCol1 = PortletUtilities.getMessage("SBIDev.lovWiz.tableCol1", "messages");
		  	output.append(			tableCol1 + "\n");
		  	output.append("		</td>\n");
		  	output.append("		<td colspan='1' class='portlet-section-header'>\n");
		  	String tableCol2 = PortletUtilities.getMessage("SBIDev.lovWiz.tableCol2", "messages");
		  	output.append(			tableCol2 + "\n");
		  	output.append("		</td>\n");
		  	output.append("		<td colspan='1' width='20' class='portlet-section-header'>&nbsp;\n");
		  	output.append("		</td>\n");
		  	output.append("	</tr>\n");
			if (lovs != null) {
				output.append("		<input type='hidden' id='indexOfFixedLovItemToDelete' name='' value=''/>\n");
				boolean alternate = false;
		        String rowClass;
				for (int i = 0; i < lovs.size(); i++) {
					LovDetail lovDet = (LovDetail) lovs.get(i); 
					String name = lovDet.getName();
					String description = lovDet.getDescription();
					
					output.append("		<input type='hidden' name='nameOfFixedListItem' value='" + name + "'/>\n");
					output.append("		<input type='hidden' name='valueOfFixedListItem' value='" + description + "'/>\n");
					
					rowClass = (alternate) ? "portlet-section-alternate" : "portlet-section-body";
		            alternate = !alternate;
					output.append("	<tr class='portlet-font'>\n");
					
					output.append("		<td class='" + rowClass + "'>" + name + "</td>\n");
					output.append("		<td class='" + rowClass + "'>" + description + "</td>\n");
					output.append("		<td class='" + rowClass + "'>\n");
					String tableCol3 = PortletUtilities.getMessage("SBIDev.lovWiz.tableCol3", "messages");
					output.append("			<input type='image' onclick='setIndexOfFixedLovItemToDelete(\""+ i +"\")' class ='portlet-menu-item' \n");
					output.append("				src= '" + renderResponse.encodeURL(renderRequest.getContextPath() + "/img/erase.gif") + "' \n");
					output.append("				title='" + tableCol3 + "' alt='" + tableCol3 + "' />\n");
		  			output.append("		</td>\n");
		  			output.append("	</tr>\n");
		  		}
		  	}
		  				 
			output.append("</table>\n");
			output.append("<script>\n");
			output.append(" function setIndexOfFixedLovItemToDelete (i) {\n");
			output.append("		document.getElementById('indexOfFixedLovItemToDelete').name = 'indexOfFixedLovItemToDelete';\n");
			output.append("		document.getElementById('indexOfFixedLovItemToDelete').value = i;\n");
			output.append(" }\n");
			output.append(" function newFixLovItemFormSubmit () {\n");
			output.append("		document.getElementById('insertFixLovItem').name = 'insertFixLovItem';\n");
			output.append("		document.getElementById('insertFixLovItem').value = 'insertFixLovItem';\n");
			output.append("		document.getElementById('modalitiesValueForm').submit();\n");
			output.append(" }\n");
			output.append("</script>\n");
            pageContext.getOut().print(output.toString());
        }
        catch (Exception ex) {
            TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.CRITICAL, "LovWizardTag::doStartTag::", ex);
            throw new JspException(ex.getMessage());
        }
	    
		return SKIP_BODY;
	}
	
    public int doEndTag() throws JspException {
        TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, "LovWizardTag::doEndTag:: invocato");
        return super.doEndTag();
    }
	
	public String getLovProvider() {
		return lovProvider;
	}

	public void setLovProvider(String lovProvider) {
		this.lovProvider = lovProvider;
	}
	
}
