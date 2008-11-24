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
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.kpi.model.bo.Model;


public class ListModelTreeModule extends AbstractModule {
	
	static private Logger logger = Logger.getLogger(ListModelTreeModule.class);
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		String parentId = (String)request.getAttribute("ID");
		List result = new ArrayList();
		Model aModel = DAOFactory.getModelDAO().loadModelWithChildrenById(Integer.parseInt(parentId));
		result.add(aModel);
		response.setAttribute(SpagoBIConstants.FUNCTIONALITIES_LIST, result);
	}

}