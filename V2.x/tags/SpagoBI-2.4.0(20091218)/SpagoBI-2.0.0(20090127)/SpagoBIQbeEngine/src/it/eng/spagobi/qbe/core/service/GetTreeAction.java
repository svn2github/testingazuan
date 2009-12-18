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
package it.eng.spagobi.qbe.core.service;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.qbe.commons.exception.QbeEngineException;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;
import it.eng.spagobi.qbe.tree.ExtJsQbeTreeBuilder;
import it.eng.spagobi.qbe.tree.filter.IQbeTreeEntityFilter;
import it.eng.spagobi.qbe.tree.filter.IQbeTreeFieldFilter;
import it.eng.spagobi.qbe.tree.filter.QbeTreeAccessModalityEntityFilter;
import it.eng.spagobi.qbe.tree.filter.QbeTreeAccessModalityFieldFilter;
import it.eng.spagobi.qbe.tree.filter.QbeTreeFilter;
import it.eng.spagobi.qbe.tree.filter.QbeTreeOrderEntityFilter;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.engines.SpagoBIEngineException;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceException;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceExceptionHandler;
import it.eng.spagobi.utilities.service.JSONSuccess;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONArray;

/**
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class GetTreeAction extends AbstractQbeEngineAction {

	// INPUT PARAMETERS
	public static final String DATAMART_NAME = "DATAMART_NAME";
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(GetTreeAction.class);
    
    
	public void service(SourceBean request, SourceBean response) {
		
		String datamartName = null;
		IQbeTreeEntityFilter entityFilter = null;
		IQbeTreeFieldFilter fieldFilter = null;
		QbeTreeFilter treeFilter = null;
		ExtJsQbeTreeBuilder qbeBuilder = null;
		List trees = null;
		JSONArray nodes = null;
		
		logger.debug("IN");
		
		try {
			super.service(request, response);	
			
			
			datamartName = getAttributeAsString(DATAMART_NAME); 		
			logger.debug(DATAMART_NAME + ": " + datamartName);
			
			Assert.assertNotNull(getEngineInstance(), "It's not possible to execute " + this.getActionName() + " service before having properly created an instance of EngineInstance class");
			Assert.assertNotNull(datamartName, "Input parameter [" + DATAMART_NAME + "] cannot be null in oder to execute " + this.getActionName() + " service");		
					
			
			entityFilter = new QbeTreeOrderEntityFilter(new QbeTreeAccessModalityEntityFilter(), getLocale() );
			fieldFilter = new QbeTreeAccessModalityFieldFilter();		   	
		   	treeFilter = new  QbeTreeFilter(entityFilter, fieldFilter);
		   	
			qbeBuilder = new ExtJsQbeTreeBuilder(treeFilter);	   	
		   	trees = qbeBuilder.getQbeTrees(getDatamartModel(), getLocale());			
		   	nodes = (JSONArray)trees.get(0);		
			
			try {
				writeBackToClient( new JSONSuccess(nodes) );
			} catch (IOException e) {
				String message = "Impossible to write back the responce to the client";
				throw new SpagoBIEngineServiceException(getActionName(), message, e);
			}
			
		} catch(Throwable t) {
			throw SpagoBIEngineServiceExceptionHandler.getInstance().getWrappedException(getActionName(), getEngineInstance(), t);
		} finally {
			logger.debug("OUT");
		}
		

	}
}
