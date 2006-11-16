<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp" %>

<%@page import="org.dom4j.io.SAXReader"%>
<%@page import="org.dom4j.Document"%>
<%@page import="org.dom4j.Node"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="sun.misc.BASE64Decoder"%>
<%@page import="java.io.ByteArrayInputStream"%>
<%@page import="it.eng.spagobi.bean.TemplateBean"%>
<%@page import="it.eng.spagobi.util.SessionObjectRemoval"%>

<html>
<head>
  <title>Initial query creation</title>
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
// cleans objects from session if necessary
String newSession = request.getParameter("new_session");
if (newSession != null && newSession.trim().equalsIgnoreCase("true")) {
	SessionObjectRemoval.removeSessionObjects(session);
}

// puts in session the spagobi content repository servlet url 
// and the document path for TemplateBean.saveTemplate method
// and other objects avoiding unuseful http reuqest parameters
String biobjectPath = request.getParameter("biobject_path");
if (biobjectPath != null) session.setAttribute("biobject_path", biobjectPath);
String spagobiurl = request.getParameter("spagobiurl");
if (spagobiurl != null) session.setAttribute("spagobiurl", spagobiurl);
String templateName = request.getParameter("templateName");
if (templateName != null) session.setAttribute("templateName", templateName);
String template = request.getParameter("template");
if (template != null) session.setAttribute("template", template);

SAXReader readerConfigFile = new SAXReader();
Document documentConfigFile = readerConfigFile.read(getClass().getResourceAsStream("/engine-config.xml"));

String connection = request.getParameter("connection");

if (connection != null && !connection.trim().equals("")) {
	Node connectionDef = documentConfigFile.selectSingleNode("//ENGINE-CONFIGURATION/CONNECTIONS-CONFIGURATION/CONNECTION[@name='"+connection+"'");
	if (connectionDef == null) {
		out.write("Connection '" + connection + "' not defined in engine-config.xml file.");
		return;
	}
	// the connection is specified so proceed with query execution
	BASE64Decoder bASE64Decoder = new BASE64Decoder();
	String templateBase64Coded = (String) session.getAttribute("template");
	byte[] templateContent = bASE64Decoder.decodeBuffer(templateBase64Coded);
	ByteArrayInputStream is = new ByteArrayInputStream(templateContent);
	SAXReader reader = new SAXReader();
	Document document = reader.read(is);
	String mdxQuery = document.selectSingleNode("//olap/MDXquery").getStringValue();
	Node cube = document.selectSingleNode("//olap/cube");
	String catalogUri = cube.valueOf("@reference");
	TemplateBean templateBean = new TemplateBean();
	templateName = (String) session.getAttribute("templateName");
	templateBean.setTemplateName(templateName);
	session.setAttribute("saveTemplate01", templateBean);
	String jndi = connectionDef.valueOf("@isJNDI");
	if (jndi.equalsIgnoreCase("true")) {
	    String iniCont = connectionDef.valueOf("@initialContext");
	    String resName = connectionDef.valueOf("@resourceName");
	    String connectionStr = "Provider=mondrian;DataSource="+iniCont+"/"+resName+";Catalog="+catalogUri+";";
    	%>
		<jp:mondrianQuery id="query01" dataSource="<%=resName%>"  catalogUri="<%=catalogUri%>">
			<%=mdxQuery%>
		</jp:mondrianQuery>
		<%
	} else {
		String driver = connectionDef.valueOf("@driver");
		String url = connectionDef.valueOf("@jdbcUrl");
		String usr = connectionDef.valueOf("@user");
		String pwd = connectionDef.valueOf("@password");
		String connectionStr = "Provider=mondrian;JdbcDrivers="+driver+";Jdbc="+url+";JdbcUser="+usr+";JdbcPassword="+pwd+";Catalog="+catalogUri+";";
		%>
	    <jp:mondrianQuery id="query01" jdbcDriver="<%=driver%>" jdbcUrl="<%=url%>" jdbcUser="<%=usr%>" jdbcPassword="<%=pwd%>" catalogUri="<%=catalogUri%>" >
			<%=mdxQuery%>
		</jp:mondrianQuery>	
		<%
	}
	%>
	<jsp:forward page="/initialQuery.jsp"/>
	<%
} else {
	%>
	<form action="chooseConnection.jsp" method="post" name="chooseConnectionForm" id="chooseConnectionForm">
		<%
		List connections = documentConfigFile.selectNodes("//ENGINE-CONFIGURATION/CONNECTIONS-CONFIGURATION/CONNECTION");
		if (connections == null || connections.size() == 0) {
			out.write("No connections defined in engine-config.xml file.");
			return;
		}
		%>
		Select connection:
		<select name="connection" id="connection" onchange="document.getElementById('chooseConnectionForm').submit()">
		<%
		Iterator connectionsIt = connections.iterator();
		Node selectedConnectionNode = null;
		while (connectionsIt.hasNext()) {
			Node aConnection = (Node) connectionsIt.next();
			String aConnectionName = aConnection.valueOf("@name");
			String isConnectionSelected = "";
			if (aConnection.valueOf("@isDefault").trim().equalsIgnoreCase("true")) {
				selectedConnectionNode = aConnection;
				isConnectionSelected = "selected='selected'";
			}
			%>
			<option value="<%=aConnectionName%>" <%=isConnectionSelected%>><%=aConnectionName%></option>
			<%
		}
		%>
		</select>
		<br>
		<input type="submit" value="Ok" />
	</form>
	<%
}
%>

</body>
</html>