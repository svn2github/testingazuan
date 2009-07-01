/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2009 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.analiticalmodel.document.x;

import it.eng.spago.base.Constants;

import org.apache.log4j.Logger;

/**
 * 
 * @author Zerbetto Davide
 *
 */
public class ExecuteDocumentAction extends AbstractSpagoBIAction {
	
	public static final String SERVICE_NAME = "EXECUTE_DOCUMENT_ACTION";
	
	// logger component
	private static Logger logger = Logger.getLogger(ExecuteDocumentAction.class);
	
	public void doService() {
		logger.debug("IN");
		// setting locale
		// TODO move this language operations to a common class
		String language = this.getAttributeAsString("SBI_LANGUAGE");
		String country = this.getAttributeAsString("SBI_COUNTRY");
		if (language != null && !language.trim().equals("")) {
			this.getSessionContainer().getPermanentContainer().setAttribute(Constants.USER_LANGUAGE, language);
			if (country != null && !country.trim().equals("")) {
				this.getSessionContainer().getPermanentContainer().setAttribute(Constants.USER_COUNTRY, country);
			}
		}
		logger.debug("OUT");
	}

}
