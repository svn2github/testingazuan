/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.meta.querybuilder.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import it.eng.qbe.model.structure.ModelField;

/**
 * @author cortella
 *
 */
public class HavingClause {
	private String filterName;
	private String leftFunction;
	private String leftOperand;
	private String operator;
	private String rightFunction;
	private String rightOperand;
	private boolean isForPrompt;
	private String booleanConnector;
	private ModelField dataMartField;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public HavingClause(){
		
	}
	
	public HavingClause(String filterName, String leftFunction, String leftOperand, 
			String operator, String rightFunction, String rightOperand, 
			boolean isForPrompt,String booleanConnector,
			ModelField dataMartField){
		this.setFilterName(filterName);
		this.setLeftFunction(leftFunction);
		this.setLeftOperand(leftOperand);
		this.setOperator(operator);
		this.setRightFunction(rightFunction);
		this.setRightOperand(rightOperand);
		this.setForPrompt(isForPrompt);
		this.setBooleanConnector(booleanConnector);
		this.setDataMartField(dataMartField);
	}

	/**
	 * @param filterName the filterName to set
	 */
	public void setFilterName(String filterName) {
		propertyChangeSupport.firePropertyChange("filterName", this.filterName,
				this.filterName = filterName);
	}

	/**
	 * @return the filterName
	 */
	public String getFilterName() {
		return filterName;
	}

	/**
	 * @param leftOperand the leftOperand to set
	 */
	public void setLeftOperand(String leftOperand) {
		propertyChangeSupport.firePropertyChange("leftOperand", this.leftOperand,
				this.leftOperand = leftOperand);
	}

	/**
	 * @return the leftOperand
	 */
	public String getLeftOperand() {
		return leftOperand;
	}

	/**
	 * @param operator the operator to set
	 */
	public void setOperator(String operator) {
		propertyChangeSupport.firePropertyChange("operator", this.operator,
				this.operator = operator);
	}

	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @param rightOperand the rightOperand to set
	 */
	public void setRightOperand(String rightOperand) {
		propertyChangeSupport.firePropertyChange("rightOperand", this.rightOperand,
				this.rightOperand = rightOperand);
	}

	/**
	 * @return the rightOperand
	 */
	public String getRightOperand() {
		return rightOperand;
	}

	/**
	 * @param isForPrompt the isForPrompt to set
	 */
	public void setForPrompt(boolean isForPrompt) {
		propertyChangeSupport.firePropertyChange("isForPrompt", this.isForPrompt,
				this.isForPrompt = isForPrompt);
	}

	/**
	 * @return the isForPrompt
	 */
	public boolean isForPrompt() {
		return isForPrompt;
	}

	/**
	 * @param booleanConnector the booleanConnector to set
	 */
	public void setBooleanConnector(String booleanConnector) {
		propertyChangeSupport.firePropertyChange("booleanConnector", this.booleanConnector,
				this.booleanConnector = booleanConnector);
	}

	/**
	 * @return the booleanConnector
	 */
	public String getBooleanConnector() {
		return booleanConnector;
	}

	/**
	 * @param dataMartField the dataMartField to set
	 */
	public void setDataMartField(ModelField dataMartField) {
		propertyChangeSupport.firePropertyChange("dataMartField", this.dataMartField,
				this.dataMartField = dataMartField);
	}

	/**
	 * @return the dataMartField
	 */
	public ModelField getDataMartField() {
		return dataMartField;
	}
	
	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	/**
	 * @param leftFunction the leftFunction to set
	 */
	public void setLeftFunction(String leftFunction) {
		propertyChangeSupport.firePropertyChange("leftFunction", this.leftFunction,
				this.leftFunction = leftFunction);
	}

	/**
	 * @return the leftFunction
	 */
	public String getLeftFunction() {
		return leftFunction;
	}

	/**
	 * @param rightFunction the rightFunction to set
	 */
	public void setRightFunction(String rightFunction) {
		propertyChangeSupport.firePropertyChange("rightFunction", this.rightFunction,
				this.rightFunction = rightFunction);
	}

	/**
	 * @return the rightFunction
	 */
	public String getRightFunction() {
		return rightFunction;
	}
}
