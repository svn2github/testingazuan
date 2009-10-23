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
package it.eng.spagobi.engines.qbe.template;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import it.eng.spagobi.utilities.assertion.Assert;


/**
 * The Class QbeTemplate.
 * 
 * @author Andrea Gioia
 */
public class QbeJSONTemplateParser implements IQbeTemplateParser {
	


	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(QbeJSONTemplateParser.class);
	
    public QbeTemplate parse(Object template) {
    	Assert.assertNotNull(template, "Input parameter [template] cannot be null");
    	Assert.assertTrue(template instanceof JSONObject, "Input parameter [template] cannot be of type [" + template.getClass().getName() + "]");
    	return parse((JSONObject)template);
    }
	
	private QbeTemplate parse(JSONObject template) {
		
		QbeTemplate qbeTemplate = null;
		
		
		
		try {
			
			qbeTemplate = new QbeTemplate();
			
			JSONObject qbeConf = template.getJSONObject("qbeConf");
			String datamartName = qbeConf.getString("datamartName");
			qbeTemplate.addDatamartName(datamartName);
			qbeTemplate.setProperty("query", qbeConf.getString("query"));
			
			logger.debug("Templete parsed succesfully");
		} catch(Throwable t) {
			throw new QbeTemplateParseException("Impossible to parse tempate [" + template.toString()+ "]", t);
		} finally {
			logger.debug("OUT");
		}	
		
		return qbeTemplate;
	}
}
