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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

// TODO: Auto-generated Javadoc
/**
 * The Class UrlTag.
 * 
 * @author Andrea Gioia
 */
public class UrlTag extends QbeTagSupport {
	
	/** The ref. */
	private String ref;
	
	/** The var. */
	private String var;
	
	/** The type. */
	private String type; // resource | action
	
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
	 */
	public int doStartTag() throws JspException {
		
		
		try {		
			if(type.equalsIgnoreCase("resource")) {
				pageContext.setAttribute(var, getResourceUrl(ref));
			} else if(type.equalsIgnoreCase("action")) {
				Map params = new HashMap();
				if(ref != null) {
					params.put("ACTION_NAME", ref);
				}
				pageContext.setAttribute(var, getActionUrl(params));
			}
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

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the ref.
	 * 
	 * @return the ref
	 */
	public String getRef() {
		return ref;
	}

	/**
	 * Sets the ref.
	 * 
	 * @param ref the new ref
	 */
	public void setRef(String ref) {
		this.ref = ref;
	}

	

}

