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
package it.eng.spagobi.engines.dossier.modules;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.engines.dossier.constants.BookletsConstants;
import it.eng.spagobi.engines.dossier.dao.IDossierPresentationsDAO;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author Andrea Gioia
 *
 */
public class BookletRefreshModule extends AbstractModule {
	
	public static final String MODULE_PAGE = "BookletRefreshPage";	
	static private Logger logger = Logger.getLogger(BookletRefreshModule.class);
	
	public void init(SourceBean config) {}
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		logger.debug("IN");
		try {
			String dossierIdStr = (String) request.getAttribute(BookletsConstants.DOSSIER_ID);
			logger.debug("Using dossier id = " + dossierIdStr);
			Integer dossierId = new Integer(dossierIdStr);
			IDossierPresentationsDAO pdDAO = DAOFactory.getDossierPresentationDAO();
			List presVersions = pdDAO.getPresentationVersions(dossierId);
			response.setAttribute(BookletsConstants.PUBLISHER_NAME, "BookletsPresentationVersion");
			response.setAttribute(BookletsConstants.BOOKLET_PRESENTATION_VERSIONS, presVersions);
			response.setAttribute(BookletsConstants.DOSSIER_ID, dossierIdStr);
		} catch (Exception e) {
			logger.error("Error while setting response attribute " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			logger.debug("OUT");
		}
	}
}
	
	
	