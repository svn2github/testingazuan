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
package it.eng.spagobi.engines.geo;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.engines.geo.commons.excpetion.GeoEngineException;
import it.eng.spagobi.engines.geo.commons.service.GeoEngineAnalysisState;
import it.eng.spagobi.engines.geo.dataset.provider.Hierarchy;
import it.eng.spagobi.engines.geo.dataset.provider.IDatasetProvider;
import it.eng.spagobi.engines.geo.map.provider.IMapProvider;
import it.eng.spagobi.engines.geo.map.renderer.IMapRenderer;
import it.eng.spagobi.engines.geo.map.renderer.Layer;
import it.eng.spagobi.utilities.engines.AbstractEngineInstance;
import it.eng.spagobi.utilities.engines.IEngineAnalysisState;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class GeoEngineInstance.
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class GeoEngineInstance extends AbstractEngineInstance {
	
	/** The map provider. */
	IMapProvider mapProvider;
	
	/** The dataset provider. */
	IDatasetProvider datasetProvider;
	
	/** The map renderer. */
	IMapRenderer mapRenderer;
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(GeoEngineInstance.class);
	
    
	/**
	 * Instantiates a new geo engine instance.
	 * 
	 * @param mapProvider the map provider
	 * @param datasetProvider the dataset provider
	 * @param mapRenderer the map renderer
	 */
	protected GeoEngineInstance(IMapProvider mapProvider, IDatasetProvider datasetProvider, IMapRenderer mapRenderer) {
		super();
		
		logger.debug("IN");
		
		setMapProvider( mapProvider );
		setDatasetProvider( datasetProvider );
		setMapRenderer( mapRenderer );
		logger.info("MapProvider class: " + getMapProvider().getClass().getName());
		logger.info("DatasetProvider class: " + getDatasetProvider().getClass().getName());
		logger.info("MapRenderer class: " + getMapRenderer().getClass().getName());
		
		logger.debug("OUT");
	}
	
	/**
	 * Instantiates a new geo engine instance.
	 * 
	 * @param template the template
	 * @param env the env
	 * 
	 * @throws GeoEngineException the geo engine exception
	 */
	protected GeoEngineInstance(SourceBean template, Map env) throws GeoEngineException {
		super( env );
		
		logger.debug("IN");
			
		setMapProvider( GeoEngineComponentFactory.buildMapProvider( template, env ) );
		setDatasetProvider( GeoEngineComponentFactory.buildDatasetProvider(template, env) );
		setMapRenderer( GeoEngineComponentFactory.buildMapRenderer(template, env) );
				
		logger.info("MapProvider class: " + getMapProvider().getClass().getName());
		logger.info("DatasetProvider class: " + getDatasetProvider().getClass().getName());
		logger.info("MapRenderer class: " + getMapRenderer().getClass().getName());
		
		validate();
		
		logger.debug("OUT");
	}

	/**
	 * Validate.
	 * 
	 * @throws GeoEngineException the geo engine exception
	 */
	public void validate() throws GeoEngineException {
		String selectedHierarchyName = getDatasetProvider().getSelectedHierarchyName();
		if(selectedHierarchyName == null) {
			logger.error("Select hierarchy name is not defined");
			String description = "Select hierarchy name is not defined";
			throw new GeoEngineException("Configuration error", description);
		}
		
		Hierarchy selectedHierarchy = getDatasetProvider().getHierarchy(selectedHierarchyName);
		if(selectedHierarchy == null) {
			logger.error("Selected hierarchy [" + selectedHierarchyName + "] does not exist");
			String description = "Selected hierarchy [" + selectedHierarchyName + "] does not exist";
			List hints = new ArrayList();
			hints.add("Check if hierarchy name is correct");
			hints.add("Check if a hierarchy named " + selectedHierarchyName +"  has been defined. Defined hierarachy are: " 
					+ Arrays.toString( getDatasetProvider().getHierarchyNames().toArray()) );
			throw new GeoEngineException("Configuration error", description, hints);
		}
		
		String selectedLevelName = getDatasetProvider().getSelectedLevelName();
		if(selectedLevelName == null) {
			logger.error("Select level name is not defined");
			String description = "Select level name is not defined";
			throw new GeoEngineException("Configuration error", description);
		}
		
		Hierarchy.Level selectedLevel = selectedHierarchy.getLevel(selectedLevelName);
		if(selectedLevel == null) {
			logger.error("Selected level [" + selectedHierarchyName + "] does not exist in selected hierarchy [" + selectedHierarchyName + "]");
			String description = "Selected level [" + selectedHierarchyName + "] does not exist in selected hierarchy [" + selectedHierarchyName + "]";
			List hints = new ArrayList();
			hints.add("Check if level name is correct");
			hints.add("Check if a level named " + selectedLevelName +"  is defined into hierarachy " + selectedHierarchyName + ". " +
					"Defined level are: " 
					+ Arrays.toString( selectedHierarchy.getLevelNames().toArray()) );
			throw new GeoEngineException("Configuration error", description, hints);
		}
		
	}
	
	

	public IEngineAnalysisState getAnalysisState() {
		GeoEngineAnalysisState analysisState = null;
		
		analysisState = new GeoEngineAnalysisState();
		analysisState.setSelectedMapName( getMapProvider().getSelectedMapName() );
		analysisState.setSelectedHierarchyName( getDatasetProvider().getSelectedHierarchyName() );
		analysisState.setSelectedLevelName( getDatasetProvider().getSelectedLevelName() );
		String selectedLayers = null;
		String[] layerNames = getMapRenderer().getLayerNames();
		if(layerNames.length > 0) selectedLayers = layerNames[0];
		for(int i = 1; i < layerNames.length; i++) {
			 selectedLayers += "," + layerNames[i];
		}
		analysisState.setSelectedLayers( selectedLayers );
		
		return analysisState;
	}
	
	/**
	 * Sets the analysis state.
	 * 
	 * @param geoAnalysisState the new analysis state
	 */
	public void setAnalysisState(IEngineAnalysisState analysisState) {	
		GeoEngineAnalysisState geoAnalysisState = null;
		String selectedHiearchyName = null;
		String selectedLevelName = null;
		String selectedMapName = null;
		String selectedLayerNames = null;
		
		logger.debug("IN");
		
		geoAnalysisState = (GeoEngineAnalysisState)analysisState;
		selectedHiearchyName = geoAnalysisState.getSelectedHierarchy();
		selectedLevelName = geoAnalysisState.getSelectedHierarchyLevel();
		selectedMapName = geoAnalysisState.getSelectedMapName();
		selectedLayerNames = geoAnalysisState.getSelectedLayers();
		
		if(selectedHiearchyName != null) {
			logger.debug("Previous selected hierarchy: " + getDatasetProvider().getSelectedHierarchyName());
			getDatasetProvider().setSelectedHierarchyName(selectedHiearchyName);
			logger.debug("New selected hierarchy: " + getDatasetProvider().getSelectedHierarchyName());
		}
		
		if(selectedLevelName != null) {
			logger.debug("Previous selected level: " + getDatasetProvider().getSelectedLevelName());
			getDatasetProvider().setSelectedLevelName(selectedLevelName);
			logger.debug("New selected level: " + getDatasetProvider().getSelectedLevelName());			
		}
		
		if(selectedMapName != null) {
			getMapProvider().setSelectedMapName(selectedMapName);
		}	
		
		if(selectedLayerNames != null) {
			logger.debug("Previous selected layers: " + Arrays.toString( getMapRenderer().getLayerNames() ) );
			getMapRenderer().clearLayers();
			String[] layers = selectedLayerNames.split(",");			
			for(int i = 0; i < layers.length; i++) {				
				Layer layer = getMapRenderer().getLayer(layers[i]);
				if(layer != null) {
					layer.setSelected(true);
				} else {
					layer = new Layer();
					layer.setName(layers[i]);
					layer.setDescription(layers[i]);
					layer.setSelected(true);
					getMapRenderer().addLayer(layer);
				}
			}
			logger.debug("New selected layers: " + Arrays.toString( getMapRenderer().getLayerNames() ) );
		}
		
		logger.debug("OUT");
	}
	
	
	
	
	
	
	/**
	 * Render map.
	 * 
	 * @param format the format
	 * 
	 * @return the file
	 * 
	 * @throws GeoEngineException the geo engine exception
	 */
	public File renderMap(String format) throws GeoEngineException {
		return getMapRenderer().renderMap( getMapProvider(), getDatasetProvider(), format);
	}
	
	/**
	 * Gets the map provider.
	 * 
	 * @return the map provider
	 */
	public IMapProvider getMapProvider() {
		return mapProvider;
	}

	/**
	 * Sets the map provider.
	 * 
	 * @param mapProvider the new map provider
	 */
	protected void setMapProvider(IMapProvider mapProvider) {
		this.mapProvider = mapProvider;
	}

	/**
	 * Gets the dataset provider.
	 * 
	 * @return the dataset provider
	 */
	public IDatasetProvider getDatasetProvider() {
		return datasetProvider;
	}

	/**
	 * Sets the dataset provider.
	 * 
	 * @param datasetProvider the new dataset provider
	 */
	protected void setDatasetProvider(IDatasetProvider datasetProvider) {
		this.datasetProvider = datasetProvider;
	}

	/**
	 * Gets the map renderer.
	 * 
	 * @return the map renderer
	 */
	public IMapRenderer getMapRenderer() {
		return mapRenderer;
	}

	/**
	 * Sets the map renderer.
	 * 
	 * @param mapRenderer the new map renderer
	 */
	protected void setMapRenderer(IMapRenderer mapRenderer) {
		this.mapRenderer = mapRenderer;
	}
}
