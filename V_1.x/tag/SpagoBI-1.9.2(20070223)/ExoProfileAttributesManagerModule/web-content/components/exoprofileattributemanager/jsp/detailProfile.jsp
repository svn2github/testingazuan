<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="javax.portlet.PortletURL,
                 it.eng.spago.navigation.LightNavigationManager" %>

<%
	SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("ExoProfileAttributeManagerModule"); 
	Map attributes = (Map)moduleResponse.getAttribute("attributes");
	Map attributeKeys = (Map)moduleResponse.getAttribute("attributeKeys");
	Set keySet = attributes.keySet();
	Iterator iterAttr = keySet.iterator();
	String username = (String)moduleResponse.getAttribute("UserName");
	String lastname = (String)moduleResponse.getAttribute("LastName");
	String firstname = (String)moduleResponse.getAttribute("FirstName");
	String email = (String)moduleResponse.getAttribute("Email");
 
   	PortletURL saveUrl = renderResponse.createActionURL();
   	saveUrl.setParameter("PAGE", "ExoProfileAttributeManagerPage");
   	saveUrl.setParameter("MESSAGE", "SAVE_PROFILE");
   	saveUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   
   	PortletURL backUrl = renderResponse.createActionURL();
   	backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
%>
		
<%@page import="java.util.Map"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>

<form method='POST' action='<%= saveUrl.toString() %>' id='attributesForm' name='attributesForm'>

	<input type='hidden' value='<%=username%>' name='UserName' />

	<table class='header-table-portlet-section'>		
		<tr class='header-row-portlet-section'>
			<td class='header-title-column-portlet-section' 
			    style='vertical-align:middle;padding-left:5px;'>
				<spagobi:message key = "profileDetail"  bundle="it.eng.spagobi.exoaddins.component_exoprofman_messages" />
			</td>
			<td class='header-empty-column-portlet-section'>&nbsp;</td>
			<td class='header-button-column-portlet-section'>
				<a href="javascript:document.getElementById('attributesForm').submit()"> 
	      			<img class='header-button-image-portlet-section' 
	      				 title='<spagobi:message key = "SBISet.eng.saveButt" />' 
	      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/save.png")%>' 
	      				 alt='<spagobi:message key = "SBISet.eng.saveButt" />' /> 
				</a>
			</td>
			<td class='header-button-column-portlet-section'>
				<a href='<%= backUrl.toString() %>'> 
	      			<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBISet.eng.backButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' alt='<spagobi:message key = "SBISet.eng.backButt" />' />
				</a>
			</td>
		</tr>
	</table>


  
    

	<div class='div_background_no_img' style='padding:5px;'>
     
    <table style="margin:5px;">
    	<tr>
    		<td class='portlet-section-header'><spagobi:message key = "profileattr.username" /></td>
    		<td class='portlet-section-header'><spagobi:message key = "profileattr.firstname" /></td>
    		<td class='portlet-section-header'><spagobi:message key = "profileattr.lastname" /></td>
    		<td class='portlet-section-header'><spagobi:message key = "profileattr.email" /></td>
    	</tr>
    	<tr>
    	  <td style="border-bottom: 1px solid #cccccc;background-color:#fafafa;"><%=username%></td>
    		<td style="border-bottom: 1px solid #cccccc;background-color:#fafafa;"><%=firstname%></td>
    		<td style="border-bottom: 1px solid #cccccc;background-color:#fafafa;"><%=lastname%></td>
    		<td style="border-bottom: 1px solid #cccccc;background-color:#fafafa;"><%=email%></td>    	
      </tr>
    </table> 
     
    <br/>
    
    <div class="div_detail_area_forms" style="padding:10px;">
	  <table>
	  	<%
	  		while(iterAttr.hasNext()) {
	  			String key = (String)iterAttr.next();
	  			String value = (String)attributes.get(key);
	   	%>
			<tr>
	   			<td width="250">
	   				<span class='portlet-form-field-label'>
						<%=key%>
					</span>
	   			</td>
	   			<td>
	   				<input class='portlet-form-input-field' type="text" name="<%=attributeKeys.get(key)%>" 
			      	       size="40" value="<%=value%>" >
	   			</td>  
	   		</tr>
	    <%  } %>
	  </table>  
		</div>

	</div>


</form>

