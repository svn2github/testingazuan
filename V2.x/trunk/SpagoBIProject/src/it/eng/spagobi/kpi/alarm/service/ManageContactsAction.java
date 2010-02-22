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
package it.eng.spagobi.kpi.alarm.service;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.x.AbstractSpagoBIAction;
import it.eng.spagobi.commons.bo.Domain;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.dao.IRoleDAO;
import it.eng.spagobi.commons.utilities.messages.MessageBuilder;
import it.eng.spagobi.kpi.alarm.dao.ISbiAlarmContactDAO;
import it.eng.spagobi.kpi.alarm.metadata.SbiAlarmContact;
import it.eng.spagobi.profiling.services.ManageRolesAction;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;

public class ManageContactsAction extends AbstractSpagoBIAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8394056124945156086L;

	// logger component
	private static Logger logger = Logger.getLogger(ManageContactsAction.class);
	
	private final String MESSAGE_DET = "MESSAGE_DET";
	// type of service
	private final String CONTACT_DETAIL = "CONTACT_DETAIL";
	private final String CONTACTS_LIST = "CONTACTS_LIST";
	@Override
	public void doService() {
		logger.debug("IN");
		ISbiAlarmContactDAO contactDao;
		try {
			contactDao = DAOFactory.getAlarmContactDAO();
		} catch (EMFUserError e1) {
			logger.error(e1.getMessage(), e1);
			throw new SpagoBIServiceException(SERVICE_NAME,	"Error occurred");
		}
		HttpServletRequest httpRequest = getHttpRequest();
		MessageBuilder m = new MessageBuilder();
		Locale locale = m.getLocale(httpRequest);

		String serviceType = this.getAttributeAsString(MESSAGE_DET);
		logger.debug("Service type "+serviceType);
		if(serviceType != null && serviceType.equals(CONTACTS_LIST)){
			List<SbiAlarmContact> contacts = contactDao.findAll();
		}else if(serviceType != null && serviceType.equals(CONTACT_DETAIL)){
			
		}
		logger.debug("OUT");
		
	}

}
