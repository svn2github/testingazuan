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
package it.eng.spagobi.scheduler.jobs;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.Domain;
import it.eng.spagobi.bo.Engine;
import it.eng.spagobi.bo.ExecutionController;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectCMSDAO;
import it.eng.spagobi.bo.dao.IBIObjectDAO;
import it.eng.spagobi.bo.dao.IDomainDAO;
import it.eng.spagobi.bo.dao.IEngineDAO;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.events.EventsManager;
import it.eng.spagobi.scheduler.to.SaveInfo;
import it.eng.spagobi.scheduler.utils.SchedulerUtilities;
import it.eng.spagobi.security.FakeUserProfile;
import it.eng.spagobi.utilities.ExecutionProxy;
import it.eng.spagobi.utilities.SpagoBITracer;
import it.eng.spagobi.utilities.UploadedFile;
import it.eng.spagobi.utilities.messages.IMessageBuilder;
import it.eng.spagobi.utilities.messages.MessageBuilderFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ExecuteBIDocumentJob implements Job {

	public void execute(JobExecutionContext jex) throws JobExecutionException {
		try{
			JobDataMap jdm = jex.getMergedJobDataMap();
			String doclabelsConcat = jdm.getString("documentLabels");
			String[] docLabels = doclabelsConcat.split(",");
			for(int ind=0; ind<docLabels.length; ind++) {
				String docLabel = docLabels[ind];
				String docParQueryString = jdm.getString(docLabel);
				// load bidocument
			    IBIObjectDAO biobjdao = DAOFactory.getBIObjectDAO();
				BIObject biobj = biobjdao.loadBIObjectByLabel(docLabel);
				// get the save options
				String saveOptString = jdm.getString("biobject_id_" + biobj.getId());
				SaveInfo sInfo = SchedulerUtilities.fromSaveInfoString(saveOptString);
				// create the execution controller 
				ExecutionController execCtrl = new ExecutionController();
				execCtrl.setBiObject(biobj);
				// fill parameters 
				execCtrl.refreshParameters(biobj, docParQueryString);
				// exec the document only if all its parameter are filled
				if(execCtrl.directExecution()) {
					ExecutionProxy proxy = new ExecutionProxy();
					proxy.setBiObject(biobj);
					IEngUserProfile profile = new FakeUserProfile("scheduler");
					
					IMessageBuilder msgBuilder = MessageBuilderFactory.getMessageBuilder();
					String startExecMsgIniPart = msgBuilder.getMessage(RequestContainer.getRequestContainer(), "scheduler.startexecsched", "component_scheduler_messages");
					String startExecMsg = startExecMsgIniPart + " " + biobj.getName();
					String endExecMsgIniPart = msgBuilder.getMessage(RequestContainer.getRequestContainer(), "scheduler.endexecsched", "component_scheduler_messages");
					String endExecMsg = endExecMsgIniPart + " " + biobj.getName();
					
					EventsManager eventManager = EventsManager.getInstance();
					List roles = DAOFactory.getBIObjectDAO().getCorrectRolesForExecution(biobj.getId());
					Integer idEvent = eventManager.registerEvent("Scheduler", startExecMsg, "", roles);
					
					byte[] response = proxy.exec(profile);
					String retCT = proxy.getReturnedContentType();
					String fileextension = proxy.getFileExtensionFromContType(retCT);
					
					eventManager.registerEvent("Scheduler", endExecMsg, "", roles);
					
					if(sInfo.isSaveAsSnapshot()) {
						saveAsSnap(sInfo, biobj, response);
					}
					
					if(sInfo.isSaveAsDocument()) {
						saveAsDocument(sInfo, jex, response, fileextension);
					}

					if(sInfo.isSendMail()) {
						sendMail(sInfo, biobj, response, retCT, fileextension);
					}
	
				} else {
					throw new Exception("The document with label "+docLabel+" cannot be executed directly, " +
							            "maybe some prameters are not filled ");
				}
			}
		} catch (Exception e) {
	    	SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
	    			           "execute", "Error while executiong job ", e );
	    }
		
		
	}

	
	
	
	private void saveAsSnap(SaveInfo sInfo,BIObject biobj, byte[] response) {
		try {
			String snapName = sInfo.getSnapshotName();
			if( (snapName==null) || snapName.trim().equals("")) {
				throw new Exception("Document name not specified");
			}
			String snapDesc = sInfo.getSnapshotDescription();
			String historylengthStr = sInfo.getSnapshotHistoryLength();
			// store document as snapshot
			IBIObjectCMSDAO objectCMSDAO = DAOFactory.getBIObjectCMSDAO();
			// get the list of snapshots
			List allsnapshots = objectCMSDAO.getSnapshots(biobj.getPath());
			// get the list of the snapshot with the store name
			List snapshots = SchedulerUtilities.getSnapshotsByName(allsnapshots, snapName);
			// get the number of previous snapshot saved
			int numSnap = snapshots.size();
			// if the number of snapshot is greater or equal to the history length then
			// delete the unecessary snapshots
			if((historylengthStr!=null) && !historylengthStr.trim().equals("")){
				try{
					Integer histLenInt = new Integer(historylengthStr);
					int histLen = histLenInt.intValue();
					if(numSnap>=histLen){
						int delta = numSnap - histLen;
						for(int i=0; i<=delta; i++) {
							BIObject.BIObjectSnapshot snap = SchedulerUtilities.getNamedHistorySnapshot(snapshots, snapName, histLen-1);
							String pathSnap = snap.getPath();
							objectCMSDAO.deleteSnapshot(pathSnap);
						}
					}
				} catch(Exception e) {
					SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
	    			           			"execute", "Error while deleting object snapshots", e );
				}
			}
			objectCMSDAO.saveSnapshot(response, biobj.getPath(), snapName, snapDesc);	
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
								"saveAsSnap", "Error while saving schedule result as new snapshot", e);
		}
	}
	
	
	
	
	
	private void saveAsDocument(SaveInfo sInfo, JobExecutionContext jex, byte[] response, String fileExt) {
		try{
			String docName = sInfo.getDocumentName();
			if( (docName==null) || docName.trim().equals("")) {
				throw new Exception("Document name not specified");
			}
			String docDesc = sInfo.getDocumentDescription();
			String docHistorylengthStr = sInfo.getDocumentHistoryLength();
			
			// recover office document sbidomains
			IDomainDAO domainDAO = DAOFactory.getDomainDAO();
			Domain officeDocDom = domainDAO.loadDomainByCodeAndValue("BIOBJ_TYPE", "OFFICE_DOC");
			// recover development sbidomains
			Domain relDom = domainDAO.loadDomainByCodeAndValue("STATE", "REL");
			// recover engine
			IEngineDAO engineDAO = DAOFactory.getEngineDAO();
			List engines = engineDAO.loadAllEnginesForBIObjectType(officeDocDom.getValueCd());
			if(engines.isEmpty()) {
				throw new Exception("No suitable engines for the new document");
			}
			Engine engine = (Engine)engines.get(0);
			// load the template
			UploadedFile uploadedFile = new UploadedFile();
			uploadedFile.setFieldNameInForm("template");
			uploadedFile.setFileName(docName + fileExt);
			uploadedFile.setSizeInBytes(response.length);
			uploadedFile.setFileContent(response);
			// load all functionality
			List storeInFunctionalities = new ArrayList();
			String functIdsConcat = sInfo.getFunctionalityIds();
			String[] functIds =  functIdsConcat.split(",");
			for(int i=0; i<functIds.length; i++) {
				String functIdStr = functIds[i];
				if(functIdStr.trim().equals(""))
					continue;
				Integer functId = Integer.valueOf(functIdStr);
				storeInFunctionalities.add(functId);
			}
			if(storeInFunctionalities.isEmpty()) {
				throw new Exception("No functionality specified where store the new document");
			}
			// create biobject
			
			String jobName = jex.getJobDetail().getName();
			String completeLabel = "scheduler_" + jobName + "_" + docName;
			String label = "sched_" + String.valueOf(Math.abs(completeLabel.hashCode()));
			
			BIObject newbiobj = new BIObject();
			newbiobj.setDescription(docDesc);
			newbiobj.setLabel(label);
			newbiobj.setName(docName);
			newbiobj.setEncrypt(new Integer(0));
			newbiobj.setEngine(engine);
			newbiobj.setRelName("");
			newbiobj.setBiObjectTypeCode(officeDocDom.getValueCd());
			newbiobj.setBiObjectTypeID(officeDocDom.getValueId());
			newbiobj.setStateCode(relDom.getValueCd());
			newbiobj.setStateID(relDom.getValueId());
			newbiobj.setVisible(new Integer(0));
			newbiobj.setTemplate(uploadedFile);
			newbiobj.setFunctionalities(storeInFunctionalities);
			IBIObjectDAO objectDAO = DAOFactory.getBIObjectDAO();
			
			BIObject biobjexist = objectDAO.loadBIObjectByLabel(label);
			if(biobjexist==null){
				objectDAO.insertBIObject(newbiobj);
			} else {
				newbiobj.setId(biobjexist.getId());
				objectDAO.modifyBIObject(newbiobj);
			}
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
		            "saveAsDocument", "Error while saving schedule result as new document", e);
		}
	}
	
	
	
	
	private void sendMail(SaveInfo sInfo, BIObject biobj, byte[] response, String retCT, String fileExt) {
		try{
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
				from = "spagobi.scheduler@eng.it";
			String user = (String)mailProfSB.getAttribute("user");
			if( (user==null) || user.trim().equals(""))
				throw new Exception("Smtp user not configured");
			String pass = (String)mailProfSB.getAttribute("password");
			if( (pass==null) || pass.trim().equals(""))
				throw new Exception("Smtp password not configured");
			String mailTos = sInfo.getMailTos();
			if( (mailTos==null) || mailTos.trim().equals("")) {	
				throw new Exception("No recipient address found");
			}
			String[] recipients = mailTos.split(",");
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
		    // set the from and to address
		    InternetAddress addressFrom = new InternetAddress(from);
		    msg.setFrom(addressFrom);
		    InternetAddress[] addressTo = new InternetAddress[recipients.length];
		    for (int i = 0; i < recipients.length; i++)  {
		        addressTo[i] = new InternetAddress(recipients[i]);
		    }
		    msg.setRecipients(Message.RecipientType.TO, addressTo);
		    // Setting the Subject and Content Type
			IMessageBuilder msgBuilder = MessageBuilderFactory.getMessageBuilder();
			String subjectfinalpart = msgBuilder.getMessage(RequestContainer.getRequestContainer(), "scheduler.mailsubject", "component_scheduler_messages");
			String subject = biobj.getName() + " " + subjectfinalpart;
			msg.setSubject(subject);
		    // create and fill the first message part
		    MimeBodyPart mbp1 = new MimeBodyPart();
		    String mailtext = msgBuilder.getMessage(RequestContainer.getRequestContainer(), "scheduler.mailtext", "component_scheduler_messages");
		    mbp1.setText(mailtext);
		    // create the second message part
		    MimeBodyPart mbp2 = new MimeBodyPart();
	        // attach the file to the message
		    SchedulerDataSource sds = new SchedulerDataSource(response, retCT, biobj.getName() + fileExt);
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
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					            "sendMail", "Error while sending schedule result mail", e);
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
