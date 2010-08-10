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
import it.eng.spagobi.utilities.PortletUtilities;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Presentation tag for Script details. 
 * 
 * @author Zerbetto
 */

public class ScriptWizardTag extends TagSupport {
	
	private String script;
	
	public int doStartTag() throws JspException {
		TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, "ScriptWizardTag::doStartTag:: invocato");
		StringBuffer output = new StringBuffer();
		
		output.append("<div class='div_detail_area_forms_lov'>\n");
		output.append("	<div class='div_detail_label_lov'>\n");
		String scriptLbl = PortletUtilities.getMessage("SBIDev.scriptWiz.scriptLbl", "messages");
		output.append("			<span class='portlet-form-field-label'>\n");
		output.append(scriptLbl);
		output.append("			</span>\n");
		output.append("	</div>\n");
		output.append("	<div class='div_detail_form' style='height:185px;'>\n");
	    output.append("		<textarea id='script' name='script' onchange='setLovProviderModified(true)' class='portlet-text-area-field' rows='10' cols='50'>" + script + "</textarea>\n");
	    output.append("	</div>\n");
	    
    
	    /*
	    output.append("	<div class='div_detail_label_lov'>\n");
	    String outputTypeLbl = PortletUtilities.getMessage("SBIDev.scriptWiz.outputType", "messages");
		output.append("			<span class='portlet-form-field-label'>\n");
		output.append(outputTypeLbl);
		output.append("			</span>\n");
		output.append("	</div>\n");
		output.append("	<div class='div_detail_form' style='height:45px;'>\n");
        output.append("		<input type='radio' name='numberout' onchange='setLovProviderModified(true)' value='single' " + selectedsingle + ">\n");
		String singleValLbl = PortletUtilities.getMessage("SBIDev.scriptWiz.SingleValLbl", "messages");
		output.append("				<span class='portlet-form-field-label'>"+ singleValLbl + "</span>\n");
		output.append("			</input>\n");
		output.append("			<br/>\n");
        output.append("			<input type='radio' name='numberout' onchange='setLovProviderModified(true)' value='list' " + selectedlist + ">\n");
		String listValLbl = PortletUtilities.getMessage("SBIDev.scriptWiz.ListValLbl", "messages");
		output.append("				<span class='portlet-form-field-label'>"+ listValLbl + "</span>\n");
		output.append("			</input>\n");
		output.append("	</div>\n");
		*/
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
	
	
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
}
