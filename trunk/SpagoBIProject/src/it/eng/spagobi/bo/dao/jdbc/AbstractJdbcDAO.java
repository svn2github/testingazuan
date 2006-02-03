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
package it.eng.spagobi.bo.dao.jdbc;

import it.eng.spago.dbaccess.DataConnectionManager;
import it.eng.spago.dbaccess.sql.DataConnection;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.utilities.SpagoBITracer;



/**
 * Abstract class that all DAO will have to extend
 * 
 * @author zoppello
 */
public class AbstractJdbcDAO {
	
	/**
	 * Gets the JDBC Connection.
	 * 
	 * @param connectLabel The connection String
	 * @return The <code>DataConnection</code> connection object
	 * @throws EMFUserError If any Exception occurs
	 */
	public static DataConnection getConnection(String connectLabel) throws EMFUserError{
		DataConnection dataConnection = null;
		try {
			dataConnection = DataConnectionManager.getInstance().getConnection(connectLabel);
		}catch(EMFInternalError uex){
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "AbstractJdbcDAO", "getConnection", "Cannot recover connection", uex);
			throw new EMFUserError(EMFErrorSeverity.ERROR,100);
		}
		return dataConnection;
	}

	

}
