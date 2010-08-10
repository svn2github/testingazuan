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
package it.eng.qbe.dao;

import it.eng.qbe.conf.QbeConf;

import java.io.File;

// TODO: Auto-generated Javadoc
/**
 * The Class DAOFactory.
 * 
 * @author Andrea Gioia
 */
public class DAOFactory {
	
	/**
	 * Gets the formula dao.
	 * 
	 * @return the formula dao
	 */
	public static FormulaDAO getFormulaDAO() {
		File datamartsDir = QbeConf.getInstance().getQbeDataMartDir();
		return new FormulaDAOFilesystemImpl(datamartsDir);
	}
	
	/**
	 * Gets the datamart jar file dao.
	 * 
	 * @return the datamart jar file dao
	 */
	public static DatamartJarFileDAO getDatamartJarFileDAO() {
		File datamartsDir = QbeConf.getInstance().getQbeDataMartDir();
		return new DatamartJarFileDAOFilesystemImpl(datamartsDir);
	}
	
	/**
	 * Gets the view jar file dao.
	 * 
	 * @return the view jar file dao
	 */
	public static ViewJarFileDAO getViewJarFileDAO() {
		File datamartsDir = QbeConf.getInstance().getQbeDataMartDir();
		return new ViewJarFileDAOFilesystemImpl(datamartsDir);
	}
	
	/**
	 * Gets the datamart properties dao.
	 * 
	 * @return the datamart properties dao
	 */
	public static DatamartPropertiesDAO getDatamartPropertiesDAO() {
		return new DatamartPropertiesDAOFilesystemImpl();
	}
	
	/**
	 * Gets the datamart labels dao.
	 * 
	 * @return the datamart labels dao
	 */
	public static DatamartLabelsDAO getDatamartLabelsDAO() {
		return new DatamartLabelsDAOFilesystemImpl();
	}
}
