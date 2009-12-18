package it.eng.spagobi.studio.core.sdk;

import it.eng.spagobi.studio.core.Activator;
import it.eng.spagobi.studio.core.preferences.PreferenceConstants;

import org.eclipse.jface.preference.IPreferenceStore;

public class SpagoBIServerConnectionDefinition {
	
	private String serverUrl;
	private String userName;
	private String password;
	
	public String getServerUrl() {
		return serverUrl;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}
	
	public SpagoBIServerConnectionDefinition() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		serverUrl = store.getString(PreferenceConstants.SPAGOBI_SERVER_URL);
		userName = store.getString(PreferenceConstants.SPABOGI_USER_NAME);
		password = store.getString(PreferenceConstants.SPABOGI_USER_PASSWORD);
	}
	
}
