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
package it.eng.qbe.action;

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.utility.SpagoBIInfo;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.utilities.GenericSavingException;
import it.eng.spagobi.utilities.SpagoBIAccessUtils;


/**
 * @author Andrea Zoppello
 * 
 * This action is responsible to Persist the current working query represented by
 * the object ISingleDataMartWizardObject in session
 */
public class PersistQueryAction extends AbstractAction {
	
	/** 
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) {
		
	
		DataMartModel dmModel = (DataMartModel)getRequestContainer().getSessionContainer().getAttribute("dataMartModel");
	
		String queryId = (String)request.getAttribute("queryId");
		
		String  queryDescritpion  = (String)request.getAttribute("queryDescritpion");
		
		String  visibility  = (String)request.getAttribute("visibility");
		
		ISingleDataMartWizardObject obj = Utils.getWizardObject(getRequestContainer().getSessionContainer());
		if ((queryId != null) && (queryId.trim().length() > 0)){
			obj.setQueryId(queryId);
		}
		
		if ((queryDescritpion != null) && (queryDescritpion.trim().length() > 0)){
			obj.setDescription(queryDescritpion);
		}
		
		if ((visibility != null) && (visibility.trim().length() > 0)){
			if (visibility.equalsIgnoreCase("public")){
				obj.setVisibility(true);
			}else {
				obj.setVisibility(false);
			}
		}
		
		String qbeMode = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-MODE.mode");
		if (qbeMode.equalsIgnoreCase("WEB")){
			IEngUserProfile userProfile =(IEngUserProfile)getRequestContainer().getSessionContainer().getPermanentContainer().getAttribute(IEngUserProfile.ENG_USER_PROFILE);
			if (userProfile != null){
				obj.setOwner(userProfile.getUserUniqueIdentifier().toString());
			}
			
			SessionContainer session = getRequestContainer().getSessionContainer();
			SpagoBIInfo spagobiInfo = (SpagoBIInfo)session.getAttribute("spagobi");
			if (spagobiInfo != null){
				obj.setOwner(spagobiInfo.getUser());
			}
		}
		dmModel.persistQueryAction(obj);
		SessionContainer session = getRequestContainer().getSessionContainer();
		SpagoBIInfo spagobiInfo = (SpagoBIInfo)session.getAttribute("spagobi");
		if(spagobiInfo != null) {
			SpagoBIAccessUtils spagoBIProxy = new SpagoBIAccessUtils();
			try {
				spagoBIProxy.saveSubObject(spagobiInfo.getSpagobiurl(), 
										   spagobiInfo.getTemplatePath(), 
										   queryId, queryDescritpion, spagobiInfo.getUser(), obj.getVisibility(), queryId);
			} catch (GenericSavingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		String cTM = String.valueOf(System.currentTimeMillis());
		if (!Utils.isSubQueryModeActive(session)){
			session.setAttribute("QBE_START_MODIFY_QUERY_TIMESTAMP", cTM);
			session.setAttribute("QBE_LAST_UPDATE_TIMESTAMP", cTM);
		}
		
	}
}
