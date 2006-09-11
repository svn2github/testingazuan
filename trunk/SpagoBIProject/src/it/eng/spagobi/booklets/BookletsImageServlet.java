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
 * Created on 4-mag-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.booklets;


import it.eng.spagobi.booklets.constants.BookletsConstants;
import it.eng.spagobi.booklets.dao.BookletsCmsDaoImpl;
import it.eng.spagobi.booklets.dao.IBookletsCmsDao;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;


public class BookletsImageServlet extends HttpServlet{
	
	public void init(ServletConfig config) throws ServletException {
        super.init(config);
     } 

	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		OutputStream out = null;
		String task = "";
		try{
	 		task = (String)request.getParameter("task");		
	 		out = response.getOutputStream();
	 		if(task.equalsIgnoreCase("getTemplateImage")){
	 			String pathimg = (String)request.getParameter("pathimg");
			 	if(pathimg!=null) {
				 	File imgFile = new File(pathimg);
				 	FileInputStream fis = new FileInputStream(imgFile);
				 	byte[] content = GeneralUtilities.getByteArrayFromInputStream(fis);
				 	out.write(content);
				 	out.flush();
		            fis.close();
		            imgFile.delete();
		            return;
			 	} 
	 		} else if(task.equalsIgnoreCase("downloadFinalDocument")){
	 			String activityKey = request.getParameter("ActivityKey");
	 			JbpmConfiguration jbpmConfiguration = JbpmConfiguration.getInstance();
	 	    	JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
	 			long activityKeyId = Long.valueOf(activityKey).longValue();
	 			TaskInstance taskInstance = jbpmContext.getTaskInstance(activityKeyId);
	 			ContextInstance contextInstance = taskInstance.getContextInstance();
	 			String pathConfBook = (String)contextInstance.getVariable(BookletsConstants.PATH_BOOKLET_CONF);
	 			jbpmContext.close();
	 			IBookletsCmsDao bookdao = new BookletsCmsDaoImpl();
		 		String bookName = bookdao.getBookletTemplateFileName(pathConfBook);
		 		byte[] finalDocBytes = bookdao.getCurrentPresentationContent(pathConfBook);
			 	response.setHeader("Content-Disposition","attachment; filename=\"" + bookName + ".ppt" + "\";");
	 			response.setContentLength(finalDocBytes.length);
	 			out.write(finalDocBytes);
	 			out.flush();
	            return;
		 		
	 		} else if(task.equalsIgnoreCase("downloadPresentationVersion")) {
	 			String pathBook = request.getParameter(BookletsConstants.PATH_BOOKLET_CONF);
                String verName =  request.getParameter(BookletsConstants.BOOKLET_PRESENTATION_VERSION_NAME);
	 			IBookletsCmsDao bookdao = new BookletsCmsDaoImpl();
	 			byte[] finalDocBytes = bookdao.getPresentationVersionContent(pathBook, verName);
	 			String bookName = bookdao.getBookletTemplateFileName(pathBook);
	 			response.setHeader("Content-Disposition","attachment; filename=\"" + bookName + ".ppt" + "\";");
	 			response.setContentLength(finalDocBytes.length);
	 			out.write(finalDocBytes);
	 			out.flush();
	            return;
	 		}
	 		
	 	}catch(Exception e){
	 		SpagoBITracer.major("SpagoBI",getClass().getName(),
	 				               "service","Exception during execution of task " + task, e);
	 	}
	 }
	
}
