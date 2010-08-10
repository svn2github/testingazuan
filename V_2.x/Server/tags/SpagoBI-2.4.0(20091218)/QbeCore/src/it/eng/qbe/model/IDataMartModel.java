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
package it.eng.qbe.model;

import java.io.Serializable;
import java.util.Map;

import it.eng.qbe.datasource.IHibernateDataSource;
import it.eng.qbe.model.accessmodality.DataMartModelAccessModality;
import it.eng.qbe.model.structure.DataMartModelStructure;
import it.eng.qbe.query.Query;


// TODO: Auto-generated Javadoc
/**
 * The Interface IDataMartModel.
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public interface IDataMartModel extends Serializable {
	
	String getName();
	
	/**
	 * Creates the statement.
	 * 
	 * @return the i statement
	 */
	//public IStatement createStatement();
	
	/**
	 * Creates the statement.
	 * 
	 * @param query the query
	 * 
	 * @return the i statement
	 */
	public IStatement createStatement(Query query);
	
	/**
	 * Gets the data mart model structure.
	 * 
	 * @return the data mart model structure
	 */
	public DataMartModelStructure getDataMartModelStructure();
	
	/**
	 * Gets the data source.
	 * 
	 * @return the data source
	 */
	public IHibernateDataSource getDataSource();
	
	/**
	 * Gets the data mart model access modality.
	 * 
	 * @return the data mart model access modality
	 */
	public DataMartModelAccessModality getDataMartModelAccessModality();
	
	/**
	 * Sets the data mart model access modality.
	 * 
	 * @param dataMartModelAccessModality the new data mart model access modality
	 */
	public void setDataMartModelAccessModality(DataMartModelAccessModality dataMartModelAccessModality);	
	
	/**
	 * Gets the data mart properties.
	 * 
	 * @return the data mart properties
	 */
	public Map getDataMartProperties();
	
	/**
	 * Sets the data mart properties.
	 * 
	 * @param dataMartProperties the new data mart properties
	 */
	public void setDataMartProperties(Map dataMartProperties);
}
