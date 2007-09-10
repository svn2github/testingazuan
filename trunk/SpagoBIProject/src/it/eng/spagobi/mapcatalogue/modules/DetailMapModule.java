/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.mapcatalogue.modules;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.module.AbstractHttpModule;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spago.validation.EMFValidationError;
import it.eng.spago.validation.coordinator.ValidationCoordinator;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.mapcatalogue.bo.GeoFeature;
import it.eng.spagobi.mapcatalogue.bo.GeoMap;
import it.eng.spagobi.mapcatalogue.bo.GeoMapFeature;
import it.eng.spagobi.mapcatalogue.bo.dao.DAOFactory;
import it.eng.spagobi.utilities.ChannelUtilities;
import it.eng.spagobi.utilities.PortletUtilities;
import it.eng.spagobi.utilities.UploadedFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletOutputStream;

/**
 * Spago Module which executes the map producing request  
 */
public class DetailMapModule extends AbstractHttpModule {
	public static final String MODULE_PAGE = "DetailMapPage";
	public static final String MOD_SAVE = "SAVE";
	public static final String MOD_SAVEBACK = "SAVEBACK";
	public static final String MOD_NO_SAVE = "NO_SAVE";
	public static final String MOD_GET_TAB_DETAIL = "GET_TAB_DETAIL";
	public static final String MOD_DEL_MAP_FEATURE = "DEL_MAP_FEATURE";
	public static final String MOD_RETURN_FROM_LOOKUP = "RETURN_FROM_LOOKUP";
	public static final String MOD_DOWNLOAD_MAP = "DOWNLOAD_MAP";
	
	
	
	private String modalita = "";
	
	
	/**
	 * Method called automatically by Spago framework when the action is invoked.
	 * The method search into the request two parameters
	 * <ul>
	 * <li>message: a message which contains the type of the request</li>
	 * </ul>
	 * 
	 * @param serviceRequest the Spago request SourceBean 
	 * @param serviceResponse the Spago response SourceBean 
	 */
	public void service(SourceBean serviceRequest, SourceBean serviceResponse) throws Exception {		
		
		EMFErrorHandler errorHandler = getErrorHandler();
		
		if(ChannelUtilities.isPortletRunning()){
			if(PortletUtilities.isMultipartRequest()) {
				serviceRequest = ChannelUtilities.getSpagoRequestFromMultipart();
				fillRequestContainer(serviceRequest, errorHandler);		
			}
		}
		String message = (String) serviceRequest.getAttribute("MESSAGEDET");
		TracerSingleton.log(SpagoBIConstants.NAME_MODULE, TracerSingleton.DEBUG,  "begin of detail Map modify/visualization service with message =" + message);
		
		try {
			if (message == null) {
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 101);				
				TracerSingleton.log(SpagoBIConstants.NAME_MODULE, TracerSingleton.DEBUG,  "The message parameter is null");
				throw userError;
			}
			if (message.trim().equalsIgnoreCase(SpagoBIConstants.DETAIL_INS) || 
				message.trim().equalsIgnoreCase( SpagoBIConstants.DETAIL_MOD)){	
				ValidationCoordinator.validate("PAGE", "DetailMapPost", this);
			}
			if (message.trim().equalsIgnoreCase(SpagoBIConstants.DETAIL_SELECT)) {
				getDetailMap(serviceRequest, serviceResponse);
			} else if (message.trim().equalsIgnoreCase(SpagoBIConstants.DETAIL_MOD)) {
				modDetailMap(serviceRequest, SpagoBIConstants.DETAIL_MOD, serviceResponse);
			} else if (message.trim().equalsIgnoreCase(SpagoBIConstants.DETAIL_NEW)) {
				newDetailMap(serviceResponse);
			} else if (message.trim().equalsIgnoreCase(SpagoBIConstants.DETAIL_INS)) {
				modDetailMap(serviceRequest, SpagoBIConstants.DETAIL_INS, serviceResponse);
			} else if (message.trim().equalsIgnoreCase(SpagoBIConstants.DETAIL_DEL)) {
				delDetailMap(serviceRequest, SpagoBIConstants.DETAIL_DEL, serviceResponse);
			} else if (message.trim().equalsIgnoreCase(MOD_DEL_MAP_FEATURE)) {
				delRelMapFeature(serviceRequest, serviceResponse);				
			} else if (message.trim().equalsIgnoreCase(MOD_RETURN_FROM_LOOKUP)) {
				insRelMapFeature(serviceRequest, serviceResponse);				
			}
		} catch (EMFUserError eex) {
			errorHandler.addError(eex);
			return;
		} catch (Exception ex) {
			EMFInternalError internalError = new EMFInternalError(EMFErrorSeverity.ERROR, ex);
			errorHandler.addError(internalError);
			return;
		}
	}
	
	
	/**
	 * sends an error message to the client
	 * @param out The servlet output stream
	 */
	private void sendError(ServletOutputStream out)  {
		try{
			out.write("<html>".getBytes());
			out.write("<body>".getBytes());
			out.write("<br/><br/><center><h2><span style=\"color:red;\">Unable to produce map</span></h2></center>".getBytes());
			out.write("</body>".getBytes());
			out.write("</html>".getBytes());
		} catch (Exception e) {
			TracerSingleton.log(SpagoBIConstants.NAME_MODULE, TracerSingleton.MAJOR, 
								"GeoAction :: sendError : " +
								"Unable to write into output stream ", e);
		}
	}
	

/**
 * Gets the detail of an map choosed by the user from the 
 * maps list. It reaches the key from the request and asks to the DB all detail
 * map information, by calling the method <code>loadMapByID</code>.
 *   
 * @param key The choosed map id key
 * @param response The response Source Bean
 * @throws EMFUserError If an exception occurs
 */   
private void getDetailMap(SourceBean request, SourceBean response) throws EMFUserError {
	try {		 									
		GeoMap map = DAOFactory.getSbiGeoMapsDAO().loadMapByID(new Integer((String)request.getAttribute("ID")));		
		getTabDetails(request, response);		
		this.modalita = SpagoBIConstants.DETAIL_MOD;
		if (request.getAttribute("SUBMESSAGEDET") != null &&
			((String)request.getAttribute("SUBMESSAGEDET")).equalsIgnoreCase(MOD_SAVEBACK))
		{
			response.setAttribute("loopback", "true");
			return;
		}
		response.setAttribute("modality", modalita);
		response.setAttribute("mapObj", map);
	} catch (Exception ex) {
		TracerSingleton.log(SpagoBIConstants.NAME_MODULE, TracerSingleton.MAJOR, "Cannot fill response container" + ex.getLocalizedMessage());	
		HashMap params = new HashMap();
		params.put(AdmintoolsConstants.PAGE, ListMapsModule.MODULE_PAGE);
		throw new EMFUserError(EMFErrorSeverity.ERROR, 5011, new Vector(), params);
	}
}
 /**
 * Inserts/Modifies the detail of an map according to the user request. 
 * When a map is modified, the <code>modifyMap</code> method is called; when a new
 * map is added, the <code>insertMap</code>method is called. These two cases are 
 * differentiated by the <code>mod</code> String input value .
 * 
 * @param request The request information contained in a SourceBean Object
 * @param mod A request string used to differentiate insert/modify operations
 * @param response The response SourceBean 
 * @throws EMFUserError If an exception occurs
 * @throws SourceBeanException If a SourceBean exception occurs
 */
private void modDetailMap(SourceBean serviceRequest, String mod, SourceBean serviceResponse)
	throws EMFUserError, SourceBeanException {
	
	try {
		
		GeoMap mapNew = recoverMapDetails(serviceRequest);
		
		EMFErrorHandler errorHandler = getErrorHandler();
		 
		// if there are some validation errors into the errorHandler does not write into DB
		Collection errors = errorHandler.getErrors();
		if (errors != null && errors.size() > 0) {
			Iterator iterator = errors.iterator();
			while (iterator.hasNext()) {
				Object error = iterator.next();
				if (error instanceof EMFValidationError) {
					serviceResponse.setAttribute("mapObj", mapNew);
					serviceResponse.setAttribute("modality", mod);
					return;
				}
			}
		}
		
		if (mod.equalsIgnoreCase(SpagoBIConstants.DETAIL_INS)) {			
			//if a map with the same name not exists on db ok else error
			if (DAOFactory.getSbiGeoMapsDAO().loadMapByName(mapNew.getName()) != null){
				HashMap params = new HashMap();
				params.put(AdmintoolsConstants.PAGE, ListMapsModule.MODULE_PAGE);
				EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, 5005, new Vector(), params );
				getErrorHandler().addError(error);
				return;
			}	 		
			/* The activity INSERT consists in:
			 * - insert a map (SBI_GEO_MAPS), 
			 * - insert of the features (SBI_GEO_FEATURES) through the 	method 'loadUpdateMapFeatures'
			 * - insert of the relations (SBI_GEO_MAP_FEATURES) through the method 'loadUpdateMapFeatures'
			 * (all objects are had taken from the template file)
			 */
			DAOFactory.getSbiGeoMapsDAO().insertMap(mapNew);
			loadUpdateMapFeatures(mapNew);
			GeoMap tmpMap = DAOFactory.getSbiGeoMapsDAO().loadMapByName(mapNew.getName());
			mapNew.setMapId(tmpMap.getMapId());
			serviceResponse.setAttribute("mapObj", mapNew);
			serviceResponse.setAttribute("modality", SpagoBIConstants.DETAIL_MOD);
			
			getTabDetails(serviceRequest, serviceResponse);
			
			if (((String)serviceRequest.getAttribute("SUBMESSAGEDET")).equalsIgnoreCase(MOD_SAVEBACK))
			{
				serviceResponse.setAttribute("loopback", "true");
				return;
			}
			
			return;

		} else {
			/* The activity UPDATE consists in:
			 * - update of a map (SBI_GEO_MAPS), 
			 * - update of the relations (SBI_GEO_MAP_FEATURES) through the method 'loadUpdateMapFeatures', eventually if 
			 *   there are new features those will be inserted. 
			 * (all objects had taken from the template file)
			 */
			List lstOldFeatures = DAOFactory.getSbiGeoMapFeaturesDAO().loadFeaturesByMapId(new Integer(mapNew.getMapId()));
			//update map
			DAOFactory.getSbiGeoMapsDAO().modifyMap(mapNew);			
			//update features
			List lstNewFeatures = loadUpdateMapFeatures(mapNew);			
			 // If in the new file svg there aren't more some feature, the user can choose if erase theme relations or not.
			List lstFeaturesDel = new ArrayList();
			
			for (int i=0; i<lstOldFeatures.size(); i++){					
				if (!(lstNewFeatures.contains(((GeoFeature)lstOldFeatures.get(i)).getName())))
					lstFeaturesDel.add(((GeoFeature)lstOldFeatures.get(i)).getName());
			}
			if (lstFeaturesDel.size() > 0){
				serviceResponse.setAttribute("lstFeaturesOld",lstFeaturesDel);
				serviceResponse.setAttribute("SUBMESSAGEDET", ((String)serviceRequest.getAttribute("SUBMESSAGEDET")));
				getTabDetails(serviceRequest, serviceResponse);			
				serviceResponse.setAttribute("modality", mod);
				serviceResponse.setAttribute("mapObj", mapNew);					
				return;										
			}				
		}  
		if (serviceRequest.getAttribute("SUBMESSAGEDET") != null && 
			((String)serviceRequest.getAttribute("SUBMESSAGEDET")).equalsIgnoreCase(MOD_SAVE)) {			
			getTabDetails(serviceRequest, serviceResponse);			
			serviceResponse.setAttribute("modality", mod);
			serviceResponse.setAttribute("mapObj", mapNew);				
			return;
		}
		else if (serviceRequest.getAttribute("SUBMESSAGEDET") != null && 
				((String)serviceRequest.getAttribute("SUBMESSAGEDET")).equalsIgnoreCase(MOD_SAVEBACK)){
				serviceResponse.setAttribute("loopback", "true");
			    return;
		}					     
	} catch (EMFUserError e){
		HashMap params = new HashMap();
		params.put(AdmintoolsConstants.PAGE, ListMapsModule.MODULE_PAGE);
		throw new EMFUserError(EMFErrorSeverity.ERROR, 5009, new Vector(), params);
		
	}
	
	catch (Exception ex) {		
		TracerSingleton.log(SpagoBIConstants.NAME_MODULE, TracerSingleton.MAJOR, "Cannot fill response container" + ex.getLocalizedMessage());		
		throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
	}	
}

/**
 * Deletes a map choosed by user from the maps list.
 * 
 * @param request	The request SourceBean
 * @param mod	A request string used to differentiate delete operation
 * @param response	The response SourceBean
 * @throws EMFUserError	If an Exception occurs
 * @throws SourceBeanException If a SourceBean Exception occurs
 */
private void delDetailMap(SourceBean request, String mod, SourceBean response)
	throws EMFUserError, SourceBeanException {
	
	try {
		String id = (String) request.getAttribute("ID");
//		if the map is associated with any BIFeautures, delete before this associations and then delete the map
		List lstMapFeatures =  DAOFactory.getSbiGeoMapFeaturesDAO().loadFeaturesByMapId(new Integer(id));
		if (lstMapFeatures != null){
			for (int i=0; i<lstMapFeatures.size();i++){
				int featureId = ((GeoFeature)lstMapFeatures.get(i)).getFeatureId();
				GeoMapFeature tmpMapFeature = DAOFactory.getSbiGeoMapFeaturesDAO().loadMapFeatures(new Integer(id), new Integer(featureId));				
				DAOFactory.getSbiGeoMapFeaturesDAO().eraseMapFeatures(tmpMapFeature);
			}
		}
		//delete the map
		GeoMap map = DAOFactory.getSbiGeoMapsDAO().loadMapByID(new Integer(id));
		DAOFactory.getSbiGeoMapsDAO().eraseMap(map);
	}   catch (EMFUserError e){
		  HashMap params = new HashMap();		  
		  params.put(AdmintoolsConstants.PAGE, ListMapsModule.MODULE_PAGE);
		  throw new EMFUserError(EMFErrorSeverity.ERROR, 5010, new Vector(), params);
			
		}
	    catch (Exception ex) {		
	    ex.printStackTrace();
		TracerSingleton.log(SpagoBIConstants.NAME_MODULE, TracerSingleton.MAJOR, "Cannot fill response container" + ex.getLocalizedMessage());
		throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
	}
	response.setAttribute("loopback", "true");	
}

/**
 * Inserts a relation between the map and the feature selected
 * 
 * @param request	The request SourceBean
 * @param mod	A request string used to differentiate delete operation
 * @param response	The response SourceBean
 * @throws EMFUserError	If an Exception occurs
 * @throws SourceBeanException If a SourceBean Exception occurs
 */
private void insRelMapFeature(SourceBean request, SourceBean response)
	throws EMFUserError, SourceBeanException {
	
	try {		
		String mapId = (String) request.getAttribute("MAP_ID");
		String featureId = (String)request.getAttribute("FEATURE_ID");	
		GeoMap map = DAOFactory.getSbiGeoMapsDAO().loadMapByID(new Integer(mapId));
		EMFErrorHandler errorHandler = getErrorHandler();
		
		// if there are some validation errors into the errorHandler does not write into DB
		Collection errors = errorHandler.getErrors();
		if (errors != null && errors.size() > 0) {
			Iterator iterator = errors.iterator();
			while (iterator.hasNext()) {
				Object error = iterator.next();
				if (error instanceof EMFValidationError) {
					response.setAttribute("mapObj", map);
					response.setAttribute("modality", SpagoBIConstants.DETAIL_MOD);
					return;
				}
			}
		}

		//inserts the relation
		GeoMapFeature mapFeature = DAOFactory.getSbiGeoMapFeaturesDAO().loadMapFeatures(new Integer(mapId), new Integer(featureId));
		if (mapFeature == null){
			mapFeature = new  GeoMapFeature();
			mapFeature.setMapId(new Integer(mapId).intValue());
			mapFeature.setFeatureId(new Integer(featureId).intValue());
			mapFeature.setSvgGroup(null);
			mapFeature.setVisibleFlag(null);		
			DAOFactory.getSbiGeoMapFeaturesDAO().insertMapFeatures(mapFeature);
		}
		//create a List of features
		List lstAllFeatures = DAOFactory.getSbiGeoMapFeaturesDAO().loadFeatureNamesByMapId(new Integer(map.getMapId()));
		List lstMapFeatures = new ArrayList();

		for (int i=0; i < lstAllFeatures.size(); i ++){			
			GeoFeature aFeature = DAOFactory.getSbiGeoFeaturesDAO().loadFeatureByName((String)lstAllFeatures.get(i));
			lstMapFeatures.add(aFeature);
			//for the first time sets selectedFeatureId with the first feature
			if (i==0)
				featureId = String.valueOf(aFeature.getFeatureId());
		
		}

	    response.setAttribute("lstMapFeatures",lstMapFeatures);
	    response.setAttribute("selectedFeatureId",featureId);	
	    response.setAttribute("mapObj", map);
	    response.setAttribute("modality", SpagoBIConstants.DETAIL_MOD);
	    
	}   catch (EMFUserError e){
		  HashMap params = new HashMap();		  
		  params.put(AdmintoolsConstants.PAGE, ListMapsModule.MODULE_PAGE);
		  throw new EMFUserError(EMFErrorSeverity.ERROR, 5027, new Vector(), params);
			
		}
	    catch (Exception ex) {		
	    ex.printStackTrace();
		TracerSingleton.log(SpagoBIConstants.NAME_MODULE, TracerSingleton.MAJOR, "Cannot fill response container" + ex.getLocalizedMessage());
		throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
	}	
}

/**
 * Instantiates a new <code>map<code> object when a new map insertion is required, in order
 * to prepare the page for the insertion.
 * 
 * @param response The response SourceBean
 * @throws EMFUserError If an Exception occurred
 */

private void newDetailMap(SourceBean response) throws EMFUserError {
	
	try {
		
		GeoMap map = null;
		this.modalita = SpagoBIConstants.DETAIL_INS;
		response.setAttribute("modality", modalita);
		map = new GeoMap();
		map.setMapId(-1);
		map.setDescr("");
		map.setName("");
		map.setUrl("");
		map.setFormat("");
		response.setAttribute("mapObj", map);
	} catch (Exception ex) {
		TracerSingleton.log(SpagoBIConstants.NAME_MODULE, TracerSingleton.MAJOR, "Cannot prepare page for the insertion" + ex.getLocalizedMessage());		
		throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
	}
	
}


private GeoMap recoverMapDetails (SourceBean serviceRequest) throws EMFUserError, SourceBeanException, IOException  {
	GeoMap map  = new GeoMap();
	
	String idStr = (String)serviceRequest.getAttribute("ID");
	Integer id = new Integer(idStr);
	String description = (String)serviceRequest.getAttribute("DESCR");	
	String name = (String)serviceRequest.getAttribute("NAME");
	String format = (String)serviceRequest.getAttribute("FORMAT");
	String url = null;
	//gets the file eventually uploaded
	UploadedFile uploaded = (UploadedFile) serviceRequest.getAttribute("UPLOADED_FILE");
    String fileName = null;
    if(uploaded!=null) {
    	fileName = uploaded.getFileName();
    }
	
	if (fileName != null && !fileName.equals("")){		
	    if (name == null || name.equals("") ||
	    	fileName == null || fileName.equals("")) return map;
	    
		File fileDir = new File(ConfigSingleton.getRootPath()+"//components//mapcatalogue//maps");	
		if(!(fileDir).exists()) fileDir.mkdirs();	   
		FileOutputStream tmpFile = new FileOutputStream(fileDir + "//"+fileName);		
	    
	    try {
	    	tmpFile.write(uploaded.getFileContent());
		} catch (Exception e) {
			e.printStackTrace();
		}		
	
		url = fileDir.getPath().substring(ConfigSingleton.getRootPath().length())+"\\"+fileName;
	}
	else
		url = (String)serviceRequest.getAttribute("sourceUrl");
	
	map.setMapId(id.intValue());
	map.setName(name);
	map.setDescr(description);
	map.setFormat(format);
	map.setUrl(url);
	
	return map;
	}
		
	/**
	 * Fills the request container object with Map information contained into
	 * the request Source Bean (they are all attributes). It is useful for validation process.
	 * 
	 * @param request The request Source Bean 
	 * @throws SourceBeanException If any exception occurred
	 */
	public void fillRequestContainer (SourceBean request, EMFErrorHandler errorHandler) throws Exception{
		RequestContainer req = getRequestContainer();
		String name = (String)request.getAttribute("NAME");
		String description = (String)request.getAttribute("DESCR");
		String format = (String)request.getAttribute("FORMAT");
		String url = null;
		UploadedFile uploaded = (UploadedFile) request.getAttribute("UPLOADED_FILE");
		TracerSingleton.log(SpagoBIConstants.NAME_MODULE, TracerSingleton.DEBUG, "uploaded: " + uploaded );
		if (uploaded != null)
		    url = uploaded.getFileName();
		
		if (url == null || url.equals(""))
			url = (String)request.getAttribute("sourceUrl");
		
		SourceBean _serviceRequest = req.getServiceRequest();
		if(_serviceRequest.getAttribute("DESR")==null)
			_serviceRequest.setAttribute("DESR",description == null ? "" : description);
		if(_serviceRequest.getAttribute("NAME")==null)
			_serviceRequest.setAttribute("NAME",name == null ? "": name);
		if(_serviceRequest.getAttribute("URL")==null)
			_serviceRequest.setAttribute("URL", url == null ? "" : url);
		if(_serviceRequest.getAttribute("FORMAT")==null)
			_serviceRequest.setAttribute("FORMAT", format == null ? "" : format);
	}
	
	private List loadUpdateMapFeatures(GeoMap mapNew) throws EMFUserError, Exception {
		try {
	//		through the url of a map, gets and opens the svg file and inserts a feature for every tag <g>
			GeoFeature feature = null;
			List lstHashFeatures = DAOFactory.getSbiGeoMapsDAO().getFeaturesFromSVG(mapNew.getUrl());		
			List lstFeatures = new ArrayList();
			int mapId;
			int featureId;
			for (int i=0; i < lstHashFeatures.size(); i++){				
				HashMap hFeature = (HashMap)lstHashFeatures.get(i);
				//checks if a feature with the same name yet exists in db 
				//NB: the field "id" of hashmap [hFeature] contains the name of the feature)
				feature = DAOFactory.getSbiGeoFeaturesDAO().loadFeatureByName((String)hFeature.get("id"));
				 
				if (feature == null || feature.getFeatureId() == 0) {
					feature = new GeoFeature();
					feature.setName((String)hFeature.get("id"));
					feature.setDescr((String)hFeature.get("descr"));
					feature.setType((String)hFeature.get("type"));
					DAOFactory.getSbiGeoFeaturesDAO().insertFeature(feature);					 				
				}
				lstFeatures.add(feature.getName());
	//			for every map/feature inserts a row in join table (SBI_GEO_MAP_FEATURES)							
				//gets map_id
				mapId = mapNew.getMapId();
				if (mapId == -1 )
					mapId = ((GeoMap)DAOFactory.getSbiGeoMapsDAO().loadMapByName(mapNew.getName())).getMapId();
				
				//gets feature id				
				if (feature.getFeatureId() == 0)
					feature = DAOFactory.getSbiGeoFeaturesDAO().loadFeatureByName((String)hFeature.get("id"));
				
				featureId = feature.getFeatureId();
				//gets relation
				GeoMapFeature mapFeature = DAOFactory.getSbiGeoMapFeaturesDAO().loadMapFeatures(new Integer(mapId), new Integer(featureId));
				if (mapFeature == null){	
					mapFeature = new GeoMapFeature();
					mapFeature.setMapId(mapId);
					mapFeature.setFeatureId(featureId);
					mapFeature.setSvgGroup(null);
					mapFeature.setVisibleFlag(null);
					DAOFactory.getSbiGeoMapFeaturesDAO().insertMapFeatures(mapFeature);			
				}
			}//for
			return lstFeatures;
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 5009);	
		}
	}
	
	/**
	 * Gets and create a list of feature associated at the map. 
	 * (for tab visualization) and puts them into response
	 * @param request The response Source Bean
	 * @param response The response Source Bean
	 * @throws EMFUserError If an exception occurs
	 */
	private void getTabDetails(SourceBean request, SourceBean response) throws EMFUserError {
		try {
			 
			//create a List of features for tabs features
			String mapId = (String)request.getAttribute("ID");
			if (mapId == null || mapId.equals("-1"))
				mapId = String.valueOf(((GeoMap)response.getAttribute("mapObj")).getMapId());
			String selectedFeatureId  = (request.getAttribute("selectedFeatureId")==null)?"-1":(String)request.getAttribute("selectedFeatureId");
			List lstAllFeatures = new ArrayList();
			lstAllFeatures = DAOFactory.getSbiGeoMapFeaturesDAO().loadFeatureNamesByMapId(new Integer(mapId));				
		
			List lstMapFeatures = new ArrayList();			
			for (int i=0; i < lstAllFeatures.size(); i ++){
				GeoFeature aFeature = DAOFactory.getSbiGeoFeaturesDAO().loadFeatureByName((String)lstAllFeatures.get(i));
				lstMapFeatures.add(aFeature);
			}
			if ((selectedFeatureId == null || selectedFeatureId.equals("-1")) && lstMapFeatures.size()>0)
				selectedFeatureId = String.valueOf(((GeoFeature)lstMapFeatures.get(0)).getFeatureId());

							 
			response.setAttribute("lstMapFeatures",lstMapFeatures);
			response.setAttribute("selectedFeatureId",selectedFeatureId);								
		} catch (Exception ex) {
			TracerSingleton.log(SpagoBIConstants.NAME_MODULE, TracerSingleton.MAJOR, "Cannot fill response container" + ex.getLocalizedMessage());	
			HashMap params = new HashMap();
			params.put(AdmintoolsConstants.PAGE, ListMapsModule.MODULE_PAGE);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 5011, new Vector(), params);
		}
	}
	
	/**
	 * Deletes relations between maps and features 
	 * @param request The response Source Bean
	 * @param response The response Source Bean
	 * @throws EMFUserError If an exception occurs
	 */
	private void delRelMapFeature(SourceBean request, SourceBean response) throws EMFUserError {
		try {
			//gets list of features for delete
			String oldFeatures =  (String)request.getAttribute("lstFeaturesOld");
			if (oldFeatures == null || oldFeatures.equals(""))			 
				oldFeatures = DAOFactory.getSbiGeoFeaturesDAO().loadFeatureByID(new Integer((String)request.getAttribute("selectedFeatureId"))).getName(); 
 
			oldFeatures = oldFeatures.replace("[","");
			oldFeatures = oldFeatures.replace("]","");
	        String[] lstOldFeatures = oldFeatures.split(",");	        		       
			if (lstOldFeatures != null){ 
				String mapId = (String)request.getAttribute("id");
				for (int i=0; i<lstOldFeatures.length; i++){
					GeoFeature aFeature = DAOFactory.getSbiGeoFeaturesDAO().loadFeatureByName(((String)lstOldFeatures[i]).trim());
					GeoMapFeature aMapFeature = (GeoMapFeature)DAOFactory.getSbiGeoMapFeaturesDAO().loadMapFeatures(new Integer(mapId), new Integer(aFeature.getFeatureId()));								 
					DAOFactory.getSbiGeoMapFeaturesDAO().eraseMapFeatures(aMapFeature);
				}					
			}  
			
			if (((String)request.getAttribute("SUBMESSAGEDET")).equalsIgnoreCase(MOD_SAVEBACK))
			{
				response.setAttribute("loopback", "true");
				return;
			}			
			else{
				request.delAttribute("selectedFeatureId");
				request.setAttribute("selectedFeatureId", "-1");
				getDetailMap(request, response);
			}
		} catch (Exception ex) {
			TracerSingleton.log(SpagoBIConstants.NAME_MODULE, TracerSingleton.MAJOR, "Cannot fill response container" + ex.getLocalizedMessage());	
			HashMap params = new HashMap();
			params.put(AdmintoolsConstants.PAGE, ListMapsModule.MODULE_PAGE);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 5011, new Vector(), params);
		}
	}	
}