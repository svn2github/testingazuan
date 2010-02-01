/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.constants.ObjectsTreeConstants;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.engines.InternalEngineIFace;
import it.eng.spagobi.engines.chart.bo.ChartImpl;
import it.eng.spagobi.engines.chart.bo.charttypes.ILinkableChart;
import it.eng.spagobi.engines.chart.bo.charttypes.barcharts.LinkableBar;
import it.eng.spagobi.engines.chart.bo.charttypes.utils.DrillParameter;
import it.eng.spagobi.engines.chart.utils.DatasetMap;
import it.eng.spagobi.engines.drivers.exceptions.InvalidOperationRequest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;




/** Internal Engine
 *  * @author Giulio Gavardi
 *     giulio.gavardi@eng.it
 */



public class SpagoBIChartInternalEngine implements InternalEngineIFace {

	private static transient Logger logger = Logger.getLogger(SpagoBIChartInternalEngine.class);

	public static final String messageBundle = "component_spagobichartKPI_messages";



	/**
	 * Executes the document and populates the response.
	 * 
	 * @param requestContainer The <code>RequestContainer</code>chartImp object (the session can be retrieved from this object)
	 * @param obj The <code>BIObject</code> representing the document to be executed
	 * @param response The response <code>SourceBean</code> to be populated
	 * 
	 * @throws EMFUserError the EMF user error
	 */

	public void execute(RequestContainer requestContainer, BIObject obj, SourceBean response) throws EMFUserError{


		DatasetMap datasets=null;
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
		String userId=(String)((UserProfile)userProfile).getUserId();

		Locale locale=GeneralUtilities.getDefaultLocale();
		String lang=(String)session.getPermanentContainer().getAttribute(SpagoBIConstants.AF_LANGUAGE);
		String country=(String)session.getPermanentContainer().getAttribute(SpagoBIConstants.AF_COUNTRY);
		if(lang!=null && country!=null){
			locale=new Locale(lang,country,"");
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
				if(contentBytes==null) {
					logger.error("TEMPLATE DOESN'T EXIST !!!!!!!!!!!!!!!!!!!!!!!!!!!");
					EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 2007);
					userError.setBundle("messages");
					throw userError; 
				}

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


			String type=content.getName();
			String subtype = (String)content.getAttribute("type");



			String data="";
			try{
				logger.debug("Getting Data Set ID");
				if(obj.getDataSetId()!=null){
					data=obj.getDataSetId().toString();
				} else {
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
							/*String parType=par.getParameter().getType();
							if(parType.equalsIgnoreCase("STRING") || parType.equalsIgnoreCase("DATE")){
								value="'"+value+"'";
							}*/
							parametersMap.put(url, value);
						}else if(values.size() >=1){
							String value = "'"+(String)values.get(0)+"'";
							for(int k = 1; k< values.size() ; k++){
								value = value + ",'" + (String)values.get(k)+"'";
							}
							parametersMap.put(url, value);
						}
					}

				}	

			} // end looking for parameters




			try{
				logger.debug("create the chart");
				// set the right chart type
				sbi=ChartImpl.createChart(type, subtype);
				sbi.setProfile(userProfile);
				sbi.setType(type);
				sbi.setSubtype(subtype);
				sbi.setData(data);
				sbi.setLocale(locale);
				sbi.setParametersObject(parametersMap);
				// configure the chart with template parameters
				sbi.configureChart(content);
				sbi.setLocalizedTitle();

				boolean linkable=sbi.isLinkable();
				if(linkable){
					logger.debug("Linkable chart, search in request for serieurlname or categoryurlname");
					String serieurlname="";
					String categoryurlname="";

					//checjk if is a linkable bar or pie
					boolean linkableBar=false;
					if(sbi instanceof LinkableBar)linkableBar=true;
					else linkableBar=false;


					//check is these parameters are in request, if not take them from template, if not use series and category by default

					if(linkableBar){
						if(serviceRequest.getAttribute("serieurlname")!=null){
							serieurlname=(String)serviceRequest.getAttribute("serieurlname");
							((LinkableBar)sbi).setSerieUrlname(serieurlname);
						}
					}

					//category is defined both for pie and bar linkable charts
					if(serviceRequest.getAttribute("categoryurlname")!=null){
						categoryurlname=(String)serviceRequest.getAttribute("categoryurlname");
						((ILinkableChart)sbi).setCategoryUrlName(categoryurlname);
					}


					//check if there are other parameters from the drill parameters whose value is in the request; elsewhere take them from template
					logger.debug("Linkable chart: search in the request for other parameters");
					HashMap< String, DrillParameter> drillParametersMap=new HashMap<String, DrillParameter>();

					if(((ILinkableChart)sbi).getDrillParametersMap()!= null){

						drillParametersMap=(HashMap)((ILinkableChart)sbi).getDrillParametersMap().clone();

						// if finds that a parameter is in the request substitute the value; but only if in RELATIVE MODE
						for (Iterator iterator = drillParametersMap.keySet().iterator(); iterator.hasNext();) {
							String name = (String) iterator.next();
							DrillParameter drillPar=drillParametersMap.get(name);
							String typePar=drillPar.getType();
							// if relative put new value!
							if(typePar.equalsIgnoreCase("relative")){
								if(serviceRequest.getAttribute(name)!=null){
									String value=(String)serviceRequest.getAttribute(name);
									((ILinkableChart)sbi).getDrillParametersMap().remove(name);
									drillPar.setValue(value);
									((ILinkableChart)sbi).getDrillParametersMap().put(name, drillPar);
								}
							}

						}
					}

				}




			}
			catch (Exception e) {
				logger.error("Error while creating the chart",e);
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 2004);
				userError.setBundle("messages");
				throw userError;
			}



			// calculate values for the chart
			try{
				logger.debug("Retrieve value by executing the dataset");
				datasets=sbi.calculateValue();
			}	
			catch (Exception e) {
				logger.error("Error in retrieving the value", e);
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 2006);
				userError.setBundle("messages");
				throw userError;
			}


			//JFreeChart chart=null;
			// create the chart


			//in the re-drawing case in document-composition check if serie or categories or cat_group have been set
			String serie=null;
			String category=null;
			String catGroup=null;
			if(serviceRequest.getAttribute("serie")!=null)
			{
				List series=(List)serviceRequest.getAttributeAsList("serie");
				for(Iterator it=series.iterator();it.hasNext();){
					serie=(String)it.next();
					response.setAttribute("serie",serie);
				}
			}

			if(serviceRequest.getAttribute("cat_group")!=null)
			{
				List catGroups=(List)serviceRequest.getAttributeAsList("cat_group");
				for(Iterator it=catGroups.iterator();it.hasNext();){
					catGroup=(String)it.next();
					response.setAttribute("cat_group",catGroup);
				}
			}

			// If categoryAll check is checked it overwrites previous informations about slider
			if(serviceRequest.getAttribute("categoryAll")!=null){
				response.setAttribute("category","0");
			}
			else if(serviceRequest.getAttribute("category")!=null)
			{
				Object catO=serviceRequest.getAttribute("category");
				category="";
				try{

					category=(String)catO;
				}
				catch (Exception e) {
					Integer catI=(Integer)catO;
					category=catI.toString();
				}
				// if category is 0 but categoryAll is not defined means that categoryAll has just been de-selected, so put category to 1
				if(category.equals("0"))
				{
					category="1";
				}
				response.setAttribute("category",category);
			}

			// if dinamically changed the number categories visualization
			if(serviceRequest.getAttribute("n_visualization")!=null){
				Object nVis=(Object)serviceRequest.getAttribute("n_visualization");
				response.setAttribute("n_visualization",nVis);
			}




			try{
				//chart = sbi.createChart(title,dataset);
				logger.debug("successfull chart creation");
				String executionId =(String) serviceRequest.getAttribute("SBI_EXECUTION_ID");
				if (executionId != null) response.setAttribute("SBI_EXECUTION_ID",executionId);
				response.setAttribute("datasets",datasets);
				response.setAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR,obj);
				response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "CHARTKPI");
				response.setAttribute("sbi",sbi);

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
	 * The <code>SpagoBIDashboardInternalEngine</code> cannot manage subobjects so this method must not be invoked.
	 * 
	 * @param requestContainer The <code>RequestContainer</code> object (the session can be retrieved from this object)
	 * @param obj The <code>BIObject</code> representing the document
	 * @param response The response <code>SourceBean</code> to be populated
	 * @param subObjectInfo An object describing the subobject to be executed
	 * 
	 * @throws EMFUserError the EMF user error
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
	 * @param response The response <code>SourceBean</code> to be populated
	 * @param obj the obj
	 * 
	 * @throws InvalidOperationRequest the invalid operation request
	 * @throws EMFUserError the EMF user error
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
	 * @param response The response <code>SourceBean</code> to be populated
	 * @param obj the obj
	 * 
	 * @throws InvalidOperationRequest the invalid operation request
	 * @throws EMFUserError the EMF user error
	 */
	public void handleDocumentTemplateEdit(RequestContainer requestContainer, 
			BIObject obj, SourceBean response) throws EMFUserError, InvalidOperationRequest {
		logger.error("SpagoBIDashboardInternalEngine cannot build document template.");
		throw new InvalidOperationRequest();
	}



}




