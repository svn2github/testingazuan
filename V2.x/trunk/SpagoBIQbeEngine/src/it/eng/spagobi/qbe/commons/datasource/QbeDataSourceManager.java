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

import it.eng.qbe.conf.QbeEngineConf;
import it.eng.qbe.datasource.CompositeHibernateDataSource;
import it.eng.qbe.datasource.DBConnection;
import it.eng.qbe.datasource.DataSourceCache;
import it.eng.qbe.datasource.DataSourceFactory;
import it.eng.qbe.datasource.DataSourceManager;
import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.naming.NamingStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andrea Gioia
 *
 */
public class QbeDataSourceManager implements DataSourceManager {
	
	private NamingStrategy namingStartegy = null;
	private DataSourceCache dataSourceCache = null;
		
	private static QbeDataSourceManager instance = null;
	
	public static QbeDataSourceManager getInstance() {
		if(instance == null) {
			NamingStrategy namingStartegy = QbeEngineConf.getInstance().getNamingStrategy();
			DataSourceCache dataSourceCache = QbeEngineConf.getInstance().getDataSourceCache();
			instance = new QbeDataSourceManager(namingStartegy, dataSourceCache);
		}
		
		return instance;
	}
	
	private QbeDataSourceManager(NamingStrategy namingStartegy, DataSourceCache dataSourceCache) {
		setNamingStartegy(namingStartegy);
		setDataSourceCache(dataSourceCache);
	}
	
	
	
	public IDataSource getDataSource(List dataMartNames, DBConnection connection) {
		return getDataSource(dataMartNames, new HashMap(), connection);
	}
	
	public IDataSource getDataSource(List dataMartNames, Map dblinkMap, DBConnection connection) {
		
		IDataSource dataSource = null;
		String dataSourceName = null;
		String dataMartName = null;
		
		
		dataSourceName = getNamingStartegy().getDatasourceName(dataMartNames, connection);
		dataMartName = getNamingStartegy().getDatamartName(dataMartNames);
		
		dataSource = getDataSourceCache().getDataSource(dataSourceName);
		
		
		//dataSource = null;
		if (dataSource == null) {
			dataSource = DataSourceFactory.buildDataSource(dataSourceName, dataMartName, dataMartNames, dblinkMap, connection);
			getDataSourceCache().addDataSource(dataSourceName, dataSource);
		} else if(dataSource instanceof CompositeHibernateDataSource) {
			CompositeHibernateDataSource compositeHibernateDataSource = (CompositeHibernateDataSource)dataSource;
			compositeHibernateDataSource.refreshDatamartViews();
		}
		
		return dataSource;
	}

	private DataSourceCache getDataSourceCache() {
		return dataSourceCache;
	}

	private void setDataSourceCache(DataSourceCache dataSourceCache) {
		this.dataSourceCache = dataSourceCache;
	}

	private NamingStrategy getNamingStartegy() {
		return namingStartegy;
	}

	private void setNamingStartegy(NamingStrategy namingStartegy) {
		this.namingStartegy = namingStartegy;
	}
}
