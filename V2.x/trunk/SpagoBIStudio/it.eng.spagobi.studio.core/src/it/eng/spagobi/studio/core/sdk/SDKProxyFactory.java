package it.eng.spagobi.studio.core.sdk;

import it.eng.spagobi.sdk.proxy.DataSetsSDKServiceProxy;
import it.eng.spagobi.sdk.proxy.DocumentsServiceProxy;
import it.eng.spagobi.sdk.proxy.EnginesServiceProxy;

public class SDKProxyFactory {

	public static DocumentsServiceProxy getDocumentsServiceProxy() {
		SpagoBIServerConnectionDefinition def = new SpagoBIServerConnectionDefinition();
		DocumentsServiceProxy proxy = new DocumentsServiceProxy(def.getUserName(), def.getPassword());
		String serverUrl = def.getServerUrl();
		if (serverUrl != null && !serverUrl.endsWith("/")) {
			serverUrl += "/";
		}
		proxy.setEndpoint(serverUrl + "sdk/DocumentsService");
		return proxy;
	}
	
	public static EnginesServiceProxy getEnginesServiceProxy() {
		SpagoBIServerConnectionDefinition def = new SpagoBIServerConnectionDefinition();
		EnginesServiceProxy proxy = new EnginesServiceProxy(def.getUserName(), def.getPassword());
		String serverUrl = def.getServerUrl();
		if (serverUrl != null && !serverUrl.endsWith("/")) {
			serverUrl += "/";
		}
		proxy.setEndpoint(serverUrl + "sdk/EnginesService");
		return proxy;
	}
	
	public static DataSetsSDKServiceProxy getDataSetsSDKServiceProxy() {
		SpagoBIServerConnectionDefinition def = new SpagoBIServerConnectionDefinition();
		DataSetsSDKServiceProxy proxy = new DataSetsSDKServiceProxy(def.getUserName(), def.getPassword());
		String serverUrl = def.getServerUrl();
		if (serverUrl != null && !serverUrl.endsWith("/")) {
			serverUrl += "/";
		}
		proxy.setEndpoint(serverUrl + "sdk/DataSetsSDKService");
		return proxy;
	}
	
}
