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
package it.eng.spagobi.geo.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import it.eng.spago.base.SourceBean;
import it.eng.spago.tracing.TracerSingleton;

/**
 * @author Andrea Gioia
 *
 */
public class DatamartProviderConfiguration {
	private String className;
	private String connectionName;
	private String query;
	private String columnId;
	private String targetFeatureName;
	private String[] kpiColumnNames;
	private Properties parameters;
	private String hierarchyType;
	private Map hierarchyLevelsToColumnMap;
	private List hierarchyLevels;
	private int aggregationLevel;
	
	
	public static final String DEFAULT_CALSS_NAME = "it.eng.spagobi.geo.datamart.DefaultDatamartProvider";
	
	public DatamartProviderConfiguration (SourceBean datamartProviderConfigurationSB) throws ConfigurationException {

		// get the class name attribute
		String className = (String)datamartProviderConfigurationSB.getAttribute(Constants.CLASS_NAME_ATTR);
		// set the default datamart provider class_name (if not already specified) 
		if(className == null) {
			TracerSingleton.log(Constants.LOG_NAME, 
        			TracerSingleton.WARNING, 
        			"DatamartProviderConfiguration :: service : " +
        			"cannot find datamart provider class name: attribute name " + Constants.CLASS_NAME_ATTR);
			TracerSingleton.log(Constants.LOG_NAME, 
        			TracerSingleton.INFORMATION, 
        			"DatamartProviderConfiguration :: service : " +
        			"default datamart provider class will be used: class name " + DEFAULT_CALSS_NAME);			
			
			className = DEFAULT_CALSS_NAME;
		}
		setClassName(className);
		
		
		// get the connection name attribute
		String connectionName = (String)datamartProviderConfigurationSB.getAttribute(Constants.CONNECTION_NAME_ATTR);
		if(connectionName == null) {
			TracerSingleton.log(Constants.LOG_NAME, 
        			TracerSingleton.CRITICAL,
        			"DatamartProviderConfiguration :: service : " +
        			"cannot find datamart provider's connection name: attribute name " + Constants.CONNECTION_NAME_ATTR);
			throw new ConfigurationException("cannot load DATAMART PROVIDER's connection name: attribute name " + Constants.CONNECTION_NAME_ATTR);
		}
		setConnectionName(connectionName);		
		
		
		// get the query attribute
		String query = (String)datamartProviderConfigurationSB.getAttribute(Constants.QUERY_ATTR);
		if(query == null) {
			TracerSingleton.log(Constants.LOG_NAME, 
        			TracerSingleton.CRITICAL,
        			"DatamartProviderConfiguration :: service : " +
        			"cannot find datamart provider's query: attribute name " + Constants.QUERY_ATTR);
			throw new ConfigurationException("cannot load DATAMART PROVIDER's query: attribute name " + Constants.QUERY_ATTR);
		}
		setQuery(query);
		
		//	get the columnid attribute
		String columnId = (String)datamartProviderConfigurationSB.getAttribute(Constants.COLUMN_ID_ATRR);
		if(columnId == null) {
			TracerSingleton.log(Constants.LOG_NAME, 
        			TracerSingleton.CRITICAL,
        			"DatamartProviderConfiguration :: service : " +
        			"cannot find datamart provider's query: attribute name " + Constants.COLUMN_ID_ATRR);
			throw new ConfigurationException("cannot load DATAMART PROVIDER's column id: attribute name " + Constants.COLUMN_ID_ATRR);
		}
		setColumnId(columnId);	
		
		
		//	get the targetFeatureName attribute
		String targetFeatureName = (String)datamartProviderConfigurationSB.getAttribute(Constants.TARGET_FEATURE_NAME_ATRR);
		if(targetFeatureName == null) {
			TracerSingleton.log(Constants.LOG_NAME, 
        			TracerSingleton.CRITICAL,
        			"DatamartProviderConfiguration :: service : " +
        			"cannot find datamart provider's targetFeatureName: attribute name " + Constants.TARGET_FEATURE_NAME_ATRR);
			throw new ConfigurationException("cannot load DATAMART PROVIDER's targetFeature: attribute name " + Constants.TARGET_FEATURE_NAME_ATRR);
		}
		setTargetFeatureName(targetFeatureName);	
		
		// get the kpiColumnNames attribute
		String kpiColumnNameStr = (String)datamartProviderConfigurationSB.getAttribute(Constants.KPI_COLUMN_NAMES_ATRR);
		if(kpiColumnNameStr == null) {
			TracerSingleton.log(Constants.LOG_NAME, 
        			TracerSingleton.CRITICAL,
        			"DatamartProviderConfiguration :: service : " +
        			"cannot find datamart provider's targetFeatureName: attribute name " + Constants.KPI_COLUMN_NAMES_ATRR);
			throw new ConfigurationException("cannot load DATAMART PROVIDER's kpiColumnNames attribute name " + Constants.KPI_COLUMN_NAMES_ATRR);
		}
		String[] kpiColumnName = kpiColumnNameStr.split(",");
		setKpiColumnNames(kpiColumnName);		
		
		// get hierarchy configuration settings
		SourceBean hierarchyConfigurationSB = (SourceBean)datamartProviderConfigurationSB.getAttribute(Constants.HIERARCHY_TAG);
		if(hierarchyConfigurationSB == null) {
			TracerSingleton.log(Constants.LOG_NAME, 
        			TracerSingleton.WARNING,
        			"DatamartProviderConfiguration :: service : " +
        			"cannot find datamart provider's hierarchy configuration: tag name " + Constants.HIERARCHY_TAG);
			TracerSingleton.log(Constants.LOG_NAME, 
        			TracerSingleton.INFORMATION,
        			"DatamartProviderConfiguration :: service : " +
        			"Default hierachy definition will be used. Aggregation level will be set too zero");
			hierarchyType = "default";
			aggregationLevel = 0;
		} else {
			readHierarchyConfiguration(hierarchyConfigurationSB);
		}		
	}
	
	public void readHierarchyConfiguration(SourceBean hierarchyConfigurationSB){
		String hierarchyType = (String)hierarchyConfigurationSB.getAttribute(Constants.HIERARCHY_TYPE_ATRR);
		if(hierarchyType == null || (!hierarchyType.equalsIgnoreCase("default") && !hierarchyType.equalsIgnoreCase("custom"))) {
			if(hierarchyType == null) {
				TracerSingleton.log(Constants.LOG_NAME, 
	        			TracerSingleton.WARNING,
	        			"DatamartProviderConfiguration :: service : " +
	        			"cannot find datamart provider's hierarchy type: attribute name " + Constants.HIERARCHY_TYPE_ATRR);
			} else {
				TracerSingleton.log(Constants.LOG_NAME, 
	        			TracerSingleton.WARNING,
	        			"DatamartProviderConfiguration :: service : " +
	        			"invalid value for provider's hierarchy type attribute: invalid attribute value " + hierarchyType);
			}
			TracerSingleton.log(Constants.LOG_NAME, 
        			TracerSingleton.INFORMATION,
        			"DatamartProviderConfiguration :: service : " +
        			"Default hierachy definition will be used");
			
			hierarchyType = "default";
		}
		setHierarchyType(hierarchyType);
		
		String aggregationLevelStr = (String)hierarchyConfigurationSB.getAttribute(Constants.HIERARCHY_AGG_LEVEL_ATRR);
		if(aggregationLevelStr == null) {
			TracerSingleton.log(Constants.LOG_NAME, 
	        		TracerSingleton.WARNING,
	        		"DatamartProviderConfiguration :: service : " +
	        		"cannot find datamart provider's hierarchy level: attribute name " + Constants.HIERARCHY_AGG_LEVEL_ATRR);
			
			TracerSingleton.log(Constants.LOG_NAME, 
        			TracerSingleton.INFORMATION,
        			"DatamartProviderConfiguration :: service : " +
        			"Hierachy level will be set to zero");
		
			aggregationLevelStr = "0";
		}
		
		int aggregationLevel = 0;
		try {
			aggregationLevel = Integer.parseInt(aggregationLevelStr);
		} catch(Exception e){
			TracerSingleton.log(Constants.LOG_NAME, 
        			TracerSingleton.WARNING,
        			"DatamartProviderConfiguration :: service : " +
        			"Attribute hierarchy level is not a valid number: atribute value " + aggregationLevelStr);
			TracerSingleton.log(Constants.LOG_NAME, 
        			TracerSingleton.INFORMATION,
        			"DatamartProviderConfiguration :: service : " +
        			"Hierachy level will be set to zero");
		}		
		setAggregationLevel(aggregationLevel);
		
		if(getHierarchyType().equalsIgnoreCase("custom")) {
			readHierarchyLevelsConfiguration(hierarchyConfigurationSB);
		}
	}
	
	private void readHierarchyLevelsConfiguration(SourceBean hierarchyConfigurationSB) {
		hierarchyLevels = new ArrayList();
		hierarchyLevelsToColumnMap = new HashMap();
		List list = hierarchyConfigurationSB.getAttributeAsList(Constants.LEVEL_TAG);
		for(int i = 0; i < list.size(); i++) {
			SourceBean levelSB = (SourceBean)list.get(i);
			String name = (String)levelSB.getAttribute(Constants.LEVEL_NAME_ATTR);
			String column = (String)levelSB.getAttribute(Constants.LEVEL_COLUMN_ATTR);
			hierarchyLevels.add(name);
			hierarchyLevelsToColumnMap.put(name, column);
		}
	}
	
	public String getAggregationColumnName() {
		String levelName = (String)hierarchyLevels.get(getAggregationLevel());
		return (String)hierarchyLevelsToColumnMap.get(levelName);
	}
	
	public String getAggregationLevelName() {
		return (String)hierarchyLevels.get(getAggregationLevel());
	}
	
	
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getColumnId() {
		return columnId;
	}
	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}
	public String getConnectionName() {
		return connectionName;
	}
	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}
	public String[] getKpiColumnNames() {
		return kpiColumnNames;
	}
	public void setKpiColumnNames(String[] kpiColumnNames) {
		this.kpiColumnNames = kpiColumnNames;
	}
	public String getQuery() {
		return query;
	}
	public String getExecutableQuery() {
		String executableQuery;
		
		executableQuery = query;
		if(executableQuery.indexOf("${") == -1) return executableQuery;
		while(executableQuery.indexOf("${")!=-1) {
			int startInd = executableQuery.indexOf("${") + 2;
			int endInd = executableQuery.indexOf("}", startInd);
			String paramName = executableQuery.substring(startInd, endInd);
			String paramValue = parameters.getProperty(paramName);
			if(paramValue==null) {
				TracerSingleton.log(Constants.LOG_NAME, 
	        			TracerSingleton.CRITICAL,
	        			"DatamartProviderConfiguration :: service : " +
	        			"cannot find in service request a valid value for parameter: parameter name " + paramName);
				
				paramValue = "";
			}
			executableQuery = executableQuery.substring(0, startInd - 2) + paramValue + executableQuery.substring(endInd + 1);
		}
		return executableQuery;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getTargetFeatureName() {
		return targetFeatureName;
	}
	public void setTargetFeatureName(String targetFeatureName) {
		this.targetFeatureName = targetFeatureName;
	}
	public Properties getParameters() {
		return parameters;
	}






	public void setParameters(Properties parameters) {
		this.parameters = parameters;
	}

	public int getAggregationLevel() {
		return aggregationLevel;
	}

	public void setAggregationLevel(int aggregationLevel) {
		this.aggregationLevel = aggregationLevel;
	}

	public String getHierarchyType() {
		return hierarchyType;
	}

	public void setHierarchyType(String hierarchyType) {
		this.hierarchyType = hierarchyType;
	}
	
	
}
