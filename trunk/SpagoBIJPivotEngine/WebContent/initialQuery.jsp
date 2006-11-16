<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp" %>
<%@ taglib uri="http://www.tonbeller.com/wcf" prefix="wcf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%@page import="mondrian.olap.MondrianDef"%>
<%@page import="mondrian.olap.MondrianDef.Cube"%>
<%@page import="mondrian.olap.MondrianDef.CubeDimension"%>
<%@page import="com.tonbeller.jpivot.olap.model.OlapModel"%>
<%@page import="it.eng.spagobi.bean.TemplateBean"%>
<%@page import="com.tonbeller.wcf.form.FormComponent"%>
<%@page import="org.dom4j.Document"%>
<%@page import="org.dom4j.Node"%>
<html>
<head>
  <title>Initial query</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <link rel="stylesheet" type="text/css" href="jpivot/table/mdxtable.css">
  <link rel="stylesheet" type="text/css" href="jpivot/navi/mdxnavi.css">
  <link rel="stylesheet" type="text/css" href="wcf/form/xform.css">
  <link rel="stylesheet" type="text/css" href="wcf/table/xtable.css">
  <link rel="stylesheet" type="text/css" href="wcf/tree/xtree.css">
  <link rel="stylesheet" type="text/css" href="css/stili.css">
</head>
<body bgcolor=white lang="en">

<form action="initialQuery.jsp" method="post">

<%
TemplateBean templateBean = (TemplateBean) session.getAttribute("saveTemplate01");
if (templateBean == null) {
	templateBean = new TemplateBean();
	session.setAttribute("saveTemplate01", templateBean);
}
Object formObj = session.getAttribute("saveTemplateForm01");
if (formObj != null) {
	FormComponent form = (FormComponent) formObj;
	form.setBean(templateBean);
}	
OlapModel om = (OlapModel) session.getAttribute("query01");
if (om == null) {
	String connection = request.getParameter("connection");
	org.dom4j.io.SAXReader readerConFile = new org.dom4j.io.SAXReader();
	Document documentConFile = readerConFile.read(getClass().getResourceAsStream("/engine-config.xml"));
	Node connectionDef = documentConFile.selectSingleNode("//ENGINE-CONFIGURATION/CONNECTIONS-CONFIGURATION/CONNECTION[@name='" + connection + "'");
	String catalogUri = request.getParameter("catalogUri");
	// puts the catalogUri in session for TemplateBean.saveTemplate() method
	session.setAttribute("catalogUri", catalogUri);
	String cubeName = request.getParameter("cube");
	String[] dimensions = request.getParameterValues("dimensions");
	String[] measures = request.getParameterValues("measures");
	String mdxQuery = "select {[Measures].[";
	for (int i = 0; i < measures.length; i++) {
		mdxQuery += measures[i] + "]";
		if (i < measures.length - 1) {
			mdxQuery += ", [Measures].[";
		} else {
			mdxQuery += "} on columns,\n";
		}
	}
	mdxQuery += "{([";
	for (int i = 0; i < dimensions.length; i++) {
		mdxQuery += dimensions[i] + "]";
		if (i < dimensions.length - 1) {
			mdxQuery += ", [";
		} else {
			mdxQuery += ")} on rows\n";
		}
	}
	mdxQuery += "from [" + cubeName + "]";
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
}
%>

<%-- include query and title, so this jsp may be used with different queries --%>
<%--
<wcf:include id="include01" httpParam="query" prefix="/WEB-INF/queries/" suffix=".jsp"/>
<c:if test="${query01 == null}">
  <jsp:forward page="/index.jsp"/>
</c:if>
--%>

<%-- define table, navigator and forms --%>
<jp:table id="table01" query="#{query01}"/>
<jp:navigator id="navi01" query="#{query01}" visible="false"/>
<wcf:form id="mdxedit01" xmlUri="/WEB-INF/jpivot/table/mdxedit.xml" model="#{query01}" visible="false"/>
<wcf:form id="sortform01" xmlUri="/WEB-INF/jpivot/table/sortform.xml" model="#{table01}" visible="false"/>
<wcf:form id="saveTemplateForm01" xmlUri="/WEB-INF/jpivot/table/saveTemplateTable.xml" model="#{saveTemplate01}" visible="false"/>

<jp:print id="print01"/>
<wcf:form id="printform01" xmlUri="/WEB-INF/jpivot/print/printpropertiesform.xml" model="#{print01}" visible="false"/>

<jp:chart id="chart01" query="#{query01}" visible="false"/>
<wcf:form id="chartform01" xmlUri="/WEB-INF/jpivot/chart/chartpropertiesform.xml" model="#{chart01}" visible="false"/>
<wcf:table id="query01.drillthroughtable" visible="false" selmode="none" editable="true"/>

<h2><c:out value="${title01}"/></h2>

<%-- define a toolbar --%>
<wcf:toolbar id="toolbar01" bundle="com.tonbeller.jpivot.toolbar.resources">
  <wcf:scriptbutton id="cubeNaviButton" tooltip="toolb.cube" img="cube" model="#{navi01.visible}"/>
  <wcf:scriptbutton id="mdxEditButton" tooltip="toolb.mdx.edit" img="mdx-edit" model="#{mdxedit01.visible}"/>
  <wcf:scriptbutton id="sortConfigButton" tooltip="toolb.table.config" img="sort-asc" model="#{sortform01.visible}"/>
  <wcf:scriptbutton id="saveTemplate" tooltip="toolb.saveTemplate" img="save" model="#{saveTemplateForm01.visible}"/>
  <wcf:separator/>
  <wcf:scriptbutton id="levelStyle" tooltip="toolb.level.style" img="level-style" model="#{table01.extensions.axisStyle.levelStyle}"/>
  <wcf:scriptbutton id="hideSpans" tooltip="toolb.hide.spans" img="hide-spans" model="#{table01.extensions.axisStyle.hideSpans}"/>
  <wcf:scriptbutton id="propertiesButton" tooltip="toolb.properties"  img="properties" model="#{table01.rowAxisBuilder.axisConfig.propertyConfig.showProperties}"/>
  <wcf:scriptbutton id="nonEmpty" tooltip="toolb.non.empty" img="non-empty" model="#{table01.extensions.nonEmpty.buttonPressed}"/>
  <wcf:scriptbutton id="swapAxes" tooltip="toolb.swap.axes"  img="swap-axes" model="#{table01.extensions.swapAxes.buttonPressed}"/>
  <wcf:separator/>
  <wcf:scriptbutton model="#{table01.extensions.drillMember.enabled}"	 tooltip="toolb.navi.member" radioGroup="navi" id="drillMember"   img="navi-member"/>
  <wcf:scriptbutton model="#{table01.extensions.drillPosition.enabled}" tooltip="toolb.navi.position" radioGroup="navi" id="drillPosition" img="navi-position"/>
  <wcf:scriptbutton model="#{table01.extensions.drillReplace.enabled}"	 tooltip="toolb.navi.replace" radioGroup="navi" id="drillReplace"  img="navi-replace"/>
  <wcf:scriptbutton model="#{table01.extensions.drillThrough.enabled}"  tooltip="toolb.navi.drillthru" id="drillThrough01"  img="navi-through"/>
  <wcf:separator/>
  <wcf:scriptbutton id="chartButton01" tooltip="toolb.chart" img="chart" model="#{chart01.visible}"/>
  <wcf:scriptbutton id="chartPropertiesButton01" tooltip="toolb.chart.config" img="chart-config" model="#{chartform01.visible}"/>
  <wcf:separator/>
  <wcf:scriptbutton id="printPropertiesButton01" tooltip="toolb.print.config" img="print-config" model="#{printform01.visible}"/>
  <wcf:imgbutton id="printpdf" tooltip="toolb.print" img="print" href="./Print?cube=01&type=1"/>
  <wcf:imgbutton id="printxls" tooltip="toolb.excel" img="excel" href="./Print?cube=01&type=0"/>
</wcf:toolbar>

<%-- render toolbar --%>
<wcf:render ref="toolbar01" xslUri="/WEB-INF/jpivot/toolbar/htoolbar.xsl" xslCache="true"/>
<p>


<wcf:render ref="saveTemplateForm01" xslUri="/WEB-INF/wcf/wcf.xsl" xslCache="true"/>

<%-- if there was an overflow, show error message --%>
<c:if test="${query01.result.overflowOccured}">
  <p>
  <strong style="color:red">Resultset overflow occured</strong>
  <p>
</c:if>

<%-- render navigator --%>
<wcf:render ref="navi01" xslUri="/WEB-INF/jpivot/navi/navigator.xsl" xslCache="true"/>

<%-- edit mdx --%>
<c:if test="${mdxedit01.visible}">
  <h3>MDX Query Editor</h3>
  <wcf:render ref="mdxedit01" xslUri="/WEB-INF/wcf/wcf.xsl" xslCache="true"/>
</c:if>

<%-- sort properties --%>
<wcf:render ref="sortform01" xslUri="/WEB-INF/wcf/wcf.xsl" xslCache="true"/>

<%-- chart properties --%>
<wcf:render ref="chartform01" xslUri="/WEB-INF/wcf/wcf.xsl" xslCache="true"/>

<%-- print properties --%>
<wcf:render ref="printform01" xslUri="/WEB-INF/wcf/wcf.xsl" xslCache="true"/>

<!-- render the table -->
<p>
<wcf:render ref="table01" xslUri="/WEB-INF/jpivot/table/mdxtable.xsl" xslCache="true"/>
<p>
Slicer:
<wcf:render ref="table01" xslUri="/WEB-INF/jpivot/table/mdxslicer.xsl" xslCache="true"/>

<p>
<!-- drill through table -->
<wcf:render ref="query01.drillthroughtable" xslUri="/WEB-INF/wcf/wcf.xsl" xslCache="true"/>

<p>
<!-- render chart -->
<wcf:render ref="chart01" xslUri="/WEB-INF/jpivot/chart/chart.xsl" xslCache="true"/>

<p>
<%-- 
<a href="index.jsp">back to index</a>
--%>
</form>
</body>
</html>