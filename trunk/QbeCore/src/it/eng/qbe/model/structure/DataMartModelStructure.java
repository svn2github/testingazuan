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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Andrea Gioia
 *
 */
public class DataMartModelStructure {
	Map entities;
	
	public DataMartModelStructure() {
		entities = new HashMap();
	}
	
	public void addEntity(DataMartEntity entity) {
		entities.put(entity.getName(), entity);
	}
	
	public DataMartEntity getEntity(String entityName) {
		return (DataMartEntity)entities.get(entityName);
	}
	
	
	
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		String key = null;
		for(Iterator it = entities.keySet().iterator(); it.hasNext();) {
			key = (String)it.next();
			DataMartEntity o = (DataMartEntity)entities.get(key);
			buffer.append("\n------------------------------------\n");
			if(o==null)
				buffer.append(key + " --> NULL\n");
			else
				buffer.append(o.toString() + "\n");
		}
		return buffer.toString();
	}
}
