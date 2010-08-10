<%@page import="it.eng.spago.configuration.ConfigSingleton"%>
<%@page import="java.util.Iterator"%>
<%@page import="it.eng.qbe.utility.LocalFileSystemDataMartModelRetriever"%>
<%@page import="java.util.List"%>
<%@page import="java.io.File"%>
<%@page import="it.eng.qbe.utility.FileUtils"%>
<%@ page import="it.eng.spago.base.*"%>
<%@page import="it.eng.spago.security.IEngUserProfile"%>

<link rel="stylesheet" href ="../css/spagobi.css" type="text/css"/>
<link rel="stylesheet" href ="../css/jsr168.css" type="text/css"/>
<link rel="stylesheet" href ="../css/external.css" type="text/css"/>
<link rel="styleSheet" href ="../css/dtree.css" type="text/css" />	

<% File contextDir = new File(ConfigSingleton.getInstance().getRootPath());
	List allMarts = LocalFileSystemDataMartModelRetriever.getAllDataMartPath(contextDir); 

   Iterator it = allMarts.iterator();
   
   RequestContainer requestContainer = RequestContainerAccess.getRequestContainer(request);
   SessionContainer sessionContainer = requestContainer.getSessionContainer();
   
   String authenticatedStr = (String)sessionContainer.getAttribute("AUTHENTICATED");
   boolean authenticated = (authenticatedStr == null || (authenticatedStr != null && authenticatedStr.equalsIgnoreCase("TRUE")) );
   sessionContainer.delAttribute("AUTHENTICATED");
   
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

<%
if(!authenticated) {
%>
<P><H2><center>Authentication failed !!!<br>Wrong user name or password</center></H2>
<p>
<form id="form1" name="form1" action="../servlet/AdapterHTTP" method="post">
		<input type="hidden" name="ACTION_NAME" value="LOGIN_ACTION"/>
		<input type="hidden" name="NEW_SESSION" value="TRUE"/>
		
		<table width="100%">
			<tr>
				<td colspan="3"/>
				</td>
			</tr>
			<tr>
				<td colspan="3"/>
				</td>
			</tr>
			<tr>
				<td colspan="3"/>
				</td>
			</tr>
			<tr>
				<td colspan="3"/>
				</td>
			</tr>
			<tr>
				<td colspan="3"/>
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
  	 							Autenticazione
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
  	 							UserID
  	 						</td>
  	 						<td width="30%" class="portlet-section-body">
  	 							<input name="userID" type="text" value=""/>
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
  	 							Password
  	 						</td>
  	 						<td width="30%" class="portlet-section-body">
  	 							<input name="password" type="password" value=""/>
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
  	 							<input type="submit" value="Login" title="Login" name="Login"/>
   	 						</td>
  	 						
  	 						<td width="20%" class="portlet-section-body">
  	 						</td>
  	 					</tr>
  	 				</tbody>
  	 	</table>
  	 	</td>	
  	 	<!-- Tabella Interna -->
  	 	<td width="25%">
		</td>
		</tr>
		<tr>
				<td colspan="3"/>
				</td>
		</tr>
		</table>	
	</form>
<%} else { %>

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
   		int countDatamartVisualized = 0;
		while(it.hasNext()){
   		 	rowClass = (alternate) ? "portlet-section-alternate" : "portlet-section-body";
         	alternate = !alternate; 
	   		dmPath = (String)it.next();
	   		completePath = new File(qbeDataMartDir  + System.getProperty("file.separator") + dmPath).toString();
	   		link = "../servlet/AdapterHTTP?ACTION_NAME=DETAIL_DATA_MART_ACTION&PATH="+dmPath;
	   		linkDelete = "../servlet/AdapterHTTP?ACTION_NAME=DELETE_DATA_MART_ACTION&PATH="+dmPath;
	   		
		%>
	   <% if (userProfile.hasRole("QbeAdmin") || userProfile.canAccessResource(dmPath)){ 
	   	      countDatamartVisualized++;				%>
	   <tr class='portlet-font'>
	   		<td class="<%=rowClass%>"><%= dmPath %></td>
	   		<td class="<%=rowClass%>"><%= completePath %></td>
	   		<td class="<%=rowClass%>">
	   		<% if ((userProfile != null) && Utils.isUserAble(userProfile, "ExecuteDatamart")){ %>
	   			<a href="<%=link%>">
	   				<img alt="Datamart Detail" title="Datamart Detail" src="../img/detail.gif"/>
	   			</a>
	   		<%}else{ %>
	   			&nbsp;
	   		<%} %>
	   		</td>
	   		<td class="<%=rowClass%>">
	   		<% if ((userProfile != null) && Utils.isUserAble(userProfile, "DeleteDatamart")){ %>
	   			<a href="<%=linkDelete%>">
	   				<img alt="Delete Datamart" title="Datamart Delete" src="../img/delete.gif"/>
	   			</a>
	   		<%}else{ %>
	   			&nbsp;
	   		<%} %>
	   		</td>
	   </tr>
	   <% } %>
	   <% if (countDatamartVisualized == 0){ %>
	   		<tr class='portlet-font'>
	   			<td class="<%=rowClass%>" colspan="4"> No datamart found or enabled for user </td>
	   		</tr>
	   <% } %>
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
<% if ((userProfile != null) && (userProfile.hasRole("QbeDev") || userProfile.hasRole("QbeAdmin"))){ %>
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
<% } %>
</table>   
<%}%>
</html>
