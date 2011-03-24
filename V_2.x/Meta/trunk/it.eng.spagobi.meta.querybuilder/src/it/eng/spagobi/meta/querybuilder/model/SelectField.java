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
import it.eng.qbe.model.structure.*;



/**
 * @author cortella
 *
 */
public class SelectField {
	private String entity;
	private String field;
	private String alias;
	private String function;
	private String order;
	private boolean group;
	private boolean include;
	private boolean visible;
	private boolean filter;
	private boolean having;
	private IModelField dataMartField;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	public SelectField(){
		
	}
	
	public SelectField(String entity, String field, String alias, 
			String function, String order, boolean group, boolean include,
			boolean visible, boolean filter, boolean having, IModelField dataMartField){
		super();
		this.setEntity(entity);
		this.setField(field);
		this.setAlias(alias);
		this.setFunction(function);
		this.setOrder(order);
		this.setGroup(group);
		this.setInclude(include);
		this.setVisible(visible);
		this.setFilter(filter);
		this.setHaving(having);
		this.setDataMartField(dataMartField);
	}
	
	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(String entity) {
		propertyChangeSupport.firePropertyChange("entity", this.entity,
				this.entity = entity);
	}

	/**
	 * @return the entity
	 */
	public String getEntity() {
		return entity;
	}

	/**
	 * @param field the field to set
	 */
	public void setField(String field) {
		propertyChangeSupport.firePropertyChange("field", this.field,
				this.field = field);
	}

	/**
	 * @return the field
	 */
	public String getField() {
		return field;
	}

	/**
	 * @param alias the alias to set
	 */
	public void setAlias(String alias) {
		propertyChangeSupport.firePropertyChange("alias", this.alias,
				this.alias = alias);
	}

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param function the function to set
	 */
	public void setFunction(String function) {
		propertyChangeSupport.firePropertyChange("function", this.function,
				this.function = function);
	}

	/**
	 * @return the function
	 */
	public String getFunction() {
		return function;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(String order) {
		propertyChangeSupport.firePropertyChange("order", this.order,
				this.order = order);
	}

	/**
	 * @return the order
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(boolean group) {
		propertyChangeSupport.firePropertyChange("group", this.group,
				this.group = group);
	}

	/**
	 * @return the group
	 */
	public boolean isGroup() {
		return group;
	}

	/**
	 * @param include the include to set
	 */
	public void setInclude(boolean include) {
		propertyChangeSupport.firePropertyChange("include", this.include,
				this.include = include);
	}

	/**
	 * @return the include
	 */
	public boolean isInclude() {
		return include;
	}

	/**
	 * @param visible the visible to set
	 */
	public void setVisible(boolean visible) {
		propertyChangeSupport.firePropertyChange("visible", this.visible,
				this.visible = visible);
	}

	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param filter the filter to set
	 */
	public void setFilter(boolean filter) {
		propertyChangeSupport.firePropertyChange("filter", this.filter,
				this.filter = filter);
	}

	/**
	 * @return the filter
	 */
	public boolean isFilter() {
		return filter;
	}

	/**
	 * @param having the having to set
	 */
	public void setHaving(boolean having) {
		propertyChangeSupport.firePropertyChange("having", this.having,
				this.having = having);
	}

	/**
	 * @return the having
	 */
	public boolean isHaving() {
		return having;
	}

	/**
	 * @param dataMartField the dataMartField to set
	 */
	public void setDataMartField(IModelField dataMartField) {
		propertyChangeSupport.firePropertyChange("dataMartField", this.dataMartField,
				this.dataMartField = dataMartField);
	}

	/**
	 * @return the dataMartField
	 */
	public IModelField getDataMartField() {
		return dataMartField;
	}
	
	
}
