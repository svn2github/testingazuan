<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="mondrian.olap.MondrianDef"%>
<%@page import="mondrian.olap.MondrianDef.Schema"%>
<%@page import="mondrian.olap.MondrianDef.Cube"%>
<%@page import="org.eigenbase.xom.XOMUtil"%>
<%@page import="org.eigenbase.xom.Parser"%>
<%@page import="org.dom4j.io.SAXReader"%>
<%@page import="org.dom4j.Document"%>
<%@page import="org.dom4j.Node"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.net.URL"%>
<%@page import="mondrian.olap.MondrianDef.VirtualCube"%>
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
//cleans objects from session if necessary
String newSession = request.getParameter("new_session");
if (newSession != null && newSession.trim().equalsIgnoreCase("true")) {
	SessionObjectRemoval.removeSessionObjects(session);
}
%>

<form action="initialQueryCreator.jsp" method="post" name="initialQueryForm" id="initialQueryForm">
<input type="hidden" name="action" id="action" value="" />
<%
//puts in session the spagobi content repository servlet url 
//and the document path for TemplateBean.saveTemplate method
String biobjectPath = request.getParameter("biobject_path");
if (biobjectPath != null) session.setAttribute("biobject_path", biobjectPath);
String spagobiurl = request.getParameter("spagobiurl");
if (spagobiurl != null) session.setAttribute("spagobiurl", spagobiurl);

String action = request.getParameter("action");
if (action != null && !action.equals("")) {
	if (action.equalsIgnoreCase("selectSchema")) {
		session.removeAttribute("MondrianCubes");
		session.removeAttribute("MondrianVirtualCubes");
	}
}

List schemas = (List) session.getAttribute("schemas");
List connections = (List) session.getAttribute("connections");
boolean schemaWasSelected = true;
if (schemas == null & connections == null) {
	schemaWasSelected = false;
	SAXReader readerConfigFile = new SAXReader();
	Document documentConfigFile = readerConfigFile.read(getClass().getResourceAsStream("/engine-config.xml"));
	schemas = documentConfigFile.selectNodes("//ENGINE-CONFIGURATION/SCHEMAS/SCHEMA");
	connections = documentConfigFile.selectNodes("//ENGINE-CONFIGURATION/CONNECTIONS-CONFIGURATION/CONNECTION");
	session.setAttribute("schemas", schemas);
	session.setAttribute("connections", connections);
}
if (schemas == null || schemas.size() == 0) {
	out.write("No schemas defined in engine-config.xml file.");
	return;
}
if (connections == null || connections.size() == 0) {
	out.write("No connections defined in engine-config.xml file.");
	return;
}

%>
Select connection:
<select name="connection" id="connection" onchange="document.getElementById('initialQueryForm').submit()">
<%
String selectedConnection = request.getParameter("connection");
Iterator connectionsIt = connections.iterator();
Node selectedConnectionNode = null;
while (connectionsIt.hasNext()) {
	Node aConnection = (Node) connectionsIt.next();
	String aConnectionName = aConnection.valueOf("@name");
	String isConnectionSelected = "";
	if (selectedConnection == null) {
		if (aConnection.valueOf("@isDefault").trim().equalsIgnoreCase("true")) {
			selectedConnectionNode = aConnection;
			isConnectionSelected = "selected='selected'";
		}
	} else {
		if (aConnectionName.equalsIgnoreCase(selectedConnection)) {
			selectedConnectionNode = aConnection;
			isConnectionSelected = "selected='selected'";
		}
	}
	%>
	<option value="<%=aConnectionName%>" <%=isConnectionSelected%>><%=aConnectionName%></option>
	<%
}
%>
</select>
<br>
<br>
<%

%>
Select schema:
<select name="schema" id="schema" onchange="document.getElementById('action').value='selectSchema';document.getElementById('initialQueryForm').submit()">
<%
if (!schemaWasSelected) {
	%>
	<option value="" selected="selected">&nbsp;</option>
	<%
}

String selectedSchema = request.getParameter("schema");
Iterator it = schemas.iterator();
Node selectedSchemaNode = null;
while (it.hasNext()) {
	Node aSchema = (Node) it.next();
	String aSchemaName = aSchema.valueOf("@name");
	String isSchemaSelected = "";
	if (aSchemaName.equalsIgnoreCase(selectedSchema)) {
		selectedSchemaNode = aSchema;
		isSchemaSelected = "selected='selected'";
	}
	%>
	<option value="<%=aSchemaName%>" <%=isSchemaSelected%>><%=aSchemaName%></option>
	<%
}
%>
</select>
<br>
<br>
<%

if (selectedSchemaNode != null) {
	String catalogUri = selectedSchemaNode.valueOf("@catalogUri");
	MondrianDef.Cube[] cubes = (MondrianDef.Cube[]) session.getAttribute("MondrianCubes");
	MondrianDef.VirtualCube[] virtualcubes = (MondrianDef.VirtualCube[]) 
								session.getAttribute("MondrianVirtualCubes");
	boolean cubeWasSelected = true;
	if (cubes == null) {
		cubeWasSelected = false;
		Parser xmlParser = XOMUtil.createDefaultParser();
		URL catalogURL = this.getServletContext().getResource(catalogUri);
		MondrianDef.Schema schema = new MondrianDef.Schema(xmlParser.parse(catalogURL));
		cubes = schema.cubes;
		virtualcubes = schema.virtualCubes;
		session.setAttribute("MondrianCubes", cubes);
		session.setAttribute("MondrianVirtualCubes", virtualcubes);
	}
	if ((cubes == null || cubes.length == 0) && (virtualcubes == null || virtualcubes.length == 0)) {
		out.write("No cubes defined in " + catalogUri + " file.");
		return;
	}
	%>
	Select cube:
	<select name="cube" id="cube" onchange="document.getElementById('initialQueryForm').submit()">
	<%
	if (!cubeWasSelected) {
		%>
		<option value="" selected="selected">&nbsp;</option>
		<%
	}
	String selectedCubeName = request.getParameter("cube");
	MondrianDef.Cube selectedCube = null;
	for (int i = 0; i < cubes.length; i++) {
		MondrianDef.Cube aCube = cubes[i];
		String isCubeSelected = "";
		if (cubeWasSelected && aCube.name.equalsIgnoreCase(selectedCubeName)) {
			selectedCube = aCube;
			isCubeSelected = "selected='selected'";
		}
		%>
		<option value="<%=aCube.name%>" <%=isCubeSelected%>><%=aCube.name%></option>
		<%
	}
	MondrianDef.VirtualCube selectedVirtualCube = null;
	for (int i = 0; i < virtualcubes.length; i++) {
		MondrianDef.VirtualCube aVirtualCube = virtualcubes[i];
		String isVirtualCubeSelected = "";
		if (aVirtualCube.name.equalsIgnoreCase(selectedCubeName)) {
			selectedVirtualCube = aVirtualCube;
			isVirtualCubeSelected = "selected='selected'";
		}
		%>
		<option value="<%=aVirtualCube.name%>" <%=isVirtualCubeSelected%>><%=aVirtualCube.name%></option>
		<%
	}
	%>
	</select>
	<br>
	<br>
	<%
	if (selectedCube != null || selectedVirtualCube != null) {
		%>
		</form>
		<form action="initialQuery.jsp" method="post">
			<input type="hidden" name="connection" 
				value="<%=selectedConnectionNode.valueOf("@name")%>"/>
			<input type="hidden" name="catalogUri" 
				value="<%=selectedSchemaNode.valueOf("@catalogUri")%>"/>
			<input type="hidden" name="cube" 
				value="<%=(selectedCube != null) ? selectedCube.name : selectedVirtualCube.name%>"/>
			<table>
				<tr>
					<td>
					Select dimensions:<br>
					<%
					if (selectedCube != null) {
						MondrianDef.CubeDimension[] dimensions = selectedCube.dimensions;
						for (int i = 0; i < dimensions.length; i++) {
							MondrianDef.CubeDimension aDimension = dimensions[i];
							%>
							<input type="checkbox" name="dimensions" id="dimensions" value="<%=aDimension.name%>" />
							<%=aDimension.name%>
							<br>
							<%
						}
					} else {
						MondrianDef.VirtualCubeDimension[] dimensions = selectedVirtualCube.dimensions;
						for (int i = 0; i < dimensions.length; i++) {
							MondrianDef.VirtualCubeDimension aDimension = dimensions[i];
							%>
							<input type="checkbox" name="dimensions" id="dimensions" value="<%=aDimension.name%>" />
							<%=aDimension.name%>
							<br>
							<%
						}
					}
					%>
					</td>
					<td>
					Select measures:<br>
					<%
					if (selectedCube != null) {
						MondrianDef.Measure[] measures = selectedCube.measures;
						for (int i = 0; i < measures.length; i++) {
							MondrianDef.Measure aMeasure = measures[i];
							%>
							<input type="checkbox" name="measures" id="measures" value="<%=aMeasure.name%>" />
							<%=aMeasure.name%>
							<br>
							<%
						}
					} else {
						MondrianDef.VirtualCubeMeasure[] measures = selectedVirtualCube.measures;
						for (int i = 0; i < measures.length; i++) {
							MondrianDef.VirtualCubeMeasure aMeasure = measures[i];
							String virtualCubeMeasureName = aMeasure.name;
							String temp = virtualCubeMeasureName.toLowerCase();
							if (virtualCubeMeasureName.startsWith("[Measures].[")) {
								virtualCubeMeasureName = virtualCubeMeasureName.substring(12, virtualCubeMeasureName.length() - 1);
							}
							%>
							<input type="checkbox" name="measures" id="measures" value="<%=virtualCubeMeasureName%>" />
							<%=virtualCubeMeasureName%>
							<br>
							<%
						}
					}
					%>
					</td>
				</tr>
			</table>
			<input type="submit" value="Ok" />
		<%
	}
}
%>
</form>
</body>
</html>