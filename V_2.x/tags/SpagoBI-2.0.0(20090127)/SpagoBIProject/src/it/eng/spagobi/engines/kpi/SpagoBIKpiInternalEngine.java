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
package it.eng.spagobi.engines.kpi;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.bo.ObjTemplate;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.constants.ObjectsTreeConstants;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.engines.InternalEngineIFace;
import it.eng.spagobi.engines.drivers.exceptions.InvalidOperationRequest;
import it.eng.spagobi.engines.kpi.bo.ChartImpl;
import it.eng.spagobi.engines.kpi.bo.KpiLine;
import it.eng.spagobi.engines.kpi.bo.KpiResourceBlock;
import it.eng.spagobi.engines.kpi.utils.StyleLabel;
import it.eng.spagobi.kpi.config.bo.Kpi;
import it.eng.spagobi.kpi.config.bo.KpiInstance;
import it.eng.spagobi.kpi.config.bo.KpiValue;
import it.eng.spagobi.kpi.model.bo.ModelInstanceNode;
import it.eng.spagobi.kpi.model.bo.Resource;
import it.eng.spagobi.kpi.threshold.bo.Threshold;
import it.eng.spagobi.tools.dataset.bo.IDataSet;
import it.eng.spagobi.tools.dataset.common.behaviour.QuerableBehaviour;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStoreMetaData;
import it.eng.spagobi.tools.dataset.common.datastore.IField;
import it.eng.spagobi.tools.dataset.common.datastore.IRecord;

import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 
 * @author Chiara Chiarelli
 * 
 */

public class SpagoBIKpiInternalEngine implements InternalEngineIFace {

    private static transient Logger logger = Logger.getLogger(SpagoBIKpiInternalEngine.class);

    public static final String messageBundle = "messages";

    protected String name = null;// Document's title
    protected String subName = null;// Document's subtitle
    protected StyleLabel styleTitle;// Document's title style
    protected StyleLabel styleSubTitle;// Document's subtitle style

    protected IEngUserProfile profile;

    protected HashMap parametersObject;

    protected boolean display_semaphore = false;// true if the semaphore will be
						// displayed
    protected boolean display_bullet_chart = false;// true if the bullet chart
						    // will be displayed
    protected boolean display_weight = false;// true if the weight will be
						// displayed
    protected boolean display_alarm = false;// true if the alarm state will be
					    // displayed
    protected boolean register_values = true;//true if the new values calculated will have to be inserted into the db
    
    protected boolean recalculate_anyway = false;//true if the scheduler calls the engine and wants the kpi to be calculated even if another value already exists

    protected HashMap confMap;// HashMap with all the config parameters

    protected List resources;// List of resources linked to the
				// ModelInstanceNode
    
    protected Date dateOfKPI = new Date();//date when the kpiValues are requested
    
    protected Date endKpiValueDate = null;//End validity date for the kpiValues 
    
    protected Integer periodInstID = null;

    //Method usually called by the scheduler only in order to recalculate kpi values
    public void execute(RequestContainer requestContainer, SourceBean response) throws EMFUserError {
    	logger.debug("IN");

       	ResponseContainer responseContainer = ResponseContainer.getResponseContainer();
    	EMFErrorHandler errorHandler = responseContainer.getErrorHandler();

    	SessionContainer session = requestContainer.getSessionContainer();
    	IEngUserProfile userProfile = (IEngUserProfile) session.getPermanentContainer().getAttribute(
    		IEngUserProfile.ENG_USER_PROFILE);
   	
    	this.parametersObject = new HashMap();
    	String recalculate = (String)requestContainer.getAttribute("recalculate_anyway");
    	if(recalculate.equals("true")){
    		this.recalculate_anyway = true;
    	}	
    	    // Date for which we want to see the KpiValues
    	    this.dateOfKPI = (Date)requestContainer.getAttribute("start_date");
    	    this.endKpiValueDate = (Date)requestContainer.getAttribute("end_date");
    	    String cascade = (String)requestContainer.getAttribute("cascade");	
    	   

    	    // **************take informations on the modelInstance and its KpiValues*****************
    	    String modelNodeInstance = (String) requestContainer.getAttribute("model_node_instance");
    	    logger.debug("ModelNodeInstance : " + modelNodeInstance);
    	    Integer modelNodeInstanceID = new Integer(modelNodeInstance);
    	    if (modelNodeInstanceID == null) {
    	    	logger.error("The modelNodeInstanceId specified in the template is null");
    	    	throw new EMFUserError(EMFErrorSeverity.ERROR, "10106", messageBundle);
    	    }
    	    /*String periodInstanceID = (String)requestContainer.getAttribute("periodicity_id");
    	    logger.debug("PeriodInstanceID : " + (periodInstanceID!=null ? periodInstanceID : "null"));
    	    
    	    if (periodInstanceID == null) {
    	    	logger.error("No periodInstID specified");
    	    }else{
    	    	periodInstID = new Integer(periodInstanceID);
    	    }*/

    	    List kpiRBlocks = new ArrayList();// List of KpiValues Trees for
    						// each Resource: it will be sent to the jsp

    	    // gets the ModelInstanceNode
    	    ModelInstanceNode mI = DAOFactory.getKpiDAO().loadModelInstanceById(modelNodeInstanceID, this.dateOfKPI);
    	    logger.debug("ModelInstanceNode, NAME=" + mI.getName());
    	    logger.debug("Loaded the modelInstanceNode with id " + modelNodeInstanceID.toString());
    	    // I set the list of resources of that specific ModelInstance
    	    if (this.resources == null || this.resources.isEmpty()) {
    	    	this.resources = mI.getResources();
    	    }
    	    logger.debug("Setted the List of Resources related to the specified Model Instance");

    	    if (cascade!=null && cascade.equals("true")){//in case all the kpi children have to be calculated too
    	    	   	    
	    	    try {
	    	    if (this.resources == null || this.resources.isEmpty()) {
	    			logger.debug("There are no resources assigned to the Model Instance");
	    			
	    			KpiResourceBlock block = new KpiResourceBlock();
	    			block.setD(this.dateOfKPI);
	    			KpiLine line = getBlock(modelNodeInstanceID, null);				
	    			block.setRoot(line);
	    			logger.debug("Setted the tree Root.");
	    			kpiRBlocks.add(block);
	    			
	    	    } else {
	    			Iterator resourcesIt = this.resources.iterator();
	    			while (resourcesIt.hasNext()) {
	    			    Resource r = (Resource) resourcesIt.next();
	    			    logger.debug("Resource: " + r.getName());
	    			    KpiResourceBlock block = new KpiResourceBlock();
	    			    block.setR(r);
	    			    block.setD(dateOfKPI);
	    			    KpiLine line = getBlock(modelNodeInstanceID, r);
	    			    block.setRoot(line);
	    			    logger.debug("Setted the tree Root.");
	    			    kpiRBlocks.add(block);
	    			}
	    	    }
	    	    } catch (EMFInternalError e) {
					e.printStackTrace();
				}
    	    }else{//in case all the kpi children don't have to be calculated 
    	    	 try {
    	    		 	KpiInstance kpiI = mI.getKpiInstanceAssociated();
		    			IDataSet dataSet = DAOFactory.getKpiDAO().getDsFromKpiId(kpiI.getKpi());
    		    	    if (this.resources == null || this.resources.isEmpty()) {
    		    			logger.debug("There are no resources assigned to the Model Instance");
    		    			
       		    			logger.debug("Retrieved the Dataset to be calculated: " + dataSet.getId());
    		    			KpiValue value = getNewKpiValue(dataSet, kpiI, null);
    		    			logger.debug("New value calculated");
    		    			// Insert new Value into the DB
    		    			DAOFactory.getKpiDAO().insertKpiValue(value);
    		    			logger.debug("New value inserted in the DB");		
    		    			// Checks if the value is alarming (out of a certain range)
    		    			// If the value is alarming a new line will be inserted in the
    		    			// sbi_alarm_event table and scheduled to be sent
    		    			DAOFactory.getKpiDAO().isAlarmingValue(value);   		    			
    		    			
    		    	    } else {
    		    			Iterator resourcesIt = this.resources.iterator();
    		    			while (resourcesIt.hasNext()) {
    		    			    Resource r = (Resource) resourcesIt.next();
    		    			    logger.debug("Resource: " + r.getName());
    		    			    logger.debug("Retrieved the Dataset to be calculated: " + dataSet.getId());
        		    			KpiValue value = getNewKpiValue(dataSet, kpiI, r);
        		    			logger.debug("New value calculated");
        		    			// Insert new Value into the DB
        		    			DAOFactory.getKpiDAO().insertKpiValue(value);
        		    			logger.debug("New value inserted in the DB");		
        		    			// Checks if the value is alarming (out of a certain range)
        		    			// If the value is alarming a new line will be inserted in the
        		    			// sbi_alarm_event table and scheduled to be sent
        		    			DAOFactory.getKpiDAO().isAlarmingValue(value);   
    		    			}
    		    	    }
    		    	    } catch (EMFInternalError e) {
    						e.printStackTrace();
    					}
    	    }
    	    logger.debug("OUT");    	
        }
    
    /**
     * Executes the document and populates the response.
     * 
     * @param requestContainer
     *                The <code>RequestContainer</code> object (the session
     *                can be retrieved from this object)
     * @param obj
     *                The <code>BIObject</code> representing the document to
     *                be executed
     * @param response
     *                The response <code>SourceBean</code> to be populated
     * 
     * @throws EMFUserError
     *                 the EMF user error
     */

    public void execute(RequestContainer requestContainer, BIObject obj, SourceBean response) throws EMFUserError {
	logger.debug("IN");

	
	ResponseContainer responseContainer = ResponseContainer.getResponseContainer();
	EMFErrorHandler errorHandler = responseContainer.getErrorHandler();

	if (obj == null) {
	    logger.error("The input object is null.");
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "100", messageBundle);
	}

	if (!obj.getBiObjectTypeCode().equalsIgnoreCase("KPI")) {
	    logger.error("The input object is not a KPI.");
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "1001", messageBundle);
	}

	String documentId = obj.getId().toString();
	logger.debug("Loaded documentId:" + documentId);
	SessionContainer session = requestContainer.getSessionContainer();
	IEngUserProfile userProfile = (IEngUserProfile) session.getPermanentContainer().getAttribute(
		IEngUserProfile.ENG_USER_PROFILE);
	String userId = (String) ((UserProfile) userProfile).getUserId();

	logger.debug("Got parameters userId=" + userId + " and documentId=" + documentId.toString());

	// **************get the template*****************
	logger.debug("Getting template.");

	try {
	    SourceBean content = null;
	    byte[] contentBytes = null;
	    try {
		ObjTemplate template = DAOFactory.getObjTemplateDAO().getBIObjectActiveTemplate(
			Integer.valueOf(documentId));
		if (template == null) {
		    logger.warn("Active Template null.");
		    throw new Exception("Active Template null.");
		}
		contentBytes = template.getContent();
		if (contentBytes == null) {
		    logger.warn("Content of the Active template null.");
		    throw new Exception("Content of the Active template null");
		}

		// get bytes of template and transform them into a SourceBean

		String contentStr = new String(contentBytes);
		content = SourceBean.fromXMLString(contentStr);
		logger.debug("Got the content of the template");
	    } catch (Exception e) {
		logger.error("Error while converting the Template bytes into a SourceBean object");
		EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 2003);
		userError.setBundle("messages");
		throw userError;
	    }

	    // Date for which we want to see the KpiValues
	    this.dateOfKPI = new Date();
	    this.parametersObject = readParameters(obj.getBiObjectParameters());
	    logger.debug("Got the date for which the KpiValues have to be calculated. Date:" + this.dateOfKPI);

	    // **************take informations on the modelInstance and its KpiValues*****************
	    String modelNodeInstance = (String) content.getAttribute("model_node_instance");
	    logger.debug("ModelNodeInstance : " + modelNodeInstance);
	    Integer modelNodeInstanceID = new Integer(modelNodeInstance);
	    if (modelNodeInstanceID == null) {
		logger.error("The modelNodeInstanceId specified in the template is null");
		throw new EMFUserError(EMFErrorSeverity.ERROR, "10106", messageBundle);
	    }
	    String periodInstanceID = (String) content.getAttribute("periodicity_id");
	    logger.debug("PeriodInstanceID : " + (periodInstanceID!=null ? periodInstanceID : "null"));
	    
	    if (periodInstanceID == null) {
	    	logger.error("No periodInstID specified");
	    }else{
	    	periodInstID = new Integer(periodInstanceID);
	    }
	    getSetConf(content);
	    logger.debug("Setted the configuration of the template");

	    List kpiRBlocks = new ArrayList();// List of KpiValues Trees for
						// each Resource: it will be
						// sent to the jsp

	    // gets the ModelInstanceNode
	    ModelInstanceNode mI = DAOFactory.getKpiDAO().loadModelInstanceById(modelNodeInstanceID, this.dateOfKPI);
	    logger.debug("ModelInstanceNode, NAME=" + mI.getName());
	    logger.debug("Loaded the modelInstanceNode with id " + modelNodeInstanceID.toString());
	    // I set the list of resources of that specific ModelInstance
	    if (this.resources == null || this.resources.isEmpty()) {
	    	this.resources = mI.getResources();
	    }
	    logger.debug("Setted the List of Resources related to the specified Model Instance");

	    if (this.resources == null || this.resources.isEmpty()) {
			logger.debug("There are no resources assigned to the Model Instance");
			KpiResourceBlock block = new KpiResourceBlock();
			block.setD(this.dateOfKPI);
			KpiLine line = getBlock(modelNodeInstanceID, null);
			block.setRoot(line);
			logger.debug("Setted the tree Root.");
			kpiRBlocks.add(block);
	    } else {
			Iterator resourcesIt = this.resources.iterator();
			while (resourcesIt.hasNext()) {
			    Resource r = (Resource) resourcesIt.next();
			    logger.debug("Resource: " + r.getName());
			    KpiResourceBlock block = new KpiResourceBlock();
			    block.setR(r);
			    block.setD(dateOfKPI);
			    KpiLine line = getBlock(modelNodeInstanceID, r);
			    block.setRoot(line);
			    logger.debug("Setted the tree Root.");
			    kpiRBlocks.add(block);
			}
	    }

	    try {
		logger.debug("Successfull kpis creation");

		response.setAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR, obj);
		response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "KPI");
		response.setAttribute("display_bullet_chart", display_bullet_chart);
		response.setAttribute("display_alarm", display_alarm);
		response.setAttribute("display_semaphore", display_semaphore);
		response.setAttribute("display_weight", display_weight);
		if (name != null) {
		    response.setAttribute("title", name);
		    response.setAttribute("styleTitle", styleTitle);
		}
		if (subName != null) {
		    response.setAttribute("subName", subName);
		    response.setAttribute("styleSubTitle", styleSubTitle);
		}
		response.setAttribute("kpiRBlocks", kpiRBlocks);
	    } catch (Exception eex) {
		EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 10107);
		userError.setBundle("messages");
		throw userError;
	    }

	    logger.debug("OUT");
	} catch (EMFUserError e) {

	    errorHandler.addError(e);
	} catch (Exception e) {
	    EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 101);
	    logger.error("Generic Error", e);
	    errorHandler.addError(userError);
	}
    }

   

    public KpiLine getBlock(Integer miId, Resource r) throws EMFUserError, EMFInternalError {
	logger.debug("IN");
	KpiLine line = new KpiLine();
	ModelInstanceNode modI = DAOFactory.getKpiDAO().loadModelInstanceById(miId, dateOfKPI);
	if (modI != null) {
	    logger.debug("Loaded Model Instance Node with id: " + modI.getModelInstanceNodeId());
	}
	String modelNodeName = modI.getName();
	line.setModelNodeName(modelNodeName);

	KpiInstance kpiI = modI.getKpiInstanceAssociated();
	if (kpiI != null) {
	    logger.debug("Got KpiInstance with ID: " + kpiI.getKpiInstanceId().toString());
	    KpiValue value = DAOFactory.getKpiDAO().getKpiValue(kpiI.getKpiInstanceId(), dateOfKPI, r);
	    logger.debug("Old KpiValue retrieved");
	    if (value == null || recalculate_anyway) {
			logger.debug("Old value not valid anymore");
			IDataSet dataSet = DAOFactory.getKpiDAO().getDsFromKpiId(kpiI.getKpi());
		
			logger.debug("Retrieved the Dataset to be calculated: " + dataSet.getId());
			value = getNewKpiValue(dataSet, kpiI, r);
			logger.debug("New value calculated");
			if(register_values){
				// Insert new Value into the DB
				DAOFactory.getKpiDAO().insertKpiValue(value);
				logger.debug("New value inserted in the DB");
			}			
			// Checks if the value is alarming (out of a certain range)
			// If the value is alarming a new line will be inserted in the
			// sbi_alarm_event table and scheduled to be sent
			DAOFactory.getKpiDAO().isAlarmingValue(value);
			logger.debug("Alarms sent if the value is over the thresholds");
			line.setValue(value);
	    } else if (value != null) {
	    	line.setValue(value);
	    } else {
	    	return line;
	    }
	    Integer kpiId = kpiI.getKpi();
	    Kpi k = DAOFactory.getKpiDAO().loadKpiById(kpiId);
	    logger.debug("Retrieved the kpi with id: " + kpiId.toString());

	    if (k != null) {
		String docLabel = k.getDocumentLabel();
		if (docLabel != null && !docLabel.equals("")) {
		    List documents = new ArrayList();
		    logger.debug("Retrieved documents associated to the KPI");
		    documents.add(docLabel);
		    line.setDocuments(documents);
		}
	    }
	    if (display_alarm && value.getValue() != null) {
		Boolean alarm = DAOFactory.getKpiDAO().isKpiInstUnderAlramControl(kpiI.getKpiInstanceId());
		logger.debug("KPI is under alarm control: " + alarm.toString());
		line.setAlarm(alarm);
	    }

	    if (display_bullet_chart && value.getValue() != null) {

		List thresholds = value.getThresholds();
		/*
		 * String chartType = value.getChartType(); logger.debug("Got
		 * chartType: "+(chartType!=null?chartType:""));
		 * if(chartType==null){ logger.debug("Chart Type is null"); }
		 */
		String chartType = "BulletGraph";
		Double val = new Double(value.getValue());
		Double target = value.getTarget();
		HashMap pars = (HashMap) confMap.clone();
		ChartImpl sbi = null;
		sbi = ChartImpl.createChart(chartType);
		logger.debug("Chart created");
		sbi.setProfile(profile);
		logger.debug("Profile setted for the chart");
		sbi.setValueDataSet(val);
		logger.debug("Value to be represented, setted: " + (val != null ? val.toString() : ""));
		if (target != null) {
		    sbi.setTarget(target);
		    logger.debug("Target setted: " + (target != null ? target.toString() : ""));
		}
		sbi.configureChart(pars);
		logger.debug("Config parameters setted into the chart");
		String thresholdsJsArray = sbi.setThresholds(thresholds);
		if (thresholdsJsArray != null) {
		    line.setThresholdsJsArray(thresholdsJsArray);
		}
		logger.debug("Thresholds setted for the chart");
		line.setChartBullet(sbi);

	    }
	    if (display_semaphore && value.getValue() != null) {
		Color semaphorColor = null;
		List thresholds = value.getThresholds();
		Double val = new Double(value.getValue());
		semaphorColor = getSemaphorColor(thresholds, val);

		line.setSemaphorColor(semaphorColor);
	    }
	}

	List children = new ArrayList();
	List childrenIds = modI.getChildrenIds();
	if (!childrenIds.isEmpty()) {
	    Iterator childrenIt = childrenIds.iterator();
	    while (childrenIt.hasNext()) {
		Integer id = (Integer) childrenIt.next();
		KpiLine childrenLine = getBlock(id, r);
		children.add(childrenLine);
	    }
	}
	line.setChildren(children);
	logger.debug("OUT");
	return line;
    }


    public KpiValue getNewKpiValue(IDataSet dataSet, KpiInstance k, Resource r) throws EMFUserError, EMFInternalError {

	logger.debug("IN");
	logger.debug("START calculating the new KpiValue:");
	KpiValue kVal = new KpiValue();
	Date begD = dateOfKPI;
	Date endDate = null;
	kVal.setBeginDate(begD);
	logger.debug("Setted the KpiValue begin Date:"+begD);
	KpiInstance kpiInst = DAOFactory.getKpiDAO().loadKpiInstanceById(k.getKpiInstanceId());
	if(endKpiValueDate!=null){
		endDate = endKpiValueDate;
		kVal.setEndDate(endKpiValueDate);
	}else{
		Integer seconds = null;
		if(periodInstID!=null){
			kpiInst.setPeriodicityId(periodInstID);
			logger.debug("Setted new Periodicity ID:"+periodInstID.toString());
		}
		seconds = DAOFactory.getKpiDAO().getPeriodicitySeconds(kpiInst.getPeriodicityId());
			
		// Transforms seconds into milliseconds
		long milliSeconds = seconds.longValue() * 1000;
		long begDtTime = begD.getTime();
		long endTime = begDtTime + milliSeconds;
		endDate = new Date(endTime);
		kVal.setEndDate(endDate);
	}
	Integer kpiInstanceID = k.getKpiInstanceId();
	Double weight = null;
	Double target = null;
	String scaleCode = null;
	String scaleName = null;
	List thresholds = null;
	String chartType = null;
	logger.debug("Setted the KpiValue end Date:"+endDate);
	kVal.setKpiInstanceId(kpiInstanceID);
	logger.debug("Setted the KpiValue Instance ID:"+kpiInstanceID);
	Date kpiInstBegDt = kpiInst.getD();
	logger.debug("kpiInstBegDt begin date: "+(kpiInstBegDt!=null ? kpiInstBegDt.toString(): "Begin date null"));
	
	if ( (dateOfKPI.after(kpiInstBegDt)||dateOfKPI.equals(kpiInstBegDt))) {
		
		logger.debug("Requested date d: "+dateOfKPI.toString()+" in between beginDate and EndDate");
		weight = kpiInst.getWeight();
		logger.debug("SbiKpiValue weight: "+(weight!=null ? weight.toString() : "weight null"));
		target = kpiInst.getTarget();
		logger.debug("SbiKpiValue target: "+(target!=null ? target.toString() : "target null"));
		thresholds = DAOFactory.getKpiDAO().getThresholds(kpiInst);
		chartType = DAOFactory.getKpiDAO().getChartType(kpiInstanceID);

		scaleCode = kpiInst.getScaleCode();
		logger.debug("SbiKpiValue scaleCode: "+(scaleCode!=null ? scaleCode : "scaleCode null"));
		scaleName =kpiInst.getScaleName();
		logger.debug("SbiKpiValue scaleName: "+(scaleName!=null ? scaleName : "scaleName null"));

	} else {// in case older thresholds have to be retrieved

		kpiInst = DAOFactory.getKpiDAO().loadKpiInstanceByIdFromHistory(kpiInstanceID,dateOfKPI);
		if (kpiInst!=null){
			Integer chartId = kpiInst.getChartTypeId();
			Integer thresholdId = kpiInst.getThresholdId();
			thresholds = DAOFactory.getKpiDAO().loadThresholdsById(thresholdId);
			chartType = "BulletGraph";
			logger.debug("Requested date d: "+dateOfKPI.toString()+" in between beginDate and EndDate");
			weight = kpiInst.getWeight();
			logger.debug("SbiKpiValue weight: "+(weight!=null ? weight.toString() : "weight null"));
			target = kpiInst.getTarget();
			scaleCode = kpiInst.getScaleCode();
			logger.debug("SbiKpiValue scaleCode: "+(scaleCode!=null ? scaleCode : "scaleCode null"));
			scaleName =kpiInst.getScaleName();
			logger.debug("SbiKpiValue scaleName: "+(scaleName!=null ? scaleName : "scaleName null"));
		}
		
	}
	
	kVal.setWeight(k.getWeight());
	logger.debug("Setted the KpiValue weight:"+k.getWeight());	
	kVal.setThresholds(thresholds);
	logger.debug("Setted the KpiValue thresholds");	
	kVal.setScaleCode(scaleCode);
	logger.debug("Kpi value scale Code setted");
	kVal.setScaleName(scaleName);
	logger.debug("Kpi value scale Name setted");
	kVal.setTarget(target);
	logger.debug("Kpi value target setted");

	// If it has to be calculated for a Resource. The resource will be set
	// as parameter
	HashMap temp = (HashMap) this.parametersObject.clone();
	if (r != null) {
	    String colName = r.getColumn_name();
	    String value = r.getName();
	    kVal.setR(r);
	    logger.debug("Setted the Resource:"+r.getName());
	    temp.put(colName, value);
	}

	// If not, the dataset will be calculated without the parameter Resource
	// and the DataSet won't expect a parameter of type resource
	if(dataSet.hasBehaviour( QuerableBehaviour.class.getName()) ) {
		dataSet.setParamsMap(temp);
		dataSet.setUserProfile(profile);
	}
	
	dataSet.loadData();
	IDataStore dataStore = dataSet.getDataStore();
	logger.debug("Gotten the datastore");
	if (dataStore != null && !dataStore.isEmpty()) {
	    // Transform result into KPIValue (I suppose that the result has a
	    // unique value)
		IDataStoreMetaData d = dataStore.getMetaData();
		
		
	    IRecord record = dataStore.getRecordAt(0);
	    List fields = record.getFields();
	    if (fields != null && !fields.isEmpty()) {
	    	int length = fields.size();
	    	for(int fieldIndex =0;fieldIndex<length;fieldIndex++){
	    		IField f = (IField)fields.get(fieldIndex);
	    		if (f != null) {
	    			if (f.getValue() != null) {
		    			String fieldName = d.getFieldName(fieldIndex);	    			
		    			if (fieldName.equals("DESCR")){
		    				String descr = f.getValue().toString();
			    			kVal.setValueDescr(descr);
			    			logger.debug("Setted the kpiValue description:"+descr);
		    			}else if(fieldName.equals("END_DATE")){
		    				String endD = f.getValue().toString();
		    				String format = "dd/MM/yyyy hh:mm:ss";
	    					SimpleDateFormat form = new SimpleDateFormat();
	    					form.applyPattern(format);
	    				
	    					try {
								endDate = form.parse(endD);
							} catch (ParseException e) {
								e.printStackTrace();
							}
		    			   if(endDate!=null && endDate.after(begD)) {				 
			    			kVal.setEndDate(endDate);
			    			logger.debug("Setted the new EndDate description:"+endD.toString());
		    			   }
		    			}else{
		    				String fieldValue = f.getValue().toString();
			    			kVal.setValue(fieldValue);
			    			logger.debug("Setted the kpiValue value:"+fieldValue);
		    			}		    			
	    		    }
	    		}
	    	}		
	    }
	} else {
	    logger.warn("The Data Set doesn't return any value!!!!!");
	}
	if (chartType != null)
	    kVal.setChartType(chartType);
	logger.debug("OUT");
	return kVal;

    }
    
    /**
     * This function fills up the vector "intervals" with the intervals of the
     * chart, getting them from a list of Thresholds
     * 
     * @param List
     *                of thresholds to set
     * @return The Color of the interval in which the value falls
     */
    public Color getSemaphorColor(List thresholds, Double value) {
	logger.debug("IN");
	Color toReturn = null;
	if (thresholds != null && !thresholds.isEmpty()) {
	    Iterator it = thresholds.iterator();

	    while (it.hasNext()) {
		Threshold t = (Threshold) it.next();
		String type = t.getType();
		Double min = t.getMinValue();
		Double max = t.getMaxValue();

		if (type.equals("RANGE")) {
			logger.debug("Threshold type RANGE");
		    if (value.doubleValue() >= min.doubleValue() && value.doubleValue() <= max.doubleValue()) {
			String label = t.getLabel();
			toReturn = t.getColor();
		    }
		} else if (type.equals("MINIMUM")) {
			logger.debug("Threshold type MINIMUM");
		    if (value.doubleValue() < min.doubleValue()) {
			String label = t.getLabel();
			toReturn = t.getColor();
		    } else {
			toReturn = Color.WHITE;
		    }
		} else if (type.equals("MAXIMUM")) {
			logger.debug("Threshold type MAXIMUM");
		    if (value.doubleValue() > max.doubleValue()) {
			String label = t.getLabel();
			toReturn = t.getColor();
		    } else {
			toReturn = Color.WHITE;
		    }
		}
		logger.debug("New interval added to the Vector");
	    }
	}
	logger.debug("OUT");
	return toReturn;
    }
    
    private HashMap readParameters(List parametersList) throws EMFUserError {
    	logger.debug("IN");
    	if (parametersList == null) {
    	    logger.warn("parametersList si NULL!!!");
    	    return new HashMap();
    	}
    	HashMap parametersMap = new HashMap();
    	logger.debug("Check for BIparameters and relative values");

    	for (Iterator iterator = parametersList.iterator(); iterator.hasNext();) {
    	    BIObjectParameter par = (BIObjectParameter) iterator.next();
    	    String url = par.getParameterUrlName();
    	    List values = par.getParameterValues();
    	    if (values != null) {
    		if (values.size() == 1) {
    		    if (url.equals("ParKpiResources")) {
    				this.resources = new ArrayList();
    				String value = (String) values.get(0);
    				Integer res = new Integer(value);
    				Resource toAdd = DAOFactory.getKpiDAO().loadResourceById(res);
    				this.resources.add(toAdd);
    		    
    		    }else {
    			    String value = (String) values.get(0);
    			    parametersMap.put(url, value);
    			    if (url.equals("ParKpiDate")) {
    					SourceBean formatSB = ((SourceBean) ConfigSingleton.getInstance().getAttribute(
    						"SPAGOBI.DATE-FORMAT"));
    					String format = (String) formatSB.getAttribute("format");
    					SimpleDateFormat f = new SimpleDateFormat();
    					f.applyPattern(format);
    					String temp = f.format(this.dateOfKPI);
    				
    					try {
    					    this.dateOfKPI = f.parse(value);
    					    Long milliseconds = this.dateOfKPI.getTime();
    					    //If the date required is today then the time considered will be the actual date
    						if(temp.equals(value)){
    							Calendar calendar = new GregorianCalendar();
    							int ore = calendar.get(Calendar.HOUR); 
    							int minuti = calendar.get(Calendar.MINUTE); 
    							int secondi = calendar.get(Calendar.SECOND); 
    							int AM = calendar.get(Calendar.AM_PM);//if AM then int=0, if PM then int=1
    							if(AM==0){
    								int millisec =  (secondi*1000) + (minuti *60*1000) + (ore*60*60*1000);
        							Long milliSecToAdd = new Long (millisec);
        							milliseconds = new Long(milliseconds.longValue()+milliSecToAdd.longValue());
        							this.dateOfKPI = new Date(milliseconds);
    							}else{
    								int millisec =  (secondi*1000) + (minuti *60*1000) + ((ore+12)*60*60*1000);
        							Long milliSecToAdd = new Long (millisec);
        							milliseconds = new Long(milliseconds.longValue()+milliSecToAdd.longValue());
        							this.dateOfKPI = new Date(milliseconds);
    							}    							
    							
    						}else{
    							//If the date required is today then the time considered will be the actual date
    							Long milliSecToAdd = new Long (60*60*24);
    							milliseconds = new Long(milliseconds.longValue()+milliSecToAdd.longValue());
    							this.dateOfKPI = new Date(milliseconds);
    						}
    					   
    					} catch (ParseException e) {
    					    logger.error("ParseException.value=" + value, e);
    					}
    			    }
    		     }   
    		   }else if (values != null && values.size() >= 1) {
    			if (url.equals("ParKpiResources")) {
    			    this.resources = new ArrayList();
    			    for (int k = 0; k < values.size(); k++) {
    				String value = (String) values.get(k);
    				Integer res = new Integer(value);
    				Resource toAdd = DAOFactory.getKpiDAO().loadResourceById(res);
    				this.resources.add(toAdd);
    			    }
    			} else {
    			    String value = "'" + (String) values.get(0) + "'";
    			    for (int k = 1; k < values.size(); k++) {
    			    	value = value + ",'" + (String) values.get(k) + "'";
    			    }
    			    parametersMap.put(url, value);
    			    if (url.equals("ParKpiDate")) {
    					SourceBean formatSB = ((SourceBean) ConfigSingleton.getInstance().getAttribute(
    						"SPAGOBI.DATE-FORMAT"));
    					String format = (String) formatSB.getAttribute("format");
    					SimpleDateFormat f = new SimpleDateFormat();
    					f.applyPattern(format);
    					String temp = f.format(this.dateOfKPI);
    					try {
    						this.dateOfKPI = f.parse(value);
    					    Long milliseconds = this.dateOfKPI.getTime();
    					    //If the date required is today then the time considered will be the actual date
    						if(temp.equals(value)){
    							Calendar calendar = new GregorianCalendar();
    							int ore = calendar.get(Calendar.HOUR); 
    							int minuti = calendar.get(Calendar.MINUTE); 
    							int secondi = calendar.get(Calendar.SECOND); 
    							int AM = calendar.get(Calendar.AM_PM);//if AM then int=0, if PM then int=1
    							if(AM==0){
    								int millisec =  (secondi*1000) + (minuti *60*1000) + (ore*60*60*1000);
        							Long milliSecToAdd = new Long (millisec);
        							milliseconds = new Long(milliseconds.longValue()+milliSecToAdd.longValue());
        							this.dateOfKPI = new Date(milliseconds);
    							}else{
    								int millisec =  (secondi*1000) + (minuti *60*1000) + ((ore+12)*60*60*1000);
        							Long milliSecToAdd = new Long (millisec);
        							milliseconds = new Long(milliseconds.longValue()+milliSecToAdd.longValue());
        							this.dateOfKPI = new Date(milliseconds);
    							}    							
    							
    						}else{
    							//If the date required is today then the time considered will be the actual date
    							Long milliSecToAdd = new Long (60*60*24);
    							milliseconds = new Long(milliseconds.longValue()+milliSecToAdd.longValue());
    							this.dateOfKPI = new Date(milliseconds);
    						}
    					} catch (ParseException e) {
    					    logger.error("ParseException.value=" + value, e);
    					}
    			    }
    			}
    	    }
    	}
    	}
    	logger.debug("OUT. Date:" + this.dateOfKPI);
    	return parametersMap;

        }

    public void getSetConf(SourceBean content) {
	logger.debug("IN");
	this.confMap = new HashMap();
	// common part for all charts
	// setting the title with parameter values if is necessary
	if (content.getAttribute("name") != null) {
	    String titleChart = (String) content.getAttribute("name");
	    String tmpTitle = titleChart;
	    while (!tmpTitle.equals("")) {
		if (tmpTitle.indexOf("$P{") >= 0) {
		    String parName = tmpTitle.substring(tmpTitle.indexOf("$P{") + 3, tmpTitle.indexOf("}"));

		    String parValue = (parametersObject.get(parName) == null) ? "" : (String) parametersObject
			    .get(parName);
		    parValue = parValue.replaceAll("\'", "");

		    if (parValue.equals("%"))
			parValue = "";
		    int pos = tmpTitle.indexOf("$P{" + parName + "}") + (parName.length() + 4);
		    titleChart = titleChart.replace("$P{" + parName + "}", parValue);
		    tmpTitle = tmpTitle.substring(pos);
		} else
		    tmpTitle = "";
	    }
	    setName(titleChart);
	} else
	    setName("");

	SourceBean styleTitleSB = (SourceBean) content.getAttribute("STYLE_TITLE");
	if (styleTitleSB != null) {

	    String fontS = (String) content.getAttribute("STYLE_TITLE.font");
	    String sizeS = (String) content.getAttribute("STYLE_TITLE.size");
	    String colorS = (String) content.getAttribute("STYLE_TITLE.color");

	    try {
		Color color = Color.decode(colorS);
		int size = Integer.valueOf(sizeS).intValue();
		styleTitle = new StyleLabel(fontS, size, color);

	    } catch (Exception e) {
		logger.error("Wrong style Title settings, use default");
	    }

	} else {
	    styleTitle = new StyleLabel("Arial", 16, new Color(255, 0, 0));
	}
	this.confMap.put("styleTitle", styleTitle);

	SourceBean styleSubTitleSB = (SourceBean) content.getAttribute("STYLE_SUBTITLE");
	if (styleSubTitleSB != null) {

	    String subTitle = (String) content.getAttribute("STYLE_SUBTITLE.name");
	    if (subTitle != null) {
		String tmpSubTitle = subTitle;
		while (!tmpSubTitle.equals("")) {
		    if (tmpSubTitle.indexOf("$P{") >= 0) {
			String parName = tmpSubTitle
				.substring(tmpSubTitle.indexOf("$P{") + 3, tmpSubTitle.indexOf("}"));
			String parValue = (parametersObject.get(parName) == null) ? "" : (String) parametersObject
				.get(parName);
			parValue = parValue.replaceAll("\'", "");
			if (parValue.equals("%"))
			    parValue = "";
			int pos = tmpSubTitle.indexOf("$P{" + parName + "}") + (parName.length() + 4);
			subTitle = subTitle.replace("$P{" + parName + "}", parValue);
			tmpSubTitle = tmpSubTitle.substring(pos);
		    } else
			tmpSubTitle = "";
		}
		setSubName(subTitle);

	    } else
		setSubName("");

	    String fontS = (String) content.getAttribute("STYLE_SUBTITLE.font");
	    String sizeS = (String) content.getAttribute("STYLE_SUBTITLE.size");
	    String colorS = (String) content.getAttribute("STYLE_SUBTITLE.color");

	    try {
		Color color = Color.decode(colorS);
		int size = Integer.valueOf(sizeS).intValue();
		styleSubTitle = new StyleLabel(fontS, size, color);
	    } catch (Exception e) {
		logger.error("Wrong style SubTitle settings, use default");
	    }

	} else {
	    styleSubTitle = new StyleLabel("Arial", 12, new Color(0, 0, 0));
	}
	this.confMap.put("styleSubTitle", styleSubTitle);

	// get all the data parameters
	try {
	    Map dataParameters = new HashMap();
	    SourceBean dataSB = (SourceBean) content.getAttribute("CONF");
	    List dataAttrsList = dataSB.getContainedSourceBeanAttributes();
	    Iterator dataAttrsIter = dataAttrsList.iterator();
	    while (dataAttrsIter.hasNext()) {
		SourceBeanAttribute paramSBA = (SourceBeanAttribute) dataAttrsIter.next();
		SourceBean param = (SourceBean) paramSBA.getValue();
		String nameParam = (String) param.getAttribute("name");
		String valueParam = (String) param.getAttribute("value");
		dataParameters.put(nameParam, valueParam);
	    }

	    display_semaphore = true;
	    if (dataParameters.get("display_semaphore") != null
		    && !(((String) dataParameters.get("display_semaphore")).equalsIgnoreCase(""))) {
		String leg = (String) dataParameters.get("display_semaphore");
		if (leg.equalsIgnoreCase("false"))
		    display_semaphore = false;
	    }
	    this.confMap.put("display_semaphore", display_semaphore);

	    display_bullet_chart = true;
	    if (dataParameters.get("display_bullet_chart") != null
		    && !(((String) dataParameters.get("display_bullet_chart")).equalsIgnoreCase(""))) {
		String fil = (String) dataParameters.get("display_bullet_chart");
		if (fil.equalsIgnoreCase("false"))
		    display_bullet_chart = false;
	    }
	    this.confMap.put("display_bullet_chart", display_bullet_chart);

	    display_weight = true;
	    if (dataParameters.get("display_weight") != null
		    && !(((String) dataParameters.get("display_weight")).equalsIgnoreCase(""))) {
		String fil = (String) dataParameters.get("display_weight");
		if (fil.equalsIgnoreCase("false"))
		    display_weight = false;
	    }
	    this.confMap.put("display_weight", display_weight);

	    display_alarm = true;
	    if (dataParameters.get("display_alarm") != null
		    && !(((String) dataParameters.get("display_alarm")).equalsIgnoreCase(""))) {
		String fil = (String) dataParameters.get("display_alarm");
		if (fil.equalsIgnoreCase("false"))
		    display_alarm = false;
	    }
	    this.confMap.put("display_alarm", display_alarm);
	    
	    register_values = true;
	    if (dataParameters.get("register_values") != null
		    && !(((String) dataParameters.get("register_values")).equalsIgnoreCase(""))) {
		String fil = (String) dataParameters.get("register_values");
		if (fil.equalsIgnoreCase("false"))
			register_values = false;
	    }
	    this.confMap.put("register_values", register_values);

	} catch (Exception e) {
	    logger.error("error in reading data source parameters");
	}
    }

    /**
     * The <code>SpagoBIDashboardInternalEngine</code> cannot manage
     * subobjects so this method must not be invoked.
     * 
     * @param requestContainer
     *                The <code>RequestContainer</code> object (the session
     *                can be retrieved from this object)
     * @param obj
     *                The <code>BIObject</code> representing the document
     * @param response
     *                The response <code>SourceBean</code> to be populated
     * @param subObjectInfo
     *                An object describing the subobject to be executed
     * 
     * @throws EMFUserError
     *                 the EMF user error
     */
    public void executeSubObject(RequestContainer requestContainer, BIObject obj, SourceBean response,
	    Object subObjectInfo) throws EMFUserError {
	// it cannot be invoked
	logger.error("SpagoBIDashboardInternalEngine cannot exec subobjects.");
	throw new EMFUserError(EMFErrorSeverity.ERROR, "101", messageBundle);
    }

    public void setName(String _name) {
	name = _name;
    }

    public void setSubName(String _name) {
	subName = _name;
    }

    /**
     * Function not implemented. Thid method should not be called
     * 
     * @param requestContainer
     *                The <code>RequestContainer</code> object (the session
     *                can be retrieved from this object)
     * @param response
     *                The response <code>SourceBean</code> to be populated
     * @param obj
     *                the obj
     * 
     * @throws InvalidOperationRequest
     *                 the invalid operation request
     * @throws EMFUserError
     *                 the EMF user error
     */
    public void handleNewDocumentTemplateCreation(RequestContainer requestContainer, BIObject obj, SourceBean response)
	    throws EMFUserError, InvalidOperationRequest {
	logger.error("SpagoBIDashboardInternalEngine cannot build document template.");
	throw new InvalidOperationRequest();

    }

    /**
     * Function not implemented. Thid method should not be called
     * 
     * @param requestContainer
     *                The <code>RequestContainer</code> object (the session
     *                can be retrieved from this object)
     * @param response
     *                The response <code>SourceBean</code> to be populated
     * @param obj
     *                the obj
     * 
     * @throws InvalidOperationRequest
     *                 the invalid operation request
     * @throws EMFUserError
     *                 the EMF user error
     */
    public void handleDocumentTemplateEdit(RequestContainer requestContainer, BIObject obj, SourceBean response)
	    throws EMFUserError, InvalidOperationRequest {
	logger.error("SpagoBIDashboardInternalEngine cannot build document template.");
	throw new InvalidOperationRequest();
    }

}
