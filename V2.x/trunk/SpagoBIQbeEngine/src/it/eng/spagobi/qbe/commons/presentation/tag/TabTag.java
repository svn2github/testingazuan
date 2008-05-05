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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

// TODO: Auto-generated Javadoc
/**
 * The Class TabTag.
 * 
 * @author Andrea Gioia
 */
public class TabTag extends QbeTagSupport {
	
	/** The id. */
	private String id;
	
	/** The msg. */
	private String msg;
	
	/** The label. */
	private String label;
	
	/** The tooltip. */
	private String tooltip;
	
	/** The CALLBAC k_ script. */
	private final String CALLBACK_SCRIPT = "vediSchermo";
	
	/** The DI v_ class. */
	private final String DIV_CLASS = "tab";
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
	 */
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
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	public int doEndTag() {
		return EVAL_PAGE;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#getId()
	 */
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#setId(java.lang.String)
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the msg.
	 * 
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * Sets the msg.
	 * 
	 * @param msg the new msg
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * Gets the label.
	 * 
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets the label.
	 * 
	 * @param label the new label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Gets the tooltip.
	 * 
	 * @return the tooltip
	 */
	public String getTooltip() {
		return tooltip;
	}

	/**
	 * Sets the tooltip.
	 * 
	 * @param tooltip the new tooltip
	 */
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

}

