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
package it.eng.spagobi.analiticalmodel.execution.presentation.tag;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.navigation.LightNavigationManager;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.Snapshot;
import it.eng.spagobi.analiticalmodel.document.service.ExecuteBIObjectModule;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.utilities.ChannelUtilities;
import it.eng.spagobi.commons.utilities.messages.IMessageBuilder;
import it.eng.spagobi.commons.utilities.messages.MessageBuilderFactory;
import it.eng.spagobi.commons.utilities.urls.IUrlBuilder;
import it.eng.spagobi.commons.utilities.urls.UrlBuilderFactory;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

/**
 * Defines a tag to create the list of Snapshots of the document that is 
 * specified by its id
 * 
 * @author Zerbetto (davide.zerbetto@eng.it)
 */
public class SnapshotsListTag extends TagSupport {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SnapshotsListTag.class);

	private List snapshotsList = null;
	
    protected SourceBean request = null;
    protected HttpServletRequest httpRequest = null;
    protected RequestContainer requestContainer = null;
    protected IUrlBuilder urlBuilder = null;
    protected IMessageBuilder msgBuilder = null;
	
    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
     */
    public int doStartTag() throws JspException {
    	logger.debug("IN");
    	httpRequest = (HttpServletRequest) pageContext.getRequest();
    	requestContainer = ChannelUtilities.getRequestContainer(httpRequest);
    	request = requestContainer.getServiceRequest();
    	urlBuilder = UrlBuilderFactory.getUrlBuilder(requestContainer.getChannelType());
    	msgBuilder = MessageBuilderFactory.getMessageBuilder();
    	String html = getHtmlForSnapshotsList();
    	try {
    	    pageContext.getOut().print(html);
    	} catch (IOException e) {
    	    logger.error("cannot start tag: IO exception occurred", e);
    	}
    	logger.debug("OUT");
    	return SKIP_BODY;
    }
    
    protected String getHtmlForSnapshotsList() {
    	logger.debug("IN");
    	String toReturn = null;
    	try {
    		IEngUserProfile profile = getCurrentUserProfile();
			if (snapshotsList == null || snapshotsList.size() == 0) {
				// the pageContext attribute is read by the presentation jsp to set the initial visibility of the box
				pageContext.setAttribute("snapshotsBoxOpen", "false");
				toReturn = "<div class='portlet-font'>" + msgBuilder.getMessage("SBIDev.docConf.snapshots.nosnapshots", httpRequest) + "</div>";
			} else {
				// the pageContext attribute is read by the presentation jsp to set the initial visibility of the box
				pageContext.setAttribute("snapshotsBoxOpen", "true");
				StringBuffer buffer = new StringBuffer();
				buffer.append("<table style='width:100%;' align='left'>\n");
				buffer.append("	<thead>\n");
				buffer.append("		<tr>\n");
				buffer.append("			<td style='vertical-align:middle;' align='left' class='portlet-section-header'>\n");
				buffer.append("				" + msgBuilder.getMessage("SBIDev.docConf.snapshots.name", httpRequest) + "\n");
				buffer.append("			</td>\n");
				buffer.append("			<td align='left' class='portlet-section-header'>&nbsp;</td>\n");
				buffer.append("			<td style='vertical-align:middle;' align='left' class='portlet-section-header'>\n");
				buffer.append("				" + msgBuilder.getMessage("SBIDev.docConf.snapshots.description", httpRequest) + "\n");
				buffer.append("			</td>\n");
				buffer.append("   		<td align='left' class='portlet-section-header'>&nbsp;</td>\n");
				buffer.append("   		<td style='vertical-align:middle;' align='left' class='portlet-section-header'>\n");
				buffer.append("				" + msgBuilder.getMessage("SBIDev.docConf.snapshots.dateCreation", httpRequest) + "\n");
				buffer.append("   		</td>\n");
				buffer.append("   		<td align='left' class='portlet-section-header'>&nbsp;</td>\n");
				buffer.append("   		<td align='left' class='portlet-section-header'>&nbsp;</td>\n");
				buffer.append("   		<td align='left' class='portlet-section-header'>&nbsp;</td>\n");
				buffer.append(" 	</tr>\n");
				buffer.append("	</thead>\n");
				
				Iterator iterSnap =  snapshotsList.iterator();
			    Snapshot snap = null;
			    String nameSnap = null;
			    String descrSnap = null;
			    Date creationDate = null;
			    String execSnapUrl = null;
			    String deleteSnapUrl = null;
				boolean alternate = false;
				String rowClass = null;
				   
	            while(iterSnap.hasNext()) {
	            	snap = (Snapshot)iterSnap.next();
					rowClass = (alternate) ? "portlet-section-alternate" : "portlet-section-body";
					alternate = !alternate;
					nameSnap = snap.getName();
					descrSnap = snap.getDescription();
					creationDate = snap.getDateCreation();
					
					Map execSnapUrlPars = new HashMap();
					execSnapUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
        		    execSnapUrlPars.put(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.EXEC_SNAPSHOT_MESSAGE);
        		    execSnapUrlPars.put(SpagoBIConstants.SNAPSHOT_ID, snap.getId());
        		    execSnapUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED,"true");
        		    execSnapUrl = urlBuilder.getUrl(httpRequest, execSnapUrlPars);
					
        		    Map deleteSnapUrlPars = new HashMap();
        		    deleteSnapUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
        		    deleteSnapUrlPars.put(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.ERASE_SNAPSHOT_MESSAGE);
        		    deleteSnapUrlPars.put(SpagoBIConstants.SNAPSHOT_ID, snap.getId());
        		    deleteSnapUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED,"true");
        		    deleteSnapUrl = urlBuilder.getUrl(httpRequest, deleteSnapUrlPars);
		                
        		    buffer.append("	<tr class='portlet-font'>\n");
        		    buffer.append("		<td style='vertical-align:middle;' class='" + rowClass + "'>" + nameSnap + "</td>\n");
        		    buffer.append("		<td class='" + rowClass + "' width='20px'>&nbsp;</td>\n");
        		    buffer.append("		<td style='vertical-align:middle;' class='" + rowClass + "' >" + descrSnap + "</td>\n");
        		    buffer.append("		<td class='" + rowClass + "' width='20px'>&nbsp;</td>\n");
        		    buffer.append("		<td style='vertical-align:middle;' class='" + rowClass + "' >" + creationDate.toString() + "</td>\n");
        		    buffer.append("		<td class='" + rowClass + "' width='20px'>&nbsp;</td>\n");
        		    buffer.append("		<td style='vertical-align:middle;' class='" + rowClass + "' width='40px'>\n");
                    if (!profile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_ADMIN)) {
                    	buffer.append("			&nbsp;\n");
                    } else {
                    	String eraseSnapMsg = msgBuilder.getMessage("ConfirmMessages.DeleteSnapshot", httpRequest);
                    	buffer.append("			<a href=\"javascript:var conf = confirm('" + eraseSnapMsg + "'); if (conf) {document.location='" + deleteSnapUrl.toString() + "';}\">\n");
                    	buffer.append("				<img \n");
                    	buffer.append("					src='" + urlBuilder.getResourceLink(httpRequest, "/img/erase.gif") + "' \n");
                    	buffer.append("					name='deleteSnapshot' alt='" + msgBuilder.getMessage("SBIDev.docConf.ListdocDetParam.deleteCaption", httpRequest) + "'\n"); 
                    	buffer.append("					title='" + msgBuilder.getMessage("SBIDev.docConf.ListdocDetParam.deleteCaption", httpRequest) + "' />\n");
                    	buffer.append("			</a>\n");
                    } 
                    buffer.append("		</td>\n");
		            buffer.append("		<td style='vertical-align:middle;' class='" + rowClass + "' width='40px'>\n");
		            buffer.append("			<a href='" + execSnapUrl + "'>\n");
		            buffer.append("				<img \n");
					buffer.append("					src='" + urlBuilder.getResourceLink(httpRequest, "/img/exec.gif") + "' \n");
					buffer.append("					name='execSnap' \n");
					buffer.append("					alt='" + msgBuilder.getMessage("SBIDev.docConf.execBIObjectParams.execButt", httpRequest) + "' \n");
					buffer.append("					title='" + msgBuilder.getMessage("SBIDev.docConf.execBIObjectParams.execButt", httpRequest) + "' />\n");
		            buffer.append("			</a>\n");
		           	buffer.append("		</td>\n");
		         	buffer.append("	</tr> \n");
		        }          
		        buffer.append("</table>\n");
				toReturn = buffer.toString();
			}
    	} catch (EMFInternalError e) {
    		logger.error(e);
    		toReturn = "<div class='portlet-msg-error'>" + msgBuilder.getMessage("101", httpRequest) + "</div>";
		} finally {
    		logger.debug("OUT");
    	}
    	return toReturn;
    }
    
	protected SessionContainer getSession() {
    	return requestContainer.getSessionContainer();
    }
    
	protected IEngUserProfile getCurrentUserProfile() {
		SessionContainer session = getSession();
		SessionContainer permSess = session.getPermanentContainer();
		IEngUserProfile profile = (IEngUserProfile) permSess.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
    	return profile;
    }
    
    /**
     * Gets the biobject id.
     * 
     * @return the biobject id
     */
    public List getSnapshotsList() {
    	return snapshotsList;
    }

	/**
	 * Sets the biobject id.
	 * 
	 * @param biobjectId the new biobject id
	 */
	public void setSnapshotsList(List snapshotsList) {
		this.snapshotsList = snapshotsList;
	}
}