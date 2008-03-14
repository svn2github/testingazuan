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
var asStylePanels = new Object();

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


function setStylePanels(pStylePanels){
	asStylePanels = pStylePanels;
}


/* Update the input url with value for refresh linked documents and execute themes */
function execDrill(name, urlDrill){
	var baseName = "iframe_";
	var labelDocClicked = name.substring(baseName.length);
	var tmpUrl = "";
	urlDrill = urlDrill.substring(urlDrill.indexOf("?")+1);
	
	for(var docMaster in asUrls){
		var labelMasterDoc = docMaster.substring(docMaster.indexOf('|')+1);
		var sbiLabelMasterDoc = docMaster.substring(0, docMaster.indexOf('|'));
		var i=0;
		for (var docLabel in asLinkedDocs){ 
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
				var tmpOldSbiSubDoc = "";
				var newUrl = "";
				var tmpUrl = "";
				var finalUrl = "";
				for (var fieldLabel in asLinkedFields){ 
					var totalLabelPar =  asLinkedFields[fieldLabel];
					var labelPar 	= totalLabelPar[0].substring(totalLabelPar[0].indexOf('|')+1);
					var	sbiLabelPar = totalLabelPar[0].substring(0, totalLabelPar[0].indexOf('|'));
					var labelSubDoc = fieldLabel.substring(fieldLabel.indexOf('|')+1);
					labelSubDoc = labelSubDoc.substring(0, labelSubDoc.indexOf("__"));
					var sbiSubDoc 	= fieldLabel.substring(0, fieldLabel.indexOf('|'));

					if (labelSubDoc == labelDocLinked){
						if (tmpOldSbiSubDoc != sbiSubDoc){
							newUrl = asUrls[sbiSubDoc+"|"+labelSubDoc]; //final url
						 	tmpUrl = newUrl[0].substring(newUrl[0].indexOf("?")+1);
						 	finalUrl = newUrl[0];
							tmpOldSbiSubDoc = sbiSubDoc;
						}
						
						var paramsNewValues = urlDrill.split("&");
						var tmpNewValue = "";
						var tmpOldValue = "";	
						if (paramsNewValues != null && paramsNewValues.length > 0) {
							for (j = 0; j < paramsNewValues.length; j++) {
								var idPar = fieldLabel.substring(fieldLabel.indexOf("__")+2);
								sbiParMaster = asLinkedFields["SBI_LABEL_PAR_MASTER__" + idPar.substring(idPar.length-1)];
								tmpNewValue = paramsNewValues[j];
								if (tmpNewValue.substring(0, tmpNewValue.indexOf("=")) == sbiParMaster){
									tmpNewValue = tmpNewValue.substring(tmpNewValue.indexOf("=")+1);
						
									var paramsOldValues = tmpUrl.split("&amp;");
									if (paramsOldValues != null && paramsOldValues.length > 0) {
										for (k = 0; k < paramsOldValues.length; k++) {
											//gets old value of parameter:
											if (paramsOldValues[k].substring(0, paramsOldValues[k].indexOf("=")) == sbiLabelPar){
												tmpOldValue = paramsOldValues[k] ;
												tmpOldValue = tmpOldValue.substring(tmpOldValue.indexOf("=")+1);
												if (tmpOldValue != "" && tmpNewValue != ""){
													finalUrl = finalUrl.replace(sbiLabelPar+"="+tmpOldValue, sbiLabelPar+"="+tmpNewValue);
													RE = new RegExp("&amp;", "ig");
													newUrl = finalUrl.replace(RE, "&");
													tmpOldValue = "";
													tmpNewValue = "";
													break;
												}
											}
										}
									}
								}
							}						
						}
	
					}
				} //for (var fieldLabel in asLinkedFields){ 	
				sendUrl(nameIframe, newUrl);
			}//if (docLabel.indexOf(labelMasterDoc) >= 0){
			i= i+1;	
		}//for (var docLabel in asLinkedDocs){ 
	} 
	
	return;
}

function sendUrl(nameIframe, url){
	//alert("SendURL - nameIframe: " + nameIframe +  " - url: "+ url);
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
  				//gets style (width and height)
  				var style = asStylePanels[strDocLabel];
				var widthPx = "";
				var heightPx = "";
				if (style != null){
					widthPx = style[0].substring(0, style[0].indexOf("|"));
					heightPx = style[0].substring(style[0].indexOf("|")+1);
					widthPx = widthPx.substring(widthPx.indexOf("WIDTH_")+6);
		       		heightPx = heightPx.substring(heightPx.indexOf("HEIGHT_")+7);
				}
				//create panel
  				var p = new Ext.Panel({
				id:'p'+i,
		        bodyBorder : true,
		        collapsible:true,
		       // width: widthPx,
		        height:Number(heightPx),
		        renderTo: 'divIframe_'+ strDocLabel
			    });
				
			    p.show(this);
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

