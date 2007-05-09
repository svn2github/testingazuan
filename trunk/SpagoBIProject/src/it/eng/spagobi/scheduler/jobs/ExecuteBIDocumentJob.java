package it.eng.spagobi.scheduler.jobs;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;
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

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import java.io.*;

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
					
					EventsManager eventManager = EventsManager.getInstance();
					List roles = DAOFactory.getBIObjectDAO().getCorrectRolesForExecution(biobj.getId());
					Integer idEvent = eventManager.registerEvent("Scheduler", "Start scheduled execution of document "+biobj.getName(), docParQueryString, roles);
					
					byte[] response = proxy.exec(profile);
					
					eventManager.registerEvent("Scheduler", "End scheduled execution of document "+biobj.getName(), docParQueryString, roles);
					
					if(sInfo.isSaveAsSnapshot()) {
						String snapName = sInfo.getSnapshotName();
						if( (snapName==null) || snapName.trim().equals("")) {
							continue;
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
					}
					
					
					
					
					
					
					
					if(sInfo.isSaveAsDocument()) {
						String docName = sInfo.getDocumentName();
						if( (docName==null) || docName.trim().equals("")) {
							continue;
						}
						String docDesc = sInfo.getDocumentDescription();
						String docHistorylengthStr = sInfo.getDocumentHistoryLength();
						
						// recover office document sbidomains
						IDomainDAO domainDAO = DAOFactory.getDomainDAO();
						Domain officeDocDom = domainDAO.loadDomainByCodeAndValue("BIOBJ_TYPE", "OFFICE_DOC");
						// recover development sbidomains
						Domain devDom = domainDAO.loadDomainByCodeAndValue("STATE", "DEV");
						// recover engine
						IEngineDAO engineDAO = DAOFactory.getEngineDAO();
						List engines = engineDAO.loadAllEnginesForBIObjectType(officeDocDom.getValueCd());
						Engine engine = (Engine)engines.get(0);
						// load the template
						UploadedFile uploadedFile = new UploadedFile();
						uploadedFile.setFieldNameInForm("template");
						uploadedFile.setFileName(docName);
						uploadedFile.setSizeInBytes(response.length);
						uploadedFile.setFileContent(response);
						// load all functionality
						//List storeInFunctionalities = new ArrayList();
						//List functIds = request.getAttributeAsList("FUNCT_ID");
						//Iterator iterFunctIds = functIds.iterator();
						//while(iterFunctIds.hasNext()) {
						//	String functIdStr = (String)iterFunctIds.next();
						//	Integer functId = new Integer(functIdStr);
						//	storeInFunctionalities.add(functId);
						//}
						// create biobject
						BIObject newbiobj = new BIObject();
						newbiobj.setDescription(docDesc);
						newbiobj.setLabel(docName);
						newbiobj.setName(docName);
						newbiobj.setEncrypt(new Integer(0));
						newbiobj.setEngine(engine);
						newbiobj.setRelName("");
						newbiobj.setBiObjectTypeCode(officeDocDom.getValueCd());
						newbiobj.setBiObjectTypeID(officeDocDom.getValueId());
						newbiobj.setStateCode(devDom.getValueCd());
						newbiobj.setStateID(devDom.getValueId());
						newbiobj.setVisible(new Integer(0));
						newbiobj.setTemplate(uploadedFile);
						//newbiobj.setFunctionalities(storeInFunctionalities);
						IBIObjectDAO objectDAO = DAOFactory.getBIObjectDAO();
						//objectDAO.insertBIObject(biobj);
					}
					
					
					
					
					
					
					if(sInfo.isSendMail()) {
						
						String mailTos = sInfo.getMailTos();
						if( (mailTos==null) || mailTos.trim().equals("")) {
							continue;
						}
						String[] recipients = mailTos.split(",");
						//Set the host smtp address
					    Properties props = new Properties();
					    props.put("mail.smtp.host", "mail.eng.it");
					    props.put("mail.smtp.auth", "true");
                        // create autheticator object
					    Authenticator auth = new SMTPAuthenticator();
					    // open session
					    Session session = Session.getDefaultInstance(props, auth);
					    // create a message
					    Message msg = new MimeMessage(session);
					    // set the from and to address
					    InternetAddress addressFrom = new InternetAddress("spagobi@eng.it");
					    msg.setFrom(addressFrom);
					    InternetAddress[] addressTo = new InternetAddress[recipients.length];
					    for (int i = 0; i < recipients.length; i++)  {
					        addressTo[i] = new InternetAddress(recipients[i]);
					    }
					    msg.setRecipients(Message.RecipientType.TO, addressTo);
					    // Setting the Subject and Content Type
 					    msg.setSubject("Scheduler");
					    // create and fill the first message part
					    MimeBodyPart mbp1 = new MimeBodyPart();
					    mbp1.setText("Messaggio Inviato dallo Scheduler");
					    // create the second message part
					    MimeBodyPart mbp2 = new MimeBodyPart();
			            // attach the file to the message
					    SchedulerDataSource sds = new SchedulerDataSource(response, "text/html", biobj.getName() + ".html");
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
					}
					
					
					
					
					
				}
			}
		} catch (Exception e) {
	    	SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
	    			           "execute", "Error while executiong job ", e );
	    }
		
		
	}

	
	private class SMTPAuthenticator extends javax.mail.Authenticator
	{
	    public PasswordAuthentication getPasswordAuthentication()
	    {
	        String username = "lfiscato";
	        String password = "fadeto79";
	        return new PasswordAuthentication(username, password);
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
