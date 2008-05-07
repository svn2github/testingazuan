/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.qbe.bo;

import it.eng.qbe.model.IDataMartModel;

import java.util.Properties;

// TODO: Auto-generated Javadoc
/**
 * The Class DatamartProperties.
 * 
 * @author Andrea Gioia
 */
public class DatamartProperties {

	/** The Constant CLASS_TYPE_TABLE. */
	public static final int CLASS_TYPE_CUBE = 1;
	
	/** The Constant CLASS_TYPE_RELATION. */
	public static final int CLASS_TYPE_DIMENSION = 2;
	
	/** The Constant CLASS_TYPE_VIEW. */
	public static final int CLASS_TYPE_VIEW = 3;
	
	/** The Constant FIELD_TYPE_UNDEFINED. */
	public static final int FIELD_TYPE_UNDEFINED = 0;
	
	/** The Constant FIELD_TYPE_MEASURE. */
	public static final int FIELD_TYPE_MEASURE = 1;
	
	/** The Constant FIELD_TYPE_DIMENSION. */
	public static final int FIELD_TYPE_ATTRIBUTE = 2;
	
	/** The Constant FIELD_TYPE_GEOREF. */
	public static final int FIELD_TYPE_GEOREF = 3;
	
	/** The qbe properties. */
	private Properties qbeProperties = null;
	
	
	
	public DatamartProperties() {
		qbeProperties = new Properties();
	}
	
	public DatamartProperties(Properties properties) {
		qbeProperties = properties;
	}
	
	/**
	 * Instantiates a new qbe properties.
	 * 
	 * @param dm the dm
	 */
	public DatamartProperties(IDataMartModel dm) {
		qbeProperties = dm.getDataMartProperties();
	}
	
	/**
	 * Checks if is table visible.
	 * 
	 * @param className the class name
	 * 
	 * @return true, if is table visible
	 */
	public boolean isTableVisible(String className) {
		if(qbeProperties == null) return true;
		
		String visiblePropertyValue = qbeProperties.getProperty(className + ".visible");
		if(visiblePropertyValue == null || visiblePropertyValue.trim().equalsIgnoreCase("true")) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks if is field visible.
	 * 
	 * @param fieldName the field name
	 * 
	 * @return true, if is field visible
	 */
	public boolean isFieldVisible(String fieldName) {
		if(qbeProperties == null) return true;
		
		String visiblePropertyValue = qbeProperties.getProperty(fieldName + ".visible");
		if(visiblePropertyValue == null || visiblePropertyValue.trim().equalsIgnoreCase("true")) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Gets the table type.
	 * 
	 * @param fieldName the field name
	 * 
	 * @return the table type
	 */
	public int getTableType(String fieldName) {
		if(qbeProperties == null) return CLASS_TYPE_DIMENSION;
		String type = qbeProperties.getProperty(fieldName + ".type");
		if(type == null) return CLASS_TYPE_DIMENSION;
		
		if(type.trim().equalsIgnoreCase("cube")) {
			return CLASS_TYPE_CUBE;
		} else if(type.trim().equalsIgnoreCase("dimension")) {
			return CLASS_TYPE_DIMENSION;
		} else if(type.trim().equalsIgnoreCase("view")) {
			return CLASS_TYPE_VIEW;
		} else {
			return CLASS_TYPE_DIMENSION;
		}
	}
	
	
	
	/**
	 * Gets the field type.
	 * 
	 * @param className the class name
	 * 
	 * @return the field type
	 */
	public int getFieldType(String className) {
		if(qbeProperties == null) return FIELD_TYPE_ATTRIBUTE;
		String type = qbeProperties.getProperty(className + ".type");
		if(type == null)return FIELD_TYPE_ATTRIBUTE;
		
		if(type.trim().equalsIgnoreCase("attribute")) {
			return FIELD_TYPE_ATTRIBUTE;
		} else if (type != null && type.trim().equalsIgnoreCase("measure")) {
			return FIELD_TYPE_MEASURE;
		} else if (type != null && type.trim().equalsIgnoreCase("georef")) {
			return FIELD_TYPE_GEOREF;
		} else {
			return FIELD_TYPE_UNDEFINED;
		}
	}
	
	/**
	 * Sets the field type.
	 * 
	 * @param className the class name
	 * @param type the type
	 */
	public void setFieldType(String className, int type) {		
		if(type == FIELD_TYPE_ATTRIBUTE) {
			qbeProperties.setProperty(className + ".type", "dimension");
		} else if(type == FIELD_TYPE_MEASURE) {
			qbeProperties.setProperty(className + ".type", "measure");
		} else if(type == FIELD_TYPE_GEOREF) {
			qbeProperties.setProperty(className + ".type", "georef");
		} else {
	
		}
	}
}
