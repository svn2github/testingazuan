<%@ page session="true" contentType="text/html; charset=ISO-8859-1" 
			import="org.dom4j.Document,
					org.dom4j.DocumentHelper,
					org.dom4j.Node" %>
<%@ taglib uri="http://spagobi.eng.it" prefix="sbi" %>
<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>


<%System.out.println(" -- DYNAMICOLAP.JSP -- "); 
String XMLsource = "<olap>"+
				   "	<connection driver='org.postgresql.Driver' url='jdbc:postgresql://giobbe:5432/foodmart' usr='foodmart' pwd='smartfood' />"+
  				   "	<cube reference='/WEB-INF/queries/FoodMart.xml' />"+
				   "	<MDXquery>"+
				   "		select {[Measures].[Unit Sales], [Measures].[Store Cost], [Measures].[Store Sales]} on columns, {Parameter(\"ProductMember\", [Product], [Product].[All Products].[Food], \"wat willste?\").children} ON rows from Sales where ([Time].[1997])"+
				   "		<parameter name='param' as='ProductMember' />"+
				   "	</MDXquery>"+
				   "</olap>";
				   			
Node parameter = null;
try{
	Document document = DocumentHelper.parseText(XMLsource);

	Node connection = document.selectSingleNode("//olap/connection");
	String driver = connection.valueOf("@driver");
	String url = connection.valueOf("@url");
	String usr = connection.valueOf("@usr");
	String pwd = connection.valueOf("@pwd");
	
	Node cube = document.selectSingleNode("//olap/cube");
	String reference = cube.valueOf("@reference");
	
	String query = document.selectSingleNode("//olap/MDXquery").getStringValue();
	
	parameter = document.selectSingleNode("//olap/MDXquery/parameter");
	
	String name = "";
	String as = "";
	if (parameter != null){
		name = parameter.valueOf("@name");
		as = parameter.valueOf("@as");
	}
	if (parameter != null){
		System.out.println(" ------- Query con parametri ------ ");%>
		<jp:setParam query="query01" httpParam="<%=name %>" mdxParam="<%=as%>">
			<sbi:spagoBIMondrianQuery id="query01" jdbcDriver="<%=driver%>" jdbcUrl="<%=url%>" jdbcUser="<%=usr%>" jdbcPassword="<%=pwd%>" catalogUri="<%=reference%>" query="<%=query%>">
			</sbi:spagoBIMondrianQuery>
		</jp:setParam>
	<% } else {
			System.out.println(" ------- Query senza  parametri ------ ");%>
			<sbi:spagoBIMondrianQuery id="query01" jdbcDriver="<%=driver%>" jdbcUrl="<%=url%>" jdbcUser="<%=usr%>" jdbcPassword="<%=pwd%>" catalogUri="<%=reference%>" query="<%=query%>">
			</sbi:spagoBIMondrianQuery>
	<% } %>
<% } catch (Exception e){
	e.printStackTrace();
}%>



