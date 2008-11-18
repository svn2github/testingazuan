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
package it.eng.spagobi.kpi.model.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.init.InitializerIFace;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.kpi.model.utils.ModelUtils;


public class ListModelTreeModule extends AbstractModule {
	
	static private Logger logger = Logger.getLogger(ListModelTreeModule.class);
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		String parentId = (String)request.getAttribute("ID");
		List result = new ArrayList();
		SourceBean root = getRoot(parentId);
		result.add(root);
		RecursiveNavigationStep(parentId, result);
		response.setAttribute(SpagoBIConstants.FUNCTIONALITIES_LIST, result);
	}
	
	
	private void recursiveNavigation(List childId,List result ) throws Exception{
		for(int i = 0; i<childId.size(); i++){
			RecursiveNavigationStep(childId.get(i).toString(), result);
		}
	}
	
	private void RecursiveNavigationStep(String parentId, List result)throws Exception{
		SourceBean item = getChildren(parentId);
		result.add(item);
		List myChildId = item.getAttributeAsList("ROW.ID");
		recursiveNavigation(myChildId, result);
	}

	private SourceBean getRoot(String parentId) throws Exception {
		SourceBean result = null;
		InitializerIFace serviceInitializer = (InitializerIFace) this;
		SourceBean statement =
            (SourceBean) serviceInitializer.getConfig().getAttribute(
                "QUERIES.SELECT_ROOT");
		ArrayList inputParameters = new ArrayList();
		inputParameters.add(parentId);
		return ModelUtils.selectQuery(statement,inputParameters);
	}
	
	private SourceBean getChildren(String parentId) throws Exception{ 
		
		InitializerIFace serviceInitializer = (InitializerIFace) this;
		SourceBean statement =
            (SourceBean) serviceInitializer.getConfig().getAttribute(
                "QUERIES.SELECT_QUERY");
		ArrayList inputParameters = new ArrayList();
		inputParameters.add(parentId);
		return ModelUtils.selectQuery(statement, inputParameters);
	}

}