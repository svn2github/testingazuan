package it.eng.spagobi.utilities;

import java.util.HashMap;
import java.util.Map;

import it.eng.spago.security.IEngUserProfile;

public class ProfileExchanger {
	
	private static ProfileExchanger instance;
	private Map profiles = new HashMap();
	
	private ProfileExchanger(){}
	
	public static ProfileExchanger getInstance() {
		if (instance == null) {
			synchronized (ProfileExchanger.class) {
				if (instance == null) {
					instance = new ProfileExchanger();
				}
			}
		}
		return instance;
	}
	
	
	public void insertProfile(String uuid, IEngUserProfile profile) {
		profiles.put(uuid, profile);
	}
	
	public IEngUserProfile getProfile(String uuid) {
		IEngUserProfile profile = (IEngUserProfile)profiles.get(uuid);
		profiles.remove(uuid);
		return profile;
	}
	
}