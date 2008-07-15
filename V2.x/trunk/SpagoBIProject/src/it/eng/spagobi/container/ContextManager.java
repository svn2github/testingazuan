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

import java.util.ArrayList;
import java.util.List;

import it.eng.spagobi.container.strategy.IContextRetrieverStrategy;

import org.apache.log4j.Logger;

public class ContextManager extends AbstractContainer {

	protected ISessionContainer _session;
	protected IContextRetrieverStrategy _strategy;
	
	static private Logger logger = Logger.getLogger(ContextManager.class);
	
	public ContextManager(ISessionContainer session, IContextRetrieverStrategy strategy) {
		logger.debug("IN");
		try {
			if (session == null)
				throw new ExceptionInInitializerError("Session in input is null");
			if (strategy == null)
				throw new ExceptionInInitializerError("Strategy in input is null");
			_session = session;
			_strategy = strategy;
		} finally {
			logger.debug("OUT");
		}
	}
	
	/**
	 * Retrieves a generic object from session using the given key.
	 * @param key The key of the object in session
	 * @return The object in session with the given key
	 */
	public Object get(String key) {
		logger.debug("IN: input key = [" + key + "]");
		if (key == null) {
			logger.warn("Input key is null!! Returning null");
			return null;
		}
		Object toReturn = null;
		try {
			Context context = _strategy.getContext(_session);
			if (context != null) {
				logger.debug("Context retrieved");
				toReturn = context.get(key);
				if (toReturn == null) {
					logger.debug("Object not found.");
				} else {
					logger.debug("Found object.");
				}
			} else {
				logger.debug("SpagoBISessionAttribute not retrieved");
			}
		} finally {
			logger.debug("OUT");
		}
		return toReturn;
	}
	
	/**
	 * Sets a generic object into session using the given key.
	 * @param key The key to be used to store object in session
	 */
	public void set(String key, Object object) {
		logger.debug("IN: input key = [" + key + "], object = [" + object + "]");
		if (key == null || object == null) {
			logger.warn("Input key or object is null!! Object will not be put on session");
			return;
		}
		try {
			Context context = _strategy.getContext(_session);
			if (context == null) {
				context = _strategy.createContext(_session);
			}
			Object previous = context.get(key);
			if (previous == null) {
				context.set(key, object);
			} else {
				logger.debug("Found an existing object in session with key = [" + key + "]: it will be overwritten.");
				context.set(key, object);
			}
		} finally {
			logger.debug("OUT");
		}
	}
	
	/**
	 * Removes a generic object from session using the given key.
	 * @param key The key of the object on session.
	 */
	public void remove(String key) {
		logger.debug("IN: input key = [" + key + "]");
		if (key == null) {
			logger.warn("Input key is null!! Object will not be removed from session");
			return;
		}
		try {
			Context context = _strategy.getContext(_session);
			if (context != null) {
				Object object = context.get(key);
				if (object == null) {
					logger.warn("Object not found!!");
				} else {
					logger.debug("Found an existing object in context with key = [" + key + "]: it will be removed.");
					context.remove(key);
				}
			} else {
				logger.warn("Map not found!!");
			}
		} finally {
			logger.debug("OUT");
		}
	}
	
	/**
	 * Destroys context.
	 */
	public void destroyCurrentContext() {
		logger.debug("IN");
		try {
			_strategy.destroyCurrentContext(_session);
		} finally {
			logger.debug("OUT");
		}
	}
	
	public void cleanOldContexts(int minutes) {
		logger.debug("IN");
		try {
			_strategy.destroyContextsOlderThan(_session, minutes);
		} finally {
			logger.debug("OUT");
		}
	}

	public List getKeys() {
		logger.debug("IN");
		List toReturn = new ArrayList();
		try {
			Context context = _strategy.getContext(_session);
			toReturn = context.getKeys();
			return toReturn;
		} finally {
			logger.debug("OUT");
		}
	}
	
}
