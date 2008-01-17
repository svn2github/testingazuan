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
import it.eng.qbe.wizard.ISelectField;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.RequestContainerAccess;
import it.eng.spago.base.RequestContainerPortletAccess;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.ResponseContainerAccess;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.configuration.ConfigSingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author Andrea Gioia
 *
 */
public class ButtonTag extends QbeTagSupport {

	private String id;
	private String type;
	private String name;
	private String tooltip;
	private String image;
	
	private Map parameters;
	private List pNames;
	
	public int doStartTag() throws JspException {
		parameters = new HashMap();
		pNames = new ArrayList();
		return EVAL_BODY_INCLUDE;
	}
	
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

	

	public void addParameter(String name, String value) {
		
		if(name != null) {
			parameters.put(name, value);
			pNames.add(name);
		} else {
			parameters.put("p" + pNames.size(), value);
			pNames.add("p" + pNames.size());
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Map getParameters() {
		return parameters;
	}

	public void setParameters(Map parameters) {
		this.parameters = parameters;
	}

	public List getPNames() {
		return pNames;
	}

	public void setPNames(List names) {
		pNames = names;
	}

	
}

