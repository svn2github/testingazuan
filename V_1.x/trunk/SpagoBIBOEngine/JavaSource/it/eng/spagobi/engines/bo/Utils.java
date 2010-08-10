/**
Copyright (c) 2005, Engineering Ingegneria Informatica s.p.a.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this list of 
      conditions and the following disclaimer.
      
    * Redistributions in binary form must reproduce the above copyright notice, this list of 
      conditions and the following disclaimer in the documentation and/or other materials 
      provided with the distribution.
      
    * Neither the name of the Engineering Ingegneria Informatica s.p.a. nor the names of its contributors may
      be used to endorse or promote products derived from this software without specific
      prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND 
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, 
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
*/
package it.eng.spagobi.engines.bo;

import it.eng.spagobi.engines.bo.exceptions.SpagoBIBOEngineException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.bo.rebean.wi.DocumentInstance;
import com.bo.rebean.wi.DrillBar;
import com.bo.rebean.wi.DrillBarObject;
import com.bo.rebean.wi.DrillDimension;
import com.bo.rebean.wi.DrillDimensions;
import com.bo.rebean.wi.DrillHierarchies;
import com.bo.rebean.wi.DrillHierarchy;
import com.bo.rebean.wi.DrillInfo;
import com.bo.rebean.wi.HTMLView;
import com.bo.rebean.wi.OutputFormatType;
import com.bo.rebean.wi.Prompt;
import com.bo.rebean.wi.Prompts;
import com.bo.rebean.wi.Report;
import com.bo.rebean.wi.ReportEngine;
import com.bo.wibean.WIDocument;
import com.bo.wibean.WIException;
import com.bo.wibean.WIOutput;
import com.bo.wibean.WIPrompt;
import com.bo.wibean.WIPrompts;
import com.bo.wibean.WISession;

public class Utils {

	private static final String PROMPT_ASSOCIATION_FILE = "WEB-INF/classes/prompts.properties";
	
	private static Logger logger = Logger.getLogger(Utils.class);
	
	public static boolean controlRepository(String repName) {
		repName = repName.trim();
		if(!repName.equalsIgnoreCase("corporate") &&
		   !repName.equalsIgnoreCase("inbox") && 
		   !repName.equalsIgnoreCase("personal")) {
			return false;
		} else return true;
	}
	
	public static int getRepCodeFromName(String repName) {
		int code = 0;
		if(repName.equalsIgnoreCase("corporate"))
			code = 0;
		if(repName.equalsIgnoreCase("ibox"))
			code = 1;
		if(repName.equalsIgnoreCase("personal"))
			code = 2;
	    return code; 
	}
	
	public static ReportEngine createReportEngine(WISession wiSession) {
		ReportEngine cdzReportEngine = new ReportEngine();
		cdzReportEngine.init(wiSession.getSessionID(), wiSession.getLocale());
		return  cdzReportEngine;
	}
	
	
	public static void fillPrompts(DocumentInstance repInstance, HttpServletRequest request) {
			Prompts prompts = repInstance.getPrompts();
			int numPrompts = prompts.getCount();
			for(int i=0; i<numPrompts; i++) {
				 Prompt prompt = prompts.getItem(i);
				 String namePrompt = prompt.getName();
				 String valuePrompt = request.getParameter(namePrompt);
				 logger.info("Engines"+ Utils.class.getName()+ 
		         			 "fillPrompts() output type not supported");
				 if (valuePrompt != null) {
					// if the parameter is multivalue, values are separated by ";" (see BODriver.addBIParameters(BIObject biobj, Map pars))
				 	String[] valsPrompt = valuePrompt.split(";");
				 	prompt.enterValues(valsPrompt);
				 }
			}
			repInstance.setPrompts();
	    }
	
	/**
	 * Fills the report prompts.
	 * @param repDocument The WIDocument object for a .rep type document; getHTMLView(true) must be invoked on this object before calling this method).
	 * @param request The HttpServletRequest request
	 * @param servletContext 
	 * @throws SpagoBIBOEngineException 
	 */
	public static void fillPrompts(WIDocument repDocument, HttpServletRequest request, ServletContext servletContext) throws SpagoBIBOEngineException {
		logger.debug("IN");
		try {
			Properties props = loadPromptsAssociationFile(servletContext);
			WIPrompts prompts = repDocument.getPrompts();
			logger.debug("Report prompts retrieved.");
			if (prompts != null) {
				fillPrompts(prompts, request, props);
			}
		} catch (SpagoBIBOEngineException e) {
			throw e;
		} catch (Exception e) {
			logger.error("Error while filling report prompts", e);
		} finally {
			logger.debug("OUT");
		}
    }
	
	private static void fillPrompts(WIPrompts prompts, HttpServletRequest request, Properties props) throws SpagoBIBOEngineException {
		logger.debug("IN");
		try {
			int numPrompts = prompts.getCount();
			List parametersNotSet = new ArrayList();
			for (int i = 0; i < numPrompts; i++) {
				 WIPrompt prompt = prompts.getItem(i + 1);
				 String namePrompt = prompt.getName();
				 logger.debug("Evaluating prompt [" + namePrompt + "]");
				 String associatedSpagoBIParName = null;
				 if (props.containsValue(namePrompt)) {
					 Enumeration keys = props.keys();
					 while (keys.hasMoreElements()) {
						 String key = (String) keys.nextElement();
						 if (props.getProperty(key).equals(namePrompt)) {
							 associatedSpagoBIParName = key;
							 break;
						 }
					 }
					 logger.debug("Found name association between prompt [" + namePrompt + "] and SpagoBI parameter [" + associatedSpagoBIParName + "].");
				 } else {
					 logger.debug("No name association found between prompt [" + namePrompt + "] and SpagoBI parameters.");
				 }
				 String valuePrompt = null;
				 if (associatedSpagoBIParName != null) {
					 valuePrompt = request.getParameter(associatedSpagoBIParName);
					 logger.debug("Find report prompt with name = [" + namePrompt + "] associated to SpagoBI parameter [" + associatedSpagoBIParName + "]; relevant httpRequest parameter value is [" + valuePrompt + "].");
				 } else {
					 valuePrompt = request.getParameter(namePrompt);
					 logger.debug("Find report prompt with name = [" + namePrompt + "]; relevant httpRequest parameter value is [" + valuePrompt + "].");
				 }
				 if (valuePrompt != null) {
					// if the parameter is multivalue, values are separated by ";" (see BODriver.addBIParameters(BIObject biobj, Map pars))
					// so there is no need to decode anything (BO wants values to be separated by ";")
					logger.debug("Entering new value = [" + valuePrompt + "] into prompt with name = [" + namePrompt + "].");
					prompt.enterValue(valuePrompt);
					/*
					OLD CODE
					String strValueList = "";
					if (decoder.isMultiValues(valuePrompt)) {			
						List values = decoder.decode(valuePrompt);
						for (int j = 0; j < values.size(); j++) {
							strValueList += (String) values.get(j);
							if (j < values.size() - 1) strValueList += ";";
						}
					} else {
						strValueList = valuePrompt;
					}
					 */
				 } else {
					 logger.error("Prompt with name = [" + namePrompt + "] has no value set. Cannot refresh document.");
					 parametersNotSet.add(namePrompt);
				 }
				 
				 /*
				try {
					prompt.getListOfValues(true);
				} catch (WIException wiException) {
					int iWiException = wiException.getNumber();
					if (iWiException != WIException.WIERR_DOC_NEEDPROMPT_01) {
						WIPrompts nestedPrompts = prompt.getPrompts();
						fillPrompts(prompts, request, props);
					}
				}
				*/
				 
			}
			if (parametersNotSet.size() != 0) {
				String message = "Prompts with name " + parametersNotSet.toString() + " have no value set. " +
						"Cannot refresh document. You must configure a parameter for the SpagoBI document for each missing prompt.";
				throw new SpagoBIBOEngineException(message);
			}
		} finally {
			logger.debug("OUT");
		}
	}
	
	private static Properties loadPromptsAssociationFile(ServletContext servletContext) {
		logger.debug("IN");
		InputStream is = null;
		try {
			String contextRealPath = servletContext.getRealPath("/");
			if (!contextRealPath.endsWith("/")) contextRealPath += "/";
			File promptsAssociationFile = new File(contextRealPath + PROMPT_ASSOCIATION_FILE);
			if (promptsAssociationFile.exists() && promptsAssociationFile.isFile()) {
				logger.debug("Loading file " + promptsAssociationFile.getAbsolutePath());
				is = new FileInputStream(promptsAssociationFile);
			} else {
				logger.debug("File " + PROMPT_ASSOCIATION_FILE + " not found");
			}
	 		//is = Thread.currentThread().getContextClassLoader().getResourceAsStream("prompts.properties");
		 	Properties props = new Properties();
		 	if (is != null) props.load(is);
		 	return props;
		} catch (Exception e) {
			logger.error("Error while loading prompts association file", e);
			return new Properties();
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					logger.debug("Error while closing stream on " + PROMPT_ASSOCIATION_FILE + " file.");
				}
			logger.debug("OUT");
		}
		
	}
	
	public static void addHtmlInSession(Report report, HttpSession session) {
		HTMLView reportHtmlView = (HTMLView) report.getView(OutputFormatType.HTML);
		String headPart = reportHtmlView.getStringPart("head", false);
		String bodyPart = reportHtmlView.getStringPart("body", false);
		session.setAttribute(BOConstants.REPORTHEADPART, headPart);
		session.setAttribute(BOConstants.REPORTBODYPART, bodyPart);
	}
	

	public static void addHtmlInSession(WIOutput wiOutput,
			HttpSession session) {
		String headPart = wiOutput.getStringPart("head", false);
		String bodyPart = wiOutput.getStringPart("body", false);
		session.setAttribute(BOConstants.REPORTHEADPART, headPart);
		session.setAttribute(BOConstants.REPORTBODYPART, bodyPart);
	}
	
	public static HashMap getDrillDimensions(DrillInfo drillInfo) throws Exception {
		HashMap hash = null;
		for (int count = 0; true; count++) {
			try {
				hash = getDrillDimensionsPrivate(drillInfo);
			} catch (Exception e) {
				logger.error(Utils.class + ":getDrillDimensions: caught excetpion " + e);
				if (count < 10) continue;
				else throw e;
			}
			if (hash != null) break;
		}
		return hash;
	}
	
	private static HashMap getDrillDimensionsPrivate(DrillInfo drillInfo) throws Exception {
		HashMap hash = new HashMap();
		DrillHierarchies drillHierarchies = drillInfo.getDrillHierarchies();
		DrillBar drillBar = drillInfo.getDrillBar();
		//insert each hierarchy into the hash Map 
		//if a hierarchy dimension is in the drillbar, it is not loaded into the list
		for (int i=0; i<drillHierarchies.getCount(); i++){
			DrillHierarchy drillHierarchy = drillHierarchies.getItem(i);
			ArrayList hierDimList = new ArrayList();
			DrillDimensions hierDrillDim = drillHierarchy.getDrillDimensions();
			String hierName = drillHierarchy.getName();
			for (int j=0; j<hierDrillDim.getCount();j++){
				if(!isInDrillBar(hierDrillDim.getItem(j), drillBar)){
					hierDimList.add(hierDrillDim.getItem(j));
					}
			}
			hash.put(hierName,hierDimList);
			}
		//then insert all free dimensions inside the HashMap, in the same way
	    DrillDimensions freeDrillDim = drillInfo.getFreeDrillDimensions();
	    ArrayList freeDimList = new ArrayList();
	    for(int k=0; k<freeDrillDim.getCount();k++){
	    	if(!isInDrillBar(freeDrillDim.getItem(k), drillBar)){
	    		freeDimList.add(freeDrillDim.getItem(k));
	    		}
	    }
	    hash.put("Other",freeDimList);
		return hash;
	}
     private static boolean isInDrillBar (DrillDimension dimension, DrillBar drillBar) {
    	 boolean isInDrillBar = false;
    	 for (int i=0; i<drillBar.getCount(); i++){
    		 DrillBarObject object = drillBar.getItem(i);
    		 if(object.getID().equalsIgnoreCase(dimension.getID())){
    			 isInDrillBar = true;
    		 }
    	 }
     return isInDrillBar;
     }

}
