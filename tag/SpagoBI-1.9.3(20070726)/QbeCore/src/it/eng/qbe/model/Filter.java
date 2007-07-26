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
package it.eng.qbe.model;

import it.eng.qbe.utility.StringUtils;

import java.io.IOException;
import java.util.Set;

/**
 * @author Andrea Gioia
 *
 */
public class Filter {
	String entityName;
	String filterCondition;
	Set parameters;
	Set fields;
	
	public Filter(String entityName, String filterCondition) {
		this.entityName = entityName;
		this.filterCondition = filterCondition;
		try {
			this.fields = StringUtils.getParameters(filterCondition, "F");
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			this.parameters = StringUtils.getParameters(filterCondition, "P");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getFilterCondition() {
		return filterCondition;
	}

	public void setFilterCondition(String filterCondition) {
		this.filterCondition = filterCondition;
	}

	public Set getFields() {
		return fields;
	}

	public Set getParameters() {
		return parameters;
	}
}
