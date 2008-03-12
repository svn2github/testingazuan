package it.eng.spagobi.analiticalmodel.execution.service;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.dao.IBIObjectDAO;
import it.eng.spagobi.analiticalmodel.document.handlers.ExecutionController;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.ExecutionProxy;
import it.eng.spagobi.services.common.IProxyService;
import it.eng.spagobi.services.common.IProxyServiceFactory;
import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;
import it.eng.spagobi.services.security.exceptions.SecurityException;
import it.eng.spagobi.services.security.service.ISecurityServiceSupplier;
import it.eng.spagobi.services.security.service.SecurityServiceSupplierFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.ListIterator;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class ExecuteAndSendAction extends AbstractHttpAction {

    private static transient Logger logger = Logger.getLogger(ExecuteAndSendAction.class);

    public void service(SourceBean request, SourceBean responseSb) throws Exception {
	logger.debug("IN");

	freezeHttpResponse();
	HttpServletResponse response = getHttpResponse();
	HttpServletRequest req = getHttpRequest();

	final String OK = "10";
	final String ERROR = "20";
	final String TONOTFOUND = "90";
	String retCode = "";

	try {

	    // GET PARAMETER
	    String objLabel = "";
	    String to = "";
	    String cc = "";
	    String object = "";
	    String message = "";
	    String queryStr = "";
	    String userId = "";
	    String login = "";
	    String pass = "";
	    String from = "";

	    // Creo una lista con un suo iteratore con dentro i parametri della
	    // request
	    List params = request.getContainedAttributes();
	    ListIterator it = params.listIterator();

	    while (it.hasNext()) {

		Object par = it.next();
		SourceBeanAttribute p = (SourceBeanAttribute) par;
		String parName = (String) p.getKey();
		logger.debug("got parName=" + parName);
		if (parName.equals("objlabel")) {
		    objLabel = (String) request.getAttribute("objLabel");
		    logger.debug("got objLabel from Request=" + objLabel);
		} else if (parName.equals("to")) {
		    to = (String) request.getAttribute("to");
		    logger.debug("got to from Request=" + to);
		} else if (parName.equals("cc")) {
		    cc = (String) request.getAttribute("cc");
		    logger.debug("got cc from Request=" + cc);
		} else if (parName.equals("object")) {
		    object = (String) request.getAttribute("object");
		    logger.debug("got object from Request=" + object);
		} else if (parName.equals("message")) {
		    message = (String) request.getAttribute("message");
		    logger.debug("got message from Request=" + message);
		} else if (parName.equals("userId")) {
		    userId = (String) request.getAttribute("userId");
		    logger.debug("got userId from Request=" + userId);
		} else if (parName.equals("login")) {
		    login = (String) request.getAttribute("login");
		    logger.debug("got user from Request");
		} else if (parName.equals("pwd")) {
		    pass = (String) request.getAttribute("pwd");
		    logger.debug("got pwd from Request");
		} else if (parName.equals("replyto")) {
			from = (String) request.getAttribute("replyto");
		    logger.debug("got email to reply to, from Request");    
		} else {
		    String value = (String) request.getAttribute(parName);
		    queryStr += parName + "=" + value + "&";
		}
	    }

	    if (to.equals("")) {
		retCode = TONOTFOUND;
		logger.error("To Address not found");
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
	    // exec the document only if all its parameters are filled
	    if (execCtrl.directExecution()) {
		ExecutionProxy proxy = new ExecutionProxy();
		proxy.setBiObject(biobj);
		
		IEngUserProfile profile = null;
		RequestContainer reqCont = RequestContainer.getRequestContainer();
		SessionContainer sessCont = reqCont.getSessionContainer();
		SessionContainer permSess = sessCont.getPermanentContainer();
		profile = (IEngUserProfile) permSess.getAttribute(IEngUserProfile.ENG_USER_PROFILE);

		if (profile == null) {
		    ConfigSingleton config = ConfigSingleton.getInstance();
		    SourceBean validateSB = (SourceBean) config.getAttribute("SPAGOBI_SSO.ACTIVE");
		    String active = (String) validateSB.getCharacters();
		    if (active != null && active.equals("true")) {
			IProxyService userProxy = IProxyServiceFactory.createProxyService();
			userId = userProxy.readUserId(req.getSession());
			logger.debug("got userId from IProxyService=" + userId);
		    } else {
			userId = req.getParameter("userId");
			logger.debug("got userId from Request=" + userId);
		    }

		    ISecurityServiceSupplier supplier = SecurityServiceSupplierFactory.createISecurityServiceSupplier();
		    try {
			SpagoBIUserProfile user = supplier.createUserProfile(userId);
			profile = new UserProfile(user);
		    } catch (Exception e) {
			logger.error("Exception while creating user profile", e);
			throw new SecurityException();
		    }

		}

		documentBytes = proxy.exec(profile);
		returnedContentType = proxy.getReturnedContentType();
		fileextension = proxy.getFileExtensionFromContType(returnedContentType);
	    }
	    // SEND MAIL
	    ConfigSingleton config = ConfigSingleton.getInstance();
	    SourceBean mailProfSB = (SourceBean) config.getFilteredSourceBeanAttribute("MAIL.PROFILES.PROFILE", "name",
		    "user");
	    if (mailProfSB == null) {
		throw new Exception("Mail profile configuration not found");
	    }
	    String smtphost = (String) mailProfSB.getAttribute("smtphost");
	    
	    if ((smtphost == null) || smtphost.trim().equals("")) throw new Exception("Smtp host not configured");
	    if ((from == null) || from.trim().equals("")) from = (String) mailProfSB.getAttribute("from");
	    if(login == null || login.trim().equals(""))login = (String) mailProfSB.getAttribute("user");
	    if(pass == null || pass.trim().equals(""))pass = (String) mailProfSB.getAttribute("password");
	    
	    if ((from == null) || from.trim().equals("")) throw new Exception("From field missing from input form or not configured");
	    if(login == null || login.trim().equals(""))throw new Exception("Login field missing from input form or not configured");
	    if(pass == null || pass.trim().equals(""))throw new Exception("Password field missing from input form or not configured");
	    // Set the host smtp address
	    Properties props = new Properties();
	    props.put("mail.smtp.host", smtphost);
	    props.put("mail.smtp.auth", "true");
	    // create autheticator object
	    Authenticator auth = new SMTPAuthenticator(login, pass);
	    // open session
	    Session session = Session.getDefaultInstance(props, auth);

	    // create a message
	    Message msg = new MimeMessage(session);
	    // set the from / to / cc address
	    InternetAddress addressFrom = new InternetAddress(from);
	    msg.setFrom(addressFrom);
	    String[] recipients = to.split(",");
	    InternetAddress[] addressTo = new InternetAddress[recipients.length];
	    for (int i = 0; i < recipients.length; i++) {
		addressTo[i] = new InternetAddress(recipients[i]);
	    }
	    msg.setRecipients(Message.RecipientType.TO, addressTo);
	    if ((cc != null) && !cc.trim().equals("")) {
		recipients = cc.split(",");
		InternetAddress[] addressCC = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++) {
		    String cc_add = recipients[i];
		    if ((cc_add != null) && !cc_add.trim().equals("")) {
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
	    SchedulerDataSource sds = new SchedulerDataSource(documentBytes, returnedContentType, "result."
		    + fileextension);
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
	    logger.error("Error while executing and sending object ", e);
	    if (retCode.equals("")) {
		retCode = ERROR;
	    }
	} finally {
	    try {
		response.getOutputStream().write(retCode.getBytes());
		response.getOutputStream().flush();
	    } catch (Exception ex) {
		logger.error("Error while sending response to client", ex);
	    }
	}
	logger.debug("OUT");
    }

    private class SMTPAuthenticator extends javax.mail.Authenticator {
	private String username = "";
	private String password = "";

	public PasswordAuthentication getPasswordAuthentication() {
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
