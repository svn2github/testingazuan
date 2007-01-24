<%@ page contentType="text/html; charset=ISO-8859-1"
	language="java"  %>

 

<%@include file="../jsp/qbe_base.jsp"%>  
<%
it.eng.qbe.model.DataMartModel dm = (it.eng.qbe.model.DataMartModel)sessionContainer.getAttribute("dataMartModel"); 
 
String[] dialectJndiDs = Utils.getJndiDsDialectFromModel(dm);

String dialect = dialectJndiDs[0];
String jndiDs = dialectJndiDs[1];

//List allJndiDs = Utils.getAllJndiDS();
List allJndiDs = QbeConf.getInstance().getConnectionNames();


IEngUserProfile userProfile = (IEngUserProfile)sessionContainer.getPermanentContainer().getAttribute(IEngUserProfile.ENG_USER_PROFILE);
java.util.List queries = dm.getQueries();

IQueryPersister queryPersister = dm.getQueryPersister();
if (queryPersister instanceof LocalFileSystemQueryPersister){
	queries.addAll(((LocalFileSystemQueryPersister)queryPersister).getPrivateQueriesFor(dm,userProfile.getUserUniqueIdentifier().toString()));
}


%>

<% if (qbeMode.equalsIgnoreCase("WEB")){ %>  
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="it.eng.qbe.wizard.ISingleDataMartWizardObject"%>
<%@page import="it.eng.spago.security.IEngUserProfile"%>
<%@page import="it.eng.qbe.utility.IQueryPersister"%>
<%@page import="it.eng.qbe.utility.LocalFileSystemQueryPersister"%>
<body>
<%}%>
<script type="text/javascript">
	function setQueryIdAndSubmitForm(queryIdValue){
		var formEl = document.getElementById('form1');
		var queryIDInputField = document.getElementById('queryId');
		queryIDInputField.value = queryIdValue;
		
		formEl.submit();
	}
</script>
<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' 

		    style='vertical-align:middle;padding-left:5px;'>
			<%=qbeMsg.getMessage(requestContainer, "QBE.Title.DatamartDetail", bundle) %> - <%=dm.getPath()%>
		</td>
		<td class='header-button-column-portlet-section'>
					<a href="../servlet/AdapterHTTP?ACTION_NAME=NO_ACTION"> 
      						<img class='header-button-image-portlet-section' title='Back'
						src='<%=qbeUrl.conformStaticResourceLink(request,"../img/back.gif")%>' alt='Back To Datamart List' />
					</a>
		</td>		
	</tr>
</table>
<form id="form1" name="form1" action="<%=qbeUrl.getUrl(request, null) %>" method="post">
	<input type="hidden" name="ACTION_NAME" value="SELECT_DATAMART_AND_INIT_NEW_WIZARD_ACTION"/>
	<input type="hidden" name="PATH" value="<%=dm.getPath() %>"/>
	<input type="hidden" id="queryId" name="queryId" value="#"/>
	<table width="100%" class="qbe-font">
		<tr>
			<td width="5%">
			</td>
			<td width="20%">
			</td>
  	 		<td width="20%">
  	 			&nbsp;
  	 		</td>
  	 		<td width="45%" rowspan="5" align="left" valign="center">
				<input type="image" alt="<%= qbeMsg.getMessage(requestContainer,"QBE.NewComposition",bundle)%>" src="<%=qbeUrl.conformStaticResourceLink(request,"../img/newwiz.gif")%>"/>
			</td>
			<td width="5%">
			</td>
  	 	</tr>
  	 	<tr>
  	 		<td>
			</td>
  	 		<td>
				Data Source
			</td>
  	 		<td colspan="2" style="padding:1px;">
			    <select name="JNDI_DS" class="qbe-font"/>
			    		<% for (Iterator it=allJndiDs.iterator(); it.hasNext(); ){  
			    				String jndiDsTmp = (String)it.next(); 
			 			%>
			    				<option value="<%=jndiDsTmp%>"><%=jndiDsTmp%></option>
			    		<% } %>
  	 			</select>
  	 		</td>
  	 		<td>
  	 		</td>
  	 	</tr>
  	 	<tr>
  	 		<td colspan="5">
  	 			&nbsp;
			</td>
  	 	</tr>
  	 	<tr>
  	 		<td>
			</td>
  	 		<td>
				Dialect 
			</td>
  	 		<td colspan="2" style="padding:1px;">
  	 			<select name="DIALECT" class="qbe-font"/>
			    	<option value="org.hibernate.dialect.HSQLDialect">HSQL Dialect</option>
			    	<option value="org.hibernate.dialect.OracleDialect">Oracle</option>		
  	 				<option value="org.hibernate.dialect.Oracle9Dialect">Oracle9</option>
  	 				<option value="org.hibernate.dialect.MySQLDialect">MySQL</option>
  	 				<option value="org.hibernate.dialect.MySQLInnoDBDialect">MySQL InnoDB</option>
  	 				<option value="org.hibernate.dialect.PostgreSQLDialect">Postgres</option>
  	 			</select>
  	 			
  	 		</td>
  	 		<td>
  	 		</td>
  	 	</tr>
  	 	<tr>
  	 		<td colspan="5">
  	 			&nbsp;
			</td>
  	 	</tr>
  	 	<tr>
  	 		<td colspan="5">
  	 			&nbsp;
			</td>
  	 	</tr>
<% if (queries.size() > 0){ %>
  	 	<tr>
  	 		<!--  LISTA QUERY SALVATE PER QBE -->
  	 		<td>
  	 		</td>
  	 		<td colspan="3">
  	 			<table width="100%" class="qbe-table">
  	 				<thead>
  	 					<tr>
  	 						<td width="20%"  class='portlet-section-header' style='vertical-align:middle;'>
  	 							<%= qbeMsg.getMessage(requestContainer, "QBE.Web.QueryID",bundle) %>
  	 						</td>
  	 						<td width="50%"  class='portlet-section-header' style='vertical-align:middle;'>
  	 							<%= qbeMsg.getMessage(requestContainer, "QBE.Web.QueryDescritpion",bundle) %>
  	 						</td>
  	 						<td width="30%"  class='portlet-section-header' style='vertical-align:middle;'>
  	 							<%= qbeMsg.getMessage(requestContainer, "QBE.Web.QueryExecution",bundle) %>
  	 						</td>
  	 					</tr>
  	 				</thead>
  	 				<tbody>
  	 				<% 	String rowClass;
						boolean alternate = false;
						Iterator itQueries = queries.iterator();
						String querExecuteLink;
						ISingleDataMartWizardObject theQuery = null;
						String executeQueryUrl = null;
						while (itQueries.hasNext()){
					            rowClass = (alternate) ? "portlet-section-alternate" : "portlet-section-body";
					            alternate = !alternate;  
					            theQuery = (ISingleDataMartWizardObject)itQueries.next();
					%>
						   		<tr class='portlet-font'>
						   			<td class="<%=rowClass%>"><%= theQuery.getQueryId() %></td>
						   			<td class="<%=rowClass%>"><%= theQuery.getDescription() %></td>
						   			<td class="<%=rowClass%>" width="22px" height="22px">
						   				<a href="#" onclick="javascript:setQueryIdAndSubmitForm('<%=theQuery.getQueryId()%>')"/>
						   					<img title="Execute Query" alt="Execute Query" src="<%= qbeUrl.conformStaticResourceLink(request,"../img/exec.gif")%>"/>
						   				</a>
						   			</td>
						   		</tr>
					<% } %>
  	 				</tbody>
  	 			</table>
  	 		</td>
  	 		<td>
  	 		</td>
  	 		
  	 		
  	 	</tr>
<% } %>
</table>
</form>

<% if (qbeMode.equalsIgnoreCase("WEB")){ %> 
</body>
<%}%>
<%@include file="../jsp/qbefooter.jsp" %>

