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
import it.eng.spagobi.bo.ModalitiesValue;
import it.eng.spagobi.bo.ParameterUse;
import it.eng.spagobi.bo.dao.IModalitiesValueDAO;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;


/**
 *	Defines the JDBC implementations for all DAO methods,
 *  for a list of values.  
 * 
 * @author zoppello
 */
public class ModalitiesValueDAOImpl extends AbstractJdbcDAO implements IModalitiesValueDAO {

	/**
	 * @see it.eng.spagobi.bo.dao.IModalitiesValueDAO#loadModalitiesValueByID(java.lang.Integer)
	 */
	
	
	public List loadAllModalitiesValue() throws EMFUserError {
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		
		List allModValues  = new ArrayList();
		
		DataConnection dataConnection = null;
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_ALL_MODALITIES_VALUE");
			
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute();
			sdr = (ScrollableDataResult) dr.getDataObject();
			
			DataRow aDataRow = null;
			ModalitiesValue modVal = null;
			if (sdr.hasRows()){
				sdr.moveTo(1);
				while (sdr.hasRows()){
					aDataRow = sdr.getDataRow();
					modVal = new ModalitiesValue();
					
					String id = aDataRow.getColumn("ID").getStringValue();
					
					modVal.setId(Integer.valueOf(id));
					
					modVal.setDescription(aDataRow.getColumn("DESCRIPTION").getStringValue());
					modVal.setName(aDataRow.getColumn("NAME").getStringValue());
					modVal.setLovProvider(aDataRow.getColumn("LOV_PROVIDER").getStringValue());
					modVal.setITypeCd(aDataRow.getColumn("INPUT_TYPE_CD").getStringValue());
					modVal.setITypeId(aDataRow.getColumn("INPUT_TYPE_ID").getStringValue());
					
					
					allModValues.add(modVal);
				}
			}
			return allModValues;
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "ParameterDAOImpl", "loadAll", "Cannot select all roles", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
	
	}
	/**
	 * @see it.eng.spagobi.bo.dao.IModalitiesValueDAO#loadAllModalitiesValue()
	 */
	public ModalitiesValue loadModalitiesValueByID(Integer modalitiesValueID) throws EMFUserError {
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		ArrayList parameters = new ArrayList(1);
		ModalitiesValue modValue = new ModalitiesValue();
		boolean crypt= false;
		DataConnection dataConnection = null;
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_MODALITIES_VALUE");
			
			DataField dataField = dataConnection.createDataField("id", Types.DECIMAL, modalitiesValueID);
			parameters.add(dataField);
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute(parameters);
			sdr = (ScrollableDataResult) dr.getDataObject();
			ResultSet rs = sdr.getResultSet();
			DataRow aDataRow = null;
			if(sdr.hasRows()){
			sdr.moveTo(1);
			while (sdr.hasRows()){
				aDataRow = sdr.getDataRow();
				String id = aDataRow.getColumn("ID").getStringValue();
				
				modValue.setId(Integer.valueOf(id));
				
				modValue.setName(aDataRow.getColumn("NAME").getStringValue());
				modValue.setDescription(aDataRow.getColumn("DESCRIPTION").getStringValue());
				modValue.setLovProvider(aDataRow.getColumn("LOV_PROVIDER").getStringValue());
				modValue.setITypeCd(aDataRow.getColumn("INPUT_TYPE_CD").getStringValue());
				modValue.setITypeId(aDataRow.getColumn("INPUT_TYPE_ID").getStringValue());
			}
			return modValue;
			}
			else { SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "ModalitiesValueDAOImpl", "load", "Cannot recover detail information");
				   throw new EMFUserError(EMFErrorSeverity.ERROR, 100);}
			

		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "ModalitiesValueDAOImpl", "load", "Cannot recover detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 1019);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
	
	}


	/**
	 * @see it.eng.spagobi.bo.dao.IModalitiesValueDAO#modifyModalitiesValue(it.eng.spagobi.bo.ModalitiesValue)
	 */
	public void modifyModalitiesValue(ModalitiesValue aModalitiesValue) throws EMFUserError {
		SQLCommand cmdSelect = null;
		ScrollableDataResult sdr = null;
		SQLCommand cmdMod = null;
		DataResult dr = null;
		DataConnection dataConnection = null;
		
		ArrayList parameters1 = new ArrayList(1);
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("UPDATE_MODALITIES_VALUE");
			ArrayList parameters = new ArrayList(6);
			parameters.add(dataConnection.createDataField("LABEL", Types.VARCHAR, aModalitiesValue.getName()));
			parameters.add(dataConnection.createDataField("DESCR", Types.VARCHAR, aModalitiesValue.getDescription()));
			parameters.add(dataConnection.createDataField("LOV_PROVIDER", Types.VARCHAR, aModalitiesValue.getLovProvider()));
			parameters.add(dataConnection.createDataField("INPUT_TYPE_CD", Types.VARCHAR, aModalitiesValue.getITypeCd()));
			parameters.add(dataConnection.createDataField("INPUT_TYPE_ID", Types.VARCHAR, aModalitiesValue.getITypeId()));
			parameters.add(dataConnection.createDataField("LOV_ID", Types.DECIMAL, aModalitiesValue.getId()));
			cmdMod = dataConnection.createUpdateCommand(strSql);
			dr = cmdMod.execute(parameters);
			
		}  catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "ModalitiesValueDAOImpl", "modify", "Cannot update detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 1018);
		} finally {
			Utils.releaseResources(dataConnection, cmdMod, dr);
		}
		
	}


	/**
	 * @see it.eng.spagobi.bo.dao.IModalitiesValueDAO#insertModalitiesValue(it.eng.spagobi.bo.ModalitiesValue)
	 */
	public void insertModalitiesValue(ModalitiesValue aModalitiesValue) throws EMFUserError {
		SQLCommand cmdIns = null;
		DataResult dr = null;
		DataConnection dataConnection = null;
		
		SQLCommand cmdSelect = null;
		ScrollableDataResult sdr = null;
		ArrayList parameters1 = new ArrayList(1);
		try {

			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("INSERT_MODALITIES_VALUE");
			ArrayList parameters = new ArrayList(5);
			parameters.add(dataConnection.createDataField("NAME", Types.VARCHAR, aModalitiesValue.getName()));
			parameters.add(dataConnection.createDataField("DESCRIPTION", Types.VARCHAR, aModalitiesValue.getDescription()));
			parameters.add(dataConnection.createDataField("LOV_PROVIDER", Types.VARCHAR, aModalitiesValue.getLovProvider()));
			parameters.add(dataConnection.createDataField("INPUT_TYPE_CD", Types.VARCHAR, aModalitiesValue.getITypeCd()));		
			parameters.add(dataConnection.createDataField("INPUT_TYPE_ID", Types.VARCHAR, aModalitiesValue.getITypeId()));		
			cmdIns = dataConnection.createInsertCommand(strSql);
			dr = cmdIns.execute(parameters);
			
		}  catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "ModalitiesValueDAOImpl", "insert", "Cannot insert detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 1018);
		} finally {
			Utils.releaseResources(dataConnection, cmdIns, dr);
		}
		
	}


	/**
	 * @see it.eng.spagobi.bo.dao.IModalitiesValueDAO#eraseModalitiesValue(it.eng.spagobi.bo.ModalitiesValue)
	 */
	public void eraseModalitiesValue(ModalitiesValue aModalitiesValue) throws EMFUserError {
		SQLCommand cmdDel = null;
		DataResult dr = null;
		DataConnection dataConnection = null;
		
		try {
			
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("DELETE_MODALITIES_VALUE");
			
			ArrayList parameters = new ArrayList(1);
			parameters.add(dataConnection.createDataField("LOV_ID", Types.DECIMAL, aModalitiesValue.getId()));
			cmdDel = dataConnection.createDeleteCommand(strSql);
			dr = cmdDel.execute(parameters);
			
		}  catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "ModalitiesValueDAOImpl", "erase", "Cannot insert detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 1020);
		} finally {
			Utils.releaseResources(dataConnection, cmdDel, dr);
		}
		
	}
	
	

	public void erase(Object obj) throws EMFUserError {
		
	}
	/**
	 * Gives a list of values id known its name.
	 * 
	 * @param name The String representing the lov name
	 * @return A string representing the lov ID
	 * @throws EMFUserError If any Exception occurred
	 */
	public String loadLovId(String name) throws EMFUserError{
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		String id = null;
		ArrayList parameters = new ArrayList(1);
		ModalitiesValue modValue = new ModalitiesValue();
		boolean crypt= false;
		DataConnection dataConnection = null;
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_LOV_ID_BY_NAME");
			DataField dataField = dataConnection.createDataField("LABEL", Types.VARCHAR, name);
			parameters.add(dataField);
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute(parameters);
			sdr = (ScrollableDataResult) dr.getDataObject();
			ResultSet rs = sdr.getResultSet();
			DataRow aDataRow = null;
			if(sdr.hasRows()){
			sdr.moveTo(1);
			while (sdr.hasRows()){
				aDataRow = sdr.getDataRow();
			    id = aDataRow.getColumn("ID").getStringValue();
				
			}
			
			}
			
			else { SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "ModalitiesValueDAO", "load", "Cannot recover detail information");
				   throw new EMFUserError(EMFErrorSeverity.ERROR, 100);}
			return id;

		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "ModalitiesValueDAO", "load", "Cannot recover detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
		
		
		
		
		
	}
	

	/**
	 * @see it.eng.spagobi.bo.dao.IModalitiesValueDAO#loadAllModalitiesValueOrderByCode()
	 */
	public List loadAllModalitiesValueOrderByCode() throws EMFUserError {
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		ArrayList parameters = new ArrayList(1);
		DataConnection dataConnection = null;
		ModalitiesValue modVal = null;
		ArrayList parameters1 = new ArrayList(1);
		
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_ALL_MODALITY_VALUES_ORDER_BY_CODE");
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute();
			sdr = (ScrollableDataResult) dr.getDataObject();
			ResultSet rs = sdr.getResultSet();
			int numRows = sdr.getRowsNumber();
			ArrayList list = new ArrayList();
			
			if (sdr.hasRows()){
			sdr.moveTo(1);	
				while(sdr.hasRows()){
					ModalitiesValue modalities = new ModalitiesValue();
					DataRow aDataRow = sdr.getDataRow();
				
					modalities.setName(aDataRow.getColumn("NAME").getStringValue());
					modalities.setDescription(aDataRow.getColumn("DESCRIPTION").getStringValue());
					String id = aDataRow.getColumn("ID").getStringValue();
					modalities.setId(Integer.valueOf(id));
					
					modalities.setITypeCd(aDataRow.getColumn("INPUT_TYPE_CD").getStringValue());
					
					list.add(modalities);
					
					}
		   return list;
			}else{
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "ParameterUseDAO", "loadParameters", "Cannot recover detail information");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);  }

		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "ParameterUseDAO", "loadParameters", "Cannot recover detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
	}
	/**
	 * @see it.eng.spagobi.bo.dao.IModalitiesValueDAO#hasParameters(java.lang.String)
	 */
	public boolean hasParameters (String lovId) throws EMFUserError{
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
			String strSql = SQLStatements.getStatement("SELECT_PARAMETER_USE_MODE_BY_LOV_ID");
			
			elements.add(dataConnection.createDataField("id", Types.DECIMAL, lovId));
		
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute(elements);
			sdr = (ScrollableDataResult) dr.getDataObject();
			boolean hasModes = false;
			if(sdr.hasRows()){
				hasModes = true;
			}
		  return hasModes;
		}
		    catch (Exception ex) {
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAOImpl", "load", "Cannot recover detail information", ex);
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
			} finally {
				Utils.releaseResources(dataConnection, cmdSelect, dr);
			}
		
	
	
		
	}
	public ModalitiesValue loadModalitiesValueByLabel(String label) throws EMFUserError {
		// TODO Auto-generated method stub
		return null;
	}

}
