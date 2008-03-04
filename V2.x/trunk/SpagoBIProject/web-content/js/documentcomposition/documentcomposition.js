/*
 * Ext JS Library 2.0.1
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

var asUrls = new Object();
var asLinkedDocs = new Object();

function setUrlIframe (pUrlIframe){
	urlIframe = pUrlIframe;
}

function setDocs(pUrls){
	asUrls = pUrls;
}

function setLinkedDocs(pLinkedDocs){
	asLinkedDocs = pLinkedDocs;
}

function execDrill(name, url){
	for (var docLabel in asLinkedDocs){ 
		alert("docLabel: " + docLabel + " docLinked: " + asLinkedDocs[docLabel] );
		var nameIframe = "iframe_" + asLinkedDocs[docLabel];
		alert(nameIframe);
		var element = document.getElementById(nameIframe);
		url = url.replace("SpagoBIDrillServlet","SpagoBIRefreshServlet");
		element.src = url;
	}
	return;
}
//create panels for each document
Ext.onReady(function() {

  			for (var docLabel in asUrls){ 				
  				var p = new Ext.Panel({
				id:'p'+i,
		        bodyBorder : true,
		        collapsible:true,
		        renderTo: 'divIframe_'+ docLabel
			    });
				
			    p.show(this);
				p.load({
				   url:urlIframe,
				    params: {urlDoc:asUrls[docLabel], nameDoc: docLabel},
				    discardUrl: false,
				    nocache: false,
				    text: "Loading ...",
				    timeout: 30,
				    scripts: true
				});   
			    
  			}
});

