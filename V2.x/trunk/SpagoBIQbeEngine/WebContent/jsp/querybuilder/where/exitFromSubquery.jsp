<%@ page contentType="text/html; charset=ISO-8859-1"%>
<%@ page language="java" %>
<%@ page import="java.util.*"%>
<%@ page import="it.eng.spago.base.*"%>
<%@ page import="it.eng.qbe.javascript.*"%>
<%@ page import="it.eng.qbe.wizard.*"%>
<%@ page import="it.eng.qbe.model.DataMartModel"%>
<%@ page import="it.eng.qbe.model.*"%>
<%@ page import="it.eng.qbe.query.*"%>

<%@ include file="/jsp/qbe_base.jsp" %>

<%
	ISingleDataMartWizardObject query = Utils.getMainWizardObject(sessionContainer);
	IQuery subquery = null;
	String qbeQueryMode = (String)sessionContainer.getAttribute("QUERY_MODE");
	boolean subqueryModeActive = (qbeQueryMode != null && qbeQueryMode.equalsIgnoreCase("SUBQUERY_MODE"));
	if ( subqueryModeActive ) {
		subquery =  query.getQuery().getSelectedSubquery();
	} else {
		subquery = query.getQuery();
	}

	String subQueryFieldId = (String)sessionContainer.getAttribute(WizardConstants.SUBQUERY_FIELD);
	boolean isValidSubquery = query.getQuery().isSelectedSubqueryValid();
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