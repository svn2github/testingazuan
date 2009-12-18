package it.eng.spagobi.studio.core.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import it.eng.spagobi.studio.core.Activator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.SPAGOBI_SERVER_URL, "http://localhost:8080/SpagoBI");
		store.setDefault(PreferenceConstants.SPABOGI_USER_NAME, "biadmin");
		store.setDefault(PreferenceConstants.SPABOGI_USER_PASSWORD,
				"biadmin");
		store.setDefault(PreferenceConstants.IREPORT_EXEC_FILE,
		"C:/Programmi/JasperSoft/iReport-3.0.0/iReport.exe");

	}

}
