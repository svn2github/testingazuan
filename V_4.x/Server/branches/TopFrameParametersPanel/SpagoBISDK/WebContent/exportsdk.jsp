<%-- SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.  If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/. --%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<title>SpagoBI test call export</title>

</head>

<body>
<p>Below is SpagoBI iframe.</p>
<br/>
	<input type="button" value="Export PDF2" onclick="exportDocFromExternal('PDF');" > &nbsp;
	<input type="button" value="Export XLS2" onclick="exportDocFromExternal('XLS');" ><br/>
	
  <iframe id="frame1" name="my_frame" src="http://localhost:8080/SpagoBISDK/example3emb.jsp" width="1000" height="800"  sandbox="allow-same-origin allow-scripts allow-popups allow-forms" >
  </iframe>
  <!-- iframe id="frame1" name="my_frame" src="http://localhost:8080/SpagoBISDK/example1emb.jsp" width="1000" height="800"  sandbox="allow-same-origin allow-scripts allow-popups" >
  </iframe-->
<script type="text/javascript">

	function exportDocFromExternal(outputType){
		
		var iframe = document.getElementById("frame1");
		
		var iframeContWin = iframe.contentWindow;

		var exportUrl =iframeContWin.exportDoc(outputType);
		window.top.open(exportUrl);
	};
</script>

</body>
</html>