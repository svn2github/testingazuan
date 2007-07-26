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
package it.eng.spagobi.engines.talend.services;

import it.eng.spagobi.engines.talend.SpagoBITalendEngine;
import it.eng.spagobi.engines.talend.runtime.Job;
import it.eng.spagobi.engines.talend.runtime.JobDeploymentDescriptor;
import it.eng.spagobi.engines.talend.runtime.RuntimeRepository;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

/**
 * @author Andrea Gioia
 *
 */
public class EngineInfoService extends HttpServlet {
	
	private static transient Logger logger = Logger.getLogger(EngineInfoService.class);
	
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		logger.debug("Starting EngineInfoService service method...");
		
		SpagoBITalendEngine engine = SpagoBITalendEngine.getInstance();
		
		String infoType = request.getParameter("infoType");
		if(infoType.equalsIgnoreCase("version")) {
			response.getOutputStream().write(engine.getVersion().getBytes());
		} else if(infoType.equalsIgnoreCase("complianceVersion")) {
				response.getOutputStream().write(engine.getComplianceVersion().getBytes());
		} else if (infoType.equalsIgnoreCase("name")) {
			response.getOutputStream().write(engine.getFullName().getBytes());
		} else {
			response.getOutputStream().write(engine.getInfo().getBytes());
		}
		
	}
}

