/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2009 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.analiticalmodel.document.x;


import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import it.eng.spago.base.SourceBean;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.handlers.ExecutionInstance;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.Parameter;
import it.eng.spagobi.behaviouralmodel.lov.bo.ILovDetail;
import it.eng.spagobi.behaviouralmodel.lov.bo.LovDetailFactory;
import it.eng.spagobi.behaviouralmodel.lov.bo.LovResultHandler;
import it.eng.spagobi.behaviouralmodel.lov.bo.ModalitiesValue;
import it.eng.spagobi.chiron.serializer.JSONStoreFeedTransformer;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;
import it.eng.spagobi.utilities.service.JSONSuccess;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class GetParameterValuesForExecutionAction  extends AbstractSpagoBIAction {
	
	public static final String SERVICE_NAME = "GET_PARAMETERS_FOR_EXECUTION_SERVICE";
	
	// request parameters
	public static String PARAMETER_ID = "PARAMETER_ID";
	
	
	// logger component
	private static Logger logger = Logger.getLogger(GetParameterValuesForExecutionAction.class);
	
	
	public void doService() {
		
		String biparameterId;
		BIObjectParameter biparameter;
		ExecutionInstance executionInstance;
		
		
		logger.debug("IN");
		
		try {
		
			biparameterId = getAttributeAsString( PARAMETER_ID );
			
			logger.debug("Parameter [" + PARAMETER_ID + "] is equals to [" + biparameterId + "]");
			
			Assert.assertNotNull(getContext(), "Parameter [" + PARAMETER_ID + "] cannot be null" );
			Assert.assertNotNull(getContext(), "Execution context cannot be null" );
			Assert.assertNotNull(getContext().getExecutionInstance( ExecutionInstance.class.getName() ), "Execution instance cannot be null");
		
			executionInstance = getContext().getExecutionInstance( ExecutionInstance.class.getName() );
			
			BIObject obj = executionInstance.getBIObject();
			String roleName = executionInstance.getExecutionRole();
	
			biparameter = null;
			List parameters = obj.getBiObjectParameters();
			for(int i = 0; i < parameters.size(); i++) {
				BIObjectParameter p = (BIObjectParameter) parameters.get(i);
				if( biparameterId.equalsIgnoreCase( p.getParameterUrlName() ) ) {
					biparameter = p;
					break;
				}
			}
			
			Assert.assertNotNull(biparameter, "Impossible to find parameter [" + biparameterId + "]" );
			
			String lovResult = null;
			ILovDetail lovProvDet = null;
			try {
				Parameter par = biparameter.getParameter();
				ModalitiesValue lov = par.getModalityValue();
				// build the ILovDetail object associated to the lov
				String lovProv = lov.getLovProvider();
				lovProvDet = LovDetailFactory.getLovFromXML(lovProv);
				// get the result of the lov
				IEngUserProfile profile = getUserProfile();
				lovResult = biparameter.getLovResult();
				if ((lovResult == null) || (lovResult.trim().equals(""))) {
					lovResult = lovProvDet.getLovResult(profile);
				}
			
			} catch (Exception e) {
				throw new SpagoBIServiceException("Impossible to get parameter's values", e);
			} 
			
			Assert.assertNotNull(lovResult, "Impossible to get parameter's values" );
			Assert.assertNotNull(lovProvDet, "Impossible to get parameter's meta" );
			
			JSONObject valuesJSON;
			
			try {
				JSONArray valuesDataJSON = new JSONArray();
				
				// get value and description column
				String valueColumn = lovProvDet.getValueColumnName();
				String descriptionColumn = lovProvDet.getDescriptionColumnName();
				// get all the rows of the result and build the option of the combobox
				LovResultHandler lovResultHandler = new LovResultHandler(lovResult);
				List rows = lovResultHandler.getRows();
				Iterator it = rows.iterator();
				while (it.hasNext()) {
					SourceBean row = (SourceBean) it.next();
					String value = (String) row.getAttribute(valueColumn);
					String description = (String) row.getAttribute(descriptionColumn);
					
					JSONObject valueJSON = new JSONObject();
					valueJSON.put("value", GeneralUtilities.substituteQuotesIntoString(value));
					valueJSON.put("label", description);
					valueJSON.put("description", description);		
					
					valuesDataJSON.put(valueJSON);
				}
				
				valuesJSON = (JSONObject)JSONStoreFeedTransformer.getInstance().transform(valuesDataJSON, "root");
			} catch (Exception e) {
				throw new SpagoBIServiceException("Impossible to serialize response", e);
			} 
			
			
			try {
				writeBackToClient( new JSONSuccess( valuesJSON ) );
			} catch (IOException e) {
				throw new SpagoBIServiceException("Impossible to write back the responce to the client", e);
			}
		
		} finally {
			logger.debug("OUT");
		}
		

	}

	
	
}
