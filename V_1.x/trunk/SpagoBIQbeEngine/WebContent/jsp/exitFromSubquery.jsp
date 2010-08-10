<%@ page contentType="text/html; charset=ISO-8859-1"%>
<%@ page language="java" %>
<%@ page import="java.util.*"%>
<%@ page import="it.eng.spago.base.*"%>
<%@ page import="it.eng.qbe.javascript.*"%>
<%@ page import="it.eng.qbe.wizard.*"%>
<%@ page import="it.eng.qbe.model.DataMartModel"%>


<%@ include file="../jsp/qbe_base.jsp" %>

<%
	ISingleDataMartWizardObject query = Utils.getMainWizardObject(sessionContainer);
	ISingleDataMartWizardObject subquery = Utils.getWizardObject(sessionContainer);
	String subQueryFieldId = (String)sessionContainer.getAttribute(WizardConstants.SUBQUERY_FIELD);
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