<%@ include file="/jsp/portlet_base.jsp"%>
<%@ page import="it.eng.spagobi.bo.BIObject,
				 it.eng.spagobi.bo.dao.IBIObjectDAO,
				 it.eng.spagobi.bo.dao.DAOFactory" %>

<%  String biObj_id = aServiceRequest.getAttribute("OBJECT_ID").toString(); 
	Integer biObjId = new Integer(biObj_id);
	BIObject BIObject = DAOFactory.getBIObjectDAO().loadBIObjectForDetail(biObjId);
	String label = BIObject.getLabel();
	String name = BIObject.getName();
	String description = BIObject.getDescription();
	String type = BIObject.getBiObjectTypeCode();	%>

<table width="100%"  style="margin-top:3px; margin-left:3px; margin-right:3px; margin-bottom:5px;">
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

<spagobi:listBiParameters moduleName="ListBIObjectParametersModule" />