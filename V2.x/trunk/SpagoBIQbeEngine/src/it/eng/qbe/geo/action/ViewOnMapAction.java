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
package it.eng.qbe.geo.action;

import it.eng.qbe.geo.configuration.DatamartProviderConfiguration;
import it.eng.qbe.geo.configuration.MapProviderConfiguration;
import it.eng.qbe.geo.configuration.MapRendererConfiguration;
import it.eng.qbe.query.ISelectField;
import it.eng.qbe.utility.QbeProperties;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.utilities.callbacks.mapcatalogue.MapCatalogueAccessUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import sun.misc.BASE64Encoder;


// TODO: Auto-generated Javadoc
/**
 * The Class ViewOnMapAction.
 * 
 * @author Andrea Gioia
 */
public class ViewOnMapAction extends GeoAbstractAction {
	
	
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.geo.action.GeoAbstractAction#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) throws Exception {
		super.service(request, response);
		
		String geoEngineUrl = (String)ConfigSingleton.getInstance().getAttribute("QBE.GEO-ENGINE-URL.value");
		
		if(geoEngineUrl == null) {
			geoEngineUrl = "http://" + getHttpRequest().getServerName() + ":" + getHttpRequest().getServerPort() + "/SpagoBIGeoEngine";
		}
		geoEngineUrl = geoEngineUrl.trim();
		if(geoEngineUrl.endsWith("/") || geoEngineUrl.endsWith("\\") ) {
			geoEngineUrl = geoEngineUrl.trim().substring(0, geoEngineUrl.trim().length() - 1);
		}
		geoEngineUrl += "/servlet/AdapterHTTP?ACTION_NAME=GEO_ACTION";
		
		DatamartProviderConfiguration datamartProviderConfiguration = mapConfiguration.getDatamartProviderConfiguration();
		MapProviderConfiguration mapProviderConfiguration = mapConfiguration.getMapProviderConfiguration();
		MapRendererConfiguration mapRendererConfiguration = mapConfiguration.getMapRendererConfiguration();
		
		
		
		String query;
		
		
		String[] measureColumnAliases;
		String[] geoColumnAliases;
		String targetGeoColumnAlias;
		String targetGeoColumnName;
		
		DatamartProviderConfiguration.Hierarchy selectedHierarchy;
		String selectedHierarchyName;
		String includedHierarchies;
		
		DatamartProviderConfiguration.Hierarchy.Level selectedLevel;		
		String baseLevelName;
		String aggregationLevelName;
		
		String[] mapNames;
		String selectedMapName;
		String[] layerNames;
		String[] selectedLayerNames;
		
		String[] measureAgregationFunctins;
		
		
		
		query = getSqlQuery();
		if(query == null || query.trim().equals("")) {
			log(TracerSingleton.MAJOR, "Impossible to get query from qbe");
			return;
		}
		
		geoColumnAliases = getGeoColumnAliases();
		if(geoColumnAliases.length == 0) {
			log(TracerSingleton.MAJOR, "The query doesn't reference any geographical data");
			return;
		}
		
		targetGeoColumnAlias = geoColumnAliases[0];
		targetGeoColumnName =  getGeoColumnNameFromQuery(query, targetGeoColumnAlias);
		
		measureColumnAliases = getMeasureColumnAliases();
		if(measureColumnAliases.length == 0) {
			log(TracerSingleton.MAJOR, "The query doesn't reference any measure");
			return;
		}
		
		measureAgregationFunctins = getMeasureAggregationFunctions(measureColumnAliases);		
		
		selectedHierarchy = getSelectedHierarchy();
		selectedHierarchyName = selectedHierarchy.getName();
		includedHierarchies = selectedHierarchyName;
		
		selectedLevel = getSelectedLevel(selectedHierarchy, targetGeoColumnName);
		baseLevelName = selectedLevel.getName();		
		aggregationLevelName = selectedLevel.getName();		
		
		if(getMapCatalogueClient() == null) {
			log(TracerSingleton.MAJOR, "Impossible to connect to the Map-Catalogue");
			return;
		}
		
		mapNames = getMapNames(getMapCatalogueClient(), selectedLevel);
		if(mapNames == null || mapNames.length == 0) {
			log(TracerSingleton.MAJOR, "Impossible to find a map containing feature '" + selectedLevel.getFeatureName() + "'");
			return;
		}		
		selectedMapName = mapNames[0];
		
		layerNames = getLayerNames(getMapCatalogueClient(), selectedMapName);
		if(mapNames == null || mapNames.length == 0) {
			log(TracerSingleton.MAJOR, "Impossible load layers for map '" + selectedMapName + "'");
			return;
		}
		selectedLayerNames = layerNames;
		
		
		// set up mapConfiguration ...	
		mapConfiguration.setMapName(selectedMapName);
		
		// set up mapProviderConfiguration ...	
		mapProviderConfiguration.setClassName("it.eng.spagobi.geo.map.provider.DBMapProvider");
		mapProviderConfiguration.setMapName(selectedMapName);
		
		datamartProviderConfiguration.setClassName("it.eng.spagobi.geo.datamart.provider.HierarchicalDatamartProvider");
		datamartProviderConfiguration.setQuery(query);
		datamartProviderConfiguration.setColumnId(targetGeoColumnAlias);
		datamartProviderConfiguration.setHierarchyName(selectedHierarchyName);
		datamartProviderConfiguration.setHierarchyLevel(aggregationLevelName);
		datamartProviderConfiguration.setHierarchyBaseLevel(baseLevelName);
		datamartProviderConfiguration.setKpiColumnNames(measureColumnAliases);		
		datamartProviderConfiguration.setKpiAgregationFunctins(measureAgregationFunctins);
		
		// set up mapRendererConfiguration ...	
		mapRendererConfiguration.setClassName("it.eng.spagobi.geo.map.renderer.DynamicMapRenderer");
		
		mapRendererConfiguration.resetLayers();
		for(int i = 0; i < selectedLayerNames.length; i++) {
			mapRendererConfiguration.addLayer(createLayer(selectedLayerNames[i]));
		}
		
		mapRendererConfiguration.resetMeasures();
		for(int i = 0; i < measureColumnAliases.length; i++) {
			//mapRendererConfiguration.addMeasure(createMeasure(measureColumnAliases[i]));
			mapRendererConfiguration.addMeasure(new MapRendererConfiguration.Measure(measureColumnAliases[i]));	
		}
		
		
		
		
		String xmlConfig = mapConfiguration.toXml();		
		System.out.println(xmlConfig);		
		BASE64Encoder encoder = new BASE64Encoder();
		String encodedTemplate = encoder.encode(xmlConfig.getBytes());
		
		
		response.setAttribute("template", encodedTemplate);
		response.setAttribute("target_level", "");
		response.setAttribute("action_url", geoEngineUrl);
		
		
		String mapCatalogueClientUrl = (String)getRequestContainer().getSessionContainer().getAttribute("MAP_CATALOGUE_CLIENT_URL");
		response.setAttribute("mapCatalogueManagerUrl", mapCatalogueClientUrl);		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Log.
	 * 
	 * @param logLevel the log level
	 * @param msg the msg
	 */
	private void log(int logLevel, String msg) {
		TracerSingleton.log("GeoViewer", logLevel, 
				"ViewOnMapAction :: service : " +
				msg);
	}
	
	/**
	 * Gets the sql query.
	 * 
	 * @return the sql query
	 */
	private String getSqlQuery() {
		String query = null;
		
		queryWizard.composeQuery(datamartModel);
		query = queryWizard.getFinalSqlQuery(datamartModel);
		
		return query;
	}
	
	/**
	 * Gets the column aliases filtered by type.
	 * 
	 * @param targetType the target type
	 * 
	 * @return the column aliases filtered by type
	 */
	private List getColumnAliasesFilteredByType(int targetType) {
		List columnAliases = new ArrayList();
		List fields;
		ISelectField field;
		String fieldCompleteName;
		String entityClassName;
		String columnName;
		int columnType;
		String columnAlias;
		
		QbeProperties qbeProperties = new QbeProperties(datamartModel);
		
		
		fields = queryWizard.getSelectClause().getSelectFields();
		for(int i = 0; i < fields.size(); i++) {
			field = (ISelectField)fields.get(i);
			fieldCompleteName = field.getFieldCompleteName();
			String fieldName = field.getFieldName();
			entityClassName = field.getFieldEntityClass().getClassName();
			columnName = fieldCompleteName.substring(entityClassName.length()+1);			
			
			columnType = qbeProperties.getFieldType(columnName);
			
			if(columnType == targetType) {
				columnAlias = field.getFieldAlias().replaceAll(" ","_");
				if(fieldName.startsWith("sum(")) columnAlias = columnAlias + "Sum";
				if(fieldName.startsWith("avg(")) columnAlias = columnAlias + "Avg";
				if(fieldName.startsWith("min(")) columnAlias = columnAlias + "Min";
				if(fieldName.startsWith("max(")) columnAlias = columnAlias + "Max";
				if(fieldName.startsWith("count(")) columnAlias = columnAlias + "Count";
				columnAliases.add(columnAlias);
			} 
		}
		
		return columnAliases;
	}
	
	/**
	 * Gets the measure column aliases.
	 * 
	 * @return the measure column aliases
	 */
	private String[] getMeasureColumnAliases() {
		String[] measureColumnAliases;
		List measureColumnAliasesList;		
		
		measureColumnAliasesList = getColumnAliasesFilteredByType(QbeProperties.FIELD_TYPE_MEASURE);
		if(measureColumnAliasesList != null) {
			measureColumnAliases = (String[])measureColumnAliasesList.toArray(new String[0]);
		} else  {
			measureColumnAliases = new String[0];
		}
		
		return measureColumnAliases;
	}
	
	/**
	 * Gets the measure aggregation functions.
	 * 
	 * @param measureColumnAliases the measure column aliases
	 * 
	 * @return the measure aggregation functions
	 */
	private String[] getMeasureAggregationFunctions(String[] measureColumnAliases) {
		String [] measureAgregationFunctins = new String[measureColumnAliases.length];
		for(int i = 0; i < measureColumnAliases.length; i++) {
			measureAgregationFunctins[i] = "sum";
		}
		
		return measureAgregationFunctins;
	}
	
	/**
	 * Gets the geo column aliases.
	 * 
	 * @return the geo column aliases
	 */
	private String[] getGeoColumnAliases() {
		String[] geoColumnAliases;
		List geoColumnAliasesList;		
		
		geoColumnAliasesList = getColumnAliasesFilteredByType(QbeProperties.FIELD_TYPE_GEOREF);
		if(geoColumnAliasesList != null) {
			geoColumnAliases = (String[])geoColumnAliasesList.toArray(new String[0]);
		} else  {
			geoColumnAliases = new String[0];
		}
		
		return geoColumnAliases;
	}
	
	
	/**
	 * Gets the geo column name from query.
	 * 
	 * @param query the query
	 * @param geoColumnAlias the geo column alias
	 * 
	 * @return the geo column name from query
	 */
	private String getGeoColumnNameFromQuery(String query, String geoColumnAlias) {		
		String geoColumnName = null;
		
		String baseQuery = query.trim().toUpperCase();
		String selectClause = query.trim().substring("SELECT".length(), baseQuery.indexOf(" FROM "));
		if(selectClause.trim().toUpperCase().startsWith("DISTINCT")) {
			selectClause = selectClause.trim().substring("DISTINCT".length());
			selectClause = selectClause.trim();
		}
		String[] selectFields = selectClause.split(",");
		for(int i = 0; i < selectFields.length; i++) {
			
			String baseSelectFields =  selectFields[i].trim().toUpperCase();
			String columnName = selectFields[i].trim().substring(0, baseSelectFields.indexOf(" AS "));
			if(columnName.indexOf('.') != -1) {
				columnName = columnName.substring(columnName.lastIndexOf('.') + 1);
			}
			String columnAlias = selectFields[i].trim().substring(baseSelectFields.indexOf(" AS ") + " AS ".length());
			columnAlias = columnAlias.trim();
			if(columnAlias.equalsIgnoreCase(geoColumnAlias)) {
				geoColumnName = columnName;
				break;
			}
		}
		
		return geoColumnName;
	}
	
	/**
	 * Gets the default hierachy.
	 * 
	 * @return the default hierachy
	 */
	private DatamartProviderConfiguration.Hierarchy getDefaultHierachy() {
		DatamartProviderConfiguration.Hierarchy defaultHierarchy = null;
		
		DatamartProviderConfiguration datamartProviderConfiguration = mapConfiguration.getDatamartProviderConfiguration();
		
		List hierarchies = datamartProviderConfiguration.getHierarchies();		
		for(int i = 0; i < hierarchies.size(); i++) {
			DatamartProviderConfiguration.Hierarchy hierarchy = (DatamartProviderConfiguration.Hierarchy)hierarchies.get(i);
			if(hierarchy.isDefaultHierarchy()) {
				defaultHierarchy = hierarchy;
				break;
			}
		}	
		
		return defaultHierarchy;
	}
	
	/**
	 * Gets the selected hierarchy.
	 * 
	 * @return the selected hierarchy
	 */
	private DatamartProviderConfiguration.Hierarchy getSelectedHierarchy() {
		return getDefaultHierachy();
	}
	
	/**
	 * Gets the selected level.
	 * 
	 * @param selectedHierarchy the selected hierarchy
	 * @param targetGeoColumnName the target geo column name
	 * 
	 * @return the selected level
	 */
	private DatamartProviderConfiguration.Hierarchy.Level getSelectedLevel(DatamartProviderConfiguration.Hierarchy selectedHierarchy, String targetGeoColumnName) {
		DatamartProviderConfiguration.Hierarchy.Level selectedLevel = null;
		
		List levels = selectedHierarchy.getLevels();
		for(int i = 0; i < levels.size(); i++) {
			DatamartProviderConfiguration.Hierarchy.Level level = 
				(DatamartProviderConfiguration.Hierarchy.Level)levels.get(i);
			
			String columnId =  level.getColumnId();
			if(columnId.equalsIgnoreCase(targetGeoColumnName)) {
				selectedLevel = level;
			}
		}
		
		return selectedLevel;
	}
	
	/**
	 * Gets the map catalogue client.
	 * 
	 * @return the map catalogue client
	 */
	private MapCatalogueAccessUtils getMapCatalogueClient() {
		MapCatalogueAccessUtils mapCatalogueClient = null;
		mapCatalogueClient = (MapCatalogueAccessUtils)getRequestContainer().getSessionContainer().getAttribute("MAP_CATALOGUE_CLIENT");
		return mapCatalogueClient;
	}
	
	/**
	 * Gets the map names.
	 * 
	 * @param mapCatalogueClient the map catalogue client
	 * @param selectedLevel the selected level
	 * 
	 * @return the map names
	 * 
	 * @throws Exception the exception
	 */
	private String[] getMapNames(MapCatalogueAccessUtils mapCatalogueClient, DatamartProviderConfiguration.Hierarchy.Level selectedLevel) throws Exception {
		String[] mapNames = null;
		List maps;
		 
		if(mapCatalogueClient != null) {			
			maps = mapCatalogueClient.getMapNamesByFeature(getUserId(), selectedLevel.getFeatureName());
			if(maps != null && maps.size() > 0) {
				mapNames = (String[])maps.toArray(new String[0]);
			}
		}
		
		return mapNames;
	}
	
	/**
	 * Gets the layer names.
	 * 
	 * @param mapCatalogueClient the map catalogue client
	 * @param selectedMap the selected map
	 * 
	 * @return the layer names
	 * 
	 * @throws Exception the exception
	 */
	private String[] getLayerNames(MapCatalogueAccessUtils mapCatalogueClient, String selectedMap) throws Exception {
		String[] layerNames = null;
		List layers;
		
		if(mapCatalogueClient != null) {
			layers = mapCatalogueClient.getFeatureNamesInMap(getUserId(), selectedMap);
			if(layers != null && layers.size() > 0) {
				layerNames = (String[])layers.toArray(new String[0]);
			}
		}
		
		return layerNames;
	}
	
	/**
	 * Creates the layer.
	 * 
	 * @param layerName the layer name
	 * 
	 * @return the map renderer configuration. layer
	 */
	private MapRendererConfiguration.Layer createLayer(String layerName) {
		MapRendererConfiguration.Layer layer = new MapRendererConfiguration.Layer();
		layer.setName(layerName);
		layer.setDescription(layerName);
		layer.setSelected(true);
		layer.setDefaultFillColor("#FFFFFF");
		
		return layer;
	}
	
	
	/** The Constant measureBaseColours. */
	private static final String[] measureBaseColours = new String[]{
		"#FF0000", "#FFFF00", "#FF00FF", "#00FFFF", 
		"#FF6600", "#FF0066", "#00FF66", "#0066FF", "#6600FF", "#66FF00",
		"#9900CC"
	};
	
	/** The counter. */
	private static int counter = 0;
	
	/**
	 * Creates the measure.
	 * 
	 * @param measureName the measure name
	 * 
	 * @return the map renderer configuration. measure
	 */
	private MapRendererConfiguration.Measure createMeasure(String measureName) {
		MapRendererConfiguration.Measure measure = new MapRendererConfiguration.Measure();
		
		measure.setColumnId(measureName);
		measure.setDescription(measureName);
		measure.setColour("#CCCC66");
		measure.setTresholdCalculatorType("quantile");
		measure.setTresholdLb("0");
		measure.setTresholdUb("none");
		measure.setAggFunc("sum");
		measure.setTresholdCalculatorParameters(new Properties());
		measure.getTresholdCalculatorParameters().setProperty("range", "");
		measure.getTresholdCalculatorParameters().setProperty("GROUPS_NUMBER", "5");
		measure.setColurCalculatorType("grad");
		measure.setColurOutboundCol("#CCCCCC");
		measure.setColurNullCol("#FFFFFF");
		measure.setColurCalculatorParameters(new Properties());
		measure.getColurCalculatorParameters().setProperty("range", "");
		measure.getColurCalculatorParameters().setProperty("BASE_COLOR", measureBaseColours[counter]);
		
		counter++;
		if(counter == measureBaseColours.length) counter = 0;
		
		return measure;
	}
}
