/*
 * Ext JS Library 2.0.1
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

var asUrls = new Object();
var asLinkedDocs = new Object();
var asLinkedFields = new Object();

function setUrlIframe (pUrlIframe){
	urlIframe = pUrlIframe;
}

function setDocs(pUrls){
	asUrls = pUrls;
}

function setLinkedDocs(pLinkedDocs){
	asLinkedDocs = pLinkedDocs;
}

function setLinkedFields(pLinkedFields){
	asLinkedFields = pLinkedFields;
}

/* Update the input url with value for refresh linked documents and execute themes */
function execDrill(name, url){
//alert(name, url);
	var baseName = "iframe_";
	var labelDocClicked = name.substring(baseName.length);
	var retUrl = "";
	
	
	for(var docMaster in asUrls){
		var labelMasterDoc = docMaster.substring(docMaster.indexOf('|')+1);
		var sbiLabelMasterDoc = docMaster.substring(0, docMaster.indexOf('|'));
		var i=0;
		for (var docLabel in asLinkedDocs){ 
			retUrl = url;
			retUrl = retUrl.replace("SpagoBIDrillServlet","SpagoBIRefreshServlet");
			if (docLabel.indexOf(labelMasterDoc) >= 0){
				var generalLabelDoc = asLinkedDocs[docLabel];
				var labelDocLinked = generalLabelDoc[0].substring(generalLabelDoc[0].indexOf('|')+1);
				var sbiLabelDocLinked = generalLabelDoc[0].substring(0, generalLabelDoc[0].indexOf('|'));
				//gets iframe element
				var nameIframe = "iframe_" + labelDocLinked;
				var element = document.getElementById(nameIframe);
	
				//updating url with fields found in object
				var j=0;
				var sbiParMaster = "";
				for (var fieldLabel in asLinkedFields){ 
					var totalLabelPar =  asLinkedFields[fieldLabel];
					var labelPar 	= totalLabelPar[0].substring(totalLabelPar[0].indexOf('|')+1);
					var	sbiLabelPar = totalLabelPar[0].substring(0, totalLabelPar[0].indexOf('|'));
					var labelSubDoc = fieldLabel.substring(fieldLabel.indexOf('|')+1);;
					var sbiSubDoc 	= fieldLabel.substring(0, fieldLabel.indexOf('|'));
					if (fieldLabel.indexOf("SBI_LABEL_PAR_MASTER")>=0)
						sbiParMaster = asLinkedFields[fieldLabel];
					else if (labelSubDoc == labelDocLinked){
						var tmpDocLabel = retUrl.substring(retUrl.indexOf("DOCUMENT_LABEL=")+15);
						tmpDocLabel = (tmpDocLabel.indexOf("&")>=0)?tmpDocLabel = tmpDocLabel.substring(0,tmpDocLabel.indexOf("&")):tmpDocLabel ;
						retUrl = retUrl.replace(tmpDocLabel,sbiSubDoc);
						retUrl = retUrl.replace(sbiParMaster, sbiLabelPar);
						j= j+1;
					}
				} //for (var fieldLabel in asLinkedFields){ 
				sendUrl(nameIframe, retUrl);
				pause(2000);
			}//if (docLabel.indexOf(labelMasterDoc) >= 0){
			i= i+1;	
		}//for (var docLabel in asLinkedDocs){ 
	} 
	
	return;
}

function sendUrl(nameIframe, url){
	alert("SendURL - nameIframe: " + nameIframe +  " - url: "+ url);
	document.getElementById(nameIframe).src = url;
	return;	
}

function pause(interval)
{
    var now = new Date();
    var exitTime = now.getTime() + interval;

    while(true)
    {
        now = new Date();
        if(now.getTime() > exitTime) return;
    }
}

//create panels for each document
Ext.onReady(function() {
			
  			for (var docLabel in asUrls){ 			
  				var totalDocLabel=docLabel;	
  				var strDocLabel = totalDocLabel.substring(totalDocLabel.indexOf('|')+1);
  				var p = new Ext.Panel({
				id:'p'+i,
		        bodyBorder : true,
		        collapsible:true,
		        renderTo: 'divIframe_'+ strDocLabel
			    });
				
			    p.show(this);
			  //  alert(asUrls[docLabel] + " -  " + strDocLabel);
				p.load({
				   url:urlIframe,
				    params: {urlDoc:asUrls[docLabel], nameDoc: strDocLabel},
				    discardUrl: false,
				    nocache: false,
				    text: "Loading ...",
				    timeout: 30,
				    scripts: true
				});   
			    
  			}
});

