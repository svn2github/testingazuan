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
package it.eng.spagobi.kpi.model.utils;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dbaccess.sql.DataConnection;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.util.QueryExecutor;
import it.eng.spagobi.commons.services.DelegatedHibernateConnectionListService;
import it.eng.spagobi.commons.utilities.HibernateUtil;

import java.sql.Connection;
import java.util.ArrayList;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ModelUtils {

	public static SourceBean selectQuery(SourceBean statement, ArrayList inputParameters) throws Exception{
		return genericQuery("SELECT", statement, inputParameters);		
	}
	
	public static void updateQuery(SourceBean statement, ArrayList inputParameters) throws Exception{
		 genericQuery(QueryExecutor.UPDATE, statement, inputParameters);
	}
	
	public static SourceBean insertQuery(SourceBean statement, ArrayList inputParameters) throws Exception{
		return genericQuery("INSERT", statement, inputParameters);
	}
	
	private static SourceBean genericQuery(String type, SourceBean statement, ArrayList inputParameters) throws Exception{
		SourceBean result = null;
		Session aSession = null;
		Transaction tx = null;
		try {			
			aSession = HibernateUtil.currentSession();
			tx = aSession.beginTransaction();
			Connection jdbcConnection = aSession.connection();
			DataConnection dataConnection = DelegatedHibernateConnectionListService.getDataConnection(jdbcConnection);
			if (type.equalsIgnoreCase("SELECT"))
					result = (SourceBean)QueryExecutor.executeQuery(dataConnection, type, statement, inputParameters);
			else//INSET AND UPDATE: ignoro il risultato..
				QueryExecutor.executeQuery(dataConnection, type, statement, inputParameters);
		} catch (HibernateException he) {
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (aSession != null) {
				if (aSession.isOpen()) aSession.close();
			}
		}
		return result;		
	}
}
