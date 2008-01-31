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
package it.eng.spagobi.qbe.commons.presentation.tag;

import it.eng.qbe.conf.QbeEngineConf;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * @author Andrea Gioia
 *
 */
public class PageTag extends QbeTagSupport {
	
	/*
	 * Put all the relevant objects in the page context in order 
	 * to make them usable into EL scripts
	 */
	private void initPageContext() {
		getRequest();
		getRequestContainer();
		getResponseContainer();
		getSessionContainer();
		getLocale();
		getDatamartModel();
		getDatamartWizard();
		getQuery();
		isStandaloneModality();
		isWebModality();
	}
	
	public int doStartTag() throws JspException {		
		StringBuffer buffer = new StringBuffer();
		try {
			initPageContext();
			if ( QbeEngineConf.getInstance().isWebModalityActive() ){
				buffer.append("<html>\n");
			}
	    	pageContext.getOut().print( buffer.toString() );
	      } catch (Exception ex) {
	         throw new JspTagException(getClass().getName() + ": " + ex.getMessage());
	      }
	      return EVAL_BODY_INCLUDE;
	}
	
	public int doEndTag() throws JspException {
		StringBuffer buffer = new StringBuffer();
		try {
			if ( QbeEngineConf.getInstance().isWebModalityActive() ){
				buffer.append("</html>\n");
			}
	    	pageContext.getOut().print( buffer.toString() );
	      } catch (Exception ex) {
	         throw new JspTagException(getClass().getName() + ": " + ex.getMessage());
	      }
		return EVAL_PAGE;
	}

}

