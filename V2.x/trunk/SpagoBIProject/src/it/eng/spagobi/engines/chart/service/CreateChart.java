package it.eng.spagobi.engines.chart.service;



import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.ObjTemplate;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.engines.chart.bo.ChartImpl;

import org.apache.log4j.Logger;
import org.jfree.data.general.Dataset;



public class CreateChart extends AbstractHttpAction {


	private static transient Logger logger=Logger.getLogger(CreateChart.class);

	private EMFErrorHandler errorHandler = null;

	public void service(SourceBean request, SourceBean responseSb)
			throws Exception {

		Dataset dataset=null;
		ChartImpl sbi=null;
		
		RequestContainer requestContainer=RequestContainer.getRequestContainer();
		SessionContainer aSessionContainer=requestContainer.getSessionContainer();
		
		//Get profile
		String userId=null;		
	    SessionContainer permanentSession = aSessionContainer.getPermanentContainer();
	    IEngUserProfile userProfile = (IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
	    if (userProfile!=null) userId=(String)userProfile.getUserUniqueIdentifier();

	    errorHandler = getErrorHandler();

		String documentId = (String)request.getAttribute("documentid");

		// check if it is redrawing the chart changing view
		/*boolean changeViewChecked=false; 
		if(request.getAttribute("changeviewchecked")!=null){
			String change=(String)request.getAttribute("changeviewchecked");
			Boolean ch=new Boolean(change);
			boolean chb=ch.booleanValue();
			if(chb)changeViewChecked=true;
		}*/
		
		logger.debug("got parameters userId="+userId+" and documentId="+documentId.toString());

		//		**************get the template*****************
		logger.debug("getting template");

		
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
			try{
			String nameofChart=content.getName();
			String type = (String)content.getAttribute("type");

			// set the right chart type
			sbi=ChartImpl.createChart(nameofChart, type);
			sbi.setProfile(userProfile);
			sbi.setType(nameofChart);
			sbi.setSubtype(type);

			// configure the chart with template parameters
			sbi.configureChart(content);
			boolean linkable=sbi.isLinkable();
			
			//changeView=new Boolean(sbi.isChangeView());

			//if it's a pie chart I must set if it has to be drawn in 2d or 3d version, if a bar chart horizontal or vertical			
		/*	if(sbi.isChangeableView()){
				if(nameofChart.equalsIgnoreCase("PIECHART")){
					((SimplePie)sbi).setChangeViewChecked(changeViewChecked);
				}
				if(nameofChart.equalsIgnoreCase("BARCHART") && type.equalsIgnoreCase("simplebar")){
					((SimpleBar)sbi).setChangeViewChecked(changeViewChecked);
				}
			}*/
			
	
			// calculate values for the chart
			dataset=sbi.calculateValue();}
			catch (Exception e) {
				logger.error("Error while converting the Template bytes into a SourceBean object");
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 2004);
				userError.setBundle("messages");
				throw userError;
			}
			//JFreeChart chart=null;
			// create the chart
			try{
				//chart = sbi.createChart(title,dataset);
				logger.debug("successfull chart creation");

				String title="titolo";
				responseSb.setAttribute("title",title);
				responseSb.setAttribute("documentid",documentId);
				responseSb.setAttribute("dataset",dataset);
				//responseSb.setAttribute("changeviewchecked",new Boolean(changeViewChecked));
				responseSb.setAttribute("sbi",sbi);
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





}















