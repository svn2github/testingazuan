/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package it.eng.spagobi.rest.client;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Generated;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

@Generated(value = {
    "wadl|file:/C:/Users/FRANCE~1/AppData/Local/Temp/wadl_4700262930860509830.wadl"
}, comments = "wadl2java, http://wadl.java.net", date = "2013-05-13T16:54:17.671+02:00")
public class TilabClient {

	public final static String CONFIG_FILE = "restclient.properties";
	
    /**
     * The base URI for the resource represented by this proxy
     * 
     */
    public final static URI BASE_URI;

    static {
        //URI originalURI = URI.create("http://localhost:8080/mdalayer/rest/");
    	
    	Properties properties = new Properties();
    	try {
			properties.load(TilabClient.class.getClassLoader().getResourceAsStream(CONFIG_FILE));
		} catch (IOException e) {
			throw new RuntimeException("Cannot read " + CONFIG_FILE + " file", e);
		}
    	
    	URI originalURI = URI.create(properties.getProperty("rest.server.url"));
        
        // Look up to see if we have any indirection in the local copy
        // of META-INF/java-rs-catalog.xml file, assuming it will be in the
        // oasis:name:tc:entity:xmlns:xml:catalog namespace or similar duck type
        java.io.InputStream is = TilabClient.class.getResourceAsStream("/META-INF/jax-rs-catalog.xml");
        if (is!=null) {
            try {
                // Ignore the namespace in the catalog, can't use wildcard until
                // we are sure we have XPath 2.0
                String found = javax.xml.xpath.XPathFactory.newInstance().newXPath().evaluate(
                    "/*[name(.) = 'catalog']/*[name(.) = 'uri' and @name ='" + originalURI +"']/@uri", 
                    new org.xml.sax.InputSource(is)); 
                if (found!=null && found.length()>0) {
                    originalURI = java.net.URI.create(found);
                }
                
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            finally {
                try {
                    is.close();
                } catch (java.io.IOException e) {
                }
            }
        }
        BASE_URI = originalURI;
    }

    public static TilabClient.V1 v1(Client client, URI baseURI) {
        return new TilabClient.V1(client, baseURI);
    }

    public static TilabClient.V1 v1() {
        return v1(Client.create(), BASE_URI);
    }

    public static TilabClient.V1 v1(Client client) {
        return v1(client, BASE_URI);
    }

    public static class V1 {

        private Client _client;
        private UriBuilder _uriBuilder;
        private Map<String, Object> _templateAndMatrixParameterValues;
        private URI _uri;

        /**
         * Create new instance using existing Client instance
         * 
         */
        public V1(Client client, URI uri) {
            _client = client;
            _uri = uri;
            _uriBuilder = UriBuilder.fromUri(uri);
            _uriBuilder = _uriBuilder.path("v1");
            _templateAndMatrixParameterValues = new HashMap<String, Object>();
        }

        private V1(Client client, UriBuilder uriBuilder, Map<String, Object> map) {
            _client = client;
            _uriBuilder = uriBuilder.clone();
            _templateAndMatrixParameterValues = map;
        }

        public TilabClient.V1 .Objects objects() {
            return new TilabClient.V1 .Objects(_client, _uriBuilder.buildFromMap(_templateAndMatrixParameterValues));
        }

        public TilabClient.V1 .ObjectTypes objectTypes() {
            return new TilabClient.V1 .ObjectTypes(_client, _uriBuilder.buildFromMap(_templateAndMatrixParameterValues));
        }

        public TilabClient.V1 .ObjectsId objectsId(String id) {
            return new TilabClient.V1 .ObjectsId(_client, _uriBuilder.buildFromMap(_templateAndMatrixParameterValues), id);
        }

        public TilabClient.V1 .Test test() {
            return new TilabClient.V1 .Test(_client, _uriBuilder.buildFromMap(_templateAndMatrixParameterValues));
        }

        public static class ObjectTypes {

            private Client _client;
            private UriBuilder _uriBuilder;
            private Map<String, Object> _templateAndMatrixParameterValues;
            private URI _uri;

            /**
             * Create new instance using existing Client instance
             * 
             */
            public ObjectTypes(Client client, URI uri) {
                _client = client;
                _uri = uri;
                _uriBuilder = UriBuilder.fromUri(uri);
                _uriBuilder = _uriBuilder.path("/objectTypes");
                _templateAndMatrixParameterValues = new HashMap<String, Object>();
            }

            private ObjectTypes(Client client, UriBuilder uriBuilder, Map<String, Object> map) {
                _client = client;
                _uriBuilder = uriBuilder.clone();
                _templateAndMatrixParameterValues = map;
            }

            public<T >T getAsJson(GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/json");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                return response.getEntity(returnType);
            }

            public<T >T getAsJson(Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/json");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                return response.getEntity(returnType);
            }

        }

        public static class Objects {

            private Client _client;
            private UriBuilder _uriBuilder;
            private Map<String, Object> _templateAndMatrixParameterValues;
            private URI _uri;

            /**
             * Create new instance using existing Client instance
             * 
             */
            public Objects(Client client, URI uri) {
                _client = client;
                _uri = uri;
                _uriBuilder = UriBuilder.fromUri(uri);
                _uriBuilder = _uriBuilder.path("/objects");
                _templateAndMatrixParameterValues = new HashMap<String, Object>();
            }

            private Objects(Client client, UriBuilder uriBuilder, Map<String, Object> map) {
                _client = client;
                _uriBuilder = uriBuilder.clone();
                _templateAndMatrixParameterValues = map;
            }

            public<T >T postJson(Object input, GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/json");
                resourceBuilder = resourceBuilder.type("application/json");
                ClientResponse response;
                response = resourceBuilder.method("POST", ClientResponse.class, input);
                return response.getEntity(returnType);
            }

            public<T >T postJson(Object input, Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/json");
                resourceBuilder = resourceBuilder.type("application/json");
                ClientResponse response;
                response = resourceBuilder.method("POST", ClientResponse.class, input);
                return response.getEntity(returnType);
            }

            public<T >T postJson(Object input, String user, String authorization, GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/json");
                if (user!= null) {
                    resourceBuilder = resourceBuilder.header("User", user);
                }
                if (authorization!= null) {
                    resourceBuilder = resourceBuilder.header("Authorization", authorization);
                }
                resourceBuilder = resourceBuilder.type("application/json");
                ClientResponse response;
                response = resourceBuilder.method("POST", ClientResponse.class, input);
                return response.getEntity(returnType);
            }

            public<T >T postJson(Object input, String user, String authorization, Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/json");
                if (user!= null) {
                    resourceBuilder = resourceBuilder.header("User", user);
                }
                if (authorization!= null) {
                    resourceBuilder = resourceBuilder.header("Authorization", authorization);
                }
                resourceBuilder = resourceBuilder.type("application/json");
                ClientResponse response;
                response = resourceBuilder.method("POST", ClientResponse.class, input);
                return response.getEntity(returnType);
            }

            public<T >T getAsJson(GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/json");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                return response.getEntity(returnType);
            }

            public<T >T getAsJson(Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/json");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                return response.getEntity(returnType);
            }

            public<T >T getAsJson(String offset, String limit, String type, String user, String authorization, GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                localUriBuilder = localUriBuilder.replaceQueryParam("offset", offset);
                localUriBuilder = localUriBuilder.replaceQueryParam("limit", limit);
                localUriBuilder = localUriBuilder.replaceQueryParam("type", type);
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/json");
                if (user!= null) {
                    resourceBuilder = resourceBuilder.header("User", user);
                }
                if (authorization!= null) {
                    resourceBuilder = resourceBuilder.header("Authorization", authorization);
                }
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                return response.getEntity(returnType);
            }

            public<T >T getAsJson(String offset, String limit, String type, String user, String authorization, Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                localUriBuilder = localUriBuilder.replaceQueryParam("offset", offset);
                localUriBuilder = localUriBuilder.replaceQueryParam("limit", limit);
                localUriBuilder = localUriBuilder.replaceQueryParam("type", type);
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/json");
                if (user!= null) {
                    resourceBuilder = resourceBuilder.header("User", user);
                }
                if (authorization!= null) {
                    resourceBuilder = resourceBuilder.header("Authorization", authorization);
                }
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                return response.getEntity(returnType);
            }

        }

        public static class ObjectsId {

            private Client _client;
            private UriBuilder _uriBuilder;
            private Map<String, Object> _templateAndMatrixParameterValues;
            private URI _uri;

            /**
             * Create new instance using existing Client instance
             * 
             */
            public ObjectsId(Client client, URI uri, String id) {
                _client = client;
                _uri = uri;
                _uriBuilder = UriBuilder.fromUri(uri);
                _uriBuilder = _uriBuilder.path("/objects/{id}");
                _templateAndMatrixParameterValues = new HashMap<String, Object>();
                _templateAndMatrixParameterValues.put("id", id);
            }

            private ObjectsId(Client client, UriBuilder uriBuilder, Map<String, Object> map) {
                _client = client;
                _uriBuilder = uriBuilder.clone();
                _templateAndMatrixParameterValues = map;
            }

            /**
             * Get id
             * 
             */
            public String getId() {
                return ((String) _templateAndMatrixParameterValues.get("id"));
            }

            /**
             * Duplicate state and set id
             * 
             */
            public TilabClient.V1 .ObjectsId setId(String id) {
                Map<String, Object> copyMap;
                copyMap = new HashMap<String, Object>(_templateAndMatrixParameterValues);
                UriBuilder copyUriBuilder = _uriBuilder.clone();
                copyMap.put("id", id);
                return new TilabClient.V1 .ObjectsId(_client, copyUriBuilder, copyMap);
            }

            public<T >T getAsJson(GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/json");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                return response.getEntity(returnType);
            }

            public<T >T getAsJson(Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/json");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                return response.getEntity(returnType);
            }

            public<T >T getAsJson(String user, String authorization, GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/json");
                if (user!= null) {
                    resourceBuilder = resourceBuilder.header("User", user);
                }
                if (authorization!= null) {
                    resourceBuilder = resourceBuilder.header("Authorization", authorization);
                }
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                return response.getEntity(returnType);
            }

            public<T >T getAsJson(String user, String authorization, Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/json");
                if (user!= null) {
                    resourceBuilder = resourceBuilder.header("User", user);
                }
                if (authorization!= null) {
                    resourceBuilder = resourceBuilder.header("Authorization", authorization);
                }
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                return response.getEntity(returnType);
            }

        }

        public static class Test {

            private Client _client;
            private UriBuilder _uriBuilder;
            private Map<String, Object> _templateAndMatrixParameterValues;
            private URI _uri;

            /**
             * Create new instance using existing Client instance
             * 
             */
            public Test(Client client, URI uri) {
                _client = client;
                _uri = uri;
                _uriBuilder = UriBuilder.fromUri(uri);
                _uriBuilder = _uriBuilder.path("/test");
                _templateAndMatrixParameterValues = new HashMap<String, Object>();
            }

            private Test(Client client, UriBuilder uriBuilder, Map<String, Object> map) {
                _client = client;
                _uriBuilder = uriBuilder.clone();
                _templateAndMatrixParameterValues = map;
            }

            public<T >T getAs(GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("*/*");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                return response.getEntity(returnType);
            }

            public<T >T getAs(Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("*/*");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                return response.getEntity(returnType);
            }

        }

    }

}
