package it.eng.spagobi.studio.geo.editors.model.bo;

import it.eng.spagobi.studio.geo.editors.model.geo.DatamartProvider;
import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;

public class DatamartProviderBO {
	public static void setHierarchy(GEODocument geoDocument,
			String hierarchyName, String levelName){
		DatamartProvider dmProvider = geoDocument.getDatamartProvider();
		dmProvider.setHierarchy(hierarchyName);
		dmProvider.setLevel(levelName);
	}
}
