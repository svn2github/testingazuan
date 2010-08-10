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
import it.eng.spagobi.bo.Parameter;
import it.eng.spagobi.bo.ParameterUse;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IParameterDAO;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


/**
 *	Defines the JDBC implementations for all DAO methods,
 *  for a parameter.
 * 
 * @author zoppello
 */
public class ParameterDAOImpl extends AbstractJdbcDAO implements IParameterDAO {

	/**
	 * @see it.eng.spagobi.bo.dao.IParameterDAO#loadForDetailByParameterID(java.lang.Integer)
	 */
	public Parameter loadForDetailByParameterID(Integer parameterID) throws EMFUserError {
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		ArrayList elements = new ArrayList(1);  //nome cambiato per evitare confusione
		Parameter parameter = new Parameter();
		boolean crypt= false;
		DataConnection dataConnection = null;
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_PARAMETERS");
		
			DataField dataField = dataConnection.createDataField("id", Types.DECIMAL, parameterID);
			elements.add(dataField);
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute(elements);
			sdr = (ScrollableDataResult) dr.getDataObject();
			ResultSet rs = sdr.getResultSet();
			if (sdr.hasRows()){
				sdr.moveTo(1);
				while (sdr.hasRows()){
					DataRow aDataRow = sdr.getDataRow();
					String id = aDataRow.getColumn("ID").getStringValue();
					
					parameter.setId(Integer.valueOf(id));
					
					parameter.setDescription(aDataRow.getColumn("DESCRIPTION").getStringValue());
					String length = aDataRow.getColumn("LENGTH").getStringValue();
					
					parameter.setLength(Integer.valueOf(length));
				
					parameter.setLabel(aDataRow.getColumn("LABEL").getStringValue());
					parameter.setType(aDataRow.getColumn("TYPE").getStringValue());
					parameter.setMask(aDataRow.getColumn("MASK").getStringValue());
					String typeId = aDataRow.getColumn("TYPE_ID").getStringValue();
					
					parameter.setTypeId(Integer.valueOf(typeId));
					
					
				}
			
			return parameter;
			}else{
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAOImpl", "load", "Cannot recover detail information");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);  }

		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAOImpl", "load", "Cannot recover detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IParameterDAO#loadForExecutionByParameterIDandRoleName(java.lang.Integer, java.lang.String)
	 */
	public Parameter loadForExecutionByParameterIDandRoleName(Integer parameterID, String roleName) throws EMFUserError {
		Parameter parameter = null;
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		ArrayList elements = new ArrayList(1); 
		DataConnection dataConnection = null;
//		 LOAD THE LOV FOR THE MODALITY
		String useIdStr = null;
		String idLovStr = null;
		
		try {
			parameter = loadForDetailByParameterID(parameterID);
			
			Role role = DAOFactory.getRoleDAO().loadByName(roleName);
			 
			dataConnection = getConnection("spagobi");
			
			// FIND THE MODALITY FOR PAR_ID AND ROLE NAME
			String strSql = SQLStatements.getStatement("SELECT_PARAMETER_USE_FROM_IDPAR_IDROLE");
			DataField dataField = dataConnection.createDataField("PAR_ID", Types.DECIMAL, parameter.getId());
			elements.add(dataField);
			dataField = dataConnection.createDataField("EXT_ROLE_ID", Types.DECIMAL, role.getId());
			elements.add(dataField);
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute(elements);
			sdr = (ScrollableDataResult) dr.getDataObject();
			
			
			
			if(sdr.hasRows()) {
				DataRow row = sdr.getDataRow(1);
				useIdStr = row.getColumn("USE_ID").getObjectValue().toString();
				idLovStr = row.getColumn("LOV_ID").getObjectValue().toString();
					
				
				
			}
			
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "ParameterDAOImpl", "load", "Cannot recover detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
		
		ModalitiesValue modVal  = DAOFactory.getModalitiesValueDAO().loadModalitiesValueByID(Integer.valueOf(idLovStr));
		parameter.setModalityValue(modVal);
		
		//LOAD THE CHECKS FOR THE MODALITY
		ParameterUse aParameterUse = DAOFactory.getParameterUseDAO().loadByUseID(new Integer(useIdStr));
		
		// Carico i Checks 
		parameter.setChecks(aParameterUse.getAssociatedChecks());
		return parameter;  
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IParameterDAO#loadAllParameters()
	 */
	public List loadAllParameters() throws EMFUserError {
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		
		List parameters = new ArrayList();
		
		DataConnection dataConnection = null;
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_ALL_PARAMETERS");
			
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute();
			sdr = (ScrollableDataResult) dr.getDataObject();
			
			DataRow aDataRow = null;
			Parameter parameter = null;
			if (sdr.hasRows()){
				sdr.moveTo(1);
				while (sdr.hasRows()){
					aDataRow = sdr.getDataRow();
					parameter = new Parameter();
					
					String id = aDataRow.getColumn("ID").getStringValue();
					
					parameter.setId(Integer.valueOf(id));
					
					parameter.setDescription(aDataRow.getColumn("DESCRIPTION").getStringValue());
					String length = aDataRow.getColumn("LENGTH").getStringValue();
					
					parameter.setLength(Integer.valueOf(length));
					
					parameter.setLabel(aDataRow.getColumn("LABEL").getStringValue());
					parameter.setType(aDataRow.getColumn("TYPE").getStringValue());
					parameter.setMask(aDataRow.getColumn("MASK").getStringValue());
					String typeId = aDataRow.getColumn("TYPE_ID").getStringValue();
					
					parameter.setTypeId(Integer.valueOf(typeId));
					
					parameters.add(parameter);
				}
			}
			return parameters;
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "ParameterDAOImpl", "loadAll", "Cannot select all roles", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
	
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IParameterDAO#modifyParameter(it.eng.spagobi.bo.Parameter)
	 */
	public void modifyParameter(Parameter aParameter) throws EMFUserError {
		SQLCommand cmdMod = null;
		DataResult dr = null;
		DataConnection dataConnection = null;
		
		try {
			
			
			String info = aParameter.getModality();
			StringTokenizer st; 
			String token = null;
			st = new StringTokenizer (info, ",", false);
			String input_type_cd = st.nextToken();
			String input_type_id = st.nextToken();
			Long typeIdLong = new Long(Long.parseLong(input_type_id));
			BigDecimal typeIdBD = BigDecimal.valueOf(typeIdLong.longValue());
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("UPDATE_PARAMETERS");
			
			ArrayList elements = new ArrayList(7);//array chiamato elements e non parameters per non creare confusione col nome della classe
			elements.add(dataConnection.createDataField("DESCRIPTION", Types.VARCHAR, aParameter.getDescription()));
			elements.add(dataConnection.createDataField("LENGTH", Types.DECIMAL, aParameter.getLength()));
			elements.add(dataConnection.createDataField("LABEL", Types.VARCHAR, aParameter.getLabel()));
			elements.add(dataConnection.createDataField("TYPE", Types.VARCHAR, input_type_cd));
			elements.add(dataConnection.createDataField("MASK", Types.VARCHAR, aParameter.getMask()));
			elements.add(dataConnection.createDataField("TYPE_ID", Types.DECIMAL, typeIdBD));
			elements.add(dataConnection.createDataField("ID", Types.DECIMAL, aParameter.getId()));
			
			cmdMod = dataConnection.createUpdateCommand(strSql);
			dr = cmdMod.execute(elements);
		}  catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAOImpl", "modify", "Cannot update detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 1015);
		} finally {
			Utils.releaseResources(dataConnection, cmdMod, dr);
		}
		
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IParameterDAO#insertParameter(it.eng.spagobi.bo.Parameter)
	 */
	public void insertParameter(Parameter aParameter) throws EMFUserError {
		SQLCommand cmdIns = null;
		DataResult dr = null;
		DataConnection dataConnection = null;
		
		try {
			
			String info = aParameter.getModality();
			StringTokenizer st; 
			String token = null;
			st = new StringTokenizer (info, ",", false);
			String input_type_cd = st.nextToken();
			String input_type_id = st.nextToken();
			Long typeIdLong = new Long(Long.parseLong(input_type_id));
			BigDecimal typeIdBD = BigDecimal.valueOf(typeIdLong.longValue());
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("INSERT_PARAMETERS");
			
			ArrayList elements = new ArrayList(6); //cambio nome array per evitare confusione
			elements.add(dataConnection.createDataField("DESCR", Types.VARCHAR, aParameter.getDescription()));
			elements.add(dataConnection.createDataField("LENGTH", Types.DECIMAL, aParameter.getLength()));		
			elements.add(dataConnection.createDataField("LABEL", Types.VARCHAR, aParameter.getLabel()));
			elements.add(dataConnection.createDataField("PAR_TYPE_CD", Types.VARCHAR, input_type_cd));
			elements.add(dataConnection.createDataField("MASK", Types.VARCHAR, aParameter.getMask()));
			elements.add(dataConnection.createDataField("PAR_TYPE_ID", Types.DECIMAL, typeIdBD));
			
			
			cmdIns = dataConnection.createInsertCommand(strSql);
			dr = cmdIns.execute(elements);
			
		}  catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "ParametersDAO", "insert", "Cannot insert detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 1015);
		} finally {
			Utils.releaseResources(dataConnection, cmdIns, dr);
		}
		
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IParameterDAO#eraseParameter(it.eng.spagobi.bo.Parameter)
	 */
	public void eraseParameter(Parameter aParameter) throws EMFUserError {
		SQLCommand cmdDel = null;
		DataResult dr = null;
		DataConnection dataConnection = null;
		try {
			
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("DELETE_PARAMETERS");
			
			ArrayList elements = new ArrayList(1);  //cambio nome array (confusione)
			elements.add(dataConnection.createDataField("PAR_ID", Types.DECIMAL, aParameter.getId()));
			
			cmdDel = dataConnection.createDeleteCommand(strSql);
			dr = cmdDel.execute(elements);
			
		}  catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "DomainsDAO", "erase", "Cannot insert detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdDel, dr);
		}
		
	}

	
	
	
	
	
	
	
	


	


	
}
