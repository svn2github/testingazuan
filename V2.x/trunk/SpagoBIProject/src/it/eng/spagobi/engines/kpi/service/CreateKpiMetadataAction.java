package it.eng.spagobi.engines.kpi.service;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.kpi.config.bo.Kpi;
import it.eng.spagobi.kpi.config.bo.KpiInstance;
import it.eng.spagobi.kpi.model.bo.ModelInstanceNode;
import it.eng.spagobi.kpi.threshold.bo.Threshold;

public class CreateKpiMetadataAction extends AbstractHttpAction{
	
	private static transient Logger logger=Logger.getLogger(CreateKpiMetadataAction.class);
	
	public void service(SourceBean serviceRequest, SourceBean serviceResponse)
	throws Exception {
		logger.debug("IN");
		
		String kpiInstanceID = (String)serviceRequest.getAttribute("KPI_INST_ID");
		String kpiBeginDate = (String)serviceRequest.getAttribute("KPI_BEGIN_DATE");
		String kpiEndDate = (String)serviceRequest.getAttribute("KPI_END_DATE");
		String kpiTarget = (String)serviceRequest.getAttribute("KPI_TARGET");
		String kpiValueDescr = (String)serviceRequest.getAttribute("KPI_VALUE_DESCR");
		String kpiModelInstanceId = (String)serviceRequest.getAttribute("KPI_MODEL_INST_ID");
		
		if (kpiInstanceID!=null){
			
			KpiInstance kI = DAOFactory.getKpiDAO().loadKpiInstanceById(new Integer(kpiInstanceID));
			Integer kpiID = kI.getKpi();
			if (kpiID!=null){
				Kpi k = DAOFactory.getKpiDAO().loadKpiById(kpiID);
				String kpiCode = k.getCode();
				String kpiDescription = k.getDescription();
				String kpiInterpretation = k.getInterpretation();
				String kpiName = k.getKpiName();
				List thresholds = k.getThresholds();
				if (kpiCode!=null){
					serviceResponse.setAttribute("KPI_CODE", kpiCode);
				}else{
					serviceResponse.setAttribute("KPI_CODE", "");
				}
				if (kpiDescription!=null){
					serviceResponse.setAttribute("KPI_DESCRIPTION", kpiDescription);
				}else{
					serviceResponse.setAttribute("KPI_DESCRIPTION", "");
				}
				if (kpiInterpretation!=null){
					serviceResponse.setAttribute("KPI_INTERPRETATION", kpiInterpretation);
				}else{
					serviceResponse.setAttribute("KPI_INTERPRETATION", "");
				}
				if (kpiName!=null){
					serviceResponse.setAttribute("KPI_NAME", kpiName);
				}else{
					serviceResponse.setAttribute("KPI_NAME", "");
				}
				if (thresholds!=null){
					serviceResponse.setAttribute("KPI_THRESHOLDS", thresholds);
				}else{
					serviceResponse.setAttribute("KPI_THRESHOLDS", new ArrayList());
				}
			}
		}
		if (kpiModelInstanceId!=null){
			Integer id = new Integer(kpiModelInstanceId);
			Date d = new Date();
			ModelInstanceNode n = DAOFactory.getKpiDAO().loadModelInstanceById(id, d);
			String name =n.getName();
			String descr = n.getDescr();
			if (name!=null){
				serviceResponse.setAttribute("MODEL_INST_NAME", name);
			}else{
				serviceResponse.setAttribute("MODEL_INST_NAME", "");
			}
			if (descr!=null){
				serviceResponse.setAttribute("MODEL_INST_DESCR", descr);
			}else{
				serviceResponse.setAttribute("MODEL_INST_DESCR", "");
			}			
		}
		
		if (kpiBeginDate!=null){
			serviceResponse.setAttribute("KPI_BEGIN_DATE", kpiBeginDate);
		}else{
			serviceResponse.setAttribute("KPI_BEGIN_DATE", "");
		}
		if (kpiEndDate!=null){
			serviceResponse.setAttribute("KPI_END_DATE", kpiEndDate);
		}else{
			serviceResponse.setAttribute("KPI_END_DATE", "");
		}
		if (kpiTarget!=null){
			serviceResponse.setAttribute("KPI_TARGET", kpiTarget);
		}else{
			serviceResponse.setAttribute("KPI_TARGET", "");
		}
		if (kpiValueDescr!=null){
			serviceResponse.setAttribute("KPI_VALUE_DESCR", kpiValueDescr);
		}else{
			serviceResponse.setAttribute("KPI_VALUE_DESCR", "");
		}
		
		logger.debug("OUT");
		
	}
}
