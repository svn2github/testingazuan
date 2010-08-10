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
package it.eng.qbe.datasource;

import java.util.List;

import it.eng.qbe.model.BasicStatement;
import it.eng.qbe.model.IQuery;
import it.eng.qbe.model.IStatement;

/**
 * @author Andrea Gioia
 *
 */
public class BasicDataSource implements IDataSource {
	int type;
		
	protected void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}
	
	public static String buildDatamartName(Object datamartRefs) {
		return  buildDatamartUnqualifiedName(datamartRefs);
	}
	
	public static String buildDatamartDescription(Object datamartRefs) {		
		return buildDatamartUnqualifiedName(datamartRefs);
	}
	
	private static String buildDatamartUnqualifiedName(Object datamartRefs) {
		String name = null;
		
		if(datamartRefs instanceof String) {
			name = (String)datamartRefs;
		} else if(datamartRefs instanceof List) {
			List list = (List)datamartRefs;
			name = "";
			for(int i = 0; i < list.size(); i++) {
				name += (i==0?"":"_") + (String)list.get(i);
			}
			name = "_" + name;
		} else {
			// error: not supported datamartRefType -> return null
		}
		
		return name;
	}
	
	public static String buildDatasourceName(Object datamartRefs) {
		String datamartName = buildDatamartName(datamartRefs);
		return datamartName + "_" + "ds";
	}
}
