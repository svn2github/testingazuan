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

package it.eng.spagobi.services.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import it.eng.spago.base.SourceBean;
import it.eng.spago.cms.exec.CMSConnection;
import it.eng.spago.cms.exec.OperationExecutor;
import it.eng.spago.cms.exec.OperationExecutorManager;
import it.eng.spago.cms.exec.operations.GetOperation;
import it.eng.spago.cms.exec.operations.OperationBuilder;
import it.eng.spago.cms.init.CMSManager;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spago.cms.exec.results.ElementDescriptor;
import it.eng.spago.dbaccess.DataConnectionManager;
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
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

/**
 * The DAO implementation for Tree functionalities.
 * 
 * @author sulis
 */
public class TreeFunctionalitiesDAO implements ITreeFunctionalitiesDAO {

	private OperationExecutor executor = null;
	private CMSConnection connection = null;
	private GetOperation getOp = null;
    private IEngUserProfile profile = null;  
	
	/**
	 * Gets a Functionality tree from path and User Profile information.
	 * 
	 * @param path The functionalities tree path String
	 * @param prof The User Profile object
	 * @return Tre SourceBean containing the Functionality Tree.
	 * 
	 */
	public SourceBean getXmlTreeFunctionalities(String path, IEngUserProfile prof) {
		SourceBean dataTree = null; 
		try {
			executor = OperationExecutorManager.getOperationExecutor();
			connection = CMSManager.getInstance().getConnection();
			getOp = OperationBuilder.buildGetOperation();
			profile = prof;
			dataTree = openRecursively(path, true);
			connection.close();
		} catch (Exception e) {
			if(connection != null) {
				if(!connection.isClose()) {
					connection.close();
				}
			}
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, "TreeFunctionalitiesDAO", 
		            "getXmlTreeFunctionalities", "Cannot get XML functionalities tree", e);
		}
		return dataTree;
	}

	/**
	 * A method that allows to open a functionality tree in a recursive way. 
	 * 
	 * @param path The tree path
	 * @param isRoot Tells us if a tree element is the root or not
	 * @return The source bean representing tree
	 */
	private SourceBean openRecursively(String path, boolean isRoot) {
		
		String name = "";
		String description = "";
		String type = "";
		BigDecimal idFunct = null;
		BigDecimal idType = null;
		String codeType = null;
		SourceBean bean = null;
			
		try {	
			
			// create the response sourceBean 
			if(isRoot) {
				bean = new SourceBean("SystemFunctionalities");
			} else {
				bean = new SourceBean("SystemFunctionality");
			}
			
			
			// set the parameters for the get operation
			getOp.setPath(path);
			getOp.setRetriveChildsInformation("true");
			getOp.setRetriveContentInformation("false");
			getOp.setRetrivePropertiesInformation("true");
			getOp.setRetriveVersionsInformation("false");        	
			// get the cms node descriptor of the functionality
			

			ElementDescriptor desc = executor.getObject(connection, getOp, profile, false);
			SourceBean result = desc.getDescriptor();
			if(result == null) {
				return null;
			}
			
            // get the type of the object
            try {
            	type = desc.getNamedElementProperties(AdmintoolsConstants.NODE_CMS_TYPE)[0].getStringValues()[0];
            } catch (Exception e) {
              type = "";  
            }
            
            
            // if the object is not a functionality and it's not the root case return null
            if(!type.equalsIgnoreCase(AdmintoolsConstants.LOW_FUNCTIONALITY_TYPE)  &&  !isRoot) {
                return null;
              }
            
            
            // if object is a functionality and it isn't the root recover information form db
            if(type.equalsIgnoreCase(AdmintoolsConstants.LOW_FUNCTIONALITY_TYPE) && !isRoot) {
            	
            	DataConnection dataConnection = null;
            	SQLCommand  cmdSelect = null;
            	DataResult dr = null;
            	try {
	            	dataConnection = dataConnection = DataConnectionManager.getInstance().getConnection("spagobi");
	    			String strSql = SQLStatements.getStatement("SELECT_FUNCTIONALITY_BY_PATH");
	    			DataField dataField = dataConnection.createDataField("path", Types.VARCHAR, path);
	    			ArrayList elements = new ArrayList(1);
	    			elements.add(dataField);
	    			cmdSelect = dataConnection.createSelectCommand(strSql);
	    			dr = cmdSelect.execute(elements);
	    			ScrollableDataResult sdr = (ScrollableDataResult) dr.getDataObject();
	    			DataRow aDataRow = null;
	    			ResultSet rs = sdr.getResultSet();
	    			if (sdr.hasRows()){
	    			sdr.moveTo(1);
	    			
	    			while(sdr.hasRows()){
	    		    aDataRow = sdr.getDataRow();
	    		    name = aDataRow.getColumn("NAME").getStringValue();
	    			//name = rs.getString(rs.findColumn("NAME"));
	    		    description = aDataRow.getColumn("DESCRIPTION").getStringValue();
	    		    //description = rs.getString(rs.findColumn("DESCRIPTION"));
	    			String idFunctStr = aDataRow.getColumn("ID").getStringValue();
	    			Long idFunctLong = new Long(Long.parseLong(idFunctStr));
					BigDecimal idFunctBD = BigDecimal.valueOf(idFunctLong.longValue());
	    		    idFunct = idFunctBD;
	    			//idFunct = rs.getBigDecimal("ID");
	    			String idTypeStr = aDataRow.getColumn("IDTYPE").getStringValue();
	    			Long idTypeLong = new Long(Long.parseLong(idTypeStr));
					BigDecimal idTypeBD = BigDecimal.valueOf(idTypeLong.longValue());
	    		    idType = idTypeBD;
	    		    String codetype = aDataRow.getColumn("CDTYPE").getStringValue();
	    			}
	    		    //idType = rs.getBigDecimal("IDTYPE");
	    			//codeType = rs.getString("CDTYPE");
	    			}else {SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "TreeFunctionalitiesDAO", 
				            "openRecursively", "Cannot recover detail information from database");}
	    			
	    			
	    			} catch (Exception ex) {
        			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "TreeFunctionalitiesDAO", 
        					            "openRecursively", "Cannot recover detail information from database", ex);
        			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
        		} finally {
        			Utils.releaseResources(dataConnection, cmdSelect, dr);
        		}
    			
            }
            
            
            // get the child of the nodes
			List childs = result.getAttributeAsList("CHILDS.CHILD");           
			// for each child
			Iterator iter = childs.iterator();
	   		while(iter.hasNext()) {
	   			SourceBean child = (SourceBean)iter.next();
	   		    String pathChild = (String)child.getAttribute("path");		    
	   		    SourceBean underBean = openRecursively(pathChild, false);
	   		    if(underBean != null) {
	   		    	bean.setAttribute(underBean);
	   		    }
	   		}
	   		
            // if it's not the root sourcebe set the property into the bean
			if(!isRoot) {
				bean.setAttribute("id", idFunct);
	            bean.setAttribute("name", name);
	            if(description==null) {
	            	description = "";
	            }
	            bean.setAttribute("description", description);
	            bean.setAttribute("path", path);
	            bean.setAttribute("idType", idType);
	            if(codeType == null) {
	            	codeType = "";
	            }
	            bean.setAttribute("codeType", codeType);
			}
            
            
		} catch (Exception e) {
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, "TreeFinctionalitiesDAO", 
		            "openRecursively", "Cannot open recursively", e);
		}
		
		// return the sourceBean
		return bean;
	}


}
   


