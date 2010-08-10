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
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.Check;
import it.eng.spagobi.bo.ModalitiesValue;
import it.eng.spagobi.bo.ParameterUse;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IParameterUseDAO;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 *	Defines the JDBC implementations for all DAO methods,
 *  for a parameter use mode.  
 * 
 * @author zoppello
 */
public class ParameterUseDAOImpl extends AbstractJdbcDAO  implements IParameterUseDAO {

	/**
	 * @see it.eng.spagobi.bo.dao.IParameterUseDAO#fillRolesForParUse(it.eng.spagobi.bo.ParameterUse)
	 */
	public void fillRolesForParUse(ParameterUse aParameterUse) throws EMFUserError {
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		ArrayList elements = new ArrayList(1);
		//nome cambiato per evitare confusione
		ParameterUse param = new ParameterUse();
		boolean crypt= false;
		DataConnection dataConnection = null;
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_ROLES_FOR_PARUSE");
			
			DataField dataField = dataConnection.createDataField("", Types.DECIMAL, aParameterUse.getUseID());
			elements.add(dataField);
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute(elements);
			sdr = (ScrollableDataResult) dr.getDataObject();
			if (sdr.hasRows()){
				List associatedRoles = new ArrayList();
				Role tmpRole = null;
				for (int i=0; i< sdr.getRowsNumber();i++){
					DataRow aDataRow = sdr.getDataRow();
					tmpRole= new Role();
					String currRole = aDataRow.getColumn("ID_EXT_ROLE").getStringValue();
					tmpRole.setId(Integer.valueOf(currRole));
					associatedRoles.add(tmpRole);
				}
				aParameterUse.setAssociatedRoles(associatedRoles);
			}
			
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAOImpl", "load", "Cannot recover detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
	}
	/**
	 * @see it.eng.spagobi.bo.dao.IParameterUseDAO#fillAssociatedChecksForParUse(it.eng.spagobi.bo.ParameterUse)
	 */
	public void fillAssociatedChecksForParUse(ParameterUse aParameterUse) throws EMFUserError {
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		ArrayList elements = new ArrayList(1);
		//nome cambiato per evitare confusione
		ParameterUse param = new ParameterUse();
		boolean crypt= false;
		DataConnection dataConnection = null;
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_CHECKS_FOR_PARUSE");
			
			DataField dataField = dataConnection.createDataField("", Types.DECIMAL, aParameterUse.getUseID());
			elements.add(dataField);
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute(elements);
			sdr = (ScrollableDataResult) dr.getDataObject();
			
			if (sdr.hasRows()){
				List localChecksList = new ArrayList();
				Check aCheck = null;
				for (int i=0; i< sdr.getRowsNumber();i++){
					DataRow aDataRow = sdr.getDataRow();
					
					String currCheckId = aDataRow.getColumn("ID_CHECK").getStringValue();
					aCheck = DAOFactory.getChecksDAO().loadCheckByID(Integer.valueOf(currCheckId));
					
					localChecksList.add(aCheck);
				}
				aParameterUse.setAssociatedChecks(localChecksList);
			}
			
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAOImpl", "load", "Cannot recover detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
	}
	/* (non-Javadoc)
	 * @see it.eng.spagobi.bo.dao.IParameterUseDAO#loadByParameterIDandListOfValuesID(java.lang.Integer, java.lang.Integer)
	 
	public ParameterUse loadByParameterIDandListOfValuesID(Integer parameterID, Integer listOfValuesID) throws EMFUserError {
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
			String strSql = SQLStatements.getStatement("SELECT_PARAMETER_USE_DETAIL");
			
			DataField dataField = dataConnection.createDataField("id", Types.DECIMAL, parameterID);
			DataField dataField1 = dataConnection.createDataField("idLov", Types.DECIMAL, listOfValuesID);
			elements.add(dataField);
			elements.add(dataField1);
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute(elements);
			sdr = (ScrollableDataResult) dr.getDataObject();
			if(sdr.hasRows()){
				sdr.moveTo(1);
				DataRow aDataRow = sdr.getDataRow();
				String useID = aDataRow.getColumn("USE_ID").getStringValue();
				param.setUseID(Integer.valueOf(useID));
					
				String id = aDataRow.getColumn("ID").getStringValue();
					
				param.setId(Integer.valueOf(id));
					
				String idLov = aDataRow.getColumn("ID_LOV").getStringValue();
					
				param.setIdLov(Integer.valueOf(idLov));
					
				param.setName(aDataRow.getColumn("NAME").getStringValue());
				param.setDescription(aDataRow.getColumn("DESCRIPTION").getStringValue());
				
			}else{
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "ParameterUseDAOImpl", "load", "Cannot recover detail information");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);  }
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAOImpl", "load", "Cannot recover detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
		fillRolesForParUse(param);
		return param;
	
	}
	*/

	/**
	 * @see it.eng.spagobi.bo.dao.IParameterUseDAO#modifyParameterUse(it.eng.spagobi.bo.ParameterUse)
	 */
	public void modifyParameterUse(ParameterUse aParameterUse) throws EMFUserError {
		SQLCommand cmd = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		DataConnection dataConnection = null;
		String sql = null;
		ArrayList parameters = null;
		try {
			
			dataConnection = getConnection("spagobi");
			dataConnection.initTransaction();
			
			// Erase the relation with sbi_paruse_det
			sql = SQLStatements.getStatement("DELETE_PARUSE_DET_ID_LOV_OLD");
			parameters = new ArrayList(1);
			parameters.add(dataConnection.createDataField("", Types.DECIMAL, aParameterUse.getUseID()));
			cmd = dataConnection.createDeleteCommand(sql);
			dr = cmd.execute(parameters);
			
			sql = SQLStatements.getStatement("DELETE_PARUSE_CK");
			parameters = new ArrayList(1);
			parameters.add(dataConnection.createDataField("", Types.DECIMAL, aParameterUse.getUseID()));
			cmd = dataConnection.createDeleteCommand(sql);
			dr = cmd.execute(parameters);
			
			//Update the sbi_paruse table
			parameters = new ArrayList(5);
			sql = SQLStatements.getStatement("UPDATE_PAR_USE");
			
			//query="UPDATE  SBI_PARUSE SET PAR_ID=?,	LOV_ID=?, LABEL=?,	DESCR=?	WHERE USE_ID=?"
			parameters.add(dataConnection.createDataField("PAR_ID", Types.DECIMAL, aParameterUse.getId()));
			parameters.add(dataConnection.createDataField("LOV_ID", Types.DECIMAL, aParameterUse.getIdLov()));
			parameters.add(dataConnection.createDataField("LABEL", Types.VARCHAR, aParameterUse.getName()));
			parameters.add(dataConnection.createDataField("DESCR", Types.VARCHAR, aParameterUse.getDescription()));
			parameters.add(dataConnection.createDataField("USE_ID", Types.DECIMAL, aParameterUse.getUseID()));
			cmd = dataConnection.createUpdateCommand(sql);
			dr = cmd.execute(parameters);
			
			//Recreate Relations with sbi_paruse_det
			List newRoles = aParameterUse.getAssociatedRoles();
			sql = SQLStatements.getStatement("INSERT_PARUSE_DET");
			
			parameters = new ArrayList(5);
			Role tmpRole= null;
			for (int i=0; i<newRoles.size();i++){
				tmpRole = (Role)newRoles.get(i);
				parameters.clear();
				parameters.add(dataConnection.createDataField("USE_ID", Types.DECIMAL, aParameterUse.getUseID()));
				parameters.add(dataConnection.createDataField("EXT_ROLE_ID", Types.DECIMAL, tmpRole.getId()));
				parameters.add(dataConnection.createDataField("PROG", Types.DECIMAL, "1"));
				cmd = dataConnection.createInsertCommand(sql);
				dr = cmd.execute(parameters);
			}
			
			sql = SQLStatements.getStatement("INSERT_PARUSE_CK");
			
			List newChecks = aParameterUse.getAssociatedChecks();
			Check tmpCheck = null;
			parameters = new ArrayList(2);
			for (int i=0; i < newChecks.size() ;i++){
				tmpCheck = (Check)newChecks.get(i);
				parameters.clear(); 
				parameters.add(dataConnection.createDataField("USE_ID", Types.DECIMAL, aParameterUse.getUseID()));
				parameters.add(dataConnection.createDataField("CHECK_ID", Types.DECIMAL, tmpCheck.getCheckId()));
				cmd = dataConnection.createInsertCommand(sql);
				dr = cmd.execute(parameters);
			}
			dataConnection.commitTransaction();
			
		}  catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAO", "modify", "Cannot update detail information", ex);
			try {
				dataConnection.rollBackTransaction();
			} catch (EMFInternalError e) {
				
				e.printStackTrace();
			}
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmd, dr);
		}
		
	}
	
	/**
	 * Gets the use id for a parameter use mode, known its label and 
	 * its referred parameter id 
	 * 
	 * @param label The label string
	 * @param parId The parameter use mode ID
	 * @return The Parameter use mode use-ID
	 * @throws EMFUserError If any exception occurs
	 */
	public Integer getUseId(String label, Integer parId) throws EMFUserError {
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		ArrayList elements = new ArrayList(2);
		DataConnection dataConnection = null;
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_USEID_FROM_LABEL");
			DataField dataField = dataConnection.createDataField("label", Types.VARCHAR, label);
			DataField dataField1 = dataConnection.createDataField("idPar", Types.DECIMAL, parId);
			elements.add(dataField);
			elements.add(dataField1);
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute(elements);
			sdr = (ScrollableDataResult) dr.getDataObject();
			if(sdr.hasRows()){
				sdr.moveTo(1);
				DataRow aDataRow = sdr.getDataRow();
				String useID = aDataRow.getColumn("USE_ID").getStringValue();
				return Integer.valueOf(useID);	
			}else{
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "ParameterUseDAOImpl", "load", "Cannot recover detail information");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);  }
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "ParameterUseDAOImpl", "load", "Cannot recover detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
	}
	
	/*
	public Integer getUseId(Integer parameterID, Integer lovID) throws EMFUserError {
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		ArrayList elements = new ArrayList(2);
		
		DataConnection dataConnection = null;
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_USEID_FROM_PARUSE");
			
			DataField dataField = dataConnection.createDataField("idPar", Types.DECIMAL, parameterID);
			DataField dataField1 = dataConnection.createDataField("idLov", Types.DECIMAL, lovID);
			elements.add(dataField);
			elements.add(dataField1);
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute(elements);
			sdr = (ScrollableDataResult) dr.getDataObject();
			if(sdr.hasRows()){
				sdr.moveTo(1);
				DataRow aDataRow = sdr.getDataRow();
				String useID = aDataRow.getColumn("USE_ID").getStringValue();
				return Integer.valueOf(useID);	
			
			}else{
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "ParameterUseDAOImpl", "load", "Cannot recover detail information");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);  }
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAOImpl", "load", "Cannot recover detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
	}
	*/
	
	
	
	
	
	/**
	 * @see it.eng.spagobi.bo.dao.IParameterUseDAO#insertParameterUse(it.eng.spagobi.bo.ParameterUse)
	 */
	public void insertParameterUse(ParameterUse aParameterUse) throws EMFUserError {
		SQLCommand cmd = null;
		DataResult dr = null;
		DataConnection dataConnection = null;
		try {
			
			dataConnection = getConnection("spagobi");
			dataConnection.initTransaction();
			String sql = SQLStatements.getStatement("INSERT_PARUSE");
			
			ArrayList parameters = new ArrayList(4); 
			parameters.add(dataConnection.createDataField("PAR_ID", Types.DECIMAL, aParameterUse.getId()));
			parameters.add(dataConnection.createDataField("LOV_ID", Types.DECIMAL, aParameterUse.getIdLov()));
			parameters.add(dataConnection.createDataField("LABEL", Types.VARCHAR, aParameterUse.getName()));
			parameters.add(dataConnection.createDataField("DESCR", Types.VARCHAR, aParameterUse.getDescription()));
			cmd = dataConnection.createInsertCommand(sql);
			dr = cmd.execute(parameters);
			dataConnection.commitTransaction();
			dataConnection.initTransaction();
			// Recreate Relations with sbi_paruse_det
			List newRoles = aParameterUse.getAssociatedRoles();
			
			// Integer useID = this.getUseId(aParameterUse.getId(), aParameterUse.getIdLov());
			Integer useID = this.getUseId(aParameterUse.getName(), aParameterUse.getId());
			
			sql = SQLStatements.getStatement("INSERT_PARUSE_DET");	
			aParameterUse.setUseID(useID);
			
			parameters = new ArrayList(5);
			Role tmpRole = null;
			for (int i=0; i < newRoles.size() ;i++){
				tmpRole = (Role)newRoles.get(i);
				parameters.clear();
				parameters.add(dataConnection.createDataField("USE_ID", Types.DECIMAL, aParameterUse.getUseID()));
				parameters.add(dataConnection.createDataField("EXT_ROLE_ID", Types.DECIMAL, tmpRole.getId()));
				parameters.add(dataConnection.createDataField("PROG", Types.DECIMAL, "1"));
				cmd = dataConnection.createInsertCommand(sql);
				dr = cmd.execute(parameters);
			}
			
			sql = SQLStatements.getStatement("INSERT_PARUSE_CK");
			List newChecks = aParameterUse.getAssociatedChecks();
			Check tmpCheck = null;
			parameters = new ArrayList(2);
			for (int i=0; i < newChecks.size() ;i++){
				tmpCheck = (Check)newChecks.get(i);
				parameters.clear(); 
				parameters.add(dataConnection.createDataField("USE_ID", Types.DECIMAL, aParameterUse.getUseID()));
				parameters.add(dataConnection.createDataField("CHECK_ID", Types.DECIMAL, tmpCheck.getCheckId()));
				cmd = dataConnection.createInsertCommand(sql);
				dr = cmd.execute(parameters);
			}
			dataConnection.commitTransaction();
		}  catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "ParametersDAO", "insert", "Cannot insert detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmd, dr);
		}
		
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IParameterUseDAO#eraseParameterUse(it.eng.spagobi.bo.ParameterUse)
	 */
	public void eraseParameterUse(ParameterUse aParameterUse) throws EMFUserError {
		SQLCommand cmd = null;
		DataResult dr = null;
		DataConnection dataConnection = null;
		
		try {
			dataConnection = getConnection("spagobi");

			ArrayList parameters = new ArrayList(1); 
			String sql = SQLStatements.getStatement("DELETE_PARUSE_DET_ID_LOV_OLD");
			parameters = new ArrayList(1);
			parameters.add(dataConnection.createDataField("", Types.DECIMAL, aParameterUse.getUseID()));
			cmd = dataConnection.createDeleteCommand(sql);
			dr = cmd.execute(parameters);
			
			sql = SQLStatements.getStatement("DELETE_PARUSE_CK");
			parameters = new ArrayList(1);
			parameters.add(dataConnection.createDataField("", Types.DECIMAL, aParameterUse.getUseID()));
			cmd = dataConnection.createDeleteCommand(sql);
			dr = cmd.execute(parameters);
			
			parameters.clear();
			sql= SQLStatements.getStatement("DELETE_PARUSE");
			parameters = new ArrayList(1);
			parameters.add(dataConnection.createDataField("", Types.DECIMAL, aParameterUse.getUseID()));
			cmd = dataConnection.createDeleteCommand(sql);
			dr = cmd.execute(parameters);
			
		}  catch (Exception ex) {
			try {
				dataConnection.rollBackTransaction();
			} catch (EMFInternalError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "DomainsDAO", "erase", "Cannot insert detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmd, dr);
		}
		
	}
	
	

	
	/**
	 * @see it.eng.spagobi.bo.dao.IParameterUseDAO#loadByUseID(java.lang.Integer)
	 */
	
	public List loadParametersUseByParId(Integer parId) throws EMFUserError {
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		ArrayList parameters = new ArrayList(1);
		DataConnection dataConnection = null;
		ModalitiesValue modVal = null;
		ArrayList parameters1 = new ArrayList(1);
		ArrayList elements = new ArrayList(1);
		
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_PARAMETER_USE_MODE_BY_PAR_ID");
			cmdSelect = dataConnection.createSelectCommand(strSql);
			elements.add(dataConnection.createDataField("id", Types.DECIMAL, parId));
			dr = cmdSelect.execute(elements);
			sdr = (ScrollableDataResult) dr.getDataObject();
			ResultSet rs = sdr.getResultSet();
			int numRows = sdr.getRowsNumber();
			
			ArrayList list = new ArrayList();
			if (sdr.hasRows()){
			sdr.moveTo(1);	
				while(sdr.hasRows()){
					ParameterUse param = new ParameterUse();
					DataRow aDataRow = sdr.getDataRow();
				
					String useIDStr = aDataRow.getColumn("USE_ID").getStringValue();
					param.setUseID(Integer.valueOf(useIDStr));
						
					String id = aDataRow.getColumn("ID").getStringValue();
						
					param.setId(Integer.valueOf(id));
						
					String idLov = aDataRow.getColumn("ID_LOV").getStringValue();
						
					param.setIdLov(Integer.valueOf(idLov));
						
					param.setName(aDataRow.getColumn("NAME").getStringValue());
					param.setDescription(aDataRow.getColumn("DESCRIPTION").getStringValue());
					
					list.add(param);
					
					}
			}
			return list;
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "ParameterUseDAO", "loadParameters", "Cannot recover detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
		
	}
	/**
	 * @see it.eng.spagobi.bo.dao.IParameterUseDAO#loadByUseID(java.lang.Integer)
	 */
	public ParameterUse loadByUseID(Integer useID) throws EMFUserError {
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
			String strSql = SQLStatements.getStatement("SELECT_PARAMETER_USE_DETAIL_BY_USEID");
			
			elements.add(dataConnection.createDataField("id", Types.DECIMAL, useID));
		
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute(elements);
			sdr = (ScrollableDataResult) dr.getDataObject();
			if(sdr.hasRows()){
				sdr.moveTo(1);
				DataRow aDataRow = sdr.getDataRow();
				String useIDStr = aDataRow.getColumn("USE_ID").getStringValue();
				param.setUseID(Integer.valueOf(useIDStr));
					
				String id = aDataRow.getColumn("ID").getStringValue();
					
				param.setId(Integer.valueOf(id));
					
				String idLov = aDataRow.getColumn("ID_LOV").getStringValue();
					
				param.setIdLov(Integer.valueOf(idLov));
					
				param.setName(aDataRow.getColumn("NAME").getStringValue());
				param.setDescription(aDataRow.getColumn("DESCRIPTION").getStringValue());
				
			}else{
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "ParameterUseDAOImpl", "load", "Cannot recover detail information");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);  }
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAOImpl", "load", "Cannot recover detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
		fillRolesForParUse(param);
		fillAssociatedChecksForParUse(param);
		return param;
	}
	
	/**
	 * @see it.eng.spagobi.bo.dao.IParameterUseDAO#hasParUseModes(java.lang.String)
	 */
	public boolean hasParUseModes (String parId) throws EMFUserError{
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
			String strSql = SQLStatements.getStatement("SELECT_PARAMETER_USE_MODE_BY_PAR_ID");
			
			elements.add(dataConnection.createDataField("id", Types.DECIMAL, parId));
		
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
	
	
	
	
	public void eraseParameterUseByParId(Integer parId) throws EMFUserError {
					List parUseList = null;
					IParameterUseDAO parUseDAO = DAOFactory.getParameterUseDAO();
					parUseList = parUseDAO.loadParametersUseByParId(parId);
					Iterator i = parUseList.iterator();
					while (i.hasNext()){
						ParameterUse parUse = (ParameterUse) i.next();
						parUseDAO.eraseParameterUse(parUse);
						
						
					}
					
					
					
					
					
					
					
			
	     
			
			}
	
}
	
