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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

// TODO: Auto-generated Javadoc
/**
 * The Class ButtonTag.
 * 
 * @author Andrea Gioia
 */
public class ButtonTag extends QbeTagSupport {

	/** The id. */
	private String id;
	
	/** The type. */
	private String type;
	
	/** The name. */
	private String name;
	
	/** The tooltip. */
	private String tooltip;
	
	/** The image. */
	private String image;
	
	/** The parameters. */
	private Map parameters;
	
	/** The p names. */
	private List pNames;
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
	 */
	public int doStartTag() throws JspException {
		parameters = new HashMap();
		pNames = new ArrayList();
		return EVAL_BODY_INCLUDE;
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	public int doEndTag() throws JspException {
		
		StringBuffer buffer = new StringBuffer();
		try {			
			
			
			
			String url;
			if(type.equalsIgnoreCase("action")){
				parameters.put("ACTION_NAME", name);
				for(int i = 0; i < pNames.size(); i++) {
					String pName = (String)pNames.get(i);
					String pValue = (String)parameters.get(pName);
					parameters.put(pName, pValue);
				}
				
				url = getActionUrl(parameters);
				
				buffer.append("<a id='" + id + "'href='" + url + "'>");
				buffer.append("		<img src='" + image + "'\n");
				buffer.append("			 alt='" + tooltip + "'\n");
				buffer.append("			 title='" + tooltip + "'/>\n");
				buffer.append("</a>\n");	
			} else if(type.equalsIgnoreCase("script")){
					url = "javascript:" + name + "(";
					for(int i = 0; i < pNames.size(); i++) {
						String pName = (String)pNames.get(i);
						String pValue = (String)parameters.get(pName);
						url += (i!=0?",":"") +  pValue;
					}
					url += ")";
					
					buffer.append("	<img src='" + image + "'\n);");
					buffer.append("		 alt='" + tooltip + "'\n");
					buffer.append("		 title='" + tooltip + "'\n");
					buffer.append("		 onclick=\"" + url + "\"/>\n");					
			}
							    		   		
	    	  
	    	pageContext.getOut().print( buffer.toString() );
	    } catch (Exception ex) {
	    	throw new JspTagException(getClass().getName() + ": " + ex.getMessage());
	    }
		
		return EVAL_PAGE;
	}

	

	/**
	 * Adds the parameter.
	 * 
	 * @param name the name
	 * @param value the value
	 */
	public void addParameter(String name, String value) {
		
		if(name != null) {
			parameters.put(name, value);
			pNames.add(name);
		} else {
			parameters.put("p" + pNames.size(), value);
			pNames.add("p" + pNames.size());
		}
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
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
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

	/**
	 * Gets the image.
	 * 
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * Sets the image.
	 * 
	 * @param image the new image
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * Gets the parameters.
	 * 
	 * @return the parameters
	 */
	public Map getParameters() {
		return parameters;
	}

	/**
	 * Sets the parameters.
	 * 
	 * @param parameters the new parameters
	 */
	public void setParameters(Map parameters) {
		this.parameters = parameters;
	}

	/**
	 * Gets the p names.
	 * 
	 * @return the p names
	 */
	public List getPNames() {
		return pNames;
	}

	/**
	 * Sets the p names.
	 * 
	 * @param names the new p names
	 */
	public void setPNames(List names) {
		pNames = names;
	}

	
}

