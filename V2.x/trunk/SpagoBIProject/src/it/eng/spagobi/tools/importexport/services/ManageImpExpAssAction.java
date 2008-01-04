package it.eng.spagobi.tools.importexport.services;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.commons.utilities.UploadedFile;
import it.eng.spagobi.commons.utilities.messages.IMessageBuilder;
import it.eng.spagobi.commons.utilities.messages.MessageBuilderFactory;
import it.eng.spagobi.commons.utilities.urls.WebUrlBuilder;
import it.eng.spagobi.tools.importexport.bo.AssociationFile;
import it.eng.spagobi.tools.importexport.dao.AssociationFileDAO;
import it.eng.spagobi.tools.importexport.dao.IAssociationFileDAO;

import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;

public class ManageImpExpAssAction extends AbstractHttpAction {

    private HttpServletRequest httpRequest = null;
    private HttpServletResponse httpResponse = null;
    IMessageBuilder msgBuild = null;
    WebUrlBuilder urlBuilder = null;

    static private Logger logger = Logger.getLogger(ManageImpExpAssAction.class);

    public void service(SourceBean request, SourceBean response) throws Exception {
	logger.debug("IN");
	try {
	    freezeHttpResponse();
	    httpRequest = getHttpRequest();
	    httpResponse = getHttpResponse();
	    msgBuild = MessageBuilderFactory.getMessageBuilder();
	    urlBuilder = new WebUrlBuilder();
	    String message = (String) request.getAttribute("MESSAGE");
	    if ((message != null) && (message.equalsIgnoreCase("SAVE_ASSOCIATION_FILE"))) {
		saveAssHandler();
	    } else if ((message != null) && (message.equalsIgnoreCase("GET_ASSOCIATION_FILE_LIST"))) {
		getAssListHandler(request);
	    } else if ((message != null) && (message.equalsIgnoreCase("DELETE_ASSOCIATION_FILE"))) {
		deleteAssHandler(request);
	    } else if ((message != null) && (message.equalsIgnoreCase("UPLOAD_ASSOCIATION_FILE"))) {
		uploadAssHandler(request);
	    } else if ((message != null) && (message.equalsIgnoreCase("DOWNLOAD_ASSOCIATION_FILE"))) {
		downloadAssHandler(request);
	    }
	} finally {
	    logger.debug("OUT");
	}
    }

    private void downloadAssHandler(SourceBean sbrequest) {
	logger.debug("IN");
	try {
	    String idass = (String) sbrequest.getAttribute("ID");
	    IAssociationFileDAO assfiledao = new AssociationFileDAO();
	    AssociationFile assFile = assfiledao.loadFromID(idass);
	    byte[] content = assfiledao.getContent(assFile);
	    httpResponse.setHeader("Content-Disposition", "attachment; filename=\"associations.xml \";");
	    httpResponse.setContentLength(content.length);
	    httpResponse.getOutputStream().write(content);
	    httpResponse.getOutputStream().flush();
	} catch (Exception e) {
	    logger.error("Error while filling response with the association file, ", e);
	} finally {
	    logger.debug("OUT");
	}
    }

    private void uploadAssHandler(SourceBean sbrequest) {
	logger.debug("IN");
	try {
	    String modality = "MANAGE";
	    String name = (String) sbrequest.getAttribute("NAME");
	    String description = (String) sbrequest.getAttribute("DESCRIPTION");
	    UploadedFile uplFile = (UploadedFile) sbrequest.getAttribute("UPLOADED_FILE");
	    AssociationFile assFile = new AssociationFile();
	    assFile.setDescription(description);
	    assFile.setName(name);
	    assFile.setDateCreation(new Date().getTime());
	    UUIDGenerator uuidgen = UUIDGenerator.getInstance();
	    UUID uuid = uuidgen.generateTimeBasedUUID();
	    String uuidStr = uuid.toString();
	    assFile.setId(uuidStr);
	    IAssociationFileDAO assfiledao = new AssociationFileDAO();
	    assfiledao.saveAssociationFile(assFile, uplFile.getFileContent());
	    List assFiles = assfiledao.getAssociationFiles();
	    String html = generateHtmlJsCss();
	    html += "<br/>";
	    html += generateHtmlForInsertNewForm();
	    html += "<br/>";
	    html += generateHtmlForList(assFiles, modality);
	    httpResponse.getOutputStream().write(html.getBytes());
	    httpResponse.getOutputStream().flush();
	} catch (Exception e) {
	    logger.error("Error while saving the association file, ", e);
	} finally {
	    logger.debug("OUT");
	}
    }

    private void deleteAssHandler(SourceBean sbrequest) {
	logger.debug("IN");
	try {
	    String modality = "MANAGE";
	    String idass = (String) sbrequest.getAttribute("ID");
	    IAssociationFileDAO assfiledao = new AssociationFileDAO();
	    AssociationFile assFile = assfiledao.loadFromID(idass);
	    assfiledao.deleteAssociationFile(assFile);
	    List assFiles = assfiledao.getAssociationFiles();
	    String html = generateHtmlJsCss();
	    html += "<br/>";
	    html += generateHtmlForInsertNewForm();
	    html += "<br/>";
	    html += generateHtmlForList(assFiles, modality);
	    httpResponse.getOutputStream().write(html.getBytes());
	    httpResponse.getOutputStream().flush();
	} catch (Exception e) {
	    logger.error("Error while deleting the association file, ", e);
	} finally {
	    logger.debug("OUT");
	}
    }

    private void getAssListHandler(SourceBean sbrequest) {
	logger.debug("IN");
	try {
	    String modality = (String) sbrequest.getAttribute("MODALITY");
	    IAssociationFileDAO assfiledao = new AssociationFileDAO();
	    List assFiles = assfiledao.getAssociationFiles();
	    String html = generateHtmlJsCss();
	    if (modality.equals("MANAGE")) {
		html += "<br/>";
		html += generateHtmlForInsertNewForm();
	    }
	    html += "<br/>";
	    html += generateHtmlForList(assFiles, modality);
	    httpResponse.getOutputStream().write(html.getBytes());
	    httpResponse.getOutputStream().flush();
	} catch (Exception e) {
	    logger.error("Error while getting the list of association files, ", e);
	} finally {
	    logger.debug("OUT");
	}
    }

    private String generateHtmlJsCss() {
	String html = "<LINK rel='StyleSheet' type='text/css' " + "      href='"
		+ urlBuilder.getResourceLink(httpRequest, "css/spagobi_shared.css") + "' />";
	html += "<LINK rel='StyleSheet' type='text/css' " + "      href='"
		+ urlBuilder.getResourceLink(httpRequest, "css/jsr168.css") + "' />";
	return html;
    }

    private String generateHtmlForInsertNewForm() {
	String html = "<div width='100%' class='portlet-section-header'>";
	html += "			&nbsp;&nbsp;&nbsp;<a class='linkAction' href='javascript:openclosenewform()'>Inserisci Nuovo</a>";
	html += "	   </div>";

	String action = httpRequest.getContextPath();
	action += "/servlet/AdapterHTTP";
	html += "		<div id='divFormNewAss' style='display:none;'>";
	html += "			<form action='" + action
		+ "' name='formNewAss' id='formNewAss' method='post' enctype='multipart/form-data'>  ";
	html += "				<input type='hidden' name='ACTION_NAME' value='MANAGE_IMPEXP_ASS_ACTION' >";
	html += "				<input type='hidden' name='MESSAGE' value='UPLOAD_ASSOCIATION_FILE' >";

	html += "<div class='div_form_container' >\n";
	html += "	<div class='div_form_margin' >\n";
	html += "		<div class='div_form_row' >\n";
	html += "			<div class='div_form_label'>\n";
	html += "				<span class='portlet-form-field-label'>\n";
	html += "					" + msgBuild.getMessage("impexp.name", "component_impexp_messages");
	html += "				</span>\n";
	html += "			</div>\n";
	html += "			<div class='div_form_field'>\n";
	html += "				<input class='portlet-form-input-field' type='text' name='NAME' \n";
	html += "	      	   		   id='nameNewAssToSave' />";
	html += "			</div>\n";
	html += "		</div>\n";
	html += "		<div class='div_form_row' >\n";
	html += "			<div class='div_form_label'>\n";
	html += "				<span class='portlet-form-field-label'>\n";
	html += "					" + msgBuild.getMessage("impexp.description", "component_impexp_messages");
	html += "				</span>\n";
	html += "			</div>\n";
	html += "			<div class='div_form_field'>\n";
	html += "				<input class='portlet-form-input-field' type='text' name='DESCRIPTION' \n";
	html += "	      	   		   id='descriptionNewAssToSave' />";
	html += "			</div>\n";
	html += "		</div>\n";
	html += "		<div class='div_form_row' >\n";
	html += "			<div class='div_form_label'>\n";
	html += "				<span class='portlet-form-field-label'>\n";
	html += "					" + msgBuild.getMessage("impexp.file", "component_impexp_messages");
	html += "				</span>\n";
	html += "			</div>\n";
	html += "			<div class='div_form_field'>\n";
	html += "				<input class='portlet-form-input-field' type='file' name='FILE' \n";
	html += "	      	   		   id='fileNewAssToSave' />";
	html += "			</div>\n";
	html += "		</div>\n";
	html += "		<div class='div_form_row' >\n";
	html += "			<div class='div_form_label'>\n";
	html += "				<span class='portlet-form-field-label'>\n";
	html += "					&nbsp;\n";
	html += "				</span>\n";
	html += "			</div>\n";
	html += "			<div class='div_form_field'>\n";
	html += "				<a class='link_without_dec' href=\"javascript:document.getElementById('formNewAss').submit()\">\n";
	html += "					<img src= '" + urlBuilder.getResourceLink(httpRequest, "/img/Save.gif") + "' "
		+ "                    	title='" + msgBuild.getMessage("impexp.save", "component_impexp_messages")
		+ "' " + " 					 	alt='" + msgBuild.getMessage("impexp.save", "component_impexp_messages") + "' />\n";
	html += "				</a>\n";
	html += "			</div>\n";
	html += "		</div>\n";
	html += "	</div>\n";
	html += "</div>\n";
	html += "<div style='clear:left;'>&nbsp;</div>\n";

	html += "			</form>";
	html += "		</div>";

	html += "		<script>";
	html += "			function openclosenewform() {";
	html += "				divfna = document.getElementById('divFormNewAss');";
	html += "				if(divfna.style.display=='none') {";
	html += "					divfna.style.display='inline';";
	html += "				} else {";
	html += "					divfna.style.display='none';";
	html += "				}";
	html += "			}";
	html += "		</script>";

	return html;
    }

    private String generateHtmlForList(List assFiles, String modality) {
	String html = "<table widht='100%'>";
	html += "<tr>";
	html += "<td class='portlet-section-header'>" + msgBuild.getMessage("impexp.name", "component_impexp_messages")
		+ "</td>";
	html += "<td class='portlet-section-header'>"
		+ msgBuild.getMessage("impexp.description", "component_impexp_messages") + "</td>";
	html += "<td class='portlet-section-header'>"
		+ msgBuild.getMessage("impexp.creationDate", "component_impexp_messages") + "</td>";
	html += "<td class='portlet-section-header'>&nbsp;</td>";
	html += "</tr>";
	String rowClass = "";
	boolean alternate = false;
	Iterator iterAssFile = assFiles.iterator();
	while (iterAssFile.hasNext()) {
	    rowClass = (alternate) ? "portlet-section-alternate" : "portlet-section-body";
	    alternate = !alternate;
	    AssociationFile assFile = (AssociationFile) iterAssFile.next();
	    html += "<tr>";
	    html += "<td class='" + rowClass + "'>" + assFile.getName() + "</td>";
	    html += "<td class='" + rowClass + "'>" + assFile.getDescription() + "</td>";
	    Date dat = new Date(assFile.getDateCreation());
	    Calendar cal = new GregorianCalendar();
	    cal.setTime(dat);
	    String datSt = "" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/"
		    + cal.get(Calendar.YEAR) + "   " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
	    html += "<td class='" + rowClass + "'>" + datSt + "</td>";
	    if (modality.equals("MANAGE")) {
		String eraseUrl = httpRequest.getContextPath();
		;
		eraseUrl += "/servlet/AdapterHTTP?ACTION_NAME=MANAGE_IMPEXP_ASS_ACTION";
		eraseUrl += "&MESSAGE=DELETE_ASSOCIATION_FILE&ID=" + assFile.getId();
		String downloadUrl = httpRequest.getContextPath();
		;
		downloadUrl += "/servlet/AdapterHTTP?ACTION_NAME=MANAGE_IMPEXP_ASS_ACTION";
		downloadUrl += "&MESSAGE=DOWNLOAD_ASSOCIATION_FILE&ID=" + assFile.getId();
		html += "<td class='" + rowClass + "'>\n";
		html += "<a class='link_without_dec' href='" + eraseUrl + "'>\n";
		html += "<img src='" + urlBuilder.getResourceLink(httpRequest, "/img/erase.gif") + "' \n" + "title='"
			+ msgBuild.getMessage("impexp.erase", "component_impexp_messages") + "' \n" + "alt='"
			+ msgBuild.getMessage("impexp.erase", "component_impexp_messages") + "' />\n";
		html += "</a>\n";
		html += "&nbsp;&nbsp;\n";
		html += "<a class='link_without_dec' href='" + downloadUrl + "'>\n";
		html += "<img src='" + urlBuilder.getResourceLink(httpRequest, "/img/down16.gif") + "' \n" + "title='"
			+ msgBuild.getMessage("Sbi.download", "component_impexp_messages") + "' \n" + "alt='"
			+ msgBuild.getMessage("Sbi.download", "component_impexp_messages") + "' />\n";
		html += "</a>\n";
		html += "</td>";
	    } else if (modality.equals("SELECT")) {
		html += "<td class='" + rowClass + "'>\n";
		html += "<a class='link_without_dec' href=\"javascript:parent.selectAssFile('" + assFile.getId()
			+ "', '" + assFile.getName() + "')\">\n";
		html += "<img src='" + urlBuilder.getResourceLink(httpRequest, "/img/button_ok.gif") + "' \n"
			+ "title='" + msgBuild.getMessage("impexp.select", "component_impexp_messages") + "' \n"
			+ "alt='" + msgBuild.getMessage("mpexp.select", "component_impexp_messages") + "' />\n";
		html += "</a>\n";
	    }
	    html += "</tr>";
	}
	html += "</table>";
	return html;
    }

    private void saveAssHandler() {
	logger.debug("IN");
	String htmlResp = "";
	try {
	    String pathFileAss = httpRequest.getParameter("PATH");
	    String name = httpRequest.getParameter("NAME");
	    String description = httpRequest.getParameter("DESCRIPTION");
	    AssociationFile assFile = new AssociationFile();
	    assFile.setDescription(description);
	    assFile.setName(name);
	    assFile.setDateCreation(new Date().getTime());
	    UUIDGenerator uuidgen = UUIDGenerator.getInstance();
	    UUID uuid = uuidgen.generateTimeBasedUUID();
	    String uuidStr = uuid.toString();
	    assFile.setId(uuidStr);
	    FileInputStream fis = new FileInputStream(pathFileAss);
	    byte[] fileAssContent = GeneralUtilities.getByteArrayFromInputStream(fis);
	    fis.close();
	    IAssociationFileDAO assfiledao = new AssociationFileDAO();
	    assfiledao.saveAssociationFile(assFile, fileAssContent);
	    htmlResp = "File delle associzioni salvato correttamente";
	} catch (Exception e) {
	    logger.error("Error wile saving the association file, ", e);
	    htmlResp = "Errore durante il salvataggio del file";
	} finally {
	    try {
		httpResponse.getOutputStream().write(htmlResp.getBytes());
		httpResponse.getOutputStream().flush();
	    } catch (Exception e) {
		logger.error("Error while sending response to the client, ", e);
	    }
	    logger.debug("OUT");
	}
    }

}
