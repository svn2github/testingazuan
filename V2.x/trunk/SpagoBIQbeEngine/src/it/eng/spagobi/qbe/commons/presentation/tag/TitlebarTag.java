/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.spagobi.qbe.commons.presentation.tag;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

// TODO: Auto-generated Javadoc
/**
 * The Class TitlebarTag.
 */
public class TitlebarTag extends QbeTagSupport {
	
	/** The datamart name. */
	String datamartName;
	
	/** The datamart description. */
	String datamartDescription;
	
	/** The page name. */
	String pageName;
	
	
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
	 */
	public int doStartTag() throws JspException {
		
		StringBuffer buffer = new StringBuffer();
		try {			
			Map paramsBack = new java.util.HashMap();
            paramsBack.put("ACTION_NAME", "RECOVER_CL_ACTION");            
            String url = getActionUrl(paramsBack);
            String startModifyTimeStamp =(String)getSessionContainer().getAttribute("QBE_START_MODIFY_QUERY_TIMESTAMP"); 
            String lastUpdTimeStamp =(String)getSessionContainer().getAttribute("QBE_LAST_UPDATE_TIMESTAMP");
			
			
			buffer.append("<table class='header-table-portlet-section'>\n");
			buffer.append("	<tr class='header-row-portlet-section'>\n");
			buffer.append("		<td class='header-title-column-portlet-section'\n");
			buffer.append("		    style='vertical-align:middle;padding-left:5px;'>\n");
			buffer.append("			" + datamartName + " : " + datamartDescription + " - " + pageName + "\n");
			buffer.append("		</td>\n");
			buffer.append("		<td class='header-empty-column-portlet-section'>&nbsp;</td>\n");
			
			//buffer.append("		<%@include file='/jsp/qbe_headers.jsp'%>\n");
			if (getQuery().isSubqueryModeActive()){
				String queryId = getQuery().getQueryId() != null ? getQuery().getQueryId() : " ";
				startModifyTimeStamp = (startModifyTimeStamp != null ? startModifyTimeStamp : " ");
				lastUpdTimeStamp = (lastUpdTimeStamp != null ? lastUpdTimeStamp : " ");
				String imgSrc = getResourceUrl("../img/back.gif");
				
				buffer.append("<td class='header-button-column-portlet-section'>\n");
				buffer.append("		<a href=\"javascript:checkSavingBeforeBack('" + url +"', '" + queryId +"','" + startModifyTimeStamp +"','" + lastUpdTimeStamp + "')\">\n"); 
				buffer.append("			<img class='header-button-image-portlet-section' title='Back'\n");
				buffer.append("				 src='" + imgSrc +"' alt='Back' />\n");
				buffer.append("		</a>\n");
				buffer.append("</td>\n");	
				
			}
			
			buffer.append("	</tr>\n");
			buffer.append("</table>\n");			
			
	    	  
	    	pageContext.getOut().print( buffer.toString() );
	      } catch (Exception ex) {
	         throw new JspTagException( getClass().getName() + ": " + ex.getMessage() );
	      }
	      return SKIP_BODY;
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	public int doEndTag() {
		return EVAL_PAGE;
	}
	
	
	

	/**
	 * Gets the datamart name.
	 * 
	 * @return the datamart name
	 */
	public String getDatamartName() {
		return datamartName;
	}

	/**
	 * Sets the datamart name.
	 * 
	 * @param datamartName the new datamart name
	 */
	public void setDatamartName(String datamartName) {
		this.datamartName = datamartName;
	}

	/**
	 * Gets the datamart description.
	 * 
	 * @return the datamart description
	 */
	public String getDatamartDescription() {
		return datamartDescription;
	}

	/**
	 * Sets the datamart description.
	 * 
	 * @param datamartDescription the new datamart description
	 */
	public void setDatamartDescription(String datamartDescription) {
		this.datamartDescription = datamartDescription;
	}

	/**
	 * Gets the page name.
	 * 
	 * @return the page name
	 */
	public String getPageName() {
		return pageName;
	}

	/**
	 * Sets the page name.
	 * 
	 * @param pageName the new page name
	 */
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
}

