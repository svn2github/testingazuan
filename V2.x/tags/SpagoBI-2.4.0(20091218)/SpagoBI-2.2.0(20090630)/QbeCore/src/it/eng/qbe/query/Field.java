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
package it.eng.qbe.query;


// TODO: Auto-generated Javadoc
/**
 * The Class Field.
 */
public class Field implements IField {
	
	/** The id. */
	private String id;
	
	/** The field name. */
	private String fieldName = null;
	
	/** The type. */
	private String type = null;
	
	/** The scale. */
	private Integer scale = null;
	
	/** The precision. */
	private Integer precision = null;
	
	/**
	 * Instantiates a new field.
	 * 
	 * @param id the id
	 */
	public Field(String id) {
		setId(id);
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IField#getId()
	 */
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IField#setId(java.lang.String)
	 */
	public void setId(String id) {
		this.id = id;
	}	
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IField#getFieldName()
	 */
	public String getFieldName() {
		return fieldName;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IField#setFieldName(java.lang.String)
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IField#getType()
	 */
	public String getType() {
		return type;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IField#setType(java.lang.String)
	 */
	public void setType(String type) {
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IField#getScale()
	 */
	public Integer getScale() {
		return scale;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IField#setScale(java.lang.Integer)
	 */
	public void setScale(Integer scale) {
		this.scale = scale;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IField#getPrecision()
	 */
	public Integer getPrecision() {
		return precision;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IField#setPrecision(java.lang.Integer)
	 */
	public void setPrecision(Integer precision) {
		this.precision = precision;
	}
}
