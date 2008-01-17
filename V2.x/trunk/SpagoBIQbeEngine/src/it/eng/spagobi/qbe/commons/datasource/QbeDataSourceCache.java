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
package it.eng.spagobi.qbe.commons.datasource;

import it.eng.qbe.datasource.AbstractDataSource;
import it.eng.qbe.datasource.DataSourceCache;
import it.eng.qbe.datasource.IDataSource;
import it.eng.spago.base.ApplicationContainer;

import java.util.List;

/**
 * @author Andrea Gioia
 *
 */
public class QbeDataSourceCache implements DataSourceCache {

	private static QbeDataSourceCache instance = null;
	
	public static QbeDataSourceCache getInstance() {
		if(instance == null) {
			instance = new QbeDataSourceCache();
		}
		return instance;
	}
	
	private QbeDataSourceCache() {
		
	}
	
	public void clearCache() {
		// TODO Auto-generated method stub
	}

	public IDataSource getDataSource(String datasourceName) {
		IDataSource datasource = null;
		
		ApplicationContainer applicationContainer = ApplicationContainer.getInstance();
		datasource = (IDataSource) applicationContainer.getAttribute(datasourceName);
		
		return datasource;
	}

	public List getDataSources() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addDataSource(String datasourceName, IDataSource dataSource) {
		ApplicationContainer applicationContainer = ApplicationContainer.getInstance();
		applicationContainer.setAttribute(datasourceName, dataSource);
		
	}

	public void delDataSource(String datasourceName) {
		ApplicationContainer applicationContainer = ApplicationContainer.getInstance();
		applicationContainer.delAttribute(datasourceName);
	}

}
