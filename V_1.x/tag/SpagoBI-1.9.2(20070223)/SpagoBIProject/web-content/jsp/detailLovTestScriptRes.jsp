<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="javax.portlet.PortletURL,
				 it.eng.spagobi.constants.SpagoBIConstants,
				 it.eng.spagobi.bo.ModalitiesValue,
				 it.eng.spago.navigation.LightNavigationManager"%>

<%
	ModalitiesValue modVal = (ModalitiesValue)aSessionContainer.getAttribute(SpagoBIConstants.MODALITY_VALUE_OBJECT);
	
	SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("DetailModalitiesValueModule"); 
  
	String result = (String)moduleResponse.getAttribute("result");
  	String stack = (String)moduleResponse.getAttribute("stacktrace");
  	String modality = (String)moduleResponse.getAttribute("modality");
  	String script = (String)moduleResponse.getAttribute("script");
  	String numberout = (String)moduleResponse.getAttribute("numberout");
  	
  	String messagedetSave = "";
  	if(modality.equals(SpagoBIConstants.DETAIL_INS))
		messagedetSave = SpagoBIConstants.DETAIL_INS_WIZARD_SCRIPT;
	else  messagedetSave = SpagoBIConstants.DETAIL_MOD_WIZARD_SCRIPT;
		
  	PortletURL formSave = renderResponse.createActionURL();
  	formSave.setParameter("PAGE", "DetailModalitiesValuePage");
  	formSave.setParameter(SpagoBIConstants.MESSAGEDET, messagedetSave);
  	formSave.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");

  	String messagedetBack = SpagoBIConstants.DETAIL_VIEW_WIZARD_SCRIPT_AFTER_TEST;  	
  	PortletURL formBack = renderResponse.createActionURL();
  	formBack.setParameter("PAGE", "DetailModalitiesValuePage");
  	formBack.setParameter(SpagoBIConstants.MESSAGEDET, messagedetBack);
  	formBack.setParameter("modality", modality);
  	formBack.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "0");
  	
%>

<%--table width='100%' cellspacing='0' border='0'>		
	<tr height='40'>
		<th align='center'><spagobi:message key = "SBIDev.scriptWiz.title" /></th>
	</tr>
</table--%>


<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section'
		    style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "SBIDev.scriptWiz.title" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<form method="post" action="<%=formSave.toString()%>" name="formSave" id="formSave">
			<td class='header-button-column-portlet-section'>
				<input type="hidden" name="script" value="<%=script%>" />
				<input type="hidden" name="numberout" value="<%=numberout%>" />
				<a href="javascript:document.getElementById('formSave').submit()"> 
      					<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBISet.Funct.saveButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/save.png")%>' alt='<spagobi:message key = "SBISet.Funct.saveButt" />' /> 
				</a>	
			</td>
		</form>
		<form method="post" action="<%=formBack.toString()%>" name="formBack" id="formBack">
			<td class='header-button-column-portlet-section'>
				<input type="hidden" name="id" value="<%=modVal.getId()%>" />
				<input type="hidden" name="<%=SpagoBIConstants.ID_MODALITY_VALUE%>" value="<%=modVal.getId()%>" />
				<input type="hidden" name="name" value="<%=modVal.getName()%>" />
				<input type="hidden" name="label" value="<%=modVal.getLabel()%>" />
				<input type="hidden" name="description" value="<%=modVal.getDescription()%>" />
				<input type="hidden" name="input_type" value="<%=modVal.getITypeCd() + "," + modVal.getITypeId()%>" />
				<a href="javascript:document.getElementById('formBack').submit()"> 
      					<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBISet.Funct.backButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' alt='<spagobi:message key = "SBISet.Funct.backButt" />' />
				</a>
			</td>
		</form>
	</tr>
</table>

<% if(result!=null) { %>
    
    <div width="100%">
	    <br/>
		<div style="position:relative;left:10%;width:80%" class='portlet-form-field-label' ><spagobi:message key = "SBIDev.scriptWiz.execCorrect" /></div>
		<br/>	
	    <div style="position:relative;left:10%;width:70%" class='portlet-section-alternate'><%= result %></div>
 	</div>

<% } else { %>

    <br/>
    <div style="left:10%;width:80%" class='portlet-form-field-label' ><spagobi:message key = "SBIDev.scriptWiz.execNotCorrect" /></div>
    <br/>	
    <div style="left:10%;width:70%" class='portlet-section-alternate'><%= stack %></div>

<% } %>


<br/>
<br/>


<%--form method="post" action="<%=formSave.toString()%>" name="formSave" id="formSave">
	<input type="hidden" name="script" value="<%=script%>" />
	<input type="hidden" name="numberout" value="<%=numberout%>" />
	
<div style="width:100%;">
	<table width="100%">
    	<tr>
    	    <td>&nbsp;</td>
    	 	<td width="80px" align='center'>
    	 	    <input type='image' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/save.png")%>' name='save' alt='save'/> 
			</td>
			</form>
			<td width='30px'>&nbsp;</td>
			
			<form method="post" action="<%=formBack.toString()%>" name="formBack" id="formBack">
				<input type="hidden" name="id" value="<%=modVal.getId()%>" />
				<input type="hidden" name="<%=SpagoBIConstants.ID_MODALITY_VALUE%>" value="<%=modVal.getId()%>" />
				<input type="hidden" name="name" value="<%=modVal.getName()%>" />
				<input type="hidden" name="label" value="<%=modVal.getLabel()%>" />
				<input type="hidden" name="description" value="<%=modVal.getDescription()%>" />
				<input type="hidden" name="input_type" value="<%=modVal.getITypeCd() + "," + modVal.getITypeId()%>" />

			<td width="80px" align='center'>
					<input type="image" src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' alt='Back' />
			</td>
			</form>
			
			<td>&nbsp;</td>
      	</tr>
      	<tr>
      	    <td>&nbsp;</td>
      	    <td width="80px" align='center'>
      	        <a href="javascript:document.getElementById('formSave').submit()">
      	        	<spagobi:message key = "SBISet.Funct.saveButt" /> 
      	        </a>
			</td>
			<td width='30px'>&nbsp;</td>
			<td width="80px" align='center'>
			  	<a href="javascript:document.getElementById('formBack').submit()">
				   	<spagobi:message key = "SBISet.Funct.backButt" />
				</a>
			</td>
			<td>&nbsp;</td>
      	</tr>
  	</table>
</div--%>





<%-- 
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
   
   
   PortletURL backUrl = renderResponse.createActionURL();
   if(modality.equals(SpagoBIConstants.DETAIL_INS)) {
   		backUrl.setParameter("PAGE", "ListLovsPage");
   } else {
   		backUrl.setParameter("PAGE", "DetailModalitiesValuePage");
   		backUrl.setParameter(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.DETAIL_SELECT);
   		backUrl.setParameter("id", modVal.getId().toString());
   }
%>
		
<form method='POST' action='<%= formUrl.toString() %>' id ='scriptSelectionWizardForm' name='scriptSelectionWizardForm'>

<input type='hidden' value='' name='id' />

<table width="100%" cellspacing="0" border="0" >
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
      					out.write("<br/>");
      				}
      			%>
      		</div>
      	</td>
      	<td>&nbsp;</td>
    </tr>
    
    
    <!-- ************************************************* -->
    
    
    
</table>

<br/>
 
--%>

