package it.eng.spagobi.studio.core.bo;

import java.util.Vector;

import it.eng.spagobi.sdk.datasets.bo.SDKDataSet;
import it.eng.spagobi.sdk.maps.bo.SDKMap;
import it.eng.spagobi.sdk.proxy.DataSetsSDKServiceProxy;
import it.eng.spagobi.sdk.proxy.MapsSDKServiceProxy;
import it.eng.spagobi.studio.core.log.SpagoBILogger;
import it.eng.spagobi.studio.core.sdk.SDKProxyFactory;


public class SpagoBIServerObjects {


	public GeoMap getGeoMapById(Integer geoId){
		GeoMap toReturn=null;

		SDKMap sdkMap=null;
		try{
			SDKProxyFactory proxyFactory=new SDKProxyFactory();
			MapsSDKServiceProxy mapsServiceProxy=proxyFactory.getMapsSDKServiceProxy();
			sdkMap=mapsServiceProxy.getMapById(geoId);
		}
		catch (Exception e) {
			SpagoBILogger.errorLog("No comunication with SpagoBI server, could not retrieve map informations", e);
		}	
		if(sdkMap!=null){
			toReturn=new GeoMap(sdkMap);

		}
		return toReturn;		

	}

	public Vector<GeoMap> getAllGeoMaps(){
		Vector<GeoMap> toReturn=new Vector<GeoMap>();

		SDKMap[] sdkMaps=null;
		try{
			SDKProxyFactory proxyFactory=new SDKProxyFactory();
			MapsSDKServiceProxy mapsServiceProxy=proxyFactory.getMapsSDKServiceProxy();
			sdkMaps=mapsServiceProxy.getMaps();
		}
		catch (Exception e) {
			SpagoBILogger.errorLog("No comunication with SpagoBI server, could not retrieve map informations", e);
		}	

		for (int i = 0; i < sdkMaps.length; i++) {
			SDKMap sdkMap=sdkMaps[i];
			GeoMap geoMap=new GeoMap(sdkMap);
			if(geoMap!=null){
				toReturn.add(geoMap);
			}
		}
		return toReturn;		

	}


	public Vector<Dataset> getAllDatasets(){
		Vector<Dataset> toReturn=new Vector<Dataset>();

		SDKDataSet[] sdkDataSets=null;
		try{
			SDKProxyFactory proxyFactory=new SDKProxyFactory();
			DataSetsSDKServiceProxy datasetsServiceProxy=proxyFactory.getDataSetsSDKServiceProxy();
			sdkDataSets=datasetsServiceProxy.getDataSets();
		}
		catch (Exception e) {
			SpagoBILogger.errorLog("No comunication with SpagoBI server, could not retrieve dataset informations", e);
		}	

		for (int i = 0; i < sdkDataSets.length; i++) {
			SDKDataSet sdkDataSet=sdkDataSets[i];
			Dataset dataset=new Dataset(sdkDataSet);
			if(dataset!=null){
				toReturn.add(dataset);
			}
		}
		return toReturn;		

	}



}
