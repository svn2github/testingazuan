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
package it.eng.qbe.model.structure;

// TODO: Auto-generated Javadoc
/**
 * The Class DataMartField.
 * 
 * @author Andrea Gioia
 */
public class DataMartField {
	
	// wbn: Would Be Nice
	
	/** The id. */
	private long id;
	
	/** The name. */
	private String name;  					// -> product.productClass.productFamily :wbn: -> productFamily
	
	/** The datamart name. */
	private String datamartName;			// -> hqlFoodmart
	
	/** The key. */
	private boolean key;
	
	/** The type. */
	String type;
	
	/** The length. */
	int length;
	
	/** The precision. */
	int precision;
	
	/** The parent. */
	DataMartEntity parent;
	
	
	
	/**
	 * Instantiates a new data mart field.
	 * 
	 * @param name the name
	 * @param parent the parent
	 */
	public DataMartField(String name, DataMartEntity parent) {
		setId( parent.getStructure().getNextId() );		
		setName(name);
		setParent(parent) ;		
	}

	/**
	 * Gets the unique name.
	 * 
	 * @return the unique name
	 */
	public String getUniqueName() {
		if(getParent().getParent() == null) {
			return getParent().getType() + ":" + getName();
		}
		return getParent().getUniqueName() + ":" + getName();
	}
	
	/**
	 * Gets the query name.
	 * 
	 * @return the query name
	 * 
	 * 
	 */
	public String getQueryName() {
		String fieldName = "";
		
		DataMartEntity entity = getParent();
		if(entity.getParent() != null) {
			fieldName = toLowerCase( entity.getName() );
			entity = entity.getParent();
		}
		while(entity.getParent() != null) {
			fieldName = toLowerCase( entity.getName() ) + "." + fieldName;
			entity = entity.getParent();
		}		
		if(!fieldName.equalsIgnoreCase("")) fieldName +=  ".";
		fieldName += getName();
		
		return fieldName;
	}
	
	/**
	 * To lower case.
	 * 
	 * @param str the str
	 * 
	 * @return the string
	 */
	private String toLowerCase(String str) {
		String head = str.substring(0,1);
		String tail = str.substring(1, str.length());
		
		return head.toLowerCase() + tail;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name + "(id="+id
		+"; parent:" + (parent==null?"NULL": parent.getName())
		+"; type="+type
		+"; length="+length
		+"; precision="+precision
		+")";
	}



	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public long getId() {
		return id;
	}



	/**
	 * Sets the id.
	 * 
	 * @param id the new id
	 */
	public void setId(long id) {
		this.id = id;
	}



	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}



	/**
	 * Sets the name.
	 * 
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	



	/**
	 * Gets the datamart name.
	 * 
	 * @return the datamart name
	 */
	public String getDatamartName() {
		return datamartName;
	}



	/**
	 * Sets the datamart name.
	 * 
	 * @param datamartName the new datamart name
	 */
	public void setDatamartName(String datamartName) {
		this.datamartName = datamartName;
	}



	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}



	/**
	 * Sets the type.
	 * 
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
	}



	/**
	 * Gets the length.
	 * 
	 * @return the length
	 */
	public int getLength() {
		return length;
	}



	/**
	 * Sets the length.
	 * 
	 * @param length the new length
	 */
	public void setLength(int length) {
		this.length = length;
	}



	/**
	 * Gets the precision.
	 * 
	 * @return the precision
	 */
	public int getPrecision() {
		return precision;
	}



	/**
	 * Sets the precision.
	 * 
	 * @param precision the new precision
	 */
	public void setPrecision(int precision) {
		this.precision = precision;
	}



	/**
	 * Gets the parent.
	 * 
	 * @return the parent
	 */
	public DataMartEntity getParent() {
		return parent;
	}



	/**
	 * Sets the parent.
	 * 
	 * @param parent the new parent
	 */
	public void setParent(DataMartEntity parent) {
		this.parent = parent;
	}

	/**
	 * Checks if is key.
	 * 
	 * @return true, if is key
	 */
	public boolean isKey() {
		return key;
	}

	/**
	 * Sets the key.
	 * 
	 * @param key the new key
	 */
	public void setKey(boolean key) {
		this.key = key;
	}
}
