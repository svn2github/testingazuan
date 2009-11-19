package it.eng.spagobi.studio.geo.editors.model.bo;

import it.eng.spagobi.studio.geo.editors.model.geo.CrossNavigation;
import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;

public class CrossNavigationBO {
	
	public static CrossNavigation getCrossNavigation(GEODocument geoDocument){
		return geoDocument.getDatamartProvider().getCrossNavigation();
	}

}
