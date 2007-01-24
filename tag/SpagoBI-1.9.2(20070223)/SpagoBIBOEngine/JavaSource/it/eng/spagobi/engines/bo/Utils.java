package it.eng.spagobi.engines.bo;

import java.util.ArrayList;
import java.util.HashMap;

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
import com.bo.wibean.WISession;

public class Utils {

	private static Logger logger = Logger.getLogger(BOServlet.class);
	
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
				 	String[] valsPrompt = valuePrompt.split(",");
				 	prompt.enterValues(valsPrompt);
				 }
			}
			repInstance.setPrompts();
	    }
	
	public static void addHtmlInSession(Report report, HttpSession session) {
		HTMLView reportHtmlView = (HTMLView) report.getView(OutputFormatType.HTML);
		String headPart = reportHtmlView.getStringPart("head", false);
		String bodyPart = reportHtmlView.getStringPart("body", false);
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
