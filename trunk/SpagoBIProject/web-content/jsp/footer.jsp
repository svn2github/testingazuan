<%@ page language="java"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         import="it.eng.spago.base.*,
         		 it.eng.spago.configuration.ConfigSingleton"
%>


<%
	// get configuration
	ConfigSingleton spagoconfigfooter = ConfigSingleton.getInstance();
	// get mode of execution
	String sbiModefooter = (String)spagoconfig.getAttribute("SPAGOBI.SPAGOBI-MODE.mode");   
%>
 
 
   
<!-- based on ecexution mode include initial html  -->   
<% if (sbiModefooter.equalsIgnoreCase("WEB")){ %> 
</body>
</html>
<%} %>

