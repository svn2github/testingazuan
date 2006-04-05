<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="it.eng.spagobi.constants.AdmintoolsConstants,
                 it.eng.spagobi.services.modules.DetailFunctionalityModule,
                 javax.portlet.PortletURL,
                 java.util.List,
                 it.eng.spagobi.bo.Role,
                 it.eng.spagobi.bo.LowFunctionality,
                 it.eng.spagobi.services.modules.TreeObjectsModule,
                 it.eng.spagobi.constants.SpagoBIConstants,
                 it.eng.spagobi.bo.dao.DAOFactory,
                 it.eng.spago.navigation.LightNavigationManager" %>

<% 
	SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("DetailFunctionalityModule"); 
    LowFunctionality functionality = (LowFunctionality)moduleResponse.getAttribute(DetailFunctionalityModule.FUNCTIONALITY_OBJ);
	String modality = (String)moduleResponse.getAttribute(AdmintoolsConstants.MODALITY);
		
	PortletURL formAct = renderResponse.createActionURL();
    formAct.setParameter(AdmintoolsConstants.PAGE, DetailFunctionalityModule.MODULE_PAGE);
    formAct.setParameter(AdmintoolsConstants.MESSAGE_DETAIL, modality);
    String pathParent = "";
    if(modality.equalsIgnoreCase(AdmintoolsConstants.DETAIL_INS)) {
    	pathParent = (String)moduleResponse.getAttribute(AdmintoolsConstants.PATH_PARENT);
    	formAct.setParameter(AdmintoolsConstants.PATH_PARENT, pathParent);
    } else {
    	formAct.setParameter(AdmintoolsConstants.FUNCTIONALITY_ID, functionality.getId().toString());
    }
    formAct.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");

    PortletURL backUrl = renderResponse.createActionURL();
    backUrl.setParameter("PAGE", TreeObjectsModule.MODULE_PAGE);
    backUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.ADMIN_ACTOR);
    backUrl.setParameter(SpagoBIConstants.OPERATION, SpagoBIConstants.FUNCTIONALITIES_OPERATION);
    backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
    
    List roles = DAOFactory.getRoleDAO().loadAllRoles();
    String[][] sysRoles = new String[roles.size()][3];
    for(int i=0; i<roles.size(); i++) {
    	Role role = (Role)roles.get(i);
    	sysRoles[i][0] = role.getId().toString();
    	sysRoles[i][1] = role.getName();
    	sysRoles[i][2] = role.getDescription();
    	
    }
%>




<form action="<%= formAct.toString() %>" method="post" id='formFunct' name = 'formFunct'>

<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section'
		    style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "SBISet.Funct.title" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href="javascript:document.getElementById('formFunct').submit()"> 
      			<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBISet.Funct.saveButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/save.png")%>' alt='<spagobi:message key = "SBISet.Funct.saveButt" />' />
			</a>
		</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= backUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBISet.Funct.backButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' alt='<spagobi:message key = "SBISet.Funct.backButt" />'/>
			</a>
		</td>
	</tr>
</table>



<div class='div_background_no_img' style='padding-top:5px;padding-left:5px;' >


    
<div class="div_detail_area_forms">
	<div class='div_detail_label'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBISet.Funct.codefield" />
		</span>
	</div>
	<div class='div_detail_form'>
	<% 
	  String code = functionality.getCode();
	  if((code==null) || (code.equalsIgnoreCase("null"))  ) {
	  	code = "";
	  }
    %>
    	<input class='portlet-form-input-field' type="text" 
	      	   size="50" name="code" id="" value="<%= code %>" 
	      	   <%  if(modality.equalsIgnoreCase(AdmintoolsConstants.DETAIL_MOD)) { out.print(" readonly "); } %> />
	    &nbsp;* 
	</div>
	<div class='div_detail_label'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBISet.Funct.nameField" />
		</span>
	</div>
	<div class='div_detail_form'> 
		<input class='portlet-form-input-field' type="text" 
	      	   size="50" name="name" id="" 
	      	   value="<%= functionality.getName() %>"  />
	   	&nbsp;*	
	</div>
	<div class='div_detail_label'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBISet.Funct.descriptionField" />
		</span>
	</div>
	<div class='div_detail_form'> 
	<% 
      String desc = functionality.getDescription();
      if( (desc==null) || (desc.equalsIgnoreCase("null"))  ) {
      	desc = "";
      } 
     %>
		<input class='portlet-form-input-field' type="text" 
               size="50" name="description" id="" value="<%= desc %>" />
	</div>
</div>



<spagobi:error/>

	
<% if(functionality.getCodType().equalsIgnoreCase("LOW_FUNCT")) { 
		LowFunctionality lowFunctionality = (LowFunctionality)functionality;
		String path = lowFunctionality.getPath();
		Role[] devRolesObj = lowFunctionality.getDevRoles();
		String[] devRules = new String[devRolesObj.length];
		for(int i=0; i<devRolesObj.length; i++) {
			devRules[i] = devRolesObj[i].getId().toString();
		}
		Role[] execRolesObj = lowFunctionality.getExecRoles();
		String[] execRules = new String[execRolesObj.length];
		for(int i=0; i<execRolesObj.length; i++) {
			execRules[i] = execRolesObj[i].getId().toString();
		}
		Role[] testRolesObj = lowFunctionality.getTestRoles();
		String[] testRules = new String[testRolesObj.length];
		for(int i=0; i<testRolesObj.length; i++) {
			testRules[i] = testRolesObj[i].getId().toString();
		}
%>	
	
<div class="div_functions_role_associations">
	 		<table>
	 				<tr>
	 					<td class='portlet-section-header' align="left">
							<spagobi:message key = "SBISet.Funct.tabCol1" />
						</td>
	 					<td class='portlet-section-header' align="center" width="90px">
							<spagobi:message key = "SBISet.Funct.tabCol2" />
						</td>
	 					<td class='portlet-section-header' align="center" width="90px">
                            <spagobi:message key = "SBISet.Funct.tabCol3" />
                        </td>
	 					<td class='portlet-section-header' align="center" width="90px">
                            <spagobi:message key = "SBISet.Funct.tabCol4" />
                        </td>
	                    <td class='portlet-section-header' align="center" width="90px">
                        	&nbsp;
                        </td> 				
	 				</tr>
	 			     <% 
	 			    	boolean alternate = false;	
	 			     	String rowClass = null;
	 			     	for(int i=0; i<sysRoles.length; i++) { 
	 			            String ruleId = sysRoles[i][0];
	 			            String ruleName = sysRoles[i][1];
	 			            String ruleDescription = sysRoles[i][2];
	 			            boolean isDev = false;
	 			            boolean isTest = false;
	 			            boolean isExec = false;
	 			            for(int j=0; j<devRules.length; j++) 
	 			               if(devRules[j].equals(ruleId)) { isDev = true; }
	 			            for(int j=0; j<testRules.length; j++) 
	 			               if(testRules[j].equals(ruleId)) { isTest = true; }
	 			            for(int j=0; j<execRules.length; j++) 
	 			               if(execRules[j].equals(ruleId)) { isExec = true; }  
	 			            rowClass = (alternate) ? "portlet-section-alternate" : "portlet-section-body";
	 			            alternate = !alternate;
	 			            %>
					 <tr class='<%=rowClass%>'>
					 	<td class='portlet-font'><%= ruleName + " (" + ruleDescription + ")" %></td>
					 	
					 	<td align="center">
					 	    <input type="checkbox" name="development" id="development" value="<%=ruleId%>" <%if(isDev) out.print(" checked='checked' ");  %> />
					 	</td>
					 	<td align="center">
					 	    <input type="checkbox" name="test" id="test" value="<%=ruleId%>" <%if(isTest) out.print(" checked='checked' ");  %> />
					 	</td>
					 	<td align="center">
					 	    <input type="checkbox" name="execution" id="execution" value="<%=ruleId%>" <%if(isExec) out.print(" checked='checked' ");  %> />
					 	</td> 
					    <td>
					    <a onclick = "selectAllInRows('<%=ruleId%>')" title='<spagobi:message key = "SBISet.Funct.selAllRow" />' alt='<spagobi:message key = "SBISet.Funct.selAllRow" />'>
					    <img  src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/expertok.gif")%>'/>
					    </a>
					    <a onclick = "deselectAllInRows('<%=ruleId%>')" title='<spagobi:message key = "SBISet.Funct.deselAllRow" />' alt='<spagobi:message key = "SBISet.Funct.deselAllRow" />'>
					    <img src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/erase.png")%>'/>
					    </a>
					    </td> 
					 </tr>	
                     <% } %>
                     <tr class='<%=rowClass%>'>
                        <td align="center">&nbsp;</td>       
                        <td align="center">
                        <a onclick = "selectAllInColumns('development')" title='<spagobi:message key = "SBISet.Funct.selAllColumn" />' alt='<spagobi:message key = "SBISet.Funct.selAllColumn" />'>
                        <img  src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/expertok.gif")%>'/>
                        </a>
					    <a onclick = "deselectAllInColumns('development')" title='<spagobi:message key = "SBISet.Funct.deselAllColumn" />' alt='<spagobi:message key = "SBISet.Funct.deselAllColumn" />'>
					    <img src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/erase.png")%>' />
					    </a>
					    </td>
					    <td align="center">
                        <a onclick = "selectAllInColumns('test')" title='<spagobi:message key = "SBISet.Funct.selAllColumn" />' alt='<spagobi:message key = "SBISet.Funct.selAllColumn" />'>
                        <img  src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/expertok.gif")%>'/>
                        </a>
					    <a onclick = "deselectAllInColumns('test')" title='<spagobi:message key = "SBISet.Funct.deselAllColumn" />' alt='<spagobi:message key = "SBISet.Funct.deselAllColumn" />'>
					    <img src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/erase.png")%>'/>
					    </a>
					    </td>
					    <td align="center">
                        <a onclick = "selectAllInColumns('execution')" title='<spagobi:message key = "SBISet.Funct.selAllColumn" />' alt='<spagobi:message key = "SBISet.Funct.selAllColumn" />'>
                        <img  src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/expertok.gif")%>'/>
                        </a>
					    <a onclick = "deselectAllInColumns('execution')" title='<spagobi:message key = "SBISet.Funct.deselAllColumn" />' alt='<spagobi:message key = "SBISet.Funct.deselAllColumn" />'>
					    <img src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/erase.png")%>'/>
					    </a>
					    </td>
						<td align="center">&nbsp;</td>   
                     </tr>
	 		</table>
</div>

<% } %>	 		
</form>
    


</div>    
    

<script>
function selectAllInColumns (columnName){
var checkCollection = document.forms.formFunct.elements[columnName];
for (var i = 0; i< checkCollection.length; i++){
if(!checkCollection[i].checked){
checkCollection[i].click();
}
}
}
function deselectAllInColumns (columnName){
var checkCollection = document.forms.formFunct.elements[columnName];
for (var i = 0; i< checkCollection.length; i++){
if(checkCollection[i].checked){
checkCollection[i].click();
}
}
}
function selectAllInRows (rowId){
var checkDevCollection = document.forms.formFunct.elements['development'];
var checkTestCollection = document.forms.formFunct.elements['test'];
var checkExecCollection = document.forms.formFunct.elements['execution'];

for(var i=0; i<checkDevCollection.length; i++){
if(checkDevCollection[i].value == rowId && !checkDevCollection[i].checked){
checkDevCollection[i].click();
}
}
for(var j=0; j<checkTestCollection.length; j++){
if(checkTestCollection[j].value == rowId && !checkTestCollection[j].checked){
checkTestCollection[j].click();
}
}
for(var k=0; k<checkExecCollection.length; k++){
if(checkExecCollection[k].value == rowId && !checkExecCollection[k].checked){
checkExecCollection[k].click();
}
}
}
function deselectAllInRows (rowId){
var checkDevCollection = document.forms.formFunct.elements['development'];
var checkTestCollection = document.forms.formFunct.elements['test'];
var checkExecCollection = document.forms.formFunct.elements['execution'];

for(var i=0; i<checkDevCollection.length; i++){
if(checkDevCollection[i].value == rowId && checkDevCollection[i].checked){
checkDevCollection[i].click();
}
}
for(var j=0; j<checkTestCollection.length; j++){
if(checkTestCollection[j].value == rowId && checkTestCollection[j].checked){
checkTestCollection[j].click();
}
}
for(var k=0; k<checkExecCollection.length; k++){
if(checkExecCollection[k].value == rowId && checkExecCollection[k].checked){
checkExecCollection[k].click();
}
}
}
</script>