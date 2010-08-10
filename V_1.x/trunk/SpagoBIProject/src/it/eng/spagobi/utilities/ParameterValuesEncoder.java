/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.utilities;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.ModalitiesValue;
import it.eng.spagobi.bo.Parameter;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.constants.SpagoBIConstants;

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
			String type = parameter.getType();
			boolean isString = "STRING".equalsIgnoreCase(type);
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
					return encodeMultivaluesParam(biobjPar.getParameterValues(), isString);
				}
			} else {
				List values = biobjPar.getParameterValues();
				if (values != null && values.size() > 0) {
					if (values.size() == 1) return (String)biobjPar.getParameterValues().get(0);
					else return encodeMultivaluesParam(biobjPar.getParameterValues(), isString);
				} else return "";
			}
		} else {
			Integer parId = biobjPar.getParID();
			boolean isString = false;
			if (parId == null) {
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "encode",
						  "Parameter object nor parameter id are set into BiObjectPrameter with label = " 
						+ biobjPar.getLabel() + " of document with id = " + biobjPar.getBiObjectID());
			} else {
				try {
					Parameter aParameter = DAOFactory.getParameterDAO().loadForDetailByParameterID(parId);
					String type = aParameter.getType();
					isString = "STRING".equalsIgnoreCase(type);
				} catch (EMFUserError e) {
					SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "encode",
							  "Error loading parameter with id = " + biobjPar.getParID());
				}
			}
			List values = biobjPar.getParameterValues();
			if (values != null && values.size() > 0) {
				if (values.size() == 1) return (String)biobjPar.getParameterValues().get(0);
				else return encodeMultivaluesParam(biobjPar.getParameterValues(), isString);
			} else return "";
		}

	}
	
//	private String encode(Object values) {
//		if(values instanceof String)
//			return (String)values;
//		else if(values instanceof List) {
//			return encodeMultivaluesParam((List)values);
//		} else {
//			return null;
//		}
//	}
		
	
	/////////////////////////////////////////////////////////////
	//	UTILITY METHODS
	/////////////////////////////////////////////////////////////
	
	/*
	 * isString: it is a flag: if it is true, the values are strings: in this 
	 * case, if the strings are not surrounded by ' character, they are surrounded by ' character.
	 * This is a work-around: generally speaking, a list of values should declare the type of contained objects.
	 */
	private String encodeMultivaluesParam(List values, boolean isString) {
		String value = "";
		
		if(values == null || values.size() == 0) return value;
		
		value += openBlockMarker;
		value += separator;
		value += openBlockMarker;
		for(int i = 0; i < values.size(); i++) {
			String valueToBeAppended = (values.get(i) == null)? "": (String)values.get(i);
			if (isString) {
				if (valueToBeAppended.equals("")) valueToBeAppended = "''";
				else {
					if (!valueToBeAppended.startsWith("'") && !valueToBeAppended.endsWith("'")) {
						valueToBeAppended = "'" + valueToBeAppended + "'";
					}
				}
			}
			value += (i>0)? separator: "";
			value += valueToBeAppended;
		}
		value += closeBlockMarker;
		value += closeBlockMarker;
				
		return value;
	}	
}
