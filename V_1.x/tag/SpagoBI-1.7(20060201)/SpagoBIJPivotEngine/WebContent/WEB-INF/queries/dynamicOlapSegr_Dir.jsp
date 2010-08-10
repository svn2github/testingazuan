<%@ page session="true" contentType="text/html; charset=ISO-8859-1" 
			import="org.dom4j.Document,
					org.dom4j.DocumentHelper,
					org.dom4j.Node" %>
<%@ taglib uri="http://spagobi.eng.it" prefix="sbi" %>
<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>



<%System.out.println(" -- DYNAMICOLAP.JSP -- "); 
String XMLsource = "<olap>"+
				   "	<connection driver='org.postgresql.Driver' url='jdbc:postgresql://localhost:5432/bilancioBI' usr='postgres' pwd='postgres' />"+
  				   "	<cube reference='/WEB-INF/queries/BilancioBIMart.xml' />"+
				   "	<MDXquery>"+
				   "		WITH"+
  				   "        MEMBER [Measures].[Cap Imp]  AS '(([Measures].[Impegni] / [Measures].[Competenza]))', FORMAT_STRING = \"#%\""+
				   "SELECT"+
  " {"+
 " [Measures].[Competenza]"+
" ,[Measures].[Impegni]"+
" ,[Measures].[Cap Imp]"+
" ,[Measures].[Mand C/Comp]"+
" ,[Measures].[Res Iniziali]"+
" ,[Measures].[Mand C/Residui]"+
" ,[Measures].[Stock Perenti]"+
" ,[Measures].[Mand Perenti]"+  
" } ON COLUMNS,"+
"   {([Bilancio per Segr/Dir].[Tutti])} ON ROWS"+
" FROM [Bilancio_Segr_Dir]"+
" WHERE ([Anno].[2005])"+
				   "	</MDXquery>"+
				   "</olap>";
				   			
Node parameter = null;
try{
	Document document = DocumentHelper.parseText(XMLsource);
	System.out.println(" -- DYNAMICOLAP.JSP 2 -- ");
	Node connection = document.selectSingleNode("//olap/connection");
	String driver = connection.valueOf("@driver");
	String url = connection.valueOf("@url");
	String usr = connection.valueOf("@usr");
	String pwd = connection.valueOf("@pwd");
	
	System.out.println(" -- DYNAMICOLAP.JSP 3 -- ");
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

	

