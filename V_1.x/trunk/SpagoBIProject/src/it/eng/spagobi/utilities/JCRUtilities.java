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
/*
 * Created on 7-lug-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.utilities;

import it.eng.spago.cms.CmsManager;
import it.eng.spago.cms.CmsNode;
import it.eng.spago.cms.operations.GetOperation;
import it.eng.spago.error.EMFInternalError;

import java.io.InputStream;

/**
 * Contains some JCR Utilities
 */
public class JCRUtilities {
	/**
	 * Gets content information known the jcr path
	 * 
	 * @param jcrPath The input jcr path string
	 * @return Content information (contained into an Input Stream Object)
	 * @throws EMFInternalError If any exception occurred
	 */
	public static InputStream getContentByPath(String jcrPath)
			throws EMFInternalError {
		try {
			SpagoBITracer.critical("SpagoBiUtilities", 
									JCRUtilities.class.getName(), 
									"getContentByPath","Retrieving content ["+ jcrPath+"]");
            CmsManager manager = new CmsManager();
			GetOperation getOp = new GetOperation(); 
			getOp.setPath(jcrPath);
			getOp.setRetriveContentInformation("true");
			getOp.setRetrivePropertiesInformation("false");
			getOp.setRetriveVersionsInformation("false");
			getOp.setRetriveChildsInformation("false");
			CmsNode cmsnode = manager.execGetOperation(getOp);
			return cmsnode.getContent();
		} catch (Exception e) {
			SpagoBITracer.critical("SpagoBiUtilities", JCRUtilities.class.getName(), "getContentByPath","Excecption occurred ", e);
			throw new EMFInternalError("Excecption", e);
		}
	}
	
	
	
	public static InputStream getContentByPathAndVersion(String jcrPath, String version) throws EMFInternalError {
		try {
			SpagoBITracer.critical("SpagoBiUtilities", JCRUtilities.class.getName(), 
					               "getContentByPathAndVersion","Retrieving content ["+ jcrPath+"]");
			
			CmsManager manager = new CmsManager();
			GetOperation getOp = new GetOperation(); 
			getOp.setPath(jcrPath);
			getOp.setVersion(version);
			getOp.setRetriveContentInformation("true");
			getOp.setRetrivePropertiesInformation("false");
			getOp.setRetriveVersionsInformation("false");
			getOp.setRetriveChildsInformation("false");
			CmsNode cmsnode = manager.execGetOperation(getOp);
			return cmsnode.getContent();
		} catch (Exception e) {
			SpagoBITracer.critical("SpagoBiUtilities", JCRUtilities.class.getName(), "getContentByPathAndVersion","Excecption occurred ", e);
			throw new EMFInternalError("Excecption", e);
		}
	}
	
	
}
