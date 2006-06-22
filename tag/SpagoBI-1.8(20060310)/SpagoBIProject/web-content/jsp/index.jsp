<%@ page contentType="text/html; charset=ISO-8859-1"%>
<%@ page language="java" %>


<%@include file="/jsp/qbe_base.jsp"%>
<% if (qbeMode.equalsIgnoreCase("WEB")){ %> 
<body>
<%}%>
<% 
   String formUrl = qbeUrl.getUrl(request, null);	
%>
<form name="formSelectMart" action="<%= formUrl %>" method="post">
	<input type="hidden" name="ACTION_NAME" value="SELECT_DATA_MART_ACTION"/>
	<table align="center">
	<tr>
		<td colspan="3"> &nbsp; </td>
	</tr>
	<tr>
		<td colspan="3"> &nbsp; </td>
	</tr>
	<tr>
		<td colspan="3"> &nbsp; </td>
	</tr>
	<tr>
		<td colspan="3"> &nbsp; </td>
	</tr>
	<tr>
		<td align="center" colspan="3"><b> DataMart Connection Details </b></td>
	</tr>
	<tr>
		<td colspan="3"> &nbsp; </td>
	</tr>	
	<tr>
		<td> DATAMART PATH </td>
		<td colspan="2">  <input name="PATH" size="50" maxlength="50" value="C:/tmp/foodmart"/></td>
	</tr>
	<tr>
		<td> JNDI DATA SOURCE </td>
		<td colspan="2">  <input name="JNDI_DS" size="50" maxlength="50" value="java:comp/env/jdbc/sbifoodmart"/></td>
	</tr>
	<tr>
		<td>  DIALECT </td>
		<td colspan="2"> 
			<select name="DIALECT">
				<option value="org.hibernate.dialect.HSQLDialect" selected="selected">HSQLDialect </option>
				<option value="org.hibernate.dialect.PostgreSQLDialect">PostgreSQLDialect </option>
				<option value="org.hibernate.dialect.OracleDialect">OracleDialect </option>
				<option value="org.hibernate.dialect.Oracle9Dialect">Oracle9Dialect </option>
				<option value="org.hibernate.dialect.MySQLDialect">MySQLDialect </option>
				<option value="org.hibernate.dialect.DB2Dialect">DB2Dialect </option>
				<option value="org.hibernate.dialect.SQLServerDialect">SQLServerDialect </option>
				<option value="org.hibernate.dialect.SybaseDialect">SybaseDialect </option>
				<option value="org.hibernate.dialect.DerbyDialect">DerbyDialect </option>
				<option value="org.hibernate.dialect.FirebirdDialect">FirebirdDialect </option>
				<option value="org.hibernate.dialect.InformixDialect">InformixDialect </option>
				
			</select>
			
		</td>
	</tr>
	<tr>
		<td colspan="3"> &nbsp; </td>
	</tr>
	<tr>
		<td align="center" colspan="3"> <input type="submit" value="Connect To Data Mart"/></td>
	</tr>
	<tr>
		<td colspan="3"> &nbsp; </td>
	</tr>
	<tr>
		<td colspan="3"> &nbsp; </td>
	</tr>
	<tr>
		<td colspan="3"> &nbsp; </td>
	</tr>
	<tr>
		<td colspan="2"> &nbsp; </td>
	</tr>
	<tr>
		<td colspan="2"> &nbsp; </td>
	</tr>
	</table>
	
</form>
	

<% if (qbeMode.equalsIgnoreCase("WEB")){ %> 
</body>
<%}%>
<%@include file="../jsp/qbefooter.jsp" %>
