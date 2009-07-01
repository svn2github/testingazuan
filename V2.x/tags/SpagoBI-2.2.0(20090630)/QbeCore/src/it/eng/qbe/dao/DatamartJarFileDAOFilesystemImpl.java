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

import it.eng.qbe.bo.DatamartJarFile;

import java.io.File;

// TODO: Auto-generated Javadoc
/**
 * The Class DatamartJarFileDAOFilesystemImpl.
 * 
 * @author Andrea Gioia
 */
public class DatamartJarFileDAOFilesystemImpl implements DatamartJarFileDAO {

	/** The datamarts dir. */
	private File datamartsDir;
	
	/**
	 * Instantiates a new datamart jar file dao filesystem impl.
	 * 
	 * @param datamartsDir the datamarts dir
	 */
	public DatamartJarFileDAOFilesystemImpl(File datamartsDir) {
		this.setDatamartsDir(datamartsDir);
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.dao.DatamartJarFileDAO#loadDatamartJarFile(java.lang.String)
	 */
	public DatamartJarFile loadDatamartJarFile(String datamartName) {
		DatamartJarFile jarFile = null;
		File targetDatamartDir = null;
		File datamartJarFile = null;
		
		targetDatamartDir = new File(getDatamartsDir(), datamartName);
		datamartJarFile = new File(targetDatamartDir, "datamart.jar");
		
		if (datamartJarFile.exists()) {
			jarFile = new DatamartJarFile(datamartJarFile);
		}

		return jarFile;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.dao.DatamartJarFileDAO#saveDatamartJarFile(java.lang.String, it.eng.qbe.bo.DatamartJarFile)
	 */
	public void saveDatamartJarFile(String datamartName, DatamartJarFile jarFile) {
		
	}
	
	

	/**
	 * Gets the datamarts dir.
	 * 
	 * @return the datamarts dir
	 */
	private File getDatamartsDir() {
		return datamartsDir;
	}

	/**
	 * Sets the datamarts dir.
	 * 
	 * @param datamartsDir the new datamarts dir
	 */
	private void setDatamartsDir(File datamartsDir) {
		this.datamartsDir = datamartsDir;
	}

}
