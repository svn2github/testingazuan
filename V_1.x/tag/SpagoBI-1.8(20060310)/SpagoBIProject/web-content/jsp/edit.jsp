<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="javax.portlet.PortletURL" %>


<table width='100%' cellspacing='0' border='0'>		
	<tr height='40'>
		<th align='left'>&nbsp;&nbsp;<spagobi:message key="editConf.configuration"/></th>
	</tr>
</table>

<%
    PortletURL formUrl = renderResponse.createActionURL();
    formUrl.setParameter("PAGE", "SaveConfigurationPage");  
    boolean it = false;
    boolean en = false;
	String[] langPref = renderRequest.getPreferences().getValues("language", null);	
    if(langPref[0].equals("it")) {
    	it = true;
    } else {
    	en = true;
    }
%>



<!-- ************************************************************* -->
<!-- ESEMPIO DI UTILIZZO DELLA CONFIGURAZIONE 
	 CONFIGURAZIONE DELLA LINGUA DEPRECATA
	 LA PAGINA DI CONFIGURAZIONE E IL MODULO ESISTONO ANCORA PER EVENTUALI UTILIZZI FUTURI -->
	 
<%--
<form action="<%= formUrl.toString() %>" method="POST" > 
<table width="100%" cellspacing="0" border="0" >
  	<tr height='1'>
  		<td width="30px"><span>&nbsp;</span></td>
  		<td width="70px"><span>&nbsp;</span></td>
  		<td width="30px"><span>&nbsp;</span></td>
  		<td><span>&nbsp;</span></td>
  	</tr>
  	<tr height='40'>
      	<td>&nbsp;</td>
      	<td class='portlet-form-field-label' ><spagobi:message key="editConf.language"/>:</td>
      	<td>&nbsp;</td>
      	<td>	
      		<input type="radio" name="language" value="it,IT" <% if(it) {out.write(" checked='checked' ");} %> >
      			<spagobi:message key="editConf.italian"/>
      		</input>
      	</td>
    </tr>
    <tr height='40'>
      	<td>&nbsp</td>
      	<td>&nbsp;</td>
      	<td>&nbsp;</td>
      	<td>	
      		<input type="radio" name="language" value="en,US" <% if(en) {out.write(" checked='checked' ");} %> >
      			<spagobi:message key="editConf.english"/>
      		</input>
      	</td>
    </tr>
    <tr height='10'>
    	<td colspan="4">&nbsp;</td>
    </tr>
    <tr height='40'>
    	<td>&nbsp;</td>
    	<td colspan="3">
    		<input type="submit" value="<spagobi:message key="editConf.save"/>"/>
    	</td>
    </tr>
</table>
</form>
--%>
<!-- ************************************************************* -->

<br/><br/>

<div width="100%" align="center">
	<span class='portlet-form-field-label' ><spagobi:message key="editConf.noConfiguration"/></span>
</div>

<br/><br/>








