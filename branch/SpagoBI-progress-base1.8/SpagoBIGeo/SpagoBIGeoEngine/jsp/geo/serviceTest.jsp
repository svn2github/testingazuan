<%@ page	extends="it.eng.spago.dispatching.httpchannel.AbstractHttpJspPage"
			contentType="text/html; charset=ISO-8859-1"
			pageEncoding="ISO-8859-1"
			session="true"
			errorPage="../spago/serviceError.jsp"
%>
<html>
<head>
	<title>Service Test</title>
</head>
<body bgcolor="#CCCCCC" text="#330066">
<script type="text/javascript">
<!-- Script Begin
function actionRequest(request){
  	var doc = this.document.service_test;
	if(request=='GEO_TEST'){
  		doc.action='../../servlet/AdapterHTTP?ACTION_NAME=GEO_TEST&MESSAGE=CALL_SERVICE&NEW_SESSION=TRUE';
  	}
  	doc.submit();
}
//  Script End -->
</script>

<table>
<form name="service_test" method="post">
  <tbody>

    <tr>
        <td colspan="2">
            <fieldset>
            <legend>
                <h1><b>Service Test</b></h1>
            </legend>
            <table>
                <tbody>
                
				      <tr>
				          <td width="25%"></td>
				          <td width="75%"><input type="button" value="Test Geo" onClick="javascript:actionRequest('GEO_TEST');"></td>
				      </tr>
               </tbody>
            </table>
            </fieldset>
        </td>
   </tr>
  </tbody>
 </form> 
</table>
</body>
</html>