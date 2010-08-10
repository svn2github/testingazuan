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
import it.eng.spago.base.RequestContainerPortletAccess;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.ResponseContainerPortletAccess;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dbaccess.DataConnectionManager;
import it.eng.spago.dbaccess.Utils;
import it.eng.spago.dbaccess.sql.DataConnection;
import it.eng.spago.dbaccess.sql.SQLCommand;
import it.eng.spago.dbaccess.sql.result.DataResult;
import it.eng.spago.dbaccess.sql.result.ScrollableDataResult;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.ModalitiesValue;
import it.eng.spagobi.bo.ObjParuse;
import it.eng.spagobi.bo.Parameter;
import it.eng.spagobi.bo.ParameterUse;
import it.eng.spagobi.bo.ScriptDetail;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IObjParuseDAO;
import it.eng.spagobi.bo.dao.IParameterUseDAO;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.PortletUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;
import it.eng.spagobi.utilities.GeneralUtilities;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Defines a tag to create a dinamic JSP page
 * 
 * @author sulis
 */
public class DynamicPageTag extends TagSupport {


	private RenderResponse renderResponse = null;
	private RenderRequest renderRequest = null;
	private String modality = null;
	private String actor = null;
	private String moduleName = "";
	PortletRequest portReq = null;
	private BIObject obj = null;
	private SessionContainer session = null;
	private EMFErrorHandler errorHandler = null;
	
	
	
	
	/**
	 * @see it.eng.spagobi.presentation.tags.ListTag#doStartTag()
	 * 
	 */
	public int doStartTag() throws JspException {
		
		StringBuffer htmlStream = new StringBuffer();
		HttpServletRequest httpRequest = (HttpServletRequest) pageContext.getRequest();
		renderResponse =(RenderResponse)httpRequest.getAttribute("javax.portlet.response");
		renderRequest =(RenderRequest)httpRequest.getAttribute("javax.portlet.request");
		RequestContainer requestContainer = RequestContainerPortletAccess.getRequestContainer(httpRequest);
		ResponseContainer responseContainer = ResponseContainerPortletAccess.getResponseContainer(httpRequest);
		errorHandler = responseContainer.getErrorHandler();
		session = requestContainer.getSessionContainer();
		obj = (BIObject) session.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
		portReq = PortletUtilities.getPortletRequest();
		
		// an empty input type hidden and the correspondent JavaScript to popolate it in case there is a lookup call (in case of query or script) 
		htmlStream.append("<input type='hidden' id='LOOKUP_OBJ_PAR_ID' name='' value=''/>\n");
		htmlStream.append("<input type='hidden' id='LOOKUP_TYPE' name='' value=''/>\n");
		htmlStream.append("<input type='hidden' id='PENDING_DELETE' name='' value=''/>\n");
		htmlStream.append("<script type='text/javascript'>\n");
		htmlStream.append("	function setLookupField(idStr, type) {\n");
		htmlStream.append("		document.getElementById('LOOKUP_OBJ_PAR_ID').value= idStr;\n");
		htmlStream.append("		document.getElementById('LOOKUP_OBJ_PAR_ID').name = 'LOOKUP_OBJ_PAR_ID';\n");
		htmlStream.append("		document.getElementById('LOOKUP_TYPE').value= type;\n");
		htmlStream.append("		document.getElementById('LOOKUP_TYPE').name = 'LOOKUP_TYPE';\n");
		htmlStream.append("	}\n");
		htmlStream.append("	function setDelateFlag() {\n");
		htmlStream.append("		document.getElementById('PENDING_DELETE').value = 'true';\n");	
		htmlStream.append("		document.getElementById('PENDING_DELETE').name = 'PENDING_DELETE';\n");	
		htmlStream.append("	}\n");
		htmlStream.append("</script>\n");
		
		List parameters = obj.getBiObjectParameters();
		
		 // gets a estimation of the max label length
        int maxLength = 0;
        Iterator iterPars = parameters.iterator();
        while(iterPars.hasNext()) {
        	BIObjectParameter biparam = (BIObjectParameter)iterPars.next();
        	String label = biparam.getLabel();
            if(label.length() > maxLength) 
            	maxLength = label.length();
        }
        // assuming 9 px for each character
        int widthLabel = maxLength * 9; 
		int widthForm = widthLabel + 300;
		
		htmlStream.append("<div class='div_detail_area_forms' style='width:"+widthForm+"px;'>\n");
      
        Iterator iter = parameters.iterator();
        while(iter.hasNext()) {
        	BIObjectParameter biparam = (BIObjectParameter)iter.next();
        	boolean createLabelAndForm = true;
        	
        	if (biparam.isTransientParmeters()) {
        		createLabelAndForm = false;
    			List biparValues = biparam.getParameterValues();
    			Object value = biparValues.get(0);
        		htmlStream.append("<input type='hidden' name='"+biparam.getParameterUrlName()+"' value='"+value+"' />\n");
        	} else {
            	Parameter par = biparam.getParameter();
            	ModalitiesValue paruse = par.getModalityValue();
            	String inputType = paruse.getITypeCd();
            	if(inputType.equalsIgnoreCase("SCRIPT")) {
            		String lov = paruse.getLovProvider();
            		ScriptDetail scriptDet = null;
            		try{
            			scriptDet = ScriptDetail.fromXML(lov);
            		} catch(Exception e) {
            			scriptDet = null;
            		}
            		if( (scriptDet!=null) && scriptDet.isSingleValue()) {
            			createLabelAndForm = false;
            			List biparValues = biparam.getParameterValues();
            			Object value = biparValues.get(0);
                		htmlStream.append("<input type='hidden' name='"+biparam.getParameterUrlName()+"' value='"+value+"' />\n");
            		}
            	}
        	}
     	
        	if(createLabelAndForm) {
        		htmlStream.append("		<div style='clear:left;width:"+widthLabel+"px;float:left;'>\n");
        		htmlStream.append("			<span class='portlet-form-field-label'>\n");
        		htmlStream.append(biparam.getLabel());
        		htmlStream.append("			</span>\n");
        		htmlStream.append("		</div>\n");
        		htmlStream.append("		<div class='div_detail_form'>\n");
        		String objParFatherLabel = createHTMLForm(biparam, htmlStream);
        		
        		

        		if (objParFatherLabel != null) {
        			String correlation = PortletUtilities.getMessage("SBIDev.docConf.execBIObjectParams.correlatedParameter", "messages");
        			correlation += " " + objParFatherLabel ;
        			htmlStream.append("		<img src= '" + renderResponse.encodeURL(renderRequest.getContextPath() + "/img/parCorrelation.gif") + "' ");
        			htmlStream.append("		 title='"+correlation+"' alt='"+correlation+"' />");
        		}
        		htmlStream.append("		</div>\n");
        	}
        }
        
        htmlStream.append("		<div style='clear:left;width:"+widthLabel+"px;float:left;'>\n");
        htmlStream.append("			&nbsp;\n");
        htmlStream.append("		</div>\n");
        htmlStream.append("		<div class='div_detail_form'>\n");
        htmlStream.append("				<a href='javascript:void(0)' onclick='clearFields()' class='portlet-form-field-label'>\n");
        String resetFieldsLbl = PortletUtilities.getMessage("SBIDev.docConf.execBIObjectParams.resetFields", "messages");
        htmlStream.append("					" + resetFieldsLbl + "\n");
        htmlStream.append("				</a>\n");
        htmlStream.append("		</div>\n");
		htmlStream.append("</div>\n");
		

        htmlStream.append("<script type='text/javascript'>\n");
        htmlStream.append("function clearFields() {\n");
        iter = parameters.iterator();
        while (iter.hasNext()) {
        	BIObjectParameter biparam = (BIObjectParameter) iter.next();
        	String id = biparam.getParameterUrlName();
    		ModalitiesValue modVal = biparam.getParameter().getModalityValue();
    		
    		String typeCode = modVal.getITypeCd();
    		String selectionType = modVal.getSelectionType();
    		
    		if(typeCode.equalsIgnoreCase(SpagoBIConstants.INPUT_TYPE_MAN_IN_CODE)) {
    			htmlStream.append(" document.getElementById('" + id + "').value = '';\n");
    		} else if(selectionType.equalsIgnoreCase("COMBOBOX")) {
    			htmlStream.append(" document.getElementById('" + id + "').selectedIndex = 0;\n");
    		} else if(selectionType.equalsIgnoreCase("CHECK_LIST")) {
    			htmlStream.append(" document.getElementById('" + id + "').value = '';\n");
    			htmlStream.append(" setDelateFlag();\n");
    		} else if(selectionType.equalsIgnoreCase("LIST")) {
    			htmlStream.append(" document.getElementById('" + id + "').value = '';\n");
    		}
    		
    		/*
    		String typeCode = modVal.getITypeCd();
    		if(typeCode.equalsIgnoreCase(SpagoBIConstants.INPUT_TYPE_FIX_LOV_CODE)) {
    			htmlStream.append(" document.getElementById('" + id + "').selectedIndex = 0;\n");
    		} else if(typeCode.equalsIgnoreCase(SpagoBIConstants.INPUT_TYPE_MAN_IN_CODE)) {
    			htmlStream.append(" document.getElementById('" + id + "').value = '';\n");
    		} else if(typeCode.equalsIgnoreCase(SpagoBIConstants.INPUT_TYPE_QUERY_CODE) ||
    				   typeCode.equalsIgnoreCase(SpagoBIConstants.INPUT_TYPE_SCRIPT_CODE)) {
    			htmlStream.append(" document.getElementById('" + id + "').value = '';\n");
    		}
    		*/
        }
        htmlStream.append("}\n");
        htmlStream.append("</script>\n");

       
		try {
			pageContext.getOut().print(htmlStream);
		} catch(IOException ioe) {
			SpagoBITracer.major("", "DynamicPageTag", "doStartTag", "cannot start tag: IO exception occurred",ioe);
		}
		
		return SKIP_BODY;
	}
	
	/**
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspException {
		TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.INFORMATION, "TitleTag::doEndTag:: invocato");
		return super.doEndTag();
	}
	
	
	
	 
	
	private String getValue(BIObjectParameter biparam) {
		String value = "";
		List values = biparam.getParameterValues();
		if(values == null) return value;
		
		if (values.size() == 1) {
			value = (String) values.get(0);
		}
		else {
			for(int i = 0; i < values.size(); i++) value += (String) values.get(i) + "; ";
		}
		return value;
	}
	
	/**
	 * Given a <code>BIObjectParameter </code> object at input, creates from it a dinamic 
	 * HTML form. It is called from the <code>doStartTag </code> method.
	 * 
	 * @param biparam The input BI object parameter
	 * @param htmlStream The buffer containing all html code 
	 * @return The label of the BIObjectParameter of dependancy, if any
	 */
	private String createHTMLForm(BIObjectParameter biparam, StringBuffer htmlStream) {
				
		String typeCode = getModalityValue(biparam).getITypeCd();
		
		if(typeCode.equalsIgnoreCase(SpagoBIConstants.INPUT_TYPE_MAN_IN_CODE)) {
			htmlStream.append("<input style='width:230px;' type='text' name='"+biparam.getParameterUrlName()+"' id='"+biparam.getParameterUrlName()+"' value='" + getValue(biparam) + "' class='portlet-form-input-field' />\n");
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
		
		if(selectionType.equalsIgnoreCase("COMBOBOX")) {
			createHTMLComboBox(biparam, htmlStream);
		}
		else if(selectionType.equalsIgnoreCase("RADIOBUTTON")) {
			createHTMLRadioButton(biparam, htmlStream);		
		}		
		else if(selectionType.equalsIgnoreCase("LIST")) {
			createHTMLListButton(biparam, htmlStream);
		}		
		else if(selectionType.equalsIgnoreCase("CHECK_LIST")) {
			createHTMLCheckListButton(biparam, htmlStream);
		}
		
		if (typeCode.equalsIgnoreCase(SpagoBIConstants.INPUT_TYPE_QUERY_CODE)) {			
			// looks for dependencies (this will be ignored if the lov is not of query type)
			Object[] results = getObjectFather(biparam);
			BIObjectParameter objParFather = (BIObjectParameter)results[1];
			ObjParuse objParuse = (ObjParuse)results[0];
			
			if (objParFather != null && objParuse != null) {
				// the BIobjectParameter is correlated to another BIObjectParameter
				htmlStream.append("<input type='hidden' name='correlatedParuseIdForObjParWithId_" + biparam.getId() + "' value='" + objParuse.getParuseId() + "' />\n");
				return objParFather.getLabel();
			}
		}
		
		return null;		
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
		stringXMLValues = PortletUtilities.cleanString(stringXMLValues);
		return SourceBean.fromXMLString(stringXMLValues);
	}
	
	private void createHTMLListButton(BIObjectParameter biparam, StringBuffer htmlStream) {
			htmlStream.append("<input type='text' style='width:230px;' " + 
							  	"name='" + biparam.getParameterUrlName() +"' "+
							  	"id='" + biparam.getParameterUrlName() + "' " +
								"class='portlet-form-input-field' readonly='true' " +
								"value='" + GeneralUtilities.substituteQuotesIntoString(getValue(biparam)) + "' />\n");
			
			htmlStream.append("<input type='image' onclick='setLookupField(\"" + biparam.getId() + "\", \"LIST\")' \n");
			htmlStream.append("		src= '" + renderResponse.encodeURL(renderRequest.getContextPath() + "/img/detail.gif") + "' \n");
			htmlStream.append("		title='Lookup' alt='Lookup' \n");
			htmlStream.append("/>\n");
	}	
	
	private void createHTMLCheckListButton(BIObjectParameter biparam, StringBuffer htmlStream) {
		htmlStream.append("<input type='text' style='width:230px;' " + 
						  	"name='" + biparam.getParameterUrlName() +"' "+
						  	"id='" + biparam.getParameterUrlName() + "' " +
							"class='portlet-form-input-field' readonly='true' " +
							"value='" + GeneralUtilities.substituteQuotesIntoString(getValue(biparam)) + "' />\n");
		
		htmlStream.append("<input type='image' onclick='setLookupField(\"" + biparam.getId() + "\", \"CHECK_LIST\")' \n");
		htmlStream.append("		src= '" + renderResponse.encodeURL(renderRequest.getContextPath() + "/img/detail.gif") + "' \n");
		htmlStream.append("		title='Lookup' alt='Lookup' \n");
		htmlStream.append("/>\n");
}	
	
	private SourceBean getLov(BIObjectParameter biparam) throws SourceBeanException {
		SourceBean lov = null;
		
		String inputType = biparam.getParameter().getModalityValue().getITypeCd();
		
			
		// different input type call different delegated class to build the list 
		if (inputType.equalsIgnoreCase(SpagoBIConstants.INPUT_TYPE_QUERY_CODE)) {			
			String queryDetXML = biparam.getParameter().getModalityValue().getLovProvider();
			SourceBean queryXML = SourceBean.fromXMLString(queryDetXML);
			String visibleColumns = ((SourceBean) queryXML.getAttribute("VISIBLE-COLUMNS")).getCharacters();
			String valueColumn = ((SourceBean) queryXML.getAttribute("VALUE-COLUMN")).getCharacters();
			String pool = ((SourceBean) queryXML.getAttribute("CONNECTION")).getCharacters();
			String statement = ((SourceBean) queryXML.getAttribute("STMT")).getCharacters();
			IEngUserProfile profile = (IEngUserProfile) session.getPermanentContainer().getAttribute(IEngUserProfile.ENG_USER_PROFILE);
			try {
				statement = GeneralUtilities.substituteProfileAttributesInString(statement, profile);
			} catch (Exception e) {
				TracerSingleton.log(Constants.NOME_MODULO,
						TracerSingleton.CRITICAL, "getLov: error while substituting profile attributes in query", e);
			}
			
			// execute query
			SourceBean result = null;
			DataConnectionManager dataConnectionManager = null;
			DataConnection dataConnection = null;
			SQLCommand sqlCommand = null;
			DataResult dataResult = null;
			try {
				dataConnectionManager = DataConnectionManager.getInstance();
				dataConnection = dataConnectionManager.getConnection(pool);
				sqlCommand = dataConnection.createSelectCommand(statement);
				dataResult = sqlCommand.execute();
				ScrollableDataResult scrollableDataResult = (ScrollableDataResult) dataResult
						.getDataObject();
				result = scrollableDataResult.getSourceBean();
			} catch (Exception ex) {
				TracerSingleton.log(Constants.NOME_MODULO,
						TracerSingleton.CRITICAL, "executeSelect:", ex);
			} finally {
				Utils.releaseResources(dataConnection, sqlCommand, dataResult);
			}	
			
			lov = new SourceBean("LOV");
			List rows = result.getAttributeAsList("ROW");
			for(Iterator it = rows.iterator(); it.hasNext(); ) {
				SourceBean row = (SourceBean)it.next();
				String value = (String)row.getAttribute(valueColumn);
				SourceBean lovElement = new SourceBean("LOV-ELEMENT");
				lovElement.setAttribute("DESC", value);
				lovElement.setAttribute("VALUE", value);
				lov.setAttribute(lovElement);
			}
		} else if (inputType.equalsIgnoreCase(SpagoBIConstants.INPUT_TYPE_FIX_LOV_CODE)) {			
			lov = getXMLValuesBean(biparam);
		} else if(inputType.equalsIgnoreCase(SpagoBIConstants.INPUT_TYPE_SCRIPT_CODE)) {			
			System.out.println(SpagoBIConstants.INPUT_TYPE_SCRIPT_CODE);
		}	
				
		return lov;
	}
	
	private void createHTMLComboBox(BIObjectParameter biparam, StringBuffer htmlStream) {
    	try{
	    	htmlStream.append("<select style='width:230px;' " +  
	    			 			"name='" + biparam.getParameterUrlName() + "' " +
	    			 			"id='"+ biparam.getParameterUrlName()+ "' " +
	    			 			"class='portlet-form-field' >\n");
			
	    	
	    	
	    	//List l = getXMLValuesBean(biparam).getAttributeAsList("LOV-ELEMENT");
	    	List l = getLov(biparam).getAttributeAsList("LOV-ELEMENT");
			
	    	Iterator it = l.iterator();
			SourceBean sbTemp = null;
			String desc = null;
			String val = null;
			while(it.hasNext()) {
				sbTemp = (SourceBean)it.next();
				desc = (String)sbTemp.getAttribute("DESC");
				val = (String)sbTemp.getAttribute("VALUE");
				String selected = "";
				if (getValue(biparam).equals(val)) selected = "selected=\"selected\"";
				htmlStream.append("<option value='"+GeneralUtilities.substituteQuotesIntoString(val)+"' " + selected + ">"+desc+"</option>\n");
			}
			htmlStream.append("</select>\n");
	    }catch (Exception ex) {
	    	ex.printStackTrace();
	    }
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
				if (getValue(biparam).equals(val)) selected = "checked";
				htmlStream.append("<input type='radio' name='"+biparam.getParameterUrlName()+"' value='"+val+"' " + selected + ">"+desc+"</input>\n");
			}
	    }catch (Exception ex) {
	    	ex.printStackTrace();
	    }
    }
    
	private void createHTMLCheckBox(BIObjectParameter biparam, StringBuffer htmlStream) {
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
				if (getValue(biparam).equals(val)) selected = "checked";
				htmlStream.append("<input type='checkbox' name='"+biparam.getParameterUrlName()+"' value='"+val+"' " + selected + ">"+desc+"</input>\n");
			}
	    }catch (Exception ex) {
	    	ex.printStackTrace();
	    }
    }
	
	
	
	
	
	
	/**
	 * Creates buttons for dinamic page, adding some code to the HTML Stream.
	 * 
	 * @param htmlStream Contains all Html input code 
	 */
	private void createButtons(StringBuffer htmlStream) {
		
		String execLabel = PortletUtilities.getMessage("SBIDev.docConf.execBIObjectParams.execButt", "messages");
		String backLabel = PortletUtilities.getMessage("SBIDev.docConf.execBIObjectParams.backButt", "messages");
		
		PortletURL backUrl = renderResponse.createActionURL();
		backUrl.setParameter("PAGE", "BIObjectsPage");
		backUrl.setParameter(SpagoBIConstants.ACTOR, actor);
		
		htmlStream.append("<table>\n");
		htmlStream.append("		<tr>\n");
		htmlStream.append("			<td width='30px'>&nbsp;</td>\n");
		htmlStream.append("			<td width='80px'>\n");		
		htmlStream.append("				<input type='image' src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/exec.png")+"' alt='exec' />\n");
		//htmlStream.append("				</form>\n");
		htmlStream.append("			</td>\n");
		htmlStream.append("			<td width='30px'>&nbsp;</td>\n");
		
		if(!moduleName.equalsIgnoreCase("SingleObjectExecutionModule")) {
			htmlStream.append("			<td width='80px'>\n");
			htmlStream.append("				<a href='"+backUrl.toString()+"' class='portlet-menu-item'>\n");
			htmlStream.append("   				<img src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")+"' />\n");
			htmlStream.append("				</a>\n");
			htmlStream.append("			</td>\n");
		} else {
			htmlStream.append("			<td width='80px'>&nbsp;</td>\n");
		}
		
		htmlStream.append("		</tr>\n");
		htmlStream.append("		<tr>\n");
		htmlStream.append("			<td width='30px'>&nbsp;</td>\n");
		htmlStream.append("			<td width='80px'>\n");
		char doubleApic = '"';
		htmlStream.append("				<a href="+doubleApic+"javascript:document.getElementById('paramsValueForm').submit()"+doubleApic+">\n");
		htmlStream.append(					execLabel);
		htmlStream.append("				</a>\n");				
		htmlStream.append("			</td>\n");
		htmlStream.append("			<td width='30px'>&nbsp;</td>\n");
		
		if(!moduleName.equalsIgnoreCase("SingleObjectExecutionModule")) {
			htmlStream.append("			<td width='80px'>\n");
			htmlStream.append("				<a href='"+backUrl.toString()+"'>");
			htmlStream.append(					backLabel);
			htmlStream.append("				</a>\n");	
			htmlStream.append("			</td>\n");
		} else {
			htmlStream.append("			<td width='80px'>&nbsp;</td>\n");
		}
		
		htmlStream.append("		</tr>\n");
		htmlStream.append("</table>\n");
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
	
	
