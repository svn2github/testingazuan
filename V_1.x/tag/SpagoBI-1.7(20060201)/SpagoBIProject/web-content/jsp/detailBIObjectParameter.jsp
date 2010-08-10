<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="it.eng.spagobi.bo.Engine,
				 it.eng.spagobi.bo.BIObjectParameter,
				 it.eng.spagobi.bo.BIObject,
				 it.eng.spagobi.constants.ObjectsTreeConstants,
 				 it.eng.spagobi.services.modules.DetailBIObjectParameterModule,
    			 javax.portlet.PortletURL,
				 it.eng.spagobi.bo.Parameter,
				 it.eng.spagobi.bo.dao.IParameterDAO,
				 it.eng.spagobi.bo.dao.DAOFactory,
				 java.util.List,
				 it.eng.spago.navigation.LightNavigationManager" %>


<%@ taglib uri="/WEB-INF/tlds/spagobi.tld" prefix="spagobi" %>
<%@ taglib uri="/WEB-INF/tlds/portlet.tld" prefix="portlet" %>
<portlet:defineObjects/>

<%
    SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("DetailBIObjectParameterModule"); 
	BIObjectParameter objPar = (BIObjectParameter)moduleResponse.getAttribute(DetailBIObjectParameterModule.NAME_ATTR_OBJECT_PAR);
	String modality = (String)moduleResponse.getAttribute(ObjectsTreeConstants.MODALITY);
	String parIdOld = (String)aServiceRequest.getAttribute("PAR_ID");
	String path = (String)moduleResponse.getAttribute(ObjectsTreeConstants.PATH);
%>

<%  
	Integer biObjId = objPar.getBiObjectID();
	BIObject BIObject = DAOFactory.getBIObjectDAO().loadBIObjectForDetail(biObjId);
	String label = BIObject.getLabel();
	String name = BIObject.getName();
	String description = BIObject.getDescription();
	String type = BIObject.getBiObjectTypeCode();	%>


<table width="100%" style="margin-top:3px; margin-left:3px; margin-right:3px; margin-bottom:5px;">
  	<tr height='1'>
  		<td width="23%"></td>
  		<td style="width:3px;"></td>
  		<td width="12%"></td>
  		<td width="15%"></td>
  		<td width="15%"></td>
  		<td width="35%"></td>
  	</tr>
  	<tr height = "20">
  		<td class='portlet-section-subheader' style='text-align:center;vertical-align:bottom;'>
  			<spagobi:message key = "SBIDev.docConf.ListdocDetParam.BIObjectInfo1" />
  		</td>
  		<td style="width:3px;"></td>
  		<td class='portlet-section-body' style='border-top: 1px solid #CCCCCC;'>
  			<spagobi:message key = "SBIDev.docConf.ListdocDetParam.BIObjectInfo.label"/>: 
  		</td>
  		<td class='portlet-section-alternate' style='border-top: 1px solid #CCCCCC;'>
  			<%=label %>
  		</td>
  		<td class='portlet-section-body' style='border-top: 1px solid #CCCCCC;'>
  			<spagobi:message key = "SBIDev.docConf.ListdocDetParam.BIObjectInfo.name"/>: 
  		</td>
  		<td class='portlet-section-alternate' style='border-top: 1px solid #CCCCCC;'>
  			<%=name %>
  		</td>
  	</tr>
  	<tr height = "20">
  		<td class='portlet-section-subheader' style='text-align:center;vertical-align:top;'>
  			<spagobi:message key = "SBIDev.docConf.ListdocDetParam.BIObjectInfo2" />
  		</td>
  		<td style="width:3px;"></td>
  		<td class='portlet-section-body'>
  			<spagobi:message key = "SBIDev.docConf.ListdocDetParam.BIObjectInfo.type"/>: 
  		</td>
  		<td class = 'portlet-section-alternate'>
  			<%=type %>
  		</td>
  		<td class='portlet-section-body'>
  			<spagobi:message key = "SBIDev.docConf.ListdocDetParam.BIObjectInfo.description"/>: 
  		</td>
  		<td class = 'portlet-section-alternate'>
  			<%=description %>
  		</td>
  	</tr>
  </table>
 
<% 
   PortletURL formUrl = renderResponse.createActionURL();
   formUrl.setParameter("PAGE", "detailBIObjectParameterPage");
   if (modality != null){
   	formUrl.setParameter("MESSAGEDET", modality);
   }
   formUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   
   PortletURL backUrl = renderResponse.createActionURL();
   backUrl.setParameter("PAGE", "ListBIObjectParametersPage");
   if ((objPar != null) && (objPar.getBiObjectID() != null) && (path != null)){ 
   	backUrl.setParameter(ObjectsTreeConstants.OBJECT_ID, objPar.getBiObjectID().toString());
    backUrl.setParameter(ObjectsTreeConstants.PATH, path);
   }
   backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
%>

<!--spagobi:error /-->

<!--table width='100%' cellspacing='0' border='0'>		
	<tr height='40'>
		<th align='center'><spagobi:message key = "SBIDev.docConf.docDetParam.title" /></th>
	</tr>
</table-->

<form method='POST' action='<%= formUrl.toString() %>' id = 'objectParameterForm' name='objectParameterForm'>

<style>
@IMPORT url("/spagobi/css/table.css");
</style>

<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section'>
			<spagobi:message key = "SBIDev.docConf.docDetParam.title" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href="javascript:document.getElementById('objectParameterForm').submit()"> 
      			<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBIDev.docConf.docDetParam.saveButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/save.png")%>' alt='<spagobi:message key = "SBIDev.docConf.docDetParam.saveButt" />' /> 
			</a>
		</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= backUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBIDev.docConf.docDetParam.backButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' alt='<spagobi:message key = "SBIDev.docConf.docDetParam.backButt" />' />
			</a>
		</td>
	</tr>
</table>

<input type='hidden' value='<%= objPar.getBiObjectID() %>' name='id' />
<input type='hidden' value='<%= parIdOld %>' name='parIdOld' />
<input type='hidden' value='<%= path %>' name='path' />
<table width="100%" cellspacing="0" border="0" >
  	<tr height='1'>
  		<td style="width:1px;"><span>&nbsp;</span></td>
  		<td width="9%"><span>&nbsp;</span></td>
  		<td width="20px"><span>&nbsp;</span></td>
  		<td><span>&nbsp;</span></td>
  	</tr>
  	<tr height='25'>
      	<td>&nbsp;</td>
      	<td align='right' class='portlet-form-field-label' ><spagobi:message key = "SBIDev.docConf.docDetParam.labelField" /></td>
      	<td>&nbsp;</td>
      	<td><input class='portlet-form-input-field' type="text" name="label" size="40" value="<%=objPar.getLabel()%>" maxlength="20">&nbsp;*</td>
    </tr>
   <tr height='25'>
      	<td>&nbsp;</td>
      	<td align='right' class='portlet-form-field-label'><spagobi:message key = "SBIDev.docConf.docDetParam.paramField" /></td>
      	<td>&nbsp;</td>
      	<td>
      	<select class='portlet-form-field' name="par_id">
      	<%
      	  IParameterDAO param = DAOFactory.getParameterDAO();
      	  String BIObj_parId = (objPar.getParameter() != null? objPar.getParameter().getId().toString() : null);
      	  List list = param.loadAllParameters();
      	  for (int i=0; i<list.size();i++) {
      	  	Parameter parameter = (Parameter)list.get(i);
      	    String curr_parId = parameter.getId().toString();
      	    boolean isPar = false;
      	    if (curr_parId.equals(BIObj_parId)){
      	  		isPar = true;
      	  
      	  }%>
      	  
      	
      	<option value="<%= parameter.getId().toString()  %>"<%if(isPar) out.print(" selected='selected' ");  %>><%=parameter.getName()%></option>
        
        <%} %>
        <
        </select>
        </td>
    </tr>
    <tr height='25'>
      	<td>&nbsp;</td>
      	<td align='right' class='portlet-form-field-label'><spagobi:message key = "SBIDev.docConf.docDetParam.parurl_nmField" /></td>
      	<td>&nbsp;</td>
      	<% 
      	  	String urlName = objPar.getParameterUrlName();
      	  	if(urlName==null) {
      	  		urlName = "";
      	  	}
      	%>
      	<td ><input class='portlet-form-input-field' type="text" name="parurl_nm" size="20" value="<%=urlName%>" maxlength="20"></td>
    </tr>
    <tr height='25'>
      	<td>&nbsp;</td>
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
    <tr height='25'>
      	<td>&nbsp;</td>
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
    <tr height='25'>
      	<td>&nbsp;</td>
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
    <tr height='25'>
      	<td>&nbsp;</td>
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
					<td width='30'>&nbsp;</td>
					<td>
					    <a href= '<%= backUrl.toString() %>' class='portlet-menu-item' >
      						<img src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' alt='Back' />
						</a> 
					</td>
      	    	</tr>
      	    	<tr>
      	    		<td>
      	    	 	    <a href="javascript:document.getElementById('objectParameterForm').submit()" > 
      	    	 	        <spagobi:message key = "SBIDev.docConf.docDetParam.saveButt" /> 
      	    	 	    </a> 
					</td>
					<td width='30'>&nbsp;</td>
					<td>
						<a href='<%= backUrl.toString() %>'>
							<spagobi:message key = "SBIDev.docConf.docDetParam.backButt" />
						</a>
					</td>
      	    	</tr>
      	    </table>
      	</td>
    </tr--%>
	
</form>

</table>

