package it.eng.spagobi.engines.kpi.service;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.messages.IMessageBuilder;
import it.eng.spagobi.commons.utilities.messages.MessageBuilderFactory;
import it.eng.spagobi.engines.kpi.bo.ChartImpl;
import it.eng.spagobi.kpi.config.bo.Kpi;
import it.eng.spagobi.kpi.config.bo.KpiInstance;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class CreateTrendAction extends AbstractHttpAction{
	
	private static transient Logger logger=Logger.getLogger(CreateTrendAction.class);
	
	public void service(SourceBean serviceRequest, SourceBean serviceResponse)
	throws Exception {
		logger.debug("IN");
		
		IMessageBuilder msgBuilder = MessageBuilderFactory.getMessageBuilder();
		RequestContainer requestContainer = RequestContainer.getRequestContainer();
		HttpServletRequest request = getHttpRequest();
		SessionContainer session = requestContainer.getSessionContainer();
		IEngUserProfile profile = (IEngUserProfile) session.getPermanentContainer().getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		String tempRId = (String)serviceRequest.getAttribute("RESOURCE_ID");
		logger.debug("Got Resource ID:"+(tempRId!=null ? tempRId : "null"));
		Integer resID = null;
		if (tempRId!= null){
			resID = new Integer(tempRId);
		}
		String endDate = (String)serviceRequest.getAttribute("END_DATE");
		logger.debug("Got End Date:"+(endDate!=null ? endDate : "null"));
		String resName = (String)serviceRequest.getAttribute("RESOURCE_NAME");
		logger.debug("Got resource name:"+(resName!=null ? resName : "null"));
		String tempKpiInstId = (String)serviceRequest.getAttribute("KPI_INST_ID");
		logger.debug("Got KpiInstance ID:"+(tempKpiInstId!=null ? tempKpiInstId : "null"));
		Integer kpiInstId = new Integer(tempKpiInstId);
		SourceBean formatSB = ((SourceBean) ConfigSingleton.getInstance().getAttribute("SPAGOBI.DATE-FORMAT-SERVER"));
		String format = (String) formatSB.getAttribute("format");
		logger.debug("Got Date format: "+(format!=null ? format : "null"));
		SimpleDateFormat f = new SimpleDateFormat();
		f.applyPattern(format);	
		Date d = new Date();
		d = f.parse(endDate);
		Long milliseconds = d.getTime();
		Calendar calendar = new GregorianCalendar();
		int ore = calendar.get(Calendar.HOUR); 
		int minuti = calendar.get(Calendar.MINUTE); 
		int secondi = calendar.get(Calendar.SECOND); 
		int AM = calendar.get(Calendar.AM_PM);//if AM then int=0, if PM then int=1
		if(AM==0){
			int millisec =  (secondi*1000) + (minuti *60*1000) + (ore*60*60*1000);
			Long milliSecToAdd = new Long (millisec);
			milliseconds = new Long(milliseconds.longValue()+milliSecToAdd.longValue());
			d = new Date(milliseconds);
		}else{
			int millisec =  (secondi*1000) + (minuti *60*1000) + ((ore+12)*60*60*1000);
			Long milliSecToAdd = new Long (millisec);
			milliseconds = new Long(milliseconds.longValue()+milliSecToAdd.longValue());
			d = new Date(milliseconds);
		}  
		String result = DAOFactory.getKpiDAO().getKpiTrendXmlResult(resID, kpiInstId, d);
		logger.debug("Result calculated:"+(result!=null ? result : "null"));
		KpiInstance ki = DAOFactory.getKpiDAO().loadKpiInstanceById(kpiInstId);
		Integer kpiID = ki.getKpi();
		Kpi k = DAOFactory.getKpiDAO().loadKpiById(kpiID);		
		
		String title = "";
		if (resName!= null){
			title = msgBuilder.getMessage("sbi.kpi.trendTitleWithResource", request);
			title = title.replaceAll("%0", k.getKpiName());
			title = title.replaceAll("%1", resName);
		}else{
			title = msgBuilder.getMessage("sbi.kpi.trendTitle", request);
			title = title.replaceAll("%0", k.getKpiName());
		}
		
		String subTitle = msgBuilder.getMessage("sbi.kpi.trendEndDate", request);
		subTitle = subTitle.replaceAll("%0",d.toString());
	    
		String chartType = "LineChart";		
		ChartImpl sbi = ChartImpl.createChart(chartType);
		logger.debug("Chart created");
		sbi.setProfile(profile);
		logger.debug("Profile setted for the chart");
		sbi.calculateValue(result);
		logger.debug("Result setted");
		sbi.setName(title);
		logger.debug("Title setted");
		sbi.setSubName(subTitle);
		logger.debug("Subtitle setted");
		
		serviceResponse.setAttribute("sbi", sbi);
		
		logger.debug("OUT");
	}

}
