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

import it.eng.qbe.datasource.IHibernateDataSource;
import it.eng.qbe.model.accessmodality.DataMartModelAccessModality;
import it.eng.qbe.model.structure.DataMartModelStructure;

import java.io.Serializable;
import java.util.Properties;


/**
 * @author Andrea Gioia
 *
 */
public interface IDataMartModel extends Serializable {
	public IStatement createStatement();
	public IStatement createStatement(IQuery query);
	
	public DataMartModelStructure getDataMartModelStructure();
	public IHibernateDataSource getDataSource();
	
	public DataMartModelAccessModality getDataMartModelAccessModality();
	public void setDataMartModelAccessModality(DataMartModelAccessModality dataMartModelAccessModality);	
	
	public Properties getDataMartProperties();
	public void setDataMartProperties(Properties dataMartProperties);
}
