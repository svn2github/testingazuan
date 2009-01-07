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
package it.eng.spagobi.tools.dataset.common.dataproxy;

import java.io.FileInputStream;

import org.apache.log4j.Logger;

import it.eng.spago.error.EMFUserError;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class FileDataProxy implements IDataProxy {
	
	String fileName;
	
	private static transient Logger logger = Logger.getLogger(FileDataProxy.class);
	
	
	public FileDataProxy() {
		
	}
			
	public FileDataProxy(String fileName) {
		
	}
	
	public Object load(String statement) throws EMFUserError {
		throw new UnsupportedOperationException("metothd FileDataProxy not yet implemented");
	}
	
	public Object load() throws EMFUserError {
		FileInputStream inputStream = null;
		
		try{
			inputStream = new FileInputStream(fileName);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.debug("File not found",e);
		}
		
		return inputStream;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
