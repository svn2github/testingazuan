 <%@ include file="/jsp/qbe_base.jsp" %>

<qbe:init-page-context/>

<qbe:begin-body/>

<c:if test="${isStandaloneModality}">
	<qbe:titlebar datamartName = "${datamartModel.name}"
			  	  datamartDescription = "${datamartModel.name}"
			  	  pageName = "${pageName}"/>
</c:if> 


<%@include file="/jsp/testata.jsp" %>


<div class='div_background_no_img'>

<% if (qbeMode.equalsIgnoreCase("WEB")){ %> 
</body>
<%}%>

<%@include file="/jsp/qbefooter.jsp" %>