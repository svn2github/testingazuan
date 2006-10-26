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
package it.eng.spagobi.exoaddins.modules;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dbaccess.sql.DataRow;
import it.eng.spago.dispatching.module.list.basic.AbstractBasicListModule;
import it.eng.spago.dispatching.service.RequestContextIFace;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.init.InitializerIFace;
import it.eng.spago.paginator.basic.ListIFace;
import it.eng.spago.paginator.basic.PaginatorIFace;
import it.eng.spago.paginator.basic.impl.GenericList;
import it.eng.spago.paginator.basic.impl.GenericPaginator;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.exoaddins.Utilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.Iterator;
import java.util.List;

import org.exoplatform.commons.utils.PageList;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.organization.GroupHandler;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserHandler;

public class ExoProfileAttributeManagerListUserModule extends AbstractBasicListModule {
	

	public ListIFace getList(SourceBean request, SourceBean response) throws Exception {
		PaginatorIFace paginator = new GenericPaginator();
		InitializerIFace serviceInitializer = (InitializerIFace) this;
		int pagedRows = 10;
		paginator.setPageSize(pagedRows);
		RequestContextIFace serviceRequestContext = (RequestContextIFace) this;		
		SourceBean rowsSourceBean = getExoUsers();
		if ((rowsSourceBean == null)) {
			EMFErrorHandler engErrorHandler = serviceRequestContext.getErrorHandler();
			engErrorHandler.addError(new EMFUserError(EMFErrorSeverity.INFORMATION, 100));
		} else { 
			List rowsVector = null;
			rowsVector = rowsSourceBean.getAttributeAsList(DataRow.ROW_TAG);
			for (int i = 0; i < rowsVector.size(); i++) {
				paginator.addRow(rowsVector.get(i));
			}
		}
		ListIFace list = new GenericList();
		list.setPaginator(paginator);
		return list;
	}

	
	
	
	private SourceBean getExoUsers() {
		SourceBean rows = null;
		try{
			rows = new SourceBean("ROWS");
			PortalContainer container = PortalContainer.getInstance();	
			if(container==null) throw new Exception("Portal container not retrived");
			OrganizationService service = (OrganizationService) container.getComponentInstanceOfType(OrganizationService.class);
			if(service==null) throw new Exception("Organization service not retrived");
			UserHandler userHandler = service.getUserHandler();
			if(userHandler==null) throw new Exception("User handler component not retrived");
			GroupHandler groupHandler = service.getGroupHandler();
			if(groupHandler==null) throw new Exception("Group handler component not retrived");
			PageList pagelist = userHandler.getUserPageList(10);
			List allUser = pagelist.getAll();
			allUser = Utilities.getExoUserFiltered(allUser, groupHandler);
			Iterator iterUser = allUser.iterator();
			while(iterUser.hasNext()) {
				User user = (User)iterUser.next();
				String userName = user.getUserName();
				SourceBean row = new SourceBean(DataRow.ROW_TAG);	
				row.setAttribute("UserName", userName);
				rows.setAttribute(row);
			}
		} catch (Exception e){
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
					            "getExoUsers", "Error while loading exo user list ", e);
			rows = null;
		}
		return rows;	
	}
} 

