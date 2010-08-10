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
package it.eng.qbe.utility;

import java.io.File;
import java.util.List;

/**
 * @author Andrea Zoppello
 * 
 * This is the interface for classes that implements 
 * logig to retrieve the datamart model file given a path
 * 
 */
public interface IDataMartModelRetriever {

	/**
	 * @param dataMartPath
	 * @return the default datamart file
	 */
	public File getJarFile(String dataMartPath);
	
	/**
	 * @param dataMartPath
	 * @param dialect
	 * @return the specific file of datamart given Hibernate Dialect
	 */
	public File getJarFile(String dataMartPath, String dialect);
	
	public List getViewJarFiles(String dataMartPath, String dialect);
}
