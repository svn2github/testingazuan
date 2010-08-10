<!--
SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
-->

<%@ page language="java"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@page import="it.eng.spago.security.IEngUserProfile"%>
<%@page import="it.eng.spago.base.SessionContainer"%>
<%@page import="it.eng.spago.base.RequestContainer"%>
<%@page import="it.eng.spagobi.utilities.ChannelUtilities"%>
<%@page import="it.eng.spagobi.utilities.GeneralUtilities"%>

<%
    if(ChannelUtilities.isPortletRunning()) {
		SessionContainer permSess = aSessionContainer.getPermanentContainer();
		IEngUserProfile portletProfile = (IEngUserProfile)permSess.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		String usernameprofile = (String)portletProfile.getUserUniqueIdentifier();
 %>


<script type="text/javascript">

	var ajaxRequestCanBeDone;

    function shareProfile() {
       url="<%=GeneralUtilities.getSpagoBiContextAddress()%>/ShareProfileService?";
       pars = "username=<%=usernameprofile%>";
       new Ajax.Request(url,
          {
            method: 'post',
            parameters: pars,
            onSuccess: function(transport){
                            response = transport.responseText || "";
                            checkResponse(response);
                        },
            onFailure: aJSErrorOccured,
            asynchronous: false
          }
        );
	  }
	  
	  function somethingWentWrong() {
	  	alert('<spagobi:message key = "somefunctionalitieswontwork" />');
	  }
	  
	  function checkResponse(response) {
	  	if(response!='done') {
	  		somethingWentWrong();
	  	}
	  }
	  
	  function aJSErrorOccured () {
	  	alert('Error during Ajax call!!')
	  } 
	  
	  /*
	  * The Ajax call is mode once in the page: if more than one portlet have this code, 
	  * only one Ajax call will be executed
	  */
	  function canMakeAjaxRequest() {
		if (ajaxRequestCanBeDone == undefined) {
			ajaxRequestCanBeDone = false; 
			return true;
		} else {
			return false;
		}
	  }
	  
	  if (canMakeAjaxRequest()) shareProfile();
	  
</script>

<%
    }
%>
