<!--
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
-->


<%@ include file="/jsp/commons/portlet_base.jsp"%>

<%@ page import="it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.Parameter,
				 it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.ParameterUse,
				 it.eng.spagobi.commons.bo.Domain,
				 it.eng.spagobi.behaviouralmodel.lov.bo.ModalitiesValue,
				 it.eng.spagobi.behaviouralmodel.check.bo.Check,
				 it.eng.spagobi.commons.bo.Role,
				 it.eng.spagobi.commons.dao.DAOFactory,
				 it.eng.spagobi.behaviouralmodel.analyticaldriver.dao.IParameterUseDAO,
                 it.eng.spagobi.commons.dao.IDomainDAO,
       			 it.eng.spagobi.behaviouralmodel.check.dao.ICheckDAO,
       			 it.eng.spagobi.commons.dao.IRoleDAO,
                 it.eng.spagobi.behaviouralmodel.lov.dao.IModalitiesValueDAO,
                 it.eng.spagobi.commons.constants.ObjectsTreeConstants,
                 it.eng.spagobi.commons.constants.AdmintoolsConstants,
                 it.eng.spago.navigation.LightNavigationManager,
                 java.util.ArrayList,
                 java.util.List,
                 java.util.Iterator" %>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>


<%
	SourceBean moduleResponse = (SourceBean) aServiceResponse.getAttribute("DetailParameterModule"); 
	Parameter parameter = (Parameter) moduleResponse.getAttribute("parametersObj");
	String modality = (String) moduleResponse.getAttribute("modality");
	ArrayList list = (ArrayList) moduleResponse.getAttribute("listObj");
	ArrayList selTypeList = (ArrayList) moduleResponse.getAttribute("listSelType");

	Map formUrlPars = new HashMap();
	formUrlPars.put("PAGE", "detailParameterPage");
	formUrlPars.put("MESSAGEDET", modality);
	formUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
    String formUrl = urlBuilder.getUrl(request, formUrlPars);
    
    Map backUrlPars = new HashMap();
    backUrlPars.put("PAGE", "detailParameterPage");
	if(moduleResponse.getAttribute("SelectedLov") != null && ((String)moduleResponse.getAttribute("SelectedLov")).equalsIgnoreCase("true"))
			backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "2");
		else
			backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
    backUrlPars.put("MESSAGEDET", "EXIT_FROM_DETAIL");
    String backUrl = urlBuilder.getUrl(request, backUrlPars);
     String readonly = "readonly" ;
     String disabled = "disabled" ;
     boolean isreadonly = true;
    if (userProfile.isAbleToExecuteAction(SpagoBIConstants.PARAMETER_MANAGEMENT)){
    	isreadonly = false;
    	readonly = "";
    	disabled = "";
    }
%>



<form method='POST' action='<%=formUrl%>' id ='parametersForm' name='parametersForm'>

<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "SBIDev.param.title" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		
		<%  if (!isreadonly){%>
		<td class='header-button-column-portlet-section'>
			<a href="javascript:document.getElementById('parametersForm').submit()"> 
      			<img class='header-button-image-portlet-section' 
      			     title='<spagobi:message key = "SBIDev.param.saveButt" />' 
      			     src='<%=urlBuilder.getResourceLink(request, "/img/save.png")%>' 
      			     alt='<spagobi:message key = "SBIDev.param.saveButt" />' /> 
			</a>
		</td>
		<td class='header-button-column-portlet-section'>
			<input type='image' name='saveAndGoBack' id='saveAndGoBack' value='true' class='header-button-image-portlet-section'
				src='<%=urlBuilder.getResourceLink(request, "/img/saveAndGoBack.png") %>'
      				title='<spagobi:message key = "SBIDev.param.saveAndGoBackButt" />' alt='<spagobi:message key = "SBIDev.param.saveAndGoBackButt" />'
			/> 
		</td>
		<%}%>
		
		<td class='header-button-column-portlet-section'>
			<% if(modality.equalsIgnoreCase(ObjectsTreeConstants.DETAIL_MOD)) {%>
				<a href='javascript:saveAndGoBackConfirm("<spagobi:message key = "SBIDev.param.saveConfirm" />","<%=backUrl%>")'> 
			<% } else { %>
				<a href='<%=backUrl%>'>
			<% } %>
      				<img class='header-button-image-portlet-section' 
				title='<spagobi:message key = "SBIDev.param.backButt" />'
				src='<%=urlBuilder.getResourceLink(request, "/img/back.png")%>' 
				alt='<spagobi:message key = "SBIDev.param.backButt"/>' />
			</a>
		</td>
	</tr>
</table>




<div class='div_background_no_img' >





<input type='hidden' value='<%= parameter.getId() %>' name='id' />






<div class="div_detail_area_forms">
	<div class='div_detail_label'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBIDev.param.labelField"/>
		</span>
	</div>
	<div class='div_detail_form'>
		<input class='portlet-form-input-field' type="text" 
               id="label" name="label" size="50" 
               value="<%=parameter.getLabel()%>" maxlength="20" <%=readonly%> />
        &nbsp;*
	</div>
	<div class='div_detail_label'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBIDev.param.nameField"/>
		</span>
	</div>
	<div class='div_detail_form'>
		<input class='portlet-form-input-field' type="text"  <%=readonly%>
			   id="name" name="name" size="50" value="<%=parameter.getName()%>" maxlength="40" />
        &nbsp;*
	</div>
	<div class='div_detail_label'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBIDev.param.descriptionField"/>
		</span>
	</div>
	<div class='div_detail_form'>
		<input class='portlet-form-input-field' type="text" <%=readonly%>
			   id="description" name="description" size="50" 
 			   value="<%=(parameter.getDescription() != null ? parameter.getDescription() : "")%>" 
               maxlength="160" />
	</div>
    <% String curr_value = parameter.getTypeId().toString();%>  
 
	<div class='div_detail_label'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBIDev.param.modalityField"/>
		</span>
	</div>
	<div class='div_detail_form'>
		<% for (int i=0	; i<list.size(); i++){
      	       Domain domain = new Domain();
      	       domain = (Domain)list.get(i);
		%>
      	<input type="radio" id="modality" name="modality" <%=disabled%>
      	<% if(modality.equalsIgnoreCase(ObjectsTreeConstants.DETAIL_MOD)) {%>
				onclick="radioButtonClicked(this.value)" 
			<% } %>			   
	           value="<%= (String)domain.getValueCd()+","+ (domain.getValueId()).toString()%>" <% if(curr_value.equals(domain.getValueId().toString())) { out.println(" checked='checked' "); } %> >
				<%= domain.getValueName()%>
		</input>
   	    <% } %>
	</div>
	<div class='div_detail_label'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBIDev.param.isFunctional"/>
		</span>
	</div>
	<div class='div_detail_form'>
		<input class='portlet-form-input-field' type="checkbox" name="isFunctional" id="isFunctional" <%=disabled%>
			   value="true" <%=(parameter.isFunctional() ? "checked='checked'" : "")%>/>
	</div>
	<div class='div_detail_label' style='display:none;'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBIDev.param.lengthField"/>
		</span>
	</div>
	<div class='div_detail_form' style='display:none;'>
		<input class='portlet-form-input-field' type="text" 
			   id="length" name="length" size="5" value="<%=parameter.getLength()%>" maxlength="2" <%=readonly%> />
	</div>
	<div class='div_detail_label' style='display:none;'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBIDev.param.maskField"/>
		</span>
	</div>
	<div class='div_detail_form' style='display:none;'>
		<input class='portlet-form-input-field' type="text" <%=readonly%>
			   id="mask" name="mask" size="50" 
			   value="<%=(parameter.getMask() != null ? parameter.getMask() : "")%>" maxlength="20" />
	</div>
</div>








<spagobi:error/>






<% if (modality.equalsIgnoreCase(ObjectsTreeConstants.DETAIL_INS)) { %>
</div>
</form>
<% } else if (modality.equalsIgnoreCase(ObjectsTreeConstants.DETAIL_MOD)) { 

   ParameterUse paruse = (ParameterUse) moduleResponse.getAttribute("modalitiesObj");

   IRoleDAO rDao = DAOFactory.getRoleDAO();
   
   List roles = null;
   if (paruse.getUseID().intValue() != -1) 
	   roles = rDao.loadAllFreeRolesForDetail(paruse.getUseID());
   else roles = rDao.loadAllFreeRolesForInsert(parameter.getId());

   String[][] sysRoles = new String[roles.size()][3];
   for(int i=0; i<roles.size(); i++) {
    	Role role = (Role)roles.get(i);
    	sysRoles[i][0] = role.getId().toString();
    	sysRoles[i][1] = role.getName();
    	sysRoles[i][2] = role.getDescription();
    }    
   
   // get all possible roles
   List allRoles = null;
   allRoles = rDao.loadAllRoles();
   String[][] allSysRoles = new String[allRoles.size()][3];
   for(int i=0; i<allRoles.size(); i++) {
    	Role role = (Role)allRoles.get(i);
    	allSysRoles[i][0] = role.getId().toString();
    	allSysRoles[i][1] = role.getName();
    	allSysRoles[i][2] = role.getDescription();
   } 
   
   boolean hasRolesFreeForInsert = true;
   List freeRolesForInsert = rDao.loadAllFreeRolesForInsert(parameter.getId());
   if (freeRolesForInsert.size() == 0) hasRolesFreeForInsert = false;
   
   
   // get system checks
   ICheckDAO checkdao = DAOFactory.getChecksDAO();
   List checks = checkdao.loadAllChecks();
   String[][] sysChecks = new String[checks.size()][3];
   for(int i=0; i<checks.size(); i++) {
    	Check check = (Check)checks.get(i);
    	sysChecks[i][0]= check.getCheckId().toString();
    	sysChecks[i][1] = check.getName();
    	sysChecks[i][2] = check.getDescription();
    } 
    
   // list of modalityValues
   IModalitiesValueDAO aModalitiesValueDAO = DAOFactory.getModalitiesValueDAO();
   List allModalitiesValues = aModalitiesValueDAO.loadAllModalitiesValueOrderByCode();  
   
   //IDomainDAO domaindao = DAOFactory.getDomainDAO() ;
   //List typeLov = domaindao.loadListDomainsByType("INPUT_TYPE");

%>

<input type='hidden' id='saveParameterUse' value='' name='' />
<input type='hidden' id='selected_paruse_id' name='' value=''/>
<input type='hidden' id='deleteParameterUse' name='' value=''/>
<input type='hidden' value='<%= (paruse != null ? String.valueOf(paruse.getUseID()) : "") %>' name='useId' />


<div style='width:100%;visibility:visible;' class='UITabs' 
     id='tabPanelWithJavascript' name='tabPanelWithJavascript'>
	<div class="first-tab-level" style="background-color:#f8f8f8">
		<div style="overflow: hidden; width:  100%">					

<%
	List paruses = (List) moduleResponse.getAttribute("parusesList");
	String paruse_idStr = (String) moduleResponse.getAttribute("selected_paruse_id");
	Integer paruse_idInt = new Integer(paruse_idStr);
	int paruse_id = Integer.parseInt(paruse_idStr);
	String linkClass = "tab";
	boolean foundSelectedParuseId = false;
	for (int i = 0; i < paruses.size(); i++){
		ParameterUse aParameterUse = (ParameterUse) paruses.get(i);
		if (aParameterUse.getUseID().equals(paruse_idInt)) {
			linkClass = "tab selected";
			foundSelectedParuseId = true;
		}
		else linkClass = "tab";
%>

			<div class='<%= linkClass%>'>
				<a href='javascript:changeParameterUse("<%= aParameterUse.getUseID().toString() %>", "<spagobi:message key = "SBIDev.param.saveAndChangeParameterUseConfirm" />")' 
					style="color:black;"> 
						<%= aParameterUse.getLabel()%>
				</a>
			</div>

<%}
	if(paruse_id < 0 || !foundSelectedParuseId) linkClass = "tab selected";
		else linkClass = "tab";
	if (hasRolesFreeForInsert && !isreadonly) {
%>

			<div class='<%= linkClass%>'>
				<a href='javascript:changeParameterUse("-1", "<spagobi:message key = "SBIDev.param.saveAndChangeParameterUseConfirm" />")' 
				   style="color:black;"> 
					<spagobi:message key = "SBIDec.param.newParameterUse" />
				</a>
			</div>


<% } %>

		</div>
	</div>
</div>










<table class='header-sub-table-portlet-section'>		
	<tr class='header-sub-row-portlet-section'>
		<% if (paruse_id != -1) { %>
		<td class='header-sub-title-column-portlet-section'>
			<spagobi:message key = "SBIDev.paramUse.title" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<%  if (!isreadonly){%>
		<td class='header-button-column-portlet-section'>
			<a href='javascript:deleteParameterUseConfirm("<spagobi:message key="SBIDev.param.deleteParameterUseConfirm"/>")'>
				<img 	src= '<%=urlBuilder.getResourceLink(request, "/img/erase.gif") %>'
					title='<spagobi:message key = "SBIDev.param.eraseParameterUseButt" />' alt='<spagobi:message key = "SBIDev.param.eraseParameterUseButt" />'
				/>
			</a>
		</td>
		<% }%>
		<% } else { %>
			<td class='header-sub-title-column-portlet-section-no-buttons'>
				<spagobi:message key = "SBIDev.paramUse.title" />
			</td>
		<% } %>
	</tr>
</table>




<div class="div_detail_area_sub_forms">
	<div class='div_detail_label'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBIDev.paramUse.labelField" />
		</span>
	</div>
	<div class='div_detail_form'>
		<input class='portlet-form-input-field' type="text" <%=readonly%>
			   id="paruseLabel" name="paruseLabel" size="50" value="<%=paruse.getLabel()%>" maxlength="20">
		&nbsp;*
	</div>
	<div class='div_detail_label'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBIDev.paramUse.nameField" />
		</span>
	</div>
	<div class='div_detail_form'>
		<input class='portlet-form-input-field' type="text" <%=readonly%>
			   id="paruseName" name="paruseName" size="50" value="<%=paruse.getName()%>" maxlength="40">
        &nbsp;*
	</div>
	<div class='div_detail_label'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBIDev.paramUse.descriptionField" />
		</span>
	</div>
	<div class='div_detail_form'>
		<input class='portlet-form-input-field' type="text" <%=readonly%>
			   id="paruseDescription" name="paruseDescription" size="50" 
			   value="<%=paruse.getDescription() == null ? "" : paruse.getDescription()%>" maxlength="160">
	</div>
	
	<div class='div_detail_label'>
		<span class='portlet-form-field-label'>
		<spagobi:message key = "SBIDev.ListParamUse.parInfo.Name"/>
	</span>
	</div>
	
	<div class='div_detail_form' id = 'divForm' >
	<% String lovName = null;
  	   Integer idLov = null;
  	   idLov = paruse.getIdLov();
  	   Integer idLovInit = new Integer(-1);
  	   if(idLov!= null){
  	   if(!idLov.toString().equals(idLovInit.toString())) {
  		   	ModalitiesValue modVal  = DAOFactory.getModalitiesValueDAO().loadModalitiesValueByID(idLov);
  			lovName = modVal.getName();
  	   }
  	   }
  	   
  	%>
  		<% 
    	boolean isManualInput = false;
    	boolean isLov = false;
    	int manual = 0;
    	if(paruse.getManualInput()!= null){
    	manual = paruse.getManualInput().intValue();}
    	if(manual > 0) { isManualInput = true; }
    	else {isLov = true;}
    %> 
  		<input type="radio" name="valueSelection"  id ="valueSelection" value="lov" <%=disabled%> <% if(isLov) { out.println(" checked='checked' "); } %> onClick = "lovControl();manualInputSelection=this.value;" />
  	
		<input 	class='portlet-form-input-field' type="text" id="paruseLovName" <%=disabled%>
		   		name="paruseLovName" size="40" 
				value="<%= lovName != null ? lovName : "" %>" maxlength="100" readonly <%if(!isLov) {out.println("disabled = 'disabled'");} %>>
  		
  		<input 	type='hidden' id='paruseLovId' value='<%=(idLov != null?(idLov.intValue() != -1 ? idLov.toString() : ""):"") %>' <%=disabled%>
           		name='paruseLovId' />           		
  		<% 	
	  		Map lovLookupURLPars = new HashMap();
  			lovLookupURLPars.put("PAGE", "lovLookupPage");
  			//lovLookupURLPars.put("ORIGIN", "lovLookupPage");
	  		String lovLookupURL = urlBuilder.getUrl(request, formUrlPars);
  		%>
  		&nbsp;*&nbsp;
    	<input 	type='image' name="loadLovLookup" <%=readonly%> id="loadLovLookup" value="LovLookup" style='<%if(isLov) {out.println("display:inline;");} else {out.println("display:none;");} %>'
		   		src='<%=urlBuilder.getResourceLink(request, "/img/detail.gif")%>' 
		   		title='Lov Lookup' alt='Lov Lookup'/>
			 
		 </div>	
		 
		 
		 <div class='div_detail_label'>
	<span class='portlet-form-field-label'>
		&nbsp;
	</span>
	</div>	
		<div class='div_detail_form' id = 'divForm' >
		
		<select class='portlet-form-input-field' NAME="selectionType" id="paruseSelType" <% if (isManualInput) { out.print("disabled='disabled'"); } %>>
		<% 
		 String curr_seltype_val = paruse.getSelectionType();
		 if(curr_seltype_val == null) curr_seltype_val = "none";
		 for (int i=0	; i<selTypeList.size(); i++){
    	       Domain domain = new Domain();
    	       domain = (Domain)selTypeList.get(i);		
		%>		
        	<option <%=disabled%> VALUE="<%=(String)domain.getValueCd()%>" <%if(curr_seltype_val.equals(domain.getValueCd().toString())) { out.println(" selected='selected' "); } %> >
        	<%=domain.getValueName()%>
       <%} %>
       </select>  
       </div>	     
	
	


	<div class='div_detail_label'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBIDev.paramUse.manInputCheck" />
		</span>
	</div>
	<div class='div_detail_form'>
		
		<input type="radio"  name="valueSelection" <%=disabled%> id ="valueSelection" value="man_in" <% if(isManualInput) { out.println(" checked='checked' "); } %> onClick = "lovControl();manualInputSelection=this.value;" ></input>
    	

	</div>
</div>


<table style="margin-bottom:5px;width:100%;">
	<tr>
		<td colspan="3" align="left">
			<table style="margin:0px;width:100%;">
				<tr>
					<td align="left" class='portlet-section-header'>
  	   					<spagobi:message key = "SBIDev.paramUse.valTab3" />
  	   				</td>
  	   				<%  if (!isreadonly){%>
  	   				<td width="50px" class='portlet-section-header'>
			  	   		<a onclick = "checkAllFreeRoles()" title='<spagobi:message 
			  	   		key = "SBIDev.paramUse.checkAllFreeRoles" />' 
			  	   		alt='<spagobi:message key = "SBIDev.paramUse.checkAllFreeRoles" />'>
							<img  src='<%=urlBuilder.getResourceLink(request, "/img/expertok.gif")%>'/>
						</a>
						<a onclick = "uncheckAllFreeRoles()" 
						title='<spagobi:message key = "SBIDev.paramUse.uncheckAllFreeRoles" />' 
						alt='<spagobi:message key = "SBIDev.paramUse.uncheckAllFreeRoles" />'>
							<img src='<%= urlBuilder.getResourceLink(request, "/img/erase.png")%>'/>
						</a>
					</td>
					<%  }%>
				</tr>
			</table>
  	   	</td>
  	</tr>
  	<% 
  		List roleAssociated = paruse.getAssociatedRoles();
  	    int count = 1;
  	    // checkableRolesStr contains the number of the roles that are checkable, separated by ','
  	    String checkableRolesStr = "";
  	    for(int i=0; i<allSysRoles.length; i++) { 
           	if(count==1) out.print("<tr class='portlet-font'>");
            boolean isRole = false;
            boolean isFree = false;
            String roleId = allSysRoles[i][0].toString();
            if (roleAssociated != null){
              	Role tmpRoleAssociated = null;
              	for (int j=0; j<roleAssociated.size(); j++) {
               		tmpRoleAssociated = (Role)roleAssociated.get(j);
               		if(roleId.equals(tmpRoleAssociated.getId().toString())) 
               		isRole = true; 
            	}
            }		    
  	    	for (int k=0; k<sysRoles.length;k++){
  	    		String id = sysRoles[k][0].toString();
  	    		if (id.equals(roleId)){
  	    			isFree = true;
  	    		}
  	    	}
  	    	out.print("<td class='portlet-section-body'>");
  	    	out.print("   <input type='checkbox' name='paruseExtRoleId' "+disabled+" id='extRole_" + i + "' value='"+roleId+"' ");
  	    	if(isRole) {
  	    		out.print(" checked='checked' ");
  	    	}
  	    	if(!isFree && !isRole) {
  	    		out.print(" disabled='disabled' ");
  	    	} else checkableRolesStr += i + ",";
  	    	out.print("></input>" + allSysRoles[i][1]);
  	    	out.print("</td>");
  	    	if((count < 3) && (i==(allSysRoles.length-1))){
  	    		int numcol = 3-count;
  	    		int num;
  	    		for (num = 0; num <numcol; num++){
  	    		  	out.print("<td class='portlet-section-body'>");
  	    	    	out.print("</td>");
  	    		}
				out.print("</tr>");
  	    	} 
  	    	if( (count==3) || (i==(allSysRoles.length-1)) ) {
  	    		out.print("</tr>");
  	    		count = 1;
  	    	} else {
  	    		count ++;
  	    	}
  	 	}
  	  %>
 </table> 
  	    	








<table style="width:100%;">
	<tr>
  		<td colspan="3" align="left" class='portlet-section-header'>
  	    	<spagobi:message key = "SBIDev.paramUse.valTab2" />
  	    </td>
  	</tr>
  	<% 
  	    		    List listChecks = paruse.getAssociatedChecks();
  	    		    Check tmpCheck = null;
  	    		    int counter = 1;
  	    		    for(int i=0; i<sysChecks.length; i++) { 
                        if(counter==1) {
                          out.print("<tr class='portlet-font'>");
                        }
                        boolean isCheck = false;
                        String checkId = sysChecks[i][0].toString();
                         //the list checks is not loaded at the moment
                        if (listChecks != null){
                        	for(int j=0; j<listChecks.size(); j++) {
                          		tmpCheck = (Check)listChecks.get(j);
                   		  		if(checkId.equals(tmpCheck.getCheckId().toString())) 
                   					isCheck = true; 
                   			}
                   		}	    
  	    		 		out.print("<td class='portlet-section-body'>");
  	    		 		out.print("   <input type='checkbox' name='paruseCheckId' "+disabled+" value='"+checkId+"' ");
  	    		 		if(isCheck) {
  	    		 			out.print(" checked='checked' ");
  	    		 		}
  	    		 		out.print(">" + sysChecks[i][1] + "</input>" );
  	    		 		out.print("</td>");
  	    		 		if((counter < 3) && (i==(sysChecks.length-1))){
  	    		 		  int numcol = 3-counter;
  	    		 		  int num;
  	    		 		  for (num = 0; num <numcol; num++){
  	    		 		  out.print("<td class='portlet-section-body'>");
  	    		 		  out.print("</td>");  
  	    		 		  }out.print("</tr>");
  	    		 		  } 
  	    		 		
  	    		 		if( (counter==3) || (i==(sysChecks.length-1)) ) {
  	    		 		 	out.print("</tr>");
  	    		 		 	counter = 1;
  	    		 		} else {
  	    		 		 	counter ++;
  	    		 		 }
  	    		  }
	%>
</table>   	

<script>
 <%
	Parameter initialParameter = (Parameter) aSessionContainer.getAttribute("initial_Parameter");
	if (initialParameter == null) initialParameter = parameter;
	ParameterUse initialParuse = (ParameterUse) aSessionContainer.getAttribute("initial_ParameterUse");
	if (initialParuse == null) initialParuse = paruse;
	%>
    var modality = '<%=initialParameter.getType() + "," + initialParameter.getTypeId()%>';

	function radioButtonClicked(mod) {
		modality = mod;
	}
	
var manualInputSelection = '<%=isLov ? "lov" : "man_in"%>';

function isParuseformModified () {

	var paruseLabel = document.getElementById('paruseLabel').value;
	var paruseName = document.getElementById('paruseName').value;
	var paruseDescription = document.getElementById('paruseDescription').value;
	var paruseLovId = document.getElementById('paruseLovId').value;
	var manIn;
	if (manualInputSelection == 'lov') {
		manIn = 0;
	}
	else {
		manIn = 1;
	}
	
	var initialRoles = new Array();
		
	<%
		List initialRoles = initialParuse.getAssociatedRoles();
		Iterator initialRolesIt = initialRoles.iterator();
		int c = 0;
		while (initialRolesIt.hasNext()) {				
			Role aRole = (Role) initialRolesIt.next();
			out.print("initialRoles["+c+"]="+aRole.getId().toString()+";\n");
			c++;
		}
	%>
		
	paruseExtRoleId=document.forms[0].paruseExtRoleId;
	var roles = new Array();
	var count = 0;
	for (i=0 ; i < paruseExtRoleId.length; i++) {
		if (paruseExtRoleId[i].checked) {
			roles[count]=paruseExtRoleId[i].value;
			count++;
		}
	}
		
	var rolesChanged = arraysChanged(roles, initialRoles);
		
	var initialChecks = new Array();
		
	<%
		List initialChecks = initialParuse.getAssociatedChecks();
		Iterator initialChecksIt = initialChecks.iterator();
		c = 0;
		while (initialChecksIt.hasNext()) {
			Check aCheck = (Check) initialChecksIt.next();
			out.print("initialChecks["+c+"]="+aCheck.getCheckId().toString()+";\n");
			c++;
		}
	%>
		
	paruseCheckId=document.forms[0].paruseCheckId;
	var checks = new Array();
	count = 0;
	for (i=0 ; i < paruseCheckId.length; i++) {
		if (paruseCheckId[i].checked) {
			checks[count]=paruseCheckId[i].value;
			count++;
		}
	}
		
	var checksChanged = arraysChanged(checks, initialChecks);
	
	if ((paruseLabel != '<%=initialParuse.getLabel()%>')
		|| (paruseName != '<%=initialParuse.getName()%>')
		|| (paruseDescription != '<%=initialParuse.getDescription() != null ? initialParuse.getDescription() : ""%>') 
		|| (paruseLovId != '<%= initialParuse.getIdLov().intValue() != -1 ? initialParuse.getIdLov().toString() : "" %>')
		|| (manIn != <%=initialParuse.getManualInput()%>)
		|| rolesChanged
		|| checksChanged) {
			return 'true';
	}
	else {
		return 'false';
	}
	
}

function changeParameterUse (paruseId, message) {

	var parameterUseFormModified = 'false';
	
	document.getElementById('selected_paruse_id').name = 'selected_paruse_id';
	document.getElementById('selected_paruse_id').value = paruseId;
	
	parameterUseFormModified = isParuseformModified();
	
	if (parameterUseFormModified == 'true') 
	{
		if (confirm(message))
		{
			document.getElementById('saveParameterUse').name = 'saveParameterUse';
			document.getElementById('saveParameterUse').value= 'yes';
		}
		else
		{
			document.getElementById('saveParameterUse').name = 'saveParameterUse';
			document.getElementById('saveParameterUse').value= 'no';
		}
	}
	
	document.getElementById('parametersForm').submit();
}



function saveAndGoBackConfirm(message, url){
		
		var label = document.getElementById('label').value;
		var name = document.getElementById('name').value;
		var description = document.getElementById('description').value;

		if ((label != '<%=initialParameter.getLabel()%>')
			|| (name != '<%=initialParameter.getName()%>')
			|| (description != '<%=initialParameter.getDescription()%>')
			|| (modality != '<%=initialParameter.getType() + "," + initialParameter.getTypeId()%>')) {
			
			if (confirm(message)) {
				document.getElementById('saveAndGoBack').click();
			} else {
				location.href = url;
			}
			return;
		}
	
		var parameterUseFormModified = isParuseformModified();
	
		if (parameterUseFormModified == 'true') {
			
			if (confirm(message)) {
				document.getElementById('saveAndGoBack').click();
			} else {
				location.href = url;
			}
			return;
		}

		location.href = url;
}

function arraysChanged(array1, array2) {
	if (array1.length != array2.length) return true;
	array1.sort();
	array2.sort();
	for (x in array1) {
		if (array1[x] != array2[x]) return true;
	}
	return false;
}

function deleteParameterUseConfirm (message) {
	if (confirm(message)) {
		document.getElementById('deleteParameterUse').name = 'deleteParameterUse';
		document.getElementById('deleteParameterUse').value = '<%= paruse.getUseID() %>';
        	document.getElementById('parametersForm').submit();
    	}
}

function lovControl(){
var var1 = document.getElementById('loadLovLookup');
var var2 = document.getElementById('paruseLovName');
var var3 = document.getElementById('paruseSelType');

if(var1.style.display != 'inline'){
   var1.style.display = 'inline';
   var2.disabled = false;
   var3.disabled = false;
 }
 else {
 var1.style.display = 'none';
 var2.disabled = true;
  var3.disabled = true;
 }
}


var checkableRoles = new Array(); 
<%
  if (checkableRolesStr.endsWith(",")) checkableRolesStr = checkableRolesStr.substring(0, checkableRolesStr.length() - 1);
  String[] checkableRoles = checkableRolesStr.split(",");
  if (checkableRoles != null && checkableRoles.length > 0) {
	  for (int k = 0; k < checkableRoles.length; k++) {
	  	%>
		checkableRoles[<%=k%>]='<%=checkableRoles[k]%>';
	  	<%
	  }
  }
%>

function checkAllFreeRoles() {
	for (x=0;x<checkableRoles.length;x=x+1)  {
		aFreeCheckId = checkableRoles[x];
		aFreeCheck = document.getElementById('extRole_' + aFreeCheckId);
		aFreeCheck.checked = true;
	}
}

function uncheckAllFreeRoles() {
	for (x=0;x<checkableRoles.length;x=x+1) {
		aFreeCheckId = checkableRoles[x];
		aFreeCheck = document.getElementById('extRole_' + aFreeCheckId);
		aFreeCheck.checked = false;
	}
}


</script>

</div>
</form>

<% } %>
