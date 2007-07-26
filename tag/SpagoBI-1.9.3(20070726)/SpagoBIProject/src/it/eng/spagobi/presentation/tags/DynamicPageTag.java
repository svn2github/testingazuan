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
package it.eng.spagobi.presentation.tags;

import it.eng.spago.base.Constants;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.paginator.basic.ListIFace;
import it.eng.spago.paginator.basic.PaginatorIFace;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.ModalitiesValue;
import it.eng.spagobi.bo.ObjParuse;
import it.eng.spagobi.bo.Parameter;
import it.eng.spagobi.bo.ParameterUse;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IObjParuseDAO;
import it.eng.spagobi.bo.dao.IParameterUseDAO;
import it.eng.spagobi.bo.lov.ILovDetail;
import it.eng.spagobi.bo.lov.LovDetailFactory;
import it.eng.spagobi.bo.lov.LovResultHandler;
import it.eng.spagobi.bo.lov.LovToListService;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.ChannelUtilities;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;
import it.eng.spagobi.utilities.messages.IMessageBuilder;
import it.eng.spagobi.utilities.messages.MessageBuilderFactory;
import it.eng.spagobi.utilities.urls.IUrlBuilder;
import it.eng.spagobi.utilities.urls.UrlBuilderFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;

/**
 * Defines a tag to create a dinamic JSP page
 */
public class DynamicPageTag extends TagSupport {

	private String modality = null;
	private String actor = null;
	private String moduleName = "";
	private SourceBean request = null;
	private HttpServletRequest httpRequest = null;
	private RequestContainer requestContainer = null;
	protected IUrlBuilder urlBuilder = null;
    protected IMessageBuilder msgBuilder = null;
	public static final int PIXEL_PER_CHAR = 9;
	protected String requestIdentity = null;
	
	private SessionContainer getSession() {
		return requestContainer.getSessionContainer();
	}
	
	private BIObject getBIObject() {
		return (BIObject)getSession().getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
	}
	
	
	private String encodeURL(String relativePath) {
		return urlBuilder.getResourceLink(httpRequest, relativePath);
	}
	
	
	private IEngUserProfile getProfile() {
		SessionContainer session = getSession();
		SessionContainer permSession = session.getPermanentContainer();
		IEngUserProfile profile = (IEngUserProfile)permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		return profile;
	}
	
	
	
	public int doStartTag() throws JspException {
		httpRequest = (HttpServletRequest) pageContext.getRequest();
		requestContainer = ChannelUtilities.getRequestContainer(httpRequest);
		request = requestContainer.getServiceRequest();
		urlBuilder = UrlBuilderFactory.getUrlBuilder(requestContainer.getChannelType());
		msgBuilder = MessageBuilderFactory.getMessageBuilder();

		// identity string for object of the page
		UUIDGenerator uuidGen  = UUIDGenerator.getInstance();
		UUID uuid = uuidGen.generateTimeBasedUUID();
		requestIdentity = uuid.toString();
		requestIdentity = requestIdentity.replaceAll("-", "");
		
		BIObject obj = getBIObject();
		List parameters = obj.getBiObjectParameters();
		if (parameters != null && parameters.size() > 0) {
			StringBuffer htmlStream = new StringBuffer();
			createSetLookupFieldJSFunction(htmlStream);
			createSetDeleteFlagJSFunction(htmlStream);
			createClearFieldsJSFunction(htmlStream);
			createRefreshJSFunction(htmlStream);
			createClearFieldJSFunction(htmlStream);
			createSetChangedFlagJSFunction(htmlStream);
			createSelectAllTextJSFunction(htmlStream);
			createSetRefreshCorrelationJSFunction(htmlStream);
			htmlStream.append("<div class='div_detail_area_forms' style='width:" + (getParamLabelDivWidth() + 300) + "px;'>\n");
	        Iterator iter = parameters.iterator();
	        while(iter.hasNext()) {
	        	BIObjectParameter biparam = (BIObjectParameter)iter.next();
	        	createParamValueHiddenInput(htmlStream, biparam);        	
	        	createParamChangedHiddenInput(htmlStream, biparam);  
	        	// the biparameter is not showed if one of the following conditions is safisfied:
	        	// 1. the biparameter is transient and has valid values
	        	// 2. the biparameter has a single value and has valid value
	        	if ((!biparam.isTransientParmeters() && !isSingleValue(biparam)) 
	        			|| !biparam.hasValidValues()) {  
	        		
	        		createParameterLabelDiv(htmlStream, biparam);        		
	        		String objParFatherLabel = createParameterInputboxDiv(biparam, htmlStream);
	        		
	        		if (objParFatherLabel != null) {
	        			String correlation = msgBuilder.getMessage(requestContainer, "SBIDev.docConf.execBIObjectParams.correlatedParameter", "messages");
	        			correlation += " " + objParFatherLabel ;
	        			htmlStream.append("		<img src= '" + encodeURL("/img/parCorrelation.gif") + "' ");
	        			htmlStream.append("		 title='" + correlation + "' alt='" + correlation + "' />");
	        		}
	        		htmlStream.append("		</div>\n");
	        	}
	        }
	        
	        htmlStream.append("		<div style='clear:left;width:" + getParamLabelDivWidth() + "px;float:left;'>\n");
	        htmlStream.append("			&nbsp;\n");
	        htmlStream.append("		</div>\n");
	        
	        createClearFieldsButton(htmlStream);
			
	        htmlStream.append("</div>\n");		
	       
			try {
				pageContext.getOut().print(htmlStream);
			} catch(IOException ioe) {
				SpagoBITracer.major("", "DynamicPageTag", "doStartTag", "cannot start tag: IO exception occurred",ioe);
			}
		}
		return SKIP_BODY;
	}
		
	
	
	
	public int doEndTag() throws JspException {
		TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.INFORMATION, "TitleTag::doEndTag:: invocato");
		return super.doEndTag();
	}
	
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	'CREATE HTML' METHODS
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public boolean isSingleValue(BIObjectParameter biparam) {
		boolean isSingleValue = false;
    	try {
			LovResultHandler lovResultHandler = new LovResultHandler(biparam.getLovResult());
			if(lovResultHandler.isSingleValue()) isSingleValue = true;
		} catch (SourceBeanException e) {
			// TODO Auto-generated catch block
		}
		return isSingleValue;
	}
	
	
	
	/**
	 * Creates two empty hidden inputs and the correspondent JavaScript to populate it in case 
	 * there is a lookup call...
	 * 	- LOOKUP_OBJ_PAR_ID: is the id of the parameter looked up
	 *  - LOOKUP_TYPE: is the type of lookup (i.e. list or cheklist)
	 * 			
	 * @param htmlStream
	 */
	private void createSetLookupFieldJSFunction(StringBuffer htmlStream) {
		htmlStream.append("<input type='hidden' id='LOOKUP_OBJ_PAR_ID" + requestIdentity + "' name='' value=''/>\n");
		htmlStream.append("<input type='hidden' id='LOOKUP_TYPE" + requestIdentity + "' name='' value=''/>\n");
		
		htmlStream.append("<script type='text/javascript'>\n");
		htmlStream.append("	function setLookupField" + requestIdentity + "(idStr, type) {\n");
		htmlStream.append("		document.getElementById('LOOKUP_OBJ_PAR_ID" + requestIdentity + "').value= idStr;\n");
		htmlStream.append("		document.getElementById('LOOKUP_OBJ_PAR_ID" + requestIdentity + "').name = 'LOOKUP_OBJ_PAR_ID';\n");
		htmlStream.append("		document.getElementById('LOOKUP_TYPE" + requestIdentity + "').value= type;\n");
		htmlStream.append("		document.getElementById('LOOKUP_TYPE" + requestIdentity + "').name = 'LOOKUP_TYPE';\n");
		htmlStream.append("	}\n");
		htmlStream.append("</script>\n");
	}
	
	/**
	 * Creates an empty hidden input and the correspondent JavaScript to populate it in case 
	 * there is a panding delete...
	 * 	- PENDING_DELETE: setted to true whene there are a pending delete
	 * 			
	 * @param htmlStream
	 */
	private void createSetDeleteFlagJSFunction(StringBuffer htmlStream) {
		htmlStream.append("<input type='hidden' id='PENDING_DELETE" + requestIdentity + "' name='' value=''/>\n");
		
		htmlStream.append("<script type='text/javascript'>\n");
		htmlStream.append("	function setDeleteFlag" + requestIdentity + "() {\n");
		htmlStream.append("		document.getElementById('PENDING_DELETE" + requestIdentity + "').value = 'true';\n");	
		htmlStream.append("		document.getElementById('PENDING_DELETE" + requestIdentity + "').name = 'PENDING_DELETE';\n");	
		htmlStream.append("	}\n");
		htmlStream.append("</script>\n");
	}	
	
	private void createSetChangedFlagJSFunction(StringBuffer htmlStream) {
		htmlStream.append("<script type='text/javascript'>\n");
		htmlStream.append("	function setChangedFlag" + requestIdentity + "(paramUrl) {\n");
		//htmlStream.append("		alert(paramUrl + 'IsChanged');\n");
		htmlStream.append("		document.getElementById(paramUrl + 'IsChanged' + '" + requestIdentity + "').value = 'true';\n");	
		htmlStream.append("	}\n");
		htmlStream.append("</script>\n");
	}	
	
	private void createRefreshJSFunction(StringBuffer htmlStream) {
		htmlStream.append("<script type='text/javascript'>\n");
		htmlStream.append("		function refresh" + requestIdentity + "(srcId, destId) {\n");
		htmlStream.append("			var srcValue = document.getElementById(srcId).value;\n");
		htmlStream.append("			var destObj = document.getElementById(destId);\n");
		htmlStream.append("			destObj.value = srcValue;\n");
		//htmlStream.append("			alert(destObj.value);\n");
		htmlStream.append("		}\n");
		htmlStream.append("</script>\n");
	}
	
	private void createClearFieldJSFunction(StringBuffer htmlStream) {
		htmlStream.append("<script type='text/javascript'>\n");
		htmlStream.append("		function clearField" + requestIdentity + "(targetId) {\n");
		//htmlStream.append("			alert('targetId: ' + targetId)\n");
		htmlStream.append("			document.getElementById(targetId).value = '';\n");
		htmlStream.append("		}\n");
		htmlStream.append("</script>\n");
	}
	
	private void createSelectAllTextJSFunction(StringBuffer htmlStream) {
		htmlStream.append("<script type='text/javascript'>\n");
		htmlStream.append("		function selectAllText" + requestIdentity + "(id) {\n");
		htmlStream.append("			var object = document.getElementById(id);\n");
		htmlStream.append("			object.select( );\n");
		htmlStream.append("		}\n");
		htmlStream.append("</script>\n");
	}
	
	private void createClearFieldsJSFunction(StringBuffer htmlStream) {
		htmlStream.append("<script type='text/javascript'>\n");
        htmlStream.append("		function clearFields" + requestIdentity + "() {\n");
        
        Iterator it = getBIObject().getBiObjectParameters().iterator();
        String anId = null;
        while (it.hasNext()) {
        	BIObjectParameter biparam = (BIObjectParameter) it.next();
        	if(isSingleValue(biparam)) continue;
        	
        	String id = biparam.getParameterUrlName();
        	anId = id;
    		ModalitiesValue modVal = biparam.getParameter().getModalityValue();
    		
    		String typeCode = modVal.getITypeCd();
    		String selectionType = modVal.getSelectionType();
    		
    		
    		if(typeCode.equalsIgnoreCase(SpagoBIConstants.INPUT_TYPE_MAN_IN_CODE)) {
    			htmlStream.append(" document.getElementById('" + id + requestIdentity + "').value = '';\n");
    			htmlStream.append(" document.getElementById('" + id + requestIdentity + "Desc').value = '';\n");
    		} else if(selectionType.equalsIgnoreCase("COMBOBOX")) {
    			htmlStream.append(" document.getElementById('" + id + requestIdentity + "').value = document.getElementById('" + id + requestIdentity + "Desc').value;\n");
    			htmlStream.append(" document.getElementById('" + id + requestIdentity + "Desc').selectedIndex = 0;\n");
    		} else if(selectionType.equalsIgnoreCase("CHECK_LIST")) {
    			htmlStream.append(" document.getElementById('" + id + requestIdentity + "').value = '';\n");
    			htmlStream.append(" document.getElementById('" + id + requestIdentity + "Desc').value = '';\n");
    		} else if(selectionType.equalsIgnoreCase("LIST")) {
    			htmlStream.append(" document.getElementById('" + id + requestIdentity + "').value = '';\n");
    			htmlStream.append(" document.getElementById('" + id + requestIdentity + "Desc').value = '';\n");
    		}
        }
        htmlStream.append(" setDeleteFlag" + requestIdentity + "();\n");
        htmlStream.append(" setRefreshCorrelationFlag" + requestIdentity + "();\n");
        htmlStream.append(" document.getElementById('" + anId + requestIdentity + "').form.submit();\n");
        htmlStream.append("}\n");
        htmlStream.append("</script>\n");
	}
	
	private void createSetRefreshCorrelationJSFunction(StringBuffer htmlStream) {
		htmlStream.append("<input type='hidden' id='REFRESH_CORRELATION" + requestIdentity + "' name='' value=''/>\n");
		
		htmlStream.append("<script type='text/javascript'>\n");
		htmlStream.append("	function setRefreshCorrelationFlag" + requestIdentity + "() {\n");
		htmlStream.append("		document.getElementById('REFRESH_CORRELATION" + requestIdentity + "').value = 'true';\n");	
		htmlStream.append("		document.getElementById('REFRESH_CORRELATION" + requestIdentity + "').name = 'REFRESH_CORRELATION';\n");	
		htmlStream.append("	}\n");
		htmlStream.append("</script>\n");
	}	
	
	private void createHiddenInput(StringBuffer htmlStream, String id, String name, String value) {
		htmlStream.append("<input type='hidden' name='"  + name + "' id='" + id + requestIdentity + "' value='" + value + "' />\n");
	}
	
	private void createParamValueHiddenInput(StringBuffer htmlStream, BIObjectParameter biparam) {
		createHiddenInput(htmlStream, 
						  biparam.getParameterUrlName(), 
						  biparam.getParameterUrlName(), 
						  GeneralUtilities.substituteQuotesIntoString(getParameterValuesAsString(biparam)));
	}
	
	private void createParamChangedHiddenInput(StringBuffer htmlStream, BIObjectParameter biparam) {
		createHiddenInput(htmlStream, 
						  biparam.getParameterUrlName() + "IsChanged", 
						  biparam.getParameterUrlName() + "IsChanged", 
						  "false");
	}
	
	private void createClearFieldsButton(StringBuffer htmlStream) {
		String resetFieldsLbl = msgBuilder.getMessage(requestContainer, "SBIDev.docConf.execBIObjectParams.resetFields", "messages");
		
		htmlStream.append("		<div class='div_detail_form'>\n");
	    htmlStream.append("				<a href='javascript:void(0)' onclick='clearFields" + requestIdentity+ "()' class='portlet-form-field-label'>\n");
	    htmlStream.append("					" + resetFieldsLbl + "\n");
	    htmlStream.append("				</a>\n");
	    htmlStream.append("		</div>\n");
	}
	
	private void createParameterLabelDiv(StringBuffer htmlStream, BIObjectParameter biparam) {
		htmlStream.append("		<div style='clear:left;width:" + getParamLabelDivWidth() + "px;float:left;'>\n");
		htmlStream.append("			<span class='portlet-form-field-label'>\n");
		htmlStream.append(biparam.getLabel());
		htmlStream.append("			</span>\n");
		htmlStream.append("		</div>\n");
		htmlStream.append("		<div class='div_detail_form'>\n");
	}
	
	/**
	 * Given a <code>BIObjectParameter </code> object at input, creates from it a dinamic 
	 * HTML form. It is called from the <code>doStartTag </code> method.
	 * 
	 * @param biparam The input BI object parameter
	 * @param htmlStream The buffer containing all html code 
	 * @return The label of the BIObjectParameter of dependancy, if any
	 */
	private String createParameterInputboxDiv(BIObjectParameter biparam, StringBuffer htmlStream) {
		String objParFathLbl = null;
		
		// search for biparameter which dependes from this
		List lblBiParamDependent = new ArrayList();
		try {
			lblBiParamDependent = DAOFactory.getObjParuseDAO().getDependencies(biparam.getId());
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
					            "createParameterInputboxDiv", "Error while recovering dependencies " +
					            " for biparm label " + biparam.getLabel(), e);
			lblBiParamDependent = new ArrayList();
		}
		
		String typeCode = getModalityValue(biparam).getITypeCd();
		
		if(typeCode.equalsIgnoreCase(SpagoBIConstants.INPUT_TYPE_MAN_IN_CODE)) {
			createHTMLManInputButton(biparam, htmlStream, lblBiParamDependent);
			return null;
		}		
		
		String selectionType = getModalityValue(biparam).getSelectionType();
		if(selectionType == null) {
			if(typeCode.equalsIgnoreCase(SpagoBIConstants.INPUT_TYPE_FIX_LOV_CODE) 
				|| typeCode.equalsIgnoreCase(SpagoBIConstants.INPUT_TYPE_SCRIPT_CODE))
				selectionType = "COMBOBOX";
			else if (typeCode.equalsIgnoreCase(SpagoBIConstants.INPUT_TYPE_QUERY_CODE))
				selectionType = "LIST";
		}			
		
		// looks for dependencies 
		Object[] results = getObjectFather(biparam);
		BIObjectParameter objParFather = (BIObjectParameter)results[1];
		ObjParuse objParuse = (ObjParuse)results[0];
		if (objParFather != null && objParuse != null) {
			// the BIobjectParameter is correlated to another BIObjectParameter
			htmlStream.append("<input type='hidden' name='correlatedParuseIdForObjParWithId_" + biparam.getId() + "' value='" + objParuse.getParuseId() + "' />\n");
			objParFathLbl=  objParFather.getLabel();
		}
		
		if(selectionType.equalsIgnoreCase("COMBOBOX")) {
			createHTMLComboBox(biparam, htmlStream, lblBiParamDependent);
		}
		else if(selectionType.equalsIgnoreCase("RADIOBUTTON")) {
			createHTMLRadioButton(biparam, htmlStream);		
		}		
		else if(selectionType.equalsIgnoreCase("LIST")) {
			createHTMLListButton(biparam, false, htmlStream, lblBiParamDependent);
		}		
		else if(selectionType.equalsIgnoreCase("CHECK_LIST")) {
			createHTMLCheckListButton(biparam, false, htmlStream, lblBiParamDependent);
		}
		
		
		return objParFathLbl;		
	}
	
	private void createHTMLManInputButton(BIObjectParameter biparam, StringBuffer htmlStream, List lblBiParamDependent) {
		htmlStream.append("<input style='width:230px;' type='text' " +
						 		 "name='" + biparam.getParameterUrlName() 		 + "Desc' " +
						 		 "id='" + biparam.getParameterUrlName() 		 + requestIdentity + "Desc' " +								 
								 "class='portlet-form-input-field' " +
								 "value='" + getParameterValuesAsString(biparam) + "' " +
								 "autocomplete='off' " +
								 "onchange=\"refresh" + requestIdentity + "('" + biparam.getParameterUrlName() + requestIdentity + "Desc','" +  biparam.getParameterUrlName() + requestIdentity + "');");
    	if (lblBiParamDependent != null && lblBiParamDependent.size() > 0) {
    		htmlStream.append("setRefreshCorrelationFlag" + requestIdentity + "();");
    		htmlStream.append("this.form.submit();");
    	}
    	htmlStream.append("\" >\n");		
	}
	
	
	private void createHTMLListButton(BIObjectParameter biparam, boolean isReadOnly, StringBuffer htmlStream, 
			List lblBiParamDependent) {	
		htmlStream.append("<input type='text' style='width:230px;' " + 
						  	"name='" + biparam.getParameterUrlName() +"Desc' "+
						  	"id='" + biparam.getParameterUrlName() + requestIdentity + "Desc' " +
							"class='portlet-form-input-field' " + (isReadOnly?"readonly='true' ":" ") +
							"value='" + GeneralUtilities.substituteQuotesIntoString(getParameterDescription(biparam)) + "' " +
							"onchange=\"refresh" + requestIdentity + "('" + biparam.getParameterUrlName() + requestIdentity + "Desc','" +  biparam.getParameterUrlName() + requestIdentity + "');" +
									   "setChangedFlag" + requestIdentity + "('" + biparam.getParameterUrlName() + "');");
    	if (lblBiParamDependent != null && lblBiParamDependent.size() > 0) {
    		htmlStream.append("setRefreshCorrelationFlag" + requestIdentity + "();");
    		htmlStream.append("this.form.submit();");
    	}
    	htmlStream.append("\" ");
    	htmlStream.append("onclick=\"selectAllText" + requestIdentity + "('" + biparam.getParameterUrlName() + requestIdentity + "Desc');\" " +							 		  
						  "autocomplete='off'/>\n");
		
		htmlStream.append("<input type='image' onclick='setLookupField" + requestIdentity + "(\"" + biparam.getId() + "\", \"LIST\")' \n");
		htmlStream.append("		src= '" + encodeURL("/img/detail.gif") + "' \n");
		htmlStream.append("		title='Lookup' alt='Lookup' \n");
		htmlStream.append("/>\n");
	}	

	private void createHTMLCheckListButton(BIObjectParameter biparam, boolean isReadOnly, StringBuffer htmlStream, 
			List lblBiParamDependent) {
		
		htmlStream.append("<input type='text' style='width:230px;' " + 
					  	"name='" + biparam.getParameterUrlName() +"Desc' "+
					  	"id='" + biparam.getParameterUrlName() + requestIdentity + "Desc' " +
						"class='portlet-form-input-field' " + (isReadOnly?"readonly='true' ":" ") +
						"value='" + GeneralUtilities.substituteQuotesIntoString(getParameterDescription(biparam)) + "' " +
						"onchange=\"refresh" + requestIdentity + "('" + biparam.getParameterUrlName() + requestIdentity + "Desc','" +  biparam.getParameterUrlName() + requestIdentity + "');" +
								   "setChangedFlag" + requestIdentity + "('" + biparam.getParameterUrlName() + "');");
    	if (lblBiParamDependent != null && lblBiParamDependent.size() > 0) {
    		htmlStream.append("setRefreshCorrelationFlag" + requestIdentity + "();");
    		htmlStream.append("this.form.submit();");
    	}
    	htmlStream.append("\" ");
    	htmlStream.append("onclick=\"selectAllText" + requestIdentity + "('" + biparam.getParameterUrlName() + requestIdentity + "Desc');\" " +							 		  
						  "autocomplete='off'/>\n");
	
		htmlStream.append("<input type='image' onclick='setLookupField" + requestIdentity + "(\"" + biparam.getId() + "\", \"CHECK_LIST\")' \n");
		htmlStream.append("		src= '" + encodeURL("/img/detail.gif") + "' \n");
		htmlStream.append("		title='Lookup' alt='Lookup' \n");
		htmlStream.append("/>\n");
	}	
	
	
	
	/**
	 * Generate html code for a combobox form
	 * @param biparam the parameter of the biobject
	 * @param htmlStream the html of the combobox
	 */
	private void createHTMLComboBox(BIObjectParameter biparam, StringBuffer htmlStream, List lblBiParamDependent) {
    	try{
    		/*
    		String onchangeStr = " onchange=\"refresh('"+biparam.getParameterUrlName()+"Desc','" +  
			 											 biparam.getParameterUrlName()+"');";
			 Iterator lblBiParamDependentIter = lblBiParamDependent.iterator();
			 while(lblBiParamDependentIter.hasNext()) {
				 String lbl = (String)lblBiParamDependentIter.next();
				 onchangeStr += "reloadValuesFor"+lbl+"();";
			 }
			 onchangeStr += "\" ";
    		*/
    		
    		// create initial html
	    	htmlStream.append("<select style='width:230px;' " +  
	    			 			"name='" + biparam.getParameterUrlName() + "Desc' " +
	    			 			"id='"+ biparam.getParameterUrlName()+ requestIdentity + "Desc' " +
	    			 			"class='portlet-form-field' " +
	    			 			//onchangeStr +
	    			 			"onchange=\"refresh" + requestIdentity + "('" + biparam.getParameterUrlName()+ requestIdentity + "Desc', " +
	    			 					  "'" + biparam.getParameterUrlName() + requestIdentity + "');");	
	    	if (lblBiParamDependent != null && lblBiParamDependent.size() > 0) {
	    		htmlStream.append("setRefreshCorrelationFlag" + requestIdentity + "();");
	    		htmlStream.append("this.form.submit();");
	    	}
	    	htmlStream.append("\" >\n");
	    	htmlStream.append("<option value=''> </option>\n");
            // get the lov associated to the parameter 
	    	Parameter par = biparam.getParameter();	    	
	    	ModalitiesValue lov = par.getModalityValue();
	    	// build the ILovDetail object associated to the lov
	    	String lovProv = lov.getLovProvider();
	    	ILovDetail lovProvDet = LovDetailFactory.getLovFromXML(lovProv);
	    	// get the result of the lov
	    	IEngUserProfile profile = getProfile();
	    	String lovResult = biparam.getLovResult();
	    	if((lovResult==null) || (lovResult.trim().equals(""))) {
	    		lovResult = lovProvDet.getLovResult(profile);
	    	}
	    	
	    	Integer biparId = biparam.getId();
	    	String biparIdStr = biparId.toString();
	    	Integer parId = par.getId();
			Integer lovId = lov.getId();
	    	
	    	Integer parusecorrId = null;
	    	IParameterUseDAO parusedao = DAOFactory.getParameterUseDAO();
	    	List paruses = parusedao.loadParametersUseByParId(parId);
	    	Iterator iterParuse = paruses.iterator();
	    	while(iterParuse.hasNext()) {
	    		 ParameterUse paruse = (ParameterUse)iterParuse.next();
	    	     if(paruse.getIdLov().equals(lovId)) {
	    	    	 parusecorrId = paruse.getUseID();
	    	     }
	    	}
	    	
	    	if(parusecorrId!=null) {			
				request.updAttribute("LOOKUP_PARAMETER_ID", biparIdStr);
				request.updAttribute("correlated_paruse_id", parusecorrId.toString());
		    	LovToListService ltls = new LovToListService(lovResult);
		    	ListIFace listvalues = ltls.getLovAsListService();
		    	listvalues = ltls.filterListForCorrelatedParam(request, listvalues, httpRequest);
		    	PaginatorIFace listPagin = listvalues.getPaginator();
		    	SourceBean allrows = listPagin.getAll();
		    	lovResult = allrows.toXML(false);
	    	}
	    	
	    	// get value and description column
	    	String valueColumn = lovProvDet.getValueColumnName();
	    	String descriptionColumn = lovProvDet.getDescriptionColumnName();
	    	// get all the rows of the result and build the option of the combobox
	    	LovResultHandler lovResultHandler = new LovResultHandler(lovResult);
	    	List rows = lovResultHandler.getRows();
	    	Iterator it = rows.iterator();
	    	while(it.hasNext()) {
	    		SourceBean row = (SourceBean)it.next();
				String value = (String)row.getAttribute(valueColumn);
				String description = (String)row.getAttribute(descriptionColumn);
				String selected = "";
				if (getParameterValuesAsString(biparam).equals(value)) selected = "selected=\"selected\"";
				htmlStream.append("<option value='"+GeneralUtilities.substituteQuotesIntoString(value) +
						"' " + selected + ">" + description  + "</option>\n");
			
	    	}
	    	// close combo box
			htmlStream.append("</select>\n");
			
			
	    }catch (Exception ex) {
	    	SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
	    			            "createHTMLComboBox", "Error while creating html combo box " +
	    			            " for biparam " + biparam.getLabel(), ex);
	    }
    }	
	
	private void eraseParameterValues (BIObjectParameter biparam) {
		biparam.setParameterValues(null);
		HashMap paramsDescriptionMap = (HashMap)getSession().getAttribute("PARAMS_DESCRIPTION_MAP");
		paramsDescriptionMap.put(biparam.getParameterUrlName(), "");
	}
	
	private void createHTMLRadioButton(BIObjectParameter biparam, StringBuffer htmlStream) {
    	try{
	    	List l = getXMLValuesBean(biparam).getAttributeAsList("LOV-ELEMENT");
			Iterator it = l.iterator();
			SourceBean sbTemp = null;
			String desc = null;
			String val = null;
			while(it.hasNext()) {
				sbTemp = (SourceBean)it.next();
				desc = (String)sbTemp.getAttribute("DESC");
				val = (String)sbTemp.getAttribute("VALUE");
				String selected = "";
				if (getParameterValuesAsString(biparam).equals(val)) selected = "checked";
				htmlStream.append("<input type='radio' name='"+biparam.getParameterUrlName()+"' value='"+val+"' " + selected + ">"+desc+"</input>\n");
			}
	    }catch (Exception ex) {
	    	ex.printStackTrace();
	    }
    }
	
	
	/**
	 * @return en extimation of the parameter label div in px
	 */
	private int getParamLabelDivWidth() {
        int maxLength = 0;
        Iterator iterPars = getBIObject().getBiObjectParameters().iterator();
        while(iterPars.hasNext()) {
        	BIObjectParameter biparam = (BIObjectParameter)iterPars.next();
        	String label = biparam.getLabel();
            if(label.length() > maxLength) 
            	maxLength = label.length();
        }
        
        int labelDivWidth = maxLength * PIXEL_PER_CHAR; 
		        
        return labelDivWidth;
	} 
	
	private String getParameterValuesAsString(BIObjectParameter biparam) {
		String valuesStr = "";
		List values = biparam.getParameterValues();
		if(values == null) return valuesStr;
		
		for(int i = 0; i < values.size(); i++) valuesStr += (i==0? "": ";") + GeneralUtilities.substituteQuotesIntoString((String)values.get(i));
		
		return valuesStr;
	}
	
	
	
	private ModalitiesValue getModalityValue(BIObjectParameter biparam) {
		 return biparam.getParameter().getModalityValue();
	}
	
	private Object[] getObjectFather(BIObjectParameter biparam) {
		BIObjectParameter objParFather = null;
		ObjParuse objParuse = null;
		try {
			IObjParuseDAO objParuseDAO = DAOFactory.getObjParuseDAO();
			IParameterUseDAO paruseDAO = DAOFactory.getParameterUseDAO();
			List objParuses = objParuseDAO.loadObjParuses(biparam.getId());
			if (objParuses != null && objParuses.size() > 0) {
				Iterator it = objParuses.iterator();
				while (it.hasNext()) {
					ObjParuse aObjParuse = (ObjParuse) it.next();
					Integer paruseId = aObjParuse.getParuseId();
					ParameterUse aParameterUse = paruseDAO.loadByUseID(paruseId);
					Integer idLov = aParameterUse.getIdLov();
					if (idLov.equals(getModalityValue(biparam).getId())) {
						// the ModalitiesValue of the BIObjectParameter corresponds to a ParameterUse correlated
						objParuse = aObjParuse;
						SpagoBITracer.debug("", "DynamicPageTag", "createHTMLForm()", "Found correlation:" +
								" dependent BIObjectParameter id = " + biparam.getId() + "," +
								" ParameterUse with id = " + paruseId + ";" +
								" BIObjectParameter father has id = " + objParuse.getObjParFatherId());
						// now we have to find the BIObjectParameter father of the correlation
						Integer objParFatherId = objParuse.getObjParFatherId();
						List parameters = getBIObject().getBiObjectParameters();
						Iterator i = parameters.iterator();
						while (i.hasNext()) {
							BIObjectParameter aBIObjectParameter = (BIObjectParameter) i.next();
							if (aBIObjectParameter.getId().equals(objParFatherId)) {
								objParFather = aBIObjectParameter;
								break;
							}
						}
						if (objParFather == null) {
							// the BIObjectParameter father of the correlation was not found
							SpagoBITracer.major("", "DynamicPageTag", "createHTMLForm()", "Cannot find the BIObjectParameter father of the correlation");
							throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
						}
						break;
					}
				}
			}
		} catch (EMFUserError e) {
			SpagoBITracer.major("", "DynamicPageTag", "createHTMLForm()", "Error while retrieving information from db", e);
			e.printStackTrace();
		}
		
		return new Object[]{objParuse, objParFather};
	}
	
	private SourceBean getXMLValuesBean(BIObjectParameter biparam) throws SourceBeanException {
		String stringXMLValues = biparam.getParameter().getModalityValue().getLovProvider();
		stringXMLValues = GeneralUtilities.cleanString(stringXMLValues);
		return SourceBean.fromXMLString(stringXMLValues);
	}
	

	
	public String getParameterDescription(BIObjectParameter biparam) {
		String description = null;
		
		HashMap paramsDescriptionMap = (HashMap)getSession().getAttribute("PARAMS_DESCRIPTION_MAP");
		description = (String)paramsDescriptionMap.get(biparam.getParameterUrlName());
		
		return description;
	}
	
	/**
	 * 
	 * @return modality The modality String to return.
	 */
	public String getModality() {
		return modality;
	}
	/**
	 * @param modality The modality to set.
	 */
	public void setModality(String modality) {
		this.modality = modality;
	}
	/**
	 * 
	 * @return The Actor's name.
	 */
	public String getActor() {
		return actor;
	}
	/**
	 * 
	 * @param actor The Actor to set.
	 */
	public void setActor(String actor) {
		this.actor = actor;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
}	
	
	
