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
<%@page import="com.tonbeller.jpivot.mondrian.ScriptableMondrianDrillThrough"%>
<%@page import="com.tonbeller.jpivot.olap.model.OlapModel"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Set"%>
<%@page import="com.tonbeller.jpivot.mondrian.MondrianDimension"%>
<%@page import="com.tonbeller.jpivot.mondrian.MondrianHierarchy"%>

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
	
		OlapModel olapModel = (OlapModel) session.getAttribute("query01");
		ScriptableMondrianDrillThrough smdt = (ScriptableMondrianDrillThrough) olapModel.getExtension("drillThrough");
		
		Connection monConnection = smdt.getConnection();
		
	    // ***************** START CODE ADDED ********************
	    
	    Logger logger = Logger.getLogger(this.getClass());
	    // get the connection role, cube and schema reader
	    Role connRole = monConnection.getRole().makeMutableClone();
	    logger.debug("MondrianModel::initialize:connection role retrived: " + connRole);
	    Query monQuery = monConnection.parseQuery(query);
	    Cube cube = monQuery.getCube();
	    logger.debug("MondrianModel::initialize: cube retrived: " + cube);
	    SchemaReader schemaReader = cube.getSchemaReader(null);
	    logger.debug("MondrianModel::initialize: schema reader retrived: " + schemaReader);
	    // clean dimension access list
	    List dimAccList = (List) session.getAttribute("dimension_access_rules");
	    logger.debug("MondrianModel::initialize: start setting data access using dimension list: " + dimAccList);
	    logger.debug("MondrianModel::initialize: start cleaning dimension list");
	    Iterator iterDimAcc = dimAccList.iterator();
	    List tmpDimAccList = new ArrayList();
	    while(iterDimAcc.hasNext()) {
	    	String dimAccStr = (String)iterDimAcc.next();
	    	if((dimAccStr==null) || (dimAccStr.trim().equals(""))){
	    		continue;
	    	} else {
	    		tmpDimAccList.add(dimAccStr);
	    	}
	    }
	    dimAccList = tmpDimAccList;
	    logger.debug("MondrianModel::initialize: end cleaning dimension list: " + dimAccList);
	    
	    logger.debug("MondrianModel::initialize: start calculating access dimension names list");
	    // calculate an List containing all the dimension name to filter
	    List dimNames = new ArrayList();
	    iterDimAcc = dimAccList.iterator();
	    while(iterDimAcc.hasNext()) {
	    	String dimAccStr = (String)iterDimAcc.next();
	    	String dimName = null;
	    	if(dimAccStr.indexOf(".") == -1){
	    		continue;
	    	} else {
	    		dimName = dimAccStr.substring(0, dimAccStr.indexOf("."));
	    	}
	    	if((dimName!=null) && !dimName.trim().equals(""))
	    		if(!dimNames.contains(dimName))
	    			dimNames.add(dimName);
	    }
	    logger.debug("MondrianModel::initialize: end calculating access dimension names list: " + dimNames);
	    
	    logger.debug("MondrianModel::initialize: start calculating memeber list for each dimension");
	    // calculate a map with couple { dimName, List of members of the dimension }
	    Map memberMap = new HashMap();
	    Iterator iterDimNames = dimNames.iterator();
	    while(iterDimNames.hasNext()){
	    	String dimName = (String)iterDimNames.next();
	    	dimName = dimName.substring(1, dimName.length() - 1);
	    	System.out.println("*********** Nome dimensione da cercare: " + dimName);
	    	List dimMembers = new ArrayList();
	    	iterDimAcc = dimAccList.iterator();
		    while(iterDimAcc.hasNext()) {
		    	String dimAccStr = (String)iterDimAcc.next();
		    	if(dimAccStr.indexOf(".") == -1){
		    		continue;
		    	} else {
		    		String memberDim = dimAccStr.substring(0, dimAccStr.indexOf("."));
		    		String tmp = "[" + dimName + "]";
		    		if(tmp.equalsIgnoreCase(memberDim)){
		    			System.out.println("*********** Sono entrato nell'if");
		    			dimMembers.add(dimAccStr);
			    	}
		    	}
		    }
		    memberMap.put(dimName, dimMembers);
	    }
	    logger.debug("MondrianModel::initialize: end calculating memeber list for each dimension: " + memberMap);
	    
	    
	    logger.debug("MondrianModel::initialize: start setting grant for each dimension or hierachy");
	    // FOR EACH DIMENSION NAME SET THE RIGHT GRANT TO THE DIMENSION OR HIERARCHY
	    Set dimKeys = memberMap.keySet();
	    Iterator iterDimKeys = dimKeys.iterator();
	    while(iterDimKeys.hasNext()){
	    	String dimName = (String)iterDimKeys.next();
	    	logger.debug("MondrianModel::initialize: processing dimension named: " + dimName);
	    	List dimMembs = (List)memberMap.get(dimName);
	    	logger.debug("MondrianModel::initialize: members of dimension: " + dimMembs);
	    	//if(dimMembs.isEmpty()) {
	    		logger.debug("MondrianModel::initialize: try to search the dimension into the cube");
				Dimension[] dimensions = cube.getDimensions();
	    		//Set cubeDimKeys = hDimensions.keySet();
	    		// Iterator itCubeDimKeys = cubeDimKeys.iterator();
	 		    // while(itCubeDimKeys.hasNext()) {
	 		    for (int i = 0; i < dimensions.length; i++) {
	 		    	//String cubeDimKey = (String)itCubeDimKeys.next();
	 		    	Dimension dim = dimensions[i];
	 		    	String cubeDimKey = dim.getName();
	 		    	System.out.println("*********** Nome dimensione: " + cubeDimKey);
	 		    	if (cubeDimKey.equalsIgnoreCase(dimName)) {
	 		    		logger.debug("MondrianModel::initialize: dimension found into the cube");
	 		    		//MondrianDimension monDim = (MondrianDimension)hDimensions.get(cubeDimKey);
	 		    		//mondrian.olap.Dimension dim = monDim.getMonDimension();
	 		    		mondrian.olap.Hierarchy[] hierarchies = dim.getHierarchies();
	 		    		if (hierarchies == null || hierarchies.length == 0) {
		 		    		connRole.grant(dim, Access.NONE);
		 		    		logger.debug("MondrianModel::initialize: setted access.none to the dimension");
		 		    		break;
	 		    		} else {
			 		    	for (int j = 0; j < hierarchies.length; j++) {
			 		    		mondrian.olap.Hierarchy aHierarchy =  hierarchies[j];
			 		    		System.out.println("*********** Nome gerarchia: " + aHierarchy.getName());
			 		    		if (aHierarchy.getName().equalsIgnoreCase(dimName)) {
			 		    			 logger.debug("MondrianModel::initialize: hierarchy found into the cube");
			 		    			 connRole.grant(aHierarchy, Access.CUSTOM, null, null);
			 		    			 logger.debug("MondrianModel::initialize: setted access.custom to the hierarchy");
			 		    		 }
			 		    	}
	 		    		}
	 		    	}

	 		     }
	 		    logger.debug("MondrianModel::initialize: end search dimension into the cube");
	    	//} else {
	    		// logger.debug("MondrianModel::initialize: try to search the hierarchy into the cube");
	    		// mondrian.olap.Hierarchy hier = cube.getHierarchy();
	    		// if (hier!= null && hier.getName().equalsIgnoreCase(dimName)) {
	    		//	 System.out.println("*********** Nome gerarchia: " + hier.getName());
	    		//	 logger.debug("MondrianModel::initialize: hierarchy found into the cube");
	    		//	 connRole.grant(hier, Access.CUSTOM, null, null);
	    		//	 logger.debug("MondrianModel::initialize: setted access.custom to the hierarchy");
	    		// }
	    		 //Set hierKeys = hHierarchies.keySet();
	    		 //Iterator itHierKeys = hierKeys.iterator();
	 		     //while(itHierKeys.hasNext()) {
	 		   	 //String hierKey = (String)itHierKeys.next();
	 		     //	if(hierKey.equalsIgnoreCase(dimName)) {
	 		     //		logger.debug("MondrianModel::initialize: hierarchy found into the cube");
	 		     //		MondrianHierarchy monHier = (MondrianHierarchy)hHierarchies.get(hierKey);
	 		     //		mondrian.olap.Hierarchy hier = monHier.getMonHierarchy();
	 		     //		connRole.grant(hier, Access.CUSTOM, null, null);
	 		     //		logger.debug("MondrianModel::initialize: setted access.custom to the hierarchy");
	 		     //		break;
	 		     //	}
	    	//}
	    }
	    logger.debug("MondrianModel::initialize: end setting grant for each dimension or hierachy");
	    
	    logger.debug("MondrianModel::initialize: start setting grant for members of dimensions");
	    // FOR EACH MEMBER SET THE GRANT
	    dimKeys = memberMap.keySet();
	    iterDimKeys = dimKeys.iterator();
	    while(iterDimKeys.hasNext()){
	    	String dimName = (String)iterDimKeys.next();
	    	logger.debug("MondrianModel::initialize: *************** dinname = " + dimName);
	    	logger.debug("MondrianModel::initialize: start processing dimension named: " + dimName);
	    	List dimMembs = (List)memberMap.get(dimName);
	    	logger.debug("MondrianModel::initialize: using member list: " + dimMembs);
	    	Iterator iterDimMembs = dimMembs.iterator();
	        while(iterDimMembs.hasNext()) {
	        	String dimMemb = (String)iterDimMembs.next();
	        	logger.debug("MondrianModel::initialize: processing member : " + dimMemb);
	        	String[] membParts = Util.explode(dimMemb);
	    	    mondrian.olap.Member member = schemaReader.getMemberByUniqueName(membParts,true);
	    	    logger.debug("MondrianModel::initialize: mondrian member object retrived: " + member);
	    	    connRole.grant(member, Access.ALL);
	    	    logger.debug("MondrianModel::initialize: setted access.all to the member");
	        }
	    }
	    logger.debug("MondrianModel::initialize: end setting grant for members of dimensions");
	        
	    // SET THE ROLE INTO CONNECTION
	    connRole.makeImmutable();
	    monConnection.setRole(connRole); 
	    logger.debug("MondrianModel::initialize: setted role with grants into connection");
	    logger.debug("MondrianModel::initialize: end setting data access");
	    
	    // ***************** END CODE ADDED ********************
		
	} catch (Exception e){
		e.printStackTrace();
	}%>