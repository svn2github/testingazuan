/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.commons.resource;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class DefaultResourceLocator implements IResourceLocator {

	String pluginId;
	
	public Properties images;
	public Properties labels;
	public Properties settings;
	
	public static final String IMAGE_ROOT_FOOLDER = "icons";
	public static final String CONF_ROOT_FOOLDER = "conf";
	
	private static Logger logger = LoggerFactory.getLogger(DefaultResourceLocator.class);
	
	public DefaultResourceLocator(String pluginId) {
		this.pluginId = pluginId;
		loadImages();
		loadLabels();
		loadSettings();
	}
	
	@Override
	public URL getBaseURL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getImage(String key) {
		URL imageURL;
		
		Bundle bundle = Platform.getBundle(pluginId); 
		imageURL = bundle.getResource(getImagePath(key));
		
		return imageURL;
	}
	
	private String getImagePath(String imageKey) {
		String imagePath;
		
		imagePath = null;
		if(images.containsKey(imageKey)) {
			imagePath =  IMAGE_ROOT_FOOLDER + "/" + images.get(imageKey);
		} else {
			imagePath =  IMAGE_ROOT_FOOLDER + "/" + imageKey;
		}
		
		return imagePath;
	}
	
	private Properties loadProperties(String propertiesFile) {
		Properties properties = new Properties();
		
		Bundle bundle = Platform.getBundle(pluginId); 
		URL resourceFileURL = bundle.getResource( CONF_ROOT_FOOLDER + "/" + propertiesFile );
		if(resourceFileURL != null) {
			try {
				properties.load( resourceFileURL.openStream() );
			} catch (IOException e) {
				logger.error("Impossible to load propertis from URL [" + resourceFileURL + "]", e);
			}
		}
		
		logger.debug("Succesfully loaded [{}] properties from file [{}]", properties.size(), resourceFileURL);
		
		return properties;
	}
	
	private void loadImages() {
		images = loadProperties("images.properties");
	}
	
	private void loadLabels() {
		labels = loadProperties("labels.properties");
	}
	
	private void loadSettings() {
		settings = loadProperties("settings.properties");
	}

	@Override
	public Object getProperty(String key) {
		return settings.getProperty(key);
	}

	@Override
	public String getString(String key) {
		String label = labels.getProperty(key);
		return label != null? label: key;
	}

	@Override
	public String getString(String key, Object[] substitutions) {
		String label = labels.getProperty(key);
		return label != null? label: key;
	}

}
