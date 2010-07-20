package it.eng.spagobi.chiron.serializer;

import it.eng.spagobi.kpi.model.bo.Model;
import it.eng.spagobi.kpi.model.bo.Resource;

import java.util.Locale;

import org.json.JSONObject;

public class ModelNodeJSONSerializer implements Serializer {

	public static final String MODEL_ID = "id";
	private static final String MODEL_CODE = "code";
	private static final String MODEL_DESCRIPTION = "description";
	private static final String MODEL_LABEL = "label";
	private static final String MODEL_NAME = "name";
	private static final String MODEL_TYPE = "type";
	private static final String MODEL_KPI = "kpi";
	private static final String MODEL_IS_LEAF = "leaf";
	private static final String MODEL_TEXT = "text";
	
	public Object serialize(Object o, Locale locale) throws SerializationException {
		JSONObject  result = null;
		
		if( !(o instanceof Model) ) {
			throw new SerializationException("ModelNodeJSONSerializer is unable to serialize object of type: " + o.getClass().getName());
		}
		
		try {
			Model model = (Model)o;
			result = new JSONObject();
			
			result.put(MODEL_ID, model.getId() );
			result.put(MODEL_CODE, model.getCode() );
			result.put(MODEL_NAME, model.getName() );
			result.put(MODEL_LABEL, model.getLabel() );
			result.put(MODEL_DESCRIPTION, model.getDescription() );
			result.put(MODEL_KPI, model.getKpiId() );
			result.put(MODEL_TYPE, model.getTypeName() );
			if(model.getChildrenNodes() != null && !model.getChildrenNodes().isEmpty()){
				result.put(MODEL_IS_LEAF, false );
			}else{
				result.put(MODEL_IS_LEAF, true );
			}
			result.put(MODEL_TEXT, model.getCode()+" - "+ model.getName() );
		} catch (Throwable t) {
			throw new SerializationException("An error occurred while serializing object: " + o, t);
		} finally {
			
		}
		
		return result;
	}

}
