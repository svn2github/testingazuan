	<%@ include file="/jsp/commons/portlet_base.jsp"%>
	
	<%@ page import="it.eng.spagobi.tools.datasource.bo.DataSource,
	 				         it.eng.spago.navigation.LightNavigationManager,
	 				         java.util.Map,java.util.HashMap,java.util.List,
	 				         java.util.Iterator,
	 				         it.eng.spagobi.commons.bo.Domain,
	 				         it.eng.spagobi.tools.dataset.service.DetailDataSetModule" %>
	 				         
	<%@page import="it.eng.spagobi.commons.utilities.ChannelUtilities"%>
	
	<%
		SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("DetailDataSetModule"); 
		DataSet ds = (DataSet)moduleResponse.getAttribute("dataset");
		String message=(String)aServiceRequest.getAttribute("MESSAGEDET");
		String modality = (String)moduleResponse.getAttribute("modality");
		String subMessageDet = ((String)moduleResponse.getAttribute("SUBMESSAGEDET")==null)?"":(String)moduleResponse.getAttribute("SUBMESSAGEDET");
		String msgWarningSave = msgBuilder.getMessage("8002", request);
		
		Map formUrlPars = new HashMap();
		if(ChannelUtilities.isPortletRunning()) {
			formUrlPars.put("PAGE", "DetailDataSetPage");	
  			formUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");	
		}
		String formUrl = urlBuilder.getUrl(request, formUrlPars);
		
		Map backUrlPars = new HashMap();
		//backUrlPars.put("PAGE", "detailMapPage");
		backUrlPars.put("PAGE", "ListDataSetPage");
	    backUrlPars.put("MESSAGEDET", "EXIT_FROM_DETAIL");
		backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
		String backUrl = urlBuilder.getUrl(request, backUrlPars);		
	%>
	
	

<%@page import="it.eng.spagobi.tools.dataset.bo.DataSet"%>
<%@page import="it.eng.spagobi.tools.dataset.bo.FileDataSet"%>
<%@page import="it.eng.spagobi.tools.dataset.bo.QueryDataSet"%>
<%@page import="it.eng.spagobi.tools.dataset.bo.WSDataSet"%>
<%@page import="it.eng.spagobi.commons.dao.DAOFactory"%>
<%@page import="it.eng.spagobi.tools.dataset.bo.DataSetParametersList"%>
<form method='POST' action='<%=formUrl%>' id='dsForm' name='dsForm' >

	<% if(ChannelUtilities.isWebRunning()) { %>
		<input type='hidden' name='PAGE' value='DetailDataSetPage' />
		<input type='hidden' ='<%=LightNavigationManager.LIGHT_NAVIGATOR_DISABLED%>' value='true' />
	<% } %>

	<input type='hidden' value='<%=modality%>' name='MESSAGEDET' />	
	<input type='hidden' value='<%=subMessageDet%>' name='SUBMESSAGEDET' />
	<input type='hidden' value='<%=ds.getDsId()%>' name='id' />
	<input type='hidden' name='parametersXMLModified' value='' id='parametersXMLModified' />
	
	
	<table width="100%" cellspacing="0" border="0" class='header-table-portlet-section'>		
		<tr class='header-row-portlet-section'>
			<td class='header-title-column-portlet-section' 
			    style='vertical-align:middle;padding-left:5px;'>
				<spagobi:message key = "SBISet.ListDataSet.TitleDetail"  />
			</td>
			<td class='header-button-column-portlet-section' id='testButton'>
			<input type='image' class='header-button-image-portlet-section' id='testButtonImage'
						onclick='setParametersXMLModifiedField();'
						name="testDataSetBeforeSave" value="testDataSetBeforeSave"  
						src='<%=urlBuilder.getResourceLink(request, "/img/test.png")%>' 
						title='<spagobi:message key = "SBIDev.DetailDataSet.TestBeforeSaveLbl" />'  
						alt='<spagobi:message key = "SBIDev.DetailDataSet.TestBeforeSaveLbl" />' 
		/>
		</td>
			<td class='header-button-column-portlet-section'>
				<a href="javascript:saveDS('SAVE')"> 
	      			<img class='header-button-image-portlet-section' 
	      				 title='<spagobi:message key = "SBISet.ListDataSet.saveButton" />' 
	      				 src='<%=urlBuilder.getResourceLink(request, "/img/save.png")%>' 
	      				 alt='<spagobi:message key = "SBISet.ListDataSet.saveButton"/>' 
	      			/> 
				</a>
			</td>		 
			<td class='header-button-column-portlet-section'>
				<input type='image' name='saveAndGoBack' id='saveAndGoBack' onClick="javascript:saveDS('SAVEBACK')" class='header-button-image-portlet-section'
				       src='<%=urlBuilder.getResourceLink(request, "/img/saveAndGoBack.png")%>' 
      				   title='<spagobi:message key = "SBISet.ListDataSet.saveBackButton" />'  
                       alt='<spagobi:message key = "SBISet.ListDataSet.saveBackButton" />' 
			   />
			</td>
			<td class='header-button-column-portlet-section'>
				<a href='javascript:goBack("<%=msgWarningSave%>", "<%=backUrl%>")'> 
	      			<img class='header-button-image-portlet-section' 
	      				 title='<spagobi:message key = "SBISet.ListDataSet.backButton"  />' 
	      				 src='<%=urlBuilder.getResourceLink(request, "/img/back.png")%>' 
	      				 alt='<spagobi:message key = "SBISet.ListDataSet.backButton" />' 
	      			/>
				</a>
			</td>		
		</tr>
	</table>
	
				<% 
				String disabledFile="disabled";
				String disabledQuery="disabled";
				String disabledWs="disabled";
				String type="";
				
   	       if(ds instanceof FileDataSet){
					type="file";
					disabledFile="";
				}
				else if(ds instanceof QueryDataSet){
					type="query";
					disabledQuery="";
				} 
				else if(ds instanceof WSDataSet){
					type="ws";
					disabledWs="";
				} 
   	       
			String datasetDisplay = "none";
			 
			DataSetParametersList dataSetParametersList=null;
			 if(type.equals("query")){
					dataSetParametersList = new DataSetParametersList();
						datasetDisplay = "inline";
						String parametersXML = ds.getParameters();
					  	if (parametersXML != null  &&  !parametersXML.equals("")){
					  		dataSetParametersList = DataSetParametersList.fromXML(parametersXML);
					}
				 
				 }
      	            	     %> 
	
	<div class='div_background' style='padding-top:5px;padding-left:5px;'>
	
	<table width="100%" cellspacing="0" border="0" id = "fieldsTable" >
	<tr>
	  <td>
		<div class='div_detail_label'>
			<span class='portlet-form-field-label'>
				<spagobi:message key = "SBISet.ListDataSet.columnLabel" />
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
			<input class='portlet-form-input-field' type="text" <%=isReadonly %>
				   name="LABEL" size="50" value="<%=label%>" maxlength="50" />
			&nbsp;*
		</div>
		<div class='div_detail_label'>
			<span class='portlet-form-field-label'>	
				<spagobi:message key = "SBISet.ListDataSet.columnName" />
			</span>
		</div>
		<div class='div_detail_form'>
		<%
			   String name = ds.getName();
			   if((name==null) || (name.equalsIgnoreCase("null"))  ) {
			   	   name = "";
			   }
		%>
			<input class='portlet-form-input-field' type="text" name="NAME" 
				   size="50" value="<%=name%>" maxlength="160" />
				   		&nbsp;*
		</div>
			
		<div class='div_detail_label'>
			<span class='portlet-form-field-label'>	
				<spagobi:message key = "SBISet.ListDataSet.columnDescr" />
			</span>
		</div>
		<div class='div_detail_form'>
		<%
			   String desc = ds.getDescription();
			   if((desc==null) || (desc.equalsIgnoreCase("null"))  ) {
			   	   desc = "";
			   }
		%>
			<input class='portlet-form-input-field' type="text" name="DESCR" 
				   size="50" value="<%= desc %>" maxlength="160" />
		</div>
					
					<%	
			if(message.equalsIgnoreCase("DETAIL_SELECT")){ 
		String mess="";
			     if(type.equals("file")){
						mess="0";
				}
				else if(type.equals("query")){
						mess="1";
				} 
				else if(type.equals("ws")){
						mess="2";
				} 
			     

		%>
			   	<input type="hidden"name="typeDataSet" value="<%=mess%>"/>
			   	
			   	<div class='div_detail_label'>
					<span class='portlet-form-field-label'>
						<spagobi:message key = "SBISet.ListDS.TypeDs" />
					</span>
				</div>	
				    <div class='div_detail_form'>		
						<span style="font-family: Verdana,Geneva,Arial,Helvetica,sans-serif;font-size: 8pt;"><%=type%></span>
				</div>
				
			<%}
			else
				{%>
   	<div class='div_detail_label'>
			<span class='portlet-form-field-label'>
				<spagobi:message key = "SBISet.ListDS.TypeDs" />
			</span>
		</div>
		<div class='div_detail_form'>

      	   	
      	   	<input type="radio" name="typeDataSet" value="0" <% if(type.equalsIgnoreCase("file")) { out.println(" checked='checked' "); } %> onClick="DisableFields('file')">
					<span class="portlet-font"><spagobi:message key = "SBISet.ListDataSet.fileType" /></span>
			</input>
      	   	<input type="radio" name="typeDataSet" value="1" 
      	   			<% if(type.equalsIgnoreCase("query")) { out.println(" checked='checked' "); } %> onClick="DisableFields('query')">
					<span class="portlet-font"><spagobi:message key = "SBISet.ListDataSet.queryType" /></span>
			</input>
			<input type="radio" name="typeDataSet" value="2" <% if(type.equalsIgnoreCase("ws")) { out.println(" checked='checked' "); } %> onClick="DisableFields('ws')">
					<span class="portlet-font"><spagobi:message key = "SBISet.ListDataSet.wsType" /></span> 
			</input>
		</div>
		<%} %>

		<div class='div_detail_label'>
			<span class='portlet-form-field-label'>	
				<spagobi:message key = "SBISet.ListDataSet.fileName" />
			</span>
		</div>
	    <div class='div_detail_form'>
		<%
			   String fileName =""; 
		       if(ds instanceof FileDataSet){	
			   	fileName = ((FileDataSet)ds).getFileName();
		       }
			   if((fileName==null) || (fileName.equalsIgnoreCase("null"))  ) {
				   fileName = "";
			   }
		%>
			<input class='portlet-form-input-field' type="text" name="FILENAME" 
				   size="50" value="<%=fileName%>" maxlength="50" <%=disabledFile%> />
	   </div>
	   
	   		<div class='div_detail_label'>
			<span class='portlet-form-field-label'>	
				<spagobi:message key = "SBISet.ListDataSet.query" />
			</span>
		</div>
	    <div style="height:90px;">
		<%
			   String query =""; 
				if(ds instanceof QueryDataSet){		
					query=((QueryDataSet)ds).getQuery();
					}
			   if((query==null) || (query.equalsIgnoreCase("null"))  ) {
				   query = "";
			   }
		%>
		
		<textarea rows="4" cols="40" name="QUERY" <%=disabledQuery%>><%=query%></textarea>
		<BR>
		
		<!-- 
			<input class='portlet-form-input-field' type="text" name="QUERY" 
				   size="50" value="<%=query%>" maxlength="50" <%=disabledQuery%> />
	    -->
	   
	   </div>

	   
	   	<div class='div_detail_label'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBISet.eng.dataSource" />
		</span>
	</div>	
	<div class='div_detail_form'>
		<select class='portlet-form-field' name="DATASOURCE" onchange= "changeEngineType(this.options[this.selectedIndex].label)" id="engineType" <%=disabledQuery%> >			
			<option></option>
			<%

			java.util.List dataSources = DAOFactory.getDataSourceDAO().loadAllDataSources();
			java.util.Iterator dataSourceIt = dataSources.iterator();
	
			String actualDsId="-1"; 
			if(ds instanceof QueryDataSet){
				actualDsId=(((QueryDataSet)ds).getDataSource()==null)?"":(new Integer((((QueryDataSet)ds).getDataSource()).getDsId())).toString();
			}
			
			while (dataSourceIt.hasNext()) {
				DataSource dataSourceD = (DataSource) dataSourceIt.next();
				String dsId = String.valueOf(dataSourceD.getDsId());
				

					
				String selected = "";
				if (dsId.equalsIgnoreCase(actualDsId)) {
					selected = "selected='selected'";										
					}				
			 	%>    			 		
    				<option value="<%= dsId  %>" label="<%= dataSourceD.getLabel() %>" <%= selected %>>
    					<%=dataSourceD.getLabel() %>	
    				</option>
    				<%				
			  	}
			
			%>
		</select>
	</div>
		
		<div class='div_detail_label'>
			<span class='portlet-form-field-label'>	
				<spagobi:message key = "SBISet.ListDataSet.address" />
			</span>
		</div>
	    <div class='div_detail_form'>
		<%
			   String address =""; 
		       if(ds instanceof WSDataSet){	
		    	   address = ((WSDataSet)ds).getAdress();
		       }
			   if((address==null) || (address.equalsIgnoreCase("null"))  ) {
				   address = "";
			   }
		%>
			<input class='portlet-form-input-field' type="text" name="ADDRESS" 
				   size="50" value="<%=address%>" maxlength="50" <%=disabledWs%> />
	   </div>
	   
	   		<div class='div_detail_label'>
			<span class='portlet-form-field-label'>	
				<spagobi:message key = "SBISet.ListDataSet.executorClass" />
			</span>
		</div>
	    <div class='div_detail_form'>
		<%
			   String executorClass =""; 
		       if(ds instanceof WSDataSet){	
		    	   executorClass = ((WSDataSet)ds).getExecutorClass();
		       }
			   if((executorClass==null) || (executorClass.equalsIgnoreCase("null"))  ) {
				   executorClass = "";
			   }
		%>
			<input class='portlet-form-input-field' type="text" name="EXECUTORCLASS" 
				   size="50" value="<%=executorClass%>" maxlength="50" <%=executorClass%> <%=disabledWs%>/>
	   </div>		
			
	
	
	</td><!-- CLOSE COLUMN WITH DATA FORM  -->
	
			<!-- START DIV FIX LIST WIZARD --> 
	<%if(type.equals("query")){ %>
        <div id="datasetWizard" style='width:100%;display:<%=datasetDisplay%>'>
			<spagobi:datasetWizard parametersXML='<%= dataSetParametersList.toXML() %>' />
		</div>	
		<!-- DIV FIX LIST WIZARD CLOSED -->
	<% }%>
		
		
		<spagobi:error/>
	</tr>
	</table>   <!-- CLOSE TABLE FORM ON LEFT AND VERSION ON RIGHT  -->
	
	 <!--</div>  background -->
	 
	 
	 
	 

	 
	 
	 
	 
	 
	 
	 	
	

	 
	 
	
	<script>
	
	<%
		String datasetModified = (String)aSessionContainer.getAttribute(SpagoBIConstants.DATASET_MODIFIED);
		if (datasetModified != null && !datasetModified.trim().equals("")) {
	%>
		var datasetModified = <%=datasetModified%>;
	<%
		} else {
	%>
		var datasetModified = false;
	<%
		}
	%>

	function isDsFormChanged () {
	
	var bFormModified = 'false';
		
	var label = document.dsForm.LABEL.value;
	var description = document.dsForm.DESCR.value;	
	
	if ((label != '<%=ds.getLabel()%>')
		|| (description != '<%=(ds.getDescription()==null)?"":ds.getDescription()%>'))
	{			
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
  	  	  if (type == 'SAVE'){
      		  document.getElementById('dsForm').submit();}
	}
	
	
		function DisableFields(type){

		if (type == 'file'){
			document.dsForm.FILENAME.disabled=false;
			document.dsForm.ADDRESS.disabled=true;
			document.dsForm.QUERY.disabled=true;
			document.dsForm.EXECUTORCLASS.disabled=true;
			document.dsForm.DATASOURCE.disabled=true;
		}
		else 
		if (type == 'query'){
			document.dsForm.FILENAME.disabled=true;
			document.dsForm.ADDRESS.disabled=true;
			document.dsForm.QUERY.disabled=false;
			document.dsForm.EXECUTORCLASS.disabled=true;
			document.dsForm.DATASOURCE.disabled=false;
		}
	    else 
	    if (type == 'ws'){
			document.dsForm.FILENAME.disabled=true;
			document.dsForm.ADDRESS.disabled=false;
			document.dsForm.QUERY.disabled=true;
			document.dsForm.EXECUTORCLASS.disabled=false;
			document.dsForm.DATASOURCE.disabled=true;
		
		}
	}
	
			function setParametersXMLModified(newValue) {
	   			 <%if(modality.equals(SpagoBIConstants.DETAIL_MOD)) { %>
					datasetModified = newValue;
				<%}%>
				}
		
			function setParametersXMLModifiedField(){
					if (modified) {
						document.getElementById("parametersXMLModified").value = 'true';
					} else {
						document.getElementById("parametersXMLModified").value = 'false';
						}
				}
		
	</script>
		
	
