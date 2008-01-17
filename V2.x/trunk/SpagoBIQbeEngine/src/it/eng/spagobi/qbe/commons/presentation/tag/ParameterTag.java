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
public class ParameterTag extends QbeTagSupport {
	
	private String name;
	private String value;
	
	public int doStartTag() throws JspException {
		
		try {
			ButtonTag buttonTag = (ButtonTag)getParent();
			buttonTag.addParameter(name, value);
	    } catch (Exception ex) {
	    	throw new JspTagException(getClass().getName() + ": " + ex.getMessage());
	    }
	    
	    return SKIP_BODY;
	}
	
	public int doEndTag() {
		return EVAL_PAGE;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}

