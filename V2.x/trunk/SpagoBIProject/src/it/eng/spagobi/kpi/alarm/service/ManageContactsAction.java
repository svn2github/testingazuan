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

import org.apache.log4j.Logger;

import it.eng.spagobi.analiticalmodel.document.x.AbstractSpagoBIAction;
import it.eng.spagobi.profiling.services.ManageRolesAction;

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
	@Override
	public void doService() {
		// TODO Auto-generated method stub
		
	}

}
