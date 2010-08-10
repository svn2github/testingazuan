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

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.ChannelUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;
import it.eng.spagobi.utilities.messages.IMessageBuilder;
import it.eng.spagobi.utilities.messages.MessageBuilderFactory;
import it.eng.spagobi.utilities.urls.IUrlBuilder;
import it.eng.spagobi.utilities.urls.UrlBuilderFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;



public class LovColumnsSelectorTag extends TagSupport {
	
	private String moduleName = null;
	private String visibleColumns = null;
	private String valueColumn = null;
	private String descriptionColumn = null;
	private String invisibleColumns = null;
	
	private RequestContainer requestContainer = null;
	private SourceBean serviceRequest = null;
	private ResponseContainer responseContainer = null;
	private SourceBean serviceResponse = null;
	private EMFErrorHandler errorHandler = null;
	private StringBuffer htmlStream = null;
    private HttpServletRequest httpRequest = null;
    private SourceBean moduleResponse = null;
	protected IUrlBuilder urlBuilder = null;
    protected IMessageBuilder msgBuilder = null;
    
	String visColumnsField = "";
	String invisColumnsField = "";
	String valueColumnsField = "";
	String descriptionColumnsField = "";		
	String columnsField = "";
    
    
	public int doStartTag() throws JspException {
		SpagoBITracer.info(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
				           "doStartTag", " method invoked");
		httpRequest = (HttpServletRequest) pageContext.getRequest();
		requestContainer = ChannelUtilities.getRequestContainer(httpRequest);
		responseContainer = ChannelUtilities.getResponseContainer(httpRequest);
		urlBuilder = UrlBuilderFactory.getUrlBuilder();
		msgBuilder = MessageBuilderFactory.getMessageBuilder();
		
		visColumnsField = msgBuilder.getMessage(requestContainer, "SBIDev.queryWiz.visColumnsField", "messages");
		invisColumnsField = msgBuilder.getMessage(requestContainer, "SBIDev.queryWiz.invisColumnsField", "messages");
		valueColumnsField = msgBuilder.getMessage(requestContainer, "SBIDev.queryWiz.valueColumnsField", "messages");
		descriptionColumnsField = msgBuilder.getMessage(requestContainer, "SBIDev.queryWiz.descriptionColumnsField", "messages");		
		columnsField = msgBuilder.getMessage(requestContainer, "SBIDev.queryWiz.columnsField", "messages");
		
		serviceRequest = requestContainer.getServiceRequest();
		serviceResponse = responseContainer.getServiceResponse();
		errorHandler = responseContainer.getErrorHandler();
		ConfigSingleton configure = ConfigSingleton.getInstance();
		if(moduleName != null) {
			SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					            "doStartTag", "Module Name: " + moduleName);
			moduleResponse = (SourceBean)serviceResponse.getAttribute(moduleName);
		} else {
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
					               "doStartTag", "Module name not specified");
			throw new JspException("Module name not specified !");
		}
		if (moduleResponse == null) {
			SpagoBITracer.warning(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
					              "doStartTag", "Module response null");
			return SKIP_BODY;
		} 
		htmlStream = new StringBuffer();
		makeTable();
		try {
			pageContext.getOut().print(htmlStream);
		} 
		catch (Exception ex) {
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					               "doStartTag", "Cannot to send the stream", ex);
			throw new JspException("Cannot to send the stream");
		} 
		return SKIP_BODY;
	} 

	
	
	protected void makeTable() throws JspException {
		// get the column names from the first row of the list
		// TODO check if all the rows have the same columns
		List columnNames = new ArrayList();
		SourceBean rowsSB = (SourceBean)moduleResponse.getAttribute("PAGED_LIST.ROWS");
		List rows = null;
		if(rowsSB!=null) {
			rows = rowsSB.getAttributeAsList("ROW");
			// take the first row 
			if(rows.size()!=0) {
				SourceBean row = (SourceBean)rows.get(0);
				List attributes = row.getContainedAttributes();
				Iterator iterAttr = attributes.iterator();
				while(iterAttr.hasNext()) {
					SourceBeanAttribute attrsba = (SourceBeanAttribute)iterAttr.next();
					columnNames.add(attrsba.getKey());
				}
			}
		}
		// create the columns table selector
		htmlStream.append("<table class=\"object-details-table\" style=\"width:100%;\">\n");
		htmlStream.append("	<tr >\n");
		htmlStream.append("		<td style=\"background:rgb(254,232,186);\" class=\"portlet-section-header\">" + columnsField + "</td>\n");
		htmlStream.append("	    <td class=\"portlet-section-header\" style=\"background:rgb(254,232,186);text-align:center;width:120px;\">" + valueColumnsField + "</td>\n");
		htmlStream.append("	    <td class=\"portlet-section-header\" style=\"background:rgb(254,232,186);text-align:center;width:150px;\">" + descriptionColumnsField + "</td>\n");
		htmlStream.append("	    <td class=\"portlet-section-header\" style=\"background:rgb(254,232,186);text-align:center;width:150px;\">" + visColumnsField + "<td>\n");
		htmlStream.append("	</tr>\n");
		
		String[] visColArr = visibleColumns.split(",");
		List visColList = Arrays.asList(visColArr);
		Iterator iterCoNames = columnNames.iterator();
		while(iterCoNames.hasNext()) {
			String colName = (String)iterCoNames.next();
			String checked = " ";
			String selectedValue = " ";
			String selectedDescr = " ";
			if(colName.equals(valueColumn)) {
				selectedValue = " checked ";
			}
			if(colName.equals(descriptionColumn)) {
				selectedDescr = " checked ";
			}
			if(visColList.contains(colName)) {
				checked = " checked ";
			}
			
			
			htmlStream.append("	<tr>\n");
			htmlStream.append("	<td style=\"background:rgb(251,247,227);\" class=\"portlet-section-body\">"+colName+"\n");
			htmlStream.append("		<INPUT type='hidden' value='"+colName+"' name='column' />\n");
			htmlStream.append("	</td>\n");
			htmlStream.append("	<td align=\"center\" style=\"background:rgb(251,247,227); align=\"center\" class=\"portlet-section-body\">\n");
			htmlStream.append("		<INPUT "+selectedValue+" type='radio' value='"+colName+"' name='valueColumn' />\n");
			htmlStream.append("	</td>\n");
			htmlStream.append("	<td align=\"center\" style=\"background:rgb(251,247,227); align=\"center\" class=\"portlet-section-body\">\n");
			htmlStream.append("		<INPUT "+selectedDescr+" type='radio' value='"+colName+"' name='descriptionColumn' />\n");
			htmlStream.append("	</td>\n");
			htmlStream.append("	<td align=\"center\" style=\"background:rgb(251,247,227); align=\"center\" class=\"portlet-section-body\">\n");
			htmlStream.append("		<INPUT "+checked+" type='checkbox' value='"+colName+"' name='visibleColumn' />\n");
			htmlStream.append("	</td>\n");
			htmlStream.append("	</tr>\n");
		}
		htmlStream.append("<table>\n");
	}



	public String getDescriptionColumn() {
		return descriptionColumn;
	}



	public void setDescriptionColumn(String descriptionColumn) {
		this.descriptionColumn = descriptionColumn;
	}



	public String getInvisibleColumns() {
		return invisibleColumns;
	}



	public void setInvisibleColumns(String invisibleColumns) {
		this.invisibleColumns = invisibleColumns;
	}



	public String getModuleName() {
		return moduleName;
	}



	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}



	public String getValueColumn() {
		return valueColumn;
	}



	public void setValueColumn(String valueColumn) {
		this.valueColumn = valueColumn;
	}



	public String getVisibleColumns() {
		return visibleColumns;
	}



	public void setVisibleColumns(String visibleColumns) {
		this.visibleColumns = visibleColumns;
	}


}
	
	
	
	

