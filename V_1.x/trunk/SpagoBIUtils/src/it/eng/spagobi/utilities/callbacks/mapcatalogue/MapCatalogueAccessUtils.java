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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.DefaultMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * A proxy class used by clients to remotly access the spagoBI EventHandler 
 * interface in a customized way.
 * 
 * @author Gioia
 */
public class MapCatalogueAccessUtils {
	
	private String mapCatalogueManagerServletUrl;
	
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
	
	public MapCatalogueAccessUtils(String eventsManagerServletUrl) {
		this.mapCatalogueManagerServletUrl = eventsManagerServletUrl;
	}
	
	public String getStandardHierarchy() throws Exception {
		HttpClient client = new HttpClient();
	    PostMethod httppost = new PostMethod(mapCatalogueManagerServletUrl);
	    NameValuePair[] parameters = {  new NameValuePair("operation", GET_STANDARD_HIERARCHY) };
	    
	    //	  Provide custom retry handler is necessary
	    DefaultMethodRetryHandler retryhandler = new DefaultMethodRetryHandler();
	    retryhandler.setRequestSentRetryEnabled(false);
	    retryhandler.setRetryCount(3);
	    httppost.setMethodRetryHandler(retryhandler);
	    httppost.setRequestBody(parameters);
        byte[] responseBody = null; 
        try {
            // Execute the method.        	
            int statusCode = client.executeMethod(httppost);
            if (statusCode != HttpStatus.SC_OK) {
              System.err.println("Method failed: " + httppost.getStatusLine());
            }
            // Read the response body.
             responseBody  = httppost.getResponseBody();
        } catch (IOException e) {
        	throw new Exception("Error while getting default hierarchy");
        } finally {
            // Release the connection.
          	httppost.releaseConnection();
        }
        
        if (responseBody == null) throw new Exception("Error while getting default hierarchy");
        
        String result = new String(responseBody);
        if(result.equalsIgnoreCase(ERROR_HIERARCHY_NOT_FOUND)) {        
        	throw new Exception("Default Hierarchy not found. ");
        }
       
        
        return result;
	}
	
	public List getMapNamesByFeature(String featureName) throws Exception {
		HttpClient client = new HttpClient();
	    PostMethod httppost = new PostMethod(mapCatalogueManagerServletUrl);
	    NameValuePair[] parameters = {  new NameValuePair("operation", GET_MAPS_BY_FEATURE), 
	    								new NameValuePair("featureName", featureName) };
	    
	    //	  Provide custom retry handler is necessary
	    DefaultMethodRetryHandler retryhandler = new DefaultMethodRetryHandler();
	    retryhandler.setRequestSentRetryEnabled(false);
	    retryhandler.setRetryCount(3);
	    httppost.setMethodRetryHandler(retryhandler);
	    httppost.setRequestBody(parameters);
        byte[] responseBody = null; 
        try {
            // Execute the method.        	
            int statusCode = client.executeMethod(httppost);
            if (statusCode != HttpStatus.SC_OK) {
              System.err.println("Method failed: " + httppost.getStatusLine());
            }
            // Read the response body.
             responseBody  = httppost.getResponseBody();
        } catch (IOException e) {
        	throw new Exception("Error while reading maps about feature " + featureName);
        } finally {
            // Release the connection.
          	httppost.releaseConnection();
        }
        
        if (responseBody == null) throw new Exception("Error while reading maps about feature " + featureName);
        
        String result = new String(responseBody);
        if(result.equalsIgnoreCase(ERROR_MAP_NOT_FOUND)) {        
        	throw new Exception("Maps about " + featureName + " not found. ");
        }
        
        String[] maps = result.split(",");
        List mapList = new ArrayList();
        for(int i = 0; i < maps.length; i++) {
        	mapList.add(maps[i]);        	
        }
        
        return mapList;
	}

	public List getFeatureNamesInMap(String mapName) throws Exception {
		HttpClient client = new HttpClient();
	    PostMethod httppost = new PostMethod(mapCatalogueManagerServletUrl);
	    NameValuePair[] parameters = {  new NameValuePair("operation", GET_FEATURES_IN_MAP), 
	    								new NameValuePair("mapName", mapName) };
	    
	    //	  Provide custom retry handler is necessary
	    DefaultMethodRetryHandler retryhandler = new DefaultMethodRetryHandler();
	    retryhandler.setRequestSentRetryEnabled(false);
	    retryhandler.setRetryCount(3);
	    httppost.setMethodRetryHandler(retryhandler);
	    httppost.setRequestBody(parameters);
        byte[] responseBody = null; 
        try {
            // Execute the method.        	
            int statusCode = client.executeMethod(httppost);
            if (statusCode != HttpStatus.SC_OK) {
              System.err.println("Method failed: " + httppost.getStatusLine());
            }
            // Read the response body.
             responseBody  = httppost.getResponseBody();
        } catch (IOException e) {
        	throw new Exception("Error while reading features about map " + mapName);
        } finally {
            // Release the connection.
          	httppost.releaseConnection();
        }
        
        if (responseBody == null) throw new Exception("Error while reading features about map " + mapName);
        
        String result = new String(responseBody);
        if(result.startsWith(ERROR_FEATURE_NOT_FOUND)) {        	
        	throw new Exception("Features about " + mapName + " not found. ");
        }
        
        String[] features = result.split(",");
        List featureList = new ArrayList();
        for(int i = 0; i < features.length; i++) {
        	featureList.add(features[i]);        	
        }
        
        return featureList;
	}
	
	public String getMapUrl(String mapName)throws Exception {
		HttpClient client = new HttpClient();
	    PostMethod httppost = new PostMethod(mapCatalogueManagerServletUrl);
	    NameValuePair[] parameters = {  new NameValuePair("operation", GET_MAP_URL), 
	    								new NameValuePair("mapName", mapName) };
	    
	    //	  Provide custom retry handler is necessary
	    DefaultMethodRetryHandler retryhandler = new DefaultMethodRetryHandler();
	    retryhandler.setRequestSentRetryEnabled(false);
	    retryhandler.setRetryCount(3);
	    httppost.setMethodRetryHandler(retryhandler);
	    httppost.setRequestBody(parameters);
        byte[] responseBody = null; 
        try {
            // Execute the method.        	
            int statusCode = client.executeMethod(httppost);
            if (statusCode != HttpStatus.SC_OK) {
              System.err.println("Method failed: " + httppost.getStatusLine());
            }
            // Read the response body.
             responseBody  = httppost.getResponseBody();
        } catch (IOException e) {
        	throw new Exception("Error while reading map url " + mapName);
        } finally {
            // Release the connection.
          	httppost.releaseConnection();
        }
        
        if (responseBody == null) throw new Exception("Error while reading map url " + mapName);
        
        String result = new String(responseBody);
        if(result.startsWith(ERROR_FEATURE_NOT_FOUND)) {        	
        	throw new Exception("Map about " + mapName + " not found. ");
        }
        return result;
	}
	
	public List getAllMapNames() throws Exception {
		HttpClient client = new HttpClient();
	    PostMethod httppost = new PostMethod(mapCatalogueManagerServletUrl);
	    NameValuePair[] parameters = {  new NameValuePair("operation", GET_ALL_MAP_NAMES)};
	    
	    //	  Provide custom retry handler is necessary
	    DefaultMethodRetryHandler retryhandler = new DefaultMethodRetryHandler();
	    retryhandler.setRequestSentRetryEnabled(false);
	    retryhandler.setRetryCount(3);
	    httppost.setMethodRetryHandler(retryhandler);
	    httppost.setRequestBody(parameters);
        byte[] responseBody = null; 
        try {
            // Execute the method.        	
            int statusCode = client.executeMethod(httppost);
            if (statusCode != HttpStatus.SC_OK) {
              System.err.println("Method failed: " + httppost.getStatusLine());
            }
            // Read the response body.
             responseBody  = httppost.getResponseBody();
        } catch (IOException e) {
        	throw new Exception("Error while reading maps");
        } finally {
            // Release the connection.
          	httppost.releaseConnection();
        }
        
        if (responseBody == null) throw new Exception("Error while reading maps ");
        
        String result = new String(responseBody);
        if(result.equalsIgnoreCase(ERROR_MAP_NOT_FOUND)) {        
        	throw new Exception("Maps not found. ");
        }        
        String[] maps = result.split(",");
        List mapList = new ArrayList();
        for(int i = 0; i < maps.length; i++) {
        	mapList.add(maps[i]);        	
        }
        
        return mapList;
	}
	
	public List getAllFeatureNames() throws Exception {
		HttpClient client = new HttpClient();
	    PostMethod httppost = new PostMethod(mapCatalogueManagerServletUrl);
	    NameValuePair[] parameters = {  new NameValuePair("operation", GET_ALL_FEATURE_NAMES)};
	    
	    //	  Provide custom retry handler is necessary
	    DefaultMethodRetryHandler retryhandler = new DefaultMethodRetryHandler();
	    retryhandler.setRequestSentRetryEnabled(false);
	    retryhandler.setRetryCount(3);
	    httppost.setMethodRetryHandler(retryhandler);
	    httppost.setRequestBody(parameters);
        byte[] responseBody = null; 
        try {
            // Execute the method.        	
            int statusCode = client.executeMethod(httppost);
            if (statusCode != HttpStatus.SC_OK) {
              System.err.println("Method failed: " + httppost.getStatusLine());
            }
            // Read the response body.
             responseBody  = httppost.getResponseBody();
        } catch (IOException e) {
        	throw new Exception("Error while reading Features");
        } finally {
            // Release the connection.
          	httppost.releaseConnection();
        }
        
        if (responseBody == null) throw new Exception("Error while reading Features ");
        
        String result = new String(responseBody);
        if(result.equalsIgnoreCase(ERROR_FEATURE_NOT_FOUND)) {        
        	throw new Exception("Feature not found. ");
        }        
        String[] maps = result.split(",");
        List mapList = new ArrayList();
        for(int i = 0; i < maps.length; i++) {
        	mapList.add(maps[i]);        	
        }
        
        return mapList;
	}
}
