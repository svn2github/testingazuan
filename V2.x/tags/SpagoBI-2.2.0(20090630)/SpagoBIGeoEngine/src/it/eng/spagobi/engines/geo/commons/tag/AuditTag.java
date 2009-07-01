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
package it.eng.spagobi.engines.geo.commons.tag;

import it.eng.spagobi.utilities.callbacks.audit.AuditAccessUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

// TODO: Auto-generated Javadoc
/**
 * The Class AuditTag.
 * 
 * @author Andrea Gioia
 */
public class AuditTag extends GeoTagSupport {
	
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
	 */
	public int doStartTag() throws JspException {
		
		try {
			/*
			String auditId = (String)getSessionContainer().getAttribute("SPAGOBI_AUDIT_ID");
			AuditAccessUtils auditAccessUtils = 
				(AuditAccessUtils) getSessionContainer().getAttribute("SPAGOBI_AUDIT_UTILS");
			if (auditId != null) {
				if (auditAccessUtils != null) {
								
					auditAccessUtils.updateAudit(auditId, 
							null, 
							new Long(System.currentTimeMillis()), 
							"EXECUTION_PERFORMED", 
							null, 
							null);
					
				}
					
				getSessionContainer().delAttribute("SPAGOBI_AUDIT_ID");
			}
			*/
	    } catch (Exception ex) {
	         throw new JspTagException(getClass().getName() + ": " + ex.getMessage());
	    }
	    return SKIP_BODY;
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	public int doEndTag() {
		return EVAL_PAGE;
	}

}

