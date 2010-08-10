<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="it.eng.spagobi.bo.BIObject,
				 it.eng.spagobi.bo.BIObjectParameter,
				 it.eng.spagobi.services.modules.DetailBIObjectModule,
				 it.eng.spagobi.services.modules.DetailBIObjectParameterModule,
				 it.eng.spagobi.bo.dao.IParameterDAO,
				 it.eng.spagobi.bo.dao.DAOFactory,
				 it.eng.spagobi.bo.Parameter,
				 java.util.List,
				 it.eng.spagobi.constants.ObjectsTreeConstants,
				 javax.portlet.PortletURL,
				 it.eng.spagobi.bo.Domain,
				 java.util.Iterator,
				 it.eng.spagobi.bo.Engine,
				 it.eng.spagobi.bo.TemplateVersion,
				 it.eng.spagobi.constants.SpagoBIConstants,
				 it.eng.spagobi.utilities.SpagoBITracer,
				 it.eng.spagobi.utilities.PortletUtilities,
				 it.eng.spago.navigation.LightNavigationManager" %>

<%@ taglib uri="/WEB-INF/tlds/spagobi.tld" prefix="spagobi" %>
<%@ taglib uri="/WEB-INF/tlds/portlet.tld" prefix="portlet" %>
<portlet:defineObjects/>

<style>
@IMPORT url("/portal/default-skin.css");
</style>
<style>
@IMPORT url("/wsrp/skin/portlet/wsrp-admin-portlet.css");
</style>
<style>
@IMPORT url("/spagobi/css/table.css");
</style>


<%
    SourceBean moduleResponse = (SourceBean) aServiceResponse.getAttribute("DetailBIObjectModule"); 
	BIObject obj = (BIObject) moduleResponse.getAttribute(DetailBIObjectModule.NAME_ATTR_OBJECT);
	List listEngines = (List) moduleResponse.getAttribute(DetailBIObjectModule.NAME_ATTR_LIST_ENGINES);
	List listTypes = (List) moduleResponse.getAttribute(DetailBIObjectModule.NAME_ATTR_LIST_OBJ_TYPES);
	List listStates = (List) moduleResponse.getAttribute(DetailBIObjectModule.NAME_ATTR_LIST_STATES);
	String modality = (String) moduleResponse.getAttribute(ObjectsTreeConstants.MODALITY);
	String actor = (String) moduleResponse.getAttribute(SpagoBIConstants.ACTOR);
	
	Engine firstEngine = null;
	String firstEngineId = "";
	if (listEngines.size() > 0) {
		firstEngine = (Engine) listEngines.get(0);
		firstEngineId = firstEngine.getId().toString();
	}

%>

<script>
function showEngField () {

	var selIndex = document.objectForm.type.selectedIndex;
	var selValue = document.objectForm.type[selIndex].value;
	var ind = selValue.indexOf(",");
	var type = selValue.substring(ind+1);

	if(type == 'REPORT'){
		engines = document.getElementById('engine');
		var i;
		for (i=0; i < engines.length ; i++) {
			en = engines[i];
			if (en.text == '') {
				engines.remove(i);
			}
		}
		document.getElementById('engine').disabled = false;
		document.objectForm.engine.selectedIndex=findIndexOf('Jasper Report Rel');
	}
	if(type == 'OLAP'){
		engines = document.getElementById('engine');
		var i;
		for (i=0; i < engines.length; i++) {
			en = engines[i];
			if (en.text == '') {
				engines.remove(i);
			}
		}
		document.getElementById('engine').disabled = false;
		document.objectForm.engine.selectedIndex=findIndexOf('Jpivot-Mondrian Rel');
	}
	if(type == 'DATAMART'){
		engines = document.objectForm.engine.options;
		document.getElementById('engine').disabled = true;
		engines[engines.length] = new Option('','<%=firstEngineId%>');
		document.objectForm.engine.selectedIndex=engines.length-1;
	}
	if(type == 'DASH'){
		engines = document.objectForm.engine.options;
		document.getElementById('engine').disabled = true;
		engines[engines.length] = new Option('','<%=firstEngineId%>');
		document.objectForm.engine.selectedIndex=engines.length-1;
	}
}

function findIndexOf (labelToFind) {
engines = document.objectForm.engine.options;
var c;
for (c = 0; c<engines.length;c++) {
   en = engines[c];
   if (en.text.match(labelToFind)) {
     return c;
   }
}
return 0;
}

</script>

<% 
   PortletURL formUrl = renderResponse.createActionURL();
   formUrl.setParameter("PAGE", "detailBIObjectPage");
   if (modality != null){
   	formUrl.setParameter("MESSAGEDET", modality);
   }
   formUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   
   
   PortletURL backUrl = renderResponse.createActionURL();
   backUrl.setParameter("PAGE", "detailBIObjectPage");
   backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   backUrl.setParameter("MESSAGEDET", "EXIT_FROM_DETAIL");
   backUrl.setParameter(SpagoBIConstants.ACTOR, actor);
%>
<%--      
   PortletURL paramsUrl = renderResponse.createActionURL();
   paramsUrl.setParameter("PAGE", "ListBIObjectParametersPage");
   if(obj.getId()!= null){
   	 paramsUrl.setParameter(ObjectsTreeConstants.OBJECT_ID, obj.getId().toString());
   } else{
	 SpagoBITracer.critical(ObjectsTreeConstants.NAME_MODULE,
			 				"DetailBIObject.jsp","","obj ID or path is NULL ");
   }  
    
   if(obj.getPath()!= null){
   	 paramsUrl.setParameter(ObjectsTreeConstants.PATH, obj.getPath());
   }
--%>

<form method='POST' action='<%= formUrl.toString() %>' id = 'objectForm' name='objectForm' enctype="multipart/form-data">

<%--table width='100%' cellspacing='0' border='0'>		
	<tr height='40'>
		<th align='center'><spagobi:message key = "SBIDev.docConf.docDet.title" /></th>
	</tr>
</table--%>

<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section'>
			<spagobi:message key = "SBIDev.docConf.docDet.title" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<%--if(modality.equalsIgnoreCase(ObjectsTreeConstants.DETAIL_MOD) && actor.equalsIgnoreCase(SpagoBIConstants.DEV_ACTOR)  ) { %>   
			<td class='header-button-column-portlet-section'>
				<a href= '<%= paramsUrl.toString() %>'>
      				<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBIDev.docConf.docDet.paramButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/objectParameters.png")%>' alt='<spagobi:message key = "SBIDev.docConf.docDet.paramButt" />'/>
				</a> 
			</td>
		<% } --%>
		<td class='header-button-column-portlet-section'>
			<input type='image' class='header-button-image-portlet-section'
				src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/save.png") %>'
      				title='<spagobi:message key = "SBIDev.docConf.docDet.saveButt" />' alt='<spagobi:message key = "SBIDev.docConf.docDet.saveButt" />'
			/>
		</td>
		<td class='header-button-column-portlet-section'>
			<input type='image' name='saveAndGoBack' value='true' class='header-button-image-portlet-section'
				src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/saveAndGoBack.png") %>'
      				title='<spagobi:message key = "SBIDev.docConf.docDet.saveAndGoBackButt" />' alt='<spagobi:message key = "SBIDev.docConf.docDet.saveAndGoBackButt" />'
			/> 
		</td>
		<td class='header-button-column-portlet-section'>
			<% if(modality.equalsIgnoreCase(ObjectsTreeConstants.DETAIL_MOD)) {%>
				<a href='javascript:saveAndGoBackConfirm("<spagobi:message key = "SBIDev.docConf.docDet.saveAndGoBackConfirm" />","<%=backUrl.toString()%>")'> 
			<% } else { %>
				<a href='<%=backUrl.toString()%>'>
			<% } %>
      				<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBIDev.docConf.docDet.backButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' alt='<spagobi:message key = "SBIDev.docConf.docDet.backButt"/>' />
			</a>
		</td>
	</tr>
</table>

	<input type='hidden' value='<%= obj.getId() %>' name='id' />
	<input type='hidden' value='<%= modality %>' name='MESSAGEDET' />
	<input type='hidden' value='<%= actor %>' name='<%= SpagoBIConstants.ACTOR %>' />
	<input type='hidden' value='' name='' id='saveAndGoBack'>
	<input type='hidden' value='' name='' id='saveBIObjectParameter'>
	
	<% if(modality.equalsIgnoreCase(ObjectsTreeConstants.DETAIL_MOD)) { %>
	<input type='hidden' value='<%= obj.getPath() %>' name='<%= ObjectsTreeConstants.PATH %>' />
	<% } %>
	
    <!--
	<div style='width:49%;float:left;'>
	-->
	<table width="100%" cellspacing="0" border="0" id = "fieldsTable">
		<tr>
			<!--td width="40%"-->
			<td>
			
		<table class="object-details-table" >
		  	<!--tr height='1'>
		  		<td width="1px"><span>&nbsp;</span></td>
		  		<td width="75px"><span>&nbsp;</span></td>
		  		<td width="20px"><span>&nbsp;</span></td>
		  		<td><span>&nbsp;</span></td>
		  	</tr-->
		  	
		  	<tr height='25'>
		      	<!--td>&nbsp;</td-->
		      	<td align='right' class='portlet-form-field-label' ><spagobi:message key = "SBIDev.docConf.docDet.labelField" /></td>
		      	<td>&nbsp;</td>
		      	<td><input class='portlet-form-input-field' type="text" style='width:230px;' name="label" id="label" value="<%=obj.getLabel()%>" maxlength="20">&nbsp;*</td>
		    </tr>
		    <tr height='25'>
		      	<!--td>&nbsp;</td-->
		      	<td align='right' class='portlet-form-field-label' ><spagobi:message key = "SBIDev.docConf.docDet.nameField" /></td>
		      	<td>&nbsp;</td>
		      	<td><input class='portlet-form-input-field' type="text" style='width:230px;' name="name" id="name" value="<%=obj.getName()%>" maxlength="40">&nbsp;*</td>
		    </tr>
		    <tr height='25'>
		      	<!--td>&nbsp;</td-->
		      	<td align='right' class='portlet-form-field-label'><spagobi:message key = "SBIDev.docConf.docDet.descriptionField" /></td>
		      	<td>&nbsp;</td>
		      	<% 
		      		String desc = obj.getDescription();
		      		if(desc==null) {
		      			desc = "";
		      		}
		      	%>
		      	<td ><input class='portlet-form-input-field' style='width:230px;' type="text" name="description" id="description" value="<%=desc%>" maxlength="160"></td>
		    </tr>
		    <tr height='25' style='display:none;'>
		      	<!--td>&nbsp;</td-->
		      	<td align='right' class='portlet-form-field-label'><spagobi:message key = "SBIDev.docConf.docDet.relNameField" /></td>
		      	<td>&nbsp;</td>
		      	<% 
		      		String relName = obj.getRelName();
		      		if(relName==null) {
		      			relName = "";
		      		}
		      	%>
		      	<td><input class='portlet-form-input-field' style='width:230px;' type="text" name="relname" id="relname" value="<%=relName%>" maxlength="400"></td>
		    </tr>
		    
		  
		    <tr height='25'>
		      	<!--td>&nbsp;</td-->
		      	<td align='right' class='portlet-form-field-label'><spagobi:message key = "SBIDev.docConf.docDet.typeField" /></td>
		      	<td>&nbsp;</td>
		      	<td>
		      		<select class='portlet-form-input-field' style='width:230px;' name="type" id="type" onchange = 'showEngField()'>
		      		<% 
		      		    Iterator iterdom = listTypes.iterator();
		      		    while(iterdom.hasNext()) {
		      		    	Domain type = (Domain)iterdom.next();
		      		    	String BIobjTypecode = obj.getBiObjectTypeCode();
		      		    	String currTypecode = type.getValueCd();
		      		    	boolean isType = false;
		      		    	if(BIobjTypecode.equals(currTypecode)){
		      		    	isType = true;   }
		      		    	boolean passType = false;
		      		    	if(type.getValueName().toString().equals("Data mining model")){
		      		    	passType = true;
		      		    	}
		      		    	if(!passType){
		      		%>
		      			<option value="<%=type.getValueId() + "," + type.getValueCd()  %>"<%if(isType) out.print(" selected='selected' ");  %>><%=type.getValueName()%></option>
		      		<% 
		      		    }
		      		    }
		      		%>
		      		</select>
		      	</td>
		    </tr>
		    
		    <tr height='25' >
		     
		      	<!--td>&nbsp;</td-->
		      	<td align='right' class='portlet-form-field-label'><spagobi:message key = "SBIDev.docConf.docDet.engineField" /></td>
		      	<td>&nbsp;</td>
		      	<td>
			
				<%
				String selectDisabled = "";
				if (obj.getBiObjectTypeCode().equals("DATAMART") || obj.getBiObjectTypeCode().equals("DASH")) {
					 selectDisabled = "disabled";
				}
				%>
		      		<select class='portlet-form-input-field' style='width:230px;' name="engine" id="engine" <%=selectDisabled%>>
				<%
				
				if (obj.getBiObjectTypeCode().equals("DATAMART") || obj.getBiObjectTypeCode().equals("DASH")) {
					 %>
					 <option value='<%=firstEngineId%>' selected='selected'></option>
					 <%
				}
				
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
		      			<option value="<%=engine.getId().toString() %>"<%if(isEngine) out.print(" selected='selected' ");  %>><%=engine.getName()%></option>
		      		<% 	
		      		}
		      		%>
		      		</select>
		      	</td>
		    </tr>
		    
			<% if(modality.equalsIgnoreCase(ObjectsTreeConstants.DETAIL_INS)) { %>
				<script>showEngField()</script>
			<% } %>









<!-- DISPLAY COMBO FOR STATE SELECTION -->
<!-- IF THE USER IS A DEV ACTOR THE COMBO FOR THE STATE SELECTION CONTAINS ONLY A VALUE
     (development) SO IS NOT USEFUL TO SHOW IT	 -->
            <% if(actor.equalsIgnoreCase(SpagoBIConstants.DEV_ACTOR)){%>
               	<tr height='25' style='display:none;'>
		      	   	<td align='right' class='portlet-form-field-label'>
		      	   		<spagobi:message key = "SBIDev.docConf.docDet.stateField" />
		      	   	</td>
		      		<td>&nbsp;</td>
		      		<td>
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
		      		</td>
		    	</tr>
            <% }else{ %>
            	<tr height='25'>
		      		<td align='right' class='portlet-form-field-label'>
		      			<spagobi:message key = "SBIDev.docConf.docDet.stateField" />
		      		</td>
		      		<td>&nbsp;</td>
		      		<td>
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
		      		</td>
		    	</tr>
            <% } %>
            
            
            
            
            
            
            
            
            
<!-- DISPLAY RADIO BUTTON FOR CRYPT SELECTION -->
<!-- FOR THE CURRENT RELEASE THIS RADIO IS HIDE -->
		    <tr height='25' style='display:none;'>
		      	<td align='right' class='portlet-form-field-label'>
		      		<spagobi:message key = "SBIDev.docConf.docDet.criptableField" />
		      	</td>
		      	<td>&nbsp;</td>
		      	<td>
		      	   <% 
		      	      boolean isCrypt = false;
		      	      int cript = obj.getEncrypt().intValue();
		      	      if(cript > 0) { isCrypt = true; }
		      	     %> 
		      	   <input type="radio" name="criptable" value="1" <% if(isCrypt) { out.println(" checked='checked' "); } %> >True</input>
		      	   <input type="radio" name="criptable" value="0" <% if(!isCrypt) { out.println(" checked='checked' "); } %> >False</input>
		      	 </td>
		    </tr>







<!-- DISPLAY FORM FOR TEMPLATE  UPLOAD -->
		    <tr height='25'>
		      	<td align='right' class='portlet-form-field-label'>
		      		<spagobi:message key = "SBIDev.docConf.docDet.templateField" />
		      	</td>
		      	<td>&nbsp;</td>
		      	<td>
		      		<input class='portlet-form-input-field' type="file" 
		      		       name="uploadFile" id="uploadFile" onchange='fileToUploadInserted()'/>
		        </td>
		    </tr>
		</table>
		


	<!-- CLOSE COLUMN WITH DATA FORM  -->
	</td>
	<!-- OPEN COLUMN WITH TREE FUNCTIONALITIES (INSERT MODE) OR TEMPLATE VERSION (MODIFY MODE)  -->	     
	<td width="60%">
    	<% if(modality.equalsIgnoreCase(ObjectsTreeConstants.DETAIL_INS)) { %>
    		<div style='padding-left:10px;'>
    			<spagobi:treeObjects moduleName="DetailBIObjectModule"  
    								 htmlGeneratorClass="it.eng.spagobi.presentation.treehtmlgenerators.FunctionalitiesTreeInsertObjectHtmlGenerator" />    	
    		</div>
		<% } %>
     	<% if(modality.equalsIgnoreCase(ObjectsTreeConstants.DETAIL_MOD)) { %>
    		<div width="100%" margin="5px" >
				<span class='portlet-form-field-label'>
					<spagobi:message key = "SBIDev.docConf.docDet.templateVersionField" />
				</span>
				<div style='border: 1px solid black;width:100%;max-height:160px;overflow:auto;'>
					<table width="100%">
		 			<% 
				List templates = obj.getTemplateVersions();
				String curVer = obj.getCurrentTemplateVersion().getVersionName();
				int numTemp = templates.size();  
				if(numTemp == 0) {
					out.print("<tr class='portlet-section-body'>");
		      			out.print("<td class='portlet-font'>No Version Found</td></tr>");
		      		}
		      		Iterator iterTemp = templates.iterator();
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
		      		        out.print("<td class='portlet-font' width='220px'>"+tempVer.getDataLoad()+"</td>");
		      		        out.print("<td class='portlet-font' width='150px'>"+tempVer.getNameFileTemplate()+"</td>");
		      		        
		      		        PortletURL eraseVerUrl = renderResponse.createActionURL();
   					eraseVerUrl.setParameter("PAGE", "detailBIObjectPage");
   					eraseVerUrl.setParameter("MESSAGEDET", SpagoBIConstants.ERASE_VERSION);
   					eraseVerUrl.setParameter(SpagoBIConstants.VERSION, tempVer.getVersionName());
   					eraseVerUrl.setParameter(SpagoBIConstants.PATH, obj.getPath());
   					eraseVerUrl.setParameter(SpagoBIConstants.ACTOR, actor);
					eraseVerUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");

							
   					String pathObj = obj.getPath();
   					String pathTemp = pathObj + "/template";
   					String downl = "/spagobi/ContentRepositoryServlet?jcrPath="+pathTemp+"&version="+tempVer.getVersionName()+"&fileName="+tempVer.getNameFileTemplate();
		      		       
		      		        if(isCurrentVer) {
		      		        	out.print("<td class='portlet-font' >&nbsp;</a></td>");
		      		        } else {
		      		        	out.print("<td class='portlet-font' ><a href='javascript:deleteVersionConfirm(\""+PortletUtilities.getMessage("SBIDev.docConf.docDet.deleteVersionConfirm", "messages")+"\", \""+eraseVerUrl.toString()+"\")' style='font-size:9px;' >" + PortletUtilities.getMessage("SBIDev.docConf.execBIObject.eraseLink", "messages") + "</a></td>");
		      		        }
		      		        out.print("<td class='portlet-font' ><a href='"+downl+"' style='font-size:9px;' >" + PortletUtilities.getMessage("SBIDev.docConf.execBIObject.downloadLink", "messages") + "</a></td>");
		      		        
		      		        if(numTemp > 1) {
		      		        	out.print("<td class='portlet-font'><input type='radio' value='"+tempVer.getVersionName()+" 'name='versionTemplate' onchange='versionTemplateSelected()' "+checkStr+" /></td></tr>");
		      		        } else {
		      		        	out.print("<td class='portlet-font'>&nbsp;</td></tr>");
		      		        }
		      		        
		      		}
		      		%>    
		      	</table>
		</div>
	<%-- LF END --%>
    	<!--
    	</div>
    	-->

    <% } %>
     	</td>
      </tr>
   </table>   

	
	<div class='errors-object-details-div'>
		<spagobi:error/>
	</div>

<% if(modality.equalsIgnoreCase(ObjectsTreeConstants.DETAIL_INS)) { %>
</form>
<% } else if(modality.equalsIgnoreCase(ObjectsTreeConstants.DETAIL_MOD)) {

   BIObjectParameter objPar = (BIObjectParameter) moduleResponse.getAttribute(DetailBIObjectModule.NAME_ATTR_OBJECT_PAR);
	
%>

	<table style='width:100%;visibility:visible;' class='UIProducerNode' id='tabPanelWithJavascript' name='tabPanelWithJavascript'>
		<tr>
			<td colspan='2' class='wsrp-admin-portlet'>
				<div class='UIWSRPAdminPortlet'>
					<ul class='UISimpleTabs'>
						<input type='hidden' id='selected_par_id' name='' value=''/>

<%
List biObjParams = obj.getBiObjectParameters();
String par_idStr = (String) moduleResponse.getAttribute("selected_par_id");
Integer par_idInt = new Integer(par_idStr);
int par_id = Integer.parseInt(par_idStr);
String linkClass = "link";
boolean foundSelectedParId = false;
for (int i = 0; i < biObjParams.size(); i++){
	BIObjectParameter biObjPar = (BIObjectParameter) biObjParams.get(i);
	if (biObjPar.getParID().equals(par_idInt)) {
		linkClass = "select-link";
		foundSelectedParId = true;
	}
	else linkClass = "link";
	%>
					<li><a class='<%= linkClass%>' 
						href='javascript:changeBIParameter("<%= biObjPar.getParID().toString() %>", "<spagobi:message key = "SBIDev.docConf.docDetParam.saveAndChangeBIParameterConfirm" />")'> 
						<%= biObjPar.getLabel()%>
					    </a>
<%}
if (par_id < 0 || !foundSelectedParId) linkClass = "select-link";
else linkClass = "link";
%>
					<li><a class='<%= linkClass%>' href='javascript:changeBIParameter("-1", "<spagobi:message key = "SBIDev.docConf.docDetParam.saveAndChangeBIParameterConfirm" />")'> 
						<spagobi:message key = "SBIDev.docConf.docDet.newBIParameter" />
					    </a>
					</ul>
				</div>
			</td>
		</tr>
	</table>
	
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

function changeBIParameter (parId, message) {

	var biobjParFormModified = 'false';
	
	document.getElementById('selected_par_id').name = 'selected_par_id';
	document.getElementById('selected_par_id').value = parId;
	
	var objParLabel = document.getElementById('objParLabel').value;
	var par_Id = document.getElementById('par_Id').value;
	var parurl_nm = document.getElementById('parurl_nm').value;
	
	if ((objParLabel != '<%=initialBIObjectParameter.getLabel()%>')
		|| (par_Id != '<%=initialBIObjectParameter.getParID() == null ? "" : initialBIObjectParameter.getParID().toString()%>')
		|| (parurl_nm != '<%=initialBIObjectParameter.getParameterUrlName()%>') )
	{
		biobjParFormModified = 'true';
	}
	
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

		var biobjFormModified = 'false';
		var biobjParFormModified = 'false';
		
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
	
		var objParLabel = document.getElementById('objParLabel').value;
		var par_Id = document.getElementById('par_Id').value;
		var parurl_nm = document.getElementById('parurl_nm').value;
		
		if ((objParLabel != '<%=initialBIObjectParameter.getLabel()%>')
			|| (par_Id != '<%=initialBIObjectParameter.getParID() == null ? "" : initialBIObjectParameter.getParID().toString()%>')
			|| (parurl_nm != '<%=initialBIObjectParameter.getParameterUrlName()%>') ) {
			
			biobjParFormModified = 'true';
		}
		
		if (biobjFormModified == 'true' || biobjParFormModified == 'true') {
			if (confirm(message)) {
				document.getElementById('saveAndGoBack').name = 'saveAndGoBack';
				document.getElementById('saveAndGoBack').value= 'true';
				document.getElementById('objectForm').submit();
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
		document.getElementById('deleteBIObjectParameter').value = '<%= objPar.getParIdOld() %>';
        	document.getElementById('objectForm').submit();
        }
}

</script>

	<%--table style='width:100%;visibility:visible;' class='UIProducerNode' id='tabPanelWithoutJavascript' name='tabPanelWithoutJavascript'>
		<tr>
			<td colspan='2' class='wsrp-admin-portlet'>
				<div class='UIWSRPAdminPortlet'>
					<ul class='UISimpleTabs'>
						<input type='hidden' id='selected_par_id' name='' value=''/>

<% 
//linkClass = "link";
//foundSelectedParId = false;
for (int i = 0; i < biObjParams.size(); i++){
	BIObjectParameter biObjPar = (BIObjectParameter) biObjParams.get(i);
	//if (biObjPar.getParID().equals(par_idInt)) {
	//	linkClass = "select-link";
	//	foundSelectedParId = true;
	//}
	//else linkClass = "link";
	%>
					<li><button name='selected_par_id' value='<%= biObjPar.getParID()%>'> 
						<%= biObjPar.getLabel()%>
					    </button>
<%}
//if (par_id < 0 || !foundSelectedParId) linkClass = "select-link";
//else linkClass = "link";
%>
					<li><button name='selected' value='-1'> 
						<spagobi:message key = "SBIDev.docConf.docDet.newBIParameter" />
					    </button>
					</ul>
				</div>
			</td>
		</tr>
	</table--%>


<%--script>
document.getElementById('tabPanelWithJavascript').style.visibility='visible';
document.getElementById('tabPanelWithoutJavascript').style.visibility='hidden';
</script--%>

<table class='header-sub-table-portlet-section' style='margin-top:5px;'>		
	<tr class='header-sub-row-portlet-section'>
		<% if (par_id != -1) { %>
		<td class='header-sub-title-column-portlet-section'>
			<spagobi:message key = "SBIDev.docConf.docDetParam.title" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='javascript:deleteBIParameterConfirm("<spagobi:message key="SBIDev.docConf.docDetParam.deleteBIParameterConfirm"/>")'>
				<img 	src= '<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/erase.gif") %>'
					title='<spagobi:message key = "SBIDev.docConf.docDetParam.eraseButt" />' alt='<spagobi:message key = "SBIDev.docConf.docDetParam.eraseButt" />'
				/>
			</a>
			<%--input type='image' name='deleteBIObjectParameter' value='<%= objPar.getParIdOld() %>'
				class='header-button-image-portlet-section'
				src= '<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/erase32.png") %>'
				title='<spagobi:message key = "SBIDev.docConf.docDetParam.eraseButt" />' alt='<spagobi:message key = "SBIDev.docConf.docDetParam.eraseButt" />'
			/--%>
		</td>
		<% } else { %>
			<td class='header-sub-title-column-portlet-section-no-buttons'>
				<spagobi:message key = "SBIDev.docConf.docDetParam.title" />
			</td>
		<% } %>
	</tr>
</table>

<input type='hidden' name='objParIdOld' value='<%= objPar.getParIdOld() != null ? objPar.getParIdOld().toString() : "-1" %>' />
<input type='hidden' name='' value='' id='deleteBIObjectParameter' />

	<div class="object-details-div" >
		<table class="object-details-table" >

			<tr height='25'>
			      	<td align='right' class='portlet-form-field-label' ><spagobi:message key = "SBIDev.docConf.docDetParam.labelField" /></td>
      				<td>&nbsp;</td>
      				<td><input class='portlet-form-input-field' type="text" name="objParLabel" id="objParLabel" size="42" value="<%=objPar.getLabel()%>" maxlength="20">&nbsp;*</td>
    			</tr>

			<tr height='25'>
      				<td align='right' class='portlet-form-field-label'><spagobi:message key = "SBIDev.docConf.docDetParam.paramField" /></td>
      				<td>&nbsp;</td>
      				<td>
				
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
      					} %>  
					
					<input class='portlet-form-input-field' type="text" id="parameterName" size="42" 
						name="parameterName" value='<%= parameter != null ? parameter.getName() : "" %>' 
						maxlength="100" readonly>
      					<input type='hidden' id='par_Id' value='<%= parameter != null ? parameter.getId().toString() : "" %>' name='par_Id' />
      					
					<%
					PortletURL parametersLookupURL = renderResponse.createActionURL();
  					parametersLookupURL.setParameter("PAGE", "parametersLookupPage"); 
					%>
  					&nbsp;*&nbsp;
					<input type='image' name="loadParametersLookup" value="loadParametersLookup" 
					src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/detail.gif")%>' 
					title='<spagobi:message key = "SBIDev.docConf.docDetParam.parametersLookupList" />' 
					alt='<spagobi:message key = "SBIDev.docConf.docDetParam.parametersLookupList" />' 
		    			/>
        								
        			</td>
    			</tr>
			
    			<tr height='25'>
      				<td align='right' class='portlet-form-field-label'><spagobi:message key = "SBIDev.docConf.docDetParam.parurl_nmField" /></td>
      				<td>&nbsp;</td>
      				<% 
      	  			String urlName = objPar.getParameterUrlName();
      	  			if(urlName==null) {
      	  				urlName = "";
      	  			}
      				%>
      				<td ><input class='portlet-form-input-field' type="text" size="42" name="parurl_nm" id="parurl_nm" value="<%=urlName%>" maxlength="20"></td>
			</tr>

   			<tr height='25' style='display:none;'>
      				<td align='right' class='portlet-form-field-label'><spagobi:message key = "SBIDev.docConf.docDetParam.view_flField" /></td>
      				<td>&nbsp;</td>
      				<td>
      	 				<% 
      	     				boolean isVisible = false;
      	      				int visible = objPar.getVisible().intValue();
      	      				if(visible > 0) { isVisible = true; }
      	     				%> 
					<input type="radio" name="view_fl" value="1" <% if(isVisible) { out.println(" checked='checked' "); } %> >True</input>
      	   				<input type="radio" name="view_fl" value="0" <% if(!isVisible) { out.println(" checked='checked' "); } %> >False</input>
      	 			</td>
    			</tr>

    			<tr height='25' style='display:none;'>
      				<td align='right' class='portlet-form-field-label'><spagobi:message key = "SBIDev.docConf.docDetParam.mod_flField" /></td>
      				<td>&nbsp;</td>
      				<td>
      	  				<% 
					boolean isModifiable = false;
      	      				int modifiable = objPar.getModifiable().intValue();
      	      				if(modifiable > 0) { isModifiable = true; }
      	     				%> 
      	   				<input type="radio" name="mod_fl" value="1" <% if(isModifiable) { out.println(" checked='checked' "); } %> >True</input>
      	   				<input type="radio" name="mod_fl" value="0" <% if(!isModifiable) { out.println(" checked='checked' "); } %> >False</input>
      	 			</td>
    			</tr>

			<tr height='25' style='display:none;'>
      				<td align='right' class='portlet-form-field-label'><spagobi:message key = "SBIDev.docConf.docDetParam.req_flField" /></td>
      				<td>&nbsp;</td>
      				<td>
      	   				<% 
      	     				boolean isRequired = false;
      	      				int required = objPar.getRequired().intValue();
      	      				if(required > 0) { isRequired = true; }
      	     				%> 
      	   				<input type="radio" name="req_fl" value="1" <% if(isRequired) { out.println(" checked='checked' "); } %> >True</input>
      	   				<input type="radio" name="req_fl" value="0" <% if(!isRequired) { out.println(" checked='checked' "); } %> >False</input>
				</td>
    			</tr>
	
    			<tr height='25' style='display:none;'>
      				<td align='right' class='portlet-form-field-label'><spagobi:message key = "SBIDev.docConf.docDetParam.mult_flField" /></td>
      				<td>&nbsp;</td>
      				<td>
      	   				<% 
      	      				boolean isMultivalue = false;
      	      				int multivalue = objPar.getMultivalue().intValue();
      	      				if(multivalue > 0) { isMultivalue = true; }
      	     				%> 
      	   				<input type="radio" name="mult_fl" value="1" <% if(isMultivalue) { out.println(" checked='checked' "); } %> >True</input>
      	   				<input type="radio" name="mult_fl" value="0" <% if(!isMultivalue) { out.println(" checked='checked' "); } %> >False</input>
      	 			</td>
    			</tr>

		</table>
	</div>
</form>

<% } %>
