/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.container;

import java.util.List;

import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.handlers.ExecutionInstance;

import org.apache.log4j.Logger;

/**
 * Abstract class that implements all <code>it.eng.spagobi.container.IContainer</code> methods apart from get/set/remove/getKeys methods.
 * All other methods are implemented starting from abstract set and get methods. 
 * This class provides implementation for standard objects cast and conversion such as String, Boolean, Integer.
 * 
 * @author Zerbetto (davide.zerbetto@eng.it)
 *
 */
public abstract class AbstractContainer implements IContainer {

	private static transient Logger logger = Logger.getLogger(AbstractContainer.class);
	
	/**
	 * Returns true if no objects are stored into the container with the input key, false otherwise
	 * @param key The input key
	 * @return true if no objects are stored into the container with the input key, false otherwise
	 */
	public boolean isNull(String key) {
		logger.debug("IN");
		try {
			Object object = this.get(key);
			if (object == null) 
				return false;
			else return true;
		} finally {
			logger.debug("OUT");
		}
	}
	
	/**
	 * Returns true if no objects are stored into the container with the input key or if the relevant 
	 * object exists and its string representation is blank, false otherwise
	 * @param key The input key
	 * @return true true if no objects are stored into the container with the input key or if the relevant 
	 * object exists and its string representation is blank, false otherwise
	 */
	public boolean isBlankOrNull(String key) {
		logger.debug("IN");
		try {
			Object object = this.get(key);
			if (object == null) 
				return true;
			else {
				String string = object.toString();
				return (string.trim().equals(""));
			}
		} finally {
			logger.debug("OUT");
		}
	}
	
	/**
	 * Returns the string representation of the object with the given key; if the key has no objects associated, null is returned
	 * @param key The input key
	 * @return the string representation of the object with the given key; if the key has no objects associated, null is returned
	 */
	public String getString(String key) {
		logger.debug("IN");
		try {
			Object object = this.get(key);
			if (object == null) 
				return null;
			if (object instanceof String) 
				return (String) object;
			else return object.toString();
		} finally {
			logger.debug("OUT");
		}
	}
	
	/** 
	 * If the key has no objects associated, null is returned. If a Boolean object is associated to that key, this Boolean is returned.
	 * Otherwise the string representation of the object is parsed with <code>Boolean.parseBoolean(string);<code> and the result is returned.
	 * 
	 * @param key The input key
	 * @return If the key has no objects associated, null is returned. If a Boolean object is associated to that key, this boolean is returned.
	 * Otherwise the string representation of the object is parsed with <code>Boolean.parseBoolean(string);<code> and the result is returned.
	 */
	public Boolean getBoolean(String key) {
		logger.debug("IN");
		try {
			Object object = this.get(key);
			if (object == null) 
				return null;
			if (object instanceof Boolean) 
				return (Boolean) object;
			else {
				String string = object.toString();
				boolean toReturn = false;
				toReturn = Boolean.parseBoolean(string);
				return new Boolean(toReturn);
			}
		} finally {
			logger.debug("OUT");
		}
	}
	
	/** 
	 * If the key has no objects associated, null is returned. If a Integer object is associated to that key, this Integer is returned.
	 * Otherwise the string representation of the object is parsed with <code>Integer.parseInt(string);<code> and the result is returned.
	 * 
	 * @param key The input key
	 * @return If the key has no objects associated, null is returned. If a Integer object is associated to that key, this Integer is returned.
	 * Otherwise the string representation of the object is parsed with <code>Integer.parseInt(string);<code> and the result is returned.
	 */
	public Integer getInteger(String key) {
		logger.debug("IN");
		try {
			Object object = this.get(key);
			if (object == null) 
				return null;
			if (object instanceof Integer) 
				return (Integer) object;
			else {
				String string = object.toString();
				int toReturn = 0;
				toReturn = Integer.parseInt(string);
				return new Integer(toReturn);
			}
		} finally {
			logger.debug("OUT");
		}
	}
	
	/**
	 * <b>TO BE USED ONLY INSIDE SPAGOBI CORE, NOT INSIDE EXTERNAL ENGINES</b>.
	 * Return the BIObject associated with the input key.
	 * If the key is associated to an object that is not a BIObject instance, a ClassCastException is thrown.
	 * 
	 * @param key The input key
	 * @return the BIObject associated with the input key.
	 */
	public BIObject getBIObject(String key) {
		logger.debug("IN");
		BIObject toReturn = null;
		try {
			Object object = get(key);
			toReturn = (BIObject) object;
			return toReturn; 
		} finally {
			logger.debug("OUT");
		}
	}

	/**
	 * <b>TO BE USED ONLY INSIDE SPAGOBI CORE, NOT INSIDE EXTERNAL ENGINES</b>.
	 * Return the ExecutionInstance associated with the input key.
	 * If the key is associated to an object that is not a ExecutionInstance instance, a ClassCastException is thrown.
	 * 
	 * @param key The input key
	 * @return the ExecutionInstance associated with the input key.
	 */
	public ExecutionInstance getExecutionInstance(String key) {
		logger.debug("IN");
		ExecutionInstance toReturn = null;
		try {
			Object object = get(key);
			toReturn = (ExecutionInstance) object;
			return toReturn; 
		} finally {
			logger.debug("OUT");
		}
	}
	
	/**
	 * Return the List associated with the input key.
	 * If the key is associated to an object that is not a List instance, a ClassCastException is thrown.
	 * 
	 * @param key The input key
	 * @return the List associated with the input key.
	 */
	public List getList(String key) {
		logger.debug("IN");
		List toReturn = null;
		try {
			Object object = get(key);
			toReturn = (List) object;
			return toReturn; 
		} finally {
			logger.debug("OUT");
		}
	}
	
}
