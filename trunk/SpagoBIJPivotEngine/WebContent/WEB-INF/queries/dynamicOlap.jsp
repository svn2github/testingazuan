<%@ page session="true" 
         contentType="text/html; charset=ISO-8859-1" 
		 import="org.dom4j.Document,
				 org.dom4j.Node,
				 java.io.InputStreamReader,
				 com.thoughtworks.xstream.XStream,
				 it.eng.spagobi.bean.AnalysisBean,
				 it.eng.spagobi.util.SessionObjectRemoval,
				 it.eng.spagobi.utilities.SpagoBIAccessUtils,
				 it.eng.spagobi.bean.SaveAnalysisBean,
				 com.tonbeller.wcf.form.FormComponent" %>

<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%

SessionObjectRemoval.removeSessionObjects(session);

Node parameter = null;
try{
	
	java.io.File jcr = null;
	java.io.InputStream is = null;
	String reference = null, nameConnection = null, query= null, name = null, as = null;
	AnalysisBean analysis = null;
	SaveAnalysisBean analysisBean = new SaveAnalysisBean();
	// if into the request is defined the attribute "nameSubObject" the engine must run a subQuery
	String nameSubObject = request.getParameter("nameSubObject");
	if (nameSubObject != null) {
		String jcrPath = (String)session.getAttribute("templatePath");
		String spagoBIBaseUrl = (String)session.getAttribute("spagobiurl");
		String user = (String)session.getAttribute("user");
		// if subObject execution in the request there are the description and visibility
		String descrSO = request.getParameter("descriptionSubObject");
		if(descrSO==null)
			descrSO = "";
		String visSO = request.getParameter("visibilitySubObject");
		if(visSO==null)
			visSO = "Private";
		analysisBean.setAnalysisName(nameSubObject);
		analysisBean.setAnalysisDescription(descrSO);
		// the possible values of the visibility are (Private/Public)
		analysisBean.setAnalysisVisibility(visSO);
		// get content from cms
		SpagoBIAccessUtils sbiUtil = new SpagoBIAccessUtils();
		byte[] jcrContent = sbiUtil.getSubObjectContent(spagoBIBaseUrl, jcrPath, nameSubObject, user);
		is = new java.io.ByteArrayInputStream(jcrContent);
		InputStreamReader isr = new InputStreamReader(is);
		XStream dataBinder = new XStream();
		try {
			analysis = (AnalysisBean) dataBinder.fromXML(isr, new AnalysisBean());
			isr.close();
			query = analysis.getMdxQuery();
			nameConnection = analysis.getConnectionName();
			reference = analysis.getCatalogUri();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	// normal execution (no subObject)	
	} else {
	
		String jcrPath = request.getParameter("templatePath");
		String spagoBIBaseUrl = ( String )request.getParameter("spagobiurl");
		byte[] jcrContent = new it.eng.spagobi.utilities.SpagoBIAccessUtils().getContent(spagoBIBaseUrl,jcrPath);
		is = new java.io.ByteArrayInputStream(jcrContent);
		org.dom4j.io.SAXReader reader = new org.dom4j.io.SAXReader();
	    Document document = reader.read(is);
		Node connection = document.selectSingleNode("//olap/connection");
		if(connection != null) {
			nameConnection = connection.valueOf("@name");
		}
		query = document.selectSingleNode("//olap/MDXquery").getStringValue();
		Node cube = document.selectSingleNode("//olap/cube");
		reference = cube.valueOf("@reference");
		parameter = document.selectSingleNode("//olap/MDXquery/parameter");
		name = "";
		as = "";
		if (parameter != null){
			name = parameter.valueOf("@name");
			as = parameter.valueOf("@as");
		}
		analysis = new AnalysisBean();
		analysis.setConnectionName(nameConnection);
		analysis.setCatalogUri(reference);
		
		session.setAttribute("analysisBean",analysis);
	}
	
	// put in session the analysis file information bean
	session.setAttribute("save01", analysisBean);
	Object formObj = session.getAttribute("saveAnalysis01");
	if (formObj != null) {
		FormComponent form = (FormComponent) formObj;
		form.setBean(analysisBean);
	}	
	
	org.dom4j.io.SAXReader readerConFile = new org.dom4j.io.SAXReader();
	Document documentConFile = readerConFile.read(getClass().getResourceAsStream("/connections-config.xml"));
	Node connectionDef = null;
	if(nameConnection!=null) {
		connectionDef = documentConFile.selectSingleNode("//CONNECTIONS-CONFIGURATION/CONNECTION[@name='"+nameConnection+"'");
	} else {
		connectionDef = documentConFile.selectSingleNode("//CONNECTIONS-CONFIGURATION/CONNECTION[@isDefault='true'");
	}
	
	String jndi = connectionDef.valueOf("@isJNDI");
	
	if(jndi.equalsIgnoreCase("true")) { 
	    String iniCont = connectionDef.valueOf("@initialContext");
	    String resName = connectionDef.valueOf("@resourceName");
	    if(parameter != null){ %>
	    <jp:setParam query="query01" httpParam="<%=name %>" mdxParam="<%=as%>">
			<jp:mondrianQuery id="query01" dataSource="<%=resName%>"  catalogUri="<%=reference%>">
				<%=query%>
			</jp:mondrianQuery>
		</jp:setParam>
	    <% } else {%>
	    	<jp:mondrianQuery id="query01" dataSource="<%=resName%>"  catalogUri="<%=reference%>">
				<%=query%>
			</jp:mondrianQuery>
	<%    } 
	} else {
		String driver = connectionDef.valueOf("@driver");
		String url = connectionDef.valueOf("@jdbcUrl");
		String usr = connectionDef.valueOf("@user");
		String pwd = connectionDef.valueOf("@password");
	    if(parameter != null){ %>
	    <jp:setParam query="query01" httpParam="<%=name %>" mdxParam="<%=as%>">
			<jp:mondrianQuery id="query01" jdbcDriver="<%=driver%>" jdbcUrl="<%=url%>" jdbcUser="<%=usr%>" jdbcPassword="<%=pwd%>" catalogUri="<%=reference%>" >
				<%=query%>
			</jp:mondrianQuery>	
		</jp:setParam>
	   <% } else {%>
	    <jp:mondrianQuery id="query01" jdbcDriver="<%=driver%>" jdbcUrl="<%=url%>" jdbcUser="<%=usr%>" jdbcPassword="<%=pwd%>" catalogUri="<%=reference%>" >
				<%=query%>
		</jp:mondrianQuery>	
	   <% } 
	}
	
	if (nameSubObject != null) {
		session.setAttribute("analysisBean", analysis);
		%>
			<jsp:include page="customizeAnalysis.jsp"/>
		<%
	}
	
} catch (Exception e){
	e.printStackTrace();
}%>







