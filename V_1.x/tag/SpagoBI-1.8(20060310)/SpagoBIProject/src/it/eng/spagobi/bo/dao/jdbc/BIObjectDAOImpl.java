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
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.Engine;
import it.eng.spagobi.bo.Parameter;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectDAO;
import it.eng.spagobi.bo.dao.IParameterDAO;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *	Defines the JDBC implementations for all DAO methods,
 *  for a BI Object.  
 * 
 * @author zoppello
 */
public class BIObjectDAOImpl extends AbstractJdbcDAO implements IBIObjectDAO {
	
	

	/**
	 * @see it.eng.spagobi.bo.dao.IBIObjectDAO#loadBIObjectForExecutionByPathAndRole(java.lang.String, java.lang.String)
	 */
	public BIObject loadBIObjectForExecutionByPathAndRole(String path, String role) throws EMFUserError {
		BIObject biObject = (BIObject) loadBIObjectByPath(path);
		biObject.setBiObjectParameters(loadBIObjectParametersForBIObject(biObject, role));
		return biObject;
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IBIObjectDAO#loadBIObjectForDetail(java.lang.Integer)
	 */
	public BIObject loadBIObjectForDetail(Integer biObjectID) throws EMFUserError {
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		ArrayList parameters = new ArrayList(1);
		BIObject biObject = new BIObject();

		DataConnection dataConnection = null;
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_BIOBJ");
			DataField dataField = dataConnection.createDataField("BIOBJD_ID",
					Types.NUMERIC, biObjectID);
			parameters.add(dataField);
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute(parameters);
			sdr = (ScrollableDataResult) dr.getDataObject();
			if (sdr.hasRows()) {
				sdr.moveTo(1);
				DataRow aDataRow = sdr.getDataRow();
				/*
				 * select biobj_id, engine_id, descr, label, encrypt, rel_name,
				 * state_id, state_cd, biobj_type_cd, biobj_type_id from
				 * sbi_objects where biobj_id = ?
				 */
				String biObjectIDStr = aDataRow.getColumn("biobj_id").getStringValue();
				Integer  biObjectIDInt = Integer.valueOf(biObjectIDStr);
				biObject.setId(biObjectIDInt);
				biObject.setDescription(aDataRow.getColumn("descr").getStringValue());
				biObject.setLabel(aDataRow.getColumn("label").getStringValue());
				String encryptStr = aDataRow.getColumn("encrypt").getStringValue();
				biObject.setEncrypt(new Integer(encryptStr));
				biObject.setRelName(aDataRow.getColumn("rel_name").getStringValue());
				String stateIDStr = aDataRow.getColumn("state_id").getStringValue();
				biObject.setStateID(new Integer(stateIDStr));
				biObject.setStateCode(aDataRow.getColumn("state_cd").getStringValue());
				String biObjectTtpeIDStr = aDataRow.getColumn("biobj_type_id").getStringValue();
				Integer biObjectTypeIDInt = Integer.valueOf(biObjectTtpeIDStr);
				biObject.setBiObjectTypeID(biObjectTypeIDInt);
				biObject.setBiObjectTypeCode(aDataRow.getColumn("state_cd").getStringValue());
				biObject.setPath(aDataRow.getColumn("path").getStringValue());
				// Load Engine For specified ID
				String engine_Id = aDataRow.getColumn("engine_id").getStringValue();
				Integer engineIdInt = Integer.valueOf(engine_Id);
				Engine engine = DAOFactory.getEngineDAO().loadEngineByID(engineIdInt);
				biObject.setEngine(engine);
				return biObject;
			} else {
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE,
						"BIObjectDAOImpl", "load",
						"Cannot recover detail information");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
			}
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "BIObjectDAOImpl",
					"load", "Cannot recover detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
	}


	
	
	/**
	 * @see it.eng.spagobi.bo.dao.IBIObjectDAO#loadBIObjectForDetail(java.lang.Integer)
	 */
	
	public BIObject loadBIObjectForDetail(String path) throws EMFUserError {
		/*
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		ArrayList parameters = new ArrayList(1);
		BIObject biObject = new BIObject();
		DataConnection dataConnection = null;
		CMSConnection connection = null;
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_BIOBJ_BYPATH");
			path = path.toUpperCase();
			DataField dataField = dataConnection.createDataField("BIOBJD_PATH", Types.VARCHAR, path);
			parameters.add(dataField);
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute(parameters);
			sdr = (ScrollableDataResult) dr.getDataObject();
			if (sdr.hasRows()) {
				sdr.moveTo(1);
				DataRow aDataRow = sdr.getDataRow();
				/*
				 * select biobj_id, engine_id, descr, label, encrypt, rel_name,
				 * state_id, state_cd, biobj_type_cd, biobj_type_id from
				 * sbi_objects where biobj_id = ?
				 
				String biObjectIDStr = aDataRow.getColumn("biobj_id").getStringValue();
				Integer biObjectIDInt = Integer.valueOf(biObjectIDStr);
				biObject.setId(biObjectIDInt);
				biObject.setDescription(aDataRow.getColumn("descr").getStringValue());
				biObject.setLabel(aDataRow.getColumn("label").getStringValue());
				String encryptStr = aDataRow.getColumn("encrypt").getStringValue();
				biObject.setEncrypt(new Integer(encryptStr));
				biObject.setRelName(aDataRow.getColumn("rel_name").getStringValue());
				String stateID = aDataRow.getColumn("state_id").getStringValue();
				biObject.setStateID(new Integer(stateID));
				biObject.setStateCode(aDataRow.getColumn("state_cd").getStringValue());
				String bIObjectTypeIDStr = aDataRow.getColumn("biobj_type_id").getStringValue();
				Integer bIObjectTypeID = Integer.valueOf(bIObjectTypeIDStr); 
				biObject.setBiObjectTypeID(bIObjectTypeID);
				biObject.setBiObjectTypeCode(aDataRow.getColumn("state_cd").getStringValue());
				biObject.setPath(aDataRow.getColumn("path").getStringValue());
				// Load Engine For specified ID
				String engine_Id = aDataRow.getColumn("engine_id").getStringValue();
				Integer engineIdInt = Integer.valueOf(engine_Id);
				Engine engine = DAOFactory.getEngineDAO().loadEngineByID(engineIdInt);
				biObject.setEngine(engine);
				
				
				RequestContainer requestContainer =  RequestContainer.getRequestContainer();
				SessionContainer session = requestContainer.getSessionContainer();
				SessionContainer permSession = session.getPermanentContainer();
				IEngUserProfile profile = (IEngUserProfile)permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
				
				
				OperationExecutor executor = OperationExecutorManager.getOperationExecutor();
				connection = CMSManager.getInstance().getConnection();
				GetOperation get = OperationBuilder.buildGetOperation();
				get.setPath(biObject.getPath() + "/template" );
				get.setRetriveContentInformation("true");
				get.setRetrivePropertiesInformation("true");
				get.setRetriveVersionsInformation("true");
				ElementDescriptor desc = executor.getObject(connection, get, profile, true);
				SourceBean descSB = desc.getDescriptor();
				if (descSB != null){
					connection = CMSManager.getInstance().getConnection();
					String currentVerStr = (String)descSB.getAttribute("VERSION");
					List versions = descSB.getAttributeAsList("VERSIONS.VERSION");
					Iterator iterVer = versions.iterator();
					ArrayList templates = new ArrayList();
					TemplateVersion currentVer = null;
					while(iterVer.hasNext()) {
						SourceBean ver = (SourceBean)iterVer.next();
						String nameVer = (String)ver.getAttribute("name");
						String dateVer = (String)ver.getAttribute("dataCreation");
						get.setVersion(nameVer);
						desc = executor.getObject(connection, get, profile, false);
						ElementProperty fileNameProp = desc.getNamedElementProperties("fileName")[0];
						String nameFile = fileNameProp.getStringValues()[0];
						TemplateVersion tempVer = new TemplateVersion();
						tempVer.setDataLoad(dateVer);
						tempVer.setVersionName(nameVer);
						tempVer.setNameFileTemplate(nameFile);
						templates.add(tempVer);
						if(nameVer.equalsIgnoreCase(currentVerStr)) {
							currentVer = tempVer;
						}
					}
					connection.close();
					biObject.setTemplateVersions(templates);
					biObject.setCurrentTemplateVersion(currentVer);
				}else{
					biObject.setTemplateVersions(new ArrayList());
					TemplateVersion tv = new TemplateVersion();
					tv.setVersionName("");
					biObject.setCurrentTemplateVersion(tv);
				}
				return biObject;
			} else {
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE,
						"BIObjectDAOImpl", "loadByPath",
						"Cannot recover detail information");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
			}
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "BIObjectDAOImpl",
					"loadByPath", "Cannot recover detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if(connection != null) {
				if(!connection.isClose()) {
					connection.close();
				}
			}
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
		*/
		return null;
	}
	
	
	/**
	 * @see it.eng.spagobi.bo.dao.IBIObjectDAO#loadBIObjectForTree(java.lang.String)
	 */
	
	public BIObject loadBIObjectForTree(String path) throws EMFUserError {
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		ArrayList parameters = new ArrayList(1);
		BIObject biObject = new BIObject();
		DataConnection dataConnection = null;
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_BIOBJ_BYPATH");
			path = path.toUpperCase();
			DataField dataField = dataConnection.createDataField("BIOBJD_PATH", Types.VARCHAR, path);
			parameters.add(dataField);
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute(parameters);
			sdr = (ScrollableDataResult) dr.getDataObject();
			if (sdr.hasRows()) {
				sdr.moveTo(1);
				DataRow aDataRow = sdr.getDataRow();
				String biObjectIDStr = aDataRow.getColumn("biobj_id").getStringValue();
				Integer biObjectIDInt = Integer.valueOf(biObjectIDStr);
				biObject.setId(biObjectIDInt);
				biObject.setDescription(aDataRow.getColumn("descr").getStringValue());
				biObject.setLabel(aDataRow.getColumn("label").getStringValue());
				String encryptStr = aDataRow.getColumn("encrypt").getStringValue();
				biObject.setEncrypt(new Integer(encryptStr));
				biObject.setRelName(aDataRow.getColumn("rel_name").getStringValue());
				String stateID = aDataRow.getColumn("state_id").getStringValue();
				biObject.setStateID(new Integer(stateID));
				biObject.setStateCode(aDataRow.getColumn("state_cd").getStringValue());
				String bIObjectTypeIDStr = aDataRow.getColumn("biobj_type_id").getStringValue();
				Integer bIObjectTypeID = Integer.valueOf(bIObjectTypeIDStr); 
				biObject.setBiObjectTypeID(bIObjectTypeID);
				biObject.setBiObjectTypeCode(aDataRow.getColumn("state_cd").getStringValue());
				biObject.setPath(aDataRow.getColumn("path").getStringValue());
				return biObject;
			} else {
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE,
						"BIObjectDAOImpl", "loadBIObjectForTree",
						"Cannot recover detail information");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
			}
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "BIObjectDAOImpl",
					"loadBIObjectForTree", "Cannot recover detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
	}
	
	
	
	
	
	/**
	 * @see it.eng.spagobi.bo.dao.IBIObjectDAO#loadBIObjectByPath(java.lang.String)
	 */
	private BIObject loadBIObjectByPath(String path) throws EMFUserError {
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		ArrayList parameters = new ArrayList(1);
		BIObject biObject = new BIObject();

		DataConnection dataConnection = null;
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_BIOBJ_BYPATH");
			//TODO modify toUpper code
			path = path.toUpperCase();
			DataField dataField = dataConnection.createDataField("BIOBJD_PATH",
			Types.VARCHAR, path);
			parameters.add(dataField);
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute(parameters);
			sdr = (ScrollableDataResult) dr.getDataObject();
			if (sdr.hasRows()) {
				sdr.moveTo(1);
				DataRow aDataRow = sdr.getDataRow();
				/*
				 * select biobj_id, engine_id, descr, label, encrypt, rel_name,
				 * state_id, state_cd, biobj_type_cd, biobj_type_id from
				 * sbi_objects where biobj_id = ?
				 */
				String biObjectIDStr = aDataRow.getColumn("biobj_id").getStringValue();
				Integer biObjectIDInt = Integer.valueOf(biObjectIDStr);
				
				biObject.setId(biObjectIDInt);
				
				biObject.setDescription(aDataRow.getColumn("descr")
						.getStringValue());
				biObject.setLabel(aDataRow.getColumn("label").getStringValue());
				String encryptStr = aDataRow.getColumn("encrypt").getStringValue();
				biObject.setEncrypt(new Integer(encryptStr));
			
				biObject.setRelName(aDataRow.getColumn("rel_name").getStringValue());
				String stateID = aDataRow.getColumn("state_id").getStringValue();
				biObject.setStateID(new Integer(stateID));
				biObject.setStateCode(aDataRow.getColumn("state_cd").getStringValue());
				String bIObjectTypeIDStr = aDataRow.getColumn("biobj_type_id").getStringValue();
				Integer bIObjectTypeID = Integer.valueOf(bIObjectTypeIDStr); 
				biObject.setBiObjectTypeID(bIObjectTypeID);
				biObject.setBiObjectTypeCode(aDataRow.getColumn(
						"state_cd").getStringValue());
				biObject.setPath(aDataRow.getColumn(
				"path").getStringValue());
				// Load Engine For specified ID
				String engine_Id = aDataRow.getColumn("engine_id").getStringValue();
				Integer engineIdInt = Integer.valueOf(engine_Id);
				Engine engine = DAOFactory.getEngineDAO().loadEngineByID(engineIdInt);
				biObject.setEngine(engine);
				
				return biObject;
			} else {
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE,
						"BIObjectDAOImpl", "loadByPath",
						"Cannot recover detail information");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
			}
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "BIObjectDAOImpl",
					"loadByPath", "Cannot recover detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IBIObjectDAO#modifyBIObject(it.eng.spagobi.bo.BIObject)
	 */
	public void modifyBIObject(BIObject obj) throws EMFUserError {
		internalModify(obj, true);
		
	}
	
	/**
	 * @see it.eng.spagobi.bo.dao.IBIObjectDAO#modifyBIObjectWithoutVersioning(it.eng.spagobi.bo.BIObject)
	 */
	public void modifyBIObjectWithoutVersioning(BIObject obj) throws EMFUserError {
		internalModify(obj, false);
		
	}
	
	/**
	 * @see it.eng.spagobi.bo.dao.IBIObjectDAO#insertBIObject(it.eng.spagobi.bo.BIObject)
	 */
	public void insertBIObject(BIObject obj) throws EMFUserError {
		/*
		SQLCommand cmdIns = null;
		DataResult dr = null;
		DataConnection dataConnection = null;
		BIObject biObject = null;
		CMSConnection connection = null;
		try {
			biObject = (BIObject)obj;
			dataConnection = getConnection("spagobi");
			dataConnection.initTransaction();
			String strSql = SQLStatements.getStatement("INSERT_BIOBJ");
			ArrayList parameters = new ArrayList(10);			 
			parameters.add(dataConnection.createDataField("engine_id", Types.NUMERIC,biObject.getEngine().getId())); 
			parameters.add(dataConnection.createDataField("descr,", Types.VARCHAR, biObject.getDescription()));
			parameters.add(dataConnection.createDataField("label", Types.VARCHAR, biObject.getLabel()));
			parameters.add(dataConnection.createDataField("encrypt",Types.NUMERIC,biObject.getEncrypt())); 
			parameters.add(dataConnection.createDataField("rel_name",Types.VARCHAR,biObject.getRelName())); 
			parameters.add(dataConnection.createDataField("state_id",Types.NUMERIC,biObject.getStateID())); 
			parameters.add(dataConnection.createDataField("state_cd",Types.VARCHAR,biObject.getStateCode()));
			parameters.add(dataConnection.createDataField("biobj_type_cd", Types.VARCHAR,biObject.getBiObjectTypeCode()));
			parameters.add(dataConnection.createDataField("biobj_type_id",Types.NUMERIC,biObject.getBiObjectTypeID()));
			parameters.add(dataConnection.createDataField("path", Types.VARCHAR,biObject.getPath()));
			
			cmdIns = dataConnection.createInsertCommand(strSql);
			dr = cmdIns.execute(parameters);
			
			RequestContainer requestContainer =  RequestContainer.getRequestContainer();
			SessionContainer session = requestContainer.getSessionContainer();
			SessionContainer permSession = session.getPermanentContainer();
			IEngUserProfile profile = (IEngUserProfile)permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
			
			OperationExecutor executor = OperationExecutorManager.getOperationExecutor();
			connection = CMSManager.getInstance().getConnection();
			SetOperation set = OperationBuilder.buildSetOperation();
			set.setPath(biObject.getPath());
			set.setType(SetOperation.TYPE_CONTAINER);
			set.setEraseOldProperties(true);
			String [] typePropValues = new String[] {biObject.getBiObjectTypeCode()};
			set.setStringProperty(AdmintoolsConstants.NODE_CMS_TYPE, typePropValues);
			executor.setObject(connection, set, profile, true);
			// Set the report template
			if(biObject.getTemplate().getFileContent().length > 0) {
				connection = CMSManager.getInstance().getConnection();
				set = OperationBuilder.buildSetOperation();
				set.setContent(new ByteArrayInputStream(biObject.getTemplate().getFileContent()));
				set.setType(SetOperation.TYPE_CONTENT);
				set.setPath(biObject.getPath() + "/template");
				String[] nameFilePropValues = new String[] { biObject.getTemplate().getFileName() };
				set.setStringProperty("fileName", nameFilePropValues);
			    String today = new Date().toString();
			    String[] datePropValues = new String[] { today };
			    set.setStringProperty("dateLoad", datePropValues);
				executor.setObject(connection, set, profile, true);
			}
			dataConnection.commitTransaction();
			// Clear bytes in memory
			biObject.setTemplate(null);
			
		}  catch (Exception ex) {
			try{
				dataConnection.rollBackTransaction();
			} catch (Exception e) {
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "BIObjectDAOImpl", "insert", "Transaction rollback failled", ex);
			}
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "BIObjectDAOImpl", "insert", "Cannot insert detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if(connection != null) {
				if(!connection.isClose()) {
					connection.close();
				}
			}
			Utils.releaseResources(dataConnection, cmdIns, dr);
		}
		*/
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IBIObjectDAO#eraseBIObject(it.eng.spagobi.bo.BIObject)
	 */
	public void eraseBIObject(BIObject obj) throws EMFUserError {
		/*
		SQLCommand cmdDel = null;
		DataResult dr = null;
		DataConnection dataConnection = null;
		BIObject biObject = null;
		CMSConnection connection = null;
		try {
			biObject = (BIObject)obj;
			dataConnection = getConnection("spagobi");
			dataConnection.initTransaction();
			String strSql = "";
			
			ArrayList parameters = new ArrayList(1);
			parameters.add(dataConnection.createDataField("biobj_id", Types.DECIMAL,biObject.getId()));
			
			// delete parameters of the report 
			strSql = SQLStatements.getStatement("DELETE_BIOBJ_BIPARAMETERS");
			cmdDel = dataConnection.createDeleteCommand(strSql);
			dr = cmdDel.execute(parameters);
			
			// delete biobject
			strSql = SQLStatements.getStatement("DELETE_BIOBJ");
			cmdDel = dataConnection.createDeleteCommand(strSql);
			dr = cmdDel.execute(parameters);
				
			// get profile user
			RequestContainer requestContainer =  RequestContainer.getRequestContainer();
			SessionContainer session = requestContainer.getSessionContainer();
			SessionContainer permSession = session.getPermanentContainer();
			IEngUserProfile profile = (IEngUserProfile)permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
			
			// erase from cms
			OperationExecutor executor = OperationExecutorManager.getOperationExecutor();
			connection = CMSManager.getInstance().getConnection();
			DeleteOperation del = OperationBuilder.buildDeleteOperation();
            del.setPath(biObject.getPath());
            executor.deleteObject(connection, del, profile, true);
			
            // commit transaction
            dataConnection.commitTransaction();
		
		}  catch (Exception ex) {
			try{
				dataConnection.rollBackTransaction();
			} catch (Exception e) {
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "BIObjectDAOImpl", "eraseBIObject", "Transaction rollback failled", ex);
			}
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "BIObjectDAOImpl", "eraseBIObject", "Cannot erase object", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if(connection != null) {
				if(!connection.isClose()) {
					connection.close();
				}
			}
			Utils.releaseResources(dataConnection, cmdDel, dr);
		}
		*/
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IBIObjectDAO#fillBIObjectTemplate(it.eng.spagobi.bo.BIObject)
	 */
	public void fillBIObjectTemplate(BIObject obj) {
		/*
		CMSConnection connection = null;
		try{
			RequestContainer requestContainer =  RequestContainer.getRequestContainer();
			SessionContainer session = requestContainer.getSessionContainer();
			SessionContainer permSession = session.getPermanentContainer();
			IEngUserProfile profile = (IEngUserProfile)permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
			OperationExecutor executor = OperationExecutorManager.getOperationExecutor();
			connection = CMSManager.getInstance().getConnection();
			GetOperation get = OperationBuilder.buildGetOperation();
			get.setPath(obj.getPath() + "/template" );
			get.setRetriveContentInformation("true");
			get.setRetrivePropertiesInformation("true");
			get.setRetriveVersionsInformation("true");
			ElementDescriptor desc = executor.getObject(connection, get, profile, true);
			ElementProperty nameProp = desc.getNamedElementProperties("fileName")[0];
			String fileName = nameProp.getStringValues()[0];
			SourceBean descSB = desc.getDescriptor();
			InputStream instr = (InputStream)descSB.getAttribute("CONTENT.STREAM");
			byte[] content = GeneralUtilities.getByteArrayFromInputStream(instr);
            UploadedFile template = new UploadedFile();
	        template.setFileContent(content);
            template.setFileName(fileName);
            obj.setTemplate(template);
            
		} catch (Exception e) {
			if(connection != null) {
				if(!connection.isClose()) {
					connection.close();
				}
			}
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, 
					            "BIObjectDAOImpl", 
					            "fillBIObjectTemplate", 
					            "Cannot fill the response", e);
		}
		*/
	}
	

	
	/**
	 * Loads all the BI object parameters referring to a given BI Object, according to the 
	 * particular role.
	 * 
	 * @param biObject The input BI Object
	 * @param role A string defining the role
	 * @return A list of all BI Object parameters associated
	 * @throws EMFUserError If any exception occurs
	 */
	public List loadBIObjectParametersForBIObject(BIObject biObject, String role) throws EMFUserError {
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		ArrayList parameters = new ArrayList(1);
		List biObjectParameters = new ArrayList();
		
		DataConnection dataConnection = null;
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_ALL_BI_OBJECT_PARAMETERS_FOR_BI_OBJECT");
			
			DataField dataField = dataConnection.createDataField("BIOBJD_ID",
					Types.NUMERIC, biObject.getId());
			
			parameters.add(dataField);
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute(parameters);
			sdr = (ScrollableDataResult) dr.getDataObject();
			
			DataRow aDataRow = null;
			BIObjectParameter aBIObjectParameter = null;
			if (sdr.hasRows()){
				sdr.moveTo(1);
				while (sdr.hasRows()){
					aDataRow = sdr.getDataRow();
					aBIObjectParameter = new BIObjectParameter();
					// par_id, biobj_id, label, req_fl, mod_fl, view_fl, mult_fl, prog, parurl_nm
					aBIObjectParameter.setLabel(aDataRow.getColumn("label")
							.getStringValue());
					String requiredStr = aDataRow.getColumn("req_fl").getStringValue();
					aBIObjectParameter.setRequired(new Integer(requiredStr));
					//aBIObjectParameter.setRequired((BigDecimal) (aDataRow.getColumn("req_fl").getObjectValue()));
					String modifiableStr = aDataRow.getColumn("mod_fl").getStringValue();
					aBIObjectParameter.setModifiable(new Integer(modifiableStr));
					//aBIObjectParameter.setModifiable((BigDecimal) (aDataRow.getColumn("mod_fl").getObjectValue()));
					String visibleStr = aDataRow.getColumn("view_fl").getStringValue();
					aBIObjectParameter.setVisible(new Integer(visibleStr));
					//aBIObjectParameter.setVisible((BigDecimal) (aDataRow.getColumn("view_fl").getObjectValue()));
					String multivalueStr = aDataRow.getColumn("mult_fl").getStringValue();
					aBIObjectParameter.setMultivalue(new Integer(multivalueStr));
					//aBIObjectParameter.setMultivalue((BigDecimal) (aDataRow.getColumn("mult_fl").getObjectValue()));
					aBIObjectParameter.setParameterUrlName(aDataRow.getColumn("parurl_nm")
							.getStringValue());
					String biObjectIDStr = aDataRow.getColumn("biobj_id").getStringValue();
					
					Integer biObjectIDInt = Integer.valueOf(biObjectIDStr);
					
					aBIObjectParameter.setBiObjectID(biObjectIDInt);
					
					
					String  parameterID = aDataRow.getColumn("par_id").getStringValue();
					
					Integer parID = Integer.valueOf(parameterID);
					aBIObjectParameter.setParID(parID);
					/*
					Parameter par = DAOFactory.getParameterDAO().loadForExecutionByParameterIDandRoleName(parID, role);
					
					
					aBIObjectParameter.setParameter(par);
					*/
					
					biObjectParameters.add(aBIObjectParameter);
				}//while (sdr.hasRows())
			}//if (sdr.hasRows())
		} catch (Exception ex) {
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "BIObjectDAOImpl", "loadBIObjectParametersForBIObject", "Cannot select all roles", ex);
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
				Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
		
		Iterator it = biObjectParameters.iterator();
		BIObjectParameter localBIObjectParameter = null;
		Parameter aParameter = null;
		IParameterDAO aParameterDAO = DAOFactory.getParameterDAO(); 
		while (it.hasNext()){
			localBIObjectParameter = (BIObjectParameter)it.next();
			aParameter = aParameterDAO.loadForExecutionByParameterIDandRoleName(localBIObjectParameter.getParID(),role);
			localBIObjectParameter.setParameter(aParameter);
		}
		return biObjectParameters;
	
	}
	/**
	 * Updates the CMS repository if the user has loaded a file template. If he has,
	 * the boolean input parameter <code>version</code> is true and the CNS is uploaded,
	 * else not. This is the JDBC version for this method.
	 * 
	 * @param obj A generic input object
	 * @param version The boolean input parameter
	 * @throws EMFUserError If any exception occurred
	 */
	private void internalModify(Object obj, boolean version) throws EMFUserError {
		/*
		SQLCommand cmdUpd = null;
		DataResult dr = null;
		DataConnection dataConnection = null;
		BIObject biObject = null;
		CMSConnection connection = null;
		try {
			biObject = (BIObject)obj;
			dataConnection = getConnection("spagobi");
			dataConnection.initTransaction();
			String strSql = SQLStatements.getStatement("UPDATE_BIOBJ");
			ArrayList parameters = new ArrayList(11);
			parameters.add(dataConnection.createDataField("engine_id", Types.NUMERIC,biObject.getEngine().getId())); 
			parameters.add(dataConnection.createDataField("descr,", Types.VARCHAR, biObject.getDescription()));
			parameters.add(dataConnection.createDataField("label", Types.VARCHAR, biObject.getLabel()));
			parameters.add(dataConnection.createDataField("encrypt",Types.NUMERIC,biObject.getEncrypt())); 
			parameters.add(dataConnection.createDataField("rel_name",Types.VARCHAR,biObject.getRelName())); 
			parameters.add(dataConnection.createDataField("state_id",Types.NUMERIC,biObject.getStateID())); 
			parameters.add(dataConnection.createDataField("state_cd",Types.VARCHAR,biObject.getStateCode()));
			parameters.add(dataConnection.createDataField("biobj_type_cd", Types.VARCHAR,biObject.getBiObjectTypeCode()));
			parameters.add(dataConnection.createDataField("biobj_type_id",Types.NUMERIC,biObject.getBiObjectTypeID()));
			parameters.add(dataConnection.createDataField("path", Types.VARCHAR,biObject.getPath()));
			parameters.add(dataConnection.createDataField("biobj_id", Types.DECIMAL,biObject.getId()));
			cmdUpd = dataConnection.createInsertCommand(strSql);
			dr = cmdUpd.execute(parameters);
			
			RequestContainer requestContainer =  RequestContainer.getRequestContainer();
			SessionContainer session = requestContainer.getSessionContainer();
			SessionContainer permSession = session.getPermanentContainer();
			IEngUserProfile profile = (IEngUserProfile)permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
			
			if (version){
			// if user has load a file template update the cms reporitory
				if (biObject.getTemplate().getFileContent().length > 0) {
					OperationExecutor executor = OperationExecutorManager
							.getOperationExecutor();
					connection = CMSManager.getInstance()
							.getConnection();
					SetOperation set = OperationBuilder.buildSetOperation();
					set = OperationBuilder.buildSetOperation();
					set.setContent(new ByteArrayInputStream(biObject
							.getTemplate().getFileContent()));
					set.setType(SetOperation.TYPE_CONTENT);
					set.setPath(biObject.getPath() + "/template");
					String[] nameFilePropValues = new String[] { biObject
							.getTemplate().getFileName() };
					set.setStringProperty("fileName", nameFilePropValues);
					String today = new Date().toString();
					String[] datePropValues = new String[] { today };
					set.setStringProperty("dateLoad", datePropValues);
					executor.setObject(connection, set, profile, true);
				} else if (biObject.getNameCurrentTemplateVersion() != null) {
					OperationExecutor executor = OperationExecutorManager
							.getOperationExecutor();
					connection = CMSManager.getInstance()
							.getConnection();
					GetOperation get = OperationBuilder.buildGetOperation();
					get.setPath(biObject.getPath() + "/template");
					get.setRetriveChildsInformation("false");
					get.setRetriveContentInformation("false");
					get.setRetrivePropertiesInformation("false");
					get.setRetriveVersionsInformation("false");
					SourceBean descSB = executor.getObject(connection, get,
							profile, true).getDescriptor();
					String verName = (String) descSB.getAttribute("VERSION");
					if (!biObject.getNameCurrentTemplateVersion().equals(
							verName)) {
						connection = CMSManager.getInstance().getConnection();
						RestoreOperation res = OperationBuilder
								.buildRestoreOperation();
						res.setPath(biObject.getPath() + "/template");
						res
								.setVersion(biObject
										.getNameCurrentTemplateVersion());
						executor.restoreObject(connection, res, profile, true);
					}
				}
			}
			
			// commit the transaction into the db
			dataConnection.commitTransaction();
			// Clear bytes in memory
			biObject.setTemplate(null);
		}  catch (Exception ex) {
			try{
				dataConnection.rollBackTransaction();
			} catch (Exception e) {
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "BIObjectDAOImpl", "modify", "Transaction rollback failled", ex);
			}
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "BIObjectDAOImpl", "modify", "Cannot modify detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if(connection != null) {
            	if(!connection.isClose()) {
            		connection.close();
            	}
            }
			Utils.releaseResources(dataConnection, cmdUpd, dr);
		}
		*/
	}
	/** 
	 * @see it.eng.spagobi.bo.dao.IBIObjectDAO#getCorrectRolesForExecution(java.lang.String, it.eng.spago.security.IEngUserProfile)
	 */
	public List getCorrectRolesForExecution(String pathReport, IEngUserProfile profile) throws EMFUserError {
		SQLCommand cmdSel = null;
		DataResult dr = null;
		DataConnection dataConnection = null;
		ScrollableDataResult sdr = null;
		List correctRoles = new ArrayList();
		try {
			
			// get scrollable resultset of the parameters of the report
		    List idParameters = new ArrayList();
 			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_PARAMETERS_FROM_REPORT_PATH");
			ArrayList parameters = new ArrayList(1);
			parameters.add(dataConnection.createDataField("report_path", Types.VARCHAR, pathReport));
			cmdSel = dataConnection.createSelectCommand(strSql);
			dr = cmdSel.execute(parameters);
			sdr = (ScrollableDataResult) dr.getDataObject();
			DataRow aDataRow = null;
			String id = null;
			if(sdr.hasRows()){
				sdr.moveTo(1);
				while (sdr.hasRows()){
					aDataRow = sdr.getDataRow();
				    id = aDataRow.getColumn("IDPARAM").getObjectValue().toString();
				    idParameters.add(id); 
				}
			}
			
            // for each roles of user control for each parameter(domain) if the parameter(domain)
			// has one an only one pamateruse (modality)
			Collection roles = profile.getRoles();
			Iterator iterRoles = roles.iterator();
			Iterator iterParam = null;
			String role = null;
			String idPar = null;
			while(iterRoles.hasNext()) {
				boolean correct = true;
				role = iterRoles.next().toString();
				iterParam = idParameters.iterator();
				while(iterParam.hasNext()) {
					idPar = iterParam.next().toString();
					//dataConnection = getConnection("spagobi");
					strSql = SQLStatements.getStatement("SELECT_PARAUSES_FROM_ROLE_AND_PARAMETER");
					parameters = new ArrayList(2);
					parameters.add(dataConnection.createDataField("role", Types.VARCHAR, role));
					parameters.add(dataConnection.createDataField("param", Types.DECIMAL, idPar));
					cmdSel = dataConnection.createSelectCommand(strSql);
					dr = cmdSel.execute(parameters);
					sdr = (ScrollableDataResult) dr.getDataObject();
					if(sdr.getRowsNumber() != 1) {
						correct = false;
					}
				}
				if(correct) {
					correctRoles.add(role);
				}
			}
			
	   } catch (Exception ex) {
	   		SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "BIObjectDAOImpl", "getCorrectRolesForExecution", "Cannot recover information", ex);
	   		throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
	   } finally {
	   		Utils.releaseResources(dataConnection, cmdSel, dr);
	   }
	   return correctRoles;
	}
	
	
	/** 
	 * @see it.eng.spagobi.bo.dao.IBIObjectDAO#getCorrectRolesForExecution(java.lang.String)
	 */
	
	public List getCorrectRolesForExecution(String pathReport) throws EMFUserError {
		SQLCommand cmdSel = null;
		DataResult dr = null;
		DataConnection dataConnection = null;
		ScrollableDataResult sdr = null;
		List correctRoles = new ArrayList();
		try {
			
			// get scrollable resultset of the parameters of the report
		    List idParameters = new ArrayList();
 			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_PARAMETERS_FROM_REPORT_PATH");
			ArrayList parameters = new ArrayList(1);
			parameters.add(dataConnection.createDataField("report_path", Types.VARCHAR, pathReport));
			cmdSel = dataConnection.createSelectCommand(strSql);
			dr = cmdSel.execute(parameters);
			sdr = (ScrollableDataResult) dr.getDataObject();
			DataRow aDataRow = null;
			String id = null;
			if(sdr.hasRows()){
				sdr.moveTo(1);
				while (sdr.hasRows()){
					aDataRow = sdr.getDataRow();
				    id = aDataRow.getColumn("IDPARAM").getObjectValue().toString();
				    idParameters.add(id); 
				}
			}
			
            // for each roles of user control for each parameter(domain) if the parameter(domain)
			// has one an only one pamateruse (modality)
			List roles = DAOFactory.getRoleDAO().loadAllRoles();
			Iterator iterRoles = roles.iterator();
			Iterator iterParam = null;
			String roleName = null;
			Role role = null;
			String idPar = null;
			while(iterRoles.hasNext()) {
				boolean correct = true;
				role = (Role)iterRoles.next();
				roleName = role.getName();
				iterParam = idParameters.iterator();
				while(iterParam.hasNext()) {
					idPar = iterParam.next().toString();
					strSql = SQLStatements.getStatement("SELECT_PARAUSES_FROM_ROLE_AND_PARAMETER");
					parameters = new ArrayList(1);
					parameters.add(dataConnection.createDataField("role", Types.VARCHAR, roleName));
					parameters.add(dataConnection.createDataField("param", Types.DECIMAL, idPar));
					cmdSel = dataConnection.createSelectCommand(strSql);
					dr = cmdSel.execute(parameters);
					sdr = (ScrollableDataResult) dr.getDataObject();
					if(sdr.getRowsNumber() != 1) {
						correct = false;
					}
				}
				if(correct) {
					correctRoles.add(roleName);
				}
			}
			
	   } catch (Exception ex) {
	   		SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "BIObjectDAOImpl", "getCorrectRolesForExecution", "Cannot recover information", ex);
	   		throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
	   } finally {
	   		Utils.releaseResources(dataConnection, cmdSel, dr);
	   }
	   return correctRoles;
	}

	
	
	
}
