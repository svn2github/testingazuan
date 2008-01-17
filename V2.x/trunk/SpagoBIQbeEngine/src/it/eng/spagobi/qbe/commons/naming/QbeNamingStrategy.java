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
package it.eng.spagobi.qbe.commons.naming;

import it.eng.qbe.datasource.DBConnection;
import it.eng.qbe.naming.NamingStrategy;

import java.util.List;

/**
 * @author Andrea Gioia
 *
 */
public class QbeNamingStrategy implements NamingStrategy {
	
	public static final String DATAMART_NAME_SUFFIX = "DM";
	public static final String DATASOURCE_NAME_SUFFIX = "DS";
	public static final String STRING_SEPARETOR = "_";
	
	public String getDatamartName(List datamartNames) {
		String datamartName = getDatamartUnqualifiedName(datamartNames);
		return datamartName;
	}	
	
	private String getDatamartUnqualifiedName(List datamartNames) {
		String name = null;
				
		name = "";
		for(int i = 0; i < datamartNames.size(); i++) {
			name += (i==0?"":"_") + (String)datamartNames.get(i);
		}
		
		if(datamartNames.size()>1){
			name = "_" + name;		
		}
		
		return name;
	}
	
	private String getDatasourceUnqualifiedName(List datamartNames, DBConnection connection) {
		String datasourceName = getDatamartName(datamartNames);
		datasourceName += "@" + connection.getName();
		return datasourceName;
	}
	
	public String getDatasourceName(List datamartNames, DBConnection connection) {
		String datasourceName = getDatasourceUnqualifiedName(datamartNames, connection);
		return datasourceName + STRING_SEPARETOR + DATASOURCE_NAME_SUFFIX;
	}	
}
