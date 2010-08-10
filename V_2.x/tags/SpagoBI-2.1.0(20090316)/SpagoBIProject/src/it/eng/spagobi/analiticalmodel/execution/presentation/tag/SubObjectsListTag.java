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
import it.eng.spago.error.EMFUserError;
import it.eng.spago.navigation.LightNavigationManager;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.SubObject;
import it.eng.spagobi.analiticalmodel.document.service.ExecuteBIObjectModule;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.ChannelUtilities;
import it.eng.spagobi.commons.utilities.messages.IMessageBuilder;
import it.eng.spagobi.commons.utilities.messages.MessageBuilderFactory;
import it.eng.spagobi.commons.utilities.urls.IUrlBuilder;
import it.eng.spagobi.commons.utilities.urls.UrlBuilderFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

/**
 * Defines a tag to create the list of SubObjects of the document that is 
 * specified by its id
 * 
 * @author Zerbetto (davide.zerbetto@eng.it)
 */
public class SubObjectsListTag extends TagSupport {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SubObjectsListTag.class);

	private Integer biobjectId = null;
	
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
    	String html = getHtmlForSubObjectsList();
    	try {
    	    pageContext.getOut().print(html);
    	} catch (IOException e) {
    	    logger.error("cannot start tag: IO exception occurred", e);
    	}
    	logger.debug("OUT");
    	return SKIP_BODY;
    }
    
    protected String getHtmlForSubObjectsList() {
    	logger.debug("IN");
    	String toReturn = null;
    	try {
    		IEngUserProfile profile = getCurrentUserProfile();
    		List subObjectsList = DAOFactory.getSubObjectDAO().getAccessibleSubObjects(biobjectId, profile);
			if (subObjectsList == null || subObjectsList.size() == 0) {
				// the pageContext attribute is read by the presentation jsp to set the initial visibility of the box
				pageContext.setAttribute("subobjectsBoxOpen", "false");
				toReturn = "<div class='portlet-font'>" + msgBuilder.getMessage("SBIDev.docConf.subBIObject.nosubobjects", httpRequest) + "</div>";
			} else {
				// the pageContext attribute is read by the presentation jsp to set the initial visibility of the box
				pageContext.setAttribute("subobjectsBoxOpen", "true");
				StringBuffer buffer = new StringBuffer();
				buffer.append("<table style='width:100%;' align='left'>\n");
				buffer.append("<tr>\n");
				buffer.append("	  <td style='vertical-align:middle;' align='left' class='portlet-section-header'>\n");
				buffer.append("       " + msgBuilder.getMessage("SBIDev.docConf.subBIObject.name", httpRequest) + " \n");
				buffer.append("   </td>\n");
				buffer.append("   <td align='left' class='portlet-section-header'>&nbsp;</td>\n");
				buffer.append("   <td style='vertical-align:middle;' align='left' class='portlet-section-header'>\n");
				buffer.append("       " + msgBuilder.getMessage("SBIDev.docConf.subBIObject.owner", httpRequest) + "\n");
				buffer.append("   </td>\n");
				buffer.append("   <td align='left' class='portlet-section-header'>&nbsp;</td>\n");
				buffer.append("   <td style='vertical-align:middle;' align='left' class='portlet-section-header'>\n");
				buffer.append("       " + msgBuilder.getMessage("SBIDev.docConf.subBIObject.description", httpRequest) + "\n");
				buffer.append("   </td>\n");
				buffer.append("   <td align='left' class='portlet-section-header'>&nbsp;</td>\n");
				buffer.append("   <td style='vertical-align:middle;' align='left' class='portlet-section-header'>\n");
				buffer.append("       " + msgBuilder.getMessage("SBIDev.docConf.subBIObject.creationDate", httpRequest) + "\n");
				buffer.append("   </td>\n");
				buffer.append("   <td align='left' class='portlet-section-header'>&nbsp;</td>\n");
				buffer.append("   <td style='vertical-align:middle;' align='left' class='portlet-section-header'>\n");
				buffer.append("       " + msgBuilder.getMessage("SBIDev.docConf.subBIObject.lastModificationDate", httpRequest) + "\n");
				buffer.append("   </td>\n");
				buffer.append("   <td align='left' class='portlet-section-header'>&nbsp;</td>\n");
				buffer.append("   <td style='vertical-align:middle;' align='left' class='portlet-section-header'>\n");
				buffer.append("       " + msgBuilder.getMessage("SBIDev.docConf.subBIObject.visibility", httpRequest) + "\n");
				buffer.append("   </td>\n");
				buffer.append("   <td align='left' class='portlet-section-header' colspan='3' >&nbsp;</td>\n");
				buffer.append(" </tr> \n");
				
				SubObject subObj = null;
				Integer idSub = null;
				String nameSub = null;
				String descr = null;
				String visib = null;
				String delete = null;
				String owner = null;
				String creationDate = null;
				String lastModificationDate = null;
				String execSubObjUrl = null;
				String deleteSubObjUrl = null;
				boolean alternate = false;
				String rowClass = null;
				
				Iterator iterSubs =  subObjectsList.iterator();
				while(iterSubs.hasNext()) {
					subObj = (SubObject)iterSubs.next();
					rowClass = (alternate) ? "portlet-section-alternate" : "portlet-section-body";
					alternate = !alternate;
					idSub = subObj.getId();
					nameSub = subObj.getName();
                    descr = subObj.getDescription();
                    owner = subObj.getOwner();
                    creationDate = subObj.getCreationDate().toString();
                    lastModificationDate = subObj.getLastChangeDate().toString();
		                        
                    visib = "Private";
                    if (subObj.getIsPublic().booleanValue()) {
                    	visib = "Public";
                    } 
                    if (owner.equals(((UserProfile)profile).getUserId().toString())) {
                    	delete = "delete";
                    }
                    Map execSubObjUrlPars = new HashMap();
                    execSubObjUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE );
                    execSubObjUrlPars.put(SpagoBIConstants.MESSAGEDET, "EXEC_SUBOBJECT");
                    execSubObjUrlPars.put(SpagoBIConstants.SUBOBJECT_ID, idSub);
                    execSubObjUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED,"true");
        		    execSubObjUrl = urlBuilder.getUrl(httpRequest, execSubObjUrlPars);
                    
        		    Map deleteSubObjUrlPars = new HashMap();
        		    deleteSubObjUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
        		    deleteSubObjUrlPars.put(SpagoBIConstants.MESSAGEDET, "DELETE_SUBOBJECT");
        		    deleteSubObjUrlPars.put(SpagoBIConstants.SUBOBJECT_ID, idSub);
        		    deleteSubObjUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED,"true");
        		    deleteSubObjUrl = urlBuilder.getUrl(httpRequest, deleteSubObjUrlPars);
					
        		    buffer.append("	<tr class='portlet-font'>\n");
        		    buffer.append("		<td style='vertical-align:middle;' class='" + rowClass + "'>\n");
        		    buffer.append("			" + nameSub + "\n");
            		buffer.append("		</td>\n");
            		buffer.append("		<td class='" + rowClass + "' width='20px'>&nbsp;</td> \n");
            		buffer.append("		<td style='vertical-align:middle;' class='" + rowClass + "' >" + owner + "</td>\n");
            		buffer.append("		<td class='" + rowClass + "' width='20px'>&nbsp;</td> \n");
            		buffer.append("		<td style='vertical-align:middle;' class='" + rowClass + "' >" + descr + "</td>\n");
            		buffer.append("		<td class='" + rowClass + "' width='20px'>&nbsp;</td> \n");
            		buffer.append("		<td style='vertical-align:middle;' class='" + rowClass + "' >" + creationDate + "</td>\n");
            		buffer.append("		<td class='" + rowClass + "' width='20px'>&nbsp;</td> \n");
            		buffer.append("		<td style='vertical-align:middle;' class='" + rowClass + "' >" + lastModificationDate + "</td>\n");
            		buffer.append("		<td class='" + rowClass + "' width='20px'>&nbsp;</td> \n");
            		buffer.append("		<td style='vertical-align:middle;' class='" + rowClass + "' >" + visib + "</td>\n");
            		buffer.append("		<td class='" + rowClass + "' width='20px'>&nbsp;</td>\n");
            		if (owner.equals(((UserProfile)profile).getUserId().toString())) {
                    	buffer.append("		<td style='vertical-align:middle;' class='" + rowClass + "' width='40px'>\n");
                		String eraseMsg = msgBuilder.getMessage("ConfirmMessages.DeleteSubObject", "messages", httpRequest);
                		buffer.append("			<a href=\"javascript:var conf = confirm('" + eraseMsg + "'); if (conf) {document.location='" + deleteSubObjUrl.toString() + "';}\">\n");
                		buffer.append("				<img \n");
		  	   			buffer.append("					src='" + urlBuilder.getResourceLink(httpRequest, "/img/erase.gif") + "' \n");
		  	   			buffer.append("					name='deleteSub' \n");
		  	            buffer.append("					alt='" + msgBuilder.getMessage("SBIDev.docConf.ListdocDetParam.deleteCaption", httpRequest) + "' \n");
		                buffer.append("					title='" + msgBuilder.getMessage("SBIDev.docConf.ListdocDetParam.deleteCaption", httpRequest) + "' />\n");
                		buffer.append("			</a>\n");
                		buffer.append("		</td>\n");
            		} else {
            			buffer.append("		<td style='vertical-align:middle;' class='" + rowClass + "' width='40px'>\n");
            			buffer.append("			&nbsp;\n");
            			buffer.append("		</td>\n");
            		}
                	buffer.append("		<td style='vertical-align:middle;' class='" + rowClass + "' width='40px'>\n");
            		buffer.append("			<a href='" + execSubObjUrl + "'>\n");
            		buffer.append("				<img \n");
	  	   			buffer.append("					src='" + urlBuilder.getResourceLink(httpRequest, "/img/exec.gif") + "' \n");
	  	   			buffer.append("					name='execSub' \n");
	  	            buffer.append("					alt='" + msgBuilder.getMessage("SBIDev.docConf.execBIObjectParams.execButt", httpRequest) + "' \n");
	                buffer.append("					title='" + msgBuilder.getMessage("SBIDev.docConf.execBIObjectParams.execButt", httpRequest) + "' />\n");
            		buffer.append("			</a>\n");
            		buffer.append("		</td>\n");
            		buffer.append("	</tr>\n");
				}
				buffer.append("</table>\n");
				toReturn = buffer.toString();
			}
		} catch (EMFUserError e) {
			logger.error("Error while loading subobjects list", e);
			toReturn = "<div class='portlet-msg-error'>" + msgBuilder.getMessage("7002", httpRequest) + "</div>";
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
    public Integer getBiobjectId() {
    	return biobjectId;
    }

	/**
	 * Sets the biobject id.
	 * 
	 * @param biobjectId the new biobject id
	 */
	public void setBiobjectId(Integer biobjectId) {
		this.biobjectId = biobjectId;
	}
}
