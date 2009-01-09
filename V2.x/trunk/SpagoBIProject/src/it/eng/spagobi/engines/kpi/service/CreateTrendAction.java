package it.eng.spagobi.engines.kpi.service;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.engines.kpi.bo.ChartImpl;
import it.eng.spagobi.engines.kpi.bo.charttypes.trendcharts.LineChart;
import it.eng.spagobi.kpi.config.bo.Kpi;
import it.eng.spagobi.kpi.config.bo.KpiInstance;
import it.eng.spagobi.tools.dataset.bo.IDataSet;

public class CreateTrendAction extends AbstractHttpAction{
	
	private static transient Logger logger=Logger.getLogger(CreateTrendAction.class);
	
	public void service(SourceBean serviceRequest, SourceBean serviceResponse)
	throws Exception {
		logger.debug("IN");
		
		RequestContainer requestContainer = RequestContainer.getRequestContainer();
		SessionContainer session = requestContainer.getSessionContainer();
		IEngUserProfile profile = (IEngUserProfile) session.getPermanentContainer().getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		String tempRId = (String)serviceRequest.getAttribute("RESOURCE_ID");
		Integer resID = null;
		if (tempRId!= null){
			resID = new Integer(tempRId);
		}
		String endDate = (String)serviceRequest.getAttribute("END_DATE");
		String resName = (String)serviceRequest.getAttribute("RESOURCE_NAME");
		String tempKpiInstId = (String)serviceRequest.getAttribute("KPI_INST_ID");
		Integer kpiInstId = new Integer(tempKpiInstId);
		SourceBean formatSB = ((SourceBean) ConfigSingleton.getInstance().getAttribute("SPAGOBI.DATE-FORMAT"));
		String format = (String) formatSB.getAttribute("format");
		SimpleDateFormat f = new SimpleDateFormat();
		f.applyPattern(format);	
		Date d = new Date();
		d = f.parse(endDate);
		String result = DAOFactory.getKpiDAO().getKpiTrendXmlResult(resID, kpiInstId, d);
		KpiInstance ki = DAOFactory.getKpiDAO().loadKpiInstanceById(kpiInstId);
		Integer kpiID = ki.getKpi();
		Kpi k = DAOFactory.getKpiDAO().loadKpiById(kpiID);		
		
		String title = " Kpi '"+k.getKpiName()+"' values trend";
		if (resName!= null) title += " for resource "+resName;
		String subTitle = "End Date: "+endDate;
	    
		String chartType = "LineChart";		
		ChartImpl sbi = ChartImpl.createChart(chartType);
		logger.debug("Chart created");
		sbi.setProfile(profile);
		logger.debug("Profile setted for the chart");
		sbi.calculateValue(result);
		logger.debug("Result setted");
		sbi.setName(title);
		sbi.setSubName(subTitle);
		
		serviceResponse.setAttribute("sbi", sbi);
		
		logger.debug("OUT");
	}

}
