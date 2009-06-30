/**
Copyright (c) 2005-2008, Engineering Ingegneria Informatica s.p.a.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this list of 
      conditions and the following disclaimer.
      
    * Redistributions in binary form must reproduce the above copyright notice, this list of 
      conditions and the following disclaimer in the documentation and/or other materials 
      provided with the distribution.
      
    * Neither the name of the Engineering Ingegneria Informatica s.p.a. nor the names of its contributors may
      be used to endorse or promote products derived from this software without specific
      prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND 
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, 
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE

**/
package it.eng.spagobi.sdk.test;

import it.eng.spagobi.sdk.documents.bo.SDKDocument;
import it.eng.spagobi.sdk.documents.bo.SDKDocumentParameter;
import it.eng.spagobi.sdk.documents.bo.SDKExecutedDocumentContent;
import it.eng.spagobi.sdk.proxy.DocumentsServiceProxy;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author Zerbetto Davide
 *
 */
public class ExportServlet extends HttpServlet {

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void service(HttpServletRequest request, HttpServletResponse response) {
    	InputStream is = null;
    	ServletOutputStream os = null;
    	try {
	    	HttpSession session = request.getSession();
	    	String user = (String) session.getAttribute("spagobi_user");
	    	String password = (String) session.getAttribute("spagobi_pwd");
	    	if (user != null && password != null) {
	    		SDKDocument document = (SDKDocument) session.getAttribute("spagobi_current_document");
	    		String role = (String) session.getAttribute("spagobi_role");
	    		SDKDocumentParameter[] parameters = (SDKDocumentParameter[]) session.getAttribute("spagobi_document_parameters"); 
	    		DocumentsServiceProxy proxy = new DocumentsServiceProxy(user, password);
	    		proxy.setEndpoint("http://localhost:8080/SpagoBI/sdk/DocumentsService");
	    		SDKExecutedDocumentContent export = proxy.executeDocument(document, parameters, role);
				is = export.getContent().getInputStream();
				response.setContentType(export.getFileType());
				response.setHeader("content-disposition", "attachment; filename=" + export.getFileName());
				os = response.getOutputStream();
				int c = 0;
				byte[] b = new byte[1024];
				while ((c = is.read(b)) != -1) {
					if (c == 1024)
						os.write(b);
					else
						os.write(b, 0, c);
				}
				os.flush();
	    	} else {
				response.sendRedirect(request.getContextPath());
	    	}
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
    		if (os != null)
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				} 
    	}
    }

}
