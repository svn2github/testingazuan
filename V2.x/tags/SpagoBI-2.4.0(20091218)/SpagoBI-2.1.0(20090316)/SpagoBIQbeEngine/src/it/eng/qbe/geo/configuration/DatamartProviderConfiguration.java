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
package it.eng.qbe.geo.configuration;

import it.eng.spago.base.SourceBean;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.utilities.callbacks.mapcatalogue.MapCatalogueAccessUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

// TODO: Auto-generated Javadoc
/**
 * The Class DatamartProviderConfiguration.
 * 
 * @author Andrea Gioia
 */
public class DatamartProviderConfiguration {
	
	/** The parent configuration. */
	private MapConfiguration parentConfiguration;
	
	/** The class name. */
	private String className;
	
	/** The connection name. */
	private String connectionName;
	
	/** The query. */
	private String query;
	
	/** The column id. */
	private String columnId;
	
	/** The hierarchy name. */
	private String hierarchyName;
	
	/** The hierarchy base level. */
	private String hierarchyBaseLevel;
	
	/** The hierarchy level. */
	private String hierarchyLevel;
	
	/** The kpi column names. */
	private String[] kpiColumnNames;
	
	/** The kpi agregation functins. */
	private String[] kpiAgregationFunctins;
		
	/** The parameters. */
	private Properties parameters;
	
	/** The hierarchy map. */
	private Map hierarchyMap;
	
	
	/** The Constant DEFAULT_CALSS_NAME. */
	public static final String DEFAULT_CALSS_NAME = "it.eng.spagobi.geo.datamart.DefaultDatamartProvider";
	
	/**
	 * Instantiates a new datamart provider configuration.
	 * 
	 * @param parentConfiguration the parent configuration
	 */
	public DatamartProviderConfiguration (MapConfiguration parentConfiguration) {
		this.parentConfiguration = parentConfiguration;
		this.hierarchyMap = new HashMap();
	}
	
	/**
	 * Instantiates a new datamart provider configuration.
	 * 
	 * @param parentConfiguration the parent configuration
	 * @param datamartProviderConfigurationSB the datamart provider configuration sb
	 * 
	 * @throws ConfigurationException the configuration exception
	 */
	public DatamartProviderConfiguration (MapConfiguration parentConfiguration, SourceBean datamartProviderConfigurationSB) throws ConfigurationException {

		setParentConfiguration(parentConfiguration);
		
		// get the class name attribute
		String className = (String)datamartProviderConfigurationSB.getAttribute(Constants.DP_CLASS_NAME_ATTR);
		// set the default datamart provider class_name (if not already specified) 
		if(className == null) {
			TracerSingleton.log(Constants.LOG_NAME, 
        			TracerSingleton.WARNING, 
        			"DatamartProviderConfiguration :: service : " +
        			"cannot find datamart provider class name: attribute name " + Constants.DP_CLASS_NAME_ATTR);
			TracerSingleton.log(Constants.LOG_NAME, 
        			TracerSingleton.INFORMATION, 
        			"DatamartProviderConfiguration :: service : " +
        			"default datamart provider class will be used: class name " + DEFAULT_CALSS_NAME);			
			
			className = DEFAULT_CALSS_NAME;
		}
		setClassName(className);
		
		
		// get the connection name attribute
		String connectionName = (String)datamartProviderConfigurationSB.getAttribute(Constants.DP_CONNECTION_NAME_ATTR);
		if(connectionName == null) {
			TracerSingleton.log(Constants.LOG_NAME, 
        			TracerSingleton.CRITICAL,
        			"DatamartProviderConfiguration :: service : " +
        			"cannot find datamart provider's connection name: attribute name " + Constants.DP_CONNECTION_NAME_ATTR);
			throw new ConfigurationException("cannot load DATAMART PROVIDER's connection name: attribute name " + Constants.DP_CONNECTION_NAME_ATTR);
		}
		setConnectionName(connectionName);		
		
		
		// get the query attribute
		String query = (String)datamartProviderConfigurationSB.getAttribute(Constants.DP_QUERY_ATTR);
		if(query == null) {
			TracerSingleton.log(Constants.LOG_NAME, 
        			TracerSingleton.CRITICAL,
        			"DatamartProviderConfiguration :: service : " +
        			"cannot find datamart provider's query: attribute name " + Constants.DP_QUERY_ATTR);
			throw new ConfigurationException("cannot load DATAMART PROVIDER's query: attribute name " + Constants.DP_QUERY_ATTR);
		}
		setQuery(query);
		
		//	get the columnid attribute
		String columnId = (String)datamartProviderConfigurationSB.getAttribute(Constants.DP_COLUMN_ID_ATRR);
		if(columnId == null) {
			TracerSingleton.log(Constants.LOG_NAME, 
        			TracerSingleton.CRITICAL,
        			"DatamartProviderConfiguration :: service : " +
        			"cannot find datamart provider's query: attribute name " + Constants.DP_COLUMN_ID_ATRR);
			throw new ConfigurationException("cannot load DATAMART PROVIDER's column id: attribute name " + Constants.DP_COLUMN_ID_ATRR);
		}
		setColumnId(columnId);	
		
		
		//	get the hierarchyName attribute
		String hierarchyName = (String)datamartProviderConfigurationSB.getAttribute(Constants.DP_HIERARCHY_NAME_ATRR);
		if(hierarchyName == null) {
			TracerSingleton.log(Constants.LOG_NAME, 
        			TracerSingleton.CRITICAL,
        			"DatamartProviderConfiguration :: service : " +
        			"cannot find datamart provider's targetFeatureName: attribute name " + Constants.DP_HIERARCHY_NAME_ATRR);
			throw new ConfigurationException("cannot load DATAMART PROVIDER's hierarchyName: attribute name " + Constants.DP_HIERARCHY_NAME_ATRR);
		}
		setHierarchyName(hierarchyName);	
		
		// get the hierarchyBaseLevel attribute
		String hierarchyBaseLevel = (String)datamartProviderConfigurationSB.getAttribute(Constants.DP_HIERARCHY_BASE_LEVEL_ATRR);
		if(hierarchyBaseLevel == null) {
			TracerSingleton.log(Constants.LOG_NAME, 
        			TracerSingleton.CRITICAL,
        			"DatamartProviderConfiguration :: service : " +
        			"cannot find datamart provider's targetFeatureName: attribute name " + Constants.DP_HIERARCHY_BASE_LEVEL_ATRR);
			throw new ConfigurationException("cannot load DATAMART PROVIDER's hierarchyBaseLevel: attribute name " + Constants.DP_HIERARCHY_BASE_LEVEL_ATRR);
		}
		setHierarchyBaseLevel(hierarchyBaseLevel);	
		
		//	get the hierarchyLevel attribute
		String hierarchyLevel = (String)datamartProviderConfigurationSB.getAttribute(Constants.DP_HIERARCHY_LEVEL_ATRR);
		if(hierarchyLevel == null) {
			TracerSingleton.log(Constants.LOG_NAME, 
        			TracerSingleton.CRITICAL,
        			"DatamartProviderConfiguration :: service : " +
        			"cannot find datamart provider's targetFeatureName: attribute name " + Constants.DP_HIERARCHY_LEVEL_ATRR);
			throw new ConfigurationException("cannot load DATAMART PROVIDER's hierarchyLevel: attribute name " + Constants.DP_HIERARCHY_LEVEL_ATRR);
		}
		setHierarchyLevel(hierarchyLevel);	
		
		
		
		// get the kpiColumnNames attribute
		String kpiColumnNameStr = (String)datamartProviderConfigurationSB.getAttribute(Constants.DP_KPI_COLUMN_NAMES_ATRR);
		if(kpiColumnNameStr == null) {
			TracerSingleton.log(Constants.LOG_NAME, 
        			TracerSingleton.CRITICAL,
        			"DatamartProviderConfiguration :: service : " +
        			"cannot find datamart provider's targetFeatureName: attribute name " + Constants.DP_KPI_COLUMN_NAMES_ATRR);
			throw new ConfigurationException("cannot load DATAMART PROVIDER's kpiColumnNames attribute name " + Constants.DP_KPI_COLUMN_NAMES_ATRR);
		}
		String[] kpiColumnName = kpiColumnNameStr.split(",");
		for(int i = 0; i < kpiColumnName.length; i++) {
			kpiColumnName[i] = kpiColumnName[i].trim();
		}
		setKpiColumnNames(kpiColumnName);		
		
		// get the kpiAggFuncs attribute
		String kpiAggFuncsStr = (String)datamartProviderConfigurationSB.getAttribute(Constants.DP_KPI_AGG_FUNCS_ATRR);
		if(kpiAggFuncsStr == null) {
			TracerSingleton.log(Constants.LOG_NAME, 
        			TracerSingleton.CRITICAL,
        			"DatamartProviderConfiguration :: service : " +
        			"cannot find datamart provider's targetFeatureName: attribute name " + Constants.DP_KPI_AGG_FUNCS_ATRR);
			throw new ConfigurationException("cannot load DATAMART PROVIDER's kpiAggFuncs attribute name " + Constants.DP_KPI_AGG_FUNCS_ATRR);
		}
		String[] kpiAggFuncs = kpiAggFuncsStr.split(",");
		setKpiAgregationFunctins(kpiAggFuncs);	
				
		// get hierarchy configuration settings
		hierarchyMap = new HashMap();
		SourceBean hierarchiesConfigurationSB = (SourceBean)datamartProviderConfigurationSB.getAttribute(Constants.HIERARCHIES_TAG);		
		
		Hierarchy hierarchy = null;
		List hierarchies = hierarchiesConfigurationSB.getAttributeAsList(Constants.HIERARCHY_TAG);
		for(int i = 0; i < hierarchies.size(); i++) {
			
			SourceBean hierarchySB = (SourceBean)hierarchies.get(i);
			String name = (String)hierarchySB.getAttribute(Constants.HIERARCHY_NAME_ATRR);
			String type = (String)hierarchySB.getAttribute(Constants.HIERARCHY_TYPE_ATRR);
			List levels = null;
			if(type.equalsIgnoreCase("custom"))  {
				hierarchy = new Hierarchy(name);
				levels =  hierarchySB.getAttributeAsList(Constants.HIERARCHY_LEVEL_TAG);
			} else {
				try {
					MapCatalogueAccessUtils mapCatalogueAccessUtils = MapConfiguration.getMapCatalogueAccessUtils();
					String sdtHierarchy = mapCatalogueAccessUtils.getStandardHierarchy();
					hierarchySB = SourceBean.fromXMLString(sdtHierarchy);
				} catch (Exception e) {
					throw new ConfigurationException("Impossible to obtain default hierarchy");
				}
				String table = (String)hierarchySB.getAttribute(Constants.HIERARCHY_TABLE_ATRR);
				hierarchy = new Hierarchy(name, table);
				levels = hierarchySB.getAttributeAsList(Constants.HIERARCHY_LEVEL_TAG);
			}
			
			for(int j = 0; j < levels.size(); j++) {
				SourceBean levelSB = (SourceBean)levels.get(j);
				String lname = (String)levelSB.getAttribute(Constants.HIERARCHY_LEVEL_NAME_ATRR);
				String lcolumnid = (String)levelSB.getAttribute(Constants.HIERARCHY_LEVEL_COLUMN_ID_ATRR);
				String lcolumndesc = (String)levelSB.getAttribute(Constants.HIERARCHY_LEVEL_COLUMN_DESC_ATRR);
				String lfeaturename = (String)levelSB.getAttribute(Constants.HIERARCHY_LEVEL_FEATURE_NAME_ATRR);
				Hierarchy.Level level = new Hierarchy.Level();
				level.setName(lname);
				level.setColumnId(lcolumnid);
				level.setColumnDesc(lcolumndesc);
				level.setFeatureName(lfeaturename);
				hierarchy.addLevel(level);
			}
			
			hierarchyMap.put(hierarchy.getName(), hierarchy);
		}			
	}
	
		
	/**
	 * Gets the class name.
	 * 
	 * @return the class name
	 */
	public String getClassName() {
		return className;
	}
	
	/**
	 * Sets the class name.
	 * 
	 * @param className the new class name
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	
	/**
	 * Gets the column id.
	 * 
	 * @return the column id
	 */
	public String getColumnId() {
		return columnId;
	}
	
	/**
	 * Sets the column id.
	 * 
	 * @param columnId the new column id
	 */
	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}
	
	/**
	 * Gets the connection name.
	 * 
	 * @return the connection name
	 */
	public String getConnectionName() {
		return connectionName;
	}
	
	/**
	 * Sets the connection name.
	 * 
	 * @param connectionName the new connection name
	 */
	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}
	
	/**
	 * Gets the kpi column names.
	 * 
	 * @return the kpi column names
	 */
	public String[] getKpiColumnNames() {
		return kpiColumnNames;
	}
	
	/**
	 * Sets the kpi column names.
	 * 
	 * @param kpiColumnNames the new kpi column names
	 */
	public void setKpiColumnNames(String[] kpiColumnNames) {
		this.kpiColumnNames = kpiColumnNames;
	}
	
	/**
	 * Gets the query.
	 * 
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}
	
	/**
	 * Gets the executable query.
	 * 
	 * @return the executable query
	 */
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
	
	/**
	 * Sets the query.
	 * 
	 * @param query the new query
	 */
	public void setQuery(String query) {
		this.query = query;
	}
	
	
	/**
	 * Gets the parameters.
	 * 
	 * @return the parameters
	 */
	public Properties getParameters() {
		return parameters;
	}

	/**
	 * Sets the parameters.
	 * 
	 * @param parameters the new parameters
	 */
	public void setParameters(Properties parameters) {
		this.parameters = parameters;
	}

	
	/**
	 * Gets the hierarchy level.
	 * 
	 * @return the hierarchy level
	 */
	public String getHierarchyLevel() {
		return hierarchyLevel;
	}

	/**
	 * Sets the hierarchy level.
	 * 
	 * @param hierarchyLevel the new hierarchy level
	 */
	public void setHierarchyLevel(String hierarchyLevel) {
		this.hierarchyLevel = hierarchyLevel;
	}

	/**
	 * Gets the hierarchy name.
	 * 
	 * @return the hierarchy name
	 */
	public String getHierarchyName() {
		return hierarchyName;
	}

	/**
	 * Sets the hierarchy name.
	 * 
	 * @param hierarchyName the new hierarchy name
	 */
	public void setHierarchyName(String hierarchyName) {
		this.hierarchyName = hierarchyName;
	}

	/**
	 * Gets the kpi agregation functins.
	 * 
	 * @return the kpi agregation functins
	 */
	public String[] getKpiAgregationFunctins() {
		return kpiAgregationFunctins;
	}

	/**
	 * Sets the kpi agregation functins.
	 * 
	 * @param kpiAgregationFunctins the new kpi agregation functins
	 */
	public void setKpiAgregationFunctins(String[] kpiAgregationFunctins) {
		this.kpiAgregationFunctins = kpiAgregationFunctins;
	}
	
	/**
	 * Adds the hieararchy.
	 * 
	 * @param hierarchy the hierarchy
	 */
	public void addHieararchy(Hierarchy hierarchy) {
		hierarchyMap.put(hierarchy.getName(), hierarchy);
	}
	
	/**
	 * Delete hieararchy.
	 * 
	 * @param hierarchyName the hierarchy name
	 */
	public void deleteHieararchy(String hierarchyName) {
		hierarchyMap.remove(hierarchyName);
	}
	
	/**
	 * Gets the hierarchy.
	 * 
	 * @param hierarchyName the hierarchy name
	 * 
	 * @return the hierarchy
	 */
	public Hierarchy getHierarchy(String hierarchyName) {
		return (Hierarchy)hierarchyMap.get(hierarchyName);
	}
	
	/**
	 * Gets the hierarchies.
	 * 
	 * @return the hierarchies
	 */
	public List getHierarchies() {
		List hierarchies = new ArrayList();
		Iterator it = hierarchyMap.keySet().iterator();
		while(it.hasNext()) hierarchies.add(hierarchyMap.get(it.next()));
		return hierarchies;
	}
	
	/**
	 * Gets the selected hierarchy.
	 * 
	 * @return the selected hierarchy
	 */
	public Hierarchy getSelectedHierarchy() {
		return getHierarchy(getHierarchyName());
	}
	
	/**
	 * Gets the selected level.
	 * 
	 * @return the selected level
	 */
	public Hierarchy.Level getSelectedLevel() {
		return getSelectedHierarchy().getLevel(getHierarchyLevel());
	}
	
	/**
	 * Gets the base level.
	 * 
	 * @return the base level
	 */
	public Hierarchy.Level getBaseLevel() {
		return getSelectedHierarchy().getLevel(getHierarchyBaseLevel());
	}
	
	
	
	
	/**
	 * The Class Hierarchy.
	 */
	public static class Hierarchy {
		
		/** The name. */
		private String name;
		
		/** The type. */
		private String type;
		
		/** The table. */
		private String table;
		
		/** The level list. */
		private List levelList;
		
		/** The level map. */
		private Map levelMap;
		
		/**
		 * Instantiates a new hierarchy.
		 * 
		 * @param name the name
		 */
		public Hierarchy(String name) {
			this.name = name;
			this.type = "custom";
			this.table = null;
			this.levelList = new ArrayList();
			this.levelMap = new HashMap();
		}
		
		/**
		 * Instantiates a new hierarchy.
		 * 
		 * @param name the name
		 * @param table the table
		 */
		public Hierarchy(String name , String table) {
			this.name = name;
			this.type = "default";
			this.table = table;
			this.levelList = new ArrayList();
			this.levelMap = new HashMap();
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
		 * Gets the table.
		 * 
		 * @return the table
		 */
		public String getTable() {
			return table;
		}
		
		/**
		 * Sets the table.
		 * 
		 * @param table the new table
		 */
		public void setTable(String table) {
			this.table = table;
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
		 * Checks if is default hierarchy.
		 * 
		 * @return true, if is default hierarchy
		 */
		public boolean isDefaultHierarchy() {
			return getType().equalsIgnoreCase("default");
		}
		
		/**
		 * Clear levels.
		 */
		public void clearLevels() {
			levelList = new ArrayList();
			levelMap = new HashMap();
		}
		
		/**
		 * Adds the level.
		 * 
		 * @param level the level
		 */
		public void addLevel(Level level) {
			if(getLevel(level.getName()) == null) {
				levelList.add(level);
			}
			levelMap.put(level.getName(), level);
		}
		
		/**
		 * Gets the level.
		 * 
		 * @param levelName the level name
		 * 
		 * @return the level
		 */
		public Level getLevel(String levelName) {
			return (Level)levelMap.get(levelName);
		}
		
		/**
		 * Gets the levels.
		 * 
		 * @return the levels
		 */
		public List getLevels() {
			return levelList;
		}
		
		/**
		 * Gets the sublevels.
		 * 
		 * @param levelName the level name
		 * 
		 * @return the sublevels
		 */
		public List getSublevels(String levelName) {
			List levels = new ArrayList();
			boolean isSubLevel = false;
			for(int i = 0; i < levelList.size(); i++) {
				Level level = (Level)levelList.get(i);
				if(isSubLevel) {
					levels.add(level);
				} else {
					if(level.getName().equalsIgnoreCase(levelName)) isSubLevel = true;
				}
			}
			
			return levels;
		}

		/**
		 * To xml.
		 * 
		 * @return the string
		 */
		public String toXml() {
			StringBuffer buffer = new StringBuffer();
			
			buffer.append("<HIERARCHY ");
			buffer.append("name=\"" + this.getName()+ "\" ");
			buffer.append("type=\"" + this.getType() + "\" ");
			
			if(isDefaultHierarchy()) {
				buffer.append("/>");
			} else {
				buffer.append(">");
				
				
				
				for(int i = 0; i < levelList.size(); i++) {
					Level level = (Level)levelList.get(i);
					buffer.append("\n" + level.toXml());
				}			
				
				buffer.append("\n</HIERARCHY>\n");
			}
			
			
			
			return buffer.toString();
		}
		
		/**
		 * The Class Level.
		 */
		public static class Level {
			
			/** The name. */
			private String name;
			
			/** The column id. */
			private String columnId;
			
			/** The column desc. */
			private String columnDesc;
			
			/** The feature name. */
			private String featureName;
			
			/**
			 * Gets the column desc.
			 * 
			 * @return the column desc
			 */
			public String getColumnDesc() {
				return columnDesc;
			}
			
			/**
			 * Sets the column desc.
			 * 
			 * @param columnDesc the new column desc
			 */
			public void setColumnDesc(String columnDesc) {
				this.columnDesc = columnDesc;
			}
			
			/**
			 * Gets the column id.
			 * 
			 * @return the column id
			 */
			public String getColumnId() {
				return columnId;
			}
			
			/**
			 * Sets the column id.
			 * 
			 * @param columnId the new column id
			 */
			public void setColumnId(String columnId) {
				this.columnId = columnId;
			}
			
			/**
			 * Gets the feature name.
			 * 
			 * @return the feature name
			 */
			public String getFeatureName() {
				return featureName;
			}
			
			/**
			 * Sets the feature name.
			 * 
			 * @param featureName the new feature name
			 */
			public void setFeatureName(String featureName) {
				this.featureName = featureName;
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
			 * To xml.
			 * 
			 * @return the string
			 */
			public String toXml() {
				StringBuffer buffer = new StringBuffer();
				
				buffer.append("\t<LEVEL ");
				buffer.append("name=\"" + this.getName()+ "\" ");
				buffer.append("column_id=\"" + this.getColumnId() + "\" ");
				buffer.append("column_desc=\"" + this.getColumnDesc() + "\" ");
				buffer.append("feature_name=\"" + this.getFeatureName() + "\" ");				
				buffer.append("/>");		
				
				return buffer.toString();
			}
		}
	}


	/**
	 * Gets the hierarchy base level.
	 * 
	 * @return the hierarchy base level
	 */
	public String getHierarchyBaseLevel() {
		return hierarchyBaseLevel;
	}


	/**
	 * Sets the hierarchy base level.
	 * 
	 * @param hierarchyBaseLevel the new hierarchy base level
	 */
	public void setHierarchyBaseLevel(String hierarchyBaseLevel) {
		this.hierarchyBaseLevel = hierarchyBaseLevel;
	}


	/**
	 * Gets the parent configuration.
	 * 
	 * @return the parent configuration
	 */
	public MapConfiguration getParentConfiguration() {
		return parentConfiguration;
	}


	/**
	 * Sets the parent configuration.
	 * 
	 * @param parentConfiguration the new parent configuration
	 */
	public void setParentConfiguration(MapConfiguration parentConfiguration) {
		this.parentConfiguration = parentConfiguration;
	}

	/**
	 * To xml.
	 * 
	 * @return the string
	 */
	public String toXml() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("<DATAMART_PROVIDER ");
		buffer.append("\nclass_name=\"" + this.getClassName()+ "\" ");
		buffer.append("\nconnection_name=\"" + this.getConnectionName() + "\" ");
		buffer.append("\nquery=\"" + this.getQuery() + "\" ");
		buffer.append("\ncolumn_id=\"" + this.getColumnId() + "\" ");
		buffer.append("\nhierarchy_name=\"" + this.getHierarchyName()+ "\" ");
		buffer.append("\nhierarchy_base_level=\"" + this.getHierarchyBaseLevel() + "\" ");
		buffer.append("\nhierarchy_level=\"" + this.getHierarchyLevel()+ "\" ");
		
		String[] columnValues = this.getKpiColumnNames();
		String columnValuesStr = "";
		String columnAggFuncStr = "";
		for(int i = 0; i < columnValues.length; i++) {
			columnValuesStr+= (i==0?"":",") + columnValues[i];
			MapRendererConfiguration.Measure measure =
				this.getParentConfiguration().getMapRendererConfiguration().getMeasure(columnValues[i]);
			if(measure == null) {
				columnAggFuncStr+= (i==0?"":",") + "sum";
			} else {
				columnAggFuncStr+= (i==0?"":",") + measure.getAggFunc();
			}
		}
		
		buffer.append("\ncolumn_values=\"" + columnValuesStr + "\" ");
		buffer.append("\nagg_type=\"" + columnAggFuncStr + "\" ");
		buffer.append(">\n");
		
		
		buffer.append("<HIERARCHIES>");
		List hierarchies = getHierarchies();
		for(int i = 0; i < hierarchies.size(); i++) {
			Hierarchy hierarchy = (Hierarchy)hierarchies.get(i);
			buffer.append("\n\t" + hierarchy.toXml());
		}
		buffer.append("\n</HIERARCHIES>");
		
		buffer.append("\n</DATAMART_PROVIDER>");
		
		return buffer.toString();
	}
	
	
}
