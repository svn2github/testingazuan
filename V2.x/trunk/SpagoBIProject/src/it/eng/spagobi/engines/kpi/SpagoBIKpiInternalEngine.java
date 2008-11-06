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
package it.eng.spagobi.engines.kpi;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
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
import it.eng.spagobi.engines.chart.SpagoBIChartInternalEngine;
import it.eng.spagobi.engines.drivers.exceptions.InvalidOperationRequest;
import it.eng.spagobi.engines.kpi.bo.ChartImpl;
import it.eng.spagobi.engines.kpi.utils.DatasetMap;
import it.eng.spagobi.engines.kpi.utils.StyleLabel;
import it.eng.spagobi.kpi.config.bo.KpiInstance;
import it.eng.spagobi.kpi.config.bo.KpiValue;
import it.eng.spagobi.kpi.model.bo.ModelInstance;
import it.eng.spagobi.kpi.model.bo.ModelInstanceNode;
import it.eng.spagobi.kpi.model.bo.Resource;
import it.eng.spagobi.kpi.threshold.bo.Threshold;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 
 * @author Chiara Chiarelli
 * 
 */

public class SpagoBIKpiInternalEngine {
	
	private static transient Logger logger = Logger.getLogger(SpagoBIKpiInternalEngine.class);

	public static final String messageBundle = "component_spagobichartKPI_messages";
	protected int titleDimension;
	protected String name=null;
	protected String subName=null;
	protected int width;
	protected int height;
	protected String data;
	protected String confDataset;
	protected IEngUserProfile profile;
	protected String type="";
	protected String subtype="";
	protected Color color;
	protected boolean legend=true;
	protected Map parametersObject;
	protected boolean show_chart=true;
	protected boolean slider=true;
	protected StyleLabel styleTitle;
	protected StyleLabel styleSubTitle;
	protected HashMap seriesLabelsMap = null;
	
	protected double lower=0.0;
	protected double upper=0.0;
	protected Map confParameters;
	protected SourceBean sbRow;



	/**
	 * Executes the document and populates the response.
	 * 
	 * @param requestContainer The <code>RequestContainer</code> object (the session can be retrieved from this object)
	 * @param obj The <code>BIObject</code> representing the document to be executed
	 * @param response The response <code>SourceBean</code> to be populated
	 * 
	 * @throws EMFUserError the EMF user error
	 */

	public void execute(RequestContainer requestContainer, BIObject obj, SourceBean response) throws EMFUserError{


		DatasetMap datasets=null;
		ChartImpl sbi=null;

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

			Integer modelNodeInstanceID = (Integer)content.getAttribute("model_node_instance");
			getSetConf(content);
			//gets the ModelInstanceNode
			ModelInstanceNode mI = DAOFactory.getKpiDAO().loadModelInstanceById(modelNodeInstanceID);
			//From the modelInstance gets the related KpiInstance
			KpiInstance kpiI = mI.getKpiInstanceAssociated();
			boolean isActual = DAOFactory.getKpiDAO().hasActualValues(kpiI);
			//if not still actual calculates the new values and updates the KpiInstance
			if (!isActual){
				kpiI = calculateNewKpiInstance(kpiI);
			}
			//From the KpiInstance gets the last KpiValues 
			
			List kpiValues = kpiI.getValue();
			Iterator kpiVIt = kpiValues.iterator();
			while (kpiVIt.hasNext()){
				KpiValue kpiV = (KpiValue)kpiVIt.next();
				Resource r = kpiV.getR();
				List thresholds = kpiV.getThresholds();
				String value = kpiV.getValue();
				Double weight = kpiV.getWeight();
				if (show_chart){
					//creates a document with the representation of the kpivalues for each resource
					
				}else{
					//creates a document without the representation of the kpivalues but only with its values for each resoruce
					System.out.println("**********************************************");
					System.out.println("RESOURCE="+r.getName());
					System.out.println("Value="+value);
					System.out.println("Weight="+weight);
					System.out.println("Thresholds:");
					Iterator threshIt = thresholds.iterator();
					while(threshIt.hasNext()){
						Threshold t = (Threshold)threshIt.next();
						String type = t.getType();
						Double min = null;
						Double max = null;
						System.out.println("++++++++Threshold Type:"+type);
						if (type.equals("INTERVAL")){
							
							min = t.getMinValue();
							max = t.getMaxValue();
							System.out.println("++++++++Min:"+min);
							System.out.println("++++++++Max:"+max);
							
						}else if (type.equals("MIN")){
							
							min = t.getMinValue();
							System.out.println("++++++++Min:"+min);
							
						}else if (type.equals("MAX")){
							
							max = t.getMaxValue();
							System.out.println("++++++++Max:"+max);
							
						}
					}
										
				}
				
			}
			
			try{
				//chart = sbi.createChart(title,dataset);
				logger.debug("successfull chart creation");

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


	public void getSetConf(SourceBean content) {
		logger.debug("IN");
		// common part for all charts
		//setting the title with parameter values if is necessary
		if(content.getAttribute("name")!=null) {
			String titleChart = (String)content.getAttribute("name");
			String tmpTitle = titleChart;
			while (!tmpTitle.equals("")){
				if (tmpTitle.indexOf("$P{") >= 0){
					String parName = tmpTitle.substring(tmpTitle.indexOf("$P{")+3, tmpTitle.indexOf("}"));
					
					String parValue = (parametersObject.get(parName)==null)?"":(String)parametersObject.get(parName);
					parValue = parValue.replaceAll("\'", "");
					
					if(parValue.equals("%")) parValue = "";
					int pos = tmpTitle.indexOf("$P{"+parName+"}") + (parName.length()+4);
					titleChart = titleChart.replace("$P{" + parName + "}", parValue);
					tmpTitle = tmpTitle.substring(pos);
				}
				else
					tmpTitle = "";
			}
			setName(titleChart);
		}
		else setName("");

		SourceBean styleTitleSB = (SourceBean)content.getAttribute("STYLE_TITLE");
		if(styleTitleSB!=null){

			String fontS = (String)content.getAttribute("STYLE_TITLE.font");
			String sizeS = (String)content.getAttribute("STYLE_TITLE.size");
			String colorS = (String)content.getAttribute("STYLE_TITLE.color");


			try{
				Color color=Color.decode(colorS);
				int size=Integer.valueOf(sizeS).intValue();
				styleTitle=new StyleLabel(fontS,size,color);
				
			}
			catch (Exception e) {
				logger.error("Wrong style Title settings, use default");
			}

		}
		
		SourceBean styleSubTitleSB = (SourceBean)content.getAttribute("STYLE_SUBTITLE");
		if(styleSubTitleSB!=null){

			String subTitle = (String)content.getAttribute("STYLE_SUBTITLE.name");
			if(subTitle!=null) {
				String tmpSubTitle = subTitle;
				while (!tmpSubTitle.equals("")){
					if (tmpSubTitle.indexOf("$P{") >= 0){
						String parName = tmpSubTitle.substring(tmpSubTitle.indexOf("$P{")+3, tmpSubTitle.indexOf("}"));
						String parValue = (parametersObject.get(parName)==null)?"":(String)parametersObject.get(parName);
						parValue = parValue.replaceAll("\'", "");
						if(parValue.equals("%")) parValue = "";
						int pos = tmpSubTitle.indexOf("$P{"+parName+"}") + (parName.length()+4);
						subTitle = subTitle.replace("$P{" + parName + "}", parValue);
						tmpSubTitle = tmpSubTitle.substring(pos);
					}
					else
						tmpSubTitle = "";
				}
				setSubName(subTitle);
			}
			else setSubName("");
			
			String fontS = (String)content.getAttribute("STYLE_SUBTITLE.font");
			String sizeS = (String)content.getAttribute("STYLE_SUBTITLE.size");
			String colorS = (String)content.getAttribute("STYLE_SUBTITLE.color");


			try{
				Color color=Color.decode(colorS);
				int size=Integer.valueOf(sizeS).intValue();
				styleSubTitle=new StyleLabel(fontS,size,color);				
			}
			catch (Exception e) {
				logger.error("Wrong style SubTitle settings, use default");
			}

		}


		if(content.getAttribute("title_dimension")!=null) 
		{
			String titleD=((String)content.getAttribute("title_dimension"));
			titleDimension=Integer.valueOf(titleD).intValue();
		}
		else setTitleDimension(18);


		String colS = (String)content.getAttribute("COLORS.background");
		if(colS!=null) 
		{
			Color col=new Color(Integer.decode(colS).intValue());
			if(col!=null){
				setColor(col);}
			else{
				setColor(Color.white);
			}
		}
		else { 	
			setColor(Color.white);
		}

		String widthS = (String)content.getAttribute("DIMENSION.width");
		String heightS = (String)content.getAttribute("DIMENSION.height");
		if(widthS==null || heightS==null){
			logger.warn("Width or height non defined, use default ones");
			widthS="400";
			heightS="300";
		}

		width=Integer.valueOf(widthS).intValue();
		height=Integer.valueOf(heightS).intValue();

		// get all the data parameters 


		try{					
			Map dataParameters = new HashMap();
			SourceBean dataSB = (SourceBean)content.getAttribute("CONF");
			List dataAttrsList = dataSB.getContainedSourceBeanAttributes();
			Iterator dataAttrsIter = dataAttrsList.iterator();
			while(dataAttrsIter.hasNext()) {
				SourceBeanAttribute paramSBA = (SourceBeanAttribute)dataAttrsIter.next();
				SourceBean param = (SourceBean)paramSBA.getValue();
				String nameParam = (String)param.getAttribute("name");
				String valueParam = (String)param.getAttribute("value");
				dataParameters.put(nameParam, valueParam);
			}

			legend=true;
			if(dataParameters.get("legend")!=null && !(((String)dataParameters.get("legend")).equalsIgnoreCase("") )){	
				String leg=(String)dataParameters.get("legend");
				if(leg.equalsIgnoreCase("false"))
					legend=false;
			}

			show_chart=true;
			if(dataParameters.get("show_chart")!=null && !(((String)dataParameters.get("show_chart")).equalsIgnoreCase("") )){	
				String fil=(String)dataParameters.get("show_chart");
				if(fil.equalsIgnoreCase("false"))
					show_chart=false;
			}
			
			
		}
		catch (Exception e) {
			logger.error("error in reading data source parameters");
		}


	}
	
	public KpiInstance calculateNewKpiInstance(KpiInstance k){
		return null;
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
	 * Sets the color.
	 * 
	 * @param color the new color
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setTitleDimension(int titleDimension) {
		this.titleDimension = titleDimension;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.chart.bo.IChart#setName(java.lang.String)
	 */
	public void setName(String _name) {
		name=_name;		
	}
	
	public void setSubName(String _name) {
		subName=_name;		
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
