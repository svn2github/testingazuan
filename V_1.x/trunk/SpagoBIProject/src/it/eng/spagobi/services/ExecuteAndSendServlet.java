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
package it.eng.spagobi.services;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.ExecutionController;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectDAO;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.ExecutionProxy;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class ExecuteAndSendServlet extends HttpServlet{
	
	public void init(ServletConfig config) throws ServletException {
        super.init(config);
     } 
	
	public void service(HttpServletRequest request, HttpServletResponse response) {
		
		final String OK = "10";
		final String ERROR = "20";
		final String TONOTFOUND = "90";
		
		String retCode = "";
		try{
			
			// GET PARAMETER
			String objLabel = "";
			String to = "";
			String cc = "";
			String object = "";
			String message = "";
					
			String queryStr = "";
			Enumeration parNames = request.getParameterNames();
			while(parNames.hasMoreElements()) {
				String parName = (String)parNames.nextElement();
				if(parName.equals("objlabel")){
					objLabel = request.getParameter("objlabel");
				} else if(parName.equals("to")) {
					to = request.getParameter("to");
				} else if(parName.equals("cc")) {
					cc = request.getParameter("cc");
				} else if(parName.equals("object")) {
					object = request.getParameter("object");
				} else if(parName.equals("message")) {
					message = request.getParameter("message");
				} else {
					String value = request.getParameter(parName);
					queryStr += parName + "=" + value + "&";
				}
			}
			
			if(to.equals("")) {
				retCode = TONOTFOUND;
				throw new Exception("To Address not found");
			}
			
			String returnedContentType = "";
			String fileextension = "";
			byte[] documentBytes = null;
			IBIObjectDAO biobjdao = DAOFactory.getBIObjectDAO();
			BIObject biobj = biobjdao.loadBIObjectByLabel(objLabel);
			// create the execution controller 
			ExecutionController execCtrl = new ExecutionController();
			execCtrl.setBiObject(biobj);
			// fill parameters 
			execCtrl.refreshParameters(biobj, queryStr);
			// exec the document only if all its parameter are filled
			if(execCtrl.directExecution()) {
				ExecutionProxy proxy = new ExecutionProxy();
				proxy.setBiObject(biobj);
				HttpSession httpSession = request.getSession();
	            IEngUserProfile profile = (IEngUserProfile)httpSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);   	
				documentBytes = proxy.exec(profile);
				returnedContentType = proxy.getReturnedContentType();
				fileextension = proxy.getFileExtensionFromContType(returnedContentType);
			}
			
		    
		    // SEND MAIL
		    ConfigSingleton config = ConfigSingleton.getInstance();
			SourceBean mailProfSB = (SourceBean)config.getFilteredSourceBeanAttribute("MAIL.PROFILES.PROFILE", "name", "scheduler");
			if(mailProfSB==null) {
				throw new Exception("Mail profile configuration not found");
			}
			String smtphost = (String)mailProfSB.getAttribute("smtphost");
			if( (smtphost==null) || smtphost.trim().equals(""))
				throw new Exception("Smtp host not configured");
			String from = (String)mailProfSB.getAttribute("from");
			if( (from==null) || from.trim().equals(""))
				from = "spagobi@eng.it";
			String user = (String)mailProfSB.getAttribute("user");
			if( (user==null) || user.trim().equals(""))
				throw new Exception("Smtp user not configured");
			String pass = (String)mailProfSB.getAttribute("password");
			if( (pass==null) || pass.trim().equals(""))
				throw new Exception("Smtp password not configured");
			//Set the host smtp address
		    Properties props = new Properties();
		    props.put("mail.smtp.host", smtphost);
		    props.put("mail.smtp.auth", "true");
	        // create autheticator object
		    Authenticator auth = new SMTPAuthenticator(user, pass);
		    // open session
		    Session session = Session.getDefaultInstance(props, auth);
		    
		    // create a message
		    Message msg = new MimeMessage(session);
		    // set the from / to / cc address
		    InternetAddress addressFrom = new InternetAddress(from);
		    msg.setFrom(addressFrom);
		    String[] recipients = to.split(",");
		    InternetAddress[] addressTo = new InternetAddress[recipients.length];
		    for (int i = 0; i < recipients.length; i++)  {
		        addressTo[i] = new InternetAddress(recipients[i]);
		    }
		    msg.setRecipients(Message.RecipientType.TO, addressTo);
		    if( (cc!=null) && !cc.trim().equals("") ) {
			    recipients = cc.split(",");
			    InternetAddress[] addressCC = new InternetAddress[recipients.length];
			    for (int i = 0; i < recipients.length; i++)  {
			    	String cc_add = recipients[i];
			    	if( (cc_add!=null) && !cc_add.trim().equals("") ) {
			    		addressCC[i] = new InternetAddress(recipients[i]);
			    	}
			    }
			    msg.setRecipients(Message.RecipientType.CC, addressCC);
		    }
		    // Setting the Subject and Content Type
			msg.setSubject(object);
			
		    // create and fill the first message part
		    MimeBodyPart mbp1 = new MimeBodyPart();
		    mbp1.setText(message);
		    // create the second message part
		    MimeBodyPart mbp2 = new MimeBodyPart();
	        // attach the file to the message
		    SchedulerDataSource sds = new SchedulerDataSource(documentBytes, returnedContentType, "result." + fileextension);
		    mbp2.setDataHandler(new DataHandler(sds));
		    mbp2.setFileName(sds.getName());
		    // create the Multipart and add its parts to it
		    Multipart mp = new MimeMultipart();
		    mp.addBodyPart(mbp1);
		    mp.addBodyPart(mbp2);
		    // add the Multipart to the message
		    msg.setContent(mp);
		    // send message
		    Transport.send(msg);
		    
		    retCode = OK;

		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
					            "service", "Error while executing and sending object ", e);
			if(retCode.equals("")) {
				retCode = ERROR;
			}
		} finally {
			try{
				response.getOutputStream().write(retCode.getBytes());
				response.getOutputStream().flush();
			} catch (Exception ex) {
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
			            			"service", "Error while sending response to client", ex);
			}
		}
	}
		
	
	private class SMTPAuthenticator extends javax.mail.Authenticator
	{
		private String username = "";
		private String password = "";
		
	    public PasswordAuthentication getPasswordAuthentication()
	    {
	        return new PasswordAuthentication(username, password);
	    }
	    
	    public SMTPAuthenticator(String user, String pass) {
	    	this.username = user;
	    	this.password = pass;
	    }
	}
	
	
	private class SchedulerDataSource implements DataSource {

		byte[] content = null;
		String name = null;
		String contentType = null;
		
		public String getContentType() {
			return contentType;
		}

		public InputStream getInputStream() throws IOException {
			ByteArrayInputStream bais = new ByteArrayInputStream(content);
			return bais;
		}

		public String getName() {
			return name;
		}

		public OutputStream getOutputStream() throws IOException {
			return null;
		}
		
		public SchedulerDataSource(byte[] content, String contentType, String name) {
			this.content = content;
			this.contentType = contentType;
			this.name = name;
		}
		
		
	}
	
	
}	

