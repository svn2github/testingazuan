<%--
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
--%>


<%@ include file="/jsp/commons/portlet_base.jsp"%>

<%@ page import="it.eng.spagobi.commons.constants.AdmintoolsConstants,

                 java.util.List,
                 it.eng.spagobi.commons.bo.Role,
                 it.eng.spagobi.commons.dao.DAOFactory,
                 it.eng.spago.navigation.LightNavigationManager" %>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>

<% 
	SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("DetailMenuModule"); 
    Menu menu = (Menu)moduleResponse.getAttribute(DetailMenuModule.MENU);
	String modality = (String)moduleResponse.getAttribute(AdmintoolsConstants.MODALITY);
	String parentId = (String) moduleResponse.getAttribute(DetailMenuModule.PARENT_ID);
	Menu parentMenu=null;
	if(parentId!=null)
	parentMenu = DAOFactory.getMenuDAO().loadMenuByID(Integer.valueOf(parentId));
    
	
    Map formUrlPars = new HashMap();
	formUrlPars.put(AdmintoolsConstants.PAGE, DetailMenuModule.MODULE_PAGE);
	formUrlPars.put(AdmintoolsConstants.MESSAGE_DETAIL, modality);
	if (modality.equalsIgnoreCase(AdmintoolsConstants.DETAIL_INS)) {
		if(parentId!=null){
		formUrlPars.put(DetailMenuModule.PARENT_ID, parentId);
	}
	} else {
    	formUrlPars.put(DetailMenuModule.MENU_ID, menu.getMenuId().toString());
    }
	formUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
    String formAct = urlBuilder.getUrl(request, formUrlPars);

    
    Map backUrlPars = new HashMap();
    backUrlPars.put("PAGE", TreeMenuModule.MODULE_PAGE);
    backUrlPars.put(SpagoBIConstants.OPERATION, SpagoBIConstants.FUNCTIONALITIES_OPERATION);
    backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
    String backUrl = urlBuilder.getUrl(request, backUrlPars);

    
    List roles = DAOFactory.getRoleDAO().loadAllRoles();
    String[][] sysRoles = new String[roles.size()][3];
    for(int i=0; i<roles.size(); i++) {
    	Role role = (Role)roles.get(i);
    	sysRoles[i][0] = role.getId().toString();
    	sysRoles[i][1] = role.getName();
    	sysRoles[i][2] = role.getDescription();
    	
    }
%>





<%@page import="it.eng.spagobi.wapp.services.DetailMenuModule"%>
<%@page import="it.eng.spagobi.wapp.bo.Menu"%>
<%@page import="it.eng.spagobi.wapp.services.TreeMenuModule"%>
<%@page import="it.eng.spagobi.analiticalmodel.document.bo.BIObject"%>
<%@page import="it.eng.spagobi.commons.utilities.GeneralUtilities"%>
<%@page import="java.io.File"%>
<form action="<%=formAct%>" method="post" id='formFunct' name = 'formFunct'>

<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section'
		    style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "SBISet.menu.detailtitle" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href="javascript:document.getElementById('formFunct').submit()"> 
      			<img class='header-button-image-portlet-section' 
      			     title='<spagobi:message key = "SBISet.detailMenu.saveButt" />' 
      			     src='<%=urlBuilder.getResourceLink(request, "/img/save.png")%>' 
      			     alt='<spagobi:message key = "SBISet.detailMenu.saveButt" />' />
			</a>
		</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%=backUrl%>'> 
      			<img class='header-button-image-portlet-section' 
      			     title='<spagobi:message 
      			     key = "SBISet.detailMenu.backButt" />' 
      			     src='<%=urlBuilder.getResourceLink(request, "/img/back.png")%>' 
      			     alt='<spagobi:message key = "SBISet.detailMenu.backButt" />'/>
			</a>
		</td>
	</tr>
</table>



<div class='div_background_no_img' style='padding-top:5px;padding-left:5px;' >


    
<div class="div_detail_area_forms">

	<div class='div_detail_label'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBISet.detailMenu.nameField" />
		</span>
	</div>
	<div class='div_detail_form'> 
		<input class='portlet-form-input-field' type="text" 
	      	   size="50" name="name" id="" 
	      	   value="<%= menu.getName() %>"  />
	   	&nbsp;*	
	</div>
	<div class='div_detail_label'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBISet.detailMenu.descriptionField" />
		</span>
	</div>
	<div class='div_detail_form'> 
	<% 
      String desc = menu.getDescr();
      if( (desc==null) || (desc.equalsIgnoreCase("null"))  ) {
      	desc = "";	
      } 
     %>
		<input class='portlet-form-input-field' type="text" 
               size="50" name="description" id="" value="<%= desc %>" />
	</div>


	<div class='div_detail_label'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBISet.menu.Homepage" />
		</span>
		</div>
			<div class='div_detail_form'> 
		<input class='portlet-form-input-field' type="checkbox" 
	      	   size="50" name="homepage" id="" 
	      	   value="true" <%if(menu.isHomepage()){%> checked="checked" <%}%>/>
	</div>
		<div class='div_detail_label'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBISet.menu.viewDocumentIcons" />
		</span>
		</div>
		<div class='div_detail_form'> 
		<input class='portlet-form-input-field' type="checkbox" 
	      	   size="50" name="viewicons" id="" 
	      	   value="true" <%if(menu.isViewIcons()){%> checked="checked" <%}%>/>
</div>
		<div class='div_detail_label'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBISet.menu.HideExecBar" />
		</span>
		</div>
		<div class='div_detail_form'> 
		<input class='portlet-form-input-field' type="checkbox" 
	      	   size="50" name="hideexecbar" id="" 
	      	   value="true" <%if(menu.isHideExecBar()){%> checked="checked" <%}%>/>
	</div>	
	



	 <%	
		String url=GeneralUtilities.getSpagoBiHost()+GeneralUtilities.getSpagoBiContext() + GeneralUtilities.getSpagoAdapterHttpUrl() + "?" + "PAGE=DocumentLookupPage&NEW_SESSION=TRUE";

	 String objId="";
		String objName="";	 
	 	if(menu.getObjId()!=null){
			Integer id=menu.getObjId();	
			 objId=id.toString();
			BIObject obj=DAOFactory.getBIObjectDAO().loadBIObjectById(id);
		 	if(obj!=null){
			objName=obj.getName();
		 	}
		 }
			 %>
	<div class='div_detail_label'>	 
		 <span class='portlet-form-field-label'>
			<spagobi:message key = "SBISet.detailMenu.relatedDoc" />
		</span>
	</div>
	 <div class='div_detail_form' id="documentForm" >
				 <input type="hidden" name="menu_obj" id="menu_obj" value="<%=objId%>"/>	
												
					<input class='portlet-form-input-field' style='width:230px;' type="text"  readonly="readonly"
									name="documentReadLabel" id="documentReadLabel" value="<%=objName%>" maxlength="400" /> 
				
					<a href='javascript:void(0);' id="documentLink">
						<img src="<%=urlBuilder.getResourceLink(request, "/img/detail.gif") %>" title="Lookup" alt="Lookup" />
					</a> 
					<a href='javascript:void(0);' id="deleteDocument">
						<img src="<%=urlBuilder.getResourceLink(request, "/img/erase.png") %>" title="DeleteDocument" alt="DeleteDocument" />
					</a> 	
		</div>
		<!--  </div>-->
			
		<script>
			var win_document;
			Ext.get('documentLink').on('click', function(){
			if(!win_document){
				win_document = new Ext.Window({
				id:'popup_document',
				title:'document',
				bodyCfg:{
					tag:'div', 
					cls:'x-panel-body', 
					children:[{tag:'iframe', 
								name: 'iframe_par_document',        			
								id  : 'iframe_par_document',        			
								src: '<%=url%>',   
								frameBorder:0,
								width:'100%',
								height:'100%',
								style: {overflow:'auto'}  
								}]
						},
					layout:'fit',
					width:800,
					height:320,
					closeAction:'hide',
					plain: true
					});
					};
				win_document.show();
				}
				);
				
			Ext.get('deleteDocument').on('click', function(){
			
			var hiddenO=document.getElementById('menu_obj');
			hiddenO.value=null;
			var textO=document.getElementById('documentReadLabel');     
			textO.value='';			
			}
			)
				
		</script>
	 
	 <% String currentStaticPage=menu.getStaticPage();
	 	// Get directory
	 	String rootPath=ConfigSingleton.getRootPath();
	 	String dirPath=rootPath+"\\static_content";
	 	File dir=new File(dirPath);
	 	if(dir!=null && dir.isDirectory()){
	 	// get all avalaible files
	 	String[] files=dir.list();
	 
	 %>
	 <br>
	 <div class='div_detail_label'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBISet.menu.staticPage" />
		</span>
		</div>
		<div class='div_detail_form'> 
	<select name="staticpage" size="1" class='portlet-form-input-field'>
		<option value=""> </option>
	<%//Insert all options (only HTML files) 
	String selected="";
	for(int i=0;i<files.length;i++){
		String fileName=files[i];
		String ext=fileName.substring(fileName.indexOf(".")+1);
		selected="";
		if(ext.equalsIgnoreCase("html") || ext.equalsIgnoreCase("htm")){
		if(fileName.equals(currentStaticPage))selected="selected='selected'";
	
	%>
			<option value="<%=fileName%>" <%=selected%>><%=fileName%></option>
	
		<%} 
		}%>
	</select>	
</div>	 
	 
	 <%} %>
	 


<spagobi:error/>


<% 
		Integer id = menu.getMenuId();
		Role[] rolesObj = menu.getRoles();
		
		int iLength=rolesObj.length;
		String[] rules = new String[iLength];
		for(int i=0; i<rolesObj.length; i++) {
			rules[i] = rolesObj[i].getId().toString();
		}

%>	
	
<div class="div_functions_role_associations">
	 		<table>
	 				<tr>
	 					<td class='portlet-section-header' align="left">
							<spagobi:message key = "SBISet.detailMenu.tabCol1" />
						</td>
	 					<td class='portlet-section-header' align="center" width="90px">
							<spagobi:message key = "SBISet.detailMenu.tabCol2" />
						</td>				
	 				</tr>
	 			     <% 
	 			    	boolean alternate = false;	
	 			     	String rowClass = null;
	 			     	for(int i=0; i<sysRoles.length; i++) { 
	 			            String ruleId = sysRoles[i][0];
	 			            String ruleName = sysRoles[i][1];
	 			            String ruleDescription = sysRoles[i][2];
	 			            DetailMenuModule detMenu = new DetailMenuModule();
	 			            boolean isParent=false;
	 			            boolean is=false;
		            		
	 			            for(int j=0; j<rules.length; j++) {
	 			               if(rules[j].equals(ruleId)) { is = true; }
	 			               		}

			               	if(!modality.equals(AdmintoolsConstants.DETAIL_INS)){
			               		if(parentMenu!=null){
 			               		if(detMenu.isParentRule(ruleId,parentMenu)){isParent = true;}
			               		}
			               		}
	 			            
	 			            

	 			            rowClass = (alternate) ? "portlet-section-alternate" : "portlet-section-body";
	 			            alternate = !alternate;
	 			            %>
	 			            
					 <tr onMouseOver="this.bgColor='#F5F6BE'" onMouseOut="this.bgColor='#FFFFFF'">
					 	<td nowrap class='portlet-font'><%= ruleName + " (" + ruleDescription + ")" %></td>
					 	
					 	<td align="center">
					 	    <input type="checkbox" name="ROLES" id="ROLES" value="<%=ruleId%>" 
					 	    	<%
					 	    	if(modality.equals(AdmintoolsConstants.DETAIL_INS)){
					 	    		if(is==true)	out.print(" checked='checked' ");
					 	    		else out.print(" disabled='disabled' ");				
					 	    	}
					 	    	else if(modality.equals(AdmintoolsConstants.DETAIL_MOD)){ //Case modify
					 	    			if(is==true){	out.print(" checked='checked' ");} 
					 	    			else {
					 	    				if (isParent==false && parentMenu!=null && parentMenu.getParentId() != null) out.print(" disabled='disabled' ");
					 	    				}
					 	    	}
					 	    	%> 
					 	    />
					 	</td>
					
					 </tr>	
                     <% } %>
                     <tr class='<%=rowClass%>'>
                        <td align="center">&nbsp;</td>       
                        <td align="center">
                        	<a onclick = "selectAllInColumns('ROLES')" title='<spagobi:message key = "SBISet.detailMenu.selAllColumn" />' alt='<spagobi:message key = "SBISet.Funct.selAllColumn" />'>
                        		<img  src='<%=urlBuilder.getResourceLink(request, "/img/expertok.gif")%>'/>
                        	</a>
					    	<a onclick = "deselectAllInColumns('ROLES')" title='<spagobi:message key = "SBISet.detailMenu.deselAllColumn" />' alt='<spagobi:message key = "SBISet.Funct.deselAllColumn" />'>
					    		<img src='<%=urlBuilder.getResourceLink(request, "/img/erase.png")%>' />
					    	</a>
					    </td>
                     </tr>
	 		</table>
</div>

		
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



</script>
