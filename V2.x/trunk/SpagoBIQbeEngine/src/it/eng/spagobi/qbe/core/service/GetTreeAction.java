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

import it.eng.qbe.model.structure.builder.BasicDataMartStructureBuilder;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;
import it.eng.spagobi.qbe.tree.ExtJsQbeTreeBuilder;
import it.eng.spagobi.qbe.tree.filter.IQbeTreeEntityFilter;
import it.eng.spagobi.qbe.tree.filter.IQbeTreeFieldFilter;
import it.eng.spagobi.qbe.tree.filter.QbeTreeAccessModalityEntityFilter;
import it.eng.spagobi.qbe.tree.filter.QbeTreeAccessModalityFieldFilter;
import it.eng.spagobi.qbe.tree.filter.QbeTreeFilter;
import it.eng.spagobi.qbe.tree.filter.QbeTreeOrderEntityFilter;
import it.eng.spagobi.utilities.engines.EngineException;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

/**
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class GetTreeAction extends AbstractQbeEngineAction {

	// valid input parameter names
	/** The Constant DATAMART_NAME. */
	public static final String DATAMART_NAME = "DATAMART_NAME";
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.utilities.engines.AbstractEngineAction#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) throws EngineException {
		super.service(request, response);	
		
		
		String datamartName = getAttributeAsString(DATAMART_NAME); 		
		JSONArray nodes = null;
		
		IQbeTreeEntityFilter entityFilter = new QbeTreeOrderEntityFilter(
					new QbeTreeAccessModalityEntityFilter(), getLocale() );
		IQbeTreeFieldFilter fieldFilter = new QbeTreeAccessModalityFieldFilter();		   	
	   	
		QbeTreeFilter treeFilter = new  QbeTreeFilter(entityFilter, fieldFilter);
	   	
		ExtJsQbeTreeBuilder qbeBuilder = new ExtJsQbeTreeBuilder(treeFilter);	   	
	   	List trees = qbeBuilder.getQbeTrees(getDatamartModel(), getLocale());
		
	   	nodes = (JSONArray)trees.get(0);		
		String treeData = nodes.toString();
		
		
		
		freezeHttpResponse();
		HttpServletResponse httpResp = getHttpResponse();
		
		try {
			httpResp.getOutputStream().print(treeData);
			httpResp.getOutputStream().flush();
		} catch (IOException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
}
