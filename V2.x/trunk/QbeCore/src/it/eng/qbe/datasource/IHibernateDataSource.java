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
package it.eng.qbe.datasource;

import it.eng.qbe.bo.Formula;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

// TODO: Auto-generated Javadoc
/**
 * The Interface IHibernateDataSource.
 */
public interface IHibernateDataSource extends IDataSource {

	/**
	 * Gets the datamart name.
	 * 
	 * @return the datamart name
	 */
	String getDatamartName();
	
	/**
	 * Gets the datamart names.
	 * 
	 * @return the datamart names
	 */
	List getDatamartNames();
	
	/**
	 * Gets the connection.
	 * 
	 * @return the connection
	 */
	DBConnection getConnection();
	
	/**
	 * Gets the configuration.
	 * 
	 * @return the configuration
	 */
	Configuration getConfiguration();	
	
	/**
	 * Gets the session factory.
	 * 
	 * @return the session factory
	 */
	SessionFactory getSessionFactory();
	
	/**
	 * Gets the session factory.
	 * 
	 * @param dmName the dm name
	 * 
	 * @return the session factory
	 */
	SessionFactory getSessionFactory(String dmName);

	/**
	 * Gets the formula.
	 * 
	 * @return the formula
	 */
	Formula getFormula();
	
	/**
	 * Sets the formula.
	 * 
	 * @param formula the new formula
	 */
	void setFormula(Formula formula);
		
	
	/**
	 * Refresh datamart views.
	 */
	void refreshDatamartViews();	
	
	/**
	 * Refresh shared views.
	 */
	void refreshSharedViews();	
	
	/**
	 * Refresh shared view.
	 * 
	 * @param sharedViewName the shared view name
	 */
	void refreshSharedView(String sharedViewName);
	
	/**
	 * Refresh.
	 */
	void refresh();		
}