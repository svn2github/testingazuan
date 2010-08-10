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
import it.eng.spagobi.bo.lov.FixedListDetail;
import it.eng.spagobi.bo.lov.FixedListItemDetail;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.PortletUtilities;

import java.net.URLDecoder;
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
			output.append("			<input class='portlet-form-input-field' type='text' id='valueOfFixedLovItemNew' name='valueOfFixedLovItemNew' size='50' value=''>&nbsp;*\n");
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
			List lovs = new ArrayList();
			if (lovProvider != null  &&  !lovProvider.equals("")){
				//lovProvider = GeneralUtilities.substituteQuotesIntoString(lovProvider);
				lovs = FixedListDetail.fromXML(lovProvider).getLovs();
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
		  	output.append("		<td colspan='1' width='20' class='portlet-section-header'>&nbsp;\n");
		  	output.append("		</td>\n");
		  	output.append("		<td colspan='1' width='20' class='portlet-section-header'>&nbsp;\n");
		  	output.append("		</td>\n");
		  	output.append("		<td colspan='1' width='18' class='portlet-section-header'>&nbsp;\n");
		  	output.append("		</td>\n");
		  	output.append("	</tr>\n");
			if (lovs != null) {
				output.append("		<input type='hidden' id='indexOfFixedLovItemToDelete' name='' value=''/>\n");
				output.append("		<input type='hidden' id='indexOfFixedLovItemToChange' name='' value=''/>\n");
				output.append("		<input type='hidden' id='indexOfItemToDown' name='' value=''/>\n");
				output.append("		<input type='hidden' id='indexOfItemToUp' name='' value=''/>\n");
				boolean alternate = false;
		        String rowClass;
				for (int i = 0; i < lovs.size(); i++) {
					FixedListItemDetail lovDet = (FixedListItemDetail) lovs.get(i); 
					String name = lovDet.getName();
					String description = lovDet.getDescription();
					
					//before sending name and description to the hidden input,
					//substitute single and double quotes with their html encoding
					name = GeneralUtilities.substituteQuotesIntoString(name);
					description= GeneralUtilities.substituteQuotesIntoString(description);
					String prova = GeneralUtilities.substituteQuotesIntoString("aaaa'aaa");
					output.append("		<input type='hidden' name='nameOfFixedListItem' value='" + name + "'/>\n");
					output.append("		<input type='hidden' id='valueItem'  name='valueOfFixedListItem' value='"+description+"'/>\n");
					rowClass = (alternate) ? "portlet-section-alternate" : "portlet-section-body";
		            alternate = !alternate;
					output.append("	<tr class='portlet-font'>\n");
					String descrDec = URLDecoder.decode(description,"UTF-8");
					output.append("		<td class='" + rowClass + "'>");
					output.append("          <span style='display:inline;' id='nameRow"+i+"'>"+name+"</span>");
					output.append("          <input type='text' style='display:none;' id='nameRow"+i+"InpText' name='nameRow"+i+"InpText' value='"+name+"' />");
					output.append("     </td>\n");
					output.append("		<td class='" + rowClass + "'>");
					output.append("          <span style='display:inline;' id='descrRow"+i+"'>"+descrDec+"</span>");
					output.append("          <input type='text' style='display:none;' id='descrRow"+i+"InpText' name='descrRow"+i+"InpText' value='"+descrDec+"' />");
					output.append("     </td>\n");
					
					output.append("		<td class='" + rowClass + "'>\n");
					String tooltipRowDetail = PortletUtilities.getMessage("SBIDev.lovWiz.tableCol3", "messages");
					String tooltipRowSave = PortletUtilities.getMessage("SBIDev.lovWiz.tableCol3.1", "messages");
					output.append("			<div style='display:inline;' id='divBtnDetailRow"+i+"'>");
					output.append("				<a href='javascript:changeRowValues(\""+ i +"\")'>");
					output.append("				<img class ='portlet-menu-item' \n");
					output.append("					src= '" + renderResponse.encodeURL(renderRequest.getContextPath() + "/img/detail.gif") + "' \n");
					output.append("					title='" + tooltipRowDetail + "' alt='" + tooltipRowDetail + "' />\n");
					output.append("				</a>");
					output.append("			</div>");
					output.append("			<div style='display:none;' id='divBtnSaveRow"+i+"'>");
					output.append("				<input type='image' onclick='saveRowValues(\""+ i +"\")' class ='portlet-menu-item' \n");
					output.append("					src= '" + renderResponse.encodeURL(renderRequest.getContextPath() + "/img/save16.gif") + "' \n");
					output.append("					title='" + tooltipRowSave + "' alt='" + tooltipRowSave + "' />\n");
					output.append("			</div>");
					output.append("		</td>\n");
					
					
					output.append("		<td class='" + rowClass + "'>\n");
					String tableCol4 = PortletUtilities.getMessage("SBIDev.lovWiz.tableCol4", "messages");
					output.append("			<input type='image' onclick='setIndexOfFixedLovItemToDelete(\""+ i +"\")' class ='portlet-menu-item' \n");
					output.append("				src= '" + renderResponse.encodeURL(renderRequest.getContextPath() + "/img/erase.gif") + "' \n");
					output.append("				title='" + tableCol4 + "' alt='" + tableCol4 + "' />\n");
		  			output.append("		</td>\n");
		  			
		  			output.append("		<td class='" + rowClass + "'>\n");
		  			if(i<(lovs.size()-1)) {
						String tableCol5 = PortletUtilities.getMessage("SBIDev.lovWiz.tableCol5", "messages");
						output.append("			<input type='image' onclick='downRow(\""+ i +"\")' class ='portlet-menu-item' \n");
						output.append("				src= '" + renderResponse.encodeURL(renderRequest.getContextPath() + "/img/down16.gif") + "' \n");
						output.append("				title='" + tableCol5 + "' alt='" + tableCol5 + "' />\n");
		  			} else {
		  				output.append("	        &nbsp;");
		  			}
		  			output.append("		</td>\n");
		  			
		  			output.append("		<td class='" + rowClass + "'>\n");
		  			if(i>0) {
						String tableCol6 = PortletUtilities.getMessage("SBIDev.lovWiz.tableCol6", "messages");
						output.append("			<input type='image' onclick='upRow(\""+ i +"\")' class ='portlet-menu-item' \n");
						output.append("				src= '" + renderResponse.encodeURL(renderRequest.getContextPath() + "/img/up16.gif") + "' \n");
						output.append("				title='" + tableCol6 + "' alt='" + tableCol6 + "' />\n");
		  			} else {
		  				output.append("	        &nbsp;");
		  			}
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
			
			output.append(" function changeRowValues(index) {\n");
			output.append("		document.getElementById('nameRow'+index).style.display = 'none';\n");
			output.append("		document.getElementById('descrRow'+index).style.display = 'none';\n");
			output.append("		document.getElementById('nameRow'+index+'InpText').style.display = 'inline';\n");
			output.append("		document.getElementById('descrRow'+index+'InpText').style.display = 'inline';\n");
			output.append("		document.getElementById('divBtnDetailRow'+index).style.display = 'none';\n");
			output.append("		document.getElementById('divBtnSaveRow'+index).style.display = 'inline';\n");
			output.append(" }\n");
			
			output.append(" function saveRowValues(i) {\n");
			output.append("		document.getElementById('indexOfFixedLovItemToChange').name = 'indexOfFixedLovItemToChange';\n");
			output.append("		document.getElementById('indexOfFixedLovItemToChange').value = i;\n");
			output.append(" }\n");
			
			output.append(" function downRow(i) {\n");
			output.append("		document.getElementById('indexOfItemToDown').name = 'indexOfItemToDown';\n");
			output.append("		document.getElementById('indexOfItemToDown').value = i;\n");
			output.append(" }\n");
			
			output.append(" function upRow(i) {\n");
			output.append("		document.getElementById('indexOfItemToUp').name = 'indexOfItemToUp';\n");
			output.append("		document.getElementById('indexOfItemToUp').value = i;\n");
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
