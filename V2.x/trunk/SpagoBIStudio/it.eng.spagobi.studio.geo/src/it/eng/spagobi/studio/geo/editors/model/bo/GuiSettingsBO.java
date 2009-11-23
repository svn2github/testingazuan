package it.eng.spagobi.studio.geo.editors.model.bo;

import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;
import it.eng.spagobi.studio.geo.editors.model.geo.GuiSettings;

public class GuiSettingsBO {
	public static GuiSettings getGuiSettings(GEODocument geoDocument){
		return geoDocument.getMapRenderer().getGuiSettings();
	}
}
