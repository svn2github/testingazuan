package it.eng.spagobi.chiron.serializer;

import it.eng.spagobi.kpi.model.bo.Resource;
import it.eng.spagobi.kpi.threshold.bo.Threshold;

import java.util.Locale;

import org.json.JSONObject;

public class ThresholdJSONSerializer implements Serializer {

	public static final String THRESHOLD_ID = "id";
	private static final String THRESHOLD_NAME = "name";
	private static final String THRESHOLD_DESCRIPTION = "description";
	private static final String THRESHOLD_CODE = "code";
	private static final String THRESHOLD_TYPE_ID = "typeId";
	private static final String THRESHOLD_TYPE_CD = "typeCd";
	
	public Object serialize(Object o, Locale locale) throws SerializationException {
		JSONObject  result = null;
		
		if( !(o instanceof Threshold) ) {
			throw new SerializationException("ThresholdJSONSerializer is unable to serialize object of type: " + o.getClass().getName());
		}
		
		try {
			Threshold thr = (Threshold)o;
			result = new JSONObject();
			
			result.put(THRESHOLD_ID, thr.getId() );
			result.put(THRESHOLD_NAME, thr.getName() );
			result.put(THRESHOLD_DESCRIPTION, thr.getDescription() );
			result.put(THRESHOLD_CODE, thr.getCode() );
			result.put(THRESHOLD_TYPE_ID, thr.getThresholdTypeId() );
			result.put(THRESHOLD_TYPE_CD, thr.getThresholdTypeCode());			
			
		} catch (Throwable t) {
			throw new SerializationException("An error occurred while serializing object: " + o, t);
		} finally {
			
		}
		
		return result;
	}

}
