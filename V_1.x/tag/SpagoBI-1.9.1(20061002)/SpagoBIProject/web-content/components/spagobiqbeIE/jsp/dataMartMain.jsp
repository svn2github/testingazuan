<%@ page contentType="text/html; charset=ISO-8859-1"
	language="java"  %>



<%@include file="../jsp/qbe_base.jsp"%>  
<%
it.eng.qbe.model.DataMartModel dm = (it.eng.qbe.model.DataMartModel)sessionContainer.getAttribute("dataMartModel"); 
it.eng.qbe.wizard.ISingleDataMartWizardObject aWizardObject = null;

java.util.List queries = dm.getQueries();
String url = "#";
%>

<% if (qbeMode.equalsIgnoreCase("WEB")){ %> 
<body>
<%}%>
	<TABLE CELLSPACING = "1" CELLPADDING = "0" BORDER = "0" WIDTH = "100%">
		<TR HEIGHT = "10">
			<TD WIDTH = "5%"></TD>
			<TD WIDTH = "15%"></TD>
			<TD WIDTH = "80%"></TD>
		</TR>
		<TR>
			 <td colspan="3"> &nbsp; </td>
		</TR>
		<TR>
			<TD></TD>
			<TD CLASS = "TESTATA" colspan="2">
							DataMart : <%= dm.getPath() %> 
			</TD>
		</TR>
		<TR>
			 <td colspan="3"> &nbsp; </td>
		</TR>
		<TR>
			<TD></TD>
			<TD CLASS = "TESTATA" colspan="2">
				DataSource : <%= dm.getJndiDataSourceName() %> 
			</TD>
		</TR>
		<TR>
			 <td colspan="3"> &nbsp; </td>
		</TR>
		<TR>
			<TD></TD>
			<TD CLASS = "TESTATA" colspan="2">
				DataSource : <%= dm.getDialect() %> 
			</TD>
		</TR>
		<TR>
			 <td colspan="3"> &nbsp; </td>
		</TR>
		<TR>
			 <TD>&nbsp;</TD>
			 <TD CLASS = "TESTATA" colspan="2">
			 	<% String urlNewQueryComposition = null; 
			 	   java.util.Map params = new java.util.HashMap();
			 	   params.put("ACTION_NAME", "INIT_WIZARD_ACTION");
			 	   urlNewQueryComposition = qbeUrl.getUrl(request, params);
			 	%>
				<a href="<%=urlNewQueryComposition %>">NEW QUERY COMPOSITION</a>
			 </TD>
		</TR>
		<TR>
			 <td colspan="3"> &nbsp; </td>
		</TR>
		<TR>
			<TD>&nbsp;</TD>
			<TD CLASS = "TESTATA" colspan="2">
				SAVED QUERIES
			</TD>
		</TR>
		<TR>
			 <td colspan="3"> &nbsp; </td>
		</TR>
<%   java.util.Iterator it = queries.iterator();
	 while(it.hasNext()){
		 aWizardObject = (it.eng.qbe.wizard.ISingleDataMartWizardObject)it.next();
		 params.clear();
		 params.put("ACTION_NAME","INIT_WIZARD_ACTION");
		 params.put("queryId", aWizardObject.getQueryId());
		 url =qbeUrl.getUrl(request, params);
%>
		<TR>
			<TD colspan="2">&nbsp;</TD>
			<TD>
				<a href="<%=url%>"><%=aWizardObject.getQueryId()%> - <%=(aWizardObject.getDescription() != null ? aWizardObject.getDescription() : "")%></a>
			</TD>
		</TR>
		<TR>
			 <td colspan="3"> &nbsp; </td>
		</TR>
<%   } %>
	</TABLE>		
		
<% if (qbeMode.equalsIgnoreCase("WEB")){ %> 
</body>
<%}%>
<%@include file="../jsp/qbefooter.jsp" %>

