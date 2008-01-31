<%@ page contentType="text/html; charset=ISO-8859-1"%>
<%@ page language="java" %>





<%@ include file="/jsp/qbe_base.jsp" %>

<%
	IQuery subquery = null;
	String qbeQueryMode = (String)sessionContainer.getAttribute("QUERY_MODE");
	boolean subqueryModeActive = (qbeQueryMode != null && qbeQueryMode.equalsIgnoreCase("SUBQUERY_MODE"));
	if ( subqueryModeActive ) {
		subquery =  query.getSelectedSubquery();
	} else {
		subquery = query;
	}

	String subQueryFieldId = (String)sessionContainer.getAttribute(QbeConstants.SUBQUERY_FIELD);
	boolean isValidSubquery = query.isSelectedSubqueryValid();
%>

<%
	String errMsg = "";
	if(!isValidSubquery) {
		errMsg = subquery.getSubqueryErrMsg();
%>
	
<SCRIPT language=JavaScript>
	alert('ERRORE: <%= errMsg%>');
</SCRIPT>
<%
	}
%>


<SCRIPT language=JavaScript>
	parent.saveSubQuery = true;
	parent.errMsg =  '<%= errMsg%>';
	parent.Windows.closeAll();	
</SCRIPT>