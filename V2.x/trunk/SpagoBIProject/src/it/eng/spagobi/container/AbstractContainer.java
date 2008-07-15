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

public abstract class AbstractContainer implements IContainer {

	private static transient Logger logger = Logger.getLogger(AbstractContainer.class);

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
