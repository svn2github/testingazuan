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
import it.eng.spagobi.bo.ParameterUse;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IRoleDAO;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;


/**
 *	Defines the JDBC implementations for all DAO methods,
 *  for a role.
 * 
 * @author zoppello
 */
public class RoleDAOImpl extends AbstractJdbcDAO implements IRoleDAO {

	/**
	 * @see it.eng.spagobi.bo.dao.IRoleDAO#loadByID(java.lang.Integer)
	 */
	public Role loadByID(Integer roleID) throws EMFUserError {
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		ArrayList parameters = new ArrayList(1);
		Role role = new Role();
		
		DataConnection dataConnection = null;
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_ROLE_BY_ID");
			
			DataField dataField = dataConnection.createDataField("EXT_ROLE_ID", Types.NUMERIC, roleID);
			parameters.add(dataField);
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute(parameters);
			sdr = (ScrollableDataResult) dr.getDataObject();
			
			if (sdr.hasRows()){
				sdr.moveTo(1);
				DataRow aDataRow = sdr.getDataRow();
				String id = aDataRow.getColumn("EXT_ROLE_ID").getStringValue();
				
				role.setId(Integer.valueOf(id));
				
				role.setName(aDataRow.getColumn("NAME").getStringValue());
				role.setDescription(aDataRow.getColumn("DESCR").getStringValue());
				role.setCode(aDataRow.getColumn("CODE").getStringValue());
				String roleTypeId = aDataRow.getColumn("ROLE_TYPE_ID").getStringValue();
				
				role.setRoleTypeID(Integer.valueOf(roleTypeId));
				
				
				return role;
			}else{
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "RoleDAOImpl", "load", "Cannot recover detail information");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
			}
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "RoleDAOImpl", "load", "Cannot recover detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IRoleDAO#loadByName(java.lang.String)
	 */
	public Role loadByName(String roleName) throws EMFUserError {
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		ArrayList parameters = new ArrayList(1);
		Role role = new Role();
		
		DataConnection dataConnection = null;
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_ROLE_BY_NAME");
			
			DataField dataField = dataConnection.createDataField("NAME", Types.VARCHAR, roleName);
			parameters.add(dataField);
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute(parameters);
			sdr = (ScrollableDataResult) dr.getDataObject();
			
			if (sdr.hasRows()){
				sdr.moveTo(1);
				DataRow aDataRow = sdr.getDataRow();
				
				String id = aDataRow.getColumn("EXT_ROLE_ID").getStringValue();
				
				role.setId(Integer.valueOf(id));
			
				role.setName(aDataRow.getColumn("NAME").getStringValue());
				role.setDescription(aDataRow.getColumn("DESCR").getStringValue());
				role.setCode(aDataRow.getColumn("CODE").getStringValue());
				String roleTypeId = aDataRow.getColumn("ROLE_TYPE_ID").getStringValue();
				
				role.setRoleTypeID(Integer.valueOf(roleTypeId));
				
				role.setRoleTypeCD(aDataRow.getColumn("ROLE_TYPE_CD").getStringValue());
				
				return role;
			}else{
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "RoleDAOImpl", "load", "Cannot recover detail information");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
			}
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "RoleDAOImpl", "load", "Cannot recover detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IRoleDAO#loadAllRoles()
	 */
	public List loadAllRoles() throws EMFUserError {
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		ArrayList parameters = new ArrayList(1);
		List roles = new ArrayList();
		
		DataConnection dataConnection = null;
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_ALL_ROLES");
			
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute();
			sdr = (ScrollableDataResult) dr.getDataObject();
			
			DataRow aDataRow = null;
			Role role = null;
			if (sdr.hasRows()){
				sdr.moveTo(1);
				while (sdr.hasRows()){
					aDataRow = sdr.getDataRow();
					role = new Role();
					String id = aDataRow.getColumn("EXT_ROLE_ID").getStringValue();
					
					role.setId(Integer.valueOf(id));
					
					role.setName(aDataRow.getColumn("NAME").getStringValue());
					role.setDescription(aDataRow.getColumn("DESCR").getStringValue());
					role.setCode(aDataRow.getColumn("CODE").getStringValue());
					String roleTypeId = aDataRow.getColumn("ROLE_TYPE_ID").getStringValue();
					
					role.setRoleTypeID(Integer.valueOf(roleTypeId));
					
					role.setRoleTypeCD(aDataRow.getColumn("ROLE_TYPE_CD").getStringValue());
					
					
					roles.add(role);
				}
			}
			return roles;
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "RoleDAOImpl", "loadAll", "Cannot select all roles", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IRoleDAO#insertRole(it.eng.spagobi.bo.Role)
	 */
	public void insertRole(Role aRole) throws EMFUserError {
		SQLCommand cmdIns = null;
		DataResult dr = null;
		DataConnection dataConnection = null;
		
		try {
			
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("INSERT_ROLE");
			
			ArrayList parameters = new ArrayList(2);
			
			parameters.add(dataConnection.createDataField("NAME", Types.VARCHAR, aRole.getName()));
			parameters.add(dataConnection.createDataField("DESCR", Types.VARCHAR, aRole.getDescription()));
			parameters.add(dataConnection.createDataField("CODE",Types.VARCHAR, aRole.getCode()));
			parameters.add(dataConnection.createDataField("ROLE_TYPE_ID", Types.NUMERIC, aRole.getRoleTypeID()));
			parameters.add(dataConnection.createDataField("ROLE_TYPE_CD", Types.VARCHAR, aRole.getRoleTypeCD()));
			

			cmdIns = dataConnection.createInsertCommand(strSql);
			dr = cmdIns.execute(parameters);
			
		}  catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "RoleDAOImpl", "insert", "Cannot insert detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdIns, dr);
		}
		
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IRoleDAO#eraseRole(it.eng.spagobi.bo.Role)
	 */
	public void eraseRole(Role aRole) throws EMFUserError {
		SQLCommand cmdDel = null;
		DataResult dr = null;
		DataConnection dataConnection = null;
		
		try {
			
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("DELETE_ROLE");
			
			ArrayList parameters = new ArrayList(1);
			parameters.add(dataConnection.createDataField("EXT_ROLE_ID", Types.DECIMAL, aRole.getId()));
			
			cmdDel = dataConnection.createDeleteCommand(strSql);
			dr = cmdDel.execute(parameters);
			
		}  catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "RoleDAOImpl", "erase", "Cannot erase detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdDel, dr);
		}
		
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IRoleDAO#modifyRole(it.eng.spagobi.bo.Role)
	 */
	public void modifyRole(Role aRole) throws EMFUserError {
		
		
	}
	
	
	
	/**
	 * @see it.eng.spagobi.bo.dao.IRoleDAO#loadAllFreeRolesForDetail(java.lang.Integer)
	 */
	public List loadAllFreeRolesForDetail(Integer parUseID) throws EMFUserError{
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		ArrayList parameters = new ArrayList(2);
		List roles = new ArrayList();
		
		ParameterUse aParameterUse = DAOFactory.getParameterUseDAO().loadByUseID(parUseID);
		DataConnection dataConnection = null;
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_ALL_FREE_ROLES_FOR_DETAIL");
			
			DataField dataField = dataConnection.createDataField("", Types.NUMERIC, aParameterUse.getId());
			//DataField dataField1 = dataConnection.createDataField("", Types.NUMERIC, aParameterUse.getIdLov());
			DataField dataField1 = dataConnection.createDataField("", Types.VARCHAR, aParameterUse.getName());
			
			parameters.add(dataField);
			parameters.add(dataField1);
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute(parameters);
			sdr = (ScrollableDataResult) dr.getDataObject();
			
			DataRow aDataRow = null;
			Role role = null;
			if (sdr.hasRows()){
				sdr.moveTo(1);
				while (sdr.hasRows()){
					aDataRow = sdr.getDataRow();
					role = new Role();
					String id = aDataRow.getColumn("EXT_ROLE_ID").getStringValue();
					
					role.setId(Integer.valueOf(id));
					
					role.setName(aDataRow.getColumn("NAME").getStringValue());
					role.setDescription(aDataRow.getColumn("DESCR").getStringValue());
					role.setCode(aDataRow.getColumn("CODE").getStringValue());
					String roleTypeId = aDataRow.getColumn("ROLE_TYPE_ID").getStringValue();
					
					role.setRoleTypeID(Integer.valueOf(roleTypeId));
					
					role.setRoleTypeCD(aDataRow.getColumn("ROLE_TYPE_CD").getStringValue());
					
					
					roles.add(role);
				}
			}
			return roles;
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "RoleDAOImpl", "loadAll", "Cannot select all roles", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
	
	}
	/**
	 * @see it.eng.spagobi.bo.dao.IRoleDAO#loadAllFreeRolesForInsert(java.lang.Integer)
	 */
	public List loadAllFreeRolesForInsert(Integer parameterID) throws EMFUserError{
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		ArrayList parameters = new ArrayList(2);
		List roles = new ArrayList();
		
		DataConnection dataConnection = null;
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_ALL_FREE_ROLES_FOR_INSERT");
			
			DataField dataField = dataConnection.createDataField("PAR_ID", Types.NUMERIC, parameterID);
			
			parameters.add(dataField);
		
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute(parameters);
			sdr = (ScrollableDataResult) dr.getDataObject();
			
			DataRow aDataRow = null;
			Role role = null;
			if (sdr.hasRows()){
				sdr.moveTo(1);
				while (sdr.hasRows()){
					aDataRow = sdr.getDataRow();
					role = new Role();
					String id = aDataRow.getColumn("EXT_ROLE_ID").getStringValue();
					
					role.setId(Integer.valueOf(id));
				
					role.setName(aDataRow.getColumn("NAME").getStringValue());
					role.setDescription(aDataRow.getColumn("DESCR").getStringValue());
					role.setCode(aDataRow.getColumn("CODE").getStringValue());
					String roleTypeId = aDataRow.getColumn("ROLE_TYPE_ID").getStringValue();
					
					role.setRoleTypeID(Integer.valueOf(roleTypeId));
					
					role.setRoleTypeCD(aDataRow.getColumn("ROLE_TYPE_CD").getStringValue());
					
					
					roles.add(role);
				}
			}
			return roles;
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "RoleDAOImpl", "loadAll", "Cannot select all roles", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
	
	}
	
	
	
	
	
	


	


	

}
