<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="it.eng.spagobi.bo.Engine,
                 javax.portlet.PortletURL,
                 it.eng.spago.navigation.LightNavigationManager" %>

<%@ taglib uri="/WEB-INF/tlds/spagobi.tld" prefix="spagobi" %>
<%@ taglib uri="/WEB-INF/tlds/portlet.tld" prefix="portlet" %>

<portlet:defineObjects/>
<%
    SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("DetailEngineModule"); 
	Engine engine = (Engine)moduleResponse.getAttribute("engineObj");
	String modality = (String)moduleResponse.getAttribute("modality");
%>

<% 
   PortletURL formUrl = renderResponse.createActionURL();
   formUrl.setParameter("PAGE", "detailEnginePage");
   formUrl.setParameter("MESSAGEDET", modality);
   formUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   
   PortletURL backUrl = renderResponse.createActionURL();
   backUrl.setParameter("PAGE", "ListEnginesPage");
   backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
%>
		
<form method='POST' action='<%= formUrl.toString() %>' id='engineForm' name='engineForm'>

<input type='hidden' value='<%= engine.getId() %>' name='id' />

<style>
@IMPORT url("/spagobi/css/table.css");
</style>

<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section'>
			<spagobi:message key = "SBISet.eng.titleMenu" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href="javascript:document.getElementById('engineForm').submit()"> 
      			<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBISet.eng.saveButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/save.png")%>' alt='<spagobi:message key = "SBISet.eng.saveButt" />' /> 
			</a>
		</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= backUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBISet.eng.backButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' alt='<spagobi:message key = "SBISet.eng.backButt" />' />
			</a>
		</td>
	</tr>
</table>

<div class="object-details-div">
	<table class="object-details-table">
	  	<!--tr height='1'>
	  		<td width="1px"><span>&nbsp;</span></td>
	  		<td width="12%"><span>&nbsp;</span></td>
	  		<td width="20px"><span>&nbsp;</span></td>
	  		<td><span>&nbsp;</span></td>
	  	</tr-->
	  	<tr height='25'>
	      	<!--td>&nbsp;</td-->
	      	<td align='right' class='portlet-form-field-label' ><spagobi:message key = "SBISet.eng.LabelField" /></td>
	      	<td>&nbsp;</td>
	      	<td><input class='portlet-form-input-field' type="text" name="label" size="50" value="<%=engine.getLabel()%>" maxlength="20">&nbsp;*</td>
	    </tr>
	  	<tr height='25'>
	      	<!--td>&nbsp;</td-->
	      	<td align='right' class='portlet-form-field-label' ><spagobi:message key = "SBISet.eng.NameField" /></td>
	      	<td>&nbsp;</td>
	      	<td><input class='portlet-form-input-field' type="text" name="name" size="50" value="<%=engine.getName()%>" maxlength="45">&nbsp;*</td>
	    </tr>
	    <tr height='25'>
	      	<!--td>&nbsp;</td-->
	      	<td align='right' class='portlet-form-field-label'><spagobi:message key = "SBISet.eng.descriptionField" /></td>
	      	<td>&nbsp;</td>
	      	<% 
	      		String desc = engine.getDescription();
	      		if( (desc==null) || (desc.equalsIgnoreCase("null"))  ) {
	      			desc = "";
	      		} 
	      	%>
	      	<td ><input class='portlet-form-input-field' type="text" name="description" size="50" value="<%= desc %>" maxlength="130"></td>
	    </tr>
	    <tr height='25'>
	      	<!--td>&nbsp;</td-->
	      	<td align='right' class='portlet-form-field-label'><spagobi:message key = "SBISet.eng.urlField" /></td>
	      	<td>&nbsp;</td>
	      	<td><input class='portlet-form-input-field' type="text" name="url" size="50" value="<%=engine.getUrl()%>" maxlength="260">&nbsp;*</td>
	    </tr>
	    <tr height='25' style='display:none;'>
	      	<!--td>&nbsp;</td-->
	      	<td align='right' class='portlet-form-field-label'><spagobi:message key = "SBISet.eng.secondaryUrlField" /></td>
	      	<td>&nbsp;</td>
	      	<% 
	      		String secUrl = engine.getSecondaryUrl();
	      		if( (secUrl==null) || (secUrl.equalsIgnoreCase("null")) ) {
	      			secUrl = "";
	      		} 
	      	%>
	      	<td><input class='portlet-form-input-field' type="text" name="secondaryUrl" size="50" value="<%=secUrl%>" maxlength="260"></td>
	    </tr>
	    <tr height='25' style='display:none;'>
	      	<!--td>&nbsp;</td-->
	      	<td align='right' class='portlet-form-field-label'><spagobi:message key = "SBISet.eng.dirUploadField" /></td>
	      	<td>&nbsp;</td>
	      	<% 
	      		String dirUpl = engine.getDirUpload();
	      		if( (dirUpl==null) || (dirUpl.equalsIgnoreCase("null")) ) {
	      			dirUpl = "";
	      		} 
	      	%>
	      	<td><input class='portlet-form-input-field' type="text" name="dirUpload" size="50" value="<%=dirUpl%>" maxlength="260"></td>
	    </tr>
	    <tr height='25' style='display:none;'>
	      	<!--td>&nbsp;</td-->
	      	<td align='right' class='portlet-form-field-label'><spagobi:message key = "SBISet.eng.dirUsableField" /></td>
	      	<td>&nbsp;</td>
	      	<% 
	      		String dirUse = engine.getDirUsable();
	      		if( (dirUse==null) || (dirUse.equalsIgnoreCase("null")) ) {
	      			dirUse = "";
	      		} 
	      	%>
	      	<td><input class='portlet-form-input-field' type="text" name="dirUsable" size="50" value="<%=dirUse%>" maxlength="260"></td>
	    </tr>
	    <tr height='25'>
	      	<!--td>&nbsp;</td-->
	      	<td align='right' class='portlet-form-field-label'><spagobi:message key = "SBISet.eng.driverNameField" /></td>
	      	<td>&nbsp;</td>
	      	<td><input class='portlet-form-input-field' type="text" name="driverName" size="50" value="<%=engine.getDriverName()%>" maxlength="260">&nbsp;*</td>
	    </tr>
	    <tr height='25' style='display:none;'>
	      	<!--td>&nbsp;</td-->
	      	<td align='right' class='portlet-form-field-label'><spagobi:message key = "SBISet.eng.criptableField" /></td>
	      	<td>&nbsp;</td>
	      	<td>
	      	   <% 
	      	      boolean isCrypt = false;
	      	      int cript = engine.getCriptable().intValue();
	      	      if(cript > 0) { isCrypt = true; }
	      	     %> 
	      	   <input type="radio" name="criptable" value="1" <% if(isCrypt) { out.println(" checked='checked' "); } %> >True</input>
	      	   <input type="radio" name="criptable" value="0" <% if(!isCrypt) { out.println(" checked='checked' "); } %> >False</input>
	      	 </td>
	    </tr>
	    <%--tr height='25'>
	      	<td colspan="4">&nbsp;</td>
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
						<td width='50'>&nbsp;</td>
						<td>
						    <a href='<%= formUrl1.toString() %>' class='portlet-menu-item' > 
	      						<img border='1' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' alt='Back' />
							</a> 
						</td>
	      	    	</tr>
	      	    	<tr>
	      	    		<td>
	      	    	 	    <a href="javascript:document.getElementById('engineForm').submit()" > 
	      	    	 	        <spagobi:message key = "SBISet.eng.saveButt" />
	      	    	 	    </a> 
						</td>
						<td width='50'>&nbsp;</td>
						<td>
							<a href='<%= formUrl1.toString() %>'>
								<spagobi:message key = "SBISet.eng.backButt" />
							</a>
						</td>
	      	    	</tr>
	      	    </table>
	      	</td>
	    </tr--%>
	</table>
</div>

<div class='errors-object-details-div'>
	<spagobi:error/>
</div>

</form>

<!-- br/-->
 


