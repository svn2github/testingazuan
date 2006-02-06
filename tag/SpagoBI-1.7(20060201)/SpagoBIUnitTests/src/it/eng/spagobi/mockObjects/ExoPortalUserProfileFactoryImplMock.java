package it.eng.spagobi.mockObjects;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.security.AnonymousCMSUserProfile;
import it.eng.spagobi.security.IUserProfileFactory;

import java.security.Principal;

public class ExoPortalUserProfileFactoryImplMock implements IUserProfileFactory {

	public IEngUserProfile createUserProfile(Principal arg0) {
		return new AnonymousCMSUserProfile();
	}

}
