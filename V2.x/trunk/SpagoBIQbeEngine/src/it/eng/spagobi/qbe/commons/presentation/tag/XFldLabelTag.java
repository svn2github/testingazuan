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

import it.eng.qbe.utility.JsTreeUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * @author Andrea Gioia
 *
 */
public class XFldLabelTag extends QbeTagSupport {
	
	private String var;
	private String fieldName;
	
	public int doStartTag() throws JspException {
		
		try {
			String label = JsTreeUtils.getLabelForQueryField(getRequestContainer(), getDatamartModel(), getQuery() ,fieldName);
			pageContext.setAttribute(var, label);
	      } catch (Exception ex) {
	         throw new JspTagException(getClass().getName() + ": " + ex.getMessage());
	      }
	      return SKIP_BODY;
	}
	
	public int doEndTag() {
		return EVAL_PAGE;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

}

