package it.eng.spagobi.engines.chart;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.ObjTemplate;
import it.eng.spagobi.behaviouralmodel.lov.bo.ModalitiesValue;
import it.eng.spagobi.behaviouralmodel.lov.dao.IModalitiesValueDAO;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.engines.chart.charttypes.SBISpeedometer;
import it.eng.spagobi.engines.chart.charttypes.utils.KpiInterval;
import it.eng.spagobi.services.common.IProxyService;
import it.eng.spagobi.services.common.IProxyServiceFactory;
import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;
import it.eng.spagobi.services.security.service.ISecurityServiceSupplier;
import it.eng.spagobi.services.security.service.SecurityServiceSupplierFactory;

import java.awt.Color;
import java.security.Principal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultValueDataset;



/**
 * Action that returns a PNG of the chart as specified in template
 */



public class GetJFreeChartAction extends AbstractHttpAction {

	private static transient Logger logger=Logger.getLogger(GetJFreeChartAction.class);


	private String title="";
	private String type="";
	private int width=0;
	private int height=0;
	private boolean isLOVConfigurationDefined=false;
	private String dataName="";
	private String confName="";
	private IEngUserProfile profile = null;

	


	public void service(SourceBean request, SourceBean responseSb) throws Exception {
		logger.debug("IN");
		freezeHttpResponse();

		HttpServletResponse response = getHttpResponse();
		ServletOutputStream out = response.getOutputStream();

		HttpServletRequest req = getHttpRequest();
		String userId=null;
		ConfigSingleton config = ConfigSingleton.getInstance();
		SourceBean validateSB =(SourceBean) config.getAttribute("SPAGOBI_SSO.ACTIVE");
		String active = (String) validateSB.getCharacters();
		if (active != null && active.equals("true")){
			Principal principal= req.getUserPrincipal();
			if (principal!=null) {
				userId=principal.getName();
				logger.debug("got userId from Principal="+userId);
			}
			IProxyService proxy=IProxyServiceFactory.createProxyService();
			userId=proxy.readUserId(req.getSession());
			logger.debug("got userId from IProxyService="+userId);
		}else {
			userId = (String)request.getAttribute("userid");
			logger.debug("got userId from Request="+userId);
		}


		ISecurityServiceSupplier supplier=SecurityServiceSupplierFactory.createISecurityServiceSupplier();
		try {
			SpagoBIUserProfile user= supplier.createUserProfile(userId);
			profile=new UserProfile(user);
		} catch (Exception e) {
			logger.error("Reading user information... ERROR",e);
			throw new SecurityException();
		}


		String documentId = (String)request.getAttribute("documentid");

		logger.debug("got parameters userId="+userId+" and documentId="+documentId.toString());

		//get the template
		logger.debug("getting template");

		byte[] contentBytes = null;
		try{
			ObjTemplate template = DAOFactory.getObjTemplateDAO().getBIObjectActiveTemplate(Integer.valueOf(documentId));
			if(template==null) throw new Exception("Active Template null");
			contentBytes = template.getContent();
			if(contentBytes==null) throw new Exception("Content of the Active template null");
		} catch (Exception e) {
			logger.error("Error while recovering template content: \n" + e);
			return;
		}
		// get bytes of template and transform them into a SourceBean
		SourceBean content = null;
		try {
			String contentStr = new String(contentBytes);
			content = SourceBean.fromXMLString(contentStr);
		} catch (Exception e) {
			logger.error("Error while converting the Template bytes into a SourceBean object");
			return;
		}

		type = (String)content.getAttribute("type");

		it.eng.spagobi.engines.chart.charttypes.SBISpeedometer sbi=null;
		if(type.equalsIgnoreCase("speedometer")){
			sbi=configureSpeedometer(content);
		}


		// Get the value from the LOV


		String res=getLovResult(profile, dataName);

		SourceBean sbRows=SourceBean.fromXMLString(res);
		SourceBean sbRow=(SourceBean)sbRows.getAttribute("ROW");
		String result=(String)sbRow.getAttribute("value");

		DefaultValueDataset dataset = new DefaultValueDataset(Double.valueOf(result));


		JFreeChart chart = sbi.createStandardDialChart(title,dataset);

		if (chart != null) {
			logger.debug("successfull chart creation");
			response.setContentType("image/png");
			ChartUtilities.writeChartAsPNG(out, chart, height, width); 
		}
		else {
			logger.error("error in chart creation");
		}

		logger.debug("OUT");
	}


	
	
	/**
	 * Reads the template and sets the configuration for the chart
.	 * 
	 * @param content A source bean of the template
	 */
	
	public SBISpeedometer configureSpeedometer(SourceBean content){
		logger.debug("Speedometer");
		SBISpeedometer sbi=new SBISpeedometer();
		if(content.getAttribute("name")!=null) sbi.setName((String)content.getAttribute("name"));
		else sbi.setName("");

		String widthS = (String)content.getAttribute("width");
		String heightS = (String)content.getAttribute("height");
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
		SourceBean dataSB = (SourceBean)content.getAttribute("DATA");
		List dataAttrsList = dataSB.getContainedSourceBeanAttributes();
		Iterator dataAttrsIter = dataAttrsList.iterator();
		while(dataAttrsIter.hasNext()) {
			SourceBeanAttribute paramSBA = (SourceBeanAttribute)dataAttrsIter.next();
			SourceBean param = (SourceBean)paramSBA.getValue();
			String nameParam = (String)param.getAttribute("name");
			String valueParam = (String)param.getAttribute("value");
			dataParameters.put(nameParam, valueParam);
		}

		if(dataParameters.get("dataname")!=null){	
			dataName=(String)dataParameters.get("dataname");
		}
		else {
			logger.error("no data source specified");
			throw new Exception("no data source specified");
		}

		if(dataParameters.get("confname")!=null && dataParameters.get("confname")!=""){	
			isLOVConfigurationDefined=true;
			confName=(String)dataParameters.get("confname");
		}
		else {
			isLOVConfigurationDefined=false;
		}


		if(isLOVConfigurationDefined==false){  // the configuration parameters are set in template
			logger.debug("Configuration in template");
			Map confParameters = new HashMap();
			SourceBean confSB = (SourceBean)content.getAttribute("CONF.GENERAL");
			List confAttrsList = confSB.getContainedSourceBeanAttributes();
			Iterator confAttrsIter = confAttrsList.iterator();
			while(confAttrsIter.hasNext()) {
				SourceBeanAttribute paramSBA = (SourceBeanAttribute)confAttrsIter.next();
				SourceBean param = (SourceBean)paramSBA.getValue();
				String nameParam = (String)param.getAttribute("name");
				String valueParam = (String)param.getAttribute("value");
				confParameters.put(nameParam, valueParam);
			}	
			if(confParameters.get("lower")!=null){	
				String lower=(String)confParameters.get("lower");
				sbi.setLower(Double.valueOf(lower).doubleValue());
			}
			else {
				logger.error("lower bound not defined");
				throw new Exception("lower bound not defined");
			}
			if(confParameters.get("upper")!=null){	
				String upper=(String)confParameters.get("upper");
				sbi.setUpper(Double.valueOf(upper).doubleValue());
			}
			else {
				logger.error("upper bound not defined");
				throw new Exception("upper bound not defined");
			}
			if(confParameters.get("increment")!=null){	
				String increment=(String)confParameters.get("increment");
				sbi.setIncrement(Double.valueOf(increment).doubleValue());
			}
			else {
				logger.error("increment not defined");
				throw new Exception("increment not defined");
			}
			if(confParameters.get("minortickcount")!=null){	
				String minorTickCount=(String)confParameters.get("minortickcount");
				sbi.setMinorTickCount(Integer.valueOf(minorTickCount).intValue());
			}
			else {
				sbi.setMinorTickCount(10);
							}

						
			//reading intervals information
			SourceBean intervalsSB = (SourceBean)content.getAttribute("CONF.INTERVALS");
			List intervalsAttrsList = intervalsSB.getContainedSourceBeanAttributes();
			Iterator intervalsAttrsIter = intervalsAttrsList.iterator();
			while(intervalsAttrsIter.hasNext()) {
				SourceBeanAttribute paramSBA = (SourceBeanAttribute)intervalsAttrsIter.next();
				SourceBean param = (SourceBean)paramSBA.getValue();
				String min= (String)param.getAttribute("min");
				String max= (String)param.getAttribute("max");
				String col= (String)param.getAttribute("color");


				KpiInterval interval=new KpiInterval();
				interval.setMin(Double.valueOf(min).doubleValue());
				interval.setMax(Double.valueOf(max).doubleValue());

				Color color=new Color(Integer.decode(col).intValue());
				if(color!=null){
					interval.setColor(color);}
				else{
					interval.setColor(Color.RED);
				}
				sbi.addInterval(interval);
			}	
		}
		else{ // configuration parameters are set in a LOV
			logger.debug("configuration parameters set in LOV");
			String parameters=getLovResult(profile, confName);

			SourceBean sourceBeanResult=null;
			try {
				sourceBeanResult = SourceBean.fromXMLString(parameters);
			} catch (SourceBeanException e) {
				logger.error("error in reading configuration lov");
				throw new Exception("error in reading configuration lov");
			}

			SourceBean sbRow=(SourceBean)sourceBeanResult.getAttribute("ROW");
			String lower=(String)sbRow.getAttribute("lower");
			String upper=(String)sbRow.getAttribute("upper");
			String increment=(String)sbRow.getAttribute("increment");
			String minorTickCount=(String)sbRow.getAttribute("minorTickCount");

			if(lower==null || upper==null || increment==null || minorTickCount==null){
				logger.error("error in reading configuration lov");
				throw new Exception("error in reading configuration lov");
			}
				
			sbi.setLower(Double.valueOf(lower).doubleValue());
			sbi.setUpper(Double.valueOf(upper).doubleValue());
			sbi.setIncrement(Double.valueOf(increment).doubleValue());
			sbi.setMinorTickCount(Integer.valueOf(minorTickCount).intValue());

			String intervalsNumber=(String)sbRow.getAttribute("intervalsnumber");
			for(int i=1;i<=Integer.valueOf(intervalsNumber).intValue();i++){
				KpiInterval interval=new KpiInterval();
				String min=(String)sbRow.getAttribute("lower"+(new Integer(i)).toString());
				String max=(String)sbRow.getAttribute("upper"+(new Integer(i)).toString());
				String col=(String)sbRow.getAttribute("color"+(new Integer(i)).toString());
				interval.setMin(Double.valueOf(min).doubleValue());
				interval.setMax(Double.valueOf(max).doubleValue());
				Color color=new Color(Integer.decode(col).intValue());
				interval.setColor(color);
				sbi.addInterval(interval);

			}

		}
		}catch (Exception e) {
			logger.error("error in reading template configurations");
			return null;
		}

		logger.debug("OUT");
		return sbi;
	}



	/**
	 * returns the result of a LOV 
	 * <p>
	 *	it is used both to get the value of the chart and to get its configuration parameters if there defined	  
	 * @param profile IEngUserProfile of the user
	 * @param lovLabel Label of the love to retrieve
	 */

	public String getLovResult(IEngUserProfile profile, String lovLabel){
		String result = "";
		logger.debug("IN");
		try{
			// get the lov type
			String type = getLovType(lovLabel);
			// get the result
			if (profile == null) {
				result = GeneralUtilities.getLovResult(lovLabel);
			} else {
				result = GeneralUtilities.getLovResult(lovLabel, profile);
			}
		}	
		catch (Exception e) {
			logger.error("Error");
		}
		logger.debug("OUT");
		return result;
	}



	private String getLovType(String lovName) {
		String toReturn = "";
		logger.debug("IN");
		try{
			IModalitiesValueDAO lovDAO = DAOFactory.getModalitiesValueDAO();
			ModalitiesValue lov = lovDAO.loadModalitiesValueByLabel(lovName);
			String type = lov.getITypeCd();
			toReturn = type;
		} catch (Exception e) {
			logger.error("Error while recovering type of lov " + lovName);
		}
		logger.debug("OUT");
		return toReturn;
	}



}


