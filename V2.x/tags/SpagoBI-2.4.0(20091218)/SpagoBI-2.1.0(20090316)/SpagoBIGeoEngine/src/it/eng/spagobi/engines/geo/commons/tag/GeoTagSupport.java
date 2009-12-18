/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.spagobi.engines.geo.commons.tag;

import it.eng.spago.base.SessionContainer;
import it.eng.spagobi.container.ContextManager;
import it.eng.spagobi.container.SpagoBIHttpSessionContainer;
import it.eng.spagobi.container.SpagoBIRequestContainer;
import it.eng.spagobi.container.strategy.ExecutionContextRetrieverStrategy;
import it.eng.spagobi.container.strategy.IContextRetrieverStrategy;
import it.eng.spagobi.engines.geo.GeoEngineInstance;
import it.eng.spagobi.utilities.engines.EngineConstants;

import java.util.Locale;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class QbeTagSupport.
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class GeoTagSupport extends BaseTagSupport {
	
	protected ContextManager getContextManager() {
		if(pageContext == null) {
			return null;
		}
		
		if(pageContext.getAttribute("contextManager") == null) {
			IContextRetrieverStrategy contextRetriveStrategy;
			SpagoBIHttpSessionContainer sessionHttpContainer = new SpagoBIHttpSessionContainer( getRequest().getSession() );
			String executionId = getRequest().getParameter("SBI_EXECUTION_ID");
			contextRetriveStrategy = new ExecutionContextRetrieverStrategy( executionId );
			ContextManager conetxtManager = new ContextManager(sessionHttpContainer, contextRetriveStrategy);
			pageContext.setAttribute("contextManager", conetxtManager );
		}
		return (ContextManager)pageContext.getAttribute("contextManager");
	}
	
	protected GeoEngineInstance getEngineInstance() {
		if(pageContext == null) {
			return null;
		}
		
		if(pageContext.getAttribute("engineInstance") == null) {
			pageContext.setAttribute("engineInstance", getContextManager().get(EngineConstants.ENGINE_INSTANCE) );
		}
		return (GeoEngineInstance)pageContext.getAttribute("engineInstance");
	}
	
	
	/**
	 * Gets the locale.
	 * 
	 * @return the locale
	 */
	protected Locale getLocale() {
		if(pageContext == null) {
			return null;
		}
		
		if(pageContext.getAttribute("locale") == null) {
			pageContext.setAttribute("locale", getEngineInstance().getEnv().get(EngineConstants.ENV_LOCALE) );
		}
		return (Locale)pageContext.getAttribute("locale");
	}
	
	
	/**
	 * Gets the resource url.
	 * 
	 * @param url the url
	 * 
	 * @return the resource url
	 */
	protected String getResourceUrl(String url) {
		return WebUrlGenerator.getInstance().getResourceUrl(getRequest() , url);
	}
	
	/**
	 * Gets the action url.
	 * 
	 * @param parameters the parameters
	 * 
	 * @return the action url
	 */
	protected String getActionUrl(Map parameters) {
		return WebUrlGenerator.getInstance().getActionUrl(getRequest() , parameters);
	}
}

