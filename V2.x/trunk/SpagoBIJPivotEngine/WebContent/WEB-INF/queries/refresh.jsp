<%--

LICENSE: see LICENSE.txt file 

--%>
<%@ page session="true" contentType="text/html; charset=UTF-8" 
		 import="org.dom4j.Document,
				 org.dom4j.Node,
				 java.io.InputStreamReader,
				 java.util.List,
				 com.thoughtworks.xstream.XStream,
				 it.eng.spagobi.jpivotaddins.bean.*,
				 it.eng.spagobi.jpivotaddins.to.*,
				 it.eng.spagobi.jpivotaddins.util.*,
				 java.io.InputStream,
				 sun.misc.BASE64Decoder,
				 java.util.*,
				 org.apache.log4j.Logger,
				 com.tonbeller.jpivot.olap.model.OlapModel,
				 it.eng.spagobi.services.proxy.DataSourceServiceProxy,
				 it.eng.spagobi.services.datasource.bo.SpagoBiDataSource" %>
				

<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>


<%
Logger logger = Logger.getLogger(this.getClass());
try {
	AnalysisBean analysis = (AnalysisBean) session.getAttribute("analysisBean");;
	String query = analysis.getMdxQuery();	
	String reference = analysis.getCatalogUri();
	String documentId = (String)session.getAttribute("document");
	// BASED ON CONNECTION TYPE WRITE THE RIGHT MONDRIAN QUERY TAG
	//calls service for gets data source object
	DataSourceServiceProxy proxyDS = new DataSourceServiceProxy();
	SpagoBiDataSource ds = proxyDS.getDataSource(documentId);
	
	if(ds != null  && !ds.getJndiName().equals("")) {
		String resName = ds.getJndiName();
		resName = resName.replace("java:comp/env/","");
		%>
		<jp:mondrianQuery id="query01" dataSource="<%=resName%>"  catalogUri="<%=reference%>">
			<%=query%>
		</jp:mondrianQuery>
	<%	
	} else {		
		%>
		<jp:mondrianQuery id="query01" jdbcDriver="<%=ds.getDriver()%>" jdbcUrl="<%=ds.getUrl()%>" 
		                   jdbcUser="<%=ds.getUser()%>" jdbcPassword="<%=ds.getPassword()%>" catalogUri="<%=reference%>" >
			<%=query%>	
		</jp:mondrianQuery>	
		<%	
	}		
	%>
	<jsp:include page="customizeAnalysis.jsp"/>
	<%
	
	
	// CHECK IF THERE ARE DATA ACCESS FILTER AND IN CASE SET THE MONDRIAN ROLE
	OlapModel olapModel = (OlapModel) session.getAttribute("query01");
	String dimensionAccessRules = (String) session.getAttribute("dimension_access_rules");
	DataSecurityManager dsm = new DataSecurityManager(olapModel, dimensionAccessRules, query);
	dsm.setMondrianRole();
	
} catch (Exception e) {
	logger.error(this.getClass().getName() + ":error while refreshing query execution\n" + e);
}
%>