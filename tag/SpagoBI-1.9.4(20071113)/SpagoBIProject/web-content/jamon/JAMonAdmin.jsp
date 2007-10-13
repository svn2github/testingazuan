<%@ page language="java" buffer="8kb" autoFlush="true" isThreadSafe="true" isErrorPage="false"  %>
<%@ page import="com.jamonapi.*" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="JAMonStyles.css">
<title>JAMon Admin Page</title>
<script language="JavaScript">
<!--
    // Row highlighter
    var objClass

    function rollOnRow(obj, txt) {
        objClass = obj.className
        obj.className = "rowon";
        window.status = txt;
    }
    
    function rollOffRow(obj) {
        obj.className = objClass;
		window.status= "";
    }
// -->
</script>
</head>
<body>

<form action="JAMonAdmin.jsp" method="post">
<table border='0' cellpadding='0' cellspacing='0' width='75%'>
<tr>
<td style='text-align:left;'><table class='layoutmain' border='0' cellpadding='2' cellspacing='2' bgcolor='#669999'>
    <tr>
    <th><input name='Refresh' type='SUBMIT' value='Refresh'></th>
    <th><input name='Reset' type='SUBMIT' value='Reset'></th>
    <th><input name='Enable' type='SUBMIT' value='Enable'></th>
    <th><input name='Disable' type='SUBMIT' value='Disable'></th>
    </tr>
</table></td>
<td><table border='0' width='100%'>
    <tr>
    <th nowrap><a href="http://www.jamonapi.com"><img src="jamon_small.jpg" id="monLink" border="0" /></a></th>
    </tr>
</table></td>
</tr>
</table>

<% 
String reset=request.getParameter("Reset");
String enable=request.getParameter("Enable");
String disable=request.getParameter("Disable");
String refresh=request.getParameter("Refresh");

String sortOrder=request.getParameter("sortOrder");
if  (sortOrder==null) 
  sortOrder="asc";

String sortColStr=request.getParameter("sortCol");
int sortCol=1;
if (sortColStr!=null)
  sortCol=Integer.parseInt(sortColStr);

if ("Reset".equals(reset))
    MonitorFactory.reset();
else if ("Enable".equals(enable))   
    MonitorFactory.setEnabled(true);
else if ("Disable".equals(disable)) 
    MonitorFactory.setEnabled(false);
  
%>


<%= MonitorFactory.getRootMonitor().getReport(sortCol, sortOrder) %>


</form>


</body>
</html>

