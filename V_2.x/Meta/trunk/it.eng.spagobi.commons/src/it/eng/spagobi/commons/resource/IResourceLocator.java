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

import java.io.File;
import java.net.URL;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public interface IResourceLocator {
	/** 
	   * Returns the URL from which all resources are based.
	   * @return the URL from which all resources are based.
	   */
	  URL getBaseURL();
	  
	  File getFile(String fileRelativePath);

	  /**
	   * Returns the description that can be used to create the image resource associated with the key.
	   * The description will typically be in the form of a URL to the image data.
	   * Creation of an actual image depends on the GUI environment;
	   * 
	   * @param key the key of the image resource.
	   * @return the description on the image resource.
	   */
	  Object getImage(String key);
	  
	  /**
	   * Return the property value associated with the key. The type of the value is typically a String
	   * 
	   * @param key the key of the property resource.
	   * @return the value of the property
	   */
	  Object getProperty(String key);
	  
	  String getPropertyAsString(String key);
	  Integer getPropertyAsInteger(String key);
	  
	  /**
	   * Returns the string resource associated with the key, translated to the current locale.
	   * 
	   * @param key the key of the string resource.
	   * @return the string resource associated with the key.
	   */
	  String getString(String key);


	  /**
	   * Returns a string resource associated with the key, and performs substitutions.
	   * 
	   * @param key the key of the string.
	   * @param substitutions the message substitutions.
	   * @return a string resource associated with the key.
	  
	   * @see #getString(String)
	   * @see java.text.MessageFormat#format(String, Object[])
	   */
	  String getString(String key, Object [] substitutions);
}
