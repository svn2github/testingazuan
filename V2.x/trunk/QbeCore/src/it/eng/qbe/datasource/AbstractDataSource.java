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
package it.eng.qbe.datasource;

import it.eng.qbe.bo.DatamartLabels;
import it.eng.qbe.bo.DatamartProperties;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractDataSource.
 * 
 * @author Andrea Gioia
 */
public class AbstractDataSource implements IDataSource {
	
	/** The name. */
	private String name;
	
	/** The type. */
	private int type;
	
	/** The properties. */
	private DatamartProperties properties = null;	
	
	
	
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.datasource.IDataSource#getName()
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
	 * Sets the type.
	 * 
	 * @param type the new type
	 */
	protected void setType(int type) {
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.datasource.IDataSource#getType()
	 */
	public int getType() {
		return type;
	}
	
	
	// =========================================================================================================
	// Qbe properties
	// =========================================================================================================
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.datasource.IDataSource#getProperties()
	 */
	public DatamartProperties getProperties() {
		return properties;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.datasource.IDataSource#setProperties(it.eng.qbe.bo.DatamartProperties)
	 */
	public void setProperties(DatamartProperties properties) {
		this.properties = properties;
	}
	
}
