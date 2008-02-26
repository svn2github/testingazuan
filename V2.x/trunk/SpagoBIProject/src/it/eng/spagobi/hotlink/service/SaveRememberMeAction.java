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
package it.eng.spagobi.hotlink.service;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.messages.IMessageBuilder;
import it.eng.spagobi.commons.utilities.messages.MessageBuilderFactory;
import it.eng.spagobi.engines.dossier.actions.DossierDownloadAction;

import java.util.Locale;

import org.apache.log4j.Logger;

/**
 * 
 * @author Zerbetto (davide.zerbetto@eng.it)
 *
 */
public class SaveRememberMeAction extends AbstractHttpAction {

	static private Logger logger = Logger.getLogger(DossierDownloadAction.class);
	
	public void service(SourceBean serviceRequest, SourceBean serviceResponse)
			throws Exception {
		logger.debug("IN");
		try {
			String docIdStr = (String) serviceRequest.getAttribute(SpagoBIConstants.OBJECT_ID);
			Integer docId = new Integer(docIdStr);
			String userId = (String) serviceRequest.getAttribute("userId");
			String parameters = (String) serviceRequest.getAttribute("parameters");
			boolean inserted = DAOFactory.getRememberMeDAO().saveRememberMe(docId, userId, parameters);
			if (inserted) {
				serviceResponse.setAttribute("NORMAL_MSG_CODE", "sbi.rememberme.saveOk");
			} else {
				serviceResponse.setAttribute("NORMAL_MSG_CODE", "sbi.rememberme.alreadyExisting");
			}
		} catch (Exception e) {
			logger.debug("Error while saving remember me: " + e);
			serviceResponse.setAttribute("ERROR_MSG_CODE", "sbi.rememberme.saveKO");
		} finally {
			logger.debug("OUT");
		}
	}

	private String createNormalHtml(String msgCode, Locale locale) {
		String msg = getInternazionalizedMessage(msgCode, locale);
		String html = 
				"<table cellspacing='0' border='0'>\n";
		html += "	<tr>";
		html += "		<td class='header-title-column-single-object-execution-portlet-section' style='vertical-align:middle;'>";
		html += "				&nbsp;&nbsp;&nbsp;" + msg;
		html += "		</td>";
		html += "	</tr>";
		html += "</table>";
		return html;
	}
	
	private String createErrorHtml(String msgCode, Locale locale) {
		return getInternazionalizedMessage(msgCode, locale);
	}
	
	private String getInternazionalizedMessage(String msgCode, Locale locale) {
		IMessageBuilder msgBuilder = MessageBuilderFactory.getMessageBuilder();
		String msg = null;
		if (locale != null) {
			msg = msgBuilder.getMessage(msgCode, locale);
		} else {
			msg = msgBuilder.getMessage(msgCode);
		}
		return msg;
	}
}
