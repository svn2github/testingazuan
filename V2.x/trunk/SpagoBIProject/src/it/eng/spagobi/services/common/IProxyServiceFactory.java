package it.eng.spagobi.services.common;

import it.eng.spagobi.services.cas.CasProxyService;

public class IProxyServiceFactory {

    public static final IProxyService createProxyService(){
	return new CasProxyService();
    }
}
