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
package it.eng.spagobi.engines.chart;



import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.bo.ObjTemplate;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.commons.constants.ObjectsTreeConstants;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.engines.InternalEngineIFace;
import it.eng.spagobi.engines.chart.bo.ChartImpl;
import it.eng.spagobi.engines.chart.bo.charttypes.barcharts.LinkableBar;
import it.eng.spagobi.engines.drivers.exceptions.InvalidOperationRequest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jfree.data.general.Dataset;

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

/** Internal Engine
 *  * @author Giulio Gavardi
 *     giulio.gavardi@eng.it
 */



public class SpagoBIChartInternalEngine implements InternalEngineIFace {

	private static transient Logger logger = Logger.getLogger(SpagoBIChartInternalEngine.class);

	public static final String messageBundle = "component_spagobichartKPI_messages";



	/**
	 * Executes the document and populates the response 
	 * 
	 * @param requestContainer The <code>RequestContainer</code> object (the session can be retrieved from this object)
	 * @param obj The <code>BIObject</code> representing the document to be executed
	 * @param response The response <code>SourceBean</code> to be populated
	 */
	public void execute(RequestContainer requestContainer, BIObject obj, SourceBean response) throws EMFUserError{


		Dataset dataset=null;
		ChartImpl sbi=null;

		//RequestContainer requestContainer=RequestContainer.getRequestContainer();
		ResponseContainer responseContainer=ResponseContainer.getResponseContainer();
		EMFErrorHandler errorHandler=responseContainer.getErrorHandler();

		if (obj == null) {
			logger.error("The input object is null.");
			throw new EMFUserError(EMFErrorSeverity.ERROR, "100", messageBundle);
		}

		if (!obj.getBiObjectTypeCode().equalsIgnoreCase("DASH")) {
			logger.error("The input object is not a dashboard.");
			throw new EMFUserError(EMFErrorSeverity.ERROR, "1001", messageBundle);
		}

		String documentId=obj.getId().toString();

		SessionContainer session = requestContainer.getSessionContainer();
		IEngUserProfile userProfile = (IEngUserProfile) session.getPermanentContainer().getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		String userId=(String)userProfile.getUserUniqueIdentifier();

		// create the title
		String title = "";
		title += obj.getName();
		String objDescr = obj.getDescription();
		if( (objDescr!=null) && !objDescr.trim().equals("") ) {
			title += ": " + objDescr;
		}

		logger.debug("got parameters userId="+userId+" and documentId="+documentId.toString());

		//		**************get the template*****************
		logger.debug("getting template");
		SourceBean serviceRequest=requestContainer.getServiceRequest();


		try{
			SourceBean content = null;
			byte[] contentBytes = null;
			try{
				ObjTemplate template = DAOFactory.getObjTemplateDAO().getBIObjectActiveTemplate(Integer.valueOf(documentId));
				if(template==null) throw new Exception("Active Template null");
				contentBytes = template.getContent();
				if(contentBytes==null) throw new Exception("Content of the Active template null"); 

				// get bytes of template and transform them into a SourceBean

				String contentStr = new String(contentBytes);
				content = SourceBean.fromXMLString(contentStr);
			} catch (Exception e) {
				logger.error("Error while converting the Template bytes into a SourceBean object");
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 2003);
				userError.setBundle("messages");
				throw userError;
			}


			//		**************take informations on the chart type*****************


			String nameofChart=content.getName();
			String type = (String)content.getAttribute("type");



			String data="";
			try{
				logger.error("Getting Data Set ID");
				data=obj.getDataSetId().toString();
				if(data==null){
					logger.error("Data Set not defined");
					throw new Exception("Data Set not defined");
				} 
			}catch (Exception e) {
				logger.error("Error while getting the dataset");
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 9207);
				userError.setBundle("messages");
				throw userError;
			}

			HashMap parametersMap=null;
			try{

				logger.debug("create the chart");
				// set the right chart type
				sbi=ChartImpl.createChart(nameofChart, type);
				sbi.setProfile(userProfile);
				sbi.setType(nameofChart);
				sbi.setSubtype(type);
				sbi.setData(data);

				// configure the chart with template parameters
				sbi.configureChart(content);
				boolean linkable=sbi.isLinkable();
				if(linkable){
					logger.debug("Linkable chart, search in request for serieurlname or categoryurlname");
					String serieurlname="";
					String categoryurlname="";

					if(serviceRequest.getAttribute("serieurlname")!=null){
						serieurlname=(String)serviceRequest.getAttribute("serieurlname");
						((LinkableBar)sbi).setSerieUrlname(serieurlname);
					}
					if(serviceRequest.getAttribute("categoryurlname")!=null){
						categoryurlname=(String)serviceRequest.getAttribute("categoryurlname");
						((LinkableBar)sbi).setCategoryUrlName(categoryurlname);
					}
					//check if there are other parameters whose value is in the request

					logger.debug("Linkable chart: search in the request for other parameters");
					HashMap drillParameters=new HashMap();
					drillParameters=(HashMap)((LinkableBar)sbi).getDrillParameter().clone();

					for (Iterator iterator = drillParameters.keySet().iterator(); iterator.hasNext();) {
						String name = (String) iterator.next();
						if(serviceRequest.getAttribute(name)!=null){
							String value=(String)serviceRequest.getAttribute(name);
							((LinkableBar)sbi).getDrillParameter().remove(name);
							((LinkableBar)sbi).getDrillParameter().put(name, value);
						}

					}

				}


				//Search if the chart has parameters
				List parametersList=obj.getBiObjectParameters();
				logger.debug("Check for BIparameters and relative values");
				if(parametersList!=null){
					parametersMap=new HashMap();
					for (Iterator iterator = parametersList.iterator(); iterator.hasNext();) {
						BIObjectParameter par= (BIObjectParameter) iterator.next();
						String url=par.getParameterUrlName();
						List values=par.getParameterValues();
						if(values!=null){
							if(values.size()==1){
								String value=(String)values.get(0);
								parametersMap.put(url, value);
							}
						}

					}	

				}
			}
			catch (Exception e) {
				logger.error("Error while creating the chart");
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 2004);
				userError.setBundle("messages");
				throw userError;
			}



			// calculate values for the chart
			try{
				logger.debug("Retrieve value by executing the dataset");
				dataset=sbi.calculateValue(parametersMap);
			}	
			catch (Exception e) {
				logger.error("Error in retrieving the value");
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 2006);
				userError.setBundle("messages");
				throw userError;
			}


			//JFreeChart chart=null;
			// create the chart


			//in the re-drawing case in document-composition check if serie or categories have been set
			String serie=null;
			String category=null;
			if(serviceRequest.getAttribute("serie")!=null)
			{serie=(String)serviceRequest.getAttribute("serie");
			response.setAttribute("serie",serie);
			}
			if(serviceRequest.getAttribute("category")!=null)
			{category=(String)serviceRequest.getAttribute("category");
			response.setAttribute("category",category);
			}

			try{
				//chart = sbi.createChart(title,dataset);
				logger.debug("successfull chart creation");

				response.setAttribute("title",title);
				response.setAttribute("dataset",dataset);
				response.setAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR,obj);
				response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "CHARTKPI");
				response.setAttribute("sbi",sbi);

				String executionContext = (String)session.getAttribute(SpagoBIConstants.EXECUTION_CONTEXT);
				if (executionContext != null)
					response.setAttribute(SpagoBIConstants.EXECUTION_CONTEXT, SpagoBIConstants.DOCUMENT_COMPOSITION);


			}
			catch (Exception eex) {
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 2004);
				userError.setBundle("messages");
				throw userError;
			}

			logger.debug("OUT");


		}
		catch (EMFUserError e) {

			errorHandler.addError(e);

		}
		catch (Exception e) {
			EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 101);
			logger.error("Generic Error");
			errorHandler.addError(userError);

		}

	}


	/**
	 * The <code>SpagoBIDashboardInternalEngine</code> cannot manage subobjects so this method must not be invoked
	 * 
	 * @param requestContainer The <code>RequestContainer</code> object (the session can be retrieved from this object)
	 * @param obj The <code>BIObject</code> representing the document
	 * @param response The response <code>SourceBean</code> to be populated
	 * @param subObjectInfo An object describing the subobject to be executed
	 */
	public void executeSubObject(RequestContainer requestContainer, BIObject obj, 
			SourceBean response, Object subObjectInfo) throws EMFUserError {
		// it cannot be invoked
		logger.error("SpagoBIDashboardInternalEngine cannot exec subobjects.");
		throw new EMFUserError(EMFErrorSeverity.ERROR, "101", messageBundle);
	}

	/**
	 * Function not implemented. Thid method should not be called
	 * 
	 * @param requestContainer The <code>RequestContainer</code> object (the session can be retrieved from this object)
	 * @param biobject The BIOBject to edit
	 * @param response The response <code>SourceBean</code> to be populated
	 * @throws InvalidOperationRequest
	 */
	public void handleNewDocumentTemplateCreation(RequestContainer requestContainer, 
			BIObject obj, SourceBean response) throws EMFUserError, InvalidOperationRequest {
		logger.error("SpagoBIDashboardInternalEngine cannot build document template.");
		throw new InvalidOperationRequest();

	}

	/**
	 * Function not implemented. Thid method should not be called
	 * 
	 * @param requestContainer The <code>RequestContainer</code> object (the session can be retrieved from this object)
	 * @param biobject The BIOBject to edit
	 * @param response The response <code>SourceBean</code> to be populated
	 * @throws InvalidOperationRequest
	 */
	public void handleDocumentTemplateEdit(RequestContainer requestContainer, 
			BIObject obj, SourceBean response) throws EMFUserError, InvalidOperationRequest {
		logger.error("SpagoBIDashboardInternalEngine cannot build document template.");
		throw new InvalidOperationRequest();
	}



}




