<%-- 

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

--%>

<%@page import="it.eng.spago.configuration.ConfigSingleton"%>
<%@page import="java.util.Iterator"%>
<%@page import="it.eng.qbe.model.io.LocalFileSystemDataMartModelRetriever"%>
<%@page import="java.util.List"%>
<%@page import="java.io.File"%>
<%@ page import="it.eng.spago.base.*"%>
<%@page import="it.eng.qbe.utility.FileUtils"%>
<%@page import="it.eng.spago.security.IEngUserProfile"%>

<link rel="stylesheet" href ="../css/spagobi.css" type="text/css"/>
<link rel="stylesheet" href ="../css/jsr168.css" type="text/css"/>
<link rel="stylesheet" href ="../css/external.css" type="text/css"/>
<link rel="styleSheet" href ="../css/dtree.css" type="text/css" />	

<% 
   File contextDir = new File(ConfigSingleton.getInstance().getRootPath());
   List allMarts = LocalFileSystemDataMartModelRetriever.getAllDataMartPath(contextDir); 
   Iterator it = allMarts.iterator();
   
   RequestContainer requestContainer = RequestContainerAccess.getRequestContainer(request);
   SessionContainer sessionContainer = requestContainer.getSessionContainer();
   
   String dmPath = null;
   String completePath = null;
   
   String qbeDataMartDir = FileUtils.getQbeDataMartDir(new File(ConfigSingleton.getInstance().getRootPath()));
   
   String link = "#";
   String linkDelete = "#";
   IExtendedEngUserProfile userProfile = (IExtendedEngUserProfile)sessionContainer.getPermanentContainer().getAttribute(IEngUserProfile.ENG_USER_PROFILE);
%>

<%@page import="it.eng.qbe.utility.Utils"%>
<%@page import="it.eng.spago.security.IExtendedEngUserProfile"%>
<html>

<tr>
		<td width="3%">
		</td>
		<td width="94%">
		</td>
		<td width="3%">
		</td>
</tr>
<tr>
	<td width="3%">
	</td>
	<td width="94%">
		<table>
		<thead>
						<tr>
							<td class='portlet-section-header' style='vertical-align:middle;'>DataMart</td>
							<td class='portlet-section-header' style='vertical-align:middle;'>Path</td>
							<td class='portlet-section-header' style='vertical-align:middle;'>&nbsp;</td>
							<td class='portlet-section-header' style='vertical-align:middle;'>
								<a href="../servlet/AdapterHTTP?ACTION_NAME=LOGOUT_ACTION"> 
      								<img class='header-button-image-portlet-section' title='Back'
										src='../img/back.gif' alt='Logout' />
								</a>
							</td>
						
						</tr>
		</thead>
		<tbody>
		<%
		String rowClass =  "portlet-section-body";
		boolean alternate = false;
   		
		while(it.hasNext()){
   		 	rowClass = (alternate) ? "portlet-section-alternate" : "portlet-section-body";
         	alternate = !alternate; 
	   		dmPath = (String)it.next();
	   		completePath = qbeDataMartDir  + System.getProperty("file.separator") + dmPath;
	   		link = "../servlet/AdapterHTTP?ACTION_NAME=DETAIL_DATA_MART_ACTION&PATH="+dmPath;
	   		linkDelete = "../servlet/AdapterHTTP?ACTION_NAME=DELETE_DATA_MART_ACTION&PATH="+dmPath;
	   		
		%>
	
	   <tr class='portlet-font'>
	   		<td class="<%=rowClass%>"><%= dmPath %></td>
	   		<td class="<%=rowClass%>"><%= completePath %></td>
	   		<td class="<%=rowClass%>">
	   		
	   			<a href="<%=link%>">
	   				<img alt="Datamart Detail" title="Datamart Detail" src="../img/detail.gif"/>
	   			</a>
	   		
	   		</td>
	   		<td class="<%=rowClass%>">
	   		
	   			<a href="<%=linkDelete%>">
	   				<img alt="Delete Datamart" title="Datamart Delete" src="../img/delete.gif"/>
	   			</a>
	   		
	   		</td>
	   </tr>
	   
	   </tbody>
<%		
   }
%>
		</table>
	</td>

	<td width="3%">
	</td>
</tr>
<tr class="portlet-font">
		<td colspan="4">
			&nbsp;
		</td>
</tr>
<tr class="portlet-font">
		<td colspan="4">
			&nbsp;
		</td>
</tr>
<tr class="portlet-font">
		<td colspan="4">
			&nbsp;
		</td>
</tr>
<tr class="portlet-font">
		<td colspan="4">
			&nbsp;
		</td>
</tr>

<form id="formUploadDataMart" name="formUploadDataMart" action="../servlet/AdapterHTTP"  ENCTYPE="multipart/form-data" method="post">
	<input type="hidden" name="ACTION_NAME" value="UPLOAD_MART_ACTION"/>
	
	<tr class="portlet-font">
		<td colspan="4">
		<table>
			<tr>
				<td width="25%">
				</td>
				<td width="25%">
				</td>
				<td width="25%">
				</td>
			</tr>
			<tr>
				<td width="25%">
				</td>
				<td width="50%">
				
				<table  class="qbe-table">
  	 				<thead>
  	 					<tr>
  	 						<td colspan="4" class='portlet-section-header' style='vertical-align:middle;'>
  	 							Uplod Nuovo Datamart
  	 						</td>
  	 					</tr>
  	 				</thead>
  	 				<tbody>
  	 					<tr class="portlet-font">
  	 						<td colspan="4" class="portlet-section-body">
  	 						</td>
  	 					</tr>
  	 					<tr class="portlet-font">
  	 						<td width="20%" class="portlet-section-body">
  	 						</td>
  	 						<td width="30%" class="portlet-section-body">
  	 							Nome Datamart
  	 						</td>
  	 						<td width="30%" class="portlet-section-body">
  	 							<input type="text" name="dmPath" value=""/>
  	 						</td>
  	 						<td width="20%" class="portlet-section-body">
  	 						</td>
  	 					</tr>
  	 					<tr class="portlet-font">
  	 						<td colspan="4" class="portlet-section-body">
  	 						</td>
  	 					</tr>
  	 					<tr class="portlet-font">
  	 						<td width="20%" class="portlet-section-body">
  	 						</td>
  	 						<td width="30%" class="portlet-section-body">
  	 							Jar File
  	 						</td>
  	 						<td width="30%" class="portlet-section-body">
  	 							<input type="file"  name="jarFile"/>
  	 						</td>
  	 						<td width="20%" class="portlet-section-body">
  	 						</td>
  	 					</tr>
  	 					<tr class="portlet-font">
  	 						<td colspan="4" class="portlet-section-body">
  	 						
  	 						</td>
  	 					</tr>
  	 					
  	 					<tr class="portlet-font">
  	 						<td width="20%" class="portlet-section-body">
  	 						</td>
  	 						<td colspan="2" class="portlet-section-body" align="center">
  	 							<input type="submit" value="Upload" name="Upload" title="Upload"/>
   	 						</td>
  	 						
  	 						<td width="20%" class="portlet-section-body">
  	 						</td>
  	 					</tr>
  	 				</tbody>
  	 		</table>
  	 		</td>
  	 		<td width="25%">
			</td>
		</tr>
		<tr>
				<td width="25%">
				</td>
				<td width="25%">
				</td>
				<td width="25%">
				</td>
		</tr>
		</table>
	</td>
	</tr>
</form>				

</table>   
</html>
