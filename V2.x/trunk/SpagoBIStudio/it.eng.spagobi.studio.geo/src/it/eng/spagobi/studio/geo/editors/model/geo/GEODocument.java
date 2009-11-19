package it.eng.spagobi.studio.geo.editors.model.geo;

import java.io.Serializable;

public class GEODocument  implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 6527389398919946121L;

	public GEODocument(GEODocument geoDocToCopy) {
		
	}
	private MapProvider mapProvider;
	private DatamartProvider datamartProvider;
	private MapRenderer mapRenderer;
	
	public MapProvider getMapProvider() {
		return mapProvider;
	}
	public void setMapProvider(MapProvider mapProvider) {
		this.mapProvider = mapProvider;
	}
	public DatamartProvider getDatamartProvider() {
		return datamartProvider;
	}
	public void setDatamartProvider(DatamartProvider datamartProvider) {
		this.datamartProvider = datamartProvider;
	}
	public MapRenderer getMapRenderer() {
		return mapRenderer;
	}
	public void setMapRenderer(MapRenderer mapRenderer) {
		this.mapRenderer = mapRenderer;
	}

}
