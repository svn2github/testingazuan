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
package it.eng.spagobi.presentation.tags;

import java.util.Iterator;
import java.util.List;

import it.eng.spago.base.Constants;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.utilities.PortletUtilities;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Presentation tag for Query Wizard details. 
 * 
 * @author Zerbetto
 */

public class QueryWizardTag extends TagSupport {

	private String connectionName;
	private String visibleColumns;
	private String invisibleColumns;
	private String valueColumns;
	private String queryDef;
	
	private HttpServletRequest httpRequest = null;
    protected RenderRequest renderRequest = null;
    protected RenderResponse renderResponse = null;
	
	public int doStartTag() throws JspException {
		TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, "QueryWizardTag::doStartTag:: invocato");
		httpRequest = (HttpServletRequest) pageContext.getRequest();
		renderRequest = (RenderRequest) httpRequest.getAttribute("javax.portlet.request");
		renderResponse = (RenderResponse) httpRequest.getAttribute("javax.portlet.response");
		String connNameField = PortletUtilities.getMessage("SBIDev.queryWiz.connNameField", "messages");
		String visColumnsField = PortletUtilities.getMessage("SBIDev.queryWiz.visColumnsField", "messages");
		String invisColumnsField = PortletUtilities.getMessage("SBIDev.queryWiz.invisColumnsField", "messages");
		String valueColumnsField = PortletUtilities.getMessage("SBIDev.queryWiz.valueColumnsField", "messages");
		String queryDefField = PortletUtilities.getMessage("SBIDev.queryWiz.queryDefField", "messages");
		String columnsField = PortletUtilities.getMessage("SBIDev.queryWiz.columnsField", "messages");
		String noPointNotationError = PortletUtilities.getMessage("SBIDev.queryWiz.noPointNotationError", "messages");
		String updateFieldsFromQuery = PortletUtilities.getMessage("SBIDev.queryWiz.updateFieldsFromQuery", "messages");
		String isNotASelect = PortletUtilities.getMessage("SBIDev.queryWiz.isNotASelect", "messages");
		String incorrectQuery = PortletUtilities.getMessage("SBIDev.queryWiz.incorrectQuery", "messages");
		StringBuffer output = new StringBuffer();
		
		
		output.append("<div id='queryWizardWithoutJavascript' style='display:inline;'>\n");
		output.append("	 <div class='div_detail_area_forms_lov'>\n");	
		output.append("		<div class='div_detail_label_lov'>\n");
		output.append("			<span class='portlet-form-field-label'>\n");
		output.append(connNameField);
		output.append("			</span>\n");
		output.append("		</div>\n");
		output.append("		<div class='div_detail_form'>\n");
		output.append("			<select style='width:180px;' class='portlet-form-input-field' name='connName' id='connName'>\n");
		ConfigSingleton config = ConfigSingleton.getInstance();
		List dbConnection = config.getAttributeAsList("DATA-ACCESS.CONNECTION-POOL");
		Iterator it = dbConnection.iterator();
		while (it.hasNext()) {
			SourceBean connectionPool = (SourceBean) it.next();
			String connectionPoolName = (String) connectionPool.getAttribute("connectionPoolName");
			String connectionDescription = (String) connectionPool.getAttribute("connectionDescription");
			if (connectionDescription == null || connectionDescription.trim().equals("")) connectionDescription = connectionPoolName;
			String connNameSelected = "";
			if (connectionPoolName.equals(connectionName)) connNameSelected = "selected=\"selected\"";
			output.append("			<option value='" + connectionPoolName + "' " + connNameSelected + ">" + connectionDescription + "</option>\n");
		}
		output.append("			</select>\n");
		output.append("		</div>\n");
		output.append("		<div class='div_detail_label_lov'>\n");
		output.append("			<span class='portlet-form-field-label'>\n");
		output.append(visColumnsField);
		output.append("			</span>\n");
		output.append("		</div>\n");
		output.append("		<div class='div_detail_form'>\n");
		output.append("			<input class='portlet-form-input-field' type='text' name='visColumns' id='visColumns' size='50' value='"+ visibleColumns + "' maxlength='100'>&nbsp;*\n");
		output.append("		</div>\n");
		output.append("		<div class='div_detail_label_lov'>\n");
		output.append("			<span class='portlet-form-field-label'>\n");
		output.append(invisColumnsField);
		output.append("			</span>\n");
		output.append("		</div>\n");
		output.append("		<div class='div_detail_form'>\n");
		output.append("			<input class='portlet-form-input-field' type='text' name='invisColumns' id='invisColumns' size='50' value='"+ invisibleColumns + "' maxlength='100'>&nbsp;*\n");
		output.append("		</div>\n");
		output.append("		<div class='div_detail_label_lov'>\n");
		output.append("			<span class='portlet-form-field-label'>\n");
		output.append(valueColumnsField);
		output.append("			</span>\n");
		output.append("		</div>\n");
		output.append("		<div class='div_detail_form'>\n");
		output.append("			<input class='portlet-form-input-field' type='text' name='valueColumns' id='valueColumns' size='50' value='"+ valueColumns + "' maxlength='100'>&nbsp;*\n");
		output.append("		</div>\n");
		output.append("		<div class='div_detail_label_lov'>\n");
		output.append("			<span class='portlet-form-field-label'>\n");
		output.append(queryDefField);
		output.append("			</span>\n");
		output.append("		</div>\n");
		output.append("		<div class='div_detail_form'>\n");
		output.append("			<textarea class='portlet-text-area-field' name='queryDef' id='queryDef' rows='5' cols='50'>" + queryDef + "</textarea>\n");
		output.append("		</div>\n");
		output.append("  </div>\n");
	    output.append("</div>\n");
		
		
	    output.append("<div id='queryWizardWithJavascript' style='display:none;'>\n");
	    output.append("	 <div class='div_detail_area_forms_lov'>\n");	
	    output.append("		<div class='div_detail_label_lov'>\n");
		output.append("			<span class='portlet-form-field-label'>\n");
		output.append(connNameField);
		output.append("			</span>\n");
		output.append("		</div>\n");
		output.append("		<div class='div_detail_form'>\n");
		output.append("			<select onchange='setLovProviderModified(true);setConnName(this.selectedIndex);' style='width:180px;' class='portlet-form-input-field' name='connNameJS' id='connNameJS' >\n");
		it = dbConnection.iterator();
		while (it.hasNext()) {
			SourceBean connectionPool = (SourceBean) it.next();
			String connectionPoolName = (String) connectionPool.getAttribute("connectionPoolName");
			String connectionDescription = (String) connectionPool.getAttribute("connectionDescription");
			if (connectionDescription == null || connectionDescription.trim().equals("")) connectionDescription = connectionPoolName;
			String connNameSelected = "";
			if (connectionPoolName.equals(connectionName)) connNameSelected = "selected=\"selected\"";
			output.append("			<option value='" + connectionPoolName + "' " + connNameSelected + ">" + connectionDescription + "</option>\n");
		}
		output.append("			</select>\n");
		output.append("		</div>\n");
		output.append("		<div class='div_detail_label_lov'>\n");
		output.append("			<span class='portlet-form-field-label'>\n");
		output.append(queryDefField);
		output.append("			</span>\n");
		output.append("		</div>\n");
		output.append("		<div style='height:110px;' class='div_detail_form'>\n");
		output.append("			<textarea style='height:100px;' class='portlet-text-area-field' name='queryDefJS' onchange='setLovProviderModified(true);setQueryDef(this.value);'  cols='50'>" + queryDef + "</textarea>\n");
		output.append("		</div>\n");
		output.append("		<div class='div_detail_label_lov'>\n");
		output.append("			&nbsp;\n");
		output.append("		</div>\n");
		output.append("		<div >\n");
		output.append("			<a onclick='' href='javascript:void(0)' style='text-decoration:none;'>\n");
		output.append("				<img style='width:20px;height:20px;' src='" + renderResponse.encodeURL(renderRequest.getContextPath() + "/img/updateState.gif") + "' />\n");
		output.append("			</a>\n");
		output.append("			<a onclick='' href='javascript:void(0)' class='portlet-form-field-label' style='text-decoration:none;'>\n");
		output.append("				" + updateFieldsFromQuery + "\n");
		output.append("			</a>\n");
		output.append("		</div>\n");
		output.append("     <div id='fieldsDiv' style=\"width:100%;margin:10px 0px 0px 0px;\"></div>\n");
		output.append("  </div>\n");
		output.append("</div>\n");
	    
	
	     
	    output.append("<script type='text/javascript'>\n");
	    output.append("function displayQueryFields() {\n");
	    output.append(" var valueColumn = '" + valueColumns + "';\n");
	    output.append(" var visibleColumns = new Array();\n");
	    String[] visColumns = visibleColumns.split(",");
	    for (int i = 0; i < visColumns.length; i++) {
	    	String visibleColumn = visColumns[i].trim();
	    	visColumns[i] = visibleColumn;
	    	output.append(" visibleColumns[" + i + "] = '" + visibleColumn +"';\n");
	    }
	    output.append("	var fields = findFieldsFromQuery();\n");
	    output.append("	var strHTML = generateHTML(fields, valueColumn, visibleColumns);\n");
	    output.append("	document.getElementById('fieldsDiv').innerHTML = strHTML;\n");
	    output.append("}\n");
	    
	    output.append("function generateHTML(fields, valueColumn, visibleColumns) {\n");
	    output.append("	var strHTML ='';\n");
	    output.append("	strHTML += '<table class=\"object-details-table\" style=\"margin:5px;width:100%;\">';\n");
	    output.append("	strHTML += '<tr>';\n");
	    output.append("	strHTML += '<td class=\"portlet-section-header\">' + '" + columnsField + "' + '</td>';\n");
	    output.append("	strHTML += '<td class=\"portlet-section-header\" style=\"text-align:center;width:100px;\">' + '" + valueColumnsField + "' + '</td>';\n");
	    output.append("	strHTML += '<td class=\"portlet-section-header\" style=\"text-align:center;width:100px;\">' + '" + visColumnsField + "' + '</td>';\n");
	    output.append("	strHTML += '</tr>';\n");
	    output.append("	var rowClass;\n");
	    output.append("	var alternate = false;\n");
	    output.append("	for (i = 0; i < fields.length; i++) {\n");
	    output.append("		if (alternate) rowClass = \"portlet-section-alternate\";\n");
	    output.append("		else rowClass = \"portlet-section-body\";\n");
	    output.append("		alternate = !alternate;\n");
	    output.append("		strHTML += '<tr class=\"portlet-font\">';\n");
	    output.append("		strHTML += '<td class=\"' + rowClass + '\">' + fields[i] + '</td>';\n");
	    output.append("		var isValueColumn = '';\n");
	    output.append("		if (fields[i] == valueColumn) isValueColumn='checked=\"checked\"';\n");
	    output.append("		strHTML += '<td class=\"' + rowClass + '\" align=\"center\"><input type=\"radio\" onclick=\"setValueColumn(this.value);\" onchange=\"setLovProviderModified(true);\" name=\"valueColumnsJS\" value=\"' + fields[i] + '\"  ' + isValueColumn + '></td>';\n");
	    output.append("		var isVisible = '';\n");
	    output.append("		for (j = 0; j < visibleColumns.length; j++) {\n");
	    output.append("			if (fields[i] == visibleColumns[j]) isVisible='checked=\"checked\"';\n");
	    output.append("		}\n");
	    output.append("		strHTML += '<td class=\"' + rowClass + '\" align=\"center\"><input type=\"checkbox\" onclick=\"setVisibleColumns(this.value,this.checked);\" onchange=\"setLovProviderModified(true);\" name=\"visColumnsJS\" id=\"visColumnsJS\" value=\"' + fields[i] + '\" ' + isVisible + '></td>';\n");
	    output.append("		strHTML += '</tr>';\n");
	    output.append("	}\n");
	    output.append("	return strHTML;\n");
	    output.append("}\n");
	    
	    output.append("function findFieldsFromQuery() {\n");
	    output.append(" var queryDef = document.getElementById('queryDef').value;\n");
	    output.append(" if (trim(queryDef) == '') {\n");
	    output.append("		resetFields();\n");
	    output.append("		return new Array();\n");
	    output.append("	}\n");
	    output.append(" var queryDefUC = queryDef.toUpperCase();\n"); 
	    output.append("	if (trim(queryDefUC).length > 0 && !isASelect(queryDefUC)) {\n");
	    output.append("		alert('" + isNotASelect + "');\n");
	    output.append("		resetFields();\n");
	    output.append("		return new Array();\n");
	    output.append("	}\n");
	    output.append("	var initialIndex = 0;\n");
	    output.append("	if (queryDefUC.indexOf(' DISTINCT ') == -1) initialIndex = queryDefUC.indexOf('SELECT ') + 7;\n");
	    output.append("	else initialIndex = queryDefUC.indexOf(' DISTINCT ') + 10;\n");
	    output.append("	var finalIndex = queryDefUC.indexOf(' FROM ');\n");
	    output.append("	var selectClause = queryDef.substring(initialIndex, finalIndex);\n");
	    output.append("	var selectFields = selectClause.split(',');\n");
	    output.append("	var fields = new Array();\n");
	    output.append("	for (i = 0; i < selectFields.length; i++) {\n");
	    output.append("		var temp;\n");
	    output.append("		var aSelectFieldUC = selectFields[i].toUpperCase();\n");
	    output.append("		if (aSelectFieldUC.indexOf(' AS ') != -1) {\n");
	    output.append("			temp = selectFields[i].substring(aSelectFieldUC.indexOf(' AS ') + 4);\n");
	    output.append("		}\n");
	    output.append(" 	else {\n");
	    output.append(" 		temp = selectFields[i];\n");
	    output.append(" 	}\n");
	    output.append("		temp = trim(temp);\n");
	    output.append("		if (!isCorrect(temp)) {\n");
	    output.append("			alert('" + incorrectQuery + "');\n");
	    output.append("			resetFields();\n");
	    output.append("			return new Array();\n");
	    output.append("		}\n");
	    output.append("		if (temp.indexOf('.') != -1) {\n");
	    output.append("			alert('" + noPointNotationError + "');\n");
	    output.append("			resetFields();\n");
	    output.append("			return new Array();\n");
	    output.append("		}\n");
	    output.append("		fields[i]=temp;\n");
	    output.append("	}\n");
	    output.append("	return fields;\n");
	    output.append("}\n");
	    
	    output.append("function updateFields() {\n");
	    output.append("	var fields = findFieldsFromQuery();\n");
	    // looks if there are some old fields (no more present in the query definition) in the visible columns field
	    output.append("	var visibleColumns = document.getElementById('visColumns').value;\n");
	    output.append("	var visibleFields = visibleColumns.split(',');\n");
	    output.append("	if (visibleFields.length == 1 && trim(visibleFields[0]) == '') visibleFields.pop();\n");
	    output.append("	for (i = 0; i < visibleFields.length; i++) {\n");
	    output.append("		var visibleFieldFound = false;\n");
	    output.append(" 	var aVisibleField = trim(visibleFields[i]);\n");
	    output.append(" 	for (j = 0; j < fields.length; j++) {\n");
	    output.append(" 		var field = trim(fields[j]);\n");
	    output.append(" 		if (aVisibleField == field) {\n");
	    output.append(" 			visibleFieldFound = true;\n");
	    output.append(" 			break;\n");
	    output.append(" 		}\n");
	    output.append(" 	}\n");
	    output.append(" 	if (!visibleFieldFound) visibleFields.splice(i,1,'');\n");
	    output.append(" }\n");
	    output.append(" visibleFields = clean(visibleFields);\n");
	    output.append("	document.getElementById('visColumns').value = visibleFields.join(',');\n");
	    // looks if there are some old fields in invisible columns field
	    output.append("	var invisibleColumns = document.getElementById('invisColumns').value;\n");
	    output.append("	var	invisibleFields = invisibleColumns.split(',');\n");
	    output.append("	if (invisibleFields.length == 1 && trim(invisibleFields[0]) == '') invisibleFields.pop();\n");
	    output.append("	for (i = 0; i < invisibleFields.length; i++) {\n");
	    output.append("		var invisibleFieldFound = false;\n");
	    output.append(" 	var aInvisibleField = trim(invisibleFields[i]);\n");
	    output.append(" 	for (j = 0; j < fields.length; j++) {\n");
	    output.append(" 		var field = trim(fields[j]);\n");
	    output.append(" 		if (aInvisibleField == field) {\n");
	    output.append(" 			invisibleFieldFound = true;\n");
	    output.append(" 			break;\n");
	    output.append(" 		}\n");
	    output.append(" 	}\n");
	    output.append(" 	if (!invisibleFieldFound) invisibleFields.splice(i,1,'');\n");
	    output.append(" }\n");
	    output.append(" invisibleFields = clean(invisibleFields);\n");
	    // looks if there are new fields; in case of new field, it is inserted into the invisible columns field
	    output.append("	for (i = 0; i < fields.length; i++) {\n");
	    output.append("		var fieldFound = false;\n");
	    output.append(" 	var field = trim(fields[i]);\n");
	    output.append(" 	for (j = 0; j < invisibleFields.length; j++) {\n");
	    output.append(" 		var aInvisiblefield = trim(invisibleFields[j]);\n");
	    output.append(" 		if (aInvisibleField == field) {\n");
	    output.append(" 			fieldFound = true;\n");
	    output.append(" 			break;\n");
	    output.append(" 		}\n");
	    output.append(" 	}\n");
	    output.append(" 	if (!fieldFound) {\n");
	    output.append(" 		for (j = 0; j < visibleFields.length; j++) {\n");
	    output.append(" 			var aVisibleField = trim(visibleFields[j]);\n");
	    output.append(" 			if (aVisibleField == field) {\n");
	    output.append(" 				fieldFound = true;\n");
	    output.append(" 				break;\n");
	    output.append(" 			}\n");
	    output.append(" 		}\n");
	    output.append(" 	}\n");
	    output.append(" 	if (!fieldFound) invisibleFields.push(field);\n");
	    output.append(" }\n");
	    output.append(" invisibleFields = clean(invisibleFields);\n");
	    output.append("	document.getElementById('invisColumns').value = invisibleFields.join(',');\n");
	    
	    // looks if the value column field contains an old field (no more present in the query definition)
	    output.append("	var valueColumn = trim(document.getElementById('valueColumns').value);\n");
	    output.append("	var valueColumnFound = false;\n");
	    output.append(" for (i = 0; i < fields.length; i++) {\n");
	    output.append(" 	var field = trim(fields[i]);\n");
	    output.append(" 	if (valueColumn == field) {\n");
	    output.append(" 		valueColumnFound = true;\n");
	    output.append(" 		break;\n");
	    output.append(" 	}\n");
	    output.append(" }\n");
	    output.append(" if (!valueColumnFound) document.getElementById('valueColumns').value = '';\n");
	    // regenerate the HTML code
	    output.append("	var strHTML = generateHTML(fields, valueColumn, visibleFields);\n");
	    output.append("	document.getElementById('fieldsDiv').innerHTML = strHTML;\n");
	    output.append("}\n");
	    
	    output.append("function setConnName(index) {\n");
	    output.append("		document.getElementById('connName').selectedIndex = index;\n");
	    output.append("}\n");
	    output.append("function setValueColumn(valcol) {\n");
	    output.append("		document.getElementById('valueColumns').value = valcol;\n");
	    output.append("}\n");
	    output.append("function setQueryDef(queryDef) {\n");
	    output.append("		document.getElementById('queryDef').value = queryDef;\n");
	    output.append("		updateFields();\n");
	    output.append("}\n");
	    output.append("function setVisibleColumns(column,checked) {\n");
	    // if the column is checked, tries to insert it into visible columns and to delete from invisible columns
	    output.append("	var visibleColumns = document.getElementById('visColumns').value;\n");
	    output.append("	var visibleFields = visibleColumns.split(',');\n");
	    output.append("	var invisibleColumns = document.getElementById('invisColumns').value;\n");
	    output.append("	var invisibleFields = invisibleColumns.split(',');\n");
	    output.append("	if (visibleFields.length == 1 && trim(visibleFields[0]) == '') visibleFields.pop();\n");
	    output.append("	if (invisibleFields.length == 1 && trim(invisibleFields[0]) == '') invisibleFields.pop();\n");
	    output.append("	var visibleFieldFound = false;\n");
	    output.append("	for (i = 0; i < visibleFields.length; i++) {\n");
	    output.append(" 	var temp = visibleFields[i];\n");
	    output.append(" 	temp = trim(temp);\n");
	    output.append(" 	if (temp == column) {\n");
	    output.append(" 		visibleFieldFound = true;\n");
	    output.append(" 		if (!checked) {\n");
	    output.append(" 			visibleFields.splice(i,1);\n");
	    output.append(" 		}\n");
	    output.append(" 		break;\n");
	    output.append(" 	}\n");
	    output.append(" }\n");
	    output.append(" if (!visibleFieldFound && checked) {\n");
	    output.append(" 	visibleFields.push(column);\n");
	    output.append("	}\n");
	    output.append("	var invisibleFieldFound = false;\n");
	    output.append("	for (i = 0; i < invisibleFields.length; i++) {\n");
	    output.append(" 	var temp = invisibleFields[i];\n");
	    output.append(" 	temp = trim(temp);\n");
	    output.append(" 	if (temp == column) {\n");
	    output.append(" 		invisibleFieldFound = true;\n");
	    output.append(" 		if (checked) {\n");
	    output.append(" 			invisibleFields.splice(i,1);\n");
	    output.append(" 		}\n");
	    output.append(" 		break;\n");
	    output.append(" 	}\n");
	    output.append(" }\n");
	    output.append(" if (!invisibleFieldFound && !checked) {\n");
	    output.append(" 	invisibleFields.push(column);\n");
	    output.append("	}\n");
	    output.append("	document.getElementById('visColumns').value = visibleFields.join(',');\n");
	    output.append("	document.getElementById('invisColumns').value = invisibleFields.join(',');\n");
	    output.append("}\n");
	    
	    output.append("function isASelect(queryDef) {\n");
	    output.append("		queryDef = trim(queryDef);\n");
	    output.append("		var init = queryDef.substring(0,7);\n");
	    output.append("		if (init != 'SELECT ') return false;\n");
	    output.append("		if (queryDef.indexOf(' FROM ') == -1) return false;\n");
	    output.append("		return true;\n");
	    output.append("}\n");
	    
	    output.append("function isCorrect(field) {\n");
	    output.append("		if (field == '') return false;\n");
	    output.append("		var fieldUC = field.toUpperCase();\n");
	    output.append("		if (fieldUC == 'AS') return false;\n");
	    output.append("		if (fieldUC.match(' AS ') != null) return false;\n");
	    output.append("		if (fieldUC.length > 3 && fieldUC.substring(fieldUC.length - 3) == ' AS') return false;\n");
	    output.append("		if (fieldUC.length > 3 && fieldUC.substring(0, 3) == 'AS ') return false;\n");
	    output.append("		return true;\n");
	    output.append("}\n");
	    
	    output.append("function trim(str) {\n");
	    output.append("		while (str.charAt(0) == ' ') str = str.substring(1);\n");
	    output.append("		while (str.charAt(str.length-1) == ' ') str = str.substring(0, str.length-1);\n");
	    output.append("		return str;\n");
	    output.append("}\n");
	    
	    output.append("function resetFields() {\n");
	    output.append("		document.getElementById('visColumns').value = '';\n");
	    output.append("		document.getElementById('invisColumns').value = '';\n");
	    output.append("		document.getElementById('valueColumns').value = '';\n");
	    output.append("}\n");
	    
	    output.append("function clean(vect) {\n");
	    output.append("		var newVector = new Array();\n");
	    output.append("		var count = 0;\n");
	    output.append("		for (i = 0; i < vect.length; i++) {\n");
	    output.append("			var temp = trim(vect[i]);\n");
	    output.append("			if (temp != '') {\n");
	    output.append("				newVector[count] = temp;\n");
	    output.append("				count++;\n");
	    output.append("			}\n");
	    output.append("		}\n");
	    output.append("		return newVector;\n");
	    output.append("}\n");
	    
	    output.append("</script>\n");
	    
	    output.append("<script>\n");
	    output.append("document.getElementById('queryWizardWithJavascript').style.display='inline';\n");
	    output.append("document.getElementById('queryWizardWithoutJavascript').style.display='none';\n");
	    output.append("displayQueryFields();\n");
	    output.append("</script>\n");
	    
        try {
            pageContext.getOut().print(output.toString());
        }
        catch (Exception ex) {
            TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.CRITICAL, "QueryWizardTag::doStartTag::", ex);
            throw new JspException(ex.getMessage());
        }
	    
		return SKIP_BODY;
	}
	
    public int doEndTag() throws JspException {
        TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, "QueryWizardTag::doEndTag:: invocato");
        return super.doEndTag();
    }
	
	public String getConnectionName() {
		return connectionName;
	}
	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}
	public String getQueryDef() {
		return queryDef;
	}
	public void setQueryDef(String queryDef) {
		this.queryDef = queryDef;
	}
	public String getValueColumns() {
		return valueColumns;
	}
	public void setValueColumns(String valueColumns) {
		this.valueColumns = valueColumns;
	}
	public String getVisibleColumns() {
		return visibleColumns;
	}
	public void setVisibleColumns(String visibleColumns) {
		this.visibleColumns = visibleColumns;
	}
	public String getInvisibleColumns() {
		return invisibleColumns;
	}
	public void setInvisibleColumns(String invisibleColumns) {
		this.invisibleColumns = invisibleColumns;
	}
	
}
