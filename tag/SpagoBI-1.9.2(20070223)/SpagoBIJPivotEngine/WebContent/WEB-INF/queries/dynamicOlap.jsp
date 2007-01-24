<%--
/**
 * 
 * LICENSE: see LICENSE.html file
 * 
 */
--%>
<%@ page session="true" 
         contentType="text/html; charset=UTF-8" 
		 import="org.dom4j.Document,
				 org.dom4j.Node,
				 java.io.InputStreamReader,
				 java.util.List,
				 com.thoughtworks.xstream.XStream,
				 it.eng.spagobi.bean.AnalysisBean,
				 it.eng.spagobi.util.SessionObjectRemoval,
				 it.eng.spagobi.utilities.SpagoBIAccessUtils,
				 it.eng.spagobi.util.ParameterSetter,
				 it.eng.spagobi.bean.SaveAnalysisBean,
				 com.tonbeller.wcf.form.FormComponent,
				 java.io.InputStream,
				 mondrian.olap.*" %>
<%@page import="sun.misc.BASE64Decoder"%>
<%@page import="java.util.Map"%>
<%@page import="it.eng.spagobi.utilities.ParametersDecoder"%>

<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%! 
private String substituteQueryParameters(String queryStr, List parameters, HttpServletRequest request) {
	
	
	String newQuery = queryStr;
	if (parameters != null && parameters.size() > 0) {
    	for (int i = 0; i < parameters.size(); i++) {
    		//update the query if there is more than one parameter (else only the last is correctly settled)
    		if(i>0){
    			queryStr = newQuery;
    		}
    		Node parameter = (Node) parameters.get(i);
    		String name = "";
    		String as = "";
    		if (parameter != null) {
    			name = parameter.valueOf("@name");
    			as = parameter.valueOf("@as");
    		}
    		String parameterValue = request.getParameter(name);
    		if((parameterValue==null) || parameterValue.trim().equals("") ){
    			continue;
    		}
    		
    		String decodedParameterValue = parameterValue;
    		ParametersDecoder decoder = new ParametersDecoder();
    		if(decoder.isMultiValues(parameterValue)) {
    			decodedParameterValue = (String)decoder.decode(parameterValue).get(0);
    		}
    		    		
			newQuery = ParameterSetter.setParameters(queryStr, as, parameterValue);				
    	}
    }		
    return newQuery;
}
%>



<%

	SessionObjectRemoval.removeSessionObjects(session);
	List parameters = null;
	InputStream is = null;
	String reference = null, nameConnection = null, query= null;
	AnalysisBean analysis = null;
	
	try{
		SaveAnalysisBean analysisBean = new SaveAnalysisBean();
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
		    Document document = reader.read(is);
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
	
	
	
	
		// put in session the analysis file information bean
		session.setAttribute("save01", analysisBean);
		Object formObj = session.getAttribute("saveAnalysis01");
		if (formObj != null) {
			FormComponent form = (FormComponent) formObj;
			form.setBean(analysisBean);
		}	
		org.dom4j.io.SAXReader readerConFile = new org.dom4j.io.SAXReader();
		Document documentConFile = readerConFile.read(getClass().getResourceAsStream("/engine-config.xml"));
		Node connectionDef = null;
		if(nameConnection!=null) {
			connectionDef = documentConFile.selectSingleNode("//ENGINE-CONFIGURATION/CONNECTIONS-CONFIGURATION/CONNECTION[@name='"+nameConnection+"'");
		} else {
			connectionDef = documentConFile.selectSingleNode("//ENGINE-CONFIGURATION/CONNECTIONS-CONFIGURATION/CONNECTION[@isDefault='true'");
		}
		String jndi = connectionDef.valueOf("@isJNDI");
		if(jndi.equalsIgnoreCase("true")) { 
		    String iniCont = connectionDef.valueOf("@initialContext");
		    String resName = connectionDef.valueOf("@resourceName");
		    String connectionStr = "Provider=mondrian;DataSource="+iniCont+"/"+resName+";Catalog="+reference+";";
		    query = substituteQueryParameters(query, parameters, request);
	    %>

		<jp:mondrianQuery id="query01" dataSource="<%=resName%>"  catalogUri="<%=reference%>">
			<%=query%>
		</jp:mondrianQuery>
		<%
		} else {
			String driver = connectionDef.valueOf("@driver");
			String url = connectionDef.valueOf("@jdbcUrl");
			String usr = connectionDef.valueOf("@user");
			String pwd = connectionDef.valueOf("@password");
		    String connectionStr = "Provider=mondrian;JdbcDrivers="+driver+";Jdbc="+url+";JdbcUser="+usr+";JdbcPassword="+pwd+";Catalog="+reference+";";
		    query = substituteQueryParameters(query, parameters, request);
			%>
		    <jp:mondrianQuery id="query01" jdbcDriver="<%=driver%>" jdbcUrl="<%=url%>" jdbcUser="<%=usr%>" jdbcPassword="<%=pwd%>" catalogUri="<%=reference%>" >
				<%=query%>
			</jp:mondrianQuery>	
			<%
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