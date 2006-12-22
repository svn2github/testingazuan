<%@ page session="true" 
         contentType="text/html; charset=ISO-8859-1" 
		 import="org.dom4j.Document,
				 org.dom4j.Node,
				 java.io.InputStreamReader,
				 java.io.InputStream,
				 java.io.ByteArrayInputStream,
				 java.util.List,
				 com.thoughtworks.xstream.XStream,
				 it.eng.spagobi.bean.AnalysisBean,
				 it.eng.spagobi.util.SessionObjectRemoval,
				 it.eng.spagobi.util.ParameterSetter,
				 it.eng.spagobi.utilities.SpagoBIAccessUtils,
				 it.eng.spagobi.bean.SaveAnalysisBean,
				 it.eng.spagobi.engines.jpivotxmla.conf.EngineXMLAConf,
				 com.tonbeller.wcf.form.FormComponent,
				 com.tonbeller.wcf.controller.RequestContext" %>

<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%! 
	private String substituteQueryParameters(String queryStr, List parameters, javax.servlet.http.HttpServletRequest request) {
	
		String newQuery = queryStr;
		if (parameters != null && parameters.size() > 0) {
	    	for (int i = 0; i < parameters.size(); i++) {
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
	    		if (parameterValue == null || parameterValue.trim().equals("")) continue;
				//System.out.println("name=" + name + "; alias=" + as + "; value=" + parameterValue);
				
				newQuery = ParameterSetter.setParameters(queryStr, as, parameterValue);
				//System.out.println("newQuery: " + newQuery);				
	    	}
	    }		
	    return newQuery;
	}
%>



<%

SessionObjectRemoval.removeSessionObjects(session);

List parameters = null;

try {

	InputStream is = null;
	String catalogName = null;
	String connectionUrl = null;
	String mdxQuery = null;
	
	AnalysisBean analysisBean = null;
	SaveAnalysisBean saveAnalysisBean = new SaveAnalysisBean();
	
	// if into the request is defined the attribute "nameSubObject" the engine must run a subQuery
	String nameSubObject = request.getParameter("nameSubObject");
	
	if (nameSubObject != null) {
		String jcrPath = (String)session.getAttribute("templatePath");
		String spagoBIBaseUrl = (String)session.getAttribute("spagobiurl");
		String user = (String)session.getAttribute("user");
		
		// if subObject execution in the request there are the description and visibility
		String descrSO = request.getParameter("descriptionSubObject");
		if(descrSO==null) descrSO = "";
		
		String visSO = request.getParameter("visibilitySubObject");
		if(visSO==null) visSO = "Private";
		
		saveAnalysisBean.setAnalysisName(nameSubObject);
		saveAnalysisBean.setAnalysisDescription(descrSO);
		saveAnalysisBean.setAnalysisVisibility(visSO);
		
		// get content from cms
		SpagoBIAccessUtils sbiUtil = new SpagoBIAccessUtils();
		byte[] jcrContent = sbiUtil.getSubObjectContent(spagoBIBaseUrl, jcrPath, nameSubObject, user);
		is = new java.io.ByteArrayInputStream(jcrContent);
		InputStreamReader isr = new InputStreamReader(is);
		XStream dataBinder = new XStream();
		try {
			analysisBean = (AnalysisBean) dataBinder.fromXML(isr, new AnalysisBean());
			isr.close();
			mdxQuery = analysisBean.getMdxQuery();
			catalogName = analysisBean.getCatalogName();
			connectionUrl = analysisBean.getCatalogUri();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	// normal execution (no subObject)	
	} else {
		String jcrPath = request.getParameter("templatePath");
		String spagoBIBaseUrl = ( String )request.getParameter("spagobiurl");
		String connectionName = (String)request.getParameter("connectionName");
		if(connectionName != null) 
			connectionUrl = EngineXMLAConf.getInstance().getConnectionUrl(connectionName);

		if(connectionUrl == null)
			connectionUrl = EngineXMLAConf.getInstance().getDefaultConnectionUrl();
			
		
		byte[] jcrContent = new it.eng.spagobi.utilities.SpagoBIAccessUtils().getContent(spagoBIBaseUrl,jcrPath);
		is = new ByteArrayInputStream(jcrContent);
		org.dom4j.io.SAXReader reader = new org.dom4j.io.SAXReader();
		Document document = reader.read(is);

		
		
		// get cube properties
		Node cube = document.selectSingleNode("//olap/cube");
		catalogName = cube.valueOf("@name");
			
		// get query properties
		mdxQuery = document.selectSingleNode("//olap/MDXquery").getStringValue();
		
		// get query parameters
		parameters = document.selectNodes("//olap/MDXquery/parameter");		
		
		analysisBean = new AnalysisBean();
		analysisBean.setCatalogName(catalogName);
		analysisBean.setCatalogUri(connectionUrl);
		
		session.setAttribute("analysisBean", analysisBean);
	}
	
	// put in session the analysis file information bean
	session.setAttribute("save01", saveAnalysisBean);
	Object formObj = session.getAttribute("saveAnalysis01");
	if (formObj != null) {
		FormComponent form = (FormComponent) formObj;
		form.setBean(saveAnalysisBean);
	}	
	
	mdxQuery = substituteQueryParameters(mdxQuery, parameters, request);
	
	/*
	XMLAQueryTag query = new XMLAQueryTag();
	
	query.setCatalog(catalogName);
	query.setMdxQuery(mdxQuery);
	query.setUri(catalogUri);
	query.setId("query01");
	query.setUser("");
	query.setPassword("");
		
	query.init(RequestContext.instance());	
	*/
%>	
	 
	<jp:xmlaQuery id="query01"
	    uri="<%=connectionUrl%>" 
	    catalog="<%=catalogName%>" >
	<%=mdxQuery%>
	</jp:xmlaQuery>
	
<%	
	if (nameSubObject != null) {
		session.setAttribute("analysisBean", analysisBean);
		%>
			<jsp:include page="customizeAnalysis.jsp"/>
		<%
	}
	
} catch (Exception e){
	e.printStackTrace();
}%>