package it.eng.spagobi.importexport.actions;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.importexport.to.AssociationFile;
import it.eng.spagobi.importexport.to.dao.AssociationFileDAO;
import it.eng.spagobi.importexport.to.dao.IAssociationFileDAO;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;
import it.eng.spagobi.utilities.UploadedFile;
import it.eng.spagobi.utilities.messages.IMessageBuilder;
import it.eng.spagobi.utilities.messages.MessageBuilderFactory;
import it.eng.spagobi.utilities.urls.WebUrlBuilder;

import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ManageImpExpAssAction extends  AbstractHttpAction {
	
	private HttpServletRequest httpRequest = null;
	private HttpServletResponse httpResponse = null;
	IMessageBuilder msgBuild = null;
	WebUrlBuilder urlBuilder = null;
	private Locale locale = null;
	
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		try{
			freezeHttpResponse();
			httpRequest = getHttpRequest();
			httpResponse = getHttpResponse();
			msgBuild = MessageBuilderFactory.getMessageBuilder();
			urlBuilder = new WebUrlBuilder();
			String language = httpRequest.getParameter("language");
			String country = httpRequest.getParameter("country");
			try {
				locale = new Locale(language, country);
			} catch (Exception e) {
				// ignore, the defualt locale will be considered
			}
			String message = (String)request.getAttribute("MESSAGE");
			if((message!=null) && (message.equalsIgnoreCase("SAVE_ASSOCIATION_FILE"))){
				saveAssHandler();
			} else if((message!=null) && (message.equalsIgnoreCase("GET_ASSOCIATION_FILE_LIST"))){
				getAssListHandler(request);
			} else if((message!=null) && (message.equalsIgnoreCase("DELETE_ASSOCIATION_FILE"))){
				deleteAssHandler(request);
			} else if((message!=null) && (message.equalsIgnoreCase("UPLOAD_ASSOCIATION_FILE"))){
				uploadAssHandler(request);
			} else if((message!=null) && (message.equalsIgnoreCase("DOWNLOAD_ASSOCIATION_FILE"))){
				downloadAssHandler(request);
			} else if ((message!=null) && (message.equalsIgnoreCase("CHECK_IF_EXISTS"))) {
				checkIfExistsHandler(request);
			}
		} finally {}
	}
		
	
	private void checkIfExistsHandler(SourceBean sbrequest) {
		String id = (String) sbrequest.getAttribute("ID");
		IAssociationFileDAO assfiledao = new AssociationFileDAO();
		String htmlResp = Boolean.toString(assfiledao.exists(id));
		try {
			httpResponse.getOutputStream().write(htmlResp.getBytes());
			httpResponse.getOutputStream().flush();	
		} catch (Exception e){
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "saveAssHandler",
                    			  "Error while sending response to the client, " + e);
		}
	}


	private void downloadAssHandler(SourceBean sbrequest) {
		try {
			String idass = (String)sbrequest.getAttribute("ID");
			IAssociationFileDAO assfiledao = new AssociationFileDAO();
			AssociationFile assFile = assfiledao.loadFromID(idass);
			byte[] content = assfiledao.getContent(assFile);
			httpResponse.setHeader("Content-Disposition","attachment; filename=\"associations.xml \";");
			httpResponse.setContentLength(content.length);
			httpResponse.getOutputStream().write(content);
			httpResponse.getOutputStream().flush();
		} catch (Exception e) {
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "downloadAssHandler",
                    			  "Error while filling response with the association file, " + e);
		}
	}
	
	
	private void uploadAssHandler(SourceBean sbrequest) {
		try{
			String modality = "MANAGE";
			String name = (String)sbrequest.getAttribute("NAME");
			if (name == null || name.trim().equals("")) {
				String msg = msgBuild.getMessage("Sbi.saving.nameNotSpecified", "component_impexp_messages", locale);
				httpResponse.getOutputStream().write(msg.getBytes());
				httpResponse.getOutputStream().flush();
				return;
			}
			String description = (String) sbrequest.getAttribute("DESCRIPTION");
			if (description == null) description = "";
			UploadedFile uplFile = (UploadedFile)sbrequest.getAttribute("UPLOADED_FILE");
			if (uplFile == null || uplFile.getFileName().trim().equals("")) {
				String msg = msgBuild.getMessage("Sbi.saving.associationFileNotSpecified", "component_impexp_messages", locale);
				httpResponse.getOutputStream().write(msg.getBytes());
				httpResponse.getOutputStream().flush();
				return;
			} else {
				if (!AssociationFile.isValidContent(uplFile.getFileContent())) {
					String msg = msgBuild.getMessage("Sbi.saving.associationFileNotValid", "component_impexp_messages", locale);
					httpResponse.getOutputStream().write(msg.getBytes());
					httpResponse.getOutputStream().flush();
					return;
				}
			}
			String overwriteStr = (String)sbrequest.getAttribute("OVERWRITE");
			boolean overwrite = (overwriteStr == null || overwriteStr.trim().equals("")) ? false : Boolean.parseBoolean(overwriteStr);
			AssociationFile assFile = new AssociationFile();
			assFile.setDescription(description);
			assFile.setName(name);
			assFile.setDateCreation(new Date().getTime());
//			UUIDGenerator uuidgen = UUIDGenerator.getInstance();
//			UUID uuid = uuidgen.generateTimeBasedUUID();
//			String uuidStr = uuid.toString();
//			assFile.setId(uuidStr);
			assFile.setId(name);
			IAssociationFileDAO assfiledao = new AssociationFileDAO();
			if (assfiledao.exists(assFile.getId())) {
				if (overwrite) {
					assfiledao.deleteAssociationFile(assFile);
					assfiledao.saveAssociationFile(assFile, uplFile.getFileContent());
				} else {
					SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "uploadAssHandler",
	                           "Overwrite parameter is false: association file with id=[" + assFile.getId() + "] " +
	                           		"and name=[" + assFile.getName() + "] will not be saved.");
				}
			} else {
				assfiledao.saveAssociationFile(assFile, uplFile.getFileContent());
			}
			List assFiles = assfiledao.getAssociationFiles();
			String html = generateHtmlJsCss();
			html += "<br/>";
			html += generateHtmlForInsertNewForm();
			html += "<br/>";
			html += generateHtmlForList(assFiles, modality);
			httpResponse.getOutputStream().write(html.getBytes());
			httpResponse.getOutputStream().flush();	
		} catch (Exception e) {
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "uploadAssHandler",
		                           "Error while saving the association file, " + e);
		} finally {	}
	}
	
	
	private void deleteAssHandler(SourceBean sbrequest) {
		try {
			String modality = "MANAGE";
			String idass = (String)sbrequest.getAttribute("ID");
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
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "deleteAssHandler",
                    			  "Error while deleting the association file, " + e);
		}
	}
	
	
	private void getAssListHandler(SourceBean sbrequest) {
		try{
			String modality = (String)sbrequest.getAttribute("MODALITY");
			IAssociationFileDAO assfiledao = new AssociationFileDAO();
			List assFiles = assfiledao.getAssociationFiles();
			String html = generateHtmlJsCss();
			if(modality.equals("MANAGE")) {
				html += "<br/>";
				html += generateHtmlForInsertNewForm();
			}
			html += "<br/>";
			html += generateHtmlForList(assFiles, modality);
			httpResponse.getOutputStream().write(html.getBytes());
			httpResponse.getOutputStream().flush();	
		} catch (Exception e) {
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "getAssListHandler",
                    			  "Error while getting the list of association files, " + e);
		}
	}
	
	
	private String generateHtmlJsCss() {
		String html = "<LINK rel='StyleSheet' type='text/css' " +
				      "      href='"+urlBuilder.getResourceLink(httpRequest, "css/spagobi_shared.css")+"' />";
		html +=  "<LINK rel='StyleSheet' type='text/css' " +
	      		 "      href='"+urlBuilder.getResourceLink(httpRequest, "css/jsr168.css")+"' />";
		html +=  "<script type=\"text/javascript\" " +
				"		src=\"" + urlBuilder.getResourceLink(httpRequest, "/js/prototype/javascripts/prototype.js") + "\"></script>";
		return html;
	}
	
	private String generateHtmlForInsertNewForm() {
		String html = "<div width='100%' class='portlet-section-header'>";
		html += "			&nbsp;&nbsp;&nbsp;<a class='linkAction' href='javascript:openclosenewform()'>" 
			+ msgBuild.getMessage("Sbi.saving.insertNew", "component_impexp_messages", locale) 
			+ "</a>";
		html += "	   </div>";

		String action = httpRequest.getContextPath(); 
		action += "/servlet/AdapterHTTP";	
		html += "		<div id='divFormNewAss' style='display:none;'>";
		html += "			<form action='"+action+"' name='formNewAss' id='formNewAss' method='post' enctype='multipart/form-data'>  ";
		html += "				<input type='hidden' name='ACTION_NAME' value='MANAGE_IMPEXP_ASS_ACTION' >";
		html += "				<input type='hidden' name='MESSAGE' value='UPLOAD_ASSOCIATION_FILE' >";
		html += "				<input type='hidden' name='OVERWRITE' id='OVERWRITE' value='' >";
		
		
		html += "<div class='div_form_container' >\n";
		html += "	<div class='div_form_margin' >\n";
		html += "		<div class='div_form_row' >\n";
		html += "			<div class='div_form_label'>\n";
		html += "				<span class='portlet-form-field-label'>\n";
		html += "					" + msgBuild.getMessage("impexp.name", "component_impexp_messages", locale);
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
		html += "					" + msgBuild.getMessage("impexp.description", "component_impexp_messages", locale);
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
		html += "					" + msgBuild.getMessage("impexp.file", "component_impexp_messages", locale);
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
		html += "					&nbsp;\n" ;
		html += "				</span>\n";
		html += "			</div>\n";
		html += "			<div class='div_form_field'>\n";
		html += "				<a class='link_without_dec' href=\"javascript:checkIfExists()\">\n";
		html += "					<img src= '"+urlBuilder.getResourceLink(httpRequest, "/img/Save.gif")+"' " +
				"                    	title='"+msgBuild.getMessage("impexp.save", "component_impexp_messages", locale)+"' " + 
				" 					 	alt='"+msgBuild.getMessage("impexp.save", "component_impexp_messages", locale)+"' />\n";
		html += "				</a>\n";	
		html += "			</div>\n";
		html += "		</div>\n";
		html += "	</div>\n";
		html += "</div>\n";
		html += "<div style='clear:left;'>&nbsp;</div>\n";
		
		/*
		html += "				<table>";
		html += "					<tr height='25px'>";
	    html += "						<td>&nbsp;&nbsp;Nome:</td>";
		html += "						<td><input type='text' name='NAME' id='nameNewAssToSave'/></td>";
		html += "					</tr>";
		html += "					<tr height='25px'>";
		html += "						<td>&nbsp;&nbsp;Descrizione:</td>";
		html += "						<td><input type='text' name='DESCRIPTION' id='descriptionNewAssToSave'/></td>";
		html += "					</tr>";
		html += "					<tr height='25px'>";
		html += "						<td>&nbsp;&nbsp;File:</td>";
		html += "						<td><input type='file' name='FILE' id='fileNewAssToSave'/></td>";
		html += "					</tr>";
		html += "					<tr height='45px' valign='middle'>";
		html += "						<td>&nbsp;</td>";
		html += "						<td><input type='submit' value='save' /></td>";
		html += "					</tr>";
		html += "				</table>";
		*/
		
		
		
		
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
		
		// check if the association already exists
		html += "		<script>\n";
		html += "		function checkIfExists() {\n";
		html += "			nameass = document.getElementById('nameNewAssToSave').value;\n";
		html += "			if (nameass==''){\n";
		html += "				alert('" + msgBuild.getMessage("Sbi.saving.nameNotSpecified", "component_impexp_messages", locale) + "');\n";
		html += "				return;\n";
		html += "			}\n";
		html += "			checkAssUrl = '" + httpRequest.getContextPath() + "';\n";
		html += "			checkAssUrl += '/servlet/AdapterHTTP?';\n";
		html += "			pars = 'ACTION_NAME=MANAGE_IMPEXP_ASS_ACTION&MESSAGE=CHECK_IF_EXISTS&ID=' + document.getElementById('nameNewAssToSave').value;\n";
		html += "			new Ajax.Request(checkAssUrl,\n";
		html += "		 		{\n";
		html += "		    		method: 'post',\n";
		html += "		    		parameters: pars,\n";
		html += "		    		onSuccess: function(transport){\n";
		html += "		            	        	response = transport.responseText || \"\";\n";
		html += "		                	    	saveAss(response);\n";
		html += "		                	   },\n";
		html += "		    		onFailure: somethingWentWrongSaveAss,\n";
		html += "		    		asynchronous: false\n";
		html += "		 		 }\n";
		html += "			 );\n";
		html += "		}\n";
		html += "		function somethingWentWrongSaveAss() {\n";
		html += "		}\n";
		html += "		</script>\n";
		
		// save the association
		html += "		<script>\n";
		html += "		function saveAss(exists) {\n";
		html += "			if (exists != 'true' || confirm('" + msgBuild.getMessage("Sbi.saving.alreadyExisting", "component_impexp_messages", locale) + "')) {\n";
		html += "				document.getElementById('OVERWRITE').value = 'true';\n";
		html += "				document.getElementById('formNewAss').submit();\n";
		html += "			}\n";
		html += "		}\n";
		html += "		</script>\n";
		return html;
	}
	
	private String generateHtmlForList(List assFiles, String modality) {
		String html = "<table widht='100%'>";
		html += "<tr>";
		html += "<td class='portlet-section-header'>"+msgBuild.getMessage("impexp.name", "component_impexp_messages", locale)+"</td>";
		html += "<td class='portlet-section-header'>"+msgBuild.getMessage("impexp.description", "component_impexp_messages", locale)+"</td>";
		html += "<td class='portlet-section-header'>"+msgBuild.getMessage("impexp.creationDate", "component_impexp_messages", locale)+"</td>";
		html += "<td class='portlet-section-header'>&nbsp;</td>";
		html += "</tr>";
		String rowClass = "";
		boolean alternate = false;
		Iterator iterAssFile = assFiles.iterator();
		while(iterAssFile.hasNext()) {
			rowClass = (alternate) ? "portlet-section-alternate" : "portlet-section-body";
			alternate = !alternate;
			AssociationFile assFile = (AssociationFile)iterAssFile.next();
			html += "<tr>";
			html += "<td class='"+rowClass+"'>"+assFile.getName()+"</td>";
			html += "<td class='"+rowClass+"'>"+assFile.getDescription()+"</td>";
			Date dat = new Date(assFile.getDateCreation());
			Calendar cal = new GregorianCalendar();
			cal.setTime(dat);
			String datSt = "" +  cal.get(Calendar.DAY_OF_MONTH) + "/" +
								 (cal.get(Calendar.MONTH) + 1) + "/" +
								 cal.get(Calendar.YEAR) +  "   " +
								 cal.get(Calendar.HOUR_OF_DAY) + ":" +
								 (cal.get(Calendar.MINUTE) < 10 ? "0" : "") + 
								 cal.get(Calendar.MINUTE);
			html += "<td class='"+rowClass+"'>"+datSt+"</td>";
			if(modality.equals("MANAGE")) {
				String eraseUrl = httpRequest.getContextPath(); ;
				eraseUrl += "/servlet/AdapterHTTP?ACTION_NAME=MANAGE_IMPEXP_ASS_ACTION";	
				eraseUrl += "&MESSAGE=DELETE_ASSOCIATION_FILE&ID="+assFile.getId();	
				String downloadUrl = httpRequest.getContextPath(); ;
				downloadUrl += "/servlet/AdapterHTTP?ACTION_NAME=MANAGE_IMPEXP_ASS_ACTION";	
				downloadUrl += "&MESSAGE=DOWNLOAD_ASSOCIATION_FILE&ID="+assFile.getId();	
				html += "<td class='"+rowClass+"'>\n";
				html += "<a class='link_without_dec' href='"+eraseUrl+"'>\n";		
				html += "<img src='"+urlBuilder.getResourceLink(httpRequest, "/img/erase.gif")+"' \n" + 
						"title='"+msgBuild.getMessage("impexp.erase", "component_impexp_messages", locale)+"' \n" + 
						"alt='"+msgBuild.getMessage("impexp.erase", "component_impexp_messages", locale)+"' />\n";
				html += "</a>\n";		
				html += "&nbsp;&nbsp;\n";
				html += "<a class='link_without_dec' href='"+downloadUrl+"'>\n";
				html += "<img src='"+urlBuilder.getResourceLink(httpRequest, "/img/down16.gif")+"' \n" + 
						"title='"+msgBuild.getMessage("Sbi.download", "component_impexp_messages", locale)+"' \n" + 
						"alt='"+msgBuild.getMessage("Sbi.download", "component_impexp_messages", locale)+"' />\n";
				html += "</a>\n";		
				html += "</td>";
			} else if(modality.equals("SELECT") ) {
				html += "<td class='"+rowClass+"'>\n";
				html += "<a class='link_without_dec' href=\"javascript:parent.selectAssFile('"+assFile.getId()+"', '"+assFile.getName()+"')\">\n";
				html += "<img src='"+urlBuilder.getResourceLink(httpRequest, "/img/button_ok.gif")+"' \n" + 
				"title='"+msgBuild.getMessage("impexp.select", "component_impexp_messages", locale)+"' \n" + 
				"alt='"+msgBuild.getMessage("mpexp.select", "component_impexp_messages", locale)+"' />\n";
				html += "</a>\n";
			}
			html += "</tr>";
		}
		html += "</table>";
		return html;
	}
	

	private void saveAssHandler() {
		String htmlResp = "";
		try{
			String pathFileAss = httpRequest.getParameter("PATH");
			String name = httpRequest.getParameter("NAME");
			String description = httpRequest.getParameter("DESCRIPTION");
			String overwriteStr = httpRequest.getParameter("OVERWRITE");
			boolean overwrite = (overwriteStr == null || overwriteStr.trim().equals("")) ? false : Boolean.parseBoolean(overwriteStr);
			AssociationFile assFile = new AssociationFile();
			assFile.setDescription(description);
			assFile.setName(name);
			assFile.setDateCreation(new Date().getTime());
//			UUIDGenerator uuidgen = UUIDGenerator.getInstance();
//			UUID uuid = uuidgen.generateTimeBasedUUID();
//			String uuidStr = uuid.toString();
//			assFile.setId(uuidStr);
			assFile.setId(name);
			FileInputStream fis = new FileInputStream(pathFileAss);
			byte[] fileAssContent = GeneralUtilities.getByteArrayFromInputStream(fis);
			fis.close();
			IAssociationFileDAO assfiledao = new AssociationFileDAO();
			if (assfiledao.exists(assFile.getId())) {
				if (overwrite) {
					assfiledao.deleteAssociationFile(assFile);
					assfiledao.saveAssociationFile(assFile, fileAssContent);
				} else {
					SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "saveAssHandler",
	                           "Overwrite parameter is false: association file with id=[" + assFile.getId() + "] " +
	                           		"and name=[" + assFile.getName() + "] will not be saved.");
				}
			} else {
				assfiledao.saveAssociationFile(assFile, fileAssContent);
			}
			htmlResp = msgBuild.getMessage("Sbi.saved.ok", "component_impexp_messages", locale);
		} catch (Exception e) {
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "saveAssHandler",
		                           "Error wile saving the association file, " + e);
			htmlResp = msgBuild.getMessage("Sbi.saved.ko", "component_impexp_messages", locale);
		} finally {	
			try{
				httpResponse.getOutputStream().write(htmlResp.getBytes());
				httpResponse.getOutputStream().flush();	
			} catch (Exception e){
				SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "saveAssHandler",
                        			  "Error while sending response to the client, " + e);
			}
		}
	}

	
	
	
	
	
	
	
}
