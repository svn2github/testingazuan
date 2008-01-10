/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.analiticalmodel.document.service;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.dao.IBIObjectDAO;
import it.eng.spagobi.commons.constants.ObjectsTreeConstants;
import it.eng.spagobi.commons.dao.DAOFactory;


public class EraseDocumentModule extends AbstractModule {
	private static transient Logger logger = Logger.getLogger(EraseDocumentModule.class);

	public void init(SourceBean config) {
	}

	public void service(SourceBean request, SourceBean response) throws Exception {
		try {
			String objIdStr = (String)request.getAttribute(ObjectsTreeConstants.OBJECT_ID);
			Integer objId = new Integer(objIdStr);
			IBIObjectDAO biobjdao = DAOFactory.getBIObjectDAO();
			BIObject obj = biobjdao.loadBIObjectById(objId);
			List functs = obj.getFunctionalities();
			Iterator iterFuncts = functs.iterator();
			while(iterFuncts.hasNext()) {
				Integer functId = (Integer)iterFuncts.next();
				biobjdao.eraseBIObject(obj, functId);
			}
		} catch (Exception e ) {
			logger.error("Error while deleting biobject " + e);
		}
		
		
		
	}

}