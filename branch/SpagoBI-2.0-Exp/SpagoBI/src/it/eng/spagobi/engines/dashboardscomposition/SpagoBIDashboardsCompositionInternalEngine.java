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

package it.eng.spagobi.engines.dashboardscomposition;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.Domain;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IDomainDAO;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.drivers.exceptions.InvalidOperationRequest;
import it.eng.spagobi.engines.InternalEngineIFace;
import it.eng.spagobi.utilities.SpagoBITracer;
import it.eng.spagobi.utilities.UploadedFile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SpagoBIDashboardsCompositionInternalEngine implements InternalEngineIFace {

	public static final String messageBundle = "component_spagobidashboardscompositionIE_messages";
	public static final String DASHBOARDS_COMPOSITION = "DASHBOARDS_COMPOSITION";
	public static final String DASHBOARDS_CONFIGURATION = "DASHBOARDS_CONFIGURATION";
	public static final String DASHBOARDS = "DASHBOARDS";
	public static final String DASHBOARD = "DASHBOARD";
	public static final String LAYOUT = "LAYOUT";
	public static final String DATA = "DATA";
	public static final String LOV_LABEL = "lovlabel";
	public static final String REFRESH_RATE = "refreshRate";
	
	
	/**
	 * Executes the document and populates the response 
	 * 
	 * @param requestContainer The <code>RequestContainer</code> object (the session can be retrieved from this object)
	 * @param obj The <code>BIObject</code> representing the document to be executed
	 * @param response The response <code>SourceBean</code> to be populated
	 */
	public void execute(RequestContainer requestContainer, BIObject obj, SourceBean response) throws EMFUserError{
		
		SpagoBITracer.debug("SpagoBIDashboardsCompositionInternalEngine",
	            this.getClass().getName(),
	            "execute",
	            "Start execute method.");
		
		if (obj == null) {
			SpagoBITracer.major("SpagoBIDashboardsCompositionInternalEngine",
		            this.getClass().getName(),
		            "execute",
		            "The input object is null.");
			throw new EMFUserError(EMFErrorSeverity.ERROR, "100", messageBundle);
		}
		
		if (!obj.getBiObjectTypeCode().equalsIgnoreCase("DASH")) {
			SpagoBITracer.major("SpagoBIDashboardsCompositionInternalEngine",
		            this.getClass().getName(),
		            "execute",
		            "The input object is not a dashboard.");
			throw new EMFUserError(EMFErrorSeverity.ERROR, "1001", messageBundle);
		}
		
		
		
		try {	

			// get the template of the object
			obj.loadTemplate();
			UploadedFile template = obj.getTemplate();
			if (template==null) { 
				SpagoBITracer.major("SpagoBIDashboardsCompositionInternalEngine",
						            this.getClass().getName(),
						            "execute",
						            "Template biobject null");
				throw new EMFUserError(EMFErrorSeverity.ERROR, "1002", messageBundle);
			}
			// get bytes of template and transform them into a SourceBean
			SourceBean content = null;
			try {
				byte[] contentBytes = template.getFileContent();
				String contentStr = new String(contentBytes);
				content = SourceBean.fromXMLString(contentStr);
			} catch (Exception e) {
				SpagoBITracer.major("SpagoBIDashboardsCompositionInternalEngine",
			            			this.getClass().getName(),
			            			"execute",
			            			"Error while converting the Template bytes into a SourceBean object");
				throw new EMFUserError(EMFErrorSeverity.ERROR, "1003", messageBundle);
			}
			
			/*
			SourceBean layout = (SourceBean) content.getAttribute(LAYOUT);
			if (layout == null) {
				// TODO
			}
			
			String layoutStr = layout.getCharacters();
			int startDashboard = layoutStr.indexOf("${");
			int count = 0;
			while (startDashboard != -1) {
				int endDashboard = layoutStr.indexOf("}", startDashboard);
				String dashboardName = layoutStr.substring(startDashboard + 2, endDashboard);
				SourceBean dashboardConfSb = null;
				Object dashboardObj = content.getFilteredSourceBeanAttribute(DASHBOARDS_CONFIGURATION + "." + DASHBOARD, "name", dashboardName);
				if (dashboardObj instanceof SourceBean)
					dashboardConfSb = (SourceBean) dashboardObj;
				else {
					SpagoBITracer.major("SpagoBIDashboardsCompositionInternalEngine",
	            			this.getClass().getName(),
	            			"execute",
	            			"Dashboard with name '" + dashboardName + "' has more than one configuration!!");
					Vector v = new Vector();
					v.add(dashboardName);
					throw new EMFUserError(EMFErrorSeverity.ERROR, "1003", v, messageBundle);
				}
//				String movie = GeneralUtilities.getSpagoBiContextAddress();
				String relMovie = (String) dashboardConfSb.getAttribute("movie");
//			    if(relMovie.startsWith("/"))
//			    	movie = movie + relMovie;
//			    else movie = movie + "/" + relMovie;
				String width = (String) dashboardConfSb.getAttribute("DIMENSION.width");
				String height = (String) dashboardConfSb.getAttribute("DIMENSION.height");
//				movie += "?paramHeight="+height+"&paramWidth="+width;
				
				String dataurl = GeneralUtilities.getSpagoBiContextAddress();
				String dataurlRel = (String) dashboardConfSb.getAttribute("DATA.url");
				if(dataurlRel.startsWith("/"))
					dataurl = dataurl + dataurlRel;
				else dataurl = dataurl + "/" + dataurlRel;
				dataurl += "?";
				// get all the parameters for data url
				SourceBean dataSB = (SourceBean) dashboardConfSb.getAttribute("DATA");
				List dataAttrsList = dataSB.getContainedSourceBeanAttributes();
				Iterator dataAttrsIter = dataAttrsList.iterator();
				while(dataAttrsIter.hasNext()) {
					SourceBeanAttribute paramSBA = (SourceBeanAttribute)dataAttrsIter.next();
					SourceBean param = (SourceBean)paramSBA.getValue();
					String nameParam = (String)param.getAttribute("name");
					String valueParam = (String)param.getAttribute("value");
					dataurl += nameParam + "=" + valueParam + "&";
				}
				
				// get all the parameters for dash configuration
				SourceBean confSB = (SourceBean) dashboardConfSb.getAttribute("CONF");
				List confAttrsList = confSB.getContainedSourceBeanAttributes();
				Iterator confAttrsIter = confAttrsList.iterator();
				while(confAttrsIter.hasNext()) {
					SourceBeanAttribute paramSBA = (SourceBeanAttribute)confAttrsIter.next();
					SourceBean param = (SourceBean)paramSBA.getValue();
					String nameParam = (String)param.getAttribute("name");
					String valueParam = (String)param.getAttribute("value");
					dataurl += nameParam + "=" + valueParam + "&";
				}
			    // append to the calling url the dataurl	
//				movie += "&dataurl=" + dataurl;
				String replacement = "\n<script type=\"text/javascript\">\n";
				String contextPath = PortletUtilities.getPortletRequest().getContextPath();
				if (!contextPath.startsWith("/")) contextPath = "/" + contextPath;
//				if (contextPath.endsWith("/")) contextPath += "dashboards";
//				else contextPath += "/dashboards";
				replacement += "lzLPSRoot = '" + contextPath + "';\n";
				replacement += "lzCanvasRuntimeVersion = 7 * 1;\n";
				//replacement += "if (lzCanvasRuntimeVersion == 6) {\n";
				//replacement += "	lzCanvasRuntimeVersion = 6.65;\n";
				//replacement += "}\n";
				//replacement += "if (isIE && isWin || detectFlash() >= lzCanvasRuntimeVersion) {\n";
				replacement += "	lzEmbed({url: '" + relMovie + "?&lzproxied=false&__lzhistconn='+top.connuid+'&__lzhisturl=' + escape('lps/includes/h.html?h='), bgcolor: '#eaeaea', width: '600', height: '600', id: 'lzapp', accessible: 'false'}, lzCanvasRuntimeVersion);\n";
				//replacement += "	lzEmbed({url: '" + relMovie + "?&lzproxied=false&__lzhistconn='+top.connuid+'&__lzhisturl=' + escape('lps/includes/h.html?h='), bgcolor: '#eaeaea', width: '75%', height: '75%', id: 'lzapp" + count + "', accessible: 'false'}, lzCanvasRuntimeVersion);\n";
				replacement += "	lzHistEmbed(lzLPSRoot);\n";
				//replacement += "} else {\n";
				//replacement += "	document.write('This application requires Flash player ' + lzCanvasRuntimeVersion + '. <a href=\"http://www.macromedia.com/go/getflashplayer\" target=\"fpupgrade\">Click here</a> to upgrade.');\n";
				//replacement += "}\n";
				replacement += "</script>\n";
				layoutStr = layoutStr.replace("${" + dashboardName + "}", replacement);
				startDashboard = layoutStr.indexOf("${", endDashboard);
				count++;
			}
			layoutStr = layoutStr.replaceAll("<", "&lt;");
			layoutStr = layoutStr.replaceAll(">", "&gt;");
			layoutStr = layoutStr.replaceAll("\"", "&quot;");
			*/
			
			
			// create the title
			String title = "";
			title += obj.getName();
			String objDescr = obj.getDescription();
			if( (objDescr!=null) && !objDescr.trim().equals("") ) {
				title += ": " + objDescr;
			}
			
			// get the actor
			SessionContainer session = requestContainer.getSessionContainer();
			String actor = (String) session.getAttribute(SpagoBIConstants.ACTOR);
			// get the possible state changes
			IDomainDAO domaindao = DAOFactory.getDomainDAO();
			List states = domaindao.loadListDomainsByType("STATE");
		    List possibleStates = new ArrayList();
		    if (actor.equalsIgnoreCase(SpagoBIConstants.DEV_ACTOR)){
		    	Iterator it = states.iterator();
		    	 while(it.hasNext()) {
		      		    	Domain state = (Domain)it.next();
		      		    	if (state.getValueCd().equalsIgnoreCase("TEST")){ 
		      					possibleStates.add(state);
		      				}
		      	}  
		    } else if (actor.equalsIgnoreCase(it.eng.spagobi.constants.SpagoBIConstants.TESTER_ACTOR)){
		    	Iterator it = states.iterator();
		    	 while(it.hasNext()) {
		      		    	Domain state = (Domain)it.next();
		      		    	if ((state.getValueCd().equalsIgnoreCase("DEV")) || ((state.getValueCd().equalsIgnoreCase("REL")))) { 
		      					possibleStates.add(state);
		      				}
		      	}  
		    }
			response.setAttribute("title", title);
			response.setAttribute("possibleStateChanges", possibleStates);
			// set information for the publisher
			response.setAttribute(content);
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, DASHBOARDS_COMPOSITION);
			
		} catch (EMFUserError error) {
			
			throw error;
			
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBIDashboardsCompositionInternalEngine", 
						this.getClass().getName(),
					    "execute", 
					    "Cannot exec the dashboard", e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "100", messageBundle);
		}

	}

	/**
	 * The <code>SpagoBIDashboardsCompositionInternalEngine</code> cannot manage subobjects so this method must not be invoked
	 * 
	 * @param requestContainer The <code>RequestContainer</code> object (the session can be retrieved from this object)
	 * @param obj The <code>BIObject</code> representing the document
	 * @param response The response <code>SourceBean</code> to be populated
	 * @param subObjectInfo An object describing the subobject to be executed
	 */
	public void executeSubObject(RequestContainer requestContainer, BIObject obj, 
			SourceBean response, Object subObjectInfo) throws EMFUserError {
		// it cannot be invoked
		SpagoBITracer.major("SpagoBIDashboardsCompositionInternalEngine", 
				this.getClass().getName(),
	            "executeSubObject", 
	            "SpagoBIDashboardsCompositionInternalEngine cannot exec subobjects.");
		throw new EMFUserError(EMFErrorSeverity.ERROR, "101", messageBundle);
	}

	/**
	 * Function not implemented. Thid method should not be called
	 * 
	 * @param requestContainer The <code>RequestContainer</code> object (the session can be retrieved from this object)
	 * @param biobject The BIOBject to edit
	 * @param response The response <code>SourceBean</code> to be populated
	 * @throws InvalidOperationRequest
	 */
	public void handleNewDocumentTemplateCreation(RequestContainer requestContainer, 
			BIObject obj, SourceBean response) throws EMFUserError, InvalidOperationRequest {
		SpagoBITracer.major("SpagoBIDashboardsCompositionInternalEngine", 
				this.getClass().getName(),
	            "handleNewDocumentTemplateCreation", 
	            "SpagoBIDashboardsCompositionInternalEngine cannot build document template.");
		throw new InvalidOperationRequest();
		
	}

	/**
	 * Function not implemented. Thid method should not be called
	 * 
	 * @param requestContainer The <code>RequestContainer</code> object (the session can be retrieved from this object)
	 * @param biobject The BIOBject to edit
	 * @param response The response <code>SourceBean</code> to be populated
	 * @throws InvalidOperationRequest
	 */
	public void handleDocumentTemplateEdit(RequestContainer requestContainer, 
			BIObject obj, SourceBean response) throws EMFUserError, InvalidOperationRequest {
		SpagoBITracer.major("SpagoBIDashboardsCompositionInternalEngine", 
				this.getClass().getName(),
	            "handleDocumentTemplateEdit", 
	            "SpagoBIDashboardsCompositionInternalEngine cannot build document template.");
		throw new InvalidOperationRequest();
	}
	
}
