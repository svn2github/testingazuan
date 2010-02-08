/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2009 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.engines.console;

import java.util.Locale;
import java.util.Map;

import org.json.JSONObject;

import it.eng.spagobi.services.proxy.EventServiceProxy;
import it.eng.spagobi.tools.dataset.bo.IDataSet;
import it.eng.spagobi.tools.datasource.bo.IDataSource;
import it.eng.spagobi.utilities.engines.AbstractEngineInstance;
import it.eng.spagobi.utilities.engines.AuditServiceProxy;
import it.eng.spagobi.utilities.engines.EngineConstants;
import it.eng.spagobi.utilities.engines.IEngineAnalysisState;
import it.eng.spagobi.utilities.engines.SpagoBIEngineException;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;

/**
 * @author Antonella Giachino (antonella.giachino@eng.it)
 */
public class ConsoleEngineInstance extends AbstractEngineInstance {
	private JSONObject template;
	
	public ConsoleEngineInstance(Object template, Map env) {
		super( env );	
		
		JSONObject templateJSON;
		
		templateJSON = null;
		if(template instanceof JSONObject) {
			templateJSON = (JSONObject)template;
		} else {
			try {
				templateJSON = new JSONObject(template);
			} catch (Throwable t) {
				throw new SpagoBIRuntimeException("Impossible to parse template", t);
			}
		}
				
		setTemplate(templateJSON);		
	}
	

	public JSONObject getTemplate() {
		return template;
	}
	
	public void setTemplate(JSONObject template) {
		this.template = template;
	}

	
	public IDataSource getDataSource() {
		return (IDataSource)this.getEnv().get(EngineConstants.ENV_DATASOURCE);
	}
	
	public IDataSet getDataSet() {
		return (IDataSet)this.getEnv().get(EngineConstants.ENV_DATASET);
	}
	
	public Locale getLocale() {
		return (Locale)this.getEnv().get(EngineConstants.ENV_LOCALE);
	}
	
	public AuditServiceProxy getAuditServiceProxy() {
		return (AuditServiceProxy)this.getEnv().get(EngineConstants.ENV_AUDIT_SERVICE_PROXY);
	}
	
	public EventServiceProxy getEventServiceProxy() {
		return (EventServiceProxy)this.getEnv().get(EngineConstants.ENV_EVENT_SERVICE_PROXY);
	}

	
	// -- unimplemented methods ------------------------------------------------------------

	public IEngineAnalysisState getAnalysisState() {
		throw new ConsoleEngineRuntimeException("Unsupported method [getAnalysisState]");
	}


	public void setAnalysisState(IEngineAnalysisState analysisState) {
		throw new ConsoleEngineRuntimeException("Unsupported method [setAnalysisState]");		
	}


	public void validate() throws SpagoBIEngineException {
		throw new ConsoleEngineRuntimeException("Unsupported method [validate]");		
	}
	
	
	
	
}
