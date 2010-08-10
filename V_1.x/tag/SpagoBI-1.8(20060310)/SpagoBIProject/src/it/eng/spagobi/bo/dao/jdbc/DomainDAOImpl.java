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

import it.eng.spago.dbaccess.SQLStatements;
import it.eng.spago.dbaccess.Utils;
import it.eng.spago.dbaccess.sql.DataConnection;
import it.eng.spago.dbaccess.sql.DataField;
import it.eng.spago.dbaccess.sql.DataRow;
import it.eng.spago.dbaccess.sql.SQLCommand;
import it.eng.spago.dbaccess.sql.result.DataResult;
import it.eng.spago.dbaccess.sql.result.ScrollableDataResult;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.Domain;
import it.eng.spagobi.bo.ModalitiesValue;
import it.eng.spagobi.bo.dao.IDomainDAO;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



/**
 *	Defines the JDBC implementations for all DAO methods,
 *  for a domain.  
 * 
 * @author zoppello
 */
public class DomainDAOImpl extends AbstractJdbcDAO implements IDomainDAO {

	
	/**
	 * @see it.eng.spagobi.bo.dao.IDomainDAO#loadListDomainsByType(java.lang.String)
	 */
	public List loadListDomainsByType(String domainType) throws EMFUserError {
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		ArrayList parameters = new ArrayList(1);
		DataConnection dataConnection = null;
		ModalitiesValue modVal = null;
		ArrayList parameters1 = new ArrayList(1);
		
		boolean crypt= false;
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_LIST_DOMAINS");
			DataField dataField = dataConnection.createDataField("DOMAIN_CD", Types.VARCHAR, domainType);
			parameters1.add(dataField);
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute(parameters1);
			sdr = (ScrollableDataResult) dr.getDataObject();
			ResultSet rs = sdr.getResultSet();
			int numRows = sdr.getRowsNumber();
			DataRow aDataRow = null;
			HashMap hash = new HashMap();
			ArrayList list = new ArrayList();
			if(sdr.hasRows()){
				sdr.moveTo(1);
				while(sdr.hasRows()){
					Domain domain = new Domain();
					aDataRow = sdr.getDataRow();
				
					domain.setValueCd(aDataRow.getColumn("VALUE_CD").getStringValue());
					domain.setValueName(aDataRow.getColumn("VALUE_NAME").getStringValue());
					
					String valueId = aDataRow.getColumn("VALUE_ID").getStringValue();
					
					domain.setValueId(Integer.valueOf(valueId));
					
					list.add(domain);
				
					}
		   return list;
			}else{
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "DomainDAOImpl", "load", "Cannot recover detail information");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);  }

		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "DomainDAOImpl", "load", "Cannot recover detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 1021);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}

	}



	/**
	 * @see it.eng.spagobi.bo.dao.IDomainDAO#loadDomainByCodeAndValue(java.lang.String, java.lang.String)
	 */
	public Domain loadDomainByCodeAndValue(String codeDomain, String codeValue) throws EMFUserError {
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		ArrayList parameters = new ArrayList(1);
		DataConnection dataConnection = null;
		ModalitiesValue modVal = null;
		ArrayList parameters1 = new ArrayList(1);
		
		boolean crypt= false;
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_DOMAIN_FROM_CODE_VALUE");
			DataField dataField = dataConnection.createDataField("DOMAIN_CODE", Types.VARCHAR, codeDomain);
			parameters1.add(dataField);
			dataField = dataConnection.createDataField("VALUE_CODE", Types.VARCHAR, codeValue);
			parameters1.add(dataField);
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute(parameters1);
			sdr = (ScrollableDataResult) dr.getDataObject();
			ResultSet rs = sdr.getResultSet();
			DataRow aDataRow = null; 
			Domain domain = new Domain();
			if (sdr.hasRows()){
				sdr.moveTo(1);
					while(sdr.hasRows()){
			       
			        aDataRow = sdr.getDataRow();
			        domain.setValueCd(aDataRow.getColumn("VALUE_CD").getStringValue());
					domain.setValueName(aDataRow.getColumn("VALUE_NAME").getStringValue());
					
					String valueId = aDataRow.getColumn("VALUE_ID").getStringValue();
					
					domain.setValueId(Integer.valueOf(valueId));
					
				
					}
		return domain;
			}else{
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "DomainDAOImpl", "load", "Cannot recover detail information");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);  }	
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "DomainDAOImpl", "load", "Cannot recover detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
	}
	

	
	
	
	
	
	



	
	





}