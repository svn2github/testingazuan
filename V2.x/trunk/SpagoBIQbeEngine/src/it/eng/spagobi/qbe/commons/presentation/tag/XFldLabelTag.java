/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.spagobi.qbe.commons.presentation.tag;

import it.eng.qbe.utility.JsTreeUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

// TODO: Auto-generated Javadoc
/**
 * The Class XFldLabelTag.
 * 
 * @author Andrea Gioia
 */
public class XFldLabelTag extends QbeTagSupport {
	
	/** The var. */
	private String var;
	
	/** The field name. */
	private String fieldName;
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
	 */
	public int doStartTag() throws JspException {
		
		try {
			String label = JsTreeUtils.getLabelForQueryField(getRequestContainer(), getDatamartModel(), getDatamartWizard() ,fieldName);
			pageContext.setAttribute(var, label);
	      } catch (Exception ex) {
	         throw new JspTagException(getClass().getName() + ": " + ex.getMessage());
	      }
	      return SKIP_BODY;
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	public int doEndTag() {
		return EVAL_PAGE;
	}

	/**
	 * Gets the field name.
	 * 
	 * @return the field name
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * Sets the field name.
	 * 
	 * @param fieldName the new field name
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * Gets the var.
	 * 
	 * @return the var
	 */
	public String getVar() {
		return var;
	}

	/**
	 * Sets the var.
	 * 
	 * @param var the new var
	 */
	public void setVar(String var) {
		this.var = var;
	}

}

