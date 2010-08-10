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
import it.eng.spago.dbaccess.sql.DataRow;
import it.eng.spago.dbaccess.sql.SQLCommand;
import it.eng.spago.dbaccess.sql.result.DataResult;
import it.eng.spago.dbaccess.sql.result.ScrollableDataResult;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.Check;
import it.eng.spagobi.bo.Engine;
import it.eng.spagobi.bo.ParameterUse;
import it.eng.spagobi.bo.dao.ICheckDAO;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;


/**
 *	Defines the JDBC implementations for all DAO methods,
 *  for a value constraint.  
 * 
 * @author zoppello
 */
public class CheckDAOImpl extends AbstractJdbcDAO implements ICheckDAO {
	/**
	 * @see it.eng.spagobi.bo.dao.ICheckDAO#loadAllChecks()
	 */
	public List loadAllChecks() throws EMFUserError{
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		
		List parameters = new ArrayList();
		
		DataConnection dataConnection = null;
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_ALL_CHECKS");
			
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute();
			sdr = (ScrollableDataResult) dr.getDataObject();
			
			DataRow aDataRow = null;
			Check check = null;
			if (sdr.hasRows()){
				sdr.moveTo(1);
				while (sdr.hasRows()){
					aDataRow = sdr.getDataRow();
					check = new Check();
					
					String id = aDataRow.getColumn("ID").getStringValue();
					
					check.setCheckId(Integer.valueOf(id));
					
					String typeId = aDataRow.getColumn("VALUE_TYPE_ID").getStringValue();
					
					check.setValueTypeId(Integer.valueOf(typeId));
					String checkType = aDataRow.getColumn("CHECK_TYPE").getStringValue();
					check.setValueTypeCd(checkType);
					String name = aDataRow.getColumn("NAME").getStringValue();
					check.setName(name);
					String description = aDataRow.getColumn("DESCRIPTION").getStringValue();
					check.setDescription(description);
					String firstValue = aDataRow.getColumn("FIRST_VALUE").getStringValue();
					check.setFirstValue(firstValue);
					String secondValue = aDataRow.getColumn("SECOND_VALUE").getStringValue();
					check.setSecondValue(secondValue);
					
					parameters.add(check);
				}
			}
			return parameters;
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "CheckDAOImpl", "loadAll", "Cannot select all roles", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
	
	}

	/**
	 * @see it.eng.spagobi.bo.dao.ICheckDAO#loadCheckByID(java.lang.Integer)
	 */
	public Check loadCheckByID(Integer id) throws EMFUserError {
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		
		List parameters = new ArrayList();
		
		DataConnection dataConnection = null;
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_CHECKS_BY_ID");
			
			cmdSelect = dataConnection.createSelectCommand(strSql);
			parameters.add(dataConnection.createDataField("", Types.NUMERIC, id));
			dr = cmdSelect.execute(parameters);
			sdr = (ScrollableDataResult) dr.getDataObject();
			
			DataRow aDataRow = null;
			Check check = null;
			if (sdr.hasRows()) {
				sdr.moveTo(1);
				aDataRow = sdr.getDataRow();
				check = new Check();
				String idStr = aDataRow.getColumn("ID").getStringValue();
				check.setCheckId(Integer.valueOf(idStr));
				String typeId = aDataRow.getColumn("VALUE_TYPE_ID")
						.getStringValue();

				check.setValueTypeId(Integer.valueOf(typeId));
				String checkType = aDataRow.getColumn("CHECK_TYPE")
						.getStringValue();
				check.setValueTypeCd(checkType);
				String name = aDataRow.getColumn("NAME").getStringValue();
				check.setName(name);
				String description = aDataRow.getColumn("DESCRIPTION")
						.getStringValue();
				check.setDescription(description);
				String firstValue = aDataRow.getColumn("FIRST_VALUE")
						.getStringValue();
				check.setFirstValue(firstValue);
				String secondValue = aDataRow.getColumn("SECOND_VALUE")
						.getStringValue();
				check.setSecondValue(secondValue);

			}
			return check;
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "CheckDAOImpl", "loadCheckByID", "Cannot load Check", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
	
	}


	/**
	 * @see it.eng.spagobi.bo.dao.ICheckDAO#eraseCheck(it.eng.spagobi.bo.Check)
	 */
	public void eraseCheck(Check check) throws EMFUserError {
		SQLCommand cmdDel = null;
		DataResult dr = null;
		DataConnection dataConnection = null;
		try {
			
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("DELETE_CHECK");
			
			ArrayList parameters = new ArrayList(1);
			parameters.add(dataConnection.createDataField("CHECK_ID", Types.DECIMAL, check.getCheckId()));
			
			cmdDel = dataConnection.createDeleteCommand(strSql);
			dr = cmdDel.execute(parameters);
			
		}  catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAOImpl", "erase", "Cannot erase detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
			
		} finally {
			Utils.releaseResources(dataConnection, cmdDel, dr);
		}
		
	}



	/**
	 * @see it.eng.spagobi.bo.dao.ICheckDAO#insertCheck(it.eng.spagobi.bo.Check)
	 */
	public void insertCheck(Check check) throws EMFUserError {
		SQLCommand cmdIns = null;
		DataResult dr = null;
		DataConnection dataConnection = null;
		
		String strSql = null;
		try {
//			name="INSERT_CHECK" 
//	               query="insert into 
//							sbi_checks (value_type_id,value_type_cd,value_1,value_2,label,descr)
//							values (?,?,?,?,?,?)"/>
			dataConnection = getConnection("spagobi");
			strSql = SQLStatements.getStatement("INSERT_CHECK");
			
			ArrayList parameters = new ArrayList(6);
			parameters.add(dataConnection.createDataField("value_type_id", Types.NUMERIC, check.getValueTypeId()));
			parameters.add(dataConnection.createDataField("value_type_cd", Types.VARCHAR, check.getValueTypeCd()));
			parameters.add(dataConnection.createDataField("value_1", Types.VARCHAR, check.getFirstValue()));
			parameters.add(dataConnection.createDataField("value_2", Types.VARCHAR, check.getSecondValue()));
			parameters.add(dataConnection.createDataField("label", Types.VARCHAR, check.getName()));
			parameters.add(dataConnection.createDataField("descr", Types.VARCHAR, check.getDescription()));
			
			cmdIns = dataConnection.createInsertCommand(strSql);
			dr = cmdIns.execute(parameters);
			
		}  catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "CheckDAOImpl", "insert", "Cannot insert detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdIns, dr);
		}
		
	}


	/**
	 * @see it.eng.spagobi.bo.dao.ICheckDAO#modifyCheck(it.eng.spagobi.bo.Check)
	 */
	public void modifyCheck(Check check) throws EMFUserError {
		SQLCommand cmdMod = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		DataConnection dataConnection = null;
		
		String strSql = null;
		try {
			
			dataConnection = getConnection("spagobi");
			strSql = SQLStatements.getStatement("UPDATE_CHECK");
			
			ArrayList parameters = new ArrayList(7);
			parameters.add(dataConnection.createDataField("value_type_id", Types.NUMERIC, check.getValueTypeId()));
			parameters.add(dataConnection.createDataField("value_type_cd", Types.VARCHAR, check.getValueTypeCd()));
			parameters.add(dataConnection.createDataField("value_1", Types.VARCHAR, check.getFirstValue()));
			parameters.add(dataConnection.createDataField("value_2", Types.VARCHAR, check.getSecondValue()));
			parameters.add(dataConnection.createDataField("label", Types.VARCHAR, check.getName()));
			parameters.add(dataConnection.createDataField("descr", Types.VARCHAR, check.getDescription()));
			parameters.add(dataConnection.createDataField("check_id", Types.NUMERIC, check.getCheckId()));
			
			cmdMod = dataConnection.createUpdateCommand(strSql);
             
			dr = cmdMod.execute(parameters);
		}  catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "CheckDAOImpl", "modify", "Cannot update detail information", ex);
			
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdMod, dr);
		}
	}

	/**
	 * @see it.eng.spagobi.bo.dao.ICheckDAO#isReferenced(java.lang.String)
	 */
	public boolean isReferenced (String checkId) throws EMFUserError{
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		ArrayList elements = new ArrayList(2);
		//nome cambiato per evitare confusione
		ParameterUse param = new ParameterUse();
		boolean crypt= false;
		DataConnection dataConnection = null;
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_CHECK_REFERENCES");
			
			elements.add(dataConnection.createDataField("check_id", Types.DECIMAL, checkId));
		
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute(elements);
			sdr = (ScrollableDataResult) dr.getDataObject();
			boolean hasRef = false;
			if(sdr.hasRows()){
				hasRef = true;
			}
		  return hasRef;
		}
		    catch (Exception ex) {
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAOImpl", "load", "Cannot recover detail information", ex);
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
			} finally {
				Utils.releaseResources(dataConnection, cmdSelect, dr);
			}
		
	
	
		
	}
	

	

	

}
