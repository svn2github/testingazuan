/**

 SpagoBI - The Business Intelligence Free Platform

 Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

 **/
package it.eng.qbe.geo.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import it.eng.qbe.geo.configuration.DatamartProviderConfiguration;
import it.eng.qbe.geo.configuration.MapProviderConfiguration;
import it.eng.qbe.geo.configuration.MapRendererConfiguration;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;

import javax.servlet.http.HttpServletResponse;

import sun.misc.BASE64Encoder;


/**
 * @author Andrea Gioia
 * 
 */
public class PreviewOnMapAction extends GeoAbstractAction {
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		super.service(request, response);
		
		String geoEngineUrl = (String)ConfigSingleton.getInstance().getAttribute("QBE.GEO-ENGINE-URL.value");
		
		if(geoEngineUrl == null) {
			geoEngineUrl = "http://" + getHttpRequest().getServerName() + ":" + getHttpRequest().getServerPort() + "/SpagoBIGeoEngine";
		}
		geoEngineUrl = geoEngineUrl.trim();
		if(geoEngineUrl.endsWith("/") || geoEngineUrl.endsWith("\\") ) {
			geoEngineUrl = geoEngineUrl.trim().substring(0, geoEngineUrl.trim().length() - 1);
		}
		geoEngineUrl += "/servlet/AdapterHTTP?ACTION_NAME=GEO_ACTION";
		
		DatamartProviderConfiguration datamartProviderConfiguration = mapConfiguration.getDatamartProviderConfiguration();
		MapProviderConfiguration mapProviderConfiguration = mapConfiguration.getMapProviderConfiguration();
		MapRendererConfiguration mapRendererConfiguration = mapConfiguration.getMapRendererConfiguration();
		
		String query = (String)request.getAttribute("query");
		
		String geoIdColumn = (String)request.getAttribute("geoIdColumn");
		String measureColumnsStr = (String)request.getAttribute("measureColumns");
		String[] measureColumns = measureColumnsStr.split(";");
		
		String selectedHierarchy = (String)request.getAttribute("selectedHierarchy");
		String baseLevel = (String)request.getAttribute("baseLevel");
		String aggregationLevel = (String)request.getAttribute("aggregationLevel");
		
		String includedHierarchies = (String)request.getAttribute("includedHierarchies");
		String selectedMap = (String)request.getAttribute("selectedMap");
		String selectedLayerStr = (String)request.getAttribute("selectedLayers");
		String[] selectedLayers = selectedLayerStr.split(";");
		
		
		mapConfiguration.setMapName(selectedMap);
		
		mapProviderConfiguration.setClassName("it.eng.spagobi.geo.map.provider.DBMapProvider");
		mapProviderConfiguration.setMapName(selectedMap);
		
		datamartProviderConfiguration.setClassName("it.eng.spagobi.geo.datamart.provider.HierarchicalDatamartProvider");
		//datamartProviderConfiguration.setConnectionName("genova");
		datamartProviderConfiguration.setQuery(query);
		datamartProviderConfiguration.setColumnId(geoIdColumn);
		datamartProviderConfiguration.setHierarchyName(selectedHierarchy);
		datamartProviderConfiguration.setHierarchyLevel(aggregationLevel);
		datamartProviderConfiguration.setHierarchyBaseLevel(baseLevel);
		datamartProviderConfiguration.setKpiColumnNames(measureColumns);
		
		mapRendererConfiguration.setClassName("it.eng.spagobi.geo.map.renderer.DynamicMapRenderer");
		mapRendererConfiguration.resetLayers();
		for(int i = 0; i < selectedLayers.length; i++) {
			MapRendererConfiguration.Layer layer = new MapRendererConfiguration.Layer();
			layer.setName(selectedLayers[i]);
			layer.setDescription(selectedLayers[i]);
			layer.setSelected(true);
			layer.setDefaultFillColor("");
			mapRendererConfiguration.addLayer(layer);
		}
		
		MapRendererConfiguration.Measure measure;
		
		
		List measures = new ArrayList();
		for(int i = 0; i < measureColumns.length; i++) {
			measure = mapRendererConfiguration.getMeasure(measureColumns[i]);
			if(measure == null) {
				//measure = createMeasure(measureColumns[i]);
				measure = new MapRendererConfiguration.Measure(measureColumns[i]);
				
			}
			measures.add(measure);
		}
		
		mapRendererConfiguration.resetMeasures();
		for(int i = 0; i < measures.size(); i++) {
			measure = (MapRendererConfiguration.Measure)measures.get(i);
			mapRendererConfiguration.addMeasure(measure);
		}
		
		
		String xmlConfig = mapConfiguration.toXml();
		System.out.println(xmlConfig);		
		BASE64Encoder encoder = new BASE64Encoder();
		String encodedTemplate = encoder.encode(xmlConfig.getBytes());
		
		
		response.setAttribute("template", encodedTemplate);
		response.setAttribute("target_level", "");
		response.setAttribute("action_url", geoEngineUrl);
		
		
		String mapCatalogueClientUrl = (String)getRequestContainer().getSessionContainer().getAttribute("MAP_CATALOGUE_CLIENT_URL");
		response.setAttribute("mapCatalogueManagerUrl", mapCatalogueClientUrl);	
	}
	
	private static final String[] measureBaseColours = new String[]{
		"#FF0000", "#FFFF00", "#FF00FF", "#00FFFF", 
		"#FF6600", "#FF0066", "#00FF66", "#0066FF", "#6600FF", "#66FF00",
		"#9900CC"
	};
	
	private static int counter = 0;
	
	private MapRendererConfiguration.Measure createMeasure(String measureName) {
		MapRendererConfiguration.Measure measure = new MapRendererConfiguration.Measure();
		
		measure.setColumnId(measureName);
		measure.setDescription(measureName);
		measure.setColour("#CCCC66");
		measure.setTresholdCalculatorType("quantile");
		measure.setTresholdLb("0");
		measure.setTresholdUb("none");
		measure.setAggFunc("sum");
		measure.setTresholdCalculatorParameters(new Properties());
		measure.getTresholdCalculatorParameters().setProperty("range", "");
		measure.getTresholdCalculatorParameters().setProperty("GROUPS_NUMBER", "5");
		measure.setColurCalculatorType("grad");
		measure.setColurOutboundCol("#CCCCCC");
		measure.setColurNullCol("#FFFFFF");
		measure.setColurCalculatorParameters(new Properties());
		measure.getColurCalculatorParameters().setProperty("range", "");
		measure.getColurCalculatorParameters().setProperty("BASE_COLOR", measureBaseColours[counter]);
		
		counter++;
		if(counter == measureBaseColours.length) counter = 0;
		
		return measure;
	}
}
