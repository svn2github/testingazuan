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
package it.eng.spagobi.qbe.commons.presentation.tag;

import it.eng.qbe.conf.QbeEngineConf;
import it.eng.qbe.model.IQuery;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.RequestContainerAccess;
import it.eng.spago.base.RequestContainerPortletAccess;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.ResponseContainerAccess;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.configuration.ConfigSingleton;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author Andrea Gioia
 *
 */
public class TabTag extends QbeTagSupport {
	
	private String id;
	private String msg;
	private String label;
	private String tooltip;
	
	private final String CALLBACK_SCRIPT = "vediSchermo";
	private final String DIV_CLASS = "tab";
	
	public int doStartTag() throws JspException {
		
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<div class='" + DIV_CLASS + "' id='" + id + "'>");
			buffer.append("		<a href=\"javascript:" + CALLBACK_SCRIPT +"('" + msg + "','" + id +"')\"\n");
			buffer.append("		   title='" + tooltip +"'\n");
			buffer.append("		   style='color:black;'\n");
			buffer.append("		>");	   
			buffer.append("		" + label);	
			buffer.append("		</a>");
			buffer.append("</div>");
	    	  
	    	pageContext.getOut().print( buffer.toString() );
	      } catch (Exception ex) {
	         throw new JspTagException(getClass().getName() + ": " + ex.getMessage());
	      }
	      return SKIP_BODY;
	}
	
	public int doEndTag() {
		return EVAL_PAGE;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

}

