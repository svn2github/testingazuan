package it.eng.spagobi.studio.geo.editors.model.bo;

import it.eng.spagobi.studio.geo.editors.model.geo.Defaults;
import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;
import it.eng.spagobi.studio.geo.editors.model.geo.GuiParam;
import it.eng.spagobi.studio.geo.editors.model.geo.GuiSettings;
import it.eng.spagobi.studio.geo.editors.model.geo.Windows;

import java.util.Vector;

public class DefaultsBO {
	public static Defaults setNewDefaults(GEODocument geoDocument){
		GuiSettings guiSettings = GuiSettingsBO.getGuiSettings(geoDocument);
		if(guiSettings == null){
			guiSettings = new GuiSettings();
			geoDocument.getMapRenderer().setGuiSettings(guiSettings);
		}		
		Windows windows = guiSettings.getWindows();
		if(windows == null){
			windows = new Windows();
			guiSettings.setWindows(windows);			
		}
		Defaults defaults = windows.getDefaults();
		if(defaults == null){
			defaults = new Defaults();
			windows.setDefaults(defaults);
		}
		Vector<GuiParam> params = defaults.getParams();
		if(params == null){
			params = new Vector<GuiParam>();
			defaults.setParams(params);
		}
		return defaults;
	}
}
