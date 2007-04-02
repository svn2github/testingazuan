<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="it.eng.spagobi.bo.BIObject,
				 it.eng.spagobi.bo.BIObjectParameter,
				 it.eng.spagobi.services.modules.DetailBIObjectModule,
				 it.eng.spagobi.bo.dao.IParameterDAO,
				 it.eng.spagobi.bo.dao.DAOFactory,
				 it.eng.spagobi.bo.Parameter,
				 java.util.List,
				 it.eng.spagobi.constants.ObjectsTreeConstants,
				 it.eng.spagobi.constants.AdmintoolsConstants,
				 it.eng.spagobi.bo.Domain,
				 java.util.Iterator,
				 it.eng.spagobi.bo.Engine,
				 it.eng.spagobi.bo.TemplateVersion,
				 it.eng.spagobi.constants.SpagoBIConstants,
				 it.eng.spagobi.utilities.SpagoBITracer,
				 it.eng.spago.navigation.LightNavigationManager,
				 it.eng.spago.base.SourceBean,
				 it.eng.spago.configuration.ConfigSingleton,
				 java.util.TreeMap,
				 java.util.Collection,
				 java.util.ArrayList,
				 java.util.Date" %>
<%@ page import="it.eng.spagobi.utilities.GeneralUtilities"%>
<%@ page import="it.eng.spagobi.utilities.ChannelUtilities"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>


<%
	// GET RESPONSE OBJECTS
    SourceBean moduleResponse = (SourceBean) aServiceResponse.getAttribute("DetailBIObjectModule"); 
	BIObject obj = (BIObject) moduleResponse.getAttribute(DetailBIObjectModule.NAME_ATTR_OBJECT);
	List listEngines = (List) moduleResponse.getAttribute(DetailBIObjectModule.NAME_ATTR_LIST_ENGINES);
	List listTypes = (List) moduleResponse.getAttribute(DetailBIObjectModule.NAME_ATTR_LIST_OBJ_TYPES);
	List listStates = (List) moduleResponse.getAttribute(DetailBIObjectModule.NAME_ATTR_LIST_STATES);
	String modality = (String) moduleResponse.getAttribute(ObjectsTreeConstants.MODALITY);
	String actor = (String) moduleResponse.getAttribute(SpagoBIConstants.ACTOR);
	
	
	// CREATE PAGE URLs
	Map formUrlPars = new HashMap();
	//formUrlPars.put("PAGE", "detailBIObjectPage");
	//if(modality != null){
	//	formUrlPars.put("MESSAGEDET", modality);
   	//}
	//formUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
    String formUrl = urlBuilder.getUrl(request, formUrlPars);
	
    Map backUrlPars = new HashMap();
    backUrlPars.put("PAGE", "detailBIObjectPage");
    backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
    backUrlPars.put("MESSAGEDET", "EXIT_FROM_DETAIL");
    backUrlPars.put(SpagoBIConstants.ACTOR, actor);
    String backUrl = urlBuilder.getUrl(request, backUrlPars);
   	
   	// CHECK IF BOOKLET MODULE IS INSTALLED
   	boolean isCompBookletsInst = GeneralUtilities.isModuleInstalled("booklets");
%>








<script>
function showEngField(docType) {
	var ind = docType.indexOf(",");
	var type = docType.substring(ind+1);
	var engines = document.objectForm.engine.options;
	engines.length = 0;
	<%
	for (int i = 0; i < listEngines.size(); i++) {
		Engine en = (Engine) listEngines.get(i);
		out.print("var engine_" + i + " = new Option('" + en.getName() + "', '" + en.getId().toString() + "');\n");
		Integer biobjTypeId = en.getBiobjTypeId();
		Domain aDomain = DAOFactory.getDomainDAO().loadDomainById(biobjTypeId);
		out.print("if ('" + aDomain.getValueCd() + "' == type) {\n");
		out.print("	engines[engines.length] = engine_" + i + ";\n");
		if (obj.getEngine() != null) {
			out.print("	if ('" + en.getId().toString() + "' == '" + obj.getEngine().getId().toString() + "') {\n");
			out.print("		document.getElementById('engine').selectedIndex = engines.length -1;\n");
			out.print("	}\n");
		}
		out.print("}\n");
	}
	%>
}

function checkFormVisibility(docType) {
	var ind = docType.indexOf(",");
	var type = docType.substring(ind+1);
	// hide or shiw template upload div for Booklet 
	var divUpload = document.getElementById("form_upload");
	if(type=="BOOKLET") {
		divUpload.style.display="none";
	} else {
		divUpload.style.display="inline";
	}
	// hide template dynamic creation button for booklet and olap document 
	var divLinkConf = document.getElementById("link_obj_conf");
	if(type=="BOOKLET" || type=="OLAP") {
		divLinkConf.style.display="inline";
	} else {
		divLinkConf.style.display="none";
	}
	
}
</script>











<form method='POST' action='<%=formUrl%>' id = 'objectForm' name='objectForm' enctype="multipart/form-data">
	
	<input type='hidden' name='PAGE' value='detailBIObjectPage' />
	<input type='hidden' name='<%=LightNavigationManager.LIGHT_NAVIGATOR_DISABLED%>' value='true' />
    <%--
    <%
		if(modality != null){
	%>	
		<input type='hidden' name='MESSAGEDET' value='<%=modality%>' />
   	<%
   		}
    %>
	--%>


<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' 
		    style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "SBIDev.docConf.docDet.title" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<% 
			if(modality.equalsIgnoreCase(ObjectsTreeConstants.DETAIL_MOD)){
				if(ChannelUtilities.isPortletRunning()) {	
		%>
		<td class='header-button-column-portlet-section'>
			<input type="hidden" name="" value="" id="loadLinksLookup">
			<a href='javascript:checkDocumentType("<spagobi:message key = "SBIDev.docConf.docDet.saveBeforeLinksConfig" />");'> 
			<img style="margin-top:2px;height:21px;" name='links' id='links' class='header-button-image-portlet-section'
				   src='<%=urlBuilder.getResourceLink(request, "/img/links.jpg") %>'
      		 title='<spagobi:message key = "SBIDev.docConf.docDet.linkButton" />' 
      		 alt='<spagobi:message key = "SBIDev.docConf.docDet.linkButton" />' />
			</a>
		</td>
		<% 		
				}
			}
		%>
		<td class='header-button-column-portlet-section'>
			<input type='image' name='save' id='save' value='true' class='header-button-image-portlet-section'
				src='<%=urlBuilder.getResourceLink(request, "/img/save.png") %>'
      				title='<spagobi:message key = "SBIDev.docConf.docDet.saveButt" />' alt='<spagobi:message key = "SBIDev.docConf.docDet.saveButt" />'
			/>
		</td>
		<td class='header-button-column-portlet-section'>
			<input type='image' name='saveAndGoBack' id='saveAndGoBack' value='true' class='header-button-image-portlet-section'
				src='<%= urlBuilder.getResourceLink(request, "/img/saveAndGoBack.png") %>'
      				title='<spagobi:message key = "SBIDev.docConf.docDet.saveAndGoBackButt" />' alt='<spagobi:message key = "SBIDev.docConf.docDet.saveAndGoBackButt" />'
			/> 
		</td>
		<td class='header-button-column-portlet-section'>
			<% if(modality.equalsIgnoreCase(ObjectsTreeConstants.DETAIL_MOD)) {%>
				<a href='javascript:saveAndGoBackConfirm("<spagobi:message key = "SBIDev.docConf.docDet.saveConfirm" />","<%=backUrl%>")'> 
			<% } else { %>
				<a href='<%=backUrl%>'>
			<% } %>
      				<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBIDev.docConf.docDet.backButt" />' 
      				src='<%= urlBuilder.getResourceLink(request, "/img/back.png")%>' alt='<spagobi:message key = "SBIDev.docConf.docDet.backButt"/>' />
			</a>
		</td>
	</tr>
</table>



<div class='div_background_no_img' >




<input type='hidden' value='<%= obj.getId() %>' name='id' />
<input type='hidden' value='<%= modality %>' name='MESSAGEDET' />
<input type='hidden' value='<%= actor %>' name='<%= SpagoBIConstants.ACTOR %>' />
<input type='hidden' value='' name='' id='saveBIObjectParameter'>
	
<% if(modality.equalsIgnoreCase(ObjectsTreeConstants.DETAIL_MOD)) { %>
	<input type='hidden' value='<%= obj.getPath() %>' name='<%= ObjectsTreeConstants.PATH %>' />
<% } %>
	
<table width="100%" cellspacing="0" border="0" id = "fieldsTable" >
	<tr>
		<td>
			<div class="div_detail_area_forms">
				<div class='div_detail_label'>
					<span class='portlet-form-field-label'>
						<spagobi:message key = "SBIDev.docConf.docDet.labelField" />
					</span>
				</div>
				<div class='div_detail_form'>
					<input class='portlet-form-input-field' type="text" style='width:230px;' 
							name="label" id="label" value="<%=obj.getLabel()%>" maxlength="20">
					&nbsp;*
				</div>
				<div class='div_detail_label'>
					<span class='portlet-form-field-label'>
						<spagobi:message key = "SBIDev.docConf.docDet.nameField" />
					</span>
				</div>
				<div class='div_detail_form'>
					<input class='portlet-form-input-field' type="text" style='width:230px;' 
							name="name" id="name" value="<%=obj.getName()%>" maxlength="40">
					&nbsp;*
				</div>
				<div class='div_detail_label'>
					<span class='portlet-form-field-label'>
						<spagobi:message key ="SBIDev.docConf.docDet.descriptionField" />
					</span>
				</div>
				<div class='div_detail_form'>
					<% 
		      		String desc = obj.getDescription();
		      		if(desc==null) {
		      			desc = "";
		      		}
		      		%>
					<input class='portlet-form-input-field' style='width:230px;' type="text" 
 							name="description" id="description" value="<%=desc%>" maxlength="160">
				</div>
				<div class='div_detail_label' style='display:none;'>
					<span class='portlet-form-field-label'>
						<spagobi:message key = "SBIDev.docConf.docDet.relNameField" />
					</span>
				</div>
				<div class='div_detail_form' style='display:none;'>
					<% 
		      		String relName = obj.getRelName();
		      		if(relName==null) {
		      			relName = "";
		      		}
		      		%>
					<input class='portlet-form-input-field' style='width:230px;' type="text" 
							name="relname" id="relname" value="<%=relName%>" maxlength="400">
				</div>
				<div class='div_detail_label'>
					<span class='portlet-form-field-label'>
						<spagobi:message key = "SBIDev.docConf.docDet.typeField" />
					</span>
				</div>
				<div class='div_detail_form'>
					<select class='portlet-form-input-field' style='width:230px;' 
							name="type" id="type" 
							onchange = 'showEngField(this.value);checkFormVisibility(this.value);'>
		      		<% 
		      		    Iterator iterdom = listTypes.iterator();
		      		    while(iterdom.hasNext()) {
		      		    	Domain type = (Domain)iterdom.next();
		      		    	String BIobjTypecode = obj.getBiObjectTypeCode();
		      		    	String currTypecode = type.getValueCd();
		      		    	if(currTypecode.equalsIgnoreCase("BOOKLET")) {
		      		    		if(!isCompBookletsInst) {
		      		    			continue;
		      		    		}
		      		    	}
		      		    	boolean isType = false;
		      		    	if(BIobjTypecode.equals(currTypecode)){
		      		    		isType = true;   
							}
		      		%>
		      			<option value="<%=type.getValueId() + "," + type.getValueCd()  %>"<%if(isType) out.print(" selected='selected' ");  %>><%=type.getValueName()%></option>
		      		<% 
		      		    }
		      		%>
		      		</select>
				</div>
				<div class='div_detail_label'>
					<span class='portlet-form-field-label'>
						<spagobi:message key = "SBIDev.docConf.docDet.engineField" />
					</span>
				</div>
				
			
				<div class='div_detail_form'>
		      		<select class='portlet-form-input-field' style='width:230px;' 
							name="engine" id="engine" >
					<%
						Iterator itereng = listEngines.iterator();
		      			while(itereng.hasNext()) {
		      		    	Engine engine = (Engine)itereng.next();
		      		    	String objEngName = (obj.getEngine() != null ? obj.getEngine().getName() : null);
		      		    	String currEngName = engine.getName();
		      		    	boolean isEngine = false;
		      		    	if( (!obj.getBiObjectTypeCode().equals("DATAMART")) && 
		      		    		(!obj.getBiObjectTypeCode().equals("DASH")) && 
		      		    		(objEngName != null) &&  
		      		    		objEngName.equals(currEngName)){
		      		    		isEngine = true; 
							}
		      		%>
		      			<option value="<%=engine.getId().toString() %>"<%if (isEngine) out.print(" selected='selected' ");  %>><%=engine.getName()%></option>
		      		<% 	
		      			}
		      		%>
		      		</select>
				</div> 

			<!-- DISPLAY COMBO FOR STATE SELECTION -->
			<!-- IF THE USER IS A DEV ACTOR THE COMBO FOR THE STATE SELECTION CONTAINS ONLY A VALUE
     			 (development) SO IS NOT USEFUL TO SHOW IT	 -->
            <% if(actor.equalsIgnoreCase(SpagoBIConstants.DEV_ACTOR)){%>
				<div class='div_detail_label' style='display:none;'>
					<span class='portlet-form-field-label'>
						<spagobi:message key = "SBIDev.docConf.docDet.stateField" />
					</span>
				</div>
				<div class='div_detail_form' style='display:none;'>
					<select class='portlet-form-input-field' style='width:230px;' name="state" id="state"> 
		      			<%     
		      		    	Iterator iterstates = listStates.iterator();
		      		    	while(iterstates.hasNext()) {
		      		    		Domain state = (Domain)iterstates.next();
		      		    		String objState = obj.getStateCode();
		      		    		String currState = state.getValueCd();
		      		    		boolean isState = false;
		      		    		if(objState.equals(currState)){
		      		    			isState = true;   
		      		    		}
		      		    		if (state.getValueCd().equalsIgnoreCase("DEV")){ %>
		      						<option value="<%=state.getValueId() + "," + state.getValueCd()  %>"<%if(isState) out.print(" selected='selected' ");  %>><%=state.getValueName()%></option>
		      					<%  }  
		      		   		 }%>
		      		  </select>  
				</div>               	
            <% }else{ %>
				<div class='div_detail_label'>
					<span class='portlet-form-field-label'>
						<spagobi:message key = "SBIDev.docConf.docDet.stateField" />
					</span>
				</div>  
 				<div class='div_detail_form'>
					<select class='portlet-form-input-field' style='width:230px;' name="state" id="state">
		      			<% 
		      		    Iterator iterstates = listStates.iterator();
		      		    while(iterstates.hasNext()) {
		      		    	Domain state = (Domain)iterstates.next();
		      		    	String objState = obj.getStateCode();
		      		    	String currState = state.getValueCd();
		      		    	boolean isState = false;
		      		    	if(objState.equals(currState)){
		      		    		isState = true;   
		      		    	}
		      			%>
		      				<option value="<%=state.getValueId() + "," + state.getValueCd()  %>"<%if(isState) out.print(" selected='selected' ");  %>><%=state.getValueName()%></option>
		      			<%  
		      		    }
		      			%>
		      		</select>	
				</div>
            <% } %>
                        
            <!-- DISPLAY RADIO BUTTON FOR CRYPT SELECTION -->
			<!-- FOR THE CURRENT RELEASE THIS RADIO IS HIDE -->
		    	<div class='div_detail_label'>
					<span class='portlet-form-field-label'>
						<spagobi:message key = "SBIDev.docConf.docDet.criptableField" />
					</span>
				</div>
				<div class='div_detail_form'>
					<% 
		      	      boolean isCrypt = false;
		      	      int cript = obj.getEncrypt().intValue();
		      	      if(cript > 0) { isCrypt = true; }
		      	     %> 
		      	   	<input type="radio" name="criptable" value="1" <% if(isCrypt) { out.println(" checked='checked' "); } %> >
										<span class="portlet-font">True</span>
								</input>
		      	   	<input type="radio" name="criptable" value="0" <% if(!isCrypt) { out.println(" checked='checked' "); } %> >
										<span class="portlet-font">False</span>
								</input>
				</div>


			<!-- DISPLAY RADIO BUTTON FOR VISIBLE SELECTION -->
		    	<div class='div_detail_label'>
					<span class='portlet-form-field-label'>
						<spagobi:message key = "SBIDev.docConf.docDet.visibleField" />
					</span>
				</div>
				<div class='div_detail_form'>
					<% 
		      	      boolean isVisible = false;
		      	      
		      	      int visible = (obj.getVisible()!=null)? obj.getVisible().intValue(): 1;
		      	      if(visible > 0) { isVisible = true; }
		      	     %> 
				   	<input type="radio" name="visible" value="1" <% if(isVisible) { out.println(" checked='checked' "); } %>>
								<span class="portlet-font">True</span>
						</input>
		      	<input type="radio" name="visible" value="0" <% if(!isVisible) { out.println(" checked='checked' "); } %>>
								<span class="portlet-font">False</span>
						</input>
				</div>


				<%
					String styleDivFormUpload = " ";
					String BIobjTypecode = obj.getBiObjectTypeCode();
				    if(BIobjTypecode.equalsIgnoreCase("BOOKLET"))
				    	styleDivFormUpload = " style='display:none' ";
				%>

				<!-- DISPLAY FORM FOR TEMPLATE  UPLOAD -->
				<div id="form_upload" <%=styleDivFormUpload%>>
					<div class='div_detail_label'>
						<span class='portlet-form-field-label'>
							<spagobi:message key = "SBIDev.docConf.docDet.templateField" />
						</span>
					</div>
					<div class='div_detail_form'>
						<input class='portlet-form-input-field' type="file" 
			      		       name="uploadFile" id="uploadFile" onchange='fileToUploadInserted()'/>
					</div>
				</div>	
				
				
				
			    <!-- TEMPLATE LABEL AND BUTTONS FOR BOOKLET AND OLAP -->
				<% 
					String styleDivLinkConf = " ";
					BIobjTypecode = obj.getBiObjectTypeCode();
			    	if(BIobjTypecode.equalsIgnoreCase("BOOKLET") || BIobjTypecode.equalsIgnoreCase("OLAP"))
			    		styleDivLinkConf = " style='display:inline' ";
			    	else styleDivLinkConf = " style='display:none' ";
			    %>	
			    <!-- LINK FOR OBJECT CONFIGURATION -->
			    <div id="link_obj_conf" <%=styleDivLinkConf%>>
					<div class='div_detail_label'>
						<span class='portlet-form-field-label'>
							<spagobi:message key = "SBIDev.docConf.docDet.templateBuild" />
						</span>
					</div>
					<div class='div_detail_form'>
					<% 
						boolean hasTemplates = false;
						Map objTemps = obj.getTemplateVersions();
		 				if( (objTemps!=null) && !objTemps.isEmpty() ) {
		 					hasTemplates = true;
		 				}
						if(!hasTemplates) {
							String hrefConf = "";
							if(!modality.equalsIgnoreCase(ObjectsTreeConstants.DETAIL_INS)) { 
								Map confUrlPars = new HashMap();
								confUrlPars.put(SpagoBIConstants.PAGE, SpagoBIConstants.DOCUMENT_TEMPLATE_BUILD);
								confUrlPars.put(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.NEW_DOCUMENT_TEMPLATE);
								confUrlPars.put(ObjectsTreeConstants.OBJECT_ID, obj.getId().toString());
								confUrlPars.put(SpagoBIConstants.ACTOR, actor);
							    hrefConf = urlBuilder.getUrl(request, confUrlPars);
							} else {
								hrefConf = "javascript:alert('"+msgBuilder.getMessage(aRequestContainer, "sbi.detailbiobj.objectnotsaved", "messages")+"')";
							}				
					%>	
							<a href="<%=hrefConf%>">
								<img class='header-button-image-portlet-section' 
	      				 			 title='<spagobi:message key = "sbi.detailbiobj.generateNewTemplate" />' 
	      				 			 src='<%=urlBuilder.getResourceLink(request, "/img/configure_booklet.jpg")%>' 
	      				 			 alt='<spagobi:message key = "sbi.detailbiobj.generateNewTemplate"  />' />
							</a>
					<%
						} else { // if(!hasTemplates)
							Map editUrlPars = new HashMap();
							editUrlPars.put(SpagoBIConstants.PAGE, SpagoBIConstants.DOCUMENT_TEMPLATE_BUILD);
							editUrlPars.put(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.EDIT_DOCUMENT_TEMPLATE);
							editUrlPars.put(ObjectsTreeConstants.OBJECT_ID, obj.getId().toString());
							editUrlPars.put(SpagoBIConstants.ACTOR, actor);
							String editUrlStr = urlBuilder.getUrl(request, editUrlPars);
					%>
							<a href="<%=editUrlStr%>">
								<img class='header-button-image-portlet-section' 
	      				 			 title='<spagobi:message key = "sbi.detailbiobj.editTemplate" />' 
	      				 			 src='<%=urlBuilder.getResourceLink(request, "/img/edit_temp_booklet.jpg")%>' 
	      				 			 alt='<spagobi:message key = "sbi.detailbiobj.editTemplate"  />' />
							</a> 	
					<%
				 		}
					%>
					</div>
	        </div>
	        
	        <!-- engine list and template buttons adjustment based on the document type -->
	        <script>
	        	var pos = document.getElementById('type').selectedIndex;
	        	typeValue = document.getElementById('type').options[pos].value;
				showEngField(typeValue);
	        	checkFormVisibility(typeValue);					
			</script>
			
			
        </div> 






	<!-- CLOSE COLUMN WITH DATA FORM  -->
	</td>
	

	<!-- OPEN COLUMN WITH TREE FUNCTIONALITIES (INSERT MODE) OR TEMPLATE VERSION (MODIFY MODE)  -->	     
	<td width="60%">
	<div style='padding:5px;'>
		<a class="portlet-form-field-label" style="text-decoration:none;" 
		   onmouseover="this.style.color='#074BF8';"
		   onmouseout="this.style.color='#074B88';" 
		   href='javascript:void(0)' onclick='switchView()' id='switchView'>
			<spagobi:message key = "SBIDev.docConf.docDet.showTemplates" />
		</a>
	</div>
	
	

	<script>
	function switchView() {
		var treeView = document.getElementById('folderTree').style.display;
		if (treeView == 'inline') {
			document.getElementById('folderTree').style.display = 'none';
			document.getElementById('versionTable').style.display = 'inline';
			document.getElementById('switchView').innerHTML = '<spagobi:message key = "SBIDev.docConf.docDet.showFolderTree" />';
		}
		else {
			document.getElementById('folderTree').style.display = 'inline';
			document.getElementById('versionTable').style.display = 'none';
			document.getElementById('switchView').innerHTML = '<spagobi:message key = "SBIDev.docConf.docDet.showTemplates" />';
		}
	}
	</script>
	
    	<div style='padding:5px;display:inline;' id='folderTree'>
    		<spagobi:treeObjects moduleName="DetailBIObjectModule"  
    			 htmlGeneratorClass="it.eng.spagobi.presentation.treehtmlgenerators.FunctionalitiesTreeInsertObjectHtmlGenerator" />    	
    	</div>
	
	
    	<div style='padding-left:5px;padding-right:5px;padding-bottom:5px;display:none;' id='versionTable'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBIDev.docConf.docDet.templateVersionField" />
		</span>
		<div style='border: 1px solid black;max-height:160px;overflow:auto;'>
			
			
			
			
			
			
			<table> 
				<% 
					TreeMap templates = obj.getTemplateVersions();
		 			if(templates==null)
		 				templates = new TreeMap();
					String curVer = "";
					if(obj.getCurrentTemplateVersion() != null) 
						curVer = obj.getCurrentTemplateVersion().getVersionName();
					int numTemp = templates.size();  
					if(numTemp == 0) {
						out.print("<tr class='portlet-section-body'>");
		      			out.print("<td class='portlet-font'>No Version Found</td></tr>");
		      		}
					Collection versions = templates.values();
					Iterator iterVers = versions.iterator();
					ArrayList reverseVersions = new ArrayList();
					while(iterVers.hasNext()) {
						Object objVer = iterVers.next();
						reverseVersions.add(0, objVer);
					}
					Iterator iterTemp = reverseVersions.iterator();
		      		while(iterTemp.hasNext()) {
		      			TemplateVersion tempVer = (TemplateVersion)iterTemp.next();
		      		 	String checkStr = " ";
		      		    boolean isCurrentVer = false;
		      		    if(curVer.equalsIgnoreCase(tempVer.getVersionName())) {
		      		       	checkStr = " checked='checked' ";
		      		      	isCurrentVer = true;
		      		    }
		      		    out.print("<tr class='portlet-section-body'>");
		      		    out.print("<td class='portlet-font' width='40px'>"+tempVer.getVersionName()+"</td>");
		      		    String dateStr = tempVer.getDataLoad();
		      		    out.print("<td class='portlet-font' width='220px'>"+tempVer.getDataLoad()+"</td>");
		      		    out.print("<td class='portlet-font' width='150px'>"+tempVer.getNameFileTemplate()+"</td>");
		      		        
		      			Map eraseVerUrlPars = new HashMap();
		      			eraseVerUrlPars.put("PAGE", "detailBIObjectPage");
		      			eraseVerUrlPars.put("MESSAGEDET", SpagoBIConstants.ERASE_VERSION);
		      			eraseVerUrlPars.put(SpagoBIConstants.VERSION, tempVer.getVersionName());
		      			eraseVerUrlPars.put(AdmintoolsConstants.OBJECT_ID, obj.getId().toString());
		      			eraseVerUrlPars.put(SpagoBIConstants.ACTOR, actor);
		      			eraseVerUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
						String eraseVerUrlStr = urlBuilder.getUrl(request, eraseVerUrlPars);
						
						String pathTemp = obj.getPath() + "/template";
   						String downl = ChannelUtilities.getSpagoBIContextName(request) + "/ContentRepositoryServlet?jcrPath="+pathTemp+"&version="+tempVer.getVersionName()+"&fileName="+tempVer.getNameFileTemplate();
		      		       
		      		        if(isCurrentVer) {
		      		        	out.print("<td class='portlet-font' >&nbsp;</a></td>");
		      		        } else {
		      		        	out.print("<td class='portlet-font' ><a href='javascript:deleteVersionConfirm(\""+msgBuilder.getMessage(aRequestContainer, "SBIDev.docConf.docDet.deleteVersionConfirm", "messages")+"\", \""+eraseVerUrlStr+"\")' style='font-size:9px;' >" + msgBuilder.getMessage(aRequestContainer, "SBIDev.docConf.execBIObject.eraseLink", "messages") + "</a></td>");
		      		        }
		      		        out.print("<td class='portlet-font' ><a href='"+downl+"' style='font-size:9px;' >" +msgBuilder.getMessage(aRequestContainer, "SBIDev.docConf.execBIObject.downloadLink", "messages") + "</a></td>");
		      		        
		      		        if(numTemp > 1) {
		      		        	out.print("<td class='portlet-font'><input type='radio' value='"+tempVer.getVersionName()+" 'name='versionTemplate' onchange='versionTemplateSelected()' "+checkStr+" /></td></tr>");
		      		        } else {
		      		        	out.print("<td class='portlet-font'>&nbsp;</td></tr>");
		      		        }
		      		        
		      		}
		      		%>    
		      	</table>








		</div>
	</div>

     	</td>
      </tr>
   </table>   <!-- CLOSE TABLE FORM ON LEFT AND VERSION ON RIGHT  -->

	






	<spagobi:error/>








<% if(modality.equalsIgnoreCase(ObjectsTreeConstants.DETAIL_INS)) { %>
</form>
<% } else if(modality.equalsIgnoreCase(ObjectsTreeConstants.DETAIL_MOD)) {
  		BIObjectParameter objPar = (BIObjectParameter) moduleResponse.getAttribute(DetailBIObjectModule.NAME_ATTR_OBJECT_PAR);
%>
	
<div style='width:100%;visibility:visible;' class='UITabs' id='tabPanelWithJavascript' name='tabPanelWithJavascript'>
	<div class="first-tab-level" style="background-color:#f8f8f8">
		<div style="overflow: hidden; width:  100%">
			<input type='hidden' id='selected_obj_par_id' name='' value=''/>
<%
	List biObjParams = obj.getBiObjectParameters();
	String obj_par_idStr = (String) moduleResponse.getAttribute("selected_obj_par_id");
	Integer obj_par_idInt = new Integer(obj_par_idStr);
	int obj_par_id = Integer.parseInt(obj_par_idStr);
	String linkClass = "tab";
	boolean foundSelectedParId = false;
	for (int i = 0; i < biObjParams.size(); i++){
		BIObjectParameter biObjPar = (BIObjectParameter) biObjParams.get(i);
		if (biObjPar.getId().equals(obj_par_idInt)) {
			linkClass = "tab selected";
			foundSelectedParId = true;
		}
		else linkClass = "tab";
	%>
					<div class='<%= linkClass%>'>
						<a href='javascript:changeBIParameter("<%= biObjPar.getId().toString() %>", "<spagobi:message key = "SBIDev.docConf.docDetParam.saveBIParameterConfirm" />")'
						   style="color:black;"> 
							<%= biObjPar.getLabel()%>
						</a>
					</div>
<%	}
	if (obj_par_id < 0 || !foundSelectedParId) 
		linkClass = "tab selected";
	else 
		linkClass = "tab";
%>
					<div class='<%= linkClass%>'>
						<a href='javascript:changeBIParameter("-1", "<spagobi:message key = "SBIDev.docConf.docDetParam.saveBIParameterConfirm" />")'
						   style="color:black;"> 
							<spagobi:message key = "SBIDev.docConf.docDet.newBIParameter" />
					    </a>
					</div>
			</div>
		</div>
	</div>


<script>

<%
	BIObject initialBIObject = (BIObject) aSessionContainer.getAttribute("initial_BIObject");
	if (initialBIObject == null) initialBIObject = obj;
	BIObjectParameter initialBIObjectParameter = (BIObjectParameter) aSessionContainer.getAttribute("initial_BIObjectParameter");
	if (initialBIObjectParameter == null) initialBIObjectParameter = objPar;
%>

var versionTemplateChanged = 'false';
var fileUploadChanged = 'false';

function versionTemplateSelected () {
	versionTemplateChanged = 'true';
}

function fileToUploadInserted() {
	fileUploadChanged = 'true';
}

function isBIObjectFormChanged() {
	
	var biobjFormModified = 'false';
	
	var label = document.getElementById('label').value;
	var name = document.getElementById('name').value;
	var description = document.getElementById('description').value;
	var relName = document.getElementById('relname').value;
	var type = document.getElementById('type').value;
	var engine = document.getElementById('engine').value;
	var state = document.getElementById('state').value;

	if ((label != '<%=initialBIObject.getLabel()%>')
		|| (name != '<%=initialBIObject.getName()%>')
		|| (description != '<%=initialBIObject.getDescription()%>')
		|| (relName != '<%=initialBIObject.getRelName()%>')
		|| (type != '<%=initialBIObject.getBiObjectTypeID()+","+initialBIObject.getBiObjectTypeCode()%>')
		|| (engine != '<%=initialBIObject.getEngine().getId()%>')
		|| (state != '<%=initialBIObject.getStateID()+","+initialBIObject.getStateCode()%>') 
		|| (versionTemplateChanged == 'true')
		|| (fileUploadChanged == 'true')) {
			
		biobjFormModified = 'true';
	}
	
	return biobjFormModified;
	
}

function isBIParameterFormChanged () {
	
	var biobjParFormModified = 'false';
	
	var objParLabel = document.getElementById('objParLabel').value;
	var par_Id = document.getElementById('par_Id').value;
	var parurl_nm = document.getElementById('parurl_nm').value;
			
	if ((objParLabel != '<%=initialBIObjectParameter.getLabel()%>')
		|| (par_Id != '<%=(initialBIObjectParameter.getParID() == null || initialBIObjectParameter.getParID().intValue() == -1) ? "" : initialBIObjectParameter.getParID().toString()%>')
		|| (parurl_nm != '<%=initialBIObjectParameter.getParameterUrlName()%>') )
	{
		biobjParFormModified = 'true';
	}
	
	return biobjParFormModified;
	
}

function changeBIParameter (objParId, message) {

	var biobjParFormModified = isBIParameterFormChanged();
	
	document.getElementById('selected_obj_par_id').name = 'selected_obj_par_id';
	document.getElementById('selected_obj_par_id').value = objParId;
	
	if (biobjParFormModified == 'true') 
	{
		if (confirm(message))
		{
			document.getElementById('saveBIObjectParameter').name = 'saveBIObjectParameter';
			document.getElementById('saveBIObjectParameter').value= 'yes';
		}
		else
		{
			document.getElementById('saveBIObjectParameter').name = 'saveBIObjectParameter';
			document.getElementById('saveBIObjectParameter').value= 'no';
		}
	}
	
	document.getElementById('objectForm').submit();
}

function saveAndGoBackConfirm(message, url){

		var biobjFormModified = isBIObjectFormChanged();
		var biobjParFormModified = isBIParameterFormChanged();

		if (biobjFormModified == 'true' || biobjParFormModified == 'true') {
			if (confirm(message)) {
				document.getElementById('saveAndGoBack').click();
			} else {
				location.href = url;
			}
		} else {
			location.href = url;
		}
}

function deleteVersionConfirm(message, url){
	if (confirm(message)){
            location.href = url;
        }
}

function deleteBIParameterConfirm (message) {
	if (confirm(message)) {
		document.getElementById('deleteBIObjectParameter').name = 'deleteBIObjectParameter';
		document.getElementById('deleteBIObjectParameter').value = '<%= objPar.getId() %>';
        	document.getElementById('objectForm').submit();
        }
}

function verifyDependencies() {
	<%
	List correlations = DAOFactory.getObjParuseDAO().loadObjParuses(objPar.getId());
	if (correlations != null && correlations.size() > 0) {
		%>
		document.getElementById('parameterCannotBeChanged').style.display = 'inline';
		<%
	} else {
		%>
		document.getElementById('loadParametersLookup').name = 'loadParametersLookup';
		document.getElementById('loadParametersLookup').value = 'loadParametersLookup';
		document.getElementById('save').click();
		<%
	}
	%>
}

function saveBIParameterConfirm (message) {

	var biobjParFormModified = isBIParameterFormChanged();

	if (biobjParFormModified == 'true') 
	{
		if (confirm(message))
		{
			document.getElementById('saveBIObjectParameter').name = 'saveBIObjectParameter';
			document.getElementById('saveBIObjectParameter').value= 'yes';
		}
		else
		{
			document.getElementById('saveBIObjectParameter').name = 'saveBIObjectParameter';
			document.getElementById('saveBIObjectParameter').value= 'no';
		}
	}
	
	document.getElementById('goToDependenciesPage').name = 'goToDependenciesPage';
	document.getElementById('goToDependenciesPage').value= 'goToDependenciesPage';
	document.getElementById('save').click();
}

function checkDocumentType(message) {
	var type = document.getElementById('type').value;
	if (type.match('REPORT') != null || type.match('DATA_MINING') != null) {
		var biobjFormModified = isBIObjectFormChanged();
		var biobjParFormModified = isBIParameterFormChanged();
		if (biobjFormModified == 'true' || biobjParFormModified == 'true') {
			if (confirm(message)) {
				document.getElementById('loadLinksLookup').name = 'loadLinksLookup';
				document.getElementById('loadLinksLookup').value = 'loadLinksLookup';
				document.getElementById('save').click();
			}
		} else {
			document.getElementById('loadLinksLookup').name = 'loadLinksLookup';
			document.getElementById('loadLinksLookup').value = 'loadLinksLookup';
			document.getElementById('save').click();
		}
	} else {
		alert('<spagobi:message key = "SBIDev.docConf.docDet.noPermissibleLinks" />');
	}
}
</script>










<table  class='header-sub-table-portlet-section' >		
	<tr class='header-sub-row-portlet-section'>
		<% if (obj_par_id != -1) { %>
		<td class='header-sub-title-column-portlet-section'>
			<spagobi:message key = "SBIDev.docConf.docDetParam.title" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<% 
		int objParPriority = objPar.getPriority().intValue();
		if (objParPriority > 1) { %>
		<td class='header-button-column-portlet-section'>
			<a href='javascript:void(0);'
			   onclick='document.getElementById("priority").selectedIndex=<%= objParPriority - 2 %>;document.getElementById("objectForm").submit();'>
				<img 	src= '<%=urlBuilder.getResourceLink(request, "/img/Back.gif") %>'
					title='<spagobi:message key = "SBIDev.docConf.docDetParam.increasePriority" />' 
					alt='<spagobi:message key = "SBIDev.docConf.docDetParam.increasePriority" />'
				/>
			</a>
		</td>
		<% } %>
		<% if (objParPriority < biObjParams.size()) { %>
		<td class='header-button-column-portlet-section'>
			<a href='javascript:void(0);' 
			   onclick='document.getElementById("priority").selectedIndex=<%= objParPriority %>;document.getElementById("objectForm").submit();'>
				<img 	src= '<%=urlBuilder.getResourceLink(request, "/img/Forward.gif") %>'
					title='<spagobi:message key = "SBIDev.docConf.docDetParam.reducePriority" />' 
					alt='<spagobi:message key = "SBIDev.docConf.docDetParam.reducePriority" />'
				/>
			</a>
		</td>
		<% } %>
		<% 
			if(biObjParams != null && biObjParams.size() > 1) { 
				if(ChannelUtilities.isPortletRunning()) {
		%>
			<td class='header-button-column-portlet-section'>
				<a href='javascript:saveBIParameterConfirm("<spagobi:message key="SBIDev.docConf.docDetParam.saveBIParameterConfirm"/>")'>
					<img 	src= '<%=urlBuilder.getResourceLink(request, "/img/Class.gif") %>'
						title='<spagobi:message key = "SBIDev.docConf.docDetParam.parametersCorrelationManagement" />'
						alt='<spagobi:message key = "SBIDev.docConf.docDetParam.parametersCorrelationManagement" />'
					/>
				</a>
			</td>
		<%
				} 
			}
		%>
		<td class='header-button-column-portlet-section'>
			<a href='javascript:deleteBIParameterConfirm("<spagobi:message key="SBIDev.docConf.docDetParam.deleteBIParameterConfirm"/>")'>
				<img 	src= '<%=urlBuilder.getResourceLink(request, "/img/erase.gif") %>'
					title='<spagobi:message key = "SBIDev.docConf.docDetParam.eraseButt" />' alt='<spagobi:message key = "SBIDev.docConf.docDetParam.eraseButt" />'
				/>
			</a>
		</td>
		<% } else { %>
			<td class='header-sub-title-column-portlet-section-no-buttons'>
				<spagobi:message key = "SBIDev.docConf.docDetParam.title" />
			</td>
		<% } %>
	</tr>
</table>



<input type='hidden' name='objParId' value='<%= objPar.getId() != null ? objPar.getId().toString() : "-1" %>' />
<input type='hidden' name='' value='' id='deleteBIObjectParameter' />
<input type='hidden' name='' value='' id='goToDependenciesPage' />
		



<div class="div_detail_area_sub_forms">
	<div class='div_detail_label'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBIDev.docConf.docDetParam.labelField" />
		</span>
	</div>
	<div class='div_detail_form'>
		<input class='portlet-form-input-field' type="text" name="objParLabel" 
			   id="objParLabel" size="42" value="<%=objPar.getLabel()%>" maxlength="20">
		&nbsp;*
	</div>
	<div class='div_detail_label'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBIDev.docConf.docDetParam.paramField" />
		</span>
	</div>
	<div class='div_detail_form'>
	<%
    	IParameterDAO param = DAOFactory.getParameterDAO();
		String objParId = (objPar.getParameter() != null ? objPar.getParameter().getId().toString() : null);
      	List list = param.loadAllParameters();
		Parameter parameter = null;
      	for (int i = 0; i < list.size(); i++) {
			Parameter parameterTemp = (Parameter) list.get(i);
      		String curr_parId = parameterTemp.getId().toString();
      		if (curr_parId.equals(objParId)){
				parameter = parameterTemp;
      			break;
      		}
      	} 
	 %> 
    <input type='hidden' id='par_Id' 
			   value='<%= parameter != null ? parameter.getId().toString() : "" %>' name='par_Id' />	 
		<input class='portlet-form-input-field' type="text" id="parameterName" size="42" 
	    	   name="parameterName" value='<%= parameter != null ? parameter.getName() : "" %>' 
			   	maxlength="100" readonly>


  		&nbsp;*&nbsp;
		<a style="text-decoration:none;"  href="javascript:verifyDependencies();">
			<img src='<%=urlBuilder.getResourceLink(request, "/img/detail.gif")%>' 
			   title='<spagobi:message key = "SBIDev.docConf.docDetParam.parametersLookupList" />' 
			   alt='<spagobi:message key = "SBIDev.docConf.docDetParam.parametersLookupList" />' />
		</a>
			   
		<input type='hidden' name='' value='' id='loadParametersLookup' />
	</div>
	<div class='div_detail_label'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBIDev.docConf.docDetParam.parurl_nmField" />
		</span>
	</div>
	<div class='div_detail_form'>
	<% 
    	String urlName = objPar.getParameterUrlName();
    	if (urlName==null) {
    		urlName = "";
    	}
    %>
		<input class='portlet-form-input-field' type="text" size="42" 
			   name="parurl_nm" id="parurl_nm" value="<%=urlName%>" maxlength="20" />&nbsp;&nbsp;*
	</div>
	
	<div class='div_detail_label'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBIDev.docConf.docDetParam.priorityField" />
		</span>
	</div>
	<div class='div_detail_form'>
		<select class='portlet-form-input-field' name="priority" id="priority">
		<%
			int objParsnumber = biObjParams.size();
			for (int i = 0; i < objParsnumber; i++) {
				%>
				<option value="<%=i + 1%>" <% if (objPar.getPriority() != null && objPar.getPriority().intValue() == i + 1) out.print(" selected='selected' ");  %>><%=i+1%></option>
				<%
			}
			if (obj_par_id < 0) {
				%>
				<option value="<%=objParsnumber+1%>" selected="selected"><%=objParsnumber+1%></option>
				<%
			} else 
		%>
		</select>
	</div>
	
	
	<div class='div_detail_label' style='display:none;'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBIDev.docConf.docDetParam.view_flField" />
		</span>
	</div>
	<div class='div_detail_form' style='display:none;'>
	<% 
    	isVisible = false;
    	visible = objPar.getVisible().intValue();
    	if(visible > 0) { isVisible = true; }
    %> 
		<input type="radio" name="view_fl" value="1" <% if(isVisible) { out.println(" checked='checked' "); } %> >True</input>
    	<input type="radio" name="view_fl" value="0" <% if(!isVisible) { out.println(" checked='checked' "); } %> >False</input>
	</div>
	<div class='div_detail_label' style='display:none;'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBIDev.docConf.docDetParam.mod_flField" />
		</span>
	</div>
	<div class='div_detail_form' style='display:none;'>
	<% 
		boolean isModifiable = false;
    	int modifiable = objPar.getModifiable().intValue();
    	if(modifiable > 0) { isModifiable = true; }
    %> 
    	<input type="radio" name="mod_fl" value="1" <% if(isModifiable) { out.println(" checked='checked' "); } %> >True</input>
    	<input type="radio" name="mod_fl" value="0" <% if(!isModifiable) { out.println(" checked='checked' "); } %> >False</input>
	</div>
	<div class='div_detail_label' style='display:none;'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBIDev.docConf.docDetParam.req_flField" />
		</span>
	</div>
	<div class='div_detail_form' style='display:none;'>
	<% 
    	boolean isRequired = false;
    	int required = objPar.getRequired().intValue();
    	if(required > 0) { isRequired = true; }
    %> 
    	<input type="radio" name="req_fl" value="1" <% if(isRequired) { out.println(" checked='checked' "); } %> >True</input>
    	<input type="radio" name="req_fl" value="0" <% if(!isRequired) { out.println(" checked='checked' "); } %> >False</input>
	</div>
	<div class='div_detail_label' style='display:none;'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBIDev.docConf.docDetParam.mult_flField" />
		</span>
	</div>
	<div class='div_detail_form' style='display:none;'>
	<% 
    	boolean isMultivalue = false;
    	int multivalue = objPar.getMultivalue().intValue();
    	if(multivalue > 0) { isMultivalue = true; }
    %> 
    	<input type="radio" name="mult_fl" value="1" <% if(isMultivalue) { out.println(" checked='checked' "); } %> >True</input>
    	<input type="radio" name="mult_fl" value="0" <% if(!isMultivalue) { out.println(" checked='checked' "); } %> >False</input>
	</div>
</div>


	
</form>

<% } %>


<div id = 'parameterCannotBeChanged' class='portlet-msg-error' style='display:none;'>
	<spagobi:message key = "SBIDev.docConf.docDetParam.cannotChangeParameter" />
</div>

</div> <!-- background -->

