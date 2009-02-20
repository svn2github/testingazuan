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
	<%@ include file="/WEB-INF/jsp/commons/portlet_base.jsp"%>
	
	<%@ page         import="it.eng.spagobi.tools.datasource.bo.DataSource,
	 				         it.eng.spago.navigation.LightNavigationManager,
	 				         java.util.Map,java.util.HashMap,java.util.List,
	 				         java.util.Iterator,
	 				         it.eng.spagobi.commons.bo.Domain,
	 				         it.eng.spagobi.tools.datasource.service.DetailDataSourceModule" %>
	 				         
	<%@page import="it.eng.spagobi.commons.utilities.ChannelUtilities"%>
<%@page import="it.eng.spagobi.commons.utilities.GeneralUtilities"%>
	
	<%@page import="it.eng.spago.util.JavaScript"%>
<script type="text/javascript" src="<%=linkProto%>"></script>
<script type="text/javascript" src="<%=linkProtoWin%>"></script>
<script type="text/javascript" src="<%=linkProtoEff%>"></script>
<link href="<%=linkProtoDefThem%>" rel="stylesheet" type="text/css"/>
<link href="<%=linkProtoAlphaThem%>" rel="stylesheet" type="text/css"/>

	<%
		SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("DetailDataSourceModule"); 
		DataSource ds = (DataSource)moduleResponse.getAttribute("dsObj");
		List listDialects = (List) moduleResponse.getAttribute(DetailDataSourceModule.NAME_ATTR_LIST_DIALECTS);
		
		String modality = (String)moduleResponse.getAttribute("modality");
		String subMessageDet = ((String)moduleResponse.getAttribute("SUBMESSAGEDET")==null)?"":(String)moduleResponse.getAttribute("SUBMESSAGEDET");
		String msgWarningSave = msgBuilder.getMessage("8002", request);
		
		Map formUrlPars = new HashMap();
		if(ChannelUtilities.isPortletRunning()) {
			formUrlPars.put("PAGE", "DetailDataSourcePage");	
  			formUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");	
		}
		String formUrl = urlBuilder.getUrl(request, formUrlPars);
		
		Map backUrlPars = new HashMap();
		//backUrlPars.put("PAGE", "detailMapPage");
		backUrlPars.put("PAGE", "ListDataSourcePage");
		backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
		String backUrl = urlBuilder.getUrl(request, backUrlPars);		
	%>
	
	

<form method='POST' action='<%=formUrl%>' id='dsForm' name='dsForm' >

	<% if(ChannelUtilities.isWebRunning()) { %>
		<input type='hidden' name='PAGE' value='DetailDataSourcePage' />
		<input type='hidden' name='<%=LightNavigationManager.LIGHT_NAVIGATOR_DISABLED%>' value='true' />
	<% } %>

	<input type='hidden' value='<%=modality%>' name='MESSAGEDET' />	
	<input type='hidden' value='<%=subMessageDet%>' name='SUBMESSAGEDET' />
	<input type='hidden' value='<%=ds.getDsId()%>' name='id' />
	
	<table width="100%" cellspacing="0" border="0" class='header-table-portlet-section'>		
		<tr class='header-row-portlet-section'>
			<td class='header-title-column-portlet-section' 
			    style='vertical-align:middle;padding-left:5px;'>
				<spagobi:message key = "SBISet.ListDS.TitleDetail"  />
			</td>
			<td class='header-button-column-portlet-section' id='testButton'>
				<a href="javascript:testConnection()"> 
	      			<img class='header-button-image-portlet-section' 
	      				 title='<spagobi:message key = "SBIDev.predLov.TestBeforeSaveLbl" />' 
	      				 src='<%=urlBuilder.getResourceLink(request, "/img/test.png")%>' 
	      				 alt='<spagobi:message key = "SBIDev.predLov.TestBeforeSaveLbl" />' 
	      			/> 
				</a>
			</td>
	<!-- 		<td class='header-empty-column-portlet-section'>&nbsp;</td>-->
			<td class='header-button-column-portlet-section'>
				<a href="javascript:saveDS('SAVE')"> 
	      			<img class='header-button-image-portlet-section' 
	      				 title='<spagobi:message key = "SBISet.ListDS.saveButton" />' 
	      				 src='<%=urlBuilder.getResourceLink(request, "/img/save.png")%>' 
	      				 alt='<spagobi:message key = "SBISet.ListDS.saveButton"/>' 
	      			/> 
				</a>
			</td>		 
			<td class='header-button-column-portlet-section'>
				<input type='image' name='saveAndGoBack' id='saveAndGoBack' onClick="javascript:saveDS('SAVEBACK')" class='header-button-image-portlet-section'
				       src='<%=urlBuilder.getResourceLink(request, "/img/saveAndGoBack.png")%>' 
      				   title='<spagobi:message key = "SBISet.ListDS.saveBackButton" />'  
                       alt='<spagobi:message key = "SBISet.ListDS.saveBackButton" />' 
			   />
			</td>
			<td class='header-button-column-portlet-section'>
				<a href='javascript:goBack("<%=msgWarningSave%>", "<%=backUrl%>")'> 
	      			<img class='header-button-image-portlet-section' 
	      				 title='<spagobi:message key = "SBISet.ListDS.backButton"  />' 
	      				 src='<%=urlBuilder.getResourceLink(request, "/img/back.png")%>' 
	      				 alt='<spagobi:message key = "SBISet.ListDS.backButton" />' 
	      			/>
				</a>
			</td>		
		</tr>
	</table>
	
	
	<div class='div_background' style='padding-top:5px;padding-left:5px;'>
	
	<table width="100%" cellspacing="0" border="0" id = "fieldsTable" >
	<tr>
	  <td>
		<div class='div_detail_label'>
			<span class='portlet-form-field-label'>
				<spagobi:message key = "SBISet.ListDS.columnLabel" />
			</span>
		</div>
		<%
			  String isReadonly = "";
			  if (modality.equalsIgnoreCase("DETAIL_MOD")){
			  		isReadonly = "readonly";
			  }
			  String label = ds.getLabel();
			   if((label==null) || (label.equalsIgnoreCase("null"))  ) {
				   label = "";
			   }
		%>
		<div class='div_detail_form'>
			<input class='portlet-form-input-field' type="text" 
				   name="LABEL" size="50" value="<%=label%>" maxlength="50" />
			&nbsp;*
		</div>
		<div class='div_detail_label'>
			<span class='portlet-form-field-label'>	
				<spagobi:message key = "SBISet.ListDS.columnDescr" />
			</span>
		</div>
		<div class='div_detail_form'>
		<%
			   String desc = ds.getDescr();
			   if((desc==null) || (desc.equalsIgnoreCase("null"))  ) {
			   	   desc = "";
			   }
		%>
			<input class='portlet-form-input-field' type="text" name="DESCR" 
				   size="50" value="<%= desc %>" maxlength="160" />
		</div>
			
		<div class='div_detail_label'>
				<span class='portlet-form-field-label'>
					<spagobi:message key = "SBISet.ListDS.columnDialect" />
				</span>
		</div>
		
	
		<div class='div_detail_form'>
      		<select class='portlet-form-input-field' style='width:250px;' 
					name="DIALECT" id="DIALECT" >
			<% if (listDialects != null){
				Iterator iterDialect = listDialects.iterator();
			   
      			while(iterDialect.hasNext()) {
      				Domain dialect = (Domain)iterDialect.next();
      				Integer objDialect = ds.getDialectId();
      				Integer currDialect = dialect.getValueId();
                    boolean isDialect = false;
      		    	if(objDialect.intValue() == currDialect.intValue()){
      		    		isDialect = true;   
      		    	}
      		%>
      			<option value="<%=dialect.getValueId() %>"<%if(isDialect) out.print(" selected='selected' ");  %>><%=dialect.getTranslatedValueName(locale)%></option>
      		<% 	
      			}
			}
      		%>
      		</select>
		</div> 
			
			
    	<div class='div_detail_label'>
			<span class='portlet-form-field-label'>
				<spagobi:message key = "SBISet.ListDS.TypeDs" />
			</span>
		</div>
		<div class='div_detail_form'>
			<% 
      	      boolean isJndi = false;
      	      String jndiName = ds.getJndi();
      	      if(jndiName != null && !jndiName.equals("")) { isJndi = true; }
      	      
      	      boolean isParameter = false;
      	      String parameter = ds.getUrlConnection();
      	      if(parameter != null && !parameter.equals("")) { isParameter = true; }
      	      
      	     %> 
      	   	<input type="radio" name="typeDS" value="1" <% if(isJndi) { out.println(" checked='checked' "); } %> onClick="DisableFields('JNDI')">
					<span class="portlet-font"><spagobi:message key = "SBISet.ListDS.jndiType" /></span>
			</input>
      	   	<input type="radio" name="typeDS" value="0" <% if(isParameter) { out.println(" checked='checked' "); } %> onClick="DisableFields('Parameter')">
					<span class="portlet-font"><spagobi:message key = "SBISet.ListDS.parameterType" /></span>
			</input>
		</div>
			
		<div class='div_detail_label'>
			<span class='portlet-form-field-label'>	
				<spagobi:message key = "SBISet.ListDS.columnJndi" />
			</span>
		</div>
		<div class='div_detail_form'>
		<%
			   String jndi = ds.getJndi();
			   if((jndi==null) || (jndi.equalsIgnoreCase("null"))  ) {
				   jndi = "";
			   }
			   String disabledParam="disabled";
			   String disabledJndi="disabled";
			   if(isJndi)
				   disabledJndi="";
			   else if (isParameter)
			   	   disabledParam="";
		%>
			<input class='portlet-form-input-field' type="text" name="JNDI" 
				   size="50" value="<%= jndi %>" maxlength="50" <%= disabledJndi %>/>
	   </div>
	   <div class='div_detail_label'>
			<span class='portlet-form-field-label'>	
				<spagobi:message key = "SBISet.ListDS.columnUrl" />
			</span>
		</div>
	   <div class='div_detail_form'>
		<%
			   String url = ds.getUrlConnection();
			   if((url==null) || (url.equalsIgnoreCase("null"))  ) {
				   url = "";
			   }
		%>
			<input class='portlet-form-input-field' type="text" name="URL_CONNECTION" 
				   size="50" value="<%= url %>" maxlength="50" <%= disabledParam %> />
	   </div>
	   <div class='div_detail_label'>
			<span class='portlet-form-field-label'>	
				<spagobi:message key = "SBISet.ListDS.columnUser" />
			</span>
		</div>
	    <div class='div_detail_form'>
		<%
			   String user = ds.getUser();
			   if((user==null) || (user.equalsIgnoreCase("null"))  ) {
				   user = "";
			   }
		%>
			<input class='portlet-form-input-field' type="text" name="USER" autocomplete='off'
				   size="50" value="<%= user %>" maxlength="50" <%= disabledParam %> />
	   </div>
	   <div class='div_detail_label'>
			<span class='portlet-form-field-label'>	
				<spagobi:message key = "SBISet.ListDS.columnPwd" />
			</span>
		</div>
	    <div class='div_detail_form'>
		<%
			   String pwd = ds.getPwd();
			   if((pwd==null) || (pwd.equalsIgnoreCase("null"))  ) {
				   pwd = "";
			   }
		%>
			<input class='portlet-form-input-field' type="password" name="PWD" autocomplete='off'
				   size="50" value="<%= pwd %>" maxlength="50" <%= disabledParam %> />
	   </div>
	   <div class='div_detail_label'>
			<span class='portlet-form-field-label'>	
				<spagobi:message key = "SBISet.ListDS.columnDriver" />
			</span>
		</div>
	    <div class='div_detail_form'>
		<%
			   String driver = ds.getDriver();
			   if((driver==null) || (driver.equalsIgnoreCase("null"))  ) {
				   driver = "";
			   }
		%>
			<input class='portlet-form-input-field' type="text" name="DRIVER" 
				   size="50" value="<%= driver %>" maxlength="160" <%= disabledParam %>/>
	   </div>
	   
	
	
	</td><!-- CLOSE COLUMN WITH DATA FORM  -->
		
		
		<spagobi:error/>
	</tr>
	</table>   <!-- CLOSE TABLE FORM ON LEFT AND VERSION ON RIGHT  -->
	
	 <!--</div>  background -->
	
	<script>
	
	function isDsFormChanged () {
	
	var bFormModified = 'false';
		
	var dialect = document.dsForm.DIALECT.value;
	var label = document.dsForm.LABEL.value;
	var description = document.dsForm.DESCR.value;	
	var jndi = document.dsForm.JNDI.value;
	var url = document.dsForm.URL_CONNECTION.value;
	var user = document.dsForm.USER.value;
	var pwd = document.dsForm.PWD.value;
	var driver = document.dsForm.DRIVER.value;

	
	if ((label != '<%=ds.getLabel()%>')
	    || (dialect != '<%=(ds.getDialectId()==null)?"":ds.getDialectId().toString()%>')
		|| (description != '<%=(ds.getDescr()==null)?"":ds.getDescr()%>')
		|| ( jndi != '<%=(ds.getJndi()==null)?"":ds.getJndi()%>')
		|| ( url != '<%=(ds.getUrlConnection()==null)?"":ds.getUrlConnection()%>')
		|| ( user != '<%=(ds.getUser()==null)?"":ds.getUser()%>')
		|| ( pwd != '<%=(ds.getPwd()==null)?"":ds.getPwd()%>')
		|| ( driver != '<%=(ds.getDriver()==null)?"":ds.getDriver()%>')) {
			
		bFormModified = 'true';
	}
	
	return bFormModified;
	
	}

	
	function goBack(message, url) {
	  
	  var bFormModified = isDsFormChanged();
	  
	  if (bFormModified == 'true'){
	  	  if (confirm(message)) {
	  	      document.getElementById('saveAndGoBack').click(); 
	  	  } else {
			location.href = url;	
    	  }	         
       } else {
			location.href = url;
       }	  
	}
	
	function saveDS(type) {	
  	  	  document.dsForm.SUBMESSAGEDET.value=type;
  	  	  if (type == 'SAVE')
      		  document.getElementById('dsForm').submit();
	}
	
	function DisableFields(type){

		if (type == 'JNDI'){
			document.dsForm.JNDI.disabled=false;
			document.dsForm.URL_CONNECTION.disabled=true;
			document.dsForm.USER.disabled=true;
			document.dsForm.PWD.disabled=true;
			document.dsForm.DRIVER.disabled=true;
		}
		else {
			document.dsForm.JNDI.disabled=true;
			document.dsForm.URL_CONNECTION.disabled=false;
			document.dsForm.USER.disabled=false;
			document.dsForm.PWD.disabled=false;
			document.dsForm.DRIVER.disabled=false;
		}
	}

	function testConnection() {
			var jndi = document.dsForm.JNDI.value;
			var isjndi = document.dsForm.typeDS[0].checked ;
			var urlc = document.dsForm.URL_CONNECTION.value;
			var user = document.dsForm.USER.value;
			var pwd = document.dsForm.PWD.value;
			var driver = document.dsForm.DRIVER.value;
		if ( isjndi ){	
		  if ( !jndi ){	
			Ext.MessageBox.show({
				msg: '<spagobi:message key="sbi.noJndiName" />',
				buttons: Ext.MessageBox.OK,
				width:150
			});
			return;
			}
		}
		if ( !isjndi  && !urlc ){	
			Ext.MessageBox.show({
				msg: '<spagobi:message key="sbi.noUrl" />',
				buttons: Ext.MessageBox.OK,
				width:150
			});
			return;
		}
		if ( !isjndi  && !user ){	
			Ext.MessageBox.show({
				msg: '<spagobi:message key="sbi.noUser" />',
				buttons: Ext.MessageBox.OK,
				width:150
			});
			return;
		}
		if ( !isjndi  && !driver){	
			Ext.MessageBox.show({
				msg: '<spagobi:message key="sbi.noDriver" />',
				buttons: Ext.MessageBox.OK,
				width:150
			});
			return;
		}
	
		urll="<%=GeneralUtilities.getSpagoBIProfileBaseUrl(userId) + "&" + LightNavigationManager.LIGHT_NAVIGATOR_DISABLED + "=TRUE"%>";
		urll += "&ACTION_NAME=TEST_CONN";
		
			urll += "&isjndi="+isjndi+"&jndi="+jndi+"&urlc="+urlc+"&user="+user+"&pwd="+pwd+"&driver="+driver  ;
		
		 Ext.Ajax.request({
 				 url: urll,
 				 method: 'get',
				 success: function (result, request) {
					response = result.responseText || "";
					showConnTestResult(response);
				},
 		 failure: somethingWentWrong
		 });
		 	 
	}
	
	function showConnTestResult(response) {
		var iconRememberMe;
		if (response=="sbi.connTestOk") {
			response = "<spagobi:message key="sbi.connTestOk" />";
			iconRememberMe = Ext.MessageBox.INFO;
		}
		if (response=="sbi.connTestError") {
			response = "<spagobi:message key="sbi.connTestError" />";
			iconRememberMe = Ext.MessageBox.ERROR;
		}
		Ext.MessageBox.show({
			title: 'Status',
			msg: response,
			buttons: Ext.MessageBox.OK,
			width:250,
			icon: iconRememberMe
		});
	}
	
	function somethingWentWrong() {
		alert('<spagobi:message key="sbi.connTestError" />');
	}
	</script>
	
	<%@ include file="/WEB-INF/jsp/commons/footer.jsp"%>
	
