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
				 com.tonbeller.jpivot.olap.model.OlapModel" %>

<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>


<%
	Logger logger = Logger.getLogger(this.getClass());
	List parameters = null;
	InputStream is = null;
	String reference = null, nameConnection = null, query= null;
	AnalysisBean analysis = null;
	Document document = null;
	try {
		SaveAnalysisBean analysisBean = (SaveAnalysisBean) session.getAttribute("save01");
		String nameSubObject = request.getParameter("nameSubObject");
	
		// if into the request is defined the attribute "nameSubObject" the engine must run a subQuery
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
	String subobjdata64Coded = request.getParameter("subobjectdata");
	BASE64Decoder bASE64Decoder = new BASE64Decoder();
	byte[] subobjBytes = bASE64Decoder.decodeBuffer(subobjdata64Coded);
	is = new java.io.ByteArrayInputStream(subobjBytes);
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
	String templateBase64Coded = request.getParameter("template");
	BASE64Decoder bASE64Decoder = new BASE64Decoder();
	byte[] template = bASE64Decoder.decodeBuffer(templateBase64Coded);
	is = new java.io.ByteArrayInputStream(template);
	org.dom4j.io.SAXReader reader = new org.dom4j.io.SAXReader();
		    document = reader.read(is);
		    nameConnection = request.getParameter("connectionName");
	query = document.selectSingleNode("//olap/MDXquery").getStringValue();
	Node cube = document.selectSingleNode("//olap/cube");
	reference = cube.valueOf("@reference");
	parameters = document.selectNodes("//olap/MDXquery/parameter");
	analysis = new AnalysisBean();
	analysis.setConnectionName(nameConnection);
	analysis.setCatalogUri(reference);
	session.setAttribute("analysisBean",analysis);
		}


		
		
		// SUBSTITUTE QUERY PARAMETERS
		query = ParameterUtilities.substituteQueryParameters(query, parameters, request);
		
		
		
		// BASED ON CONNECTION TYPE WRITE THE RIGHT MONDRIAN QUERY TAG
		DbConnection dbConnection = null;
		if(nameConnection!=null) {
	dbConnection = new DbConnection(nameConnection);	
		} else {
	dbConnection = new DbConnection();		
		}
		if(dbConnection.isJndi()) {
	JndiConnection jndiCon = dbConnection.getJndiConnection();
%>
			<jp:mondrianQuery id="query01" dataSource="<%=jndiCon.getResName()%>"  catalogUri="<%=reference%>">
				<%=query%>
				
				<%
				if (document != null) {
				List clickables = document.selectNodes("//olap/MDXquery/clickable");
				if (clickables != null && clickables.size() > 0) {
					for (int i = 0; i < clickables.size(); i++) {
						Node clickable = (Node) clickables.get(i);
						String urlPattern = clickable.valueOf("@urlPattern");
						List clickParameters = document.selectNodes("//olap/MDXquery/clickable/clickParameter");
						if (clickParameters != null && clickParameters.size() > 0) {
							urlPattern += "?";
							for (int j = 0; j < clickParameters.size(); j++) {
								Node clickParameter = (Node) clickParameters.get(j);
								String clickParameterName = clickParameter.valueOf("@name");
								String clickParameterValue = clickParameter.valueOf("@value");
								urlPattern += clickParameterName + "=" + clickParameterValue + "&";
							}
						}
						String uniqueName = clickable.valueOf("@uniqueName");
						%>
						<jp:clickable urlPattern="<%=urlPattern%>" uniqueName="<%=uniqueName%>"/>
						<%
					}
				}
				}
				%>
				
			</jp:mondrianQuery>
		<%	
		} else {
			JdbcConnection jdbcCon = dbConnection.getJdbcConnection();
		%>
			 <jp:mondrianQuery id="query01" jdbcDriver="<%=jdbcCon.getDriver()%>" jdbcUrl="<%=jdbcCon.getUrl()%>" 
			                   jdbcUser="<%=jdbcCon.getUsr()%>" jdbcPassword="<%=jdbcCon.getPwd()%>" catalogUri="<%=reference%>" >
				<%=query%>
								
				<%
				if (document != null) {
				List clickables = document.selectNodes("//olap/MDXquery/clickable");
				if (clickables != null && clickables.size() > 0) {
					for (int i = 0; i < clickables.size(); i++) {
						Node clickable = (Node) clickables.get(i);
						String urlPattern = clickable.valueOf("@urlPattern");
						List clickParameters = document.selectNodes("//olap/MDXquery/clickable/clickParameter");
						if (clickParameters != null && clickParameters.size() > 0) {
							urlPattern += "?";
							for (int j = 0; j < clickParameters.size(); j++) {
								Node clickParameter = (Node) clickParameters.get(j);
								String clickParameterName = clickParameter.valueOf("@name");
								String clickParameterValue = clickParameter.valueOf("@value");
								urlPattern += clickParameterName + "=" + clickParameterValue + "&";
							}
						}
						String uniqueName = clickable.valueOf("@uniqueName");
						%>
						<jp:clickable urlPattern="<%=urlPattern%>" uniqueName="<%=uniqueName%>"/>
						<%
					}
				}
				}
				%>
				
			</jp:mondrianQuery>	
		<%	
		}		
		
		
		
		// IN CASE OF SUBOBJECT EXECUTION PUT INTO SESSION THE ANALYSYS BEAN AND INCLUDE THE ANALYSIS JSP	
		if (nameSubObject != null) {
			session.setAttribute("analysisBean", analysis);
			%>
				<jsp:include page="customizeAnalysis.jsp"/>
			<%
		}
	
		
		// CHECK IF THERE ARE DATA ACCESS FILTER AND IN CASE SET THE MONDRIAN ROLE
		OlapModel olapModel = (OlapModel) session.getAttribute("query01");
		String dimensionAccessRules = (String)session.getAttribute("dimension_access_rules");
		logger.debug(this.getClass().getName() + ":dimension access rules: " + dimensionAccessRules);
		DataSecurityManager dsm = new DataSecurityManager(olapModel, dimensionAccessRules, query);
		dsm.setMondrianRole();
		//FilteringUtilities.setMondrianRole(olapModel, dimensionAccessRules, query);
		
		
		
	} catch (Exception e){
		logger.error(this.getClass().getName() + ":error while executing query \n" + e);
		throw e;
	}%>
