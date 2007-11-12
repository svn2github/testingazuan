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
package it.eng.qbe.model.structure;

/**
 * @author Andrea Gioia
 *
 */
public class DataMartField {
	
	long id;
	String name;
	String path;
	DataMartEntity parent;
	String type;
	int length;
	int precision;
	
	public DataMartField(String name, DataMartEntity parent) {
		this.name = name;
		this.parent = parent;
		this.id = parent.getStructure().getNextId();
	}

	public String getName() {
		return name;
	}
	
	public String toString() {
		return name + "(id="+id
		+"; path="+path
		+"; parent:" + (parent==null?"NULL": parent.getName())
		+"; type="+type
		+"; length="+length
		+"; precision="+precision
		+")";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public DataMartEntity getParent() {
		return parent;
	}

	public void setParent(DataMartEntity parent) {
		this.parent = parent;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
