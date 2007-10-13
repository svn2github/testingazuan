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
package it.eng.spagobi.services;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.mapcatalogue.bo.GeoFeature;
import it.eng.spagobi.mapcatalogue.bo.GeoMap;
import it.eng.spagobi.mapcatalogue.bo.dao.DAOFactory;
import it.eng.spagobi.mapcatalogue.bo.dao.ISbiGeoFeaturesDAO;
import it.eng.spagobi.mapcatalogue.bo.dao.ISbiGeoMapFeaturesDAO;
import it.eng.spagobi.mapcatalogue.bo.dao.ISbiGeoMapsDAO;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A servlet used to manage and control the EventManager
 * 
 * @author Gioia
 *
 */
public class MapCatalogueManagerServlet extends HttpServlet{
	
	public static final String GET_STANDARD_HIERARCHY = "getStandardHierarchy";
	public static final String GET_MAPS_BY_FEATURE = "getMapsByFeature";
	public static final String GET_FEATURES_IN_MAP = "getFeaturesInMap";
	public static final String GET_ALL_MAP_NAMES = "getAllMapNames";
	public static final String GET_ALL_FEATURE_NAMES = "getAllFeatureNames";
	public static final String GET_MAP_URL = "getMapUrl";
	public static final String DOWNLOAD = "DOWNLOAD";
	public static final String ERROR_PREFIX = "$";
	public static final String ERROR_HIERARCHY_NOT_FOUND = ERROR_PREFIX + "01";
	public static final String ERROR_FEATURE_NOT_FOUND = ERROR_PREFIX + "02";
	public static final String ERROR_MAP_NOT_FOUND = ERROR_PREFIX + "03";
	protected final 	String DOCUMENT_FILE_NAME = "geoDefaultHierarchy.xml";
	protected final 	String DOCUMENT_PATH_NAME = "GEOENGINE.DEFAULT_HIERARCHY";
	
	/**
	 * Init method definition
	 */
	public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
	
	/**
     * Service method definition
     * 
     * @param request The http servlet request
     * @param response The http servlet response
     * @throws IOException If any exception occurred
     */
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		byte[] returnValue = null;
		String strRet = null;
		SpagoBITracer.debug("SpagoBI", getClass().getName(), "service:", "Start processing request ...");	 				
	 	try{
	 		String operation = request.getParameter("operation");
	 		if(operation == null) {}
	 		
	 		if(operation.equalsIgnoreCase(GET_STANDARD_HIERARCHY)) {
	 			strRet = getStandardHierarchy();
	 			if (strRet == null){
	 				strRet = ERROR_HIERARCHY_NOT_FOUND;
	 			}
	 		} else if(operation.equalsIgnoreCase(GET_MAPS_BY_FEATURE)) {
	 			String featureName = request.getParameter("featureName");
	 			strRet = getMapsByFeature(featureName);
	 			if (strRet == null){
	 				strRet = ERROR_MAP_NOT_FOUND;
	 			}
	 		} else if(operation.equalsIgnoreCase(GET_FEATURES_IN_MAP)) {
	 			String mapName = request.getParameter("mapName");
	 			strRet = getFeaturesInMap(mapName);
	 			if (strRet == null){
	 				strRet = ERROR_FEATURE_NOT_FOUND;
	 			}	 		
	 		} else if(operation.equalsIgnoreCase(GET_MAP_URL)) {
	 			String mapName = request.getParameter("mapName");
	 			strRet = getMapUrl(request, mapName);
	 			if (strRet == null){
	 				strRet = ERROR_MAP_NOT_FOUND;
	 			}
			} else if(operation.equalsIgnoreCase(GET_ALL_MAP_NAMES)) {	 			
	 			strRet = getAllMapNames();
	 			if (strRet == null){
	 				strRet = ERROR_MAP_NOT_FOUND;
	 			}			
	 		} else if(operation.equalsIgnoreCase(GET_ALL_FEATURE_NAMES)) {	 			
	 			strRet = getAllFeatureNames();
	 			if (strRet == null){
	 				strRet = ERROR_FEATURE_NOT_FOUND;
	 			}
			}
	 		else if(operation.equalsIgnoreCase(DOWNLOAD)) {
				downloadFile(request, response);
			}
	 		
	 		if (!operation.equalsIgnoreCase(DOWNLOAD)) {
		 		returnValue = strRet.getBytes();
		 		response.setContentLength(returnValue.length);
			 	response.getOutputStream().write(returnValue);
			 	response.getOutputStream().flush();
	 		}
	 	} catch(Exception e) {
	 		SpagoBITracer.critical("SpagoBI", getClass().getName(), "service", "Exception", e);
	 	}
	}
	
	
			
	private String getStandardHierarchy() {	
		//load a xml file
		StringBuffer buffer = new StringBuffer();
		ConfigSingleton config = ConfigSingleton.getInstance();
		SourceBean pathSB = (SourceBean) config.getAttribute(DOCUMENT_PATH_NAME);
		String path = (String) pathSB.getAttribute("path");
	
		String baseTemplateFileStr = ConfigSingleton.getRootPath()+ path + "/" + DOCUMENT_FILE_NAME;		
		File baseFile = null;
		if(baseTemplateFileStr != null) baseFile = new File(baseTemplateFileStr);
		InputStream is = null;
		if(baseFile!=null && baseFile.exists()) {
			try {
				is = new FileInputStream(baseFile);
			} catch (FileNotFoundException e1) {
				SpagoBITracer.critical("SpagoBI", getClass().getName(), "getStandardHierarchy", "Exception", e1);
			}
		}
		
		BufferedReader reader = new BufferedReader( new InputStreamReader(is) );
		String line = null;
		try {
			while( (line = reader.readLine()) != null) {
				buffer.append(line + "\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buffer.toString();
		

	}
	
	private String getMapsByFeature(String featureName) {
		String toReturn = null;
		try{
			ISbiGeoMapFeaturesDAO mapFeaturesDAO = DAOFactory.getSbiGeoMapFeaturesDAO();
			ISbiGeoFeaturesDAO featureDAO = DAOFactory.getSbiGeoFeaturesDAO();
			GeoFeature tmpFeature = featureDAO.loadFeatureByName(featureName);
			if (tmpFeature == null) return null;
			List lstMaps =  mapFeaturesDAO.loadMapNamesByFeatureId(new Integer(tmpFeature.getFeatureId()));
			if (lstMaps != null){
				for (int i=0; i<lstMaps.size(); i++){
					toReturn = ((toReturn==null)?"":toReturn) + (String)lstMaps.get(i)+((i==lstMaps.size()-1)?"":",");
				} 	
			}
	 		return toReturn; 		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}	
	
	}

	private String getFeaturesInMap(String mapName) {
		String toReturn = null;
		try{
			ISbiGeoMapFeaturesDAO mapFeaturesDAO = DAOFactory.getSbiGeoMapFeaturesDAO();
			ISbiGeoMapsDAO mapDAO = DAOFactory.getSbiGeoMapsDAO();
			GeoMap tmpMap = mapDAO.loadMapByName(mapName);
			if (tmpMap == null) return null;
			List lstFeatures =  mapFeaturesDAO.loadFeatureNamesByMapId(new Integer(tmpMap.getMapId()));
			if (lstFeatures != null){
				for (int i=0; i<lstFeatures.size(); i++){
					toReturn = ((toReturn==null)?"":toReturn) + (String)lstFeatures.get(i) +((i==lstFeatures.size()-1)?"":",");
				}
			}
	 		return toReturn; 		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
		//return mapName + ",centroidi_" + mapName;
	}
	
	private String getMapUrl(HttpServletRequest request, String mapName) {
		String toReturn = null;
		try{			
			ISbiGeoMapsDAO mapDAO = DAOFactory.getSbiGeoMapsDAO();
			GeoMap tmpMap = mapDAO.loadMapByName(mapName);
			if (tmpMap == null) return null;
			toReturn = tmpMap.getUrl();
			//toReturn = toReturn.substring(ConfigSingleton.getRootPath().length());
			toReturn = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + toReturn; 

	 		return toReturn; 		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}				
	}

	/**
	 * Handle a download request of a map file. Reads the file, sends it as an http response attachment.
	 * and in the end deletes the file.
	 * @param request the http request
	 * @param response the http response
	 * @param deleteFile if true delete the downloadedFile
	 */
	private void downloadFile (HttpServletRequest request, HttpServletResponse response) throws Exception {			
		
			String filePathName = ConfigSingleton.getRootPath() + (String)request.getParameter("path");
			
 			//download file
 			try{			
			File fileMap = new File(filePathName);
			String fileName = fileMap.getName();
			response.setHeader("Content-Disposition","attachment; filename=\"" + fileName + "\";");
			byte[] fileContent = "".getBytes();
			FileInputStream fis = null;
			try{
				fis = new FileInputStream(filePathName);
				fileContent = GeneralUtilities.getByteArrayFromInputStream(fis);
			} catch (IOException ioe) {
				SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "manageDownload",
                        			  "Cannot get bytes of the exported file" + ioe);
			}
			response.setContentLength(fileContent.length);
			response.getOutputStream().write(fileContent);
			response.getOutputStream().flush();
		 	if(fis!=null)
		 		fis.close();

		} catch (IOException ioe) {
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "manageDownload",
		                           "Cannot flush response" + ioe);
		}
	}

	private String getAllMapNames() {
		String toReturn = null;
		try{			
			ISbiGeoMapsDAO mapDAO = DAOFactory.getSbiGeoMapsDAO();
			List lstMaps  = mapDAO.loadAllMaps();
			if (lstMaps == null) return null;
			if (lstMaps != null){
				for (int i=0; i<lstMaps.size(); i++){
					toReturn = ((toReturn==null)?"":toReturn) + ((GeoMap)lstMaps.get(i)).getName()+((i==lstMaps.size()-1)?"":",");
				} 	
			}
	 		return toReturn; 		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}			
	}
	
	private String getAllFeatureNames() {
		String toReturn = null;
		try{			
			ISbiGeoFeaturesDAO featureDAO = DAOFactory.getSbiGeoFeaturesDAO();
			List lstFeatures  = featureDAO.loadAllFeatures();
			if (lstFeatures == null) return null;
			if (lstFeatures != null){
				for (int i=0; i<lstFeatures.size(); i++){
					toReturn = ((toReturn==null)?"":toReturn) + ((GeoFeature)lstFeatures.get(i)).getName()+((i==lstFeatures.size()-1)?"":",");
				} 	
			}
	 		return toReturn; 		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}			
	}

}
