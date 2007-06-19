<%@ page language="java"
         extends="it.eng.spago.dispatching.httpchannel.AbstractHttpJspPage"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         session="true" 
         import="it.eng.spago.base.*,
 				 it.eng.spago.configuration.ConfigSingleton,
				 it.eng.spagobi.geo.bo.SbiGeoMaps,
                 it.eng.spagobi.geo.bo.dao.DAOFactory,
                 java.util.Map,
                 java.util.HashMap" %>
<%
	System.out.println("detailMap.jsp::inizio");
	RequestContainer aRequestContainer = null;
	ResponseContainer aResponseContainer = null;
	SessionContainer aSessionContainer = null;
//	IUrlBuilder urlBuilder = null;
//	IMessageBuilder msgBuilder = null;
	
	// get configuration
//	ConfigSingleton spagoconfig = ConfigSingleton.getInstance();
	// get mode of execution
	//String sbiMode = (String)spagoconfig.getAttribute("SPAGOBI.SPAGOBI-MODE.mode");   
//	String sbiMode = null;
		
	// case of portlet mode
	//aRequestContainer = RequestContainerPortletAccess.getRequestContainer(request);
	//aResponseContainer = ResponseContainerPortletAccess.getResponseContainer(request);
	if (aRequestContainer == null) {
		// case of web mode
		aRequestContainer = RequestContainerAccess.getRequestContainer(request);
		aResponseContainer = ResponseContainerAccess.getResponseContainer(request);
	}
	// get other spago object
	SourceBean aServiceRequest = aRequestContainer.getServiceRequest();
	SourceBean aServiceResponse = aResponseContainer.getServiceResponse();
	System.out.println("aServiceRequest: " + aServiceRequest);
	System.out.println("aServiceResponse: " + aServiceResponse);

	aSessionContainer = aRequestContainer.getSessionContainer();

//	SourceBean actionResponse = (SourceBean)aServiceResponse.getAttribute("DetailMapAction"); 
//	System.out.println("actionResponse: " + actionResponse);
	SbiGeoMaps map = (SbiGeoMaps)aServiceResponse.getAttribute("mapObj");
	System.out.println("map: " + map);
	String modality = (String)aServiceResponse.getAttribute("modality");
	System.out.println("modality: " + modality);
 
	Map formUrlPars = new HashMap();
	formUrlPars.put("ACTION", "detailMapAction");
	formUrlPars.put("MESSAGEDET", modality);

%>
<SCRIPT LANGUAGE="JavaScript">
function setMsg(val) {
	alert("MESSAGE: "  + val);
	document.mapForm.action= "http://localhost:8080/SpagoBIGeoEngine/servlet/AdapterHTTP";
	document.mapForm.NEW_SESSION.value = "TRUE";
	document.mapForm.MESSAGEDET.value = val;
	document.mapForm.MAP_ID.value = "1";
	document.mapForm.submit();
							
}
</SCRIPT>
<form method='POST' action="" id='mapForm' name='mapForm'>
	<input type="hidden" name="ACTION_NAME" value="DETAIL_MAP_ACTION"/>
	<input type="hidden" name="NEW_SESSION" value="TRUE"/>
	<input type="hidden" name="MESSAGEDET" value=""/>
<!-- 	<input type="hidden" name="MAP_ID" value="1"/> -->
	
	<table class='header-table-portlet-section'>
		<tr class='header-row-portlet-section'>
			<td class='header-title-column-portlet-section' 
			    style='vertical-align:middle;padding-left:5px;'>MAP ID:</td>		
			<td class='header-button-column-portlet-section'>
			  <input class='portlet-form-input-field' type="text" id="MAP_ID" size="42" 
		     	     name="MAP_ID" value='<%=map.getMapId()%>' 
				   	  maxlength="100" >
			</td>
		</tr>
		<tr class='header-row-portlet-section'>
			<td class='header-title-column-portlet-section' 
			    style='vertical-align:middle;padding-left:5px;'>MAP NAME:</td>		
			<td class='header-button-column-portlet-section'>
			  <input class='portlet-form-input-field' type="text" id="NAME" size="42" 
		     	     name="NAME" value='<%= map.getName() != null ? map.getName() : "" %>' 
				   	  maxlength="100" >
			</td>
		</tr>
		<tr class='header-row-portlet-section'>
			<td class='header-title-column-portlet-section' 
			    style='vertical-align:middle;padding-left:5px;'>MAP DESC:</td>		
			<td class='header-button-column-portlet-section'>
			  <input class='portlet-form-input-field' type="text" id="DESCR" size="42" 
		     	     name="DESCR" value='<%= map.getDescr() != null ? map.getDescr() : "" %>' 
				   	  maxlength="100" >
			</td>
		</tr>
		<tr class='header-row-portlet-section'>
			<td class='header-title-column-portlet-section' 
			    style='vertical-align:middle;padding-left:5px;'>MAP URL:</td>		
			<td class='header-button-column-portlet-section'>
			  <input class='portlet-form-input-field' type="text" id="URL" size="42" 
		     	     name="URL" value='<%= map.getUrl() != null ? map.getUrl() : "" %>' 
				   	  maxlength="100" >
			</td>
		</tr>	
		<tr>
		<td><input type="button" value="INSERT" onclick="setMsg('DETAIL_INS')"></td>
		<td><input type="button" value="UPDATE" onclick="setMsg('DETAIL_MOD')"></td>
		<td><input type="button" value="DELETE" onclick="setMsg('DETAIL_DEL')"></td>
		</tr>	
	</table>
</form>
