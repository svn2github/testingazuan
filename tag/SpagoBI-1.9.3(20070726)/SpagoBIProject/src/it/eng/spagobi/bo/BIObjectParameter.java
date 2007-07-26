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
package it.eng.spagobi.bo;

import java.io.Serializable;
import java.util.List;

/**
 * Defines a <code>BIObjectParameter</code> object.
 * 
 * @author Zoppello This class map the SBI_OBJ_PAR table
 */
public class BIObjectParameter implements Serializable {
	
	private Integer id = null;
	
	/* BIOBJ_ID NUMBER N Business Intelligence Object identifier */
	private Integer biObjectID = null;
	
	/* PAR_ID NUMBER N Parameter identifier */	
	private Integer parID = null;

	private Parameter parameter = null;
	
	/*
	 * LABEL VARCHAR2(36) Y Parameter label in BIObj use (short textual
	 * identifier)
	 */
	private String label = null;

	/* REQ_FL NUMBER Y Parameter required flag. */
	private Integer required = null;

	/* MOD_FL NUMBER Y Parameter modifiable flag. */
	private Integer modifiable = null;

	/* VIEW_FL NUMBER Y Paramenter visibility flag. */
	private Integer visible = null;

	/* MULT_FL NUMBER Y Multivalue parameter. */
	private Integer multivalue = null;

	/*
	 * PROG NUMBER N Ordinal number for sorting
	 */
	private Integer prog = null;

	/*
	 * PRIORITY NUMBER N Ordinal number for sorting BIObjectParameters relative to one BIObject
	 */
	private Integer priority = null;
	
	/* PARURL_NM VARCHAR2(18) Y Parameter name in HTTP request. */
	private String parameterUrlName = null;

	/* VALUES LIST OF THE PARAMETER (STRINGS) */
	private List parameterValues = null;
	
	/* transient flag. set to true for parameters buil on the fly */
	private boolean transientParmeters = false;
	
	private String lovResult;
	
	private boolean hasValidValues = true;
	
	/**
	 * @return Returns the id of the BIObjectParameter
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id The BIObjectParameter to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return Returns the biObjectID.
	 */
	public Integer getBiObjectID() {
		return biObjectID;
	}
	/**
	 * @param biObjectID The biObjectID to set.
	 */
	public void setBiObjectID(Integer biObjectID) {
		this.biObjectID = biObjectID;
	}
	/**
	 * @return Returns the label.
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label The label to set.
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return Returns the modifiable.
	 */
	public Integer getModifiable() {
		return modifiable;
	}
	/**
	 * @param modifiable The modifiable to set.
	 */
	public void setModifiable(Integer modifiable) {
		this.modifiable = modifiable;
	}
	/**
	 * @return Returns the multivalue.
	 */
	public Integer getMultivalue() {
		return multivalue;
	}
	/**
	 * @param multivalue The multivalue to set.
	 */
	public void setMultivalue(Integer multivalue) {
		this.multivalue = multivalue;
	}
	/**
	 * @return Returns the parameterUrlName.
	 */
	public String getParameterUrlName() {
		return parameterUrlName;
	}
	/**
	 * @param parameterUrlName The parameterUrlName to set.
	 */
	public void setParameterUrlName(String parameterUrlName) {
		this.parameterUrlName = parameterUrlName;
	}
	/**
	 * @return Returns the parameterValues.
	 */
	public List getParameterValues() {
		return parameterValues;
	}
	/**
	 * @param parameterValues The parameterValues to set.
	 */
	public void setParameterValues(List parameterValues) {
		this.parameterValues = parameterValues;
	}
	/**
	 * @return Returns the parID.
	 */
	public Integer getParID() {
		return parID;
	}
	/**
	 * @param parID The parID to set.
	 */
	public void setParID(Integer parID) {
		this.parID = parID;
	}
	/**
	 * @return Returns the prog.
	 */
	public Integer getProg() {
		return prog;
	}
	/**
	 * @param prog The prog to set.
	 */
	public void setProg(Integer prog) {
		this.prog = prog;
	}
	/**
	 * @return Returns the required.
	 */
	public Integer getRequired() {
		return required;
	}
	/**
	 * @param required The required to set.
	 */
	public void setRequired(Integer required) {
		this.required = required;
	}
	/**
	 * @return Returns the visible.
	 */
	public Integer getVisible() {
		return visible;
	}
	/**
	 * @param visible The visible to set.
	 */
	public void setVisible(Integer visible) {
		this.visible = visible;
	}
	/**
	 * @return the Parameter object
	 */
	public Parameter getParameter() {
		return parameter;
	}
	/**
	 * @param parameter The Parameter to set
	 */
	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}
	/**
	 * @return Returns the priority
	 */
	public Integer getPriority() {
		return priority;
	}
	/**
	 * @param priority The priority to set
	 */
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	public boolean isTransientParmeters() {
		return transientParmeters;
	}
	
	public void setTransientParmeters(boolean transientParmeters) {
		this.transientParmeters = transientParmeters;
	}
	public String getLovResult() {
		return lovResult;
	}
	public void setLovResult(String lovResult) {
		this.lovResult = lovResult;
	}
	
	public boolean hasValidValues() {
		return hasValidValues;
	}
	
	public void setHasValidValues(boolean hasValidValues) {
		this.hasValidValues = hasValidValues;
	}

	
}
