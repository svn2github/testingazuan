<%-- SpagoBI, the Open Source Business Intelligence suite

Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. --%>
 
<%@ page language="java"
         contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" 
%>
<%@page session="false" %>


<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=8" />
</head>
<body>

<script type="text/javascript" src="./js/lib/ext-3.1.1/adapter/ext/ext-base.js"></script>
<%-- Ext lib for release --%>
<script type="text/javascript" src="./js/lib/ext-3.1.1/ext-all.js"></script>
<%-- Ext js overrides --%>
<script type="text/javascript" src="./js/lib/ext-3.1.1/overrides/overrides.js"></script>

<LINK rel='StyleSheet'  href='./js/lib/ext-3.1.1/resources/css/ext-all.css' type='text/css' />
<%-- Ext css overrides --%>
<LINK rel='StyleSheet' href='./js/lib/ext-3.1.1/overrides/resources/css/overrides.css' type='text/css' />
	  	  
<LINK rel='StyleSheet' href='./js/lib/ext-3.1.1/resources/css/xtheme-gray.css' type='text/css' />

<script type="text/javascript">
    Ext.BLANK_IMAGE_URL = './js/lib/ext-3.1.1/resources/images/default/s.gif';
    
	Ext.onReady(function(){
    	Ext.MessageBox.show({
       		title: 'Session expired!'
       		, msg: 'Session has expired, please log-in again'
       		, buttons: Ext.MessageBox.OK     
       		, icon: Ext.MessageBox.WARNING
       		, modal: true
   		});
	});
</script>

</body>
</html>