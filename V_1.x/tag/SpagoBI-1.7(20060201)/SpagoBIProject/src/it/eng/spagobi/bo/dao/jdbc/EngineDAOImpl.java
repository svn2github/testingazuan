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
import it.eng.spago.dispatching.service.DefaultRequestContext;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.Engine;
import it.eng.spagobi.bo.ParameterUse;
import it.eng.spagobi.bo.dao.IEngineDAO;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.services.modules.ListEnginesModule;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;


/**
 *	Defines the JDBC implementations for all DAO methods,
 *  for an engine.
 * 
 * @author zoppello
 */
public class EngineDAOImpl extends AbstractJdbcDAO implements IEngineDAO {

	/**
	 * @see it.eng.spagobi.bo.dao.IEngineDAO#loadEngineByID(java.lang.Integer)
	 */
	public Engine loadEngineByID(Integer engineID) throws EMFUserError {
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		ArrayList parameters = new ArrayList(1);
		Engine engine = new Engine();
		boolean crypt= false;
		DataConnection dataConnection = null;
		String strSql = null;
		try {
			dataConnection = getConnection("spagobi");
			strSql = SQLStatements.getStatement("SELECT_ENGINE");
			DataField dataField = dataConnection.createDataField("id", Types.DECIMAL, engineID);
			parameters.add(dataField);
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute(parameters);
			sdr = (ScrollableDataResult) dr.getDataObject();
			//ResultSet rs = sdr.getResultSet();
			DataRow aDataRow = null;
			if(sdr.hasRows()){
			sdr.moveTo(1);
			while (sdr.hasRows()){
			aDataRow = sdr.getDataRow();
			String id = aDataRow.getColumn("ID").getStringValue();
			
			engine.setId(Integer.valueOf(id));
			//engine.setId((BigDecimal)(aDataRow.getColumn("ID").getObjectValue()));
			engine.setUrl(aDataRow.getColumn("URL").getStringValue());
			String cript = aDataRow.getColumn("CRIPTABLE").getStringValue();
			
			engine.setCriptable(Integer.valueOf(cript));
			//engine.setCriptable((BigDecimal)(aDataRow.getColumn("CRIPTABLE").getObjectValue()));
			engine.setName(aDataRow.getColumn("NAME").getStringValue());
			engine.setDirUpload(aDataRow.getColumn("UPLDIR").getStringValue());
			engine.setDescription(aDataRow.getColumn("DESCRIPTION").getStringValue());
			engine.setDirUsable(aDataRow.getColumn("USEDIR").getStringValue());
			engine.setSecondaryUrl(aDataRow.getColumn("SECURL").getStringValue());
			engine.setDriverName(aDataRow.getColumn("DRIVER_NAME").getStringValue());
			
			}
			return engine;
			}
			else {
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAOImpl", "load", "Cannot recover detail information:there are no rows for the query");
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAOImpl", "load", "Statement Name:SELECT_ENGINE ---- Statement Value:" + strSql  );
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);}
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAOImpl", "load", "Cannot recover detail information", ex);
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAOImpl", "load", "Statement Name:SELECT_ENGINE ---- Statement Value:" + strSql  );
			throw new EMFUserError(EMFErrorSeverity.ERROR, 1014);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IEngineDAO#loadAllEngines()
	 */
	public List loadAllEngines() throws EMFUserError {
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		String strSql = null;
		
		List roles = new ArrayList();
		
		
		DataConnection dataConnection = null;
		try {
			dataConnection = getConnection("spagobi");
			 strSql = SQLStatements.getStatement("SELECT_ALL_ENGINE");
			
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute();
			sdr = (ScrollableDataResult) dr.getDataObject();
			
			DataRow aDataRow = null;
			Engine engine = null;
			if (sdr.hasRows()){
				sdr.moveTo(1);
				while (sdr.hasRows()){
					aDataRow = sdr.getDataRow();
					engine = new Engine();
					
					String id = aDataRow.getColumn("ID").getStringValue();
					
					engine.setId(Integer.valueOf(id));
					//engine.setId((BigDecimal)(aDataRow.getColumn("ID").getObjectValue()));
					engine.setUrl(aDataRow.getColumn("URL").getStringValue());
					String cript = aDataRow.getColumn("CRIPTABLE").getStringValue();
					BigDecimal criptBD = new BigDecimal(cript);
					engine.setCriptable(Integer.valueOf(cript));
					//engine.setCriptable((BigDecimal)(aDataRow.getColumn("CRIPTABLE").getObjectValue()));
					engine.setName(aDataRow.getColumn("NAME").getStringValue());
					engine.setDirUpload(aDataRow.getColumn("UPLDIR").getStringValue());
					engine.setDescription(aDataRow.getColumn("DESCRIPTION").getStringValue());
					engine.setDirUsable(aDataRow.getColumn("USEDIR").getStringValue());
		            engine.setSecondaryUrl(aDataRow.getColumn("SECURL").getStringValue());
					
					
					roles.add(engine);
				}
			}
			return roles;
			
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAOImpl", "load", "Cannot recover detail information", ex);
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAOImpl", "load", "Statement Name:SELECT_ALL_ENGINE ---- Statement Value:" + strSql  );
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IEngineDAO#modifyEngine(it.eng.spagobi.bo.Engine)
	 */
	public void modifyEngine(Engine aEngine) throws EMFUserError {
		SQLCommand cmdMod = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		DataConnection dataConnection = null;
		
		String strSql = null;
		try {
			
			dataConnection = getConnection("spagobi");
			strSql = SQLStatements.getStatement("UPDATE_ENGINE");
			
			ArrayList parameters = new ArrayList(9);
			parameters.add(dataConnection.createDataField("DESCR", Types.VARCHAR, aEngine.getDescription()));
			parameters.add(dataConnection.createDataField("MAIN_URL", Types.VARCHAR, aEngine.getUrl()));
			parameters.add(dataConnection.createDataField("SECN_URL", Types.VARCHAR, aEngine.getSecondaryUrl()));
			parameters.add(dataConnection.createDataField("OBJ_UPL_DIR", Types.VARCHAR, aEngine.getDirUpload()));
			parameters.add(dataConnection.createDataField("OBJ_USE_DIR", Types.VARCHAR, aEngine.getDirUsable()));
			parameters.add(dataConnection.createDataField("ENCRYPT", Types.DECIMAL, aEngine.getCriptable()));
			parameters.add(dataConnection.createDataField("NAME", Types.VARCHAR, aEngine.getName()));
			parameters.add(dataConnection.createDataField("DRIVER_NM", Types.VARCHAR, aEngine.getDriverName()));
			parameters.add(dataConnection.createDataField("ENGINE_ID", Types.DECIMAL, aEngine.getId()));
			
			if(aEngine.getName().equals("")){
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAOImpl", "modify", "Cannot update detail information:  Name field Cannot be null");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);	
			}
			
			if (aEngine.getDriverName().equals("")){
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAOImpl", "modify", "Cannot update detail information: Driver Name field Cannot be null");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);	
			}
			
			if (aEngine.getDescription().equals("")){
				
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAOImpl", "modify", "Cannot update detail information: Description field Cannot be null");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
			}
			
			
			cmdMod = dataConnection.createUpdateCommand(strSql);
             
			dr = cmdMod.execute(parameters);
		}  catch (Exception ex) {
			
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAOImpl", "modify", "Cannot update detail information", ex);
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAOImpl", "modify", "Statement Name:UPDATE ENGINE ---- Statement Value:" + strSql  );
			throw new EMFUserError(EMFErrorSeverity.ERROR, 1012);
			
		} finally {
			Utils.releaseResources(dataConnection, cmdMod, dr);
		}
		
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IEngineDAO#insertEngine(it.eng.spagobi.bo.Engine)
	 */
	public void insertEngine(Engine aEngine) throws EMFUserError {
		SQLCommand cmdIns = null;
		DataResult dr = null;
		DataConnection dataConnection = null;
		
		String strSql = null;
		try {
			
			dataConnection = getConnection("spagobi");
			strSql = SQLStatements.getStatement("INSERT_ENGINE");
			
			ArrayList parameters = new ArrayList(8);
			parameters.add(dataConnection.createDataField("ENCRYPT", Types.DECIMAL, aEngine.getCriptable()));
			parameters.add(dataConnection.createDataField("NAME", Types.VARCHAR, aEngine.getName()));
			parameters.add(dataConnection.createDataField("DESCR", Types.VARCHAR, aEngine.getDescription()));
			parameters.add(dataConnection.createDataField("MAIN_URL", Types.VARCHAR, aEngine.getUrl()));		
			parameters.add(dataConnection.createDataField("SECN_URL", Types.VARCHAR, aEngine.getSecondaryUrl()));
			parameters.add(dataConnection.createDataField("OBJ_UPL_DIR", Types.VARCHAR, aEngine.getDirUpload()));
			parameters.add(dataConnection.createDataField("OBJ_USE_DIR", Types.VARCHAR, aEngine.getDirUsable()));
			parameters.add(dataConnection.createDataField("DRIVER_NM", Types.VARCHAR, aEngine.getDriverName()));
			
			if(aEngine.getName().equals("")){
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAOImpl", "insert", "Cannot insert detail information:  Name field Cannot be null");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);	
			}
			
			if (aEngine.getDriverName().equals("")){
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAOImpl", "insert", "Cannot insert detail information: Driver Name field Cannot be null");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);	
			}
			
			if (aEngine.getDescription().equals("")){
				
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAOImpl", "insert", "Cannot insert detail information: Description field Cannot be null");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
			}
			
		   
			cmdIns = dataConnection.createInsertCommand(strSql);
			dr = cmdIns.execute(parameters);
			
		}  catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAOImpl", "insert", "Cannot insert detail information", ex);
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAOImpl", "insert", "Statement Name:INSERT_ENGINE ---- Statement Value:" + strSql  );
			throw new EMFUserError(EMFErrorSeverity.ERROR, 1012);
			
		} finally {
			Utils.releaseResources(dataConnection, cmdIns, dr);
		}
		
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IEngineDAO#eraseEngine(it.eng.spagobi.bo.Engine)
	 */
	public void eraseEngine(Engine aEngine) throws EMFUserError {
		SQLCommand cmdDel = null;
		DataResult dr = null;
		DataConnection dataConnection = null;
		Engine engine = null;
		try {
			
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("DELETE_ENGINE");
			
			ArrayList parameters = new ArrayList(1);
			parameters.add(dataConnection.createDataField("ENGINE_ID", Types.DECIMAL, aEngine.getId()));
			
			
			cmdDel = dataConnection.createDeleteCommand(strSql);
			dr = cmdDel.execute(parameters);
			
		}  catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAOImpl", "erase", "Cannot erase detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 1013);
			
		} finally {
			Utils.releaseResources(dataConnection, cmdDel, dr);
		}
		
	}
	/**
	 * @see it.eng.spagobi.bo.dao.IEngineDAO#hasBIObjAssociated(java.lang.String)
	 */
	public boolean hasBIObjAssociated (String engineId) throws EMFUserError{
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
			String strSql = SQLStatements.getStatement("SELECT_BIOBJ_BY_ENGINE_ID");
			
			elements.add(dataConnection.createDataField("engine_id", Types.DECIMAL, engineId));
		
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute(elements);
			sdr = (ScrollableDataResult) dr.getDataObject();
			boolean hasBIObjects = false;
			if(sdr.hasRows()){
				hasBIObjects = true;
			}
		  return hasBIObjects;
		}
		    catch (Exception ex) {
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAOImpl", "load", "Cannot recover detail information", ex);
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
			} finally {
				Utils.releaseResources(dataConnection, cmdSelect, dr);
			}
		
	
	
		
	}
	

}
