<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ include file="/WEB-INF/jsp/commons/portlet_base.jsp"%>
<%

boolean backUrlB=false;
String backUrl="";
if(session.getAttribute(SpagoBIConstants.BACK_URL)!=null){
	backUrl=(String)session.getAttribute(SpagoBIConstants.BACK_URL);
	backUrlB=true;
}

session.invalidate();

//Check if SSO is active
ConfigSingleton serverConfig = ConfigSingleton.getInstance();
SourceBean validateSB = (SourceBean) serverConfig.getAttribute("SPAGOBI_SSO.ACTIVE");
String active = (String) validateSB.getCharacters();
if (active == null || active.equalsIgnoreCase("false") && backUrlB==false) {
	String context = request.getContextPath();
	response.sendRedirect(context);
}
else if (active != null && active.equalsIgnoreCase("true")) {

	SourceBean logoutSB = (SourceBean) serverConfig.getAttribute("SPAGOBI_SSO.SECURITY_LOGOUT_URL");
	String urlLogout = (String) logoutSB.getCharacters();
	
%>

<iframe id='invalidSessionJasper'
                 name='invalidSessionJasper'
                 src='<%=urlLogout %>'
                 height='0'
                 width='0'
                 frameborder='0' >
</iframe> 
<%} %>
<iframe id='invalidSessionJasper'
                 name='invalidSessionJasper'
                 src='<%=GeneralUtilities.getSpagoBiHost()+GeneralUtilities.getSpagoBiContext()%>/SpagoBIJasperReportEngine/invalidateSession.jsp'
                 height='0'
                 width='0'
                 frameborder='0' >
</iframe>  

<iframe id='invalidSessionJasper'
                 name='invalidSessionJasper'
                 src='<%=GeneralUtilities.getSpagoBiHost()+GeneralUtilities.getSpagoBiContext()%>/SpagoBIJPivotEngine/invalidateSession.jsp'
                 height='0'
                 width='0'
                 frameborder='0' >
</iframe>  

<iframe id='invalidSessionJasper'
                 name='invalidSessionJasper'
                 src='<%=GeneralUtilities.getSpagoBiHost()+GeneralUtilities.getSpagoBiContext()%>/SpagoBIQbeEngine/invalidateSession.jsp'
                 height='0'
                 width='0'
                 frameborder='0' >
</iframe>  

<iframe id='invalidSessionJasper'
                 name='invalidSessionJasper'
                 src='<%=GeneralUtilities.getSpagoBiHost()+GeneralUtilities.getSpagoBiContext()%>/SpagoBIGeoEngine/invalidateSession.jsp'
                 height='0'
                 width='0'
                 frameborder='0' >
</iframe>  

<%if (active != null && active.equalsIgnoreCase("true")) { %>
	<script>window.close();</script>
<% } %>

<% //if there is a BACK URL specified redirect the page

if(backUrlB==true){
	response.sendRedirect(backUrl); 
}

%>