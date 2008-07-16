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

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.container.Context;
import it.eng.spagobi.container.ISessionContainer;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * This strategy create/retrieve/destroy the context using the "SBI_EXECUTION_ID" attribute contained into the Spago request 
 * SourceBean object.
 * The context is put on ISessionContainer object with a key that has a fix part "SPAGOBI_SESSION_ATTRIBUTE" and a dynamic part, the 
 * "SBI_EXECUTION_ID" request attribute; if this attribute is missing, the key used to put context on session is 
 * the static string "SPAGOBI_SESSION_ATTRIBUTE".
 * 
 * @author Zerbetto (davide.zerbetto@eng.it)
 *
 */
public class ExecutionContextRetrieverStrategy implements
		IContextRetrieverStrategy {

	static private Logger logger = Logger.getLogger(ExecutionContextRetrieverStrategy.class);
	
	private static final String _sessionAttributeBaseKey = "SPAGOBI_SESSION_ATTRIBUTE";
	private static final String EXECUTION_ID = "SBI_EXECUTION_ID";
	
	private String _key;
	
	/**
	 * Look for the "SBI_EXECUTION_ID" attribute on request to get the key for context storage on session.
	 * @param request The Spago SourceBean service request object
	 */
	public ExecutionContextRetrieverStrategy(SourceBean request) {
		logger.debug("IN");
		try {
			String executionId = (String) request.getAttribute(EXECUTION_ID);
			if (executionId == null || executionId.trim().equals("")) {
				logger.debug("Request does not contain execution id. Using fix base attribute key...");
				_key = _sessionAttributeBaseKey;
			} else {
				logger.debug("Execution id found on request: [" + executionId + "]");
				_key = _sessionAttributeBaseKey + "_" + executionId;
			}
		} finally {
			logger.debug("OUT");
		}
	}
	
	/**
	 * Retrieves the context from the input ISessionContainer instance
	 */
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

	/**
	 * Creates a new context and puts it on the input ISessionContainer instance
	 */
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

	/**
	 * Destroys the current context on the input ISessionContainer instance
	 */
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

	/**
	 * Destroys all the contexts on the input ISessionContainer instance older than the number of minutes specified at input.
	 */
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
								logger.debug("Deleting context instance with last usage date = [" + context.getLastUsageDate() + "]");
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
