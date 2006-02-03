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

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.cms.exec.CMSConnection;
import it.eng.spago.cms.exec.OperationExecutor;
import it.eng.spago.cms.exec.OperationExecutorManager;
import it.eng.spago.cms.exec.operations.DeleteOperation;
import it.eng.spago.cms.exec.operations.GetOperation;
import it.eng.spago.cms.exec.operations.OperationBuilder;
import it.eng.spago.cms.exec.operations.SetOperation;
import it.eng.spago.cms.exec.results.ElementDescriptor;
import it.eng.spago.cms.init.CMSManager;
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
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.Domain;
import it.eng.spagobi.bo.LowFunctionality;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.ILowFunctionalityDAO;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.services.modules.TreeObjectsModule;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;


/**
 *	Defines the JDBC implementations for all DAO methods,
 *  for a low functionality. 
 * 
 * @author zoppello
 */
public class LowFunctionalityDAOImpl extends AbstractJdbcDAO  implements ILowFunctionalityDAO {

	/**
	 * @see it.eng.spagobi.bo.dao.ILowFunctionalityDAO#loadLowFunctionalityByID(java.lang.Integer)
	 */
	public LowFunctionality loadLowFunctionalityByID(Integer functionalityID) throws EMFUserError {
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		ArrayList parameters = new ArrayList(1);
		LowFunctionality funct = new LowFunctionality();
		boolean crypt= false;
		DataConnection dataConnection = null;
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_LOW_FUNCTIONALITY_BY_ID");
			DataField dataField = dataConnection.createDataField("id", Types.DECIMAL, functionalityID);
			parameters.add(dataField);
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute(parameters);
			sdr = (ScrollableDataResult) dr.getDataObject();
			
			if(!sdr.hasRows()) {
				return null;
			}else{
				sdr.moveTo(1);
				DataRow aDataRow = sdr.getDataRow();
				
				String idFunct= aDataRow.getColumn("ID").getStringValue();
				
				funct.setId(Integer.valueOf(idFunct));
				funct.setDescription(aDataRow.getColumn("DESCRIPTION").getStringValue());
				funct.setName(aDataRow.getColumn("NAME").getStringValue());
				funct.setPath(aDataRow.getColumn("PATH").getStringValue());
				funct.setCode(aDataRow.getColumn("CODE").getStringValue());
				fillRoles(funct);
				return funct;
			}
			
			

			
          
			

		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "LowFunctionalityDAOImpl", "load", "Cannot recover detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 1004);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
		
	}
	
	/**
	 * @see it.eng.spagobi.bo.dao.ILowFunctionalityDAO#loadLowFunctionalityByPath(java.lang.String)
	 */
	public LowFunctionality loadLowFunctionalityByPath(String functionalityPath) throws EMFUserError {
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		ArrayList parameters = new ArrayList(1);
		LowFunctionality funct = new LowFunctionality();
		boolean crypt= false;
		DataConnection dataConnection = null;
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_LOW_FUNCTIONALITY_BY_PATH");
			DataField dataField = dataConnection.createDataField("path", Types.VARCHAR, functionalityPath);
			parameters.add(dataField);
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute(parameters);
			sdr = (ScrollableDataResult) dr.getDataObject();	
			if(!sdr.hasRows()) {
				return null;
			}else{
				sdr.moveTo(1);
				DataRow aDataRow = sdr.getDataRow();
				String idFunct= aDataRow.getColumn("ID").getStringValue();
				funct.setId(Integer.valueOf(idFunct));
				funct.setDescription(aDataRow.getColumn("DESCRIPTION").getStringValue());
				funct.setName(aDataRow.getColumn("NAME").getStringValue());
				funct.setPath(aDataRow.getColumn("PATH").getStringValue());
				funct.setCode(aDataRow.getColumn("CODE").getStringValue());
				fillRoles(funct);
				return funct;
			}	
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "LowFunctionalityDAOImpl", "load", "Cannot recover detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 1004);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
	}
	
	
	
	/**
	 * Sets the roles for a given low functionality.
	 * 
	 * @param aLowFunctionality The input Low Functionality
	 * @throws EMFUserError If any exception occurred
	 */
	public void fillRoles(LowFunctionality aLowFunctionality)
			throws EMFUserError {
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		ArrayList parameters = new ArrayList(1);
		LowFunctionality funct = new LowFunctionality();
		boolean crypt = false;
		DataConnection dataConnection = null;
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements
					.getStatement("SELECT_ROLES_OF_THE_LOW_FUNCTIONALITY");
			DataField dataField = dataConnection.createDataField("id",
					Types.DECIMAL, aLowFunctionality.getId());
			parameters.clear();
			parameters.add(dataField);
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute(parameters);
			sdr = (ScrollableDataResult) dr.getDataObject();

			List execRolesList = new ArrayList();
			List devRolesList = new ArrayList();
			List testRolesList = new ArrayList();

			if (sdr.hasRows()) {
				sdr.moveTo(1);
				while (sdr.hasRows()) {
					DataRow aDataRow = sdr.getDataRow();
					String id_role = aDataRow.getColumn("IDROLE")
							.getStringValue();
					Integer idRole = Integer.valueOf(id_role);
					Role role = DAOFactory.getRoleDAO().loadByID(idRole);
					String state = (String) (aDataRow.getColumn("CDSTATE")
							.getObjectValue());
					if (state.equals("DEV")) {
						devRolesList.add(role);

					}
					if (state.equals("TEST")) {
						testRolesList.add(role);

					}
					if (state.equals("REL")) {
						execRolesList.add(role);

					}
				}
			}

			Role[] execRoles = new Role[execRolesList.size()];
			Role[] devRoles = new Role[devRolesList.size()];
			Role[] testRoles = new Role[testRolesList.size()];

			for (int i = 0; i < execRolesList.size(); i++)
				execRoles[i] = (Role) execRolesList.get(i);
			for (int i = 0; i < testRolesList.size(); i++)
				testRoles[i] = (Role) testRolesList.get(i);
			for (int i = 0; i < devRolesList.size(); i++)
				devRoles[i] = (Role) devRolesList.get(i);

			aLowFunctionality.setDevRoles(devRoles);
			aLowFunctionality.setExecRoles(execRoles);
			aLowFunctionality.setTestRoles(testRoles);
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE,
					"LowFunctionalityDAOImpl", "load",
					"Cannot recover detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 1004);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
	}

	

	/**
	 * @see it.eng.spagobi.bo.dao.ILowFunctionalityDAO#modifyLowFunctionality(it.eng.spagobi.bo.LowFunctionality)
	 */
	public void modifyLowFunctionality(LowFunctionality aLowFunctionality) throws EMFUserError {

		SQLCommand cmdMod = null;
		SQLCommand cmdDel = null;
		SQLCommand cmdIns = null;
 		DataResult dr = null;
		DataConnection dataConnection = null;
		
		try {
			
			dataConnection = getConnection("spagobi");
			dataConnection.initTransaction();
			String strSql = SQLStatements.getStatement("UPDATE_LOWFUNCTIONALITY");
			ArrayList parameters = new ArrayList(4);
			parameters.add(dataConnection.createDataField("NAME", Types.VARCHAR, aLowFunctionality.getName()));
			parameters.add(dataConnection.createDataField("DESCR", Types.VARCHAR, aLowFunctionality.getDescription()));
			parameters.add(dataConnection.createDataField("CODE", Types.VARCHAR, aLowFunctionality.getCode()));
			parameters.add(dataConnection.createDataField("FUNCT_ID", Types.DECIMAL, aLowFunctionality.getId()));
			cmdMod = dataConnection.createUpdateCommand(strSql);
			dr = cmdMod.execute(parameters);
			strSql = SQLStatements.getStatement("DELETE_ROLES_FUNCTIONALITY");
			parameters = new ArrayList(1);
			parameters.add(dataConnection.createDataField("FUNCT_ID", Types.DECIMAL, aLowFunctionality.getId()));
			cmdDel = dataConnection.createDeleteCommand(strSql);
			cmdDel.execute(parameters);
			
			strSql = SQLStatements.getStatement("INSERT_LOWFUNCTIONALITY_ROLES");
			Role[] devRoles = aLowFunctionality.getDevRoles();
			insertRolesFunctionality(dataConnection, aLowFunctionality, strSql, devRoles, "DEV");
			Role[] execRoles = aLowFunctionality.getExecRoles();
			insertRolesFunctionality(dataConnection, aLowFunctionality, strSql, execRoles, "REL");
			Role[] testRoles = aLowFunctionality.getTestRoles();	
			insertRolesFunctionality(dataConnection, aLowFunctionality, strSql, testRoles, "TEST");
						
			dataConnection.commitTransaction();
			
		}  catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAOImpl", "modify", "Cannot update detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 1003);
		} finally {
			Utils.releaseResources(dataConnection, cmdMod, dr);
		}
		
	}

	/**
	 * It is the implementation code for a DB insertion. All row inserted match a functionality 
	 * with its roles and states.
	 * 
	 * @param dataConnection The Data Connection object
	 * @param aLowFunctionality The low functionality object
	 * @param strSql The SQL code string
	 * @param roles	The list of all roles associated
	 * @param state The String defining the state
	 * @throws Exception If any exception occurred
	 */
	private void insertRolesFunctionality(DataConnection dataConnection, LowFunctionality aLowFunctionality,
										  String strSql, Role[] roles, String state) throws Exception {
		DataResult dr = null;
		SQLCommand cmdIns = null;
		try {
			Domain domState = DAOFactory.getDomainDAO().loadDomainByCodeAndValue("STATE", state);
			cmdIns = dataConnection.createInsertCommand(strSql);
			ArrayList parameters = null;
			Role role = null;
			for(int i=0; i<roles.length; i++) {
				parameters = new ArrayList(4);
				role = roles[i];
				parameters.add(dataConnection.createDataField("ROLE_ID", Types.DECIMAL, role.getId()));
				parameters.add(dataConnection.createDataField("FUNCT_ID", Types.DECIMAL, aLowFunctionality.getId()));
				parameters.add(dataConnection.createDataField("STATE_ID", Types.DECIMAL, domState.getValueId()));
				parameters.add(dataConnection.createDataField("STATE_CD", Types.VARCHAR, state));
				dr = cmdIns.execute(parameters); 				
			}
		} catch (Exception e) {
			Utils.releaseResources(dataConnection, cmdIns, dr);
			throw e;
		}
	}
	
	
	/**
	 * @see it.eng.spagobi.bo.dao.ILowFunctionalityDAO#insertLowFunctionality(it.eng.spagobi.bo.LowFunctionality,it.eng.spago.security.IEngUserProfile) )
	 */
	public void insertLowFunctionality(LowFunctionality aLowFunctionality, IEngUserProfile profile) throws EMFUserError {
		SQLCommand cmdIns = null;
		SQLCommand cmdSel = null;
		DataResult dr = null;
		DataConnection dataConnection = null;
		CMSConnection connection = null;
		
		try {
			
			dataConnection = getConnection("spagobi");
			dataConnection.initTransaction();
			String strSql = SQLStatements.getStatement("INSERT_LOWFUNCTIONALITY");
			ArrayList parameters = new ArrayList(6);
			parameters.add(dataConnection.createDataField("NAME", Types.VARCHAR, aLowFunctionality.getName()));
			parameters.add(dataConnection.createDataField("DESCR", Types.VARCHAR, aLowFunctionality.getDescription()));
			parameters.add(dataConnection.createDataField("PATH", Types.VARCHAR, aLowFunctionality.getPath()));
			Domain typeLow = DAOFactory.getDomainDAO().loadDomainByCodeAndValue("FUNCT_TYPE", "LOW_FUNCT");
			parameters.add(dataConnection.createDataField("FUNCT_TYPE_ID", Types.DECIMAL, new BigDecimal(typeLow.getValueId().toString())));		
			parameters.add(dataConnection.createDataField("FUNCT_TYPE_CODE", Types.VARCHAR, "LOW_FUNCT"));
			parameters.add(dataConnection.createDataField("CODE", Types.VARCHAR, aLowFunctionality.getCode()));
			cmdIns = dataConnection.createInsertCommand(strSql);
			dr = cmdIns.execute(parameters);
			strSql = SQLStatements.getStatement("SELECT_LOW_FUNCTIONALITY_BY_PATH");
			parameters = new ArrayList(1);
			parameters.add(dataConnection.createDataField("PATH", Types.VARCHAR, aLowFunctionality.getPath()));
			cmdSel = dataConnection.createSelectCommand(strSql);
			dr = cmdSel.execute(parameters);
			ScrollableDataResult sdr = (ScrollableDataResult) dr.getDataObject();
			ResultSet rs = sdr.getResultSet();
			BigDecimal functID =  rs.getBigDecimal("ID");
			aLowFunctionality.setId(new Integer(functID.intValue()));
			
			strSql = SQLStatements.getStatement("INSERT_LOWFUNCTIONALITY_ROLES");
			cmdIns = dataConnection.createInsertCommand(strSql);
			Role[] devRoles = aLowFunctionality.getDevRoles();
			insertRolesFunctionality(dataConnection, aLowFunctionality, strSql, devRoles, "DEV");
			Role[] execRoles = aLowFunctionality.getExecRoles();
			insertRolesFunctionality(dataConnection, aLowFunctionality, strSql, execRoles, "REL");
			Role[] testRoles = aLowFunctionality.getTestRoles();	
			insertRolesFunctionality(dataConnection, aLowFunctionality, strSql, testRoles, "TEST");
			
			
			OperationExecutor executor = OperationExecutorManager.getOperationExecutor();
			connection = CMSManager.getInstance().getConnection();
			SetOperation set = OperationBuilder.buildSetOperation();
			set.setPath(aLowFunctionality.getPath());
			set.setType(SetOperation.TYPE_CONTAINER);
			set.setEraseOldProperties(true);
			String [] typePropValues = new String[] {AdmintoolsConstants.LOW_FUNCTIONALITY_TYPE};
			set.setStringProperty(AdmintoolsConstants.NODE_CMS_TYPE, typePropValues);
			executor.setObject(connection, set, profile, true);
			
			dataConnection.commitTransaction();
			
		}  catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAOImpl", "insert", "Cannot insert detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 1002);
		} finally {
			if(connection != null) {
				if(!connection.isClose()) {
					connection.close();
				}
			}
			Utils.releaseResources(dataConnection, cmdIns, dr);
		}
		
	}

	/**
	 * @see it.eng.spagobi.bo.dao.ILowFunctionalityDAO#eraseLowFunctionality(it.eng.spagobi.bo.LowFunctionality, it.eng.spago.security.IEngUserProfile)
	 */
	public void eraseLowFunctionality(LowFunctionality aLowFunctionality, IEngUserProfile profile) throws EMFUserError {
		SQLCommand cmdDel = null;
		DataResult dr = null;
		DataConnection dataConnection = null;
		CMSConnection connection = null;
		
		try {
		
			if(hasChild(aLowFunctionality.getPath())) {
				HashMap params = new HashMap();
				params.put(AdmintoolsConstants.PAGE, TreeObjectsModule.MODULE_PAGE);
				params.put(SpagoBIConstants.ACTOR, SpagoBIConstants.ADMIN_ACTOR);
				params.put(SpagoBIConstants.OPERATION, SpagoBIConstants.FUNCTIONALITIES_OPERATION);
				throw new EMFUserError(EMFErrorSeverity.ERROR, 1000, new Vector(), params);
			}
			
			OperationExecutor executor = OperationExecutorManager.getOperationExecutor();
			connection = CMSManager.getInstance().getConnection();

			dataConnection = getConnection("spagobi");
			dataConnection.initTransaction();
			String strSql = SQLStatements.getStatement("DELETE_ROLES_FUNCTIONALITY");
			ArrayList parameters = new ArrayList(1);
			parameters.add(dataConnection.createDataField("FUNCT_ID", Types.DECIMAL, aLowFunctionality.getId()));
			cmdDel = dataConnection.createDeleteCommand(strSql);
			dr = cmdDel.execute(parameters);
			
			strSql = SQLStatements.getStatement("DELETE_FUNCTIONALITY");
			cmdDel = dataConnection.createDeleteCommand(strSql);
			dr = cmdDel.execute(parameters);
			
			DeleteOperation del = OperationBuilder.buildDeleteOperation();
			del.setPath(aLowFunctionality.getPath());
			executor.deleteObject(connection, del, profile, true);
			
			dataConnection.commitTransaction();
			
		} catch (EMFUserError emfue) {
			throw emfue;
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "LowFunctionalityDAO", "erase", "Cannot erase the functioanlity", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 1001);
		} finally {
			if(connection != null) {
				if(!connection.isClose()) {
					connection.close();
				}
			}
			Utils.releaseResources(dataConnection, cmdDel, dr);
		}
		
	}

	/**
	 * @see it.eng.spagobi.bo.dao.ILowFunctionalityDAO#existByCode(java.lang.String)
	 * 
	 */
	public Integer existByCode(String code) throws EMFUserError {
		Integer id = null;
		SQLCommand cmdIns = null;
		SQLCommand cmdSel = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		DataConnection dataConnection = null;
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_FUNCTIONALITY_FROM_CODE");
			ArrayList parameters = new ArrayList(1);
			parameters.add(dataConnection.createDataField("CODE", Types.VARCHAR, code));
			cmdIns = dataConnection.createSelectCommand(strSql);
			dr = cmdIns.execute(parameters);
			sdr = (ScrollableDataResult)dr.getDataObject();
			if(sdr.hasRows()) {
				id = new Integer(sdr.getDataRow(0).getColumn("ID").getObjectValue().toString());
			}
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
								"LowFunctionalityDAOImpl", 
								"existByCode", 
								"Error during recorer of the functionality by code", e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 1003);
		} finally {
			Utils.releaseResources(dataConnection, cmdSel, dr);
		}
		return id;
	}

	/**
	 * @see it.eng.spagobi.bo.dao.ILowFunctionalityDAO#hasChild(java.lang.String)
	 * 
	 */
	public boolean hasChild(String path) throws EMFUserError {
		CMSConnection connection = null;
		try{
			RequestContainer reqCont = RequestContainer.getRequestContainer();
			SessionContainer sessCont = reqCont.getSessionContainer();
			SessionContainer permSess = sessCont.getPermanentContainer();
			IEngUserProfile profile = (IEngUserProfile)permSess.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
			OperationExecutor executor = OperationExecutorManager.getOperationExecutor();
			connection = CMSManager.getInstance().getConnection();
			GetOperation get = OperationBuilder.buildGetOperation();
			get.setPath(path);
			ElementDescriptor desc = executor.getObject(connection, get, profile, true);
			if(desc.hasChilds()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			if(connection!=null){
				if(!connection.isClose()) {
					connection.close();
				}
			}
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
					"LowFunctionalityDAOImpl", 
					"existByCode", 
					"Error during recorer of the functionality by code", e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 1003);
		}
	}

	
	


	
	
	
	
	

}
