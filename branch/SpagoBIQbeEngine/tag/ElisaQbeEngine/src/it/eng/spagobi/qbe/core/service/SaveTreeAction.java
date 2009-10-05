/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.spagobi.qbe.core.service;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import it.eng.qbe.model.structure.DataMartCalculatedField;
import it.eng.qbe.query.serializer.SerializationConstants;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceException;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceExceptionHandler;
import it.eng.spagobi.utilities.service.JSONAcknowledge;


/**
 * The Class ExecuteQueryAction.
 */
public class SaveTreeAction extends AbstractQbeEngineAction {	
	
	public static final String SERVICE_NAME = "SAVE_TREE_ACTION";
	public String getActionName(){return SERVICE_NAME;}
	
	
	// INPUT PARAMETERS
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(SaveTreeAction.class);
   
    
	
	public void service(SourceBean request, SourceBean response)  {				
		
		Map calculatedFields;
				
		logger.debug("IN");
		
		try {
		
			super.service(request, response);		
			
			Assert.assertNotNull(getEngineInstance(), "It's not possible to execute " + this.getActionName() + " service before having properly created an instance of EngineInstance class");
			
			calculatedFields = getDatamartModel().getDataMartModelStructure().getCalculatedFields();
			Iterator it = calculatedFields.keySet().iterator();
			while(it.hasNext()) {
				String entityName = (String)it.next();
				List fields = (List)calculatedFields.get(entityName);
				for(int i = 0; i < fields.size(); i++) {
					DataMartCalculatedField field = (DataMartCalculatedField)fields.get(i);
					System.out.println(field.getUniqueName());
				}
			}
			
			try {
				writeBackToClient( new JSONAcknowledge() );
			} catch (IOException e) {
				String message = "Impossible to write back the responce to the client";
				throw new SpagoBIEngineServiceException(getActionName(), message, e);
			}
			
		} catch(Throwable t) {
			throw SpagoBIEngineServiceExceptionHandler.getInstance().getWrappedException(getActionName(), getEngineInstance(), t);
		} finally {
			logger.debug("OUT");
		}			
	}



	private DataMartCalculatedField deserialize(JSONObject fieldJSON) {
		DataMartCalculatedField field;
		String alias;
		String fieldType;
		
		String fieldUniqueName;		
		String group;
		String order;
		String funct;
		
		JSONObject fieldClaculationDescriptor;
		String type;
		String expression;
		
		boolean visible;
		boolean included;
		
		
		try {
			alias = fieldJSON.getString(SerializationConstants.FIELD_ALIAS);
			fieldType = fieldJSON.getString(SerializationConstants.FIELD_TYPE);
						
			fieldClaculationDescriptor = fieldJSON.getJSONObject("calculationDescriptor");
			type = fieldClaculationDescriptor.getString(SerializationConstants.FIELD_TYPE);
			expression = fieldClaculationDescriptor.getString(SerializationConstants.FIELD_EXPRESSION);
			
			field = new DataMartCalculatedField(alias, type, expression);
		} catch (Throwable t) {
			throw new SpagoBIEngineServiceException(getActionName(), "impossible to deserialize calculated field [" + fieldJSON.toString() + "]", t);
		}					
		
		
		return field;
	}
	
}