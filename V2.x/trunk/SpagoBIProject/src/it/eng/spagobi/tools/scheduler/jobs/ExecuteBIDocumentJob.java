/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.tools.scheduler.jobs;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.bo.ObjTemplate;
import it.eng.spagobi.analiticalmodel.document.bo.Snapshot;
import it.eng.spagobi.analiticalmodel.document.dao.IBIObjectDAO;
import it.eng.spagobi.analiticalmodel.document.dao.ISnapshotDAO;
import it.eng.spagobi.analiticalmodel.document.handlers.ExecutionController;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.behaviouralmodel.check.bo.Check;
import it.eng.spagobi.commons.bo.Domain;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.dao.IDomainDAO;
import it.eng.spagobi.commons.utilities.ExecutionProxy;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.commons.utilities.ObjectsAccessVerifier;
import it.eng.spagobi.commons.utilities.messages.IMessageBuilder;
import it.eng.spagobi.commons.utilities.messages.MessageBuilderFactory;
import it.eng.spagobi.engines.config.bo.Engine;
import it.eng.spagobi.engines.config.dao.IEngineDAO;
import it.eng.spagobi.events.EventsManager;
import it.eng.spagobi.tools.distributionlist.bo.DistributionList;
import it.eng.spagobi.tools.distributionlist.bo.Email;
import it.eng.spagobi.tools.scheduler.to.SaveInfo;
import it.eng.spagobi.tools.scheduler.utils.BIObjectParametersIterator;
import it.eng.spagobi.tools.scheduler.utils.SchedulerUtilities;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ExecuteBIDocumentJob implements Job {

    static private Logger logger = Logger.getLogger(ExecuteBIDocumentJob.class);	
	
	/* (non-Javadoc)
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	public void execute(JobExecutionContext jex) throws JobExecutionException {
	    logger.debug("IN");
		try{
			JobDataMap jdm = jex.getMergedJobDataMap();
			String doclabelsConcat = jdm.getString("documentLabels");
			String[] docLabels = doclabelsConcat.split(",");
			
			
			IEngUserProfile profile = UserProfile.createSchedulerUserProfile();
			
			for(int ind=0; ind<docLabels.length; ind++) {
				String docLabel = docLabels[ind];
				String docParQueryString = jdm.getString(docLabel);
				// load bidocument
			        IBIObjectDAO biobjdao = DAOFactory.getBIObjectDAO();
				BIObject biobj = biobjdao.loadBIObjectByLabel(docLabel.substring(0, docLabel.lastIndexOf("__")));
				// get the save options
				String saveOptString = jdm.getString("biobject_id_" + biobj.getId() + "__"+ (ind+1));
				SaveInfo sInfo = SchedulerUtilities.fromSaveInfoString(saveOptString);
				// create the execution controller 
				ExecutionController execCtrl = new ExecutionController();
				execCtrl.setBiObject(biobj);
				// fill parameters 
				execCtrl.refreshParameters(biobj, docParQueryString);
				
				BIObjectParametersIterator objectParametersIterator = new BIObjectParametersIterator(biobj.getBiObjectParameters());
				while (objectParametersIterator.hasNext()) {
					List parameters = (List) objectParametersIterator.next();
					biobj.setBiObjectParameters(parameters);
				
					StringBuffer toBeAppendedToName = new StringBuffer();
					StringBuffer toBeAppendedToDescription = new StringBuffer(" [");
					Iterator parametersIt = parameters.iterator();
					while (parametersIt.hasNext()) {
						BIObjectParameter aParameter = (BIObjectParameter) parametersIt.next();
						if (aParameter.isIterative()) {
							toBeAppendedToName.append("_" + aParameter.getParameterValuesAsString());
							toBeAppendedToDescription.append(aParameter.getLabel() + ":" + aParameter.getParameterValuesAsString() + "; ");
						}
					}
					// if there are no iterative parameters, toBeAppendedToDescription is " [" and must be cleaned
					if (toBeAppendedToDescription.length() == 2) {
						toBeAppendedToDescription.delete(0, 2);
					} else {
						// toBeAppendedToDescription ends with "; " and must be cleaned
						toBeAppendedToDescription.delete(toBeAppendedToDescription.length() - 2, toBeAppendedToDescription.length());
						toBeAppendedToDescription.append("]");
					}
					
					// appending the current date
					Date date = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat();
					sdf.applyPattern("ddMMyyyy");
					String dateStr = sdf.format(date);
					toBeAppendedToName.append("_" + dateStr);
					
					//check parameters value: if a parameter hasn't value but isn't mandatory the process 
					//must go on and so hasValidValue is set to true
					List tmpBIObjectParameters = biobj.getBiObjectParameters();
					Iterator it = tmpBIObjectParameters.iterator();
					while (it.hasNext()){
						boolean isMandatory = false;
						BIObjectParameter aBIObjectParameter = (BIObjectParameter)it.next();
						List checks = aBIObjectParameter.getParameter().getChecks();
						if (checks != null && !checks.isEmpty()) {
							Iterator checksIt = checks.iterator();
							while (checksIt.hasNext()) {
								Check check = (Check) checksIt.next();
								if (check.getValueTypeCd().equalsIgnoreCase("MANDATORY")) {
									isMandatory = true;
									break;
								}
							}
						}
						if (!isMandatory && 
								(aBIObjectParameter.getParameterValues() == null  || aBIObjectParameter.getParameterValues().size() == 0)) {
							aBIObjectParameter.setParameterValues(new ArrayList());
							aBIObjectParameter.setHasValidValues(true);
						}
					}

					
					// exec the document only if all its parameter are filled
					if(execCtrl.directExecution()) {
						
						ExecutionProxy proxy = new ExecutionProxy();
						proxy.setBiObject(biobj);
						IMessageBuilder msgBuilder = MessageBuilderFactory.getMessageBuilder();
						//String startExecMsgIniPart = msgBuilder.getMessage("scheduler.startexecsched", "component_scheduler_messages");
						//String startExecMsg = startExecMsgIniPart + " " + biobj.getName();
						String startExecMsg = "${scheduler.startexecsched} " + biobj.getName();
						//String endExecMsgIniPart = msgBuilder.getMessage("scheduler.endexecsched", "component_scheduler_messages");
						//String endExecMsg = endExecMsgIniPart + " " + biobj.getName();
						String endExecMsg = "${scheduler.endexecsched} " + biobj.getName();
						
						EventsManager eventManager = EventsManager.getInstance();
						List roles = DAOFactory.getBIObjectDAO().getCorrectRolesForExecution(biobj.getId());
						Integer idEvent = eventManager.registerEvent("Scheduler", startExecMsg, "", roles);
						
						byte[] response = proxy.exec(profile, "SCHEDULATION", null);
						String retCT = proxy.getReturnedContentType();
						String fileextension = proxy.getFileExtensionFromContType(retCT);
						
						eventManager.registerEvent("Scheduler", endExecMsg, "", roles);
						
						if(sInfo.isSaveAsSnapshot()) {
							saveAsSnap(sInfo, biobj, response, toBeAppendedToName.toString(), toBeAppendedToDescription.toString());
						}
						
						if(sInfo.isSaveAsDocument()) {
							saveAsDocument(sInfo, biobj,jex, response, fileextension, toBeAppendedToName.toString(), toBeAppendedToDescription.toString());
						}
	
						if(sInfo.isSendMail()) {
							sendMail(sInfo, biobj, response, retCT, fileextension, toBeAppendedToName.toString(), toBeAppendedToDescription.toString());
						}
						if(sInfo.isSendToDl()) {
							sendToDl(sInfo, biobj, response, retCT, fileextension, toBeAppendedToName.toString(), toBeAppendedToDescription.toString());
							if(jex.getNextFireTime()== null){
								String triggername = jex.getTrigger().getName();
								List dlIds = sInfo.getDlIds();
								 it = dlIds.iterator();
								while(it.hasNext()){
									Integer dlId = (Integer)it.next();
									DistributionList dl = DAOFactory.getDistributionListDAO().loadDistributionListById(dlId);
									DAOFactory.getDistributionListDAO().eraseDistributionListObjects(dl, (biobj.getId()).intValue(), triggername);
								}
							}
						}
		
					} else {
					    logger.warn("The document with label "+docLabel+" cannot be executed directly, " +
					            "maybe some prameters are not filled ");
						throw new Exception("The document with label "+docLabel+" cannot be executed directly, " +
								            "maybe some prameters are not filled ");
					}
				}
			}
			
		} catch (Exception e) {
		    logger.error("Error while executiong job ", e);
	    } finally {
	    	logger.debug("OUT");
	    }
	}

	
	
	
	private void saveAsSnap(SaveInfo sInfo,BIObject biobj, byte[] response, String toBeAppendedToName, String toBeAppendedToDescription) {
	    logger.debug("IN");
		try {
			String snapName = sInfo.getSnapshotName();
			if( (snapName==null) || snapName.trim().equals("")) {
				throw new Exception("Document name not specified");
			}
			snapName += toBeAppendedToName;
			if (snapName.length() > 100) {
				logger.warn("Snapshot name [" + snapName + "] exceeds maximum length that is 100, it will be truncated");
				snapName = snapName.substring(0, 100);
			}
			
			String snapDesc = sInfo.getSnapshotDescription() != null ? sInfo.getSnapshotDescription() : "";
			snapDesc += toBeAppendedToDescription;
			if (snapDesc.length() > 1000) {
				logger.warn("Snapshot description [" + snapDesc + "] exceeds maximum length that is 1000, it will be truncated");
				snapDesc = snapDesc.substring(0, 1000);
			}
			
			String historylengthStr = sInfo.getSnapshotHistoryLength();
			// store document as snapshot
			ISnapshotDAO snapDao = DAOFactory.getSnapshotDAO();
			// get the list of snapshots
			List allsnapshots = snapDao.getSnapshots(biobj.getId());
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
							Snapshot snap = SchedulerUtilities.getNamedHistorySnapshot(allsnapshots, snapName, histLen-1);
							Integer snapId = snap.getId();
							snapDao.deleteSnapshot(snapId);
						}
					}
				} catch(Exception e) {
				    logger.error("Error while deleting object snapshots", e);
				}
			}
			snapDao.saveSnapshot(response, biobj.getId(), snapName, snapDesc);	
		} catch (Exception e) {
		    logger.error("Error while saving schedule result as new snapshot", e);
		}finally{
		    logger.debug("OUT");
		}
	}
	
	
	
	
	
	private void saveAsDocument(SaveInfo sInfo,BIObject biobj, JobExecutionContext jex, byte[] response, String fileExt, String toBeAppendedToName, String toBeAppendedToDescription) {
	    logger.debug("IN");
	    try{
			String docName = sInfo.getDocumentName();
			if( (docName==null) || docName.trim().equals("")) {
				throw new Exception(" Document name not specified");
			}
			docName += toBeAppendedToName;
			String docDesc = sInfo.getDocumentDescription() + toBeAppendedToDescription;
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
				throw new Exception(" No suitable engines for the new document");
			}
			Engine engine = (Engine)engines.get(0);		
			// load the template
			ObjTemplate objTemp = new ObjTemplate();
			objTemp.setActive(new Boolean(true));
			objTemp.setContent(response);
			objTemp.setName(docName + fileExt);
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
				throw new Exception(" No functionality specified where store the new document");
			}
			// create biobject
			
			String jobName = jex.getJobDetail().getName();
			String completeLabel = "scheduler_" + jobName + "_" + docName;
			String label = "sched_" + String.valueOf(Math.abs(completeLabel.hashCode()));
			
			BIObject newbiobj = new BIObject();
			newbiobj.setDescription(docDesc);
			newbiobj.setCreationUser("scheduler");
			newbiobj.setLabel(label);
			newbiobj.setName(docName);
			newbiobj.setEncrypt(new Integer(0));
			newbiobj.setEngine(engine);
			newbiobj.setDataSourceId(biobj.getDataSourceId());
			newbiobj.setRelName("");
			newbiobj.setBiObjectTypeCode(officeDocDom.getValueCd());
			newbiobj.setBiObjectTypeID(officeDocDom.getValueId());
			newbiobj.setStateCode(relDom.getValueCd());
			newbiobj.setStateID(relDom.getValueId());
			newbiobj.setVisible(new Integer(0));
			newbiobj.setFunctionalities(storeInFunctionalities);
			IBIObjectDAO objectDAO = DAOFactory.getBIObjectDAO();
			
			BIObject biobjexist = objectDAO.loadBIObjectByLabel(label);
			if(biobjexist==null){
				objectDAO.insertBIObject(newbiobj, objTemp);
			} else {
				newbiobj.setId(biobjexist.getId());
				objectDAO.modifyBIObject(newbiobj, objTemp);
			}
		} catch (Exception e) {
		    logger.error("Error while saving schedule result as new document",e);
		}finally{
		    logger.debug("OUT");
		}
	}
	
	
	
	
	private void sendMail(SaveInfo sInfo, BIObject biobj, byte[] response, String retCT, String fileExt, String toBeAppendedToName, String toBeAppendedToDescription) {
	    logger.debug("IN");
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
			String mailSubj = sInfo.getMailSubj();

			String mailTxt = sInfo.getMailTxt();

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
			String subject = mailSubj + " " + biobj.getName() + toBeAppendedToName;
			msg.setSubject(subject);
		    // create and fill the first message part
		    MimeBodyPart mbp1 = new MimeBodyPart();
		    mbp1.setText(mailTxt + "\n" + toBeAppendedToDescription);
		    // create the second message part
		    MimeBodyPart mbp2 = new MimeBodyPart();
	        // attach the file to the message
		    SchedulerDataSource sds = new SchedulerDataSource(response, retCT, biobj.getName() + toBeAppendedToName + fileExt);
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
		    logger.error("Error while sending schedule result mail",e);
		}finally{
		    logger.debug("OUT");
		}
	}

	private void sendToDl(SaveInfo sInfo, BIObject biobj, byte[] response, String retCT, String fileExt, String toBeAppendedToName, String toBeAppendedToDescription) {
	    logger.debug("IN");
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
			
			String mailTos = "";
			List dlIds = sInfo.getDlIds();
			Iterator it = dlIds.iterator();
			while(it.hasNext()){
				
				Integer dlId = (Integer)it.next();
				DistributionList dl = DAOFactory.getDistributionListDAO().loadDistributionListById(dlId);
				
				List emails = new ArrayList();
				emails = dl.getEmails();
				Iterator j = emails.iterator();
				while(j.hasNext()){
					Email e = (Email) j.next();
					String email = e.getEmail();
					String userTemp = e.getUserId();
					IEngUserProfile userProfile = GeneralUtilities.createNewUserProfile(userTemp);				
					if(ObjectsAccessVerifier.canSee(biobj, userProfile))	{				
						if (j.hasNext()) {mailTos = mailTos+email+",";}
						else {mailTos = mailTos+email;}
					}
					
				}
			}
			
			
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
			String subject = biobj.getName() + toBeAppendedToName;
			msg.setSubject(subject);
		    // create and fill the first message part
		    //MimeBodyPart mbp1 = new MimeBodyPart();
		    //mbp1.setText(mailTxt);
		    // create the second message part
		    MimeBodyPart mbp2 = new MimeBodyPart();
	        // attach the file to the message
		    SchedulerDataSource sds = new SchedulerDataSource(response, retCT, biobj.getName() + toBeAppendedToName + fileExt);
		    mbp2.setDataHandler(new DataHandler(sds));
		    mbp2.setFileName(sds.getName());
		    // create the Multipart and add its parts to it
		    Multipart mp = new MimeMultipart();
		    //mp.addBodyPart(mbp1);
		    mp.addBodyPart(mbp2);
		    // add the Multipart to the message
		    msg.setContent(mp);
		    // send message
		    Transport.send(msg);
		} catch (Exception e) {
		    logger.error("Error while sending schedule result mail",e);
		}finally{
		    logger.debug("OUT");
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
