package it.eng.spagobi.studio.geo.editors.model.bo;

import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;
import it.eng.spagobi.studio.geo.editors.model.geo.Layer;
import it.eng.spagobi.studio.geo.editors.model.geo.Layers;
import it.eng.spagobi.studio.geo.editors.model.geo.MapRenderer;

import java.util.Vector;

public class LayersBO {
	public static Layers getLayers(GEODocument geoDocument){
		MapRenderer mapRenderer = geoDocument.getMapRenderer();
		return mapRenderer.getLayers();
	}
	
	public static void setNewLayers(GEODocument geoDocument, String mapName) {
		MapRenderer mapRenderer = geoDocument.getMapRenderer();
		Layers layers = new Layers();
		layers.setMapName(mapName);
		mapRenderer.setLayers(layers);

		//add columns
		Vector<Layer> layer= new Vector<Layer>();
		layers.setLayer(layer);
	}
}
