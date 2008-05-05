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
package it.eng.qbe.urlgenerator;



import it.eng.spagobi.qbe.commons.urlgenerator.IQbeUrlGenerator;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;



// TODO: Auto-generated Javadoc
/**
 * The Class SelectClassForWizardURLGenerator.
 * 
 * @author Andrea Zoppello
 * 
 * This implementation of IURLGenerator is responsible for generating URL to
 * select a class
 */
public class SelectClassForWizardURLGenerator implements IURLGenerator{

	/** The qbe url generator. */
	private IQbeUrlGenerator qbeUrlGenerator = null;
	
	/** The http request. */
	private HttpServletRequest httpRequest = null;
	
	/**
	 * Instantiates a new select class for wizard url generator.
	 * 
	 * @param qbeUrlGenerator the qbe url generator
	 * @param httpRequest the http request
	 */
	public SelectClassForWizardURLGenerator (IQbeUrlGenerator qbeUrlGenerator, HttpServletRequest httpRequest ){
		this.qbeUrlGenerator = qbeUrlGenerator;;
		this.httpRequest = httpRequest;
	}
	
	/**
	 * Generate url.
	 * 
	 * @param source the source
	 * 
	 * @return the string
	 * 
	 * @see it.eng.qbe.utility.javascript.IURLGenerator#generateURL(java.lang.Object)
	 */
	public String generateURL(Object source) {
		
		Map params = new HashMap();
		
		params.put("ACTION_NAME","SELECT_CLASS_ACTION");
		params.put("className", source.toString());
		params.put("NEW_SESSION", "TRUE");
		
		return qbeUrlGenerator.getActionUrl(httpRequest, params);
		
	}

	/**
	 * Generate url.
	 * 
	 * @param source the source
	 * @param addtionalParameter the addtional parameter
	 * 
	 * @return the string
	 * 
	 * @see it.eng.qbe.javascript.IURLGenerator#generateURL(java.lang.Object, java.lang.Object)
	 */
	public String generateURL(Object source, Object addtionalParameter) {
		return generateURL(source);
	}
	
	/**
	 * Generate url.
	 * 
	 * @param source the source
	 * @param source2 the source2
	 * @param addtionalParameter the addtional parameter
	 * 
	 * @return the string
	 * 
	 * @see it.eng.qbe.javascript.IURLGenerator#generateURL(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	public String generateURL(Object source, Object source2, Object addtionalParameter) {
		return generateURL(source);
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.urlgenerator.IURLGenerator#getClassName()
	 */
	public String getClassName() {
		return null;
	}
}
