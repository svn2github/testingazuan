<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="it.eng.spagobi.constants.AdmintoolsConstants,
				 it.eng.spagobi.constants.SpagoBIConstants,
				 it.eng.spagobi.bo.ModalitiesValue,
				 it.eng.spagobi.bo.ScriptDetail,
                 javax.portlet.PortletURL,
                 java.util.HashMap,
                 java.util.Set,
                 java.util.Iterator,
                 it.eng.spago.navigation.LightNavigationManager"
                 %>

<%
  SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("DetailModalitiesValueModule"); 
  HashMap profileattrs = (HashMap)moduleResponse.getAttribute(SpagoBIConstants.PROFILE_ATTRS);
  String modality = (String)moduleResponse.getAttribute(SpagoBIConstants.MODALITY);
  ScriptDetail scriptDet = new ScriptDetail();
  ModalitiesValue modVal = (ModalitiesValue)aSessionContainer.getAttribute(SpagoBIConstants.MODALITY_VALUE_OBJECT);
  if(modVal != null){
  		String lovProvider = modVal.getLovProvider();
  		if (lovProvider != null  &&  !lovProvider.equals("")){
  			scriptDet = ScriptDetail.fromXML(lovProvider);
  		}
  }
%>

<%String lovLabel = modVal.getLabel();
  String lovName = modVal.getName();
  String lovDescription = modVal.getDescription();
  String lovType = modVal.getITypeCd(); %>
  
<table width="100%"  style="margin-top:3px; margin-left:3px; margin-right:3px; margin-bottom:5px;">
  	<tr height='1'>
  		<td width="25%"></td>
  		<td style="width:3px;"></td>
  		<td width="12%"></td>
  		<td width="15%"></td>
  		<td width="15%"></td>
  		<td width="33%"></td>
  	</tr>
  	<tr height = "20">
  		<td class='portlet-section-subheader' style='text-align:center;vertical-align:bottom;'>
  			<spagobi:message key = "SBIDev.lovWiz.valueDet1"/>
  		</td>
  		<td style="width:3px;"></td>
  		<td class='portlet-section-body' style='border-top: 1px solid #CCCCCC;'>
  			<spagobi:message key = "SBIDev.lovWiz.valueDet.label"/>: 
  		</td>
  		<td class='portlet-section-alternate' style='border-top: 1px solid #CCCCCC;'>
  			<%=lovLabel %>
  		</td>
  		<td class='portlet-section-body' style='border-top: 1px solid #CCCCCC;'>
  			<spagobi:message key = "SBIDev.lovWiz.valueDet.name"/>: 
  		</td>
  		<td class='portlet-section-alternate' style='border-top: 1px solid #CCCCCC;'>
  			<%=lovName %>
  		</td>
  	</tr>
  	<tr height = "20">
  		<td class='portlet-section-subheader' style='text-align:center;vertical-align:top;'>
  			<spagobi:message key = "SBIDev.lovWiz.valueDet2" />
  		</td>
  		<td style="width:3px;"></td>
  		<td class='portlet-section-body' >
  			<spagobi:message key = "SBIDev.lovWiz.valueDet.type"/>: 
  		</td>
  		<td class = 'portlet-section-alternate'>
  			<%=lovType %>
  		</td>
  		<td class='portlet-section-body'>
  			<spagobi:message key = "SBIDev.lovWiz.valueDet.description"/>: 
  		</td>
  		<td class = 'portlet-section-alternate'>
  			<%=lovDescription %>
  		</td>
  	</tr>
  </table>

<% 
   PortletURL formUrl = renderResponse.createActionURL();
   formUrl.setParameter("PAGE", "detailModalitiesValuePage");
   if(modality.equals(SpagoBIConstants.DETAIL_INS)) {
   		formUrl.setParameter(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.DETAIL_INS_WIZARD_SCRIPT);
   } else {
   		formUrl.setParameter(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.DETAIL_MOD_WIZARD_SCRIPT);
   }
   formUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   
   PortletURL backUrl = renderResponse.createActionURL();
   if(modality.equals(SpagoBIConstants.DETAIL_INS)) {
   		backUrl.setParameter("PAGE", "ListLovsPage");
   } else {
   		backUrl.setParameter("PAGE", "DetailModalitiesValuePage");
   		backUrl.setParameter(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.DETAIL_SELECT);
   		backUrl.setParameter("id", modVal.getId().toString());
   }
   backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
%>
		
<form method='POST' action='<%= formUrl.toString() %>' id ='scriptSelectionWizardForm' name='scriptSelectionWizardForm'>

<!--table width='100%' cellspacing='0' border='0'>		
	<tr height='40'>
		<th align='center'><spagobi:message key = "SBIDev.scriptWiz.title" /></th>
	</tr>
</table-->

<style>
@IMPORT url("/spagobi/css/table.css");
</style>

<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section'>
			<spagobi:message key = "SBIDev.scriptWiz.title" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href="javascript:document.getElementById('scriptSelectionWizardForm').submit()"> 
      			<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBIDev.scriptWiz.saveButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/save.png")%>' alt='<spagobi:message key = "SBIDev.scriptWiz.saveButt" />'/>
			</a> 
		</td>
		<td class='header-button-column-portlet-section'>
			<a href= '<%= backUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBIDev.scriptWiz.backButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' alt='<spagobi:message key = "SBIDev.scriptWiz.backButt" />'/>
			</a>
		</td>
</table>

<input type='hidden' value='' name='id' />

<br/>

<div class='portlet-form-field-label' style='margin-left:5px'>
	<spagobi:message key = "SBIDev.scriptWiz.scriptLbl" />
</div>
<div style='margin-left:5px'>
    <% String script = scriptDet.getScript();
	   if(script == null) {
	 	 	script = "";
	 	}
	%>
	<textarea id="script" name="script" class='portlet-text-area-field' rows="10" style="width:99%;"><%=script%></textarea>
</div>
<div style='margin-left:5px'>
	<%
    	String selectedsingle = " checked='checked' ";
    	String selectedlist = "";
         if(scriptDet.isListOfValues()) {
    		selectedlist = " checked='checked' ";
    		selectedsingle = "";
    	}
    %>
	<input type="radio" name="numberout" value="single" <%= selectedsingle  %> >
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBIDev.scriptWiz.SingleValLbl" />
		</span>
	</input> 
	<input type="radio" name="numberout" value="list" <%= selectedlist %> >
		<span class='portlet-form-field-label'>
			<spagobi:message key = "SBIDev.scriptWiz.ListValLbl" />
		</span>
	</input> 
</div>
<div style='margin-left:5px'>
    <input type='checkbox' name="testbeforesave" /> 
    <span class='portlet-form-field-label'>
    	<spagobi:message key = "SBIDev.scriptWiz.TestBeforeSaveLbl" />
    </span>
</div>
<br/>

</form>

<script type="text/javascript">
function showSintax(){
	var display = document.getElementById("sintax").style.display
	if (display == "none") {
		document.getElementById("sintax").style.display = "inline"
		document.getElementById("sintaxButton").value = "<spagobi:message key = 'SBIDev.scriptWiz.hideSintax'/>"
	}
	else {
		document.getElementById("sintax").style.display = "none"
		document.getElementById("sintaxButton").value = "<spagobi:message key = 'SBIDev.scriptWiz.showSintax'/>"
	}
}
</script>

<input id="sintaxButton" type="button" onclick="showSintax()" value='<spagobi:message key = "SBIDev.scriptWiz.showSintax"/>'>
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
	      			&lt;result&gt; <br/>
	      				<span>&nbsp;&nbsp;&nbsp;</span>&lt;rows&gt; <br/>
	      					<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>&lt;row nameAttribute1="valueAttribute1" nameAttribute2="nameAttribute2" ... &gt;  <br/>
	      				<span>&nbsp;&nbsp;&nbsp;</span>&lt;/rows&gt; <br/>
	      				<span>&nbsp;&nbsp;&nbsp;</span>.... <br/>
	      				<span>&nbsp;&nbsp;&nbsp;</span>&lt;visible-columns&gt;list of visible columns(coma separator)&lt;/visible-columns&gt; <br/>
	      				<span>&nbsp;&nbsp;&nbsp;</span>&lt;value-column&gt;name of the value column&lt;/value-column&gt; <br/>
	      			&lt;/result&gt; <br/>
	      		</div>
	      	</td>
	    </tr>
	</table>
</div>

<%--table width="100%" cellspacing="0" border="0" >
  	<tr height='1'>
  		<td width="30px"><span>&nbsp;</span></td>
  		<td width="60px"><span>&nbsp;</span></td>
  		<td width="30px"><span>&nbsp;</span></td>
  		<td><span>&nbsp;</span></td>
  		<td width="30px"><span>&nbsp;</span></td>
  	</tr>
  	
  	
  	<!-- ************************************************* -->
  	
  	<tr>
      	<td>&nbsp;</td>
      	<td align='right' class='portlet-form-field-label' ><spagobi:message key = "SBIDev.scriptWiz.scriptLbl" /></td>
      	<td>&nbsp;</td>
      	<td>
      	    <% String script = scriptDet.getScript();
      		   if(script == null) {
      		 	 	script = "";
      		 	}
      		%>
      		<textarea id="script" name="script" class='portlet-text-area-field' rows="5" style="width:100%;"><%=script%></textarea>
      	</td>
      	<td>&nbsp;</td>
    </tr>
    <tr>
      	<td colspan="3">&nbsp;</td>
      	<td>
      	    <%
      	    	String selectedsingle = " checked='checked' ";
      	    	String selectedlist = "";
                if(scriptDet.isListOfValues()) {
      	    		selectedlist = " checked='checked' ";
      	    		selectedsingle = "";
      	    	}
      	    %>
      		<input type="radio" name="numberout" value="single" <%= selectedsingle  %> >
      			<span class='portlet-form-field-label'>
      				<spagobi:message key = "SBIDev.scriptWiz.SingleValLbl" />
      			</span>
      		</input> 
      		<input type="radio" name="numberout" value="list" <%= selectedlist %> >
      			<span class='portlet-form-field-label'>
      				<spagobi:message key = "SBIDev.scriptWiz.ListValLbl" />
      			</span>
      		</input> 
      	</td>
      	<td>&nbsp;</td>      	
    </tr>
    <tr>
      	<td colspan="3">&nbsp;</td>
      	<td>
    		<input type='checkbox' name="testbeforesave" /> 
    		<span class='portlet-form-field-label'>
    			<spagobi:message key = "SBIDev.scriptWiz.TestBeforeSaveLbl" />
    		</span>
    	</td>
    	<td>&nbsp;</td>
    </tr>
            
    
    <!-- ************************************************* -->
    
    
    
    <tr height='25'>
      	<td colspan="5">&nbsp;</td>
    </tr>
    <tr height='40'>
      	<td>&nbsp;</td>
      	<td align='right'>&nbsp;</td>
      	<td>&nbsp;</td>
      	<td>
      	    <table>
      	    	<tr>
      	    	 	<td>
      	    	 	   	
      	    	 	    <input type='image' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/save.png")%>' name='save' alt='save'/> 
					</td>
					</form>
					<td width='30'>&nbsp;</td>
					<td>
					    <a href= '<%= backUrl.toString() %>' class='portlet-menu-item' >
      						<img src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' alt='Back' />
						</a> 
					</td>
      	    	</tr>
      	    	<tr>
      	    		<td>
      	    	 	    <a href="javascript:document.getElementById('scriptSelectionWizardForm').submit()" > 
      	    	 	        <spagobi:message key = "SBIDev.scriptWiz.saveButt" />
      	    	 	    </a> 
					</td>
					<td width='30'>&nbsp;</td>
					<td>
						<a href='<%= backUrl.toString() %>'>
							<spagobi:message key = "SBIDev.scriptWiz.backButt" />
						</a>
					</td>
      	    	</tr>
      	    </table>
      	</td>
      	<td>&nbsp;</td>
    </tr>
    
    
    
    <!-- ************************************************* -->
    <tr height='25'>
      	<td colspan="5">&nbsp;</td>
    </tr>
    <tr>
      	<td colspan="3">&nbsp;</td>
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
      	<td>&nbsp;</td>
    </tr>
    <tr>
      	<td colspan="3">&nbsp;</td>
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
      	<td>&nbsp;</td>
    </tr>
    <tr>
      	<td colspan="3">&nbsp;</td>
      	<td>
      		<div class='portlet-section-subheader' style='text-align:center;vertical-align:bottom;' width="100%">
      			<spagobi:message key = "SBIDev.scriptWiz.xmlstruct" />
      		</div>
      		<div class='portlet-section-alternate' width="100%">
      			&lt;result&gt; <br/>
      				<span>&nbsp;&nbsp;&nbsp;</span>&lt;rows&gt; <br/>
      					<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>&lt;row nameAttribute1="valueAttribute1" nameAttribute2="nameAttribute2" ... &gt;  <br/>
      				<span>&nbsp;&nbsp;&nbsp;</span>&lt;/rows&gt; <br/>
      				<span>&nbsp;&nbsp;&nbsp;</span>.... <br/>
      				<span>&nbsp;&nbsp;&nbsp;</span>&lt;visible-columns&gt;list of visible columns(coma separator)&lt;/visible-columns&gt; <br/>
      				<span>&nbsp;&nbsp;&nbsp;</span>&lt;value-column&gt;name of the value column&lt;/value-column&gt; <br/>
      			&lt;/result&gt; <br/>
      		</div>
      	</td>
      	<td>&nbsp;</td>
    </tr>
    
    
    <!-- ************************************************* -->
    
    
    
</table--%>

<!--br/-->
 


