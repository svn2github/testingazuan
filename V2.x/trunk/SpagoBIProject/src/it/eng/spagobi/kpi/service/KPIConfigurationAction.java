/*
 *   Copyright 2008 Engineering Ingegneria Informatica S.p.A.

    This file is part of Spago4Q.

    Spago4Q is free software; you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published 
    by the Free Software Foundation; either version 3 of the License, or
    any later version.

    Spago4Q is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package it.eng.spagobi.kpi.service;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;

import org.apache.log4j.Logger;

public class KPIConfigurationAction extends AbstractAction {

	private static Logger logger = Logger.getLogger(KPIConfigurationAction.class);
	
	public void service(SourceBean serviceRequest, SourceBean serviceResponse) throws Exception {
		logger.info("service called");
	}

}
