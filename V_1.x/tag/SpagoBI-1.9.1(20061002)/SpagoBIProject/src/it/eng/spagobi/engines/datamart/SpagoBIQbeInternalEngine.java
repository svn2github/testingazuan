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
package it.eng.spagobi.engines.datamart;

import it.eng.qbe.action.RecoverClassLoaderAction;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.utility.Logger;
import it.eng.qbe.utility.SpagoBICmsDataMartModelRetriever;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.SingleDataMartWizardObjectSourceBeanImpl;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.ApplicationContainer;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.BIObject.SubObjectDetail;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.engines.InternalEngineIFace;
import it.eng.spagobi.services.modules.ExecuteBIObjectModule;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.Iterator;
import java.util.List;

public class SpagoBIQbeInternalEngine implements InternalEngineIFace {

	public static final String messageBundle = "component_spagobiqbeIE_messages";
	
	/**
	 * Executes the document and populates the response 
	 * 
	 * @param requestContainer The <code>RequestContainer</code> object (the session can be retrieved from this object)
	 * @param obj The <code>BIObject</code> representing the document to be executed
	 * @param response The response <code>SourceBean</code> to be populated
	 */
	public void execute(RequestContainer requestContainer, BIObject obj, SourceBean response) throws EMFUserError {
		SpagoBITracer.debug("SpagoBIQbeInternalEngine",
	            this.getClass().getName(),
	            "execute",
	            "Calling executeSubObject method with null subObjectInfo.");
		executeSubObject(requestContainer, obj, response, null);
	}
	
	/**
	 * Executes the subobject of the document and populates the response 
	 * 
	 * @param requestContainer The <code>RequestContainer</code> object (the session can be retrieved from this object)
	 * @param obj The <code>BIObject</code> representing the document
	 * @param response The response <code>SourceBean</code> to be populated
	 * @param subObjectInfo An object describing the subobject to be executed
	 */
	public void executeSubObject(RequestContainer requestContainer, BIObject obj, 
			SourceBean response, Object subObjectInfo) throws EMFUserError {
		
		SubObjectDetail subObject = (SubObjectDetail) subObjectInfo;
		
		String nameQuery = subObject == null ? null : subObject.getName();
		
		SpagoBITracer.debug("SpagoBIQbeInternalEngine",
	            this.getClass().getName(),
	            "executeSubObject",
	            "Start execute method with subobject information: " + nameQuery + ".");
		
		if (obj == null) {
			SpagoBITracer.major("SpagoBIQbeInternalEngine",
		            this.getClass().getName(),
		            "executeSubObject",
		            "The input object is null.");
			throw new EMFUserError(EMFErrorSeverity.ERROR, "100", messageBundle);
		}
		
		if (!obj.getBiObjectTypeCode().equalsIgnoreCase("DATAMART")) {
			SpagoBITracer.major("SpagoBIQbeInternalEngine",
		            this.getClass().getName(),
		            "executeSubObject",
		            "The input object is not a datamart.");
			throw new EMFUserError(EMFErrorSeverity.ERROR, "1001", messageBundle);
		}
		
		try {
			
			String jndiDataSourceName = "";
			String dialect = "";
			List parameters = obj.getBiObjectParameters();
			Iterator iterPar = parameters.iterator();
			BIObjectParameter parameter = null;
			String urlName = null;
			while(iterPar.hasNext()) {
				parameter = (BIObjectParameter)iterPar.next();
				urlName = parameter.getParameterUrlName();
			    if(urlName.equalsIgnoreCase("JNDI_DS")) {
				   	jndiDataSourceName = (String)parameter.getParameterValues().get(0);
			    }
			    if(urlName.equalsIgnoreCase("DIALECT")) {
			    	dialect = (String)parameter.getParameterValues().get(0);
			    }   
			}
			
			SpagoBITracer.debug("SpagoBIQbeInternalEngine", this.getClass().getName(), "executeSubObject", "JNDI data source name: " + jndiDataSourceName + ".");
			/*
			if (jndiDataSourceName == null || "".equalsIgnoreCase(jndiDataSourceName)) {
				SpagoBITracer.major("SpagoBIQbeInternalEngine", this.getClass().getName(), "executeSubObject", "The name of the JNDI data source is missing.");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 1002, messageBundle);
			}
			*/
			SpagoBITracer.debug("SpagoBIQbeInternalEngine", this.getClass().getName(), "executeSubObject", "Hibernate dialect: " + dialect + ".");
			/*
			if (dialect == null || "".equalsIgnoreCase(dialect)) {
				SpagoBITracer.major("SpagoBIQbeInternalEngine", this.getClass().getName(), "executeSubObject", "The Hibernate dialect is missing.");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 1003, messageBundle);
			}
			*/
			SessionContainer session = requestContainer.getSessionContainer();
			session.setAttribute(SpagoBICmsDataMartModelRetriever.REFRESH_DATAMART, "TRUE");
			String dmName = obj.getName();
			String dmDescription = obj.getDescription();
			String dmLabel = obj.getLabel();
//			ConfigSingleton config = ConfigSingleton.getInstance();
//			SourceBean biobjectsPathSB = (SourceBean) config.getAttribute(SpagoBIConstants.CMS_BIOBJECTS_PATH);
//			String biobjectsPath = (String) biobjectsPathSB.getAttribute("path");
//			String dmPath = biobjectsPath + "/" + obj.getUuid();
			String dmPath = obj.getPath();
			DataMartModel dmModel = new DataMartModel(dmPath, jndiDataSourceName, dialect);
			dmModel.setName(dmName);
			dmModel.setDescription(dmDescription);
			dmModel.setLabel(dmLabel);
			//ApplicationContainer application = ApplicationContainer.getInstance();
			//SessionFactory sf = Utils.getSessionFactory(dmModel, application);
			session.setAttribute("dataMartModel", dmModel);
			ISingleDataMartWizardObject aWizardObject = null;
			if (nameQuery==null) {
				aWizardObject = new SingleDataMartWizardObjectSourceBeanImpl();
			} else {
				aWizardObject = dmModel.getQuery(nameQuery);
			}
			
			String cTM = String.valueOf(System.currentTimeMillis());
			session.setAttribute("QBE_START_MODIFY_QUERY_TIMESTAMP", cTM);
			session.setAttribute("QBE_LAST_UPDATE_TIMESTAMP", cTM);
			
			Logger.debug(ExecuteBIObjectModule.class, (String)session.getAttribute("QBE_LAST_UPDATE_TIMESTAMP"));
			session.setAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD, aWizardObject);
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "SELECT_FIELDS_FOR_SELECTION_PUBLISHER");
			
			ClassLoader toRecoverClassLoader = Thread.currentThread().getContextClassLoader(); 
			Logger.debug(RecoverClassLoaderAction.class, "Saving Class Loader for recovering " + toRecoverClassLoader.toString());
			if (ApplicationContainer.getInstance().getAttribute("CURRENT_THREAD_CONTEXT_LOADER") == null){
				ApplicationContainer.getInstance().setAttribute("CURRENT_THREAD_CONTEXT_LOADER", toRecoverClassLoader);
			}
			return;
		} catch (Exception e) {
			SpagoBITracer.major("SPAGOBI", 
					            "ExecuteBIObjectMOdule", 
					            "execQbe", 
					            "Cannot exec the subObject", e);
	   		throw new EMFUserError(EMFErrorSeverity.ERROR, "100", messageBundle); 
		}

	}

}
