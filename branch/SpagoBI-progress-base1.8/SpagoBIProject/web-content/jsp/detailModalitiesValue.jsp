<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="it.eng.spagobi.bo.ModalitiesValue,
                 it.eng.spagobi.bo.Domain,
                 it.eng.spagobi.bo.dao.jdbc.DomainDAOImpl,
                 it.eng.spagobi.bo.QueryDetail,
                 it.eng.spagobi.bo.ScriptDetail,
                 it.eng.spagobi.bo.LovDetailList,
                 it.eng.spagobi.bo.LovDetail,
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
	HashMap profileattrs = (HashMap)moduleResponse.getAttribute(SpagoBIConstants.PROFILE_ATTRS);
	ModalitiesValue modVal = (ModalitiesValue)moduleResponse.getAttribute(SpagoBIConstants.MODALITY_VALUE_OBJECT);
	String modality = (String)moduleResponse.getAttribute(SpagoBIConstants.MODALITY);
	ArrayList list = (ArrayList)moduleResponse.getAttribute(SpagoBIConstants.LIST_INPUT_TYPE);

	PortletURL formUrl = renderResponse.createActionURL();
	formUrl.setParameter("PAGE", "detailModalitiesValuePage");
	formUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
	  
	PortletURL backUrl = renderResponse.createActionURL();
	backUrl.setParameter("PAGE", "ListLovsPage");
	backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
	
	String queryDisplay = "none";
	String scriptDisplay = "none";
	String lovDisplay = "none";
	
	QueryDetail query = new QueryDetail();
	if (modVal != null && modVal.getITypeCd().equalsIgnoreCase("QUERY") ) {
		String lovProvider = modVal.getLovProvider();
		if (lovProvider != null  &&  !lovProvider.equals("")){
		  	query = QueryDetail.fromXML(lovProvider);
		  	queryDisplay = "inline";
		}
	}
	
	ScriptDetail scriptDet = new ScriptDetail();
	if (modVal != null && modVal.getITypeCd().equalsIgnoreCase("SCRIPT") ) {
		String lovProvider = modVal.getLovProvider();
	  	if (lovProvider != null  &&  !lovProvider.equals("")){
	  		scriptDet = ScriptDetail.fromXML(lovProvider);
	  		scriptDisplay = "inline";
	  	}
	}
	
	String lovProvider = "";
	if (modVal != null && modVal.getITypeCd().equalsIgnoreCase("FIX_LOV") ) {
		lovProvider = modVal.getLovProvider();
	  	if (lovProvider != null  &&  !lovProvider.equals("")){
	  		lovDisplay = "inline";
	  	}
	}
	
	
	String testButtonVisibility = null;
	if (modVal != null && (modVal.getITypeCd().equalsIgnoreCase("QUERY")
		|| modVal.getITypeCd().equalsIgnoreCase("SCRIPT")) )  {
			testButtonVisibility = "visible";
		}	
	else {
			testButtonVisibility = "hidden";
	}
	
%>


<form method='POST' action='<%= formUrl.toString() %>' id ='modalitiesValueForm' name='modalitiesValueForm'>
	<input type='hidden' value='<%= modVal.getId() %>' name='id' />
	<input type='hidden' value='<%= modality %>' name='<%= SpagoBIConstants.MESSAGEDET  %>' />



<script type="text/javascript">
  function showWizard(){
	var wizard = document.getElementById("input_type").value
	if (wizard.match("QUERY") != null) {
		document.getElementById("queryWizard").style.display = "inline"
		document.getElementById("scriptWizard").style.display = "none"
		document.getElementById("lovWizard").style.display = "none"
		document.getElementById("testButton").style.visibility = "visible"
	}
	if (wizard.match("SCRIPT") != null) {
		document.getElementById("queryWizard").style.display = "none"
		document.getElementById("scriptWizard").style.display = "inline"
		document.getElementById("lovWizard").style.display = "none"
		document.getElementById("testButton").style.visibility = "visible"
	}
	if (wizard.match("FIX_LOV") != null) {
		document.getElementById("queryWizard").style.display = "none"
		document.getElementById("scriptWizard").style.display = "none"
		document.getElementById("lovWizard").style.display = "inline"
		document.getElementById("testButton").style.visibility = "hidden"
	}
	if (wizard.match("MAN_IN") != null) {
		document.getElementById("queryWizard").style.display = "none"
		document.getElementById("scriptWizard").style.display = "none"
		document.getElementById("lovWizard").style.display = "none"
		document.getElementById("testButton").style.visibility = "hidden"
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
		<input type='image' class='header-button-image-portlet-section' name="testLovBeforeSave" value="testLovBeforeSave" 
				src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/test.png")%>' 
				title='<spagobi:message key = "SBIDev.predLov.TestBeforeSaveLbl" />'  
				alt='<spagobi:message key = "SBIDev.predLov.TestBeforeSaveLbl" />' 
		/>
		</td>
		<td class='header-button-column-portlet-section'>
		<input type='image' class='header-button-image-portlet-section' name="saveLov" value="saveLov" 
				src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/save.png")%>' 
				title='<spagobi:message key = "SBIDev.predLov.saveButt" />'  
				alt='<spagobi:message key = "SBIDev.predLov.saveButt" />' 
		/>
		</td>
		<td class='header-button-column-portlet-section'>
			<a href= '<%= backUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBIDev.predLov.backButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' alt='<spagobi:message key = "SBIDev.predLov.backButt" />' />
			</a>
		</td>
	</tr>
</table>



<div class='div_background_no_img' >




<div class="div_detail_area_forms_lov">
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
   		<select style='width:180px;' name="input_type" id="input_type" class='portlet-form-input-field' onChange="showWizard()">
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





<div id="queryWizard" style='width:100%;display:<%=queryDisplay%>'>
	<div style='margin:5px;padding-top:5px;padding-left:5px;' class='portlet-section-header'>
		<spagobi:message key = "SBIDev.queryWiz.wizardTitle" />
	</div> 
	<spagobi:queryWizard 
		connectionName='<%= query.getConnectionName()!= null ? query.getConnectionName().toString() : "" %>' 
		visibleColumns='<%= query.getVisibleColumns()!= null ? query.getVisibleColumns().toString() : "" %>' 
		valueColumns='<%= query.getValueColumns()!= null ? query.getValueColumns().toString() : "" %>' 
		queryDef='<%= query.getQueryDefinition()!= null ? query.getQueryDefinition().toString() : "" %>' 
		/> 
</div>





<div id="lovWizard" style='width:100%;display:<%=lovDisplay%>'>
	<div style='margin:5px;padding-top:5px;padding-left:5px;' class='portlet-section-header'>
		<spagobi:message key = "SBIDev.lovWiz.wizardTitle" />
	</div> 
	<spagobi:lovWizard lovProvider='<%= lovProvider!= null ? lovProvider : "" %>' />
</div>





<div id="scriptWizard" style='width:100%;display:<%=scriptDisplay%>'>
	<div style='margin:5px;padding-top:5px;padding-left:5px;' class='portlet-section-header'>
		<spagobi:message key = "SBIDev.scriptWiz.wizardTitle" />
	</div> 
	<spagobi:scriptWizard 
		script='<%= scriptDet.getScript()!= null ? scriptDet.getScript() : "" %>' 
		isListOfValues='<%= scriptDet.isListOfValues() ? "true" : "false" %>' 
	/>
	
</form>
	
	<br/>
	<span class='portlet-form-field-label'>
		<spagobi:message key = "SBIDev.scriptWiz.ListValExplanation" />
	</span>
	<br/>
	<script type="text/javascript">
		function showSintax(){
		var display = document.getElementById("sintax").style.display;
		if (display == "none") {
			document.getElementById("sintax").style.display = "inline";
			document.getElementById("showSintax").innerHTML = "<spagobi:message key = 'SBIDev.scriptWiz.hideSintax'/>";
		}
		else {
			document.getElementById("sintax").style.display = "none";
			document.getElementById("showSintax").innerHTML = "<spagobi:message key = 'SBIDev.scriptWiz.showSintax'/>";
		}
	}
	</script>

	<a id="showSintax" href="javascript:void(0)" onclick="showSintax()" class='portlet-form-field-label'>
		<spagobi:message key = "SBIDev.scriptWiz.showSintax"/>
	</a>
	

	<br/>
	
	<div id="sintax" style="display:none;" >
		<br/>
		<table width="50%">
		    <tr height='25'>
		      	<td>
		      		<div class='portlet-section-subheader' style='text-align:center;vertical-align:bottom;' width="100%">
		      			<spagobi:message key = "SBIDev.scriptWiz.SintaxLbl" />
		      		</div>
		      		<div class='portlet-section-alternate' width="100%">
		      			<spagobi:message key = "SBIDev.scriptWiz.FixValLbl" />
		      			=
		      			<spagobi:message key = "SBIDev.scriptWiz.FixValExpr" />
		      			<br/>
		      			<spagobi:message key = "SBIDev.scriptWiz.ProfileValLbl" />
		      			=
		      			<spagobi:message key = "SBIDev.scriptWiz.ProfileValExpr" />
		      			<br/>
		      			<spagobi:message key = "SBIDev.scriptWiz.ScriptlLbl" />
		      			=
		      			<spagobi:message key = "SBIDev.scriptWiz.ScriptExpr" />
		      			<br/>
		      		</div>
		      	</td>
		    </tr>
		    <tr height='25'>
		      	<td>
		      		<div class='portlet-section-subheader' style='text-align:center;vertical-align:bottom;' width="100%">
		      			<spagobi:message key = "SBIDev.scriptWiz.ProfileAttrsLbl" />
		      		</div>
		      		<div class='portlet-section-alternate' width="100%">
		      			<%
		      			    Set nameAttrs = profileattrs.keySet();
		      			    Iterator iterAttrs = nameAttrs.iterator();
		      				String attribute = null;
		      				while(iterAttrs.hasNext()) {
		      					String attributename = (String)iterAttrs.next();
		      					out.write(attributename);
		      					out.write(" / ");
		      				}
		      			%>
		      		</div>
		      	</td>
		    </tr>
		    <tr height='25'>
		      	<td>
		      		<div class='portlet-section-subheader' style='text-align:center;vertical-align:bottom;' width="100%">
		      			<spagobi:message key = "SBIDev.scriptWiz.xmlstruct" />
		      		</div>
		      		<div class='portlet-section-alternate' width="100%">
		      				&lt;rows&gt; <br/>
		      				<span>&nbsp;&nbsp;&nbsp;</span>&lt;row <spagobi:message key =
							"SBIDev.scriptWiz.xmlstructNameAttribute" />1="<spagobi:message key =
							"SBIDev.scriptWiz.xmlstructValueAttribute" />1" <spagobi:message key =
							"SBIDev.scriptWiz.xmlstructNameAttribute" />2="<spagobi:message key =
							"SBIDev.scriptWiz.xmlstructValueAttribute" />2" ... /&gt;  <br/>
						<span>&nbsp;&nbsp;&nbsp;</span>.... <br/>
						<span>&nbsp;&nbsp;&nbsp;</span>&lt;visible-columns&gt;<spagobi:message key =
							"SBIDev.scriptWiz.xmlstructVisibleColumns" />&lt;/visible-columns&gt; <br/>
						<span>&nbsp;&nbsp;&nbsp;</span>&lt;value-column&gt;<spagobi:message key =
							"SBIDev.scriptWiz.xmlstructValueColumn" />&lt;/value-column&gt; <br/>
		      				&lt;/rows&gt; <br/>
		      		</div>
		      	</td>
		    </tr>
		</table>
	</div>
</div>




</div> <!-- close background --> 
