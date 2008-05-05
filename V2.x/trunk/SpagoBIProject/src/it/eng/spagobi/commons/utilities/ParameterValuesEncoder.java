/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.commons.utilities;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.Parameter;
import it.eng.spagobi.behaviouralmodel.lov.bo.ModalitiesValue;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author Gioia
 * 
 */
public class ParameterValuesEncoder {

    static private Logger logger = Logger.getLogger(ParameterValuesEncoder.class);
    private String separator;
    private String openBlockMarker;
    private String closeBlockMarker;

    public static final String DEFAULT_SEPARATOR = ";";
    public static final String DEFAULT_OPEN_BLOCK_MARKER = "{";
    public static final String DEFAULT_CLOSE_BLOCK_MARKER = "}";

    // ///////////////////////////////////////////////////////////
    // CONSTRUCTORS
    // ///////////////////////////////////////////////////////////

    /**
     * Instantiates a new parameter values encoder.
     */
    public ParameterValuesEncoder() {
	this(DEFAULT_SEPARATOR, DEFAULT_OPEN_BLOCK_MARKER, DEFAULT_CLOSE_BLOCK_MARKER);
    }

    /**
     * Instantiates a new parameter values encoder.
     * 
     * @param separator the separator
     * @param openBlockMarker the open block marker
     * @param closeBlockMarker the close block marker
     */
    public ParameterValuesEncoder(String separator, String openBlockMarker, String closeBlockMarker) {
	this.separator = separator;
	this.openBlockMarker = openBlockMarker;
	this.closeBlockMarker = closeBlockMarker;
    }

    // ///////////////////////////////////////////////////////////
    // ACCESS METHODS
    // ///////////////////////////////////////////////////////////

    /**
     * Gets the close block marker.
     * 
     * @return the close block marker
     */
    public String getCloseBlockMarker() {
	return closeBlockMarker;
    }

    /**
     * Sets the close block marker.
     * 
     * @param closeBlockMarker the new close block marker
     */
    public void setCloseBlockMarker(String closeBlockMarker) {
	this.closeBlockMarker = closeBlockMarker;
    }

    /**
     * Gets the open block marker.
     * 
     * @return the open block marker
     */
    public String getOpenBlockMarker() {
	return openBlockMarker;
    }

    /**
     * Sets the open block marker.
     * 
     * @param openBlockMarker the new open block marker
     */
    public void setOpenBlockMarker(String openBlockMarker) {
	this.openBlockMarker = openBlockMarker;
    }

    /**
     * Gets the separator.
     * 
     * @return the separator
     */
    public String getSeparator() {
	return separator;
    }

    /**
     * Sets the separator.
     * 
     * @param separator the new separator
     */
    public void setSeparator(String separator) {
	this.separator = separator;
    }

    // ///////////////////////////////////////////////////////////
    // PUBLIC METHODS
    // ///////////////////////////////////////////////////////////

    /**
     * Encode.
     * 
     * @param biobjPar the biobj par
     * 
     * @return the string
     */
    public String encode(BIObjectParameter biobjPar) {
	logger.debug("IN");
	if (biobjPar.getParameterValues() == null) {
	    logger.error("biobjPar.getParameterValues() == null");
	    return null;
	}

	Parameter parameter = biobjPar.getParameter();
	if (parameter != null) {
	    String type = parameter.getType();
	    logger.debug("type="+type);
	    boolean isString = "STRING".equalsIgnoreCase(type);
	    ModalitiesValue modValue = parameter.getModalityValue();
	    if (modValue != null) {
		boolean mult = biobjPar.getParameter().getModalityValue().isMultivalue();

		String typeCode = biobjPar.getParameter().getModalityValue().getITypeCd();
		logger.debug("typeCode="+typeCode);
		if (typeCode.equalsIgnoreCase(SpagoBIConstants.INPUT_TYPE_MAN_IN_CODE)) {
		    mult = false;
		}

		if (!mult) {
		    return (String) biobjPar.getParameterValues().get(0);
		} else {
		    return encodeMultivaluesParam(biobjPar.getParameterValues(), isString);
		}
	    } else {
		List values = biobjPar.getParameterValues();
		if (values != null && values.size() > 0) {
		    if (values.size() == 1)
			return (String) biobjPar.getParameterValues().get(0);
		    else
			return encodeMultivaluesParam(biobjPar.getParameterValues(), isString);
		} else
		    return "";
	    }
	} else {
	    Integer parId = biobjPar.getParID();
	    boolean isString = false;
	    if (parId == null) {
		logger.warn("Parameter object nor parameter id are set into BiObjectPrameter with label = "
			+ biobjPar.getLabel() + " of document with id = " + biobjPar.getBiObjectID());
	    } else {
		try {
		    Parameter aParameter = DAOFactory.getParameterDAO().loadForDetailByParameterID(parId);
		    String type = aParameter.getType();
		    isString = "STRING".equalsIgnoreCase(type);
		} catch (EMFUserError e) {
		    logger.warn("Error loading parameter with id = " + biobjPar.getParID());
		}
	    }
	    List values = biobjPar.getParameterValues();
	    if (values != null && values.size() > 0) {
		if (values.size() == 1)
		    return (String) biobjPar.getParameterValues().get(0);
		else
		    return encodeMultivaluesParam(biobjPar.getParameterValues(), isString);
	    } else
		return "";
	}

    }

    // ///////////////////////////////////////////////////////////
    // UTILITY METHODS
    // ///////////////////////////////////////////////////////////

    /*
     * isString: it is a flag: if it is true, the values are strings: in this
     * case, if the strings are not surrounded by ' character, they are
     * surrounded by ' character. This is a work-around: generally speaking, a
     * list of values should declare the type of contained objects.
     */
    private String encodeMultivaluesParam(List values, boolean isString) {
	logger.debug("IN");
	String value = "";

	if (values == null || values.size() == 0)
	    return value;

	value += openBlockMarker;
	value += separator;
	value += openBlockMarker;
	for (int i = 0; i < values.size(); i++) {
	    String valueToBeAppended = (values.get(i) == null) ? "" : (String) values.get(i);
	    if (isString) {
		if (valueToBeAppended.equals(""))
		    valueToBeAppended = "''";
		else {
		    if (!valueToBeAppended.startsWith("'") && !valueToBeAppended.endsWith("'")) {
			valueToBeAppended = "'" + valueToBeAppended + "'";
		    }
		}
	    }
	    value += (i > 0) ? separator : "";
	    value += valueToBeAppended;
	}
	value += closeBlockMarker;
	value += closeBlockMarker;
	logger.debug("IN.value=" + value);
	return value;
    }
}
