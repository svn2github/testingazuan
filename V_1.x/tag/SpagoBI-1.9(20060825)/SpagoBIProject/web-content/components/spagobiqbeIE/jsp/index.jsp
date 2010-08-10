<%@page import="it.eng.spago.configuration.ConfigSingleton"%>
<%@page import="java.util.Iterator"%>
<%@page import="it.eng.qbe.utility.LocalFileSystemDataMartModelRetriever"%>
<%@page import="java.util.List"%>
<%@ page import="it.eng.spago.base.*"%>

<link rel="stylesheet" href ="../css/spagobi.css" type="text/css"/>
<link rel="stylesheet" href ="../css/jsr168.css" type="text/css"/>
<link rel="stylesheet" href ="../css/external.css" type="text/css"/>
<link rel="styleSheet" href ="../css/dtree.css" type="text/css" />	

<% List allMarts = LocalFileSystemDataMartModelRetriever.getAllDataMartPath(); 
   Iterator it = allMarts.iterator();
   
   String dmPath = null;
   String completePath = null;
   
   String qbeDataMartDir = (String)ConfigSingleton.getInstance().getAttribute("QBE.QBE-MART_DIR.dir");
   
   String link = "#";
%>
<table>
<% 
   while(it.hasNext()){
	   dmPath = (String)it.next();
	   completePath = qbeDataMartDir  + System.getProperty("file.separator") + dmPath;
	   link = "../servlet/AdapterHTTP?ACTION_NAME=SELECT_DATA_MART_ACTION&PATH="+dmPath;
%>
	   <tr>
	   		<td><%= dmPath %></td>
	   		<td><%= completePath %></td>
	   		<td><a href="<%=link%>">Compose Query</a></td>
	   </tr>
<%		
   }
%>
</table>   
