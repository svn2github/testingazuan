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
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.navigation.LightNavigationManager;
import it.eng.spago.paginator.basic.ListIFace;
import it.eng.spago.paginator.basic.PaginatorIFace;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spago.util.StringUtils;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.handlers.ExecutionInstance;
import it.eng.spagobi.analiticalmodel.document.service.BIObjectsModule;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.ObjParuse;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.Parameter;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.ParameterUse;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.dao.IObjParuseDAO;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.dao.IParameterUseDAO;
import it.eng.spagobi.behaviouralmodel.check.bo.Check;
import it.eng.spagobi.behaviouralmodel.lov.bo.ILovDetail;
import it.eng.spagobi.behaviouralmodel.lov.bo.LovDetailFactory;
import it.eng.spagobi.behaviouralmodel.lov.bo.LovResultHandler;
import it.eng.spagobi.behaviouralmodel.lov.bo.LovToListService;
import it.eng.spagobi.behaviouralmodel.lov.bo.ModalitiesValue;
import it.eng.spagobi.commons.constants.ObjectsTreeConstants;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.ChannelUtilities;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.commons.utilities.ParameterValuesEncoder;
import it.eng.spagobi.commons.utilities.messages.IMessageBuilder;
import it.eng.spagobi.commons.utilities.messages.MessageBuilderFactory;
import it.eng.spagobi.commons.utilities.urls.IUrlBuilder;
import it.eng.spagobi.commons.utilities.urls.UrlBuilderFactory;
import it.eng.spagobi.container.ContextManager;
import it.eng.spagobi.container.SpagoBISessionContainer;
import it.eng.spagobi.container.strategy.LightNavigatorContextRetrieverStrategy;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

/**
 * Defines a tag to create a Parameters Input
 * 
 * @author Bernabei Angelo
 */
public class ParametersGeneratorTag extends TagSupport {
    static private Logger logger = Logger.getLogger(ParametersGeneratorTag.class);

    private String modality = null;
    private String moduleName = "";
    private SourceBean request = null;
    private HttpServletRequest httpRequest = null;
    private RequestContainer requestContainer = null;
    protected IUrlBuilder urlBuilder = null;
    protected IMessageBuilder msgBuilder = null;
    private ContextManager contextManager = null;
    public static final int PIXEL_PER_CHAR = 9;
    // identity string for object of the page
    protected String requestIdentity;
    
    private String roleName = null;

	private ExecutionInstance getExecutionInstance() {
		return contextManager.getExecutionInstance(ExecutionInstance.class.getName());
	}
	
    private String encodeURL(String relativePath) {
    	return urlBuilder.getResourceLink(httpRequest, relativePath);
    }

    private IEngUserProfile getProfile() {
	logger.debug("IN");
	SessionContainer session = requestContainer.getSessionContainer();
	SessionContainer permSession = session.getPermanentContainer();
	IEngUserProfile profile = (IEngUserProfile) permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
	logger.debug("OUT");
	return profile;
    }

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
	if (requestIdentity == null)
	    requestIdentity = "";
	SessionContainer session = requestContainer.getSessionContainer();
	contextManager = new ContextManager(new SpagoBISessionContainer(session), 
			new LightNavigatorContextRetrieverStrategy(request));

	ExecutionInstance instance = null;
	try {
		instance = getExecutionInstance();
	} catch (Exception e) {
		logger.error("Error while retrieving execution instance", e);
		return SKIP_BODY;
	}
	BIObject obj = instance.getBIObject();
	roleName = instance.getExecutionRole();
	
	List parameters = obj.getBiObjectParameters();
	boolean hasParametersToBeShown = false;
	StringBuffer htmlStream = new StringBuffer();
	if (parameters != null && parameters.size() > 0) {
		createParametersFormButtons(htmlStream);
		openParametersForm(htmlStream);
		createFieldForViewPoints(htmlStream);
	    createSetLookupFieldJSFunction(htmlStream);
	    createSetDeleteFlagJSFunction(htmlStream);
	    createClearFieldsJSFunction(htmlStream);
	    createRefreshJSFunction(htmlStream);
	    createClearFieldJSFunction(htmlStream);
	    createSetChangedFlagJSFunction(htmlStream);
	    createSelectAllTextJSFunction(htmlStream);
	    createSetRefreshCorrelationJSFunction(htmlStream);
	    Iterator iter = parameters.iterator();
	    while (iter.hasNext()) {
			BIObjectParameter biparam = (BIObjectParameter) iter.next();
			
			String typeCode = getModalityValue(biparam).getITypeCd();
			Parameter par = biparam.getParameter();
			String parType = "";
			if (par != null){
				parType = par.getType();
			}
			
			// the biparameter is not showed if has valid values and (is single value or is transient)
			if (!biparam.hasValidValues() || (!isSingleValue(biparam) && !biparam.isTransientParmeters())) {
				
				if (!typeCode.equalsIgnoreCase(SpagoBIConstants.INPUT_TYPE_MAN_IN_CODE) ) {
					createParamValueHiddenInput(htmlStream, biparam);	
					createParamChangedHiddenInput(htmlStream, biparam);
				} else if (typeCode.equalsIgnoreCase(SpagoBIConstants.INPUT_TYPE_MAN_IN_CODE) ) {
					if (!parType.equals("DATE")){
						createParamValueHiddenInput(htmlStream, biparam);
						createParamChangedHiddenInput(htmlStream, biparam);
					}
				}
				
			    // opens the div tag for the parameters form only the first
			    // time
			    if (!hasParametersToBeShown) {
				//htmlStream.append("<div class='div_detail_area_forms' style='width:"
				//	+ (getParamLabelDivWidth() + 300) + "px;'>\n");
				htmlStream.append("<div>\n");
				hasParametersToBeShown = true;
			    }
	
			    createParameterLabelDiv(htmlStream, biparam);
			    String objParFatherLabel = createParameterInputboxDiv(biparam, htmlStream);
	
			    if (objParFatherLabel != null) {
				String correlation = msgBuilder.getMessage(
					"SBIDev.docConf.execBIObjectParams.correlatedParameter", "messages", httpRequest);
				correlation += " " + objParFatherLabel;
				htmlStream.append("		<img style='text-decoration:none' src= '" + encodeURL("/img/parCorrelation.gif") + "' ");
				htmlStream.append("		 title='" + correlation + "' alt='" + correlation + "' />");
			    }
			    htmlStream.append("		</div>\n");
			}
	    }

	    if (hasParametersToBeShown) {
		//htmlStream.append("		<div style='clear:left;width:" + getParamLabelDivWidth() + "px;float:left;'>\n");
		htmlStream.append("		<div >\n");
		htmlStream.append("			&nbsp;\n");
		htmlStream.append("		</div>\n");
		//createClearFieldsButton(htmlStream);
		// closes the div tag of the parameters form
		htmlStream.append("</div>\n");
	    } else {
		createNoParametersMessage(htmlStream);
	    }
	    closeParametersForm(htmlStream);
	} else {
		openParametersForm(htmlStream);
	    createNoParametersMessage(htmlStream);
	    closeParametersForm(htmlStream);
	}

	try {
	    pageContext.getOut().print(htmlStream);
	} catch (IOException ioe) {
	    logger.error("cannot start tag: IO exception occurred", ioe);
	}
	logger.debug("OUT");
	return SKIP_BODY;
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
     */
    public int doEndTag() throws JspException {
	logger.debug("TitleTag::doEndTag:: invocato");
	return super.doEndTag();
    }

    // ///////////////////////////////////////////////////////////////////////////////////////////////////////
    // 'CREATE HTML' METHODS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Checks if is single value.
     * 
     * @param biparam the biparam
     * 
     * @return true, if is single value
     */
    public boolean isSingleValue(BIObjectParameter biparam) {
	logger.debug("IN");
	boolean isSingleValue = false;
	try {
	    logger.debug("biparam.getLovResult():"+biparam.getLovResult());
	    if (biparam.getLovResult()!=null){
		    LovResultHandler lovResultHandler = new LovResultHandler(biparam.getLovResult());
		    if (lovResultHandler.isSingleValue())
			isSingleValue = true;		
	    }
	} catch (SourceBeanException e) {
	    logger.warn("SourceBeanException",e);
	}
	logger.debug("OUT");
	return isSingleValue;
    }

    private void openParametersForm(StringBuffer htmlStream) {
	logger.debug("IN");
    	Map executeUrlPars = new HashMap();
    	executeUrlPars.put("PAGE", "ValidateExecuteBIObjectPage");
    	executeUrlPars.put(SpagoBIConstants.MESSAGEDET, ObjectsTreeConstants.EXEC_PHASE_RUN);
    	executeUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
    	String execUrl = urlBuilder.getUrl(httpRequest, executeUrlPars);
    	htmlStream.append("<div class='form'>\n");
    	htmlStream.append("	<form id='parametersForm" + requestIdentity +"' name='parametersForm" + requestIdentity +"' " +
    			"action='" + execUrl + "' method='POST'>\n");
    	logger.debug("OUT");
    }
    
    private void closeParametersForm(StringBuffer htmlStream) {
    	htmlStream.append("	</form>\n");
    	htmlStream.append("</div>\n");
    }
    
    private void createFieldForViewPoints(StringBuffer htmlStream) {
    	htmlStream.append("<input type='hidden' name='SUBMESSAGEDET' value='' />\n");
    	htmlStream.append("<input type='hidden' name='tmp_nameVP' value='' />\n");
    	htmlStream.append("<input type='hidden' name='tmp_descVP' value='' />\n");
    	htmlStream.append("<input type='hidden' name='tmp_scopeVP' value='' />\n");
    }
    
    
    /**
     * Displays the save as viewpoint, execute and reset fields buttons if the parameters form must be shown
     * @param htmlStream The modified html stream
     */
    private void createParametersFormButtons(StringBuffer htmlStream) {
	logger.debug("IN");
    	boolean hasParametersFormToBeShown = hasParametersFormToBeDisplayed();
    	if (hasParametersFormToBeShown) {
	    	String executeMsg = msgBuilder.getMessage("sbi.execution.parametersForm.execute", httpRequest);
	    	String saveMsg = msgBuilder.getMessage("sbi.execution.parametersForm.save", httpRequest);
	    	String resetMsg = msgBuilder.getMessage("sbi.execution.parametersForm.reset", httpRequest);
	    	htmlStream.append("<div class='buttons'>\n");
	    	htmlStream.append("	<ul>\n");
	    	htmlStream.append("		<li><a href='javascript:void(0);' id='p_execute_button' class='button p_execute_button' onclick='document.getElementById(\"parametersForm" + requestIdentity + "\").submit();'><b><b><b>" + executeMsg + "</b></b></b></a></li>\n");
	    	htmlStream.append("		<li><a href='javascript:void(0);' id='p_save_button" + requestIdentity + "'    class='button p_save_button'><b><b><b>" + saveMsg + "</b></b></b></a></li>\n");
	    	htmlStream.append("		<li><a href='javascript:void(0);' id='p_reset_button'   class='button p_reset_button' onclick='clearFields" + requestIdentity + "()'><b><b><b>" + resetMsg + "</b></b></b></a></li>\n");
	    	htmlStream.append("	</ul>\n");
	    	htmlStream.append("</div>\n");
	    	
	    	htmlStream.append("<script>\n");
	    	htmlStream.append("var saveVPForm" + requestIdentity + ";\n");
	    	htmlStream.append("var viewpointname" + requestIdentity + ";\n");
	    	htmlStream.append("var viewpointdescr" + requestIdentity + ";\n");
	    	htmlStream.append("var comboVP" + requestIdentity + ";\n");
	    	htmlStream.append("Ext.onReady(function(){\n");
	    	htmlStream.append("    Ext.QuickTips.init();\n");
	    	htmlStream.append("    var store = new Ext.data.SimpleStore({fields: ['scopeName', 'scopeDescr'],data : [['Public', '" + msgBuilder.getMessage("SBIDev.docConf.viewPoint.scopePublic", httpRequest) + "'],['Private', '" + msgBuilder.getMessage("SBIDev.docConf.viewPoint.scopePrivate", httpRequest) + "']]});\n");
	    	htmlStream.append("    comboVP" + requestIdentity + " = new Ext.form.ComboBox({\n");
	    	htmlStream.append("		   id:'comboVP" + requestIdentity + "',\n");
	    	htmlStream.append("        fieldLabel: '" + msgBuilder.getMessage("SBIDev.docConf.viewPoint.scope", httpRequest) + "',\n");
	    	htmlStream.append("        store: store,\n");
	    	htmlStream.append("        displayField:'scopeDescr',\n");
	    	htmlStream.append("        valueField:'scopeName',\n");
	    	htmlStream.append("        typeAhead: true,\n");
	    	htmlStream.append("        mode: 'local',\n");
	    	htmlStream.append("        triggerAction: 'all',\n");
	    	htmlStream.append("        emptyText:'...',\n");
	    	htmlStream.append("        selectOnFocus:true\n");
	    	htmlStream.append("    });\n");
	    	htmlStream.append("    viewpointname" + requestIdentity + " = new Ext.form.TextField({\n");
	    	htmlStream.append("			id:'nameVP" + requestIdentity + "',\n");
	    	htmlStream.append("			name:'nameVP',\n");
	    	htmlStream.append("			allowBlank:false, \n");
	    	htmlStream.append("			inputType:'text',  \n");
	    	htmlStream.append("			fieldLabel:'" + msgBuilder.getMessage("SBIDev.docConf.viewPoint.name", httpRequest) + "' \n");
	    	htmlStream.append("    });\n");
	    	htmlStream.append("    viewpointdescr" + requestIdentity + " = new Ext.form.TextField({\n");
	    	htmlStream.append("			id:'descVP" + requestIdentity + "',\n");
	    	htmlStream.append("			name:'descVP',\n");
	    	htmlStream.append("			inputType:'text',  \n");
	    	htmlStream.append("			fieldLabel:'" + msgBuilder.getMessage("SBIDev.docConf.viewPoint.description", httpRequest) + "' \n");
	    	htmlStream.append("    });\n");
	    	htmlStream.append("    Ext.form.Field.prototype.msgTarget = 'side';\n");
	    	htmlStream.append("    saveVPForm" + requestIdentity + " = new Ext.form.FormPanel({\n");
	    	htmlStream.append("        labelWidth: 75,\n");
	    	htmlStream.append("        frame:true,\n");
	    	htmlStream.append("        bodyStyle:'padding:5px 5px 0',\n");
	    	htmlStream.append("        width: 350,\n");
	    	htmlStream.append("        height: 50,\n");
	    	htmlStream.append("        labelWidth: 150,\n");
	    	htmlStream.append("        defaults: {width: 230},\n");
	    	htmlStream.append("        defaultType: 'textfield',\n");
//	    	htmlStream.append("        onSubmit: Ext.emptyFn,\n");
//	    	htmlStream.append("        submit: function() {\n");
//	    	htmlStream.append("            this.getForm().getEl().dom.submit();\n");
//	    	htmlStream.append("        },\n");
	    	htmlStream.append("        items: [viewpointname" + requestIdentity + ",viewpointdescr" + requestIdentity + ",comboVP" + requestIdentity + "],\n");
	    	htmlStream.append("        buttons:[{text:'" + msgBuilder.getMessage("SBIDev.docConf.viewPoint.saveButt", httpRequest) + "',handler:function() {saveViewpoint" + requestIdentity + "()}}]\n");
	    	htmlStream.append("    });\n");
	    	htmlStream.append("});\n");
	    	htmlStream.append("var win_saveVP" + requestIdentity + ";\n");
	    	htmlStream.append("Ext.get('p_save_button" + requestIdentity + "').on('click', function(){\n");
	    	htmlStream.append("	if(!win_saveVP" + requestIdentity + ") {\n");
	    	htmlStream.append("		win_saveVP" + requestIdentity + " = new Ext.Window({\n");
	    	htmlStream.append("			id:'popup_saveVP" + requestIdentity + "',\n");
	    	htmlStream.append("			layout:'fit',\n");
	    	htmlStream.append("			width:500,\n");
	    	htmlStream.append("			height:200,\n");
	    	htmlStream.append("			closeAction:'hide',\n");
	    	htmlStream.append("			plain: true,\n");
	    	htmlStream.append("			title: '" + msgBuilder.getMessage("SBIDev.docConf.viewPoint.saveButt", httpRequest) + "',\n");
	    	htmlStream.append("			items: saveVPForm" + requestIdentity + "\n");
	    	htmlStream.append("		});\n");
	    	htmlStream.append("	};\n");
	    	htmlStream.append("	win_saveVP" + requestIdentity + ".show();\n");
	    	htmlStream.append("	}\n");
	    	htmlStream.append(");\n");
	    	
	    	htmlStream.append("function saveViewpoint" + requestIdentity + "() {\n");
	    	htmlStream.append("	var nameVP = viewpointname" + requestIdentity + ".getValue();\n");
	    	htmlStream.append("	var descVP = viewpointdescr" + requestIdentity + ".getValue();\n");
	    	htmlStream.append("	var scopeVP = comboVP" + requestIdentity + ".getValue();\n");
	    	htmlStream.append("	if (nameVP == null || nameVP.length == 0){\n");
	    	htmlStream.append("		alert('" + msgBuilder.getMessage("6000", httpRequest) + "');\n");
	    	htmlStream.append("		return;\n");
	    	htmlStream.append("	}\n");
	    	htmlStream.append("	if (scopeVP == null || scopeVP.length == 0){\n");
	    	htmlStream.append("		alert('" + msgBuilder.getMessage("6001", httpRequest) + "');\n");
	    	htmlStream.append("		return;\n");
	    	htmlStream.append("	}\n");
	    	htmlStream.append("	var mainForm = document.getElementById('parametersForm" + requestIdentity + "');\n");		
	    	htmlStream.append("	mainForm.SUBMESSAGEDET.value = '"+ SpagoBIConstants.VIEWPOINT_SAVE + "';\n");
	    	htmlStream.append("	mainForm.tmp_nameVP.value = nameVP;\n");
	    	htmlStream.append("	mainForm.tmp_descVP.value = descVP;\n");	
	    	htmlStream.append("	mainForm.tmp_scopeVP.value = scopeVP;\n");							
	    	htmlStream.append("	mainForm.submit();\n");
	    	htmlStream.append("}\n");
	    	
	    	
	    	htmlStream.append("</script>\n");
	    	
	    	
    	}
    	logger.debug("OUT");
    }

    /**
     * Returns true if the parameters form must be displayed:
     * the parameters forms must not be displayed if the document has no parameters or each parameters 
     * has a valid value and (is single value or is transient) 
     * @return true if the parameters form must be displayed, false otherwise
     */
    private boolean hasParametersFormToBeDisplayed() {
    	logger.debug("IN");
    	try {
			ExecutionInstance instance = getExecutionInstance();
	    	BIObject obj = instance.getBIObject();
	    	List parameters = obj.getBiObjectParameters();
	    	// if the document has no parameters returns false
	    	if (parameters == null || parameters.size() == 0) return false;
	    	
		    Iterator iter = parameters.iterator();
		    while (iter.hasNext()) {
		    	BIObjectParameter biparam = (BIObjectParameter) iter.next();
		    	if (!biparam.hasValidValues() || (!isSingleValue(biparam) && !biparam.isTransientParmeters())) {
		    		return true;
		    	}
		    }
		    return false;
    	} finally {
    		logger.debug("OUT");
    	}
    	
//    	// looks if the parameters are all single value
//    	boolean allParametersAreSingleValue = true;
//	    Iterator iter = parameters.iterator();
//	    while (iter.hasNext()) {
//	    	BIObjectParameter biparam = (BIObjectParameter) iter.next();
//	    	if (!isSingleValue(biparam)) {
//	    		allParametersAreSingleValue = false;
//	    		break;
//	    	}
//	    }
//	    // if all parameters are single value, returns false
//	    if (allParametersAreSingleValue) return false;
//    	
//    	// looks if modality is single_object 
//	    if (modality.equalsIgnoreCase(BIObjectsModule.SINGLE_OBJECT)) {
//	    	boolean allParametersHaveValidValues = true;
//    	    iter = parameters.iterator();
//    	    while (iter.hasNext()) {
//    	    	BIObjectParameter biparam = (BIObjectParameter) iter.next();
//    	    	if (!biparam.hasValidValues()) {
//    	    		allParametersHaveValidValues = false;
//    	    		break;
//    	    	}
//    	    }
//    	    logger.debug("OUT");
//    	    return allParametersHaveValidValues;
//	    } else {
//	    	// if modality is not single_object, parameters form must be shown
//	    	logger.debug("OUT");
//	    	return true;
//	    }
    }
    
    /**
     * Creates two empty hidden inputs and the correspondent JavaScript to
     * populate it in case there is a lookup call... - LOOKUP_OBJ_PAR_ID: is the
     * id of the parameter looked up - LOOKUP_TYPE: is the type of lookup (i.e.
     * list or cheklist)
     * 
     * @param htmlStream
     */
    private void createSetLookupFieldJSFunction(StringBuffer htmlStream) {
	logger.debug("IN");
	htmlStream.append("<input type='hidden' id='LOOKUP_OBJ_PAR_ID" + requestIdentity + "' name='' value=''/>\n");
	htmlStream.append("<input type='hidden' id='LOOKUP_TYPE" + requestIdentity + "' name='' value=''/>\n");

	htmlStream.append("<script type='text/javascript'>\n");
	htmlStream.append("	function setLookupField" + requestIdentity + "(idStr, type) {\n");
	htmlStream.append("		document.getElementById('LOOKUP_OBJ_PAR_ID" + requestIdentity + "').value= idStr;\n");
	htmlStream.append("		document.getElementById('LOOKUP_OBJ_PAR_ID" + requestIdentity
		+ "').name = 'LOOKUP_OBJ_PAR_ID';\n");
	htmlStream.append("		document.getElementById('LOOKUP_TYPE" + requestIdentity + "').value= type;\n");
	htmlStream.append("		document.getElementById('LOOKUP_TYPE" + requestIdentity + "').name = 'LOOKUP_TYPE';\n");
	htmlStream.append("	}\n");
	htmlStream.append("</script>\n");
	logger.debug("OUT");
    }

    /**
     * Creates an empty hidden input and the correspondent JavaScript to
     * populate it in case there is a panding delete... - PENDING_DELETE: setted
     * to true whene there are a pending delete
     * 
     * @param htmlStream
     */
    private void createSetDeleteFlagJSFunction(StringBuffer htmlStream) {
	logger.debug("IN");
	htmlStream.append("<input type='hidden' id='PENDING_DELETE" + requestIdentity + "' name='' value=''/>\n");

	htmlStream.append("<script type='text/javascript'>\n");
	htmlStream.append("	function setDeleteFlag" + requestIdentity + "() {\n");
	htmlStream.append("		document.getElementById('PENDING_DELETE" + requestIdentity + "').value = 'true';\n");
	htmlStream.append("		document.getElementById('PENDING_DELETE" + requestIdentity
		+ "').name = 'PENDING_DELETE';\n");
	htmlStream.append("	}\n");
	htmlStream.append("</script>\n");
	logger.debug("OUT");
    }

    private void createSetChangedFlagJSFunction(StringBuffer htmlStream) {
	logger.debug("IN");
	htmlStream.append("<script type='text/javascript'>\n");
	htmlStream.append("	function setChangedFlag" + requestIdentity + "(paramUrl) {\n");
	// htmlStream.append(" alert(paramUrl + 'IsChanged');\n");
	htmlStream.append("		document.getElementById(paramUrl + 'IsChanged' + '" + requestIdentity
		+ "').value = 'true';\n");
	htmlStream.append("	}\n");
	htmlStream.append("</script>\n");
	logger.debug("OUT");
    }

    private void createRefreshJSFunction(StringBuffer htmlStream) {
	logger.debug("IN");
	htmlStream.append("<script type='text/javascript'>\n");
	htmlStream.append("		function refresh" + requestIdentity + "(srcId, destId) {\n");
	htmlStream.append("			var srcValue = document.getElementById(srcId).value;\n");
	htmlStream.append("			var destObj = document.getElementById(destId);\n");
	htmlStream.append("			destObj.value = srcValue;\n");
	htmlStream.append("		}\n");
	htmlStream.append("</script>\n");
	logger.debug("OUT");
    }

    private void createClearFieldJSFunction(StringBuffer htmlStream) {
	logger.debug("IN");
	htmlStream.append("<script type='text/javascript'>\n");
	htmlStream.append("		function clearField" + requestIdentity + "(targetId) {\n");
	htmlStream.append("			document.getElementById(targetId).value = '';\n");
	htmlStream.append("		}\n");
	htmlStream.append("</script>\n");
	logger.debug("OUT");
    }

    private void createSelectAllTextJSFunction(StringBuffer htmlStream) {
	logger.debug("IN");
	htmlStream.append("<script type='text/javascript'>\n");
	htmlStream.append("		function selectAllText" + requestIdentity + "(id) {\n");
	htmlStream.append("			var object = document.getElementById(id);\n");
	htmlStream.append("			object.select( );\n");
	htmlStream.append("		}\n");
	htmlStream.append("</script>\n");
	logger.debug("OUT");
    }

    private void createClearFieldsJSFunction(StringBuffer htmlStream) {
	logger.debug("IN");
	htmlStream.append("<script type='text/javascript'>\n");
	htmlStream.append("		function clearFields" + requestIdentity + "() {\n");

	ExecutionInstance instance = getExecutionInstance();
	BIObject obj = instance.getBIObject();
	Iterator it = obj.getBiObjectParameters().iterator();
	String anId = null;
	while (it.hasNext()) {
	    BIObjectParameter biparam = (BIObjectParameter) it.next();
	    if (isSingleValue(biparam))
		continue;

	    String id = biparam.getParameterUrlName();
	    anId = id;
	    ModalitiesValue modVal = biparam.getParameter().getModalityValue();

	    String typeCode = modVal.getITypeCd();
	    String selectionType = modVal.getSelectionType();

	    if (typeCode.equalsIgnoreCase(SpagoBIConstants.INPUT_TYPE_MAN_IN_CODE)) {
		htmlStream.append(" document.getElementById('" + id + requestIdentity + "').value = '';\n");
		htmlStream.append(" document.getElementById('" + id + requestIdentity + "Desc').value = '';\n");
	    } else if (selectionType.equalsIgnoreCase("COMBOBOX")) {
		htmlStream.append(" document.getElementById('" + id + requestIdentity
			+ "').value = document.getElementById('" + id + requestIdentity + "Desc').value;\n");
		htmlStream.append(" document.getElementById('" + id + requestIdentity + "Desc').selectedIndex = 0;\n");
	    } else if (selectionType.equalsIgnoreCase("CHECK_LIST")) {
		htmlStream.append(" document.getElementById('" + id + requestIdentity + "').value = '';\n");
		htmlStream.append(" document.getElementById('" + id + requestIdentity + "Desc').value = '';\n");
	    } else if (selectionType.equalsIgnoreCase("LIST")) {
		htmlStream.append(" document.getElementById('" + id + requestIdentity + "').value = '';\n");
		htmlStream.append(" document.getElementById('" + id + requestIdentity + "Desc').value = '';\n");
	    }
	}
	htmlStream.append(" setDeleteFlag" + requestIdentity + "();\n");
	htmlStream.append(" setRefreshCorrelationFlag" + requestIdentity + "();\n");
	htmlStream.append(" document.getElementById('" + anId + requestIdentity + "').form.submit();\n");
	htmlStream.append("}\n");
	htmlStream.append("</script>\n");
	logger.debug("OUT");
    }

    private void createSetRefreshCorrelationJSFunction(StringBuffer htmlStream) {
	logger.debug("IN");
	htmlStream.append("<input type='hidden' id='REFRESH_CORRELATION" + requestIdentity + "' name='' value=''/>\n");

	htmlStream.append("<script type='text/javascript'>\n");
	htmlStream.append("	function setRefreshCorrelationFlag" + requestIdentity + "() {\n");
	htmlStream.append("		document.getElementById('REFRESH_CORRELATION" + requestIdentity + "').value = 'true';\n");
	htmlStream.append("		document.getElementById('REFRESH_CORRELATION" + requestIdentity
		+ "').name = 'REFRESH_CORRELATION';\n");
	htmlStream.append("	}\n");
	htmlStream.append("</script>\n");
	logger.debug("OUT");
    }

    private void createHiddenInput(StringBuffer htmlStream, String id, String name, String value) {
	htmlStream.append("<input type='hidden' name='" + name + "' id='" + id + requestIdentity + "' value='" + value
		+ "' />\n");
    }

    private void createParamValueHiddenInput(StringBuffer htmlStream, BIObjectParameter biparam) {
	createHiddenInput(htmlStream, biparam.getParameterUrlName(), biparam.getParameterUrlName(), GeneralUtilities
		.substituteQuotesIntoString(getParameterValuesAsString(biparam)));
    }

    private void createParamChangedHiddenInput(StringBuffer htmlStream, BIObjectParameter biparam) {
	createHiddenInput(htmlStream, biparam.getParameterUrlName() + "IsChanged", biparam.getParameterUrlName()
		+ "IsChanged", "false");
    }


    private void createParameterLabelDiv(StringBuffer htmlStream, BIObjectParameter biparam) {
        htmlStream.append("		<div style='clear:left;width:" + getParamLabelDivWidth() + "px;float:left;'>\n");
	
	htmlStream.append("			<span class='portlet-form-field-label'>");
	String toInsert = biparam.getLabel();
	//Puts an * if the parameter is mandatory
	List checks = biparam.getParameter().getChecks();
	if (!checks.isEmpty()){
		Iterator it = checks.iterator();	
		Check check = null;
		while (it.hasNext()){
		check = (Check)it.next();
		if (check.getValueTypeCd().equalsIgnoreCase("MANDATORY")){
			toInsert = "*"+toInsert;
			break;
		}else{
			toInsert = "&nbsp;"+toInsert;
		}
		} 
	  }else{
		  toInsert = "&nbsp;"+toInsert; 
	  }
	htmlStream.append(toInsert);
	htmlStream.append("</span>\n");
	htmlStream.append("		</div>\n");
	htmlStream.append("		<div class='div_detail_form'>\n");
	
    }

    /**
     * Given a <code>BIObjectParameter </code> object at input, creates from it
     * a dinamic HTML form. It is called from the <code>doStartTag </code>
     * method.
     * 
     * @param biparam
     *                The input BI object parameter
     * @param htmlStream
     *                The buffer containing all html code
     * @return The label of the BIObjectParameter of dependancy, if any
     */
    private String createParameterInputboxDiv(BIObjectParameter biparam, StringBuffer htmlStream) {
	logger.debug("IN");
	String objParFathLbl = null;

	// search for biparameter which dependes from this
	List lblBiParamDependent = new ArrayList();
	try {
	    lblBiParamDependent = DAOFactory.getObjParuseDAO().getDependencies(biparam.getId());
	} catch (Exception e) {
	    logger.error("Error while recovering dependencies " + " for biparm label " + biparam.getLabel(), e);
	    lblBiParamDependent = new ArrayList();
	}

	String typeCode = getModalityValue(biparam).getITypeCd();
	Parameter par = biparam.getParameter();
	String parType = "";
	if (par != null){
		parType = par.getType();
	}
	
	if (typeCode.equalsIgnoreCase(SpagoBIConstants.INPUT_TYPE_MAN_IN_CODE)) {
		if (parType.equals("DATE")){
			createDataInputButton(biparam, htmlStream, lblBiParamDependent);
		}else{
			createHTMLManInputButton(biparam, htmlStream, lblBiParamDependent);
		}
	    return null;
	}

	String selectionType = getModalityValue(biparam).getSelectionType();
	if (selectionType == null) {
	    if (typeCode.equalsIgnoreCase(SpagoBIConstants.INPUT_TYPE_FIX_LOV_CODE)
		    || typeCode.equalsIgnoreCase(SpagoBIConstants.INPUT_TYPE_SCRIPT_CODE))
		selectionType = "COMBOBOX";
	    else if (typeCode.equalsIgnoreCase(SpagoBIConstants.INPUT_TYPE_QUERY_CODE))
		selectionType = "LIST";
	}

	// looks for dependencies
	Object[] results = getObjectFather(biparam);
	BIObjectParameter objParFather = (BIObjectParameter) results[1];
	ObjParuse objParuse = (ObjParuse) results[0];
	if (objParFather != null && objParuse != null) {
	    // the BIobjectParameter is correlated to another BIObjectParameter
	    htmlStream.append("<input type='hidden' name='correlatedParuseIdForObjParWithId_" + biparam.getId()
		    + "' value='" + objParuse.getParuseId() + "' />\n");
	    objParFathLbl = objParFather.getLabel();
	}

	if (selectionType.equalsIgnoreCase("COMBOBOX")) {
	    createHTMLComboBox(biparam, htmlStream, lblBiParamDependent);
	} else if (selectionType.equalsIgnoreCase("RADIOBUTTON")) {
	    createHTMLRadioButton(biparam, htmlStream);
	} else if (selectionType.equalsIgnoreCase("LIST")) {
	    createHTMLListButton(biparam, false, htmlStream, lblBiParamDependent);
	} else if (selectionType.equalsIgnoreCase("CHECK_LIST")) {
		createHTMLListButton(biparam, false, htmlStream, lblBiParamDependent);
	}
	logger.debug("OUT");
	return objParFathLbl;
    }

    private void createDataInputButton(BIObjectParameter biparam, StringBuffer htmlStream, List lblBiParamDependent) {
	logger.debug("IN");
	SourceBean formatSB = ((SourceBean)ConfigSingleton.getInstance().getAttribute("SPAGOBI.DATE-FORMAT"));
	String format = (String) formatSB.getAttribute("format");
	logger.debug("DTE FORMAT:"+format);
	
	Date d = new Date();
	SimpleDateFormat f =  new SimpleDateFormat();
	f.applyPattern(format);
	try {
		d = f.parse(getParameterValuesAsString(biparam));
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	String datePickerFormat = "MM/dd/yyyy";
	String dateValue = StringUtils.dateToString(d, datePickerFormat);
	htmlStream.append("<script type='text/javascript' src='" + urlBuilder.getResourceLink(httpRequest, "/js/dojo/dojo.js" )+ "'></script>"
    		+ "<script type='text/javascript'>"
    		+ " dojo.require('dojo.widget.DropdownDatePicker');"
    		+ " "
    		+ " </script>"
    		+ " <input style='width:230px;' type='text' "
    		+ "	   name='" + biparam.getParameterUrlName()+ "' id='"+ biparam.getParameterUrlName()+requestIdentity+ "'" 
    		+ "	   dojoType='dropdowndatepicker' "
    		+ " saveFormat='"+format+"' displayFormat='"+format+"'"
    		+ " widgetId='startDateWidget' "
    		+ "    class='portlet-form-input-field' value='" + dateValue + "' "
    		+ "   onchange=\"refresh" + requestIdentity + "('" + biparam.getParameterUrlName()
    		+ requestIdentity + "','" + biparam.getParameterUrlName() + requestIdentity + "');");
    	if (lblBiParamDependent != null && lblBiParamDependent.size() > 0) {
    	    htmlStream.append("setRefreshCorrelationFlag" + requestIdentity + "();");
    	    htmlStream.append("this.form.submit();");
    	}
    	htmlStream.append("\" />\n");
    	logger.debug("OUT");
        }
 

    private void createHTMLManInputButton(BIObjectParameter biparam, StringBuffer htmlStream, List lblBiParamDependent) {
	htmlStream.append("<input style='width:230px;' type='text' " + "name='" + biparam.getParameterUrlName()
		+ "Desc' " + "id='" + biparam.getParameterUrlName() + requestIdentity + "Desc' "
		+ "class='portlet-form-input-field' " + "value='" + getParameterValuesAsString(biparam) + "' "
		+ "autocomplete='off' " + "onchange=\"refresh" + requestIdentity + "('" + biparam.getParameterUrlName()
		+ requestIdentity + "Desc','" + biparam.getParameterUrlName() + requestIdentity + "');");
	if (lblBiParamDependent != null && lblBiParamDependent.size() > 0) {
	    htmlStream.append("setRefreshCorrelationFlag" + requestIdentity + "();");
	    htmlStream.append("this.form.submit();");
	}
	htmlStream.append("\" />\n");
    }

    private void createHTMLListButton(BIObjectParameter biparam, boolean isReadOnly, StringBuffer htmlStream,
	    List lblBiParamDependent) {

	String parameterId=biparam.getId().toString();
	String parameterFieldName="par_"+parameterId+ biparam.getParameterUrlName();
	String userId = getProfile().getUserUniqueIdentifier().toString();
	String url = GeneralUtilities.getSpagoBIProfileBaseUrl(userId) + 
		"&PAGE=SelectParameterPage&objParId=" + biparam.getId().toString() +  
		"&parameterId=" + biparam.getParID().toString() + "&roleName=" + roleName +
		"&parameterFieldName=" + parameterFieldName + "&returnParam=" + biparam.getParameterUrlName() + requestIdentity +
		"&uuid=" + requestIdentity;
	
	// does this parameter depend on other parameters? if it is the case, puts in the url the values of the father parameters
	Map dependencies = getDependencies(biparam);
	Set keys = dependencies.keySet();
	Iterator keysIt = keys.iterator();
	while (keysIt.hasNext()) {
		String key = (String) keysIt.next();
		String value = (String) dependencies.get(key);
		if (value == null) continue;
		url += "&" + key + "=" + value;
	}
	url = GeneralUtilities.substituteQuotesIntoString(url); // javascript escape
	
	String id="p_search_button_"+biparam.getParameterUrlName();
	htmlStream.append("\n");
	String description = biparam.getParameterValuesDescription();
	if (description == null) 
		description = "";
	String tmpValue = (description.equals("null"))?"":description;
	htmlStream.append("<input value='" + GeneralUtilities.substituteQuotesIntoString(tmpValue) + "' type='text' style='width:230px;' " + "name='' " + "id='"+biparam.getParameterUrlName()+requestIdentity+"Desc' "
		+ "class='portlet-form-input-field' " + (isReadOnly ? "readonly='true' " : " "));
	htmlStream.append("onchange=\"refresh" + requestIdentity + "('" + biparam.getParameterUrlName() + requestIdentity + "Desc','" +  biparam.getParameterUrlName() + requestIdentity + "');" +
	   "setChangedFlag" + requestIdentity + "('" + biparam.getParameterUrlName() + "');");
	if (lblBiParamDependent != null && lblBiParamDependent.size() > 0) {
		htmlStream.append("setRefreshCorrelationFlag" + requestIdentity + "();");
		htmlStream.append("this.form.submit();");
	}
	htmlStream.append("\" autocomplete='off'/>");
	htmlStream.append("&nbsp;<a href='javascript:void(0);' id='"+id+"' style='text-decoration:none' >\n");
	htmlStream.append("	<img src= '" + encodeURL("/img/detail.gif") + "' title='Lookup' alt='Lookup' />\n");
	htmlStream.append("</a>\n");
	
	htmlStream.append("\n<script>\n");
	htmlStream.append("var win_"+parameterFieldName+";\n");
	htmlStream.append("Ext.get('"+id+"').on('click', function(){\n");
	htmlStream.append("	if(!win_"+parameterFieldName+") {\n");
	htmlStream.append("		win_"+parameterFieldName+" = new Ext.Window({\n");
	htmlStream.append("			id:'popup_" + parameterFieldName + "',\n");
	htmlStream.append("			title:'" + biparam.getLabel() + "',\n");
	htmlStream.append("			bodyCfg:{");
	htmlStream.append("				tag:'div'");
	htmlStream.append("    			,cls:'x-panel-body'");
	htmlStream.append("   			,children:[{");
	htmlStream.append("        			tag:'iframe',");
	htmlStream.append("        			name: 'iframe_" + parameterFieldName + "',");
	htmlStream.append("        			id  : 'iframe_" + parameterFieldName + "',");
	htmlStream.append("        			src: '" + url + "',");
	htmlStream.append("        			frameBorder:0,");
	htmlStream.append("        			width:'100%',");
	htmlStream.append("        			height:'100%',");
	htmlStream.append("        			style: {overflow:'auto'}   ");  
	htmlStream.append("   			}]");
	htmlStream.append("			},");
	htmlStream.append("			layout:'fit',\n");
	htmlStream.append("			width:800,\n");
	htmlStream.append("			height:320,\n");
	htmlStream.append("			closeAction:'hide',\n");
	htmlStream.append("			plain: true\n");
	htmlStream.append("		});\n");
	htmlStream.append("	};\n");
	htmlStream.append("	win_"+parameterFieldName+".show();\n");
	htmlStream.append("	}\n");
	htmlStream.append(");\n");	
	htmlStream.append("\n</script>");	
    }
    

    /**
     * Finds the parameters the input parameter depends on.
     * Returns a map: the key is the father parameter url name, the value is the current father parameter value
     * @param biparam The dependent (if it is the case) parameter
     * @return a map: the key is the father parameter url name, the value is the current father parameter value
     */
    private Map getDependencies(BIObjectParameter biparam) {
    	Map toReturn = new HashMap();
    	ExecutionInstance instance = getExecutionInstance();
    	BIObject obj = instance.getBIObject();
		BIObjectParameter objParFather = null;
		ObjParuse objParuse = null;
		ParameterValuesEncoder parValuesEncoder = new ParameterValuesEncoder();
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
						logger.debug("Found correlation:" +
								" dependent BIObjectParameter id = " + biparam.getId() + "," +
								" ParameterUse with id = " + paruseId + ";" +
								" BIObjectParameter father has id = " + objParuse.getObjParFatherId());
						// now we have to find the BIObjectParameter father of the correlation
						Integer objParFatherId = objParuse.getObjParFatherId();
						List parameters = obj.getBiObjectParameters();
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
							logger.error("Cannot find the BIObjectParameter father of the correlation");
							throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
						}
						if (objParFather.getParameterValues() != null && objParFather.getParameterValues().size() > 0) {
							String value = parValuesEncoder.encode(objParFather);
							toReturn.put(objParFather.getParameterUrlName(), value);
						}
					}
				}
			}
		} catch (EMFUserError e) {
			logger.error("Error while retrieving information from db", e);
			return new HashMap();
		}
		return toReturn;
	}
    
	private void createHTMLCheckListButton(BIObjectParameter biparam, boolean isReadOnly, StringBuffer htmlStream,
	    List lblBiParamDependent) {

	htmlStream.append("<input type='text' style='width:230px;' " + "name='" + biparam.getParameterUrlName()
		+ "Desc' " + "id='" + biparam.getParameterUrlName() + requestIdentity + "Desc' "
		+ "class='portlet-form-input-field' " + (isReadOnly ? "readonly='true' " : " ") + "value='"
		+ GeneralUtilities.substituteQuotesIntoString(getParameterDescription(biparam)) + "' "
		+ "onchange=\"refresh" + requestIdentity + "('" + biparam.getParameterUrlName() + requestIdentity
		+ "Desc','" + biparam.getParameterUrlName() + requestIdentity + "');" + "setChangedFlag"
		+ requestIdentity + "('" + biparam.getParameterUrlName() + "');");
	if (lblBiParamDependent != null && lblBiParamDependent.size() > 0) {
	    htmlStream.append("setRefreshCorrelationFlag" + requestIdentity + "();");
	    htmlStream.append("this.form.submit();");
	}
	htmlStream.append("\" ");
	htmlStream.append("onclick=\"selectAllText" + requestIdentity + "('" + biparam.getParameterUrlName()
		+ requestIdentity + "Desc');\" " + "autocomplete='off'/>\n");

	htmlStream.append("&nbsp;<input type='image' onclick='setLookupField" + requestIdentity + "(\"" + biparam.getId()
		+ "\", \"CHECK_LIST\")' \n");
	htmlStream.append("		src= '" + encodeURL("/img/detail.gif") + "' \n");
	htmlStream.append("		title='Lookup' alt='Lookup' \n");
	htmlStream.append("/>\n");
    }

    /**
     * Generate html code for a combobox form
     * 
     * @param biparam
     *                the parameter of the biobject
     * @param htmlStream
     *                the html of the combobox
     */
    private void createHTMLComboBox(BIObjectParameter biparam, StringBuffer htmlStream, List lblBiParamDependent) {
	try {

	    // create initial html
	    htmlStream.append("<select style='width:230px;font-size: 8pt;' " + "name='" + biparam.getParameterUrlName() + "Desc' "
		    + "id='" + biparam.getParameterUrlName() + requestIdentity + "Desc' "
		    + "class='portlet-form-field' "
		    +
		    // onchangeStr +
		    "onchange=\"refresh" + requestIdentity + "('" + biparam.getParameterUrlName() + requestIdentity
		    + "Desc', " + "'" + biparam.getParameterUrlName() + requestIdentity + "');");
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
	    if ((lovResult == null) || (lovResult.trim().equals(""))) {
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
	    while (iterParuse.hasNext()) {
		ParameterUse paruse = (ParameterUse) iterParuse.next();
		if (paruse.getIdLov().equals(lovId)) {
		    parusecorrId = paruse.getUseID();
		}
	    }

	    if (parusecorrId != null) {
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
	    // get all the rows of the result and build the option of the
	    // combobox
	    LovResultHandler lovResultHandler = new LovResultHandler(lovResult);
	    List rows = lovResultHandler.getRows();
	    Iterator it = rows.iterator();
	    while (it.hasNext()) {
		SourceBean row = (SourceBean) it.next();
		String value = (String) row.getAttribute(valueColumn);
		String description = (String) row.getAttribute(descriptionColumn);
		String selected = "";
		if (getParameterValuesAsString(biparam).equals(value))
		    selected = "selected=\"selected\"";
		htmlStream.append("<option value='" + GeneralUtilities.substituteQuotesIntoString(value) + "' "
			+ selected + ">" + description + "</option>\n");

	    }
	    // close combo box
	    htmlStream.append("</select>\n");

	} catch (Exception ex) {
	    logger.error("Error while creating html combo box " + " for biparam " + biparam.getLabel(), ex);
	}
    }

    private void eraseParameterValues(BIObjectParameter biparam) {
	biparam.setParameterValues(null);
	HashMap paramsDescriptionMap = (HashMap) contextManager.get("PARAMS_DESCRIPTION_MAP");
	paramsDescriptionMap.put(biparam.getParameterUrlName(), "");
    }

    private void createHTMLRadioButton(BIObjectParameter biparam, StringBuffer htmlStream) {
	try {
	    List l = getXMLValuesBean(biparam).getAttributeAsList("LOV-ELEMENT");
	    Iterator it = l.iterator();
	    SourceBean sbTemp = null;
	    String desc = null;
	    String val = null;
	    while (it.hasNext()) {
		sbTemp = (SourceBean) it.next();
		desc = (String) sbTemp.getAttribute("DESC");
		val = (String) sbTemp.getAttribute("VALUE");
		String selected = "";
		if (getParameterValuesAsString(biparam).equals(val))
		    selected = "checked";
		htmlStream.append("<input type='radio' name='" + biparam.getParameterUrlName() + "' value='" + val
			+ "' " + selected + ">" + desc + "</input>\n");
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

    private void createNoParametersMessage(StringBuffer htmlStream) {
	htmlStream.append("		<span class='portlet-font' >\n");
	htmlStream.append("			"
		+ msgBuilder.getMessage("SBIDev.docConf.subBIObject.newComposition1", "messages", httpRequest) + "\n");
	htmlStream.append("			<a href=\"javascript:document.getElementById('parametersForm" + requestIdentity
		+ "').submit();\"\n");
	htmlStream.append("				class='portlet-form-field-label'\n");
	htmlStream.append("				onmouseover=\"this.style.color='#9297ac';\"\n");
	htmlStream.append("				onmouseout=\"this.style.color='#074B88';\">");
	htmlStream.append(msgBuilder.getMessage("SBIDev.docConf.subBIObject.newComposition2", "messages", httpRequest) + "</a>\n");
	htmlStream.append("		</span>\n");
	htmlStream.append("		<br/>\n");
	htmlStream.append("		<br/>\n");
    }

    /**
     * @return en extimation of the parameter label div in px
     */
    private int getParamLabelDivWidth() {
	int maxLength = 0;
	ExecutionInstance instance = getExecutionInstance();
	BIObject obj = instance.getBIObject();
	Iterator iterPars = obj.getBiObjectParameters().iterator();
	while (iterPars.hasNext()) {
	    BIObjectParameter biparam = (BIObjectParameter) iterPars.next();
	    String label = biparam.getLabel();
	    if (label.length() > maxLength)
		maxLength = label.length();
	}

	int labelDivWidth = maxLength * PIXEL_PER_CHAR;

	return labelDivWidth;
    }

    private String getParameterValuesAsString(BIObjectParameter biparam) {
	String valuesStr = "";
	List values = biparam.getParameterValues();
	if (values == null)
	    return valuesStr;

	for (int i = 0; i < values.size(); i++)
	    valuesStr += (i == 0 ? "" : ";") + GeneralUtilities.substituteQuotesIntoString((String) values.get(i));

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
			// the ModalitiesValue of the BIObjectParameter
			// corresponds to a ParameterUse correlated
			objParuse = aObjParuse;
			logger.debug("Found correlation:" + " dependent BIObjectParameter id = " + biparam.getId()
				+ "," + " ParameterUse with id = " + paruseId + ";"
				+ " BIObjectParameter father has id = " + objParuse.getObjParFatherId());
			// now we have to find the BIObjectParameter father of
			// the correlation
			Integer objParFatherId = objParuse.getObjParFatherId();
			ExecutionInstance instance = getExecutionInstance();
			BIObject obj = instance.getBIObject();
			List parameters = obj.getBiObjectParameters();
			Iterator i = parameters.iterator();
			while (i.hasNext()) {
			    BIObjectParameter aBIObjectParameter = (BIObjectParameter) i.next();
			    if (aBIObjectParameter.getId().equals(objParFatherId)) {
				objParFather = aBIObjectParameter;
				break;
			    }
			}
			if (objParFather == null) {
			    // the BIObjectParameter father of the correlation
			    // was not found
			    logger.error("Cannot find the BIObjectParameter father of the correlation");
			    throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
			}
			break;
		    }
		}
	    }
	} catch (EMFUserError e) {
	    logger.error("Error while retrieving information from db", e);
	    e.printStackTrace();
	}

	return new Object[] { objParuse, objParFather };
    }

    private SourceBean getXMLValuesBean(BIObjectParameter biparam) throws SourceBeanException {
	String stringXMLValues = biparam.getParameter().getModalityValue().getLovProvider();
	stringXMLValues = GeneralUtilities.cleanString(stringXMLValues);
	return SourceBean.fromXMLString(stringXMLValues);
    }

    private String getParameterDescription(BIObjectParameter biparam) {
	String description = null;

	HashMap paramsDescriptionMap = (HashMap) contextManager.get("PARAMS_DESCRIPTION_MAP");
	description = (String) paramsDescriptionMap.get(biparam.getParameterUrlName());

	return description;
    }

    /**
     * Gets the modality.
     * 
     * @return modality The modality String to return.
     */
    public String getModality() {
	return modality;
    }

    /**
     * Sets the modality.
     * 
     * @param modality The modality to set.
     */
    public void setModality(String modality) {
	this.modality = modality;
    }

    /**
     * Gets the module name.
     * 
     * @return the module name
     */
    public String getModuleName() {
	return moduleName;
    }

    /**
     * Sets the module name.
     * 
     * @param moduleName the new module name
     */
    public void setModuleName(String moduleName) {
	this.moduleName = moduleName;
    }

    /**
     * Gets the request identity.
     * 
     * @return the request identity
     */
    public String getRequestIdentity() {
	return requestIdentity;
    }

    /**
     * Sets the request identity.
     * 
     * @param requestIdentity the new request identity
     */
    public void setRequestIdentity(String requestIdentity) {
	this.requestIdentity = requestIdentity;
    }
}

