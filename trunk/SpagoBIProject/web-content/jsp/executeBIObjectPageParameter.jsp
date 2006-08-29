<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="javax.portlet.PortletURL,
                 java.util.List,
                 java.util.Iterator,
                 it.eng.spagobi.constants.ObjectsTreeConstants,
                 it.eng.spagobi.services.modules.ExecuteBIObjectModule,
                 it.eng.spagobi.constants.AdmintoolsConstants,
                 it.eng.spagobi.constants.SpagoBIConstants,
                 it.eng.spagobi.utilities.GeneralUtilities,
                 it.eng.spagobi.bo.BIObject,
                 it.eng.spago.security.IEngUserProfile,
                 it.eng.spago.base.SessionContainer,
                 it.eng.spagobi.utilities.PortletUtilities,
                 it.eng.spago.navigation.LightNavigationManager" %>

<% 
    // get object from the session 
    BIObject obj = (BIObject)aSessionContainer.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);  
	//substitute profile attributes for FIX LOV parameters
	GeneralUtilities.subsituteBIObjectParametersLovProfileAttributes(obj,aSessionContainer);
    // get profile of the user
    SessionContainer permSess = aSessionContainer.getPermanentContainer();
    IEngUserProfile profile = (IEngUserProfile)permSess.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
    String currentUser = (String)profile.getUserUniqueIdentifier();
    // get response of the module
    SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("ExecuteBIObjectModule"); 
	// get actor from session
    String actor = (String)aSessionContainer.getAttribute(SpagoBIConstants.ACTOR); 
	// get subObject List
	List subObjs = (List)moduleResponse.getAttribute(SpagoBIConstants.SUBOBJECT_LIST);
	String typeObj = (String)moduleResponse.getAttribute(SpagoBIConstants.BIOBJECT_TYPE_CODE);
	
	// try to get the the "NO_PARAMETERS", if it is present the object has only subobjects but not
	// parameters
	String noParsStr = (String)moduleResponse.getAttribute("NO_PARAMETERS");
	boolean noPars = false;
	if(noParsStr!=null)
		noPars = true;
	
    // try to get the modality
	boolean isSingleObjExec = false;
	String modality = (String)aSessionContainer.getAttribute(SpagoBIConstants.MODALITY);
   	if( (modality!=null) && modality.equals(SpagoBIConstants.SINGLE_OBJECT_EXECUTION_MODALITY) ) 
   		isSingleObjExec = true;
   	
    //  build the string of the title
    String title = "";
    title = obj.getName();
    String objDescr = obj.getDescription();
    if( (objDescr!=null) && !(objDescr.trim().equals("")) ) 
    	title += ": " + objDescr;
   	
   	// build the url for the parameters form
   	PortletURL execUrl = renderResponse.createActionURL();
   	execUrl.setParameter("PAGE", "ValidateExecuteBIObjectPage");
	execUrl.setParameter(SpagoBIConstants.ACTOR, actor);
   	execUrl.setParameter(SpagoBIConstants.MESSAGEDET, ObjectsTreeConstants.EXEC_PHASE_RUN);
    	execUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
    
   	// build the back url 
   	PortletURL backUrl = renderResponse.createActionURL();
	backUrl.setParameter("PAGE", "BIObjectsPage");
	backUrl.setParameter(SpagoBIConstants.ACTOR, actor);
   	backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
	
%>
	
<form method='POST' action='<%=execUrl.toString()%>' id='paramsValueForm' name='paramsValueForm'>	


<% 
	// IF NOT SINGLE OBJECT MODALITY SHOW DEFAULT TITLE BAR
	if(!isSingleObjExec) {
%>

<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			<%=title%>
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<% if ( !noPars ) { %>
			<td class='header-button-column-portlet-section'>
				<a href="javascript:document.getElementById('paramsValueForm').submit()"> 
      					<img class='header-button-image-portlet-section' 
					title='<spagobi:message key ="SBIDev.docConf.execBIObjectParams.execButt" />' 
					src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/exec.png")%>' 
					alt='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.execButt" />' /> 
				</a>
			</td>
		<% } %>
		<td class='header-button-column-portlet-section'>
			<a href='<%= backUrl.toString() %>'> 
      				<img class='header-button-image-portlet-section' 
				title='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.backButt" />' 
				src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' 
				alt='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.backButt" />' />
			</a>
		</td>
	</tr>
</table>


<% 
	// IF SINGLE OBJECT MODALITY SHOW THE PROPER TITLE BAR
	} else {
%>	
<table width='100%' cellspacing='0' border='0'>
	<tr>
		<td class='header-title-column-single-object-execution-portlet-section' 
		    style='vertical-align:middle;padding-left:5px;'>
			<%=title%>
		</td>
		<td class='header-empty-column-single-object-execution-portlet-section'>&nbsp;</td>
		<td class='header-button-column-single-object-execution-portlet-section'>
			<input type='image' 
				src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/exec22.png")%>' 
				name='exec' 
				alt='<%=PortletUtilities.getMessage("SBIDev.docConf.execBIObjectParams.execButt", "messages")%>' 
				title='<%=PortletUtilities.getMessage("SBIDev.docConf.execBIObjectParams.execButt", "messages")%>' />
		</td>
</table>
	

<% } %>

<div class='div_background_no_img' >
	
<!-- if there aren't parameters dont't show the parameter form  -->
<% if(!noPars) { %>
	<spagobi:dynamicPage modality="EXECUTION_MODALITY" actor="<%=actor %>" />
<% } %>


<div class='errors-object-details-div'>
	<spagobi:error/>
</div>	
 



<!--  ************************* CLOSE FORM ********************************** -->
</form>




<% if( (subObjs!=null) && (subObjs.size()!=0) ) { %>

   <!-- if there aren't parameters show the link for the new composition -->
   <% if(noPars) { 
	   	  	PortletURL execNewCompUrl = renderResponse.createActionURL();
	   		execNewCompUrl.setParameter("PAGE", "ValidateExecuteBIObjectPage");
	   		execNewCompUrl.setParameter(SpagoBIConstants.MESSAGEDET, ObjectsTreeConstants.EXEC_PHASE_RUN);
	   		execNewCompUrl.setParameter(SpagoBIConstants.ACTOR, actor);
	   		execNewCompUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   %>
   <span class='portlet-font'>
			<%=PortletUtilities.getMessage("SBIDev.docConf.subBIObject.newComposition1", "messages")%>
	 </span>
   <a href='<%=execUrl.toString()%>'
			class='portlet-form-field-label'
			onmouseover="this.style.color='#9297ac';"
			onmouseout="this.style.color='#074B88';">
			<%=PortletUtilities.getMessage("SBIDev.docConf.subBIObject.newComposition2", "messages")%>
	 </a>

   <br/><br/> 
   <% } %>

	
	
	
	<div style='width:100%;visibility:visible;' 
			 class='UITabs' 
			 id='tabPanelWithJavascript' 
			 name='tabPanelWithJavascript'>
		<div class="first-tab-level" style="background-color:#f8f8f8">
			<div style="overflow: hidden; width:100%">
				<div class='tab'>
					<%=PortletUtilities.getMessage("SBIDev.docConf.subBIObject.title","messages")%>
				</div>
			</div>
		</div>
	</div>
				
    <table style='width:100%;'> 
	     <tr>
	       <td style='vertical-align:middle;' align="left" class="portlet-section-header">
	           <spagobi:message key='SBIDev.docConf.subBIObject.name' />
	       </td>
	       <td align="left" class="portlet-section-header">&nbsp;</td>
	       <td style='vertical-align:middle;' align="left" class="portlet-section-header">
	           <spagobi:message key='SBIDev.docConf.subBIObject.description' />
	       </td>
	       <td align="left" class="portlet-section-header">&nbsp;</td>
	       <td style='vertical-align:middle;' align="left" class="portlet-section-header">
	           <spagobi:message key='SBIDev.docConf.subBIObject.visibility' />
	       </td>
	       <td align="left" class="portlet-section-header" colspan='3' >&nbsp;</td>
	     <tr> 
		              
		    	<% Iterator iterSubs =  subObjs.iterator();
		    	   BIObject.SubObjectDetail subObj = null;
		    	   String nameSub = "";
		    	   String descr = "";
		    	   String visib = null;
		    	   String delete = "";
		    	   String owner = "";
		    	   PortletURL execSubObjUrl = null;
		    	   PortletURL deleteSubObjUrl = null;
			   
			   boolean alternate = false;
			   String rowClass = "";
			   
                   while(iterSubs.hasNext()) {
    	                subObj = (BIObject.SubObjectDetail)iterSubs.next();
			
			rowClass = (alternate) ? "portlet-section-alternate" : "portlet-section-body";
			alternate = !alternate;
			
                        nameSub = subObj.getName();
                        descr = subObj.getDescription();
                        owner = subObj.getOwner();
                        visib = "Private";
                        if(subObj.isPublicVisible()) {
                        	visib = "Public";
                        } 
                        if(owner.equals(currentUser)) {
                        	delete = "delete";
                        }
    	                execSubObjUrl = renderResponse.createActionURL();
    	                execSubObjUrl.setParameter("PAGE", ExecuteBIObjectModule.MODULE_PAGE );
    	                execSubObjUrl.setParameter(SpagoBIConstants.MESSAGEDET, "EXEC_SUBOBJECT");
    	                execSubObjUrl.setParameter(SpagoBIConstants.ACTOR, actor);
    	                execSubObjUrl.setParameter("NAME_SUB_OBJECT", nameSub);
    	                execSubObjUrl.setParameter("DESCRIPTION_SUB_OBJECT", descr);
    	                execSubObjUrl.setParameter("VISIBILITY_SUB_OBJECT", visib);
    	                execSubObjUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED,"true");
    	                
    	                deleteSubObjUrl = renderResponse.createActionURL();
    	                deleteSubObjUrl.setParameter("PAGE", ExecuteBIObjectModule.MODULE_PAGE );
    	                deleteSubObjUrl.setParameter(SpagoBIConstants.MESSAGEDET, "DELETE_SUBOBJECT");
    	                deleteSubObjUrl.setParameter(SpagoBIConstants.ACTOR, actor);
    	                deleteSubObjUrl.setParameter("NAME_SUB_OBJECT", nameSub);
    	                deleteSubObjUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED,"true");
    	                
                   %>
                        <tr class='portlet-font'>
                        	<td style='vertical-align:middle;' class='<%= rowClass %>'>
                        		<%= nameSub %>
                        	</td>
                        	<td class='<%= rowClass %>' width="20px">&nbsp;</td> 
                        	<td style='vertical-align:middle;' class='<%= rowClass %>' ><%=descr %></td>
                        	<td class='<%= rowClass %>' width="20px">&nbsp;</td> 
                        	<td style='vertical-align:middle;' class='<%= rowClass %>' ><%=visib %></td>
                        	<td class='<%= rowClass %>' width="20px">&nbsp;</td> 
                        	<td style='vertical-align:middle;' class='<%= rowClass %>' width="40px">
                        		<a href="<%=execSubObjUrl.toString()%>">
                        			<img width="20px" height="20px" 
				  	   		src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/exec.gif")%>' 
				  	                name='execSub' 
				  	                alt='<%=PortletUtilities.getMessage("SBIDev.docConf.execBIObjectParams.execButt", "messages")%>' 
				                        title='<%=PortletUtilities.getMessage("SBIDev.docConf.execBIObjectParams.execButt", "messages")%>' />
                        		</a>
                        	</td>
                        	<td style='vertical-align:middle;' class='<%= rowClass %>' width="40px">
                        		<% String eraseMsg = PortletUtilities.getMessage("ConfirmMessages.DeleteSubObject", "messages"); %>
                        		<a href="javascript:var conf = confirm('<%=eraseMsg%>'); if(conf) {document.location='<%=deleteSubObjUrl.toString()%>';}">
                        			<img width="20px" height="20px" 
				  	   		src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/erase.gif")%>' 
				  	                name='deleteSub' 
				  	                alt='<%=PortletUtilities.getMessage("SBIDev.docConf.ListdocDetParam.deleteCaption", "messages")%>' 
				                        title='<%=PortletUtilities.getMessage("SBIDev.docConf.ListdocDetParam.deleteCaption", "messages")%>' />
                        		</a>
                        	</td>
                        </tr> 
                  <% } %>           
	  </table> 
	  <br/>	
 <% } %>

</div>



 		   
