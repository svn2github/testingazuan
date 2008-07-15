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
package it.eng.spagobi.container.strategy;

import java.util.Iterator;
import java.util.List;

import it.eng.spago.base.SourceBean;
import it.eng.spago.navigation.LightNavigationManager;
import it.eng.spagobi.container.Context;
import it.eng.spagobi.container.ISessionContainer;

import org.apache.log4j.Logger;

public class LightNavigatorContextRetrieverStrategy implements
		IContextRetrieverStrategy {

	static private Logger logger = Logger.getLogger(LightNavigatorContextRetrieverStrategy.class);
	
	private static final String _sessionAttributeBaseKey = "SPAGOBI_SESSION_ATTRIBUTE";
	private String _key;
	
	public LightNavigatorContextRetrieverStrategy(SourceBean request) {
		logger.debug("IN");
		try {
			String lightNavigatorId = (String) request.getAttribute(LightNavigationManager.LIGHT_NAVIGATOR_ID);
			if (lightNavigatorId == null || lightNavigatorId.trim().equals("")) {
				logger.debug("Request does not contain light navigator id. Using fix base attribute key...");
				_key = _sessionAttributeBaseKey;
			} else {
				logger.debug("Light navigator id found on request: [" + lightNavigatorId + "]");
				_key = _sessionAttributeBaseKey + "_" + lightNavigatorId;
			}
		} finally {
			logger.debug("OUT");
		}
	}
	
	public Context getContext(ISessionContainer sessionContainer) {
		logger.debug("IN");
		try {
			logger.debug("Looking at Context on session with key = [" + _key + "]");
			Context context = (Context) sessionContainer.get(_key);
			return context;
		} finally {
			logger.debug("OUT");
		}
	}

	public Context createContext(ISessionContainer sessionContainer) {
		logger.debug("IN");
		try {
			logger.debug("Creating a new context and putting on session with key = [" + _key + "]");
			Context context = new Context();
			sessionContainer.set(_key, context);
			return context;
		} finally {
			logger.debug("OUT");
		}
	}

	public void destroyCurrentContext(ISessionContainer sessionContainer) {
		logger.debug("IN");
		try {
			Context context = (Context) sessionContainer.get(_key);
			if (context != null) {
				sessionContainer.remove(_key);
			} else {
				logger.warn("Context not found!!");
			}
		} finally {
			logger.debug("OUT");
		}
	}

	public void destroyContextsOlderThan(ISessionContainer session,
			int minutes) {
		logger.debug("IN");
		try {
			synchronized (session) {
				List attributeNames = session.getKeys();
				Iterator it = attributeNames.iterator();
				while (it.hasNext()) {
					String attributeName = (String) it.next();
					if (!attributeName.startsWith(_sessionAttributeBaseKey)) {
						Object attributeObject = session.get(attributeName);
						if (attributeObject instanceof Context) {
							Context context = (Context) attributeObject;
							if (context.isOlderThan(minutes)) {
								logger.debug("Deleting context instance with creation date = [" + context.getCreationDate() + "]");
								session.remove(attributeName);
							}
						} else {
							logger.debug("Session attribute with key [" + attributeName + "] is not a Context object; cannot delete it.");
						}
					}
				}
			}
		} finally {
			logger.debug("OUT");
		}
	}

}
