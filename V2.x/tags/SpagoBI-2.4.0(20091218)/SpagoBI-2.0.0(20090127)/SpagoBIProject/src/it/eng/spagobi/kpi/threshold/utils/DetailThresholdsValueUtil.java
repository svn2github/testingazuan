package it.eng.spagobi.kpi.threshold.utils;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.kpi.threshold.bo.Threshold;
import it.eng.spagobi.kpi.threshold.bo.ThresholdValue;

public class DetailThresholdsValueUtil {

	public static void selectThresholdValue(Integer id,
			SourceBean serviceResponse) throws SourceBeanException,
			EMFUserError {
		ThresholdValue toReturn = DAOFactory.getThresholdValueDAO()
				.loadThresholdValueById(id);
		serviceResponse.setAttribute("THRESHOLDVALUE", toReturn);
	}

	public static void updateThresholdValueFromRequest(
			SourceBean serviceRequest, Integer id) throws EMFUserError {
		ThresholdValue threshold = getThresholdValueFromRequest(serviceRequest);
		threshold.setId(id);
		DAOFactory.getThresholdValueDAO().modifyThresholdValue(threshold);

	}

	private static ThresholdValue getThresholdValueFromRequest(
			SourceBean serviceRequest) {
		String sId = (String) serviceRequest.getAttribute("id");
		String sThresholdId = (String) serviceRequest
				.getAttribute("threshold_id");
		String sPosition = (String) serviceRequest.getAttribute("position");
		String label = (String) serviceRequest.getAttribute("label");
		String sMinValue = (String) serviceRequest.getAttribute("min_Value");
		String sMaxValue = (String) serviceRequest.getAttribute("max_Value");
		String colour = (String) serviceRequest.getAttribute("colour");
		String sSeverityId = (String) serviceRequest
				.getAttribute("severity_id");

		ThresholdValue toReturn = new ThresholdValue();

		Integer id = null;
		if (sId != null && !(sId.trim().equals(""))) {
			id = Integer.parseInt(sId);
		}

		Integer thresholdId = null;
		if (sThresholdId != null && !(sThresholdId.trim().equals(""))) {
			thresholdId = Integer.parseInt(sThresholdId);
		}

		Integer position = null;
		if (sPosition != null && !(sPosition.trim().equals(""))) {
			position = Integer.parseInt(sPosition);
		}

		Double minValue = null;
		if (sMinValue != null && !(sMinValue.trim().equals(""))) {
			minValue = new Double(sMinValue);
		}

		Double maxValue = null;
		if (sMaxValue != null && !(sMaxValue.trim().equals(""))) {
			maxValue = new Double(sMaxValue);
		}

		Integer severityId = null;
		if (sSeverityId != null && !(sSeverityId.trim().equals(""))) {
			severityId = Integer.parseInt(sSeverityId);
		}

		toReturn.setId(id);
		toReturn.setThresholdId(thresholdId);
		toReturn.setPosition(position);
		toReturn.setLabel(label);
		toReturn.setMinValue(minValue);
		toReturn.setMaxValue(maxValue);
		toReturn.setColourString(colour);
		toReturn.setSeverityId(severityId);

		return toReturn;

	}

	public static void newThresholdValue(SourceBean serviceRequest,
			SourceBean serviceResponse) throws EMFUserError,
			SourceBeanException {
		ThresholdValue toCreate = getThresholdValueFromRequest(serviceRequest);

		Integer thresholdValueId = DAOFactory.getThresholdValueDAO()
				.insertThresholdValue(toCreate);

		serviceResponse.setAttribute("ID", thresholdValueId);
		serviceResponse.setAttribute("MESSAGE", SpagoBIConstants.DETAIL_SELECT);
		selectThresholdValue(thresholdValueId, serviceResponse);

	}

	public static void restoreThresholdValue(Integer id, SourceBean serviceRequest,
			SourceBean serviceResponse) throws Exception {
		ThresholdValue toReturn = getThresholdValueFromRequest(serviceRequest);
		if (id != null) {
			toReturn.setId(id);
		}
		serviceResponse.setAttribute("THRESHOLDVALUE", toReturn);
	}

}
