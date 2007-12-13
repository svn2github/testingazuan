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
package it.eng.spagobi.utilities.callbacks.mapcatalogue;

import it.eng.spagobi.services.proxy.ContentServiceProxy;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

/**
 * A proxy class used by clients to remotly access the spagoBI EventHandler 
 * interface in a customized way.
 * 
 * @author Gioia
 */
public class MapCatalogueAccessUtils {
	
	
	public static final String GET_STANDARD_HIERARCHY = "getStandardHierarchy";
	public static final String GET_MAPS_BY_FEATURE = "getMapsByFeature";
	public static final String GET_FEATURES_IN_MAP = "getFeaturesInMap";
	public static final String GET_ALL_MAP_NAMES = "getAllMapNames";
	public static final String GET_ALL_FEATURE_NAMES = "getAllFeatureNames";
	public static final String GET_MAP_URL = "getMapUrl";
	public static final String ERROR_PREFIX = "$";
	public static final String ERROR_HIERARCHY_NOT_FOUND = ERROR_PREFIX + "01";
	public static final String ERROR_MAP_NOT_FOUND = ERROR_PREFIX + "02";
	public static final String ERROR_FEATURE_NOT_FOUND = ERROR_PREFIX + "03";
	public static final String ERROR_MAP_URL_NOT_FOUND = ERROR_PREFIX + "04";
	
	private HttpSession session=null;
	
	public MapCatalogueAccessUtils(HttpSession session) {
	    this.session=session;
	}
	
	public String getStandardHierarchy(HttpSession sessione,String userId) throws Exception {

	ContentServiceProxy proxy = new ContentServiceProxy(session);
	String ris = proxy.mapCatalogue(userId, GET_STANDARD_HIERARCHY, null, null, null);
	if (ris == null)
	    throw new Exception("Error while getting default hierarchy");
	if (ris.equalsIgnoreCase(ERROR_HIERARCHY_NOT_FOUND)) {
	    throw new Exception("Default Hierarchy not found. ");
	}
	return ris;
    }
	
	public List getMapNamesByFeature(String userId,String featureName) throws Exception {

	ContentServiceProxy proxy = new ContentServiceProxy(session);
	String ris = proxy.mapCatalogue(userId, GET_MAPS_BY_FEATURE, null, featureName, null);
	if (ris == null)
	    throw new Exception("Error while reading maps about feature " + featureName);
	if (ris.equalsIgnoreCase(ERROR_FEATURE_NOT_FOUND)) {
	    throw new Exception("Error while reading maps about feature " + featureName);
	}

	if (ris.equalsIgnoreCase(ERROR_MAP_NOT_FOUND)) {
	    throw new Exception("Maps about " + featureName + " not found. ");
	}

	String[] maps = ris.split(",");
	List mapList = new ArrayList();
	for (int i = 0; i < maps.length; i++) {
	    mapList.add(maps[i]);
	}

	return mapList;
    }

	public List getFeatureNamesInMap(String userId,String mapName) throws Exception {

	ContentServiceProxy proxy = new ContentServiceProxy(session);
	String ris = proxy.mapCatalogue(userId, GET_FEATURES_IN_MAP, null, null, mapName);

	if (ris == null)
	    throw new Exception("Error while reading features about map " + mapName);

	if (ris.startsWith(ERROR_FEATURE_NOT_FOUND)) {
	    throw new Exception("Features about " + mapName + " not found. ");
	}

	String[] features = ris.split(",");
	List featureList = new ArrayList();
	for (int i = 0; i < features.length; i++) {
	    featureList.add(features[i]);
	}

	return featureList;
    }
	
	public String getMapUrl(String userId,String mapName) throws Exception {

	ContentServiceProxy proxy = new ContentServiceProxy(session);
	String ris = proxy.mapCatalogue(userId, GET_MAP_URL, null, null, mapName);

	if (ris == null)
	    throw new Exception("Error while reading map url " + mapName);

	if (ris.startsWith(ERROR_FEATURE_NOT_FOUND)) {
	    throw new Exception("Map about " + mapName + " not found. ");
	}
	return ris;
    }
	
	public List getAllMapNames(String userId) throws Exception {

	ContentServiceProxy proxy = new ContentServiceProxy(session);
	String ris = proxy.mapCatalogue(userId, GET_ALL_MAP_NAMES, null, null, null);

	if (ris == null)
	    throw new Exception("Error while reading maps ");

	if (ris.equalsIgnoreCase(ERROR_MAP_NOT_FOUND)) {
	    throw new Exception("Maps not found. ");
	}
	String[] maps = ris.split(",");
	List mapList = new ArrayList();
	for (int i = 0; i < maps.length; i++) {
	    mapList.add(maps[i]);
	}

	return mapList;
    }
	
	public List getAllFeatureNames(String userId) throws Exception {
	ContentServiceProxy proxy = new ContentServiceProxy(session);
	String ris = proxy.mapCatalogue(userId, GET_ALL_FEATURE_NAMES, null, null, null);
	if (ris == null)
	    throw new Exception("Error while reading Features ");

	if (ris.equalsIgnoreCase(ERROR_FEATURE_NOT_FOUND)) {
	    throw new Exception("Feature not found. ");
	}
	String[] maps = ris.split(",");
	List mapList = new ArrayList();
	for (int i = 0; i < maps.length; i++) {
	    mapList.add(maps[i]);
	}

	return mapList;
    }
}
