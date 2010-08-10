<%@ page contentType="text/html; charset=ISO-8859-1"%>
<%@ page language="java" %>





<%@ include file="/jsp/qbe_base.jsp" %>

<%

	String errMsg = null;
	if( query.isSubqueryModeActive() ) {
		errMsg = query.getSubqueryErrMsg( query.getSubqueryFieldId() );
		if( errMsg != null) {
%>	

<SCRIPT language=JavaScript>
	alert('ERRORE: <%= errMsg%>');
</SCRIPT>

<%
		} else {
			errMsg = "";
		}
	}
%>


<SCRIPT language=JavaScript>
	parent.saveSubQuery = true;
	parent.errMsg =  '<%= errMsg%>';
	parent.Windows.closeAll();	
</SCRIPT>