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
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.navigation.LightNavigationManager;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spago.util.StringUtils;
import it.eng.spagobi.analiticalmodel.document.bo.Viewpoint;
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
 * Defines a tag to create the list of View Points of the document that is 
 * specified by its id
 * 
 * @author Zerbetto (davide.zerbetto@eng.it)
 */
public class ViewPointsListTag extends TagSupport {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ViewPointsListTag.class);

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
    	String html = getHtmlForViewPointsList();
    	try {
    	    pageContext.getOut().print(html);
    	} catch (IOException e) {
    	    logger.error("cannot start tag: IO exception occurred", e);
    	}
    	logger.debug("OUT");
    	return SKIP_BODY;
    }
    
    protected String getHtmlForViewPointsList() {
    	logger.debug("IN");
    	String toReturn = null;
    	try {
    		IEngUserProfile profile = getCurrentUserProfile();
    		List viewPointsList = null;
			viewPointsList = DAOFactory.getViewpointDAO().loadAccessibleViewpointsByObjId(biobjectId, profile);
			if (viewPointsList == null || viewPointsList.size() == 0) {
				// the pageContext attribute is read by the presentation jsp to set the initial visibility of the box
				pageContext.setAttribute("viewpointsBoxOpen", "false");
				toReturn = "<div class='portlet-font'>" + msgBuilder.getMessage("SBIDev.docConf.viewPoint.noViewPoints", httpRequest) + "</div>";
			} else {
				// the pageContext attribute is read by the presentation jsp to set the initial visibility of the box
				pageContext.setAttribute("viewpointsBoxOpen", "true");
				StringBuffer buffer = new StringBuffer();
				buffer.append("<table style='width:100%;' align='left'>\n");
				buffer.append(" <tr>\n");
				buffer.append("   <td style='vertical-align:middle;' align='left' class='portlet-section-header'>\n");
				buffer.append("       " + msgBuilder.getMessage("SBIDev.docConf.viewPoint.name", httpRequest) + "\n");
				buffer.append("   </td>\n");
				buffer.append("   <td align='left' class='portlet-section-header'>&nbsp;</td>\n");
				buffer.append("   <td style='vertical-align:middle;' align='left' class='portlet-section-header'>\n");
				buffer.append("       " + msgBuilder.getMessage("SBIDev.docConf.viewPoint.owner", httpRequest) + "\n");
				buffer.append("   </td>\n");
				buffer.append("   <td align='left' class='portlet-section-header'>&nbsp;</td>\n");
				buffer.append("   <td style='vertical-align:middle;' align='left' class='portlet-section-header'>\n");
				buffer.append("       " + msgBuilder.getMessage("SBIDev.docConf.viewPoint.description", httpRequest) + "\n");
				buffer.append("   </td>\n");
				buffer.append("   <td align='left' class='portlet-section-header'>&nbsp;</td>\n");
				buffer.append("   <td style='vertical-align:middle;' align='left' class='portlet-section-header'>\n");
				buffer.append("       " + msgBuilder.getMessage("SBIDev.docConf.viewPoint.scope", httpRequest) + "\n");
				buffer.append("   </td>\n");
				buffer.append("   <td align='left' class='portlet-section-header'>&nbsp;</td>\n");
				buffer.append("   <td style='vertical-align:middle;' align='left' class='portlet-section-header'>\n");
				buffer.append("       " + msgBuilder.getMessage("SBIDev.docConf.viewPoint.dateCreation", httpRequest) + "\n");
				buffer.append("   </td>\n");
				buffer.append("   <td align='left' class='portlet-section-header'>&nbsp;</td>\n");
				buffer.append("   <td align='left' class='portlet-section-header'>&nbsp;</td>\n");
				buffer.append("   <td align='left' class='portlet-section-header'>&nbsp;</td>\n");
				buffer.append(" </tr> \n");
				              
				Iterator iterVP =  viewPointsList.iterator();
			    Viewpoint vp = null;
			    String ownerVP = null;				    
			    String nameVP = null;
			    String descrVP = null;
			    String scopeVP = null;
			    Date creationDateVP = null;
			    String execVPUrl = null;
			    String deleteVPUrl = null;
			    String viewVPUrl = null;				    				   
				boolean alternate = false;
				String rowClass = null;
					   
	            while (iterVP.hasNext()) {
	            	vp = (Viewpoint)iterVP.next();
					rowClass = (alternate) ? "portlet-section-alternate" : "portlet-section-body";
					alternate = !alternate;
					nameVP = vp.getVpName();
					ownerVP = vp.getVpOwner();						
					descrVP = vp.getVpDesc();
					scopeVP = vp.getVpScope();
					creationDateVP = vp.getVpCreationDate();
					
					Map execVPUrlPars = new HashMap();
					execVPUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
					execVPUrlPars.put(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.VIEWPOINT_EXEC);	
					execVPUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED,"true");						
					execVPUrlPars.put("content", vp.getVpValueParams());
					execVPUrlPars.put("vpId",vp.getVpId());
					execVPUrl = urlBuilder.getUrl(httpRequest, execVPUrlPars);
						
	       		    Map deleteVPUrlPars = new HashMap();
	       		    deleteVPUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
	       		    deleteVPUrlPars.put(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.VIEWPOINT_ERASE);
	       		    deleteVPUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED,"true");
	       		    deleteVPUrlPars.put("vpId",vp.getVpId());
	       		    deleteVPUrl = urlBuilder.getUrl(httpRequest, deleteVPUrlPars);
	
	       		    Map viewVPUrlPars = new HashMap();
	       		    viewVPUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
	       		    viewVPUrlPars.put(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.VIEWPOINT_VIEW);
	       		    viewVPUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED,"true");
	       		    viewVPUrlPars.put("vpId",vp.getVpId());
	       		    viewVPUrl = urlBuilder.getUrl(httpRequest, viewVPUrlPars);
	
	       	        ConfigSingleton conf = ConfigSingleton.getInstance();
	       		    SourceBean formatSB = (SourceBean) conf.getAttribute("DATA-ACCESS.DATE-FORMAT");
	       		    String format = (String) formatSB.getAttribute("format");
	       		    format = format.replaceAll("D", "d");
	       		    format = format.replaceAll("m", "M");
	       		    format = format.replaceAll("Y", "y");
	       		    String date = StringUtils.dateToString(creationDateVP, format);
	            
	       		    buffer.append("	<tr class='portlet-font'>\n");
	       		    buffer.append("		<td style='vertical-align:middle;' class='" + rowClass + "'>" +  nameVP + "</td>\n");
	       		    buffer.append("		<td class='" + rowClass + "' width='20px'>&nbsp;</td>\n");
	       		    buffer.append("		<td style='vertical-align:middle;' class='" + rowClass + "'>" +  ownerVP + "</td>\n");
	       		    buffer.append("		<td class='" + rowClass + "' width='20px'>&nbsp;</td>\n");
	       		    buffer.append("		<td style='vertical-align:middle;' class='" + rowClass + "' >" + descrVP + "</td>\n");
		       		buffer.append("		<td class='" + rowClass + "' width='20px'>&nbsp;</td>\n");
		       		buffer.append("		<td style='vertical-align:middle;' class='" + rowClass + "' >" + scopeVP + "</td>\n");
		       		buffer.append("		<td class='" + rowClass + "' width='20px'>&nbsp;</td>\n");
		       		buffer.append("		<td style='vertical-align:middle;' class='" + rowClass + "' >" + date + "</td>\n");
		       		buffer.append("		<td style='vertical-align:middle;' class='" + rowClass + "' width='40px'>\n");
		       		buffer.append("     	<a href=\"javascript:document.location='" + viewVPUrl.toString() + "';\">\n");
		       		buffer.append("     		<img \n");
		       		buffer.append("     			src='" + urlBuilder.getResourceLink(httpRequest, "/img/notes.jpg") + "' \n");
		       		buffer.append("        			name='getViewpoint' alt='" + msgBuilder.getMessage("SBIDev.docConf.viewPoint.viewButt", httpRequest) + "'\n"); 
		       		buffer.append("         		title='" + msgBuilder.getMessage("SBIDev.docConf.viewPoint.viewButt", httpRequest) + "'\n");
		       		buffer.append("         	/>\n");
		       		buffer.append("     	</a>\n");
	                buffer.append("     </td>\n");	            
	                buffer.append("    <td style='vertical-align:middle;' class='" + rowClass + "' width='40px'>\n");
	                IEngUserProfile userProfile = getCurrentUserProfile();
	                if (ownerVP.equals(((UserProfile)userProfile).getUserId().toString())) {
	                 	String eraseVPMsg = msgBuilder.getMessage("ConfirmMessages.DeleteViewpoint", httpRequest);
	                 	buffer.append("		<a href=\"javascript:var conf = confirm('" + eraseVPMsg + "'); if (conf) {document.location='" + deleteVPUrl.toString() + "';}\">\n");
	                 	buffer.append("			<img \n");
	                 	buffer.append("				src='" + urlBuilder.getResourceLink(httpRequest, "/img/erase.gif") + "' \n");
	                 	buffer.append("				name='deleteViewpoint' alt='" + msgBuilder.getMessage("SBIDev.docConf.ListdocDetParam.deleteCaption", httpRequest) + "'\n"); 
	                 	buffer.append(" 			title='" + msgBuilder.getMessage("SBIDev.docConf.ListdocDetParam.deleteCaption", httpRequest) + "' \n");
	                 	buffer.append("  		/>\n");
	                 	buffer.append("		</a>\n");
	                }
	                buffer.append("		<td style='vertical-align:middle;' class='" + rowClass + "' width='40px'>\n");
	                buffer.append("			<a href='" + execVPUrl + "'>\n");
	                buffer.append("      		 <img \n");
	                buffer.append("  	   			src='" + urlBuilder.getResourceLink(httpRequest, "/img/exec.gif") + "'\n");
	                buffer.append("  	       		name='execSnap'\n");
	                buffer.append("  	        	alt='" + msgBuilder.getMessage("SBIDev.docConf.execBIObjectParams.execButt", httpRequest) + "'\n");
	                buffer.append("            		title='" + msgBuilder.getMessage("SBIDev.docConf.execBIObjectParams.execButt", httpRequest) + "'\n");
	                buffer.append("       		/>\n");
	                buffer.append("			</a>\n");
	                buffer.append("		</td>\n");
	                buffer.append("	</tr>\n");
	            }
	            buffer.append("</table>\n");
	            toReturn = buffer.toString();
			}
		} catch (EMFUserError e) {
			logger.error("Error while loading view points list", e);
			toReturn = "<div class='portlet-msg-error'>" + msgBuilder.getMessage("6003", httpRequest) + "</div>";
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
