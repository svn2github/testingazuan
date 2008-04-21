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
package it.eng.spagobi.engines.geo.map.renderer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.engines.geo.AbstractGeoEngineComponent;
import it.eng.spagobi.engines.geo.commons.excpetion.GeoEngineException;
import it.eng.spagobi.engines.geo.dataset.DataSet;
import it.eng.spagobi.engines.geo.dataset.provider.IDatasetProvider;
import it.eng.spagobi.engines.geo.map.provider.IMapProvider;
import it.eng.spagobi.engines.geo.map.provider.configurator.AbstractMapProviderConfigurator;
import it.eng.spagobi.engines.geo.map.provider.configurator.SOMapProviderConfigurator;
import it.eng.spagobi.engines.geo.map.renderer.configurator.AbstractMapRendererConfigurator;

/**
 * @author Andrea Gioia
 *
 */
public class AbstractMapRenderer extends AbstractGeoEngineComponent  implements  IMapRenderer {
	
	private Map measures;
	private Map layers;
	
	/**
     * Logger component
     */
    public static transient Logger logger = Logger.getLogger(AbstractMapRenderer.class);
	
    
	public AbstractMapRenderer() {
		  super();
		  measures = new HashMap();
	}
	
	public void init(Object conf) throws GeoEngineException {
		super.init(conf);
		AbstractMapRendererConfigurator.configure( this, getConf() );
	}

	public File renderMap(IMapProvider mapProvider, IDatasetProvider datamartProvider, String outputType) throws GeoEngineException {
		return null;
	}
	
	public File renderMap(IMapProvider mapProvider, IDatasetProvider datamartProvider) throws GeoEngineException {
		return null;
	}
	
	public Measure getMeasure(String measureName) {
		Measure measure = (Measure)measures.get( measureName );
		return  measure;
	}
	
	public String[] getTresholdsArray(String measureName) {
		Measure measure = getMeasure(measureName);
		if(measure != null) {
			Properties params = (Properties)measure.getTresholdCalculatorParameters();
			if(params == null) return null;
			String pValue = params.getProperty("range");
			String[] trasholds = pValue.split(",");
			return trasholds;
		}

		return null;
	}
	
	public String[] getColoursArray(String measureName) {
		Measure measure = getMeasure(measureName);
		if(measure != null) {
			Properties params = (Properties)measure.getColurCalculatorParameters();
			if(params == null) return null;
			String pValue = params.getProperty("range");
			if(pValue == null) return new String[0];
			String[] colours = pValue.split(",");
			return colours;
		}

		return null;
	}
	
	public Layer getLayer(String layerName) {
		return (Layer)layers.get(layerName);
	}
	
	public void addLayer(Layer layer) {
		layers.put(layer.getName(), layer);
	}
	
	public String[] getLayerNames() {
		if(layers == null) return null;
		return (String[])layers.keySet().toArray(new String[0]);
	}

	public void setMeasures(Map measures) {
		this.measures = measures;
	}

	public void setLayers(Map layers) {
		this.layers = layers;
	}
	
	public void clearLayers() {
		layers.clear();
	}

}
