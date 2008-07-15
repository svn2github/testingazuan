/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.container;

import java.util.List;

public interface IContainer {

	public boolean isNull(String key);
	
	public boolean isBlankOrNull(String key);
	
	public Object get(String key);
	
	public String getString(String key);
	
	public Boolean getBoolean(String key);
	
	public Integer getInteger(String key);
	
	public List getList(String key);
	
	public List getKeys();
	
	public void set(String key, Object value);
	
	public void remove(String key);
	
}
