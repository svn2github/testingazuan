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
import org.json.JSONArray;
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
    
    public static String STATIC_XOR_FILTERS_PREFIX = "xorFilter-";
    public static String STATIC_XOR_OPTIONS_PREFIX = "option-";
    public static String STATIC_ON_OFF_FILTERS_PREFIX = "onOffFilter-";
    public static String OPEN_FILTERS_PREFIX = "openFilter-";
    public static String DYNAMIC_FILTERS_PREFIX = "dynamicFilter-";
    public static String GROUPING_VARIABLE_PREFIX = "groupingVariable-";
    
	
    public QbeTemplate parse(Object template) {
    	Assert.assertNotNull(template, "Input parameter [template] cannot be null");
    	Assert.assertTrue(template instanceof JSONObject, "Input parameter [template] cannot be of type [" + template.getClass().getName() + "]");
    	return parse((JSONObject)template);
    }
	
	private QbeTemplate parse(JSONObject template) {
		logger.debug("IN: input template: " + template);
		QbeTemplate qbeTemplate = null;
		try {
			
			qbeTemplate = new QbeTemplate();
			addAdditionalInfo(template);
			logger.debug("Modified template: " + template);
			qbeTemplate.setProperty("jsonTemplate", template);
			
			JSONObject qbeConf = template.optJSONObject("qbeConf");
			JSONArray datamartsName = (JSONArray) qbeConf.get("datamartsName");
			for (int i = 0; i < datamartsName.length(); i++ ) {
				String aDatamartName = (String) datamartsName.get(i);
				qbeTemplate.addDatamartName(aDatamartName);
			}
			qbeTemplate.setProperty("query", qbeConf.getString("query"));
			
			logger.debug("Templete parsed succesfully");
		} catch(Throwable t) {
			throw new QbeTemplateParseException("Impossible to parse tempate [" + template.toString()+ "]", t);
		} finally {
			logger.debug("OUT");
		}	
		
		return qbeTemplate;
	}

	private void addAdditionalInfo(JSONObject template) {
		logger.debug("IN");
		try {
			JSONArray staticClosedFilters = template.optJSONArray("staticClosedFilters");
			int xorFiltersCounter = 1;
			int onOffFiltersCounter = 1;
			if (staticClosedFilters != null && staticClosedFilters.length() > 0) {
				for (int i = 0; i < staticClosedFilters.length(); i++) {
					JSONObject aStaticClosedFilter = (JSONObject) staticClosedFilters.get(i);
					if (aStaticClosedFilter.getBoolean("singleSelection")) {
						// xor filter
						aStaticClosedFilter.put("id", STATIC_XOR_FILTERS_PREFIX + xorFiltersCounter++);
						JSONArray options = aStaticClosedFilter.getJSONArray("filters");
						for (int j = 0; j < options.length(); j++) {
							JSONObject anOption = (JSONObject) options.get(j);
							anOption.put("id", STATIC_XOR_OPTIONS_PREFIX + (j+1));
						}
					} else {
						// on off filter
						JSONArray filters = aStaticClosedFilter.getJSONArray("filters");
						for (int j = 0; j < filters.length(); j++) {
							JSONObject anOption = (JSONObject) filters.get(j);
							anOption.put("id", STATIC_ON_OFF_FILTERS_PREFIX + onOffFiltersCounter++);
						}
					}
				}
			}
			
			JSONArray staticOpenFilters = template.optJSONArray("staticOpenFilters");
			if (staticOpenFilters != null && staticOpenFilters.length() > 0) {
				for (int i = 0; i < staticOpenFilters.length(); i++) {
					JSONObject aStaticOpenFilter = (JSONObject) staticOpenFilters.get(i);
					aStaticOpenFilter.put("id", OPEN_FILTERS_PREFIX + (i+1));
				}
			}
			
			JSONArray dynamicFilters = template.optJSONArray("dynamicFilters");
			if (dynamicFilters != null && dynamicFilters.length() > 0) {
				for (int i = 0; i < dynamicFilters.length(); i++) {
					JSONObject aDynamicFilter = (JSONObject) dynamicFilters.get(i);
					aDynamicFilter.put("id", DYNAMIC_FILTERS_PREFIX + (i+1));
				}
			}
			
			JSONArray groupingVariables = template.optJSONArray("groupingVariables");
			if (groupingVariables != null && groupingVariables.length() > 0) {
				for (int i = 0; i < groupingVariables.length(); i++) {
					JSONObject aGroupingVariable = (JSONObject) groupingVariables.get(i);
					aGroupingVariable.put("id", GROUPING_VARIABLE_PREFIX + (i+1));
				}
			}
			
		} catch(Throwable t) {
			throw new QbeTemplateParseException("Cannot parse template [" + template.toString()+ "]", t);
		} finally {
			logger.debug("OUT");
		}
	}
}
