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
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.Parameter;
import it.eng.spagobi.bo.ParameterUse;
import it.eng.spagobi.bo.dao.IBIObjectParameterDAO;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;


/**
 *	Defines the JDBC implementations for all DAO methods,
 *  for a BI Object Parameter.  
 * 
 * @author zoppello
 */
public class BIObjectParameterDAOImpl extends AbstractJdbcDAO  implements IBIObjectParameterDAO{

	/**
	 * @see it.eng.spagobi.bo.dao.IBIObjectParameterDAO#loadBIObjectParameterForDetail(java.lang.Integer, java.lang.Integer)
	 */
	public BIObjectParameter loadBIObjectParameterForDetail(Integer biObjectID, Integer paramaterID) throws EMFUserError {
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		ArrayList elements = new ArrayList(1);  //nome cambiato per evitare confusione
		BIObjectParameter parameter = new BIObjectParameter();
		boolean crypt= false;
		DataConnection dataConnection = null;
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_BI_OBJECT_PARAMETER_DETAIL");
			
			DataField dataField = dataConnection.createDataField("biobj_id", Types.DECIMAL, biObjectID);
			DataField dataField1 = dataConnection.createDataField("par_id", Types.DECIMAL, paramaterID);
			
			elements.add(dataField);
			elements.add(dataField1);
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute(elements);
			sdr = (ScrollableDataResult) dr.getDataObject();
			ResultSet rs = sdr.getResultSet();
			
			DataRow aDataRow = null;
			if(sdr.hasRows()){
			sdr.moveTo(1);
			while (sdr.hasRows()){
			aDataRow = sdr.getDataRow();
			Parameter param = new Parameter();
			
			String biObjectIDStr = aDataRow.getColumn("BIOBJ_ID").getStringValue();
			
			parameter.setBiObjectID(Integer.valueOf(biObjectIDStr));
			
			String parIDStr = aDataRow.getColumn("PAR_ID").getStringValue();
			
			param.setId(Integer.valueOf(parIDStr));
			
			parameter.setParameter(param);
			parameter.setLabel(aDataRow.getColumn("LABEL").getStringValue());
			String requiredStr = aDataRow.getColumn("REQ_FL").getStringValue();
			parameter.setRequired(new Integer(requiredStr));
			String modifiableStr = aDataRow.getColumn("MOD_FL").getStringValue();
			parameter.setModifiable(new Integer(modifiableStr));
			String visibleStr = aDataRow.getColumn("VIEW_FL").getStringValue();
			parameter.setVisible(new Integer(visibleStr));
			String multivalueStr = aDataRow.getColumn("MULT_FL").getStringValue();
			parameter.setMultivalue(new Integer(multivalueStr));
			String progStr = aDataRow.getColumn("PROG").getStringValue();
			parameter.setProg(new Integer(progStr));
			parameter.setParameterUrlName(aDataRow.getColumn("PARURL_NM").getStringValue());
			
			}
			return parameter;
			}
			else {
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "BIObjectParameterDAOImpl", "load", "Cannot recover detail information");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);}
			

		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "BIObjectParameterDAOImpl", "load", "Cannot recover detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
	
	}

	

	/**
	 * @see it.eng.spagobi.bo.dao.IBIObjectParameterDAO#modifyBIObjectParameter(it.eng.spagobi.bo.BIObjectParameter)
	 */
	public void modifyBIObjectParameter(BIObjectParameter aBIObjectParameter) throws EMFUserError {
		SQLCommand cmdMod = null;
		SQLCommand cmdDel = null;
		DataResult dr = null;
		DataResult dr1 = null;
		DataConnection dataConnection = null;
		
		try {
			
			
			
			dataConnection = getConnection("spagobi");
			dataConnection.initTransaction();
			String strSql1 = SQLStatements.getStatement("DELETE_BI_OBJECT_PARAMETER");
			ArrayList parameters = new ArrayList(2);
			
			parameters.add(dataConnection.createDataField("par_id", Types.DECIMAL, aBIObjectParameter.getParIdOld()));
			parameters.add(dataConnection.createDataField("biobj_id", Types.DECIMAL, aBIObjectParameter.getBiObjectID()));
			cmdDel = dataConnection.createDeleteCommand(strSql1);
			dr = cmdDel.execute(parameters);
			
			
			String strSql = SQLStatements.getStatement("INSERT_BI_OBJECT_PARAMETER");
			
			ArrayList elements = new ArrayList(9);//array chiamato elements e non parameters per non creare confusione col nome della classe
			
			elements.add(dataConnection.createDataField("PAR_ID", Types.DECIMAL, aBIObjectParameter.getParameter().getId()));
			elements.add(dataConnection.createDataField("BIOBJ_ID", Types.DECIMAL, aBIObjectParameter.getBiObjectID()));
			elements.add(dataConnection.createDataField("LABEL", Types.VARCHAR, aBIObjectParameter.getLabel()));
			elements.add(dataConnection.createDataField("REQ_FL", Types.DECIMAL, aBIObjectParameter.getRequired()));
			elements.add(dataConnection.createDataField("MOD_FL", Types.DECIMAL, aBIObjectParameter.getModifiable()));
			elements.add(dataConnection.createDataField("VIEW_FL", Types.DECIMAL, aBIObjectParameter.getVisible()));
			elements.add(dataConnection.createDataField("MULT_FL", Types.DECIMAL, aBIObjectParameter.getMultivalue()));
			
			elements.add(dataConnection.createDataField("PARURL_NM", Types.VARCHAR, aBIObjectParameter.getParameterUrlName()));
	
			
			cmdMod = dataConnection.createUpdateCommand(strSql);
			dr1 = cmdMod.execute(elements);
			dataConnection.commitTransaction();
			
			
		}  catch (Exception ex) {
			try {
				dataConnection.rollBackTransaction();
			} catch (EMFInternalError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "BIObjectParameterDAOImpl", "modify", "Cannot update detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdMod, dr);
		}
		
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IBIObjectParameterDAO#insertBIObjectParameter(it.eng.spagobi.bo.BIObjectParameter)
	 */
	public void insertBIObjectParameter(BIObjectParameter aBIObjectParameter) throws EMFUserError {
		SQLCommand cmdIns = null;
		DataResult dr = null;
		DataConnection dataConnection = null;
		
		try {
			
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("INSERT_BI_OBJECT_PARAMETER");
			
			ArrayList parameters = new ArrayList(9);
			
			
			 parameters.add(dataConnection.createDataField("par_id", Types.NUMERIC,aBIObjectParameter.getParameter().getId()));
			 parameters.add(dataConnection.createDataField("biobj_id", Types.NUMERIC,aBIObjectParameter.getBiObjectID()));
			 parameters.add(dataConnection.createDataField("label", Types.VARCHAR,aBIObjectParameter.getLabel())); 
			 parameters.add(dataConnection.createDataField("req_fl", Types.NUMERIC,aBIObjectParameter.getRequired())); 
			 parameters.add(dataConnection.createDataField("mod_fl", Types.NUMERIC,aBIObjectParameter.getModifiable())); 
			 parameters.add(dataConnection.createDataField("view_fl", Types.NUMERIC,aBIObjectParameter.getVisible())); 
			 parameters.add(dataConnection.createDataField("mult_fl", Types.NUMERIC,aBIObjectParameter.getMultivalue())); 
			
			 parameters.add(dataConnection.createDataField("parurl_nm", Types.VARCHAR, aBIObjectParameter.getParameterUrlName()));
			 
			cmdIns = dataConnection.createInsertCommand(strSql);
			dr = cmdIns.execute(parameters);
			
		}  catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "BIObjectParameterDAOImpl", "insert", "Cannot insert detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdIns, dr);
		}
		
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IBIObjectParameterDAO#eraseBIObjectParameter(it.eng.spagobi.bo.BIObjectParameter)
	 */
	public void eraseBIObjectParameter(BIObjectParameter aBIObjectParameter) throws EMFUserError {
//		delete from sbi_obj_par where par_id=? and biobj_id=? and prog=?"
		SQLCommand cmdDel = null;
		DataResult dr = null;
		DataConnection dataConnection = null;
		
		try {
			
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("DELETE_BI_OBJECT_PARAMETER");
			
			ArrayList parameters = new ArrayList(2);
			
			parameters.add(dataConnection.createDataField("par_id", Types.DECIMAL, aBIObjectParameter.getParameter().getId()));
			parameters.add(dataConnection.createDataField("biobj_id", Types.DECIMAL, aBIObjectParameter.getBiObjectID()));
			
			cmdDel = dataConnection.createDeleteCommand(strSql);
			dr = cmdDel.execute(parameters);
			
		}  catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "BIObjectParameterDAOImpl", "erase", "Cannot erase detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdDel, dr);
		}
		
	}
	/** 
	 * @see it.eng.spagobi.bo.dao.IBIObjectParameterDAO#hasObjParameters(java.lang.String)
	 */
	public boolean hasObjParameters (String parId) throws EMFUserError{
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
			String strSql = SQLStatements.getStatement("SELECT_BI_OBJECT_PARAMETER_BY_PAR_ID");
			
			elements.add(dataConnection.createDataField("id", Types.DECIMAL, parId));
		
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute(elements);
			sdr = (ScrollableDataResult) dr.getDataObject();
			boolean hasObjects = false;
			if(sdr.hasRows()){
				hasObjects = true;
			}
		  return hasObjects;
		}
		    catch (Exception ex) {
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "EngineDAOImpl", "load", "Cannot recover detail information", ex);
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
			} finally {
				Utils.releaseResources(dataConnection, cmdSelect, dr);
			}
		
	
	
		
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IBIObjectParameterDAO#loadBIObjectParametersById(java.lang.Integer)
	 */
	public List loadBIObjectParametersById(Integer biObjectID) throws EMFUserError {
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		ArrayList elements = new ArrayList(1);  //nome cambiato per evitare confusione
		List BIobjPar = new ArrayList();
		boolean crypt= false;
		DataConnection dataConnection = null;
		try {
			dataConnection = getConnection("spagobi");
			String strSql = SQLStatements.getStatement("SELECT_ALL_BI_OBJECT_PARAMETERS_FOR_BI_OBJECT");
			
			DataField dataField = dataConnection.createDataField("biobj_id", Types.DECIMAL, biObjectID);
			elements.add(dataField);
			
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute(elements);
			sdr = (ScrollableDataResult) dr.getDataObject();
			ResultSet rs = sdr.getResultSet();
			
			DataRow aDataRow = null;
			if(sdr.hasRows()){
			sdr.moveTo(1);
			while (sdr.hasRows()){
			aDataRow = sdr.getDataRow();
			Parameter param = new Parameter();
			BIObjectParameter parameter = new BIObjectParameter();
			String biObjectIDStr = aDataRow.getColumn("BIOBJ_ID").getStringValue();
			
			parameter.setBiObjectID(Integer.valueOf(biObjectIDStr));
			
			String parIDStr = aDataRow.getColumn("PAR_ID").getStringValue();
			
			param.setId(Integer.valueOf(parIDStr));
			
			parameter.setParameter(param);
			parameter.setLabel(aDataRow.getColumn("LABEL").getStringValue());
			String requiredStr = aDataRow.getColumn("REQ_FL").getStringValue();
			parameter.setRequired(new Integer(requiredStr));
			String modifiableStr = aDataRow.getColumn("MOD_FL").getStringValue();
			parameter.setModifiable(new Integer(modifiableStr));
			String visibleStr = aDataRow.getColumn("VIEW_FL").getStringValue();
			parameter.setVisible(new Integer(visibleStr));
			String multivalueStr = aDataRow.getColumn("MULT_FL").getStringValue();
			parameter.setMultivalue(new Integer(multivalueStr));
			String progStr = aDataRow.getColumn("PROG").getStringValue();
			parameter.setProg(new Integer(progStr));
			parameter.setParameterUrlName(aDataRow.getColumn("PARURL_NM").getStringValue());
			BIobjPar.add(parameter);
			}
			return BIobjPar;
			}
			else {
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "BIObjectParameterDAOImpl", "load", "Cannot recover detail information");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);}
			

		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "BIObjectParameterDAOImpl", "load", "Cannot recover detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
	
	}



}
