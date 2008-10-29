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
package it.eng.spagobi.hotlink.service;

import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.services.BaseProfileAction;
import it.eng.spagobi.engines.dossier.actions.DossierDownloadAction;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * 
 * @author Zerbetto (davide.zerbetto@eng.it)
 *
 */
public class SaveRememberMeAction extends BaseProfileAction {

	private static final long serialVersionUID = 1L;
	static private Logger logger = Logger.getLogger(DossierDownloadAction.class);
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.commons.services.BaseProfileAction#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean serviceRequest, SourceBean serviceResponse)
			throws Exception {
		
		//Check of the userId in order to keep performing the request
		super.service(serviceRequest, serviceResponse);
		
		logger.debug("IN");
		String message = null;
		freezeHttpResponse();
		HttpServletResponse httResponse = getHttpResponse();
		try {
			String name = (String) serviceRequest.getAttribute("name");
			String description = (String) serviceRequest.getAttribute("description");
			String docIdStr = (String) serviceRequest.getAttribute(SpagoBIConstants.OBJECT_ID);
			Integer docId = new Integer(docIdStr);
			SessionContainer permSession = this.getRequestContainer().getSessionContainer().getPermanentContainer();
			IEngUserProfile profile = (IEngUserProfile) permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
			String userId = ((UserProfile)profile).getUserId().toString();  
			String subobjectIdStr = (String) serviceRequest.getAttribute("subobject_id");
			Integer subobjectId = null;
			if (subobjectIdStr != null && !subobjectIdStr.trim().equals("")) {
				subobjectId = new Integer(subobjectIdStr);
			}
			String parameters = (String) serviceRequest.getAttribute("parameters");
			boolean inserted = DAOFactory.getRememberMeDAO().saveRememberMe(name, description, docId, subobjectId, userId, parameters);
			if (inserted) {
				message = "sbi.rememberme.saveOk";
			} else {
				message = "sbi.rememberme.alreadyExisting";
			}
		} catch (Exception e) {
			logger.debug("Error while saving remember me: " + e);
			message = "sbi.rememberme.errorWhileSaving";
		} finally {
			httResponse.getOutputStream().write(message.getBytes());
			httResponse.getOutputStream().flush();
			logger.debug("OUT");
		}
	}
	
}
