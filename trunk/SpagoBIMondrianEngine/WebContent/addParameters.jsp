<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp" %>
<%@ taglib uri="http://www.tonbeller.com/wcf" prefix="wcf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%@page import="com.tonbeller.jpivot.olap.model.OlapModel"%>
<%@page import="com.tonbeller.jpivot.olap.navi.MdxQuery"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>

<%@page import="org.apache.commons.validator.GenericValidator"%>
<html>
<head>
  <title>Mdx query edit</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <link rel="stylesheet" type="text/css" href="jpivot/table/mdxtable.css">
  <link rel="stylesheet" type="text/css" href="jpivot/navi/mdxnavi.css">
  <link rel="stylesheet" type="text/css" href="wcf/form/xform.css">
  <link rel="stylesheet" type="text/css" href="wcf/table/xtable.css">
  <link rel="stylesheet" type="text/css" href="wcf/tree/xtree.css">
  <link rel="stylesheet" type="text/css" href="css/stili.css">
</head>

<body bgcolor=white lang="en">

<%
// retrieves the Mondrian query
OlapModel om = (OlapModel) session.getAttribute("query01");
MdxQuery query = (MdxQuery) om.getExtension("mdxQuery");
String mondrianQuery = query.getMdxQuery();

// retrieves the query with parameters from the form below
String queryWithParameters = request.getParameter("queryWithParameters");
// if it is not null the form below was submitted
if (queryWithParameters == null) {
	String initialQueryWithParameters = (String) session.getAttribute("initialQueryWithParameters");
	String initialMondrianQuery = (String) session.getAttribute("initialMondrianQuery");
	if (initialQueryWithParameters != null && initialMondrianQuery != null) {
		if (mondrianQuery.trim().equalsIgnoreCase(initialMondrianQuery.trim())) {
			// the initial Mondrian query was not modified
			queryWithParameters = initialQueryWithParameters;
		} else {
			// the initial Mondrian query was not modified
			queryWithParameters = mondrianQuery;
		}
	} else {
		queryWithParameters = mondrianQuery;
	}
}
// puts in session the queryWithParameters so the TemplateBean.saveTemplate can retrieve it
session.setAttribute("queryWithParameters", queryWithParameters);

String parameterName = request.getParameter("parameterName");
String parameterUrlName = request.getParameter("parameterUrlName");
HashMap parameters = (HashMap) session.getAttribute("parameters");
if (parameters == null) {
	parameters = new HashMap();
	session.setAttribute("parameters", parameters);
}
String action = request.getParameter("action");
if (action != null && action.trim().equalsIgnoreCase("addParameter")) {
	String ALPHANUMERIC_STRING_REGEXP="^([a-zA-Z0-9\\s\\-\\_])*$";
	int maxLength = 30;
	if (GenericValidator.isBlankOrNull(parameterName) || GenericValidator.isBlankOrNull(parameterUrlName)) {
		%>
		<span style="font-family: Verdana,Geneva,Arial,Helvetica,sans-serif;color: red;font-size: 8pt;font-weight: bold;">
		ERROR: Missing parameter name or parameter url name!!
		</span>
		<%
	} else if (!GenericValidator.maxLength(parameterName, maxLength) 
			|| !GenericValidator.maxLength(parameterUrlName, maxLength)) {
		%>
		<span style="font-family: Verdana,Geneva,Arial,Helvetica,sans-serif;color: red;font-size: 8pt;font-weight: bold;">
		ERROR: Parameter name and parameter url name must not exceed <%=maxLength%> characters!!
		</span>
		<%
	} else if (!GenericValidator.matchRegexp(parameterName, ALPHANUMERIC_STRING_REGEXP) 
			|| !GenericValidator.matchRegexp(parameterUrlName, ALPHANUMERIC_STRING_REGEXP)) {
		%>
		<span style="font-family: Verdana,Geneva,Arial,Helvetica,sans-serif;color: red;font-size: 8pt;font-weight: bold;">
		ERROR: Parameter name and parameter url name must be aplhanumeric!!
		</span>
		<%
	} else {
		parameterName = parameterName.trim();
		parameterUrlName = parameterUrlName.trim();
		parameters.put(parameterName, parameterUrlName);
		parameterName = null;
		parameterUrlName = null;
	}
}

if (action != null && action.trim().equalsIgnoreCase("deleteParameter")) {
	if (parameterName != null && parameters.containsKey(parameterName)) {
		parameters.remove(parameterName);
		parameterName = null;
	}
}
%>

<form action="addParameters.jsp" method="post" name="addParametersForm" id="addParametersForm">
	<wcf:form id="saveTemplateForm01" xmlUri="/WEB-INF/jpivot/table/saveTemplateTable.xml" model="#{saveTemplate01}" visible="false"/>
	<wcf:toolbar id="toolbar02" bundle="com.tonbeller.jpivot.toolbar.resources">
		<wcf:scriptbutton id="saveTemplate" tooltip="toolb.saveTemplate" img="save" model="#{saveTemplateForm01.visible}"/>
	</wcf:toolbar>
	<p>
	<wcf:render ref="toolbar02" xslUri="/WEB-INF/jpivot/toolbar/htoolbar.xsl" xslCache="true"/>
	<p>
	<wcf:render ref="saveTemplateForm01" xslUri="/WEB-INF/wcf/wcf.xsl" xslCache="true"/>
	<p>
	<span style="font-family: Verdana,Geneva,Arial,Helvetica,sans-serif;color: #074B88;font-size: 8pt;">
	<b>Type parameters in the query below:</b>
	<br>
	<textarea style="width:42%;height:100;" name="queryWithParameters" /><%=queryWithParameters%></textarea>
	<p>
	<b>Type name and url name for each parameter inserted above:</b>
	<br>
	<table cellpadding="5" cellspacing="0" width="42%" style="border:1px solid #7f9db9;font-family: Verdana,Geneva,Arial,Helvetica,sans-serif;color: #074B88;font-size: 8pt;">
		<tr>
			<td style="width: 45%;">Insert parameter name:</td>
			<td style="width: 45%;"><input type="text" name="parameterName" value="<%=(parameterName != null) ? parameterName : ""%>" /></td>
			<td rowspan="2" align="center" style="width: 10%;">
				<input type="image" title="Add parameter" alt="Add parameter" 
						name="action" value="addParameter"
						src="jpivot/table/drill-position-expand.gif" />
			</td>
		</tr>
		<tr>
			<td>Insert parameter url name:</td>
			<td><input type="text" name="parameterUrlName" value="<%=(parameterUrlName != null) ? parameterUrlName : ""%>" /></td>
		</tr>
	</table>
	</span>
</form>
<p>
<%
if (parameters.size() > 0) {
	%>
	<span style="font-family: Verdana,Geneva,Arial,Helvetica,sans-serif;color: #074B88;font-size: 8pt;">
	<b>Defined parameters:</b>
	<br>
	<form action="addParameters.jsp" method="post" name="deleteParametersForm" id="deleteParametersForm">
		<input type="hidden" name="action" value="deleteParameter" />
		<input type="hidden" name="queryWithParameters" value="<%=queryWithParameters%>" />
		
		<table cellpadding="5" cellspacing="0" width="42%" style="border:1px solid #7f9db9;font-family: Verdana,Geneva,Arial,Helvetica,sans-serif;color: #074B88;font-size: 8pt;">
			<tr>
				<th style="background-color: #DEE3EF;color: Black;text-align: left;width: 45%;">Parameter name</th>
				<th style="background-color: #DEE3EF;color: Black;text-align: left;width: 45%;">Parameter url name</th>
				<th style="background-color: #DEE3EF;color: Black;text-align: left;width: 10%;">&nbsp;</th>
			</tr>
			<%
			Set keys = parameters.keySet();
			Iterator keyIt = keys.iterator();
			while (keyIt.hasNext()) {
				String aParameterName = (String) keyIt.next();
				String aParameterUrlName = (String) parameters.get(aParameterName);
				%>
				<tr>
					<td><%=aParameterName%></td>
					<td><%=aParameterUrlName%></td>
					<td align="center">
						<input type="image" title="Remove parameter" alt="Remove parameter" 
							name="parameterName" value="<%=aParameterName%>"
							src="jpivot/table/drill-position-collapse.gif" />
					</td>
				</tr>
				<%
			}
			%>
		</table>
	</form>
	</span>
	<%
}
%>
</body>
</html>