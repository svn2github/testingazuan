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

<%@ include file="/jsp/portlet_base.jsp"%>

<%@page import="it.eng.spago.base.SourceBean"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="it.eng.spago.navigation.LightNavigationManager"%>
<%@page import="it.eng.spagobi.constants.SpagoBIConstants"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="it.eng.spagobi.bo.BIObjectParameter"%>
<%@page import="it.eng.spagobi.scheduler.modules.SchedulerGUIModule"%>

<%  
   SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("SchedulerGUIModule"); 
   Integer objid = (Integer)moduleResponse.getAttribute(SpagoBIConstants.OBJECT_ID);
   String jobExist = (String)moduleResponse.getAttribute(SpagoBIConstants.OBJ_JOB_EXISTS);
   ObjExecSchedulation oes = (ObjExecSchedulation)moduleResponse.getAttribute(SpagoBIConstants.OBJ_SCHEDULE_DETAIL);
   List opis = oes.getObjExecParameters();
   Iterator opisIter = opis.iterator();
   
   PortletURL backUrl = renderResponse.createActionURL();
   backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
   
   PortletURL saveUrl = renderResponse.createActionURL();
   saveUrl.setParameter("PAGE", SchedulerGUIModule.MODULE_PAGE);
   saveUrl.setParameter("MESSAGEDET", SpagoBIConstants.MESSAGE_SCHEDULE_OBJECT);
   saveUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "TRUE");
   
%>


<%@page import="it.eng.spagobi.scheduler.to.ObjExecSchedulation"%>
<%@page import="it.eng.spagobi.scheduler.to.BIObjectParamInfo"%>
<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			Change Me			
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= backUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "Sbi.back" bundle="component_impexp_messages" />' 
      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/importexport/img/back.png")%>' 
      				 alt='<spagobi:message key = "Sbi.back"  bundle="component_impexp_messages"/>' />
			</a>
		</td>
	</tr>
</table>


<form method="post" action="<%=saveUrl.toString()%>" >
    <input type="hidden" name="<%=SpagoBIConstants.OBJECT_ID%>" value="<%=objid%>" />
	<input type="hidden" name="<%=SpagoBIConstants.OBJ_JOB_EXISTS%>" value="<%=jobExist%>" />
	<table>
		<tr>
		 	<td colspan="2" bgcolor="yellow" style="height:25px;">Schedule Info</td>
		</tr>
		<tr>
		 	<td width="200px">Schedule Name:</td>
		 	<td><input type="text" name="triggername" /></td>
		</tr>
		<tr>
		 	<td width="200px">Schedule Description:</td>
		 	<td><input type="text" name="triggerdescription" /></td>
		</tr>
		<tr>
		 	<td width="200px">Start Date:</td>
		 	<td><input type="text" name="startdate" />gg/mm/yyyy</td>
		</tr>
		<tr>
		 	<td width="200px">Start Time:</td>
		 	<td><input type="text" name="starttime" />hh:mm</td>
		</tr>
		<tr>
		 	<td width="200px">End Date:</td>
		 	<td><input type="text" name="enddate" />gg/mm/yyyy</td>
		</tr>
		<tr>
		 	<td width="200px">End Time:</td>
		 	<td><input type="text" name="endtime" />hh:mm</td>
		</tr>
		<tr>
		 	<td width="200px">Repeat Interval:</td>
		 	<td><input type="text" name="repeatInterval" />ms</td>
		</tr>
		<tr>
		 	<td width="200px">Store Output:</td>
		 	<td><input type="checkbox" name="storeoutput" /></td>
		</tr>
		<tr>
		 	<td width="200px">Store Name:</td>
		 	<td><input type="text" name="storename" /></td>
		</tr>
		<tr>
		 	<td width="200px">Store Description:</td>
		 	<td><input type="text" name="storedescription" /></td>
		</tr>
		<tr>
		 	<td width="200px">History Length:</td>
		 	<td><input type="text" name="historylength" /></td>
		</tr>
		<tr>
		 	<td width="200px">Store As:</td>
		 	<td>
		 		<input type="radio" name="storetype" value="storesnap" checked="checked"/> Document snapshot
				<input type="radio" name="storetype" value="storedoc" /> New document 
			</td>
		</tr>
		<tr>
		 	<td width="200px">Path Document:</td>
		 	<td><input type="text" name="pathdocument" /></td>
		</tr>
		<tr>
		 	<td colspan="2" bgcolor="yellow" style="height:25px;">BIObject Parameters</td>
		</tr>
		<%
			while(opisIter.hasNext()) {
				BIObjectParamInfo bipo = (BIObjectParamInfo)opisIter.next();
		%>
		<tr>
		 	<td width="200px"><%=bipo.getLabel()%>:</td>
		 	<td><input type="text" name="biobjpar_<%=bipo.getUrlname()%>" /></td>
		</tr>
		<%
			}
		%>
	</table>
   
	
	
	


	
	
	
	

	<input type="submit" value="save" />


</form>

