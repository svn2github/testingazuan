package it.eng.spagobi.kpi.config.utils;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.kpi.config.bo.Kpi;
import it.eng.spagobi.kpi.threshold.bo.Threshold;
import it.eng.spagobi.tools.dataset.bo.IDataSet;

public class DetailKpiUtil {

	public static void selectKpi(int id, SourceBean serviceResponse)
			throws EMFUserError, SourceBeanException {
		Kpi toReturn = DAOFactory.getKpiDAO().loadKpiDefinitionById(id);
		serviceResponse.setAttribute("KPI", toReturn);
	}

	public static void updateKpiFromRequest(SourceBean serviceRequest,
			Integer id) throws EMFUserError {
		Kpi kpi = getKpiFromRequest(serviceRequest);
		kpi.setKpiId(id);
		DAOFactory.getKpiDAO().modifyKpi(kpi);
	}

	static private Kpi getKpiFromRequest(SourceBean serviceRequest)
			throws EMFUserError {
		String name = (String) serviceRequest.getAttribute("name");
		String description = (String) serviceRequest
				.getAttribute("description");
		String code = (String) serviceRequest.getAttribute("code");
		String metric = (String) serviceRequest.getAttribute("metric");
		String sWeight = (String) serviceRequest.getAttribute("weight");
		String documentLabel = (String) serviceRequest
				.getAttribute("document_label");
		String sDs_id = (String) serviceRequest.getAttribute("ds_id");

		String sThresold_id = (String) serviceRequest
				.getAttribute("threshold_id");

		Double weight = null;
		if (sWeight != null && !sWeight.trim().equals(""))
			try {
				weight = new Double(sWeight);
			} catch (NumberFormatException nfe) {
				weight = null;
			}

		Integer ds_id = null;
		IDataSet ds = null;
		if (sDs_id != null && !sDs_id.trim().equals("")) {
			ds_id = Integer.parseInt(sDs_id);
			// try {
			ds = DAOFactory.getDataSetDAO().loadDataSetByID(ds_id);
			// } catch (EMFUserError e) {
			// e.printStackTrace();
			// }
		}

		Integer threshold_id = null;
		Threshold threshold = null;
		if (sThresold_id != null && !sThresold_id.trim().equals("")) {
			threshold_id = Integer.parseInt(sThresold_id);
			// try {
			threshold = DAOFactory.getThresholdDAO().loadThresholdById(
					threshold_id);
			// } catch (EMFUserError e) {
			// e.printStackTrace();
			// }
		}
		
		if (documentLabel!=null && documentLabel.trim().equals(""))
			documentLabel = null;

		Kpi toReturn = new Kpi();

		toReturn.setKpiName(name);
		toReturn.setDescription(description);
		toReturn.setCode(code);
		toReturn.setMetric(metric);
		toReturn.setStandardWeight(weight);
		toReturn.setDocumentLabel(documentLabel);
		toReturn.setKpiDs(ds);
		toReturn.setThreshold(threshold);

		return toReturn;
	}

	public static void newKpi(SourceBean serviceRequest,
			SourceBean serviceResponse) throws EMFUserError,
			SourceBeanException {
		Kpi toCreate = getKpiFromRequest(serviceRequest);

		Integer kpiId = DAOFactory.getKpiDAO().insertKpi(toCreate);

		serviceResponse.setAttribute("ID", kpiId);
		serviceResponse.setAttribute("MESSAGE", SpagoBIConstants.DETAIL_SELECT);
		selectKpi(kpiId, serviceResponse);
	}

	public static void restoreKpiValue(Integer id, SourceBean serviceRequest,
			SourceBean serviceResponse) throws Exception {
		Kpi toReturn = getKpiFromRequest(serviceRequest);
		if (id != null) {
			toReturn.setKpiId(id);
		}
		serviceResponse.setAttribute("KPI", toReturn);
	}

}
