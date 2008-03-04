package it.eng.spagobi.engines.chart.bo.charttypes;



import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.ObjTemplate;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.engines.chart.ChartImpl;
import it.eng.spagobi.engines.chart.bo.charttypes.barcharts.LinkableBar;
import it.eng.spagobi.engines.chart.bo.charttypes.barcharts.SimpleBar;
import it.eng.spagobi.engines.chart.bo.charttypes.piecharts.SimplePie;
import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;
import it.eng.spagobi.services.security.service.ISecurityServiceSupplier;
import it.eng.spagobi.services.security.service.SecurityServiceSupplierFactory;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.data.general.Dataset;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;



public class CreateJFreeChart {


	private static transient Logger logger=Logger.getLogger(CreateJFreeChart.class);

	private EMFErrorHandler errorHandler = null;
	private String title="";
	private String type="";
	private IEngUserProfile profile = null;
	private ChartImpl sbi=null;
	private boolean changeableView=false;
	private String path=null;
	private ChartRenderingInfo info=null;
	private boolean linkable=false;
	private boolean changeViewCecked=false;
	private String rootUrl="";
	private String changeViewLabel="";



	public JFreeChart createChart(String userId,String documentId) throws Exception {
		logger.debug("IN");



		ISecurityServiceSupplier supplier=SecurityServiceSupplierFactory.createISecurityServiceSupplier();
		try {
			SpagoBIUserProfile user= supplier.createUserProfile(userId);
			profile=new UserProfile(user);
		} catch (Exception e) {
			logger.error("Reading user information... ERROR",e);
			throw new SecurityException();
		}




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
				throw userError;
			}


			//		**************take informations on the chart type*****************

			String nameofChart=content.getName();
			type = (String)content.getAttribute("type");

			// set the right chart type
			sbi=ChartImpl.createChart(nameofChart, type);
			sbi.setProfile(profile);

			// configure the chart with template parameters
			sbi.configureChart(content);
			linkable=sbi.isLinkable();
			//changeView=new Boolean(sbi.isChangeView());

			//if it's a pie chart I must set if it has to be drawn in 2d or 3d version
			if(sbi.isChangeableView()){
				changeableView=true;
				if(nameofChart.equalsIgnoreCase("PIECHART")){
					((SimplePie)sbi).setChangeViewChecked(changeViewCecked);
					changeViewLabel=SimplePie.CHANGE_VIEW_LABEL;
				}
				if(nameofChart.equalsIgnoreCase("BARCHART") && type.equalsIgnoreCase("simplebar")){
					((SimpleBar)sbi).setChangeViewChecked(changeViewCecked);
					changeViewLabel=SimpleBar.CHANGE_VIEW_LABEL;
				}
			}
			else
			{
				changeableView=false;
			}

			if(nameofChart.equalsIgnoreCase("BARCHART") && type.equalsIgnoreCase("linkablebar")){
				((LinkableBar)sbi).setRootUrl(rootUrl);
			}
			// calculate values for the chart
			Dataset dataset=sbi.calculateValue();

			JFreeChart chart=null;
			// create the chart
			try{
				chart = sbi.createChart(title,dataset);
				logger.debug("successfull chart creation");


				//Create the temporary dir
				UUIDGenerator uuidGen = UUIDGenerator.getInstance();
				UUID uuid = uuidGen.generateTimeBasedUUID();
				String executionId = uuid.toString();
				executionId = executionId.replaceAll("-", "");

				info = new ChartRenderingInfo(new StandardEntityCollection());
				//Saving image on a temporary file
				String dir=System.getProperty("java.io.tmpdir");
				path=dir+"/"+executionId+".png";
				java.io.File file1 = new java.io.File(path);
				ChartUtilities.saveChartAsPNG(file1, chart, sbi.getHeight(), sbi.getWidth(), info);



				//response.setContentType("image/png");
				//ChartUtilities.writeChartAsPNG(out, chart, sbi.getHeight(), sbi.getWidth()); 


			}
			catch (Exception eex) {
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 2004);
				logger.debug("The message parameter is null");
				throw userError;
			}

			logger.debug("OUT");
			return chart;

		}
		catch (EMFUserError e) {
			errorHandler.addError(e);
			return null;
		}
		catch (Exception e) {
			EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 101);
			logger.error("Generic Error");
			errorHandler.addError(userError);
			return null;
		}
	}



	public String getPath() {
		return path;
	}



	public void setPath(String path) {
		this.path = path;
	}



	public ChartRenderingInfo getInfo() {
		return info;
	}



	public void setInfo(ChartRenderingInfo info) {
		this.info = info;
	}




	public boolean isLinkable() {
		return linkable;
	}



	public void setLinkable(boolean linkable) {
		this.linkable = linkable;
	}



	public boolean isChangeViewCecked() {
		return changeViewCecked;
	}



	public void setChangeViewCecked(boolean changeViewCecked) {
		this.changeViewCecked = changeViewCecked;
	}



	public String getRootUrl() {
		return rootUrl;
	}



	public void setRootUrl(String rootUrl) {
		this.rootUrl = rootUrl;
	}


	public String getChangeViewLabel() {
		return changeViewLabel;
	}



	public void setChangeViewLabel(String changeViewLabel) {
		this.changeViewLabel = changeViewLabel;
	}



	public boolean isChangeableView() {
		return changeableView;
	}



	public void setChangeableView(boolean changeableView) {
		this.changeableView = changeableView;
	}


}















