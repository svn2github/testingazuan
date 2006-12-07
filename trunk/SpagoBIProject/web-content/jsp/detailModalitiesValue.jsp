<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="it.eng.spagobi.bo.ModalitiesValue,
                 it.eng.spagobi.bo.Domain,
                 it.eng.spagobi.bo.lov.QueryDetail,
                 it.eng.spagobi.bo.lov.ScriptDetail,
                 it.eng.spagobi.bo.lov.JavaClassDetail,
                 it.eng.spagobi.bo.lov.FixedListDetail,
                 it.eng.spagobi.bo.lov.FixedListItemDetail,
		 		 it.eng.spagobi.bo.ParameterUse,
		 		 it.eng.spagobi.bo.dao.DAOFactory,
                 java.util.Set,
                 java.util.HashMap,
                 java.util.List,
                 java.util.Iterator,
                 java.util.ArrayList,
                 java.util.StringTokenizer,
                 javax.portlet.PortletURL,
                 it.eng.spagobi.constants.SpagoBIConstants,
                 it.eng.spago.navigation.LightNavigationManager" %>

<%
	SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("DetailModalitiesValueModule"); 
	List nameAttrs = (List) moduleResponse.getAttribute(SpagoBIConstants.PROFILE_ATTRS);
	ModalitiesValue modVal = (ModalitiesValue)moduleResponse.getAttribute(SpagoBIConstants.MODALITY_VALUE_OBJECT);
	String modality = (String)moduleResponse.getAttribute(SpagoBIConstants.MODALITY);
	ArrayList list = (ArrayList)moduleResponse.getAttribute(SpagoBIConstants.LIST_INPUT_TYPE); 

	Iterator iterAttrs = null;
	
	PortletURL formUrl = renderResponse.createActionURL();
	formUrl.setParameter("PAGE", "detailModalitiesValuePage");
	formUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
	  
	PortletURL backUrl = renderResponse.createActionURL();
	backUrl.setParameter("PAGE", "detailModalitiesValuePage");
	backUrl.setParameter("MESSAGEDET", "EXIT_FROM_DETAIL");
	backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
	
	String queryDisplay = "none";
	String scriptDisplay = "none";
	String lovDisplay = "none";
	String javaClassDisplay = "none";
	
	String isSingleValue = "false";
	
	QueryDetail query = new QueryDetail();
	if (modVal != null && modVal.getITypeCd().equalsIgnoreCase("QUERY") ) {
		queryDisplay = "inline";
		String lovProvider = modVal.getLovProvider();
		if (lovProvider != null  &&  !lovProvider.equals("")){
		  	query = QueryDetail.fromXML(lovProvider);
		}
	}
	
	ScriptDetail scriptDet = new ScriptDetail();
	if (modVal != null && modVal.getITypeCd().equalsIgnoreCase("SCRIPT") ) {
		scriptDisplay = "inline";
		String lovProvider = modVal.getLovProvider();
	  	if (lovProvider != null  &&  !lovProvider.equals("")){
	  		scriptDet = ScriptDetail.fromXML(lovProvider);
	  	}
	  	if(scriptDet.isSingleValue()) isSingleValue = "true";
	}
	
	JavaClassDetail javaClassDetail = new JavaClassDetail();
	if (modVal != null && modVal.getITypeCd().equalsIgnoreCase("JAVA_CLASS") ) {
		javaClassDisplay = "inline";
		String lovProvider = modVal.getLovProvider();
	  	if (lovProvider != null  &&  !lovProvider.equals("")){
	  		javaClassDetail = JavaClassDetail.fromXML(lovProvider);
	  	}
	  	if(javaClassDetail.isSingleValue()) isSingleValue = "true";
	}
	
	FixedListDetail fixedListDetail = new FixedListDetail();
	if (modVal != null && modVal.getITypeCd().equalsIgnoreCase("FIX_LOV") ) {
		lovDisplay = "inline";
		String lovProvider = modVal.getLovProvider();
	  	if (lovProvider != null  &&  !lovProvider.equals("")){
	  		fixedListDetail = FixedListDetail.fromXML(lovProvider);
	  	}
	}
	
	
	String testButtonVisibility = null;
	String testButtonDisabled = "";
	if (modVal != null 
			&& (
					modVal.getITypeCd().equalsIgnoreCase("QUERY")
					|| modVal.getITypeCd().equalsIgnoreCase("SCRIPT")
					|| modVal.getITypeCd().equalsIgnoreCase("JAVA_CLASS")
					|| modVal.getITypeCd().equalsIgnoreCase("FIX_LOV")
				)
		)  {
			testButtonVisibility = "visible";
		}	
	else {
			testButtonVisibility = "hidden";
			testButtonDisabled="disabled='disabled'";
	}
	
%>


<%@page import="it.eng.spagobi.utilities.PortletUtilities"%>
<form method='POST' action='<%= formUrl.toString() %>' id ='modalitiesValueForm' name='modalitiesValueForm'>
	<input type='hidden' value='<%= modVal.getId() %>' name='id' />
	<input type='hidden' value='<%= modality %>' name='<%= SpagoBIConstants.MESSAGEDET  %>' />
	<input type='hidden' name='lovProviderModified' value='' id='lovProviderModified' />
	<input type='hidden' name='singlevalue' value='<%=isSingleValue%>' id='singlevalue' />



<script type="text/javascript">

<%
	String lovProviderModified = (String) moduleResponse.getAttribute("lovProviderModified");
	if (lovProviderModified != null && !lovProviderModified.trim().equals("")) {
		%>
		var lovProviderModified = <%=lovProviderModified%>;
		<%
	} else {
		%>
		var lovProviderModified = false;
		<%
	}
%>

function setLovProviderModified(newValue) {
	lovProviderModified = newValue;
}

function showWizard(){
	var wizard = document.getElementById("input_type").value
	if (wizard.match("QUERY") != null) {
		document.getElementById("queryWizard").style.display = "inline"
		document.getElementById("scriptWizard").style.display = "none"
		document.getElementById("lovWizard").style.display = "none"
		document.getElementById("javaClassWizard").style.display = "none"
		document.getElementById("testButton").style.visibility = "visible"
		document.getElementById("testButtonImage").disabled = false
	}
	if (wizard.match("SCRIPT") != null) {
		document.getElementById("queryWizard").style.display = "none"
		document.getElementById("scriptWizard").style.display = "inline"
		document.getElementById("lovWizard").style.display = "none"
		document.getElementById("javaClassWizard").style.display = "none"
		document.getElementById("testButton").style.visibility = "visible"
		document.getElementById("testButtonImage").disabled = false
	}
	if (wizard.match("FIX_LOV") != null) {
		document.getElementById("queryWizard").style.display = "none"
		document.getElementById("scriptWizard").style.display = "none"
		document.getElementById("lovWizard").style.display = "inline"
		document.getElementById("javaClassWizard").style.display = "none"
		document.getElementById("testButton").style.visibility = "hidden"
		document.getElementById("testButtonImage").disabled = true
	}
	if (wizard.match("JAVA_CLASS") != null) {
		document.getElementById("queryWizard").style.display = "none"
		document.getElementById("scriptWizard").style.display = "none"
		document.getElementById("lovWizard").style.display = "none"
		document.getElementById("javaClassWizard").style.display = "inline"
		document.getElementById("testButton").style.visibility = "visible"
		document.getElementById("testButtonImage").disabled = false
	}
}

function askForConfirmIfNecessary() {
	<%
	List paruses = DAOFactory.getParameterUseDAO().getParameterUsesAssociatedToLov(modVal.getId());
	Iterator parusesIt = paruses.iterator();
	List documents = new ArrayList();
	while (parusesIt.hasNext()) {
		ParameterUse aParuse = (ParameterUse) parusesIt.next();
		List temp = DAOFactory.getBIObjectParameterDAO().getDocumentLabelsListUsingParameter(aParuse.getId());
		documents.addAll(temp);
	}
	if (documents.size() > 0) {
		String documentsStr = documents.toString();
		%>
			if (lovProviderModified) {
				if (confirm('<spagobi:message key = "SBIDev.predLov.savePreamble" />' + ' ' + '<%=documentsStr%>' + '. ' + '<spagobi:message key = "SBIDev.predLov.saveConfirm" />')) {
					document.getElementById("saveLov").name = 'saveLov';
					document.getElementById("saveLov").value = 'saveLov';
					document.getElementById("modalitiesValueForm").submit();
				}
			} else {
				document.getElementById("saveLov").name = 'saveLov';
				document.getElementById("saveLov").value = 'saveLov';
				document.getElementById("modalitiesValueForm").submit();
			}
		<%
	} else {
		%>
		document.getElementById("saveLov").name = 'saveLov';
		document.getElementById("saveLov").value = 'saveLov';
		document.getElementById("modalitiesValueForm").submit();
		<%
	}
	%>
}

function setLovProviderModifiedField(){
	if (lovProviderModified) {
		document.getElementById("lovProviderModified").value = 'true';
	} else {
		document.getElementById("lovProviderModified").value = 'false';
	}
}
</script>



<table class='header-table-portlet-section' >		
	<tr class='header-row-portlet-section'> 
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "SBIDev.predLov.title" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section' style='visibility:<%=testButtonVisibility%>;' id='testButton'>
		<input type='image' class='header-button-image-portlet-section' id='testButtonImage'
				onclick='setLovProviderModifiedField();'
				name="testLovBeforeSave" value="testLovBeforeSave" <%=testButtonDisabled%> 
				src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/test.png")%>' 
				title='<spagobi:message key = "SBIDev.predLov.TestBeforeSaveLbl" />'  
				alt='<spagobi:message key = "SBIDev.predLov.TestBeforeSaveLbl" />' 
		/>
		</td>
		<td class='header-button-column-portlet-section'>
			<input type='hidden' id="saveLov" name="" value="" />
			<a href= 'javascript:askForConfirmIfNecessary();' >
				<img class='header-button-image-portlet-section'
					src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/save.png")%>' 
					title='<spagobi:message key = "SBIDev.predLov.saveButt" />'  
					alt='<spagobi:message key = "SBIDev.predLov.saveButt" />' 
				/>
			</a>
		</td>
		<td class='header-button-column-portlet-section'>
			<a href= '<%= backUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBIDev.predLov.backButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' alt='<spagobi:message key = "SBIDev.predLov.backButt" />' />
			</a>
		</td>
	</tr>
</table>


<div class='div_background_no_img'>



<div class="div_detail_area_forms_lov" >
	<div class='div_detail_label_lov'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBIDev.predLov.labelField" />
		</span>
	</div>
	<div class='div_detail_form'>
		<input class='portlet-form-input-field' type="text" name="label" 
	      	   size="50" value="<%=modVal.getLabel()%>" maxlength="20">
	    &nbsp;*
	</div>
	<div class='div_detail_label_lov'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBIDev.predLov.nameField" />
		</span>
	</div>
	<div class='div_detail_form'>
		<input class='portlet-form-input-field' type="text" name="name" 
	      	   size="50" value="<%=modVal.getName()%>" maxlength="40">
	    &nbsp;*
	</div>
	<div class='div_detail_label_lov'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBIDev.predLov.descriptionField" />
		</span>
	</div>
	<div class='div_detail_form'>
		<% 
	      String desc = modVal.getDescription();
	      if((desc==null) || (desc.equalsIgnoreCase("null"))  ) {
	      		desc = "";
	      } 
	    %>
	    <input class='portlet-form-input-field' type="text" name="description" 
	      	   size="50" value="<%=desc%>" maxlength="160">
	</div>
	<div class='div_detail_label_lov'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBIDev.predLov.input_typeField" />
		</span>
	</div>
	<div class='div_detail_form'>
		<% 
      		String selectDis = " ";
      		if(modality.equals(SpagoBIConstants.DETAIL_MOD)) { 
      			selectDis = " disabled ";
      			String valueHid = modVal.getITypeCd()+","+modVal.getITypeId();
      		} 
      	%>	
   		<select style='width:180px;' name="input_type" id="input_type" class='portlet-form-input-field' 
			onchange="setLovProviderModified(true);showWizard();">
      	<% 
      	   	String curr_input_type = modVal.getITypeCd();
      	   	if(curr_input_type==null) {
      	   		curr_input_type = "";
      	   	}
      		for(int i=0; i<list.size(); i++){
      			Domain domain = new Domain();
      	       	domain = (Domain)list.get(i);
      	       	String selectedStr = "";
      	       	if(curr_input_type.equals(domain.getValueCd().toString())) {
      	       		selectedStr = " selected='selected' ";
      	       	}
   		%>
      	 	<option value="<%= (String)domain.getValueCd()+","+ (domain.getValueId()).toString()%>" <%=selectedStr%>  > 
      	    	<%= domain.getValueName()%>
      	    </option>
		<% 
   			} 
		%>
 		</select>
	</div>
</div>



<spagobi:error/>



	<script type="text/javascript">
			function showSintaxScript(){
					var divSintax = document.getElementById("sintaxScript");
					var display = divSintax.style.display;
					if (display == "none") {
						document.getElementById("sintaxScript").style.display = "inline";
						document.getElementById("showSintaxScript").innerHTML = "<spagobi:message key = 'SBIDev.scriptWiz.hideSintax'/>";
					}
					else {
						document.getElementById("sintaxScript").style.display = "none";
						document.getElementById("showSintaxScript").innerHTML = "<spagobi:message key = 'SBIDev.scriptWiz.showSintax'/>";
					}
			}
			function showSintaxQuery(){
					var divSintax = document.getElementById("sintaxQuery");
					var display = divSintax.style.display;
					if (display == "none") {
						document.getElementById("sintaxQuery").style.display = "inline";
						document.getElementById("showSintaxQuery").innerHTML = "<spagobi:message key = 'SBIDev.queryWiz.hideSintax'/>";
					}
					else {
						document.getElementById("sintaxQuery").style.display = "none";
						document.getElementById("showSintaxQuery").innerHTML = "<spagobi:message key = 'SBIDev.queryWiz.showSintax'/>";
					}
			}
			function showSintaxJavaClass(){
					var divSintax = document.getElementById("sintaxJavaClass");
					var display = divSintax.style.display;
					if (display == "none") {
						document.getElementById("sintaxJavaClass").style.display = "inline";
						document.getElementById("showSintaxJavaClass").innerHTML = "<spagobi:message key = 'SBIDev.javaClassWiz.hideSintax'/>";
					}
					else {
						document.getElementById("sintaxJavaClass").style.display = "none";
						document.getElementById("showSintaxJavaClass").innerHTML = "<spagobi:message key = 'SBIDev.javaClassWiz.showSintax'/>";
					}
			}
			function showSintaxFixlov(){
					var divSintax = document.getElementById("sintaxFixlov");
					var display = divSintax.style.display;
					if (display == "none") {
						document.getElementById("sintaxFixlov").style.display = "inline";
						document.getElementById("showSintaxFixlov").innerHTML = "<spagobi:message key = 'SBIDev.fixlovWiz.hideSintax'/>";
					}
					else {
						document.getElementById("sintaxFixlov").style.display = "none";
						document.getElementById("showSintaxFixlov").innerHTML = "<spagobi:message key = 'SBIDev.fixlovWiz.showSintax'/>";
					}
			}
	</script>





	<div id="queryWizard" style='width:100%;display:<%=queryDisplay%>'>
		<div style='margin:5px;padding-top:5px;padding-left:5px;' class='portlet-section-header'>
			<spagobi:message key = "SBIDev.queryWiz.wizardTitle" />
		</div> 
		<div style="float:left;" >
			<spagobi:queryWizard 
				connectionName='<%= query.getConnectionName()!= null ? query.getConnectionName().toString() : "" %>' 
				visibleColumns='<%= query.getVisibleColumns()!= null ? query.getVisibleColumns().toString() : "" %>' 
				valueColumns='<%= query.getValueColumns()!= null ? query.getValueColumns().toString() : "" %>' 
				descriptionColumns='<%= query.getDescriptionColumns()!= null ? query.getDescriptionColumns().toString() : "" %>' 
				queryDef='<%= query.getQueryDefinition()!= null ? query.getQueryDefinition().toString() : "" %>' 
				invisibleColumns='<%= query.getInvisibleColumns()!= null ? query.getInvisibleColumns().toString() : "" %>' 
				 /> 	
		</div>
		<div style="width:100%" >
				<span class='portlet-form-field-label'>
					<spagobi:message key = "SBIDev.queryWiz.rulesLabel" />
				</span>
				<a id="showSintaxQuery" 
					 href="javascript:void(0)" 
					 onclick="showSintaxQuery()" 
					 class='portlet-form-field-label'
					 onmouseover="this.style.color='#074BF8';"
					 onmouseout="this.style.color='#074B88';"
					 style="text-decoration:none;">
					<spagobi:message key = "SBIDev.queryWiz.showSintax"/>
				</a>
				<br/>	
				<div style="display:none;" id="sintaxQuery"  >
					<br/>
					<table width="100%">
						<tr height='25'>
							<td>
								<div class='portlet-section-subheader' 
								     style='text-align:center;vertical-align:bottom;' 
									 width="100%">
									<spagobi:message key = "SBIDev.queryWiz.rulesTitle" />
								</div>
								<div class='portlet-section-alternate' width="100%" 
								     style="background-color:#FFFFEF;border:1px solid #dddddd;" >
								  <ul style='margin:5px;padding-left:20px;padding-right:5px;'>   
									<li><spagobi:message key = "SBIDev.queryWiz.profAttr" /></li>
									<li><spagobi:message key = "SBIDev.queryWiz.dotNotation" /></li>
									</ul>
							    </div>
							</td>
							<tr>
									<td>
										<div class='portlet-section-subheader' 
										     style='text-align:center;vertical-align:bottom;' 
												 width="100%">
											<spagobi:message key = "SBIDev.scriptWiz.ProfileAttrsLbl" />
										</div>
										<div class='portlet-section-alternate' width="100%" 
                         style="background-color:#FFFFEF;border: 1px solid #dddddd;">
											<%
											    if(nameAttrs.isEmpty()) {
											    	out.write("<ul style='margin:5px;padding-left:20px;'>");
											    	out.write("<li>");
											    	out.write(PortletUtilities.getMessage("SBIDev.scriptWiz.NoProfileAttrs", "messages"));
											    	out.write("</li>");
                            out.write("</ul>");
                          } else {
											    	out.write("<ul style='margin:5px;padding-left:20px;'>");  
													iterAttrs = nameAttrs.iterator();
													while(iterAttrs.hasNext()) {
														String attributename = (String)iterAttrs.next();
														out.write("<li>");
														out.write(attributename);
														out.write("</li>");
													}
													out.write("</ul>");
											    }
											%>
										</div>
									</td>
									<td width="5px">&nbsp;</td>
							</tr>
							<td width="5px">&nbsp;</td>
						</tr>
						
					</table>
				</div>
		</div>
		<div style="clear:left;">
			&nbsp;
		</div>
  </div>


  
   
  <div id="lovWizard" style='width:100%;display:<%=lovDisplay%>'>
    	<div style='margin:5px;padding-top:5px;padding-left:5px;' class='portlet-section-header'>
  			<spagobi:message key = "SBIDev.lovWiz.wizardTitle" />
  		</div> 
  		<div style="float:left;">
       		<spagobi:lovWizard lovProvider='<%= fixedListDetail.toXML() %>' />
    	</div>
 		<div style="width:100%" >
  			<span class='portlet-form-field-label'>
  				<spagobi:message key = "SBIDev.fixlovWiz.rulesLabel" />
  			</span>
  			<a id="showSintaxFixlov" 
  				 href="javascript:void(0)" 
  				 onclick="showSintaxFixlov()" 
  				 class='portlet-form-field-label'
  				 onmouseover="this.style.color='#074BF8';"
  				 onmouseout="this.style.color='#074B88';"
  				 style="text-decoration:none;">
  				<spagobi:message key = "SBIDev.fixlovWiz.showSintax"/>
  			</a>
  			<br/>
  			<div style="display:none;" id="sintaxFixlov"  >
				<br/>
				<table width="100%">
					<tr height='25'>
						<td>
							<div class='portlet-section-subheader' 
							     style='text-align:center;vertical-align:bottom;' 
								 width="100%">
								<spagobi:message key = "SBIDev.fixlovWiz.rulesTitle" />
							</div>
							<div class='portlet-section-alternate' width="100%" 
							     style="background-color:#FFFFEF;border:1px solid #dddddd;" >
							 	 <ul style='margin:5px;padding-left:20px;padding-right:5px;'>   
									<li><spagobi:message key = "SBIDev.fixlovWiz.profAttr" /></li>
								 </ul>
							</div>
						</td>
					</tr>	
					<tr>
						<td>
							<div class='portlet-section-subheader' 
							     style='text-align:center;vertical-align:bottom;' 
								 width="100%">
								<spagobi:message key = "SBIDev.scriptWiz.ProfileAttrsLbl" />
							</div>
							<div class='portlet-section-alternate' width="100%" 
                         		 style="background-color:#FFFFEF;border: 1px solid #dddddd;">
								<%
								   if(nameAttrs.isEmpty()) {
								    	out.write("<ul style='margin:5px;padding-left:20px;'>");
								    	out.write("<li>");
								    	out.write(PortletUtilities.getMessage("SBIDev.scriptWiz.NoProfileAttrs", "messages"));
								    	out.write("</li>");
                            			out.write("</ul>");
                          			} else {
									   	out.write("<ul style='margin:5px;padding-left:20px;'>");  
										iterAttrs = nameAttrs.iterator();
										while(iterAttrs.hasNext()) {
											String attributename = (String)iterAttrs.next();
											out.write("<li>");
											out.write(attributename);
											out.write("</li>");
										}
										out.write("</ul>");
									}
								%>
							</div>
						</td>
						<td width="5px">&nbsp;</td>
					</tr>
					<tr>
						<td width="5px">&nbsp;</td>
					</tr>
				</table>
			</div>
  		</div>
  		<div style="clear:left;">&nbsp;</div>
  </div>
  





	<div id="scriptWizard" style='width:100%;display:<%=scriptDisplay%>'>
		<div style='margin:5px;padding-top:5px;padding-left:5px;' class='portlet-section-header'>
			<spagobi:message key = "SBIDev.scriptWiz.wizardTitle" />
		</div> 
		<div style="float:left;" />
			<spagobi:scriptWizard 
				script='<%= scriptDet.getScript()!= null ? scriptDet.getScript() : "" %>' />
		</div>
		<div style="width:100%" />
				<span class='portlet-form-field-label'>
					<spagobi:message key = "SBIDev.scriptWiz.ListValExplanation" />
				</span>
				<a id="showSintaxScript" 
					 href="javascript:void(0)" 
					 onclick="showSintaxScript()" 
					 class='portlet-form-field-label'
					 onmouseover="this.style.color='#074BF8';"
					 onmouseout="this.style.color='#074B88';"
					 style="text-decoration:none;">
					<spagobi:message key = "SBIDev.scriptWiz.showSintax"/>
				</a>
				<br/>	
				<div style="display:none;" id="sintaxScript" >
					<br/>
					<table width="100%" style="margin-bottom:5px;">
							<tr height='25'>
									<td>
										<div class='portlet-section-subheader' 
										     style='text-align:center;vertical-align:bottom;' 
												 width="100%">
											<spagobi:message key = "SBIDev.scriptWiz.SintaxLbl" />
										</div>
										<div class='portlet-section-alternate' width="100%" 
                         style="background-color:#FFFFEF;border: 1px solid #dddddd;padding-right:10px;">
											<ul style='margin:5px;padding-left:20px;padding-right:5px;'>
												<li>
													<spagobi:message key = "SBIDev.scriptWiz.FixValLbl" />
													<br/>
													<spagobi:message key = "SBIDev.scriptWiz.FixValExpr" />
												</li>
												<li>
													<spagobi:message key = "SBIDev.scriptWiz.SingleValueProfileAttrLbl" />
													<br/>
													<spagobi:message key = "SBIDev.scriptWiz.SingleValueProfileAttrExpr" />
												</li>
												<li>
													<spagobi:message key = "SBIDev.scriptWiz.MultiValueProfileAttrLbl" />
													<br/>
													<spagobi:message key = "SBIDev.scriptWiz.MultiValueProfileAttrExpr" />
													<br/>
													<spagobi:message key = "SBIDev.scriptWiz.MultiValueProfileAttrExplanation" />
												</li>
												<li>
													<spagobi:message key = "SBIDev.scriptWiz.ScriptlLbl" />
													<br/>
													<spagobi:message key = "SBIDev.scriptWiz.ScriptExpr" />
												</li>
											</ul>
										</div>
									</td>
									<td width="5px">&nbsp;</td>
							</tr>
							<tr>
									<td>
										<div class='portlet-section-subheader' 
										     style='text-align:center;vertical-align:bottom;' 
												 width="100%">
											<spagobi:message key = "SBIDev.scriptWiz.ProfileAttrsLbl" />
										</div>
										<div class='portlet-section-alternate' width="100%" 
                         style="background-color:#FFFFEF;border: 1px solid #dddddd;">
											<%
											    if(nameAttrs.isEmpty()) {
											    	out.write("<ul style='margin:5px;padding-left:20px;'>");
											    	out.write("<li>");
											    	out.write(PortletUtilities.getMessage("SBIDev.scriptWiz.NoProfileAttrs", "messages"));
											    	out.write("</li>");
                            out.write("</ul>");
                          } else {
											    	out.write("<ul style='margin:5px;padding-left:20px;'>");  
													iterAttrs = nameAttrs.iterator();
													while(iterAttrs.hasNext()) {
														String attributename = (String)iterAttrs.next();
														out.write("<li>");
														out.write(attributename);
														out.write("</li>");
													}
													out.write("</ul>");
											    }
											%>
										</div>
									</td>
									<td width="5px">&nbsp;</td>
							</tr>
							<tr height='25'>
									<td>
										<div class='portlet-section-subheader' 
										     style='text-align:center;vertical-align:bottom;' 
												 width="100%">
											<spagobi:message key = "SBIDev.scriptWiz.xmlstruct" />
										</div>
										<div class='portlet-section-alternate' width="100%"
                         style="text-align:left;padding:10px;background-color:#FFFFEF;border: 1px solid #dddddd;">
												&lt;rows&gt; <br/>
												<span>&nbsp;&nbsp;&nbsp;</span>&lt;row 
												<br/> 
												<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><spagobi:message key ="SBIDev.scriptWiz.xmlstructNameAttribute" />1="<spagobi:message key ="SBIDev.scriptWiz.xmlstructValueAttribute" />1" 
												<br/>
												<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><spagobi:message key ="SBIDev.scriptWiz.xmlstructNameAttribute" />2="<spagobi:message key ="SBIDev.scriptWiz.xmlstructValueAttribute" />2" 
												<br/>
												<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>... /&gt;
												<br/>
									<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>.... <br/>
									<span>&nbsp;&nbsp;&nbsp;</span>&lt;visible-columns&gt;
									<br/>
									<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><spagobi:message key ="SBIDev.scriptWiz.xmlstructVisibleColumns" />
									<br/>
									<span>&nbsp;&nbsp;&nbsp;</span>&lt;/visible-columns&gt; 
									<br/>
									<span>&nbsp;&nbsp;&nbsp;</span>&lt;value-column&gt;
									<br/>
									<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
									<spagobi:message key = "SBIDev.scriptWiz.xmlstructValueColumn1" />
									<br/>	
									<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                  <spagobi:message key = "SBIDev.scriptWiz.xmlstructValueColumn2" />
									<span>&nbsp;&nbsp;&nbsp;</span>&lt;/value-column&gt; <br/>
									
									<span>&nbsp;&nbsp;&nbsp;</span>&lt;description-column&gt;
									<br/>
									<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
									<spagobi:message key = "SBIDev.scriptWiz.xmlstructDescriptionColumn" />
									<br/>
									<span>&nbsp;&nbsp;&nbsp;</span>&lt;/description-column&gt;
									<br/>
									
									&lt;/rows&gt; <br/>
									
									</div>
									</td>
									<td width="5px">&nbsp;</td>
							</tr>
					</table>
				</div>
		</div>
		<div style="clear:left;">
			&nbsp;
		</div>
	</div>

	

	<div id="javaClassWizard" style='width:100%;display:<%=javaClassDisplay%>'>
		<div style='margin:5px;padding-top:5px;padding-left:5px;' class='portlet-section-header'>
			<spagobi:message key = "SBIDev.javaClassWiz.title" />
		</div> 
		<div style="float:left;" />
			<spagobi:javaClassWizard 
				javaClassName='<%= javaClassDetail.getJavaClassName()!= null ? javaClassDetail.getJavaClassName() : "" %>'  />
		</div>
		<div style="width:100%" />
				<span class='portlet-form-field-label'>
					<spagobi:message key = "SBIDev.javaClassWiz.ListValExplanation" />
				</span>
				<a id="showSintaxJavaClass" 
					 href="javascript:void(0)" 
					 onclick="showSintaxJavaClass()" 
					 class='portlet-form-field-label'
					 onmouseover="this.style.color='#074BF8';"
					 onmouseout="this.style.color='#074B88';"
					 style="text-decoration:none;">
					<spagobi:message key = "SBIDev.javaClassWiz.showSintax"/>
				</a>
				<br/>	
				<div style="display:none;" id="sintaxJavaClass" >
					<br/>
					<table width="100%" style="margin-bottom:5px;">
							<tr height='25'>
									<td>
										<div class='portlet-section-subheader' 
										     style='text-align:center;vertical-align:bottom;' 
												 width="100%">
											<spagobi:message key = "SBIDev.javaClassWiz.SintaxLbl" />
										</div>
										<div class='portlet-section-alternate' width="100%" 
                         style="background-color:#FFFFEF;border: 1px solid #dddddd;">
											<ul style='margin:5px;padding-left:20px;padding-right:5px;'>
												<li>
													<spagobi:message key = "SBIDev.javaClassWiz.interfaceName" />
													<br/>
													<spagobi:message key = "SBIDev.javaClassWiz.interfaceMethod" />
													<br/>
													<spagobi:message key = "SBIDev.javaClassWiz.interfaceMethodReturn" />
												</li>
											</ul>
										</div>
									</td>
									<td width="5px">&nbsp;</td>
							</tr>
							<tr height='25'>
									<td>
										<div class='portlet-section-subheader' 
										     style='text-align:center;vertical-align:bottom;' 
												 width="100%">
											<spagobi:message key = "SBIDev.javaClassWiz.ProfileAttrsLbl" />
										</div>
										<div class='portlet-section-alternate' width="100%" 
                         style="background-color:#FFFFEF;border: 1px solid #dddddd;">
											<%
											    if(nameAttrs.isEmpty()) {
											    	out.write("<ul style='margin:5px;padding-left:20px;'>");
											    	out.write("<li>");
											    	out.write(PortletUtilities.getMessage("SBIDev.scriptWiz.NoProfileAttrs", "messages"));
											    	out.write("</li>");
                            out.write("</ul>");
                          } else {
											    	out.write("<ul style='margin:5px;padding-left:20px;'>");  
													iterAttrs = nameAttrs.iterator();
													while(iterAttrs.hasNext()) {
														String attributename = (String)iterAttrs.next();
														out.write("<li>");
														out.write(attributename);
														out.write("</li>");
													}
													out.write("</ul>");
											    }
											%>
										</div>
									</td>
									<td width="5px">&nbsp;</td>
							</tr>
							<tr height='25'>
									<td>
										<div class='portlet-section-subheader' 
										     style='text-align:center;vertical-align:bottom;' 
												 width="100%">
											<spagobi:message key = "SBIDev.fixlovWiz.xmlstruct" />
										</div>
										<div class='portlet-section-alternate' width="100%"  
                         style="text-align:left;padding:10px;background-color:#FFFFEF;border: 1px solid #dddddd;">
												&lt;rows&gt; <br/>
												<span>&nbsp;&nbsp;&nbsp;</span>&lt;row 
												<br/> 
												<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><spagobi:message key ="SBIDev.scriptWiz.xmlstructNameAttribute" />1="<spagobi:message key ="SBIDev.scriptWiz.xmlstructValueAttribute" />1" 
												<br/>
												<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><spagobi:message key ="SBIDev.scriptWiz.xmlstructNameAttribute" />2="<spagobi:message key ="SBIDev.scriptWiz.xmlstructValueAttribute" />2" 
												<br/>
												<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>... /&gt;
												<br/>
									<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>.... <br/>
									<span>&nbsp;&nbsp;&nbsp;</span>&lt;visible-columns&gt;
									<br/>
									<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><spagobi:message key ="SBIDev.scriptWiz.xmlstructVisibleColumns" />
									<br/>
									<span>&nbsp;&nbsp;&nbsp;</span>&lt;/visible-columns&gt; 
									<br/>
									<span>&nbsp;&nbsp;&nbsp;</span>&lt;value-column&gt;
									<br/>
									<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
									<spagobi:message key =
										"SBIDev.scriptWiz.xmlstructValueColumn1" />
									<br/>	
									<span>&nbsp;&nbsp;&nbsp;</span>&lt;/value-column&gt; <br/>
									
									<span>&nbsp;&nbsp;&nbsp;</span>&lt;description-column&gt;
									<br/>
									<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
									<spagobi:message key = "SBIDev.scriptWiz.xmlstructDescriptionColumn" />
									<br/>
									<span>&nbsp;&nbsp;&nbsp;</span>&lt;/description-column&gt;
									<br/>
									
									&lt;/rows&gt; <br/>
									</div>
									</td>
									<td width="5px">&nbsp;</td>
							</tr>
					</table>
				</div>
		</div>
		<div style="clear:left;">
			&nbsp;
		</div>
	</div>

	
	
	<table>
		<tr><td>&nbsp;</td></tr>
	</table>

	
	
</form>
	

<!--
</div>

-->


</div> <!-- close background --> 
