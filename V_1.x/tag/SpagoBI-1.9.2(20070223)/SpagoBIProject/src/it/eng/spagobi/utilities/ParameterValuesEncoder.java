/**
 * 
 */
package it.eng.spagobi.utilities;

import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.ModalitiesValue;
import it.eng.spagobi.bo.Parameter;
import it.eng.spagobi.constants.SpagoBIConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gioia
 *
 */
public class ParameterValuesEncoder {

	private String separator;
	private String openBlockMarker;
	private String closeBlockMarker;
	
	public static final String DEFAULT_SEPARATOR = ";";
	public static final String DEFAULT_OPEN_BLOCK_MARKER = "{";
	public static final String DEFAULT_CLOSE_BLOCK_MARKER = "}";
	
	/////////////////////////////////////////////////////////////
	//	CONSTRUCTORS
	/////////////////////////////////////////////////////////////
	
	public ParameterValuesEncoder() {
		this(DEFAULT_SEPARATOR, DEFAULT_OPEN_BLOCK_MARKER, DEFAULT_CLOSE_BLOCK_MARKER);
	}
	
	public ParameterValuesEncoder(String separator, String openBlockMarker, String closeBlockMarker) {
		this.separator = separator;
		this.openBlockMarker = openBlockMarker;
		this.closeBlockMarker = closeBlockMarker;
	}
	
	/////////////////////////////////////////////////////////////
	//	ACCESS METHODS
	/////////////////////////////////////////////////////////////
	
	public String getCloseBlockMarker() {
		return closeBlockMarker;
	}

	public void setCloseBlockMarker(String closeBlockMarker) {
		this.closeBlockMarker = closeBlockMarker;
	}

	public String getOpenBlockMarker() {
		return openBlockMarker;
	}

	public void setOpenBlockMarker(String openBlockMarker) {
		this.openBlockMarker = openBlockMarker;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}
	
	/////////////////////////////////////////////////////////////
	//	PUBLIC METHODS
	/////////////////////////////////////////////////////////////
	
	public String encode(BIObjectParameter biobjPar) {
		
		if(biobjPar.getParameterValues() == null) throw new RuntimeException();
		
		Parameter parameter = biobjPar.getParameter();
		if (parameter != null) {
			ModalitiesValue modValue = parameter.getModalityValue();
			if (modValue != null) {
				boolean mult = biobjPar.getParameter().getModalityValue().isMultivalue();
				
				String typeCode = biobjPar.getParameter().getModalityValue().getITypeCd();
				if(typeCode.equalsIgnoreCase(SpagoBIConstants.INPUT_TYPE_MAN_IN_CODE)) {
					mult = false;
				}
				
				if(!mult) {
					return (String)biobjPar.getParameterValues().get(0);
				} else {
					return encodeMultivaluesParam(biobjPar.getParameterValues());
				}
			} else {
				List values = biobjPar.getParameterValues();
				if (values != null && values.size() > 0) {
					if (values.size() == 1) return (String)biobjPar.getParameterValues().get(0);
					else return encodeMultivaluesParam(biobjPar.getParameterValues());
				} else return "";
			}
		} else {
			List values = biobjPar.getParameterValues();
			if (values != null && values.size() > 0) {
				if (values.size() == 1) return (String)biobjPar.getParameterValues().get(0);
				else return encodeMultivaluesParam(biobjPar.getParameterValues());
			} else return "";
		}

	}
	
	private String encode(Object values) {
		if(values instanceof String)
			return (String)values;
		else if(values instanceof List) {
			return encodeMultivaluesParam((List)values);
		} else {
			return null;
		}
	}
		
	
	/////////////////////////////////////////////////////////////
	//	UTILITY METHODS
	/////////////////////////////////////////////////////////////
	
	private String encodeMultivaluesParam(List values) {
		String value = "";
		
		if(values == null || values.size() == 0) return value;
		
		value += openBlockMarker;
		value += separator;
		value += openBlockMarker;
		for(int i = 0; i < values.size(); i++) {
			String valueToBeAppended = (values.get(i) == null)? "": (String)values.get(i);
			value += (i>0)? separator: "";
			value += valueToBeAppended;
		}
		value += closeBlockMarker;
		value += closeBlockMarker;
				
		return value;
	}	
}
