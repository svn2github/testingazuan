	<%@ include file="/jsp/commons/portlet_base.jsp"%>
	
	<%@ page import="it.eng.spagobi.tools.datasource.bo.DataSource,
	 				         it.eng.spago.navigation.LightNavigationManager,
	 				         java.util.Map,java.util.HashMap,java.util.List,
	 				         java.util.Iterator,
	 				         it.eng.spagobi.commons.bo.Domain,
	 				         it.eng.spagobi.tools.dataset.service.DetailDataSetModule" %>
	 				         <script type="text/javascript" src="<%=linkProto%>"></script>
	 				         
	 				         
	<%@page import="it.eng.spagobi.commons.utilities.ChannelUtilities"%>
	
	<%
		SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("DetailDataSetModule"); 
		DataSet ds = (DataSet)moduleResponse.getAttribute(DetailDataSetModule.DATASET);
		String message=(String)aServiceRequest.getAttribute("MESSAGEDET");
		String modality = (String)moduleResponse.getAttribute(SpagoBIConstants.MODALITY);
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

<script type="text/javascript" src="<%=linkProto%>"></script>
<script type="text/javascript" src="<%=linkProtoWin%>"></script>
<script type="text/javascript" src="<%=linkProtoEff%>"></script>
<link href="<%=linkProtoDefThem%>" rel="stylesheet" type="text/css"/>
<link href="<%=linkProtoAlphaThem%>" rel="stylesheet" type="text/css"/>



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
				
			
				String disableFile="disabled";
				String disableQuery="disabled";
				String disableWs="disabled";
				
				String hideFile="style=\"display: none;\"";
				String hideQuery="style=\"display: none;\"";
				String hideWs="style=\"display: none;\"";
				
				String type="";
				
   	       if(ds instanceof FileDataSet){
					type="file";
					disableFile="";
					hideFile="";
   	       }
				else if(ds instanceof QueryDataSet){
					type="query";
					disableQuery="";
					hideQuery="";
				} 
				else if(ds instanceof WSDataSet){
					type="ws";
					disableWs="";
					hideWs="";
				
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
			if(message.equalsIgnoreCase("DETAIL_SELECT") || message.equalsIgnoreCase("DETAIL_MOD")){ 
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

	<div id="filecontainer" <%=hideFile%>>
		<div class='div_detail_label' id="FILENAMELABEL" >
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
			<input class='portlet-form-input-field' type="text" name="FILENAME" id="FILENAME"
				   size="50" value="<%=fileName%>" maxlength="50" <%=disableFile%> />
	   </div>
	   </div>
	   
	   <div id="querycontainer" <%=hideQuery%>>
	   		<div class='div_detail_label' id="QUERYLABEL">
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
		
		<textarea id="QUERY" rows="4" cols="80" name="QUERY" <%=disableQuery%>><%=query%></textarea>
		<BR>
	   
	   </div>

	   
	   	<div class='div_detail_label' id="DATASOURCELABEL">
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBISet.eng.dataSource" />
		</span>
	</div>	
	<div class='div_detail_form'>
		<select class='portlet-form-field' name="DATASOURCE" onchange= "changeEngineType(this.options[this.selectedIndex].label)" id="DATASOURCE" <%=disableQuery%> >			
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
	</div>
	<div id="wscontainer" <%=hideWs%>>		
		<div class='div_detail_label' id="ADDRESSLABEL">
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
			<input class='portlet-form-input-field' type="text" name="ADDRESS" id="ADDRESS"
				   size="50" value="<%=address%>" maxlength="50" <%=disableWs%> />
	   </div>
	   
	   		<div class='div_detail_label' id="EXECUTORCLASSLABEL">
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
			<input class='portlet-form-input-field' type="text" name="EXECUTORCLASS" id="EXECUTORCLASS"
				   size="50" value="<%=executorClass%>" maxlength="50" <%=executorClass%> <%=disableWs%>/>
	   </div>		
			
	</div>
	
	</td><!-- CLOSE COLUMN WITH DATA FORM  -->
	
			<!-- START DIV FIX LIST WIZARD --> 
        <table id="tag" style="display:<%=datasetDisplay%>;">
  		<tr><td>
		<spagobi:datasetWizard parametersXML='<%= dataSetParametersList!= null ? dataSetParametersList.toXML() : "" %>' /> 	
		</td></tr></table>	
		
		
		
		<!-- DIV FIX LIST WIZARD CLOSED -->
		
		
		<spagobi:error/>
	</tr>
	</table>   <!-- CLOSE TABLE FORM ON LEFT AND VERSION ON RIGHT  -->
	
	 <!--</div>  background -->
	 
	 
	 
	 

	 
	 
	 
	 
	 
	 
	 	
	

	 
	 
	
	<script><!--
	
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
			document.getElementById("tag").style.display = "none";
			document.getElementById("filecontainer").style.display = "inline";
			document.getElementById("querycontainer").style.display = "none";
			document.getElementById("wscontainer").style.display = "none";
	
			
		}
		else 
		if (type == 'query'){
			document.dsForm.FILENAME.disabled=true;
			document.dsForm.ADDRESS.disabled=true;
			document.dsForm.QUERY.disabled=false;
			document.dsForm.EXECUTORCLASS.disabled=true;
			document.dsForm.DATASOURCE.disabled=false;
			document.getElementById("tag").style.display = "inline";
			document.getElementById("filecontainer").style.display = "none";
			document.getElementById("querycontainer").style.display = "inline";
			document.getElementById("wscontainer").style.display = "none";
			
		}
	    else 
	    if (type == 'ws'){
			document.dsForm.FILENAME.disabled=true;
			document.dsForm.ADDRESS.disabled=false;
			document.dsForm.QUERY.disabled=true;
			document.dsForm.EXECUTORCLASS.disabled=false;
			document.dsForm.DATASOURCE.disabled=true;
			document.getElementById("tag").style.display = "none";
			document.getElementById("filecontainer").style.display = "none";
			document.getElementById("querycontainer").style.display = "none";
			document.getElementById("wscontainer").style.display = "inline";
	
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
		
	var profattrwinopen = false;
	var winPA = null;
	
	function opencloseProfileAttributeWin() {
		if(!profattrwinopen){
			profattrwinopen = true;
			openProfileAttributeWin();
		}
	}
	
	function openProfileAttributeWin(){
		if(winPA==null) {
			winPA = new Window('winPAId', {className: "alphacube", title: "<%=msgBuilder.getMessage("SBIDev.lov.avaiableProfAttr", "messages", request)%>", width:400, height:300, destroyOnClose: true});
	      	winPA.setContent('profileattributeinfodiv', false, false);
	      	winPA.showCenter(false);
	    } else {
	      	winPA.showCenter(false);
	    }
	}
	
	observerWPA = { 
		onClose: function(eventName, win) {
			if (win == winPA) {
				profattrwinopen = false;
			}
		}
	}
	
	Windows.addObserver(observerWPA);
--></script>

<div id='profileattributeinfodiv' style='display:none;'>	
	<hr/>
	<br/>
	<ul>
    <%
    List nameAttrs = (List) moduleResponse.getAttribute(SpagoBIConstants.PROFILE_ATTRS);
    if(nameAttrs!=null && nameAttrs.size()>0){
		Iterator profAttrsIter = nameAttrs.iterator();
		while(profAttrsIter.hasNext()) {
			String profAttrName = (String)profAttrsIter.next();
	%>
	 	<li><%=profAttrName%></li>
	<% 	
		}
    }
	%>
	</ul>
	<br/>
</div>	
</form>		
		
		
		
	
